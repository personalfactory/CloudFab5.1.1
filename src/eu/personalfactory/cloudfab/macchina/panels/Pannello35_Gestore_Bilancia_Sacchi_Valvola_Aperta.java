package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.bilancia.valvola.aperta.BilanciaSacchiValvolaAperta;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta extends MyStepPanel {

    BilanciaSacchiValvolaAperta bilancia;
    boolean interrompiLettura;
    boolean abilita_blocca;
    boolean abilita_sblocca;
    boolean avvio_coclea;
    boolean arresto_coclea;
    MyStepPanel pannello_corrente;

    //COSTRUTTORE
    public Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta() {

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
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(4);
        impostaDimLabelProg(0);
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

        pannello_corrente = this;

        elemLabelSimple[0].setHorizontalAlignment(SwingConstants.RIGHT);
    }

    //Inizializza il Pannello
    public void initPanel() {

        //Impostazione Visibilità Pannello
        setPanelVisibile();
////
////        bilancia = new BilanciaSacchiValvolaAperta(
////                ParametriSingolaMacchina.parametri.get(412),
////                ParametriSingolaMacchina.parametri.get(413),
////                ParametriSingolaMacchina.parametri.get(414));

        //Lettura Messaggi da database
        new LeggiDizionario().start();

        //Lettura Messaggi da database
        new LeggiPeso().start();

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
                elemLabelSimple[0].setText("000000" + " " + ParametriSingolaMacchina.parametri.get(340));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 942, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 943, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 944, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 945, ParametriSingolaMacchina.parametri.get(111)));
 
                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 877, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 878, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 946, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 947, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals(elemBut[0].getName())) {

            abilita_blocca = true;

        } else if (button.equals(elemBut[1].getName())) {
            abilita_sblocca = true;
        } else if (button.equals(elemBut[2].getName())) {
            avvio_coclea = true;
        } else if (button.equals(elemBut[3].getName())) {
            arresto_coclea = true;
        }

    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello() {

        ((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).initPanel(false);
        this.setVisible(false);

    }

    //Lettura Informazioni da Database
    private class LeggiPeso extends Thread {

        @Override
        public void run() {

            interrompiLettura = false;

            bilancia.inizializzaDispositivo();

            while (!interrompiLettura && pannello_corrente.isVisible()) {

                try {
                    if (abilita_blocca) {

                        bilancia.abilitazioneBloccoSacco(true);
                        abilita_blocca = false;

                    } else if (abilita_sblocca) {

                        bilancia.abilitazioneBloccoSacco(false);
                        abilita_sblocca = false;

                    }
                    if (avvio_coclea) {

                        bilancia.attivaMotoreCoclee(ParametriSingolaMacchina.parametri.get(425));
                        avvio_coclea = false;

                    } else if (arresto_coclea) {

                        bilancia.abilitazioneBloccoSacco(false);
                        bilancia.attivaMotoreCoclee("0");
                        arresto_coclea = false;

                    } else {

                        String result = bilancia.leggiPeso();

                        try {

                            elemLabelSimple[0].setText(result.substring(1, result.length()) + " " + ParametriSingolaMacchina.parametri.get(340));
                        } catch (Exception e) {

                        }
                    }
                    LeggiPeso.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            bilancia.inizializzaDispositivo();
        }
    }
}
