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
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_APERTURA_VALOLA_SCARICO_MATERIE_PRIME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_CHIUSURA_VALOLA_SCARICO_MATERIE_PRIME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;

import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaScaricoMateriali extends Thread {

    private final Pulizia pulizia;

    public ThreadPuliziaScaricoMateriali(Pulizia pulizia) {

        //Dichiarazione Variabile Processo
        this.pulizia = pulizia;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Scarico Materiali");

        if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Aggiornamento Label "Peso Carico"
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).aggiornaLabelPesaCarico("0");

        }

        for (int i = 0; i < Integer.parseInt(ParametriGlobali.parametri.get(29)); i++) {

            if (!pulizia.pannelloPulizia.isVisible()
                    && pulizia.puliziaInterrottaManualmente) {
                break;
            }

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Apertura Valvola di Carico");

            if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

                //Impostazione Label Messaggio Utente
                ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(5);

            } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

                //Impostazione Label Messaggio Utente
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(5);

            } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

                //Impostazione Label Messaggio Utente
                ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(5);

            }

            if (!pulizia.puliziaInterrottaManualmente) {

            	if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(440))) {
            		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
            				OUTPUT_TRUE_CHAR);
            	} else {
            		
            		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO + OUTPUT_SEP_CHAR 
            				+ USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO,
            				OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR 
            				+ OUTPUT_TRUE_CHAR);
            		
            	}
            }
            //Attesa Tempo Apertura
            int timeToWait = TEMPO_APERTURA_VALOLA_SCARICO_MATERIE_PRIME / 1000;

            if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

                //Impostazione Visibilità Label Tempo Residuo
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(true);

            } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

                //Impostazione Label Messaggio Utente
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

                    ThreadPuliziaScaricoMateriali.sleep(1000);
                } catch (InterruptedException ex) {
                }

                timeToWait--;

            }

            if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

                //Impostazione Visibilità Label Tempo Residuo
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(false);

                //Impostazione Label Messaggio Utente
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(6);

            } else if (pulizia.pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

                //Impostazione Visibilità Label Tempo Residuo
                ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).impostaVisLabelTempoResiduo(false);

                //Impostazione Label Messaggio Utente
                ((Pannello40_Pulizia_Manuale) pulizia.pannelloPulizia).modificaTestoMessaggioUtente(6);

            }

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Chiusura Valvola di Carico");
 
            if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(440))) {
        		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
        				OUTPUT_FALSE_CHAR);
        	} else {
        		
        		GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO + OUTPUT_SEP_CHAR 
        				+ USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO,
        				OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR 
        				+ OUTPUT_FALSE_CHAR);
        		
        	}

            //Attesa Tempo Chisura
            timeToWait = TEMPO_CHIUSURA_VALOLA_SCARICO_MATERIE_PRIME / 1000;

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

                    ThreadPuliziaScaricoMateriali.sleep(1000);
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

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Scarico Materiali Eseguito");

        if (!pulizia.puliziaInterrottaManualmente) {

            //Avvio Miscelazione 
            new ThreadPuliziaMiscelazione(pulizia).start();

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Scarico Materiali");

    }
}
