/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIPETIZIONI_AGGIORNAMENTO_STATO_SVUOTA_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 *
 * Attende l'Apertura dello Sportello Miscelatore per l'inserimento della
 * chimica e del colore
 *
 */
public class ThreadProcessoControlloInserimentoMaterialeSvuotaValvola extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloInserimentoMaterialeSvuotaValvola(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Controllo Inserimento Materiale Svuota Valvola Prima Miscela");

        TimeLineLogger.logger.log(Level.INFO, "Avvio Processo Reinserimento Materiale Svuota Valvola - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        //Inizializzazione Variabili
        boolean inserimentoEffettuato = false;
        boolean sportelloAperto = false;
        boolean statoDefaultContattoSportello = ParametriSingolaMacchina.parametri.get(18).equals("1");

        processo.threadInserimentoManualeInEsecuzione = true;

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Sblocco Sacco Svuota Valvola Prima miscela");

        //Aggiorna Visualizzazione Pannello
        processo.pannelloProcesso.modificaPannello(15);

        GestoreIO_ModificaOut(
                USCITA_LOGICA_EV_BLOCCA_SACCO,
                OUTPUT_FALSE_CHAR);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Sacco Sbloccato");

        //Inizializzazione Visualizzazione
        processo.pannelloProcesso.labelImageAux[5].setVisible(true);

        int timer = 0;
        int indexImage = 0;

        //Inizio loop
        while (!inserimentoEffettuato
                && !processo.processoInterrottoManualmente
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Controllo Inserimento Materiale Svuota Valvola Prima Miscela in Esecuzione");

            if (!sportelloAperto) { 
                
                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE) != statoDefaultContattoSportello) {

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

                    //Modifica Maggio 2015 
                    boolean registrazioneEseguita = false;
                    int i = 0;

                    while (i < RIPETIZIONI_AGGIORNAMENTO_STATO_SVUOTA_VALVOLA && !registrazioneEseguita) {

                        try {

                            registrazioneEseguita = !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(77));

                            //Memorizzazione Log Processo
                            ProcessoLogger.logger.info("Registrazione Aggiornamento Svuota Tubo Eseguito");

                        } catch (Exception e) {
                        }

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.warning("Attenzione Errore Aggiornamento Ultimo Passo");

                        //Aggiornamento Stato Svuotamento Valvola Eseguito 
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(77),
                                77,
                                "false",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.warning("Nuovo Tentativo Registrazione Aggiornamento Svuota Tubo Eseguito");

                        i++;

                        try {
                            ThreadProcessoGestoreProcesso.sleep(200);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ThreadProcessoGestoreProcesso.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.log(Level.INFO, "Registrazione Aggiornamento Svuota Tubo - ripetizioni ={0}", i);

                    //Avvio Gestore Processo
                    processo.aggiornaGestoreProcesso();

                } else {
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
                                processo.pannelloProcesso.labelImageAux[8].setVisible(true);
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
                ThreadProcessoControlloInserimentoMaterialeSvuotaValvola.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(190)));
            } catch (InterruptedException ex) {
            }

        }//fine loop

        TimeLineLogger.logger.log(Level.INFO, "Fine Processo Reinserimento Materiale Svuota Valvola - id_ciclo ={0}", TrovaSingoloValoreParametroRipristino(83));

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Controllo Inserimento Materiale Svuota Valvola Prima Miscela");

        //Aggiornamento Visualizzazione Pannello
        processo.pannelloProcesso.labelImageAux[5].setVisible(false);
        processo.pannelloProcesso.labelImageAux[6].setVisible(false);
        processo.pannelloProcesso.labelImageAux[7].setVisible(false);
        processo.pannelloProcesso.labelImageAux[8].setVisible(false);
        processo.pannelloProcesso.labelImageAux[9].setVisible(false);
        processo.pannelloProcesso.labelImageAux[10].setVisible(false);

        processo.threadInserimentoManualeInEsecuzione = false;

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Controllo Inserimento Materiale Svuota Valvola Prima Miscela");

    }
}
