/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPreseAbilitatePerLaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A; 
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloMotoriCoclee extends Thread {

	private final Controllo controllo;

	// COSTRUTTORE
	public ThreadControlloMotoriCoclee(Controllo controllo) {

		// Dichiarazione Variabile Controllo
		this.controllo = controllo;

		// Impostazione Nome del Thread
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run() {

		// Memorizzazione Logger Procedure di Controllo
		ControlloLogger.logger.config("Inizio Thread Controllo Motori Coclee");

		controllo.idPresa = 0;
		controllo.idUscita = 0;
		controllo.avvio = false;
		controllo.arresto = false;
		controllo.attivaByPass = false;
		controllo.avvioAttuatoreValvolaAspiratoreSilo= false;
		controllo.arrestoAttuatoreValvolaAspiratoreSilo= false;;

		int numPreseMacchina = TrovaPreseAbilitatePerLaMacchina().size();

		if (controllo.pannelloControllo.isVisible()) {

			// Arresta Inverter
			inverter_screws.avviaArrestaInverter(false);

		}

		// Impostazione Validità Pulsanti
		controllo.pannelloControllo.elemBut[21].setVisible(true);

		boolean esitoTestCoclee[] = new boolean[numPreseMacchina];

		controllo.pannelloControllo.impostaMessaggioEsito(
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 466,
						ParametriSingolaMacchina.parametri.get(111))),
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 489,
						ParametriSingolaMacchina.parametri.get(111))));

		// Inizio loop
		// while (idPresa != Integer.parseInt(ParametriSingolaMacchina.parametri.get(2))
		while (controllo.idPresa != numPreseMacchina && controllo.pannelloControllo.isVisible()) {

			// Impostazione Visibilità Pulsante Vibro
			controllo.pannelloControllo.impostaVisibilitaPulsantiVibro(true);

			controllo.pannelloControllo.elemBut[25].setEnabled(true);
			controllo.pannelloControllo.elemBut[26].setEnabled(false);

			controllo.pannelloControllo.elemBut[33].setEnabled(true);
			controllo.pannelloControllo.elemBut[34].setEnabled(false);
			
			controllo.pannelloControllo.elemBut[39].setVisible(true);
			controllo.pannelloControllo.elemBut[40].setVisible(true);
			controllo.pannelloControllo.elemBut[39].setEnabled(true);
			controllo.pannelloControllo.elemBut[40].setEnabled(false);

			controllo.aggiornaIdPresa();

			// Abilitazione Pulsante Avvio
			controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

			// Impostazione Visibilita Pulsanti Salta Prese
			controllo.pannelloControllo.impostaVisibilitaPulsantiSaltaPrese(true);

			// Attesa Avvio
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inizio
			// loop
			while (!controllo.avvio && controllo.pannelloControllo.isVisible()) {

				// Frequenza del Thread
				try {
					ThreadControlloMotoriCoclee.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
				} catch (InterruptedException ex) {
				}

				if (controllo.avvioSoffiatori) {

					controllo.avvioSoffiatori = false;

					Integer out = Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A)
							+ ((controllo.idPresa) * 3);
					 
					GestoreIO_ModificaOut(Integer.toString(out), OUTPUT_TRUE_CHAR);

					ControlloLogger.logger.info("Avvio Soffiatori Pneumatici Coclea");
					
					
					System.out.println("Avvio Soffiatori Pneumatici Coclea - uscita logica" + out);

				} else if (controllo.arrestoSoffiatori) {

					controllo.arrestoSoffiatori = false;
					Integer out = Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A)
							+ ((controllo.idPresa) * 3); 
					
					GestoreIO_ModificaOut(Integer.toString(out), OUTPUT_FALSE_CHAR);

					ControlloLogger.logger.info("Arresto Soffiatori Pneumatici Coclea");

				} else if (controllo.avvioVibroPneumatici) {

					controllo.avvioVibroPneumatici = false;

					Integer out = Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A)
							+ ((controllo.idPresa) * 3);
					GestoreIO_ModificaOut(Integer.toString(out), OUTPUT_TRUE_CHAR);

					ControlloLogger.logger.info("Avvio Vibratori Pneumatici Coclea");

				} else if (controllo.arrestoVibroPneumatici) {

					controllo.arrestoVibroPneumatici = false;

					Integer out = Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A)
							+ ((controllo.idPresa) * 3);
					GestoreIO_ModificaOut(Integer.toString(out), OUTPUT_FALSE_CHAR);

					ControlloLogger.logger.info("Arresto Vibratori Pneumatici Coclea");

				} else if (controllo.avvioAttuatoreValvolaAspiratoreSilo) {

					controllo.avvioAttuatoreValvolaAspiratoreSilo = false;

					Integer out = Integer.parseInt(USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A)
							+ ((controllo.idPresa) * 3);
					GestoreIO_ModificaOut(Integer.toString(out), OUTPUT_TRUE_CHAR);

					ControlloLogger.logger.info("Avvio Attuatre Aspiratore Silo");

				} else if (controllo.arrestoAttuatoreValvolaAspiratoreSilo) {

					controllo.arrestoAttuatoreValvolaAspiratoreSilo = false;

					Integer out = Integer.parseInt(USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A)
							+ ((controllo.idPresa) * 3);
					GestoreIO_ModificaOut(Integer.toString(out), OUTPUT_FALSE_CHAR);

					ControlloLogger.logger.info("Arresto Attuatre Aspiratore Silo");
				}

			} ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// fine
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// loop

			// Impostazione Validità Pulsanti
			controllo.pannelloControllo.elemBut[21].setVisible(false);
			controllo.pannelloControllo.elemBut[22].setVisible(false);

			controllo.pannelloControllo.impostaVisibilitaPulsantiSaltaPrese(false);

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Uscita
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inverter
			if (controllo.pannelloControllo.isVisible()) {

				GestoreIO_ModificaOut(Integer.toString(controllo.idUscita), OUTPUT_TRUE_CHAR);

				if (!controllo.attivaByPass) {
					// Attesa tempo Delay
					try {
						ThreadControlloMotoriCoclee.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
					} catch (InterruptedException ex) {
					}
				}
			}

			controllo.avvio = false;

			if (controllo.pannelloControllo.isVisible()) {

				if (!controllo.attivaByPass) {

					////////////////////////
					// BYPASS NON ATTIVO ///
					////////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Cambio
					//////////////////////// Vel e Avvio Inverter
					// Impostazione Vel di Dosatura Diretta Inverter
					inverter_screws.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(137));

					// Avvio inverter
					inverter_screws.avviaArrestaInverter(true);

				} else {

					/////////////////////
					// BYPASS ATTIVO ///
					/////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione
					///////////////////// Linea Bypass
					GestoreIO_ModificaOut(USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER, OUTPUT_TRUE_CHAR);

				}

			}

			// Abilitazione Pulsante Avvio
			controllo.pannelloControllo.impostaAbilitazionePulsanteArresto(true);

			// Attesa arresto
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inizio
			// loop
			while (!controllo.arresto && controllo.pannelloControllo.isVisible()) {

				// Frequenza del Thread
				try {
					ThreadControlloMotoriCoclee.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
				} catch (InterruptedException ex) {
				}

			} ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// fine
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// loop

			controllo.arresto = false;

			if (controllo.pannelloControllo.isVisible()) {

				if (!controllo.attivaByPass) {

					/////////////////////////
					// BYPASS NON ATTIVO ///
					/////////////////////////

					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Arresto
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inverter
					inverter_screws.avviaArrestaInverter(false);

				} else {

					/////////////////////
					// BYPASS ATTIVO ///
					/////////////////////
					///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Disattivazione
					///////////////////// Split Linea Diretta
					GestoreIO_ModificaOut(USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER, OUTPUT_FALSE_CHAR);

				}

			}
			if (!controllo.attivaByPass) {
				// Attesa tempo Delay
				try {
					ThreadControlloMotoriCoclee.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
				} catch (InterruptedException ex) {
				}
			}

			if (controllo.pannelloControllo.isVisible()) {

				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Disattivazine
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Contattore
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Motore
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Coclea
				GestoreIO_ModificaOut(Integer.toString(controllo.idUscita), OUTPUT_FALSE_CHAR);

				controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);

			}
			// Inizio loop
			while (!controllo.testSuperato && !controllo.testFallito && controllo.pannelloControllo.isVisible()) {

				// Frequenza del Thread
				try {
					ThreadControlloMotoriCoclee.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
				} catch (InterruptedException ex) {
				}
			} // fine loop

			if (controllo.testSuperato) {

				esitoTestCoclee[controllo.idPresa] = true;

				// Memorizzazione Logger Procedure di Controllo
				ControlloLogger.logger.log(Level.INFO, "Controllo Coclea {0} Superato", controllo.idPresa);

			} else {
				esitoTestCoclee[controllo.idPresa] = false;

				// Memorizzazione Logger Procedure di Controllo
				ControlloLogger.logger.log(Level.WARNING, "Controllo Coclea {0} Fallito", controllo.idPresa);
			}

			controllo.testSuperato = false;
			controllo.testFallito = false;

			if (controllo.attivaByPass) {

				/////////////////////
				// BYPASS ATTIVO ///
				/////////////////////
				controllo.pannelloControllo.elemBut[21].setVisible(false);
				controllo.pannelloControllo.elemBut[22].setVisible(true);

			} else {

				/////////////////////////
				// BYPASS NON ATTIVO ///
				/////////////////////////
				controllo.pannelloControllo.elemBut[21].setVisible(true);
				controllo.pannelloControllo.elemBut[22].setVisible(false);
			}

			controllo.idPresa++;

		} // fine loop

		// Impostazione Label Presa/Timer
		controllo.pannelloControllo.impostaLabelPresaTimer("");

		controllo.pannelloControllo.impostaVisibilitaStartStop(false);

		boolean esitoTest = true;
		for (int i = 0; i < esitoTestCoclee.length; i++) {

			if (!esitoTestCoclee[i]) {
				esitoTest = false;
				break;
			}
		}
		if (esitoTest) {

			controllo.pannelloControllo.impostaMessaggioEsito(
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
							ParametriSingolaMacchina.parametri.get(111))),
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 466,
							ParametriSingolaMacchina.parametri.get(111))) + " " + ParametriGlobali.parametri.get(46)
							+ " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463,
									ParametriSingolaMacchina.parametri.get(111))));

			// Memorizzazione Logger Procedure di Controllo
			ControlloLogger.logger.info("Controllo Coclee Superato");

			controllo.esitoTest[6] = true;

			controllo.pannelloControllo.impostaLabelImage(2);

		} else {

			controllo.pannelloControllo.impostaMessaggioEsito(
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
							ParametriSingolaMacchina.parametri.get(111))),
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 466,
							ParametriSingolaMacchina.parametri.get(111))) + " " + ParametriGlobali.parametri.get(46)
							+ " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464,
									ParametriSingolaMacchina.parametri.get(111))));

			// Memorizzazione Logger Procedure di Controllo
			ControlloLogger.logger.warning("Controllo Coclee Fallito");

			controllo.esitoTest[6] = false;

			controllo.pannelloControllo.impostaLabelImage(1);
		}

		// Impostazione Validità Pulsanti
		controllo.pannelloControllo.elemBut[21].setVisible(false);
		controllo.pannelloControllo.elemBut[22].setVisible(false);


		controllo.pannelloControllo.elemBut[39].setVisible(false);
		controllo.pannelloControllo.elemBut[40].setVisible(false);
		controllo.attivaByPass = false;

		// Impostazione Validità Pulsanti Scelta Test
		controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

		GestoreIO_ModificaOut(USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER, OUTPUT_FALSE_CHAR);

		// Impostazione Visbilità Pulsanti Controllo Test
		controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(false);

		// Memorizzazione Logger Procedure di Controllo
		ControlloLogger.logger.config("Fine Thread Controllo Motori Coclee");

	}

}
