package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciChimicaValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello04_SceltaTipoChimica extends MyStepPanel {

    private int numCodChimicaSfusiValidi;
    private int numCodChimicaValidi;

    //COSTRUTTORE
    public Pannello04_SceltaTipoChimica() {
        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(2);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(1);
        impostaDimLabelBut(2);
        impostaColori(3);

        //Inizializzazione Colore Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colore Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colore Label Title
        initColorLabelTitle(elemColor[2]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Modifica la Visibilità di Default delle Righe di Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

            //Inizializzazione Validità Pulsante
            elemBut[1].setEnabled(false);

            //Impostazione Visibilità Pannello
            setPanelVisibile();

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Vocaboli Traducibili da Database
            new LeggiDizionario().start();

            //Ricerca Codici Chimica Sfusa
            new TrovaCodiciSfusi().start();

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);
            
        } else {

            //Memorizzazione Cambio Confezione Chimica (0 false - 1 True)
            scelte.cambioConfezioneChimica = false;

            scelte.numeroChimicheDisponibili = Integer.parseInt(ParametriSingolaMacchina.parametri.get(1));

            gestoreScambioPannello();
        }

    }

    //Controllo Esistenza Codici Chimiche Sfuse per il Prodotto Selezionato
    private class TrovaCodiciSfusi extends Thread {

        @Override
        public void run() {

            if (scelte.nomeProdotto.substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143))
                    || scelte.nomeProdotto.substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302))
                    || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

                elemBut[1].setEnabled(true);

            } else {

                //Lista codici Chimica Validi
                String[] listaCodici = TrovaCodiciChimicaValidi(
                        scelte.codiceChimica.substring(0, Integer.parseInt(ParametriGlobali.parametri.get(47))));

                numCodChimicaSfusiValidi = 0;
                numCodChimicaValidi = 0;

                for (int i = 0; i < listaCodici.length; i++) {

                    if (listaCodici[i].substring(0, 1).equals(
                            ParametriGlobali.parametri.get(19))) {
                        numCodChimicaSfusiValidi++;

                    } else if (listaCodici[i].substring(0, 1).equals(
                            ParametriGlobali.parametri.get(15))) {
                        numCodChimicaValidi++;
                    }
                }

                if (numCodChimicaValidi > 0) {
                    elemBut[0].setEnabled(true);
                } else {
                    elemBut[0].setEnabled(false);
                }

                if (numCodChimicaSfusiValidi > 0) {
                    elemBut[1].setEnabled(true);
                } else {
                    elemBut[1].setEnabled(false);
                }

            }

        }
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
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 77, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 78, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 76, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 75, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Button
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 73, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 74, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Gestione Eventi dei Pulsanti
    public void gestorePulsanti(String button) {

        //Memorizzazione Cambio Confezione Chimica (0 false - 1 True)
        scelte.cambioConfezioneChimica = button.equals(elemBut[1].getName());

        if (button.equals(elemBut[0].getName())) {
            scelte.numeroChimicheDisponibili = numCodChimicaValidi;
        }

        //Scambio Pannello
        gestoreScambioPannello();

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        //Memorizzazione Scelte Effettuate
        ((Pannello05_SceltaNumMiscele) pannelliCollegati.get(1)).scelte = scelte;
        ((Pannello05_SceltaNumMiscele) pannelliCollegati.get(1)).findDatiProdottoSelezionato(scelte.idProdotto);
        ((Pannello05_SceltaNumMiscele) pannelliCollegati.get(1)).initPanel();

    }

}
