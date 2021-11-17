package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import eu.personalfactory.cloudfab.macchina.pulizia.DettagliPulizia;
import eu.personalfactory.cloudfab.macchina.pulizia.ThreadPuliziaAttivaVibroFunghiSilos;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiPuliziaPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdCatPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPreseAbilitateNonAssegnatePulizia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloComponentePerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreProdottoByIdProd;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PULIZIA_MAX_VEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.swing.JLabel;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO;

@SuppressWarnings("serial")
public class Pannello39_Pulizia_Automatica extends MyStepPanel {

    MyStepPanel pannello_corrente;
    public int numImage = 7;
    public Pulizia pulizia;
    private int indexComponente = 0;
    private int idOrdine;
    private int idProdotto;
    private Date dtInizio;

    private ArrayList<Integer> listaPreseAssegnate;
    private ArrayList<Integer> listaIdComponenti = new ArrayList<>();

    //COSTRUTTORE
    public Pannello39_Pulizia_Automatica() {

        super();
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(13);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(13);
        impostaDimLabelProg(0);
        impostaDimLabelBut(17);
        impostaColori(6);

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

        elemLabelSimple[0].setHorizontalAlignment(JLabel.CENTER);
        elemLabelSimple[0].setForeground(elemColor[3]);

        elemLabelSimple[1].setHorizontalAlignment(JLabel.CENTER);
        elemLabelSimple[1].setForeground(elemColor[3]);

        elemLabelSimple[2].setHorizontalAlignment(JLabel.CENTER);
        elemLabelSimple[2].setForeground(elemColor[3]);

        elemLabelSimple[3].setHorizontalAlignment(JLabel.CENTER);
        elemLabelSimple[3].setForeground(elemColor[3]);

        elemLabelSimple[4].setHorizontalAlignment(JLabel.CENTER);
        elemLabelSimple[4].setForeground(elemColor[3]);

        elemLabelSimple[5].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelSimple[5].setForeground(elemColor[3]);

        elemLabelSimple[6].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelSimple[6].setForeground(elemColor[3]);

        elemLabelSimple[7].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[7].setForeground(elemColor[5]);

        elemLabelSimple[8].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[8].setForeground(elemColor[5]);

        elemLabelSimple[9].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[9].setForeground(elemColor[5]);

        elemLabelSimple[10].setHorizontalAlignment(JLabel.LEFT);
        elemLabelSimple[10].setForeground(elemColor[5]);

        elemLabelSimple[12].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelSimple[12].setForeground(elemColor[1]);

        elemLabelTitle[1].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelTitle[2].setHorizontalAlignment(JLabel.LEFT);
        elemLabelTitle[3].setHorizontalAlignment(JLabel.LEFT);
        elemLabelTitle[4].setHorizontalAlignment(JLabel.LEFT);
        elemLabelTitle[5].setHorizontalAlignment(JLabel.LEFT);

        elemLabelTitle[10].setForeground(elemColor[4]);
        elemLabelTitle[11].setForeground(elemColor[4]);
        elemLabelTitle[12].setForeground(elemColor[4]);

    }

    //Inizializza il Pannello
    public void initPanel(int idOrdine, int idProdotto) {

        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;

        //Nuova Istanza Procedura Pulizia
        pulizia = new Pulizia(this);

        //Inizializzazione Procedura Pulizia
        pulizia.init();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //aggiornaPesoConfezione();
        //Lettura Messaggi da database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        aggiornaListaComponenti();

        aggiornaComponenteVisualizzati(0);

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

                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 959, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 961, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 962, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 963, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 964, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 965, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelTitle[6].setText(HTML_STRINGA_INIZIO + ParametriSingolaMacchina.parametri.get(340) + HTML_STRINGA_FINE);
                elemLabelTitle[7].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 966, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[8].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 967, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[9].setText(HTML_STRINGA_INIZIO + ParametriSingolaMacchina.parametri.get(340) + HTML_STRINGA_FINE);
                elemLabelTitle[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 968, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 958, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[12].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 969, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamentop Testo Label Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 960, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

            }
        }
    }

    //Aggiorna Lista Componenti
    public void aggiornaListaComponenti() {

        listaPreseAssegnate = TrovaPreseAbilitateNonAssegnatePulizia();
        listaIdComponenti = new ArrayList<>();

        for (int i = 0; i < listaPreseAssegnate.size(); i++) {

            listaIdComponenti.add(TrovaSingoloComponentePerIdPresa(listaPreseAssegnate.get(i)));

        }

    }

    //Aggiorna Compoennte Visualizzato
    public void aggiornaComponenteVisualizzati(int i) {

        elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, listaIdComponenti.get(i), ParametriSingolaMacchina.parametri.get(111)));

        indexComponente = i;

        //Inserimento label Tipo Title
        elemLabelSimple[1].setText(ParametriSingolaMacchina.parametri.get(118));
        elemLabelSimple[2].setText(Integer.toString(Integer.parseInt(ParametriSingolaMacchina.parametri.get(135)) / 1000));
        elemLabelSimple[3].setText(ParametriSingolaMacchina.parametri.get(134));
        elemLabelSimple[4].setText(ParametriSingolaMacchina.parametri.get(136));

        if (idProdotto != 0) {

            boolean trovato = false;
            //Trova Prese Prodotto
            ArrayList<Integer> comp = TrovaComponentiPuliziaPerIdProdotto(idProdotto);

            if (comp.size() > 0) {
                for (int j = 0; j < listaIdComponenti.size(); j++) {

                    if (Objects.equals(comp.get(0), listaIdComponenti.get(j))) {

                        //Inserimento label Tipo Title
                        elemLabelSimple[1].setText(Integer.toString(TrovaComponentiPuliziaPerIdProdotto(idProdotto).get(1)));
                        elemLabelSimple[2].setText(Integer.toString(Integer.parseInt(TrovaValoreProdottoByIdProd(idProdotto, 1)) / 1000));
                        elemLabelSimple[3].setText(TrovaValoreProdottoByIdProd(idProdotto, 2));
                        elemLabelSimple[4].setText(ParametriSingolaMacchina.parametri.get(136));
                        indexComponente = j;
                        trovato = true;
                        elemBut[4].setEnabled(false);
                        elemBut[5].setEnabled(false);
                        break;
                    }
                }
            }

            if (!trovato) {

                //Visualizzazione e Gestione Errore
                ((Pannello44_Errori) pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(41);

                idProdotto = 0;

            }
        }

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            //////////////////////////////////////
            // ATTIVA ARIA A1 - FLUDIFICATORI  ///
            /////////////////////////////////////
            labelImageAux[2].setVisible(!labelImageAux[2].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {
                
                 if (labelImageAux[2].isVisible()) {
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
            labelImageAux[3].setVisible(!labelImageAux[3].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {
                
                if (labelImageAux[3].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                            OUTPUT_FALSE_CHAR);
                }
            }

        } else if (button.equals(elemBut[2].getName())) {

            ///////////////////////////////////////
            // ATTIVA ARIA A3 - SVUOTA VALVOLA ///
            ///////////////////////////////////////
            labelImageAux[4].setVisible(!labelImageAux[4].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {
                
                if (labelImageAux[4].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                            OUTPUT_FALSE_CHAR);
                } 
            }
        } else if (button.equals(elemBut[3].getName())) {

            ////////////////////////////////////
            // ATTIVA ARIA A4 - SVUOTA TUBO  ///
            ////////////////////////////////////
            labelImageAux[5].setVisible(!labelImageAux[5].isVisible());
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {
                
                if (labelImageAux[5].isVisible()) {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                            OUTPUT_TRUE_CHAR);
                } else {
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO,
                            OUTPUT_FALSE_CHAR);
                }
            }
        } else if (button.equals(elemBut[4].getName())) {

            /////////////////////
            // COMPONENTE GIU ///
            ///////////////////// 
            if (indexComponente > 0) {

                indexComponente--;

            } else {
                indexComponente = listaIdComponenti.size() - 1;

            }
            aggiornaComponenteVisualizzati(indexComponente);

        } else if (button.equals(elemBut[5].getName())) {

            ////////////////////
            // COMPONENTE SU ///
            ////////////////////
            if (indexComponente < listaIdComponenti.size() - 1) {

                indexComponente++;
            } else {
                indexComponente = 0;

            }
            aggiornaComponenteVisualizzati(indexComponente);

        } else if (button.equals(elemBut[6].getName())) {

            ///////////////////////////////
            // QUANTITA DA CARICARE GIU ///
            ///////////////////////////////
            if (Integer.parseInt(elemLabelSimple[1].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(435)) > 0) {

                elemLabelSimple[1].setText(Integer.toString(Integer.parseInt(elemLabelSimple[1].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(435))));
            }

        } else if (button.equals(elemBut[7].getName())) {

            //////////////////////////////
            // QUANTITA DA CARICARE SU ///
            //////////////////////////////
            elemLabelSimple[1].setText(Integer.toString(Integer.parseInt(elemLabelSimple[1].getText()) + Integer.parseInt(ParametriSingolaMacchina.parametri.get(435))));

        } else if (button.equals(elemBut[8].getName())) {

            /////////////////////
            // DURATA MIX GIU ///
            /////////////////////
            if (Integer.parseInt(elemLabelSimple[2].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(436)) > 0) {
                elemLabelSimple[2].setText(Integer.toString(Integer.parseInt(elemLabelSimple[2].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(436))));
            }
        } else if (button.equals(elemBut[9].getName())) {

            ////////////////////
            // DURATA MIX SU ///
            ////////////////////
            elemLabelSimple[2].setText(Integer.toString(Integer.parseInt(elemLabelSimple[2].getText()) + Integer.parseInt(ParametriSingolaMacchina.parametri.get(436))));

        } else if (button.equals(elemBut[10].getName())) {

            //////////////////
            // VEL MIX GIU ///
            //////////////////
            if (Integer.parseInt(elemLabelSimple[3].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(437)) > 0) {
                elemLabelSimple[3].setText(Integer.toString(Integer.parseInt(elemLabelSimple[3].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(437))));
            }
        } else if (button.equals(elemBut[11].getName())) {

            /////////////////
            // VEL MIX SU ///
            /////////////////
            if (Integer.parseInt(elemLabelSimple[3].getText()) <= PULIZIA_MAX_VEL) {
                elemLabelSimple[3].setText(Integer.toString(Integer.parseInt(elemLabelSimple[3].getText()) + Integer.parseInt(ParametriSingolaMacchina.parametri.get(437))));
            }
        } else if (button.equals(elemBut[12].getName())) {

            //////////////////////////
            // PESO CONFEZIONE GIU ///
            //////////////////////////
            if (Integer.parseInt(elemLabelSimple[4].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(438)) > 0) {
                elemLabelSimple[4].setText(Integer.toString(Integer.parseInt(elemLabelSimple[4].getText()) - Integer.parseInt(ParametriSingolaMacchina.parametri.get(438))));
            }

        } else if (button.equals(elemBut[13].getName())) {

            /////////////////////////
            // PESO CONFEZIONE SU ///
            ////////////////////////
            elemLabelSimple[4].setText(Integer.toString(Integer.parseInt(elemLabelSimple[4].getText()) + Integer.parseInt(ParametriSingolaMacchina.parametri.get(438))));

        } else if (button.equals(elemBut[14].getName())) {

            //////////////////
            // PULSANTE OK ///
            //////////////////
            impostaVisibilitaDatiPulizia(true);

            setDtInizio(new Date());

            //Calcolo del Peso da Raggiungere
            pulizia.pesoDaRaggiungereConfezione = Integer.parseInt(elemLabelSimple[1].getText())
                    - Integer.parseInt(ParametriSingolaMacchina.parametri.get(125));

            pulizia.dettagliPulizia = new DettagliPulizia(
                    listaIdComponenti.get(indexComponente), //Componente da pesare
                    Integer.parseInt(elemLabelSimple[1].getText()), //Quantità di componente da pesare
                    Integer.parseInt(elemLabelSimple[2].getText()), //Tempo di miscelazione
                    elemLabelSimple[3].getText(), //Velocità di miscelazione
                    Integer.parseInt(elemLabelSimple[4].getText()), //Peso Confezione
                    idOrdine, //Id Ordine
                    idProdotto);    //Id Prodotto 

            pulizia.dettagliPulizia.setIdCiclo(RegistraNuovoCiclo(ParametriGlobali.parametri.get(149), //tipo_ciclo,
                    pulizia.dettagliPulizia.getIdOrdine(), //id_ordine,
                    pulizia.dettagliPulizia.getIdProdotto(), TrovaIdCatPerIdProdotto(pulizia.dettagliPulizia.getIdProdotto()), //id_categoria,
                    Integer.parseInt(pulizia.dettagliPulizia.getVelMix()), //vel_mix,
                    pulizia.dettagliPulizia.getTempoMix(), //tempo_mix,
                    1, //numero_sacchi,
                    0, //int numero_sacchi_aggiuntivi,
                    Boolean.toString(!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(431))),//vibro_attivo,
                    "", //aria_condotta_scarico,
                    "", //aria_interno_valvola,
                    "", //aria_pulisci_valvola,
                    "", //info_1,
                    "", //info_2,
                    "", //info_3,
                    "", //info_4,
                    "", //info_5,
                    "", //info_6,
                    "", //info_7,
                    "", //info_8,
                    "", //info_9,
                    "", //info_10
                    "", //id_serie_colore
                    "")); //id_serie_additivo

            //Impostazione Validità Pulsanti Pulizia
            impostaValiditaPulsantiPulizia(false);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                    && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) { 
                
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR +
                            USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR +
                            USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE, 
                            OUTPUT_TRUE_CHAR+ OUTPUT_SEP_CHAR +
                            OUTPUT_TRUE_CHAR+ OUTPUT_SEP_CHAR +
                            OUTPUT_TRUE_CHAR); 
            }

            //Procedura di Pulizia Automatica
            pulizia.PuliziaAutomatica();

        } else if (button.equals(elemBut[15].getName())) {

            /////////////////////
            // PULSANTE PANIC ///
            ////////////////////
            impostaVisibilitaDatiPulizia(false);

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

        } else if (button.equals(elemBut[16].getName())) {

            ///////////////////
            // ATTIVA VIBRO ///
            ///////////////////
            new ThreadPuliziaAttivaVibroFunghiSilos(pulizia).start();
            
            
            System.out.println("PULSANTE VIBRO - PROCEDURA NON DEFINITA");

        }
    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello() {

        ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

        pulizia.interrompiComunicazioneSchedaIO = true;

        pannello_corrente.setVisible(false);

    }

    public void impostaVisibilitaDatiPulizia(Boolean vis) {

        if (vis) {
            aggiornaLabelPesaCarico("0");
            aggiornaLabelPesaScarico("0");

            String dt1 = EstraiStringaHtml(elemLabelTitle[2].getText()) + ": " + elemLabelSimple[1].getText() + " " + EstraiStringaHtml(elemLabelTitle[6].getText());
            String dt2 = EstraiStringaHtml(elemLabelTitle[3].getText()) + ": " + elemLabelSimple[2].getText() + " " + EstraiStringaHtml(elemLabelTitle[7].getText());
            String dt3 = EstraiStringaHtml(elemLabelTitle[4].getText()) + ": " + elemLabelSimple[3].getText() + " " + EstraiStringaHtml(elemLabelTitle[8].getText());
            String dt4 = EstraiStringaHtml(elemLabelTitle[5].getText()) + ": " + elemLabelSimple[4].getText() + " " + EstraiStringaHtml(elemLabelTitle[9].getText());

            elemLabelSimple[7].setText(HTML_STRINGA_INIZIO + dt1 + HTML_STRINGA_FINE);
            elemLabelSimple[8].setText(HTML_STRINGA_INIZIO + dt2 + HTML_STRINGA_FINE);
            elemLabelSimple[9].setText(HTML_STRINGA_INIZIO + dt3 + HTML_STRINGA_FINE);
            elemLabelSimple[10].setText(HTML_STRINGA_INIZIO + dt4 + HTML_STRINGA_FINE);

        }

        elemLabelSimple[1].setVisible(!vis);
        elemLabelSimple[2].setVisible(!vis);
        elemLabelSimple[3].setVisible(!vis);
        elemLabelSimple[4].setVisible(!vis);
        elemLabelSimple[5].setVisible(vis);
        elemLabelSimple[6].setVisible(vis);

        elemLabelTitle[2].setVisible(!vis);
        elemLabelTitle[3].setVisible(!vis);
        elemLabelTitle[4].setVisible(!vis);
        elemLabelTitle[5].setVisible(!vis);
        elemLabelTitle[6].setVisible(!vis);
        elemLabelTitle[7].setVisible(!vis);
        elemLabelTitle[8].setVisible(!vis);
        elemLabelTitle[9].setVisible(!vis);
        elemLabelTitle[10].setVisible(vis);
        elemLabelTitle[11].setVisible(vis);
        elemLabelTitle[12].setVisible(vis);

        elemBut[6].setVisible(!vis);
        elemBut[7].setVisible(!vis);
        elemBut[8].setVisible(!vis);
        elemBut[9].setVisible(!vis);
        elemBut[10].setVisible(!vis);
        elemBut[11].setVisible(!vis);
        elemBut[12].setVisible(!vis);
        elemBut[13].setVisible(!vis);
        elemBut[14].setVisible(!vis);
        elemBut[15].setVisible(vis);
        elemBut[16].setVisible(vis);

        labelImageAux[0].setVisible(!vis);
        labelImageAux[1].setVisible(vis);

        elemLabelSimple[7].setVisible(vis);
        elemLabelSimple[8].setVisible(vis);
        elemLabelSimple[9].setVisible(vis);
        elemLabelSimple[10].setVisible(vis);

    }

    //Aggiornamento Messaggio Operatore  
    public void modificaTestoMessaggioUtente(int index) {

        switch (index) {
            case 0: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 400, ParametriSingolaMacchina.parametri.get(111)));  //POSIZIONA E BLOCCA SACCO
                break;
            }

            case 1: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 436, ParametriSingolaMacchina.parametri.get(111)));  //PREMERE PULSANTE "STOP"
                break;
            }

            case 2: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 654, ParametriSingolaMacchina.parametri.get(111)));  //APERTURA-CHIUSURA VALVOLA DI CARICO
                break;
            }

            case 3: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 655, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - CARICO INERTI
                break;
            }

            case 4: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 656, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - MISCELAZIONE
                break;
            }

            case 5: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 657, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - APERTURA VALVOLA
                break;
            }

            case 6: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 658, ParametriSingolaMacchina.parametri.get(111)));   //PULIZIA AUTOMATICA - CHIUSURA VALVOLA
                break;
            }

            case 7: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 659, ParametriSingolaMacchina.parametri.get(111)));  //SVUOTAMENTO MISCELATORE
                break;
            }
            case 8: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 748, ParametriSingolaMacchina.parametri.get(111)));  //DEVIATORE ASPIRATORE ATTIVATO - POSIZIONARE SACCO
                break;
            }

            case 9: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 957, ParametriSingolaMacchina.parametri.get(111)));  //DEVIATORE ASPIRATORE ATTIVATO - POSIZIONARE SACCO
                break;
            }

            case 10: {
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 970, ParametriSingolaMacchina.parametri.get(111)));  //PULIZIA AUTOMATICA - OK PER AVVIARE LA PROCEDURA
                break;
            }

        }
    }

    //Inizializzazione Pannello
    public void initPannello() {

        modificaTestoMessaggioUtente(10);

        impostaVisibilitaDatiPulizia(false);
        impostaValiditaPulsantiPulizia(true);

    }

    public void impostaValiditaPulsantiPulizia(Boolean vis) {

        elemBut[4].setEnabled(vis);
        elemBut[5].setEnabled(vis);
        elemBut[6].setEnabled(vis);
        elemBut[7].setEnabled(vis);
        elemBut[8].setEnabled(vis);
        elemBut[9].setEnabled(vis);
        elemBut[10].setEnabled(vis);
        elemBut[11].setEnabled(vis);
        elemBut[12].setEnabled(vis);
        elemBut[13].setEnabled(vis);

    }

    public void aggiornaLabelPesaCarico(String valore_peso) {

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

        pulizia.ultimoPesoBilanciaCarico = valore_peso;
    }

    public void aggiornaLabelPesaScarico(String valore_peso) {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            ////////////////////////
            // CONVERSIONE PESO  ///
            ////////////////////////
            elemLabelSimple[6].setText(ConvertiPesoVisualizzato(valore_peso, ParametriSingolaMacchina.parametri.get(338))
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340));
        } else {
            ///////////////////////
            // SISTEMA METRICO  ///
            ///////////////////////                
            elemLabelSimple[6].setText(valore_peso
                    + " "
                    + ParametriSingolaMacchina.parametri.get(340));
        }

        pulizia.ultimoPesoBilanciaSacchetti = valore_peso;

    }

    public void impostaVisLabelTempoResiduo(Boolean vis) {

        pulizia.pannelloPulizia.labelImageAux[6].setVisible(vis);
        pulizia.pannelloPulizia.elemLabelSimple[12].setVisible(vis);
    }

    public void impostaLabelTempoResiduo(Integer timeToWait) {

        pulizia.pannelloPulizia.elemLabelSimple[12].setText(Integer.toString(timeToWait));
    }

    public Date getDtInizio() {
        return dtInizio;
    }

    public void setDtInizio(Date dtInizio) {
        this.dtInizio = dtInizio;
    }
}
