/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.microdosatore.Microdosatore_Inverter_2017;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PRESA_MICRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_NUMERO_RIP_TENTATIVI_CONFIG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_QUANTITA_DIFF_TOTALE_RICHIESTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.IGNORA_CONTROLLI_INIZIALI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoGestoreMicrodosaturaConfigurazione extends Thread {

	private final Processo processo;
	
	
	// COSTRUTTORE
	public ThreadProcessoGestoreMicrodosaturaConfigurazione(Processo processo) {
		
 
		// Dichiarazione Varibale Processo
		this.processo = processo;

		// Impostazione Nome Thread Corrente
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run() {
		

		processo.microdosatori_2017 = new ArrayList<>();

		@SuppressWarnings("unused")
		int counterEsitoTestMicro = 0;
		boolean interrompiThreadConfigurazione = false;

		processo.counterConfigMicro = 0;

		processo.pannelloProcesso.elemLabelSimple[38].setVisible(true);

		for (int i = 0; i < processo.numeroMateriePrime; i++) {

			if (interrompiThreadConfigurazione) {

				break;

			} else {

				if (processo.componenti.get(i).getPresa().charAt(0) == MICRO_CHAR_PRESA_MICRO.charAt(0)) {

					if (!Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(i))) {

						int q_pesata = 0;

						///////////////////////////////////////////////////
						// COMPONENTE DA MICRODOSARE NON ANCORA PESATO ///
						///////////////////////////////////////////////////

						///////////////////////////////////
						// MICRODOSATORE SERIE 1 -2017 ///
						///////////////////////////////////
						processo.microdosatori_2017.add(new Microdosatore_Inverter_2017(processo.componenti.get(i)
								.getPresa().replace(processo.componenti.get(i).getPresa().substring(0, 1), "")));
						processo.microdosatori_2017.get(processo.counterConfigMicro)
								.setIdComponente(processo.componenti.get(i).getId());
						processo.microdosatori_2017.get(processo.counterConfigMicro)
								.setNomeComponente(processo.componenti.get(i).getNome());
						processo.microdosatori_2017.get(processo.counterConfigMicro)
								.setPresaComponente(processo.componenti.get(i).getPresa());
						processo.microdosatori_2017.get(processo.counterConfigMicro).setIdComponenteInPeso(i);
						processo.microdosatori_2017.get(processo.counterConfigMicro)
								.setQuantitaRichiesta(processo.componenti.get(i).getQuantità());
						processo.microdosatori_2017.get(processo.counterConfigMicro)
								.setTolleranzaInEccesso(processo.componenti.get(i).getTolleranzaEccesso());
						processo.microdosatori_2017.get(processo.counterConfigMicro)
								.setTolleranzaInDifetto(processo.componenti.get(i).getTolleranzaEccesso());
						processo.microdosatori_2017.get(processo.counterConfigMicro).setStatoMicrodosatura("0");
						processo.pannelloProcesso.elemLabelSimple[38].setText("CONFIG MICRO "
								+ processo.microdosatori_2017.get(processo.counterConfigMicro).getPresaComponente());

						// Memorizzazione Log Processo
						ProcessoLogger.logger.log(Level.INFO,
								"{0}Microdosatore = {1}{2}" + "Nome Componente = {3}{4}" + "Id Componente = {5}{6}"
										+ "Quantita Componente = {7}{8}" + "Volo Componente = {9}{10}"
										+ "Curva Dosatura = {11}",
								new Object[] { LOG_CHAR_SEPARATOR, processo.componenti.get(i).getPresa(),
										LOG_CHAR_SEPARATOR,
										processo.microdosatori_2017.get(processo.counterConfigMicro)
												.getNomeComponente(),
										LOG_CHAR_SEPARATOR,
										processo.microdosatori_2017.get(processo.counterConfigMicro).getIdComponente(),
										LOG_CHAR_SEPARATOR, processo.componenti.get(i).getQuantità(),
										LOG_CHAR_SEPARATOR, processo.componenti.get(i).getVolo(), LOG_CHAR_SEPARATOR,
										processo.componenti.get(i).getCurvaDosatura() });

						// Inizializzazione Dispositivo
						processo.microdosatori_2017.get(processo.counterConfigMicro).inizializzaDispositivo();

						try {
							ThreadProcessoGestoreMicrodosaturaConfigurazione.sleep(100);
						} catch (InterruptedException ex) {
							Logger.getLogger(ThreadProcessoGestoreMicrodosaturaConfigurazione.class.getName())
									.log(Level.SEVERE, null, ex);
						}

						////////////////////////////////////////////
						// CONTROLLO INIZIALE PESO MICRODSATORE ///
						////////////////////////////////////////////
						String peso_microdosatore = processo.microdosatori_2017.get(processo.counterConfigMicro)
								.leggiPeso();

						if (peso_microdosatore.equals("")) {

							/////////////////////////////////
							// TEST MICRODOSATRE FALLITO ///
							/////////////////////////////////
							// Memorizzazione Log Processo
							ProcessoLogger.logger.severe("Test Microdosatore Fallito");

							interrompiThreadConfigurazione = true;

							if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
								RegistraAllarme(11,
										EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 895,
												ParametriSingolaMacchina.parametri.get(111))),
										TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
							}

							if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
									.visualizzaErrore(37) == 0) {

								// Memorizzazione Log Processo
								ProcessoLogger.logger.warning("Riattivazione Controlli Iniziali");

								// Riavvio Verifica
								new ThreadProcessoGestoreMicrodosaturaConfigurazione(processo).start();

								break;

							} else {

								// Memorizzazione Log Processo
								ProcessoLogger.logger.warning("Finalizzazione Processo");

								// Finalizzazione Processo
								processo.finalizzazioneProcesso();

								// Scambio Pannello
								processo.pannelloProcesso.gestoreScambioPannello();

							}

						} else {

							///////////////////////////////////
							// Registrazione Peso Iniziale ///
							///////////////////////////////////

							int peso_iniziale = 0;

							try {

								Integer.parseInt(
										TrovaGruppoValoreParametroRipristino(71).get(processo.counterConfigMicro));

								if (peso_iniziale != 0) {
									if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(459))) {

										// PRESENZA DEL RELE RESET MICRODOSATORI
										// Registra Pesa Componente Eseguita
										AggiornaValoreParametriRipristino(processo,
												TrovaIndiceTabellaRipristinoPerIdParRipristino(71)
														+ processo.counterConfigMicro,
												71, peso_microdosatore, ParametriSingolaMacchina.parametri.get(15));
									}
								}
							} catch (NumberFormatException e) {
							}

							// Q_pesata
							q_pesata = peso_iniziale - (int) Double.parseDouble(peso_microdosatore);

							//////////////////////////////////
							// TEST MICRODOSATRE SUPERATO ///
							//////////////////////////////////
							int qRichiesta = processo.microdosatori_2017.get(processo.counterConfigMicro)
									.getQuantitaRichiesta() // Quantita da pesare componente
									* ((Integer.parseInt(TrovaSingoloValoreParametroRipristino(8)) // Miscele da pesare
											- Integer.parseInt(TrovaSingoloValoreParametroRipristino(62)))); // Miscele
																												// pesate

							if (peso_microdosatore.length() > 1) {

								int qRilevata = (int) Double
										.parseDouble(peso_microdosatore.substring(1, peso_microdosatore.length()));

								processo.pannelloProcesso.elemLabelSimple[38]
										.setText(
												"CONFIG MICRO "
														+ processo.microdosatori_2017.get(processo.counterConfigMicro)
																.getPresaComponente()
														+ " - " + qRichiesta + " / " + qRilevata);

								if ((qRilevata > (qRichiesta + MICRO_QUANTITA_DIFF_TOTALE_RICHIESTO))
										|| !processo.controlloInizialeMicro) {

									///////////////////////////////////////
									// TEST QTA MICRODOSATORI SUPERATO ///
									///////////////////////////////////////
									// Memorizzazione log
									counterEsitoTestMicro++;

								} else {

									int result = ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati
											.get(1)).gestoreErrori.visualizzaErrore(38);

									if (result == 1) {

										////////////////////////
										// ANNULLA PROCESSO ///
										////////////////////////
										// Finalizzazione Processo
										processo.finalizzazioneProcesso();

										// Memorizzazione Log Processo
										ProcessoLogger.logger.warning("Finalizzazione Processo");

										// Scambio Pannello
										processo.pannelloProcesso.gestoreScambioPannello();

										break;
									} else {

										//////////////
										// RIPROVA ///
										//////////////
										// Memorizzazione Log Processo
										ProcessoLogger.logger.warning(
												"Ignora segnalazione errore materiale non sufficiente microdosatori");

										// Avvio Processo
										new ThreadProcessoGestoreMicrodosaturaConfigurazione(processo).start();

										break;

									}

								}

							}
						}

						int counterRip = 0;
						if (interrompiThreadConfigurazione) {
							// Configurazione Dispositivo
							while (!processo.microdosatori_2017.get(processo.counterConfigMicro).configuraMicro(
									(processo.componenti.get(i).getQuantità() - q_pesata),
									processo.componenti.get(i).getVolo(),
									processo.componenti.get(i).getQuantitàStopMescolatore(),
									processo.componenti.get(i).getCurvaDosatura(),
									processo.componenti.get(i).getQuantitàStopVibro())
									&& processo.pannelloProcesso.isVisible()) {

								if (counterRip > MICRO_NUMERO_RIP_TENTATIVI_CONFIG) {

									// Finalizzazione Processo
									processo.finalizzazioneProcesso();

									// Memorizzazione Log Processo
									ProcessoLogger.logger.warning("Finalizzazione Processo");

									// Scambio Pannello
									processo.pannelloProcesso.gestoreScambioPannello();
								}
								try {
									ThreadProcessoGestoreMicrodosaturaConfigurazione.sleep(100);
								} catch (InterruptedException ex) {
									Logger.getLogger(ThreadProcessoGestoreMicrodosaturaConfigurazione.class.getName())
											.log(Level.SEVERE, null, ex);
								}

								// Memorizzazione Log Processo
								ProcessoLogger.logger.log(Level.INFO, "Tentativo di Configurazione Dispositivo {0}",
										new Object[] { processo.componenti.get(i).getPresa() });

								counterRip++;

							}

							String peso_init = "";

							counterRip = 0;
							while (peso_init.equals("")) {

								peso_init = processo.microdosatori_2017.get(processo.counterConfigMicro).leggiPeso();

								try {
									ThreadProcessoGestoreMicrodosaturaConfigurazione.sleep(100);
								} catch (InterruptedException ex) {
									Logger.getLogger(ThreadProcessoGestoreMicrodosaturaConfigurazione.class.getName())
											.log(Level.SEVERE, null, ex);
								}
								if (counterRip > MICRO_NUMERO_RIP_TENTATIVI_CONFIG) {

									// Finalizzazione Processo
									processo.finalizzazioneProcesso();

									// Memorizzazione Log Processo
									ProcessoLogger.logger.warning("Finalizzazione Processo");

									// Scambio Pannello
									processo.pannelloProcesso.gestoreScambioPannello();

								}

								counterRip++;
							}

							////////////////////////////////////////
							// CONTROLLO STATO CONTATTO SPORTELLO //
							////////////////////////////////////////
							if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(456))) {

								if (processo.microdosatori_2017.get(processo.counterConfigMicro).isStatoContatto()) {

									/////////////////////////////////////////////////////////
									// CONTATTO CHIUSURA MISCELATORE IN POSIZIONE ERRATA ///
									/////////////////////////////////////////////////////////
									// Regitrazione allarme
									if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
										RegistraAllarme(13,
												EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1003,
														ParametriSingolaMacchina.parametri.get(111)))
														+ processo.microdosatori_2017.get(processo.counterConfigMicro)
																.getPresaComponente(),
												TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
									}

									if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati
											.get(1)).gestoreErrori.visualizzaErrore(42) == 1) {

										// ANNULLA PROCESSO
										processo.finalizzazioneProcesso();
									}
								}

							}

							if (!peso_init.equals("") && peso_init.length() > 2) {

								processo.microdosatori_2017.get(processo.counterConfigMicro).setPesoIniziale(
										Integer.parseInt(peso_init.substring(1, peso_init.length()) + q_pesata));

								// Incremento idCiclo
								AggiornaValoreParametriRipristino(processo,
										TrovaIndiceTabellaRipristinoPerIdParRipristino(71)
												+ processo.counterConfigMicro,
										71, Integer.toString(processo.microdosatori_2017
												.get(processo.counterConfigMicro).getPesoIniziale()),
										ParametriSingolaMacchina.parametri.get(15));

							}
							processo.counterConfigMicro++;
						}

					} else {

						//////////////////////////////////////////////
						// COMPONENTE DA MICRODOSATRE GIA' PESATO ///
						//////////////////////////////////////////////
						counterEsitoTestMicro++;

					}
				}
			}
		}

		if (!interrompiThreadConfigurazione) {

			processo.pannelloProcesso.elemLabelSimple[38].setVisible(false);

			// Inizializzazione Parametri Microdosatura
			processo.indiceMicroVisibile = 0;
			processo.microdosaturaCompletata = false;
		}
		 
		if (Integer.parseInt(TrovaSingoloValoreParametroRipristino(54))==0) {
			 
			///////////////////////////////
			// AVVIO PROCEDURA STANDARD ///
			///////////////////////////////
			
			if (IGNORA_CONTROLLI_INIZIALI) {
				
				// Avvio Processo ignorando i controlli iniziali
				new ThreadProcessoGestoreAvvioProcesso(processo).start();
				
			} else {
			
				// Avvio Controlli Iniziali
				new ThreadProcessoControlliIniziali(processo).start();
			}
			
		} else if (processo.pannelloProcesso.ripristinato||processo.eseguitoResetManuale) {
				
				/////////////////////////////////
				// AVVIO PROCEDURA RIPRISTINO ///
				/////////////////////////////////
			
				// Avvio Processo ignorando i controlli iniziali
				new ThreadProcessoGestoreAvvioProcesso(processo).start();
			      
		}  
		

	}

}
