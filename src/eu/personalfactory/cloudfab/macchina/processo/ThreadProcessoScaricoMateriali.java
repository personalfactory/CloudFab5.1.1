/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQ_THREAD_CONTROLLO_FINE_CARICO_MATERIE_PRIME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_APERTURA_VALOLA_SCARICO_MATERIE_PRIME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_CHIUSURA_VALOLA_SCARICO_MATERIE_PRIME;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoScaricoMateriali extends Thread {

	private final Processo processo;

	// COSTRUTTORE
	public ThreadProcessoScaricoMateriali(Processo processo) {

		// Dichiarazione Variabile Processo
		this.processo = processo;

		// Impostazione Nome del Thread
		this.setName(this.getClass().getSimpleName());

	}

	@Override
	public void run() {

		boolean pesaturaChimicaDaEseuguire = Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))
				&& Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(461));
		boolean pesaturaCompletata = false;

		// Attesa completamento Pesatura Componenti (SILOS e MANUALE) e chimica
		while (processo.pannelloProcesso.isVisible() && !processo.processoInterrottoManualmente
				&& !processo.resetProcesso && !pesaturaCompletata) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Attesa Completamento Pesatura Materie Prime e Chimica");

			if (pesaturaChimicaDaEseuguire) {

				pesaturaCompletata = Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(102))
						&& Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(55));
			} else {

				pesaturaCompletata = Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(55));
			}
			try {
				ThreadProcessoScaricoMateriali.sleep(FREQ_THREAD_CONTROLLO_FINE_CARICO_MATERIE_PRIME);
			} catch (InterruptedException ex) {
			}
		}

		processo.ignoraControlloMicroSvuotaComponenti = false;
		processo.microdosaturaCompletata = false;

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE LINEE ASPIRAZIONE
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
				&& Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

			GestoreIO_ModificaOut(
					USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
							+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
							+ USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
					OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_TRUE_CHAR);

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(372))) {

				processo.pannelloProcesso.elemBut[14].setVisible(true);

			}

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizio Procedura Scarico Materiali");

		TimeLineLogger.logger.log(Level.INFO, "Avvio Procedura Scarico Materiali  - id_ciclo={0}",
				TrovaSingoloValoreParametroRipristino(91));
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE VIBRO TRAMOGGIA
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(501))) {


			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Attivazione Vibro Pneumatici,Funghi Tramoggia e Aspiratore Linea Tramoggia di Carico");

				GestoreIO_ModificaOut(
						USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO+ OUTPUT_SEP_CHAR
						+USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
						OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR 
						+ OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR 
						+ OUTPUT_TRUE_CHAR);

			} else {
				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Attivazione Vibro Pneumatici e Funghi Tramoggia");

				GestoreIO_ModificaOut(
						USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO,
						OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_TRUE_CHAR);

			}

		} else {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Attivazione Vibro Pneumatici Tramoggia e Aspiratore Linea Tramoggia di Carico");

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) { 
				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
						+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
						OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR 
						+OUTPUT_TRUE_CHAR);
			} else {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Attivazione Vibro Pneumatici Tramoggia");

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO,
						OUTPUT_TRUE_CHAR);
			}
		}
		 
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) { 
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// APERTURE RIPETUTE VALVOLA DI CARICO
		for (int i = 0; i < Integer.parseInt(ParametriGlobali.parametri.get(29)); i++) {

			if (processo.processoInterrottoManualmente) {

				break;
			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Scarico Materiali : Apertura Valvola di Carico");

			// Aggiorna Visibilità Elemento Pannello
			processo.pannelloProcesso.labelImageAux[2].setVisible(true);

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO, OUTPUT_TRUE_CHAR);

				// Attesa Tempo Apertura Valvola
				try {
					ThreadProcessoScaricoMateriali.sleep(TEMPO_APERTURA_VALOLA_SCARICO_MATERIE_PRIME);
				} catch (InterruptedException ex) {
				}

			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Scarico Materiali : Chiusura Valvola di Carico");

			// Aggiorna Visibilità Elemento Pannello
			processo.pannelloProcesso.labelImageAux[2].setVisible(false);

			if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

				GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO, OUTPUT_FALSE_CHAR);

				// Attesa Tempo Chiusura Valvola
				try {
					ThreadProcessoScaricoMateriali.sleep(TEMPO_CHIUSURA_VALOLA_SCARICO_MATERIE_PRIME);
				} catch (InterruptedException ex) {
				}

			}

		}
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) { 
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DISATTIVAZIONE VIBRO TRAMOGGIA
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(501))) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Attivazione Vibro Pneumatici e Funghi Tramoggia");

			GestoreIO_ModificaOut(
					USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
					+ USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO,
					OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR);

		} else {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Attivazione Vibro Pneumatici e Funghi Tramoggia");

			GestoreIO_ModificaOut(USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO,
					OUTPUT_FALSE_CHAR);
		}


		// Attesa Controllo Bilancia
		try {
			ThreadProcessoScaricoMateriali.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(220)));
		} catch (InterruptedException ex) {
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.info("Scarico Materiali Eseguito");

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			if (!processo.processoInterrottoManualmente) {

				new ThreadProcessoControlloBilanciaCaricoFinePesatura(processo).start();
				 
			}
		}
		
		try {
			ThreadProcessoScaricoMateriali.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(429)));
		} catch (InterruptedException ex) {
			Logger.getLogger(ThreadProcessoScaricoMateriali.class.getName()).log(Level.SEVERE, null, ex);
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// DISATTIVAZIONE VIBRO TRAMOGGIA
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Disattivazione Aspiratore Linea Tramoggia di Carico");

			GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO, OUTPUT_FALSE_CHAR);
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Procedura Scarico Materiali");

	}

}
