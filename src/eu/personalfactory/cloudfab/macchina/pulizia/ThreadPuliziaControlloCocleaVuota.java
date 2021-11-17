/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 *
 * Controlla che la Coclea non sia Vuota
 *
 */
public class ThreadPuliziaControlloCocleaVuota extends Thread {

    private Pulizia pulizia;

    //COSTRUTTORE
    public ThreadPuliziaControlloCocleaVuota(Pulizia pulizia) {

        //Dichiarazione Varibale Processo
        this.pulizia = pulizia;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Controllo Coclea Vuota");

        pulizia.interrompiControlloCocleaVuota = false;

        //Dichiarazione e inizializzazione Variabili
        int valorePesoPrec = 0;
        int valorePesoSucc = 0;
        boolean contaTempo;
        int contatoreT = 0;
        pulizia.interruzioneCocleaVuota = false;

        //Inizio loop
        while (!pulizia.interrompiControlloCocleaVuota
                && pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            if (pulizia.valorePesaCarico != null) {

                try {
                    valorePesoPrec = Integer.parseInt(pulizia.valorePesaCarico);

                    ThreadPuliziaControlloCocleaVuota.sleep(
                            Integer.parseInt(
                                    ParametriSingolaMacchina.parametri.get(55)));

                    valorePesoSucc = Integer.parseInt(pulizia.valorePesaCarico);

                    contaTempo = (valorePesoSucc - valorePesoPrec)
                            < Integer.parseInt(ParametriSingolaMacchina.parametri.get(53));

                    if (contaTempo) {

                        if (contatoreT < Integer.parseInt(ParametriSingolaMacchina.parametri.get(54))) {

                            contatoreT++;
                            //Memorizzazione Log Processo
                            PuliziaLogger.logger.log(Level.CONFIG, "{0} / {1}", new Object[]{contatoreT, ParametriSingolaMacchina.parametri.get(54)});
                        
                        } else {

                            //Memorizzazione Log Processo
                            PuliziaLogger.logger.warning("Rilevata Coclea Vuota");

                            //Interruzione il loop del Thread Corrente
                            pulizia.interrompiControlloCocleaVuota = true;

                            //Impostazione Variabile di Controllo
                            pulizia.interruzioneCocleaVuota = true;

                            //Interruzione Thread di Pesatura
                            pulizia.pesoRaggiunto = true;

                        }
                    } else {
                        contatoreT = 0;
                    }

                } catch (NumberFormatException | InterruptedException ex) {
                }
            }
        }
        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Controllo Coclea Vuota");

    }
}
