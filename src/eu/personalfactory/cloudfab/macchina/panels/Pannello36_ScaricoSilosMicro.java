package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import eu.personalfactory.cloudfab.macchina.svuotamento.assistito.SvuotamentoAssistito;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParComponenteOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroCompPerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;

import java.util.Date;

import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Pannello36_ScaricoSilosMicro extends MyStepPanel {

    MyStepPanel pannello_corrente;
    private int idComp, idSilo;
    public int idPresa;
    public boolean assistito, bilanciaStandard, manuale;
    public SvuotamentoAssistito svuotamentoAssistito;
    private String qResidua;
    public int numImage = 2;
    public String codComp;
    public boolean confezionamentoInEsecuzione;

    //COSTRUTTORE
    public Pannello36_ScaricoSilosMicro() {

        super();
        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(18);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(7);
        impostaDimLabelProg(0);
        impostaDimLabelBut(11);
        impostaColori(4);

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

        pannello_corrente = this;

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        elemLabelSimple[0].setForeground(elemColor[3]);
        elemLabelSimple[1].setForeground(elemColor[3]);
        elemLabelSimple[2].setForeground(elemColor[3]);

        elemLabelSimple[12].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelSimple[13].setHorizontalAlignment(JLabel.RIGHT);
        elemLabelSimple[14].setHorizontalAlignment(JLabel.RIGHT);

    }

    //Inizializza il Pannello
    public void initPanel(int idComp, String qResidua, int idSilo, String codComp) {

        svuotamentoAssistito = new SvuotamentoAssistito(this);

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        modificaPannello(0);

        this.idComp = idComp;
        this.codComp = codComp;
        this.qResidua = qResidua;
        this.idSilo = idSilo;
        this.idPresa = Integer.parseInt(TrovaSingoloValoreParametroCompPerIdComp(idComp, 1));

        //Lettura Messaggi da database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);
        
         	
        elemBut[4].setEnabled(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(401)));
    	
        

    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {
 
                //Impostazione lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                elemLabelSimple[0].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 918, ParametriSingolaMacchina.parametri.get(111)))
                        + ": "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, idComp, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                elemLabelSimple[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 919, ParametriSingolaMacchina.parametri.get(111)))
                        + ": "
                        + TrovaPresaPerIdPresa(idPresa)
                        + HTML_STRINGA_FINE);

                String tipologia = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 921, ParametriSingolaMacchina.parametri.get(111)));
                if (idPresa >= 100) {
                    tipologia = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 922, ParametriSingolaMacchina.parametri.get(111)));
                }

                elemLabelSimple[2].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 920, ParametriSingolaMacchina.parametri.get(111)))
                        + ": "
                        + tipologia
                        + HTML_STRINGA_FINE);

                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 923, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 924, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 925, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 929, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[7].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 930, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[8].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 934, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[9].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 935, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 936, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[11].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 937, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[12].setText("0 " + ParametriSingolaMacchina.parametri.get(340));
                elemLabelSimple[13].setText(Integer.toString(Integer.parseInt(ParametriGlobali.parametri.get(10)) / 1000));
                elemLabelSimple[14].setText("0 " + ParametriSingolaMacchina.parametri.get(340));

                elemLabelSimple[15].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 939, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[16].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 944, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[17].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 945, ParametriSingolaMacchina.parametri.get(111)));

                //Inserimento label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 916, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 923, ParametriSingolaMacchina.parametri.get(111)))
                        + ": "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 926, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                elemLabelTitle[2].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 924, ParametriSingolaMacchina.parametri.get(111)))
                        + ": "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 927, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                elemLabelTitle[3].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 925, ParametriSingolaMacchina.parametri.get(111)))
                        + ": "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 928, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 931, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 932, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 933, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamentop Testo Label Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 917, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);
 
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
    	 
        if (button.equals(elemBut[0].getName())) {

            assistito = true;
            manuale = false;
            modificaPannello(1);

        } else if (button.equals(elemBut[1].getName())) {
            assistito = false;
            manuale = true;

            if ((DettagliSessione.getIdFiguraTipo() >= Integer.parseInt(ParametriSingolaMacchina.parametri.get(418)) || DettagliSessione.isSuperUser())
                   && idPresa < 98) {

                modificaPannello(3);
           }

        } else if (button.equals(elemBut[2].getName())) {

            registraScaricoMateriaPrima();

            gestoreScambioPannello();

        } else if (button.equals(elemBut[3].getName())) {

            /////////////////////////
            // BILANCIA STANDARD  ///
            /////////////////////////
            bilanciaStandard = true;
            modificaPannello(2);

        } else if (button.equals(elemBut[4].getName())) {

            ////////////////////////////////////////
            // BILANCIA SACCHI A VALVOLA APERTA  ///
            ////////////////////////////////////////
        	
        	bilanciaStandard = false;
        	modificaPannello(2);

        } else if (button.equals(elemBut[5].getName())) {

            if (idPresa >= 100) {

                /////////////////////
                // MICRODOSATORE  ///
                /////////////////////
                svuotamentoAssistito.avvioThreadCaricaDaMicro();
            } else {

                ////////////
                // SILO  ///
                ////////////
                svuotamentoAssistito.avvioThreadCaricaDaSilo();
            }
            elemBut[5].setEnabled(false);
            elemBut[6].setEnabled(true);
            elemBut[7].setEnabled(false);
            elemBut[8].setEnabled(false);
            butFreccia.setEnabled(false);

            elemLabelSimple[8].setEnabled(false);
            elemLabelSimple[9].setEnabled(true);
            elemLabelSimple[10].setEnabled(false);
            elemLabelSimple[11].setEnabled(false);

        } else if (button.equals(elemBut[6].getName())) {

            //////////////////////
            // ARRESTO CARICA  ///
            //////////////////////
            svuotamentoAssistito.interrompiThreadCarica = true;

        } else if (button.equals(elemBut[7].getName())) {

            if (!confezionamentoInEsecuzione) {

                confezionamentoInEsecuzione = true;

                elemBut[5].setEnabled(false);
                elemBut[6].setEnabled(false);
                elemBut[7].setEnabled(true);
                elemBut[8].setEnabled(false);
                butFreccia.setEnabled(false);

                elemLabelSimple[8].setEnabled(false);
                elemLabelSimple[9].setEnabled(false);
                elemLabelSimple[10].setEnabled(true);
                elemLabelSimple[11].setEnabled(false);

                elemLabelSimple[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 938, ParametriSingolaMacchina.parametri.get(111)));
                svuotamentoAssistito.avvioConfezionamento();

            } else {

                elemLabelSimple[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 936, ParametriSingolaMacchina.parametri.get(111)));
                svuotamentoAssistito.interrompiConfezionamento = true;
                confezionamentoInEsecuzione = false;
                elemBut[5].setEnabled(true);
                elemBut[6].setEnabled(true);
                elemBut[7].setEnabled(true);
                elemBut[8].setEnabled(true);

                elemLabelSimple[8].setEnabled(true);
                elemLabelSimple[9].setEnabled(true);
                elemLabelSimple[10].setEnabled(true);
                elemLabelSimple[11].setEnabled(true);

                butFreccia.setEnabled(true);

            }

        } else if (button.equals(elemBut[8].getName())) {

            ///////////////
            // CONFERMA ///
            ///////////////
            modificaPannello(0);

        } else if (button.equals(elemBut[9].getName())) {

            //////////////////////////////////////
            // AVVIO COCLEA ROTAZIONE INVERSA  ///
            //////////////////////////////////////
            svuotamentoAssistito.avvioCocleaRotazioneInversa(true);
            
        } else if (button.equals(elemBut[10].getName())) {

            ////////////////////////////////////////
            // ARRESTO COCLEA ROTAZIONE INVERSA  ///
            //////////////////////////////////////// 
            svuotamentoAssistito.avvioCocleaRotazioneInversa(false);
            modificaPannello(0);
        }
    }

    //Gestione Scambio Pannello Collegato
    public void gestoreScambioPannello() {

        svuotamentoAssistito.interrompiComunicazioneSchedaIO = true;

        ((Pannello34_Gestione_Materie_Prime) pannelliCollegati.get(0)).initPanel(0);
        this.setVisible(false);

    }

    public void aggiornaTempoValvola(int i) {

        elemLabelSimple[13].setText(Integer.toString(Integer.parseInt(ParametriGlobali.parametri.get(10)) / 1000 - i));
        repaint();
    }

    public void modificaPannello(int value) {

        switch (value) {
            case 0:

                /////////////////////////////////
                // INIZIALIZZAZIONE PANNELLO  ///
                /////////////////////////////////
                //Labels Simple
                elemLabelSimple[0].setVisible(true);
                elemLabelSimple[1].setVisible(true);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(true);
                elemLabelSimple[4].setVisible(true);
                elemLabelSimple[5].setVisible(true);
                elemLabelSimple[6].setVisible(false);
                elemLabelSimple[7].setVisible(false);
                elemLabelSimple[8].setVisible(false);
                elemLabelSimple[9].setVisible(false);
                elemLabelSimple[10].setVisible(false);
                elemLabelSimple[11].setVisible(false);
                elemLabelSimple[12].setVisible(false);
                elemLabelSimple[13].setVisible(false);
                elemLabelSimple[14].setVisible(false);
                elemLabelSimple[15].setVisible(false);
                elemLabelSimple[16].setVisible(false);
                elemLabelSimple[17].setVisible(false);
                //Labels Title
                elemLabelTitle[0].setVisible(true);
                elemLabelTitle[1].setVisible(true);
                elemLabelTitle[2].setVisible(true);
                elemLabelTitle[3].setVisible(true);
                elemLabelTitle[4].setVisible(false);
                elemLabelTitle[5].setVisible(false);
                elemLabelTitle[6].setVisible(false);
                //Buttons
                elemBut[0].setVisible(true);
                elemBut[1].setVisible(true);
                elemBut[2].setVisible(true);
                elemBut[3].setVisible(false);
                elemBut[4].setVisible(false);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(false);
                elemBut[7].setVisible(false);
                elemBut[8].setVisible(false);
                elemBut[9].setVisible(false);
                elemBut[10].setVisible(false);

                //Image Aux
                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                break;
            case 1:

                /////////////////////////////////
                // ASSISTITO SCELTA BILANCIA  ///
                /////////////////////////////////
                //Labels Simple
                elemLabelSimple[0].setVisible(true);
                elemLabelSimple[1].setVisible(true);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(false);
                elemLabelSimple[4].setVisible(false);
                elemLabelSimple[5].setVisible(false);
                elemLabelSimple[6].setVisible(true);
                elemLabelSimple[7].setVisible(true);
                elemLabelSimple[8].setVisible(false);
                elemLabelSimple[9].setVisible(false);
                elemLabelSimple[10].setVisible(false);
                elemLabelSimple[11].setVisible(false);
                elemLabelSimple[12].setVisible(false);
                elemLabelSimple[13].setVisible(false);
                elemLabelSimple[14].setVisible(false);
                elemLabelSimple[15].setVisible(false);
                elemLabelSimple[16].setVisible(false);
                elemLabelSimple[17].setVisible(false);
                //Labels Title
                elemLabelTitle[0].setVisible(true);
                elemLabelTitle[1].setVisible(false);
                elemLabelTitle[2].setVisible(false);
                elemLabelTitle[3].setVisible(false);
                elemLabelTitle[4].setVisible(false);
                elemLabelTitle[5].setVisible(false);
                elemLabelTitle[6].setVisible(false);

                //Buttons
                elemBut[0].setVisible(false);
                elemBut[1].setVisible(false);
                elemBut[2].setVisible(false);
                elemBut[3].setVisible(true);
                elemBut[4].setVisible(true);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(false);
                elemBut[7].setVisible(false);
                elemBut[8].setVisible(false);
                elemBut[9].setVisible(false);
                elemBut[10].setVisible(false);
                //Image Aux
                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                break;
            case 2:

                /////////////////
                // ASSISTITO  ///
                /////////////////
                //Labels Simple
                elemLabelSimple[0].setVisible(true);
                elemLabelSimple[1].setVisible(true);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(false);
                elemLabelSimple[4].setVisible(false);
                elemLabelSimple[5].setVisible(false);
                elemLabelSimple[6].setVisible(false);
                elemLabelSimple[7].setVisible(false);
                elemLabelSimple[8].setVisible(true);
                elemLabelSimple[9].setVisible(true);
                elemLabelSimple[10].setVisible(true);
                elemLabelSimple[11].setVisible(true);
                elemLabelSimple[12].setVisible(true);
                elemLabelSimple[13].setVisible(true);
                elemLabelSimple[14].setVisible(true);
                elemLabelSimple[15].setVisible(false);
                elemLabelSimple[16].setVisible(false);
                elemLabelSimple[17].setVisible(false);

                elemLabelSimple[8].setEnabled(true);
                elemLabelSimple[9].setEnabled(false);
                elemLabelSimple[10].setEnabled(false);
                elemLabelSimple[11].setEnabled(false);

                //Labels Title
                elemLabelTitle[0].setVisible(true);
                elemLabelTitle[1].setVisible(false);
                elemLabelTitle[2].setVisible(false);
                elemLabelTitle[3].setVisible(false);
                elemLabelTitle[4].setVisible(true);
                elemLabelTitle[5].setVisible(true);
                elemLabelTitle[6].setVisible(true);

                //Buttons
                elemBut[0].setVisible(false);
                elemBut[1].setVisible(false);
                elemBut[2].setVisible(false);
                elemBut[3].setVisible(false);
                elemBut[4].setVisible(false);
                elemBut[5].setVisible(true);
                elemBut[6].setVisible(true);
                elemBut[7].setVisible(true);
                elemBut[8].setVisible(true);
                elemBut[9].setVisible(false);
                elemBut[10].setVisible(false);

                elemBut[5].setEnabled(true);
                elemBut[6].setEnabled(false);
                elemBut[7].setEnabled(false);
                elemBut[8].setEnabled(false);

                butFreccia.setEnabled(true);

                //Image Aux
                labelImageAux[0].setVisible(true);
                labelImageAux[1].setVisible(false);
                break;

            case 3:

                ///////////////
                // MANUALE  ///
                ///////////////
                //Labels Simple
                elemLabelSimple[0].setVisible(true);
                elemLabelSimple[1].setVisible(true);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(false);
                elemLabelSimple[4].setVisible(false);
                elemLabelSimple[5].setVisible(false);
                elemLabelSimple[6].setVisible(false);
                elemLabelSimple[7].setVisible(false);
                elemLabelSimple[8].setVisible(false);
                elemLabelSimple[9].setVisible(false);
                elemLabelSimple[10].setVisible(false);
                elemLabelSimple[11].setVisible(false);
                elemLabelSimple[12].setVisible(false);
                elemLabelSimple[13].setVisible(false);
                elemLabelSimple[14].setVisible(false);
                elemLabelSimple[15].setVisible(true);
                elemLabelSimple[16].setVisible(true);
                elemLabelSimple[17].setVisible(true);

                elemLabelSimple[8].setEnabled(true);
                elemLabelSimple[9].setEnabled(true);
                elemLabelSimple[10].setEnabled(true);
                elemLabelSimple[11].setEnabled(true);

                //Labels Title
                elemLabelTitle[0].setVisible(false);
                elemLabelTitle[1].setVisible(false);
                elemLabelTitle[2].setVisible(false);
                elemLabelTitle[3].setVisible(false);
                elemLabelTitle[4].setVisible(false);
                elemLabelTitle[5].setVisible(false);
                elemLabelTitle[6].setVisible(false);

                //Buttons
                elemBut[0].setVisible(false);
                elemBut[1].setVisible(false);
                elemBut[2].setVisible(false);
                elemBut[3].setVisible(false);
                elemBut[4].setVisible(false);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(false);
                elemBut[7].setVisible(false);
                elemBut[8].setVisible(false);
                elemBut[9].setVisible(true);
                elemBut[10].setVisible(true);

                elemBut[5].setEnabled(true);
                elemBut[6].setEnabled(false);
                elemBut[7].setEnabled(false);
                elemBut[8].setEnabled(false);

                butFreccia.setEnabled(true);

                //Image Aux
                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(true);
                break;
            default:
                break;
        }

    }

    public void registraScaricoMateriaPrima() {

        int q = Integer.parseInt(qResidua) * 1000;

        String segno_operazione = "-1";

        //ASSOCIAZIONE COMPONENTE CONTENITORE 
        AggiornaValoreParComponenteOri(
                1,
                idComp,
                ParametriGlobali.parametri.get(64));

        //ASSOCIAZIONE COMPONENTE CONTENITORE 
        AggiornaValoreParComponenteOri(
                6,
                idComp,
                "");

        //Registra Ciclo Eseguita
        int id_ciclo_inserito = RegistraNuovoCiclo(
                ParametriGlobali.parametri.get(127), //tipo_ciclo 
                0, //id_ordine
                idComp, //id_prodotto
                0, //id_cat
                0, //velocita_mix
                0, //tempo_mix
                0, //num_sacchi
                0, //num_sacchi_aggiuntivi
                "", //vibro_attivo
                "", //aria_cond_scarico
                "", //aria_interno_valvola
                "", //aria_pulisci_valvola
                "", //info1
                "", //info2
                "", //info3
                "", //info4
                "", //info5
                "", //info6
                "", //info7
                "", //info8
                "", //info9
                "", //info10
                "", //id_serie_colore
                ""); //id_serie_additivo

        //Registra Movimento di Magazzino
        RegistraNuovoMovimentoMagazzino(
                idComp, //id_materiale
                ParametriGlobali.parametri.get(117), //tipo_materiale
                q, //qEffettivo
                codComp, //cod_ingresso_comp
                DettagliSessione.getCodiceFigura(), //cod_operatore
                segno_operazione, //segno_op
                ParametriGlobali.parametri.get(124), //procedura
                ParametriGlobali.parametri.get(122), //tipo_mov
                ParametriGlobali.parametri.get(128), //descri_mov
                Integer.toString(idSilo), //id_silo
                q, //qTeorico
                id_ciclo_inserito,	//id_ciclo
                ParametriGlobali.parametri.get(140), //origine_mov 
                new Date(),			//DataMov
                true,				//Abilitato
                TrovaValoreParametriComponentePerIdComp(idComp).get(9),					//Info1
                TrovaValoreParametriComponentePerIdComp(idComp).get(10),					//Info2 
                "",					//Info3 					
                "",					//Info4 
                "",					//Info5 
                "",					//Info6 
                "",					//Info7 
                "",					//Info8 
                "",					//Info9 
                ""); 				//Info10 
        

    }

    public void modificaVisualizzazioneFinePesatura() {

        elemBut[5].setEnabled(true);
        elemBut[6].setEnabled(false);
        elemBut[7].setEnabled(true);
        elemBut[8].setEnabled(true);
        butFreccia.setEnabled(true);

        elemLabelSimple[8].setEnabled(true);
        elemLabelSimple[9].setEnabled(false);
        elemLabelSimple[10].setEnabled(true);
        elemLabelSimple[11].setEnabled(true);

    }

}
