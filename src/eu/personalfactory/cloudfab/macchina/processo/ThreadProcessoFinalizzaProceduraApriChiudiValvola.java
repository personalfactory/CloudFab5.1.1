/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
//finalizzaApriChiudiValvola
public class ThreadProcessoFinalizzaProceduraApriChiudiValvola extends Thread {

	private Processo processo;

	// COSTRUTTORE
	public ThreadProcessoFinalizzaProceduraApriChiudiValvola(Processo processo) {
		this.processo = processo;

		// Dichiarazione Varibale Processo
		this.processo = processo;

		// Impostazione Nome Thread Corrente
		this.setName(this.getClass().getSimpleName());

	}

	@Override
	public void run() {

		processo.interrompiThreadControlloBloccoScarico = true;

		// Reset Variabile di Controllo
		processo.attivaVelNormaleMiscelatore = false;

		// Impostazione Visibilità Pulsante
		processo.pannelloProcesso.elemBut[3].setVisible(false);
		processo.pannelloProcesso.elemLabelSimple[28].setVisible(false);

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Chiusura Valvola di Scarico");


		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Disattivazione Uscita Motore Miscelatore");

			// Chiusura Valvola
			 if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

	            	///////////////////////////
	            	// ATTUATORE MULTISTADIO //
	            	///////////////////////////

	            	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
	            	ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
	            } else {

	            	////////////////////
	            	// COMANDO UNICO  //
	            	////////////////////

	            	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
	            	ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");

	            }

			try {
				Thread.sleep(200);
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadProcessoFinalizzaProceduraApriChiudiValvola.class.getName()).log(Level.SEVERE,
						null, ex);
			}

			GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_FALSE_CHAR);

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Disattivazione Uscita Motore Miscelatore eseguita");

		}

		// Interruzione Animazione Miscelatore
		processo.interrompiAnimMiscelatore = true;

		if (processo.pannelloProcesso.isVisible() && !processo.resetProcesso) {

			//////////////////////////////
			// ATTESA SVUOTAMENTO TUBO ///
			//////////////////////////////
			
			// Aspetta Tempo di Chiusura
			int tempoSvuotamento = processo.tempoSvuotamentoTubo[processo.indiceConfezioneInPesa]; // -
																									// tempo_svuotamento_aria;
			int numeroRipetizioniSvuotamento = tempoSvuotamento / FRAZIONAMENTO_TEMPI;
			boolean interrompiTimerSvuotamento = false;
			int counterSvuotamento = 0;

			if (tempoSvuotamento > 0) {
				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Attesa Tempo Svuotamento = {0}", tempoSvuotamento);

				// Inizio loop
				while (!interrompiTimerSvuotamento && processo.pannelloProcesso.isVisible()
						&& !processo.resetProcesso) {

					counterSvuotamento++;

					if (counterSvuotamento >= numeroRipetizioniSvuotamento) {

						interrompiTimerSvuotamento = true;

					} else {

						try {
							ThreadProcessoFinalizzaProceduraApriChiudiValvola.sleep(FRAZIONAMENTO_TEMPI);
						} catch (InterruptedException ex) {
						}
					}

				} // fine loop
			}
		}

		// Controllo tipologia impianto
		if (!processo.abilitaLineaDirettaMiscelatore
				|| processo.materialeDosaturaSoloVibro[processo.indiceConfezioneInPesa] == 0) {

			// Arresto Inverter Motore
			inverter_mix.avviaArrestaInverter(false);

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Procedura Controllo Peso Confezioni");

		processo.threadApriChiudiValvolaAvviato = false;

		// Aggiornamento Peso Reale
		processo.interrompiAggiornamentoPesoRealeConfezione = true;
	}
}
