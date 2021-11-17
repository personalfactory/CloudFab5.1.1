/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;

import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 *
 * Controlla che la Coclea non sia Vuota
 *
 */
public class ThreadProcessoControlloBloccoScarico extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloBloccoScarico(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Inizio Procedura Controllo Blocco Scarico");

        int valorePesoPrec = 0;
        int valorePesoSucc = 0;
        int contatore = 0;
        int contatoreChiusure = 0;
        boolean contaTempo;
        boolean condizioneBloccoScarico = false;

        processo.interrompiThreadControlloBloccoScarico = false;
        processo.disattivaVarVelocitaMiscelatore = false;

        //Inizio loop
        while (!processo.interrompiThreadControlloBloccoScarico
                && processo.pannelloProcesso.isVisible()
                && !processo.pesoConfezioneRaggiunto
                && !processo.resetProcesso) {

            if (processo.valorePesaConfezioni != null) {

                try {
                    valorePesoPrec = Integer.parseInt(processo.valorePesaConfezioni);

                    ThreadProcessoControlloBloccoScarico.sleep(
                            Integer.parseInt(
                                    ParametriSingolaMacchina.parametri.get(55)));

                    valorePesoSucc = Integer.parseInt(processo.valorePesaConfezioni);
                } catch (NumberFormatException | InterruptedException ex) {

                    ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante il calcolo della portata e={0}", ex);

                }

                int portata = valorePesoSucc - valorePesoPrec;

                ProcessoLogger.logger.log(Level.INFO, "Portata Rilevata allo Scarico q={0} in {1} ms",
                        new Object[]{portata, ParametriSingolaMacchina.parametri.get(55)});

                //Valura Portata Sufficiente
                contaTempo = portata < Integer.parseInt(ParametriSingolaMacchina.parametri.get(53)); ///////////////////////

                if (contaTempo) {

                    ///////////////////////
                    // PORTATATA BASSA  ///
                    ///////////////////////
                    if (!condizioneBloccoScarico) {

                        ///////////////////////////////////
                        // BLOCCO SCARICO NON RILEVATO  ///
                        ///////////////////////////////////
                        if (contatore < Integer.parseInt(ParametriSingolaMacchina.parametri.get(310))) {

                            contatore++;

                            //Memorizzazione Log Processo
                            ProcessoLogger.logger.log(
                                    Level.CONFIG, "Thread Controllo Blocco Scarico = {0} / {1}",
                                    new Object[]{contatore, ParametriSingolaMacchina.parametri.get(54)});

                        } else {

                            ////////////////////////////////////
                            // CONDIZIONI DI BLOCCO SCARICO  ///
                            ////////////////////////////////////
                            //Memorizzazione Log Processo
                            ProcessoLogger.logger.warning("Rilevato Blocco Scarico");

                            //Interrompe il loop del Thread Corrente
                            condizioneBloccoScarico = true;

                            //Inizializzazione Contatore
                            contatore = 0;

                            //Controllo tipologia impianto
                            if (!processo.abilitaLineaDirettaMiscelatore) {

                                //Cambio Vel Miscelatore 
                                inverter_mix.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(312));

                                //Sopsensione Cambio Velocita Motore Miscelatore
                                processo.disattivaVarVelocitaMiscelatore = true;
                            }
                        }
                    } else {

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.warning("Blocco Scarico Rilevato");

                        ///////////////////////////////
                        // BLOCCO SCARICO RILEVATO  ///
                        ///////////////////////////////
                        if (contatoreChiusure < Integer.parseInt(ParametriSingolaMacchina.parametri.get(311))) {

                        	//Chiusura Valvola
                        	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

                        		///////////////////////////
                        		// ATTUATORE MULTISTADIO //
                        		///////////////////////////
                        		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF); 
                        		ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
                        	} else {

                        		///////////////////
                        		// COMANDO UNICO //
                        		///////////////////
                        		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO); 
                        		ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
                        	}

                            try {
                                ThreadProcessoControlloBloccoScarico.sleep(
                                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(313)));
                            } catch (InterruptedException ex) {

                                ProcessoLogger.logger.log(Level.SEVERE, "Errore durante l''attesa del tempo di chiusura-apertura  e={0}", ex);
                            }

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
                            	ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");

                            }

                            try {
                                ThreadProcessoControlloBloccoScarico.sleep(
                                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(313)));
                            } catch (InterruptedException ex) {

                                ProcessoLogger.logger.log(Level.SEVERE, "Errore durante l''attesa del tempo di chiusura-apertura  e={0}", ex);
                            }

                            contatoreChiusure++;

                        } else {

                            //Controllo tipologia impianto
                            if (!processo.abilitaLineaDirettaMiscelatore) {

                                //Inizializzazione velocità miscelatore
                                processo.inizializzazioneVelMiscelatore();

                                //Sopsensione Cambio Velocita Motore Miscelatore
                                processo.disattivaVarVelocitaMiscelatore = false;
                            }

                            condizioneBloccoScarico = false;

                            //Inizializzazione Contatori
                            contatore = 0;
                            contatoreChiusure = 0;

                        }
                    }

                } else {

                    ////////////////////////
                    // PORTATA ADEGUATA  ///
                    ////////////////////////
                    //Controllo tipologia impianto
                    if (!processo.abilitaLineaDirettaMiscelatore) {

                        ///////////////////////////////////////////////////////////////
                        // NUOVO IMPIANTO O VECCHIO DOTATO DI INVERTER MISCELATORE  ///
                        ///////////////////////////////////////////////////////////////
                        if (processo.disattivaVarVelocitaMiscelatore) {

                            //Inizializzazione velocità miscelatore
                            processo.inizializzazioneVelMiscelatore();

                            //Sopsensione Cambio Velocita Motore Miscelatore
                            processo.disattivaVarVelocitaMiscelatore = false;
                        }

                    }
                    condizioneBloccoScarico = false;

                    //Inizializzazione Contatori
                    contatore = 0;
                    contatoreChiusure = 0;
                }
            }
        } //fine loop

        //////////////////////////
        //////////////////////////
        ///////////////////////////
        ///////////////////////////
        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Fine Procedura Controllo Blocco Scarico");
    }
}
