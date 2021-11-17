/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;

/**
 *
 * @author francescodigaudio
 *
 *
 */
public class ThreadPuliziaMiscelazione extends Thread {

    private final Pulizia pulizia;

    //COSTRUTTORE
    public ThreadPuliziaMiscelazione(Pulizia pulizia) {

        //Dichiarazione Variabile Processo
        this.pulizia = pulizia;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Miscelazione");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Attivazione Relè Motore Miscelatore");

        if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Visibiltà Messaggio Utente
            ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(4);

        } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Visibiltà Messaggio Utente
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(4);

        } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Visibiltà Messaggio Utente
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(4);

        }

        if (pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN,
                    OUTPUT_TRUE_CHAR);
        }

        //Inizializzazione Vel Miscelatore
        if (pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Inizializzazione Vel Miscelatore");

            //Cambio Vel 
            inverter_mix.cambiaVelInverter(pulizia.dettagliPulizia.getVelMix());

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.INFO, "Inizializzazione Vel Miscelatore Eseguita - Valore ={0}", ParametriSingolaMacchina.parametri.get(134));

        }

        //Avvio Inverter Motore Miscelatore
        if (pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            try {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Attesa Tempo Delay Inverter");

                //Attesa Delay Inverter
                try {
                    ThreadPuliziaMiscelazione.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
                } catch (InterruptedException ex) {
                }

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Attesa Tempo Delay Inverter Eseguita");

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Avvio Inverter Motore Miscelatore");

                //Avvio inverter 
                inverter_mix.avviaArrestaInverter(true);

                //Memorizzazione Log Processo
                PuliziaLogger.logger.info("Avvio Inverter Motore Miscelatore Eseguito");

            } catch (NumberFormatException ex) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.severe("Errore Durante l'Avvio Motore Miscelatore");
            }
        }

        //Attesa Tempo Miscelazione
        int timeToWait = pulizia.dettagliPulizia.getTempoMix() / 1000;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.INFO, "Attesa Tempo Miscelazione ={0}", timeToWait);

        if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Visibilità Label Tempo Residuo
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(true);

        } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Visibilità Label Tempo Residuo
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(true);

        }

        while (timeToWait > 0
                && pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

                //Modifica Valore Label Tempo Residuo
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaLabelTempoResiduo(timeToWait);

            } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

                //Modifica Valore Label Tempo Residuo
                ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaLabelTempoResiduo(timeToWait);

            }

            try {

                ThreadPuliziaMiscelazione.sleep(1000);
            } catch (InterruptedException ex) {
            }

            timeToWait--;
        }

        if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Visibilità Label Tempo Residuo
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(false);

        } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Visibilità Label Tempo Residuo
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(false);

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Attesa Tempo Miscelazione Eseguita");

        //Arresto Inverter Motore Miscelatore
        if (pulizia.pannelloPulizia.isVisible()) {

            try {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Arresto Inverter Miscelatore");

                //Avvio inverter
                inverter_mix.avviaArrestaInverter(false);

                //Memorizzazione Log Processo
                PuliziaLogger.logger.info("Arresto Inverter Miscelatore Eseguito");

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Attesa Tempo Delay Inverter");

                //Attesa Delay Inverter
                try {
                    ThreadPuliziaMiscelazione.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
                } catch (InterruptedException ex) {
                }

                //Memorizzazione Log Processo
                PuliziaLogger.logger.log(Level.CONFIG, "Attesa Tempo Delay Inverter Eseguita - Valore ={0}", Integer.parseInt(ParametriGlobali.parametri.get(8)));

            } catch (NumberFormatException ex) {
            }
        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Disattivazione Relè Motore Miscelatore");

        if (pulizia.pannelloPulizia.isVisible()) {

            GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN,
                    OUTPUT_FALSE_CHAR);

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Disattivazione Relè Motore Miscelatore Eseguita");

            pulizia.svuotamentoPulizia = true;

            if (!pulizia.puliziaInterrottaManualmente) {

                //Avvio Thread Controllo Blocco Sacchetto
                new ThreadPuliziaControlloBloccoSacchetto(pulizia).start();
            }

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Miscelazione");

    }
}
