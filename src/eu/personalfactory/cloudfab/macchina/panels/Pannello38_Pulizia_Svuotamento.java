package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdCatPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.Date;
import javax.swing.JLabel;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO;

@SuppressWarnings("serial")
public class Pannello38_Pulizia_Svuotamento extends MyStepPanel {

    MyStepPanel pannello_corrente;
    public int idPresa;
    public boolean assistito, bilanciaStandard, manuale;
    public int numImage = 5;
    public boolean confezionamentoInEsecuzione;
    private Pulizia pulizia;
    private int idOrdine;
    private String VelMiscelatore;
    private Date dtInizio;
    private int idCiclo;
    private int idProdotto;

    //COSTRUTTORE
    public Pannello38_Pulizia_Svuotamento() {

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
        impostaDimLabelTitle(3);
        impostaDimLabelProg(0);
        impostaDimLabelBut(9);
        impostaColori(5);

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

        //Label Peso Teorico
        elemLabelSimple[2].setForeground(elemColor[3]);
        elemLabelSimple[2].setHorizontalAlignment(JLabel.RIGHT);

        //Label Peso Reale
        elemLabelSimple[5].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelSimple[5].setForeground(elemColor[3]);

        //Label Titolo Peso Reale 
        elemLabelTitle[2].setForeground(elemColor[4]);

    }

    //Inizializza il Pannello
    public void initPanel(int idOrdine, int idProdotto) {

        setIdOrdine(idOrdine);

        setIdProdotto(idProdotto);

        //Nuova Istanza Procedura Pulizia
        pulizia = new Pulizia(this);

        //Inizializzazione Procedura Pulizia
        pulizia.init();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        aggiornaPesoConfezione();

        //Lettura Messaggi da database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Inizializzazione Pannello
        initPannello();
 
    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 954, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 955, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[3].setText(HTML_STRINGA_INIZIO + ParametriSingolaMacchina.parametri.get(340) + HTML_STRINGA_FINE);

                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 953, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 956, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 958, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamentop Testo Label Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 397, ParametriSingolaMacchina.parametri.get(111)))
                        + "\n"
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 399, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            ///////////////////////////
            // SVUOTAMENTO STANDARD  //
            ///////////////////////////
            //Impostazione Validità Pulsanti Pulizia
            impostaValiditaPulsantiPulizia(false);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                    && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                        OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_TRUE_CHAR);

            }

            setVelMiscelatore(ParametriSingolaMacchina.parametri.get(133));
            setDtInizio(new Date());

            //Registra Ciclo Eseguita
            setIdCiclo(RegistraNuovoCiclo(ParametriGlobali.parametri.get(149), //tipo_ciclo 
                    getIdOrdine(), //id_ordine
                    getIdProdotto(), TrovaIdCatPerIdProdotto(getIdProdotto()), //id_cat
                    Integer.parseInt(getVelMiscelatore()), //velocita_mix
                    0, //tempo_mix
                    1, //num_sacchi
                    0, //num_sacchi_aggiuntivi
                    Boolean.toString(!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(431))), //vibro_attivo
                    "", //aria_cond_scarico
                    "", //aria_interno_valvola
                    "", //aria_pulisci_valvola
                    "", //info1
                    "", //info2
                    "", //info3
                    "", //info4
                    "", //info5
                    "", //info6
                    "", //info7
                    "", //info8
                    "", //info9
                    "", //info10
                    "", //id_serie_colore
                    ""));//id_serie_additivo

            //Procedura di Scvuotamento a Vel Lenta
            pulizia.SvuotamentoMiscelatore(Integer.parseInt(getVelMiscelatore()));

        } else if (button.equals(elemBut[1].getName())) {

            ////////////////////////
            // SVUOTAMENTO EXTRA  //
            ////////////////////////
            //Visualizzazione e Gestione Errore
            if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(25) == 0) {
                //Impostazione Validità Pulsanti Pulizia
                impostaValiditaPulsantiPulizia(false);

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                        && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                            + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                            OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                            + OUTPUT_TRUE_CHAR);

                }

                setVelMiscelatore(ParametriSingolaMacchina.parametri.get(132));
                setDtInizio(new Date());

                //Registra Ciclo Eseguita
                setIdCiclo(RegistraNuovoCiclo(ParametriGlobali.parametri.get(149), //tipo_ciclo 
                        getIdOrdine(), //id_ordine
                        getIdProdotto(), TrovaIdCatPerIdProdotto(getIdProdotto()), //id_cat
                        Integer.parseInt(getVelMiscelatore()), //velocita_mix
                        0, //tempo_mix
                        1, //num_sacchi
                        0, //num_sacchi_aggiuntivi
                        Boolean.toString(!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(431))), //vibro_attivo
                        "", //aria_cond_scarico
                        "", //aria_interno_valvola
                        "", //aria_pulisci_valvola
                        "", //info1
                        "", //info2
                        "", //info3
                        "", //info4
                        "", //info5
                        "", //info6
                        "", //info7
                        "", //info8
                        "", //info9
                        "", //info10
                        "", //id_serie_colore
                        ""));//id_serie_additivo

                //Procedura di Scvuotamento a Vel Lenta
                pulizia.SvuotamentoMiscelatore(Integer.parseInt(getVelMiscelatore()));
            }

        } else if (button.equals(elemBut[2].getName())) {

            //////////////////////////////////////
            // DIMINUZIONE PESO SACCO PULIZIA  ///
            //////////////////////////////////////
            try {
                if (pulizia.pesoSaccoSvuotamento > Double.parseDouble(ParametriSingolaMacchina.parametri.get(339))) {
                    pulizia.pesoSaccoSvuotamento = (int) ((double) pulizia.pesoSaccoSvuotamento - Double.parseDouble(ParametriSingolaMacchina.parametri.get(339)));
                    pulizia.pesoDaRaggiungereConfezione = (int) ((double) pulizia.pesoDaRaggiungereConfezione - Double.parseDouble(ParametriSingolaMacchina.parametri.get(339)));

                }
            } catch (NumberFormatException e) {
            }

            aggiornaPesoConfezione();

        } else if (button.equals(elemBut[3].getName())) {

            /////////////////////////////////////
            // INCREMENTO PESO SACCO PULIZIA  ///
            /////////////////////////////////////
            try {
                pulizia.pesoSaccoSvuotamento += (int) Double.parseDouble(ParametriSingolaMacchina.parametri.get(339));

                pulizia.pesoDaRaggiungereConfezione += (int) Double.parseDouble(ParametriSingolaMacchina.parametri.get(339));

            } catch (NumberFormatException e) {
            }

            aggiornaPesoConfezione();

        } else if (button.equals(elemBut[4].getName())) {

            /////////////
            // PANIC  ///
            /////////////
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
        } else if (button.equals(elemBut[5].getName())) {

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

        } else if (button.equals(elemBut[6].getName())) {

            ///////////////////////////////////////
            // ATTIVA ARIA A2 - PULISCI VALVOLA ///
            ///////////////////////////////////////
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

        } else if (button.equals(elemBut[7].getName())) {

            ///////////////////////////////////
            // ATTIVA ARIA A3 - SVUOTA VALVOLA ///
            ///////////////////////////////////
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
        } else if (button.equals(elemBut[8].getName())) {

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
        }

    }

    public void aggiornaPesoConfezione() {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            ////////////////////////
            // CONVERSIONE PESO  ///
            ////////////////////////
            elemLabelSimple[2].setText(ConvertiPesoVisualizzato(Integer.toString(pulizia.pesoSaccoSvuotamento), ParametriSingolaMacchina.parametri.get(338)));
        } else {
            ///////////////////////
            // SISTEMA METRICO  ///
            ///////////////////////                
            elemLabelSimple[2].setText(Integer.toString(pulizia.pesoSaccoSvuotamento));
        }
    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello() {

        ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

        pulizia.interrompiComunicazioneSchedaIO = true;

        pannello_corrente.setVisible(false);

    }

    //Aggiornamento Messaggio Operatore
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
        }
    }

    public void impostaValiditaPulsantiPulizia(Boolean vis) {
        elemBut[0].setEnabled(vis);
        elemBut[1].setEnabled(vis);
        elemBut[2].setEnabled(vis);
        elemBut[3].setEnabled(vis);
    }

    //Imposta la visualizzazione dei dettagli di pesatura
    public void impostaVisibilitaDatiPulizia(Boolean vis) {

        //Inizializzazione Label
        if (vis) {

            elemLabelSimple[5].setText("0 " + ParametriSingolaMacchina.parametri.get(340));

        }

        //Aggiornamento Visibilità
        labelImageAux[4].setVisible(vis);
        elemLabelSimple[5].setVisible(vis);
        elemLabelTitle[2].setVisible(vis);

    }

    public void initPannello() {

        modificaTestoMessaggioUtente(9);

        impostaVisibilitaDatiPulizia(false);

        impostaValiditaPulsantiPulizia(true);

    }

////    public void impostaVisibilitaMessaggioUtente(Boolean vis) {
////
////        // DA IMPLEMENTARE SE NECESSARIO
////    }
    public void aggiornaLabelPesaScarico(String valore_peso) {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            ////////////////////////
            // CONVERSIONE PESO  ///
            ////////////////////////
            elemLabelSimple[5].setText(ConvertiPesoVisualizzato(valore_peso, ParametriSingolaMacchina.parametri.get(338))
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340));
        } else {
            ///////////////////////
            // SISTEMA METRICO  ///
            ///////////////////////                
            elemLabelSimple[5].setText(valore_peso
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340));
        }

    }

    ///////////////////////////
    // GETTERS AND SETTERS  ///
    ///////////////////////////
    public String getVelMiscelatore() {
        return VelMiscelatore;
    }

    public void setVelMiscelatore(String VelMiscelatore) {
        this.VelMiscelatore = VelMiscelatore;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Date getDtInizio() {
        return dtInizio;
    }

    public void setDtInizio(Date dtInizio) {

        this.dtInizio = dtInizio;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

}
