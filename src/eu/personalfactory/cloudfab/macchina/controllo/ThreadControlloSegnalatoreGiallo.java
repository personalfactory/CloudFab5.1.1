/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO;

import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloSegnalatoreGiallo extends Thread {

    private Controllo controllo; 

    //COSTRUTTORE
    public ThreadControlloSegnalatoreGiallo(Controllo controllo) {

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
        ControlloLogger.logger.config("Inizio Controllo Segnalatore Luminoso Giallo");

        controllo.pannelloControllo.impostaMessaggioEsito("TEST " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1061, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1058, ParametriSingolaMacchina.parametri.get(111))));

        //Abilitazione Pulsante Avvio
        controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

        //Attesa Avvio
        //Inizio loop
        while (!controllo.avvio
                && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloSegnalatoreGiallo.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }

        }//fine loop

        controllo.avvio = false;

        if (controllo.pannelloControllo.isVisible()) {

            //Attivazione Elettrovalvola Ribalta Sacco
            GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
                    OUTPUT_TRUE_CHAR);
            
        }

        //Abilitazione Pulsante Avvio
        controllo.pannelloControllo.impostaAbilitazionePulsanteArresto(true);


        //Attesa arresto
        //Inizio loop
        while (!controllo.arresto
                && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloSegnalatoreGiallo.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }

        }//fine loop
          
        controllo.arresto = false;

        if (controllo.pannelloControllo.isVisible()) {

            //Disattivazione Elettrovalvola Ribalta Sacco
            GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
                    OUTPUT_FALSE_CHAR);
            
            controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);

        }

        //Inizio loop
        while (!controllo.testSuperato
                && !controllo.testFallito
                && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloSegnalatoreGiallo.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }
        }//fine loop

        if (controllo.testSuperato) {

            controllo.pannelloControllo.impostaMessaggioEsito("TEST " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1061, ParametriSingolaMacchina.parametri.get(111))),
                    "TEST " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1061, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[18] = true;

            controllo.pannelloControllo.impostaLabelImage(2);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Segnalatore Giallo Superato");

        } else {

            controllo.pannelloControllo.impostaMessaggioEsito("TEST " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1061, ParametriSingolaMacchina.parametri.get(111))),
                    "TEST " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1061, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[18] = false;

            controllo.pannelloControllo.impostaLabelImage(1);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.severe("Controllo Segnale Luminoso Giallo Fallito");

        }

        controllo.testSuperato = false;
        controllo.testFallito = false;

        //Impostazione Val Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Impostazione Vis Pulsanti Start Stop
        controllo.pannelloControllo.impostaVisibilitaStartStop(false);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Fine Controllo Segnale Giallo");

    }
}
