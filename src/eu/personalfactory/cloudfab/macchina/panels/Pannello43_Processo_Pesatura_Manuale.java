package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.loggers.BilanceLogger;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.processo.ActionListenerControlloCodiceChimicaSfusaPesaturaManuale;
import eu.personalfactory.cloudfab.macchina.processo.ActionListenerControlloCodiceComponentePesaturaManuale;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import eu.personalfactory.cloudfab.macchina.processo.Processo;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoResetProcesso;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AdattaInfoPesoTeorico;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaDatiMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.DecodificaCodiceChimicaSfusa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMisceleDisponibiliPerCodChimicaSfusa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaQuantitaChimicaByProdottoByComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaCodiceMateriaPrima;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaPresenzaValiditaCodiceChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_MANUALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_TEXT_FIELD;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_MANUALE;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudFont;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdom.Element;

@SuppressWarnings("serial")
public class Pannello43_Processo_Pesatura_Manuale extends MyStepPanel {

    //Parametri Pannello 
    private final int numImage = 3;
    //Variabili
    private Processo processo;
    private ActionListenerControlloCodiceComponentePesaturaManuale ctrlCodice;
    private ActionListenerControlloCodiceChimicaSfusaPesaturaManuale ctrlChimica;
    public ArrayList<JTextField> txtFieldAuxList;
    int idCompPesaManuale = 0;
    int id_movimento_pesa_manuale = 0;
    int ultimo_pannello = 0;
    boolean pesaturaInCorso, azzeraBilanciaManuale, interrompiLetturaPesoBilanciaManuale;

    //COSTRUTTORE
    public Pannello43_Processo_Pesatura_Manuale() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(8);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(9);
        impostaDimLabelProg(0);
        impostaDimLabelBut(7);
        impostaColori(4);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Inizializzazione Colori Label Componente
        elemLabelSimple[3].setForeground(elemColor[3]);

        //Inizializzazione Colori Label Componente
        elemLabelSimple[5].setForeground(elemColor[3]);

        //Configurazione di Base Pannello
        configuraPannello();

        inserisciTextFieldAux(2);

        //Inserimento Tastiera
        inserisciTastiera();

        tastiera.impostaVisibilitaTastiera(false);

        //Creazione Action Listener Controllo Codice
        ctrlCodice = new ActionListenerControlloCodiceComponentePesaturaManuale(this);

        //Creazione Action Listener Controllo Codice
        ctrlChimica = new ActionListenerControlloCodiceChimicaSfusaPesaturaManuale(this);

        //Creazione Action Listener Text Field
        txtFieldAuxList.get(0).addActionListener(ctrlCodice);

        //Creazione Action Listener Text Field
        txtFieldAuxList.get(1).addActionListener(ctrlChimica);

        tastiera.assegnaTextField(txtFieldAuxList.get(0));

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

    }

    //Inizializzazione Pannello
    public void initPanel(Processo processo) {

        this.processo = processo;

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

    }

    //Inizializzazione Pannello
    public void impostaVisibilitaPannello() {

        setPanelVisibile();

        analizzaStatoPesatura();

    }

    public void analizzaStatoPesatura() {

        boolean presenzaElementiDaPesareManualmente = false;
        boolean presenzaChimicaDaPesareManualmente = false;

        if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))
                && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(461))
                && !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(102))) {

            ////////////////////////////////////////
            // PESATURA MANUALE - CHIMICA SFUSA  ///
            ////////////////////////////////////////
            presenzaChimicaDaPesareManualmente = true;

        } else {

            for (int i = 0; i < processo.componenti.size(); i++) {

                if (processo.componenti.get(i).getMetodoPesa().equals(ParametriGlobali.parametri.get(131))) {

                    if (!(Integer.toString(processo.componenti.get(i).getId())).equals(ParametriSingolaMacchina.parametri.get(301))) {

                        //////////////////////////////////////////////////////
                        // COMPONENTI PESATURA MANUALE (ADDITIVI - COLORI  ///
                        //////////////////////////////////////////////////////
                        if (!Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(i))) {
                            elemLabelSimple[3].setText(processo.componenti.get(i).getNome());

                            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                                ///////////////////////////
                                // CONVERSIONE DI PESO  ///
                                /////////////////////////// 
                                elemLabelSimple[4].setText(ConvertiPesoVisualizzato(
                                        Integer.toString(processo.componenti.get(i).getQuantità()),
                                        ParametriSingolaMacchina.parametri.get(338)));

                            } else {

                                ///////////////////////
                                // SISTEMA METRICO  ///
                                /////////////////////// 
                                elemLabelSimple[4].setText(Integer.toString(processo.componenti.get(i).getQuantità()));

                            }

                            idCompPesaManuale = i;
                            presenzaElementiDaPesareManualmente = true;
                            break;
                        }

                    }
                }
            }
        }

        if (pesaturaInCorso) {
             ////////////////////////////////////////////////////////////////////////////////// Modifica Novembre 2021
            if(presenzaChimicaDaPesareManualmente){
                 modificaPannello(3);
            } else{
                 modificaPannello(1);
            }
             
            //modificaPannello(1);
             ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        } else {

            if (presenzaElementiDaPesareManualmente) {

                /////////////////////////////////
                //PESA COMPONENTE DA ESEGUIRE  //
                /////////////////////////////////
                txtFieldAuxList.get(0).setVisible(true);
                txtFieldAuxList.get(0).addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                });

                txtFieldAuxList.get(0).addFocusListener(new FocusListener() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        txtFieldAuxList.get(0).setText(null);
                        //tastiera.impostaVisibilitaTastiera(true);
                        for (JLabel elemLabelHelp1 : elemLabelHelp) {
                            elemLabelHelp1.setVisible(false);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); 
                    }
                });

                modificaPannello(0);

            } else if (presenzaChimicaDaPesareManualmente) {

                ////////////////////////////////////
                //PESA CHIMICA SFUSA DA ESEGUIRE  //
                ////////////////////////////////////
                
                
                //VERIFICA CODICE DA INSERIRE 
                if ((TrovaSingoloValoreParametroRipristino(3).substring(0,ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {
                
                        ///////////////////////
                        // CHIMICA INTERNA  /// 
                        ///////////////////////
                         
                        String codiceInserito = ParametriSingolaMacchina.parametri.get(144);
                        String qChimica = TrovaQuantitaChimicaByProdottoByComp(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)),
                                Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)));
          
                   
                        registraCodice(codiceInserito, qChimica);

                        //Avvio Pesatura
                        new ThreadLetturaPesoBilanciaManuale().start();
               
                        
                }else {
                        //////////////////////////
                        // CODICE DA INSERIRE  /// 
                        //////////////////////////
                         
                txtFieldAuxList.get(1).setVisible(true);
                txtFieldAuxList.get(1).addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                });

                txtFieldAuxList.get(1).addFocusListener(new FocusListener() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        txtFieldAuxList.get(1).setText("");
                        //tastiera.impostaVisibilitaTastiera(true);
                        for (JLabel elemLabelHelp1 : elemLabelHelp) {
                            elemLabelHelp1.setVisible(false);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        //throw new UnsupportedOperationException("Not supported yet."); 
                    }
                });

                modificaPannello(2);
                }

            } else {

                processo.pesaturaManualeCompletata = true;
                processo.pannelloProcesso.elemBut[16].setVisible(false);

                gestoreScambioPannello();

            }
        }

    }
    //Lettura Informazioni da Database

    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 159, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 880, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 884, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 885, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 887, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[5].setText(ParametriSingolaMacchina.parametri.get(340));
                elemLabelTitle[6].setText(ParametriSingolaMacchina.parametri.get(340));
                elemLabelTitle[7].setText(HTML_STRINGA_INIZIO
                        + TrovaSingoloValoreParametroRipristino(2)
                        + " - "
                        + TrovaSingoloValoreParametroRipristino(3)
                        + HTML_STRINGA_FINE);
                elemLabelTitle[8].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1015, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 881, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 882, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 883, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 752, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[7].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1014, ParametriSingolaMacchina.parametri.get(111)));
            }
        }
    }

    //Scambio Pannello
    public void gestoreScambioPannello() {

        this.setVisible(false);
        ((Pannello11_Processo) pannelliCollegati.get(0)).reimpostaVis();

    }

    //Gestisce la Visibiltà ed il Contenuto degli Elementi del Pannello
    public void modificaPannello(int s) {

        ultimo_pannello = s;
        switch (s) {
            case 0: {
                labelImageAux[0].setVisible(true);
                labelImageAux[1].setVisible(false);
                labelImageAux[2].setVisible(false);

                //Pulsanti
                elemBut[1].setVisible(true);
                elemBut[2].setVisible(false);
                elemBut[3].setVisible(true);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(false);

                elemLabelSimple[0].setVisible(true);
                elemLabelSimple[1].setVisible(false);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(true);
                elemLabelSimple[4].setVisible(false);
                elemLabelSimple[5].setVisible(false);
                elemLabelSimple[5].setText("0");
                elemLabelSimple[7].setVisible(false);

                elemLabelTitle[1].setVisible(true);
                elemLabelTitle[2].setVisible(true);
                elemLabelTitle[3].setVisible(true);
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 885, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setVisible(false);
                elemLabelTitle[5].setVisible(false);
                elemLabelTitle[6].setVisible(false);
                elemLabelTitle[8].setVisible(false);

                tastiera.impostaVisibilitaTastiera(false);
                tastiera.assegnaTextField(txtFieldAuxList.get(0));
                break;

            }
            case 1: {

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(true);
                labelImageAux[2].setVisible(false);

                //Pulsanti
                elemBut[1].setVisible(false);
                elemBut[2].setVisible(true);
                elemBut[3].setVisible(true);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(false);

                elemLabelSimple[0].setVisible(false);
                elemLabelSimple[1].setVisible(true);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(true);
                elemLabelSimple[4].setVisible(true);
                elemLabelSimple[5].setVisible(true);
                elemLabelSimple[7].setVisible(false);

                elemLabelTitle[1].setVisible(false);
                elemLabelTitle[2].setVisible(true);
                elemLabelTitle[3].setVisible(true);
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 886, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setVisible(true);
                elemLabelTitle[5].setVisible(true);
                elemLabelTitle[6].setVisible(true);
                elemLabelTitle[8].setVisible(false);

                tastiera.impostaVisibilitaTastiera(false);
                tastiera.assegnaTextField(txtFieldAuxList.get(0));
                txtFieldAuxList.get(0).setText("");
                txtFieldAuxList.get(0).setVisible(false);
                break;
            }
            case 2: {

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                labelImageAux[2].setVisible(true);

                //Pulsanti
                elemBut[1].setVisible(false);
                elemBut[2].setVisible(false);
                elemBut[3].setVisible(false);
                elemBut[5].setVisible(true);
                elemBut[6].setVisible(false);

                elemLabelSimple[0].setVisible(false);
                elemLabelSimple[1].setVisible(false);
                elemLabelSimple[2].setVisible(false);
                elemLabelSimple[3].setVisible(false);
                elemLabelSimple[4].setVisible(false);
                elemLabelSimple[5].setVisible(false);
                elemLabelSimple[7].setVisible(true);

                elemLabelTitle[1].setVisible(false);
                elemLabelTitle[2].setVisible(false);
                elemLabelTitle[3].setVisible(false);
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 886, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setVisible(false);
                elemLabelTitle[5].setVisible(false);
                elemLabelTitle[6].setVisible(false);
                elemLabelTitle[8].setVisible(true);

                tastiera.impostaVisibilitaTastiera(true);
                tastiera.assegnaTextField(txtFieldAuxList.get(1));

                txtFieldAuxList.get(1).setText("");
                txtFieldAuxList.get(1).setEnabled(true);
                txtFieldAuxList.get(1).setVisible(true);

                break;
            }

            case 3: {

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(true);
                labelImageAux[2].setVisible(false);

                //Pulsanti
                elemBut[1].setVisible(false);
                elemBut[2].setVisible(true);
                elemBut[3].setVisible(false);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(true);
                elemLabelSimple[0].setVisible(false);
                elemLabelSimple[1].setVisible(true);
                elemLabelSimple[2].setVisible(true);
                elemLabelSimple[3].setVisible(true);
                elemLabelSimple[4].setVisible(true);
                elemLabelSimple[5].setVisible(true);
                elemLabelSimple[7].setVisible(false);

                elemLabelSimple[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1016, ParametriSingolaMacchina.parametri.get(111)));

                try {
                    int value = Integer.parseInt(TrovaSingoloValoreParametroRipristino(103));

                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                        ///////////////////////////
                        // CONVERSIONE DI PESO  ///
                        /////////////////////////// 
                        elemLabelSimple[4].setText(ConvertiPesoVisualizzato(
                                Integer.toString(value),
                                ParametriSingolaMacchina.parametri.get(338)));

                    } else {

                        ///////////////////////
                        // SISTEMA METRICO  ///
                        /////////////////////// 
                        elemLabelSimple[4].setText(Integer.toString(value));
                    }

                } catch (NumberFormatException e) {

                    ProcessoLogger.logger.log(Level.SEVERE, "Pesatura Manuale - Errore conversione Pesatura - Verificare Codice e={0}", e);
                }

                elemLabelTitle[1].setVisible(false);
                elemLabelTitle[2].setVisible(true);
                elemLabelTitle[3].setVisible(true);
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 886, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setVisible(true);
                elemLabelTitle[5].setVisible(true);
                elemLabelTitle[6].setVisible(true);
                elemLabelTitle[8].setVisible(false);

                tastiera.impostaVisibilitaTastiera(false);
                tastiera.assegnaTextField(txtFieldAuxList.get(0));
                txtField.setVisible(false);
                txtFieldAuxList.get(0).setVisible(false);
                txtFieldAuxList.get(1).setVisible(false);
                break;
            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        switch (button) {

            case "0": {

                //////////////////////////////////////////
                // NASCONDI PANNELLO PESATURA MANUALE  ///
                ////////////////////////////////////////// 
                gestoreScambioPannello();

                break;
            }

            case "1": {
                ///////////////////////////////////////////////
                // ELEMENTI PESA MANUALE PULSANTE TASTIERA  ///
                ////////////////////////////////////////////// 

                tastiera.impostaVisibilitaTastiera(!tastiera.getVisibilitaTastiera());

                txtFieldAuxList.get(0).setText("");

                break;
            }

            case "2": {

                /////////////////////////////////////////////
                // ELEMENTI PESA MANUALE PULSANTE AZZERA  ///
                /////////////////////////////////////////////
                azzeraBilanciaManuale = true;
                break;
            }
            case "3": {

                ////////////////////////////////////////////
                // CONFERMA PESATURA MANUALE COMPONENTI  ///
                //////////////////////////////////////////// 
                if (!pesaturaInCorso) {
                    verificaCodice();

                } else {

                    if ((Double.parseDouble(elemLabelSimple[5].getText()) - Double.parseDouble(elemLabelSimple[4].getText()) >= 0
                            && Double.parseDouble(elemLabelSimple[5].getText()) - Double.parseDouble(elemLabelSimple[4].getText()) <= processo.componenti.get(idCompPesaManuale).getTolleranzaEccesso())
                            || (Double.parseDouble(elemLabelSimple[4].getText()) - Double.parseDouble(elemLabelSimple[5].getText()) >= 0
                            && (Double.parseDouble(elemLabelSimple[4].getText()) - Double.parseDouble(elemLabelSimple[5].getText()) <= processo.componenti.get(idCompPesaManuale).getTolleranzaDifetto()))) {

                        //Registrazione Movimento di Magazzino
                        AggiornaDatiMovimentoMagazzino(id_movimento_pesa_manuale, (int) Double.parseDouble(elemLabelSimple[5].getText()));

                        //Registra Pesa Componente Eseguita
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(25) + idCompPesaManuale,
                                25,
                                "true",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Aggiornamento Formula Effettiva
                        String formEffettiva = TrovaSingoloValoreParametroRipristino(28);

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {
                            /////////////////////////////////////////////////
                            // REGISTRAZIONE INFO AGGIUNTIVE ABILITITATA  ///
                            /////////////////////////////////////////////////

                            if (formEffettiva.equals("")) {
                                formEffettiva = formEffettiva
                                        + processo.componenti.get(idCompPesaManuale).getId()
                                        + ParametriGlobali.parametri.get(22)
                                        + AdattaInfoPesoTeorico((int) Double.parseDouble(elemLabelSimple[5].getText())) //"1200"
                                        + ParametriGlobali.parametri.get(22)
                                        + AdattaInfoPesoTeorico(processo.componenti.get(idCompPesaManuale).getQuantità());
                            } else {
                                formEffettiva = formEffettiva
                                        + ParametriGlobali.parametri.get(21)
                                        + processo.componenti.get(idCompPesaManuale).getId()
                                        + ParametriGlobali.parametri.get(22)
                                        + AdattaInfoPesoTeorico((int) Double.parseDouble(elemLabelSimple[5].getText())) //"1200"
                                        + ParametriGlobali.parametri.get(22)
                                        + AdattaInfoPesoTeorico(processo.componenti.get(idCompPesaManuale).getQuantità());
                            }
                        } else {

                            /////////////////////////////////////////////////////
                            // REGISTRAZIONE INFO AGGIUNTIVE NON ABILITITATA  ///
                            /////////////////////////////////////////////////////
                            if (formEffettiva.equals("")) {
                                formEffettiva = formEffettiva
                                        + processo.componenti.get(idCompPesaManuale).getId()
                                        + ParametriGlobali.parametri.get(22)
                                        + (int) Double.parseDouble(elemLabelSimple[5].getText()); //"1222";
                            } else {
                                formEffettiva = formEffettiva
                                        + ParametriGlobali.parametri.get(21)
                                        + processo.componenti.get(idCompPesaManuale).getId()
                                        + ParametriGlobali.parametri.get(22)
                                        + (int) Double.parseDouble(elemLabelSimple[5].getText());//"1222";
                            }
                        }

                        //Aggiornamento Record Formula Effettiva
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(28),
                                28,
                                formEffettiva,
                                ParametriSingolaMacchina.parametri.get(15));

                        pesaturaInCorso = false;
                        interrompiLetturaPesoBilanciaManuale = true;

                        TimeLineLogger.logger.log(Level.INFO, "Fine Pesatura Manuale - id_ciclo={0}", TrovaSingoloValoreParametroRipristino(91));

                    }
                }
                break;
            }
            case "4": {

                //////////////////////
                // PULSANTE PANIC  ///
                //////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.warning("Digitato Pulsante di Reset dal Pannello Processo");

                new ThreadProcessoResetProcesso(processo).start();

                break;
            }

            case "5": {

                ///////////////////////
                // VERIFICA CODICE  ///
                ///////////////////////
                //Memorizzazione Logger Processo
                ProcessoLogger.logger.info("Digitato Pulsante VERIFICA codice chimica");

                if (!txtFieldAuxList.get(1).getText().equals("")) {
                    //Analisi Testo Text Field
                    txtFieldAuxList.get(1).postActionEvent();

                }

                break;
            }

            case "6": {

                /////////////////////////////////////////
                // CONFERMA PESATURA MANUALE CHIMICA  ///
                ///////////////////////////////////////// 
                if ((Double.parseDouble(elemLabelSimple[5].getText()) - Double.parseDouble(elemLabelSimple[4].getText()) >= 0
                        && Double.parseDouble(elemLabelSimple[5].getText()) - Double.parseDouble(elemLabelSimple[4].getText()) <= Double.parseDouble(ParametriGlobali.parametri.get(6)))
                        || (Double.parseDouble(elemLabelSimple[4].getText()) - Double.parseDouble(elemLabelSimple[5].getText()) >= 0
                        && (Double.parseDouble(elemLabelSimple[4].getText()) - Double.parseDouble(elemLabelSimple[5].getText()) <= Double.parseDouble(ParametriGlobali.parametri.get(6))))) {
                ProcessoLogger.logger.info("Digitato Pulsante CONFERMA PESATURA chimica");
                //Registra Pesa Compound Chimico Eseguita
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(102),
                        102,
                        "true",
                        ParametriSingolaMacchina.parametri.get(15));

                //Quantitativo chimica
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(43),
                        43, 
                        elemLabelSimple[5].getText(),
                        ParametriSingolaMacchina.parametri.get(15));

                pesaturaInCorso = false;
                interrompiLetturaPesoBilanciaManuale = true;

                TimeLineLogger.logger.log(Level.INFO, "Fine Pesatura Manuale Chimica - id_ciclo={0}", TrovaSingoloValoreParametroRipristino(91));

                }
                break;
            }
        }
    }

    public void verificaCodice() {

        String codice = txtFieldAuxList.get(0).getText();
        txtFieldAuxList.get(0).setText("");

        if (controlloCodicePesaturaManuale(codice)) {

            txtFieldAuxList.get(0).setVisible(false);
            tastiera.impostaVisibilitaTastiera(false);

            modificaPannello(1);

            //Registrazione Movimento di Magazzino
            id_movimento_pesa_manuale = RegistraNuovoMovimentoMagazzino(processo.componenti.get(idCompPesaManuale).getId(), //id_materiale
                    ParametriGlobali.parametri.get(117), //tipo_materiale
                    0, //qEffettivo
                    processo.componenti.get(idCompPesaManuale).getCodiceMovimento_Magazzino(), //cod_ingresso_comp
                    DettagliSessione.getCodiceFigura(), //cod_operatore
                    "-1", //segno_op
                    ParametriGlobali.parametri.get(148), //procedura
                    ParametriGlobali.parametri.get(119), //tipo_mov
                    ParametriGlobali.parametri.get(132), //descri_mov
                    processo.componenti.get(idCompPesaManuale).getPresa().substring(0, 1), //id_silo
                    processo.componenti.get(idCompPesaManuale).getQuantità(), Integer.parseInt(TrovaSingoloValoreParametroRipristino(91)),//id_ciclo
                    ParametriGlobali.parametri.get(140), //origine_mov 
                    new Date(),			//DataMov
                    true,				//Abilitato
                    TrovaValoreParametriComponentePerIdComp(processo.componenti.get(idCompPesaManuale).getId()).get(9),					//Info1
                    TrovaValoreParametriComponentePerIdComp(processo.componenti.get(idCompPesaManuale).getId()).get(10),					//Info2 
                    "",					//Info3 					
                    "",					//Info4 
                    "",					//Info5 
                    "",					//Info6 
                    "",					//Info7 
                    "",					//Info8 
                    "",					//Info9 
                    ""); 				//Info10 
               
            new ThreadLetturaPesoBilanciaManuale().start();
        }

    }

    public void verificaCodiceChimica() {

        //Lettura del codice Inserito
        String codiceInserito = txtFieldAuxList.get(1).getText().toUpperCase();

        //Reset Text Field
        txtFieldAuxList.get(1).setText("");

        //Controllo Chimica Interna
        if ((TrovaSingoloValoreParametroRipristino(3).substring(
                0,
                ParametriSingolaMacchina.parametri.get(302).length()).equals(
                ParametriSingolaMacchina.parametri.get(302)))) {

            //////////////////////////////
            // CODICE CHIMICA SBLOCCO  ///
            ////////////////////////////// 
            String nomeProdotto = TrovaSingoloValoreParametroRipristino(3);

            for (int i = 0; i < nomeProdotto.length(); i++) {

                if (nomeProdotto.charAt(i) == ParametriSingolaMacchina.parametri.get(303).charAt(0)) {
                    nomeProdotto = nomeProdotto.substring(i + 1, nomeProdotto.length());
                    break;

                }

            }
            if (nomeProdotto.equals(codiceInserito)) {

                String qChimica = TrovaQuantitaChimicaByProdottoByComp(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)),
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)));

                registraCodice(codiceInserito, qChimica);

                //Avvio Pesatura
                new ThreadLetturaPesoBilanciaManuale().start();

            }

        } else {

            /////////////////////////////////////
            // CODICE CHIMICA SFUSO STANDARD  ///
            /////////////////////////////////////
            ArrayList<String> dettagliCodice = DecodificaCodiceChimicaSfusa(codiceInserito);
            boolean codiceFormalmenteCorretto = true;
            for (int i = 0; i < dettagliCodice.size(); i++) {

                if (dettagliCodice.get(i).equals("")) {
                    codiceFormalmenteCorretto = false;
                    break;
                }
            }

            if (codiceFormalmenteCorretto) { 
                ////////////////////////////////////////////
                // CODICE INSERITO FORMALMENTE CORRETTO  ///
                ////////////////////////////////////////////
                if (dettagliCodice.get(0).substring(0, ParametriGlobali.parametri.get(19).length()).equals(
                        ParametriGlobali.parametri.get(19))
                        && (VerificaPresenzaValiditaCodiceChimica(codiceInserito))
                        && (dettagliCodice.get(0).substring(ParametriGlobali.parametri.get(19).length(),
                                TrovaSingoloValoreParametroRipristino(2).length() + 1).equals(TrovaSingoloValoreParametroRipristino(2))
                        && (TrovaMisceleDisponibiliPerCodChimicaSfusa(codiceInserito) > 0))) {
                    
                    /////////////////////
                    // CODICE VALIDO  ///
                    /////////////////////
                    registraCodice(codiceInserito, dettagliCodice.get(1));

                    //Avvio Pesatura
                    new ThreadLetturaPesoBilanciaManuale().start();
                }

            }

        }

    }
    //Dichiarazione Area di Testo per Inserimento Caratteri

    public void inserisciTextFieldAux(int numTextField) {

        txtFieldAuxList = new ArrayList<>();

        for (int i = 0; i < numTextField; i++) {

            //Lettura chiave file xml 
            Element elm = root.getChild(XML_TEXT_FIELD + i);

            JTextField txtFieldAux = new JTextField();

            //Posizionamento e dimenzionameno dei Label 
            txtFieldAux.setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                    ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

            //Settaggio Font 
            txtFieldAux.setFont(FabCloudFont.setDimensione(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

            //Settaggio Trasparenze 
            txtFieldAux.setOpaque(false);

            //Settaggio Bordi 
            txtFieldAux.setBorder(null);

            //Impostazione Nome TextField
            txtFieldAux.setName("BASETXTFIELD");

            txtFieldAux.setVisible(false);

            //Text Field
            add(txtFieldAux);

            txtFieldAuxList.add(txtFieldAux);

        }
    }

    public boolean controlloCodicePesaturaManuale(String codice) {

        return VerificaCodiceMateriaPrima(
                processo.componenti.get(idCompPesaManuale).getId(),
                codice);

    }

    private class ThreadLetturaPesoBilanciaManuale extends Thread {

        @Override
        public void run() {

            interrompiLetturaPesoBilanciaManuale = false;
            pesaturaInCorso = true;
            ClientPesaTLB4 pesaManuale;
            double valore_peso = 0;

            try {

//                pesaManuale = new ClientPesaTLB4(
//                        ID_BILANCIA_MANUALE,
//                        ParametriSingolaMacchina.parametri.get(150),
//                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

            	pesaManuale = new ClientPesaTLB4(ID_BILANCIA_MANUALE);
            	  
                //IMPLEMENTARE INIZIALIZZAZIONE CLIENT DI PESA
                while (!interrompiLetturaPesoBilanciaManuale
                        && processo.pannelloProcesso.isVisible()
                        && pesaturaInCorso) {

                    if (azzeraBilanciaManuale) {
                        azzeraBilanciaManuale = false;

                        pesaManuale.commutaNetto();

                    } else {

                        valore_peso = Double.parseDouble(pesaManuale.pesoNetto())
                                / Integer.parseInt(ParametriSingolaMacchina.parametri.get(223));

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                            ///////////////////////////
                            // CONVERSIONE DI PESO  ///
                            /////////////////////////// 
                            elemLabelSimple[5].setText(ConvertiPesoVisualizzato(
                                    Double.toString(valore_peso),
                                    ParametriSingolaMacchina.parametri.get(338)));

                        } else {

                            ///////////////////////
                            // SISTEMA METRICO  ///
                            /////////////////////// 
                            elemLabelSimple[5].setText(Double.toString(valore_peso));
                        }

                    }

                    try {
                        ThreadLetturaPesoBilanciaManuale.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_MANUALE);
                    } catch (InterruptedException ex) {
                    }
                }

                ProcessoLogger.logger.info("Fine Pesatura Manuale");
               
                pesaturaInCorso = false;


                ThreadLetturaPesoBilanciaManuale.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_MANUALE);

                ProcessoLogger.logger.info("Fine Pesatura Manuale - Chiusura Client di Pesa");
               
                pesaManuale.chiudi();


                ProcessoLogger.logger.severe("Errore Chiusura Cliente di Pesa = ");

                ThreadLetturaPesoBilanciaManuale.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_MANUALE);


                ProcessoLogger.logger.info("Fine Pesatura Manuale - analizzaStatoPesatura");

                analizzaStatoPesatura();

            } catch (IOException | NumberFormatException ex) {

                BilanceLogger.logger.log(Level.SEVERE, "Errore creazione client bilancia Manuale - ex", ex);
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public void registraCodice(String codiceChimica, String quantitaChimica) {

        try {
            //Registrazione codice chimica
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(101),
                    101,
                    codiceChimica,
                    ParametriSingolaMacchina.parametri.get(15));

            //Codice chimica inserito      
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(57),
                    57,
                    "true",
                    ParametriSingolaMacchina.parametri.get(15));

            //Quantitativo chimica da pesare
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(103),
                    103,
                    quantitaChimica,
                    ParametriSingolaMacchina.parametri.get(15));

        } catch (Exception ex) {

            //Memorizzazione Logger Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Errore di Registrazione su Database e ={0}", ex);
        }

        modificaPannello(3);
    }

}
