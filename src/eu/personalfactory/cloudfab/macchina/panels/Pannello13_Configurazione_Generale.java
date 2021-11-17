package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParSingMacOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDatiMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdLinguaPerNomeLingua;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaLingueDisponibili;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPrese;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPreseAbilitatePerLaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_INTERLINEA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello13_Configurazione_Generale extends MyStepPanel {

    //PARAMETRI PANNELLO
    public final int numImage = 2;
    //VARIABILI
    private String msgParametriMacchina;
    private ArrayList<String> lingue, prese;
    private int index, iPresa;
    public boolean editingProcesso;

    //COSTRUTTORE
    public Pannello13_Configurazione_Generale() {
        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(27);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(0);
        impostaDimLabelBut(29);
        impostaColori(2);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Defizione Caratteristiche Label Simple
        new DefinisciCaratteristicheLabel().start();

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

    }

    //Definizione Caratteristiche Label Simple
    private class DefinisciCaratteristicheLabel extends Thread {

        @Override
        public void run() {

            for (int i = 0; i < elemLabelSimple.length; i++) {

                elemLabelSimple[i].setVerticalAlignment(SwingConstants.CENTER);

            }

            for (int i = 5; i < 11; i++) {

                elemBut[i].setHorizontalAlignment(SwingConstants.LEFT);
                elemBut[i].setVerticalAlignment(SwingConstants.NORTH);
            }

            elemLabelSimple[5].setVerticalAlignment(SwingConstants.NORTH);
            elemLabelSimple[5].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            elemLabelSimple[13].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            for (int i = 0; i < 12; i++) {
                elemLabelSimple[14 + i].setVerticalAlignment(SwingConstants.CENTER);
                elemLabelSimple[14 + i].setHorizontalAlignment(SwingConstants.CENTER);
            }

        }
    }

    //Inizializzazione pannello
    public void initPanel(Boolean editingProcesso) {

        this.editingProcesso = editingProcesso;

        if (!editingProcesso) {

            index = 0;

            prese = TrovaPrese();

            new LeggiDizionario().start();

            new LeggiParametriSingolaMacchina().start();

            butFreccia.setVisible(true);

            aggiornaAbilitaPulsantiEdintigPrese(true);

            ////////////////////////////////////////// Modificato  CloudFab3.2.0
            modificaPannello(index);

            butFreccia.setVisible(true);

            setPanelVisibile();
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } else {

            //Inserito in CloudFab3.2.0
            index = 1;

            //Apertura pannelo carico/scarico Silos
            gestoreScambioPannello(8);
        }
    }

    //Lettura vocaboli traducibili da database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testi Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 257, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 258, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testi Label Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 258, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 851, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 260, ParametriSingolaMacchina.parametri.get(111)));

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(410))) {

                    elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 876, ParametriSingolaMacchina.parametri.get(111)));

                } else {
                    elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 261, ParametriSingolaMacchina.parametri.get(111)));
                }
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 721, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[12].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 280, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[13].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 281, ParametriSingolaMacchina.parametri.get(111)));
   
                elemLabelSimple[26].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 625, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Lettura valori parametri singola macchina
    private class LeggiParametriSingolaMacchina extends Thread {

        @Override
        public void run() {

            ArrayList<String> mess = new ArrayList<>();

            mess.add(ParametriSingolaMacchina.parametri.get(1));
            //mess.add(ParametriSingolaMacchina.parametri.get(2));
            mess.add(Integer.toString(TrovaPreseAbilitatePerLaMacchina().size()));
            mess.add(ParametriSingolaMacchina.parametri.get(15));
            mess.add(ParametriSingolaMacchina.parametri.get(16));
            mess.add(ParametriSingolaMacchina.parametri.get(18));

            ArrayList<String> datiMacchinaList = TrovaDatiMacchina();

            if (datiMacchinaList.isEmpty()) {

                datiMacchinaList.add("-");
                datiMacchinaList.add("-");
                datiMacchinaList.add("-");
                datiMacchinaList.add("-");
                datiMacchinaList.add("-");

            }

            msgParametriMacchina
                    = HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 263, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + mess.get(0)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 264, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + mess.get(1)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 265, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + mess.get(2)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 266, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + mess.get(3)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 267, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + mess.get(4)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 268, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + ParametriSingolaMacchina.parametri.get(211)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 269, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + datiMacchinaList.get(0)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 270, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + datiMacchinaList.get(1)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 271, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + datiMacchinaList.get(2)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 272, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + datiMacchinaList.get(3)
                    + HTML_INTERLINEA
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 273, ParametriSingolaMacchina.parametri.get(111)))
                    + " " + ParametriGlobali.parametri.get(46) + " "
                    + datiMacchinaList.get(4);

            if (!DettagliSessione.getCodiceFigura().equals("")) {
                msgParametriMacchina
                        += HTML_INTERLINEA
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 624, ParametriSingolaMacchina.parametri.get(111)))
                        + " " + ParametriGlobali.parametri.get(46) + " "
                        + DettagliSessione.getCodiceFigura()
                        + HTML_STRINGA_FINE;
            }

            msgParametriMacchina += HTML_STRINGA_FINE;

            lingue = TrovaLingueDisponibili();

            aggiornaLabelLingue();

            elemLabelSimple[5].setText(msgParametriMacchina);

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
        
        switch (button) {

            case "4":

                /////////////////////
                // MICRODOSATURA  ///
                /////////////////////
                if (Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)) > 0) {

                    //////////////////////////////////////////
                    // ALMENO UN MICRODOSATORE INSTALLATO  ///
                    //////////////////////////////////////////
                    gestoreScambioPannello(7);
                }
                break;

            case "28":
                gestoreScambioPannello(6);
                break;
            case "26":
                if (iPresa > 0) {

                    iPresa--;
                    modificaTestoPulsanti(iPresa);

                }
                break;
            case "27":
                if (iPresa < prese.size() - 12) {

                    iPresa++;
                    modificaTestoPulsanti(iPresa);

                }
                break;
            case "11":
                if (index > 0) {

                    index--;
                    aggiornaLabelLingue();

                }
                break;
            case "12":
                if (index < lingue.size() - 6) {

                    index++;
                    aggiornaLabelLingue();

                }
                break;
            case "14":
            case "15":
            case "16":
            case "17":
            case "18":
            case "19":
            case "20":
            case "21":
            case "22":
            case "23":
            case "24":
            case "25":
                ((Pannello15_Configurazione_Prese) pannelliCollegati.get(2)).impostaPresa(
                        elemLabelSimple[Integer.parseInt(button)].getText());
                gestoreScambioPannello(2);
                break;
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
                if (!elemLabelSimple[Integer.parseInt(button) + 1].getText().equals("")) {

                    if (!TrovaIdLinguaPerNomeLingua(EstraiStringaHtml(elemLabelSimple[Integer.parseInt(button) + 1].getText())).equals("0")) {

                        AggiornaValoreParSingMacOri(111, TrovaIdLinguaPerNomeLingua(EstraiStringaHtml(elemLabelSimple[Integer.parseInt(button) + 1].getText())));

                        ParametriSingolaMacchina.init();
                        gestoreScambioPannello(0);
                    }

                }
                break;
            case "13":
                ParametriSingolaMacchina.init();
                gestoreScambioPannello(0);
                break;
            case "1":
                //Inserito in CloudFab3.2.0 
                //Apertura pannelo carico/scarico Silos
                gestoreScambioPannello(8);
                break;
            default:
                modificaPannello(Integer.parseInt(button));
                break;
        }
    }

    //Aggiornamento Label Lingue Disponibili
    public void aggiornaLabelLingue() {

        for (int i = 0; i < lingue.size(); i++) {

            if (i < 6) {
                elemLabelSimple[6 + i].setText(lingue.get(index + i));
            }

        }
    }

    //Aggiornamento del pannello
    public void modificaPannello(int i) {

        switch (i) {

            case 0: {

                valPulsantiAusiliari(true);
                elemLabelSimple[5].setVisible(true);
                elemLabelSimple[26].setVisible(true);
                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                visPulsantiLingue(false);
                elemLabelTitle[1].setText(elemLabelSimple[i].getText());
                visPulsanteRipristino(false);
                visPulsantiPrese(false);
                break;
            }
            case 2: {
                elemLabelSimple[5].setVisible(false);
                elemLabelSimple[26].setVisible(false);
                elemLabelTitle[1].setText(elemLabelSimple[i].getText());
                labelImageAux[0].setVisible(true);
                labelImageAux[1].setVisible(false);
                visPulsantiLingue(true);
                index = 0;
                aggiornaLabelLingue();
                visPulsanteRipristino(false);
                visPulsantiPrese(false);
                valPulsantiAusiliari(false);
                break;
            }
            case 3: {

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(410))) {

                    gestoreScambioPannello(9);
                    valPulsantiAusiliari(false);
                } else {
                    gestoreScambioPannello(1);

                    valPulsantiAusiliari(false);
                }
                break;
            }
            case 4: {
                elemLabelSimple[5].setVisible(false);
                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                visPulsantiLingue(false);
                elemLabelTitle[1].setText(elemLabelSimple[i].getText());
                visPulsantiPrese(false);
                visPulsanteRipristino(true);
                valPulsantiAusiliari(false);
                break;
            }

        }

    }

    //Impostazione visibilità pulsanti lingue
    public void visPulsantiLingue(boolean vis) {

        for (int i = 5; i < 11; i++) {
            elemBut[i].setVisible(vis);
            elemLabelSimple[i + 1].setVisible(vis);
        }

        elemBut[11].setVisible(vis);
        elemBut[12].setVisible(vis);

    }

    //Impostazione validità pulsanti ausiliari
    public void valPulsantiAusiliari(boolean vis) {

        boolean visOp = vis;

        elemBut[28].setVisible(vis);

        elemLabelSimple[26].setEnabled(vis);

        if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(208))) {
            visOp = false;
        }

        elemBut[28].setEnabled(visOp);
        elemLabelSimple[26].setEnabled(visOp);

    }

    //Impostazione visibilità pulsante di ripristino 
    public void visPulsanteRipristino(boolean vis) {

        elemBut[13].setVisible(vis);

        elemLabelSimple[12].setVisible(vis);
        elemLabelSimple[13].setVisible(vis);
    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello(int p) {

        this.setVisible(false);

        switch (p) {

            case 0: {

                if (editingProcesso) {

                    if (pannelliCollegati.get(4) instanceof Pannello10_ScelteEffettuate) {
                        ((Pannello10_ScelteEffettuate) pannelliCollegati.get(3)).initPanel();

                    }
                } else if (pannelliCollegati.get(0) instanceof Pannello00_Principale) {

                    ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                }
                break;
            }

            case 1: {

                ((Pannello14_Configurazione_Parametri) pannelliCollegati.get(1)).initPanel();

                break;

            }

            case 2: {

                ((Pannello15_Configurazione_Prese) pannelliCollegati.get(2)).initPanel();

                break;

            }

            case 6: {

                DettagliSessione.setCodiceFigura("");

                ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

                break;

            }

            case 7: {

                ((Pannello31_Comando_Microdosatori) pannelliCollegati.get(4)).initPanel();

                break;

            }

            case 8: {

                ((Pannello34_Gestione_Materie_Prime) pannelliCollegati.get(5)).initPanel(index);

                break;

            }
            case 9: {

                ((Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta) pannelliCollegati.get(6)).initPanel();

                break;

            }

        }

    }

    //Impostazione visibilità pulsanti prese
    public void visPulsantiPrese(boolean vis) {

        for (int i = 0; i < 14; i++) {

            elemBut[14 + i].setVisible(false);

        }

        for (int i = 0; i < prese.size() & i < 12; i++) {

            elemBut[14 + i].setVisible(vis);

            elemLabelSimple[14 + i].setVisible(vis);

        }

        if (prese.size() >= 12) {

            elemBut[26].setVisible(vis);
            elemBut[27].setVisible(vis);
        }

    }

    //Aggiornamento testi pulsanti
    public void modificaTestoPulsanti(int j) {

        for (int i = 14; i < 26; i++) {

            if (i < prese.size() + 14 - j) {

                elemLabelSimple[i].setText(prese.get(i - 14 + j));

            }

        }
    }

    //Imposta abilitazione pulsanti durante l'editing delle prese
    public void aggiornaAbilitaPulsantiEdintigPrese(Boolean enab) {

        elemBut[0].setEnabled(enab);
        elemBut[1].setEnabled(enab);
        elemBut[2].setEnabled(enab);
        elemBut[3].setEnabled(enab);
        elemBut[4].setEnabled(enab);

        elemBut[11].setEnabled(enab);
        elemBut[12].setEnabled(enab);
        elemBut[13].setEnabled(enab);

        elemBut[26].setEnabled(enab);
        elemBut[27].setEnabled(enab);
        elemBut[28].setEnabled(enab);
    }
}
