package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaStatoCodiciChimicaInventariatiPerCodiceChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ApriConnessioneMySql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ChiudiConnessioneMySql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciChimicaPerCodiceLotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiCodiciChimicaValidiInventario;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaCodiceChimicaSfusa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SCROLL_SENSIBILITA_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_TEXT_FIELD;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudFont;
import eu.personalfactory.cloudfab.macchina.utility.GestoreScrollLista;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JTextField;
import org.jdom.Element;

@SuppressWarnings("serial")
public class Pannello28_Inventario extends MyStepPanel {

    private JTextField txtFieldInventario = new JTextField();
    private ArrayList<String> codici;
    private int indice = 0;

    //COSTRUTTORE
    public Pannello28_Inventario() {

        super();

        setLayer();

    }

    //Dichirazioni Pannello
    private void setLayer() {

       //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(12);
        impostaDimLabelSimple(3);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(3);
        impostaColori(3);

        //Avvio Thread Caricamento Immagine di Caricamento
        new ThreadInserisciLoadingImage(this).start();

        //Configurazione di Base Pannello
        configuraPannello();

        //Inserimento Area di Testo
        inserisciLocalTextField();

        //Inserimento Tastiera
        inserisciTastiera();

        inserisciButtonInfo();

        tastiera.assegnaTextField(txtFieldInventario);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Title
        initColorLabelSimple(elemColor[2]);


        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione
        codici = new ArrayList<>();
        indice = 0;

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        impostaVisibilitaAiuto(false);

        //Inizializzazione Testo Text Field
        txtFieldInventario.setText("");

        txtFieldInventario.requestFocusInWindow();

        //Inizializzazione Lista Elementi Selezionabili
        initLista();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visiblità Pannello
        setPanelVisibile();

        new ControllaScrollInventario(this).start();

        loadingImg.setVisible(false);

    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText("");
        }

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 697, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 699, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Tipo Title
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 709, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 710, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 711, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 700, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 701, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);
            }
        }
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);
        ((Pannello22_Ricerca_Filtro_Generale) pannelliCollegati.get(1)).initPanel();
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        switch (button) {
            case "0": {

                ////////////////////////
                // INSERISCI CODICE  ///
                ////////////////////////

                aggiungiCodice();

                break;
            }
            case "1": {

                ///////////////////////////
                // CANCELLAZIONE LISTA  ///
                ///////////////////////////

                svuotaLista();

                break;
            }

            case "2": {

                //////////////////////////////
                // AGGIORNAMENTO DATABASE  ///
                //////////////////////////////
                 
 
                //Visualizzazione e Gestione Errore
                if (((Pannello44_Errori) this.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(34) == 0) {

                    loadingImg.setVisible(true);

                    new ThreadAggiornaDatabase().start();
                }
                
                

                loadingImg.setVisible(false);

                break;

            }
        }

    }

    private class ThreadAggiornaDatabase extends Thread {

        @Override
        public void run() {
            sincornizzaCodici();

            svuotaLista();

        }
    }

    private void inserisciLocalTextField() {

        //Lettura chiave file xml 
        Element elm = root.getChild(XML_TEXT_FIELD);

        //Posizionamento e dimenzionameno dei Label 
        txtFieldInventario.setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

        //Settaggio Font 
        txtFieldInventario.setFont(FabCloudFont.setDimensione(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

        //Settaggio Trasparenze 
        txtFieldInventario.setOpaque(false);

        //Settaggio Bordi 
        txtFieldInventario.setBorder(null);

        //Impostazione Nome TextField
        txtFieldInventario.setName("BASETXTFIELD");

        //Text Field
        add(txtFieldInventario);



    }

    private void svuotaLista() {

        for (int i = 0; i < elemLabelPlus.length; i++) {

            elemLabelPlus[i].setText("");

        }

        //Inizializzazione
        codici = new ArrayList<>();
        indice = 0;

    }

    //Aggiungi codice inserito lista
    private void aggiungiCodice() {

        if (!txtFieldInventario.getText().equals("")) {
            codici.add(txtFieldInventario.getText());
            txtFieldInventario.setText("");
            scorriLista(0);
        }


    }

    //Scorre la lista 
    public void scorriLista(int j) {

        if (codici.size() > 0) {
            for (int i = 0; i < elemLabelPlus.length; i++) {

                if (i <= codici.size() - 1 - j) {

                    elemLabelPlus[i].setText(codici.get(codici.size() - 1 - i - j));
                } else {
                    elemLabelPlus[i].setText("");
                }

            }
        }

    }

    //Sincronizzazione dei codici su database
    private void sincornizzaCodici() {
         
        ////////////////////////////
        // RICERCA CODICI LOTTO  ///
        ////////////////////////////
 
        //Lettura da database dei codici con stato =0 o stato =-1
        ArrayList<String> codiciFiltrati = new ArrayList<>();

        for (int i = 0; i < codici.size(); i++) {

            //Individuazione dei codici Lotto
            if (codici.get(i).charAt(0) == ParametriGlobali.parametri.get(15).charAt(0)
                    && codici.get(i).length() == Integer.parseInt(ParametriGlobali.parametri.get(16))) {

                //////////////////////
                // CODICE CHIMICA  ///
                //////////////////////
                codiciFiltrati.add(codici.get(i));
                 
            } else if (codici.get(i).charAt(0) == ParametriGlobali.parametri.get(106).charAt(0)
                    && codici.get(i).length() == Integer.parseInt(ParametriGlobali.parametri.get(107))) {

                ////////////////////
                // CODICE LOTTO  ///
                ////////////////////

                ArrayList<String> codiciLotto = TrovaCodiciChimicaPerCodiceLotto(codici.get(i));

                for (int j = 0; j < codiciLotto.size(); j++) {

                    codiciFiltrati.add(codiciLotto.get(j));


                }
            } else if (codici.get(i).charAt(0) == ParametriGlobali.parametri.get(19).charAt(0)) {

                ////////////////////////////////////
                // VERIFICA CODICE CHIMICA SFUSA ///
                ////////////////////////////////////

                if (VerificaCodiceChimicaSfusa(codici.get(i))) {
                    codiciFiltrati.add(codici.get(i));
                }
            }
        } 

        //////////////////////////////////////
        // LETTURA DEI CODICI DA DATABASE  ///
        //////////////////////////////////////

        //Lettura da database dei codici con stato =0 o stato =-1
        ArrayList<String> codiciValidi = TrovaTuttiCodiciChimicaValidiInventario();
 
        //Lettura da database dei codici con stato =0 o stato =-1
        ArrayList<String> codiciDaEliminare = new ArrayList<>();

        for (int i = 0; i < codiciValidi.size(); i++) {
            boolean trovato = false;
            for (int j = 0; j < codiciFiltrati.size(); j++) {
                if (codiciValidi.get(i).equals(codiciFiltrati.get(j))) {
                    trovato = true;
                    break;

                }
            }
            if (!trovato) {
                codiciDaEliminare.add(codiciValidi.get(i));

            }
        } 
         
        //////////////////////////////////////////////
        // AGGIORNAMENTO TABELLA CHIMICA DATABASE  ///
        //////////////////////////////////////////////

        //Apertura Connessione
        Connection connessione = ApriConnessioneMySql();
        
         
        for (int i = 0; i < codiciDaEliminare.size(); i++) {

            //Aggiornamento Stato Chimica
            AggiornaStatoCodiciChimicaInventariatiPerCodiceChimica(connessione, codiciDaEliminare.get(i), ParametriGlobali.parametri.get(105));

        }

        //Chiusura Connessione
        ChiudiConnessioneMySql(connessione);


    }

    private class ControllaScrollInventario extends Thread {

        //VARIABILI
        private Pannello28_Inventario pannello;
        private final int FREQ = 50;

        public ControllaScrollInventario(Pannello28_Inventario pannello) {
            this.pannello = pannello;
        }

        @Override
        public void run() {

            //scorriLista(indice);

            GestoreScrollLista scrollEvent = new GestoreScrollLista();


            for (int i = 0; i < elemLabelPlus.length; i++) {
                pannello.elemLabelPlus[i].addMouseMotionListener(scrollEvent);
            }
            while (pannello.isVisible()) {

                //Temporizzatore Thread
                try {
                    ControllaScrollInventario.sleep(FREQ);
                } catch (InterruptedException ex) {
                }

                ///////////////////////////
                // SCROLLING VERTICALE  ///
                ///////////////////////////˙

                if (scrollEvent.isYDraggedDW()) {

                    if (scrollEvent.getDy() > SCROLL_SENSIBILITA_Y) {
                        scrollEvent.setDy(0);
                        if (indice < codici.size() - 1) {
                            indice++;
                            scorriLista(indice);
                        }
                    }
                } else if (scrollEvent.isYDraggedUP()) {
                    if (scrollEvent.getDy() > SCROLL_SENSIBILITA_Y) {
                        scrollEvent.setDy(0);
                        if (indice > 0) {
                            indice--;
                        }
                        scorriLista(indice);
                    }
                }

            }

        }
    }
}
