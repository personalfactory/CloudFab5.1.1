package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.microdosatore.Microdosatore_Inverter_2017;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
//import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_FREQUENZA_THREAD_CONTROLLO_CONTATTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_FREQ_THREAD_LETTURA_PESO_CONTROLLO_MANUALE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello31_Comando_Microdosatori extends MyStepPanel {

    private int microdosatoreCorrente;
    private boolean microdosatoreInRun;
    ArrayList<Microdosatore_Inverter_2017> microdosatori_2017;
    private boolean interrompiThreadLeggiPeso, interrompiAnimazioneCoclea;
    private final int numImage = 8;
    int FREQ_THREAD_ANIMAZIONE = 500;
    private boolean interrompiAnimazioneAgitatore;
    double correttivoTempoAnimazione;
    boolean letturaPesoInCorso, impostaVelLentaPesatura, impostaVelNormalePesatura,
            impostaVelAltaPesatura, arrestoInverterPesatura, arrestoMescolaPesatura, avvioMescolaPesatura;
    MyStepPanel pannelloCorrente;

//COSTRUTTORE 
    public Pannello31_Comando_Microdosatori() {
        super();
        setLayer();
    }

    //Dichirazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(2);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(10);
        impostaColori(2);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelSimple(elemColor[1]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Allineamento Label
        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        pannelloCorrente = this;

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Indice Microdosatore
        microdosatoreCorrente = 0;

        //Aggiorna Microdosatore Corrente
        elemLabelSimple[0].setText(TrovaPresaPerIdPresa(100 + microdosatoreCorrente));

        ///////////////////////////////////
        // MICRODOSATORE SERIE 1 -2017  ///
        ///////////////////////////////////
        microdosatori_2017 = new ArrayList<>();

        for (int i = 0; i < Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)); i++) {

            ///////////////////////////////////
            // MICRODOSATORE SERIE 1 -2017  ///
            ///////////////////////////////////
            microdosatori_2017.add(new Microdosatore_Inverter_2017(Integer.toString(i + 1)));

            //Memorizzazione Log Processo
            SessionLogger.logger.log(Level.INFO,
                    "{0}Microdosatore {1}{2}"
                    + "Indirizzo= {3}",
                    new Object[]{
                        LOG_CHAR_SEPARATOR,
                        i,
                        LOG_CHAR_SEPARATOR,
                        microdosatori_2017.get(i).getMicroIPAddress()});

        }

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        labelImageAux[0].setVisible(true);
        labelImageAux[3].setVisible(true);
        labelImageAux[6].setVisible(false);
        labelImageAux[7].setVisible(false);

    }

//    //Lettura Vocaboli Traducibili da Database
//    private class ThreadStatoContatto extends Thread {
//
//        @Override
//        public void run() {
//
//            while (pannelloCorrente.isVisible()) {
//                if (!letturaPesoInCorso) {
//                    microdosatori_2017.get(microdosatoreCorrente).aggiornaStatoContatto();
//                }
//
//                labelImageAux[6].setVisible(microdosatori_2017.get(microdosatoreCorrente).isStatoContatto());
//                labelImageAux[7].setVisible(!microdosatori_2017.get(microdosatoreCorrente).isStatoContatto());
//
//                try {
//                    ThreadStatoContatto.sleep(MICRO_FREQUENZA_THREAD_CONTROLLO_CONTATTO);
//                } catch (InterruptedException ex) {
//                }
//            }
//        }
//
//    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 722, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 820, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Simple
                elemLabelSimple[1].setText("0" + " " + ParametriSingolaMacchina.parametri.get(340));
            }

        }
    }

    //Thread Animazione Agitatore
    private class ThreadAnimazioneAgitatore extends Thread {

        @Override
        public void run() {

            interrompiAnimazioneAgitatore = false;
            labelImageAux[0].setVisible(false);
            labelImageAux[1].setVisible(false);
            labelImageAux[2].setVisible(true);
            int counter = 0;
            float numRip = FREQ_THREAD_ANIMAZIONE / FRAZIONAMENTO_TEMPI;

            while (!interrompiAnimazioneAgitatore) {

                if (counter > numRip) {
                    counter = 0;
                    labelImageAux[2].setVisible(labelImageAux[1].isVisible());
                    labelImageAux[1].setVisible(!labelImageAux[1].isVisible());

                } else {
                    counter++;
                }

                try {
                    ThreadAnimazioneAgitatore.sleep(FRAZIONAMENTO_TEMPI);
                } catch (InterruptedException ex) {
                    SessionLogger.logger.log(Level.WARNING, "Errore durante lo sleep del thread - e:{0}", ex);
                }
            }

            labelImageAux[0].setVisible(true);
            labelImageAux[1].setVisible(false);
            labelImageAux[2].setVisible(false);
        }
    }

    //Thread Animazione Coclea
    private class ThreadAnimazioneCoclea extends Thread {

        @Override
        public void run() {

            interrompiAnimazioneCoclea = false;
            labelImageAux[3].setVisible(false);
            labelImageAux[4].setVisible(false);
            labelImageAux[5].setVisible(true);

            int counter = 0;
            float numRip = FREQ_THREAD_ANIMAZIONE / FRAZIONAMENTO_TEMPI;

            while (!interrompiAnimazioneCoclea) {

                if (counter > numRip * correttivoTempoAnimazione) {
                    counter = 0;
                    labelImageAux[4].setVisible(labelImageAux[5].isVisible());
                    labelImageAux[5].setVisible(!labelImageAux[5].isVisible());

                } else {
                    counter++;
                }

                try {
                    ThreadAnimazioneCoclea.sleep(FRAZIONAMENTO_TEMPI);
                } catch (InterruptedException ex) {
                    SessionLogger.logger.log(Level.WARNING, "Errore durante lo sleep del thread - e:{0}", ex);
                }

            }

            labelImageAux[3].setVisible(true);
            labelImageAux[4].setVisible(false);
            labelImageAux[5].setVisible(false);
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String buttonName) {

        switch (buttonName) {
            case "0": {

                ////////////////////////////
                // MICRODOSATORE AVANTI  ///
                ////////////////////////////
                interrompiThreadLeggiPeso = true;

                if (microdosatoreCorrente < Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)) - 1) {
                    microdosatoreCorrente++;
                } else {
                    microdosatoreCorrente = 0;
                }

                //Aggiorna Microdosatore Corrente
                elemLabelSimple[0].setText(TrovaPresaPerIdPresa(100 + microdosatoreCorrente));

                break;
            }
            case "1": {

                //////////////////////////////
                // MICRODOSATORE INDIETRO  ///
                //////////////////////////////
                interrompiThreadLeggiPeso = true;

                if (microdosatoreCorrente > 0) {
                    microdosatoreCorrente--;
                } else {
                    microdosatoreCorrente = Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)) - 1;
                }

                //Aggiorna Microdosatore Corrente
                elemLabelSimple[0].setText(TrovaPresaPerIdPresa(100 + microdosatoreCorrente));

                break;
            }

            case "2": {

                //////////////////////////////////////
                // IMPOSTA VELOCITA LENTA E AVVIA  ///
                //////////////////////////////////////
                abilitaPulsanti(false);
                abilitaPulsantiAvantiDietro(false);
                correttivoTempoAnimazione = 2;

                SessionLogger.logger.log(Level.INFO, "Comando Manuale Microdosatore Cambio Vel S1 ={0}", ParametriSingolaMacchina.parametri.get(366));

                ///////////////////////////////////
                // MICRODOSATORE SERIE 1 -2017  ///
                /////////////////////////////////// 
                if (!letturaPesoInCorso) {
                    micro2017_impostaVelLenta();
                    impostaVelLentaPesatura = false;
                } else {
                    impostaVelLentaPesatura = true;
                }

                abilitaPulsanti(true);
                elemBut[2].setEnabled(false);

                break;

            }

            case "3": {

                ////////////////////////////////////////
                // IMPOSTA VELOCITA NORMALE E AVVIA  ///
                //////////////////////////////////////// 
                abilitaPulsanti(false);
                abilitaPulsantiAvantiDietro(false);
                correttivoTempoAnimazione = 1;

                SessionLogger.logger.log(Level.INFO, "Comando Manuale Microdosatore Cambio Vel S2 ={0}", ParametriSingolaMacchina.parametri.get(367));

                ///////////////////////////////////
                // MICRODOSATORE SERIE 1 -2017  ///
                /////////////////////////////////// 
                if (!letturaPesoInCorso) {
                    micro2017_impostaVelNormale();
                    impostaVelNormalePesatura = false;
                } else {
                    impostaVelNormalePesatura = true;
                }
                abilitaPulsanti(true);
                elemBut[3].setEnabled(false);
                break;
            }

            case "4": {

                /////////////////////////////////////
                // IMPOSTA VELOCITA ALTA E AVVIA  ///
                ///////////////////////////////////// 
                abilitaPulsanti(false);
                abilitaPulsantiAvantiDietro(false);
                correttivoTempoAnimazione = 0.5;

                SessionLogger.logger.log(Level.INFO, "Comando Manuale Microdosatore Cambio Vel S3 ={0}", ParametriSingolaMacchina.parametri.get(368));

                ///////////////////////////////////
                // MICRODOSATORE SERIE 1 -2017  ///
                /////////////////////////////////// 
                if (!letturaPesoInCorso) {
                    micro2017_impostaVelAlta();
                    impostaVelAltaPesatura = false;
                } else {
                    impostaVelAltaPesatura = true;
                }

                abilitaPulsanti(true);
                elemBut[3].setEnabled(false);

                break;
            }

            case "5": {

                ////////////////////////
                // ARRESTO INVERTER  ///
                //////////////////////// 
                microdosatoreInRun = false;

                abilitaPulsanti(false);
                abilitaPulsantiAvantiDietro(false);

                interrompiAnimazioneCoclea = true;

                SessionLogger.logger.info("Comando Manuale Microdosatore Arresto inverter");

                ///////////////////////////////////
                // MICRODOSATORE SERIE 1 -2017  ///
                ///////////////////////////////////
                if (!letturaPesoInCorso) {
                    microdosatori_2017.get(microdosatoreCorrente).attivaMotoreCoclee("0");
                    arrestoInverterPesatura = false;
                } else {
                    arrestoInverterPesatura = true;
                }

                abilitaPulsanti(true);
                abilitaPulsantiAvantiDietro(true);

                break;
            }

            case "6": {

                /////////////////////
                // AVVIO MESCOLA  ///
                /////////////////////  
                microdosatoreInRun = false;

                abilitaPulsanti(false);
                abilitaPulsantiAvantiDietro(false);

                SessionLogger.logger.info("Comando Manuale Microdosatore Avvio Mescolatore");

                ///////////////////////////////////
                // MICRODOSATORE SERIE 0 -2017  ///
                ///////////////////////////////////
                if (!letturaPesoInCorso) {
                    microdosatori_2017.get(microdosatoreCorrente).attivaMescola(true);
                    avvioMescolaPesatura = false;
                } else {
                    avvioMescolaPesatura = true;
                }

                abilitaPulsanti(true);

                new ThreadAnimazioneAgitatore().start();
                break;
            }

            case "7": {

                ///////////////////////
                // ARRESTO MESCOLA  ///
                ///////////////////////  
                microdosatoreInRun = false;

                abilitaPulsanti(false);
                abilitaPulsantiAvantiDietro(false);

                interrompiAnimazioneAgitatore = true;

                SessionLogger.logger.info("Comando Manuale Microdosatore Arresto Mescolatore");

                ///////////////////////////////////
                // MICRODOSATORE SERIE 1 -2017  ///
                ///////////////////////////////////
                if (!letturaPesoInCorso) {
                    microdosatori_2017.get(microdosatoreCorrente).attivaMescola(false);
                    arrestoMescolaPesatura = false;
                } else {
                    arrestoMescolaPesatura = true;
                }

                abilitaPulsanti(true);
                abilitaPulsantiAvantiDietro(true);

                break;
            }

            case "8": {

                ///////////////////////////////
                // AVVIO THREAD LEGGI PESO  ///
                ///////////////////////////////
                new threadLeggiPeso(this).start();

                elemBut[8].setEnabled(false);

                abilitaPulsantiAvantiDietro(false);

                break;
            }

            case "9": {

                /////////////////////////////////
                // ARRESTO THREAD LEGGI PESO  ///
                /////////////////////////////////
                interrompiThreadLeggiPeso = true;
                abilitaPulsantiAvantiDietro(true);

                break;
            }
        }
    }

    //Abilitazione - Disabilitazione Pulsanti
    public void abilitaPulsanti(boolean vis) {

        //elemBut[0].setEnabled(vis);
        //elemBut[1].setEnabled(vis);
        elemBut[2].setEnabled(vis);
        elemBut[3].setEnabled(vis);
        elemBut[4].setEnabled(vis);
        elemBut[5].setEnabled(vis);
        elemBut[6].setEnabled(vis);
        elemBut[7].setEnabled(vis);
        elemBut[8].setEnabled(vis);
        elemBut[9].setEnabled(vis);
    }

    public void abilitaPulsantiAvantiDietro(boolean vis) {
        elemBut[0].setEnabled(vis);
        elemBut[1].setEnabled(vis);

    }

    //Thread Visualizzazione e Modifica Informazioni
    private class threadLeggiPeso extends Thread {

        private final Pannello31_Comando_Microdosatori pannello;

        public threadLeggiPeso(Pannello31_Comando_Microdosatori pannello) {
            this.pannello = pannello;
        }

        @Override
        public void run() {

            interrompiThreadLeggiPeso = false;

            letturaPesoInCorso = true;

            while (!interrompiThreadLeggiPeso && pannello.isVisible()) {

                ///////////////////////////////////
                // MICRODOSATORE SERIE 1 -2017  ///
                ///////////////////////////////////
                if (impostaVelLentaPesatura) {

                    /////////////////////////////////////////////////
                    // IMPOSTA VELOCITA LENTA  - PESATURA ATTIVA  ///
                    /////////////////////////////////////////////////
                    micro2017_impostaVelLenta();

                    impostaVelLentaPesatura = false;

                } else if (impostaVelNormalePesatura) {

                    ///////////////////////////////////////////////////
                    // IMPOSTA VELOCITA NORMALE  - PESATURA ATTIVA  ///
                    ///////////////////////////////////////////////////
                    micro2017_impostaVelNormale();

                    impostaVelLentaPesatura = false;

                } else if (impostaVelAltaPesatura) {

                    ///////////////////////////////////////////////////
                    // IMPOSTA VELOCITA ALTA  - PESATURA ATTIVA  ///
                    ///////////////////////////////////////////////////
                    micro2017_impostaVelAlta();

                    impostaVelLentaPesatura = false;

                } else if (arrestoInverterPesatura) {

                    ///////////////////////////////////////////////////
                    // ARRESTO INVERTER COCLEE   - PESATURA ATTIVA  ///
                    ///////////////////////////////////////////////////
                    microdosatori_2017.get(microdosatoreCorrente).attivaMotoreCoclee("0");

                    arrestoInverterPesatura = false;

                } else if (avvioMescolaPesatura) {

                    ///////////////////////////////////////
                    // AVVIO MESCOLA - PESATURA ATTIVA  ///
                    ///////////////////////////////////////
                    microdosatori_2017.get(microdosatoreCorrente).attivaMescola(true);

                    avvioMescolaPesatura = false;

                } else if (arrestoMescolaPesatura) {

                    /////////////////////////////////////////
                    // ARRESTO MESCOLA - PESATURA ATTIVA  ///
                    /////////////////////////////////////////
                    microdosatori_2017.get(microdosatoreCorrente).attivaMescola(false);

                    arrestoMescolaPesatura = false;

                } else {

                    ////////////////////
                    // LETTURA PESO  ///
                    ////////////////////
                    String rep = microdosatori_2017.get(microdosatoreCorrente).leggiPeso();

                    if (rep.length() > 0) {

                        rep = rep.substring(1, rep.length());

                        //Memorizzazione log
                        SessionLogger.logger.log(Level.INFO, "Risposta Ricevuta dal Microdosatore = {0}", rep);

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                            ///////////////////////////
                            // CONVERSIONE DI PESO  ///
                            ///////////////////////////
                            elemLabelSimple[1].setText(ConvertiPesoVisualizzato(rep, ParametriSingolaMacchina.parametri.get(338)) + " " + ParametriSingolaMacchina.parametri.get(340));

                        } else {

                            /////////////////////
                            // SISTEMA METRICO ///
                            //////////////////////
                            elemLabelSimple[1].setText(rep + " " + ParametriSingolaMacchina.parametri.get(340));
                        }
                    }
                }
                try {
                    threadLeggiPeso.sleep(MICRO_FREQ_THREAD_LETTURA_PESO_CONTROLLO_MANUALE);
                } catch (InterruptedException ex) {
                    SessionLogger.logger.log(Level.SEVERE, "Errore durante lo sleep del thread - ex ={0}", ex);
                }

            }
            letturaPesoInCorso = false;
            elemLabelSimple[1].setText("0" + " " + ParametriSingolaMacchina.parametri.get(340));
            elemBut[8].setEnabled(true);
        }
    }

    public void micro2017_impostaVelLenta() {

        if (!microdosatoreInRun) {
            microdosatoreInRun = true;

            new ThreadAnimazioneCoclea().start();

            SessionLogger.logger.info("Comando Manuale Microdosatore Avvio inverter");
            //Avvio Inverter
            microdosatori_2017.get(microdosatoreCorrente).attivaMotoreCoclee(ParametriSingolaMacchina.parametri.get(366));

        } else {
            //Cambio Velocit
            microdosatori_2017.get(microdosatoreCorrente).cambioVel(ParametriSingolaMacchina.parametri.get(366));

        }

    }

    public void micro2017_impostaVelNormale() {
        if (!microdosatoreInRun) {
            microdosatoreInRun = true;

            new ThreadAnimazioneCoclea().start();

            SessionLogger.logger.info("Comando Manuale Microdosatore Avvio inverter");
            //Avvio Inverter
            microdosatori_2017.get(microdosatoreCorrente).attivaMotoreCoclee(ParametriSingolaMacchina.parametri.get(367));

        } else {
            //Cambio Velocit
            microdosatori_2017.get(microdosatoreCorrente).cambioVel(ParametriSingolaMacchina.parametri.get(367));

        }
    }

    public void micro2017_impostaVelAlta() {
        if (!microdosatoreInRun) {
            microdosatoreInRun = true;

            new ThreadAnimazioneCoclea().start();

            SessionLogger.logger.info("Comando Manuale Microdosatore Avvio inverter");
            //Avvio Inverter
            microdosatori_2017.get(microdosatoreCorrente).attivaMotoreCoclee(ParametriSingolaMacchina.parametri.get(368));

        } else {
            //Cambio Velocit
            microdosatori_2017.get(microdosatoreCorrente).cambioVel(ParametriSingolaMacchina.parametri.get(368));

        }
    }
}
