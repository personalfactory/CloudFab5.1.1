/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaCodiceSacco;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponenteChimicaPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaPresenzaComponentiPesaturaManuale;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_COD_SACCHETTO_TRACC_DISAB;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIPETIZIONI_INVIO_AGGIORNAMENTO_PASSO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import java.util.logging.Level;
import java.util.logging.Logger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;

/**
 *
 * @author francescodigaudio
 *
 * Gestisce i Vari Step del Processo
 *
 *
 */
public class ThreadProcessoGestoreProcesso extends Thread {

    //private int ultimoPasso;
    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoGestoreProcesso(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    //Registrazione Ultimo Passo Eseguito
    public void incrementoPasso() {

        processo.ultimoPasso++;

        AggiornaValoreParametriRipristino(processo,
                TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                54,
                Integer.toString(processo.ultimoPasso),
                ParametriSingolaMacchina.parametri.get(15));

        boolean registrazioneEseguita = false;

        try {

            registrazioneEseguita = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54)) == processo.ultimoPasso;

        } catch (NumberFormatException e) {
        }

        if (registrazioneEseguita) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Ultimo Passo Aggiornato Correttamente");

        } else {

            int i = 0;

            while (i < RIPETIZIONI_INVIO_AGGIORNAMENTO_PASSO && !registrazioneEseguita) {

                try {

                    registrazioneEseguita = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54)) == processo.ultimoPasso;

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.warning("Forzatura Aggiornamento Ultimo Passo Eseguito");

                } catch (NumberFormatException e) {
                }

                //Memorizzazione Log Processo
                ProcessoLogger.logger.warning("Attenzione Errore Aggiornamento Ultimo Passo");

                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                        54,
                        Integer.toString(processo.ultimoPasso),
                        ParametriSingolaMacchina.parametri.get(15));

                //Memorizzazione Log Processo
                ProcessoLogger.logger.warning("Nuovo Tentativo Aggiornamento Ultimo Passo");

                i++;

                try {
                    ThreadProcessoGestoreProcesso.sleep(100);
                } catch (InterruptedException ex) {
                
                }
            }

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.WARNING, "Procedura Aggiornamento Ultimo Passo - ripetizioni ={0}", i);

        }
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Gestore Processo");

        try {
            //Lettura Ultimo Passo
            processo.ultimoPasso = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54));
        } catch (NumberFormatException e) {

            ProcessoLogger.logger.warning("Errore Lettura Ultimo Passo");
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Ultimo Passo :{0}", processo.ultimoPasso);

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            switch (processo.ultimoPasso) {

                case 0: {

                    ////////////////////////////
                    /// PESATURA COMPONENTI  ///
                    ////////////////////////////
                    TimeLineLogger.logger.log(Level.INFO, "Inizio Pesatura Componenti - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(91));

                    //Inizializzazione Valore Peso Carico
                    processo.valorePesaCarico = "0";

                    //Incremento Passo
                    incrementoPasso();

                    //Procedura di Controllo Stato Processo di Pesatura
                    processo.analizzaPesaComponenti();

                    //Aggiornamento Visualizzazione Animazioni Pannello
                    processo.pannelloProcesso.modificaPannello(0);

                    //Avvio Pesa Componenti Complementari
                    //new GestoreCompComplementari().start();
                    //Animazione Carico
                    new ThreadProcessoAnimazioneCarico(processo).start();

                    break;
                }
                case 1: {

                    //////////////////////////
                    /// SCARICO MATERIALI  ///
                    ////////////////////////// 
                    
                    //Verifica Pesatura Chimica Sfusa
                    if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(102))){
                        processo.pesaturaManualeCompletata = false;
                        processo.pannelloProcesso.elemBut[16].setVisible(true);
                    }
                    
                    
                    
                    //Aggiornamento Visualizzazione Animazioni Pannello
                    processo.pannelloProcesso.modificaPannello(1);

                    //Interruzione Animazione Carico
                    processo.interrompiAnimCarico = true;

                    //Incremento Passo
                    incrementoPasso();

                    //Aggiornamento Variabile di Controllo
                    if (processo.dosaturaDirettaAttiva) {

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.info("Dosatura Diretta non Attiva");

                        processo.dosaturaDirettaAttiva = false;

                        new ThreadProcessoDisattivaDosaturaDiretta(processo).start();

                    } else {

                        //Avvio Thread Apertura/Chiusura Valvola di Carico
                        new ThreadProcessoScaricoMateriali(processo).start();

                    }

                    break;
                }

                case 2: {

                    ///////////////////////////////////////////////
                    /// CONTROLLO E INSERIMENTO CODICE CHIMICA  ///
                    ///////////////////////////////////////////////
                    //Incremento Passo
                    incrementoPasso();

                    ///////////////////
                    /// ASPIRATORE  ///
                    ///////////////////
                    processo.aspiratoreAttivato = false;

                    //Controllo se la Chimica è Sfusa o Preconfezionata
                    if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))) {

                        ////////////////////////////////
                        /// CHIMICA PRECONFEZIONATA  ///
                        ////////////////////////////////
                        if ((processo.prodotto.getNome().substring(
                                0,
                                ParametriSingolaMacchina.parametri.get(143).length())
                                .equals(
                                        ParametriSingolaMacchina.parametri.get(143)))
                                || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

                            //Azzeramento Valore Codice Chimica
                            AggiornaValoreParametriRipristino(processo,
                                    TrovaIndiceTabellaRipristinoPerIdParRipristino(58),
                                    58,
                                    ParametriSingolaMacchina.parametri.get(144),
                                    ParametriSingolaMacchina.parametri.get(15));

                            //Incremento Passo
                            incrementoPasso();

                            boolean compManuali = false;
                            for (int i = 0; i < processo.componenti.size(); i++) {

                                if (processo.componenti.get(i).getMetodoPesa().equals(ParametriGlobali.parametri.get(147))) {
                                    compManuali = true;
                                    break;
                                }

                            }

                            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))
                                    || (processo.prodotto.getNome().substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                                    && (VerificaPresenzaComponentiPesaturaManuale(processo)
                                    || TrovaComponenteChimicaPerIdProdotto(processo.prodotto.getId(),
                                            ParametriSingolaMacchina.parametri.get(301)))
                                    || compManuali) {

                                //Thread di Controllo Apertura e Chiusura Sportello
                                new ThreadProcessoControlloInserimentoChimica(processo).start();

                                //Aggiornamento Visualizzazione Animazioni Pannello
                                processo.pannelloProcesso.modificaPannello(3);

                            } else {

                                //Memorizzazione Log Processo
                                ProcessoLogger.logger.info("Controllo Inserimento Codice Chimica Ignorato");

                                //Aggiornamento Visualizzazione Pannello
                                processo.pannelloProcesso.labelImageAux[5].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[6].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[7].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[8].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[9].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[10].setVisible(false);
                                //Avvio Gestore Processo
                                processo.aggiornaGestoreProcesso();
                            }

                        } else {

                            processo.pannelloProcesso.modificaPannello(2);
                        }
                    } else {

                        ///////////////////////
                        //// CHIMICA SFUSA  ///
                        ///////////////////////
                        //Thread di Controllo Apertura e Chiusura Sportello
                        new ThreadProcessoControlloInserimentoChimica(processo).start();

                        //Aggiornamento Visualizzazione Animazioni Pannello
                        processo.pannelloProcesso.modificaPannello(3);

                        //Incremento Passo
                        incrementoPasso();

                    }

                    break;
                }
                case 3: {

                    //Impostazione Visibilità Sfondo Tastiera
                    processo.pannelloProcesso.labelImageAux[17].setVisible(false);

                    processo.pannelloProcesso.elemBut[1].setVisible(false);

                    ////////////////////////////
                    /// INSERIMENTO CHIMICA  ///
                    ////////////////////////////
                    //Thread di Controllo Apertura e Chiusura Sportello
                    new ThreadProcessoControlloInserimentoChimica(processo).start();

                    ///Aggiornamento Visualizzazione Animazioni Pannello
                    processo.pannelloProcesso.modificaPannello(3);

                    //Incremento Passo
                    incrementoPasso();

                    break;
                }
                case 4: {

                    //////////////////
                    // ASPIRATORE  ///
                    //////////////////
                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

                        ////////////////////////////////////////////////////////
                        // ASPIRATORE CON LINEE SEPARATE E VALVOLE DEDICATE  ///
                        ////////////////////////////////////////////////////////
                        GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO + OUTPUT_SEP_CHAR
                                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                                OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR);

                    }

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

                        processo.pannelloProcesso.elemBut[14].setVisible(false);

                    }

                    /////////////////////////////////
                    /// INSERIMENTO CODICE SACCO  ///
                    /////////////////////////////////
                    incrementoPasso();

                    if (!processo.svuotamento_valvova_da_eseguire) {

                        ///////////////////////////////////////////
                        // SVUOTAMENTO VALVOLA DA NON ESEGUIRE  ///
                        ///////////////////////////////////////////
                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(441))) {

                            //////////////////////////////////////
                            // CONTEGGIO AUTOMATICO ABILITATO  ///
                            //////////////////////////////////////
                            AggiornaCodiceSacco(processo);

                            //Interrompe Animazione Blocco Sacchetto
                            processo.interrompiAnimConfezione = true;

                            processo.pannelloProcesso.modificaPannello(13);

                            //Impostazione Visibilità Sfondo Tastiera
                            processo.pannelloProcesso.labelImageAux[17].setVisible(false);

                            processo.pannelloProcesso.elemBut[1].setVisible(false);

                            //Memorizzazione Log Processo
                            ProcessoLogger.logger.config("Fine Loop Animazione Carico - Controllo Inserimento Codice Chimica");

                            if (TrovaSingoloValoreParametroRipristino(54).equals("5")) {

                                //Memorizzazione Log Processo
                                ProcessoLogger.logger.config("Prima Confezione");

                                //Avvio Gestore Processo
                                processo.aggiornaGestoreProcesso();

                            } else {

                                //Memorizzazione Log Processo
                                ProcessoLogger.logger.config("Confezioni Successive");

                                //Aggiorna Visualizzazione Panello
                                processo.pannelloProcesso.modificaPannello(5);

                                //Riattivazione Controllo Blocco Sacchetto
                                new ThreadProcessoControlloBloccoSacchetto(processo).start();
                            }

                        } else {

                            if (processo.disattivaTracc) {

                                //////////////////////////////////
                                // TRACCIABILITA' DISATTIVATA  ///
                                //////////////////////////////////
                                //Indice Codice Sacchetto Inserito
                                AggiornaValoreParametriRipristino(processo,
                                        TrovaIndiceTabellaRipristinoPerIdParRipristino(46),
                                        46,
                                        "true",
                                        ParametriSingolaMacchina.parametri.get(15));

                                //Codice Sacchetto
                                AggiornaValoreParametriRipristino(processo,
                                        TrovaIndiceTabellaRipristinoPerIdParRipristino(47),
                                        47,
                                        DEFAULT_COD_SACCHETTO_TRACC_DISAB,
                                        ParametriSingolaMacchina.parametri.get(15));

                                //Interrompe Animazione Blocco Sacchetto
                                processo.interrompiAnimConfezione = true;

                                processo.pannelloProcesso.modificaPannello(13);

                                //Impostazione Visibilità Sfondo Tastiera
                                processo.pannelloProcesso.labelImageAux[17].setVisible(false);

                                processo.pannelloProcesso.elemBut[1].setVisible(false);

                                //Memorizzazione Log Processo
                                ProcessoLogger.logger.config("Fine Loop Animazione Carico - Controllo Inserimento Codice Chimica");

                                if (TrovaSingoloValoreParametroRipristino(54).equals("5")) {

                                    //Memorizzazione Log Processo
                                    ProcessoLogger.logger.config("Prima Confezione");

                                    //Avvio Gestore Processo
                                    processo.aggiornaGestoreProcesso();

                                } else {

                                    //Memorizzazione Log Processo
                                    ProcessoLogger.logger.config("Confezioni Successive");

                                    //Aggiorna Visualizzazione Panello
                                    processo.pannelloProcesso.modificaPannello(5);

                                    //Riattivazione Controllo Blocco Sacchetto
                                    new ThreadProcessoControlloBloccoSacchetto(processo).start();
                                }

                            } else {

                                ////////////////////////////////////////////////
                                // PROCEDURA STANDARD DI INSERIMENTO CODICE  ///
                                ////////////////////////////////////////////////
                                // SVUOTA VALVOLA PRIMA MISCELA DISATTIVATO
                                //Thread Animazione Confezione
                                new ThreadProcessoAnimazioneConfezione(processo).start();

                                //Aggiornamento Visualizzazione Animazioni Pannello
                                processo.pannelloProcesso.modificaPannello(4);

                                TimeLineLogger.logger.log(Level.INFO, "Inserimento Codice Primo Sacco - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

                            }

                        }

                    } else {

                        ///////////////////////////////////////
                        // SVUOTAMENTO VALVOLA DA ESEGUIRE  ///
                        ///////////////////////////////////////
                        //Avvio Gestore Processo
                        processo.aggiornaGestoreProcesso();

                    }
                    break;
                }
                case 5: {

                    //Reset Condice Chimica Sfusa -- Correzione 5/03/2014
                    processo.codChimicaSfusa = "";

                    ///////////////////////
                    /// BLOCCO SACCO  /////
                    ///////////////////////
                    //Incremento Passo
                    incrementoPasso();

                    //Thread Controllo Blocco Sacchetto
                    new ThreadProcessoControlloBloccoSacchetto(processo).start();

                    //Aggiornamento Visualizzazione Animazioni Pannello
                    processo.pannelloProcesso.modificaPannello(5);

                    break;
                }
                case 6: {

                    /////////////////////
                    /// MISCELAZIONE  ///
                    /////////////////////
                    //Incremento Passo
                    incrementoPasso();

                    //Aggiornamento Visualizzazione Animazioni Pannello
                    processo.pannelloProcesso.modificaPannello(6);
 
                    //Esecuzione Miscelazione
                    new ThreadProcessoMiscelazione(processo).start();
                    
                    break;
                }

                case 7: {

                    ///////////////////////////////////////
                    /// MISCELAZIONE E PESA SUCCESSIVA  ///
                    ///////////////////////////////////////
                	 
                    if (processo.numCompMicrodosatura == 0) {

                        ////////////////////////////////////
                        // PROCESSO SENZA MICRODOSATURA  ///
                        ////////////////////////////////////
                        //Incremento Passo
                        incrementoPasso();

                        //Aggiornamento Visualizzazione Animazioni Pannello
                        processo.pannelloProcesso.modificaPannello(8);

                        //Interruzione Animazione Carico
                        //processo.interrompiAnimCarico = true;

                        //Registrazione Pesa Successiva Completata
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(60),
                                60,
                                "true",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Aggiornamento Variabile di Controllo
                        processo.pesaSuccessivaCompletata = true;

                    } else if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(52))) {

                        ////////////////////////////////////////////
                        // PROCESSO CON MICRODOSATURA COMPLETATA ///
                        ////////////////////////////////////////////
                        //Registrazione Pesa Successiva Completata
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(60),
                                60,
                                "true",
                                ParametriSingolaMacchina.parametri.get(15));

                    } else {

                        ////////////////////////////////////////////////
                        // PROCESSO CON MICRODOSATURA NON COMPLETATA ///
                        ////////////////////////////////////////////////
                        //Aggiornamento Visualizzazione Animazioni Pannello
                        processo.pannelloProcesso.modificaPannello(8);

                        //Interruzione Animazione Carico
                        //processo.interrompiAnimCarico = true;

                        //Aggiornamento Variabile di Controllo
                        processo.pesaSuccessivaCompletata = true;

                        //Incremento Passo
                        incrementoPasso();

                    }

                    break;

                }

                case 8: {

                    ////////////////////////
                    /// PESA CONFEZIONI  ///
                    ////////////////////////
                    //Aggiornamento Valore Indice Confezione da Pesare
                    processo.indiceConfezioneInPesa = Integer.parseInt(TrovaSingoloValoreParametroRipristino(49));

                    try {
                    	
                        ///////////////////////////////////////////////////
                        // PESATURA BILANCIA STANDARD PERSONAL FACTORY  ///
                        ///////////////////////////////////////////////////
                    	
                        //Procedura di Pesatura Sacchetti
                        processo.analizzaPesaConfezioni();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadProcessoGestoreProcesso.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;

                }

                case 9: {

                    //////////////////////////////////////////
                    /// SBLOCCO E RIBALTAMENTO CONFEZIONI  ///
                    //////////////////////////////////////////
                    //Incremento Passo
                    incrementoPasso();

                    if (!processo.forzaPesaAggiuntiva && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(363))) {
                        processo.pannelloProcesso.elemBut[13].setVisible(true);
                    }

                    //Avvio Thread Sblocco e Ribaltamento Confezione
                    new ThreadProcessoSbloccaRibaltaConfezione(processo).start();

                    break;
                }

                case 10: {

                    /////////////////////////////////
                    /// FINALIZZAZIONE PROCEDURA  ///
                    ///////////////////////////////// 
                    //Finalizzazione e Chiusura Metodi Processo
                    processo.finalizzazioneProcesso();

                    //Aggiornamento Visualizzazione Pannello
                    processo.pannelloProcesso.modificaPannello(12);

                    break;
                }

                default: {

                    //Memorizzazione Logger della Sessione
                    ProcessoLogger.logger.severe("Errore di Gestione Processo...");

                    break;

                }
            }

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Gestore Processo");
    }

////////////    private class ThreadAttendiPesaturaComponentiManuali extends Thread {
////////////
////////////        @Override
////////////        public void run() {
////////////
////////////            while (!processo.pesaturaManualeComponentiCompletata) {
////////////
////////////                try {
////////////                    ThreadAttendiPesaturaComponentiManuali.sleep(1000);
////////////                } catch (InterruptedException ex) {
////////////                    Logger.getLogger(ThreadProcessoGestoreProcesso.class.getName()).log(Level.SEVERE, null, ex);
////////////                }
////////////
////////////                ProcessoLogger.logger.info("Attesa Completamento Pesatura Componenti Manuali");
////////////
////////////            }
////////////            
////////////            processo.threadPesaturaManualeEseguito = false;
////////////             
////////////            //Aggiornamento Visualizzazione Animazioni Pannello
////////////            processo.pannelloProcesso.modificaPannello(6);
////////////
////////////            //Esecuzione Miscelazione
////////////            new ThreadProcessoMiscelazione(processo).start();
////////////        }
////////////
////////////    }
////                    
}
