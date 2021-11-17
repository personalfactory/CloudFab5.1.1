/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO;

import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoApriChiudiValvola;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadPuliziaApriChiudiValvola extends Thread {

    private boolean interrompiThreadApriChiudiValvola;
    private final Pulizia pulizia;

    public ThreadPuliziaApriChiudiValvola(Pulizia pulizia) {
        this.pulizia = pulizia;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        interrompiThreadApriChiudiValvola = false;

        while (!interrompiThreadApriChiudiValvola
                && pulizia.pannelloPulizia.isVisible()
                && !pulizia.pesoConfezioneRaggiunto) {

        	
        	if (!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO)) {

        		//////////////////////////////////////////////////////////////
        		// CHIUSURA VALVOLA DI SCARICO  POS_100 - VALVOLA CHIUSA  ///
        		/////////////////////////////////////////////////////////////

        		PuliziaLogger.logger.config("Chiusura Valvola di Scarico - POS_100");

        		//Chiusura Valvola
        		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

        			///////////////////////////
        			// ATTUATORE MULTISTADIO //
        			///////////////////////////
        			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
        			PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
        		} else {

        			////////////////////
        			// COMANDO UNICO  //
        			////////////////////
        			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
        			PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
        		}
        	}

            ////////////////////////////////////////
            // ATTESA TEMPO DI CHIUSURA VALVOLA  ///
            ////////////////////////////////////////
            //Aspetta Tempo di Chiusura
            int tempoChiusura = Integer.parseInt(ParametriSingolaMacchina.parametri.get(216));
            int numeroRipetizioniChiusura = tempoChiusura
                    / FRAZIONAMENTO_TEMPI;
            boolean interrompiTimerChiusura = false;
            int counterChiusura = 0;
 
            PuliziaLogger.logger.log(Level.CONFIG, "Attesa Tempo Chiusura ={0}", tempoChiusura);

            //Inizio loop 
            while (!interrompiTimerChiusura
                    && pulizia.pannelloPulizia.isVisible()
                    && !pulizia.pesoConfezioneRaggiunto) {

                counterChiusura++;

                if (counterChiusura >= numeroRipetizioniChiusura | pulizia.pesoMancanteConfezioni <= 0) {

                    interrompiTimerChiusura = true;

                }
                try {
                    ThreadProcessoApriChiudiValvola.sleep(FRAZIONAMENTO_TEMPI);
                } catch (InterruptedException ex) {
                }

            }//fine loop

            //////////////////////////////////////////////////////////////////////
            // VERIFICA RAGGIUNGIMENTO PESO DURANTE LA CHIUSURA DELLA VALVOLA  ///
            //////////////////////////////////////////////////////////////////////
            if (pulizia.pesoMancanteConfezioni > 0
                    && pulizia.pannelloPulizia.isVisible()
                    && !pulizia.pesoConfezioneRaggiunto) {

                ///////////////////////////////////////////
                // APERTURA VALVOLA DI SCARICO  POS_20  ///
                //////////////////////////////////////////
                boolean aperturaAnnullata = false;
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

                	///////////////////////////
                	// ATTUATORE MULTISTADIO //
                	///////////////////////////
                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_20); 
                	PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_20");

                }
                ////////////////////////////////
                // ATTESA TEMPO DI APERTURA  ///
                ////////////////////////////////
                boolean interrompiTimerApertura = false;
                int counterApertura = 0;
                int tempoApertura = Integer.parseInt(ParametriSingolaMacchina.parametri.get(215));
                int numeroRipetizioniApertura = tempoApertura
                        / FRAZIONAMENTO_TEMPI;
                
                //Memorizzazione Log Processo
                PuliziaLogger.logger.log(Level.CONFIG, "Attesa Tempo Apertura Valvola ={0}", tempoApertura);

                //Inizio loop
                while (!interrompiTimerApertura
                        && pulizia.pannelloPulizia.isVisible()
                        && !pulizia.pesoConfezioneRaggiunto
                        && !aperturaAnnullata) {

                    counterApertura++;

                    if (counterApertura >= numeroRipetizioniApertura | pulizia.pesoMancanteConfezioni <= 0) {

                        interrompiTimerApertura = true;


                        if (!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO)) {

                        	/////////////////////////////////
                        	// CHIUDI VALVOLA DI SCARICO  ///
                        	/////////////////////////////////

                        	//Chiusura Valvola
                        	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
                        		
                        		///////////////////////////
                        		// ATTUATORE MULTISTADIO //
                        		///////////////////////////
                        		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
                        		
                        		PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
                        	} else {
                        		////////////////////
                        		// COMANDO UNICO  //
                        		////////////////////
                        		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
                        		PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
                        	}

                        }

                    } else {
                        try {
                            ThreadProcessoApriChiudiValvola.sleep(FRAZIONAMENTO_TEMPI);
                        } catch (InterruptedException ex) {
                        }
                    }

                }

                if (pulizia.pesoMancanteConfezioni <= 0
                        || !pulizia.pannelloPulizia.isVisible()
                        || pulizia.pesoConfezioneRaggiunto) {

                    interrompiThreadApriChiudiValvola = true;

                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.config("Peso Raggiunto Durante l'Apertura della Valvola");
                }

            } else {

                ////////////////////////////////////////////////////////
                // PESO RAGGIUNTO DURANTE LA CHIUSURA DELLA VALVOLA  ///
                ////////////////////////////////////////////////////////
                interrompiThreadApriChiudiValvola = true;

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Peso Raggiunto Durante la Chiusura della Valvola");

            }

        }
    }

}
