/*
 zz * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.AZZERAMENTO_BIL_CARICO_VALORE_RIF;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_F;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_G;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_H;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_I;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_J;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_K;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_L;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_STABILIAZZAZIONE_BILANCIA_CARICO_DOPO_APERTURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIPETIZIONI_AZZERAMENTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 *         Esegue una serie di Controlli su Contatti, Impianto Pneumatico e
 *         Bilance
 */
public class ThreadProcessoControlliIniziali extends Thread {

	private final Processo processo;

	// COSTRUTTORE
	public ThreadProcessoControlliIniziali(Processo processo) {

		// Dichiarazione Variabile Processo
		this.processo = processo;

		// Impostazione Nome del Thread
		this.setName(this.getClass().getSimpleName());

	}

	@Override
	public void run() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizio Procedura Controlli Iniziali");

		TimeLineLogger.logger.log(Level.INFO, "Inizio Procedura Controlli Iniziali - id_ciclo ={0}",
				TrovaSingoloValoreParametroRipristino(83));

		// Aggiornamento Stato Variabile di Controllo
		processo.controlliInizialiInCorso = true;

		// Inizializzazione Array
		processo.esitoTestStatoContatti = new ArrayList<>();

		///////////////////////////////////////////
		/// CONTROLLO STATO INIZIALE CONTATTI ////
		///////////////////////////////////////////

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Controllo Stato Iniziale Contatti di Sicurezza");

		// Controllo Stato Iniziale Contatto di Sicurezza Sportello Miscelatore
		processo.esitoTestStatoContatti.add(GestoreIO_OttieniStatoIngresso(
				INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE) == ParametriSingolaMacchina.parametri.get(18)
						.equals("1"));

		// Controllo Stato Iniziale Contatto Valvola di Carico
		processo.esitoTestStatoContatti.add(GestoreIO_OttieniStatoIngresso(
				INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO) == ParametriSingolaMacchina.parametri.get(19).equals("1"));

		// Controllo Stato Iniziale Contatto Valvola di Scarico
		processo.esitoTestStatoContatti.add(GestoreIO_OttieniStatoIngresso(
				INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO) == ParametriSingolaMacchina.parametri.get(20).equals("1"));

		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(447))) {

			//////////////////////////////////////////
			// CONTROLLO CONTATTI COCLEE ABILITATO ///
			//////////////////////////////////////////

			Boolean stato_def_contatto_coclee = Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(502));
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_A) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_B) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_C) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_D) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_E) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_F) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_G) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_H) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_I) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_J) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_K) == stato_def_contatto_coclee);
			processo.esitoTestStatoContatti.add(
					GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_L) == stato_def_contatto_coclee);

			//////////////////////////////////////////
			// CONTROLLO CONTATTI COCLEE ABILITATO ///
			//////////////////////////////////////////

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.INFO, "Controllo Sicurezza Sportello Miscelatore :{0}{1}"
					+ "Controllo Sicurezza Valvola di Carico :{2} {3}"
					+ "Controllo Sicurezza Valvola di Scarico :{4}{5}" + "Controllo Contatto Sicurezza Coclea A :{6}{7}"
					+ "Controllo Contatto Sicurezza Coclea B :{8}{9}"
					+ "Controllo Contatto Sicurezza Coclea C :{10}{11}"
					+ "Controllo Contatto Sicurezza Coclea D :{12}{13}"
					+ "Controllo Contatto Sicurezza Coclea E :{14}{15}"
					+ "Controllo Contatto Sicurezza Coclea F :{16}{17}"
					+ "Controllo Contatto Sicurezza Coclea G :{18}{19}"
					+ "Controllo Contatto Sicurezza Coclea H :{20}{21}"
					+ "Controllo Contatto Sicurezza Coclea I :{22}{23}"
					+ "Controllo Contatto Sicurezza Coclea J :{24}{25}" + "Controllo Contatto Sicurezza Coclea L :{26}",
					new Object[] { processo.esitoTestStatoContatti.get(0), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(1), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(2), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(3), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(4), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(5), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(6), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(7), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(8), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(9), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(10), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(11), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(12), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(13), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(14) });
		} else {

			//////////////////////////////////////////////
			// CONTROLLO CONTATTI COCLEE NON ABILITATO ///
			//////////////////////////////////////////////

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.INFO,
					"Controllo Sicurezza Sportello Miscelatore :{0}{1}Controllo Sicurezza Valvola di Carico :{2} {3}Controllo Sicurezza Valvola di Scarico :{4}",
					new Object[] { processo.esitoTestStatoContatti.get(0), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(1), LOG_CHAR_SEPARATOR,
							processo.esitoTestStatoContatti.get(2) });

		}

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			if ((processo.esitoTestStatoContatti.get(0) && processo.esitoTestStatoContatti.get(1)
					&& processo.esitoTestStatoContatti.get(2)
					&& !Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(447))
					|| processo.esitoTestStatoContatti.get(0) && processo.esitoTestStatoContatti.get(1)
							&& processo.esitoTestStatoContatti.get(2) && processo.esitoTestStatoContatti.get(3)
							&& processo.esitoTestStatoContatti.get(4) && processo.esitoTestStatoContatti.get(5)
							&& processo.esitoTestStatoContatti.get(6) && processo.esitoTestStatoContatti.get(7)
							&& processo.esitoTestStatoContatti.get(8) && processo.esitoTestStatoContatti.get(9)
							&& processo.esitoTestStatoContatti.get(10) && processo.esitoTestStatoContatti.get(11)
							&& processo.esitoTestStatoContatti.get(12) && processo.esitoTestStatoContatti.get(13)
							&& processo.esitoTestStatoContatti.get(14))) {

				/////////////////////////////////////////////
				// TEST STATO INIZIALE CONTATTI SUPERATO ///
				/////////////////////////////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Test Stato Iniziale Contatti Superato");

				////////////////////////////////////////////
				// MICRODOSATORI NON PRENSENTI PRESENTI ///
				////////////////////////////////////////////

				// Avvio Controllo Bilance
				new controlloBilanciaCarico().start();

			} else {

				/////////////////////////////////////////////
				// TEST STATO INIZIALE CONTATTI FALLITO ///
				/////////////////////////////////////////////

				// Memorizzazione Log Processo
				ProcessoLogger.logger.severe("Test Contatti Iniziali Fallito");

				String statoContattiTestFallito = "";

				for (int i = 0; i < processo.esitoTestStatoContatti.size(); i++) {

					statoContattiTestFallito += processo.esitoTestStatoContatti.get(i);

					if (i != processo.esitoTestStatoContatti.size() - 1) {
						statoContattiTestFallito += ",";
					}

				}
				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
					RegistraAllarme(7,
							EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 888,
									ParametriSingolaMacchina.parametri.get(111))) + " =" + statoContattiTestFallito,
							TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
				}

				if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
						.visualizzaErrore(0) == 0) {

					// Memorizzazione Log Processo
					ProcessoLogger.logger.warning("Riattivazione Controlli Iniziali");

					// Riavvio Controlli
					new ThreadProcessoControlliIniziali(processo).start();

				} else {

					// Memorizzazione Log Processo
					ProcessoLogger.logger.warning("Finalizzazione Processo");

					// Finalizzazione Processo
					processo.finalizzazioneProcesso();

				}
			}
		}

	}

	// Test Presenza Materiale Pesa Carico
	public class controlloBilanciaCarico extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Controllo Bilancia di Carico");

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				try {

					// Creazione Client Pesa Carico	
					ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
					
//					ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO,
//							ParametriSingolaMacchina.parametri.get(150),
//							Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

					Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

					// Verifica Connessione Avvenuta
					if (!pesaCarico.verficaConn()) {

						// Memorizzazione Log Processo
						ProcessoLogger.logger.severe("Connessione Bilancia di Carico Fallita");

						//////////////////////////////////////////
						// CONNESSIONE SOCKET BILANCE FALLITA ///
						//////////////////////////////////////////

						if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
								.visualizzaErrore(17) == 0) {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.warning("Finalizzazione Procedura");

							// Finalizzazione Processo
							processo.finalizzazioneProcesso();

						}

					} else {

						///////////////////////////////////////////
						// CONNESSIONE SOCKET BILANCE AVVENUTA ///
						///////////////////////////////////////////
						// Memorizzazione Log Processo
						ProcessoLogger.logger.info("Connessione Bilancia di Carico Eseguita");
						
						
						
						///////////////////////////////////////////////////////////////////////////////////////////////////////////// COMMUTAZIONE PESO NETTO

						for (int i=0; i<10; i++) {
							
							pesaCarico.commutaNetto();
							
							Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

							
							
						}
						
						
						// Controllo Processo Ripristinato
						if (processo.pannelloProcesso.ripristinato && !processo.inControlloBilanciaCarico) {

							////////////////////////////
							// PROCESSO RIPRISTINATO ///
							////////////////////////////

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Controllo Bilance - Processo Ripristinato");

							// Chiusura del Client

							Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

							pesaCarico.chiudi();
							

							Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);


							if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

								// Controllo Bilancia Confezionamento
								controlloBilanciaConfezionamento();
							}

						} else {

							///////////////////////
							// PROCESSO NORMALE ///
							///////////////////////

							// Memorizzazione Log Processo
							ProcessoLogger.logger.info("Controllo Bilance - Processo Normale");

							processo.inControlloBilanciaCarico = true;

							int pesoLordoPesaCarico = 0;
							int counter = 0;

							// Inizio loop
							while (pesoLordoPesaCarico == 0
									&& counter < Integer.parseInt(ParametriSingolaMacchina.parametri.get(170))
									&& processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

								// Lettura del Peso Lordo
								try {

									ThreadProcessoControlliIniziali.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

									pesoLordoPesaCarico = Integer.parseInt(pesaCarico.pesoLordo());

								} catch (NumberFormatException ex) {

									ProcessoLogger.logger.log(Level.SEVERE,
											"Errore Durante la Conversione del Peso - ex ={0}", ex);
								} catch (InterruptedException e) {

								}

								counter++;

							} // fine loop

							// Memorizzazione Log Processo
							ProcessoLogger.logger.log(Level.INFO, "Peso Letto sulla Bilancia di Carico = {0}",
									pesoLordoPesaCarico);

							////////
							try {
								ThreadProcessoControlliIniziali.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);
							} catch (NumberFormatException | InterruptedException ex) {
							}

							// Chiusura del Client
							pesaCarico.chiudi();

							Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

							if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

								// Controllo Eccedenza Peso
								if (Math.abs(pesoLordoPesaCarico) > Math
										.abs(Integer.parseInt(ParametriSingolaMacchina.parametri.get(22)))) {

									/////////////////////////////////////////////////
									// PESO RILEVATO SUPERIORE LIMITE CONSENTITO ///
									/////////////////////////////////////////////////
									// Controllo peso rilevato accettabile
									if (Math.abs(pesoLordoPesaCarico) < Math.abs(Integer
											.parseInt(ParametriSingolaMacchina.parametri.get(22))
											+ Integer.parseInt(ParametriSingolaMacchina.parametri.get(22))
													* Integer.parseInt(ParametriSingolaMacchina.parametri.get(23))
													/ 100)) {

										//////////////////////////////////////////////////////////////////
										// PESO RILEVATO SUPERIORE LIMITE CONSENTITO - MA ACCETTABILE ///
										//////////////////////////////////////////////////////////////////
										// Memorizzazione Log Processo
										ProcessoLogger.logger.log(Level.WARNING,
												"Test Bilancia di Carico - Presenza NON Eccessiva di Materiale sulla Pesa ={0}",
												pesoLordoPesaCarico);

										if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
											RegistraAllarme(9,
													EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,
															889, ParametriSingolaMacchina.parametri.get(111))) + " ="
															+ pesoLordoPesaCarico,
													TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
										}

										int res = ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati
												.get(1)).gestoreErrori.visualizzaErrore(2);

										switch (res) {
										case 0:
											/////////////////////
											// IGNORA ERRORE ///
											/////////////////////

											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Errore Peso Eccessivo Carico Ignorato");
											// Controllo Impianto Pneumatico
											controlloImpiantoPneumatico();
											break;
										case 1:
											////////////////////////
											// RIPETI CONTROLLO ///
											////////////////////////

											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Ripetizione Controlli Iniziali");
											// Ripeti Procedura di Controllo
											new ThreadProcessoControlliIniziali(processo).start();
											break;
										default:
											/////////////////////////////////
											// ANNULLA PROCESSO CORRENTE ///
											/////////////////////////////////

											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Finalizzazione Procedura");
											// Annulla Processo in Corso
											processo.finalizzazioneProcesso();
											break;
										}

									} else {

										///////////////////////////////////////////////////////////////////
										// PESO RILEVATO SUPERIORE LIMITE CONSENTITO - NON ACCETTABILE ///
										///////////////////////////////////////////////////////////////////
										// Memorizzazione Log Processo
										ProcessoLogger.logger.log(Level.WARNING,
												"Test Bilancia di Carico - Presenza Eccessiva di Materiale sulla Pesa ={0}",
												pesoLordoPesaCarico);

										if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
											RegistraAllarme(9,
													EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,
															890, ParametriSingolaMacchina.parametri.get(111))) + " ="
															+ pesoLordoPesaCarico,
													TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
										}

										if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati
												.get(1)).gestoreErrori.visualizzaErrore(3) == 0) {

											////////////////////////
											// RIPETI CONTROLLO ///
											////////////////////////
											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Ripetizione Controlli Iniziali");

											// Ripeti Procedura di Controllo
											new ThreadProcessoControlliIniziali(processo).start();

										} else {

											//////////////////////////////////
											// ANNULLA PROCESSO CONTROLLO ///
											//////////////////////////////////
											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Finalizzazione Procedura");

											// Annulla Processo in Corso
											processo.finalizzazioneProcesso();
										}

									}

								} else {

									///////////////////////////
									// PESO RILEVATO VALIDO ///
									///////////////////////////
									// Memorizzazione Log Processo
									ProcessoLogger.logger.log(Level.INFO,
											"Test Bilancia di Carico Superato - Valore ={0}", pesoLordoPesaCarico);

									// Memorizzazione Peso Lordo Letto
									AggiornaValoreParametriRipristino(processo,
											TrovaIndiceTabellaRipristinoPerIdParRipristino(52), 52,
											Integer.toString(pesoLordoPesaCarico),
											ParametriSingolaMacchina.parametri.get(15));

									// Controllo Impianto Pneumatico
									controlloImpiantoPneumatico();

								}

								processo.inControlloBilanciaCarico = false;

							}

						}

						// Memorizzazione Log Processo
						ProcessoLogger.logger.config("Fine Controllo Bilancia di Carico");

					}
				} catch (NumberFormatException | IOException e) {

					// Memorizzazione Log Processo
					ProcessoLogger.logger.log(Level.SEVERE, "Errore Controllo Bilancia di Carico :{0}", e);

					if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
							.visualizzaErrore(3) == 0) {

						///////////////////////
						// RIPETI CONTROLLO ///
						///////////////////////
						// Memorizzazione Log Processo
						ProcessoLogger.logger.warning("Ripetizione Controlli Iniziali");

						// Ripeti Procedura di Controllo
						new ThreadProcessoControlliIniziali(processo).start();

					} else {

						/////////////////////////////////
						// ANNULLA PROCESSO CONTROLLO ///
						/////////////////////////////////
						// Memorizzazione Log Processo
						ProcessoLogger.logger.warning("Finaliazzazione Procedura");

						// Annulla Processo in Corso
						processo.finalizzazioneProcesso();
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Fine Controllo Bilancia di Carico");

		}
	}

	// Test Materiale Presente Bilancia di Confezionamento
	public void controlloBilanciaConfezionamento() {

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Controllo Bilancia Confezionamento");

			try {
				
				ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);

//				ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI,
//						ParametriSingolaMacchina.parametri.get(150),
//						Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

				Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

				// Verifica Connessione Avvenuta
				if (!pesaConfezioni.verficaConn()) {

					// Memorizzazione Log Processo
					ProcessoLogger.logger.severe("Connessione Bilancia Confezioni Fallita");

					//////////////////////////////////////////
					// CONNESSIONE SOCKET BILANCE FALLITA ///
					//////////////////////////////////////////
					if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
							.visualizzaErrore(17) == 0) {

						// Memorizzazione Log Processo
						ProcessoLogger.logger.config("Finalizzazione Procedura");

						// Finalizzazione Processo
						processo.finalizzazioneProcesso();
					}

				} else {

					//////////////////////////////////////////
					// CONNESSIONE SOCKET BILANCE AVVENUTA ///
					//////////////////////////////////////////
					// Memorizzazione Log Processo
					ProcessoLogger.logger.info("Connessione Bilancia Confezioni Eseguita");

					// Controllo Processo Ripristinato
					if (processo.pannelloProcesso.ripristinato && !processo.inControlloBilanciaConfezioni) {

						////////////////////////////
						// PROCESSO RIPRISTINATO ///
						////////////////////////////
						// Memorizzazione Log Processo
						ProcessoLogger.logger.info("Tipo di Processo :Ripristinato");

						Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

						// Chiusura del Client
						pesaConfezioni.chiudi();

						try {
							ThreadProcessoControlliIniziali
									.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
						} catch (InterruptedException ex) {
						}

						if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {
							// Avvio Processo
							new ThreadProcessoGestoreAvvioProcesso(processo).start();

						}

					} else {

						processo.inControlloBilanciaConfezioni = true;

						// Memorizzazione Log Processo
						ProcessoLogger.logger.info("Tipo di Processo :Normale");

						// Lettura del Peso Lordo
						int pesoLordoPesaConfezioni = 0;
						int counter = 0;

						try {
							ThreadProcessoControlliIniziali
									.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
						} catch (InterruptedException ex) {
						}

						// Inizio Loop
						while (pesoLordoPesaConfezioni == 0
								&& counter < Integer.parseInt(ParametriSingolaMacchina.parametri.get(170))
								&& processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

							Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

							// Lettura del Peso Lordo
							try {
								pesoLordoPesaConfezioni = Integer.parseInt(pesaConfezioni.pesoLordo());
							} catch (NumberFormatException ex) {

								// Memorizzazione log processo
								ProcessoLogger.logger.log(Level.SEVERE,
										"Errore Durante la Conversione del Peso - ex ={0}", ex);
							}

							counter++;

							try {
								ThreadProcessoControlliIniziali
										.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
							} catch (InterruptedException ex) {
							}
						} // fine loop

						Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);

						// Chiusura del Socket
						pesaConfezioni.chiudi();

						try {
							ThreadProcessoControlliIniziali
									.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
						} catch (InterruptedException ex) {
						}

						// Memorizzazione Valore Pesa su Database
						if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

							AggiornaValoreParametriRipristino(processo,
									TrovaIndiceTabellaRipristinoPerIdParRipristino(53), 53,
									Integer.toString(pesoLordoPesaConfezioni),
									ParametriSingolaMacchina.parametri.get(15));

							// Controllo Eccedenza Peso
							if (Math.abs(pesoLordoPesaConfezioni) > Math
									.abs(Integer.parseInt(ParametriSingolaMacchina.parametri.get(24)))) {

								////////////////////////////////////////////////
								// PESO RILEVATO SUPERIORE LIMITE CONSENTITO ///
								////////////////////////////////////////////////
								// Controllo se Accettabile
								if (Math.abs(pesoLordoPesaConfezioni) < Math.abs(Integer
										.parseInt(ParametriSingolaMacchina.parametri.get(24))
										+ Integer.parseInt(ParametriSingolaMacchina.parametri.get(24))
												* Integer.parseInt(ParametriSingolaMacchina.parametri.get(25)) / 100)) {

									/////////////////////////////////////////////////////////////////
									// PESO RILEVATO SUPERIORE LIMITE CONSENTITO - MA ACCETTABILE ///
									/////////////////////////////////////////////////////////////////
									// Memorizzazione Log Processo
									ProcessoLogger.logger.log(Level.WARNING,
											"Presenza non Eccessiva di Materiale sulla Pesa = {0}",
											pesoLordoPesaConfezioni);

									if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
										RegistraAllarme(9,
												EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 891,
														ParametriSingolaMacchina.parametri.get(111))) + " ="
														+ pesoLordoPesaConfezioni,
												TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
									}

									if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

										int res = ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati
												.get(1)).gestoreErrori.visualizzaErrore(4);

										switch (res) {

										case 0:
											////////////////////
											// IGNORA ERRORE ///
											////////////////////

											// Memorizzazione Log Processo
											ProcessoLogger.logger
													.warning("Errore Peso Bilancia di Confezionamento Ignorato");

											// Avvio Processo Ignorando l'Errore
											new ThreadProcessoGestoreAvvioProcesso(processo).start();
											break;

										case 1:
											///////////////////////
											// RIPETI CONTROLLO ///
											///////////////////////

											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Ripetizione Controlli Iniziali");

											// Ripeti Procedura di Controllo
											new ThreadProcessoControlliIniziali(processo).start();
											break;

										default:
											// Memorizzazione Log Processo
											ProcessoLogger.logger.warning("Finalizzazione Procedura");

											//////////////////////////
											// FINALIZZA PROCESSO ///
											//////////////////////////

											// Annulla Processo in Corso
											processo.finalizzazioneProcesso();
											break;
										}

									}

								} else {

									////////////////////////////////////////////////////////////
									// PESO RILEVATO SUPERIORE LIMITE CONSENTITO - ECCESSIVO ///
									////////////////////////////////////////////////////////////
									// Memorizzazione Log Processo
									ProcessoLogger.logger.log(Level.WARNING,
											"Presenza Eccessiva di Materiale sulla Pesa ={0}", pesoLordoPesaConfezioni);

									if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
										RegistraAllarme(9,
												EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 892,
														ParametriSingolaMacchina.parametri.get(111))) + " ="
														+ pesoLordoPesaConfezioni,
												TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
									}

									if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati
											.get(1)).gestoreErrori.visualizzaErrore(5) == 0) {

										///////////////////////
										// RIPETI CONTROLLO ///
										///////////////////////
										// Memorizzazione Log Processo
										ProcessoLogger.logger.warning("Ripetizione Controlli Iniziali");

										// Ripeti Procedura di Controllo
										new ThreadProcessoControlliIniziali(processo).start();

									} else {

										/////////////////////////
										// FINALIZZA PROCESSO ///
										/////////////////////////
										// Memorizzazione Log Processo
										ProcessoLogger.logger.warning("Finalizzazione Procedura");

										// Annulla Processo in Corso
										processo.finalizzazioneProcesso();
									}

								}
							} else {

								////////////////////////////
								// PESO RILEVATO VALIDO ///
								////////////////////////////
								// Memorizzazione Log Processo
								ProcessoLogger.logger.log(Level.INFO, "Test Bilancia Confezioni Superato Valore ={0}",
										pesoLordoPesaConfezioni);

								if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

///OTTOBRE2018/////////////                                        if (Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)) > 0
////////////////                                                && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(370))) {
////////////////
////////////////                                            //////////////////////////////
////////////////                                            // MICRODOSATORI PRESENTI  ///
////////////////                                            //////////////////////////////
////////////////                                            controlloMicrodosatori();
////////////////
////////////////                                        } else {
									//////////////////////////////////
									// MICRODOSATORI NON PRESENTI ///
									/////////////////////////////////
									// Avvio Processo Ignorando l'Errore
									new ThreadProcessoGestoreAvvioProcesso(processo).start();
////////////////                                        }
								}

							}
							processo.inControlloBilanciaConfezioni = false;
						}
					}
				}

			} catch (Exception ex) {
				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.SEVERE, "Errore Creazione Client :{0}", ex);
			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Fine Controllo Bilancia Confezionamento");

		}
	}

	public void controlloImpiantoPneumatico() {

		////////////////////////////////
		// PROCESSO NON RIPRISTINATO ///
		////////////////////////////////
		boolean resultTestAperturaValvola;

		//////////////////////////////////////
		// TEST APERTURA VALVOLA DI CARICO ///
		//////////////////////////////////////

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Test Impianto Pneumatico - Apertura Valvola di Carico");

		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO, OUTPUT_TRUE_CHAR);

		try {
			ThreadProcessoControlliIniziali.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(21)));
		} catch (InterruptedException ex) {
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Test Impianto Pneumatico - Chiusura Valvola di Carico");

		resultTestAperturaValvola = GestoreIO_OttieniStatoIngresso(
				INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO) != ParametriSingolaMacchina.parametri.get(19).equals("1");

		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO, OUTPUT_FALSE_CHAR);

		try {
			ThreadProcessoControlliIniziali.sleep(TEMPO_STABILIAZZAZIONE_BILANCIA_CARICO_DOPO_APERTURA);
		} catch (InterruptedException ex) {
		}

		try {
			// Azzzeramento Bilancia
			
			// Creazione Client Pesa Carico
			ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
			
//			ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO, ParametriSingolaMacchina.parametri.get(150),
//					Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

			boolean pesoAzzerato = false;
			int counterPeso = 0;
			


			ThreadProcessoControlliIniziali.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);
			Integer valore_peso_netto = 0;

			// Verifica Azzeramento
			if (!pesaCarico.pesoNetto().equals("")) {
				valore_peso_netto = Integer.parseInt(pesaCarico.pesoNetto());

			}
			
			ThreadProcessoControlliIniziali.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

			if (!(valore_peso_netto < AZZERAMENTO_BIL_CARICO_VALORE_RIF)
					|| !(valore_peso_netto > (AZZERAMENTO_BIL_CARICO_VALORE_RIF) * -1)) {

				while (counterPeso < RIPETIZIONI_AZZERAMENTO && !pesoAzzerato) {

					try {

						ThreadProcessoControlliIniziali.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

						// Azzeramento Modulo di Pesa
						pesaCarico.commutaNetto();

						if (Integer.parseInt(pesaCarico.pesoNetto()) < AZZERAMENTO_BIL_CARICO_VALORE_RIF
								&& Integer.parseInt(pesaCarico.pesoNetto()) > AZZERAMENTO_BIL_CARICO_VALORE_RIF * -1) {
							pesoAzzerato = true;

						} else {
							counterPeso++;
							ProcessoLogger.logger.log(Level.INFO, "Contatore Ripetizioni Azzeramento={0}", counterPeso);
						}
					} catch (NumberFormatException e) {
						ProcessoLogger.logger.log(Level.WARNING, "Errore Durante l''azzeramento={0}", e);
					}

				}
			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.INFO,
					"Test Impianto Pneumatico - Peso Letto Dopo La chiusura della Valvola Dopo Azzeramento ={0}",
					pesaCarico.pesoNetto());

			ThreadProcessoControlliIniziali.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

			pesaCarico.chiudi();

		} catch (IOException | InterruptedException ex) {
			ProcessoLogger.logger.log(Level.SEVERE, "Errore durante i controlli Iniziali{0}", ex);
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Test Impianto Pneumatico Eseguito");

		// Analizza Stato Contatto Valvola di Carico
		if (resultTestAperturaValvola || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(334))) {

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(334))) {

				/////////////////////////////////////
				// CONTROLLO IMPIANTO DISABILITATO //
				////////////////////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Test Impianto Pneumatico Disabilitato");

				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
					RegistraAllarme(10,
							EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 893,
									ParametriSingolaMacchina.parametri.get(111))),
							TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
				}
			} else {
				/////////////////////
				// TEST SUPERATO ///
				/////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Test Impianto Pneumatico Superato");
			}

			// Controllo Bilancia di Confezionamento
			controlloBilanciaConfezionamento();

		} else {

			////////////////////
			// TEST FALLITO ///
			////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.severe("Test Impianto Pneumatico Fallito");
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
				RegistraAllarme(10,
						EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 894,
								ParametriSingolaMacchina.parametri.get(111))),
						TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
			}

			if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori
					.visualizzaErrore(1) == 0) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.warning("Riattivazione Controlli Iniziali");

				// Riavvio Controlli
				new ThreadProcessoControlliIniziali(processo).start();

			} else {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.warning("Finalizzazione Processo");

				// Finalizzazione Processo
				processo.finalizzazioneProcesso();

			}
		}

	}

}
