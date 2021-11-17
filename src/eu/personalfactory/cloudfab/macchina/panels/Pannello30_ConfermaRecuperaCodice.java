package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaStatoCodiciChimicaInventariatiPerCodiceChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaStatoCodiciChimicaInventariatiPerCodiceLotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ApriConnessioneMySql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ChiudiConnessioneMySql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.sql.Connection;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello30_ConfermaRecuperaCodice extends MyStepPanel {

    public String[] codiciRipristinabili;
    private String codiceSelezionato;
    //COSTRUTTORE

    public Pannello30_ConfermaRecuperaCodice() {

        super();

        setLayer();
    }

    //Dichirazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(3);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(2);
        impostaColori(2);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelSimple(elemColor[1]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Allineamento Label
        for (int i = 0; i < elemLabelSimple.length; i++) {
            elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);
        }

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel(String codiceSelezionato) {

        this.codiceSelezionato = codiceSelezionato;

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);
        if (!codiceSelezionato.equals("")) {

            elemLabelSimple[2].setText(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 713, ParametriSingolaMacchina.parametri.get(111)))
                    + HTML_BREAK_LINE
                    + codiceSelezionato
                    + HTML_BREAK_LINE
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 714, ParametriSingolaMacchina.parametri.get(111)))
                    + HTML_STRINGA_FINE);
        } else {
            elemLabelSimple[2].setText(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 719, ParametriSingolaMacchina.parametri.get(111)))
                    + HTML_STRINGA_FINE);
        }

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 712, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 715, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 716, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String buttonName) {
        switch (buttonName) {
            case "0": {

                ///////////////
                // ANNULLA  ///
                ///////////////
                //Scambio Pannello
                gestoreScambioPannello();

                break;
            }
            case "1": {

                ////////////////
                // CONFERMA  ///
                ////////////////
                
                //////////////////////////////////////////////////////////
                // AGGIORNAMENTO STATO CODICE SELEZIONATO SU DATABASE  ///
                //////////////////////////////////////////////////////////
                //Apertura Connessione
                Connection connessione = ApriConnessioneMySql();

                if (codiceSelezionato.length()>0 && codiceSelezionato.charAt(0) == ParametriGlobali.parametri.get(106).charAt(0)
                        && codiceSelezionato.length() == Integer.parseInt(ParametriGlobali.parametri.get(107))) {

                    ///////////////////////////////////////
                    // RIPRISTINO SINGOLO CODICE LOTTO  ///
                    /////////////////////////////////////// 
                    //Aggiornamento Stato Chimica
                    AggiornaStatoCodiciChimicaInventariatiPerCodiceLotto(connessione, codiceSelezionato, "0");

                } else {

                    /////////////////////////////////////////
                    // RIPRISTINO SINGOLO CODICE CHIMICA  ///
                    /////////////////////////////////////////
                    //Aggiornamento Stato Chimica
                    AggiornaStatoCodiciChimicaInventariatiPerCodiceChimica(connessione, codiceSelezionato, "0");

                }

                //Chiusura Connessione
                ChiudiConnessioneMySql(connessione);

                //Scambio Pannello
                gestoreScambioPannello();

                break;
            }
        }
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        ((Pannello29_RecuperaCodice) pannelliCollegati.get(0)).initPanel();

    }
}
