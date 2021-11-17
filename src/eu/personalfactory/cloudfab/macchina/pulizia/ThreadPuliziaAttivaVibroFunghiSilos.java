/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A;

import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaAttivaVibroFunghiSilos extends Thread {

    private final Pulizia pulizia;

    public ThreadPuliziaAttivaVibroFunghiSilos(Pulizia pulizia) {

        //Dichiarazione Variabile Pulizia
        this.pulizia = pulizia;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        
		// Memorizzazione Log Processo
		PuliziaLogger.logger.info("Attivazione Vibro Silo");

		// Impostazione Pulante Vinro
		pulizia.pannelloPulizia.elemBut[16].setEnabled(false);

		//CALCOLO ID USCITA
		String id = Integer.toString(((pulizia.dettagliPulizia.getIdPresa()-1) * 3)
				+ Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A));
 
		// ATTIVAZIONE VIBRO FUNGHI SILOS
		GestoreIO_ModificaOut(id, OUTPUT_TRUE_CHAR);

		try {
			ThreadPuliziaAttivaVibroFunghiSilos.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(423)));
		} catch (InterruptedException ex) {
		}
		// DISATTIVAZIONE VIBRO FUNGHI SILOS
		GestoreIO_ModificaOut(id, OUTPUT_FALSE_CHAR);
		// Reimpostazione Pulante Vibro
		pulizia.pannelloPulizia.elemBut[16].setEnabled(true);

		// Memorizzazione Log Processo
		PuliziaLogger.logger.info("Disattivazione Vibro Silo");

	}
}
