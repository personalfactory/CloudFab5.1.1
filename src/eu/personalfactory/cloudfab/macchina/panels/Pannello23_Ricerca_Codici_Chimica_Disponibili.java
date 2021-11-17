package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerNomeProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciChimicaValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello23_Ricerca_Codici_Chimica_Disponibili extends MyStepPanel {

    //VARIABILI
    private String selezione;

    //COSTRUTTORE
    public Pannello23_Ricerca_Codici_Chimica_Disponibili() { 

    
        super();
  
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(0);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(0);
        impostaColori(5);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[4]);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Button Freccia
        inserisciButtonInfo();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Text Field
        inserisciTextField();

        //Configurazione di Base Pannello
        configuraPannello();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();
    }

    //Inizializzazione Pannello
    public void initPanel(String selezione) {

        //Inizializzazione Variabile Selezione Effettuata
        this.selezione = selezione;

        //Inizializzazione Testo Text Field
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Impostazione Visibiltà Tastiera
        tastiera.impostaVisibilitaTastiera(true);

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(false);

        //Inizializzazione Elementi Lista Selezionabile
        initLista();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Aggiornamento Testi Lista Selezionabile
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
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 585, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 584, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 594, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Lettura Elementi Lista Selezionabile da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Ricerca Codici Chimica Validi
            String[] codiciChimica = TrovaCodiciChimicaValidi(TrovaCodiceProdottoPerNomeProdotto(
                    selezione,ParametriSingolaMacchina.parametri.get(111)));

            //Dichiarazione e Assegnazione Codici Chimica Validi alla Lista Selezionabile
            String[] lista = new String[Math.max(elemLabelPlus.length, codiciChimica.length)];
            System.arraycopy(codiciChimica, 0, lista, 0, codiciChimica.length);
            for (int i = codiciChimica.length; i < elemLabelPlus.length; i++) {
                lista[i] = "";
            }

            //Aggiornamento Testi Elementi Lista Selezionabile
            elemLabelPlus[0].setText(lista[0]);
            elemLabelPlus[1].setText(lista[1]);
            elemLabelPlus[2].setText(lista[2]);
            elemLabelPlus[3].setText(lista[3]);
            elemLabelPlus[4].setText(lista[4]);
            elemLabelPlus[5].setText(lista[5]);
            elemLabelPlus[6].setText(lista[6]);
            elemLabelPlus[7].setText(lista[7]);

            txtField.setText("");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(lista);
            startThreadControllo();

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione lElementi Lista Selezionabile
    public void initLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(String selezione) {

        this.setVisible(false);

        ((Pannello26_Ricerca_Dettagli) pannelliCollegati.get(1)).initPanel(
                selezione, 1);

    }
}
