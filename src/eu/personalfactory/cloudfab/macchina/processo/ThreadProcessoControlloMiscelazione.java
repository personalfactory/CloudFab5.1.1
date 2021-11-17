/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 * Controlla che la Miscelazione sia Completata verificando lo stato della
 * variabile miscelazioneCompletata. Quando la miscelazione è completata viene
 * chiamato il gestore processo (case 8) Il Thread si interrompe quando la
 * variabile interrompiControolloMiscelazione=true
 *
 *
 */
public class ThreadProcessoControlloMiscelazione extends Thread {

    private boolean interrompiControlloMiscelazione;
    private final Processo processo; 


    //COSTRUTTORE
    public ThreadProcessoControlloMiscelazione(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Controllo Esecuzione Miscelazione");

        //Inizializzazione Variabili di Controllo
        interrompiControlloMiscelazione = false;
        processo.miscelazioneCompletata = false;

        //begin loop
        while (!interrompiControlloMiscelazione
                && !processo.processoInterrottoManualmente
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Controllo Miscelazione in Esecuzione");

            try {
                ThreadProcessoControlloMiscelazione.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(192)));
            } catch (InterruptedException ex) {
            }

            if (processo.miscelazioneCompletata) {

                //Interruzione Thread di Controllo
                interrompiControlloMiscelazione = true;

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Miscelazione Completata");

                try {
                    ThreadProcessoControlloMiscelazione.sleep(
                            Integer.parseInt(ParametriGlobali.parametri.get(90)));
                } catch (InterruptedException ex) {
                }

                //Chiamata Gestore di Processo
                processo.aggiornaGestoreProcesso();

            }
        }//fine loop

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Controllo Miscelazione");


        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Controllo Esecuzione Miscelazione");
    }
}