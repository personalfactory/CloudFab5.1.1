/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;
 

import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoGestorePesaturaManuale extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoGestorePesaturaManuale(Processo processo) {
        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        processo.threadPesaturaManualeEseguito = true;

        TimeLineLogger.logger.log(Level.INFO, "Avvio Verifica Pesatura Manuale - id_ciclo={0}", TrovaSingoloValoreParametroRipristino(91));

        try {
            ThreadProcessoGestorePesaturaManuale.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadProcessoGestorePesaturaManuale.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean componentiPesaManuale = false;

        if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))
                && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(461))
                && !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(102))) {
 
            ////////////////////////////////////////
            // PESATURA MANUALE - CHIMICA SFUSA  ///
            ////////////////////////////////////////
            componentiPesaManuale = true;
            
        } else {
            for (int i = 0; i < processo.componenti.size(); i++) {

                if (processo.componenti.get(i).getMetodoPesa().equals(ParametriGlobali.parametri.get(131))) {

                    if (!(Integer.toString(processo.componenti.get(i).getId())).equals(ParametriSingolaMacchina.parametri.get(301))) {

                        //////////////////////////////////////////////////////
                        // COMPONENTI PESATURA MANUALE (ADDITIVI - COLORI  ///
                        //////////////////////////////////////////////////////
                        if (!Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(i))) {

                            componentiPesaManuale = true;
                            break;

                        }

                    }

                }
            }
        }

        //////////////////////////////////////////
        // INIZIALIZZAZIONE PANNELLO PROCESSO  ///
        //////////////////////////////////////////
        if (componentiPesaManuale) {

            TimeLineLogger.logger.log(Level.INFO,"Avvio Pesatura Manuale - id_ciclo={0}", TrovaSingoloValoreParametroRipristino(91));

            processo.pesaturaManualeCompletata = false;
            processo.pannelloProcesso.elemBut[16].setVisible(true);

        } else {
            processo.pesaturaManualeCompletata = true;
            processo.pannelloProcesso.elemBut[16].setVisible(false);
        }

    }

}
