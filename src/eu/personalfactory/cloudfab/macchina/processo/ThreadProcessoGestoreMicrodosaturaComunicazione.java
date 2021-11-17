/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaDatiMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ControlloTolleranzediPesoComponentiProcesso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraPesaturaComponenteMicrodosatura;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_FREQ_AGGIORNAMENTO_STATO_MICRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_FREQ_THREAD_CONTROLLO_MICRODOSATURE_ESEGUITE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoGestoreMicrodosaturaComunicazione extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoGestoreMicrodosaturaComunicazione(Processo processo) {
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
        boolean controlloFineMicrodosatori = false;

        processo.visibilitaMicro = true;
        processo.pannelloProcesso.impostaVisElementiPannelloMicro(true);

        //Conviene impostarlo su un valore errato in modo da forzare l'inzializzazione del pannello
        int indiceMicroPrec = 999;
        processo.counterMicroVis = 0;
        processo.indiceMicroVisibile = 0;

        //Inizio Loop
        while (!controlloFineMicrodosatori
                && !processo.interrompiControlloMicrodosatori /// DEFINIRE LA SOSPENSIONE DEL THREAD
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            ////////////////////////////////////
            // MICRODOASTORE SERIE 1 - 2017  ///
            ////////////////////////////////////
            //try {
            String rep_micro = processo.microdosatori_2017.get(processo.indiceMicroVisibile).leggiPeso();

            if (rep_micro.length() > 1) {

                try {
                    processo.microdosatori_2017.get(processo.indiceMicroVisibile).setStatoMicrodosatura(rep_micro.substring(0, 1));

                    processo.microdosatori_2017.get(processo.indiceMicroVisibile).setPesoCorrente(Integer.parseInt(rep_micro.substring(1, rep_micro.length())));

                    processo.microdosatori_2017.get(processo.indiceMicroVisibile).setPesoComponente(
                            processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoIniziale()
                            - processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoCorrente());

                    ProcessoLogger.logger.log(Level.INFO, "Microdosatura Componente {0} - peso ={2} - id_ciclo{1} ",
                            new Object[]{
                                processo.microdosatori_2017.get(processo.indiceMicroVisibile).getNomeComponente(),
                                TrovaSingoloValoreParametroRipristino(91),
                                processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoComponente()});

                    agggiornaPesoCorrenteComponentePesato();
                    aggiornaStatoMicro();

                } catch (NumberFormatException e) {

                }

            }

            if (indiceMicroPrec != processo.indiceMicroVisibile) {
                aggiornaInfoMicro();
                indiceMicroPrec = processo.indiceMicroVisibile;
            }

            if (processo.counterMicroVis < MICRO_FREQ_AGGIORNAMENTO_STATO_MICRO) {
                processo.counterMicroVis++;
            } else {

                processo.counterMicroVis = 0;

                if (processo.indiceMicroVisibile < processo.microdosatori_2017.size() - 1) {
                    processo.indiceMicroVisibile++;

                } else {

                    processo.indiceMicroVisibile = 0;

                }

            }

            controlloFineMicrodosatori = true;

            for (int i = 0; i < processo.microdosatori_2017.size(); i++) {

                if (!processo.microdosatureEseguiteRegistrate[i]
                        && !processo.microdosatureEseguite[i]) {

                    controlloFineMicrodosatori = false;

                    if (processo.microdosatori_2017.get(i).getStatoMicrodosatura().equals("3")) {
                        //&& !processo.microdosatureEseguiteRegistrate[i]) {

                        processo.pannelloProcesso.impostaVisPulsanteMultifuzioneMicro(false);

                        /////////////////////////////////
                        // MICORODOSATURA COMPLETATA  ///
                        /////////////////////////////////
                        processo.microdosatureEseguite[i] = true;
                        processo.microdosatureEseguiteRegistrate[i] = true;

                        //Registrazione su database e aggiornamento formula
                        RegistraPesaturaComponenteMicrodosatura(processo, processo.microdosatori_2017.get(i).getIdComponente(),
                                processo.microdosatori_2017.get(i).getPesoComponente(),
                                processo.microdosatori_2017.get(i).getIdComponenteInPeso());

                        //Analizza Errore Dosaggio
                        ControlloTolleranzediPesoComponentiProcesso(
                                processo.microdosatori_2017.get(i).getPesoDesiderato(),
                                Integer.toString(processo.microdosatori_2017.get(i).getPesoComponente()),
                                processo.microdosatori_2017.get(i).getTolleranzaInEccesso(),
                                processo.microdosatori_2017.get(i).getTolleranzaInDifetto(),
                                processo.microdosatori_2017.get(i).getNomeComponente());

                        //Aggiorna dati movimenti di magazzino
                        AggiornaDatiMovimentoMagazzino(
                                processo.microdosatori_2017.get(i).getIdMovimentoMagazzino(),
                                processo.microdosatori_2017.get(i).getPesoComponente());

                        TimeLineLogger.logger.log(Level.INFO, "Fine Microdosatura Componente {0} - id_ciclo{1}",
                                new Object[]{
                                    processo.microdosatori_2017.get(i).getNomeComponente(),
                                    TrovaSingoloValoreParametroRipristino(91)});

                    }

                }
            }

            switch (processo.microdosatori_2017.get(processo.indiceMicroVisibile).getStatoMicrodosatura()) {

                case "0":
                    //////////////////////////////////
                    // MICORODOSATORE IN STAND BY  ///
                    //////////////////////////////////

                    // DA IMPLEMENTARE
                    break;

                case "1":
                    //////////////////////////////////
                    // MICORODOSATORE CONFIGURATO  ///
                    //////////////////////////////////

                    // DA IMPLEMENTARE
                    break;

                case "2":
                    /////////////////////////////
                    // MICORODOSATORE IN RUN  ///
                    /////////////////////////////

                    processo.pannelloProcesso.impostaVisPulsanteMultifuzioneMicro(true);

                    //Inizializza Testo Pulsante
                    processo.pannelloProcesso.elemLabelSimple[23].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 818, ParametriSingolaMacchina.parametri.get(111)));

                    break;

                case "3":
                    /////////////////////////////////
                    // MICORODOSATURA COMPLETATA  ///
                    ///////////////////////////////// 
                    processo.pannelloProcesso.impostaVisPulsanteMultifuzioneMicro(false);

                    break;

                default:

                    ////////////////////
                    // DEFAULT CASE  ///
                    ////////////////////
                    processo.pannelloProcesso.impostaVisPulsanteMultifuzioneMicro(false);

                    //Inizializza Testo Pulsante
                    processo.pannelloProcesso.elemLabelSimple[23].setText("");

                    break;
            }

////            } catch (NumberFormatException e) {
////                //Memomirazzione Log Procesoo
////                ProcessoLogger.logger.log(Level.SEVERE, "Errore durante il processo di microdosatura - e={0}", e);
////            }
            try {
                ThreadProcessoGestoreMicrodosaturaComunicazione.sleep(MICRO_FREQ_THREAD_CONTROLLO_MICRODOSATURE_ESEGUITE);
            } catch (InterruptedException ex) {
            }

        }//fine Loop //fine Loop 

        if (controlloFineMicrodosatori) {

            ////////////////////////////////
            // MICRODOSATURA COMPLETATA  ///
            ////////////////////////////////
            //Registra fine Pesa Componenti
            processo.finalizzaPesaComponenti();

            processo.microdosaturaCompletata = true;

        } else if (processo.interrompiControlloMicrodosatori) {

            ///////////////////////////////////
            // INTERRUZIONE CONTROLLO MICRO  //
            ///////////////////////////////////
            ////////////////////////////////////
            // MICRODOSATORE SERIE 1 - 2017  ///
            ////////////////////////////////////
            for (int i = 0; i < processo.microdosatori_2017.size(); i++) {
                processo.microdosatori_2017.get(i).inizializzaDispositivo();
            }

        }

        processo.visibilitaMicro = false;
        processo.pannelloProcesso.impostaVisElementiPannelloMicro(false);

        ////////////////////////////////////
        // MICRODOSATORE SERIE 1 - 2017  ///
        ////////////////////////////////////
        for (int i = 0; i < processo.microdosatori_2017.size(); i++) {
            processo.microdosatureEseguiteRegistrate[i] = false;
            processo.microdosatureEseguite[i] = false;
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("Fine Thread Gestore Microdosatura Comunicazione");

    }

    public void aggiornaInfoMicro() {

        ////////////////////////////////////
        // MICRODOASTORE SERIE 0 - 2017  ///
        ////////////////////////////////////
        processo.pannelloProcesso.elemLabelSimple[22].setText(HTML_STRINGA_INIZIO
                + processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPresaComponente()
                + HTML_STRINGA_FINE);

        processo.pannelloProcesso.elemLabelTitle[13].setText(HTML_STRINGA_INIZIO
                + processo.microdosatori_2017.get(processo.indiceMicroVisibile).getNomeComponente()
                + HTML_STRINGA_FINE);

        processo.pannelloProcesso.elemLabelTitle[14].setText(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 816, ParametriSingolaMacchina.parametri.get(111)))
                + HTML_STRINGA_FINE);

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            ///////////////////////////
            // CONVERSIONE DI PESO  ///
            ///////////////////////////
            processo.pannelloProcesso.elemLabelTitle[15].setText(HTML_STRINGA_INIZIO
                    + ConvertiPesoVisualizzato(
                            Integer.toString(processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoDesiderato()),
                            ParametriSingolaMacchina.parametri.get(338))
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340)
                    + HTML_STRINGA_FINE);

        } else {

            //////////////////////
            // SISTEMA METRICO ///
            //////////////////////
            processo.pannelloProcesso.elemLabelTitle[15].setText(HTML_STRINGA_INIZIO
                    + processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoDesiderato()
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340)
                    + HTML_STRINGA_FINE);

        }

        processo.pannelloProcesso.elemLabelTitle[17].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 817, ParametriSingolaMacchina.parametri.get(111)));

    }

    public void agggiornaPesoCorrenteComponentePesato() {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            ///////////////////////////
            // CONVERSIONE DI PESO  ///
            ///////////////////////////
            processo.pannelloProcesso.elemLabelSimple[21].setText((int) Double.parseDouble(ConvertiPesoVisualizzato(
                            Integer.toString(processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoComponente()),
                            ParametriSingolaMacchina.parametri.get(338)))
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340));

        } else {

            /////////////////////
            // SISTEMA METRICO ///
            ////////////////////// 
            processo.pannelloProcesso.elemLabelSimple[21].setText((int) Double.parseDouble(
                    Integer.toString(processo.microdosatori_2017.get(processo.indiceMicroVisibile).getPesoComponente()))
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340));

        }
    }

    public void aggiornaStatoMicro() {

        String result;

        ////////////////////////////////////
        // MICRODOSATORE SERIE 1 - 2017  ///
        ////////////////////////////////////
        switch (processo.microdosatori_2017.get(processo.indiceMicroVisibile).getStatoMicrodosatura()) {
            case "0":
                //STANDBY: MICRODOSATORE PRONTO
                result = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 871, ParametriSingolaMacchina.parametri.get(111)));
                break;
            case "1":
                //CONFIG: MICRODOSATURA DA AVVIARE
                result = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 810, ParametriSingolaMacchina.parametri.get(111)));
                break;
            case "2":
                //RUN: MICRODOSATURA IN CORSO
                result = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 811, ParametriSingolaMacchina.parametri.get(111)));
                break;
            case "3":
                //END: MICRODOSATURA COMPLETATA
                result = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 813, ParametriSingolaMacchina.parametri.get(111)));
                break;
            case "4":
                //EMPTY: MICRODOSATURA INTERROTTA PER COCLEA VUOTA
                result = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 814, ParametriSingolaMacchina.parametri.get(111)));
                break;
            default:
                result = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 815, ParametriSingolaMacchina.parametri.get(111)));
                break;
        }

        processo.pannelloProcesso.elemLabelSimple[31].setText(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 821, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + processo.microdosatori_2017.get(processo.indiceMicroVisibile).getStatoMicrodosatura()
                + " - "
                + result
                + HTML_STRINGA_FINE);

    }

}
