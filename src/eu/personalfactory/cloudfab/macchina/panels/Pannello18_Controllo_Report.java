package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.loggers.ReportLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello18_Controllo_Report extends MyStepPanel {

    //PARAMETRI PANNELLO
    public final int numImage = 60;
    //VARIABILI
    public int codType;
    public boolean esitoTest[];

    //COSTRUTTORE
    public Pannello18_Controllo_Report() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(30);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(0);
        impostaColori(2);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Impostazione Allineamento Label Simple
        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setVerticalAlignment(SwingConstants.CENTER);
            elemLabelSimple1.setHorizontalAlignment(SwingConstants.LEFT);
        }

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Inizializzazione Logger
        ReportLogger.init();

        //Inizializzazione Immagini Ausiliarie
        aggiornaLabelImageAux();

        //Impostazione Visibilità pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

    }

    //Inizializzazione Immagini Ausiliarie
    public void aggiornaLabelImageAux() {

        //Reset Visualizzazione Immagini
        for (int i = 0; i < labelImageAux.length; i++) {

            labelImageAux[i].setVisible(false);

        }

        //Memorizzazione Risultati Finali
        ReportLogger.logger.log(Level.INFO, "\n\n--{0}--\n\nDATA ={1}\n\n{2} = {3}\n{4} = {5}\n{6} = "
                + "{7}\n{8} = {9}\n{10} = {11}\n{12} = {13}\n{14} = {15}\n{16} = {17}\n{18} = "
                + "{19}\n{20} = {21}\n{22} = {23}\n{24} = {25}\n{26} = {27}\n{28} = {29}\n{30} = "
                + "{31}\n{32} = {33}\n{34} = {35}\n{36} = {37}\n{38} = {39}\n{40} = {41}\n{42} = "
                + "{43}\n{44} = {45}\n{46} = {47}\n{48} = {49}\n{50} = {51}\n{52} = {53}\n{54} = "
                + "{55}\n{56} = {57}\n{58} = {59}\n{60} = {61}",
                new Object[]{EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 501,
                            ParametriSingolaMacchina.parametri.get(111))),
                    new Date(),
                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 502, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[0], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 503, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[1], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 504, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[2], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 505, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[3], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 506, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[4], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 507, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[5], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 508, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[6], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 509, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[7], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 510, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[8], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 511, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[9], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 512, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[10], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 513, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[11], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 514, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[12], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 515, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[13], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 516, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[14], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 517, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[15], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 518, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[16], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 519, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[17], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 520, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[18], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 521, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[19], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 522, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[20], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 744, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[21], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 745, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[22], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 746, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[23], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 789, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[24], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 791, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[25], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[26], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 795, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[27], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 809, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[28], EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1008, ParametriSingolaMacchina.parametri.get(111))),
                    esitoTest[29]});

        //Aggiornamento Visualizzazione Immagini Ausiliarie
        for (int i = 0; i < esitoTest.length; i++) {

            if (!esitoTest[i]) {
                labelImageAux[i * 2].setVisible(true);

            } else {
                labelImageAux[i * 2 + 1].setVisible(true);
            }

        }

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornametno Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 501, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornametno Testo Label Tipo Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 502, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 503, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 504, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 505, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 506, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 507, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 508, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[7].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 509, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[8].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 510, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[9].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 511, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 512, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 513, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[12].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 514, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[13].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 515, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[14].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 516, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[15].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 517, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 518, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[17].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 519, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[18].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 520, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[19].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 521, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[20].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 522, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[21].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 744, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[22].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 745, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[23].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 746, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[24].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 789, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[25].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 791, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[26].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 793, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[27].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 795, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[28].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 809, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[29].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1008, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }
}
