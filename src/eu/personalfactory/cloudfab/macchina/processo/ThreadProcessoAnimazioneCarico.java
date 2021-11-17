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
 * Thread Animazione Carico Vengono Visualizzazione le animazione previste
 * durante il carico delle materie prime. Il Thread si interrompe quando la
 * variabile interrompiAnimCarico=true;
 *
 *
 */
public class ThreadProcessoAnimazioneCarico extends Thread {

    //VARIABILI
    private Processo processo; 


    //COSTRUTTORE
    public ThreadProcessoAnimazioneCarico(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;


        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
    	 
        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Animazione Carico");

        processo.interrompiAnimCarico = false;
        int i = 0;
 
        
        //Inizio loop
        while (!processo.interrompiAnimCarico
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Animazione Carico in Esecuzione");

            switch (i) {

                case 0: {

                    processo.pannelloProcesso.labelImageAux[18].setVisible(true);
                    processo.pannelloProcesso.labelImageAux[21].setVisible(false);
                    i = 1;

                    break;
                }

                case 1: {
                    processo.pannelloProcesso.labelImageAux[18].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[19].setVisible(true);
                    i = 2;


                    break;
                }

                case 2: {
                    processo.pannelloProcesso.labelImageAux[19].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[20].setVisible(true);
                    i = 3;


                    break;
                }
                case 3: {
                    processo.pannelloProcesso.labelImageAux[20].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[21].setVisible(true);
                    i = 0;


                    break;
                }

            }
             
            try {
                ThreadProcessoAnimazioneCarico.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(183)));
            } catch (InterruptedException ex) {
            }
        }//fine loop
         
 
        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Animazione Carico");
        
        processo.pannelloProcesso.labelImageAux[18].setVisible(false);
        processo.pannelloProcesso.labelImageAux[19].setVisible(false);
        processo.pannelloProcesso.labelImageAux[20].setVisible(false);
        processo.pannelloProcesso.labelImageAux[21].setVisible(false);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Animazione Carico");
    }
}