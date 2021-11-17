/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR; 

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoAttivaDosaturaDiretta extends Thread {

    private final Processo processo;

    public ThreadProcessoAttivaDosaturaDiretta(Processo processo) {

        //Dichiarazione Variabile Pulizia
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Attivazione Dosatura Diretta");

        if (!processo.dosaturaDirettaAttiva) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Attivazione Uscita Attiva-Disattiva Inverter");
////////
////////        PROCEDURA DA RIVEDERE
////////            //Spegnimento Fisico Inverter
////////            GestoreIO_ModificaOut(
////////                    USCITA_LOGICA_CONTATTORE_ATTIVA_INVERTER_COCLEE,
////////                    OUTPUT_TRUE_CHAR);

            ((Pannello45_Dialog) processo.pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(4);

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Attivazione Uscita Dosatura Diretta");

            //Attivazione Uscita Linea Diretta
            GestoreIO_ModificaOut(
                    CloudFabConstants.USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER,
                    OUTPUT_TRUE_CHAR);

            //Aggiornamento Variabile di Controllo
            processo.dosaturaDirettaAttiva = true;

            //Memorizzazione Log Processo
            ProcessoLogger.logger.info("Dosatura Diretta Attiva");

        }

        processo.analizzaPesaComponenti();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Attivazione Dosatura Diretta");

    }
}
