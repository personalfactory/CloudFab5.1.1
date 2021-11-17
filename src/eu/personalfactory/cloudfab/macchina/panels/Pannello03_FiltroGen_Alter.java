package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodicePadreById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdPadrePerCodiceProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdProdottoByCodice;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdProdottoPerCodProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciProdottiAbilitati;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciProdottiPerData;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciProdottoPerCliente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaNomeProdottoCodiceByIdFamiglia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaNumCodiciValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_FAMIGLIE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SuppressWarnings("serial")
public class Pannello03_FiltroGen_Alter extends MyStepPanel {

    //VARIABILI
    //Elementi lista Filtrata
    private String[] codici, codiciControllo, prodotti, nomeProdottoControllo;
    //Chimiche disponibili
    private int[] chimiche;
    //Tipologia di Pannello Clienti (0) - Date(1) - Famiglia(2)
    public int panelType;
    //Lista Codici Validi
    private List<?> codiciAbilitati;

    //COSTRUTTORE
    public Pannello03_FiltroGen_Alter() {

        super();

        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(1);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(3);
        impostaDimLabelProg(1);
        impostaDimLabelBut(0);
        impostaColori(5);

        //Inizializzazione Colore Label Help
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colore Label Title
        initColorLabelTitle(elemColor[4]);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Loading Image
        new ThreadInserisciLoadingImage(this).start();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Area di Testo
        inserisciTextField();

        //Configurazione di Base Pannello
        configuraPannello();

        //Inizializzazione Cursore
        elemLabelSimple[0].setCursor(Cursor.getDefaultCursor());

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Text Field
        txtField.setText("");

        //Impostazione visibilità Button Freccia
        butFreccia.setVisible(false);

        //Inizializzazione lista Selezionabile
        initLista();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Impostazione Visibilità Loading Image
        loadingImg.setVisible(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Elementi della Lista
        new LeggiLista().start();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 37, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Progresso
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 39, ParametriSingolaMacchina.parametri.get(111)));
            }

        }
    }

    //Lettura Elementi della Lista Selezionabile
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Aggiornamento Label Tipo Title
            elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 36, ParametriSingolaMacchina.parametri.get(111)));
            elemLabelTitle[2].setText(HTML_STRINGA_INIZIO + ParametriGlobali.parametri.get(70) + " "
                    + scelte.selezione + HTML_STRINGA_INIZIO);

            //Ricerca codici prodotto Abiliati
            codiciAbilitati = TrovaListaOrdinataCodiciProdottiAbilitati();

            switch (panelType) {

                case 1: {
                    ////////////////////////////////////////////
                    // LISTA PRODOTTI IN BASE ALLA FAMIGLIA  ///
                    ////////////////////////////////////////////

                    //Aggiornamento Testo Label Tipo Title
                    elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 35, ParametriSingolaMacchina.parametri.get(111)));

                    //Ricerca idProd e codProd in Funzione dell'idFamiglia Selezionato
                    String result[][] = TrovaNomeProdottoCodiceByIdFamiglia(TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_FAMIGLIE,
                                    scelte.selezione,
                                    ParametriSingolaMacchina.parametri.get(111)));

                    //Ricerca del Nome Prodotto dal Dizionario in Funzione dell'idProd
                    String[] prodotti_diz = new String[result[0].length];
                    for (int i = 0; i < result[0].length; i++) {
                        prodotti_diz[i]
                                = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                                Integer.parseInt(result[0][i]),
                                                ParametriSingolaMacchina.parametri.get(111)));
                    }

                    //Array List Prodotti
                    prodotti = new String[Math.max(elemLabelPlus.length, prodotti_diz.length)];
                    System.arraycopy(prodotti_diz, 0, prodotti, 0, prodotti_diz.length);

                    //Array List Codici
                    codici = new String[Math.max(elemLabelPlus.length, result[1].length)];
                    System.arraycopy(result[1], 0, codici, 0, result[1].length);

                    //Codici Controllo
                    codiciControllo = new String[Math.max(elemLabelPlus.length, result[1].length)];
                    System.arraycopy(result[1], 0, codiciControllo, 0, result[1].length);
                    nomeProdottoControllo = new String[Math.max(elemLabelPlus.length, prodotti_diz.length)];
                    System.arraycopy(prodotti_diz, 0, nomeProdottoControllo, 0, prodotti_diz.length);

                    for (int i = result[1].length; i < elemLabelPlus.length; i++) {
                        codici[i] = "";
                    }
                    for (int i = result[0].length; i < elemLabelPlus.length; i++) {
                        prodotti[i] = "";
                    }

                    //Formattazione Visualizzazione Prodotti
                    for (int j = 0; j < result[0].length; j++) {
                        prodotti[j] = HTML_STRINGA_INIZIO + codici[j] + " - " + prodotti[j] + HTML_STRINGA_FINE;
                    }

                    //Analisi Codici Padre
                    for (int i = 0; i < codiciControllo.length; i++) {

                        int idPadre = TrovaIdPadrePerCodiceProdotto(codiciControllo[i]);

                        if (idPadre != 0) {
                            codiciControllo[i] = TrovaCodiceProdottoPerIdProdotto(idPadre);
                            //codiciControllo[i] = Benefit.findNomeProdottoPadreById(idPadre);

                            nomeProdottoControllo[i] = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                            idPadre,
                                            ParametriSingolaMacchina.parametri.get(111)));

                        }

                    }

                    //Lettura Chimiche
                    chimiche = new int[codici.length];

                    for (int i = 0; i < result[1].length; i++) {
 
                        if ((nomeProdottoControllo[i].length() > 0)
                                && ((nomeProdottoControllo[i].substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                                || (nomeProdottoControllo[i].substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                                || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419)))) {

                            chimiche[i] = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));
                        } else {

                            chimiche[i] = TrovaNumCodiciValidi(codiciControllo[i]);
                        }
                    }
                    break;
                }

                case 2: {

                    ////////////////////////////////////////////
                    // LISTA DI PRODOTTI IN BASE AL CLIENTE  ///
                    ////////////////////////////////////////////
                    //Aggiornamento Testo Label Tipo Title
                    elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 40, ParametriSingolaMacchina.parametri.get(111)));

                    String result[] = TrovaListaOrdinataCodiciProdottoPerCliente(scelte.selezione);
                    codici = new String[Math.max(elemLabelPlus.length, result.length)];
                    System.arraycopy(result, 0, codici, 0, result.length);

                    codiciControllo = new String[Math.max(elemLabelPlus.length, result.length)];
                    System.arraycopy(result, 0, codiciControllo, 0, result.length);

                    for (int i = result.length; i < elemLabelPlus.length; i++) {
                        codici[i] = "";
                    }

                    //Ricerca idProdotto
                    int idProdotto[] = new int[codici.length];
                    for (int i = 0; i < codici.length; i++) {
                        idProdotto[i] = TrovaIdProdottoByCodice(codici[i]);

                    }

                    //Dichiarazione Array List Prodotti
                    prodotti = new String[codici.length];

                    //Ricerca Nome Prodotto dal Dizionario
                    for (int i = 0; i < idProdotto.length; i++) {
                        prodotti[i]
                                = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                                idProdotto[i],
                                                ParametriSingolaMacchina.parametri.get(111)));
                    }

                    nomeProdottoControllo = new String[Math.max(elemLabelPlus.length, prodotti.length)];
                    System.arraycopy(prodotti, 0, nomeProdottoControllo, 0, prodotti.length);

                    //Dichiarazione Array Chimiche
                    chimiche = new int[codici.length];

                    //Analisi Codici Padre
                    for (int i = 0; i < codiciControllo.length; i++) {

                        int idPadre = TrovaIdPadrePerCodiceProdotto(codiciControllo[i]);

                        if (idPadre != 0) {
                            //codiciControllo[i] = Benefit.findCodProdottoById(idPadre);

                            nomeProdottoControllo[i] = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                            idPadre,
                                            ParametriSingolaMacchina.parametri.get(111)));
                        }

                    }

                    //Lettura Chimiche e Individuazione Nome Prodotto
                    for (int i = 0; i < result.length; i++) {

                        if (verificaProdottoAbilitato(i)) {
                            if ((nomeProdottoControllo[i].length() > 0)
                                    && ((nomeProdottoControllo[i].substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                                    || (nomeProdottoControllo[i].substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                                    || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419)))) {

                                chimiche[i] = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));
                            } else {

                                chimiche[i] = TrovaNumCodiciValidi(codiciControllo[i]);

                            }
                        }

                    }

                    //Formattazione Visualizzazione Prodotti
                    for (int j = 0; j < result.length; j++) {
                        prodotti[j] = HTML_STRINGA_INIZIO + codici[j] + ParametriGlobali.parametri.get(48) + prodotti[j]
                                + HTML_STRINGA_FINE;
                    }

                    break;
                }

                case 3: {

                    ////////////////////////////////////////////////////////
                    // LISTA DI PRODOTTI IN BASE ALLA DATA DI PRODUZIONE ///
                    ////////////////////////////////////////////////////////
                    //Aggiornamento Testo Label Tipo Title
                    elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,
                            43,
                            ParametriSingolaMacchina.parametri.get(111)));

                    //Ricerca Lista Ordinata Prodotti per Data
                    String[] result = null;

                    try {

                        result = TrovaListaOrdinataCodiciProdottiPerData(new SimpleDateFormat(ParametriGlobali.parametri.get(43)).parse(scelte.selezione));

                    } catch (ParseException e) {
                    }

                    int dimArray = 0;

                    if (result != null) {

                        dimArray = result.length;
                    }

                    //Dichiarazione Array codici e Codici di Controllo
                    codici = new String[Math.max(elemLabelPlus.length, dimArray)];
                    System.arraycopy(result, 0, codici, 0, dimArray);

                    codiciControllo = new String[Math.max(elemLabelPlus.length, dimArray)];
                    System.arraycopy(result, 0, codiciControllo, 0, dimArray);

                    for (int i = dimArray; i < elemLabelPlus.length; i++) {
                        codici[i] = "";
                    }

                    //Ricerca idProdotto
                    int idProdotto[] = new int[codici.length];
                    for (int i = 0; i < codici.length; i++) {
                        idProdotto[i] = TrovaIdProdottoByCodice(codici[i]);

                    }

                    //Dichiarazione Array Prodotti
                    prodotti = new String[codici.length];

                    //Ricerca Nome Prodotto dal Dizionario
                    for (int i = 0; i < idProdotto.length; i++) {
                        prodotti[i]
                                = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                                idProdotto[i],
                                                ParametriSingolaMacchina.parametri.get(111)));
                    }

                    nomeProdottoControllo = new String[Math.max(elemLabelPlus.length, prodotti.length)];
                    System.arraycopy(prodotti, 0, nomeProdottoControllo, 0, prodotti.length);

                    chimiche = new int[codici.length];

                    //Analisi Codici Padre
                    for (int i = 0; i < codiciControllo.length; i++) {

                        int idPadre = TrovaIdPadrePerCodiceProdotto(codiciControllo[i]);

                        if (idPadre != 0) {

                            codiciControllo[i] = TrovaCodiceProdottoPerIdProdotto(idPadre);

                            nomeProdottoControllo[i] = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                            idPadre,
                                            ParametriSingolaMacchina.parametri.get(111)));
                        }

                    }

                    //Lettura Chimiche e Individuazione Nome Prodotto
                    for (int i = 0; i < dimArray; i++) {
                        if (verificaProdottoAbilitato(i)) {
                            if ((nomeProdottoControllo[i].length() > 0)
                                    && ((nomeProdottoControllo[i].substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                                    || (nomeProdottoControllo[i].substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                                    || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419)))) {

                                chimiche[i] = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));
                            } else {
                                chimiche[i] = TrovaNumCodiciValidi(codiciControllo[i]);
                            }
                        }
                    }

                    //Formattazione Visualizzazione Prodotti
                    for (int j = 0; j < dimArray; j++) {
                        prodotti[j] = HTML_STRINGA_INIZIO
                                + codici[j] + ParametriGlobali.parametri.get(48) + prodotti[j]
                                + HTML_STRINGA_FINE;
                    }

                    break;
                }

            }

            //Aggiornamento Label Elementi Lista Selezionabile
            elemLabelPlus[0].setText(prodotti[0]);
            elemLabelPlus[1].setText(prodotti[1]);
            elemLabelPlus[2].setText(prodotti[2]);
            elemLabelPlus[3].setText(prodotti[3]);
            elemLabelPlus[4].setText(prodotti[4]);
            elemLabelPlus[5].setText(prodotti[5]);
            elemLabelPlus[6].setText(prodotti[6]);
            elemLabelPlus[7].setText(prodotti[7]);

            txtField.setText("");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(prodotti);
            definisciListaAttributi(chimiche);
            startThreadControllo();

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(false);

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Label Lista Selezionabile
    public void initLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        //Memorizzazione Id Prodotto Selezionato
        scelte.idProdotto = TrovaIdProdottoPerCodProdotto(
                scelte.selezione.substring(0,
                        Integer.parseInt(ParametriGlobali.parametri.get(47))));

///       //Memorizzazione Codice Prodotto Selezionato o Codice Padre
////       int codicePadre = Benefit.findCodicePadreById(scelte.idProdotto);
        
///////////////////////////
////        if (codicePadre != 0) {
////
////            scelte.codiceProdotto = Benefit.findCodProdottoById(codicePadre);
////
////        } else {
////
////            scelte.codiceProdotto = Benefit.findCodProdottoById(scelte.idProdotto);
////
////        }
        

        int codicePadre = TrovaCodicePadreById(scelte.idProdotto);
        
        scelte.codiceChimica= TrovaCodiceProdottoPerIdProdotto(scelte.idProdotto);
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

    //Verifica Codice Prodotti Abilitati
    public boolean verificaProdottoAbilitato(int i) {

        boolean abilitato = false;

        //Controllo se il prodotto è abilitato
        for (int j = 0; j < codiciAbilitati.size(); j++) {
            if (codici[i].equals(codiciAbilitati.get(j))) {
                abilitato = true;
                break;
            }
        }
        if (!abilitato) {
            chimiche[i] = 0;
        }

        return abilitato;

    }
}
