/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INTERVALLO_CONTROLLO_SCAMBIO_LINEE_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloAspiratore extends Thread {

    private Controllo controllo;

    public ThreadControlloAspiratore(Controllo controllo) {

        //Dichiarazione Variabile Controllo
        this.controllo = controllo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        controllo.avvio = false;
        controllo.arresto = false;

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Aspiratore");

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1049, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 796, ParametriSingolaMacchina.parametri.get(111))));

        //Abilitazione Pulsante Avvio
        controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

        //Imposta Visibilità Pulsanti 
        controllo.pannelloControllo.elemLabelSimple[0].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[1].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[24].setForeground(controllo.pannelloControllo.elemColor[2]);

        while (!controllo.avvio && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloAspiratore.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione Linea Aspiratore
        GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE,
                OUTPUT_TRUE_CHAR);

        controllo.pannelloControllo.elemLabelSimple[34].setVisible(true);
        controllo.pannelloControllo.elemLabelSimple[34].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1050, ParametriSingolaMacchina.parametri.get(111)));
        //Abilitazione Pulsante Avvio
        controllo.pannelloControllo.impostaAbilitazionePulsanteArresto(true);

        //Attesa arresto
        //inizio loop
        while (!controllo.arresto && controllo.pannelloControllo.isVisible()) {
            try {
                if (!controllo.arresto) {
                    
                    //Frequenza del Thread
                    ThreadControlloAspiratore.sleep(INTERVALLO_CONTROLLO_SCAMBIO_LINEE_ASPIRATORE);

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione LINEA MANUALE
                    GestoreIO_ModificaOut(
                            USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE,
                            OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR);

                    controllo.pannelloControllo.elemLabelSimple[34].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1051, ParametriSingolaMacchina.parametri.get(111)));
                    //Frequenza del Thread
                    ThreadControlloAspiratore.sleep(INTERVALLO_CONTROLLO_SCAMBIO_LINEE_ASPIRATORE);

                }

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione LINEA BILANCIA SACCHETTI
                if (!controllo.arresto) {

                    GestoreIO_ModificaOut(
                            USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE,
                            OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR);

                    controllo.pannelloControllo.elemLabelSimple[34].setVisible(true);
                    controllo.pannelloControllo.elemLabelSimple[34].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1052, ParametriSingolaMacchina.parametri.get(111)));
                    //Frequenza del Thread
                    ThreadControlloAspiratore.sleep(INTERVALLO_CONTROLLO_SCAMBIO_LINEE_ASPIRATORE);

                }
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione LINEA TRAMOGGIA DI CARICO
                if (!controllo.arresto) {

                    GestoreIO_ModificaOut(
                            USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE,
                            OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR);

                    controllo.pannelloControllo.elemLabelSimple[34].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1053, ParametriSingolaMacchina.parametri.get(111)));
                    //Frequenza del Thread
                    ThreadControlloAspiratore.sleep(INTERVALLO_CONTROLLO_SCAMBIO_LINEE_ASPIRATORE);

                }

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attivazione LINEA MISCELATORE
                if (!controllo.arresto) {

                    GestoreIO_ModificaOut(
                            USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE,
                            OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_TRUE_CHAR);

                    controllo.pannelloControllo.elemLabelSimple[34].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1054, ParametriSingolaMacchina.parametri.get(111)));
  
                }

            } catch (InterruptedException ex) {
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Disattivazione linee e contattore aspiratore
        GestoreIO_ModificaOut(
                USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE,
                OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR);

        controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);
        controllo.pannelloControllo.elemLabelSimple[34].setVisible(false);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Esito Test
        while (!controllo.testSuperato
                && !controllo.testFallito
                && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloAspiratore.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }
        }

        if (controllo.testSuperato) {
            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 795, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 795, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[27] = true;

            controllo.pannelloControllo.impostaLabelImage(2);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo  Aspiratore Superato");

        } else {

            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 795, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 795, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[27] = false;

            controllo.pannelloControllo.impostaLabelImage(1);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.warning("Controllo Aspiratore Fallito");

        }

        controllo.testSuperato = false;
        controllo.testFallito = false;

        //Impostazione Val Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(
                true);

        //Impostazione Vis Pulsanti Start Stop
        controllo.pannelloControllo.impostaVisibilitaStartStop(
                false);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config(
                "Controllo Aspiratore");

    }

}
