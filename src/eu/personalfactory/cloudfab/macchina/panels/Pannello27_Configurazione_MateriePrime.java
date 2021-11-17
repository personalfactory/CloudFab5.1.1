package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.processo.ActionListenerInserimentoCodiceComponente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParComponenteOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroCompPerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;


@SuppressWarnings("serial")
public class Pannello27_Configurazione_MateriePrime extends MyStepPanel{
    
    //VARIABILI
    private int idComponente;
    private String codComponente;
    
    //COSTRUTTORE
    public Pannello27_Configurazione_MateriePrime() { 

        super();
 
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

       //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(1);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(3);
        impostaDimLabelProg(0);
        impostaDimLabelBut(4);
        impostaColori(3); 
        
        //Inizializzazione Colori Label Tipo Simple
        initColorLabelSimple(elemColor[0]);
        
        //Inizializzazione Colori Label Tipo Help
        initColorLabelHelp(elemColor[1]);
        
        //Inizializzazione Colori Label Tipo Title
        initColorLabelTitle(elemColor[2]);
         
        //Configurazione di Base Pannello
        configuraPannello();

        //Inserimento Area di Testo
        inserisciTextField();

        //Inserimento Tastiera
        inserisciTastiera();

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

        //Creazione Action Listener
        txtField.addActionListener(new ActionListenerInserimentoCodiceComponente(this));
         
    }

    //Inizializzazione Pannello
    public void initPanel(String componente) {

        //Aggiornamento Testo Label Simple
        elemLabelSimple[0].setText(componente);

        //Impostazione Visibilità Pannello
        ImpostaVisibilitaPannello(true);

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura id Componente
        idComponente = TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_COMPONENTI,
                EstraiStringaHtml(componente),
                ParametriSingolaMacchina.parametri.get(111));
        
        //Lettura Codice Componente
        codComponente = TrovaSingoloValoreParametroCompPerIdComp(
                idComponente,
                Integer.parseInt(ParametriGlobali.parametri.get(65)));

        //Inizializzazione Testo textFied
        txtField.setText(codComponente);
         

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread{

        @Override
        public void run() {
            
             if (!ParametriSingolaMacchina.parametri.get(111).equals(language)){
                 
                //Impostazione Lingua Pannello
                language=ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testi Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,616, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testi Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,614, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,617, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA,615, ParametriSingolaMacchina.parametri.get(111)));

             }
            }

    }

    //Impostazione Visibilità Elementi del Pannello
    public void ImpostaVisibilitaPannello(boolean vis) {

        //Impostazione Visibilità Text Field
        txtField.setVisible(vis);

        //Impostazione Posizione Cursore Text Field
        txtField.setFocusable(true);

        //Impostazione Visibilità Messaggi Aiuto
        for (int i=0; i<elemLabelHelp.length; i++){
        elemLabelHelp[i].setVisible(vis);
        }

        //Impostazione Visibilità Tastiera
        tastiera.impostaVisibilitaTastiera(vis);

        //Impostazione Visibiltà Label Title
        for (int i=0; i<elemLabelTitle.length; i++){
            elemLabelTitle[i].setVisible(vis);
        }

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName()) | button.equals(elemBut[2].getName())) {

            txtField.postActionEvent();

        } else  if (button.equals(elemBut[1].getName())) {
           
            txtField.setText("");
        
        } else if (button.equals(elemBut[3].getName())) {
        
            txtField.setText(codComponente);
        
        }
        
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        ((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).initPanel(((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).editingProcesso);

        this.setVisible(false);


    }

    //Aggiornamento Codice Relativo al Componente
    public void registraCodiceComponente() {

        if (!codComponente.equals(txtField.getText())) {

            AggiornaValoreParComponenteOri(
                    Integer.parseInt(ParametriGlobali.parametri.get(65)),
                    idComponente,
                    txtField.getText());
        }

    }

}
