package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciConfezioniProdottePerCliente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello25_Ricerca_Codici_Per_Cliente extends MyStepPanel {

    //VARIABILI
    private String selezione_prodotto, selezione_cliente;

    //COSTRUTTORE
    public Pannello25_Ricerca_Codici_Per_Cliente() { 
        
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

        //Inizializzazione Colori Label Tipo Help
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colori Label Tipo Title
        initColorLabelTitle(elemColor[4]);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Text Field
        inserisciTextField();

        //Configurazione di Base Pannello
        configuraPannello();

        //Avvio Thread di Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();
    }

    //Inizializzazione Pannello
    public void initPanel(String selezione_prodotto, String selezione_cliente) {

        //Inizializzazione Variabili
        this.selezione_prodotto = selezione_prodotto;
        this.selezione_cliente = selezione_cliente;

        //Inizializzazione Testo Text Field
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Impostazione Visibilità Tastiera
        tastiera.impostaVisibilitaTastiera(true);

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(false);

        //Inizializzazione Elementi Lista Selezionabile
        initLista();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Aggiornamento Lista Selezionabile
        new LeggiLista().start();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testi Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 590, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 589, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testi Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 591, ParametriSingolaMacchina.parametri.get(111)));
            }

        }
    }

    //Lettura Elementi Lista Selezionabile da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Ricerca Lista Ordinata Codici Sacco per Cliente
            String result[] = TrovaListaOrdinataCodiciConfezioniProdottePerCliente(
                    selezione_prodotto, 
                    selezione_cliente,
                    ParametriSingolaMacchina.parametri.get(111));

            //Dichiarazione Array
            String[] lista = new String[Math.max(elemLabelPlus.length, result.length)];

            //Copia Lista Ordinata dei Codici nell'Array
            System.arraycopy(result, 0, lista, 0, result.length);

            //Inizializzaione Elementi Lista Selezionabile Vuoti
            for (int i = result.length; i < elemLabelPlus.length; i++) {
                lista[i] = "";
            }

            //Aggiornamento Testi Lista Selezionabile
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

    //Aggiornamento Elementi Lista Selezionabile
    public void initLista() {

        //Inizializzazione Testi Elementi Label Plus
        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(String selezione) {

        this.setVisible(false);

        ((Pannello26_Ricerca_Dettagli) pannelliCollegati.get(1)).initPanel(
                selezione, 2);


    }
}
