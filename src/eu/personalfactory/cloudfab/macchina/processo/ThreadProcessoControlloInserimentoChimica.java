/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponenteChimicaPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiProdottoPerIdProdPerIdComponente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;

import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;

/**
 *
 * @author francescodigaudio
 *
 * Attende l'Apertura dello Sportello Miscelatore per l'inserimento della
 * chimica e del colore
 *
 */
public class ThreadProcessoControlloInserimentoChimica extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloInserimentoChimica(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Controllo Inserimento Chimica");

        //Inizializzazione Variabili
        boolean inserimentoEffettuato = false;
        boolean sportelloAperto = false;
        boolean statoDefaultContattoSportello = ParametriSingolaMacchina.parametri.get(18).equals("1");
        boolean prodottoColorato = Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(30));

        //Inizializzazione Visualizzazione
        processo.pannelloProcesso.labelImageAux[5].setVisible(true);

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                    OUTPUT_TRUE_CHAR);

        }
        int timer = 0;
        int indexImage = 0; 
        int contatore_simulazione = 0;

        TimeLineLogger.logger.log(Level.INFO, "Inizio Controllo Chimica Componenti - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        //Inizio loop
        while (!inserimentoEffettuato
                && !processo.processoInterrottoManualmente
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Controllo Inserimento Chimica in Esecuzione");

            if (!sportelloAperto) {

                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE) != statoDefaultContattoSportello
                        || (!TrovaComponenteChimicaPerIdProdotto(processo.prodotto.getId(),
                                ParametriSingolaMacchina.parametri.get(301))
                        && !Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(432)))) {

                    sportelloAperto = true;

                    processo.pannelloProcesso.labelImageAux[5].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[6].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[7].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[8].setVisible(true);
                    processo.pannelloProcesso.labelImageAux[9].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[10].setVisible(false);
                    timer = 0;
                    indexImage = 0;

                } else {
                	
                	if (ABILITA_SIMULAZIONE_PROCESSO) {
                		
                		contatore_simulazione++;
                		
                		sportelloAperto = (contatore_simulazione>30);
                		
                	}

                    //Gestisci Visualizzazione
                    if (timer > 0) {
                        timer = 0;
                        switch (indexImage) {

                            case 0: {

                                processo.pannelloProcesso.labelImageAux[5].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[6].setVisible(true);
                                processo.pannelloProcesso.labelImageAux[7].setVisible(false);
                                indexImage = 1;

                                break;
                            }
                            case 1: {

                                processo.pannelloProcesso.labelImageAux[5].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[6].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[7].setVisible(true);
                                indexImage = 2;

                                break;
                            }

                            case 2: {

                                processo.pannelloProcesso.labelImageAux[5].setVisible(true);
                                processo.pannelloProcesso.labelImageAux[6].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[7].setVisible(false);
                                indexImage = 0;

                                break;
                            }

                        }
                    } else {
                        timer++;
                    }

                }

            } else {
 
                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE) == statoDefaultContattoSportello) {

                    inserimentoEffettuato = true;

                    processo.pannelloProcesso.labelImageAux[8].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[9].setVisible(false);
                    processo.pannelloProcesso.labelImageAux[10].setVisible(false);

                    //Aggiorna Stato Codice Chimica
                    processo.registraChimicaUsata();

                    //Avvio Gestore Processo
                    processo.aggiornaGestoreProcesso();

                } else {
                	
                	
                	if (ABILITA_SIMULAZIONE_PROCESSO) {
                		
                		contatore_simulazione++;
                		
                		inserimentoEffettuato = (contatore_simulazione>60);
                		
                	}
                	
                    //Gestisci Visualizzazione
                    if (timer > 0) {
                        timer = 0;
                        switch (indexImage) {
                            case 0: {
                                processo.pannelloProcesso.labelImageAux[8].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[9].setVisible(true);
                                processo.pannelloProcesso.labelImageAux[10].setVisible(false);
                                indexImage = 1;
                                break;
                            }

                            case 1: {
                                processo.pannelloProcesso.labelImageAux[8].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[9].setVisible(false);

                                if (prodottoColorato) {

                                    //////////////////////////
                                    /// PRODOTTO COLORATO  ///
                                    //////////////////////////
                                    indexImage = 2;
                                    processo.pannelloProcesso.labelImageAux[10].setVisible(true);

                                } else {
                                    //////////////////////////////
                                    /// PRODOTTO NON COLORATO  ///
                                    //////////////////////////////

                                    indexImage = 0;
                                    processo.pannelloProcesso.labelImageAux[8].setVisible(true);

                                }
                                break;
                            }

                            case 2: {
                                processo.pannelloProcesso.labelImageAux[8].setVisible(true);
                                processo.pannelloProcesso.labelImageAux[9].setVisible(false);
                                processo.pannelloProcesso.labelImageAux[10].setVisible(false);
                                indexImage = 0;
                                break;
                            }
                        }

                    } else {
                        timer++;
                    }

                }

            }

            try {
                ThreadProcessoControlloInserimentoChimica.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(190)));
            } catch (InterruptedException ex) {
            }

        }//fine loop

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                    OUTPUT_FALSE_CHAR);
        }
        //Aggiorna movimento di magazzino chimica
        Integer qChimica = 0;
        List<?> componentiColl = TrovaComponentiProdottoPerIdProdPerIdComponente(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)),
                Integer.parseInt(TrovaSingoloValoreParametroRipristino(32)),
                Integer.parseInt(TrovaSingoloValoreParametroRipristino(99)),
                Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)));

        if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))) {

            /////////////////////
            // CHIMICA SFUSA  ///
            /////////////////////
            qChimica = Integer.parseInt(TrovaSingoloValoreParametroRipristino(103));

            //Aggiornamento Quantità Chimica Pesata
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(103),
                    103,
                    "0",
                    ParametriSingolaMacchina.parametri.get(15));

            //Aggiornamento Chimica Pesatura Manuale Eseguita
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(102),
                    102,
                    "false",
                    ParametriSingolaMacchina.parametri.get(15));

            //Aggiornamento Codice chimica Pesata Manualemnte
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(58),
                    58,
                    TrovaSingoloValoreParametroRipristino(101),
                    ParametriSingolaMacchina.parametri.get(15));

            //Aggiornamento Codice Chimica miscela Successiva
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(101),
                    101,
                    "",
                    ParametriSingolaMacchina.parametri.get(15));

            if (qChimica > 0) {
                RegistraNuovoMovimentoMagazzino(Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)), //id_materiale
                        ParametriGlobali.parametri.get(135), //tipo_materiale
                        qChimica, TrovaSingoloValoreParametroRipristino(58), //cod_ingresso_comp,
                        DettagliSessione.getCodiceFigura(), //cod_operatore,
                        "-1", //segno_op,
                        ParametriGlobali.parametri.get(151), //procedura
                        ParametriGlobali.parametri.get(119), //tipo_mov
                        ParametriGlobali.parametri.get(132), //descri_mov
                        ParametriGlobali.parametri.get(136), //id_silo,
                        qChimica, Integer.parseInt(TrovaSingoloValoreParametroRipristino(83)),         // id_ciclo   
                        ParametriGlobali.parametri.get(140), //origine_mov 
                        new Date(),			//DataMov
                        true,				//Abilitato
                        "",					//Info1
                        "",					//Info2 
                        "",					//Info3 					
                        "",					//Info4 
                        "",					//Info5 
                        "",					//Info6 
                        "",					//Info7 
                        "",					//Info8 
                        "",					//Info9 
                        ""); 				//Info10 
                
            }

        } else {

            ////////////////////////////
            // CHIMICA CONFEZIONATA  ///
            ////////////////////////////
            //Assegnazione dei Dati letti da Database
            for (Object o : componentiColl) {
                ComponentePesaturaOri componentiProdOri = (ComponentePesaturaOri) o;
                qChimica = componentiProdOri.getQuantita();

            }

            if (qChimica > 0) {
                RegistraNuovoMovimentoMagazzino(Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)), //id_materiale
                        ParametriGlobali.parametri.get(135), //tipo_materiale
                        qChimica, TrovaSingoloValoreParametroRipristino(58), //cod_ingresso_comp,
                        DettagliSessione.getCodiceFigura(), //cod_operatore,
                        "-1", //segno_op,
                        ParametriGlobali.parametri.get(151), //procedura
                        ParametriGlobali.parametri.get(119), //tipo_mov
                        ParametriGlobali.parametri.get(132), //descri_mov
                        ParametriGlobali.parametri.get(136), //id_silo,
                        qChimica, Integer.parseInt(TrovaSingoloValoreParametroRipristino(83)),         // id_ciclo   
                        ParametriGlobali.parametri.get(140), //origine_mov 
                        new Date(),			//DataMov
                        true,				//Abilitato
                        "",					//Info1
                        "",					//Info2 
                        "",					//Info3 					
                        "",					//Info4 
                        "",					//Info5 
                        "",					//Info6 
                        "",					//Info7 
                        "",					//Info8 
                        "",					//Info9 
                        ""); 				//Info10 
                
            }
        }

        TimeLineLogger.logger.log(Level.INFO, "Inizio Controllo Chimica Componenti - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Controllo Inserimento Chimica");

        //Aggiornamento Visualizzazione Pannello
        processo.pannelloProcesso.labelImageAux[5].setVisible(false);
        processo.pannelloProcesso.labelImageAux[6].setVisible(false);
        processo.pannelloProcesso.labelImageAux[7].setVisible(false);
        processo.pannelloProcesso.labelImageAux[8].setVisible(false);
        processo.pannelloProcesso.labelImageAux[9].setVisible(false);
        processo.pannelloProcesso.labelImageAux[10].setVisible(false);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Controllo Inserimento Chimica");

    }
}
