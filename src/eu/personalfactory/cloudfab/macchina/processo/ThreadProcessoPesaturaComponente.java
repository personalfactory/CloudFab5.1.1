/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_RITARDO_ATTIVAZIONE_INVERTER;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio Pesatura del Componente
 *
 *
 */
public class ThreadProcessoPesaturaComponente extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoPesaturaComponente(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Thread Processo Pesatura Componente");

        TimeLineLogger.logger.log(Level.INFO, "Abilitazione Uscita Coclea");

        //Attivazione Rele Coclea Componente in Pesa
        processo.gestoreReleCoclee(true);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Attivazione Rele Coclea Materiale in Pesa - Eseguita");

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Pesatura Componente {0}", processo.indiceCompInPesa);

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            if (!processo.dosaturaDirettaAttiva) {

                //Attesa Delay Inverter
                try {
                    ThreadProcessoPesaturaComponente.sleep(TEMPO_RITARDO_ATTIVAZIONE_INVERTER);
                } catch (NumberFormatException | InterruptedException ex) {
                }

                //Avvio Inverter 
                inverter_screws.avviaArrestaInverter(true);

                TimeLineLogger.logger.log(Level.INFO, "Avvio Inverter");

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Avvio Inverter Eseguito");
            }
        }

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Avvio Controllo Coclea Vuota
            new ThreadProcessoControlloCocleaVuota(processo).start();
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Processo Pesatura Componente");
    }
}
