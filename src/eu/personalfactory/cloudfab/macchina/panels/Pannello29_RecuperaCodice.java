package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ApriConnessioneMySql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ChiudiConnessioneMySql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RipristinaTuttiCodiciInventariati;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiCodiciChimicaRipristinabiliInventario;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello29_RecuperaCodice extends MyStepPanel {

    public String[] codiciRipristinabili;
    //COSTRUTTORE

    public Pannello29_RecuperaCodice() {

        super();

        setLayer();
    }

    //Dichirazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(1);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(1);
        impostaColori(4);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);
        
        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[3]);

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
        for (int i = 0; i < elemLabelSimple.length; i++) {
            elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);
             
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

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 698, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 699, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 702, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 703, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);
                 
                //Aggiornamento Label Tipo Help
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 720, ParametriSingolaMacchina.parametri.get(111)));
               
                

            }

        }
    }

    //Lettura Elementi Lista Selezionabile
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Ricerca Lista Ordinata Clienti
            ArrayList<String> result = TrovaTuttiCodiciChimicaRipristinabiliInventario();

            //Dichiarazione Array Clienti
            codiciRipristinabili = new String[Math.max(elemLabelPlus.length, result.size())];
 
            //Copia Array 
            for (int i = 0; i < result.size(); i++) {
                codiciRipristinabili[i] = result.get(i).toUpperCase(Locale.ITALIAN);
            }
            for (int i = result.size(); i < elemLabelPlus.length; i++) {
                codiciRipristinabili[i] = "";
            }
            for (int i = 0; i < elemLabelPlus.length; i++) {
                elemLabelPlus[i].setText(codiciRipristinabili[i]);
            }

            //Inizializzazione Testo Text Field
            txtField.setText("");

            definisciColoriLista(elemColor[2], elemColor[2], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(codiciRipristinabili);
            startThreadControllo();

            //Impostazione Visibilità Loading Image
            loadingImg.setVisible(false);

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String codiceSelezionato) {

        if (codiceSelezionato.equals("0")) {

            ripristinaTuttiCodici();
            gestoreScambioPannello("");
        } else {
            gestoreScambioPannello(codiceSelezionato);
        }
 
    }
    //Gestione Scambio Pannelli Collegati

    public void gestoreScambioPannello(String selezione) {

        this.setVisible(false);

        ((Pannello30_ConfermaRecuperaCodice) pannelliCollegati.get(1)).initPanel(selezione);

    }

    public void ripristinaTuttiCodici() {

        Connection connessione = ApriConnessioneMySql();

        //Aggiornamento Stato Chimica
        RipristinaTuttiCodiciInventariati(connessione);

        //Chiusura Connessione
        ChiudiConnessioneMySql(connessione);

    }
}
