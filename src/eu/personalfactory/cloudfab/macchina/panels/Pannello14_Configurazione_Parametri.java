package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParSingMacOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello14_Configurazione_Parametri extends MyStepPanel {

    //PARAEMTRI PANNELLO
    public final int numImage = 4;
    //VARIABILI
    public int[] value;

    //COSTRUTTORE
    public Pannello14_Configurazione_Parametri() {

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
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(7);
        impostaDimLabelProg(0);
        impostaDimLabelBut(54);
        impostaColori(2);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        // Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Impostazione Allineamento Label Tipo Simple
        for (int i = 0; i < elemLabelSimple.length; i++) {
            elemLabelSimple[i].setVerticalAlignment(SwingConstants.CENTER);
            elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);
        }

        //Impostazione Allineamento Label Tipo Title
        elemLabelTitle[5].setHorizontalAlignment(SwingConstants.LEFT);
        elemLabelTitle[6].setHorizontalAlignment(SwingConstants.RIGHT);

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, true).start();
    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Lettura Valori Parametri Prestazioni
        new LeggiPrestazioni().start();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Impostazione Visibilità Pannello
        setPanelVisibile();

    }

    //Lettura Valori Parametri Prestazioni da Database
    private class LeggiPrestazioni extends Thread {

        @Override
        public void run() {

            //Inzializzazione Array
            value = new int[numImage];

            //Correttivo Vel
            value[0] = Integer.parseInt(
                    ParametriSingolaMacchina.parametri.get(108));

            //Correttivo Tempo
            value[1] = Integer.parseInt(
                    ParametriSingolaMacchina.parametri.get(109));

            //Correttivo Materiale Inizio Pesatura Fine
            value[2] = Integer.parseInt(
                    ParametriSingolaMacchina.parametri.get(110));

            //Correttivo Precisione Peso Carico
            value[3] = Integer.parseInt(
                    ParametriSingolaMacchina.parametri.get(112));

            //Impostazione Posizione Cursori
            labelImageAux[0].setBounds(calcolaPosizione(value[0]),
                    labelImageAux[0].getY(),
                    labelImageAux[0].getWidth(),
                    labelImageAux[0].getHeight());

            labelImageAux[1].setBounds(calcolaPosizione(value[1]),
                    labelImageAux[1].getY(),
                    labelImageAux[1].getWidth(),
                    labelImageAux[1].getHeight());

            labelImageAux[2].setBounds(calcolaPosizione(200 - value[2]),
                    labelImageAux[2].getY(),
                    labelImageAux[2].getWidth(),
                    labelImageAux[2].getHeight());

            labelImageAux[3].setBounds(calcolaPosizione(200 - value[3]),
                    labelImageAux[3].getY(),
                    labelImageAux[3].getWidth(),
                    labelImageAux[3].getHeight());

        }
    }

    //Calcolo Posizione Cursori
    private int calcolaPosizione(int value) {

        return (((value - 50) * (elemBut[14].getX() - elemBut[2].getX())) / 100)
                + elemBut[2].getX();

    }

    private void spostaCursore(int index, String buttonName) {

        labelImageAux[index].setBounds(elemBut[Integer.parseInt(buttonName)].getX(),
                labelImageAux[index].getY(),
                labelImageAux[index].getWidth(),
                labelImageAux[index].getHeight());

         
        value[0] = 50 + ((labelImageAux[0].getX() - elemBut[2].getX()) * 100
                    / (elemBut[14].getX() - elemBut[2].getX()));
          
        value[1] = 50 + ((labelImageAux[1].getX() - elemBut[2].getX()) * 100
                    / (elemBut[14].getX() - elemBut[2].getX()));
         
        value[2] = 200 - (50 + ((labelImageAux[2].getX() - elemBut[2].getX()) * 100
                    / (elemBut[14].getX() - elemBut[2].getX()))); 
        
        value[3] = 200 - (50 + ((labelImageAux[3].getX() - elemBut[2].getX()) * 100
                    / (elemBut[14].getX() - elemBut[2].getX()))); 
        
    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 258, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 276, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 277, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 278, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 279, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 325, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 326, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 274, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 275, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
        switch (button) {
            case "0": {

                /////////////////////////
                // LETTURA PARAMETRI  ///
                /////////////////////////

                new LeggiPrestazioni().start();

                break;
            }
            case "1": {

                ///////////////////////////////
                // REGISTRAZIONE PARAMETRI  ///
                ///////////////////////////////

                new RegistraModifiche().start();

                break;
            }
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
            case "13":
            case "14": {

                spostaCursore(0, button);

                break;
            }

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
            case "26":
            case "27": {

                spostaCursore(1, button);

                break;
            }
            case "28":
            case "29":
            case "30":
            case "31":
            case "32":
            case "33":
            case "34":
            case "35":
            case "36":
            case "37":
            case "38":
            case "39":
            case "40": {

                spostaCursore(2, button);

                break;
            }

            case "41":
            case "42":
            case "43":
            case "44":
            case "45":
            case "46":
            case "47":
            case "48":
            case "49":
            case "50":
            case "51":
            case "52":
            case "53": {

                spostaCursore(3, button);

                break;
            }

        }

    }

    //Registrazione Modifiche sui Parametri
    private class RegistraModifiche extends Thread {

        @Override
        public void run() {

            //Correttivo Vel
            if (!ParametriSingolaMacchina.parametri.get(108).equals(approssimazioneValore(value[0]))) {

                AggiornaValoreParSingMacOri(108, approssimazioneValore(value[0]));
            }

            //Correttivo Tempo
            if (!ParametriSingolaMacchina.parametri.get(109).equals(approssimazioneValore(value[1]))) {

                AggiornaValoreParSingMacOri(109, approssimazioneValore(value[1]));
            }

            //Correttivo Materiale Inizio Pesatura Fine
            if (!ParametriSingolaMacchina.parametri.get(110).equals(approssimazioneValore(value[2]))) {

                AggiornaValoreParSingMacOri(110, approssimazioneValore(value[2]));
            }

            //Correttivo Precisione Peso Carico
            if (!ParametriSingolaMacchina.parametri.get(112).equals(approssimazioneValore(value[3]))) {

                AggiornaValoreParSingMacOri(112, approssimazioneValore(value[3]));
            }

            //Inizializzazione Parametri Singola Macchina
            ParametriSingolaMacchina.init();

        }
    }

    //Approssimazione del Valore Impostato dall'Utente
    private String approssimazioneValore(int value) {
        int result;
        int incr = 5;

        result = value - (value % incr);

        if (value % incr > 2) {
            result += incr;
        }

        return Integer.toString(result);
    }
}
