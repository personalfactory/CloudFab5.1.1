/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.GestoreDialog;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoContaTempoArrestoInverter extends Thread {

    //VARIABILI
    private final GestoreDialog gestoreDialog;

    //COSTRUTTORE
    public ThreadProcessoContaTempoArrestoInverter(GestoreDialog gestoreDialog) {

        //Dichiarazione Variabile Gestore Dialog
        this.gestoreDialog = gestoreDialog;

        //Impostazione Nome del Thread
        this.setName(this.getName().getClass().getSimpleName());
    }

    @Override
    public void run() {

        gestoreDialog.pannello.valPulsanti(false);

        String s1 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 674, ParametriSingolaMacchina.parametri.get(111)))
                + " ";
        String s2 = " "
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 675, ParametriSingolaMacchina.parametri.get(111)));

        int timer = Integer.parseInt(ParametriGlobali.parametri.get(86)) / 1000;

        //Inizio loop
        while (timer > 0
                && gestoreDialog.pannello.isVisible()) {

            gestoreDialog.pannello.setMessage(s1 + timer + s2);

            try {
                ThreadProcessoContaTempoArrestoInverter.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(202)));
            } catch (InterruptedException ex) {
            }

            timer--;

        }//fine loop 

        if (gestoreDialog.pannello.isVisible()) {
            gestoreDialog.pannello.gestorePulsanti("1");
        }

    }
}