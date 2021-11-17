package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import eu.personalfactory.cloudfab.macchina.svuotamento.assistito.SvuotamentoAssistito;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RipulisciTabellaValoreRipristinoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Pannello37_Pulizia extends MyStepPanel {

    MyStepPanel pannello_corrente;
    public int numImage = 0;
    Pulizia pulizia;

    // private int idComp, idSilo; 
    //  private String qResidua;
    public int idPresa;
    public boolean assistito, bilanciaStandard, manuale;
    public SvuotamentoAssistito svuotamentoAssistito;

    public String codComp;
    public boolean confezionamentoInEsecuzione;

    //COSTRUTTORE
    public Pannello37_Pulizia() {

        super();
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(3);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(3);
        impostaColori(3);

        //Inizializza Colore Label Title     
        initColorLabelTitle(elemColor[0]);

        //Inizializza Colore Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializza Colore Label Simple
        initColorLabelSimple(elemColor[2]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Modifica la Visibilità di Default delle Righe di Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        elemLabelSimple[0].setVerticalAlignment(JLabel.BOTTOM);
        elemLabelSimple[1].setVerticalAlignment(JLabel.BOTTOM);
        elemLabelSimple[2].setVerticalAlignment(JLabel.BOTTOM);

    }

    //Inizializza il Pannello
    public void initPanel() {
        //Aggiornamento Valori Parametri Singola Macchina
        //ParametriSingolaMacchina.init();
        
        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Lettura Messaggi da database
        new LeggiDizionario().start();
         
        //Eliminazione Tabella Ripristino
        RipulisciTabellaValoreRipristinoOri();
  

    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 948, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento label Tipo Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 949, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 950, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 951, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamentop Testo Label Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 952, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
 
        if (button.equals(elemBut[0].getName())) {

            ////////////////////////////
            // PANNELLO SVUOTAMENTO  ///
            ////////////////////////////
            ((Pannello38_Pulizia_Svuotamento) pannelliCollegati.get(1)).initPanel(0,0);   //Nessuno ordine e nessun prodotto
            this.setVisible(false);

        } else if (button.equals(elemBut[1].getName())) {

            //////////////////////////
            // PULIZIA AUTOMATICA  ///
            //////////////////////////
            ((Pannello39_Pulizia_Automatica) pannelliCollegati.get(2)).initPanel(0,0);
            this.setVisible(false);

        } else if (button.equals(elemBut[2].getName())) {

            ///////////////////////
            // PULIZIA MANUALE  ///
            ///////////////////////
            ((Pannello40_Pulizia_Manuale) pannelliCollegati.get(3)).initPanel();
            this.setVisible(false);

        }
    }

}
