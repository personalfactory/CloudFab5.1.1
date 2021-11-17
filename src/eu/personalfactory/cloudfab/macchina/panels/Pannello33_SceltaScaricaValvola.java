package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello33_SceltaScaricaValvola extends MyStepPanel {

    //COSTRUTTORE
    public Pannello33_SceltaScaricaValvola() { 

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

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[2]);

        //Inserimento Button Freccia
        inserisciButtonFreccia();
 
        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Vel Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

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
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 777, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 778, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 776, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 775, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 773, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 774, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
          
        if (button.equals(elemBut[0].getName())) {
            scelte.abilitaScaricaValvola = false;
        } else if (button.equals(elemBut[1].getName())) {
            scelte.abilitaScaricaValvola = true;
        } 
        gestoreScambioPannello();

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);
 
         
       ((Pannello08_SceltaCliente) pannelliCollegati.get(1)).scelte = scelte;
       ((Pannello08_SceltaCliente) pannelliCollegati.get(1)).initPanel();

    }
}
