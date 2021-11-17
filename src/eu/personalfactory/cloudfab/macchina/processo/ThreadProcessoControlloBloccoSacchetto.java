/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPORIZZATORE_ARIA_FORMA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI;

/**
 *
 * @author francescodigaudio
 *
 *
 */
public class ThreadProcessoControlloBloccoSacchetto extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloBloccoSacchetto(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Controllo Blocco Sacchetto");

        TimeLineLogger.logger.log(Level.INFO, "Inizio Controllo Blocco Sacchetto - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        boolean sacchettoBloccato = false;
    	int contatore_blocco_sacco = 0; 
        if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(82))) {

            //Inizio loop
            while (!sacchettoBloccato
                    && !processo.processoInterrottoManualmente
                    && processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso) {
 
                  if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO) || contatore_blocco_sacco>30) {   
                    

                    //Procedura Forma Sacco
                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))
                            && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(369))) {

                        /////////////////////////////
                        // FORMA SACCO ABILITATO  ///
                        /////////////////////////////
                        //Blocco Sacchetto e attivazione aria svuota tubo
                        if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {

                            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO + OUTPUT_SEP_CHAR
                                    + USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                                    OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                    + OUTPUT_TRUE_CHAR);

                        } else {
                            
                            GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                                    OUTPUT_TRUE_CHAR);

                        }
                         
                        ProcessoLogger.logger.info("Blocco sacco e attivazione aria svuota tubo");

                        try {
                            ThreadProcessoControlloBloccoSacchetto.sleep(TEMPORIZZATORE_ARIA_FORMA_SACCO);
                        } catch (InterruptedException ex) {
                        }

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {
                           
                              GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                                    OUTPUT_FALSE_CHAR);
                        }
                        try {
                            ThreadProcessoControlloBloccoSacchetto.sleep(800);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThreadProcessoControlloBloccoSacchetto.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        ProcessoLogger.logger.info("Disattivazione aria svuota tubo");

                    } else {

                        /////////////////////////////////
                        // FORMA SACCO NON ABILITATO  ///
                        /////////////////////////////////
                        //Blocco Sacchetto
                        if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {

                             GestoreIO_ModificaOut(
                                    USCITA_LOGICA_EV_BLOCCA_SACCO,
                                    OUTPUT_TRUE_CHAR);

                            ProcessoLogger.logger.info("Blocco sacco");
                        }
                    }

                    sacchettoBloccato = true;
                
                  } else  if (ABILITA_SIMULAZIONE_PROCESSO) {
                  		
                  		contatore_blocco_sacco++;
                  	  
                	 }

                try {
                    ThreadProcessoControlloBloccoSacchetto.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(186)));
                } catch (InterruptedException ex) {
                }

            }
            //fine loop

            TimeLineLogger.logger.log(Level.INFO, "Sacchetto Bloccato - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

            try {

                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Creazione Client di Pesa e Commutazione Peso Netto");

                //Creazione del Client
                ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
                 
                int pesoLordoPesaConfezioni = 0;
                int counter = 0;
                //Inizio loop
                while (pesoLordoPesaConfezioni == 0
                        && counter < Integer.parseInt(ParametriSingolaMacchina.parametri.get(170))
                        && processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso) {

                    //Lettura del Peso Lordo
                    try {
                        pesoLordoPesaConfezioni = Integer.parseInt(pesaConfezioni.pesoLordo());
                        
                        if (pesoLordoPesaConfezioni>0) {
                        	
                        	counter = Integer.parseInt(ParametriSingolaMacchina.parametri.get(170));
                        	
                        }
                    } catch (NumberFormatException e) {
                    }

                    counter++;
                    
                    try {
                        ThreadProcessoControlloPesoConfezioni.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);
                    } catch (InterruptedException ex) {
                    }

                }//fine loop

                //Memorizzazione Log Processo
                ProcessoLogger.logger.log(Level.INFO, "Peso Letto sulla Bilancia Confezioni dopo del Blocco Sacco ={0}", pesaConfezioni.pesoNetto());
                
                    ThreadProcessoControlloPesoConfezioni.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);
                
                //Azzeramento Modulo di Pesa
                pesaConfezioni.commutaNetto();

                //Memorizzazione Log Processo
                ProcessoLogger.logger.log(Level.INFO, "Peso Letto sulla Bilancia Confezioni dopo del Blocco Sacco ={0}", pesaConfezioni.pesoNetto());
               
                    ThreadProcessoControlloPesoConfezioni.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI);
               
                //Chiusura del Client
                pesaConfezioni.chiudi();

                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Chiusura Client di Pesa Eseguita");

            } catch (Exception e) {

                //Memorizzazione Log Processo
                ProcessoLogger.logger.log(Level.SEVERE, "Errore durante la Commutazione del Peso dopo il Blocco Sacco e ={0}", e);

            }
        }

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            if (!processo.processoInterrottoManualmente) {

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Blocco Sacchetto Disabilitato");

                //Aggiornamento Indice Sacchetto Bloccato su Database
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(45),
                        45,
                        "true",
                        ParametriSingolaMacchina.parametri.get(15));

                processo.pannelloProcesso.modificaPannello(16);

                try {
                    ThreadProcessoControlloBloccoSacchetto.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadProcessoControlloBloccoSacchetto.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (TrovaSingoloValoreParametroRipristino(54).equals("6")) {

                    //Aggiornamento Gestore Processo
                    processo.aggiornaGestoreProcesso();

                } else {

                    //Aggiornamento Visualizzazione Pannello
                    processo.pannelloProcesso.modificaPannello(8);

                    try {
                        //Procedura controllo stato Pesa Sacchetti
                        processo.analizzaPesaConfezioni();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadProcessoControlloBloccoSacchetto.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Controllo Blocco Sacchetto");
    }
}
