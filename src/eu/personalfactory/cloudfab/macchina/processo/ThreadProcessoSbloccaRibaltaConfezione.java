/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_RIBALTA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_RIBALTA_SACCO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio Sblocca e Ribalta il Sacco in Caso non si utilizzi
 * il Secchio
 *
 */
public class ThreadProcessoSbloccaRibaltaConfezione extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoSbloccaRibaltaConfezione(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
    	

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(450))) {

            ///////////////////////
            // NASTRO PRESENTE  ///
            ///////////////////////
        	
			if (processo.indiceConfezioneInPesa >= processo.numConfezioniPerMiscela) {

				////////////////////////////////////////////////////////////// ATTIVAZIONE NASTRO SACCHETTO STANDARD
				new ThreadProcessoGestoreNastroFineProcesso(
						Integer.parseInt(ParametriSingolaMacchina.parametri.get(506))).start();

			} else {

				//////////////////////////////////////////////////////////////ATTIVAZIONE NASTRO ULTIMO SACCHETTO
				new ThreadProcessoGestoreNastroFineProcesso(
						Integer.parseInt(ParametriSingolaMacchina.parametri.get(415))).start();
			}
            
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Sblocca Ribalta Confezione");

        try {
            ThreadProcessoSbloccaRibaltaConfezione.sleep(
                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(202)));
        } catch (InterruptedException ex) {
        }

        TimeLineLogger.logger.log(Level.INFO, "Sblocco e Ribalato Sacco - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {

                /////////////////////////////////////
                // SBLOCCO SECCHIO - NON RIBALTA  ///
                /////////////////////////////////////
            	
                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Sblocco Secchio senza Ribaltamento");

                //Aggiorna Visualizzazione Pannello
                processo.pannelloProcesso.modificaPannello(15);

                GestoreIO_ModificaOut(
                        USCITA_LOGICA_EV_BLOCCA_SACCO,
                        OUTPUT_FALSE_CHAR);

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Secchio Sbloccato");

            } else {

                ////////////////////////////////////
                // SBLOCCO CONFEZIONE - RIBALTA  ///
                ////////////////////////////////////
                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Sblocco Sacco e Ribaltamento");

                if (processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso) {

                    if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(76))
                            && !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(82))) {

                        GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO + OUTPUT_SEP_CHAR
                                + USCITA_LOGICA_EV_RIBALTA_SACCO,
                                OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR);

                    } else {

                        GestoreIO_ModificaOut(
                                USCITA_LOGICA_EV_BLOCCA_SACCO,
                                OUTPUT_FALSE_CHAR);

                    }
                }

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Confezione Sbloccata e Ribaltata");

                //Aggiorna Visualizzazione Pannello
                processo.pannelloProcesso.modificaPannello(10);

            }

            //Aggiornamento Indice Sacchetto Bloccato su Database
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(45),
                    45,
                    "false",
                    ParametriSingolaMacchina.parametri.get(15));

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Attesa Tempo Riposizionamento Sacco dopo Ribalto");

        boolean interrompiAttendiRibalta = false;
        boolean interrompiSaccoRibaltato = false;
        double numRip = Integer.parseInt(ParametriGlobali.parametri.get(18)) / FRAZIONAMENTO_TEMPI;
        int i = 0;

        while ((!interrompiAttendiRibalta
                && !interrompiSaccoRibaltato)
                && !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))
                && !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(82))) {

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(294))) { 

                interrompiSaccoRibaltato = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_RIBALTA_SACCO)
                        != (ParametriSingolaMacchina.parametri.get(288)).equals("1");

            }

            //Attesa tempo Ribalta Sacco
            try {
                ThreadProcessoSbloccaRibaltaConfezione.sleep(FRAZIONAMENTO_TEMPI);
            } catch (InterruptedException ex) {
            }

            i += 4;
            interrompiAttendiRibalta = i > numRip;
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Sacco Ribaltato - Raggiungimento Contatto = {0}{1}Tempo trascorso (ms) ={2}",
                new Object[]{
                    interrompiSaccoRibaltato,
                    LOG_CHAR_SEPARATOR,
                    i * FRAZIONAMENTO_TEMPI});

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Riposizionamento Sacco");

        TimeLineLogger.logger.log(Level.INFO, "Riposizionamento Sacco - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {
            if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(76))) {

                GestoreIO_ModificaOut(
                        USCITA_LOGICA_EV_RIBALTA_SACCO,
                        OUTPUT_FALSE_CHAR);

            }

            //Aggiornamento Visualizzazione Pannello
            processo.pannelloProcesso.modificaPannello(11);
            
          /////////////////////////////////////////////////////////////////////////////////////////Modifica Novembre 2021 - Aggiunta ritardo spegnimento aspiratore
            new ThreadGestoreSpegniApiratore().start();

////            //Modifica Maggio 2015
////            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
////                    && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {
////
////                GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
////                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
////                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
////                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
////                        OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
////                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
////                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
////                        + OUTPUT_FALSE_CHAR);
////
////                ProcessoLogger.logger.info("Disattivazione Uscita Contattore Aspiratore Eseguita");
////            }

            processo.pannelloProcesso.elemBut[13].setVisible(false);

            try {
                //Analizza lo Stato del Processo di Pesa Confezioni
                processo.analizzaPesaConfezioni();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadProcessoSbloccaRibaltaConfezione.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Sblocca Ribalta Confezione");

    }
    
      
    /////////////////////////////////////////////////////////////////////////////////////////Modifica Novembre 2021 - Aggiunta ritardo spegnimento aspiratore
    class ThreadGestoreSpegniApiratore extends Thread{

        @Override
        public void run() {
            super.run(); 

            int delay = 0; 
            
            try{ 
                    delay = Integer.parseInt(ParametriSingolaMacchina.parametri.get(515));
                    
                     this.sleep(delay);
                } catch (NumberFormatException ex) {
            } catch (InterruptedException ex) { 
                Logger.getLogger(ThreadProcessoSbloccaRibaltaConfezione.class.getName()).log(Level.SEVERE, null, ex);
            }

           //Modifica Maggio 2015
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                    && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                        OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR);

                ProcessoLogger.logger.info("Disattivazione Uscita Contattore Aspiratore Eseguita");
            }
        }
    
    
    
    
    }
}
