package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataAdditivPerSerieAdditivi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello42_SceltaAdditivo extends MyStepPanel {

    //VARIABILI
    private String[] additivi;

    //COSTRUTTORE
    public Pannello42_SceltaAdditivo() {

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
        impostaDimLabelTitle(2);
        impostaDimLabelProg(1);
        impostaDimLabelBut(1);
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
         
        inizializzaScelte();
          
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
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 991, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 992, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 989, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 990, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamneto Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 91, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti 
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 66, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Lettura Elementi Lista Selezionabile
    private class LeggiLista extends Thread {

        @Override
        public void run() {

           
            //Ricerca Lista Ordinata Clienti
            String[] result = TrovaListaOrdinataAdditivPerSerieAdditivi(scelte.serie_additivo);

            //Dichiarazione Array Clienti
            additivi = new String[Math.max(elemLabelPlus.length, result.length)];

            //Copia Array
            System.arraycopy(result, 0, additivi, 0, result.length);
            for (int i = 0; i < result.length; i++) {
                additivi[i] = HTML_STRINGA_INIZIO + additivi[i].toUpperCase(Locale.ITALIAN) + HTML_STRINGA_FINE;
            }
            for (int i = result.length; i < elemLabelPlus.length; i++) {
                additivi[i] = "";
            }
            for (int i = 0; i < elemLabelPlus.length; i++) {
                elemLabelPlus[i].setText(additivi[i]);
            }

            //Inizializzazione Testo Text Field
            txtField.setText("");

            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(additivi);
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

        inizializzaScelte();
        gestoreScambioPannello();

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        if (!scelte.nomeAdditivoSelezionato.equals("")) {

            scelte.idAdditivo = TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_PRODOTTI, EstraiStringaHtml(scelte.nomeAdditivoSelezionato), ParametriSingolaMacchina.parametri.get(111));
 
        }
        ((Pannello10_ScelteEffettuate) pannelliCollegati.get(1)).scelte = scelte;
        ((Pannello10_ScelteEffettuate) pannelliCollegati.get(1)).initPanel();
         
    }

    public void inizializzaScelte() {
        
        scelte.idAdditivo = 0;
        scelte.additivato = false; 
        scelte.nomeAdditivoSelezionato = ""; 
////        scelte.serie_additivo = new ArrayList();
    }

}
