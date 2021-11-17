package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParCompOri;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.utility.AggiornaValoriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiAdditiviPerIdAdditivo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiColoriPerIdColore;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiProdottoPerIdProdPerIdComponente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPreseAbilitatePerLaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroCompPerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiValoriParametriCompPerIdComponente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponenti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreProdottoByIdProd;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_PRESA_NON_DEFINITA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_TEMPO_MISCELAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_VELOCITA_MISCELAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUMERO_PRESE_VISUALIZZATE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.*;

@SuppressWarnings("serial")
public class Pannello10_ScelteEffettuate extends MyStepPanel {

    //PARAMETRI PANNELLO
    //Dimensione della Lugnhezza di una Riga di Formula Colore
    private final int numSfondoTab = 4;
    //VARIABILI
    //Immagini di Sfondo Tab
    public JLabel[] imageSfondoTab;
    //Indice per la Selezione delle Prese
    private int indexPresa = 0;
    //Esito dei Test di Verfica sulle Prese
    private boolean preseDuplicate, preseCodiceNonAssociato, preseIndefinite, preseIncongruenti;

    //COSTRUTTORE
    public Pannello10_ScelteEffettuate() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(22);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(0);
        impostaDimLabelProg(1);
        impostaDimLabelBut(13);
        impostaColori(5);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[2]);

        //Inizializzazione Colori Label Help **/
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colori Label Title **/
        initColorLabelTitle(elemColor[4]);

        //Inserimento Button Freccia
        inserisciButtonFreccia();

        //Dichiarazione Immagini di Sfondo Tab
        imageSfondoTab = new JLabel[numSfondoTab];

        //Configurazione di Base Pannello
        configuraPannello();

        //Avvio Thread Caricamento Loading Image
        new ThreadInserisciLoadingImage(this).start();

        //Insermento Immagini di Sfondo dei Tab
        new ThreadInserisciSfondoTab(this).start();

        //Definizione Caratteristiche Label Simple
        definisciLabelSimple();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità e Abilitazione Prese 
        for (int i = 0; i < NUMERO_PRESE_VISUALIZZATE; i++) {
            elemBut[7 + i].setVisible(false);
            elemBut[7 + i].setEnabled(false);
            elemBut[7 + i].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        //Inizializzazione Tab Visualizzato
        impostaVisTabDatiGenerali();

        //Impostazione Visibilità Pulsanti
        impostaVisPulsanti(false);

        aggiornaVisTabFormulaColore(false);
        elemLabelSimple[20].setText("");

        //Lettura Parametri Componenti Relativi al Prodotto Scelto
        leggiParComponenti();

        //Verifica Errori Sulle Prese
        verificaErrori();

        //Aggiornamento Label Prese
        aggiornaLabelPrese();

        //Aggiornamento Colori Label Prese
        aggiornaColoriLabelPrese(0);

        //Aggiornamento Label Tab Formula Prodotto
        aggiornaLabelTabFormulaProdotto(indexPresa);

        //Impostazione Visibilità Pulsanti
        impostaVisPulsanti(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Avvio ThreadLettura Dati Relativi alle Scelte Effettuate **/
        new LeggiDettagli().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Impostazione Abilitazione Pulsanti Scorrimento Prese
        elemBut[5].setEnabled(false);
        elemBut[6].setEnabled(scelte.numeroComponenti
                > NUMERO_PRESE_VISUALIZZATE);

        //Impostaione Visibilità Pannello
        setPanelVisibile();

        leggiInfoColoriAdditivi();

    }

    //Lettura Informazioni da Database
    private class LeggiDettagli extends Thread {

        @Override
        public void run() {

            //Analisi delle Variabili Boolean per Definire le Stringhe da Visualizzare
            String strColorato = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 104, ParametriSingolaMacchina.parametri.get(111)));
            // String strCemento = Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 70, ParametriSingolaMacchina.parametri.get(111)));
            String strContenitore = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 58, ParametriSingolaMacchina.parametri.get(111)));
            String strChimica = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 73, ParametriSingolaMacchina.parametri.get(111)));
            String strBilancia = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 874, ParametriSingolaMacchina.parametri.get(111)));

            //Inizializzazione Valore di Default
            String timeMix = DEFAULT_TEMPO_MISCELAZIONE;
            String velMix = DEFAULT_VELOCITA_MISCELAZIONE;

            try {

                String readTimeMix = TrovaValoreProdottoByIdProd(scelte.idProdotto, 1);
                String readVelMix = TrovaValoreProdottoByIdProd(scelte.idProdotto, 2);

                if (!readTimeMix.equals("")) {
                    timeMix = readTimeMix;
                }
                if (!readVelMix.equals("")) {
                    velMix = readVelMix;
                }

            } catch (Exception ex) {

                SessionLogger.logger.log(Level.SEVERE, "Errore durante il caricamento di tempo e velocita di miscelazione del prodtto - e:{0}", ex);

            }

            velMix = velMix.substring(0, velMix.length() - 2) + "." + velMix.substring(velMix.length() - 2, velMix.length());

            //Aggiornamento in Funzione delle Scelte Effettuate
            if (scelte.colorato) {
                strColorato = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 103, ParametriSingolaMacchina.parametri.get(111)));
            }
////            if (scelte.cambioCemento) {
////                strCemento = Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 69, ParametriSingolaMacchina.parametri.get(111)));
////            }
            if (scelte.cambioContenitore) {
                strContenitore = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 59, ParametriSingolaMacchina.parametri.get(111)));
            }
            if (scelte.cambioConfezioneChimica) {
                strChimica = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 74, ParametriSingolaMacchina.parametri.get(111)));
            }

            if (scelte.cambioBilancia) {
                strBilancia = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 875, ParametriSingolaMacchina.parametri.get(111)));
            }

            //Lettura e Assegnazione Dati Visualizzati nel Tab Dati Generali
            elemLabelSimple[4].setText(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 96, ParametriSingolaMacchina.parametri.get(111))) + TrovaCodiceProdottoPerIdProdotto(scelte.idProdotto)
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 593, ParametriSingolaMacchina.parametri.get(111))) + scelte.codiceProdotto
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 97, ParametriSingolaMacchina.parametri.get(111))) + scelte.nomeProdotto
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 98, ParametriSingolaMacchina.parametri.get(111))) + scelte.descrizioneFamiglia
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 99, ParametriSingolaMacchina.parametri.get(111))) + scelte.numeroMiscele
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 100, ParametriSingolaMacchina.parametri.get(111))) + scelte.numeroSacchetti
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 873, ParametriSingolaMacchina.parametri.get(111))) + strBilancia
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 101, ParametriSingolaMacchina.parametri.get(111))) + ConvertiPesoVisualizzato(Integer.toString(scelte.pesoSacchetto), ParametriSingolaMacchina.parametri.get(338))
                    + ParametriSingolaMacchina.parametri.get(340)
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 102, ParametriSingolaMacchina.parametri.get(111))) + strColorato
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 105, ParametriSingolaMacchina.parametri.get(111))) + strContenitore
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 106, ParametriSingolaMacchina.parametri.get(111))) + strChimica
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1000, ParametriSingolaMacchina.parametri.get(111))) + scelte.cambioCemento
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 108, ParametriSingolaMacchina.parametri.get(111))) + scelte.cliente
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 759, ParametriSingolaMacchina.parametri.get(111)))
                    + timeMix + " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 761, ParametriSingolaMacchina.parametri.get(111)))
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 760, ParametriSingolaMacchina.parametri.get(111)))
                    + velMix + " " + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 762, ParametriSingolaMacchina.parametri.get(111)))
                    //                    + FabCloudConstants.HTML_BREAK_LINE
                    //                    + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 915, ParametriSingolaMacchina.parametri.get(111))) + ": " + scelte.cambioBilancia
                    + HTML_STRINGA_FINE);

            //Lettura e Assegnazione Label "SCEGLI UNA PRESA"
            elemLabelSimple[19].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 117, ParametriSingolaMacchina.parametri.get(111)));

            repaint();

        }
    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 91, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 92, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 93, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 988, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 95, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(int panel) {

        loadingImg.setVisible(false);

        this.setVisible(false);

        if (panel == 1) {
            
            ///////////////////////
            // AVVIO PRODUZIONE ///
            /////////////////////// 
            ((Pannello11_Processo) pannelliCollegati.get(1)).initPanel();

        } else if (panel == 0) {

            /////////////////
            //  INDIETRO  ///
            ///////////////// 
            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
        }

        scelte.inizializzaScelte();

    }

    //Gestione Scambio Pannelli Collegati
    public void scambioPannelloEditaPrese() {

        this.setVisible(false);

        ((Pannello13_Configurazione_Generale) pannelliCollegati.get(2)).initPanel(true);

    }

    //Gestione Visibilità Tab Selezionato
    private void aggiornaTabVisibile(int j) {
        for (int i = 0; i < imageSfondoTab.length; i++) {
            imageSfondoTab[i].setVisible((i == j));
        }

    }

    //Aggiornamento Colori Label Visualizzati nel Tab "FORMULA PRODOTTO" Relativi alle Prese
    private void aggiornaColoriLabelPrese(int j) {

        for (int i = 9; i < 20; i++) {
            elemLabelSimple[i].setForeground(elemColor[0]);
        }

        elemLabelSimple[9 + j].setForeground(elemColor[1]);
        elemLabelSimple[10 + j].setForeground(elemColor[1]);

    }

    //Aggiornamento Dati Visualizzati nel Tab "FORMULA PRODOTTO" Relativi ai Componenti **/
    private void aggiornaLabelTabFormulaProdotto(int j) {

        elemLabelSimple[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 110, ParametriSingolaMacchina.parametri.get(111)));
        elemLabelSimple[6].setText(HTML_STRINGA_INIZIO
                + scelte.descriComponente.get(j)
                + HTML_STRINGA_INIZIO);
        elemLabelSimple[7].setText(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 110, ParametriSingolaMacchina.parametri.get(111))) + scelte.codiceComponente.get(j)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 112, ParametriSingolaMacchina.parametri.get(111))) + ConvertiPesoVisualizzato(Integer.toString(scelte.secondaVelocitaComponente.get(j)), ParametriSingolaMacchina.parametri.get(338))
                + ParametriSingolaMacchina.parametri.get(340)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 113, ParametriSingolaMacchina.parametri.get(111))) + scelte.correttivoVelocita.get(j)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 114, ParametriSingolaMacchina.parametri.get(111))) + ConvertiPesoVisualizzato(Integer.toString(scelte.voloComponente.get(j)), ParametriSingolaMacchina.parametri.get(338))
                + ParametriSingolaMacchina.parametri.get(340)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 684, ParametriSingolaMacchina.parametri.get(111))) + scelte.curvaVelComponente.get(j)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 202, ParametriSingolaMacchina.parametri.get(111))) + ConvertiPesoVisualizzato(Integer.toString(scelte.dosaturaFineDirettaComponente.get(j)), ParametriSingolaMacchina.parametri.get(338))
                + ParametriSingolaMacchina.parametri.get(340)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 115, ParametriSingolaMacchina.parametri.get(111))) + ConvertiPesoVisualizzato(Double.toString(scelte.quantitaComponente.get(j)), ParametriSingolaMacchina.parametri.get(338))
                + ParametriSingolaMacchina.parametri.get(340)
                + HTML_BREAK_LINE
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 116, ParametriSingolaMacchina.parametri.get(111)))
                + HTML_STRINGA_FINE);

        if ((scelte.presaComponente.get(j).substring(0, 1).equals(TrovaPresaPerIdPresa(Integer.parseInt(ParametriGlobali.parametri.get(134)))))) {
            elemLabelSimple[8].setText(TrovaPresaPerIdPresa(Integer.parseInt(ParametriGlobali.parametri.get(134))));

        } else {
            elemLabelSimple[8].setText(scelte.presaComponente.get(j));
        }

    }

    //Aggiornamento Visibilita Label nel Tab "FORMULA PRODOTTO"
    public void aggiornaVisTabFormulaProdotto(boolean vis) {

        for (int i = 5; i < 20; i++) {
            elemLabelSimple[i].setVisible(vis);
        }
        elemBut[5].setVisible(vis);
        elemBut[6].setVisible(vis);
    }

    //Aggiornamento Visibilita Label nel Tab "FORMULA COLORE" **/
    public void aggiornaVisTabFormulaColore(boolean vis) {

        elemLabelSimple[20].setVisible(vis);

    }

    //Impostazione Allineamento e Colori Label Tipo Simple
    private void definisciLabelSimple() {

        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            //Impostazione Allineamento Orizzontale
            elemLabelSimple1.setHorizontalAlignment(SwingConstants.LEFT);
            //Impostazione Allineamento Verticale
            elemLabelSimple1.setVerticalAlignment(SwingConstants.TOP);
            //Impostazione Cursore
            elemLabelSimple1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        //Impostazione Cursori
        for (int i = 0; i < 4; i++) {
            elemLabelSimple[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        switch (button) {
            case "0": {

                /////////////////////////
                // TAB DATI GENERALI  ///
                /////////////////////////
                impostaVisTabDatiGenerali();

                break;
            }

            case "1": {

                ////////////////////////////
                // TAB FORMULA PRODOTTO  ///
                ////////////////////////////
                impostaVisTabFormulaProdotto();

                break;
            }

            case "2": {

                /////////////////////////
                // TAB FORMULA COLORE ///
                /////////////////////////
                if (scelte.colorato || scelte.additivato) {
                    impostaVisTabColore();
                }
                break;
            }
            case "3": {

                ///////////////
                // TAB NOTE ///
                ///////////////
                impostaVisTabNote();

                break;
            }
            case "4": {

                ///////////////////
                // PULSANTE OK  ///
                ///////////////////
                impostaVisTabDatiGenerali();

                //Controllo Errori Prese
                if (!preseIncongruenti & !preseDuplicate & !preseIndefinite) {

                    loadingImg.setVisible(true);
                    impostaVisPulsanti(false);

                    String id_serie_colore = "";
                    String id_serie_additivo = "";

                    if (scelte.idColore > 0) {

                        id_serie_colore = Integer.toString(scelte.idColore);
                    }

                    if (scelte.idAdditivo > 0) {

                        id_serie_additivo = Integer.toString(scelte.idAdditivo);
                    }
                    //Registrazione Nuovo Ciclo
                    scelte.id_ciclo = RegistraNuovoCiclo(ParametriGlobali.parametri.get(129),
                            scelte.ord_id_ordine,
                            scelte.idProdotto,
                            scelte.idCategoria,
                            Integer.parseInt(TrovaValoreProdottoByIdProd(scelte.idProdotto, 2)),
                            Integer.parseInt(TrovaValoreProdottoByIdProd(scelte.idProdotto, 1)),
                            scelte.numeroSacchetti,
                            0,
                            TrovaValoreProdottoByIdProd(scelte.idProdotto, 3),
                            TrovaValoreProdottoByIdProd(scelte.idProdotto, 4),
                            TrovaValoreProdottoByIdProd(scelte.idProdotto, 5),
                            TrovaValoreProdottoByIdProd(scelte.idProdotto, 6),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            id_serie_colore,
                            id_serie_additivo);

                    TimeLineLogger.init();

                    TimeLineLogger.logger.log(Level.INFO, "Avvio Processo Produttivo - id_ciclo ={0}", scelte.id_ciclo);

                    new AggiornaValoriRipristino(scelte, this).start();

                } else {

                    impostaVisTabNote();
                }

                break;
            }
            case "5": {

                /////////////////////////////
                // PULSANTE PRESE DIETRO  ///
                /////////////////////////////
                if (indexPresa > 0) {
                    indexPresa--;
                }
                elemBut[5].setEnabled(!(indexPresa == 0));
                elemBut[6].setEnabled(!(indexPresa == scelte.numeroComponenti
                        - NUMERO_PRESE_VISUALIZZATE));
                aggiornaColoriLabelPrese(0);
                aggiornaLabelTabFormulaProdotto(indexPresa);
                aggiornaLabelPrese();
                break;
            }
            case "6": {

                /////////////////////////////
                // PULSANTE PRESE AVANTI  ///
                /////////////////////////////
                if (indexPresa < scelte.numeroComponenti - NUMERO_PRESE_VISUALIZZATE) {
                    indexPresa++;
                }

                elemBut[6].setEnabled(!(indexPresa == scelte.numeroComponenti
                        - NUMERO_PRESE_VISUALIZZATE));

                elemBut[5].setEnabled(!(indexPresa == 0));

                aggiornaColoriLabelPrese(0);

                aggiornaLabelTabFormulaProdotto(indexPresa);

                aggiornaLabelPrese();

                break;
            }
            case "7": {

                ///////////////
                // PRESA 1  ///
                ///////////////
                aggiornaLabelTabFormulaProdotto(0 + indexPresa);

                aggiornaColoriLabelPrese(0);

                break;
            }
            case "8": {

                ///////////////
                // PRESA 2  ///
                ///////////////
                aggiornaLabelTabFormulaProdotto(1 + indexPresa);

                aggiornaColoriLabelPrese(2);

                break;
            }
            case "9": {

                ///////////////
                // PRESA 3  ///
                ///////////////
                aggiornaLabelTabFormulaProdotto(2 + indexPresa);

                aggiornaColoriLabelPrese(4);

                break;
            }
            case "10": {

                ///////////////
                // PRESA 4  ///
                ///////////////
                aggiornaLabelTabFormulaProdotto(3 + indexPresa);

                aggiornaColoriLabelPrese(6);

                break;
            }
            case "11": {

                ///////////////
                // PRESA 5  ///
                ///////////////
                aggiornaLabelTabFormulaProdotto(4 + indexPresa);

                aggiornaColoriLabelPrese(8);

                break;
            }

            case "12": {

                ///////////////////
                // EDITA PRESE  ///
                ///////////////////
                scambioPannelloEditaPrese();

                break;
            }
        }

    }

    //Lettura Parametri Componenti da Database
    private void leggiParComponenti() {

        ArrayList<Integer> idComponenti = new ArrayList<>();

        List<?> valoreParCompOriColl = TrovaValoreParametriComponenti(scelte);

        //Dimensionamento Array Dati Relativi ai Componenti
        scelte.dimArrayDatiComponenti(valoreParCompOriColl.size());

        //Assegnazione dei Dati Letti da Database
        for (Object o : valoreParCompOriColl) {
            ValoreParCompOri valParCompOri = (ValoreParCompOri) o;
            idComponenti.add(valParCompOri.getIdComp());

        }
        List<?> componentiColl;

        for (int i = 0; i < idComponenti.size(); i++) {

            //if (scelte.cambioCemento && idCementoGrigio == idComponenti.get(i)) {
            boolean componenteDaSostituire = false;
            int idComponenteSostitutivo = 0;
            for (int j = 0; j < scelte.compSostitutivi.size(); j++) {

                if (scelte.compSostitutivi.get(j).get(0).equals(idComponenti.get(i))) {
                    componenteDaSostituire = true;
                    idComponenteSostitutivo = scelte.compSostitutivi.get(j).get(1);
                    break;
                }

            }

            if (componenteDaSostituire) {

                componentiColl = TrovaComponentiProdottoPerIdProdPerIdComponente(scelte.idProdotto, scelte.idColore, scelte.idAdditivo, idComponenti.get(i));

                //Assegnazione dei Dati letti da Database
                for (Object o : componentiColl) {
                    ComponentePesaturaOri componentiPesaturaOri = (ComponentePesaturaOri) o;
                    scelte.quantitaComponente.add(componentiPesaturaOri.getQuantita());

                    scelte.tolleranzaDifettoDosaggioComponente.add(Integer.toString(componentiPesaturaOri.getTollDifetto()));
                    scelte.tolleranzaEccessoDosaggioComponente.add(Integer.toString(componentiPesaturaOri.getTollEccesso()));
                    scelte.metodo_pesa.add(componentiPesaturaOri.getMetodoPesa());
                    scelte.fluidificazione.add(componentiPesaturaOri.getFluidificazione());
                    scelte.valore_residuo_flidificazione.add(Integer.toString(componentiPesaturaOri.getValoreResiduoFluidificazione()));
                    scelte.ordine_dosaggio.add(Integer.toString(componentiPesaturaOri.getOrdineDosaggio()));
                    scelte.step_dosaggio.add(componentiPesaturaOri.getStepDosaggio());
                    scelte.step_dosaggio_tempo_mix.add(componentiPesaturaOri.getTempoMix());
                    scelte.step_dosaggio_vel_mix.add(componentiPesaturaOri.getVelocitaMix());

                }

                scelte.cambioCemento = true;

                //Assegnazione Dati Relativi al Cemento Bianco
                scelte.idComponenti.add(idComponenteSostitutivo);

                //scelte.codiceComponente.add(ParametriGlobali.parametri.get(12));
                scelte.codiceComponente.add(TrovaCodiceComponentePerIdComp(idComponenteSostitutivo));

                //Lettura Descrizione Componenti da Dizionario
                scelte.descriComponente.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, idComponenteSostitutivo, ParametriSingolaMacchina.parametri.get(111))));

            } else {

                //Lettura dei dati relativi ai singoli componenti per prodotto
                componentiColl = TrovaComponentiProdottoPerIdProdPerIdComponente(scelte.idProdotto, scelte.idColore, scelte.idAdditivo, idComponenti.get(i));

                //Assegnazione dei Dati letti da Database
                for (Object o : componentiColl) {

                    ComponentePesaturaOri componentiPesaturaOri = (ComponentePesaturaOri) o;

                    scelte.idComponenti.add(idComponenti.get(i));
                    scelte.quantitaComponente.add(componentiPesaturaOri.getQuantita());

                    scelte.codiceComponente.add(TrovaCodiceComponentePerIdComp(componentiPesaturaOri.getIdComp()));

                    scelte.tolleranzaDifettoDosaggioComponente.add(Integer.toString(componentiPesaturaOri.getTollDifetto()));
                    scelte.tolleranzaEccessoDosaggioComponente.add(Integer.toString(componentiPesaturaOri.getTollEccesso()));
                    scelte.metodo_pesa.add(componentiPesaturaOri.getMetodoPesa());
                    scelte.fluidificazione.add(componentiPesaturaOri.getFluidificazione());
                    scelte.valore_residuo_flidificazione.add(Integer.toString(componentiPesaturaOri.getValoreResiduoFluidificazione()));
                    scelte.ordine_dosaggio.add(Integer.toString(componentiPesaturaOri.getOrdineDosaggio()));
                    scelte.step_dosaggio.add(componentiPesaturaOri.getStepDosaggio());
                    scelte.step_dosaggio_tempo_mix.add(componentiPesaturaOri.getTempoMix());
                    scelte.step_dosaggio_vel_mix.add(componentiPesaturaOri.getVelocitaMix());

                    //Lettura Descrizione Componenti da Dizionario
                    scelte.descriComponente.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, scelte.idComponenti.get(i), ParametriSingolaMacchina.parametri.get(111))));

                }
            }
        }

        List<?> valoreParCompColl;
        for (int i = 0; i < scelte.numeroComponenti; i++) {

            valoreParCompColl = TrovaTuttiValoriParametriCompPerIdComponente(scelte.idComponenti.get(i));
            int k = 0;
            for (Object o : valoreParCompColl) {
                ValoreParCompOri valoreParCompOri = (ValoreParCompOri) o;
                scelte.parametriComponente[i][k] = valoreParCompOri.getValoreVariabile();
                k++;

            }
        }

  //      int jPresaCounter = 1;

        //Assegnazione dei Parametri agli ArrayList
        for (int i = 0; i < scelte.numeroComponenti; i++) {

            double secondaVelocitaComponente = Double.parseDouble(scelte.parametriComponente[i][1]);
            double voloComponente = Double.parseDouble(scelte.parametriComponente[i][2]);
            double correttivoVel = Double.parseDouble(scelte.parametriComponente[i][3]);
            double dosaturaFineDirettaComponente = Double.parseDouble(scelte.parametriComponente[i][4]);

            int qStopCocleeComponente = Integer.parseInt(scelte.parametriComponente[i][7]);
            int qStopVibro = Integer.parseInt(scelte.parametriComponente[i][8]);

            if (scelte.metodo_pesa.get(i).equals(ParametriGlobali.parametri.get(131))) {
                scelte.idPresa.add(Integer.parseInt(ParametriGlobali.parametri.get(134)));
                scelte.presaComponente.add(TrovaPresaPerIdPresa(Integer.parseInt(ParametriGlobali.parametri.get(134)))); //+ jPresaCounter);
               // jPresaCounter++;

            } else {
                scelte.idPresa.add(Integer.parseInt(scelte.parametriComponente[i][0]));
                scelte.presaComponente.add(TrovaPresaPerIdPresa(Integer.parseInt(scelte.parametriComponente[i][0])));
            }

            scelte.secondaVelocitaComponente.add((int) secondaVelocitaComponente);
            scelte.voloComponente.add((int) voloComponente);
            scelte.qStopMescolaComponente.add(qStopCocleeComponente);
            scelte.qStopVibroComponente.add(qStopVibro);

            scelte.correttivoVelocita.add(correttivoVel);
            scelte.dosaturaFineDirettaComponente.add((int) dosaturaFineDirettaComponente);
            scelte.rifMagazzinoComponenti.add(scelte.parametriComponente[i][5]);

            String curvaVelComponente = "";

            try {

                curvaVelComponente = (scelte.parametriComponente[i][6]);

            } catch (Exception ex) {

                SessionLogger.logger.log(Level.SEVERE, "Attenzione Parametro componente non definito {0}", ex);

            }
            scelte.curvaVelComponente.add(curvaVelComponente);

        }

        for (int i = 0; i < Math.min(scelte.idComponenti.size(), NUMERO_PRESE_VISUALIZZATE); i++) {
            elemBut[7 + i].setVisible(true);
            elemBut[7 + i].setEnabled(true);
            elemBut[7 + i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        }
    }

    private void leggiInfoColoriAdditivi() {

        /////////////////////
        // LETTURA COLORI  //
        /////////////////////
        String coloriQuantita = "";
        ArrayList<String> colori = TrovaComponentiColoriPerIdColore(scelte.idColore);

        for (int i = 0; i < colori.size(); i++) {
            coloriQuantita += colori.get(i) + HTML_BREAK_LINE;

        }
        String msg_colore = "";

        if (!coloriQuantita.equals("")) {
            msg_colore = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 986, ParametriSingolaMacchina.parametri.get(111))) + ": "
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, scelte.idColore, ParametriSingolaMacchina.parametri.get(111)))
                    + HTML_BREAK_LINE
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 987, ParametriSingolaMacchina.parametri.get(111))) + ": "
                    + HTML_BREAK_LINE
                    + coloriQuantita + HTML_BREAK_LINE
                    + HTML_BREAK_LINE;

        }

        ///////////////////////
        // LETTURA ADDITIVI  //
        ///////////////////////
        String additivi_Quantita = "";

        ArrayList<String> additivi = TrovaComponentiAdditiviPerIdAdditivo(scelte.idAdditivo);

        for (int i = 0; i < additivi.size(); i++) {
            additivi_Quantita += additivi.get(i) + HTML_BREAK_LINE;

        }
        String msg_additivo = "";
        if (!additivi_Quantita.equals("")) {
            msg_additivo = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 993, ParametriSingolaMacchina.parametri.get(111))) + ": "
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, scelte.idAdditivo, ParametriSingolaMacchina.parametri.get(111)))
                    + HTML_BREAK_LINE
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 987, ParametriSingolaMacchina.parametri.get(111))) + ": "
                    + HTML_BREAK_LINE
                    + additivi_Quantita + HTML_BREAK_LINE
                    + HTML_STRINGA_FINE;

        }

        //Lettura e Assegnazione Dati Visualizzati nel Tab Dati Generali
        elemLabelSimple[20].setText(HTML_STRINGA_INIZIO
                + msg_colore
                + msg_additivo);

    }

    //Verifica la Presenza di Errori Relativi alle Prese
    public void verificaErrori() {

        //String errStr[] = new String[FabCloudConstants.NUMERO_ERRORI_PRESE];
        ArrayList<String> errStr = new ArrayList<>();

        //Verifica dei Dati Relativi alle Prese
        verificaCongruenzaPrese();
        verificaDefinizionePrese();
        verificaUguaglianzaPrese();
        verificaCodiceAssociato();

        //Verifica che Tutte le Prese Definite per i Componenti Siano Diverse
        if (preseDuplicate) {
            errStr.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 119, ParametriSingolaMacchina.parametri.get(111))));
        }

        //Verifica che per Ogni Componente sia Definita una Presa
        if (preseIndefinite) {
            errStr.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 120, ParametriSingolaMacchina.parametri.get(111))));
        }

        //Verifica che le Prese Definite Siano Congruenti
        if (preseIncongruenti) {

            errStr.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 121, ParametriSingolaMacchina.parametri.get(111))));

        }
        //Verifica che le Prese Definite Siano Congruenti
        if (preseCodiceNonAssociato) {

            errStr.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1002, ParametriSingolaMacchina.parametri.get(111))));

        }

        if (!preseIncongruenti & !preseDuplicate & !preseIndefinite && !preseCodiceNonAssociato) {

            elemLabelSimple[21].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 122, ParametriSingolaMacchina.parametri.get(111)));

        } else {

            String msg = HTML_STRINGA_INIZIO;

            for (int i = 0; i < errStr.size() - 1; i++) {

                msg += errStr.get(i) + HTML_BREAK_LINE;

            }

            msg += errStr.get(errStr.size() - 1) + HTML_STRINGA_FINE;

            elemLabelSimple[21].setText(msg);
        }

    }

    //Controlla Ripetizioni Prese
    private void verificaUguaglianzaPrese() {

        preseDuplicate = false;

        for (int i = 0; i < scelte.presaComponente.size(); i++) {

            for (int j = 0; j < scelte.presaComponente.size(); j++) {

                if (i != j) {

                    if (scelte.presaComponente.get(i).equals(scelte.presaComponente.get(j)) && !scelte.presaComponente.get(i).equals(TrovaPresaPerIdPresa(98))) {
                        preseDuplicate = true;
                        break;
                    }
                }
            }

        }
    }

    //Controllo Prese Non Definite
    public void verificaCodiceAssociato() {

        preseCodiceNonAssociato = false;

        for (int i = 0; i < scelte.idComponenti.size(); i++) {

            if ((TrovaSingoloValoreParametroCompPerIdComp(scelte.idComponenti.get(i), 1)).equals(ParametriGlobali.parametri.get(98))) {
                preseCodiceNonAssociato = true;
                break;
            }
        }

    }

    public void verificaDefinizionePrese() {

        preseIndefinite = false;

        for (int i = 0; i < scelte.presaComponente.size(); i++) {

            //if (!scelte.codiceComponente.get(i).equals(ParametriGlobali.parametri.get(25))) {
            if (!scelte.codiceComponente.get(i).equals(ParametriSingolaMacchina.parametri.get(301))) {

                if (scelte.presaComponente.get(i).equals(TrovaPresaPerIdPresa(99))) {

                    preseIndefinite = true;

                    break;
                }
            }
        }
    }

    //Verifica Congruenza Definizioni delle Prese con la Macchina
    public void verificaCongruenzaPrese() {

        preseIncongruenti = false;

        Boolean[] preseMacchina = new Boolean[scelte.idPresa.size()];
        for (int i = 0; i < preseMacchina.length; i++) {

            preseMacchina[i] = false;

        }

        for (int i = 0; i < scelte.idPresa.size(); i++) {

            if (scelte.idPresa.get(i) != 99) {
                for (int j = 0; j < TrovaPreseAbilitatePerLaMacchina().size(); j++) {

                    if (TrovaPresaPerIdPresa(scelte.idPresa.get(i)).equals(TrovaPreseAbilitatePerLaMacchina().get(j))) {
                        preseMacchina[i] = true;
                        break;
                    }
                }
            }
        }

        for (Boolean preseMacchina1 : preseMacchina) {
            if (preseMacchina1 == false) {
                preseIncongruenti = true;
                break;
            }
        }

    }

    //Aggiorna Label Relativi alle Prese Visualizzate
    public void aggiornaLabelPrese() {

        for (int k = 0; k < 5; k++) {

            elemLabelSimple[9 + 2 * k].setText("");
            elemLabelSimple[10 + 2 * k].setText("");
        }

        for (int k = 0; k < Math.min(scelte.numeroComponenti, 5); k++) {

            String s_presa = CARATTERE_PRESA_NON_DEFINITA;

            if (scelte.idPresa.get(k) != Integer.parseInt(ParametriGlobali.parametri.get(64))) {

                s_presa = scelte.presaComponente.get(indexPresa + k);
            }
            elemLabelSimple[9 + 2 * k].setText(s_presa);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                ///////////////////////
                // PESO CONVERTITO  ///
                ///////////////////////
                elemLabelSimple[10 + 2 * k].setText(ConvertiPesoVisualizzato(
                                Double.toString(scelte.quantitaComponente.get(indexPresa + k)),
                                ParametriSingolaMacchina.parametri.get(338)));
            } else {
                ///////////////////////
                // SISTEMA METRICO  ///
                ///////////////////////

                elemLabelSimple[10 + 2 * k].setText(
                        Double.toString(scelte.quantitaComponente.get(indexPresa + k)));
            }
        }

    }

    //Impostazione Visibilità e/o Validità Pulsanti
    private void impostaVisPulsanti(boolean vis) {

        //Impostazione Visibilità Freccia Indietro
        butFreccia.setVisible(vis);

        //Abilitazione Pulsanti
        elemBut[0].setEnabled(vis);
        elemBut[1].setEnabled(vis);
        elemBut[2].setEnabled(vis);
        elemBut[3].setEnabled(vis);
        elemBut[4].setEnabled(vis);

    }

    //Rende Visibile il Tab "DATI GENERALI"
    private void impostaVisTabDatiGenerali() {

        aggiornaTabVisibile(0);
        elemLabelSimple[4].setVisible(true);
        elemLabelSimple[21].setVisible(false);
        aggiornaVisTabFormulaProdotto(false);
        aggiornaVisTabFormulaColore(false);
        aggiornaVisPulsanteEditaPrese(false);
    }

    //Rende Visibile il Tab "FORMULA PRODOTTO"
    private void impostaVisTabFormulaProdotto() {

        aggiornaTabVisibile(1);
        elemLabelSimple[4].setVisible(false);
        elemLabelSimple[21].setVisible(false);
        aggiornaVisPulsanteEditaPrese(true);
        aggiornaVisTabFormulaProdotto(true);
        aggiornaVisTabFormulaColore(false);
    }

    //Rende Visibile il Tab "FORMULA COLORE"
    private void impostaVisTabColore() {

        aggiornaTabVisibile(2);
        elemLabelSimple[4].setVisible(false);
        elemLabelSimple[21].setVisible(false);
        aggiornaVisTabFormulaProdotto(false);
        aggiornaVisPulsanteEditaPrese(false);
        aggiornaVisTabFormulaColore(true);
    }

    //Rende Visibile il Tab "FORMULA COLORE"
    private void impostaVisTabNote() {

        aggiornaTabVisibile(3);
        elemLabelSimple[4].setVisible(false);
        elemLabelSimple[21].setVisible(true);
        aggiornaVisTabFormulaProdotto(false);
        aggiornaVisTabFormulaColore(false);
        aggiornaVisPulsanteEditaPrese(false);

    }

    public void aggiornaVisPulsanteEditaPrese(Boolean vis) {
        elemBut[12].setVisible(vis);
        elemBut[12].setEnabled(vis);
    }
}
