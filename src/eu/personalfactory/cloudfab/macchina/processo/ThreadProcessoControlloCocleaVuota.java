/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 *
 * Controlla che la Coclea non sia Vuota
 *
 */
public class ThreadProcessoControlloCocleaVuota extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloCocleaVuota(Processo processo) {

        //Dichiarazione Varibale Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Controllo Coclea Vuota");

        processo.interrompiThreadControlloCocleaVuota = false;

        //Dichiarazione e inizializzazione Variabili
        int valorePesoPrec = 0;
        int valorePesoSucc = 0;
        boolean contaTempo;
        int index = 0;
        processo.interruzioneControlloPesoCocleaVuota = false;
        processo.autoRegVelAttiva = false;

        //Dichiarazione ArrayList e Estrapolazione limiti Contatore
        ArrayList<String> limContatore = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < ParametriSingolaMacchina.parametri.get(308).length(); i++) {
            if (ParametriSingolaMacchina.parametri.get(308).charAt(i)
                    == ParametriSingolaMacchina.parametri.get(309).charAt(0)) {
                limContatore.add(temp);
                temp = "";

            } else {
                temp += ParametriSingolaMacchina.parametri.get(308).charAt(i);
            }
        }
        limContatore.add(temp);

        //Dichiarazione e Inizializzazione Array cambio Eseguito 
        Boolean[] cambioEseguito = new Boolean[limContatore.size()];

        for (int i = 0; i < cambioEseguito.length; i++) {
            cambioEseguito[i] = false;
        }

        //Inizio loop
        while (!processo.interrompiThreadControlloCocleaVuota
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            if (processo.valorePesaCarico != null) {

                try {
                    valorePesoPrec = Integer.parseInt(processo.valorePesaCarico);

                    ThreadProcessoControlloCocleaVuota.sleep(
                            Integer.parseInt(
                                    ParametriSingolaMacchina.parametri.get(55)));

                    valorePesoSucc = Integer.parseInt(processo.valorePesaCarico);
                } catch (NumberFormatException | InterruptedException ex) {

                    ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante il calcolo della portata e={0}", ex);

                }

                int portata = valorePesoSucc - valorePesoPrec;

                ProcessoLogger.logger.log(Level.CONFIG, "Portata Rilevata q={0} in {1} ms", new Object[]{
                    portata,
                    ParametriSingolaMacchina.parametri.get(55)});

                contaTempo = portata < Integer.parseInt(ParametriSingolaMacchina.parametri.get(53));

                if (contaTempo) {

                    ///////////////////////
                    // PORTATATA BASSA  ///
                    ///////////////////////
                    if (index < Integer.parseInt(ParametriSingolaMacchina.parametri.get(54))) {

                        index++;

                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.log(Level.CONFIG, "Thread Controllo Coclea Vuota - Counter = {0} / {1}",
                                new Object[]{
                                    index,
                                    ParametriSingolaMacchina.parametri.get(54)});

                        /////////////////////////////////////////
                        // CONTROLLO AUTOREGOLAZIONE PORTATA  ///
                        /////////////////////////////////////////
                        //Analisi Autoregolazione Portata
                        for (int i = 0; i < limContatore.size(); i++) {

                            if (index >= Integer.parseInt(limContatore.get(i)) && !cambioEseguito[i]
                                    && !processo.pulsanteSpeedAttivato) {

                                cambioEseguito[i] = true;

                                processo.autoRegVelAttiva = true;

                                processo.corrAutoRegCarico++;

                                break;

                            }
                        }

                    } else {

                        //////////////////////////////////
                        // CONDIZIONI DI COCLEA VUOTA  ///
                        //////////////////////////////////
                        //Memorizzazione Log Processo
                        ProcessoLogger.logger.warning("Rilevata Coclea Vuota");
                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                            RegistraAllarme(1,
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 898, ParametriSingolaMacchina.parametri.get(111)))
                                    + " "
                                    + processo.componenti.get(processo.indiceCompInPesa).getPresa()
                                    + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 899, ParametriSingolaMacchina.parametri.get(111))),
                                    TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
                        }

                        //Interrompe il loop del Thread Corrente
                        processo.interrompiThreadControlloCocleaVuota = true;

                        //Impostazione Variabile di Controllo
                        processo.interruzioneControlloPesoCocleaVuota = true;

                    }

                } else if (!processo.forzaturaVelManualeInCorso) {

                    /////////////////////////////////////////////////////
                    // PROCEDURA NORMALE - PULSANTE SPEED NON PREMUTO ///
                    /////////////////////////////////////////////////////
                    //Inizializzazione Contatore
                    index = 0;

                    //Inizializzazione Variabile di Controllo
                    processo.autoRegVelAttiva = false;

                    processo.corrAutoRegCarico = 0;

                    processo.corrAutoRegCaricoPrec = 0;

                    //Inizializzazione Array controllo cambio Vel Eseguito
                    for (int i = 0; i < cambioEseguito.length; i++) {
                        cambioEseguito[i] = false;
                    }

                }

                if (processo.pulsanteSpeedAttivato) {

                    /////////////////////////////////////////
                    // AUTOREGOLAZIONE PORTATA  MANUALE   ///
                    /////////////////////////////////////////
                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.warning("Incremento Vel pulsante Speed");

                    processo.corrAutoRegCarico++;

                    processo.autoRegVelAttiva = true;

                    processo.pulsanteSpeedAttivato = false;

                    processo.disattivaVariazioneVel = true;

                    //Impostazione Visibilità Pulsante
                    processo.pannelloProcesso.elemBut[4].setVisible(true);
                    processo.pannelloProcesso.elemLabelSimple[29].setVisible(true);

                }
            }
        } //fine loop

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Controllo Coclea Vuota");
    }
}
