package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiAlternativi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataAdditivPerSerieAdditivi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataClienti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataColoriPerSerieColori;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSerieAdditivoProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSerieColoreProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello08_SceltaCliente extends MyStepPanel {

    //VARIABILI
    private String[] clienti;

    //COSTRUTTORE
    public Pannello08_SceltaCliente() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

       //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(2);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(1);
        impostaDimLabelBut(2);
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

        //Avvio Thread Caricamento Loading Image
        new ThreadInserisciLoadingImage(this).start();

        //Inserimento Area di Testo
        inserisciTextField();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Allineamento Label
        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setHorizontalAlignment(SwingConstants.CENTER);
        }

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Lista Elementi Selezionabili
        initLista();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Impostazione Visibilità Loading Image
        loadingImg.setVisible(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Elementi Lista Selezionabile
        new LeggiLista().start();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 63, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 64, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 60, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 61, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamneto Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 62, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 65, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 66, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Lettura Elementi Lista Selezionabile
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Ricerca Lista Ordinata Clienti
            String[] result = TrovaListaOrdinataClienti(ParametriGlobali.parametri.get(40));

            //Dichiarazione Array Clienti
            clienti = new String[Math.max(elemLabelPlus.length, result.length)];

            //Copia Array
            System.arraycopy(result, 0, clienti, 0, result.length);
            for (int i = 0; i < result.length; i++) {
                clienti[i] = HTML_STRINGA_INIZIO + clienti[i].toUpperCase(Locale.ITALIAN) + HTML_STRINGA_FINE;
            }
            for (int i = result.length; i < elemLabelPlus.length; i++) {
                clienti[i] = "";
            }
            for (int i = 0; i < elemLabelPlus.length; i++) {
                elemLabelPlus[i].setText(clienti[i]);
            }

            //Inizializzazione Testo Text Field
            txtField.setText("");

            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(clienti);
            startThreadControllo();

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(false);

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setText("");
        }

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {
            if (txtField.getText().equals("")) {
                scelte.cliente = ParametriGlobali.parametri.get(40);
            } else {
                scelte.cliente = txtField.getText();
            }
        } else if (button.equals(elemBut[1].getName())) {
            scelte.cliente = ParametriGlobali.parametri.get(40);
        }
        gestoreScambioPannello();

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        //Controllo Possibilità Scambio Componenti
        if (!controlloScambioComponenti()) {

            scelte.serie_colore = TrovaSerieColoreProdotto(scelte.idProdotto);
            scelte.serie_additivo = TrovaSerieAdditivoProdotto(scelte.idProdotto);

            if (scelte.serie_colore.equals(ParametriGlobali.parametri.get(155))
                    ||scelte.serie_colore.equals("")
                || ((TrovaListaOrdinataColoriPerSerieColori(scelte.serie_colore)).length == 0)) {

                if (scelte.serie_additivo.equals(ParametriGlobali.parametri.get(155))
                        ||scelte.serie_additivo.equals("")
                    || (TrovaListaOrdinataAdditivPerSerieAdditivi(scelte.serie_additivo)).length == 0) {
                    
                    ((Pannello10_ScelteEffettuate) pannelliCollegati.get(2)).scelte = scelte;
                    ((Pannello10_ScelteEffettuate) pannelliCollegati.get(2)).initPanel();

                } else {

                    ((Pannello42_SceltaAdditivo) pannelliCollegati.get(4)).scelte = scelte;
                    ((Pannello42_SceltaAdditivo) pannelliCollegati.get(4)).initPanel();
                }

            } else {

                ((Pannello41_SceltaColore) pannelliCollegati.get(3)).scelte = scelte;
                ((Pannello41_SceltaColore) pannelliCollegati.get(3)).initPanel();

            }

        } else {

            ((Pannello09_SceltaCambioComponenti) pannelliCollegati.get(1)).scelte = scelte;
            ((Pannello09_SceltaCambioComponenti) pannelliCollegati.get(1)).initPanel();
        }

    }

    //Controllo Scambio Componenti
    private boolean controlloScambioComponenti() {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(455))) {
            scelte.compSostitutivi = TrovaComponentiAlternativi(scelte.idProdotto);
        } else {

            scelte.compSostitutivi = new ArrayList<>();
        }
        return scelte.compSostitutivi.size() > 0;

    }
}
