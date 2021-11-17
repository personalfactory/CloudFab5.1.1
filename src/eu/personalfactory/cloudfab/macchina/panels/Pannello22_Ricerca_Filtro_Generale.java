package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TraovaTuttiCodiciChimicaValidiPerCodiceProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceByIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciLotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiAlternativi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiProdottoById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataClienti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciChimiche;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciConfezioni;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataProdotti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaProdotti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMisceleDisponibiliPerCodChimicaSfusa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaNumCodiciValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaComponentiAlternativi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_MSG_LISTA_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_MSG_LISTA_LOTTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_MSG_LISTA_PRODOTTI; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_FILE_DEFAULT_VERIFICA_DATABASE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level; 

@SuppressWarnings("serial")
public class Pannello22_Ricerca_Filtro_Generale extends MyStepPanel {

    //VARIABILI
    //Lista Selezionabile
    private String[] lista;
    //Tipologia di Pannello
    private int panelType;

    private ArrayList<Integer> idProdotto;
    private ArrayList<String> codice;
    private ArrayList<String> listaProdottoDisponibili;
    private ArrayList<Integer> numeroChimicheProdottiDisponibili;

    //COSTRUTTORE
    public Pannello22_Ricerca_Filtro_Generale() {

        super();

        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(0);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(1);
        impostaColori(5);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[4]);

        //Avvio Thread Caricamento Loading Image
        new ThreadInserisciLoadingImage(this).start();

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Text Field
        inserisciTextField();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();
    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Immagine
        loadingImg.setVisible(true);

        //Inzializzazione Variabili
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Impostazione Visibiltà Tastiera
        tastiera.impostaVisibilitaTastiera(true);

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(false);

        //Inizializzazione Elementi Lista Selezionabile
        initLista();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Imposta Visibilità Button Download Info
        elemBut[0].setVisible(false);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Aggiornamento Lista Selezionabile
        new LeggiLista().start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 539, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Aggiornamento Lista Selezionabile da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            int[] chimiche = null;

            switch (panelType) {

                case 0: {

                    //////////////////////////////////
                    // LISTA ORDINATA DI PRODOTTI  ///
                    //////////////////////////////////
                    //Aggiornamento Testi Label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 538, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento Testi Label Tipo Help
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 540, ParametriSingolaMacchina.parametri.get(111)));

                    //Ricerca Lista Ordinata di Prodotti
                    String[] prodotti = TrovaListaProdotti(ParametriSingolaMacchina.parametri.get(111));

                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(305))) {
                        for (int p = 0; p < prodotti.length; p++) {

                            int idProdotto = TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_PRODOTTI,
                                    EstraiStringaHtml(prodotti[p]),
                                    ParametriSingolaMacchina.parametri.get(111));

//                            String msg = HTML_STRINGA_INIZIO
//                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 546, ParametriSingolaMacchina.parametri.get(111)))
//                                    + HTML_BREAK_LINE;

                            //Ricerca Lista Codici Chimiche Valide
                            List<String> chimicheValide = TraovaTuttiCodiciChimicaValidiPerCodiceProdotto(TrovaCodiceProdottoPerIdProdotto(idProdotto));

                            //Ricerca Lista Codici Chimiche Valide
////                            List<String> chimicheUsate = Benefit.findAllCodiciChimicaUsatiProdotto(Benefit.findCodProdottoById(idProdotto));
//
//                            int numCodChimicaSfusiValidi = 0;
                            int numCodChimicaValidi = 0;
                            int numProcessiChimicaSfusa = 0;

                            for (int i = 0; i < chimicheValide.size(); i++) {

                                if (chimicheValide.get(i).substring(0, 1).equals(
                                        ParametriGlobali.parametri.get(19))) {
//                                    numCodChimicaSfusiValidi++;
                                    numProcessiChimicaSfusa += TrovaMisceleDisponibiliPerCodChimicaSfusa(chimicheValide.get(i));

                                } else if (chimicheValide.get(i).substring(0, 1).equals(
                                        ParametriGlobali.parametri.get(15))) {
                                    numCodChimicaValidi++;
                                }
                            }

                            prodotti[p] = HTML_STRINGA_INIZIO
                                    + EstraiStringaHtml(prodotti[p])
                                    + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 768, ParametriSingolaMacchina.parametri.get(111)))
                                    + " "
                                    + numCodChimicaValidi
                                    + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 769, ParametriSingolaMacchina.parametri.get(111)))
                                    + " "
                                    + numProcessiChimicaSfusa
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 770, ParametriSingolaMacchina.parametri.get(111)))
                                    + HTML_STRINGA_FINE;

                        }
                    }
                    //Dichiarazione Array Lista
                    lista = new String[Math.max(elemLabelPlus.length, prodotti.length)];

                    //Copia Array Prodotti
                    System.arraycopy(prodotti, 0, lista, 0, prodotti.length);

                    //Inizializzazione Elementi Lista Selezionabile Vuoti
                    for (int i = prodotti.length; i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                    }
                    break;

                }

                case 1: {

                    ///////////////////////////////////////////////
                    // LISTA ORDINATA DI PRODOTTI  CON CHIMICHE ///
                    ///////////////////////////////////////////////
                    //Aggiornamento Testi Label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 541, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento Testi Label Tipo Aiuto
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 540, ParametriSingolaMacchina.parametri.get(111)));

                    //Inizializzazione Array List Codice
                    codice = new ArrayList<>();
                    listaProdottoDisponibili = new ArrayList<>();
                    numeroChimicheProdottiDisponibili = new ArrayList<>();
                    idProdotto = new ArrayList<>();

                    //Ricerca Lista Ordinata Prodotti
                    List<?> resultList = TrovaListaOrdinataProdotti();

                    for (Object o : resultList) {
                        ProdottoOri prodOri = (ProdottoOri) o;

                        String codiceProdotto = prodOri.getCodProdotto();
                        if (Integer.parseInt(prodOri.getColorato()) != prodOri.getIdProdotto()) {

                            codiceProdotto = TrovaCodiceByIdProdotto(Integer.parseInt(prodOri.getColorato()));
                        }

                        int numChimica = TrovaNumCodiciValidi(codiceProdotto);
                        if (numChimica > 0) {
                            listaProdottoDisponibili.add(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, prodOri.getIdProdotto(), ParametriSingolaMacchina.parametri.get(111)));
                            codice.add(prodOri.getCodProdotto());
                            idProdotto.add(prodOri.getIdProdotto());
                            numeroChimicheProdottiDisponibili.add(numChimica);

                        }
                    }

                    //Dichiarazione e Impostazione Lista Selezionabile
                    lista = new String[Math.max(elemLabelPlus.length, listaProdottoDisponibili.size())];
                    //Dichiarazione Array Chimiche
                    chimiche = new int[Math.max(elemLabelPlus.length, numeroChimicheProdottiDisponibili.size())];

                    for (int i = 0; i < listaProdottoDisponibili.size(); i++) {
                        lista[i] = listaProdottoDisponibili.get(i);
                        chimiche[i] = numeroChimicheProdottiDisponibili.get(i);

                    }
                    //Inizializzazione Elementi Lista Selezionabile Vuoti
                    for (int i = listaProdottoDisponibili.size(); i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                        chimiche[i] = 0;
                    }

                    if (DettagliSessione.isSuperUser()) {

                        //Imposta Visibilità Button Download Info
                        elemBut[0].setVisible(true);

                    }

                    break;

                }

                case 2: {

                    /////////////////////////////////
                    // LISTA ORDINATA DI CLIENTI  ///
                    /////////////////////////////////
                    //Aggiornamento Testi Label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 542, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento Testi Label Tipo Help
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 543, ParametriSingolaMacchina.parametri.get(111)));

                    //Ricerca Lita Ordinata di Clienti
                    String[] result = TrovaListaOrdinataClienti(ParametriGlobali.parametri.get(40));

                    //Dichiarazione e Impostazione Lista Selezionabile
                    lista = new String[Math.max(elemLabelPlus.length, result.length)];
                    System.arraycopy(result, 0, lista, 0, result.length);
                    for (int i = 0; i < result.length; i++) {
                        lista[i] = lista[i].toUpperCase();
                    }

                    //Inizializzazione Elementi Lista Selezionabile Vuoti
                    for (int i = result.length; i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                    }

                    break;

                }

                case 3: {

                    ////////////////////
                    // LISTA CODICI  ///
                    ////////////////////
                    //Aggiornamento Testi Label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 544, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento Testi Label Tipo Help
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 545, ParametriSingolaMacchina.parametri.get(111)));

                    //Ricerca Lista Ordinata Codici Confezioni
                    String[] result_confezioni = TrovaListaOrdinataCodiciConfezioni(ParametriGlobali.parametri.get(82));

                    //Ricerca Lista Ordinata Codici Chimiche
                    String[] result_chimiche = TrovaListaOrdinataCodiciChimiche();

                    //Dichiarazione e Impostazione Lista Selezionabile
                    lista = new String[Math.max(elemLabelPlus.length, (result_confezioni.length + result_chimiche.length))];

                    for (int i = 0; i < result_confezioni.length; i++) {

                        lista[i] = result_confezioni[i].toUpperCase();

                    }

                    for (int i = 0; i < result_chimiche.length; i++) {

                        lista[result_confezioni.length + i] = result_chimiche[i].toUpperCase();

                    }

                    //Inizializzazione Elementi Lista Selezionabile Vuoti
                    for (int i = (result_confezioni.length + result_chimiche.length); i < lista.length; i++) {

                        lista[i] = "";

                    }

                    //Inizializzazione Elementi Lista Selezionabile Vuoti
                    for (int i = (result_confezioni.length + result_chimiche.length); i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                    }

                    break;

                }

            }

            //Aggiornamento Testi Lista Selezionabile
            elemLabelPlus[0].setText(lista[0]);
            elemLabelPlus[1].setText(lista[1]);
            elemLabelPlus[2].setText(lista[2]);
            elemLabelPlus[3].setText(lista[3]);
            elemLabelPlus[4].setText(lista[4]);
            elemLabelPlus[5].setText(lista[5]);
            elemLabelPlus[6].setText(lista[6]);
            elemLabelPlus[7].setText(lista[7]);

            //Avvio Gestore Lista Selezionabile
            txtField.setText("");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(lista);
            if (chimiche != null) {
                definisciListaAttributi(chimiche);
            }
            startThreadControllo();

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(false);
        }
    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Impostazione Tipologia Pannello
    public void setPanelType(int panelType) {
        this.panelType = panelType;
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            ///////////////////
            // VERIFICA DB  ///
            ///////////////////
            ArrayList<Integer> idComponentiNecessari = new ArrayList<>();

            try {
                File file = new File(PATH_FILE_DEFAULT_VERIFICA_DATABASE);
                FileWriter fw = new FileWriter(file);

                fw.write(DEFAULT_MSG_LISTA_PRODOTTI + "\n\n");

                for (int i = 0; i < listaProdottoDisponibili.size(); i++) {

                    //NOME COMPONENTE
                    fw.write("\n"
                            + codice.get(i)
                            + " - "
                            + EstraiStringaHtml(listaProdottoDisponibili.get(i))
                            + "\t("
                            + numeroChimicheProdottiDisponibili.get(i).toString()
                            + ")\n");
                    List<?> componentiList = TrovaComponentiProdottoById(idProdotto.get(i));

                    for (Object o : componentiList) {
                        ComponentePesaturaOri compOri = (ComponentePesaturaOri) o;

                        String str = compOri.getInfo1();
                        String temp = "";
                        ArrayList<Integer> sostituzioni = new ArrayList<>();

                        for (int h = 0; h < str.length(); h++) {

                            if (str.charAt(h) == ParametriGlobali.parametri.get(156).charAt(0)) {

                                if (VerificaComponentiAlternativi(Integer.parseInt(temp))) {
                                    //Verifica Presenza Componente
                                    sostituzioni.add(Integer.parseInt(temp));
                                }
                                temp = "";

                            } else {
                                temp += str.charAt(h);

                            }
                        }
                        if (!temp.equals("")) {
                            if (VerificaComponentiAlternativi(Integer.parseInt(temp))) {
                                //Verifica Presenza Componente
                                sostituzioni.add(Integer.parseInt(temp));
                            }
                        }

                        //FORMULA COMPONENTE
                        fw.write("\t\t"
                                + compOri.getIdComp()
                                + " - "
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compOri.getIdComp(), ParametriSingolaMacchina.parametri.get(111)))
                                + "\t(" + compOri.getQuantita() + ")\n");
                        //SOTITUZIONI
                        for (int y = 0; y < sostituzioni.size(); y++) {

                            fw.write("\t\t\t\t\t\t"
                                    + sostituzioni.get(y)
                                    + " - "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, sostituzioni.get(y), ParametriSingolaMacchina.parametri.get(111)))
                                    + "\n");

                        }
                        boolean trovato = false;
                        for (int j = 0; j < idComponentiNecessari.size(); j++) {

                            if (Objects.equals(idComponentiNecessari.get(j), compOri.getIdComp())) {
                                trovato = true;
                                break;
                            }
                        }
                        if (!trovato) {

                            idComponentiNecessari.add(compOri.getIdComp());

                        }
                    }

                    // COMPONENTI ALTERNATIVI
                    ArrayList<ArrayList<Integer>> compAlternative = TrovaComponentiAlternativi(idProdotto.get(i));

                    for (int k = 0; k < compAlternative.size(); k++) {

                        for (int l = 0; l < compAlternative.get(k).size(); l++) {

                            boolean trovatoAlternative = false;
                            for (int m = 0; m < idComponentiNecessari.size(); m++) {

                                if (Objects.equals(idComponentiNecessari.get(m), compAlternative.get(k).get(l))) {

                                    trovatoAlternative = true;
                                }
                            }

                            if (!trovatoAlternative) {

                                idComponentiNecessari.add(compAlternative.get(k).get(l));

                            }
                        }
                    }

                    fw.write("\n\n");

                }
                fw.write("\n\n" + DEFAULT_MSG_LISTA_COMPONENTI + "\n\n");
                for (int i = 0; i < idComponentiNecessari.size(); i++) {
                    fw.write("\t"
                            + idComponentiNecessari.get(i)
                            + " - "
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, idComponentiNecessari.get(i), ParametriSingolaMacchina.parametri.get(111)))
                            + "\n");

                }

                //LISTA CODICI LOTTO
                fw.write("\n\n" + DEFAULT_MSG_LISTA_LOTTI + "\n\n");

                ArrayList<String> codiciLotto = TrovaCodiciLotto();

                for (int i = 0; i < codiciLotto.size(); i++) {
                    fw.write("\t" + codiciLotto.get(i) + "\n");

                }

                fw.flush();
                fw.close();
            } catch (IOException e) {
                SessionLogger.logger.log(Level.SEVERE, "CREAZIONE FILE FALLITA - e ={0}", e);

            }

        }
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(String selezione) {

        this.setVisible(false);

        switch (panelType) {

            case 0: {

                String str = " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 768, ParametriSingolaMacchina.parametri.get(111))) + " ";
                String sel = EstraiStringaHtml(selezione);

                if (sel.contains(str)) {

                    selezione = HTML_STRINGA_INIZIO
                            + sel.substring(0, sel.indexOf(str))
                            + HTML_STRINGA_FINE;

                }

                ((Pannello26_Ricerca_Dettagli) pannelliCollegati.get(1)).initPanel(EstraiStringaHtml(selezione), 0);

                break;
            }
            case 1: {

                ((Pannello23_Ricerca_Codici_Chimica_Disponibili) pannelliCollegati.get(2)).initPanel(EstraiStringaHtml(selezione));

                break;
            }
            case 2: {

                ((Pannello24_Ricerca_Prodotti_Per_Cliente) pannelliCollegati.get(3)).initPanel(
                        selezione);

                break;
            }

            case 3: {

                ((Pannello26_Ricerca_Dettagli) pannelliCollegati.get(1)).initPanel(
                        selezione, 3);

                break;
            }

        }
    }
}
