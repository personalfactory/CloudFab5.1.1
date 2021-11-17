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
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloContattoSportello extends Thread {

    private Controllo controllo;

    //COSTRUTTORE
    public ThreadControlloContattoSportello(Controllo controllo) {

        //Dichiarazione Variabile Processo
        this.controllo = controllo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Contatto Sportello");

        boolean ingressoAttivo = false;
        boolean testFallito = false;
        int tempoResiduo = Integer.parseInt(ParametriSingolaMacchina.parametri.get(207));

        //Impostazione Label Presa/Timer
        controllo.pannelloControllo.impostaLabelPresaTimer(Integer.toString(tempoResiduo));

        int counter = 0;

        //Aggiornamento Messaggio Visualizzato
        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 488, ParametriSingolaMacchina.parametri.get(111))));

        //Imposta Visibilità Pulsanti 
        controllo.pannelloControllo.elemLabelSimple[0].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[24].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[25].setForeground(controllo.pannelloControllo.elemColor[2]);

        if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE)
                != ParametriSingolaMacchina.parametri.get(18).equals("1")) {

            //Impostazione Label Presa/Timer
            controllo.pannelloControllo.impostaLabelPresaTimer("");

            //Aggiornamento Messaggio Visaulizzato
            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111))),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + ParametriGlobali.parametri.get(46)
                    + " "
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.warning("Controllo Contatto Sportello Fallito - Stato Iniziale Errato");

            controllo.esitoTest[4] = false;

            controllo.pannelloControllo.impostaLabelImage(1);

            //Impostazione Val Pulsanti Scelta Test
            controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        } else {

            controllo.esitoTest[4] = true;

            //Inizio loop
            while (!ingressoAttivo && !testFallito
                    && controllo.pannelloControllo.isVisible()) {

                //Frequenza del Thread
                try {
                    ThreadControlloContattoSportello.sleep(
                            Integer.parseInt(ParametriSingolaMacchina.parametri.get(204)));
                } catch (InterruptedException ex) {
                }

                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE)) {

                    ingressoAttivo = true;

                    controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111))),
                            EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111)))
                            + " "
                            + ParametriGlobali.parametri.get(46)
                            + " "
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

                    controllo.esitoTest[5] = true;

                    //Memorizzazione Logger Procedure di Controllo
                    ControlloLogger.logger.info("Controllo Contatto Sportello Superato");

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

                            controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111))),
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 465, ParametriSingolaMacchina.parametri.get(111)))
                                    + " "
                                    + ParametriGlobali.parametri.get(46)
                                    + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

                            //Memorizzazione Logger Procedure di Controllo
                            ControlloLogger.logger.warning("Controllo Contatto Sportello Fallito - Stato Finale Errato");

                            controllo.esitoTest[5] = false;

                        }
                    }

                }

            }//fine loop

            //Impostazione Label Presa/Timer
            controllo.pannelloControllo.impostaLabelPresaTimer("");

            if (controllo.esitoTest[4] && controllo.esitoTest[5]) {

                controllo.pannelloControllo.impostaLabelImage(2);

            } else {
                controllo.pannelloControllo.impostaLabelImage(1);

            }

            controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.config("Fine Controllo Contatto Sportello");
        }
    }
}
