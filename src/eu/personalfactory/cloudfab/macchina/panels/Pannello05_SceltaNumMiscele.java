package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaNumeroMisceleRealizzabiliChimicaSfusaPerCodProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_FAMIGLIE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.List;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello05_SceltaNumMiscele extends MyStepPanel {

    //VARIABILI
    private String[] nMiscele;
    private int[] nMisceleVal;
    public int correttivoMiscela;

    //COSTRUTTORE
    public Pannello05_SceltaNumMiscele() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(12);
        impostaDimLabelSimple(0);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(1);
        impostaDimLabelBut(2);
        impostaColori(5);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[4]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Modifica la Visibilità di Default delle Righe di Aiuto
        impostaVisibilitaAiuto(true);

        //Impostazione Allineamento Testi Lista Selezionabile
        for (int j = 0; j < elemLabelPlus.length; j++) {

            elemLabelPlus[j].setHorizontalAlignment(SwingConstants.RIGHT);
            elemLabelPlus[j].setVerticalAlignment(SwingConstants.BOTTOM);
        }

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Label Lista Selezionabile
        initLista();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Elementi Lista Selezionabile
        new LeggiLista().start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Label Tipo Helo
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 47, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 48, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 44, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 45, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 46, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Lettura Elementi Lista Selezionabile da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {
 
             
            int numeroMisceleRealizzabili = Math.min(scelte.numeroChimicheDisponibili,Integer.parseInt(ParametriSingolaMacchina.parametri.get(1)));
            int numeroMisceleRealizzabiliChimicaSfusa = 0;

            if (numeroMisceleRealizzabili < elemLabelPlus.length) {
                nMiscele = new String[elemLabelPlus.length];
                nMisceleVal = new int[elemLabelPlus.length];
            } else {
                nMiscele = new String[numeroMisceleRealizzabili];
                nMisceleVal = new int[numeroMisceleRealizzabili];
            }

            String nomeProdotto = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, scelte.idProdotto, ParametriSingolaMacchina.parametri.get(111)));

            for (int i = 1; i <= nMiscele.length; i++) {

                nMiscele[i - 1] = Integer.toString(i + correttivoMiscela);

                if (nomeProdotto.startsWith(ParametriSingolaMacchina.parametri.get(143))
                        || nomeProdotto.startsWith(ParametriSingolaMacchina.parametri.get(302))
                        || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

                    ///////////////////////////////////////////////////
                    // PRODOTTO CON CONTROLLO CHIMICA DISABILITATO  ///
                    ///////////////////////////////////////////////////
                    nMiscele[i - 1] = Integer.toString(i);
                    nMisceleVal[i - 1] = 1;

                } else ////////////////////////////////////////////////
                // PRODOTTO CON CONTROLLO CHIMICA ABILITATO  ///
                ////////////////////////////////////////////////
                if (!scelte.cambioConfezioneChimica) {

                    ////////////////////////////
                    // CHIMICA CONFEZIONATA  ///
                    ////////////////////////////
                    if (i < scelte.numeroChimicheDisponibili + 1 - correttivoMiscela) {
                        nMisceleVal[i - 1] = 1;
                    } else {
                        nMisceleVal[i - 1] = 0;
                    }

                } else {

                    /////////////////////
                    // CHIMICA SFUSA  ///
                    ///////////////////// 
                    if (numeroMisceleRealizzabiliChimicaSfusa == 0 && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(317))) {

                        numeroMisceleRealizzabiliChimicaSfusa = TrovaNumeroMisceleRealizzabiliChimicaSfusaPerCodProdotto(scelte.codiceChimica);

                    } else {

                        numeroMisceleRealizzabiliChimicaSfusa = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));

                    }

                    if (i < numeroMisceleRealizzabiliChimicaSfusa + 1 - correttivoMiscela) {
                        nMisceleVal[i - 1] = 1;
                    } else {
                        nMisceleVal[i - 1] = 0;
                    }
                }
            }

            for (int j = nMiscele.length; j < elemLabelPlus.length; j++) {
                nMiscele[j] = Integer.toString(j);
                nMisceleVal[j] = 0;
            }

            //Aggiornamento Testo Label Lista Selezionabile
            for (int j = 0; j < elemLabelPlus.length; j++) {
                elemLabelPlus[j].setText(nMiscele[j]);
            }

            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 1);
            definisciLista(nMiscele);
            definisciListaAttributi(nMisceleVal);
            startThreadControllo();

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        //Inserimento label Tipo Lista
        for (int j = 0; j < elemLabelPlus.length; j++) {
            elemLabelPlus[j].setText("");
        }

        //Impostazione Visibilità Elementi Lista Selezionabile
        for (int j = 0; j < elemLabelPlus.length; j++) {
            elemLabelPlus[j].setVisible(true);
        }

    }

    //Ricerca Informazioni sul Prodotto Selezionato dal Database
    public void findDatiProdottoSelezionato(int id_prodotto) {

        //Lettura Database: Lista prodotti per id
        List<?> prodottoColl = TrovaProdottoPerIdProdotto(id_prodotto);

        for (Object o : prodottoColl) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            scelte.idProdotto = prodottoOri.getIdProdotto();
            scelte.idCategoria = prodottoOri.getIdCat().getIdCat();
            scelte.tipoFamiglia = prodottoOri.getTipoFamiglia();
            scelte.limiteColore = Integer.parseInt(prodottoOri.getLimColore());
            scelte.fattoreDivisore = Integer.parseInt(prodottoOri.getFattoreDiv());
            scelte.fascia = Double.parseDouble(prodottoOri.getFascia());
            scelte.idCodice = prodottoOri.getIdCodice();
          //  scelte.idMazzetta = prodottoOri.getIdMazzetta().getIdMazzetta();

            //Ricerca Nome Prodotto e Descrizione Famiglia da Dizionario
            scelte.nomeProdotto = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                    prodottoOri.getIdProdotto(),
                    ParametriSingolaMacchina.parametri.get(111)));

            scelte.descrizioneFamiglia = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_FAMIGLIE,
                    prodottoOri.getIdCodice(),
                    ParametriSingolaMacchina.parametri.get(111)));

        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        if (scelte.ord_id_ordine>0) {
            ((Pannello10_ScelteEffettuate) pannelliCollegati.get(2)).scelte = scelte;
            ((Pannello10_ScelteEffettuate) pannelliCollegati.get(2)).initPanel();

        } else {
            ((Pannello06_SceltaDimContenitore) pannelliCollegati.get(1)).scelte = scelte; 
            ((Pannello06_SceltaDimContenitore) pannelliCollegati.get(1)).initPanel();
        }

        
        this.setVisible(false);

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        //pannelloAvanti = null;
        if (button.equals(elemBut[1].getName())) {

            ///////////////////////////
            // PANNELLO PRODUZIONE  ///
            /////////////////////////// 
            controllaSelezione.interrompi = true;

            if (correttivoMiscela < Integer.parseInt(ParametriSingolaMacchina.parametri.get(1)) - 12) {
                correttivoMiscela += 1;
            }

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Elementi Lista Selezionabile
            new LeggiLista().start();

        } else if (button.equals(elemBut[0].getName())) {

            /////////////////////
            // AGGIORNAMENTO  ///
            /////////////////////
            controllaSelezione.interrompi = true;
            if (correttivoMiscela > 0) {
                correttivoMiscela -= 1;
            }

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Elementi Lista Selezionabile
            new LeggiLista().start();

        }

    }

}
