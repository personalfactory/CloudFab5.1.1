/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraSvuotamentoMiscelatore;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraSvuotamentoMiscelatorePulizia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_CONFIG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_INFO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PULIZIA_LIMITE_POS53;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_RITARDO_ATTIVAZIONE_INVERTER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_STABILIAZZIONE_PESO_FINE_PESA_CONFEZIONI; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI;

import java.io.IOException;
import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 *         Thread di Controllo di Raggiungimento Peso Sacchetto Desiderato
 *
 *
 */
public class ThreadPuliziaControlloPesoConfezioni extends Thread {

	private final Pulizia pulizia;
	private int iCount;
	private ClientPesaTLB4 pesaConfezioni;
	private boolean inizializzazioneVelPesaturaFine;
	private boolean interrompiAggiornamentoPesoReale;
	private int counterLog;

	// COSTRUTTORE
	public ThreadPuliziaControlloPesoConfezioni(Pulizia pulizia) {

		// Dichiarazione Varibale Processo
		this.pulizia = pulizia;

		// Impostazione Nome Thread Corrente
		this.setName(this.getClass().getSimpleName());

	}

	@Override
	public void run() {

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Inizio Procedura Controllo Peso Confezioni");

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Inizializzazione variabili di controllo ");

		//////////////////////////////////////////////
		// INIZIALIZZAZIONE VARIBILI DI CONTROLLO ///
		//////////////////////////////////////////////
		// Inzializzazione Variabili di Controllo Procedura
		pulizia.pesoConfezioneRaggiunto = false;
		iCount = 0;
		pulizia.pesoMancanteConfezioni = 0;
		pulizia.pesoSacco = "0";
		pulizia.valorePesaConfezioni = "0";
		interrompiAggiornamentoPesoReale = false;
		counterLog = 0;

		///////////////////////////////////////////////////////////
		/// CREAZIONE CLIENT DI PESA E COMMUTAZIONE PESO NETTO ///
		///////////////////////////////////////////////////////////
		try {

			// Memorizzazione Log Processo
			PuliziaLogger.logger.config("Creazione Client di Pesa");

			// Creazione del Client Pesa
			pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
			
//			pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI, ParametriSingolaMacchina.parametri.get(150),
//					Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

			ThreadPuliziaControlloPesoConfezioni
			.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);


			// Memorizzazione Log Processo
			PuliziaLogger.logger.config("Commutazione Peso Netto");

			// Commutazione Peso Netto Bilancia Confezionamento
			pesaConfezioni.commutaNetto();


			ThreadPuliziaControlloPesoConfezioni
			.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);


			// Memorizzazione Log Processo
			PuliziaLogger.logger.info("Creazione Client di Pesa e Commutazione Peso Netto Eseguita");

		} catch (IOException ex) {

			// Memorizzazione Log Processo
			PuliziaLogger.logger.log(Level.SEVERE, "Errore durante la creazione del Client Pesa e={0}", ex);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Inizio Thread Controllo Peso Confezioni");

		// begin loop
		while (!pulizia.pesoConfezioneRaggiunto && pulizia.pannelloPulizia.isVisible()
				&& !pulizia.puliziaInterrottaManualmente) {

			controlloPesoConfezioni();

			// Memorizzazione Log Processo
			PuliziaLogger.logger.finer("Thread Controllo Peso Confezioni in Esecuzione");

		} // end loop

		// Memorizzazione Log Processo
		PuliziaLogger.logger.finer("Thread Controllo Peso Confezioni Completato");

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Fine Thread Controllo Peso Confezioni");

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Avvio Thread Aggiornamento Peso Reale Confezioni");

		new ThreadAggiornamentoPesoRealeConfezione().start();

		ChiusuraValovlaScaricoFineConfezionamento();

		//////////////////////////////////
		// ARRESTO MOTORE MISCELATORE ///
		//////////////////////////////////
		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Arresto Motore Miscelatore");

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Arresto Inverter Motore Miscelatore");

		// Arresto Motore Miscelatore
		inverter_mix.avviaArrestaInverter(false);

		// Memorizzazione Log Processo
		PuliziaLogger.logger.info("Arresto Inverter Motore Miscelatore Eseguito");

		try {
			ThreadPuliziaControlloPesoConfezioni.sleep(TEMPO_STABILIAZZIONE_PESO_FINE_PESA_CONFEZIONI);
		} catch (InterruptedException ex) {
		}

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Disattivazione Uscita Motore Miscelatore");

		try {
			ThreadPuliziaControlloPesoConfezioni.sleep(TEMPO_RITARDO_ATTIVAZIONE_INVERTER);
		} catch (InterruptedException ex) {
		}

		GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_FALSE_CHAR);

		/////////////////////////////////////////////
		/// PRESENZA VALVOLE DEDICATE ASPIRATORE ///
		/////////////////////////////////////////////
		PuliziaLogger.logger.info("Riposizionamento Deviatori Aspirazione");

		GestoreIO_ModificaOut(
				USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
				OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR);

		// Memorizzazione Log Processo
		PuliziaLogger.logger.info("Disattivazione Uscita Motore Miscelatore Eseguita");

		///////////////////////////////
		// ATTESA SVUOTAMENTO TUBO ///
		///////////////////////////////
		// Aspetta Tempo di Chiusura
		int tempoSvuotamento = Integer.parseInt(ParametriSingolaMacchina.parametri.get(124));
		int numeroRipetizioniSvuotamento = tempoSvuotamento / FRAZIONAMENTO_TEMPI;
		boolean interrompiTimerSvuotamento = false;
		int counterSvuotamento = 0;

		// Memorizzazione Log Processo
		PuliziaLogger.logger.log(Level.INFO, "Attesa Tempo Svuotamento ={0}", tempoSvuotamento);

		// Inizio loop
		while (!interrompiTimerSvuotamento) {

			counterSvuotamento++;

			if (counterSvuotamento >= numeroRipetizioniSvuotamento) {
				interrompiTimerSvuotamento = true;
			}
			try {
				ThreadPuliziaControlloPesoConfezioni.sleep(FRAZIONAMENTO_TEMPI);
			} catch (InterruptedException ex) {
			}

		} // fine loop

		/////////////////////
		// ARRESTO VIBRO ///
		/////////////////////
		// Memorizzazione Log Processo
		PuliziaLogger.logger.info("Arresto Vibro");

		GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO,
				// + OUTPUT_SEP_CHAR+ USCITA_LOGICA_MOTORE_VIBRO_BASE,
				OUTPUT_FALSE_CHAR // + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
		);

		////////////////////////////
		// ARRESTO LETTURA PESO ///
		////////////////////////////
		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Interruzione Thread Aggiornamento Peso Reale Confezioni");

		// Aggiornamento Peso Reale
		interrompiAggiornamentoPesoReale = true;

		/////////////////////////////
		// REGISTRAZIONE PULIZIA ///
		/////////////////////////////
		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Registrazione Procedura Pulizia");

		if (!pulizia.puliziaInterrottaManualmente) {

			// Registrazione Processo di Pulizia
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(214))) {

				if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

					RegistraSvuotamentoMiscelatore(pulizia,
							((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).getVelMiscelatore(),
							((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).getIdCiclo(),
							((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).getIdOrdine(),
							((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).getDtInizio(),
							((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).getIdProdotto()); // IdProdotto

				} else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

					RegistraSvuotamentoMiscelatorePulizia(pulizia,
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.dettagliPulizia
									.getVelMix(),
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.dettagliPulizia
									.getIdCiclo(),
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.dettagliPulizia
									.getIdOrdine(),
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).getDtInizio(),
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.dettagliPulizia
									.getIdProdotto(),
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.dettagliPulizia
									.getIdPresa(),
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.dettagliPulizia
									.getQComponente(), // pTeoricoComponente
							((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.ultimoPesoBilanciaCarico,
							Integer.parseInt(
									((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).pulizia.ultimoPesoBilanciaSacchetti)); // pesoRConf

				}

				// Memorizzazione Log Processo
				PuliziaLogger.logger.info("Registrazione Procedura Pulizia - Eseguita");
			}
		}

		/////////////////////
		// SBLOCCO SACCO ///
		/////////////////////
		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Comando Sblocco Confezione");

		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO, OUTPUT_FALSE_CHAR);

		// Memorizzazione Log Processo
		PuliziaLogger.logger.info("Comando Sblocco Confezione Eseguito");

		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(225))) {

			//////////////////////////////////////////
			// FINALIZZA PROCEDURA SENZA RIBALTO ///
			//////////////////////////////////////////
			// Memorizzazione Log Processo
			PuliziaLogger.logger.config("Finalizzazione Procedura Pulizia senza Ribalto");

			// Finalizzazione Procedura
			pulizia.finalizzaPulizia();

		} else {

			// Memorizzazione Log Processo
			PuliziaLogger.logger.config("Finalizzazione Procedura Pulizia con Ribalto");

			new ThreadPuliziaRibaltaSacco(pulizia).start();

		}

		// Memorizzazione Log Processo
		PuliziaLogger.logger.config("Fine Procedura Controllo Peso Confezioni");
	}

	public void controlloPesoConfezioni() {

		// Lettura Peso Confezioni
		if (letturaPesoConfezioni()) {

			// Controllo Stato della Pesatura
			if (pulizia.pesoMancanteConfezioni <= 0 | GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP)) {

				//////////////////////////////////
				// PESO DESIDERATO RAGGIUNTO ////
				//////////////////////////////////
				// Memorizzazione Log Processo
				PuliziaLogger.logger.info("Peso Desiderato Raggiunto o Pesatura Forzata Manualmente");

				// Interruzione Thread Corrente
				pulizia.pesoConfezioneRaggiunto = true; 
				
				//Chiusura Valvola
				 if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

	                	///////////////////////////
	                	// ATTUATORE MULTISTADIO //
	                	/////////////////////////// 
	                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
	                	PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
	                } else {

	                	////////////////////
	                	// COMANDO UNICO  //
	                	//////////////////// 
	                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
	                	PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");

	                }

				// Memorizzazione Log Processo
				PuliziaLogger.logger.info("Chiusura Valvola di Scarico");

			} else {

				///////////////////////////////////////////
				// PESO DESIDERATO NON ANCORA RAGGIUNTO ///
				///////////////////////////////////////////
				if (pulizia.pesoMancanteConfezioni <= Integer.parseInt(ParametriSingolaMacchina.parametri.get(126))
						&& !inizializzazioneVelPesaturaFine) {

					/////////////////////////////////////////////////
					// PESATURA FINE  POS_20 - VALVOLA APERTA 20% ///
					/////////////////////////////////////////////////
 
					if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

						///////////////////////////
						// ATTUATORE MULTISTADIO //
						///////////////////////////

						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_20); 
						PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_20");
					}
					
					inizializzazioneVelPesaturaFine = true;
					// Memorizzazione Log Processo
					PuliziaLogger.logger.config("Impostazione Vel Pesatura Fine");

					// Vel Pesatura Fine
					String velPesaturaFine = VerificaLunghezzaStringa(
							Integer.parseInt(ParametriSingolaMacchina.parametri.get(235)), 2);

					inverter_mix.cambiaVelInverter(velPesaturaFine);

					// Memorizzazione Log Processo
					PuliziaLogger.logger.log(Level.INFO, "Impostazione Vel Pesatura Fine Eseguita - Valore= {0}",
							velPesaturaFine);

					// Avvio Thread Chiusura e Apertura Valvola
					new ThreadPuliziaApriChiudiValvola(pulizia).start();

				} else if (pulizia.pesoMancanteConfezioni <= PULIZIA_LIMITE_POS53 && !pulizia.pos_53_impostata) {

					//////////////////////////////////
					// POS_53 - VALVOLA APERTA 50% ///
					//////////////////////////////////
					
					pulizia.pos_53_impostata=true;
					
					if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

						///////////////////////////
						// ATTUATORE MULTISTADIO //
						///////////////////////////
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_53); 
						PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_53");
 
						// Memorizzazione Log Processo
						PuliziaLogger.logger.log(Level.INFO, "Impostazione POS_53 - pesoMancante = {0}",
								pulizia.pesoMancanteConfezioni);

					}
					

				} 

			}
		}
	}

	// Lettura e Validazione Peso Confezioni
	public boolean letturaPesoConfezioni() {

		boolean lettura_effettuata = false;
		// Lettura del Peso Netto
		String socketRep = pesaConfezioni.pesoNetto();

		try {

			Thread.sleep(20);
			
			if (!socketRep.equals("")) {
				int valoreDaValidare = Integer.parseInt(socketRep);

				if (!socketRep.equals("0")) {

					if (Math.abs(valoreDaValidare - Integer.parseInt(pulizia.valorePesaConfezioni)) <= Integer
							.parseInt(ParametriSingolaMacchina.parametri.get(173))) {

						pulizia.valorePesaConfezioni = Integer.toString(valoreDaValidare);
						iCount = 0;
					} else {

						if (iCount > Integer.parseInt(ParametriSingolaMacchina.parametri.get(172))) {
							pulizia.valorePesaConfezioni = Integer.toString(valoreDaValidare);

							// Memorizzazione Log Processo
							PuliziaLogger.logger.log(Level.WARNING, "Valore di Peso Riagganciato dopo {0} tentativi",
									iCount);

							iCount = 0;
						} else {

							if (valoreDaValidare > 0) {
								iCount++;
							} else {
								iCount = 0;
							}
						}

					}
				}
			}

			if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

				// Impostazione Visibiltà Messaggio Utente
				((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia)
						.aggiornaLabelPesaScarico(pulizia.valorePesaConfezioni);

			} else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

				// Impostazione Visibiltà Messaggio Utente
				((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia)
						.aggiornaLabelPesaScarico(pulizia.valorePesaConfezioni);

			}

			pulizia.pesoSacco = pulizia.valorePesaConfezioni;

			// Calcolo Materiale Mancante
			pulizia.pesoMancanteConfezioni = pulizia.pesoDaRaggiungereConfezione
					- Double.parseDouble(pulizia.valorePesaConfezioni);

			lettura_effettuata = true;

			memorizzaPesoLog(socketRep);

		} catch (NumberFormatException e) {

			// Memorizzazione Log Processo
			PuliziaLogger.logger.log(Level.WARNING, "ATTENZIONE: Errore durante la Validazione del Peso Letto e={0}",
					e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lettura_effettuata;

	}

	// Gestore Memorizzazione Peso Letto su File di Log
	private void memorizzaPesoLog(String socketRep) {

		// Memorizzazione sul File Log
		if (counterLog <= Math.max(LOG_PESATURA_INTERVALLO_REP_CONFIG, LOG_PESATURA_INTERVALLO_REP_INFO)) {

			counterLog++;

			// Memorizzazione Log Processo
			PuliziaLogger.logger.log(Level.FINE,
					"Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}",
					new Object[] { socketRep, LOG_CHAR_SEPARATOR, pulizia.valorePesaConfezioni, LOG_CHAR_SEPARATOR,
							pulizia.pesoMancanteConfezioni });

			if (counterLog % LOG_PESATURA_INTERVALLO_REP_CONFIG == 0) {

				// Memorizzazione Log Processo
				PuliziaLogger.logger.log(Level.CONFIG,
						"Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}",
						new Object[] { socketRep, LOG_CHAR_SEPARATOR, pulizia.valorePesaConfezioni, LOG_CHAR_SEPARATOR,
								pulizia.pesoMancanteConfezioni });

			}

			if (counterLog % LOG_PESATURA_INTERVALLO_REP_INFO == 0) {

				// Memorizzazione Log Processo
				PuliziaLogger.logger.log(Level.INFO,
						"Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}",
						new Object[] { socketRep, LOG_CHAR_SEPARATOR, pulizia.valorePesaConfezioni, LOG_CHAR_SEPARATOR,
								pulizia.pesoMancanteConfezioni });

			}

		} else {

			// Inizializzazione Variabile
			counterLog = 0;

		}

	}

	// Aggiornamento Peso Reale Sacco
	private class ThreadAggiornamentoPesoRealeConfezione extends Thread {

		@Override
		public void run() {

			// Inizio loop
			while (!interrompiAggiornamentoPesoReale) {

				letturaPesoConfezioni();

			} // Fine Loop

			// Chiusura del Client di Pesa
			pesaConfezioni.chiudi();
			
			//pesaConfezioni.closeConnection();

		}
	}

	public void ChiusuraValovlaScaricoFineConfezionamento() {

		PuliziaLogger.logger.config("Svuotamento Buffer IO - Fine Confezionamento");

		//Chiusura Valvola
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
			///////////////////////////
			// ATTUATORE MULTISTADIO //
			///////////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
			PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
		} else {

			////////////////////
			// COMANDO UNICO  //
			////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
			PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
		}


		PuliziaLogger.logger.info("Svuotamento Buffer IO Fine Confezionamento Eseguito");

	}
}
