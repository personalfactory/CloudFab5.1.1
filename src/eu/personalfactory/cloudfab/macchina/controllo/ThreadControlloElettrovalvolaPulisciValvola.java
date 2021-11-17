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
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloElettrovalvolaPulisciValvola extends Thread {

    private final Controllo controllo;

    public ThreadControlloElettrovalvolaPulisciValvola(Controllo controllo) {

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
        ControlloLogger.logger.config("Inizio Controllo Elettrovalvola Pulisci Valvola");

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 794, ParametriSingolaMacchina.parametri.get(111))));

        //Abilitazione Pulsante Avvio
        controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

        //Imposta Visibilità Pulsanti 
        controllo.pannelloControllo.elemLabelSimple[0].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[1].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[25].setForeground(controllo.pannelloControllo.elemColor[2]);

        //Attesa Avvio
        //Inizio loop
        while (!controllo.avvio && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloElettrovalvolaPulisciValvola.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }

        }//fine loop

        controllo.avvio = false;

        if (controllo.pannelloControllo.isVisible()) {

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                        OUTPUT_TRUE_CHAR);
                              
            }
            //Abilitazione Pulsante Avvio
            controllo.pannelloControllo.impostaAbilitazionePulsanteArresto(true);

        }

        //Attesa arresto
        //inizio loop
        while (!controllo.arresto && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloElettrovalvolaPulisciValvola.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }

        }//fine loop

        controllo.arresto = false;

        if (controllo.pannelloControllo.isVisible()) {
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                        OUTPUT_FALSE_CHAR);
                     
            }

            controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);

        }

        while (!controllo.testSuperato
                && !controllo.testFallito
                && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloElettrovalvolaPulisciValvola.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }
        }

        if (controllo.testSuperato) {
            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[27] = true;

            controllo.pannelloControllo.impostaLabelImage(2);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Elettrovalvola Aria Pulisci Valvola Superato");

        } else {

            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[27] = false;

            controllo.pannelloControllo.impostaLabelImage(1);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.warning("Controllo Elettrovalvola Aria Pulisci Valvola Fallito");

        }

        controllo.testSuperato = false;
        controllo.testFallito = false;

        //Impostazione Val Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Impostazione Vis Pulsanti Start Stop
        controllo.pannelloControllo.impostaVisibilitaStartStop(false);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Elettrovalvola Pulisci Valvola");

    }
}
