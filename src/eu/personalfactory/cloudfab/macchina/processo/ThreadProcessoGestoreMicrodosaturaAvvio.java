/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_THREAD_RIP_AVVIA_MICRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_NUMERO_RIP_TENTATIVI_CONFIG;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoGestoreMicrodosaturaAvvio extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoGestoreMicrodosaturaAvvio(Processo processo) {
        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        ////////////////////////////////////////////////
        // INIZIALIZZAZIONE VARIABILI MICRODOSATURA  ///
        ////////////////////////////////////////////////
        //Dimensione ArrayList microdosatori
        int dimArrayListMicro;

        //Aggiornamento Visualizzazione Animazioni Pannello
        processo.pannelloProcesso.modificaPannello(1);

        ////////////////////////////////////
        // MICRODOSATORE SERIE 1 - 2017  ///
        ////////////////////////////////////
        dimArrayListMicro = processo.microdosatori_2017.size();

        //Dichiarazione array per il controllo delle microdosature completate
        processo.microdosatureEseguite = new boolean[dimArrayListMicro];
        processo.microdosatureEseguiteRegistrate = new boolean[dimArrayListMicro];

        for (int i = 0; i < processo.microdosatureEseguite.length; i++) {

            processo.microdosatureEseguite[i] = false;
            processo.microdosatureEseguiteRegistrate[i] = false;
        }

        ////////////////////////////
        // AVVIO  MICRODOSATURA  ///
        ////////////////////////////
        for (int i = 0; i < processo.microdosatori_2017.size(); i++) {

            if (!Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(processo.microdosatori_2017.get(i).getIdComponenteInPeso()))) {

                //////////////////////////
                // COMPONENTE DA PESARE //
                ////////////////////////// 
                int counterRip = 0;

                processo.microdosatureEseguite[i] = false;
                processo.microdosatureEseguiteRegistrate[i] = false;

                while (processo.microdosatori_2017.get(i).avviaPesatura().equals("")) {

                    ProcessoLogger.logger.log(Level.INFO,
                            "Tentativo avvio microdosatura Componente {0} - id_ciclo {1}",
                            new Object[]{processo.microdosatori_2017.get(i).getNomeComponente(), TrovaSingoloValoreParametroRipristino(91)});

                    try {
                        ThreadProcessoGestoreMicrodosaturaAvvio.sleep(FREQUENZA_THREAD_RIP_AVVIA_MICRO);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadProcessoGestoreMicrodosaturaAvvio.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (counterRip > MICRO_NUMERO_RIP_TENTATIVI_CONFIG) {

                        ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(37);

                        ProcessoLogger.logger.log(Level.SEVERE, "MICRO RUN ERROR MRUN0 - MICRO {0}", processo.componenti.get(i).getPresa());

                        //Finalizzazione Processo
                        processo.finalizzazioneProcesso();

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.warning("Finalizzazione Processo");

                        //Scambio Pannello
                        processo.pannelloProcesso.gestoreScambioPannello();

                    }
                    counterRip++;

                }

                TimeLineLogger.logger.log(Level.INFO,
                        "Avvio Microdosatura Componente {0} - id_ciclo {1}",
                        new Object[]{processo.microdosatori_2017.get(i).getNomeComponente(), TrovaSingoloValoreParametroRipristino(91)});

                //Registra Movimento di Magazzino
                processo.microdosatori_2017.get(i).setIdMovimentoMagazzino(RegistraNuovoMovimentoMagazzino(
                		processo.microdosatori_2017.get(i).getIdComponente(), //id_materiale
                        ParametriGlobali.parametri.get(117), //tipo_materiale
                        0, //qEffettivo
                        processo.componenti.get(processo.microdosatori_2017.get(i).getIdComponenteInPeso()).getCodiceMovimento_Magazzino(), //cod_ingresso_comp
                        DettagliSessione.getCodiceFigura(), //cod_operatore
                        "-1", //segno_op
                        ParametriGlobali.parametri.get(133), //procedura
                        ParametriGlobali.parametri.get(119), //tipo_mov//ParametriGlobali.parametri.get(122), //tipo_mov
                        ParametriGlobali.parametri.get(132), //descri_mov
                        processo.microdosatori_2017.get(i).getPresaComponente(), //id_silo
                        processo.microdosatori_2017.get(i).getPesoDesiderato(), Integer.parseInt(TrovaSingoloValoreParametroRipristino(91)),     //id_ciclo
                        ParametriGlobali.parametri.get(140), //origine_mov 
                        new Date(),			//DataMov
                        true,				//Abilitato
                        TrovaValoreParametriComponentePerIdComp(processo.microdosatori_2017.get(i).getIdComponente()).get(9),				//Info1
                        TrovaValoreParametriComponentePerIdComp(processo.microdosatori_2017.get(i).getIdComponente()).get(10),					//Info2 
                        "",					//Info3 					
                        "",					//Info4 
                        "",					//Info5 
                        "",					//Info6 
                        "",					//Info7 
                        "",					//Info8 
                        "",					//Info9 
                        "")); 				//Info10 
                   
            } else {

                /////////////////////////////
                // MICRODOSATURA ESEGUITA  //
                /////////////////////////////
                processo.microdosatureEseguite[i] = true;
            }

        }

        processo.controlloInizialeMicro = true;

        //Avvio thread di lettura e visualizzazione
        new ThreadProcessoGestoreMicrodosaturaComunicazione(processo).start();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Fine Thread Gestore Microdosatura Avvio");

    }

}
