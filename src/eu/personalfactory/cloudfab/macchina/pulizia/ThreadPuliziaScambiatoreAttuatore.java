/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaScambiatoreAttuatore extends Thread {

    private final Pulizia pulizia;

    public ThreadPuliziaScambiatoreAttuatore(Pulizia pulizia) {

        //Dichiarazione Variabile Pulizia
        this.pulizia = pulizia;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Attiva Scambiatore");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Attivazione Attuatore");

        if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Testo Messagio Utente 
            ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(8);

        } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Testo Messagio Utente 
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(8);

        } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Testo Messagio Utente 
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(8);

        }

        boolean riposizionaScambiatore = false;
        boolean sacchettoBloccato = false;

        while (!riposizionaScambiatore
                && pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            try {
                ThreadPuliziaScambiatoreAttuatore.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP)) {

                riposizionaScambiatore = true;
            } else {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Controllo Blocco Sacchetto Procedura Scambio Aspiratore");

                if (!sacchettoBloccato
                        && pulizia.pannelloPulizia.isVisible()
                        && !pulizia.puliziaInterrottaManualmente) {

                    ////////////////////
                    // BLOCCO SACCO  ///
                    //////////////////// 
                    if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO)) {

                        GestoreIO_ModificaOut(
                                USCITA_LOGICA_EV_BLOCCA_SACCO,
                                OUTPUT_TRUE_CHAR);

                        //Modifica Variabile di Controllo loop
                        sacchettoBloccato = true;

                        //Memorizzazione Log Processo
                        PuliziaLogger.logger.info("Confezione Bloccata e Uscita Blocco Sacco Disabilitata");

                    }
                }

            }
        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Disattivazione Attuatore");

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

            ///////////////////////////
            // PRESENZA ASPIRATORE  ///
            ///////////////////////////
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                /////////////////////////////////
                // ASPIRATORE LINEE SEPARATE  ///
                /////////////////////////////////
                PuliziaLogger.logger.info("Disattivazione Contattore Aspiratore, Linee Separate Aspiratore e Valvola di Carico");

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                         OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR);

            } else {
                PuliziaLogger.logger.info("Disattivazione Scambiatore Aspiratore e Valvola di Carico");

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                        OUTPUT_FALSE_CHAR);

            }
        } else {
            PuliziaLogger.logger.info("Disattivazione Valvola di Carico");

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_FALSE_CHAR);

        }
        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Sblocco Confezione");

        GestoreIO_ModificaOut(
                USCITA_LOGICA_EV_BLOCCA_SACCO,
                OUTPUT_FALSE_CHAR);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Disattivazione Uscita Logica Blocco Sacco Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Confezione Sbloccata");

        if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Testo Messagio Utente 
            ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).impostaValiditaPulsantiPulizia(true);

        } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Testo Messagio Utente 
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaValiditaPulsantiPulizia(true);

        } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Testo Messagio Utente 
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaValiditaPulsantiPulizia(true);

            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).initPannello();
        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Apri Valvola di Carico");

    }
}
