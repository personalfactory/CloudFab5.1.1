/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO;
import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoApriChiudiValvola extends Thread {

    private boolean interrompiThreadApriChiudiValvola;
    private final Processo processo;

    public ThreadProcessoApriChiudiValvola(Processo processo) {
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Apri-Chiudi Valvola di Scarico");

        interrompiThreadApriChiudiValvola = false;

        processo.threadApriChiudiValvolaAvviato = true;
        

        //Loop Thread Apri Chiudi Valvola
        while (!interrompiThreadApriChiudiValvola
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso
                && !processo.pesoConfezioneRaggiunto) {
        	
            
			//Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Apri-Chiudi Valvola di Esecuzione");

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// CHIUSURA VALVOLA DI SCARICO
            if (!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO)) {
            	
            	chiudiValvola();  
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTESA TEMPO DI CHIUSURA 
        	int tempoChiusura = calcolaTempoChiusura(processo.pesoMancanteConfezioni);
        	ProcessoLogger.logger.log(Level.INFO, "Attesa Tempo di Chiusura = {0}", tempoChiusura);

    		int numeroRipetizioniChiusura = tempoChiusura / FRAZIONAMENTO_TEMPI;
    		boolean interrompiTimerChiusura = false;
    		int counterChiusura = 0;

    		// Inizio loop
    		while (!interrompiTimerChiusura && processo.pannelloProcesso.isVisible() && !processo.resetProcesso
    				&& !processo.pesoConfezioneRaggiunto) {

    			counterChiusura++;

    			if (counterChiusura >= numeroRipetizioniChiusura | processo.pesoMancanteConfezioni <= 0) {
    				interrompiTimerChiusura = true;
    			}
    			try {
    				ThreadProcessoApriChiudiValvola.sleep(FRAZIONAMENTO_TEMPI);
    			} catch (InterruptedException ex) {
    			}

    		}
         
        	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// VERIFICA RAGGIUNGIMENTO PESO DURANTE LA CHIUSURA
        	if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso
                    && !processo.pesoConfezioneRaggiunto) {

                //////////////////////////////////////
                // PESO CONFEZIONE NON RAGGIUNTO  ///
                /////////////////////////////////////
        		
        		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// APERTURA VALVOLA DI SCARICO
                
        		boolean statoValvola = false; 
        		apriValvola();   
        		statoValvola = true;
        		
        		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ATTESA TEMPO DI APERTURA 
            	  
                boolean interrompiTimerApertura = false;
                int counterApertura = 0;
                int tempoApertura = calcolaTempoApertura(processo.pesoMancanteConfezioni);
                int numeroRipetizioniApertura = tempoApertura
                        / FRAZIONAMENTO_TEMPI;
                
                //Memorizzazione Log Processo
                ProcessoLogger.logger.log(Level.INFO, "Attesa Tempo Apertura = {0}", tempoApertura);
 
        		boolean aperturaAnnullata = false; 
        	
        		
                //Inizio loop
                while (!interrompiTimerApertura
                        && processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso
                        && !processo.pesoConfezioneRaggiunto
                        && !aperturaAnnullata) {

                   counterApertura++;

                   if (counterApertura >= numeroRipetizioniApertura) {

                        interrompiTimerApertura = true;

                    } else {

                        //if (processo.pesoMancanteConfezioni <= 0) {
                        if (processo.pesoConfezioneRaggiunto) {

                            interrompiTimerApertura = true;

                            /////////////////////////////////////////////////////////////////////////////////////////// CHIUSURA VALVOLA DI SCARICO 
                            if (statoValvola) {
                            	 
                            	/////////////////////////////////////////////////////////////////////////////////////////// CHIUSURA VALVOLA DI SCARICO 
                            	chiudiValvola();
                            	
                            	statoValvola = false;
                            }

                        } else {
                        	
                        	if (!statoValvola) {
                        		
                        		/////////////////////////////////////////////////////////////////////////////////////////// APERTURA VALVOLA DI SCARICO
                        		apriValvola();
                        	
                        		statoValvola = true;
                        		}

                        }
 
                    try {
                        ThreadProcessoApriChiudiValvola.sleep(FRAZIONAMENTO_TEMPI);
                    } catch (InterruptedException ex) {
                    }

                    }
                }

                //Memorizzazione Log Processo
                ProcessoLogger.logger.fine("Fine Attesa Tempo Apertura");

            } else {

                ////////////////////////////////////////////////////////
                // PESO RAGGIUNTO DURANTE LA CHIUSURA DELLA VALVOLA  ///
                ////////////////////////////////////////////////////////
                interrompiThreadApriChiudiValvola = true;

                //Memorizzazione Log Processo
                ProcessoLogger.logger.fine("Peso Raggiunto Peso durante la Chiusura");
            }

        }//fine loop
        
        chiudiValvola();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Fine Procedura Apri-Chiudi Valvola di Scarico");

        new ThreadProcessoFinalizzaProceduraApriChiudiValvola(processo).start();

    }

    //Restituisce il Tempo di Atteso prima di Riaprire la Valvola
    private int calcolaTempoApertura(double matMancante) {

        double result = 0;

        if (processo.limiti[processo.indiceConfezioneInPesa][0] > matMancante) {

            result = processo.tempiApertura[processo.indiceConfezioneInPesa][0];
        } else {

            for (int i = Integer.parseInt(ParametriGlobali.parametri.get(60)) - 2; i >= 0; i--) {
                if (matMancante >= processo.limiti[processo.indiceConfezioneInPesa][i]) {

                    result = processo.tempiApertura[processo.indiceConfezioneInPesa][i + 1];
                    break;
                }
            }
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Calcolo Tempo Apertura Materiale Mancante =" + matMancante + " - Tempo Riapertura = " + result);

        return (int) result;
    }

    //Restituisce il Tempo di Atteso prima di Riaprire la Valvola
    private int calcolaTempoChiusura(double matMancante) {

        int result = 0;

        if (processo.limiti[processo.indiceConfezioneInPesa][0] > matMancante) {

            result = processo.tempiChiusura[processo.indiceConfezioneInPesa][0];
        } else {

            for (int i = Integer.parseInt(ParametriGlobali.parametri.get(60)) - 2; i >= 0; i--) {
                if (matMancante >= processo.limiti[processo.indiceConfezioneInPesa][i]) {

                    result = processo.tempiChiusura[processo.indiceConfezioneInPesa][i + 1];
                    break;
                }
            }

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Calcolo Tempo Chiusura Materiale Mancante =" + matMancante + " - Tempo Chiusura = " + result);

        return result;
    }
    
    
    private void chiudiValvola() {
    	
    	///////////////////////////////////
        // CHIUSURA VALVOLA DI SCARICO  ///
        ///////////////////////////////////
        
        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Peso Confezione Non Raggiunto - Chiusura Valvola di Scarico");

		// Chiusura Valvola
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

			////////////////////////////
			// ATTUATORE MULTISTADIO  //
			////////////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
			ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
			
		} else {
			///////////////////
			// COMANDO UNICO //
			///////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
			ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
			 
		} 
    	
    }
    
    
    private void apriValvola() {
    	
    	///////////////////////////////////
        // APERTURA VALVOLA DI SCARICO  ///
        ///////////////////////////////////
    	
    	//Memorizzazione Log Processo
    	ProcessoLogger.logger.fine("Peso non Raggiunto Peso durante la Chiusura - Apertura Valvola di Scarico");

    	//Apertura Totale Valvola
    	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

    		///////////////////////////
    		// ATTUATORE MULTISTADIO //
    		///////////////////////////
    		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);
    		ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");

    	} else {

    		///////////////////
    		// COMANDO UNICO //
    		///////////////////
    		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);
    		ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100_COMANDO_UNICO");

    	}
    }
     
}
