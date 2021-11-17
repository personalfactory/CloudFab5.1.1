package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_INTERLINEA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import javax.swing.JLabel;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO;

@SuppressWarnings("serial")
public class Pannello40_Pulizia_Manuale extends MyStepPanel {

    MyStepPanel pannello_corrente;
    public int numImage = 4;

    public Pulizia pulizia;

    //COSTRUTTORE
    public Pannello40_Pulizia_Manuale() {

        super();
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(6);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(9);
        impostaColori(3);

        //Inizializza Colore Label Title     
        initColorLabelTitle(elemColor[0]);

        //Inizializza Colore Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializza Colore Label Simple
        initColorLabelSimple(elemColor[2]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Modifica la Visibilità di Default delle Righe di Aiuto
        impostaVisibilitaAiuto(true);

        pannello_corrente = this;

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        elemLabelSimple[0].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[1].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[2].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[3].setHorizontalAlignment(JLabel.LEFT);

        elemLabelSimple[0].setVerticalAlignment(JLabel.CENTER);
        elemLabelSimple[1].setVerticalAlignment(JLabel.CENTER);
        elemLabelSimple[2].setVerticalAlignment(JLabel.CENTER);
        elemLabelSimple[3].setVerticalAlignment(JLabel.CENTER);

        elemLabelSimple[5].setHorizontalAlignment(JLabel.CENTER);
        elemLabelSimple[5].setVerticalAlignment(JLabel.CENTER);
    }

    //Inizializza il Pannello
    public void initPanel() {

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Nuova Istanza Procedura Pulizia
        pulizia = new Pulizia(this);

        //Inizializzazione Procedura Pulizia
        pulizia.init();

        //Lettura Messaggi da database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        initPannello();

    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 973, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 974, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 975, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 976, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 951, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamentop Testo Label Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 978, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_INTERLINEA
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 979, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_INTERLINEA
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 980, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            //////////////////////////////////////
            // ATTIVA ARIA A1 - FLUDIFICATORI  ///
            /////////////////////////////////////
            labelImageAux[0].setVisible(!labelImageAux[0].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {
                
                if (labelImageAux[0].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI,
                            OUTPUT_FALSE_CHAR);
                }

            }

        } else if (button.equals(elemBut[1].getName())) {

            //////////////////////////////////////
            // ATTIVA ARIA A2 - SVUOTA VALVOLA ///
            //////////////////////////////////////
            labelImageAux[1].setVisible(!labelImageAux[1].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {
                
                if (labelImageAux[1].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                            OUTPUT_FALSE_CHAR);
                }

            }

        } else if (button.equals(elemBut[2].getName())) {

            //////////////////////////////////////
            // ATTIVA ARIA A3 - SVUOTA VALVOLA ///
            //////////////////////////////////////
            labelImageAux[2].setVisible(!labelImageAux[2].isVisible());

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {
                
                if (labelImageAux[2].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                            OUTPUT_FALSE_CHAR);
                }
            }
        } else if (button.equals(elemBut[3].getName())) {

            ///////////////////////////////////////
            // ATTIVA ARIA A4 - SVUOTA TUBO ///
            ///////////////////////////////////////
            labelImageAux[3].setVisible(!labelImageAux[3].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {
                
                if (labelImageAux[3].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                            OUTPUT_FALSE_CHAR);
                }
            }
        } else if (button.equals(elemBut[4].getName())) {

            /////////////////////////////////////////////
            // APERTURA - CHIUSURA VALVOLA DI CARICO  ///
            /////////////////////////////////////////////
            //Impostazione Validità Pulsanti Pulizia
            impostaValiditaPulsantiPulizia(false);

            //Procedura di Apertura Valvola di Carico
            pulizia.ApriValvolaCarico();

        } else if (button.equals(elemBut[5].getName())) {

            ////////////////////
            // BLOCCA SACCO  ///
            ////////////////////
            //Impostazione Validità Pulsanti Pulizia
            impostaValiditaPulsantiPulizia(false);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                    && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                        OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_TRUE_CHAR);
            }

            //Procedura di Posizionamento e Blocco Sacco
            pulizia.BloccaSacco();

        } else if (button.equals(elemBut[6].getName())) {

            //////////////////
            // ASPIRATORE  ///
            //////////////////
            //Visualizzazione e Gestione Errore
            if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(35) == 0) {

                //Impostazione Validità Pulsanti Pulizia
                impostaValiditaPulsantiPulizia(false);

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                        && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                        OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_TRUE_CHAR);
                    
                }

                pulizia.attivazioneScambiatoreAspiratore();
            }

        } else if (button.equals(elemBut[7].getName())) {

            /////////////////////////////////
            // BILANCIA VIBRATORE CARICO  ///
            ///////////////////////////////// 
            //Impostazione Validità Pulsanti Pulizia
            impostaValiditaPulsantiPulizia(false);

            //Procedura di Apertura Valvola di Carico
            pulizia.ApriVibroValvolaCarico();

        } else if (button.equals(elemBut[8].getName())) {

            /////////////////////
            // PULSANTE PANIC ///
            /////////////////////
            //Visualizzazione e Gestione Errore
            if (((Pannello44_Errori) pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(31) == 0) {

                //Impostazione Variabile di Controllo
                pulizia.puliziaInterrottaManualmente = true;

                //Finalizza Procedura di Pulizia Corrente
                pulizia.finalizzaProcedura();

                //Creazione Gestore Dialog
                if (((Pannello45_Dialog) pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(3) == 1) {

                    ///////////////////////
                    // ARRESTO FORZATO  ///
                    //////////////////////
                    //Scambio Pannello
                    gestoreScambioPannello();

                } else {

                    ////////////////
                    // RIPRENDI  ///
                    ////////////////
                    //Nuova Istanza Procedura Pulizia
                    pulizia = new Pulizia(this);

                    //Inizializzazione Procedura Pulizia
                    pulizia.init();

                    initPannello();

                    //Aggiornamento Valore Variabile di Controllo
                    pulizia.puliziaInterrottaManualmente = false;

                }

            }

        }

    }
    //Gestione Scambio Pannello Collegato

    public void gestoreScambioPannello() {

        ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

        pulizia.interrompiComunicazioneSchedaIO = true;

        pannello_corrente.setVisible(false);

    }

    public void modificaVisualizzazioneFinePesatura() {

        elemBut[5].setEnabled(true);
        elemBut[6].setEnabled(false);
        elemBut[7].setEnabled(true);
        elemBut[8].setEnabled(true);
        butFreccia.setEnabled(true);

        elemLabelSimple[8].setEnabled(true);
        elemLabelSimple[9].setEnabled(false);
        elemLabelSimple[10].setEnabled(true);
        elemLabelSimple[11].setEnabled(true);

    }

    //Inizializzazione Pannello
    public void initPannello() {

        modificaTestoMessaggioUtente(11);

        impostaValiditaPulsantiPulizia(true);

        impostaVisLabelTempoResiduo(false);

        impostaLabelTempoResiduo(0);

    }

    public void impostaValiditaPulsantiPulizia(Boolean vis) {

        elemBut[4].setEnabled(vis);
        elemBut[5].setEnabled(vis);
        elemBut[6].setEnabled(vis);

        if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(440))) {
            elemBut[7].setEnabled(false);
        } else {
            elemBut[7].setEnabled(vis);
        }

    }

    public void modificaTestoMessaggioUtente(int index) {

        switch (index) {
            case 0: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 400, ParametriSingolaMacchina.parametri.get(111)));  //POSIZIONA E BLOCCA SACCO
                break;
            }

            case 1: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 436, ParametriSingolaMacchina.parametri.get(111)));  //PREMERE PULSANTE "STOP"
                break;
            }

            case 2: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 654, ParametriSingolaMacchina.parametri.get(111)));  //APERTURA-CHIUSURA VALVOLA DI CARICO
                break;
            }

            case 3: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 655, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - CARICO INERTI
                break;
            }

            case 4: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 656, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - MISCELAZIONE
                break;
            }

            case 5: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 657, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - APERTURA VALVOLA
                break;
            }

            case 6: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 658, ParametriSingolaMacchina.parametri.get(111)));   //PULIZIA AUTOMATICA - CHIUSURA VALVOLA
                break;
            }

            case 7: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 659, ParametriSingolaMacchina.parametri.get(111)));  //SVUOTAMENTO MISCELATORE
                break;
            }
            case 8: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 748, ParametriSingolaMacchina.parametri.get(111)));  //DEVIATORE ASPIRATORE ATTIVATO - POSIZIONARE SACCO
                break;
            }

            case 9: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 957, ParametriSingolaMacchina.parametri.get(111)));  //DEVIATORE ASPIRATORE ATTIVATO - POSIZIONARE SACCO
                break;
            }

            case 10: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 970, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - OK PER AVVIARE LA PROCEDURA
                break;
            }
            case 11: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 977, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - OK PER AVVIARE LA PROCEDURA
                break;
            }
            case 12: {
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 981, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - OK PER AVVIARE LA PROCEDURA
                break;
            }

        }
    }

    public void impostaVisLabelTempoResiduo(Boolean vis) {

        elemLabelSimple[5].setVisible(vis);

    }

    //Impostazione Visibilità Label Tempo Residuo di Miscelazione
    public void impostaLabelTempoResiduo(int value) {

        elemLabelSimple[5].setText(Integer.toString(value));

    }

}
