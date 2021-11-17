package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataClienti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataDtProduzione;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataFamiglie;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_FAMIGLIE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.Locale;

@SuppressWarnings("serial")
public class Pannello03_FiltroGen extends MyStepPanel {
    //VARIABILI
    //Elementi lista filtrata

    private String[] lista;
    
    //Tipologia di Pannello Clienti (0) - Date(1) - Famiglia(2) **/
    public int panelType;

    //COSTRUTTORE
    public Pannello03_FiltroGen() {

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
        impostaDimLabelProg(1);
        impostaDimLabelBut(0);
        impostaColori(5);

        //Inizializzazione Colore Label Help
        initColorLabelHelp(elemColor[3]);

        //Inizializzazione Colore Label Title
        initColorLabelTitle(elemColor[4]);
 
        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Area di Testo
        inserisciTextField();

        //Configurazione di Base Pannello
        configuraPannello();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();
    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Text Field
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Inizializzazione Label lista
        initLista();

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Elementi della Lista
        new LeggiLista().start();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Inserimento label Tipo Progresso
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 39, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

////                //Inserimento label Tipo Title
////                elemLabelTitle[0].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 26, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Lettura elementi della lista
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            switch (panelType) {

                case 1: {


                    //////////////////////////////////
                    // LISTA ORDINATA DI FAMIGLIE  ///
                    //////////////////////////////////

                    //Aggiornamento label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 33, ParametriSingolaMacchina.parametri.get(111)));
                    elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 35, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento label Tipo Help
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 34, ParametriSingolaMacchina.parametri.get(111)));


                    //Lista Ordinata id Famiglie
                    String[] idFam = TrovaListaOrdinataFamiglie();

                    //Lettura Descrizione Famiglie da Database
                    String[] result = new String[idFam.length];

                    for (int i = 0; i < idFam.length; i++) {
                        result[i] =
                                TrovaVocabolo(ID_DIZIONARIO_FAMIGLIE, 
                                Integer.parseInt(idFam[i]), 
                                ParametriSingolaMacchina.parametri.get(111));

                    }

                    lista = new String[Math.max(elemLabelPlus.length, result.length)];
                    System.arraycopy(result, 0, lista, 0, result.length);
                    for (int i = result.length; i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                    }
                    break;

                }

                case 2: {


                    ////////////////////////////////
                    // LISTA ORDINATA DI CLIENTI ///
                    ////////////////////////////////

                    //Aggiornamento label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 61, ParametriSingolaMacchina.parametri.get(111)));
                    elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 40, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento label Tipo Help
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 38, ParametriSingolaMacchina.parametri.get(111)));


                    //Lista ordinata di clienti
                    String[] result = TrovaListaOrdinataClienti(ParametriGlobali.parametri.get(40));
                    lista = new String[Math.max(elemLabelPlus.length, result.length)];
                    System.arraycopy(result, 0, lista, 0, result.length);
                    for (int i = 0; i < result.length; i++) {
                        lista[i] = HTML_STRINGA_INIZIO + lista[i].toUpperCase(Locale.ITALIAN) + HTML_STRINGA_FINE;
                    }
                    for (int i = result.length; i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                    }

                    break;

                }
                case 3: {


                    ///////////////////////////////////////////
                    // LISTA ORDINATA DI DATE DI PRODUZIONE ///
                    ///////////////////////////////////////////
                    
                    //Aggiornamento label Tipo Title
                    elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 41, ParametriSingolaMacchina.parametri.get(111)));
                    elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 43, ParametriSingolaMacchina.parametri.get(111)));

                    //Aggiornamento label Tipo Help
                    elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 42, ParametriSingolaMacchina.parametri.get(111)));

                    //Lista ordinata di date
                    String[] result = TrovaListaOrdinataDtProduzione(ParametriGlobali.parametri.get(43));
                    lista = new String[Math.max(elemLabelPlus.length, result.length)];
                    System.arraycopy(result, 0, lista, 0, result.length);
                    for (int i = result.length; i < elemLabelPlus.length; i++) {
                        lista[i] = "";
                    }

                    break;

                }
            }


            //Aggiornamento label Tipo Lista
            elemLabelPlus[0].setText(lista[0]);
            elemLabelPlus[1].setText(lista[1]);
            elemLabelPlus[2].setText(lista[2]);
            elemLabelPlus[3].setText(lista[3]);
            elemLabelPlus[4].setText(lista[4]);
            elemLabelPlus[5].setText(lista[5]);
            elemLabelPlus[6].setText(lista[6]);
            elemLabelPlus[7].setText(lista[7]);


            //Dichiarazione lista selezionabile
            txtField.setText("");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(lista);
            startThreadControllo();

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Label lista
    public void initLista() {

        //Inizializzazione Label lista
        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        ((Pannello03_FiltroGen_Alter) pannelliCollegati.get(1)).scelte = scelte;
        ((Pannello03_FiltroGen_Alter) pannelliCollegati.get(1)).initPanel();
        ((Pannello03_FiltroGen_Alter) pannelliCollegati.get(1)).panelType = panelType;

    }
}
