/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloMotoreMiscelatore extends Thread {

	private Controllo controllo;

	// COSTRUTTORE
	public ThreadControlloMotoreMiscelatore(Controllo controllo) {

		// Dichiarazione Variabile Controllo
		this.controllo = controllo;

		// Impostazione Nome del Thread
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run() {

		// Memorizzazione Logger Procedure di Controllo
		ControlloLogger.logger.config("Inizio Thread Controllo Motore Miscelatore");

		controllo.avvio = false;
		controllo.arresto = false;
		controllo.attivaByPass = false;

		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(233))) {

			// Impostazione Val Pulsanti
			controllo.pannelloControllo.elemBut[21].setVisible(true);

		}

		controllo.pannelloControllo.impostaMessaggioEsito(
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
						ParametriSingolaMacchina.parametri.get(111))),
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 489,
						ParametriSingolaMacchina.parametri.get(111))));

		// Abilitazione Pulsante Avvio
		controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

		// Attesa Avvio
		// Inizio loop
		while (!controllo.avvio && controllo.pannelloControllo.isVisible()) {

			// Frequenza del Thread
			try {
				ThreadControlloMotoreMiscelatore.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
			} catch (InterruptedException ex) {
			}

		} // fine loop

		controllo.avvio = false;

		// Impostazione Val Pulsanti
		controllo.pannelloControllo.elemBut[21].setVisible(false);
		controllo.pannelloControllo.elemBut[22].setVisible(false);

		if (controllo.pannelloControllo.isVisible()) {

			GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_TRUE_CHAR);

		}

		if (!controllo.attivaByPass) {

			/////////////////////////
			// BYPASS NON ATTIVO ///
			/////////////////////////
			// Attesa tempo Delay
			try {
				ThreadControlloMotoreMiscelatore.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
			} catch (InterruptedException ex) {
			}

			inverter_mix.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(138));

			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadControlloMotoreMiscelatore.class.getName()).log(Level.SEVERE, null, ex);
			}

			// Avvio Inverter
			inverter_mix.avviaArrestaInverter(true);

		} else {

			/////////////////////
			// BYPASS ATTIVO ///
			/////////////////////

			GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER, OUTPUT_TRUE_CHAR);

		}

		if (controllo.pannelloControllo.isVisible()) {
			// Abilitazione Pulsante Avvio
			controllo.pannelloControllo.impostaAbilitazionePulsanteArresto(true);
		}
		// Attesa arresto
		// Inizio loop
		while (!controllo.arresto && controllo.pannelloControllo.isVisible()) {

			// Frequenza del Thread
			try {
				ThreadControlloMotoreMiscelatore.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
			} catch (InterruptedException ex) {
			}

		} // fine loop

		controllo.arresto = false;

		// Arresto Inverter
		if (controllo.pannelloControllo.isVisible()) {

			

			if (!controllo.attivaByPass) {
			
				/////////////////////////
				// BYPASS NON ATTIVO  ///
				///////////////////////// 
				
				
				// Arresto inverter
				inverter_mix.avviaArrestaInverter(false);
				
				// Attesa tempo Delay
				try {
					ThreadControlloMotoreMiscelatore.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
				} catch (InterruptedException ex) {
				}

			} else {

				/////////////////////
				// BYPASS ATTIVO  ///
				/////////////////////

				GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER, OUTPUT_FALSE_CHAR);

			}
		}

		if (controllo.pannelloControllo.isVisible()) {

			GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_FALSE_CHAR);
		}

		controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);

		// Inizio loop
		while (!controllo.testSuperato && !controllo.testFallito && controllo.pannelloControllo.isVisible()) {

			// Frequenza del Thread
			try {
				ThreadControlloMotoreMiscelatore.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
			} catch (InterruptedException ex) {
			}
		} // fine loop

		if (controllo.testSuperato) {
			controllo.pannelloControllo.impostaMessaggioEsito(
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
							ParametriSingolaMacchina.parametri.get(111))),
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
							ParametriSingolaMacchina.parametri.get(111))) + " " + ParametriGlobali.parametri.get(46)
							+ " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463,
									ParametriSingolaMacchina.parametri.get(111))));

			controllo.esitoTest[7] = true;

			controllo.pannelloControllo.impostaLabelImage(2);

			// Memorizzazione Logger Procedure di Controllo
			ControlloLogger.logger.info("Controllo Motore Miscelatore Superato");

		} else {

			controllo.pannelloControllo.impostaMessaggioEsito(
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
							ParametriSingolaMacchina.parametri.get(111))),
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 468,
							ParametriSingolaMacchina.parametri.get(111))) + " " + ParametriGlobali.parametri.get(46)
							+ " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464,
									ParametriSingolaMacchina.parametri.get(111))));

			controllo.esitoTest[7] = false;

			controllo.pannelloControllo.impostaLabelImage(1);

			// Memorizzazione Logger Procedure di Controllo
			ControlloLogger.logger.warning("Controllo Motore Miscelatore Fallito");

		}

		controllo.testSuperato = false;

		controllo.testFallito = false;

		controllo.attivaByPass = false;

		// Impostazione Validità Pulsanti
		controllo.pannelloControllo.elemBut[21].setVisible(false);
		controllo.pannelloControllo.elemBut[22].setVisible(false);

		GestoreIO_ModificaOut(
				USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER,
				OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR);

		// Impostazione Validità Pulsanti Scelta Test
		controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

		// Impostazione Visibilità Pulsanti Start Stop
		controllo.pannelloControllo.impostaVisibilitaStartStop(false);

		// Memorizzazione Logger Procedure di Controllo
		ControlloLogger.logger.config("Fine Thread Controllo Motore Miscelatore");

	}
}
