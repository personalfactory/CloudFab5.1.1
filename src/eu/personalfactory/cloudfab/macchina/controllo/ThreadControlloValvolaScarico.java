/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA;

import java.util.GregorianCalendar;
import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloValvolaScarico extends Thread {

	private Controllo controllo;
	private Integer contatoreAperture;

	// COSTRUTTORE
	public ThreadControlloValvolaScarico(Controllo controllo) {

		// Dichiarazione Variabile Controllo
		this.controllo = controllo;

		// Impostazione Nome del Thread
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public void run() {

		controllo.avvio = false;
		controllo.arresto = false;
		contatoreAperture = 0;

		// Memorizzazione Logger Procedure di Controllo
		ControlloLogger.logger.config("Inizio Controllo Valvola di Scarico");

		controllo.pannelloControllo.impostaMessaggioEsito(
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 474,
						ParametriSingolaMacchina.parametri.get(111))),
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 487,
						ParametriSingolaMacchina.parametri.get(111))));

		controllo.pannelloControllo.elemLabelSimple[30].setText("NUMERO APERTURE");
		controllo.pannelloControllo.elemLabelSimple[31].setText("0");
		controllo.pannelloControllo.elemLabelSimple[32].setText("TEMPO MEDIO (ms)");
		controllo.pannelloControllo.elemLabelSimple[33].setText("0");

		controllo.pannelloControllo.elemLabelSimple[30].setVisible(true);
		controllo.pannelloControllo.elemLabelSimple[31].setVisible(true);
		controllo.pannelloControllo.elemLabelSimple[32].setVisible(true);
		controllo.pannelloControllo.elemLabelSimple[33].setVisible(true);

		//Chiusura Valvola
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

			////////////////////////////
			// ATTUATORE MULTISTADIO  //
			////////////////////////////
			controllo.pannelloControllo.elemBut[35].setVisible(true);
			controllo.pannelloControllo.elemBut[36].setVisible(false);
			controllo.pannelloControllo.elemBut[37].setVisible(false);
			controllo.pannelloControllo.elemBut[38].setVisible(false);
			ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_DEF");
		} else {

			////////////////////
			// COMANDO UNICO  //
			////////////////////
			controllo.pannelloControllo.elemBut[35].setVisible(false);
			controllo.pannelloControllo.elemBut[36].setVisible(false);
			controllo.pannelloControllo.elemBut[37].setVisible(true);
			controllo.pannelloControllo.elemBut[38].setVisible(false);
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO); 
			ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");


		}
		controllo.pannelloControllo.elemBut[35].setEnabled(true);
		controllo.pannelloControllo.elemBut[36].setEnabled(true);
		controllo.pannelloControllo.elemBut[37].setEnabled(true);
		controllo.pannelloControllo.elemBut[38].setEnabled(true);

		// Abilitazione Pulsante Avvio
		controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

		// Attesa Avvio
		// Inizio loop
		while (!controllo.avvio && controllo.pannelloControllo.isVisible()) {

			// Frequenza del Thread
			try {
				ThreadControlloValvolaScarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
			} catch (InterruptedException ex) {
			}

		} // fine loop

		controllo.avvio = false;

		controllo.pannelloControllo.elemBut[35].setVisible(false);
		controllo.pannelloControllo.elemBut[36].setVisible(false);
		controllo.pannelloControllo.elemBut[37].setVisible(false);
		controllo.pannelloControllo.elemBut[38].setVisible(false);

		// Abilitazione Pulsante Avvio
		controllo.pannelloControllo.impostaAbilitazionePulsanteArresto(true);

		// Avvio Thread Controllo Ingressi Sensore Valvola
		new ControlloStatoContattoValvola().start();

		// Pulsanti apertura manuale valvola
		controllo.pannelloControllo.elemBut[35].setVisible(false);
		controllo.pannelloControllo.elemBut[36].setVisible(false);
		controllo.pannelloControllo.elemBut[37].setVisible(false);
		controllo.pannelloControllo.elemBut[38].setVisible(false);

		controllo.pannelloControllo.elemBut[35].setEnabled(false);
		controllo.pannelloControllo.elemBut[36].setEnabled(false);
		controllo.pannelloControllo.elemBut[37].setEnabled(false);
		controllo.pannelloControllo.elemBut[38].setEnabled(false);

		try {

			
			//Chiusura Valvola
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				////////////////////////////
				// ATTUATORE MULTISTADIO  //
				////////////////////////////
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
				ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_DEF");
			} else {

				////////////////////
				// COMANDO UNICO  //
				////////////////////
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO); 
				ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");


			}

			Thread.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
			controllo.pannelloControllo.elemBut[38].setEnabled(true);

		} catch (InterruptedException ex) {
		}
		// Inizio loop
		while (!controllo.arresto && controllo.pannelloControllo.isVisible()) {

			// Frequenza del Thread
			try {
				
			 
				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

					
					controllo.pannelloControllo.elemBut[38].setVisible(false);
					controllo.pannelloControllo.elemBut[35].setVisible(true);
					GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_20);
					ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
					ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_20");

					controllo.pannelloControllo.elemBut[35].setVisible(false);
					controllo.pannelloControllo.elemBut[36].setVisible(true);
					GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_53);
					ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
					ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_53");

					controllo.pannelloControllo.elemBut[36].setVisible(false);
					controllo.pannelloControllo.elemBut[37].setVisible(true);
					GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);
					ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
					ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_100");

					controllo.pannelloControllo.elemBut[37].setVisible(false);
					controllo.pannelloControllo.elemBut[38].setVisible(true);
					GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
					ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
					ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_0");

				} else {

					controllo.pannelloControllo.elemBut[37].setVisible(false);
					controllo.pannelloControllo.elemBut[38].setVisible(true);
					GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
					ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
					ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");

					controllo.pannelloControllo.elemBut[37].setVisible(true);
					controllo.pannelloControllo.elemBut[38].setVisible(false);
					GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);
					ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
					ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_100_COMANDO_UNICO");

				}

				// System.out.println(contatoreAperture);
				ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola ={0}", contatoreAperture);

				contatoreAperture += 1;

				controllo.pannelloControllo.elemLabelSimple[31].setText(Integer.toString(contatoreAperture));

			} catch (InterruptedException e) {
			}

		} // fine loop

		controllo.arresto = false;

		try {
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
				
				controllo.pannelloControllo.elemBut[38].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
				Thread.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
				ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_0DEF");
				
			} else {

				controllo.pannelloControllo.elemBut[38].setVisible(false);
				controllo.pannelloControllo.elemBut[35].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
				ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
				ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");

			}

		} catch (InterruptedException ex) {
		}

		// Visibilità Pulsanti Controllo esito test
		controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);

		// Visibilità Comandi manuli
		controllo.pannelloControllo.elemBut[35].setVisible(false);
		controllo.pannelloControllo.elemBut[36].setVisible(false);
		controllo.pannelloControllo.elemBut[37].setVisible(false);
		controllo.pannelloControllo.elemBut[38].setVisible(false);

		// Inizio loop
		while (!controllo.testSuperato && !controllo.testFallito && controllo.pannelloControllo.isVisible()) {

			// Frequenza del Thread
			try {
				ThreadControlloValvolaScarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
			} catch (InterruptedException ex) {
			}
		} // fine loop

		if (controllo.testSuperato) {
			controllo.pannelloControllo.impostaMessaggioEsito(
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 474,
							ParametriSingolaMacchina.parametri.get(111))),
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 474,
							ParametriSingolaMacchina.parametri.get(111))) + " " + ParametriGlobali.parametri.get(46)
							+ " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463,
									ParametriSingolaMacchina.parametri.get(111))));

			controllo.esitoTest[16] = true;

			controllo.pannelloControllo.impostaLabelImage(2);

			// Memorizzazione Logger Procedure di Controllo
			ControlloLogger.logger.info("Controllo Valvola di Scarico Superato");

		} else {

			controllo.pannelloControllo.impostaMessaggioEsito(
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 474,
							ParametriSingolaMacchina.parametri.get(111))),
					EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 474,
							ParametriSingolaMacchina.parametri.get(111))) + " " + ParametriGlobali.parametri.get(46)
							+ " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464,
									ParametriSingolaMacchina.parametri.get(111))));

			controllo.esitoTest[16] = false;

			controllo.pannelloControllo.impostaLabelImage(1);

			// Memorizzazione Logger Procedure di Controllo
			ControlloLogger.logger.warning("Controllo Valvola di Scarico Fallito");

		}

		controllo.testSuperato = false;
		controllo.testFallito = false;

		controllo.pannelloControllo.elemLabelSimple[30].setVisible(false);
		controllo.pannelloControllo.elemLabelSimple[31].setVisible(false);
		controllo.pannelloControllo.elemLabelSimple[32].setVisible(false);
		controllo.pannelloControllo.elemLabelSimple[33].setVisible(false);

		// Pulsanti apertura manuale valvola
		controllo.pannelloControllo.elemBut[35].setVisible(false);
		controllo.pannelloControllo.elemBut[36].setVisible(false);
		controllo.pannelloControllo.elemBut[37].setVisible(false);
		controllo.pannelloControllo.elemBut[38].setVisible(false);

		// Impostazione Val Pulsanti Scelta Test
		controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

		controllo.pannelloControllo.impostaVisibilitaStartStop(false);
		
		//Chiusura Valvola
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

			////////////////////////////
			// ATTUATORE MULTISTADIO  //
			////////////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
			ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_DEF");
		} else {

			////////////////////
			// COMANDO UNICO  //
			////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO); 
			ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");


		}

		// Memorizzazione Logger Procedure di Controllo
		ControlloLogger.logger.config("Fine Controllo Valvola di Scarico");
	}

	private class ControlloStatoContattoValvola extends Thread {

		@Override
		public void run() {

			boolean valvolaAperta = false;
			long start = new GregorianCalendar().getTimeInMillis();
			long stop = new GregorianCalendar().getTimeInMillis();
			long precedente = 0;
			long media = 0;
			long differenziale = 0;
			// Inizio loop
			while (!controllo.arresto && controllo.pannelloControllo.isVisible()) {

				// Frequenza del Thread
				try {

					// Frequenza del Thread
					ControlloStatoContattoValvola.sleep(2);

					if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO)) {

						if (!valvolaAperta) {

							ControlloLogger.logger.log(Level.INFO, "Sensore Valvola - Apertura {0}", contatoreAperture);

							valvolaAperta = true;

							// Registrazione Istante Apertura
							start = new GregorianCalendar().getTimeInMillis();

						}

					} else if (!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO)) {

						if (valvolaAperta) {

							ControlloLogger.logger.log(Level.INFO, "Sensore Valvola - Chiusura {0}", contatoreAperture);

							valvolaAperta = false;

							// Registrazione Istante Chiusura
							stop = new GregorianCalendar().getTimeInMillis();

							differenziale = stop - start;

							if (precedente == 0) {
								precedente = differenziale;
								media = precedente;
							} else {
								media = (differenziale + precedente) / 2;
								precedente = differenziale;

							}
							controllo.pannelloControllo.elemLabelSimple[33].setText(Long.toString(media));

							ControlloLogger.logger.log(Level.INFO, "Differenziale ={0} Media ={1}",
									new Object[] { differenziale, media });
						}
					}

				} catch (InterruptedException e) {
				}

			} // fine loop

		}
	}
}
