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
 * Controlla che sia la Miscelazione che la Pesa Successiva siano Completate
 *
 */
public class ThreadProcessoControlloMiscelazionePesa extends Thread {

    private boolean interrompiControlloMiscelazionePesa;
    private final Processo processo; 


    //COSTRUTTORE
    public ThreadProcessoControlloMiscelazionePesa(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Controllo Esecuzione Miscelazione e Pesa Successiva");

        //Inizializzazione Variabili di Controllo
        interrompiControlloMiscelazionePesa = false;
        processo.miscelazioneCompletata = false;
        processo.pesaSuccessivaCompletata = false;

        //Inizio loop
        while (!interrompiControlloMiscelazionePesa
                && !processo.processoInterrottoManualmente
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Controllo Miscelazione e Pesa in Esecuzione");

            try {
                ThreadProcessoControlloMiscelazione.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(193)));
            } catch (InterruptedException ex) {
            }

            if (processo.miscelazioneCompletata && processo.pesaSuccessivaCompletata) {

                //Interruzione del Thread di Controllo
                interrompiControlloMiscelazionePesa = true;

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Miscelazione e Pesa Completate");

                try {
                    ThreadProcessoControlloMiscelazionePesa.sleep(
                            Integer.parseInt(ParametriGlobali.parametri.get(90)));
                } catch (InterruptedException ex) {
                }

                //Chiamata Gestore di Processo
                processo.aggiornaGestoreProcesso();

            }
        }//fine loop


        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Controllo Miscelazione e Pesa");
 
        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Controllo Esecuzione Miscelazione e Pesa Successiva");

    }
}
