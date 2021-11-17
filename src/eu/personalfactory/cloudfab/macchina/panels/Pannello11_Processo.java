package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdPresaPerPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMovimentoMagazzinoMateriaPrima;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaPresaAbilitata;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_TEXT_FIELD;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdom.Element;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.processo.ActionListenerControlloCodice;
import eu.personalfactory.cloudfab.macchina.processo.Processo;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoAttivaSvuotaValvolaPesaturaFine;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoInizializzaPannelloProcesso;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoResetProcesso;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabFrame;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudFont;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello11_Processo extends MyStepPanel {

    //Parametri Pannello
    private final int numLabelComponenti = 4;
    private final int numImage = 32;
    //Variabili
    private Processo processo;
    public int codType;
    private ActionListenerControlloCodice ctrlCodice;
    public boolean ripristinato;
    public boolean serverIngressiIsConnected;
    public boolean valutaPresaDirettaEseguito;
    public ArrayList<JTextField> txtFieldAuxList;
    int idCompPesaManuale = 0;
    int id_movimento_pesa_manuale = 0;
    int ultimo_pannello = 0;

    //COSTRUTTORE
    public Pannello11_Processo() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(39);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(24);
        impostaDimLabelProg(0);
        impostaDimLabelBut(20);
        impostaColori(10);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Inizializzazione Colori Label Simple Pulsanti
        elemLabelSimple[26].setForeground(elemColor[1]);
        elemLabelSimple[27].setForeground(elemColor[2]);
        elemLabelSimple[28].setForeground(elemColor[2]);
        elemLabelSimple[29].setForeground(elemColor[2]);
        elemLabelSimple[30].setForeground(elemColor[1]);

        //Configurazione di Base Pannello
        configuraPannello();

        //Inserimento Text Field
        inserisciTextField();

        //Inserimento Tastiera
        inserisciTastiera();
        tastiera.impostaVisibilitaTastiera(false);

        //Creazione Action Listener Controllo Codice
        ctrlCodice = new ActionListenerControlloCodice();

        //Creazione Action Listener Text Field
        txtField.addActionListener(ctrlCodice);

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        inserisciTextFieldAux(1);

    }

    //Inizializzazione Pannello
    public void initPanel() {

        valutaPresaDirettaEseguito = false;

        pannelliCollegati.get(3).setVisible(false);

        //Aggiornamento Valori Parametri Singola Macchina
        ParametriSingolaMacchina.init();

        //Impostazione Iniziale Visibilità Pannello
        setPanelVisibile();

        //Inizializzazione Interfaccia Pannello
        new ThreadProcessoInizializzaPannelloProcesso(this).start();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Nuovo Istanza Oggetto Processo
        processo = new Processo(this);

        //Assegnazione Oggetto Processo ad Action Listenere "Controllo Codice"
        ctrlCodice.SetProcesso(processo);

        //Inizializza Pannello Alternativo
        ((Pannello43_Processo_Pesatura_Manuale) pannelliCollegati.get(3)).initPanel(processo);

    }
    

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 159, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 160, ParametriSingolaMacchina.parametri.get(111)))
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 168, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                elemLabelTitle[2].setText("");
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 161, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 162, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 186, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 187, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[7].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 189, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[8].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 194, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[9].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 186, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 187, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 195, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[12].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 196, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelTitle[18].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 880, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[19].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 884, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[20].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 885, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelTitle[21].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 887, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[22].setText(ParametriSingolaMacchina.parametri.get(340));
                elemLabelTitle[23].setText(ParametriSingolaMacchina.parametri.get(340));

                //elemLabelSimple[24].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 734, ParametriSingolaMacchina.parametri.get(111)));
               //elemLabelSimple[25].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 735, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[26].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 752, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[27].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 753, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[28].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 754, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[29].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 754, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[32].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 881, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[33].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 882, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[34].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 883, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Aggiornamento Visualizzazione Peso - Pesa di Carico
    public void aggiornaLabelPesaCarico(int valore1, int valore2) {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            ///////////////////////////
            // CONVERSIONE DI PESO  ///
            ///////////////////////////
            elemLabelSimple[12].setText(ConvertiPesoVisualizzato(
                    Integer.toString(valore1),
                    ParametriSingolaMacchina.parametri.get(338)));
            elemLabelSimple[13].setText(ConvertiPesoVisualizzato(
                    Integer.toString(valore2),
                    ParametriSingolaMacchina.parametri.get(338)));

        } else {

            ///////////////////////
            // SISTEMA METRICO  ///
            ///////////////////////
            elemLabelSimple[12].setText(Integer.toString(valore1));
            elemLabelSimple[13].setText(Integer.toString(valore2));
        }

    }

    //Aggiornamento Visualizzazione Peso - Pesa Sacchetti
    public void aggiornaLabelPesaConfezioni(String peso) {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

            elemLabelSimple[17].setText(ConvertiPesoVisualizzato(
                    peso,
                    ParametriSingolaMacchina.parametri.get(338)));
        } else {

            elemLabelSimple[17].setText(peso);
        }
    }

    //Scambio Pannello
    public void gestoreScambioPannello() {

        cambioPanelDietro();

    }

    //Aggiornamento Visualizzazione Componenti in Pesa
    public void aggiornaLabelComponenti() {

        int j = 0;
        int k = 0;

        for (int i = processo.indiceCompInPesa; i < processo.numeroMateriePrime; i++) {

            if (j < numLabelComponenti && (processo.indiceCompInPesa + k < processo.numeroMateriePrime)) {

                //Modifica 11Agosto2014 
                if (VerificaPresaAbilitata(TrovaIdPresaPerPresa(processo.componenti.get(processo.indiceCompInPesa + k).getPresa()))) {

                    elemLabelSimple[0 + j * 3].setText(HTML_STRINGA_INIZIO
                            + processo.componenti.get(processo.indiceCompInPesa + k).getNome()
                            + HTML_STRINGA_FINE);
                    elemLabelSimple[1 + j * 3].setText(HTML_STRINGA_INIZIO
                            + processo.componenti.get(processo.indiceCompInPesa + k).getPresa()
                            + HTML_STRINGA_FINE);

                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                        ///////////////////////////
                        // CONVERSIONE DI PESO  ///
                        ///////////////////////////
                        elemLabelSimple[2 + j * 3].setText(HTML_STRINGA_INIZIO
                                + ConvertiPesoVisualizzato(
                                        Integer.toString(processo.componenti.get(processo.indiceCompInPesa + k).getQuantità()),
                                        ParametriSingolaMacchina.parametri.get(338))
                                + HTML_STRINGA_FINE);

                    } else {

                        ///////////////////////
                        // SISTEMA METRICO  ///
                        ///////////////////////
                        elemLabelSimple[2 + j * 3].setText(HTML_STRINGA_INIZIO
                                + processo.componenti.get(processo.indiceCompInPesa + k).getQuantità()
                                + HTML_STRINGA_FINE);

                    }
                    j++;
                }

            }
            k++;
        }
        for (int i = j; i < numLabelComponenti; i++) {
            elemLabelSimple[0 + i * 3].setText("");
            elemLabelSimple[1 + i * 3].setText("");
            elemLabelSimple[2 + i * 3].setText("");
        }
    }

    //Gestisce la Visibiltà ed il Contenuto degli Elementi del Pannello
    public void modificaPannello(int s) {

        ultimo_pannello = s;
        switch (s) {
            case 0: {

                for (int i = 0; i < 14; i++) {
                    elemLabelSimple[i].setVisible(true);

                }

                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 169, ParametriSingolaMacchina.parametri.get(111)));
                // new visualizzaCompPesaComplementari().start();
                aggiornaLabelPesaCarico(0, 0);
                labelImageAux[0].setVisible(true);
                labelImageAux[1].setVisible(true);
                elemLabelTitle[3].setVisible(true);
                elemLabelTitle[4].setVisible(true);
                break;

            }
            case 1: {
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 170, ParametriSingolaMacchina.parametri.get(111)));

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                elemLabelTitle[3].setVisible(false);
                elemLabelTitle[4].setVisible(false);

                for (int i = 0; i < 14; i++) {
                    elemLabelSimple[i].setVisible(false);
                }

                break;
            }
            case 2: {
                codType = 0;
                elemBut[0].setVisible(true);
                labelImageAux[3].setVisible(true);
                ///// labelImageAux[4].setVisible(true);
                elemLabelTitle[7].setVisible(true);
                txtField.setVisible(true);
                txtField.requestFocusInWindow();
                txtField.setText("");
                //Label Pulsante Manuale-Lettore
                elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 190, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[16].setVisible(true);
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 171, ParametriSingolaMacchina.parametri.get(111)));
                break;
            }
            case 3: {
                elemBut[0].setVisible(false);
                elemBut[1].setVisible(false);
                txtField.setVisible(false);
                tastiera.impostaVisibilitaTastiera(false);
                aggiornaVisLabelDettagliProduzione(true);
                labelImageAux[3].setVisible(false);
                labelImageAux[4].setVisible(false);
                elemLabelTitle[7].setVisible(false);
                elemLabelSimple[16].setVisible(false);
                labelImageAux[17].setVisible(false);
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 172, ParametriSingolaMacchina.parametri.get(111)));
                break;
            }
            case 4: {

                codType = 1;
                elemBut[0].setVisible(true);
                labelImageAux[3].setVisible(true);
                elemLabelTitle[7].setVisible(true);
                txtField.setVisible(true);
                txtField.requestFocusInWindow();
                txtField.setText("");
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 173, ParametriSingolaMacchina.parametri.get(111)));
                //Label Pulsante Manuale-Lettore
                elemLabelSimple[16].setVisible(true);
                elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 190, ParametriSingolaMacchina.parametri.get(111)));

                break;
            }
            case 5: {

                labelImageAux[13].setVisible(true);
                labelImageAux[17].setVisible(false);
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 174, ParametriSingolaMacchina.parametri.get(111)));

                break;
            }
            case 6: {
                labelImageAux[13].setVisible(false);
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 175, ParametriSingolaMacchina.parametri.get(111)));
                break;
            }
            case 7: {
                for (int i = 0; i < 14; i++) {
                    elemLabelSimple[i].setVisible(true);

                }

                labelImageAux[0].setVisible(true);
                labelImageAux[1].setVisible(true);
                elemLabelTitle[3].setVisible(true);
                elemLabelTitle[4].setVisible(true);
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 176, ParametriSingolaMacchina.parametri.get(111)));
                break;
            }

            case 8: {
                for (int i = 0; i < 14; i++) {
                    elemLabelSimple[i].setVisible(false);

                }

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                labelImageAux[2].setVisible(false);
                elemLabelTitle[3].setVisible(false);
                elemLabelTitle[4].setVisible(false);
                labelImageAux[13].setVisible(false);
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 177, ParametriSingolaMacchina.parametri.get(111)));
                break;
            }

            case 9: {

                labelImageAux[14].setVisible(true);
                labelImageAux[15].setVisible(true);
                labelImageAux[16].setVisible(false);
                labelImageAux[12].setVisible(false);
                //Titolo - Pesatura Sacchetti
                elemLabelTitle[8].setVisible(true);
                //Peso Sacchetti
                elemLabelSimple[17].setVisible(true);
                break;
            }

            case 10: {

                //Image Sfondo Peso Sacchetto
                labelImageAux[14].setVisible(false);
                labelImageAux[15].setVisible(false);
                labelImageAux[16].setVisible(false);
                labelImageAux[27].setVisible(true);
                elemLabelTitle[8].setVisible(false);
                //Miscele da Pesare
                elemLabelSimple[17].setVisible(false);

                break;
            }

            case 11: {

                labelImageAux[27].setVisible(false);
                break;
            }

            case 12: {

                for (JLabel labelImageAux1 : labelImageAux) {
                    labelImageAux1.setVisible(false);
                }

                for (JLabel elemLabelSimple1 : elemLabelSimple) {
                    elemLabelSimple1.setVisible(false);
                }

                //Impostazione Visibilità Tastiera
                tastiera.impostaVisibilitaTastiera(false);

                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 178, ParametriSingolaMacchina.parametri.get(111)));
                break;
            }

            case 13: {

                elemBut[0].setVisible(false);
                labelImageAux[3].setVisible(false);
                labelImageAux[4].setVisible(false);
                elemLabelTitle[7].setVisible(false);
                txtField.setVisible(false);
                elemLabelSimple[16].setVisible(false);
                tastiera.impostaVisibilitaTastiera(false);
                aggiornaVisLabelDettagliProduzione(true);
                break;
            }

            case 14: {
                labelImageAux[13].setVisible(false);
                break;
            }

            case 15: {
                elemLabelTitle[8].setVisible(false);
                //Image Sfondo Peso Sacchetto
                labelImageAux[14].setVisible(false);
                //Miscele da Pesare
                elemLabelSimple[17].setVisible(false);
                labelImageAux[15].setVisible(false);
                labelImageAux[16].setVisible(false);
                break;
            }

            case 16: {
                labelImageAux[15].setVisible(true);
                labelImageAux[16].setVisible(false);
                break;
            }
            case 17: {
                labelImageAux[15].setVisible(false);
                labelImageAux[16].setVisible(true);
                break;
            }

////////////////            case 18: {
////////////////
//////////////////////                if (Benefit.findPresenzaComponentiPesaManuale(processo)) {
//////////////////////                    elemBut[16].setVisible(true);
//////////////////////                }
//////////////////////                elemBut[15].setVisible(false);
//////////////////////                labelImageAux[31].setVisible(false);
//////////////////////
//////////////////////                labelImageAux[30].setVisible(false);
//////////////////////
//////////////////////                //Pulsanti
//////////////////////                elemBut[17].setVisible(false);
//////////////////////                elemBut[18].setVisible(false);
//////////////////////                elemBut[19].setVisible(false);
//////////////////////                elemLabelSimple[32].setVisible(false);
//////////////////////                elemLabelSimple[33].setVisible(false);
//////////////////////                elemLabelSimple[34].setVisible(false);
//////////////////////                elemLabelSimple[35].setVisible(false);
//////////////////////                elemLabelSimple[36].setVisible(false);
//////////////////////                elemLabelSimple[37].setVisible(false);
//////////////////////
//////////////////////                elemLabelTitle[18].setVisible(false);
//////////////////////                elemLabelTitle[1].setVisible(true);
//////////////////////                elemLabelTitle[19].setVisible(false);
//////////////////////                elemLabelTitle[20].setVisible(false);
//////////////////////                elemLabelTitle[21].setVisible(false);
//////////////////////                elemLabelTitle[22].setVisible(false);
//////////////////////                elemLabelTitle[23].setVisible(false);
//////////////////////
//////////////////////                tastiera.assegnaTextField(txtField);
//////////////////////                txtFieldAuxList.get(0).setVisible(false);
////////////////
////////////////                break;
////////////////            }
////////////////
////////////////            case 19: {
////////////                boolean findComponenteDaPesare = false;
////////////                
////////////                for (int i = 0; i < processo.componenti.size(); i++) {
////////////
////////////                    if (processo.componenti.get(i).getMetodoPesa().equals(ParametriGlobali.parametri.get(131))) {
////////////
////////////                        if (!Boolean.parseBoolean(Benefit.findGruppoValoreParametroRipristino(25).get(i))) {
////////////                            elemLabelSimple[35].setText(processo.componenti.get(i).getNome());
////////////
////////////                            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {
////////////
////////////                                ///////////////////////////
////////////                                // CONVERSIONE DI PESO  ///
////////////                                /////////////////////////// 
////////////                                elemLabelSimple[36].setText(Benefit.convertiPesoVisualizzato(
////////////                                        Integer.toString(processo.componenti.get(i).getQuantità()),
////////////                                        ParametriSingolaMacchina.parametri.get(338)));
////////////
////////////                            } else {
////////////
////////////                                ///////////////////////
////////////                                // SISTEMA METRICO  ///
////////////                                /////////////////////// 
////////////                                elemLabelSimple[36].setText(Integer.toString(processo.componenti.get(i).getQuantità()));
////////////
////////////                            }
////////////
////////////                            idCompPesaManuale = i;
////////////                            findComponenteDaPesare = true;
////////////                            break;
////////////                        }
////////////                    }
////////////                }
////////////
////////////                if (findComponenteDaPesare) {
////////////
////////////                    /////////////////////////////////
////////////                    //PESA COMPONENTE DA ESEGUIRE  //
////////////                    /////////////////////////////////
////////////                    txtFieldAuxList.get(0).setVisible(true);
////////////                    txtFieldAuxList.get(0).addActionListener(new ActionListener() {
////////////
////////////                        @Override
////////////                        public void actionPerformed(ActionEvent e) {
////////////                        }
////////////                    });
////////////
////////////                    txtFieldAuxList.get(0).addFocusListener(new FocusListener() {
////////////
////////////                        @Override
////////////                        public void focusGained(FocusEvent e) {
////////////                            txtFieldAuxList.get(0).setText(null);
////////////                            //tastiera.impostaVisibilitaTastiera(true);
////////////                            for (JLabel elemLabelHelp1 : elemLabelHelp) {
////////////                                elemLabelHelp1.setVisible(false);
////////////                            }
////////////                        }
////////////
////////////                        @Override
////////////                        public void focusLost(FocusEvent e) {
////////////                            //throw new UnsupportedOperationException("Not supported yet."); 
////////////                        }
////////////                    });
////////////
////////////                    tastiera.assegnaTextField(txtFieldAuxList.get(0));
////////////
////////////                    labelImageAux[30].setVisible(true);
////////////                    labelImageAux[31].setVisible(false);
////////////                    elemBut[15].setVisible(true);
////////////                    elemBut[16].setVisible(false);
////////////
////////////                    //Pulsanti
////////////                    elemBut[17].setVisible(true);
////////////                    elemBut[18].setVisible(false);
////////////                    elemBut[19].setVisible(true);
////////////                    elemLabelSimple[32].setVisible(true);
////////////                    elemLabelSimple[33].setVisible(false);
////////////                    elemLabelSimple[34].setVisible(true);
////////////
////////////                    elemLabelSimple[35].setVisible(true);
////////////                    elemLabelSimple[36].setVisible(false);
////////////                    elemLabelSimple[37].setVisible(false);
////////////                    elemLabelSimple[37].setText("0");
////////////
////////////                    elemLabelTitle[18].setVisible(true);
////////////                    elemLabelTitle[1].setVisible(false);
////////////                    elemLabelTitle[19].setVisible(true);
////////////                    elemLabelTitle[20].setVisible(true);
////////////                    elemLabelTitle[20].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 885, ParametriSingolaMacchina.parametri.get(111)));
////////////                    elemLabelTitle[21].setVisible(false);
////////////                    elemLabelTitle[22].setVisible(false);
////////////                    elemLabelTitle[23].setVisible(false);
////////////
////////////                    impostaVisElementiPannelloMicro(false);
////////////
////////////                } else {
////////////
////////////                    ////////////////////////////////////////
////////////                    //PESA MANUALE COMPONENTI COMPLETATA  //
////////////                    ////////////////////////////////////////
////////////                    processo.pesaturaManualeCompletata = true;
////////////
////////////                    elemBut[16].setVisible(false);
////////////                    elemBut[15].setVisible(false);
////////////                    labelImageAux[31].setVisible(false);
////////////
////////////                    labelImageAux[30].setVisible(false);
////////////
////////////                    //Pulsanti
////////////                    elemBut[17].setVisible(false);
////////////                    elemBut[18].setVisible(false);
////////////                    elemBut[19].setVisible(false);
////////////                    elemLabelSimple[32].setVisible(false);
////////////                    elemLabelSimple[33].setVisible(false);
////////////                    elemLabelSimple[34].setVisible(false);
////////////                    elemLabelSimple[35].setVisible(false);
////////////                    elemLabelSimple[36].setVisible(false);
////////////                    elemLabelSimple[37].setVisible(false);
////////////
////////////                    elemLabelTitle[18].setVisible(false);
////////////                    elemLabelTitle[1].setVisible(true);
////////////                    elemLabelTitle[19].setVisible(false);
////////////                    elemLabelTitle[20].setVisible(false);
////////////                    elemLabelTitle[21].setVisible(false);
////////////                    elemLabelTitle[22].setVisible(false);
////////////                    elemLabelTitle[23].setVisible(false);
////////////
////////////                    tastiera.assegnaTextField(txtField);
////////////
////////////                }
////////////////////
////////////////////                break;
////////////////////            }
////////////////////
////////////////////            case 20: {
////////////////////
////////////////////                labelImageAux[30].setVisible(false);
////////////////////                labelImageAux[31].setVisible(true);
////////////////////                elemBut[15].setVisible(true);
////////////////////                //elemBut[16].setVisible(false);
////////////////////
////////////////////                //Pulsanti
////////////////////                elemBut[17].setVisible(false);
////////////////////                elemBut[18].setVisible(true);
////////////////////                elemBut[19].setVisible(true);
////////////////////                elemLabelSimple[32].setVisible(false);
////////////////////                elemLabelSimple[33].setVisible(true);
////////////////////                elemLabelSimple[34].setVisible(true);
////////////////////
////////////////////                elemLabelSimple[35].setVisible(true);
////////////////////                elemLabelSimple[36].setVisible(true);
////////////////////                elemLabelSimple[37].setVisible(true);
////////////////////
////////////////////                elemLabelTitle[18].setVisible(true);
////////////////////                elemLabelTitle[1].setVisible(false);
////////////////////                elemLabelTitle[19].setVisible(true);
////////////////////                elemLabelTitle[20].setVisible(true);
////////////////////                elemLabelTitle[20].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 886, ParametriSingolaMacchina.parametri.get(111)));
////////////////////                elemLabelTitle[21].setVisible(true);
////////////////////                elemLabelTitle[22].setVisible(true);
////////////////////                elemLabelTitle[23].setVisible(true);
////////////////////                txtFieldAuxList.get(0).setText("");
////////////////////                txtFieldAuxList.get(0).setVisible(false);
////////////////////                break;
////////////////////            }
            default: {

                //Memorizzazione Logger della Sessione
                ProcessoLogger.logger.severe("Errore di Visualizzazione Pannello Processo...");

                break;
            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        switch (button) {
            case "0": {

                //Eliminazione Testo Text Field
                resetTextField();
                if (tastiera.getVisibilitaTastiera()) {
                    //Modifica Testo Pulsante - MANUALE
                    elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 190, ParametriSingolaMacchina.parametri.get(111)));
                    txtField.requestFocusInWindow();

                } else {
                    //Modifica Testo Pulsante - LETTORE
                    elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 191, ParametriSingolaMacchina.parametri.get(111)));
                }
                //Modifica lo Stato di Visibilità della Tastiera
                tastiera.impostaVisibilitaTastiera(!tastiera.getVisibilitaTastiera());
                aggiornaVisLabelDettagliProduzione(!tastiera.getVisibilitaTastiera());
                //Impostazione Visibilità Tasto Invio 
                elemBut[1].setVisible(tastiera.getVisibilitaTastiera());
                //Impostazione Visibilità Sfondo Tastiera
                labelImageAux[17].setVisible(tastiera.getVisibilitaTastiera());
                break;
            }

            case "1": {
                txtField.postActionEvent();
                tastiera.impostaVisibilitaTastiera(false);
                aggiornaVisLabelDettagliProduzione(true);
                //Modifica Testo Pulsante - MANUALE 
                elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 190, ParametriSingolaMacchina.parametri.get(111)));
                //Visibilità Immagine di Sfondo Dettagli Produzione
                labelImageAux[4].setVisible(false);
                labelImageAux[17].setVisible(false);
                elemBut[1].setVisible(false);
                break;
            }

            case "2": {

                //////////////////////
                // PULSANTE PANIC  ///
                //////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante di Reset dal Pannello Processo");

                new ThreadProcessoResetProcesso(processo).start();

                break;
            }

            case "3": {

                ////////////////////////////////////////
                // PULSANTE FORZA VELOCITA' SCARICO  ///
                ////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante di Forzatura Vel Normale Pesatura Fine");

                if (processo.raggiungimentoCondArrestoMotore) {

                    //Avvio Motore Miscelartore
                    inverter_mix.avviaArrestaInverter(true);

                } else {

                    //Riattivazione Velocità Normale Motore
                    processo.attivaVelNormaleMiscelatore = true;
                }

                //Controllo Presenza Fluidificatori e Abilitazione con contatto Speed
                if (!processo.threadFludificaPesaturaFineAttivo
                        && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))
                        && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(357))) {

                    //////////////////////////////
                    // AVVIO THREAD FLUDIFICA  ///
                    //////////////////////////////
                    processo.threadFludificaPesaturaFineAttivo = true;

                    new ThreadProcessoAttivaSvuotaValvolaPesaturaFine(processo).start();

                }

                break;
            }

            case "4": {

                ///////////////////////////////////////
                // PULSANTE FORZA VELOCITA' CARICO  ///
                ///////////////////////////////////////
                elemBut[4].setVisible(false);
                elemLabelSimple[29].setVisible(false);

                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante di Forzatura Vel Carico Componenti");

                processo.pulsanteSpeedAttivato = true;

                //Modifica 17 SETTEMBRE (FORZATURA VELOCITA CARICO UNICA VELOCITA)
                processo.forzaturaVelManualeInCorso = true;

                break;
            }
            case "5": {

                /////////////////////////////////////////////////
                // PULSANTE LINEA DIRETTA MOTORE MISCELATORE  ///
                /////////////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Linea Diretta Miscelatore");

                processo.abilitaLineaDirettaMiscelatore = true;

                elemBut[5].setVisible(false);
                elemLabelSimple[30].setVisible(false);

                processo.abilitaLineaDirettaMiscelatore();

                break;
            }

            case "6": {

                /////////////////////////////
                // PULSANTE MICRO DIETRO  ///
                /////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Micro Dietro");

                processo.counterMicroVis = 0;

                if (processo.indiceMicroVisibile > 0) {
                    processo.indiceMicroVisibile--;

                } else {

                    ////////////////////////////////////
                    // MICRODOSATORE SERIE 1 - 2017  ///
                    ////////////////////////////////////
                    processo.indiceMicroVisibile = processo.microdosatori_2017.size() - 1;

                }

                break;
            }

            case "7": {

                /////////////////////////////
                // PULSANTE MICRO AVANTI  ///
                /////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Micro Avanti");

                processo.counterMicroVis = 0;

                ////////////////////////////////////
                // MICRODOSATORE SERIE 1 - 2017  ///
                ////////////////////////////////////
                if (processo.indiceMicroVisibile < processo.microdosatori_2017.size() - 1) {
                    processo.indiceMicroVisibile++;

                } else {

                    processo.indiceMicroVisibile = 0;

                }

                break;
            }

            case "8": {

                //////////////////////////////////////////
                // PULSANTE FORZA PESATURA COMPONENTE  ///
                //////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Forza Pesatura Componente");

                processo.forzaPesoComponente = true;

                elemBut[8].setVisible(false);
                elemLabelSimple[27].setVisible(false);

                break;
            }

            case "9": {

                /////////////////////////////////////
                // PULSANTE ATTIVA VIBRO  FUNGHI  ///
                /////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Attiva Vibro Funghi");

                impostaAbilitaPulsantiVibroFunghi(false);

                processo.attivaVibroFunghiSiloCorrente(true);

                break;
            }

            case "10": {

                ////////////////////////////////////
                // PULSANTE ARRESTA VIBRO FUNGHI ///
                ////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Disattiva Vibro Funghi");

                impostaAbilitaPulsantiVibroFunghi(true);

                processo.attivaVibroFunghiSiloCorrente(false);

                processo.interrompiThreadVibro = true;

                break;
            }

            case "11": {

                /////////////////////////////////////////////
                // PULSANTE DISATTIVA TRACCIABILITA SACCO ///
                /////////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Disabilita Tracciabilita");

                processo.disattivaTracc = true;

                elemBut[11].setVisible(false);

                break;
            }

            case "12": {

                ///////////////////////////////////////////
                // PULSANTE MULTIFUNZIONE MICRODOSATURA ///
                ///////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Multifunzione Microdosatura");

                if (EstraiStringaHtml(
                        elemLabelSimple[23].getText()).equals(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 818, ParametriSingolaMacchina.parametri.get(111))))) {

                    ////////////////////////////////////
                    // MICRODOSATORE SERIE 1 - 2017  ///
                    ////////////////////////////////////
                    ////processo.microdosatori_2017.get(processo.indiceMicroVisibile).inizializzaDispositivo();
                } else if (EstraiStringaHtml(
                        elemLabelSimple[23].getText()).equals(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 819, ParametriSingolaMacchina.parametri.get(111))))) {

                    ////////////////////////////////////
                    // MICRODOSATORE SERIE 1 - 2017  ///
                    ////////////////////////////////////
                    ///processo.microdosatori_2017.get(processo.indiceMicroVisibile).avviaPesatura();
                }
                break;
            }
            case "13": {

                ////////////////////////////
                // FORZA PESA AGGIUNTIVA ///
                ////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Forza Pesa Aggiuntiva");

                processo.forzaPesaAggiuntiva = true;

                elemBut[13].setVisible(false);

                break;
            }

            case "14": {

                //////////////////////////////////
                // CHIUSURA MANUALE ASPIRATORE ///
                //////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Chiusura Manuale Aspiratore");

                GestoreIO_ModificaOut(
                        USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                        OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                        + OUTPUT_FALSE_CHAR);

                elemBut[14].setVisible(false);

                break;
            }
            case "15": {

                //////////////////////////////////////
                // NASCONDI ELEMENTI PESA MANUALE  ///
                //////////////////////////////////////
                //Attivazione Contattore Miscelatore 
                if (processo.visibilitaMicro) {
                    impostaVisElementiPannelloMicro(true);
                }

                break;
            }

            case "16": {
               
                ((Pannello43_Processo_Pesatura_Manuale) pannelliCollegati.get(3)).impostaVisibilitaPannello();

                break;
            }
//
//            case "17": {
//
//                break;
//            }

            case "18": {

                ////////////////////////////////////////
                // PULSANTE ATTIVA VIBRO PNEUMATICI  ///
                ////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Attiva Vibro Pneumatici");

                impostaAbilitaPulsantiVibroPneumatici(false);

                processo.attivaVibroPneumaticiSiloCorrente(true);

                break;
            }

            case "19": {

                ////////////////////////////////////////
                // PULSANTE ARRESTA VIBRO NEUMATICI  ///
                ////////////////////////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante Disattiva Vibro Pneumatici");

                impostaAbilitaPulsantiVibroPneumatici(true);

                processo.attivaVibroPneumaticiSiloCorrente(false);


                break;
            }

//            case "20": {
//
//                new ThreadProcessoAttivaVibroElettricoSiloDedicato(processo).start();
//
//                break;
//            }

        }

    }

    //Elimina il Testo Presente nel Text Field
    public void resetTextField() {
        txtField.setText("");
    }

    public void aggiornaLabelConfezioniPesate(int i) {

        elemLabelSimple[18].setText(Integer.toString(i));
    }

    public void aggiornaLabelMiscelePesate(int num) {

        elemLabelSimple[15].setText(Integer.toString(num));
    }

    public void aggiornaVisLabelDettagliProduzione(boolean vis) {

        //Miscele da Pesare
        elemLabelSimple[14].setVisible(vis);

        //Miscele Pesate
        elemLabelSimple[15].setVisible(vis);

        //Confezioni da Pesare
        elemLabelSimple[19].setVisible(vis);

        //Confezioni Pesate
        elemLabelSimple[18].setVisible(vis);

        //Visibilità Immagine di Sfondo Dettagli Produzione
        labelImageAux[26].setVisible(vis);

        elemLabelTitle[5].setVisible(vis);

        elemLabelTitle[6].setVisible(vis);

        elemLabelTitle[9].setVisible(vis);

        elemLabelTitle[10].setVisible(vis);

        elemLabelTitle[11].setVisible(vis);

        elemLabelTitle[12].setVisible(vis);

    }

    //Modifica Valore Label Tempo Residuo Miscelazione
    public void impostaLabelTempoResiduoMiscelazione(String timeToWait) {

        elemLabelSimple[20].setText(timeToWait);

    }

    //Modifica Valore Label Tempo Residuo Miscelazione
    public void impostaVisLabelTempoResiduoMiscelazione(Boolean vis) {

        elemLabelSimple[20].setVisible(vis);
        labelImageAux[28].setVisible(vis);

    }

    public void impostaVisPulsantiVibroFunghi(boolean vis) {

        elemBut[9].setVisible(vis);
        elemBut[10].setVisible(!vis); 

    }
    
    public void resetVisPulsantiVibro() {

        elemBut[9].setVisible(false);
        elemBut[10].setVisible(false);
        elemBut[18].setVisible(false);
        elemBut[19].setVisible(false);

    }

    public void impostaAbilitaPulsantiVibroFunghi(boolean vis) {

        processo.pannelloProcesso.elemBut[9].setEnabled(vis);
        processo.pannelloProcesso.elemBut[10].setEnabled(!vis);
        
        processo.pannelloProcesso.elemBut[9].setVisible(vis);
        processo.pannelloProcesso.elemBut[10].setVisible(!vis);

    }
    
    public void impostaVisPulsantiVibroPneumatici(boolean vis) {

        elemBut[18].setVisible(vis);
        elemBut[19].setVisible(!vis);

    }

    public void impostaAbilitaPulsantiVibroPneumatici(boolean vis) {

        processo.pannelloProcesso.elemBut[18].setEnabled(vis);
        processo.pannelloProcesso.elemBut[19].setEnabled(!vis);

        processo.pannelloProcesso.elemBut[18].setVisible(vis);
        processo.pannelloProcesso.elemBut[19].setVisible(!vis);
    }

    //Dichiarazione Area di Testo per Inserimento Caratteri
    public void inserisciTextFieldAux(int numTextField) {

        txtFieldAuxList = new ArrayList<>();

        for (int i = 0; i < numTextField; i++) {

            //Lettura chiave file xml 
            Element elm = root.getChild(XML_TEXT_FIELD + i);

            JTextField txtFieldAux = new JTextField();

            //Posizionamento e dimenzionameno dei Label 
            txtFieldAux.setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                    ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

            //Settaggio Font 
            txtFieldAux.setFont(FabCloudFont.setDimensione(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

            //Settaggio Trasparenze 
            txtFieldAux.setOpaque(false);

            //Settaggio Bordi 
            txtFieldAux.setBorder(null);

            //Impostazione Nome TextField
            txtFieldAux.setName("BASETXTFIELD");

            txtFieldAux.setVisible(false);

            //Text Field
            add(txtFieldAux);

            txtFieldAuxList.add(txtFieldAux);
        }
    }

    public int controlloCodicePesaturaManuale(String codice) {

        //RICERCA DEL CODICE NEI MOVIMENTI DI MAGAZZINO E RECUPERO ID
        int id = TrovaMovimentoMagazzinoMateriaPrima(
                codice, //codice inserito
                processo.componenti.get(idCompPesaManuale).getId(), //id componente selezionato
                ParametriGlobali.parametri.get(117), //"RAW MATERIAL"
                ParametriGlobali.parametri.get(118), //"DELIVERY NOTE"
                ParametriGlobali.parametri.get(119), //"WAREHOUSE TRANSACTION"
                ParametriGlobali.parametri.get(120));       //"LOADING FOR PURCHASE"

        return id;

    }

    public void impostaVisElementiPannelloMicro(boolean vis) {

        labelImageAux[29].setVisible(vis);
        elemBut[6].setVisible(vis);
        elemBut[7].setVisible(vis);
        elemBut[12].setVisible(vis);

        elemLabelSimple[21].setVisible(vis);
        elemLabelSimple[22].setVisible(vis);
        elemLabelSimple[23].setVisible(vis);
        elemLabelSimple[31].setVisible(vis);
        elemLabelTitle[13].setVisible(vis);
        elemLabelTitle[14].setVisible(vis);
        elemLabelTitle[15].setVisible(vis);
        elemLabelTitle[17].setVisible(vis);

    }

    public void impostaVisPulsanteMultifuzioneMicro(boolean vis) {

        elemBut[12].setVisible(vis);
        elemLabelSimple[23].setVisible(vis);

    }

    public void reimpostaVis() { 

        CloudFabFrame.getFRAME().setContentPane(this);
        this.updateUI();
        this.repaint();
        this.requestFocus();

    }

}
