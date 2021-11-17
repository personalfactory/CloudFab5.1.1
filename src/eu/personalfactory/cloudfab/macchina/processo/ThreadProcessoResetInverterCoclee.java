/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;
 
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DELAY_RESET_INVERTER; 
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws; 

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoResetInverterCoclee extends Thread {

    private final Processo processo;

    public ThreadProcessoResetInverterCoclee(Processo processo) {

        //Dichiarazione Variabile Pulizia
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

    	ProcessoLogger.logger.config("Inizio Procedura Reset Inverter");

    	String rep = inverter_screws.resetFault();

    	ProcessoLogger.logger.severe("Procedura Reset Inverter Coclee = " + rep);

    	((Pannello45_Dialog) processo.pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(4);

    	try {
    		Thread.sleep(DELAY_RESET_INVERTER);
    	} catch (InterruptedException ex) { 
    	}

    	((Pannello45_Dialog) processo.pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(5);

    	processo.analizzaPesaComponenti();

    	//Memorizzazione Log Processo
    	ProcessoLogger.logger.config("Fine Procedura Reset Inverter");

    }
}
