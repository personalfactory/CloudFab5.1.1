package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConteggioOrdiniNonEvasi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RipulisciTabellaValoreRipristinoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaOrdini;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaPresenzaValoreParRipristinoAbilitati;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Pannello01_SceltaProduzione extends MyStepPanel {

    //COSTRUTTORE
    public Pannello01_SceltaProduzione() {

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

        //Inizializza Colore Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializza Colore Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializza Colore Label Title     
        initColorLabelTitle(elemColor[2]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Modifica la Visibilità di Default delle Righe di Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializza il Pannello
    public void initPanel() {

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Messaggi da database
        new LeggiDizionario().start();

        //Impostazione Abilitazione Pulsante "RIPRENDI CICLO"
        elemBut[1].setEnabled(VerificaPresenzaValoreParRipristinoAbilitati());

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 9, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 10, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamentop Testo Label Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 12, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 13, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Inserimento label Tipo Progresso
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 14, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 15, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {
            gestoreScambioPannello(pannelliCollegati.get(1));
        }
        if (button.equals(elemBut[1].getName())) {
            gestoreScambioPannello(pannelliCollegati.get(2));
        }

    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello(MyStepPanel pannelloAvanti) {

        if (pannelloAvanti instanceof Pannello02_SceltaFiltro) {

            //Reset Tabella Valori di Ripristino
            RipulisciTabellaValoreRipristinoOri();

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(379))) {

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(381))) {

                    //RICERCA ORDINI INEVASI 
                    if (ConteggioOrdiniNonEvasi() > 0) {

                        //Visualizzazione Errore e Gestione Scelta Operatorex
                        ((Pannello44_Errori) this.pannelliCollegati.get(3)).gestoreErrori.visualizzaErrore(39);

                    }
                }

                this.setVisible(false);

                //RICERCA ORDINI DA EVADERE
                ArrayList<ArrayList<ArrayList<String>>> ordini = TrovaOrdini(Integer.parseInt(ParametriSingolaMacchina.parametri.get(382)));

                ///////////////////////
                // ORDINI CARICATI  ///
                ///////////////////////
                ((Pannello03_Ordini) pannelliCollegati.get(4)).initPanel(ordini);

            } else {

                this.setVisible(false);

                //////////////////////////////
                // NESSUN ORDINE CARICATO  ///
                //////////////////////////////
                ((Pannello02_SceltaFiltro) pannelliCollegati.get(1)).initPanel();

            }
        } else if (pannelloAvanti instanceof Pannello11_Processo) {
        	
            this.setVisible(false);

            AggiornaValoreParametroRipristino(
                    46,
                    "false",
                    ParametriSingolaMacchina.parametri.get(15));

            ((Pannello11_Processo) pannelliCollegati.get(2)).ripristinato = true;
            ((Pannello11_Processo) pannelliCollegati.get(2)).initPanel();
        }

    }
}
