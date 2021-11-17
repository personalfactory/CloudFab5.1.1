package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello16_Controllo extends MyStepPanel {

    //VARIABILI
    public int codType;

    //COSTRUTTORE
    public Pannello16_Controllo() {
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
        impostaDimLabelProg(0);
        impostaDimLabelBut(2);
        impostaColori(3);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[2]);

        //Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);
 
        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();
        
        //Impostazione Allineamento Label Tipo Simple
        elemLabelSimple[0].setHorizontalAlignment(SwingConstants.CENTER);
        elemLabelSimple[1].setHorizontalAlignment(SwingConstants.CENTER);
        
        //Impostazione Allineamento Label Tipo Simple
        elemLabelSimple[0].setVerticalAlignment(SwingConstants.CENTER);
        elemLabelSimple[1].setVerticalAlignment(SwingConstants.CENTER);


    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);
        

    }

    //Lettura Vocaboli Traducibili da Databse
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornametno Testo Label Tipo Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 453, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 454, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornametno Testo Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 455, ParametriSingolaMacchina.parametri.get(111)))
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 456, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 457, ParametriSingolaMacchina.parametri.get(111)))
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 458, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornametno Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 459, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        gestoreScambioPannello(Integer.parseInt(button));

    }

    //Gestione Scambio Pannelli Collegato
    public void gestoreScambioPannello(int t) {

        this.setVisible(false);

        if (pannelliCollegati.get(1) instanceof Pannello17_Verifiche) {

            ((Pannello17_Verifiche) pannelliCollegati.get(1)).panelType = t;
            ((Pannello17_Verifiche) pannelliCollegati.get(1)).initPanel();

        }
    }
}
