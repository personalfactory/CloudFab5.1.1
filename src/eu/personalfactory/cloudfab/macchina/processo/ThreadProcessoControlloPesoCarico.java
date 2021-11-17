/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AdattaInfoPesoTeorico;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaDatiMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ControlloTolleranzediPesoComponentiProcesso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ElaboraVelInverter;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_CONFIG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_INFO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SIMULAZIONE_PESO_ABILITATA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SIMULAZIONE_PESO_INCREMENTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CARICO; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_RITARDO_ATTIVAZIONE_INVERTER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_VOLO_MATERIALE; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIPETIZIONI_AZZERAMENTO;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 *
 *         Controlla il Raggiungimento del Peso Componentente Gestisce il Cambio
 *         di Vel delle Coclee e l'Intervento della Seconda Velocita' per i
 *         Vecchi Impianti Esegue la lettura del Peso dal Modulo di Pesa
 *         Aggiorna il Database quando la Pesa del Componente Viene Completata
 *
 */
public class ThreadProcessoControlloPesoCarico extends Thread {

	private final Processo processo;
	private ClientPesaTLB4 pesaCarico;
	// private int indexVel;
	private int contaTentativiRiaggancioPeso;
	private int qPesata;
	private boolean interrompiThreadLetturaPesoCarico;
	private double pesoMancante;
	private int counterLog;
	private boolean threadCambioVelInEsecuzione;
	// private boolean eseguiCambioVel;
	private String autoRegolazioneNuovaVel;
	private int ultimoValorePeso;
	private boolean vibroAttivato;
	private int mov_magazzino;

	// COSTRUTTORE
	public ThreadProcessoControlloPesoCarico(Processo processo) {

		// Dichiarazione Variabile Processo
		this.processo = processo;

		// Impostazione Nome Thread Corrente
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run() {

		//////////////////////////////////
		// INIZIALIZZAZIONE VARIABILI ///
		//////////////////////////////////
		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizio Procedura Controllo Peso Carico");

		// Memorizzazione Log Processo
		ProcessoLogger.logger.info("Caratteristiche Componente in Pesa" + LOG_CHAR_SEPARATOR + "Nome ="
				+ processo.componenti.get(processo.indiceCompInPesa).getNome() + LOG_CHAR_SEPARATOR + "Id ="
				+ processo.componenti.get(processo.indiceCompInPesa).getId() + LOG_CHAR_SEPARATOR + "Presa  ="
				+ processo.componenti.get(processo.indiceCompInPesa).getPresa() + LOG_CHAR_SEPARATOR + "Quantita'  ="
				+ processo.componenti.get(processo.indiceCompInPesa).getQuantità() + LOG_CHAR_SEPARATOR
				+ "Correttivo Vel  =" + processo.componenti.get(processo.indiceCompInPesa).getCorrettivoVelocità()
				+ LOG_CHAR_SEPARATOR + "Volo =" + processo.componenti.get(processo.indiceCompInPesa).getVolo()
				+ LOG_CHAR_SEPARATOR + "Quantita Seconda Velocita Inverter O4  ="
				+ processo.componenti.get(processo.indiceCompInPesa).getQuantitàSecondaVelocitàInverter()
				+ LOG_CHAR_SEPARATOR + "Tolleranza in Difetto ="
				+ processo.componenti.get(processo.indiceCompInPesa).getTolleranzaDifetto() + LOG_CHAR_SEPARATOR
				+ "Tolleranza in Eccesso =" + processo.componenti.get(processo.indiceCompInPesa).getTolleranzaEccesso()
				+ LOG_CHAR_SEPARATOR + "Quantità Stop Mescolatore ="
				+ processo.componenti.get(processo.indiceCompInPesa).getQuantitàStopMescolatore() + LOG_CHAR_SEPARATOR
				+ "Quantità Stop Mescolatore ="
				+ processo.componenti.get(processo.indiceCompInPesa).getQuantitàStopVibro() + LOG_CHAR_SEPARATOR
				+ "Curva Dosatura =" + processo.componenti.get(processo.indiceCompInPesa).getCurvaDosatura()
				+ LOG_CHAR_SEPARATOR + "Metodo di Pesatura ="
				+ processo.componenti.get(processo.indiceCompInPesa).getMetodoPesa() + LOG_CHAR_SEPARATOR
				+ "Fluidificazione Attiva  =" + processo.componenti.get(processo.indiceCompInPesa).isFluidificazione()
				+ LOG_CHAR_SEPARATOR + "Valore Residuo Fluidificazione  ="
				+ processo.componenti.get(processo.indiceCompInPesa).getValoreResiduoFluidificazione()
				+ LOG_CHAR_SEPARATOR + "Ordine di Dosaggio  ="
				+ processo.componenti.get(processo.indiceCompInPesa).getOrdineDosaggio() + LOG_CHAR_SEPARATOR
				+ "Step di Dosaggio  =" + processo.componenti.get(processo.indiceCompInPesa).getStepDosaggio());

		TimeLineLogger.logger.log(Level.INFO, "Avvio Pesa Componente {1} - id_ciclo ={0}",
				new Object[] { TrovaSingoloValoreParametroRipristino(91),
						processo.componenti.get(processo.indiceCompInPesa).getNome() });

		// Inzializzazione Variabili di Controllo Peso
		processo.interruzioneControlloPesoRaggiunto = false;
		processo.interruzioneControlloPesoCocleaVuota = false;
		processo.interruzioneControlloPesoTastoReset = false;
		processo.interruzioneControlloPesoInterruzioneManuale = false;

		processo.dosaturaDirettaAttiva = false;
		processo.autoRegVelAttiva = false;

		threadCambioVelInEsecuzione = false;

		processo.velCaricoCorrente = "";
		processo.iVelCaricoCorrente = 0;

		processo.corrAutoRegCaricoPrec = 0;
		processo.corrAutoRegCarico = 0;

		// Inizializzazione Counter File Log
		counterLog = 0;

		// Inizializzazione Indice di Vel
		contaTentativiRiaggancioPeso = 0;
		processo.pulsanteSpeedAttivato = false;
		interrompiThreadLetturaPesoCarico = false;

		mov_magazzino = 0;

		vibroAttivato = false;

		pesoMancante = processo.componenti.get(processo.indiceCompInPesa).getQuantità()
				- processo.componenti.get(processo.indiceCompInPesa).getVolo();

		processo.pesoMancantePercCarico = (pesoMancante / Double.parseDouble(ParametriGlobali.parametri.get(31))) * 100;

		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(238))) {

			processo.pannelloProcesso.elemBut[8].setVisible(true);
			processo.pannelloProcesso.elemLabelSimple[27].setVisible(true);

			processo.forzaPesoComponente = false;
		}

		////////////////////////////////
		// CREAZIONE CLIENT DI PESA ///
		////////////////////////////////

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			try {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Inizializzazione Client di Pesa");

				// Creazione Client di Pesa Carico
				pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
				
				
				// Abilitazione Pulsante Riattiva Vel Normale
				processo.pannelloProcesso.elemBut[4].setVisible(true);
				processo.pannelloProcesso.elemLabelSimple[29].setVisible(true);

				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Inizializzazione Client di Pesa Eseguita");

			} catch (IOException ex) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.severe("Errore durante la creazione del Client Pesa  e =" + ex);

				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
					RegistraAllarme(8,
							EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 902,
									ParametriSingolaMacchina.parametri.get(111))) + " =" + ex,
							TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
				}
			}
		}

		/////////////////////////////////////////////
		// INIZIALIZZAZIONE VALORE PESO DI CARICO ///
		/////////////////////////////////////////////
		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			// Inizializzazione Valore Peso Carico
			inizializzazioneValorePesoCarico();

			//////////////////////////////////////////
			// INIZIALIZZAZIONE VELOCITA' INVERTER ///
			//////////////////////////////////////////
			inizializzazioneVeloInverterCoclee();

			//////////////////////////////////
			// AVVIO PROCEDURA DI PESATURA ///
			//////////////////////////////////
			// Avvio Thread Pesatura Componente
			new ThreadProcessoPesaturaComponente(processo).start();

			/////////////////////////////////////////
			// INIZIO THREAD LETTURA PESO CARICO ///
			/////////////////////////////////////////
			// Avvio Thread Lettura Peso Bilancia di Carico
			new ThreadLetturaPesoCarico().start();

		}

		/////////////////////////////////
		// VIBRATORI SINGOLI PRESENTI ///
		/////////////////////////////////
		processo.pannelloProcesso.impostaVisPulsantiVibroFunghi(true);
		processo.pannelloProcesso.impostaVisPulsantiVibroPneumatici(true);

		processo.pannelloProcesso.impostaAbilitaPulsantiVibroFunghi(true);
		processo.pannelloProcesso.impostaAbilitaPulsantiVibroPneumatici(true);

		mov_magazzino = Integer.parseInt(TrovaSingoloValoreParametroRipristino(100));

		if (mov_magazzino == 0) {

			// Registra Movimento di Magazzino
			mov_magazzino = RegistraNuovoMovimentoMagazzino(processo.componenti.get(processo.indiceCompInPesa).getId(), // id_materiale
					ParametriGlobali.parametri.get(117), // tipo_materiale
					0, // qEffettivo
					processo.componenti.get(processo.indiceCompInPesa).getCodiceMovimento_Magazzino(), // cod_ingresso_comp
					DettagliSessione.getCodiceFigura(), // cod_operatore
					"-1", // segno_op
					ParametriGlobali.parametri.get(124), // procedura_adottata
					ParametriGlobali.parametri.get(119), // tipo_mov
					ParametriGlobali.parametri.get(132), // descri_mov
					processo.componenti.get(processo.indiceCompInPesa).getPresa(), // id_silo
					processo.componenti.get(processo.indiceCompInPesa).getQuantità(),
					Integer.parseInt(TrovaSingoloValoreParametroRipristino(91)), // id_ciclo
					ParametriGlobali.parametri.get(140), //origine_mov 
					new Date(),			//DataMov
                    true,				//Abilitato
                    TrovaValoreParametriComponentePerIdComp(processo.componenti.get(processo.indiceCompInPesa).getId()).get(9),					//Info1
                    TrovaValoreParametriComponentePerIdComp(processo.componenti.get(processo.indiceCompInPesa).getId()).get(10),					//Info2 
                    "",					//Info3 					
                    "",					//Info4 
                    "",					//Info5 
                    "",					//Info6 
                    "",					//Info7 
                    "",					//Info8 
                    "",					//Info9 
                    ""); 				//Info10 
               
			// Aggiornamento Valore Parametro di Ripristino IdMovimento di Magazzino
			AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(100), 100,
					Integer.toString(mov_magazzino), ParametriSingolaMacchina.parametri.get(15));
		}

		// Controllo Abilitazione Gestione
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(443))) {

			////////////////////////////////////////////////
			// CONTROLLO VELOCITA MISCELAZIONE ABILITATO ///
			////////////////////////////////////////////////

			inverter_mix.cambiaVelInverter(processo.prodotto.getMixingSpeed());

		}

		/////////////////////////////////////
		// INIZIO LOOP CONTROLLO PESATURA ///
		/////////////////////////////////////
		while (!processo.interruzioneControlloPesoRaggiunto && processo.pannelloProcesso.isVisible()
				&& !processo.resetProcesso && !processo.interruzioneControlloPesoCocleaVuota
				&& !processo.interruzioneControlloPesoTastoReset
				&& !processo.interruzioneControlloPesoInterruzioneManuale) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.fine("Loop Controllo Peso Carico in Esecuzione");

			controlloPesoCarico();

		} // Fine loop

		// Impostazione Visibilità Pulsanti Vibro
		processo.pannelloProcesso.resetVisPulsantiVibro(); 
		
		processo.attivaVibroFunghiSiloCorrente(false);

		processo.interrompiThreadVibro = true;

		// Memorizzazione Log Processo
		ProcessoLogger.logger.fine("Fine Loop Controllo Peso Carico");

		// Abilitazione Pulsante Riattiva Vel Normale
		processo.pannelloProcesso.elemBut[4].setVisible(false);
		processo.pannelloProcesso.elemLabelSimple[29].setVisible(false);

		TimeLineLogger.logger.log(Level.INFO, "Peso Componente {0} Raggiunto - Ultimo Valore = {1}g - id_ciclo ={2}",
				new Object[] { processo.componenti.get(processo.indiceCompInPesa).getNome(), processo.valorePesaCarico,
						TrovaSingoloValoreParametroRipristino(91) });

		////////////////////////
		// ARRESTO INVERTER ///
		////////////////////////
		if (!processo.dosaturaDirettaAttiva) {

			new ThreadArrestoInverter().start();

		}

		// Controllo Interruzione per Coclea Vuota
		if (processo.resetProcesso) {

			///////////////////////////////////////////
			/// LOOP INTERROTTO PER RESET PROCESSO ///
			///////////////////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Loop Controllo Peso Interrotto per Reset Processo");

			// Finalizzazione Controllo Peso Carico per Peso Desiderato Raggiunto
			finalizzaControlloPesoResetProcesso();

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
				RegistraAllarme(2,
						EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 903,
								ParametriSingolaMacchina.parametri.get(111))) + " "
								+ processo.componenti.get(processo.indiceCompInPesa).getNome(),
						TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
			}

		} else if (processo.interruzioneControlloPesoRaggiunto) {

			////////////////////////////////////////////////////
			/// LOOP INTERROTTO PER RAGGIUNGIMENTO DEL PESO ///
			////////////////////////////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Loop Controllo Peso Interrotto per Raggiungimento del Peso");

			// Serve per la Simulazione
			processo.valorePesaConfezioni = "0";
			processo.counter_simulazione_peso = 0;

			// Finalizzazione Controllo Peso Carico per Peso Desiderato Raggiunto
			finalizzaControlloPesoCaricoRaggiunto();

		} else if (processo.interruzioneControlloPesoCocleaVuota) {

			/////////////////////////////////////////
			/// LOOP INTERROTTO PER COCLEA VUOTA ///
			/////////////////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.warning("Loop Controllo Peso Interrotto per Coclea Vuota");

			// Finalizzazione Controllo Peso Carico per Peso Coclea Vuota
			finalizzaControlloPesoCocleaVuota();

		} else if (processo.interruzioneControlloPesoInterruzioneManuale) {

			/////////////////////////////////////////////////
			/// LOOP INTERROTTO PER INTERRUZIONE MANUALE ///
			/////////////////////////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.warning("Loop Controllo Peso Interrotto per Interruzione Manuale");

			// Finalizzazione Controllo Peso Carico per Interruzione Manuale
			finalizzaControlloPesoInterruzioneManuale();

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
				RegistraAllarme(2,
						EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 904,
								ParametriSingolaMacchina.parametri.get(111))) + " "
								+ processo.componenti.get(processo.indiceCompInPesa).getNome(),
						TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
			}
		}

		processo.pannelloProcesso.elemBut[8].setVisible(false);
		processo.pannelloProcesso.elemLabelSimple[27].setVisible(false);

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Procedura Controllo Peso Carico");
	}

	public void inizializzazioneVeloInverterCoclee() {

		////////////////////////
		// ARRESTO INVERTER ///
		////////////////////////
		// Arresto Inverter
		inverter_screws.avviaArrestaInverter(false);

		/////////////////////////////////
		// INIZILIZZAZIONE VELOCITA' ///
		/////////////////////////////////
		if (!processo.dosaturaDirettaAttiva && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(246))) {

			// Valutazione Dosatura Diretta o Curva di Dosaggio
			if (Double.parseDouble(TrovaGruppoValoreParametroRipristino(19).get(processo.indiceCompInPesa)) > Double
					.parseDouble(TrovaGruppoValoreParametroRipristino(20).get(processo.indiceCompInPesa))) {

				// Reimpostazione Vel di Base Inverter
				inverter_screws.cambiaVelInverter(ParametriGlobali.parametri.get(28));

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Inizializzazione Velocità Inverter = {0}",
						ParametriGlobali.parametri.get(28));
			} // Modifica Novembre 2015 - Corretto Aprile 2016
			else {

				// DOSATURA DIRETTA A VELOCITA RIDOTTA
				// Reimpostazione Vel di Base Inverter
				inverter_screws.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(56));

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Inizializzazione Velocità Inverter = {0}",
						ParametriSingolaMacchina.parametri.get(56));

			}

		} else {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Inizializzazione Velocità Inverter Ignorata");

		}

	}

	// Registrazione Pesa Componente
	public void registraPesaComponente(String valorePeso) {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Registrazione Pesa Componente {0} Completata",
				processo.indiceCompInPesa);

		processo.pesoRegistratoComponente = processo.valorePesaCarico;

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			try {

				// Costruzione e Memorizzazione Formula Effettiva
				String formEffettiva = TrovaSingoloValoreParametroRipristino(28);

				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {
					/////////////////////////////////////////////////
					// REGISTRAZIONE INFO AGGIUNTIVE ABILITITATA ///
					/////////////////////////////////////////////////

					if (formEffettiva.equals("")) {
						formEffettiva = formEffettiva + processo.componenti.get(processo.indiceCompInPesa).getId()
								+ ParametriGlobali.parametri.get(22) + processo.pesoRegistratoComponente
								+ ParametriGlobali.parametri.get(22) + AdattaInfoPesoTeorico(
										processo.componenti.get(processo.indiceCompInPesa).getQuantità());
					} else {
						formEffettiva = formEffettiva + ParametriGlobali.parametri.get(21)
								+ processo.componenti.get(processo.indiceCompInPesa).getId()
								+ ParametriGlobali.parametri.get(22) + processo.pesoRegistratoComponente
								+ ParametriGlobali.parametri.get(22) + AdattaInfoPesoTeorico(
										processo.componenti.get(processo.indiceCompInPesa).getQuantità());
					}
				} else {

					/////////////////////////////////////////////////////
					// REGISTRAZIONE INFO AGGIUNTIVE NON ABILITITATA ///
					/////////////////////////////////////////////////////
					if (formEffettiva.equals("")) {
						formEffettiva = formEffettiva + processo.componenti.get(processo.indiceCompInPesa).getId()
								+ ParametriGlobali.parametri.get(22) + processo.pesoRegistratoComponente;
					} else {
						formEffettiva = formEffettiva + ParametriGlobali.parametri.get(21)
								+ processo.componenti.get(processo.indiceCompInPesa).getId()
								+ ParametriGlobali.parametri.get(22) + processo.pesoRegistratoComponente;
					}
				}

				// Aggiornamento Record Formula Effettiva
				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(28), 28,
						formEffettiva, ParametriSingolaMacchina.parametri.get(15));

				// Aggiornamento dei Parametri Ripristino Pesa Componenti
				AggiornaValoreParametriRipristino(processo,
						TrovaIndiceTabellaRipristinoPerIdParRipristino(25) + processo.indiceCompInPesa, 25, "true",
						ParametriSingolaMacchina.parametri.get(15));

				// Aggiornamento Fattore Correttivo della Pesa
				Integer qCompPesati = processo.quantComponentePesata + Integer.parseInt(valorePeso);

				// Aggiornamento Quantita Componenti Pesata
				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(26), 26,
						Integer.toString(qCompPesati), ParametriSingolaMacchina.parametri.get(15));

				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Registrazione Pesa Componentente Completata - Eseguita");

			} catch (NumberFormatException e) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.SEVERE, "Registrazione Pesa Componentente Completata - Fallita  e ={0}",
						e);

				// Visualizzazione Errore di Registrazione
				((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
						.visualizzaErrore(6);

			}

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Registrazione Pesa Componente");

	}

	// Procedura di lettura del Peso sulla Bilancia di Carico
	public void letturaPesoCarico() {

		// Lettura del Peso dal Socket
		String socketRep;

		if (SIMULAZIONE_PESO_ABILITATA) {

			////////////////////////
			// SIMULAZIONE PESO ///
			////////////////////////
			if (!processo.interruzioneControlloPesoRaggiunto) {

				if (processo.counter_simulazione_peso < 200) {

					processo.counter_simulazione_peso++;

					socketRep = processo.valorePesaCarico;

					processo.valorePesaCarico = socketRep;

				} else {

					processo.counter_simulazione_peso = 0;

					socketRep = Integer
							.toString(Integer.parseInt(processo.valorePesaCarico) + SIMULAZIONE_PESO_INCREMENTO);

					processo.valorePesaCarico = socketRep;

					processo.counter_simulazione_peso += SIMULAZIONE_PESO_INCREMENTO;

				}
			} else {
				socketRep = processo.valorePesaCarico;
			}
		} else {

			// Controllo processo Ripristinato
			if (processo.pannelloProcesso.ripristinato) {

				///////////////////////////////
				// PROCESSO RIPRISTINATO /////
				///////////////////////////////
				// Lettura del peso Lordo
				socketRep = pesaCarico.pesoLordo();

			} else {

				////////////////////////////////////
				// PROCESSO NON RIPRISTINATO
				////////////////////////////////////
				// Lettura del peso Netto
				socketRep = pesaCarico.pesoNetto();
			}

			try {
				if (!socketRep.equals("")) {

					int valoreDaValidare = Integer.parseInt(socketRep) - qPesata;

					if (!socketRep.equals("0")) {

						if (Math.abs(valoreDaValidare - Integer.parseInt(processo.valorePesaCarico)) <= Integer
								.parseInt(ParametriSingolaMacchina.parametri.get(174))) {

							processo.valorePesaCarico = Integer.toString(valoreDaValidare);
							contaTentativiRiaggancioPeso = 0;
						} else {

							if (contaTentativiRiaggancioPeso > Integer
									.parseInt(ParametriSingolaMacchina.parametri.get(171))) {
								processo.valorePesaCarico = Integer.toString(valoreDaValidare);

								// Memorizzazione Log Processo
								ProcessoLogger.logger.log(Level.INFO, "Valore di Peso Riagganciato dopo {0} Tentativi",
										contaTentativiRiaggancioPeso);

								contaTentativiRiaggancioPeso = 0;

							} else {

								if (valoreDaValidare > 0) {
									contaTentativiRiaggancioPeso++;
								} else {
									contaTentativiRiaggancioPeso = 0;
								}
							}

						}
					}
				}

			} catch (NumberFormatException e) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.SEVERE,
						"ATTENZIONE: Errore durante la Validazione del Peso Letto e={0}", e);
			}

		}

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			// Aggiornamento Label Visualizzazione Peso Pannello Processo
			processo.pannelloProcesso.aggiornaLabelPesaCarico(Integer.parseInt(processo.valorePesaCarico),
					Integer.parseInt(processo.valorePesaCarico) + processo.quantComponentePesata);

			// Calcolo del Peso Mancante
			pesoMancante = (processo.componenti.get(processo.indiceCompInPesa).getQuantità()
					- processo.componenti.get(processo.indiceCompInPesa).getVolo())
					- Integer.parseInt(processo.valorePesaCarico);

			// Calcolo del Peso Mancante Percentuale
			processo.pesoMancantePercCarico = (pesoMancante / Double.parseDouble(ParametriGlobali.parametri.get(31)))
					* 100;

			// Controllo Abilitazione Gestione
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(443))
					&& !processo.cambioGestVelMixEseguito) {

				/////////////////////////////////////////////////
				// CONTROLLO VELOCITA MISCELAZIONE ABILITATO ///
				/////////////////////////////////////////////////
				if (pesoMancante < Integer.parseInt(ParametriSingolaMacchina.parametri.get(444))) {

					// processo.invMotore.cambioVelInverter(ParametriSingolaMacchina.parametri.get(445));
////                    GestoreIO_InviaComunicazioneRS485(
////                            inverter_mix.device,
////                            inverter_mix.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(445)));
					inverter_mix.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(445));
					processo.cambioGestVelMixEseguito = true;

				}
			}

			// Memorizzazione Peso Letto su File di Lo
			memorizzaPesoLog(socketRep);

		}

	}

	// Gestore Memorizzazione Peso Letto su File di Log
	private void memorizzaPesoLog(String socketRep) {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.FINE,
				"Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}{5}Peso Mancante Percentuale = {6}",
				new Object[] { socketRep, LOG_CHAR_SEPARATOR, processo.valorePesaCarico, LOG_CHAR_SEPARATOR,
						pesoMancante, LOG_CHAR_SEPARATOR, processo.pesoMancantePercCarico });

		if (ParametriSingolaMacchina.parametri.get(234).equals("INFO")) {

			if (counterLog >= LOG_PESATURA_INTERVALLO_REP_INFO) {
				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO,
						"Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}{5}Peso Mancante Percentuale = {6}",
						new Object[] { socketRep, LOG_CHAR_SEPARATOR, processo.valorePesaCarico, LOG_CHAR_SEPARATOR,
								pesoMancante, LOG_CHAR_SEPARATOR, processo.pesoMancantePercCarico });

				counterLog = 0;
			} else {
				counterLog++;
			}
		} else if (counterLog >= LOG_PESATURA_INTERVALLO_REP_CONFIG) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.CONFIG,
					"Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}{5}Peso Mancante Percentuale = {6}",
					new Object[] { socketRep, LOG_CHAR_SEPARATOR, processo.valorePesaCarico, LOG_CHAR_SEPARATOR,
							pesoMancante, LOG_CHAR_SEPARATOR, processo.pesoMancantePercCarico });

			counterLog = 0;
		} else {
			counterLog++;
		}
	}

	// Gestisce i cambi di velocità delle coclee
	private class ThreadGestoreCambioVelocita extends Thread {

		@Override
		public void run() {

			threadCambioVelInEsecuzione = true;

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso && !processo.dosaturaDirettaAttiva) {

				// Controllo disattivazione Variazione Velocita
				if (!processo.disattivaVariazioneVel && !processo.autoRegVelAttiva) {

					ultimoValorePeso = Integer.parseInt(processo.valorePesaCarico);

					/////////////////////////////////
					// VARIAZIONE VELOCITA ATTIVA ///
					/////////////////////////////////
					for (int i = processo.componenti.get(processo.indiceCompInPesa).getLimitiCambioVelocità().size()
							- 1; i >= 0; i--) {

						if (processo.pesoMancantePercCarico >= processo.componenti.get(processo.indiceCompInPesa)
								.getLimitiCambioVelocità().get(i)) {

							ProcessoLogger.logger.log(Level.CONFIG,
									"Peso Mancante Percentuale Carico ={0}{1}Aliquota di Riferiemnto ={2}",
									new Object[] { processo.pesoMancantePercCarico, LOG_CHAR_SEPARATOR,
											processo.componenti.get(processo.indiceCompInPesa).getLimitiCambioVelocità()
													.get(i) });

							int indexVel = processo.componenti.get(processo.indiceCompInPesa).getVelocità().size()
									- (processo.componenti.get(processo.indiceCompInPesa).getLimitiCambioVelocità()
											.size() - i);

							autoRegolazioneNuovaVel = processo.componenti.get(processo.indiceCompInPesa).getVelocità()
									.get(indexVel);

							if (!autoRegolazioneNuovaVel.equals(processo.velCaricoCorrente)) {

								processo.velCaricoCorrente = autoRegolazioneNuovaVel;
								processo.iVelCaricoCorrente = indexVel;
								cambioVelCarico();

								ProcessoLogger.logger.log(Level.CONFIG,
										"processo.velCaricoCorrente = {0}{1}\nprocesso.velCaricoCorrente = autoRegolazioneNuovaVel = {2}{3}\nprocesso.iVelCaricoCorrente = {4}{5}",
										new Object[] { processo.velCaricoCorrente, LOG_CHAR_SEPARATOR,
												autoRegolazioneNuovaVel, LOG_CHAR_SEPARATOR,
												processo.iVelCaricoCorrente, LOG_CHAR_SEPARATOR });

							}

							processo.pannelloProcesso.elemBut[4].setVisible(true);
							processo.pannelloProcesso.elemLabelSimple[29].setVisible(true);
							break;

						} else if (processo.pesoMancantePercCarico <= processo.componenti.get(processo.indiceCompInPesa)
								.getLimitiCambioVelocità().get(0)) {

							autoRegolazioneNuovaVel = processo.componenti.get(processo.indiceCompInPesa).getVelocità()
									.get(0);

							if (!autoRegolazioneNuovaVel.equals(processo.velCaricoCorrente)) {

								processo.velCaricoCorrente = autoRegolazioneNuovaVel;
								processo.iVelCaricoCorrente = 0;
								cambioVelCarico();

								ProcessoLogger.logger.log(Level.INFO,
										"processo.velCaricoCorrente = {0}{1}processo.velCaricoCorrente = autoRegolazioneNuovaVel = {2}{3}processo.iVelCaricoCorrente = {4}",
										new Object[] { processo.velCaricoCorrente, LOG_CHAR_SEPARATOR,
												autoRegolazioneNuovaVel, LOG_CHAR_SEPARATOR,
												processo.iVelCaricoCorrente });

							}
							break;

						}

					}

				} else {

					///////////////////////////////
					// AUTO REGOLAZIONE ATTIVA ///
					///////////////////////////////
					if (Integer.parseInt(processo.valorePesaCarico) - ultimoValorePeso > Integer
							.parseInt(ParametriSingolaMacchina.parametri.get(222))) {

						processo.disattivaVariazioneVel = false;

						processo.forzaturaVelManualeInCorso = false;

					} else {

						if (processo.corrAutoRegCarico != processo.corrAutoRegCaricoPrec) {

							processo.corrAutoRegCaricoPrec = processo.corrAutoRegCarico;

							if (processo.iVelCaricoCorrente + processo.corrAutoRegCarico < processo.componenti
									.get(processo.indiceCompInPesa).getVelocità().size()) {

								//////////////////////////////////////////////////////
								// CAMBIO VELOCITA' AUTOREGOLAZIONE PORTATA BASSA ///
								//////////////////////////////////////////////////////
								ProcessoLogger.logger.info("Abilitazione Cambio Vel ");

								String velPrecedente = autoRegolazioneNuovaVel;

								processo.iVelCaricoCorrente = (processo.iVelCaricoCorrente
										+ processo.corrAutoRegCarico);

								if (processo.corrAutoRegCarico > 0) {
									processo.corrAutoRegCarico--;
									processo.corrAutoRegCaricoPrec = processo.corrAutoRegCarico;
								}

								/////////////////////////////////////////////////////////////////////
								// CONTROLLO CHE LA NUOVA VELOCITA SIA MAGGIORE DELLA PRECEDENTE ///
								/////////////////////////////////////////////////////////////////////
								String velSuccessiva = processo.componenti.get(processo.indiceCompInPesa).getVelocità()
										.get(processo.iVelCaricoCorrente);

								if (Double.parseDouble(velSuccessiva) > Double.parseDouble(velPrecedente)) {

									//////////////////////////////////////////////
									// VELOCITA SUPERIORE DI QUELLA PRECEDENTE ///
									//////////////////////////////////////////////
									autoRegolazioneNuovaVel = velSuccessiva;

									velPrecedente = velSuccessiva;

									cambioVelCarico();

									ProcessoLogger.logger.log(Level.INFO,
											"Auto Regolazione Vel Carico - Vel Precedente = {0} - Nuova Vel = {1}",
											new Object[] { velPrecedente, autoRegolazioneNuovaVel });

								}

							} else {

								processo.pannelloProcesso.elemBut[4].setVisible(false);
								processo.pannelloProcesso.elemLabelSimple[29].setVisible(false);

							}
						}
					}

				}
			}

			threadCambioVelInEsecuzione = false;
		}
	}

	public void cambioVelCarico() {

		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(296))
				&& (processo.forzaturaVelManualeInCorso)) {

			autoRegolazioneNuovaVel = ParametriSingolaMacchina.parametri.get(297);

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.INFO, "Invio Comando Cambio Vel - Forzatura {0}",
					ParametriSingolaMacchina.parametri.get(297));
		}
		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Invio Comando Cambio Vel");

		String vel = ElaboraVelInverter(autoRegolazioneNuovaVel,
				processo.componenti.get(processo.indiceCompInPesa).getCorrettivoVelocità());

		// processo.invCarico.cambioVelInverter(vel);
////        GestoreIO_InviaComunicazioneRS485(
////                inverter_screws.device,
////                inverter_screws.cambiaVelInverter(vel));
		inverter_screws.cambiaVelInverter(vel);

		processo.velCaricoCorrente = autoRegolazioneNuovaVel;

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Fine Comando Cambio Vel ={0}", vel);

		processo.pannelloProcesso.elemBut[4].setVisible(true);
		processo.pannelloProcesso.elemLabelSimple[29].setVisible(true);

	}

	// Inizializzazione Valore Peso Carico
	public void inizializzazioneValorePesoCarico() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizializzazione Valore Peso Carico");

		// Inizializzazione valorePesoCarico
		if (processo.pannelloProcesso.ripristinato) {

			/////////////////////////////
			// PROCESSO RIPRISTINATO ///
			/////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Processo Ripristinato");

			// Lettura quantità componente pesata
			processo.quantComponentePesata = Integer.parseInt(TrovaSingoloValoreParametroRipristino(26));

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.CONFIG, "Quant di componente pesata = {0}", processo.quantComponentePesata);

			// Lettura quantità componente pesata + taraPesaCarico
			qPesata = processo.quantComponentePesata + Integer.parseInt(TrovaSingoloValoreParametroRipristino(52));

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.CONFIG, "Quant di componente pesata + tara = {0}", qPesata);

			// Lettura del Peso Lordo
			processo.valorePesaCarico = "0";
			int counter = 0;

			// Inizio loop
			while (processo.valorePesaCarico.equals("0")
					&& counter < Integer.parseInt(ParametriSingolaMacchina.parametri.get(170))
					&& processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				int peso = 0;
				try {
					peso = Integer.parseInt(pesaCarico.pesoLordo());
				} catch (NumberFormatException e) {
				}

				try {
					processo.valorePesaCarico = Integer.toString(peso - qPesata);
				} catch (Exception e) {
				}
				counter++;

				try {
					ThreadProcessoControlloPesoCarico
							.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(187)));
				} catch (InterruptedException ex) {
				}
			} // fine loop

		} else {

			///////////////////////
			// PROCESSO NORMALE ///
			///////////////////////
			
			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Processo Normale");

			qPesata = 0;

			processo.valorePesaCarico = "0";

			// Lettura quantità componente pesata
			processo.quantComponentePesata = Integer.parseInt(TrovaSingoloValoreParametroRipristino(26));

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.CONFIG, "Quant di componente pesata = {0}", processo.quantComponentePesata);

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.CONFIG, "Valore Peso Carico al Termine dell''Inizializzazione ={0}",
				processo.valorePesaCarico);

	}

	// Finalizzazione Procedura Controllo Peso per Peso Raggiunto
	public void finalizzaControlloPesoCaricoRaggiunto() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.info("Loop di Pesatura Interrotto per Raggiungimento del Peso Desiderato");

		// Interruzione Thread di Controllo Coclea Vuota
		processo.interrompiThreadControlloCocleaVuota = true;

		if (!processo.dosaturaDirettaAttiva) {

			///////////////////////
			// PROCESSO NORMALE ///
			///////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Attesa Volo Materiale");

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.INFO, "Tempo di Attesa Deposito Previsto Delay Contattore = {0}",
					TEMPO_RITARDO_ATTIVAZIONE_INVERTER);

			TimeLineLogger.logger.log(Level.INFO, "Avvio tempo volo ={1} - componente = {2} - id_ciclo ={0}",
					new Object[] { TrovaSingoloValoreParametroRipristino(91), TEMPO_RITARDO_ATTIVAZIONE_INVERTER,
							processo.componenti.get(processo.indiceCompInPesa).getNome() });

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				try {
					// Attesa Deposito Volo Materiale
					ThreadProcessoControlloPesoCarico.sleep(TEMPO_RITARDO_ATTIVAZIONE_INVERTER);
				} catch (InterruptedException ex) {
				}

				// Registrazione Pesa Componente Completata
				registraPesaComponente(processo.valorePesaCarico);
			}

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				// Disattivazione Relay Coclea
				processo.gestoreReleCoclee(false);

			}

		} else {

			// Registrazione Pesa Componente Completata
			registraPesaComponente(processo.valorePesaCarico);

			///////////////////////////////
			// DOSATURA DIRETTA ATTIVA ///
			///////////////////////////////
			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				// Disattivazione Relay Coclea
				processo.gestoreReleCoclee(false);

			}

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Tempo di Attesa Deposito Previsto per il Volo Materiale = {0}",
				TEMPO_VOLO_MATERIALE);
		TimeLineLogger.logger.log(Level.INFO, "Attesa Volo Materiale = {0}", TEMPO_VOLO_MATERIALE);

		try {
			// Attesa Deposito Volo Materiale
			ThreadProcessoControlloPesoCarico.sleep(TEMPO_VOLO_MATERIALE);
		} catch (InterruptedException ex) {
		}

		// Controllo tolleranza di peso
		ControlloTolleranzediPesoComponentiProcesso(processo.componenti.get(processo.indiceCompInPesa).getQuantità(), // peso_desiderato
				processo.valorePesaCarico, // peso_attuale
				processo.componenti.get(processo.indiceCompInPesa).getTolleranzaDifetto(), // tolleranza_difetto
				processo.componenti.get(processo.indiceCompInPesa).getTolleranzaEccesso(), // tolleranza_eccesso
				processo.componenti.get(processo.indiceCompInPesa).getNome()); // nome_componente

		// Registra Aggiornamento Fine Procedura Pesatura Componente
		AggiornaDatiMovimentoMagazzino(mov_magazzino, Integer.parseInt(processo.pesoRegistratoComponente));

		// Aggiornamento Valore Parametro di Ripristino IdMovimento di Magazzino
		AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(100), 100, "0",
				ParametriSingolaMacchina.parametri.get(15));

		// Interruzione Thread di Lettura Peso Carico
		interrompiThreadLetturaPesoCarico = true;

	}

	// Finalizzazione Procedura Controllo Peso per interruzione Manuale
	public void finalizzaControlloPesoInterruzioneManuale() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.warning("Interruzione Manuale del Processo");

		// Interruzione Thread di Controllo Coclea Vuota
		processo.interrompiThreadControlloCocleaVuota = true;

		interrompiThreadLetturaPesoCarico = true;

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Attesa Delay Contattore Inverter = {0}",
				ParametriGlobali.parametri.get(8));

		// Attesa Delay Contattattore inverter
		try {
			ThreadProcessoControlloPesoCarico.sleep(TEMPO_RITARDO_ATTIVAZIONE_INVERTER);
		} catch (InterruptedException ex) {
		}

		// Disattivazione Relay Coclea
		processo.gestoreReleCoclee(false);

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Attesa Deposito Volo Materiale = {0}",
				ParametriGlobali.parametri.get(9));

		// Attesa Deposito Volo Materiale
		try {
			ThreadProcessoControlloPesoCarico.sleep(Integer.parseInt(ParametriGlobali.parametri.get(9)));
		} catch (InterruptedException ex) {
		}

	}

	// Finalizzazione Procedura Controllo Peso per interruzione Manuale
	public void finalizzaControlloPesoResetProcesso() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.warning("Reset Manuale del Processo - Tasto Reset");

		// Interruzione Thread di Controllo Coclea Vuota
		processo.interrompiThreadControlloCocleaVuota = true;

		// Interruzione Thread di Lettura Peso Carico
		interrompiThreadLetturaPesoCarico = true;

		// Disattivazione Relay Coclea
		processo.gestoreReleCoclee(false);

	}

	// Finalizzazione Procedura Controllo Peso per interruzione Manuale
	public void finalizzaControlloPesoCocleaVuota() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.warning("Interruzione Processo per Coclea Vuota");

		interrompiThreadLetturaPesoCarico = true;

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Attesa Deposito Materiale Volo");

		// Attesa Deposito Volo Materiale
		try {
			ThreadProcessoControlloPesoCarico.sleep(TEMPO_RITARDO_ATTIVAZIONE_INVERTER);
		} catch (InterruptedException ex) {
		}

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			// Disattivazione Relay Coclea
			processo.gestoreReleCoclee(false);

			// Codice Chimica Inserito non Valido
			int scelta = ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
					.visualizzaErrore(27);
			switch (scelta) {
			case 0:
				//////////////////////////////////
				/// RIPROVA PROCEDURA NORMALE ///
				//////////////////////////////////
				// Analisi Stato Processo di Pesa
				processo.analizzaPesaComponenti();
				break;
			case 1:
				/////////////////////////
				/// RIAVVIO INVERTER ///
				/////////////////////////
				new ThreadProcessoResetInverterCoclee(processo).start();
				break;
			case 2:
				//////////////////////////////////
				/// DOSATURA DIRETTA INVERTER ///
				//////////////////////////////////
				new ThreadProcessoAttivaDosaturaDiretta(processo).start();
				break;
			default:
				break;
			}

		}

	}

	// Thread Lettura Peso Carico
	private class ThreadLetturaPesoCarico extends Thread {

		// COSTRUTTORE
		public ThreadLetturaPesoCarico() {

			// Impostazione Nome Thread
			this.setName(this.getClass().getSimpleName());
		}

		@Override
		public void run() {

			processo.cambioGestVelMixEseguito = false;

			while (!interrompiThreadLetturaPesoCarico && processo.pannelloProcesso.isVisible()
					&& !processo.resetProcesso) {

				letturaPesoCarico();

				try {
					ThreadLetturaPesoCarico.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);
				} catch (InterruptedException e) {
				}

			} // fine loop

			if (processo.interruzioneControlloPesoRaggiunto && processo.pannelloProcesso.isVisible()
					&& !processo.resetProcesso) {

				///////////////////////////////////////
				// INTERRUZIONE PER PESO RAGGIUNTO ///
				///////////////////////////////////////
				// Ripetizioni Commutazione Peso Netto
				for (int i = 0; i < RIPETIZIONI_AZZERAMENTO; i++) {

					// Commutazione Peso Netto
					pesaCarico.commutaNetto();

					try {
						ThreadProcessoControlloPesoCarico.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);
					} catch (InterruptedException ex) {
					}

				}

				// Chiusura del client
				pesaCarico.chiudi();

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Incremento Indice Componente in Pesa");

				if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

					// Reinizializzazione Variabile di controllo Processo Ripristinato
					processo.pannelloProcesso.ripristinato = false;

					// Incremento Indice Componente in Pesa
					processo.indiceCompInPesa++;

					// Analisi Stato Processo di Pesa
					processo.analizzaPesaComponenti();

				}
			}

		}
	}

	// Controllo Peso Carico
	public void controlloPesoCarico() {

		// Controllo Peso Raggiunto
		if (pesoMancante <= 0 || processo.forzaPesoComponente) {

			//////////////////////////////////
			/// PESO DESIDERATO RAGGIUNTO ///
			//////////////////////////////////
			// Interruzione del loop corrente
			processo.interruzioneControlloPesoRaggiunto = true;

			processo.forzaPesoComponente = false;

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Peso Desiderato Componente Raggiunto");

		} else {

			/////////////////////////////////////
			/// PESO DESIDERATO NON RAGGIUNTO ///
			/////////////////////////////////////

			// Controllo Attivazione Vibro
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(285))
					&& Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(87).get(processo.indiceCompInPesa))
					&& pesoMancante <= Integer
							.parseInt(TrovaGruppoValoreParametroRipristino(88).get(processo.indiceCompInPesa))
					&& !vibroAttivato) {

				vibroAttivato = true;

				new processoThreadVibroCoclea(processo).start();
			}

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				// Valutazione Dosatura Diretta o Curva di Dosaggio
				if (Double.parseDouble(TrovaGruppoValoreParametroRipristino(19).get(processo.indiceCompInPesa)) < Double
						.parseDouble(TrovaGruppoValoreParametroRipristino(20).get(processo.indiceCompInPesa))
						|| Double.parseDouble(processo.valorePesaCarico) > Integer
								.parseInt(ParametriSingolaMacchina.parametri.get(336))
						|| ((!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(246)) || Double.parseDouble(
								TrovaGruppoValoreParametroRipristino(19).get(processo.indiceCompInPesa)) <= Double
										.parseDouble(TrovaGruppoValoreParametroRipristino(20)
												.get(processo.indiceCompInPesa))))) {

					if (!threadCambioVelInEsecuzione) {

						// gestoreCaGestore Cambio Vel Coclea
						new ThreadGestoreCambioVelocita().start();

					}

				}
			}
		}
	}

	private class ThreadArrestoInverter extends Thread {

		@Override
		public synchronized void start() {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Arresto Inverter");

			// Arresto Inverter
			inverter_screws.avviaArrestaInverter(false);

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Arresto Inverter Eseguito");
		}
	}

	private class processoThreadVibroCoclea extends Thread {

		private final Processo processo;

		public processoThreadVibroCoclea(Processo processo) {
			this.processo = processo;
		}

		@Override
		public void run() {

			// Inizializzazione Variabili di Controllo
			boolean tempoVibroEseguito = false;
			boolean vibroAttivo = false;
			int contatore = 0;
			int contatoreApriChiudi = 0;

			double tempo = Integer.parseInt(ParametriSingolaMacchina.parametri.get(286)) / FRAZIONAMENTO_TEMPI;
			double tempoApriChiudi = Integer.parseInt(ParametriSingolaMacchina.parametri.get(292))
					/ FRAZIONAMENTO_TEMPI;

			processo.interrompiThreadVibro = false;

			while (!processo.interrompiThreadVibro && !tempoVibroEseguito) {

				if (contatore > tempo) {

					// Aggiornamento variabile di Controllo Thread
					tempoVibroEseguito = true;

					// Disattivazione Uscita Vibro
					processo.attivaVibroFunghiSiloCorrente(false);

				} else {

					if (contatoreApriChiudi > tempoApriChiudi) {
						contatoreApriChiudi = 0;

						vibroAttivo = !vibroAttivo;
						// Attivazione Uscita Vibro
						processo.attivaVibroFunghiSiloCorrente(vibroAttivo);

					} else {

						contatoreApriChiudi++;
					}

					/////////////////////////////////
					// TEMPO VIBRO NON TRASCORSO ///
					/////////////////////////////////
					// Attesa Tempo
					try {
						processoThreadVibroCoclea.sleep(FRAZIONAMENTO_TEMPI);
					} catch (InterruptedException ex) {
					}

					// Incremento Contatore
					contatore++;
				}

			}

			// Disattivazione Uscita Vibro
			processo.attivaVibroFunghiSiloCorrente(false);

		}
	}
}
