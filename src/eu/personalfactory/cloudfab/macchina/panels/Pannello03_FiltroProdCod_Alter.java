package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodicePadreById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdProdottoPerCodProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodici;
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
public class Pannello03_FiltroProdCod_Alter extends MyStepPanel {

    //Variabili
    public String[] nomeProdotto;
    public String[] codice;
    //public String[] nomeProdottoControllo
    public ArrayList<String> nomeProdottoControllo;

    public ArrayList<Integer> idProdotto;
    public ArrayList<Integer> idPadre;

    public int[] chimiche;

    //COSTRUTTORE
    public Pannello03_FiltroProdCod_Alter() {

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
        impostaDimLabelBut(0);
        impostaColori(6);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[3]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[4]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[5]);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Avvio Thread Inserimento Loading Image
        new ThreadInserisciLoadingImage(this).start();

        //Inserimento Area di Testo
        inserisciTextField();

        //Inserimento Area di Scambio Pannello Alternativo
        inserisciAreaScambio();

        //Configurazione di Base Pannello
        configuraPannello();

        //Aggiorna Cursori Label Simple
        aggiornaCursori();

        //Avvio Threda Inserimento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Testo Text Field
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Inizializzazione Lista Selezionabile
        initLista();

        //Impostazione Visibilità Loading Image
        loadingImg.setVisible(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Elementi Lista Selezionabile da Database
        new LeggiLista().start();

    }

    //Inizializzazione Label Lista Selezionabile
    public void initLista() {

        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setText("");
            elemLabelPlu.setVisible(true);
        }
        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelSimple[i].setText("");
        }

    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Agionrmanto label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 30, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 29, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Inserimento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 28, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 27, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 31, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamneto Label Tipo Progresso **/
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 39, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Lettura Elementi Lista Selezionabile
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Dichiarazione array id Prodotto e id Prodotto Padre
            idProdotto = new ArrayList<>();
            idPadre = new ArrayList<>();
            nomeProdottoControllo = new ArrayList<>();

            //Ricerca Lista Ordinata Codici
            List<?> resultList = TrovaListaOrdinataCodici();

            //Dichiarazioni Array
            nomeProdotto = new String[Math.max(elemLabelPlus.length, resultList.size())];
            codice = new String[Math.max(elemLabelPlus.length, resultList.size())];
            chimiche = new int[Math.max(elemLabelPlus.length, resultList.size())];
            //nomeProdottoControllo = new String[Math.max(elemLabelPlus.length, resultList.size())];

            //Lettura Nomi Prodotto da Dizionario
            int h = 0;
            for (Object o : resultList) {
                ProdottoOri prodOri = (ProdottoOri) o;
                nomeProdotto[h] = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                        prodOri.getIdProdotto(),
                        ParametriSingolaMacchina.parametri.get(111));
                codice[h] = prodOri.getCodProdotto();

                nomeProdottoControllo.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                        prodOri.getIdProdotto(),
                        ParametriSingolaMacchina.parametri.get(111))));

                idProdotto.add(prodOri.getIdProdotto());
                idPadre.add(Integer.parseInt(prodOri.getColorato()));
                h++;
            }

            //Analisi Codici Padre
            for (int i = 0; i < codice.length; i++) {

                if (i < idPadre.size()) {
                    if (idPadre.get(i) != 0) {
                        codice[i] = TrovaCodiceProdottoPerIdProdotto(idPadre.get(i));

                        nomeProdottoControllo.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                idPadre.get(i),
                                ParametriSingolaMacchina.parametri.get(111))));
                    }
                } else {
                    codice[i] = "";
                }

            }

            for (int k = 0; k < codice.length; k++) {

                if (nomeProdottoControllo.size()>0
                        && nomeProdottoControllo.get(k).length() - 1 > ParametriSingolaMacchina.parametri.get(143).length()
                        && ((nomeProdottoControllo.get(k).substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                        || (nomeProdottoControllo.get(k).substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                        || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419)))) {

                    chimiche[k] = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));
                } else {

                    chimiche[k] = TrovaNumCodiciValidi(codice[k]);

                }
            }

            //Aggiornamento Elementi Lista Selezionabile
            for (int i = 0;
                    i < elemLabelPlus.length;
                    i++) {

                if (i < codice.length) {
                    elemLabelPlus[i].setText(codice[i]);

                } else {
                    elemLabelPlus[i].setText("");
                }

            }

            //Aggiornamento Elementi Lista non Selezionabile
            for (int i = 0;
                    i < elemLabelSimple.length;
                    i++) {

                if (i < nomeProdotto.length) {
                    elemLabelSimple[i].setText(nomeProdotto[i]);

                } else {
                    elemLabelSimple[i].setText("");
                }

            }

            txtField.setText(
                    "");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus,
                    0);
            definisciLista(codice);

            definisciListaAttributi(chimiche);

            startThreadControllo();

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(
                    false);

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(
                    true);

        }
    }

    //Aggiornamento Cursori Elementi non Selezionabili
    public void aggiornaCursori() {

        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setCursor(Cursor.getDefaultCursor());
        }
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        setVisible(false);

        //Memorizzazione id Prodotto Selezionato
        scelte.idProdotto = TrovaIdProdottoPerCodProdotto(scelte.selezione);

////////        //Memorizzazione Codice Prodotto Selezionato o Codice Padre
////////        int codicePadre = Benefit.findCodicePadreById(scelte.idProdotto);
////////
////////        if (codicePadre != 0) {
////////
////////            scelte.codiceProdotto = Benefit.findCodProdottoById(codicePadre);
////////
////////        } else {
////////
////////            scelte.codiceProdotto = Benefit.findCodProdottoById(scelte.idProdotto);
////////
////////        }
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
}
