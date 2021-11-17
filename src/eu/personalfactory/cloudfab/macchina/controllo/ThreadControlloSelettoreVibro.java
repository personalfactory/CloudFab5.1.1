/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO;

/**
 *
 * @author francescodigaudio
 *
 *
 */
public class ThreadControlloSelettoreVibro extends Thread {

    private Controllo controllo;

    //COSTRUTTORE
    public ThreadControlloSelettoreVibro(Controllo controllo) {

        //Dichiarazione Variabile Controllo
        this.controllo = controllo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Selettore Vibro");

        boolean ingressoAttivo = false;
        boolean testFallito = false;
        int tempoResiduo = Integer.parseInt(ParametriSingolaMacchina.parametri.get(207));

        //Impostazione Label Presa/Timer
        controllo.pannelloControllo.impostaLabelPresaTimer(Integer.toString(tempoResiduo));

        int counter = 0;

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 494, ParametriSingolaMacchina.parametri.get(111))));
 
        if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO)) {

            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

            controllo.esitoTest[11] = false;

            controllo.pannelloControllo.impostaLabelImage(1);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.warning("Controllo Selettore Vibro Fallito - Stato Iniziale Errato");
        } else {

            //Inizio loop
            while (!ingressoAttivo && !testFallito && controllo.pannelloControllo.isVisible()) {

                //Frequenza del Thread
                try {
                    ThreadControlloSelettoreVibro.sleep(
                            Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
                } catch (InterruptedException ex) {
                }
 
                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO)) {

                    ingressoAttivo = true;

                    controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111))),
                            EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111)))
                            + " "
                            + ParametriGlobali.parametri.get(46)
                            + " "
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

                    controllo.esitoTest[11] = true;

                    controllo.pannelloControllo.impostaLabelImage(2);

                    //Memorizzazione Logger Procedure di Controllo
                    ControlloLogger.logger.info("Controllo Selettore Vibro Superato");

                } else {

                    counter++;

                    if (counter > 1000 / Integer.parseInt(ParametriSingolaMacchina.parametri.get(204))) {

                        counter = 0;

                        if (tempoResiduo > 1) {
                            tempoResiduo--;

                            //Impostazione Label Presa/Timer
                            controllo.pannelloControllo.impostaLabelPresaTimer(Integer.toString(tempoResiduo));
                        } else {

                            testFallito = true;

                            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111))),
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 471, ParametriSingolaMacchina.parametri.get(111)))
                                    + " "
                                    + ParametriGlobali.parametri.get(46)
                                    + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

                            controllo.esitoTest[11] = false;

                            controllo.pannelloControllo.impostaLabelImage(1);

                            //Memorizzazione Logger Procedure di Controllo
                            ControlloLogger.logger.warning("Controllo Selettore Vibro Fallito - Stato Finale Errato");
                        }
                    }

                }

            }//fine loop
        }

        //Impostazione Label Presa/Timer
        controllo.pannelloControllo.impostaLabelPresaTimer("");

        //Impostazione Validità Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Fine Controllo Selettore Vibro");
    }
}
