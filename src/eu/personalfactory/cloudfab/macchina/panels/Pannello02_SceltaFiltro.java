package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello02_SceltaFiltro extends MyStepPanel {
 
    //COSTRUTTORE
    public Pannello02_SceltaFiltro() {
        super();
  
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

         //Dichiarazione File Parametri
         impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(4);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(1);
        impostaDimLabelBut(4);
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
 

    //Inizializzazione Pannello
    public void initPanel() {

////        //Reset Tabella Valori di Ripristino
////        Benefit.destroyEntityValoreRipristinoOri();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Messaggi da Database
        new LeggiDizionario().start();

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

                //Impostazione Label Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 16, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 17, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 18, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 19, ParametriSingolaMacchina.parametri.get(111)));

                //Impostazione Messaggi di Aiuto
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 20, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 21, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 22, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 23, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 24, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Impostazione Messaggi Avanzamento
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 25, ParametriSingolaMacchina.parametri.get(111)));

                //Impostazione Titoli
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 26, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            ///////////////////////
            /// NUOVO PROCESSO  ///
            /////////////////////// 
            
            gestoreScambioPannello(pannelliCollegati.get(2));

        } else {
            //////////////////////////
            /// RIPRENDI PROCESSO  ///
            //////////////////////////

            ((Pannello03_FiltroGen) pannelliCollegati.get(1)).panelType = Integer.parseInt(button);
            
            gestoreScambioPannello(pannelliCollegati.get(1));
        }
 
    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello(MyStepPanel pannello) {

        this.setVisible(false);
        
        if (pannello instanceof Pannello03_FiltroProdCod) {  
            ((Pannello03_FiltroProdCod) pannelliCollegati.get(2)).initPanel();
        } else { 
            ((Pannello03_FiltroGen) pannelliCollegati.get(1)).initPanel();
        }

    }
}
