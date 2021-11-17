package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdProdottoByCodice;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataCodiciProdottoPerCliente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello24_Ricerca_Prodotti_Per_Cliente extends MyStepPanel {

    //VARIABILI
    private String selezione;

    //COSTRUTTORE
    public Pannello24_Ricerca_Prodotti_Per_Cliente() { 
        
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

        //Aggiornamento Testi Label Tipo Help
        initColorLabelHelp(elemColor[3]);

        ///Aggiornamento Testi Label Tipo Title
        initColorLabelTitle(elemColor[4]);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Area di Testo
        inserisciTextField();

        // Configurazione di Base Pannello
        configuraPannello();

////        //Creazione Pannelli Collegati
////        impostaDimLinkedPanel(1);
////        pannelliCollegati[0] = new Pannello36_Ricerca_Codici_Per_Cliente(pannelloDietro, pannelloAvanti);

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel(String selezione) {

        //Inizializzazione Variabile Controllo Selezione
        this.selezione = selezione;

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

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

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
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 587, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 586, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testi Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 588, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Lettura Elementi Lista Selezionabile da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Ricerca Lista Ordinata Codici Prodotto
            String result[] = TrovaListaOrdinataCodiciProdottoPerCliente(selezione);

            String[] codiciProdotto = new String[Math.max(elemLabelPlus.length, result.length)];
            System.arraycopy(result, 0, codiciProdotto, 0, result.length);

            //Inizializzazione Elementi Lista Selezionabile Vuoti
            for (int i = result.length; i < elemLabelPlus.length; i++) {
                codiciProdotto[i] = "";
            }

            //Ricerca Id Prodotto
            int idProdotto[] = new int[codiciProdotto.length];
            for (int i = 0; i < codiciProdotto.length; i++) {
                idProdotto[i] = TrovaIdProdottoByCodice(codiciProdotto[i]);

            }

            //Dichiarazione Array Nome Prodotti
            String[] nomiProdotto = new String[codiciProdotto.length];

            //Ricerca Nome Prodotti dal Dizionario
            for (int i = 0; i < idProdotto.length; i++) {
                nomiProdotto[i] = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, idProdotto[i], ParametriSingolaMacchina.parametri.get(111));
            }

            //Dichiarazione e Assegnazione Nomi Prodotto alla Lista
            String[] lista = new String[Math.max(elemLabelPlus.length, nomiProdotto.length)];
            System.arraycopy(nomiProdotto, 0, lista, 0, nomiProdotto.length);
            for (int i = nomiProdotto.length; i < elemLabelPlus.length; i++) {
                lista[i] = "";
            }

            //Aggiornamento Testi Lista Selezionabili
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

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        //Aggiornamento Testi Label Tipo Plus
        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(String prodotto) {

        this.setVisible(false);

        ((Pannello25_Ricerca_Codici_Per_Cliente) pannelliCollegati.get(1)).initPanel(EstraiStringaHtml(prodotto), selezione);

    }
}
