package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello21_Ricerca extends MyStepPanel {

    //COSTRUTTORE
    public Pannello21_Ricerca() {

        super();

        setLayer();
    }

    //Dichirazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(6);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(6);
        impostaColori(2);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Configurazione di Base Pannello
        configuraPannello();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visiblità Pannello
        setPanelVisibile();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 533, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 697, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Tipo Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 534, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 535, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 536, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 537, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 695, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 696, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {


        MyStepPanel pannelloSuccessivo = this;

        if (button.equals(elemBut[0].getName())) {

            pannelloSuccessivo = pannelliCollegati.get(1);
            ((Pannello22_Ricerca_Filtro_Generale) pannelloSuccessivo).setPanelType(0);

        } else if (button.equals(elemBut[1].getName())) {

            pannelloSuccessivo = pannelliCollegati.get(1);
            ((Pannello22_Ricerca_Filtro_Generale) pannelloSuccessivo).setPanelType(2);

        } else if (button.equals(elemBut[2].getName())) {

            pannelloSuccessivo = pannelliCollegati.get(1);
            ((Pannello22_Ricerca_Filtro_Generale) pannelloSuccessivo).setPanelType(1);

        } else if (button.equals(elemBut[3].getName())) {

            pannelloSuccessivo = pannelliCollegati.get(1);
            ((Pannello22_Ricerca_Filtro_Generale) pannelloSuccessivo).setPanelType(3);

        } else if (button.equals(elemBut[4].getName())) {

            pannelloSuccessivo = pannelliCollegati.get(2);

        } else if (button.equals(elemBut[5].getName())) {

            pannelloSuccessivo = pannelliCollegati.get(3);

        }

        //Scambio Pannello
        gestoreScambioPannello(pannelloSuccessivo);

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(MyStepPanel pannelloSuccessivo) {

        //Impostazione Visibilità Pannello Corrente
        this.setVisible(false);
        
        if (pannelloSuccessivo instanceof Pannello22_Ricerca_Filtro_Generale) {
            ((Pannello22_Ricerca_Filtro_Generale) pannelloSuccessivo).initPanel();
        } else if (pannelloSuccessivo instanceof Pannello28_Inventario) {
            ((Pannello28_Inventario) pannelloSuccessivo).initPanel();
        } else if (pannelloSuccessivo instanceof Pannello29_RecuperaCodice) {
            ((Pannello29_RecuperaCodice) pannelloSuccessivo).initPanel();
        }
    }
}
