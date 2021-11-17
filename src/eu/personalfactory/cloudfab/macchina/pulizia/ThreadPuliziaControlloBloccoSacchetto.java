/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO;

import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 *
 */
public class ThreadPuliziaControlloBloccoSacchetto extends Thread {

    private final Pulizia pulizia;

    public ThreadPuliziaControlloBloccoSacchetto(Pulizia pulizia) {

        //Dichiarazione Variabile Pulizia
        this.pulizia = pulizia;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Controllo Blocco Sacchetto");

        boolean sacchettoBloccato = false;

        if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Testo Messagio Utente 
            ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(0);

        } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Testo Messagio Utente 
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(0);

        } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Testo Messagio Utente 
            ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(0);

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Avvio Procedura controllo Blocca Sacchetto");

        //Inizio loop
        while (!sacchettoBloccato
                && pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            try {
                ThreadPuliziaControlloBloccoSacchetto.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(213)));
            } catch (InterruptedException ex) {
            }

            if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO)) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO,
                        OUTPUT_TRUE_CHAR);

                //Modifica Variabile di Controllo loop
                sacchettoBloccato = true;

            }

        }//fine loop

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Confezione Bloccata e Uscita Blocco Sacco Disabilitata");

        boolean sacchettoSbloccato = false;

        if (pulizia.bloccoSacchettoSingolo) {

            if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

                //Impostazione Testo Messagio Utente 
                ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(1);

            } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

                //Impostazione Testo Messagio Utente 
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(1);

            } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

                //Impostazione Testo Messagio Utente 
                ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(1);

            }

            ////////////////////////
            // BLOCCO SACCHETTO  ///
            ////////////////////////
            while (!sacchettoSbloccato
                    && pulizia.pannelloPulizia.isVisible()
                    && !pulizia.puliziaInterrottaManualmente) {

                try {
                    ThreadPuliziaControlloBloccoSacchetto.sleep(
                            Integer.parseInt(ParametriSingolaMacchina.parametri.get(186)));
                } catch (InterruptedException ex) {
                }

                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP)) {

                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.info("Confezione Sbloccata");

                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO,
                            OUTPUT_FALSE_CHAR);

                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.config("Disattivazione Uscita Logica Blocco Sacco Eseguita");

                    //Modifica Variabile di Controllo
                    sacchettoSbloccato = true;

                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.info("Confezione Sbloccata");
                }

            }

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                    && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                        OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR);

            }

            // Finalizzazione e Chiusura Metodi Pulizia
            pulizia.finalizzaPulizia();

        } else {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.info("Avvio Pesatura Confezioni");

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Attivazione Motore Miscelatore, Valvola di Scarico Vibro Valvola e Vibro Base");

            if (!pulizia.puliziaInterrottaManualmente) {

                if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {
                    //Impostazione Label Messaggio Utente
                    ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(7);
                } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {
                    //Impostazione Label Messaggio Utente
                    ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(7);

                } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {
                    //Impostazione Label Messaggio Utente
                    ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(7);
                }

                GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN,
                        OUTPUT_TRUE_CHAR);

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Attesa Tempo Delay Inverter");

                //Attesa Tempo Delay Inverter
                try {
                    ThreadPuliziaControlloBloccoSacchetto.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
                } catch (InterruptedException ex) {
                }

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Avvio Inverter Motore Miscelatore");

                if (pulizia.svuotamentoPulizia) {

                    pulizia.svuotamentoPulizia = false;

                    //Variazione Vel
                    inverter_mix.cambiaVelInverter(VerificaLunghezzaStringa(
                            Integer.parseInt(ParametriSingolaMacchina.parametri.get(133)), 4));

                }

                inverter_mix.avviaArrestaInverter(true);

                //Memorizzazione Log Processo
                PuliziaLogger.logger.info("Avvio Inverter Motore Miscelatore Eseguito");

            }

            if (!pulizia.puliziaInterrottaManualmente) {
 
                //Apertura Valvola
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
                	///////////////////////////
                	// ATTUATORE MULTISTADIO //
                	///////////////////////////
                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);  
                	PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");
                } else {
                	////////////////////
                	// COMANDO UNICO  //
                	////////////////////
                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);  
                	PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_100_COMANDO_UNICO");
                }
                 
                try {
                    ThreadPuliziaControlloBloccoSacchetto.sleep(200);
                } catch (InterruptedException ex) {
                }

                //Attivazione Vibratore Valvola di Scarico
                GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO,
                        OUTPUT_TRUE_CHAR);

                //Avvio Thread Controllo Peso
                new ThreadPuliziaControlloPesoConfezioni(pulizia).start();

            }

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Controllo Blocco Sacchetto");

    }
}
