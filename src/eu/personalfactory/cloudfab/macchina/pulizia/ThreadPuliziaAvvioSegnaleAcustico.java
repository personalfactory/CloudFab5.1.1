/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaAvvioSegnaleAcustico extends Thread {

    private final Pulizia pulizia;

    public ThreadPuliziaAvvioSegnaleAcustico(Pulizia pulizia) {

        //Dichiarazione Variabile Pulizia
        this.pulizia = pulizia;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Segnalazione Acustico Luminosa");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Avvio  Segnalazione Acustica");

        //Aggiornamento Messaggio Operatore
        pulizia.pannelloPulizia.elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1001, ParametriSingolaMacchina.parametri.get(111)));

        GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
                OUTPUT_TRUE_CHAR);

        //Attesa Tempo Segnalazione Acustica
        int timeToWait = Integer.parseInt(ParametriGlobali.parametri.get(7)) / 1000;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.INFO, "Attesa Durata Segnalazione Acustica = {0}", timeToWait);

        while (timeToWait > 0 && pulizia.pannelloPulizia.isVisible()) {

            try {

                ThreadPuliziaAvvioSegnaleAcustico.sleep(1000);
            } catch (InterruptedException ex) {
            }

            timeToWait--;

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Arresto Segnalazione Acustica");

        GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
                OUTPUT_FALSE_CHAR);

        //Procedura di Controllo Stato Processo di Pesatura Componenti
        pulizia.analizzaPesaComponenti();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Segnalazione Acustico Luminosa");

    }
}
