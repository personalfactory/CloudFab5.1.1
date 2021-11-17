package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParComponenteOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdComponentiTabellaComponentiPesatura;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdPresaPerPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiComponentiPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiIdComponenti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Pannello15_Configurazione_Prese extends MyStepPanel {

    //VARIABILI
    private String[] componenti;
    private int idPresa;
    private ArrayList<Integer> compIdPresaArray;

    //COSTRUTTORE
    public Pannello15_Configurazione_Prese() {

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
        impostaDimLabelTitle(3);
        impostaDimLabelProg(0);
        impostaDimLabelBut(0);
        impostaColori(6);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[3]);

        //Inizializzazione Colori Label Help **/
        initColorLabelHelp(elemColor[4]);

        //Inizializzazione Colori Label Title **/
        initColorLabelTitle(elemColor[5]);

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

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Text Field
        txtField.setText("");

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Inizializzazione Elementi Lista Selezionabile
        initLista();

        //Inserimento Action Listener Controllo Selezione
        inserisciControllaSelezione();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Prese Disponibili
        new LeggiLista().start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();
        
       
    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 327, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 328, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 329, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Lettura Informazioni Prese da Database
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            //Ricerca Componenti Assegnati alla Presa
            compIdPresaArray = TrovaTuttiComponentiPerIdPresa(idPresa);

            if (compIdPresaArray.isEmpty()) {

                //////////////////////////////////////////////
                // NESSUN COMPONENTE ASSEGNATO ALLA PRESA  ///
                //////////////////////////////////////////////
                //Inizializzazione Nome Componente Valore di Default
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 331, ParametriSingolaMacchina.parametri.get(111)));

            } else if (compIdPresaArray.size() == 1) {

                //////////////////////////////////////////
                // UN COMPONENTE ASSEGNATO ALLA PRESA  ///
                //////////////////////////////////////////
                //Assegnazione None Componenten al Label
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compIdPresaArray.get(0), ParametriSingolaMacchina.parametri.get(111)));

            } else if (compIdPresaArray.size() > 1) {

                /////////////////////////////////////////////////
                // PIU DI UN COMPONENTE ASSEGNATO ALLA PRESA  ///
                /////////////////////////////////////////////////
                //Cancellazione Componenti Assegnati dopo il Primo
                for (int i = 1; i < compIdPresaArray.size(); i++) {

                    AggiornaValoreParComponenteOri(
                            1,
                            compIdPresaArray.get(i),
                            ParametriGlobali.parametri.get(64));

                }

            }

            //Ricerca Componenti nei Prodotti
            //Ricerca id Componenti
            ArrayList<Integer> compProdottiArray = TrovaIdComponentiTabellaComponentiPesatura();
  
            //Ricerca id Componenti
            ArrayList<Integer> compArray = TrovaTuttiIdComponenti();

            //Dichiarazione Array Componenti
            componenti = new String[Math.max(elemLabelPlus.length, compArray.size())];

            int j = 0;
            //Lettura Nomi Componenti dal Dizionario in Funzione dell'Id
            for (int i = 0; i < componenti.length; i++) {

                if (i < compArray.size()) {

                    for (Integer compProdottiArray1 : compProdottiArray) {
                        if (compArray.get(i).equals(compProdottiArray1)) {
                            componenti[j] = TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compArray.get(i), ParametriSingolaMacchina.parametri.get(111));
                            j++;
                            break;
                        }
                    }
                } 

            }
            
            //Inizializzazione Array 
            for (int i=j; i<componenti.length; i++){
            componenti[i] = "";
            
            }

            //Inizializzazione Testo Elementi Lista Selezionabile
            elemLabelPlus[0].setText(componenti[0]);
            elemLabelPlus[1].setText(componenti[1]);
            elemLabelPlus[2].setText(componenti[2]);
            elemLabelPlus[3].setText(componenti[3]);
            elemLabelPlus[4].setText(componenti[4]);
            elemLabelPlus[5].setText(componenti[5]);
            elemLabelPlus[6].setText(componenti[6]);
            elemLabelPlus[7].setText(componenti[7]);

            txtField.setText("");
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 0);
            definisciLista(componenti);
            startThreadControllo();

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
            elemLabelPlus[i].setVisible(true);
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello(String select) {

        //Aggiornamento Parametri Database in Funzione della Scelta
        if (!compIdPresaArray.isEmpty()) {

            AggiornaValoreParComponenteOri(
                    1,
                    compIdPresaArray.get(0),
                    ParametriGlobali.parametri.get(64));

        }
        AggiornaValoreParComponenteOri(1,
                TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_COMPONENTI,
                        EstraiStringaHtml(select),
                        ParametriSingolaMacchina.parametri.get(111)),
                Integer.toString(idPresa));

        this.setVisible(false);

        //Controllo Registrazione Codice Materie Prime Abilitato
        if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(209))) {

            ///////////////////////////////////////////
            // REGISTRAZIONE MATERIE NON ABILITATA  ///
            ///////////////////////////////////////////
            ((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).initPanel(((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).editingProcesso);

        } else {

            ///////////////////////////////////////
            // REGISTRAZIONE MATERIE ABILITATA  ///
            ///////////////////////////////////////
            ((Pannello27_Configurazione_MateriePrime) pannelliCollegati.get(1)).initPanel(select);

        }

    }

    //Impostazione Presa Selezionata
    public void impostaPresa(String p) {

        idPresa = TrovaIdPresaPerPresa(p);
        elemLabelTitle[2].setText(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 330, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + p
                + HTML_STRINGA_FINE);

    }
}
