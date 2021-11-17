/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 * Thread Aninazione Posizionamento Sacchetto Viene visualizzata l'animazione
 * prevista durante il posizionamento del sacchetto. Il processo si interrompe
 * quando la variabile interrompiAnimConfezione=true ATTENZIONE: A fine thread è
 * previsto il controllo dell'inserimento del codice Sacchetto. Se è stato già
 * inserito viene chiamato il gestore processo altrimenti viene chiamato il
 * modifica pannello (case 5) e viene avviato il thread Controlla Blocca
 * Sacchetto
 *
 */
public class ThreadProcessoAnimazioneConfezione extends Thread {

    private final Processo processo; 


    //COSTRUTTORE
    public ThreadProcessoAnimazioneConfezione(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Animazione Confezione");

        processo.interrompiAnimConfezione = false;
        int i = 0;
        int index = 0;

        processo.pannelloProcesso.labelImageAux[11].setVisible(true);
        processo.pannelloProcesso.labelImageAux[12].setVisible(false);

        while (!processo.interrompiAnimConfezione
                && !processo.processoInterrottoManualmente
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Animazione Carico in Esecuzione");

            if (index < Integer.parseInt(ParametriSingolaMacchina.parametri.get(184))) {

                index++;
            } else {
                index = 0;

                switch (i) {

                    case 0: {
                        processo.pannelloProcesso.labelImageAux[11].setVisible(false);
                        processo.pannelloProcesso.labelImageAux[12].setVisible(true);
                        i = 1;

                        break;
                    }

                    case 1: {
                        processo.pannelloProcesso.labelImageAux[11].setVisible(true);
                        processo.pannelloProcesso.labelImageAux[12].setVisible(false);
                        i = 0;


                        break;
                    }

                }
            }
            try {
                ThreadProcessoAnimazioneConfezione.sleep(1);
            } catch (InterruptedException ex) {
            }
        }

        processo.pannelloProcesso.labelImageAux[11].setVisible(false);
        processo.pannelloProcesso.labelImageAux[12].setVisible(false);

        if (processo.pannelloProcesso.isVisible()) {

            if (!processo.processoInterrottoManualmente) {

                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Fine Loop Animazione Carico - Controllo Inserimento Codice Chimica");

                if (TrovaSingoloValoreParametroRipristino(54).equals("5")) {


                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.config("Prima Confezione");

                    //Avvio Gestore Processo
                    processo.aggiornaGestoreProcesso();

                } else {

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.config("Confezioni Successive");

                    //Aggiorna Visualizzazione Panello
                    processo.pannelloProcesso.modificaPannello(5);

                    //Riattivazione Controllo Blocco Sacchetto
                    new ThreadProcessoControlloBloccoSacchetto(processo).start();
                }

            }
        }
        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Animazione Confezione");
    }
}
