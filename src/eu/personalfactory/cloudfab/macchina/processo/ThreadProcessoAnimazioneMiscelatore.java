/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 * Thread Aninazione Inserimento Miscelatore Viene visualizzata l'animazione
 * prevista quando il motore è in funzione. Il thread si interrompe quando la
 * variabile interrompiAnimMiscelatore=true
 *
 *
 */
public class ThreadProcessoAnimazioneMiscelatore extends Thread {

    private final Processo processo; 


    //COSTRUTTORE
    public ThreadProcessoAnimazioneMiscelatore(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Animazione Miscelatore");

        processo.interrompiAnimMiscelatore = false;
        int i = 0;

        //Inizio loop
        while (!processo.interrompiAnimMiscelatore
                && !processo.processoInterrottoManualmente
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Animazione Miscelatore in Esecuzione");

            switch (i) {

                case 0: {
                    processo.pannelloProcesso.labelImageAux[22].setVisible(true);
                    processo.pannelloProcesso.labelImageAux[25].setVisible(false);
                    i = 1;

                    break;
                }

                case 1: {
                    processo.pannelloProcesso.labelImageAux[22].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[23].setVisible(true);
                    i = 2;


                    break;
                }

                case 2: {
                    processo.pannelloProcesso.labelImageAux[23].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[24].setVisible(true);
                    i = 3;


                    break;
                }
                case 3: {
                    processo.pannelloProcesso.labelImageAux[24].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[25].setVisible(true);
                    i = 0;


                    break;
                }

            }

            try {
                ThreadProcessoAnimazioneMiscelatore.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(185)));
            } catch (InterruptedException ex) {
            }
        } //fine loop


        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Animazione Miscelatore");

        //Impostazione Visibilità labelImageAux
        processo.pannelloProcesso.labelImageAux[22].setVisible(false);
        processo.pannelloProcesso.labelImageAux[23].setVisible(false);
        processo.pannelloProcesso.labelImageAux[24].setVisible(false);
        processo.pannelloProcesso.labelImageAux[25].setVisible(false);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Animazione Miscelatore");

    }
}