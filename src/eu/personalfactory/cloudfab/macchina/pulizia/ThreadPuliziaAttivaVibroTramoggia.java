/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaAttivaVibroTramoggia extends Thread {

    private Pulizia pulizia;

    public ThreadPuliziaAttivaVibroTramoggia(Pulizia pulizia) {

        //Dichiarazione Variabile Pulizia
        this.pulizia = pulizia;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Apri Valvola di Carico");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Apertura Valvola di Carico");
 
        if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(440))) {

        	GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
        			OUTPUT_TRUE_CHAR );

        } else {

        	GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR +
        			USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO, 
        			OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR 
        			+ OUTPUT_TRUE_CHAR);
        }
 
        //Attesa Tempo Apertura Valvola 
        int timeToWait = Integer.parseInt(ParametriGlobali.parametri.get(10)) / 1000;

        //Imposta Visibilità Label Tempo Residuo
        ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(true);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.CONFIG, "Attesa Tempo Apertura Valvola ={0}", timeToWait);

        while (timeToWait > 0 && pulizia.pannelloPulizia.isVisible()) {

            //Modifica Valore Label Tempo Residuo
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaLabelTempoResiduo(timeToWait);

            try {
                ThreadPuliziaAttivaVibroTramoggia.sleep(1000);
            } catch (InterruptedException ex) {
            }

            timeToWait--;

        }
        //Imposta Visibilità Label Tempo Residuo
        ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(false);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Chiusura Valvola di Carico");

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(440))) {
  
        	GestoreIO_ModificaOut(
        			USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO, 
        			OUTPUT_FALSE_CHAR);
        }

        try {
            ThreadPuliziaAttivaVibroTramoggia.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(429)));
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadPuliziaAttivaVibroTramoggia.class.getName()).log(Level.SEVERE, null, ex);
        }

        GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                OUTPUT_FALSE_CHAR);

        ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaValiditaPulsantiPulizia(true);

        ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).initPannello();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Apri Valvola di Carico");

    }
}
