/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPORIZZATORE_SEGNALE_ACUSTICO;

/**
 *
 * @author francescodigaudio Attivazione Uscita Segnalatore Acustico
 *
 *
 */
public class ThreadProcessoGestoreAvvioProcesso extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoGestoreAvvioProcesso(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Avvio Processo");

        //Inizializzazione Varibili di Controllo Interruzioni
        processo.interruzioneControlloPesoCocleaVuota = false;
        processo.interruzioneControlloPesoInterruzioneManuale = false;
        processo.interruzioneControlloPesoRaggiunto = false;
        processo.interruzioneControlloPesoTastoReset = false;

        //Aggiornamento Stato Variabile di Controllo
        processo.controlliInizialiInCorso = false;

        //Memorizzazione Log Processo
        TimeLineLogger.logger.log(Level.INFO, "Attivazione Segnalatore Acustico - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) { 
        	
            GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO + OUTPUT_SEP_CHAR
                    + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                    OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                    + OUTPUT_TRUE_CHAR);

            //Attesa Tempo Durata Segnalazione Acustica
            try {
                ThreadProcessoGestoreAvvioProcesso.sleep(TEMPORIZZATORE_SEGNALE_ACUSTICO);

            } catch (InterruptedException ex) {
            }

        } 
        
        //Memorizzazione Log Processo
        TimeLineLogger.logger.log(Level.INFO, "Disattivazione Segnalatore Acustico - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        GestoreIO_ModificaOut(USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO,
                OUTPUT_FALSE_CHAR);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Verifica Stato Attuale del Processo");

        //Impostazione Visibilità Pulsante Linea Diretta
        processo.pannelloProcesso.elemBut[5].setVisible(false);

        //Impostazione Visibilità Disabilita Tracciabilita
        processo.pannelloProcesso.elemBut[11].setVisible(false);

        processo.pannelloProcesso.elemLabelSimple[30].setVisible(false);

        //Ripristina Ultimo Passo
        int ultimoPasso = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54));

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.CONFIG, "Ultimo Passo ={0}", ultimoPasso);

        
        //Verifica Stato Attuale del Processo
        if (ultimoPasso > 0 && ultimoPasso != 9) {

            //Modifica Microdosatori
            processo.ignoraConfigurazioneMicrodosatori = true;

            if (ultimoPasso != 8) {

                //Aggiornamento Valore Parametri Ripristino
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                        54,
                        Integer.toString(ultimoPasso - 1),
                        ParametriSingolaMacchina.parametri.get(15));
            } else {
                //Aggiornamento Valore Parametri Ripristino
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                        54,
                        Integer.toString(ultimoPasso - 2),
                        ParametriSingolaMacchina.parametri.get(15));
            }

        } 
        
        //Avvio Thread Controntrollo Interruttori Manuali
        new ThreadProcessoControlloInterruttoriManuali(processo).start();

        //Chiamata Gestore Processo
        new ThreadProcessoGestoreProcesso(processo).start();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Avvio Processo");
    }
}
