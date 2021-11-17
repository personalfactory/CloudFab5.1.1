package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_BROWSER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_VIDEO_CHAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_PANEL_REDUCED;
import javax.swing.SwingConstants;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabFrame;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame; 
import org.jdom.Element;

@SuppressWarnings("serial")
public class Pannello20_Assistenza extends MyStepPanel {

    //PARAMETRI PANNELLO
    public final int numImage = 3;
    //VARIABILI
    private boolean contattiVisibili;
    private JFrame frameAlternativo;

    //COSTRUTTORE
    public Pannello20_Assistenza() { 
        
        super();
 
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

       //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(7);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(3);
        impostaDimLabelProg(0);
        impostaDimLabelBut(5);
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
        for (int i = 0; i < elemLabelSimple.length; i++) {
            elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);
            elemLabelSimple[i].setVerticalAlignment(SwingConstants.BOTTOM);
        }

        elemLabelSimple[5].setHorizontalAlignment(SwingConstants.LEFT);
        elemLabelSimple[6].setHorizontalAlignment(SwingConstants.LEFT);

        //Impostazione Colore Label Simple
        elemLabelSimple[4].setForeground(Color.WHITE);

        //Avvio Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        //Definizione Caratteristiche Frame
        definisciFrame();


    }

    //Definizione Caratteristiche Frame
    public void definisciFrame() {

        //Dichiarazione Frame Alternativo
        frameAlternativo = new JFrame();

        Element elm = root.getChild(XML_PANEL_REDUCED);

        //Impostazione Dimensioni e Posizione Frame
        frameAlternativo.setBounds(Integer.parseInt(elm.getAttributeValue(XML_CORD_X)),
                Integer.parseInt(elm.getAttributeValue(XML_CORD_Y)),
                Integer.parseInt(elm.getAttributeValue(XML_DIMENSION_LARG)),
                Integer.parseInt(elm.getAttributeValue(XML_DIMENSION_ALT)));

        //Eliminazione Decorazioni Finestra
        frameAlternativo.setUndecorated(true);

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Label Image
        labelImageAux[2].setVisible(true);

        //Impostazione Visibilità Pulsante Scelta
        impostaVisButScelta(true);

        //Impostazione Visiblità Button Dietro
        impostaVisButDietro(false);

        //Impostazione Visibilità
        butFreccia.setVisible(true);

        //Impostazione Visibilità Contatti
        impostaVisContatti(false);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Dettagli Assistenza da Database
        new LeggiDettagliAssistenza().start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

    }

    //Lettura Informazioni Relative ai Riferimenti per l'Assistenza
    private class LeggiDettagliAssistenza extends Thread {

        @Override
        public void run() {

            elemLabelSimple[5].setText(HTML_STRINGA_INIZIO
                    + ParametriGlobali.parametri.get(36)
                    + HTML_BREAK_LINE
                    + ParametriGlobali.parametri.get(37)
                    + HTML_BREAK_LINE
                    + ParametriGlobali.parametri.get(38)
                    + HTML_BREAK_LINE
                    + ParametriGlobali.parametri.get(39)
                    + HTML_STRINGA_FINE);

            elemLabelSimple[6].setText(HTML_STRINGA_INIZIO
                    + ParametriSingolaMacchina.parametri.get(139)
                    + HTML_BREAK_LINE
                    + ParametriSingolaMacchina.parametri.get(140)
                    + HTML_BREAK_LINE
                    + ParametriSingolaMacchina.parametri.get(141)
                    + HTML_BREAK_LINE
                    + ParametriSingolaMacchina.parametri.get(142)
                    + HTML_STRINGA_FINE);
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
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 529, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 531, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 532, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Label Tipo Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 525, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 526, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 527, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 528, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 530, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

////        //Chiusura Eventuali Applicazioni Aperte
////        closeApp();
  
        switch (Integer.parseInt(button)) {

            case 0: {

                ////////////////////
                // PULSANTE FAQ  ///
                ////////////////////

                //Impostazione Visibilità Pulsante Scelta
                impostaVisButScelta(false);

                //Impostazione Visibilità Frame Aggiuntivo
                impostaFrame(true);

                //Impostazione Visibilità Button Freccia
                butFreccia.setVisible(false);

                //Impostazione Visibilità Button Dietro
                impostaVisButDietro(true);

                try {
                      
                    Runtime.getRuntime().exec(PATH_BROWSER
                            + " "
                            + ParametriGlobali.parametri.get(67)
                            + File.separator
                            + ParametriGlobali.parametri.get(69));
                     
                } catch (IOException ex) {
                }

                break;
            }
            case 1: {

                ////////////////////////
                // PULSANTE MANUALE  ///
                ////////////////////////

                //Impostazione Visibilità Pulsante Scelta
                impostaVisButScelta(false);

                //Impostazione Visibilità Frame Aggiuntivo
                impostaFrame(true);

                //Impostazione Visibilità Button Freccia
                butFreccia.setVisible(false);

                //Impostazione Visibilità Button Dietro
                impostaVisButDietro(true);
                try {
                    Runtime.getRuntime().exec(PATH_BROWSER
                            + " "
                            + ParametriGlobali.parametri.get(66)
                            + File.separator
                            + ParametriGlobali.parametri.get(68));
                } catch (IOException ex) {
                }



                break;
            }

            case 2: {

                ////////////////////////
                // PULSANTE ON-LINE  ///
                ////////////////////////

                //Impostazione Visibilità Pulsante Scelta
                impostaVisButScelta(false);

                //Impostazione Visibilità Frame Aggiuntivo
                impostaFrame(true);

                //Impostazione Visibilità Button Dietro
                impostaVisButDietro(true);

                //Impostazione Visibilità Button Freccia
                butFreccia.setVisible(false);

                try {
                    Runtime.getRuntime().exec(PATH_VIDEO_CHAT);
                } catch (IOException ex) {
                }

                break;
            }


            case 3: {

                /////////////////////////
                // PULSANTE CONTATTI  ///
                /////////////////////////

                //Impostazione Visibilità Pulsante Scelta
                impostaVisButScelta(false);

                //Impostazione Visibilità Contatti
                impostaVisContatti(true);

                break;
            }

            case 4: {

                /////////////////////////
                // PULSANTE INDIETRO  ///
                /////////////////////////

                //Impostazione Visibilità Pulsante Scelta
                impostaVisButScelta(true);

                //Impostazione Visibilità Frame Aggiuntivo
                impostaFrame(false);

                //Impostazione Visiblilità Button Freccia
                butFreccia.setVisible(true);

                //Impostazione Visibilità Button Dietro
                impostaVisButDietro(false);

                break;
            }

        }

    }

////    //Chiusura Eventuali Applicazioni Aperte
////    public void closeApp() {
////
////        //Chiusura Browser
////        try {
////            Runtime.getRuntime().exec(
////                    FabCloudConstants.COMANDO_CHIUSURA_APPLICAZIONE
////                    + " "
////                    + FabCloudConstants.NOME_BROWSER);
////        } catch (IOException ex) {
////        }
////
////        //Chiusuta Video Chat
////        try {
////            Runtime.getRuntime().exec(
////                    FabCloudConstants.COMANDO_CHIUSURA_APPLICAZIONE
////                    + " "
////                    + FabCloudConstants.NOME_VIDEO_CHAT);
////            
////        } catch (IOException ex) {
////        }
////
////    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello() {

        if (contattiVisibili) {

            //Impostazione Visibilità Contatti
            impostaVisContatti(false);

            //Impostazione Visibilità Pulsanti Scelte
            impostaVisButScelta(true);

            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        } else {

            this.setVisible(false);

            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
        }

    }

    //Impostazione Visibilità Frame Aggiuntivo
    public void impostaFrame(boolean vis) {

        //Ridimensionamento Finestra
        if (vis) {

            //Impostazione Visibilità Label Immagini Ausiliarie
            labelImageAux[1].setVisible(true);
            labelImageAux[2].setVisible(false);

            //Impostazione Visibilità Frame 
            CloudFabFrame.getFRAME().setVisible(false);

            //Impostazione Visibilità Finestra Principale
            frameAlternativo.setContentPane(this);

            //Impostazione Visibilità Finestra Principale
            frameAlternativo.setVisible(true);

        } else {

            //Impostazione Visibilità Label Immagini Ausiliarie
            labelImageAux[1].setVisible(false);
            labelImageAux[2].setVisible(true);

            //Impostazione Visiblità Frame Alternativo
            frameAlternativo.setVisible(false);

            //Assegnazione Pannello Corrente a Frame Principale
            CloudFabFrame.getFRAME().setContentPane(this);

            //Impostazione Visibilità Finestra Principale
            CloudFabFrame.getFRAME().setVisible(true);

        }

    }

    //Impostazione Visiblità Button Scelta
    public void impostaVisButScelta(boolean vis) {

        //Impostazione Visibilità Pulsanti Principali
        elemBut[0].setVisible(vis);
        elemBut[1].setVisible(vis);
        elemBut[2].setVisible(vis);
        elemBut[3].setVisible(vis);

        //Impostazione Visibilità Label Simple
        elemLabelSimple[0].setVisible(vis);
        elemLabelSimple[1].setVisible(vis);
        elemLabelSimple[2].setVisible(vis);
        elemLabelSimple[3].setVisible(vis);

    }

    //Impostazione Visibilità Button Dietro
    public void impostaVisButDietro(boolean vis) {

        //Impostazione Visibilità Label Button Dietro
        elemLabelSimple[4].setVisible(vis);

        //Impostazione Visibilità Button Dietro
        elemBut[4].setVisible(vis);

    }

    //Impostazione Visiblità Contatti
    public void impostaVisContatti(boolean vis) {

        contattiVisibili = vis;
        labelImageAux[0].setVisible(vis);

        elemLabelSimple[5].setVisible(vis);
        elemLabelSimple[6].setVisible(vis);

        elemLabelTitle[1].setVisible(vis);
        elemLabelTitle[2].setVisible(vis);


    }
}
