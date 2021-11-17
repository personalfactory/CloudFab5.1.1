/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_RIBALTA_SACCO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaRibaltaSacco extends Thread {

    private final Pulizia pulizia; 

    public ThreadPuliziaRibaltaSacco(Pulizia pulizia) {

        //Dichiarazione Variabile Pulizia
        this.pulizia = pulizia;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Ribalta Sacco");

         GestoreIO_ModificaOut( 
                USCITA_LOGICA_EV_RIBALTA_SACCO,
                OUTPUT_TRUE_CHAR);

        //Attesa Tempo Segnalazione Acustica
        int timeToWait = Integer.parseInt(ParametriGlobali.parametri.get(94)) / 1000;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.CONFIG, "Inizio Attesa Tempo Ribalta - Valore ={0}", timeToWait);


        while (timeToWait > 0 && pulizia.pannelloPulizia.isVisible()) {


            try {

                ThreadPuliziaRibaltaSacco.sleep(1000);
            } catch (InterruptedException ex) {
            }

            timeToWait--;

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Attesa Tempo Ribalta Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Riposizionamento Sacco");

        GestoreIO_ModificaOut( 
                USCITA_LOGICA_EV_RIBALTA_SACCO,
                OUTPUT_FALSE_CHAR);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Comando Riposizionamento Sacco Inviato");

        pulizia.finalizzaPulizia();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Ribalta Sacco");

    }
}
