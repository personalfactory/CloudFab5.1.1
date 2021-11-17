/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_TEMPO_MISCELAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_VELOCITA_MISCELAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIPETIZIONI_INVIO_AGGIORNAMENTO_PASSO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CARICO;
import java.io.IOException;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RIPETIZIONI_AZZERAMENTO;
/**
 *
 * @author francescodigaudio
 *
 *
 */
public class ThreadProcessoMiscelazione extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    ThreadProcessoMiscelazione(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Miscelazione");
         
        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(55))) {

                //PESA COMPONENTI COMPLETATA 
                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Pesa Componenti Eseguita");

                //Incremento Ultimo Passo Completato
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                        54,
                        "8",
                        ParametriSingolaMacchina.parametri.get(15));

                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Passo Corrente Impostato ad 8");

                //Thread di Attesa Completamento Miscelazione
                new ThreadProcessoControlloMiscelazione(processo).start();

            } else {

                //PESA COMPONENTI  INCOMPLETA
                //Aggiornamento Stato Parametri di Ripristino
                int miscelePesare = Integer.parseInt(TrovaSingoloValoreParametroRipristino(8));
                int miscelePesate = Integer.parseInt(TrovaSingoloValoreParametroRipristino(62));

                //Indice Miscelazione Completata
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(59),
                        59,
                        "false",
                        ParametriSingolaMacchina.parametri.get(15));

                //Controllo Miscele Residue
                if (miscelePesate == miscelePesare) {

                    ///////////////////////
                    /// ULTIMA MISCELA  ///
                    ///////////////////////
                    if (processo.pannelloProcesso.isVisible()
                            && !processo.resetProcesso) {

                        //Aggiornamento indice ultima miscela = true su database
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(61),
                                61,
                                "true",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.info("Controllo Pesa Successiva :Ultima Miscela");

                        //Incremento Ultimo Passo Completato
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                                54,
                                "8",
                                ParametriSingolaMacchina.parametri.get(15));

                        if (TrovaSingoloValoreParametroRipristino(54).equals("8")) {

                            //Memorizzazione Log Processo
                            ProcessoLogger.logger.info("Ultimo Passo Aggiornato Correttamento");

                        } else {
                            int i = 0;
                            while (i < RIPETIZIONI_INVIO_AGGIORNAMENTO_PASSO && !TrovaSingoloValoreParametroRipristino(54).equals("8")) {
                                //Memorizzazione Log Processo
                                ProcessoLogger.logger.warning("Attenzione Errore Aggiornamento Ultimo Passo");

                                AggiornaValoreParametriRipristino(processo,
                                        TrovaIndiceTabellaRipristinoPerIdParRipristino(54),
                                        54,
                                        "8",
                                        ParametriSingolaMacchina.parametri.get(15));

                                //Memorizzazione Log Processo
                                ProcessoLogger.logger.warning("Nuovo Tentativo Aggiornamento Ultimo Passo");

                                i++;
                            }

                        }

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.log(Level.CONFIG, "Passo Corrente Impostato ad (8) ={0}", TrovaSingoloValoreParametroRipristino(54));

                        //Thread di Attesa Completamento Miscelazione
                        new ThreadProcessoControlloMiscelazione(processo).start();

                    }

                } else {

                    ///////////////////////////////////
                    /// PESA SUCCESSIVA NECESSARIA  ///
                    ///////////////////////////////////
                	
                    if (processo.pannelloProcesso.isVisible()
                            && !processo.resetProcesso) {

                        //Aggiornamento indice ultima miscela = false su database
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(61),
                                61,
                                "false",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.info("Controllo Pesa Successiva :Necessaria");

                        processo.microdosaturaCompletata = false;
 
                        //Reset Valore iniziale di peso
                        for (int i = 0; i < Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)); i++) {
                            //Registra Pesa Componente Eseguita
                            AggiornaValoreParametriRipristino(processo,
                                    TrovaIndiceTabellaRipristinoPerIdParRipristino(71) + processo.counterConfigMicro,
                                    71,
                                    "0",
                                    ParametriSingolaMacchina.parametri.get(15));
                        }

                        //new ThreadProcessoGestoreProcesso(processo).start();

                        //Thread di Attesa Completamento Miscelazione e Pesa
                        new ThreadProcessoControlloMiscelazionePesa(processo).start();

                        //Animazione Carico
                        new ThreadProcessoAnimazioneCarico(processo).start();
                    }

                    if (processo.pannelloProcesso.isVisible()
                            && !processo.resetProcesso) {

                        try {

                            //Creazione Client Pesa di Carico
                        	ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
                        	
                            //Ripetizioni Commutazione Peso Netto
                            for (int i = 0; i < RIPETIZIONI_AZZERAMENTO; i++) {

                                //Commutazione Peso Netto
                                pesaCarico.commutaNetto();

                                ThreadProcessoControlloPesoCarico.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);
                                
                            }
                            //Chiusura del Client
                            pesaCarico.chiudi();
                            
                            ThreadProcessoControlloPesoCarico.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

                        } catch (IOException ex) {

                            //Memorizzazione Log Processo
                            ProcessoLogger.logger.log(Level.SEVERE, "Errore durante la creazione del Client Pesa e ={0}", ex);

                            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                                RegistraAllarme(8,
                                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 907, ParametriSingolaMacchina.parametri.get(111)))
                                        + " ="
                                        + ex,
                                        TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
                            }
                        } catch (InterruptedException e) {
							e.printStackTrace();
						}

                        processo.pannelloProcesso.modificaPannello(7);

                        //Analisi Stato Processo di Pesa
                        processo.analizzaPesaComponenti();

                    }

                }
            }
        }

        if (processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Attivazione Uscita Rele Motore Miscelatore");

            GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN,
                    OUTPUT_TRUE_CHAR);

            //Avvio Inverter Motore Miscelatore
            if (!processo.abilitaLineaDirettaMiscelatore) {

                //Attesa Tempo Delay Inverter
                try {
                    ThreadProcessoMiscelazione.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
                } catch (InterruptedException ex) {
                }

                //Cambio Vel Inverter
                String velMotoreMix = DEFAULT_VELOCITA_MISCELAZIONE;

                try {

                    String readVelMotoreMix = processo.prodotto.getMixingSpeed();

                    if (!readVelMotoreMix.equals("")) {
                        velMotoreMix = readVelMotoreMix;
                    }

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.log(Level.INFO, "Lettura Parametro Prodotto Velocita Miscelatore - vel ={0}", velMotoreMix);

                } catch (Exception e) {

                    ProcessoLogger.logger.log(Level.SEVERE, "Errore durante l''impostazione della velocita di miscelazione - e:{0}", e);
                }
 
                inverter_mix.cambiaVelInverter(velMotoreMix);

                //Memorizzazione Log Processo
                ProcessoLogger.logger.log(Level.INFO, "Cambio Velocita Miscelatore Eseguito - vel ={0}", velMotoreMix);

                //Avvio Motore Miscelatore 
                inverter_mix.avviaArrestaInverter(true);

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Avvio Inverter Eseguito");

                TimeLineLogger.logger.log(Level.INFO, "Avvio Miscelazione: Velocità = {1}, tempo ={2} - id_ciclo ={0} ", new Object[]{TrovaSingoloValoreParametroRipristino(83), velMotoreMix, processo.prodotto.getMixingTime()});

            }

            //Avvio Animazione Miscelatore
            new ThreadProcessoAnimazioneMiscelatore(processo).start();

        }

        //Attesa Tempo Miscelazione
        int timeToWait = Integer.parseInt(DEFAULT_TEMPO_MISCELAZIONE);

        try {

            timeToWait = processo.prodotto.getMixingTime() / 1000;

        } catch (Exception e) {
            ProcessoLogger.logger.log(Level.SEVERE, "Errore durante l''impostazione del tempo\u02d9 di miscelazione - e:{0}", e);

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Avvio Miscelazione Tempo di Miscelazione{0}", timeToWait);

        processo.pannelloProcesso.impostaVisLabelTempoResiduoMiscelazione(true);

        while (timeToWait > 0
                && processo.pannelloProcesso.isVisible()
                && !processo.processoInterrottoManualmente
                && !processo.resetProcesso) {

            //Modifica Valore Label Tempo Residuo
            processo.pannelloProcesso.impostaLabelTempoResiduoMiscelazione(Integer.toString(timeToWait));

            try {
                ThreadProcessoMiscelazione.sleep(1000);
            } catch (InterruptedException ex) {
            }
            timeToWait--;
        }

        //Modifica Valore Label Tempo Residuo
        processo.pannelloProcesso.impostaLabelTempoResiduoMiscelazione("");

        processo.pannelloProcesso.impostaVisLabelTempoResiduoMiscelazione(false);

        //Arresto Inverter Motore Miscelatore
        if (!processo.abilitaLineaDirettaMiscelatore) {

            //Arresto Motore Miscelatore 
            inverter_mix.avviaArrestaInverter(false);

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Arresto Inverter Miscelatore");

            //Attesa Tempo Delay Inverter
            try {
                ThreadProcessoMiscelazione.sleep(
                        Integer.parseInt(ParametriGlobali.parametri.get(8)));
            } catch (InterruptedException ex) {
            }
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Disattivazione Rele Motore Miscelatore");

        GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN,
                OUTPUT_FALSE_CHAR);

        //Registrazione completamento miscelazione su database
        AggiornaValoreParametriRipristino(processo,
                TrovaIndiceTabellaRipristinoPerIdParRipristino(59),
                59,
                "true",
                ParametriSingolaMacchina.parametri.get(15));

        TimeLineLogger.logger.log(Level.INFO, "Fine Miscelazione - id_ciclo ={0} ", new Object[]{TrovaSingoloValoreParametroRipristino(83)});

        //Interruzione Animazione Miscelatore
        processo.interrompiAnimMiscelatore = true;

        //Finalizzazione Processo di Miscelazione
        processo.miscelazioneCompletata = true;

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Fine Procedura Miscelazione");

    }

}
