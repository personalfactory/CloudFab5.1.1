package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ChimicaOri;
import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import eu.personalfactory.cloudfab.macchina.entity.ProcessoOri;
import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TraovaTuttiCodiciChimicaValidiPerCodiceProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciChimicaValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiProdottoById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDettagliCodiceChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDettagliCodiceChimicaUsato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDettagliCodiceConfezione;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMaxMisceleTabellaProcessoPerCodChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMisceleDisponibiliPerCodChimicaSfusa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiCodiciChimicaUsatiPerCodiceProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreProdottoByIdProd;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_TEMPO_MISCELAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_VELOCITA_MISCELAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.List;

@SuppressWarnings("serial")
public class Pannello26_Ricerca_Dettagli extends MyStepPanel {

    //COSTRUTTORE
    public Pannello26_Ricerca_Dettagli() {

        super();

        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(1);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(0);
        impostaColori(2);

        //Inizializzazione Colori Label Tipo Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Tipo Title
        initColorLabelTitle(elemColor[1]);

        //Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel(String selezione, int panelType) {

        //Inizializzazione Testo Text Field
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Aggiornamento Dati Visualizzati
        new aggiornaDati(selezione, panelType).start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

    }

    //Aggiornamento Dati Visualizzati in Funzione della Selezione
    private class aggiornaDati extends Thread {

        private final String selezione;
        private final int panelType;

        public aggiornaDati(String selezione, int panelType) {
            this.selezione = selezione;
            this.panelType = panelType;
        }

        @Override
        public void run() {

            switch (panelType) {

                case 0: {

                    /////////////////////////////////////////////
                    // DATI RELATIVI AL PRODOTTO SELEZIONATO  ///
                    /////////////////////////////////////////////
                    int idProdotto = TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_PRODOTTI,
                            selezione,
                            ParametriSingolaMacchina.parametri.get(111));

                    String msg = HTML_STRINGA_INIZIO
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 546, ParametriSingolaMacchina.parametri.get(111)))
                            + HTML_BREAK_LINE;

                    //Ricerca Lista Codici Chimiche Valide
                    List<String> chimicheValide = TraovaTuttiCodiciChimicaValidiPerCodiceProdotto(TrovaCodiceProdottoPerIdProdotto(idProdotto));

                    //Ricerca Lista Codici Chimiche Valide
                    List<String> chimicheUsate = TrovaTuttiCodiciChimicaUsatiPerCodiceProdotto(ParametriGlobali.parametri.get(15) + TrovaCodiceProdottoPerIdProdotto(idProdotto));

                    int numCodChimicaSfusiValidi = 0;
                    int numCodChimicaValidi = 0;
                    int numProcessiChimicaSfusa = 0;

                    for (int i = 0; i < chimicheValide.size(); i++) {

                        if (chimicheValide.get(i).substring(0, 1).equals(
                                ParametriGlobali.parametri.get(19))) {
                            numCodChimicaSfusiValidi++;
                            numProcessiChimicaSfusa += TrovaMisceleDisponibiliPerCodChimicaSfusa(chimicheValide.get(i));

                        } else if (chimicheValide.get(i).substring(0, 1).equals(
                                ParametriGlobali.parametri.get(15))) {
                            numCodChimicaValidi++;
                        }
                    }

                    //Ricerca Prodotto in Funzione dell'Id Selezionato
                    List<?> resultList = TrovaProdottoPerIdProdotto(idProdotto);

                    for (Object o : resultList) {
                        ProdottoOri prodottoOri = (ProdottoOri) o; 
                        String timeMix = DEFAULT_TEMPO_MISCELAZIONE;
                        String velMix = DEFAULT_VELOCITA_MISCELAZIONE;

                        try {
                            String readTimeMix = TrovaValoreProdottoByIdProd(prodottoOri.getIdProdotto(), 1);
                            String readVelMix = TrovaValoreProdottoByIdProd(prodottoOri.getIdProdotto(), 2);

                            if (!readTimeMix.equals("")) {
                                timeMix = readTimeMix;
                            }
                            if (!readVelMix.equals("")) {
                                velMix = readVelMix;
                            }

                        } catch (Exception ex) {

                            SessionLogger.logger.severe("Errore durante la lettura dei Parametri Prodotto - e:" + ex);

                        }

                        velMix = velMix.substring(0, velMix.length() - 2) + "." + velMix.substring(velMix.length() - 2, velMix.length());

                        //Creazione Messaggio Visualizzato
                        msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 547, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + selezione
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 548, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + prodottoOri.getCodProdotto()
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 549, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + prodottoOri.getDescriFamiglia()
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 550, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + prodottoOri.getIdCat().getNomeCategoria()
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 759, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + timeMix + " "
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 761, ParametriSingolaMacchina.parametri.get(111)))
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 760, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + velMix + " "
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 762, ParametriSingolaMacchina.parametri.get(111)))
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 764, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + numCodChimicaValidi
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 765, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + chimicheUsate.size()//numeroChimicheUtilizzate
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 766, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + numCodChimicaSfusiValidi
                                + HTML_BREAK_LINE
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 767, ParametriSingolaMacchina.parametri.get(111)))
                                + " " + ParametriGlobali.parametri.get(46) + " "
                                + numProcessiChimicaSfusa
                                + HTML_BREAK_LINE
                                + HTML_BREAK_LINE;

                    }
                    //Creazione Messaggio Visualizzato
                    msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 552, ParametriSingolaMacchina.parametri.get(111)))
                            + HTML_BREAK_LINE;

                    resultList = TrovaComponentiProdottoById(idProdotto);

                    for (Object o : resultList) {
                        ComponentePesaturaOri compProdOri;
                        compProdOri = (ComponentePesaturaOri) o;

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                            ////////////////////////
                            // CONVERSIONE PESO  ///
                            ////////////////////////
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compProdOri.getIdComp(), ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + ConvertiPesoVisualizzato(Double.toString(compProdOri.getQuantita()),
                                            ParametriSingolaMacchina.parametri.get(338))
                                    + " "
                                    + ParametriSingolaMacchina.parametri.get(340)
                                    + HTML_BREAK_LINE;
                        } else {

                            ///////////////////////
                            // SISTEMA METRICO  ///
                            ///////////////////////
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compProdOri.getIdComp(), ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + compProdOri.getQuantita()
                                    + " "
                                    + ParametriSingolaMacchina.parametri.get(340)
                                    + HTML_BREAK_LINE;
                        }

                    }

                    msg += HTML_STRINGA_FINE;

                    elemLabelSimple[0].setText(msg);

                    break;
                }

                case 1: {

                    ///////////////////////////////////////////////////
                    // DATI RELATIVI AL CODICE CHIMICA SELEZIONATO  ///
                    ///////////////////////////////////////////////////
                    String msg = HTML_STRINGA_INIZIO
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 554, ParametriSingolaMacchina.parametri.get(111)))
                            + HTML_BREAK_LINE;

                    //Ricerca Dettagli Codice Chimica
                    List<?> resultList = TrovaDettagliCodiceChimica(selezione);

                    if (selezione.charAt(0) == ParametriGlobali.parametri.get(15).charAt(0)) {//Benefit.findValoreParametroGlobale(15).charAt(0)) {

                        for (Object o : resultList) {

                            ChimicaOri chimicaOri = (ChimicaOri) o;

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 560, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 561, ParametriSingolaMacchina.parametri.get(111)))
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 555, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getCodChimica()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 556, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getDescriFormula()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 557, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getNumBolla()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 558, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getDtBolla()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 559, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getCodLotto();
                        }
                    } else {

                        String[] sub = new String[3];
                        String s = selezione;
                        int i = 0;

                        while (i < 2) {
                            sub[i] = "";
                            while (s.charAt(0) != ParametriGlobali.parametri.get(20).charAt(0)) {//Benefit.findValoreParametroGlobale(20).charAt(0)) {
                                sub[i] += s.charAt(0);
                                s = s.substring(1, s.length());
                            }
                            i++;
                            s = s.substring(1, s.length());
                        }

                        sub[i] = s;

                        //Ricerca Numero Miscele Realizzate
                        int misceleRealizzate = (TrovaMaxMisceleTabellaProcessoPerCodChimica(selezione));

                        for (Object o : resultList) {

                            ChimicaOri chimicaOri = (ChimicaOri) o;

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 560, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 562, ParametriSingolaMacchina.parametri.get(111)))
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 555, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getCodChimica()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 563, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + sub[1]
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 564, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + sub[2]
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 565, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + misceleRealizzate
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 566, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + (Integer.parseInt(sub[2]) - misceleRealizzate)
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 556, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getDescriFormula()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 557, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getNumBolla()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 558, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getDtBolla()
                                    + HTML_BREAK_LINE
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 559, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + chimicaOri.getCodLotto();

                        }

                    }

                    //Creazione Messaggio Visualizzato
                    msg += HTML_STRINGA_FINE;

                    elemLabelSimple[0].setText(msg);

                    break;
                }

                case 2: {

                    //////////////////////////////////////////////////////
                    // DATI RELATIVI AL CODICE CONFEZIONE SELEZIONATO  ///
                    //////////////////////////////////////////////////////
                    String msg = HTML_STRINGA_INIZIO
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 567, ParametriSingolaMacchina.parametri.get(111)))
                            + HTML_BREAK_LINE;

                    //Ricerca Dettagli Confezione
                    List<?> resultList = TrovaDettagliCodiceConfezione(selezione);

                    for (Object o : resultList) {

                        ProcessoOri processoOri = (ProcessoOri) o;

                        if (processoOri.getCodSacco() != null) {

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 568, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getCodSacco()
                                    + HTML_BREAK_LINE;
                        }

                        if (processoOri.getCliente() != null) {
                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 569, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getCliente()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getCodChimica() != null) {

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 570, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getCodChimica()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getCodColore() != null) {

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 571, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getCodColore()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getCodCompPeso() != null) {

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 572, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getCodCompPeso()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getCodProdotto() != null) {

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 573, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getCodProdotto()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getDtProduzione() != null) {

                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 574, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getDtProduzione()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getIdProcesso() != null) {
                            //Creazione Messaggio Visualizzato
                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 575, ParametriSingolaMacchina.parametri.get(111)))
                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                    + processoOri.getIdProcesso()
                                    + HTML_BREAK_LINE;

                        }
                        if (processoOri.getPesoRealeSacco() != null) {

                            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {
                                ////////////////////////
                                // CONVERSIONE PESO  ///
                                ////////////////////////
                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 576, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + ConvertiPesoVisualizzato(Integer.toString(processoOri.getPesoRealeSacco()),
                                                ParametriSingolaMacchina.parametri.get(338))
                                        + " "
                                        + ParametriSingolaMacchina.parametri.get(340);
                            } else {

                                ///////////////////////
                                // SISTEMA METRICO  ///
                                ///////////////////////
                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 576, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getPesoRealeSacco();
                            }
                        }

                        //Creazione Messaggio Visualizzato
                        msg += HTML_STRINGA_FINE;

                    }

                    elemLabelSimple[0].setText(msg);

                    break;
                }

                case 3: {

                    //////////////////////////////////////////
                    // DATI RELATIVI AL CODICE SELEZIONATO  ///
                    ///////////////////////////////////////////
                    String msg = HTML_STRINGA_INIZIO
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 577, ParametriSingolaMacchina.parametri.get(111)))
                            + HTML_BREAK_LINE;

                    if (selezione.charAt(0) == ParametriGlobali.parametri.get(17).charAt(0)) {//Benefit.findValoreParametroGlobale(17).charAt(0)) {

                        /////////////////////////
                        // CODICE CONFEZIONE  ///
                        /////////////////////////
                        //Ricerca Dettagli Relativi al Codice Confezione
                        List<?> resultList = TrovaDettagliCodiceConfezione(selezione);

                        for (Object o : resultList) {

                            ProcessoOri processoOri = (ProcessoOri) o;

                            if (processoOri.getCodSacco() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 568, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getCodSacco()
                                        + HTML_BREAK_LINE;
                            }

                            if (processoOri.getCliente() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 569, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getCliente()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getCodChimica() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 570, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getCodChimica()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getCodColore() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 571, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getCodColore()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getCodCompPeso() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 572, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getCodCompPeso()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getCodProdotto() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 573, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getCodProdotto()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getDtProduzione() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 574, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getDtProduzione()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getIdProcesso() != null) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 575, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + processoOri.getIdProcesso()
                                        + HTML_BREAK_LINE;

                            }
                            if (processoOri.getPesoRealeSacco() != null) {
                                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {
                                    ////////////////////////
                                    // CONVERSIONE PESO  ///
                                    ////////////////////////
                                    //Creazione Messaggio Visualizzato
                                    msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 576, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + ConvertiPesoVisualizzato(Integer.toString(processoOri.getPesoRealeSacco()),
                                                    ParametriSingolaMacchina.parametri.get(338))
                                            + " "
                                            + ParametriSingolaMacchina.parametri.get(340);
                                } else {

                                    ///////////////////////
                                    // SISTEMA METRICO  ///
                                    ///////////////////////
                                    //Creazione Messaggio Visualizzato
                                    msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 576, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + processoOri.getPesoRealeSacco();
                                }
                            }

                            //Creazione Messaggio Visualizzato
                            msg += HTML_STRINGA_FINE;

                        }
                    } else {

                        if (selezione.charAt(0) == ParametriGlobali.parametri.get(15).charAt(0)) {//Benefit.findValoreParametroGlobale(15).charAt(0)) {

                            ///////////////////////////////////////
                            // CODICE KIT CHIMICA CONFEZIONATA  ///
                            ///////////////////////////////////////
                            if (TrovaCodiciChimicaValidi(selezione).length > 0) {

                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 579, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + selezione
                                        + HTML_BREAK_LINE
                                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 580, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 581, ParametriSingolaMacchina.parametri.get(111)))
                                        + HTML_BREAK_LINE;

                                //Ricerca Dettagli Codice Chimica
                                List<?> dettagliCodice = TrovaDettagliCodiceChimica(selezione);

                                for (Object o : dettagliCodice) {

                                    ChimicaOri chimicaOri = (ChimicaOri) o;

                                    //Creazione Messaggio Visualizzato
                                    msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 559, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getCodLotto()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 556, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getDescriFormula()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 557, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getNumBolla()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 558, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getDtBolla()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 582, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getDtAbilitato()
                                            + HTML_STRINGA_FINE;

                                }

                            } else {

                                //////////////////////////////////
                                //KIT CHIMICO GIA' UTILIZZATO  ///
                                //////////////////////////////////
                                //Creazione Messaggio Visualizzato
                                msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 579, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + selezione
                                        + HTML_BREAK_LINE
                                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 580, ParametriSingolaMacchina.parametri.get(111)))
                                        + " " + ParametriGlobali.parametri.get(46) + " "
                                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 583, ParametriSingolaMacchina.parametri.get(111)))
                                        + HTML_BREAK_LINE;

                                //Ricerca Dettagli Codice Chimica Utilizzata
                                List<?> dettagliCodice = TrovaDettagliCodiceChimicaUsato(selezione);

                                int i = 0;
                                for (Object o : dettagliCodice) {
                                    ProcessoOri processoOri = (ProcessoOri) o;

                                    if (i == 0) {
                                        i++;
                                        if (processoOri.getCliente() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 569, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + processoOri.getCliente()
                                                    + HTML_BREAK_LINE;

                                        }
                                        if (processoOri.getCodChimica() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 570, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + processoOri.getCodChimica()
                                                    + HTML_BREAK_LINE;

                                        }
                                        if (processoOri.getCodColore() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 571, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + processoOri.getCodColore()
                                                    + HTML_BREAK_LINE;

                                        }
                                        if (processoOri.getCodCompPeso() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 572, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + processoOri.getCodCompPeso()
                                                    + HTML_BREAK_LINE;

                                        }
                                        if (processoOri.getCodProdotto() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 573, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + processoOri.getCodProdotto()
                                                    + HTML_BREAK_LINE;

                                        }
                                        if (processoOri.getDtProduzione() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 574, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + processoOri.getDtProduzione()
                                                    + HTML_BREAK_LINE;

                                        }
                                        if (processoOri.getCodSacco() != null) {

                                            //Creazione Messaggio Visualizzato
                                            msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 578, ParametriSingolaMacchina.parametri.get(111)))
                                                    + " " + ParametriGlobali.parametri.get(46) + " "
                                                    + HTML_BREAK_LINE;
                                        }
                                    }

                                    //Creazione Messaggio Visualizzato
                                    msg += processoOri.getCodSacco() + " " + ParametriGlobali.parametri.get(48) + " ";
                                }

                                //Creazione Messaggio Visualizzato
                                msg += HTML_STRINGA_FINE;
                            }

                        } else {

                            List<?> resultList = TrovaDettagliCodiceChimica(selezione);

                            /////////////////////
                            // CHIMICA SFUSA  ///
                            /////////////////////
                            String[] sub = new String[3];
                            String s = selezione;
                            int i = 0;

                            if (resultList != null & resultList.size() > 0) {

                                while (i < 2) {
                                    sub[i] = "";
                                    while (s.charAt(0) != ParametriGlobali.parametri.get(20).charAt(0)) {
                                        sub[i] += s.charAt(0);
                                        s = s.substring(1, s.length());
                                    }
                                    i++;
                                    s = s.substring(1, s.length());
                                }

                                sub[i] = s;

                                //Ricerca Numero Miscele Realizzate
                                int misceleRealizzate = (TrovaMaxMisceleTabellaProcessoPerCodChimica(selezione));

                                for (Object o : resultList) {

                                    ChimicaOri chimicaOri = (ChimicaOri) o;

                                    //Creazione Messaggio Visualizzato
                                    msg += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 560, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 562, ParametriSingolaMacchina.parametri.get(111)))
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 555, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getCodChimica()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 563, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + sub[1]
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 564, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + sub[2]
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 565, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + misceleRealizzate
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 566, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + (Integer.parseInt(sub[2]) - misceleRealizzate)
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 556, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getDescriFormula()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 557, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getNumBolla()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 558, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getDtBolla()
                                            + HTML_BREAK_LINE
                                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 559, ParametriSingolaMacchina.parametri.get(111)))
                                            + " " + ParametriGlobali.parametri.get(46) + " "
                                            + chimicaOri.getCodLotto();

                                }

                            } else {

                                //Creazione Messaggio Visualizzato
                                msg += HTML_BREAK_LINE + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 680, ParametriSingolaMacchina.parametri.get(111)));
                            }

                        }

                        //Creazione Messaggio Visualizzato
                        msg += HTML_STRINGA_FINE;

                    }

                    elemLabelSimple[0].setText(msg);

                    break;
                }
            }

        }
    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {
            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 553, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }
}
