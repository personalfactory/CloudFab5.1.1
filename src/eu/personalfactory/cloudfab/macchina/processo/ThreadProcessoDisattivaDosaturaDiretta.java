/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoDisattivaDosaturaDiretta extends Thread {

    private Processo processo;

    public ThreadProcessoDisattivaDosaturaDiretta(Processo processo) {

        //Dichiarazione Variabile Pulizia
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Disattivazione Dosatura Diretta");

        GestoreIO_ModificaOut(USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER + OUTPUT_SEP_CHAR,
                OUTPUT_FALSE_CHAR);

        ((Pannello45_Dialog) processo.pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(5);

        //Avvio Thread Apertura/Chiusura Valvola di Carico 
        new ThreadProcessoScaricoMateriali(processo).start();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Disattivazione Dosatura Diretta");

    }
}
