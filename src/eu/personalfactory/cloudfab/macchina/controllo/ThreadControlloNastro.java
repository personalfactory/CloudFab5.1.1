/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;

import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_FINECORSA_NASTRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_NASTRO; 

import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloNastro extends Thread {

    private Controllo controllo;
    //COSTRUTTORE

    public ThreadControlloNastro(Controllo controllo) {

        //Dichiarazione Variabile Controllo
        this.controllo = controllo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Nastro");

        controllo.avvio = false;
        controllo.arresto = false;

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1025, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1026, ParametriSingolaMacchina.parametri.get(111))));

        //Abilitazione Pulsante Avvio
        controllo.pannelloControllo.impostaAbilitazionePulsanteAvvio(true);

        //Attesa Avvio
        //Inizio loop
        while (!controllo.avvio
                && controllo.pannelloControllo.isVisible()) {

            //Frequenza del Thread
            try {
                ThreadControlloNastro.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }

        }//fine loop

        controllo.avvio = false;

        if (controllo.pannelloControllo.isVisible()) {

            //////////////////////////////////////////////////////////////////////////////////////////////// ATTIVAZIONE CONTATTORE NASTRO
            GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO,
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
                ThreadControlloNastro.sleep(200);
            } catch (InterruptedException ex) {
            }
            
            if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_FINECORSA_NASTRO)) {

            	////////////////////////////////////////////////////////////////////////////////////////////////DISATTIVAZIONE CONTATTORE NASTRO - INTERVENTO FINEOCRSA
                GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO, OUTPUT_FALSE_CHAR);
                controllo.arresto= true;
                
            }

        }//fine loop

        controllo.arresto = false;

        if (controllo.pannelloControllo.isVisible()) {

        	////////////////////////////////////////////////////////////////////////////////////////////////DISATTIVAZIONE CONTATTORE NASTRO
            GestoreIO_ModificaOut(USCITA_LOGICA_NASTRO,
                    OUTPUT_FALSE_CHAR);

        }

        controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(true);

        //Inizio loop
        while (!controllo.testSuperato
                && !controllo.testFallito
                && controllo.pannelloControllo.isVisible()) {

            //Attesa tempo Delay
            try {
                ThreadControlloNastro.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
            } catch (InterruptedException ex) {
            }
        }//fine loop

        if (controllo.testSuperato) {
            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1025, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1025, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[20] = true;

            controllo.pannelloControllo.impostaLabelImage(2);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Nastro Superato");

        } else {

            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1025, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1025, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[20] = false;

            controllo.pannelloControllo.impostaLabelImage(1);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.warning("Controllo Nastro Fallito");

        }

        controllo.testSuperato = false;
        controllo.testFallito = false;

        //Impostazione Validità Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Impostazione Visbilità Pulsanti Start Stop
        controllo.pannelloControllo.impostaVisibilitaStartStop(false);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Fine Controllo Nastro");
    }
}
