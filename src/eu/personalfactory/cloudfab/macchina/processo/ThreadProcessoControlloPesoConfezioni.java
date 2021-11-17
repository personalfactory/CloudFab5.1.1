/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.Arrotonda;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistrazioneProcesso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQ_CONTR_CHIUS_VALV_FINE_PESATURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_APPROSSIMAZIONE_CAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_APPROSSIMAZIONE_NUM_CAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_CONFIG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_INFO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIP_CONTR_CHIUS_VALV_FINE_PESATURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_COMANDO_CHIUS_VALV_FINE_PESATURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SCUOTITORI_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
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
public class ThreadProcessoControlloPesoConfezioni extends Thread {

	private final Processo processo;
	private int iScaricoVel;
	// private int qTara;
	private int iCount;
	private ClientPesaTLB4 pesaConfezioni;
	private double pesoPercMancante;
	private double pesoVelNormale;
	private boolean controlloReimpostazioneVelFine;
	private boolean inizializzazionePesaturaFine;
	private int counterLog;
	private boolean esecuzioneCambioVel;
	private boolean raggiungimentoCondArrestoMotore;
	private boolean ariaValvolaAttiva;
	private boolean scuotitore_attivo;
	private boolean scuotitore_disattivato; 
	private int contatoreSimulazione; 

	// COSTRUTTORE
	public ThreadProcessoControlloPesoConfezioni(Processo processo) {

		// Dichiarazione Varibale Processo
		this.processo = processo;

		// Impostazione Nome Thread Corrente
		this.setName(this.getClass().getSimpleName());

	}

	@Override
	public void run() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizio Procedura Controllo Peso Confezioni");

		// Inizializzazione Variabili
		inizializzaVariabili();

		try {

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				// Creazione Client Pesa e Commutazione Peso Netto
				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Creazione Client Pesa Confezioni");

				// Creazione del Client
				pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
				 
				Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

			}

			if (!processo.abilitaLineaDirettaMiscelatore) {

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					// Inizializzazione velocità miscelatore
					processo.inizializzazioneVelMiscelatore();
				}

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					// Avvio Motore Miscelatore
					inverter_mix.avviaArrestaInverter(true);
				}
			}

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				// Aggiornamento Visualizzazione Pannello
				processo.pannelloProcesso.modificaPannello(17);

				// Inizializzazione Valore Pesa Confezioni
				inizializzazioneValorePesaConfezioni();

				raggiungimentoCondArrestoMotore = false;

				new ThreadProcessoControlloBloccoScarico(processo).start();

			}

			ariaValvolaAttiva = false;

			////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ARIA
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////// VALVOLA
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

				ProcessoLogger.logger.info("AttivazioneUcita Aria Valvola");
				// Attivazione Aria Condotto Scarico

				new ThreadProcessoGestoreSvuotaValvola(processo).start();

				ariaValvolaAttiva = true;

				ProcessoLogger.logger.info("Attivazione Aria Fludificatori");

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI, OUTPUT_TRUE_CHAR);

			}

			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// LOOP
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DI
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// PESATURA
			while (!processo.pesoConfezioneRaggiunto && processo.pannelloProcesso.isVisible()
					&& !processo.resetProcesso) {

				// Pesatura Confezioni
				controlloPesoConfezioni();

				Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

			}

			pesaConfezioni.chiudi();
			 
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(503))) {

				GestoreIO_ModificaOut(USCITA_LOGICA_SCUOTITORI_BILANCIA_SACCHETTI, OUTPUT_FALSE_CHAR);
			 
			}

			// Modifica Feb2016
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

				ProcessoLogger.logger.info("Disattivazione Aria Fluidificatori");

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI, OUTPUT_FALSE_CHAR);
				 

			}

		} catch (IOException ex) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante la Creazione del Client Pesa Confezioni e={0}", ex);

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
				RegistraAllarme(8,
						EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 905,
								ParametriSingolaMacchina.parametri.get(111))) + " =" + ex,
						TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
			}
		} catch (InterruptedException e) {
		} catch (NumberFormatException e) { 
		}

		// Interruzione Thread Processo Attiva Fluidificatori PesaturaFine
		processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine = true;

		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))
				&& (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321)))) {
			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Disattivazione Aria Valvola e Pulisci Valvola");

			// Interruzione Thread Gestione Fulidificatori
			processo.interrompiThreadGestoreSvuotaValvola = true;

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA, OUTPUT_FALSE_CHAR);
			}
			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Disattivazione Aria Valvola e Pulisci Valvola - Eseguita");

		} else {

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Disattivazione Aria Valvola");

				processo.interrompiThreadGestoreSvuotaValvola = true;

				ProcessoLogger.logger.info("Disattivazione Aria Pulisci Valvola - Eseguita");
			}

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Disattivazione Aria Pulisci Valvola");

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA, OUTPUT_FALSE_CHAR);

				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Disattivazione Aria Pulisci Valvola - Eseguita");

			}

		}

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			////////////////////////////////////////
			// PROCEDURA NORMALE PESO RAGGIUNTO ///
			////////////////////////////////////////
			new ThreadAggiornamentoPesoRealeConfezione().start();

			if (!processo.threadApriChiudiValvolaAvviato) {

				new ThreadProcessoFinalizzaProceduraApriChiudiValvola(processo).start();
			}

		} else {

			////////////////////////////////////////////////
			// CHIUSURA PANNELLO O TASTO RESET DIGITATO ///
			////////////////////////////////////////////////
			ProcessoLogger.logger.warning("Digitato pulsante reset durante la Pesatura Confezioni");

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
				RegistraAllarme(2,
						EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 906,
								ParametriSingolaMacchina.parametri.get(111))),
						TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
			}
  
		}
 

	}

	// Gestisce il Cambio Velocita Motore Miscelatore durante il Confezionamento
	private class ThreadGestoreCambioVelocita extends Thread {

		private final double peso;

		public ThreadGestoreCambioVelocita(double peso) {
			this.peso = peso;
		}

		@Override
		public void run() {

			if (!processo.disabilitaCambioVelMiscelatoreBloccoScarico) {

				for (int i = 0; i < processo.aliqScarico.size(); i++) {

					if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

						if (peso < processo.aliqScarico.get(i)) {

							if (iScaricoVel != i) {

								esecuzioneCambioVel = true;

								iScaricoVel = i;

								if (!processo.abilitaLineaDirettaMiscelatore) {

									inverter_mix.cambiaVelInverter(VerificaLunghezzaStringa(
											processo.velocitaMiscelatoreFaseScarico[processo.indiceConfezioneInPesa][iScaricoVel],
											4));

									// Memorizzazione Log Processo
									ProcessoLogger.logger.log(Level.CONFIG,
											"Comando Cambio Vel {0}Peso Percentuale Mancante = {1}{2}Nuova Vel = {3}",
											new Object[] {
													LOG_CHAR_SEPARATOR, Arrotonda(Double.toString(peso),
															LOG_APPROSSIMAZIONE_NUM_CAR, LOG_APPROSSIMAZIONE_CAR),
													LOG_CHAR_SEPARATOR,
													VerificaLunghezzaStringa(
															processo.velocitaMiscelatoreFaseScarico[processo.indiceConfezioneInPesa][iScaricoVel],
															4) });
								}

								break;
							}

							break;
						}

					}
				}

				esecuzioneCambioVel = false;

			}
		}
	}

	// Aggiornamento Peso Reale Sacco
	private class ThreadAggiornamentoPesoRealeConfezione extends Thread {

		@Override
		public void run() {


			// Creazione del Client
			try {
				
				pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
				
//				pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI, ParametriSingolaMacchina.parametri.get(150),
//						Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));
				
				
				// Inizio loop
				while (!processo.interrompiAggiornamentoPesoRealeConfezione && !processo.resetProcesso) {

					letturaPesoConfezioni();

					Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

				}

				// Chiusura del Client di Pesa
				pesaConfezioni.chiudi();

			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			////////////////////////////////////////////////////
			// ATTIVAZIONE ARIA PULISCI TUBO SCARICO SCARICO ///
			////////////////////////////////////////////////////

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))
					&& !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO, OUTPUT_TRUE_CHAR);

				// Attesa Tempo Svuotamento Condotto
				int tempoSvuotamentoCondotto = Integer.parseInt(ParametriSingolaMacchina.parametri.get(320));
				int numeroRipetizioniSvuotamentoCondotto = tempoSvuotamentoCondotto / FRAZIONAMENTO_TEMPI;
				boolean interrompiTimerSvuotamentoCondotto = false;
				int counterSvuotamentoCondotto = 0;

				// Inizio loop
				while (!interrompiTimerSvuotamentoCondotto && processo.pannelloProcesso.isVisible()
						&& !processo.resetProcesso) {

					counterSvuotamentoCondotto++;

					if (counterSvuotamentoCondotto >= numeroRipetizioniSvuotamentoCondotto) {

						interrompiTimerSvuotamentoCondotto = true;

					} else {

						try {
							ThreadProcessoFinalizzaProceduraApriChiudiValvola.sleep(FRAZIONAMENTO_TEMPI);
						} catch (InterruptedException ex) {
						}
					}

				}
				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

					GestoreIO_ModificaOut(
							USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO + OUTPUT_SEP_CHAR
									+ USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO,
							OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR);
				}

			}

			///////////////////////////////////////////////////
			// FINALIZZAZIONE PROCEDURA DI CONFEZIONAMENTO ///
			///////////////////////////////////////////////////

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Arresto Vibro");

			if (processo.pannelloProcesso.isVisible()) {

				GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO, OUTPUT_FALSE_CHAR);

				if (!processo.svuotamento_valvova_da_eseguire) {

					// Aggiornamento Pesa Confezione Eseguita
					AggiornaValoreParametriRipristino(processo,
							TrovaIndiceTabellaRipristinoPerIdParRipristino(51) + processo.indiceConfezioneInPesa, 51,
							"true", ParametriSingolaMacchina.parametri.get(15));
				}
			}

			/////////////////////////////////////////////
			// CONTROLLO CHIUSURA VALVOLA DI SCARICO ///
			/////////////////////////////////////////////
			int c = 0;
			int t = 0;

			// Inizio loop
			while (c < RIP_CONTR_CHIUS_VALV_FINE_PESATURA && processo.pannelloProcesso.isVisible()
					&& !processo.resetProcesso
					&& GestoreIO_OttieniStatoIngresso(
							INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO) != ParametriSingolaMacchina.parametri.get(20)
									.equals("1")) {

				if (GestoreIO_OttieniStatoIngresso(
						INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO) != ParametriSingolaMacchina.parametri.get(20)
								.equals("1")) {

					try {
						ThreadAggiornamentoPesoRealeConfezione.sleep(FREQ_CONTR_CHIUS_VALV_FINE_PESATURA);

						if (t > TEMPO_COMANDO_CHIUS_VALV_FINE_PESATURA / FREQ_CONTR_CHIUS_VALV_FINE_PESATURA) {
							t = 0;
							c++;

							// Chiusura Valvola
							if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

								///////////////////////////
								// ATTUATORE MULTISTADIO //
								///////////////////////////

								GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
								ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
							} else {

								///////////////////
								// COMANDO UNICO //
								///////////////////
								GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
								ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");

							}

						}
					} catch (InterruptedException ex) {
					}
				}
				t++;
			} // fine loop

			if (GestoreIO_OttieniStatoIngresso(
					INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO) == ParametriSingolaMacchina.parametri.get(20)
							.equals("1")) {

				//////////////////////////////////////////////
				// CHIUSURA VALVOLA DI SCARICO - ESEGUITA ///
				//////////////////////////////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Chiusura Valvola di Scarico Eseguita");

			} else {

				////////////////////////////////////////////
				// CHIUSURA VALVOLA DI SCARICO - FALLITA ///
				/////////////////////////////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.severe("Errore durante la chiusura della Valvola di Scarico");
			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Aggiornamento Parametri");

			// Interruzione Thread Controllo Peso
			processo.interruzioneControlloPesoTastoReset = false;

			if (!processo.svuotamento_valvova_da_eseguire) {

				///////////////////////////////////////////////
				// SVUOTA VALVOLA PRIMA MISCELA DISABILITATO //
				///////////////////////////////////////////////

				// Aggiornamento Parametri - Pesatura Sacco Eseguita
				processo.indiceConfezioneInPesa++;

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					// Aggiornamento Label Confezioni Pesate
					if (processo.indiceConfezioneInPesa < Integer.parseInt(TrovaSingoloValoreParametroRipristino(9))) {
						processo.pannelloProcesso.aggiornaLabelConfezioniPesate(processo.indiceConfezioneInPesa);
					}

					// Incremento Miscele Completate
					AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(63), 63,
							Integer.toString(Integer.parseInt(TrovaSingoloValoreParametroRipristino(63)) + 1),
							ParametriSingolaMacchina.parametri.get(15));

				}

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Registrazione Processo");

				// Reimpostazione Variabile di Controllo
				processo.pannelloProcesso.ripristinato = false;

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					TimeLineLogger.logger.log(Level.INFO, "Fine Pesautra Confezione {1} - id_ciclo ={0}",
							new Object[] { TrovaSingoloValoreParametroRipristino(83), processo.indiceCompInPesa });

					// Registrazione Processo
					RegistrazioneProcesso(processo); // Codice Sblocco Chimica

				}

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					// Aggiorna Gestore Processo
					processo.aggiornaGestoreProcesso();

				}
			} else {

				////////////////////////////////////////////
				// SVUOTA VALVOLA PRIMA MISCELA ABILITATO //
				////////////////////////////////////////////

				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Procedura svuota valvola prima miscela - Fine pesatura");

				// Reimpostazione Variabile di Controllo
				processo.pannelloProcesso.ripristinato = false;
				
				// Aggiorna Gestore Processo
				new ThreadProcessoControlloInserimentoMaterialeSvuotaValvola(processo).start();

			}

			processo.svuotamento_valvova_da_eseguire = false;
		}
	}

	//////////////
	// METODI ///
	//////////////

	// Inizializzazione Variabili di Controllo
	private void inizializzaVariabili() {

		//////////////////////////////////
		// INIZIALIZZAZIONE VARIABILI ///
		//////////////////////////////////
		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizializzazione Varibili");

		// Inzializzazione Variabili di Controllo Procedura
		processo.pesoConfezioneRaggiunto = false;
		iScaricoVel = 0;
		iCount = 0;
		processo.pesoMancanteConfezioni = 0;
		pesoVelNormale = 0;
		controlloReimpostazioneVelFine = false;
		inizializzazionePesaturaFine = false;
		processo.interrompiAggiornamentoPesoRealeConfezione = false;
		processo.disabilitaCambioVelMiscelatoreBloccoScarico = false;
		counterLog = 0;
		scuotitore_attivo = false;
		contatoreSimulazione = 0; 

		processo.raggiungimentoCondArrestoMotore = false;

	}

	// Inizializzazione Valore Pesa Confezioni
	public void inizializzazioneValorePesaConfezioni() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizializzazione Valore Pesa Confezioni");

		// Inizializzazione Valore Pesa Confezioni
		processo.valorePesaConfezioni = "0";

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Inizializzazione Valore Pesa Confezioni");

	}

	private void controlloPesoConfezioni() {

		letturaPesoConfezioni();

		// Controllo Stato della Pesatura
		if (processo.pesoMancanteConfezioni <= 0) {

			processo.interrompiThreadControlloBloccoScarico = true;

			//////////////////////////////////
			// PESO DESIDERATO RAGGIUNTO ////
			//////////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Peso Desiderato Raggiunto");

			// Chiusura Valvola
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				///////////////////////////
				// ATTUATORE MULTISTADIO //
				///////////////////////////

				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
				ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
			} else {

				////////////////////
				// COMANDO UNICO //
				////////////////////

				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
				ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");

			}

			// Interruzione Thread Corrente
			processo.pesoConfezioneRaggiunto = true;

			// Interruzione Thread Processo Attiva Fluidificatori PesaturaFine
			processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine = true;

		} else if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP) || contatoreSimulazione>30) {

			////////////////////////////////////////////////
			// RAGGIUNGIMENTO PESO FORZATO MANUALMENTE ////
			////////////////////////////////////////////////
			processo.interrompiThreadControlloBloccoScarico = true;

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Pesatura Forzata Manualmente");

			// Chiusura Valvola
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				///////////////////////////
				// ATTUATORE MULTISTADIO //
				///////////////////////////

				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
				ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
			} else {

				////////////////////
				// COMANDO UNICO //
				////////////////////

				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
				ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");

			}
			ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_0DEF");

			// Interruzione Thread Corrente
			processo.pesoConfezioneRaggiunto = true;

			// Interruzione Thread Processo Attiva Fluidificatori PesaturaFine
			processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine = true;

		} else {
			
			if (ABILITA_SIMULAZIONE_PROCESSO) {
			contatoreSimulazione++;
			}

			// Abilitazione Aria Valvola
			if (ariaValvolaAttiva && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))
					&& (Double.parseDouble(processo.valorePesaConfezioni) >= Integer
							.parseInt(ParametriSingolaMacchina.parametri.get(322)))) {

				ariaValvolaAttiva = false;

				processo.interrompiThreadGestoreSvuotaValvola = true;

			}

			///////////////////////////////////////////
			// PESO DESIDERATO NON ANCORA RAGGIUNTO ///
			///////////////////////////////////////////

			//////////////////////////////////////////////////////////////////////
			// CONTROLLO RAGGIUNGIMENTO LIMITE THREAD CONTROLLO BLOCCO SCARICO ///
			//////////////////////////////////////////////////////////////////////
			Integer peso = 0;

			try {
				peso = Integer.parseInt(processo.valorePesaConfezioni);
			} catch (NumberFormatException ex) {

				ProcessoLogger.logger.log(Level.WARNING,
						"Errore durante la conversione del Valore Peso Confezioni - e ={0}", ex);
			}

			if (!processo.interrompiThreadControlloBloccoScarico
					&& peso > Integer.parseInt(ParametriSingolaMacchina.parametri.get(314))) {

				processo.interrompiThreadControlloBloccoScarico = true;

			}

			if (!processo.svuotamento_valvova_da_eseguire) {

				if (processo.materialeDosaturaSoloVibro[processo.indiceConfezioneInPesa] > 0) {

					///////////////////////////////////////////////////////////
					// CONTROLLO RAGGIUNGIMENTO LIMITE SOLO VIBRO ABILITATO ///
					///////////////////////////////////////////////////////////

					if (!raggiungimentoCondArrestoMotore
							&& processo.pesoMancanteConfezioni <= processo.materialeDosaturaSoloVibro[processo.indiceConfezioneInPesa]) {

						///////////////////////////////////////////////
						// CONDIZIONI DI PESATURA CON MOTORE FERMO ///
						///////////////////////////////////////////////
						if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Disattivazione Uscita Motore Miscelatore");

							GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_FALSE_CHAR);

							// Memorizzazione Log Processo
							ProcessoLogger.logger.info("Disattivazione Uscita Motore Miscelatore eseguita");

						}

						raggiungimentoCondArrestoMotore = true;

					}
				}
			}

			// Verifica Stato Pesatura Fine
			if (processo.pesoMancanteConfezioni <= processo.materialeInizioPesaturaFine[processo.indiceConfezioneInPesa]) {

				processo.interrompiThreadControlloBloccoScarico = true;

				////////////////////////////////
				// CONDIZIONI PESATURA FINE ///
				////////////////////////////////

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					// Cambio di Vel Prima di Eseguire la Pesatura Fine
					if (!inizializzazionePesaturaFine) {

						////////////////////////////////////////////
						// IMPOSTAZIONE VELOCITA' PESATURA FINE ///
						////////////////////////////////////////////
						inizializzazionePesaturaFine = true;

						if (!processo.svuotamento_valvova_da_eseguire) {
							// Impostazione Vel Pesatura Fine
							impostazioneVelMiscelatorePesaturaFine();

						}
						/////////////////////////////////////////////////////////////////////////////////////////////////////////// DISATTIVAZIONE
						/////////////////////////////////////////////////////////////////////////////////////////////////////////// SCUOTITORI
						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(503))) {

							GestoreIO_ModificaOut(USCITA_LOGICA_SCUOTITORI_BILANCIA_SACCHETTI, OUTPUT_FALSE_CHAR);
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
							}
						}

						/////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE
						/////////////////////////////////////////////////////////////////////////////////////////////////////////// SOFFIATORE
						/////////////////////////////////////////////////////////////////////////////////////////////////////////// PULISCI
						/////////////////////////////////////////////////////////////////////////////////////////////////////////// VALVOLA
						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Attivazione Aria Pulisci Valvola");

							GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
									OUTPUT_TRUE_CHAR);

							// Memorizzazione Log Processo
							ProcessoLogger.logger.info("Attivazione Aria Pulisci Valvola - Eseguita");

						}

						// Avvio Thread Chiusura e Apertura Valvola
						new ThreadProcessoApriChiudiValvola(processo).start();

					} else if (processo.attivaVelNormaleMiscelatore) {

						///////////////////////////////////////////////////
						// RIATTIVAZIONE VELOCITA' NORMALE MISCELATORE ///
						///////////////////////////////////////////////////
						// Controllo Riattivazione Vel Fine
						controlloReimpostazioneVelFine = true;

						// Vel Miscelatore
						String velMiscelatore = VerificaLunghezzaStringa(
								processo.velocitaMiscelatoreFaseScarico[processo.indiceConfezioneInPesa][2], 4);

						if (!processo.abilitaLineaDirettaMiscelatore && !processo.svuotamento_valvova_da_eseguire) {

							// Cambio Vel Motore Miscelatore = Vel Normale
							inverter_mix.cambiaVelInverter(velMiscelatore);

							// Memorizzazione Log Processo
							ProcessoLogger.logger.log(Level.WARNING,
									"Forzatura Vel Normale Miscelatore - Vel Miscelatore = {0}", velMiscelatore);
						}

						// Reset Variabile di Controllo
						processo.attivaVelNormaleMiscelatore = false;

						// Impostazione Visibilità Pulsante
						processo.pannelloProcesso.elemBut[3].setVisible(false);
						processo.pannelloProcesso.elemLabelSimple[28].setVisible(false);

						// Impostazione Valore Peso al Momento dell'attivazione della velocità normale
						pesoVelNormale = Double.parseDouble(processo.valorePesaConfezioni);

					} else if (controlloReimpostazioneVelFine && Double.parseDouble(processo.valorePesaConfezioni)
							- pesoVelNormale > Integer.parseInt(ParametriSingolaMacchina.parametri.get(221))) {

						///////////////////////////////////////////////////////////////////////////////////
						// IMPOSTAZIONE VELOCITA' PESATURA FINE IN SEGUITO A FORZATURA VELOCITA NORMALE
						/////////////////////////////////////////////////////////////////////////////////// ///
						///////////////////////////////////////////////////////////////////////////////////
						// Inizializzazione Variabile
						controlloReimpostazioneVelFine = false;

						// Impostazione Vel Pesatura Fine
						impostazioneVelMiscelatorePesaturaFine();

					}

				}

			} else {

				//////////////////////////////////////////////
				// CONDIZIONI DIVERSE DALLA PESATURA FINE ///
				//////////////////////////////////////////////

				// Calcolo Vel Motore Miscelatore - Solo in caso di confezionamento in Sacco
				if (!processo.confezionamentoInSecchio) {

					////////////////////////////////
					// CONFEZIONAMENTO IN SACCO ///
					////////////////////////////////
					if (!esecuzioneCambioVel) {

						// Controllo Cambio Vel
						new ThreadGestoreCambioVelocita(pesoPercMancante).start();

					}

				}
				///////////////////////////////////////////////////////////////////////////////////////////////////////// SCUOTITORE
				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(503))) {

					if (scuotitore_attivo && processo.pesoMancanteConfezioni < Integer
							.parseInt(ParametriSingolaMacchina.parametri.get(505))) {

						if (!scuotitore_disattivato) {
							GestoreIO_ModificaOut(USCITA_LOGICA_SCUOTITORI_BILANCIA_SACCHETTI, OUTPUT_FALSE_CHAR);

							scuotitore_disattivato = true;
						}

					} else if (processo.pesoMancanteConfezioni < Integer
							.parseInt(ParametriSingolaMacchina.parametri.get(504)) && !scuotitore_attivo) {

						scuotitore_attivo = true;

						GestoreIO_ModificaOut(USCITA_LOGICA_SCUOTITORI_BILANCIA_SACCHETTI, OUTPUT_TRUE_CHAR);

					}

				}

			}
		}
	}
	// Lettura e Validazione Peso Confezioni

	public void letturaPesoConfezioni() {

		// Lettura del Peso dal Socket
		String socketRep;

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			// Lettura del peso Netto
			socketRep = pesaConfezioni.pesoNetto();

			if (!socketRep.equals("")) {

				// Validazione Valore letto
				processo.valorePesaConfezioni = validazionePeso(socketRep, // Valore letto dal Socket
						processo.valorePesaConfezioni, // Valore Precedente
						0, // Tara Iniziale (Processo Ripristinato)
						Integer.parseInt(ParametriSingolaMacchina.parametri.get(173)), // Tolleranza Accettabile
						Integer.parseInt(ParametriSingolaMacchina.parametri.get(172))); // Numero Ripetizioni Riaggancio
																						// Peso

				if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {
					try {
						processo.valorePesaConfezioni = Integer.toString(Integer.parseInt(processo.valorePesaConfezioni)
								- Integer.parseInt(ParametriSingolaMacchina.parametri.get(442)));

					} catch (NumberFormatException e) {
					}
				}
				
			}

			// Aggiornamento Visualizzazione del Peso
			processo.pannelloProcesso.aggiornaLabelPesaConfezioni(processo.valorePesaConfezioni);

			if (processo.indiceConfezioneInPesa < processo.numConfezioniPerMiscela) {

				if (processo.svuotamento_valvova_da_eseguire) {

					// SVUOTA VALVOLA ABILITATO
					processo.pesoMancanteConfezioni = Double.parseDouble(ParametriSingolaMacchina.parametri.get(316))
							- Double.parseDouble(processo.valorePesaConfezioni);

					// Calcolo Percentuale Materiale Mancante
					pesoPercMancante = (processo.pesoMancanteConfezioni
							/ Double.parseDouble(ParametriSingolaMacchina.parametri.get(316))) * 100;

				} else {

					// SVUOTA VALVOLA DISABILITATO
					processo.pesoMancanteConfezioni = processo.pesoDaRaggiungereConfezione[processo.indiceConfezioneInPesa]
							- Double.parseDouble(processo.valorePesaConfezioni);

					// Calcolo Percentuale Materiale Mancante
					pesoPercMancante = (processo.pesoMancanteConfezioni
							/ processo.pesoDaRaggiungereConfezione[processo.indiceConfezioneInPesa]) * 100;

				}

			}

			memorizzaPesoLog(socketRep);

		}

	}

	// Gestore Memorizzazione Peso Letto su File di Log
	private void memorizzaPesoLog(String socketRep) {

		// Memorizzazione sul File Log
		if (counterLog <= Math.max(LOG_PESATURA_INTERVALLO_REP_CONFIG, LOG_PESATURA_INTERVALLO_REP_INFO)) {

			counterLog++;

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.FINE,
					"ValoreRicevuto dal Socket = {0}{1}Valore Peso Confezioni = {2}{3}Peso Mancante ={4}{5}Peso Percentuale Mancante = {6}",
					new Object[] { socketRep, LOG_CHAR_SEPARATOR, processo.valorePesaConfezioni, LOG_CHAR_SEPARATOR,
							processo.pesoMancanteConfezioni, LOG_CHAR_SEPARATOR, pesoPercMancante });

			if (counterLog % LOG_PESATURA_INTERVALLO_REP_CONFIG == 0) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.CONFIG,
						"ValoreRicevuto dal Socket = {0}{1}Valore Peso Confezioni = {2}{3}Peso Mancante ={4}{5}Peso Percentuale Mancante = {6}",
						new Object[] { socketRep, LOG_CHAR_SEPARATOR, processo.valorePesaConfezioni, LOG_CHAR_SEPARATOR,
								processo.pesoMancanteConfezioni, LOG_CHAR_SEPARATOR, pesoPercMancante });

			}

			if (counterLog % LOG_PESATURA_INTERVALLO_REP_INFO == 0) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO,
						"ValoreRicevuto dal Socket = {0}{1}Valore Peso Confezioni = {2}{3}Peso Mancante ={4}{5}Peso Percentuale Mancante = {6}",
						new Object[] { socketRep, LOG_CHAR_SEPARATOR, processo.valorePesaConfezioni, LOG_CHAR_SEPARATOR,
								processo.pesoMancanteConfezioni, LOG_CHAR_SEPARATOR, pesoPercMancante });

			}

		} else {

			// Inizializzazione Variabile
			counterLog = 0;

		}
	}

	// Validazione del Peso Letto
	private String validazionePeso(String valorePeso, String valPrecedente, int tara, int tolleranza, int numRep) {

		// Inizializzazione Variabile Risultato
		int result = 0;

		// Definizione Valore da da Validare corretto della Tara (per i processi
		// ripristinati)
		int valoreDaValidare = Integer.parseInt(valorePeso) - tara;

		// Controllo Valore Peso Rispetto al Precendente
		if (Math.abs(valoreDaValidare - Integer.parseInt(valPrecedente)) <= tolleranza) {

			/////////////////////
			// VALORE VALIDO ///
			/////////////////////
			result = valoreDaValidare;

			iCount = 0;

		} else {

			//////////////////////////////////////////////////////////////
			// VALORE NON VALIDO - CALCOLO CONDIZIONI RIAGGANCIO PESO ///
			//////////////////////////////////////////////////////////////
			if (iCount > numRep) {

				///////////////////////
				// RIAGGANCIO PESO ///
				///////////////////////
				result = valoreDaValidare;

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Valore di Peso Riagganciato dopo {0} tentativi", iCount);

				iCount = 0;

			} else {

				////////////////////////////////////////////
				// INCREMENTO CONTATORE RIAGGANCIO PESO ///
				////////////////////////////////////////////
				if (valoreDaValidare > 0) {

					iCount++;

				} else {

					iCount = 0;

				}
			}
		}

		return Integer.toString(result);
	}

	// Impostazione Vel Miscelatore Durante la Pesatura fine
	public void impostazioneVelMiscelatorePesaturaFine() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizio Impostazione Vel Pesatura Fine Eseguita");

		// Impostazione Vel Motore Miscelatore - Vel Lenta
		if (!processo.confezionamentoInSecchio) {

			////////////////////////////////
			/// CONFEZIONAMENTO IN SACCO ///
			////////////////////////////////
			// Impostazione Visibilità Pulsante
			processo.pannelloProcesso.elemBut[3].setVisible(true);
			processo.pannelloProcesso.elemLabelSimple[28].setVisible(true);

			// Vel Pesatura Fine
			String velPesaturaFine = VerificaLunghezzaStringa(
					processo.velocitaMiscelatoreFaseScarico[processo.indiceConfezioneInPesa][0], 4);

			if (!processo.abilitaLineaDirettaMiscelatore) {
				// Impostazione Vel Iniziale Motore Miscelatore = Vel Normale
				// processo.invMotore.cambioVelInverter(velPesaturaFine);

////                GestoreIO_InviaComunicazioneRS485(
////                        inverter_mix.device,
////                        inverter_mix.cambiaVelInverter(velPesaturaFine));
				inverter_mix.cambiaVelInverter(velPesaturaFine);
				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Impostazione Vel Pesatura Fine Eseguita  - Valore = {0}",
						velPesaturaFine);
			}
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizio Impostazione Vel Pesatura Fine Eseguita");
	}

}
