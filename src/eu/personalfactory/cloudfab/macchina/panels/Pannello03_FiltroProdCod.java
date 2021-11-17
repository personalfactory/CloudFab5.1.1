package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodicePadreById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataProdotti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataProdottiRealizzabili;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaNumCodiciValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Pannello03_FiltroProdCod extends MyStepPanel {

    //VARIABILI
    public String[] nomeProdotto;
    public ArrayList<String> nomeProdottoControllo;
    public ArrayList<String> codice;
    public ArrayList<Integer> idProdotto;
    public int[] chimiche;
    public ArrayList<Integer> idPadre;
    public boolean listaVis;

    //COSTRUTTORE
    public Pannello03_FiltroProdCod() {

        super();

        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(12);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(3);
        impostaDimLabelProg(1);
        impostaDimLabelBut(1);
        impostaColori(6);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[3]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[4]);

        //Inizializza Colori Label Title
        initColorLabelTitle(elemColor[5]);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Area di Testo
        inserisciTextField();

        //Avvio Thread Caricamento Immagine di Caricamento
        new ThreadInserisciLoadingImage(this).start();

        //Configurazione di Base Pannello
        configuraPannello();

        //Inserimento Area di Scambio Pannello Alternativo
        inserisciAreaScambio();

        //Aggiornamento Cursori Label Simple
        aggiornaCursori();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inzializzazione Testo Text Field
        txtField.setText("");

        //Impostazione Visualizzazione Button Freccia
        butFreccia.setVisible(false);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        listaVis = false;
        new ThreadGeneraLista().start();

        //Impostazione Visbilità Pannello
        setPanelVisibile();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Inserimento label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 29, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 30, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Inserimento Label Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 27, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 28, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 31, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento Label Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 39, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Inizializzazione Lista Elementi Selezionabili 
    public void initLista() {

        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setText("");
            elemLabelPlu.setVisible(true);
        }
        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelSimple[i].setText("");
        }

    }

    //Aggiornamento Cursori Label Simple
    public void aggiornaCursori() {

        for (int i = 0; i < elemLabelSimple.length; i++) {
            elemLabelSimple[i].setCursor(Cursor.getDefaultCursor());
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        //Memorizzazione id Prodotto Selezionato
        scelte.idProdotto = TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_PRODOTTI,
                EstraiStringaHtml(scelte.selezione),
                ParametriSingolaMacchina.parametri.get(111));

        int codicePadre = TrovaCodicePadreById(scelte.idProdotto);

        scelte.codiceChimica = TrovaCodiceProdottoPerIdProdotto(scelte.idProdotto);
        scelte.codiceProdotto = TrovaCodiceProdottoPerIdProdotto(scelte.idProdotto);

        if (codicePadre != 0) {

            scelte.codiceChimica = TrovaCodiceProdottoPerIdProdotto(codicePadre);

        }

        scelte.nomeProdotto = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                scelte.idProdotto,
                ParametriSingolaMacchina.parametri.get(111)));

        ((Pannello04_SceltaTipoChimica) pannelliCollegati.get(1)).scelte = scelte;
        ((Pannello04_SceltaTipoChimica) pannelliCollegati.get(1)).initPanel();

    }

    //Lettura Vocaboli Traducibili da Database
    private class ThreadGeneraLista extends Thread {

        @Override
        public void run() {

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(true);
            butFreccia.setVisible(false);
            elemBut[0].setVisible(false);

            //Inizializzazione Label Lista Selezionabile
            initLista();

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Aggiornamento Label Lista Selezionabile
            for (JLabel elemLabelPlu : elemLabelPlus) {
                elemLabelPlu.setText("");
            }

            //Aggiornamento Label Lista non Selezionabile
            for (JLabel elemLabelSimple1 : elemLabelSimple) {
                elemLabelSimple1.setText("");
            }
            //Dichiarazioni Array List
            codice = new ArrayList<>();
            idProdotto = new ArrayList<>();
            idPadre = new ArrayList<>();
            nomeProdottoControllo = new ArrayList<>();

            if (!listaVis) {

                ///////////////////////////////////////////////
                // VISUALIZZA SOLO I PRODOTTI REALIZZABILI  ///
                ///////////////////////////////////////////////
                ArrayList<ProdottoOri> prodottoRealizzabili = TrovaListaOrdinataProdottiRealizzabili();

                nomeProdotto = new String[Math.max(elemLabelPlus.length, prodottoRealizzabili.size())];
                chimiche = new int[Math.max(elemLabelPlus.length, prodottoRealizzabili.size())];

                for (int i = 0; i < prodottoRealizzabili.size(); i++) {

                    nomeProdotto[i] = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                            prodottoRealizzabili.get(i).getIdProdotto(),
                            ParametriSingolaMacchina.parametri.get(111));

                    nomeProdottoControllo.add(EstraiStringaHtml(nomeProdotto[i]));
                    idPadre.add(Integer.parseInt(prodottoRealizzabili.get(i).getColorato()));
                    codice.add(prodottoRealizzabili.get(i).getCodProdotto());
                    idProdotto.add(prodottoRealizzabili.get(i).getIdProdotto());

                    if ((nomeProdottoControllo.get(i).length() - 1 > ParametriSingolaMacchina.parametri.get(143).length())
                            && ((nomeProdottoControllo.get(i).substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                            || (nomeProdottoControllo.get(i).substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                            || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419)))) {

                        chimiche[i] = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));
                    } else {

                        if (Integer.parseInt(prodottoRealizzabili.get(i).getColorato()) == prodottoRealizzabili.get(i).getIdProdotto()) {

                            chimiche[i] = TrovaNumCodiciValidi(prodottoRealizzabili.get(i).getCodProdotto());
                        } else {

                            chimiche[i] = TrovaNumCodiciValidi(TrovaCodiceProdottoPerIdProdotto(Integer.parseInt(prodottoRealizzabili.get(i).getColorato())));

                        }

                    }
                }
            } else {

                ///////////////////////////////////
                // VISUALIZZA TUTTI I PRODOTTI  ///
                ///////////////////////////////////
                //Lista Ordinata di Prodotti
                List<?> resultList = TrovaListaOrdinataProdotti();

                //Dichiarazione Array Nomi Prodotto e Chimiche Disponibili
                nomeProdotto = new String[Math.max(elemLabelPlus.length, resultList.size())];
                chimiche = new int[Math.max(elemLabelPlus.length, resultList.size())];

                //Lettura Nomi Prodotti Da Dizionario
                int h = 0;
                for (Object o : resultList) {
                    ProdottoOri prodOri = (ProdottoOri) o;

                    nomeProdotto[h] = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                            prodOri.getIdProdotto(),
                            ParametriSingolaMacchina.parametri.get(111));

                    nomeProdottoControllo.add(EstraiStringaHtml(nomeProdotto[h]));
                    idPadre.add(Integer.parseInt(prodOri.getColorato()));
                    codice.add(prodOri.getCodProdotto());
                    idProdotto.add(prodOri.getIdProdotto());
                    h++;
                }
                //Analisi Codici Padre
                for (int i = 0; i < codice.size(); i++) {

                    if (idPadre.get(i) != 0) {
                        codice.set(i, TrovaCodiceProdottoPerIdProdotto(idPadre.get(i)));

                        nomeProdottoControllo.set(i, EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                        idPadre.get(i),
                                        ParametriSingolaMacchina.parametri.get(111))));
                    }

                }
                for (int k = 0; k < nomeProdottoControllo.size(); k++) {

                    if ((nomeProdottoControllo.get(k).length() - 1 > ParametriSingolaMacchina.parametri.get(143).length())
                            && ((nomeProdottoControllo.get(k).substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                            || (nomeProdottoControllo.get(k).substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                            || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419)))) {

                        chimiche[k] = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));
                    } else {

                        chimiche[k] = TrovaNumCodiciValidi(codice.get(k));

                    }
                }
            }
            //Aggiornamento Label Lista Selezionabile
            for (int i = 0; i < elemLabelPlus.length; i++) {

                if (i < nomeProdotto.length) {
                    elemLabelPlus[i].setText(nomeProdotto[i]);

                } else {
                    elemLabelPlus[i].setText("");
                }

            }

            //Aggiornamento Label Lista non Selezionabile
            for (int i = 0; i < elemLabelSimple.length; i++) {

                if (i < codice.size()) {
                    elemLabelSimple[i].setText(codice.get(i));

                } else {
                    elemLabelSimple[i].setText("");
                }

            }

            txtField.setText("");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(nomeProdotto);
            definisciListaAttributi(chimiche);
            startThreadControllo();

            //Impostazione Visibilità Loading Image
            elemBut[0].setVisible(true);
            loadingImg.setVisible(false);
            butFreccia.setVisible(true);

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            ///////////////////////////
            // CAMBIA VIS PRODOTTI  ///
            ///////////////////////////
            controllaSelezione.interrompi = true;

            listaVis = !listaVis;

            new ThreadGeneraLista().start();

        }
    }
}
