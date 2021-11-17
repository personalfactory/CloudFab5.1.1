package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;

@SuppressWarnings("serial")
public class Pannello07_SceltaTipoContenitore extends MyStepPanel {

    //COSTRUTTORE
    public Pannello07_SceltaTipoContenitore() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(5);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(1);
        impostaDimLabelBut(5);
        impostaColori(3);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[2]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

        //Impostazioni Cursori Pulsanti
        elemLabelSimple[0].setCursor(Cursor.getDefaultCursor());
        elemLabelSimple[1].setCursor(Cursor.getDefaultCursor());

        elemBut[2].setEnabled(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(304)));

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        elemBut[3].setEnabled(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(410)));
        elemBut[4].setEnabled(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(410)));

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
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 56, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 57, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 54, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 55, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testi Button
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 58, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 59, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 763, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 872, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[4].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 872, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 59, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

            }

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        ////        if (button.equals("2")) {
////
////            /////////////////////////////////
////            // RIBALTA SACCO DISABILITATO  //
////            /////////////////////////////////
////            scelte.cambioContenitore = button.equals("0");
////            scelte.disabilitaRibalta = true;
////
////        } else {
////
////            ///////////////////////////////////
////            // SACCO O SECCHIO DISABILITATO  //
////            ///////////////////////////////////
////            scelte.cambioContenitore = button.equals(elemBut[1].getName());
////            scelte.disabilitaRibalta = false;
////        }
////         
        switch (button) {
            case "0":
                //////////////////////
                // SACCO STANDARD  ///
                //////////////////////

                scelte.cambioContenitore = false;
                scelte.disabilitaRibalta = false;
                scelte.cambioBilancia = false;
                break;

            case "1":
                //////////////////
                // SECCHIO STD ///
                //////////////////

                scelte.cambioContenitore = true;
                scelte.disabilitaRibalta = true;
                scelte.cambioBilancia = false;
                break;
            case "2":
                ////////////////////////////
                // RIBALTO DISABILITATO  ///
                ////////////////////////////

                scelte.disabilitaRibalta = true;
                scelte.cambioContenitore = false;
                scelte.cambioBilancia = false;
                break;
            case "3":
                ////////////////////////////////////
                // SACCO A VALVOLA APERTA SACCHI ///
                ////////////////////////////////////
                scelte.disabilitaRibalta = true;
                scelte.cambioContenitore = false;
                scelte.cambioBilancia = true;
                break;
            case "4":
                ////////////////////////////////////
                // SACCO A VALVOLA APERTA SECCHI ///
                ///////////////////////////////////
                scelte.disabilitaRibalta = true;
                scelte.cambioContenitore = true;
                scelte.cambioBilancia = true;
                break;
            default:
                break;
        }

        gestoreScambioPannello();
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(315))) { //&& scelte.numeroMiscele > 1) {

            ((Pannello33_SceltaScaricaValvola) pannelliCollegati.get(2)).scelte = scelte;
            ((Pannello33_SceltaScaricaValvola) pannelliCollegati.get(2)).initPanel();

        } else {

            ((Pannello08_SceltaCliente) pannelliCollegati.get(1)).scelte = scelte;
            ((Pannello08_SceltaCliente) pannelliCollegati.get(1)).initPanel();
        }

    }
}
