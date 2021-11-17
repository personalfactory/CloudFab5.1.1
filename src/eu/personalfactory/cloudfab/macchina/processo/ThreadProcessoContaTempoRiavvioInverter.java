/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.GestoreDialog;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoContaTempoRiavvioInverter extends Thread {

    private final GestoreDialog gestoreDialog;

    //COSTRUTTORE
    public ThreadProcessoContaTempoRiavvioInverter(GestoreDialog gestoreDialog) {

        //Dichiarazione Variabile Gestore Dialog
        this.gestoreDialog = gestoreDialog;

        //Impostazione Nome del Thread
        this.setName(this.getName().getClass().getSimpleName());
    }

    @Override
    public void run() {

        gestoreDialog.pannello.valPulsanti(false);

        String s1 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 676,
                ParametriSingolaMacchina.parametri.get(111)))
                + " ";
        String s2 = " "
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 677, ParametriSingolaMacchina.parametri.get(111)));

        int timer = Integer.parseInt(ParametriGlobali.parametri.get(87)) / 1000;

        //Inizio loop
        while (timer > 0
                && gestoreDialog.pannello.isVisible()) {

            gestoreDialog.pannello.setMessage(s1 + timer + s2);

            try {
                ThreadProcessoContaTempoRiavvioInverter.sleep(
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