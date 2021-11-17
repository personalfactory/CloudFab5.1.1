package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaStatoOrdine;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodicePadreById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciChimicaValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaOrdini;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiDettagliOrdinePerIdOrdine;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovavaloreParOrdinePerIdOrdinePerIdPar;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaComponentiAlternativi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEF_STATE_ORDINE_SOSPESO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEF_STATE_ORDINE_VALIDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_FAMIGLIE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Pannello03_Ordini extends MyStepPanel {

    //VARIABILI 
    ArrayList<ArrayList<ArrayList<String>>> ordini;
    int index = 0;
    int indiceRighe = 0;
    int ordineProduzione = 0;
    boolean abilitaRiprendiProduzioni = false;

    //COSTRUTTORE
    public Pannello03_Ordini() {

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
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(5);
        impostaDimLabelProg(1);
        impostaDimLabelBut(9);
        impostaColori(10);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[2]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializza Colori Label Title
        initColorLabelTitle(elemColor[0]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Avvio Thread Caricamento Immagine di Caricamento
        new ThreadInserisciLoadingImage(this).start();

        //Configurazione di Base Pannello
        configuraPannello();

        //Aggiornamento Cursori Label Simple
        aggiornaCursori();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

        //Impostazione Colori Titoli Tabella
        elemLabelTitle[1].setForeground(elemColor[5]);
        elemLabelTitle[2].setForeground(elemColor[5]);
        elemLabelTitle[3].setForeground(elemColor[5]);
        elemLabelTitle[4].setForeground(elemColor[5]);

        //Impostazione Colori Label Data
        elemLabelSimple[36].setForeground(elemColor[6]);

        //Impostazione Colori Label Testo Pulsanti
        elemLabelSimple[37].setForeground(elemColor[7]);
        elemLabelSimple[38].setForeground(elemColor[7]);

    }

    //Inizializzazione Pannello
    public void initPanel(ArrayList<ArrayList<ArrayList<String>>> ordini) {

        this.ordini = ordini;

        index = ordini.size() / 2;

        elemBut[4].setEnabled(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(380)));
        elemBut[5].setEnabled(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(383)));
        elemBut[6].setEnabled(true);

        impostaVisibilitaAiuto(true);

        //Impostazione Visualizzazione Button Freccia
        butFreccia.setVisible(false);

        //Impostazione visibilità Loading Image
        loadingImg.setVisible(true);

        //Impostazione Visbilità Pannello
        setPanelVisibile();

        //Imposta Visibilità But Lucchetto
        elemBut[7].setVisible(true);
        elemBut[8].setVisible(false);

        //Iniziliazzazione visibilità riprendi produzioni sospese
        abilitaRiprendiProduzioni = false;

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //lettura Elementi Lista Selezionabile
        new LeggiLista().start();
        
        scelte.compSostitutivi = new ArrayList<>();

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
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 840, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Inserimento Label Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 841, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 842, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 843, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 844, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 845, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento Label Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 39, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento Label Pulsanti
                elemLabelSimple[37].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 846, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[38].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 847, ParametriSingolaMacchina.parametri.get(111)));
            }
        }
    }

    //Lettura Elementi Lista Selezionabile da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            aggiornaListaProduzioni();

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(false);

            //Impostazione Visbilità Button Freccia
            butFreccia.setVisible(true);
        }
    }

    //Aggiornamento Cursori Label Simple
    public void aggiornaCursori() {

        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setCursor(Cursor.getDefaultCursor());
        }

    }

    public void aggiornaListaProduzioni() {

        //Inizializzazione Label Simple
        for (int i = 0; i < 36; i++) {
            elemLabelSimple[i].setText("");
        }

        elemLabelSimple[36].setText(ordini.get(index).get(0).get(0));

        int indexProgrammati = 0;
        int indexEseguitiIncompleti = 0;

        int conunterProgrammati = 0;
        int counterEseguitiIncompleti = 0;

        //Aggiornamento Label
        for (int j = 0; j < ordini.get(index).size(); j++) {

            if (ordini.get(index).get(j).size() > 6) {
                ////////////////////////////////////
                // LABEL PRODOTTI DA REALIZZARE  ///
                ////////////////////////////////////
                if (ordini.get(index).get(j).get(7).equals(DEF_STATE_ORDINE_VALIDO) || (abilitaRiprendiProduzioni && ordini.get(index).get(j).get(7).equals(DEF_STATE_ORDINE_SOSPESO))) {

                    if ((19 + indexProgrammati * 4 < 36)) {

                        if (conunterProgrammati < indiceRighe) {

                            conunterProgrammati++;

                        } else {

                            if (indexProgrammati == 0) {

                                ordineProduzione = j;
                            }
                            //Aggiornamento Testi 
                            elemLabelSimple[16 + indexProgrammati * 4].setText(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                    Integer.parseInt(ordini.get(index).get(j).get(3)),
                                    ParametriSingolaMacchina.parametri.get(111)));

                            elemLabelSimple[17 + indexProgrammati * 4].setText(ordini.get(index).get(j).get(6));
                            elemLabelSimple[18 + indexProgrammati * 4].setText(ordini.get(index).get(j).get(5));

                            //Numero sacchi Per Miscela
                            int numSacchiMiscela = Integer.parseInt(TrovavaloreParOrdinePerIdOrdinePerIdPar(Integer.parseInt(ordini.get(index).get(j).get(1)), 2));

                            //Conteggio Miscele per realizzare il quantitativo desiderato
                            int numMiscele = (Integer.parseInt(ordini.get(index).get(j).get(5)) - Integer.parseInt(ordini.get(index).get(j).get(6))) / numSacchiMiscela;
                            int resto = (Integer.parseInt(ordini.get(index).get(j).get(5)) - Integer.parseInt(ordini.get(index).get(j).get(6))) % numSacchiMiscela;

                            if (resto > 0) {
                                numMiscele++;
                            }

                            elemLabelSimple[19 + indexProgrammati * 4].setText(Integer.toString(numMiscele));

                            int indiceColori = 2;

                            if (TrovaIdCodiceProdottoPerIdProdotto(ordini.get(index).get(j).get(3)).equals(ParametriSingolaMacchina.parametri.get(385))) {

                                //////////////
                                // PULIZIA ///
                                /////////////
                                indiceColori = 8;

                            } else if (TrovaIdCodiceProdottoPerIdProdotto(ordini.get(index).get(j).get(3)).equals(ParametriSingolaMacchina.parametri.get(386))) {
                                //////////////////
                                // SVUOTAMENTO ///
                                //////////////////
                                indiceColori = 9;

                            } else if (ordini.get(index).get(j).get(7).equals(DEF_STATE_ORDINE_SOSPESO)) {

                                /////////////////////
                                // ORDINE SOSPESO ///
                                /////////////////////
                                indiceColori = 3;
                            }

                            elemLabelSimple[16 + indexProgrammati * 4].setForeground(elemColor[indiceColori]);
                            elemLabelSimple[17 + indexProgrammati * 4].setForeground(elemColor[indiceColori]);
                            elemLabelSimple[18 + indexProgrammati * 4].setForeground(elemColor[indiceColori]);
                            elemLabelSimple[19 + indexProgrammati * 4].setForeground(elemColor[indiceColori]);
                            indexProgrammati++;
                        }
                    }
                } else {

                    /////////////////////////////////////////
                    // LABEL PRODOTTI ESEGUITI INCOMPLETI ///
                    /////////////////////////////////////////
                    if (counterEseguitiIncompleti < indiceRighe) {

                        counterEseguitiIncompleti++;

                    } else {

                        if ((3 + indexEseguitiIncompleti * 4 < 16)) {

                            //Aggiornamento Testi 
                            elemLabelSimple[0 + indexEseguitiIncompleti * 4].setText(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                    Integer.parseInt(ordini.get(index).get(j).get(3)),
                                    ParametriSingolaMacchina.parametri.get(111)));

                            elemLabelSimple[1 + indexEseguitiIncompleti * 4].setText(ordini.get(index).get(j).get(6));
                            elemLabelSimple[2 + indexEseguitiIncompleti * 4].setText(ordini.get(index).get(j).get(5));

                            //Numero sacchi Per Miscela
                            int numSacchiMiscela = Integer.parseInt(TrovavaloreParOrdinePerIdOrdinePerIdPar(Integer.parseInt(ordini.get(index).get(j).get(1)), 2));

                            //Conteggio Miscele per realizzare il quantitativo desiderato
                            int numMiscele = (Integer.parseInt(ordini.get(index).get(j).get(5)) - Integer.parseInt(ordini.get(index).get(j).get(6))) / numSacchiMiscela;
                            int resto = (Integer.parseInt(ordini.get(index).get(j).get(5)) - Integer.parseInt(ordini.get(index).get(j).get(6))) % numSacchiMiscela;

                            if (resto > 0) {
                                numMiscele++;
                            }

                            elemLabelSimple[3 + indexEseguitiIncompleti * 4].setText(Integer.toString(numMiscele));

                            int indiceColori = 4;
                            if (ordini.get(index).get(j).get(7).equals(DEF_STATE_ORDINE_SOSPESO)) {
                                indiceColori = 3;
                            }

                            elemLabelSimple[0 + indexEseguitiIncompleti * 4].setForeground(elemColor[indiceColori]);
                            elemLabelSimple[1 + indexEseguitiIncompleti * 4].setForeground(elemColor[indiceColori]);
                            elemLabelSimple[2 + indexEseguitiIncompleti * 4].setForeground(elemColor[indiceColori]);
                            elemLabelSimple[3 + indexEseguitiIncompleti * 4].setForeground(elemColor[indiceColori]);

                            indexEseguitiIncompleti++;
                        }
                    }
                }

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            ////////////////////
            // GIORNO DIETRO ///
            ////////////////////
            if (index > 0) {

                index--;
                indiceRighe = 0;
                elemBut[6].setEnabled(true);
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(380))) {
                    elemBut[4].setEnabled(true);
                }

                aggiornaListaProduzioni();

            }

        } else if (button.equals(elemBut[1].getName())) {
            ////////////////////
            // GIORNO AVANTI ///
            ////////////////////
            if (index < ordini.size() - 1) {

                index++;

                aggiornaListaProduzioni();

            }

        } else if (button.equals(elemBut[2].getName())) {

            //////////////
            // RIGA SU ///
            ////////////// 
            if (indiceRighe > 0) {
                indiceRighe--;
                aggiornaListaProduzioni();
            }

            if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(384))) {
                elemBut[6].setEnabled(indiceRighe == 0);
            } else {
                elemBut[6].setEnabled(true);
            }

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(380))) {
                elemBut[4].setEnabled(true);
            }

        } else if (button.equals(elemBut[3].getName())) {

            //////////////
            // RIGA GIU ///
            ////////////// 
            if (indiceRighe < ordini.get(index).size()) {
                indiceRighe++;
                elemBut[6].setEnabled(false);

                if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(384))) {
                    elemBut[6].setEnabled(indiceRighe == 0);
                } else {
                    elemBut[6].setEnabled(true);
                }

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(380)) && !Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(384))) {
                    elemBut[4].setEnabled(false);
                }
                aggiornaListaProduzioni();
            }
        } else if (button.equals(elemBut[4].getName())) {

            //////////////////////
            /// PULSANTE SKIP  ///
            ////////////////////// 
            if (indiceRighe == 0 && !elemLabelSimple[19].getText().equals("")) {

                //Aggiornamento Stato Ordine "saltato"
                AggiornaStatoOrdine(Integer.parseInt(ordini.get(index).get(ordineProduzione).get(1)), DEF_STATE_ORDINE_SOSPESO);

                //Aggiornamento Lista Ordini
                ordini = TrovaOrdini(Integer.parseInt(ParametriSingolaMacchina.parametri.get(382)));

                //Aggiornamento Lista Ordini Visualizzati
                aggiornaListaProduzioni();
            }

        } else if (button.equals(elemBut[5].getName())) {

            //////////////
            // IGNORA  ///
            //////////////
            this.setVisible(false);

            ((Pannello02_SceltaFiltro) pannelliCollegati.get(1)).initPanel();

        } else if (button.equals(elemBut[6].getName())) {

            if (indiceRighe == 0 || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(384))) {

                ///////////////
                // CONFERMA ///
                ///////////////
                if (!elemLabelSimple[19].getText().equals("")) {

                    if (TrovaIdCodiceProdottoPerIdProdotto(ordini.get(index).get(ordineProduzione).get(3)).equals(ParametriSingolaMacchina.parametri.get(385))) {

                        ///////////////
                        // PULIZIA  ///
                        ///////////////
                        ((Pannello39_Pulizia_Automatica) pannelliCollegati.get(6)).initPanel(
                                Integer.parseInt(ordini.get(index).get(ordineProduzione).get(2)),
                                Integer.parseInt(ordini.get(index).get(ordineProduzione).get(3)));

                        //Impostazione visibilita pannello corrente
                        this.setVisible(false);
                    } else if (TrovaIdCodiceProdottoPerIdProdotto(ordini.get(index).get(ordineProduzione).get(3)).equals(ParametriSingolaMacchina.parametri.get(386))) {

                        ///////////////////
                        // SVUOTAMENTO  ///
                        /////////////////// 
                        ((Pannello38_Pulizia_Svuotamento) pannelliCollegati.get(5)).initPanel(
                                Integer.parseInt(ordini.get(index).get(ordineProduzione).get(2)),
                                Integer.parseInt(ordini.get(index).get(ordineProduzione).get(3)));

                        //Impostazione visibilita pannello corrente
                        this.setVisible(false);

                    } else {

                        /////////////////
                        // PRODUZIONE ///
                        /////////////////
                        ////////////////////////////////////
                        // LETTURA INFORMAZIONI ORDINE  ////
                        ////////////////////////////////////
                        scelte.idProdotto = Integer.parseInt(ordini.get(index).get(ordineProduzione).get(3));
                        scelte.ord_id_ordine_sm = Integer.parseInt(ordini.get(index).get(ordineProduzione).get(1));
                        scelte.ord_id_ordine = Integer.parseInt(ordini.get(index).get(ordineProduzione).get(2));
                        scelte.ord_contatore = Integer.parseInt(ordini.get(index).get(ordineProduzione).get(6));
                        scelte.ord_num_pezzi = Integer.parseInt(ordini.get(index).get(ordineProduzione).get(5));

                        System.out.println(scelte.ord_id_ordine_sm);
                        System.out.println(scelte.ord_id_ordine);
                        
                        //Lettura Database: Lista prodotti per id
                        List<?> prodottoColl = TrovaProdottoPerIdProdotto(scelte.idProdotto);

                        for (Object o : prodottoColl) {
                            ProdottoOri prodottoOri = (ProdottoOri) o;
                            scelte.idProdotto = prodottoOri.getIdProdotto();
                            scelte.idCategoria = prodottoOri.getIdCat().getIdCat();
                            scelte.tipoFamiglia = prodottoOri.getTipoFamiglia();
                            scelte.limiteColore = Integer.parseInt(prodottoOri.getLimColore());
                            scelte.fattoreDivisore = Integer.parseInt(prodottoOri.getFattoreDiv());
                            scelte.fascia = Double.parseDouble(prodottoOri.getFascia());
                            scelte.idCodice = prodottoOri.getIdCodice();
                           // scelte.idMazzetta = prodottoOri.getIdMazzetta().getIdMazzetta();

                            //Ricerca Nome Prodotto e Descrizione Famiglia da Dizionario
                            scelte.nomeProdotto = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI,
                                    prodottoOri.getIdProdotto(),
                                    ParametriSingolaMacchina.parametri.get(111)));

                            scelte.descrizioneFamiglia = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_FAMIGLIE,
                                    prodottoOri.getIdCodice(),
                                    ParametriSingolaMacchina.parametri.get(111)));

                        }
 
                        int codicePadre = TrovaCodicePadreById(scelte.idProdotto);

                        scelte.codiceChimica = TrovaCodiceProdottoPerIdProdotto(scelte.idProdotto);
                        scelte.codiceProdotto = TrovaCodiceProdottoPerIdProdotto(scelte.idProdotto);

                        if (codicePadre != 0) {

                            scelte.codiceChimica = TrovaCodiceProdottoPerIdProdotto(codicePadre);

                        }

                        scelte.dettagliOrdine = TrovaTuttiDettagliOrdinePerIdOrdine(scelte.ord_id_ordine_sm);

                        //Memorizzazione Cambio Confezione Chimica (0 false - 1 True)
                        scelte.cambioConfezioneChimica = Boolean.parseBoolean(scelte.dettagliOrdine.get(0));

                        scelte.numeroChimicheDisponibili = trovaCodiciChimicaValidi(scelte, scelte.cambioConfezioneChimica);

                        scelte.numeroSacchetti = Integer.parseInt(scelte.dettagliOrdine.get(1));
                        scelte.pesoSacchetto = Integer.parseInt(scelte.dettagliOrdine.get(2));
                        scelte.cambioContenitore = Boolean.parseBoolean(scelte.dettagliOrdine.get(3));
                        scelte.disabilitaRibalta = Boolean.parseBoolean(scelte.dettagliOrdine.get(4));

                        scelte.cliente = ParametriGlobali.parametri.get(40);

                        if (!scelte.dettagliOrdine.get(6).equals("")) {
                            scelte.cliente = scelte.dettagliOrdine.get(5);
                        }

                        scelte.numeroChimicheDisponibili = Integer.parseInt(elemLabelSimple[19].getText());

                        //scelte.numeroChimicheDisponibili = Math.min(trovaCodiciChimicaValidi(scelte, scelte.cambioConfezioneChimica), Integer.parseInt(elemLabelSimple[19].getText()));
                        //Aggiornamento Variabile Controllo Cambio Tipo Cemento
                        scelte.cambioCemento = Boolean.parseBoolean(scelte.dettagliOrdine.get(6));
                         
                        //COLORI 
                        
                        //Memorizzazione Prodotto Colorato
                        scelte.colorato = Boolean.parseBoolean(scelte.dettagliOrdine.get(7));

                        scelte.cambioBilancia = Boolean.parseBoolean(scelte.dettagliOrdine.get(8));

                            System.out.println(Boolean.parseBoolean(scelte.dettagliOrdine.get(6)));
                        if (Boolean.parseBoolean(scelte.dettagliOrdine.get(6))) {

                            //////////////////////////
                            // SCAMBIO COMPONENTI  ///
                            //////////////////////////
                            String strCompSostitutivi = scelte.dettagliOrdine.get(12);
                            boolean origCompTrovato = false;
                            String temp1 = "";
                            String temp2 = "";
                            
                            ArrayList<Integer> arrayComp;
                            
                            System.out.println(strCompSostitutivi);

                            for (int i = 0; i < strCompSostitutivi.length(); i++) {

                                if (i == strCompSostitutivi.length() - 1) {
                                    temp2 += strCompSostitutivi.charAt(i);
                                    arrayComp = new ArrayList<>();
                                    arrayComp.add(Integer.parseInt(temp1));
                                    //Verifica Componente Sostitutivo
                                    if (VerificaComponentiAlternativi(Integer.parseInt(temp2))) {
                                        arrayComp.add(Integer.parseInt(temp2));
                                    } else {
                                        arrayComp.add(Integer.parseInt(temp1));
                                    }
                                    scelte.compSostitutivi.add(arrayComp);
                                } else {

                                    if (!origCompTrovato) {
                                        if (strCompSostitutivi.charAt(i) == ParametriGlobali.parametri.get(22).charAt(0)) {
                                            origCompTrovato = true;
                                        } else {

                                            temp1 += strCompSostitutivi.charAt(i);
                                        }
                                    } else {
                                        if (strCompSostitutivi.charAt(i) == ParametriGlobali.parametri.get(156).charAt(0)) {
                                            origCompTrovato = false;
                                            arrayComp = new ArrayList<>();
                                            arrayComp.add(Integer.parseInt(temp1));
                                            //Verifica Componente Sostitutivo
                                            if (VerificaComponentiAlternativi(Integer.parseInt(temp2))) {
                                                arrayComp.add(Integer.parseInt(temp2));
                                            } else {
                                                arrayComp.add(Integer.parseInt(temp1));
                                            }
                                            scelte.compSostitutivi.add(arrayComp);
                                            temp1 = "";
                                            temp2 = "";
                                        } else {
                                            temp2 += strCompSostitutivi.charAt(i);
                                        }
                                    }
                                }
                            }
                        }
 
                        scelte.additivato = Boolean.parseBoolean(scelte.dettagliOrdine.get(9));

                        scelte.idColore = 0;
                        scelte.idAdditivo = 0;
                        scelte.nomeAdditivoSelezionato = "";
                        scelte.nomeColoreSelezionato = "";

                        if (scelte.colorato) {

                            scelte.idColore = Integer.parseInt(scelte.dettagliOrdine.get(10));
                            scelte.nomeColoreSelezionato = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, scelte.idColore, ParametriSingolaMacchina.parametri.get(111));
                        }
                        if (scelte.additivato) {

                            scelte.idAdditivo = Integer.parseInt(scelte.dettagliOrdine.get(11));
                            scelte.nomeAdditivoSelezionato = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, scelte.idAdditivo, ParametriSingolaMacchina.parametri.get(111));
                        } 
                        
                        ///////////////////////
                        // CAMBIO PANNELLO  ///
                        ///////////////////////
                        if (scelte.numeroChimicheDisponibili > 0
                                || scelte.nomeProdotto.substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143))
                                || scelte.nomeProdotto.substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302))
                                || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

                            //////////////////////////////////////////////////////////////////
                            // CHIMICHE VALIDE O PRODOTTO PRODUCIBILE CON CHIMICA INTERNA  ///
                            //////////////////////////////////////////////////////////////////
                            //Impostazione visibilita pannello corrente
                            this.setVisible(false);

                            //Impostazione visibilita pannello successivo
                            ((Pannello05_SceltaNumMiscele) pannelliCollegati.get(2)).scelte = scelte;
                            ((Pannello05_SceltaNumMiscele) pannelliCollegati.get(2)).initPanel();

                        } else {

                            //Visualizzazione Errore
                            ((Pannello44_Errori) pannelliCollegati.get(3)).gestoreErrori.visualizzaErrore(40);

                        }
                    }

                }
            }

        } else if (button.equals(elemBut[7].getName())) {

            //////////////////////////////////////////////
            // ABILITA RIPRENDI PRODUZIONI INCOMPLETE  ///
            //////////////////////////////////////////////
            //Aggiornamento Variabile di Controllo
            abilitaRiprendiProduzioni = true;

            //Aggiornamento Lista Ordini Visualizzati
            aggiornaListaProduzioni();

            //Aggiornamento Pulsante Visualizzato
            impostaVisButLucchetto();

        } else if (button.equals(elemBut[8].getName())) {

            //////////////////////////////////////////////
            // ABILITA RIPRENDI PRODUZIONI INCOMPLETE  ///
            //////////////////////////////////////////////
            //Aggiornamento Variabile di Controllo
            abilitaRiprendiProduzioni = false;

            //Aggiornamento Lista Ordini Visualizzati
            aggiornaListaProduzioni();

            //Aggiornamento Pulsante Visualizzato
            impostaVisButLucchetto();
        }

    }

    public int trovaCodiciChimicaValidi(ScelteEffettuate scelte, Boolean cambio_tipo_chimica) {
 
        //Lista codici Chimica Validi
        String[] listaCodici = TrovaCodiciChimicaValidi(
                scelte.codiceChimica.substring(0, Integer.parseInt(ParametriGlobali.parametri.get(47))));

        int result = 0;

        for (String listaCodici1 : listaCodici) {
            if (cambio_tipo_chimica && listaCodici1.substring(0, 1).equals(ParametriGlobali.parametri.get(19))) {
                result++;
            } else if (!cambio_tipo_chimica && listaCodici1.substring(0, 1).equals(ParametriGlobali.parametri.get(15))) {
                result++;
            }
        }
        return result;

    }

    //Imposta Visibilità But Lucchetto
    public void impostaVisButLucchetto() {

        elemBut[7].setVisible(!elemBut[7].isVisible());
        elemBut[8].setVisible(!elemBut[8].isVisible());

    } 
}
