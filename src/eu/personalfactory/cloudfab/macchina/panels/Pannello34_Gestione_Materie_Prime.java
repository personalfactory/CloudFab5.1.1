package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParCompOri;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaDataFineCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaDataFineMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParComponenteOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.CalcolaQResiduaMovimento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RiconvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDettagliComponenti;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdComponentiAssegnati;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdComponentiTabellaComponentiPesatura;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdPresaPerPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdVocaboloPerIdDizionarioPerVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMovimentoMagazzinoMateriaPrima;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPreseAbilitateNonAssegnate;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiComponentiPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaTuttiValoriParametriCompPerIdComponente;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdComponentiTabellaComponentiPesaturaConChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSiloSuggeritoMovimentoMagazzinoMateriaPrima;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_TEXT_FIELD;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_BLOCCO_PORTA_COCLEA_A;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudFont;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina; 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdom.Element;

@SuppressWarnings("serial")
public class Pannello34_Gestione_Materie_Prime extends MyStepPanel {

    int numImage = 5;
    int qMateriaPrimaCaricata = 0;
    public String componenteSelezionato = "";
    ArrayList<JTextField> txtFieldAuxList;
    ArrayList<String> datiComp;
    int id_mov_ori = 0;
    int id_comp = 0;
    ArrayList<String> prese;
    int id_presa_vis = 0;
    boolean caricamentoInCorso = false;
    int id_movimento_inserito, id_ciclo_inserito;
    boolean carica;
    String q_residua = "0";
    int index;
    int indice;
    String silo_suggerito; 

    //COSTRUTTORE
    public Pannello34_Gestione_Materie_Prime() {

        super();

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(8);
        impostaDimLabelSimple(55);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(11);
        impostaDimLabelProg(0);
        impostaDimLabelBut(8);
        impostaColori(9);

        //Inserimento Tastiera
        inserisciTastiera();

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Inserimento Pulsante Info
        inserisciButtonInfo();

        //Inserimento Area di Testo
        inserisciTextField();

        inserisciTextFieldAux(2);

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Vel Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[2]);

        //Inizializzazione Colore textField
        txtField.setForeground(elemColor[3]);

        //Inizializzazione Colori Label Simple - Materia Prima Selezionata
        elemLabelSimple[46].setForeground(elemColor[4]);

        //Inizializzazione Colori Label Simple - Codice Materia Prima
        elemLabelSimple[47].setForeground(elemColor[4]);

        //Inizializzazione Colori Label Simple - Codice Materia Prima
        elemLabelSimple[50].setForeground(elemColor[4]);
        
        //Inizializzazione Colori Label Simple - Silo Suggerito
        elemLabelSimple[54].setForeground(elemColor[8]);

    }

    //Inizializzazione Pannello
    public void initPanel(int index) {

    	 
        componenteSelezionato = "";
        
        /////////////////////////////////////////// Inizializzazione label silo suggerito
        elemLabelSimple[54].setVisible(false);
        elemLabelSimple[54].setText("");
        silo_suggerito = "";
         
        this.index = index;

        tastiera.abilitaSoloNumeri(false);

        q_residua = "0";

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        modificaPannello(0);
        
        tastiera.assegnaTextField(txtField);
         
    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 852, ParametriSingolaMacchina.parametri.get(111)));
                //Aggiornamento Label Tipo Title Tabella
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 859, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 860, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[3].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 861, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 862, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[5].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 863, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[6].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 864, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[7].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 865, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[8].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 866, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[9].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 867, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 868, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 857, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 858, ParametriSingolaMacchina.parametri.get(111)));
 
                elemLabelSimple[49].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1065, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[51].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1065, ParametriSingolaMacchina.parametri.get(111)));
                
            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
     
        if (button.equals(elemBut[0].getName())) {

            /////////////
            // CARICA ///
            /////////////
        	 
            //Modifica Impostazioni Visibilità Pannello
            modificaPannello(1);
            carica = true;
        } else if (button.equals(elemBut[1].getName())) {

            //////////////
            // SCARICA ///
            //////////////
            //Modifica Impostazioni Visibilità Pannello
            modificaPannello(7);
            carica = false;
            
        } else if (button.equals(elemBut[2].getName())) {

            ///////////////////////////////////////
            // OK CONFERMA CODICE MATERIA PRIMA ///
            ///////////////////////////////////////
            controllaCodiceMateriaPrima();

        } else if (button.equals(elemBut[3].getName())) {

            /////////////////////////////////////////
            // OK CONFERMA QUANTITA MATERIA PRIMA ///
            /////////////////////////////////////////
            controllaQuantitaMateriaPrima();

            modificaPannello(4);

        } else if (button.equals(elemBut[4].getName())) {

            ///////////////////
            // PRESA DIETRO ///
            ///////////////////
            incrementaPresaVisualizzata(false);

        } else if (button.equals(elemBut[5].getName())) {

            ///////////////////
            // PRESA AVANTI ///
            ///////////////////
            incrementaPresaVisualizzata(true);

        } else if (button.equals(elemBut[6].getName())) {

            ////////////////////////
            // OK CONFERMA PRESA ///
            ////////////////////////
            modificaPannello(5);

        } else if (button.equals(elemBut[7].getName())) {

            if (carica) {

                ////////////////////////////////
                // AVVIO CARICA CONTENITORE  ///
                ////////////////////////////////
                if (!caricamentoInCorso) {

                    caricamentoInCorso = true;
                    modificaPannello(6);

                } else {
                	 
                	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(507))) {
                		GestoreIO_ModificaOut(
                				Integer.toString(Integer.parseInt(USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A) + indice*3) + 
                				OUTPUT_SEP_CHAR + 
                				Integer.toString(Integer.parseInt(USCITA_LOGICA_BLOCCO_PORTA_COCLEA_A) + indice) + 
                				OUTPUT_SEP_CHAR + 
                				Integer.toString(Integer.parseInt(USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_A) + indice), 
                				OUTPUT_FALSE_CHAR +  OUTPUT_SEP_CHAR +
                				OUTPUT_FALSE_CHAR +  OUTPUT_SEP_CHAR + 
                				OUTPUT_FALSE_CHAR);
                	}

                    caricamentoInCorso = true;
                    
                    AggiornaDataFineMovimentoMagazzino(id_movimento_inserito);

                    AggiornaDataFineCiclo(id_ciclo_inserito);

                    modificaPannello(0);

                }
            } else {
                
                gestoreScambioPannello(1);

            }
        }

    }

    public void modificaPannello(int stato) { 
    	 
        switch (stato) {
            case 0:
                for (int i = 0; i < txtFieldAuxList.size(); i++) {
                    txtFieldAuxList.get(i).setText("");
                    txtFieldAuxList.get(i).setVisible(false);
                }
                butInfo.setEnabled(true);
                butFreccia.setEnabled(true);
                caricamentoInCorso = false;
                id_comp = 0;
                id_presa_vis = 0; 
                aggiornaDettagliInfoMateriePrime();
                impostaVisibilitaAiuto(true);
                tastiera.impostaVisibilitaTastiera(false);
                txtField.setVisible(false);
                butInfo.setVisible(false);

                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 853, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 854, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                elemBut[0].setVisible(true);
                elemBut[1].setVisible(true);
                elemBut[2].setVisible(false);
                elemBut[3].setVisible(false);
                elemBut[4].setVisible(false);
                elemBut[5].setVisible(false);
                elemBut[6].setVisible(false);
                elemBut[7].setVisible(false);

                for (int i = 0; i < 46; i++) {
                    elemLabelSimple[i].setVisible(true);
                }
                for (int i = 1; i < 5; i++) {
                    elemLabelTitle[i].setVisible(true);
                }
                elemLabelTitle[5].setVisible(false);
                for (JLabel elemLabelPlu : elemLabelPlus) {
                    elemLabelPlu.setVisible(false);
                }
                elemLabelSimple[46].setText("");
                elemLabelSimple[46].setVisible(false);
                elemLabelTitle[6].setVisible(false);

                elemLabelSimple[47].setText("");
                elemLabelSimple[47].setVisible(false);

                elemLabelSimple[48].setVisible(false);
                elemLabelSimple[49].setVisible(false);
                elemLabelSimple[50].setVisible(false);
                elemLabelSimple[51].setVisible(false);

                elemLabelSimple[52].setVisible(false);
                elemLabelSimple[52].setText("");

                elemLabelSimple[53].setVisible(false);
                elemLabelSimple[53].setText("");

                elemLabelTitle[7].setVisible(false);
                elemLabelTitle[8].setVisible(false);
                elemLabelTitle[9].setVisible(false);
                elemLabelTitle[10].setVisible(false);

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                labelImageAux[2].setVisible(false);
                labelImageAux[3].setVisible(false);
                labelImageAux[4].setVisible(true);

                break;
            case 1:

                impostaVisibilitaAiuto(false);
                tastiera.impostaVisibilitaTastiera(true);
                txtField.setVisible(true);
                butInfo.setVisible(true);
                tastiera.assegnaTextField(txtField);
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 855, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 856, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);
                elemBut[0].setVisible(false);
                elemBut[1].setVisible(false);
                for (int i = 0; i < 46; i++) {
                    elemLabelSimple[i].setVisible(false);
                }
                for (int i = 1; i < 5; i++) {
                    elemLabelTitle[i].setVisible(false);
                }
                elemLabelTitle[5].setVisible(true);
                labelImageAux[0].setVisible(true);
                labelImageAux[4].setVisible(false);
                
                ArrayList<Integer> compArray; 
                String[] componenti; 
                
                
                int j=0; 
                compArray = TrovaIdComponentiTabellaComponentiPesaturaConChimica();
                componenti = new String[Math.max(elemLabelPlus.length, compArray.size())];

                for (int i=0; i<componenti.length; i++) {

                	componenti[i]="";
                }
                for (int i = 0; i < componenti.length; i++) {

                	if (i < compArray.size()) { 
                		
                		if (!TrovaValoreParametriComponentePerIdComp(compArray.get(i)).get(9).equals("-") && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(512))) {
         
                        	componenti[j] = HTML_STRINGA_INIZIO + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compArray.get(i), ParametriSingolaMacchina.parametri.get(111))) 
                        	+ " ("+ TrovaValoreParametriComponentePerIdComp(compArray.get(i)).get(9) + " " + TrovaValoreParametriComponentePerIdComp(compArray.get(i)).get(10) +  ")"+HTML_STRINGA_FINE;
                    		
                        } else {
                		 
                        	componenti[j] = TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compArray.get(i), ParametriSingolaMacchina.parametri.get(111));
                    		
                        }
                		
                		
                		
                		j++; 
                	}
                } 

                //Inizializzazione Testo Elementi Lista Selezionabile
                elemLabelPlus[0].setText(componenti[0]);
                elemLabelPlus[1].setText(componenti[1]);
                elemLabelPlus[2].setText(componenti[2]);
                elemLabelPlus[3].setText(componenti[3]);
                elemLabelPlus[4].setText(componenti[4]);
                elemLabelPlus[5].setText(componenti[5]);
                elemLabelPlus[6].setText(componenti[6]);
                elemLabelPlus[7].setText(componenti[7]);
                txtField.setText("");
                inserisciControllaSelezione();
                definisciColoriLista(elemColor[4], elemColor[5], elemColor[6]);
                definisciGestoreLista(elemLabelPlus, 0);
                definisciLista(componenti);
                startThreadControllo();
                //Impostazione Visibilità Button Freccia
                butFreccia.setVisible(true);
                for (JLabel elemLabelPlu : elemLabelPlus) {
                    elemLabelPlu.setVisible(true);
                }
                break;
            case 2:

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(true);
                labelImageAux[4].setVisible(false);
                controllaSelezione.interrompi = true;
                elemBut[2].setVisible(true);
                elemBut[2].setEnabled(true);

                //Impostazione Visibilità Lista Selezionabile
                for (JLabel elemLabelPlu : elemLabelPlus) {
                    elemLabelPlu.setVisible(false);
                }
                txtField.setVisible(false);

				elemLabelSimple[46].setVisible(true);
				componenteSelezionato = EstraiStringaHtml(componenteSelezionato);
				if (componenteSelezionato.contains("(")) {
					componenteSelezionato = componenteSelezionato.substring(0, componenteSelezionato.indexOf("(") - 1);
				}

				// Recupero id componente
				id_comp = TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_COMPONENTI,
						EstraiStringaHtml(componenteSelezionato), ParametriSingolaMacchina.parametri.get(111));

				if (!TrovaValoreParametriComponentePerIdComp(id_comp).get(9).equals("-")
						&& Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(512))) {

					elemLabelSimple[46].setText(HTML_STRINGA_INIZIO + componenteSelezionato + " ("
							+ TrovaValoreParametriComponentePerIdComp(id_comp).get(9) + " "
							+ TrovaValoreParametriComponentePerIdComp(id_comp).get(10) + ")" + HTML_STRINGA_FINE);

				} else {
					
					elemLabelSimple[46].setText(HTML_STRINGA_INIZIO + componenteSelezionato + HTML_STRINGA_FINE);

				}

                List<?> dettagliComponente = TrovaTuttiValoriParametriCompPerIdComponente(id_comp);

                datiComp = new ArrayList<>();
                for (int i = 0; i < dettagliComponente.size(); i++) {

                    datiComp.add(((ValoreParCompOri) dettagliComponente.get(i)).getValoreVariabile());

                }

                elemLabelTitle[6].setVisible(true);

                txtFieldAuxList.get(0).setVisible(true);
                txtFieldAuxList.get(0).setFocusable(true);

                txtFieldAuxList.get(0).setText(datiComp.get(5));

                if (carica) {
                    txtFieldAuxList.get(0).addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                        }
                    });

                    txtFieldAuxList.get(0).addFocusListener(new FocusListener() {

                        @Override
                        public void focusGained(FocusEvent e) {
                            txtFieldAuxList.get(0).setText(null);
                            tastiera.impostaVisibilitaTastiera(true);
                            for (JLabel elemLabelHelp1 : elemLabelHelp) {
                                elemLabelHelp1.setVisible(false);
                            }
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            //throw new UnsupportedOperationException("Not supported yet."); 
                        }
                    });

                    tastiera.assegnaTextField(txtFieldAuxList.get(0));

                } else {

                    modificaPannello(3);

                }
                break;

            case 3:

                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                labelImageAux[2].setVisible(true);
                labelImageAux[4].setVisible(false);

                elemBut[2].setEnabled(false);

                elemLabelSimple[47].setVisible(true);
                elemLabelSimple[47].setText(txtFieldAuxList.get(0).getText());
                txtFieldAuxList.get(0).setVisible(false);
                txtFieldAuxList.get(1).setVisible(true);

                elemLabelTitle[7].setVisible(true);
                elemLabelTitle[8].setVisible(true);
                elemLabelTitle[9].setVisible(true);

                elemLabelSimple[48].setVisible(true);
                elemLabelSimple[49].setVisible(true);
                elemLabelSimple[51].setVisible(true);

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                    ///////////////////////////
                    // CONVERSIONE DI PESO  ///
                    ///////////////////////////
                    elemLabelSimple[48].setText(ConvertiPesoVisualizzato(q_residua,
                            ParametriSingolaMacchina.parametri.get(338)));

                } else {

                    ///////////////////////
                    // SISTEMA METRICO  ///
                    ///////////////////////
                    elemLabelSimple[48].setText(q_residua);
                }

                elemBut[3].setVisible(true);
                elemBut[3].setEnabled(true);
                txtFieldAuxList.get(1).setText("");
                tastiera.abilitaSoloNumeri(true);
                tastiera.assegnaTextField(txtFieldAuxList.get(1));

                if (carica) {

                    txtFieldAuxList.get(1).addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            controllaQuantitaMateriaPrima();
                            modificaPannello(4);
                        }
                    });

                    txtFieldAuxList.get(1).addFocusListener(new FocusListener() {

                        @Override
                        public void focusGained(FocusEvent e) {
                            txtFieldAuxList.get(1).setText(null);
                            tastiera.impostaVisibilitaTastiera(true);
                            for (JLabel elemLabelHelp1 : elemLabelHelp) {
                                elemLabelHelp1.setVisible(false);
                            }
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            //throw new UnsupportedOperationException("Not supported yet."); 
                        }
                    });
                } else {

                    elemLabelSimple[48].setText(CalcolaQResiduaMovimento(
                            txtFieldAuxList.get(0).getText(),
                            Integer.toString(id_comp),
                            ParametriGlobali.parametri.get(121),
                            ParametriGlobali.parametri.get(124),
                            ParametriGlobali.parametri.get(133),
                            ParametriGlobali.parametri.get(148)));
                    txtFieldAuxList.get(1).setText(CalcolaQResiduaMovimento(
                            txtFieldAuxList.get(0).getText(),
                            Integer.toString(id_comp),
                            ParametriGlobali.parametri.get(121),
                            ParametriGlobali.parametri.get(124),
                            ParametriGlobali.parametri.get(133),
                            ParametriGlobali.parametri.get(148)));
                    modificaPannello(4);
                }

                break;
            case 4:

            
                labelImageAux[0].setVisible(false);
                labelImageAux[1].setVisible(false);
                labelImageAux[2].setVisible(false);
                labelImageAux[3].setVisible(true);
                labelImageAux[4].setVisible(false);

                elemBut[2].setEnabled(false);
                elemBut[3].setEnabled(false);

                elemBut[4].setEnabled(true);
                elemBut[5].setEnabled(true);
                elemBut[6].setEnabled(true);

                elemBut[4].setVisible(true);
                elemBut[5].setVisible(true);
                elemBut[6].setVisible(true);

                elemLabelSimple[50].setVisible(true);
                elemLabelSimple[50].setText(txtFieldAuxList.get(1).getText());
                elemLabelTitle[10].setVisible(true);
                elemLabelSimple[50].setVisible(true);
                
                /////////////////////////////////////////// Presa Suggerita
                elemLabelSimple[54].setVisible(true);
                elemLabelSimple[54].setText(silo_suggerito);

                //Inizializzazione Colori Label Simple - Codice Materia Prima
                elemLabelSimple[52].setForeground(elemColor[0]);

                if (!datiComp.get(0).equals(ParametriGlobali.parametri.get(64))) {

                    //Inizializzazione Colori Label Simple - Codice Materia Prima
                    elemLabelSimple[52].setForeground(elemColor[7]);
                    elemLabelSimple[52].setText(TrovaPresaPerIdPresa(Integer.parseInt(datiComp.get(0))));

                    elemBut[4].setEnabled(false);
                    elemBut[5].setEnabled(false);

                } else {

                    prese = TrovaPreseAbilitateNonAssegnate();
                    id_presa_vis = 0;
                    
                    /////////////////////////////////////////////////// Inizializzazione prese silo suggerito
					if (!silo_suggerito.equals("")) {
						for (int i = 0; i < prese.size(); i++) {
							if (prese.get(i).equals(silo_suggerito)) {
								id_presa_vis = i;
								break;
							}
						}
					}
                    
                    elemLabelSimple[52].setText(prese.get(id_presa_vis));
                    elemBut[4].setEnabled(true);

                }

                elemLabelSimple[52].setVisible(true);

                if (!carica) {

                    controllaQuantitaMateriaPrima();
                    modificaPannello(5);

                }

                txtFieldAuxList.get(1).setText("");
                txtFieldAuxList.get(1).setVisible(false);

                break;

            case 5:

                elemBut[4].setEnabled(false);
                elemBut[5].setEnabled(false);
                elemBut[6].setEnabled(false);
                elemBut[7].setEnabled(true);

                elemBut[4].setVisible(false);
                elemBut[5].setVisible(false);
                elemBut[7].setVisible(true);

                labelImageAux[4].setVisible(false);

                elemLabelSimple[52].setForeground(elemColor[0]);

                elemLabelSimple[53].setVisible(true);
                elemLabelSimple[53].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 869, ParametriSingolaMacchina.parametri.get(111)));
                
                /////////////////////////////////////////// Presa Suggerita
                elemLabelSimple[54].setVisible(false);
                elemLabelSimple[54].setText("");
                break;

            case 6:

                labelImageAux[4].setVisible(false);

                try {
                    /// VERIFICA ASSEGNAZIONE PRESA
                    String id_presa = datiComp.get(0);
                    String id_silo = Integer.toString(TrovaIdPresaPerPresa(elemLabelSimple[52].getText()));
                    @SuppressWarnings("unused")
					int qResidua = Integer.parseInt(q_residua);
                    
                      
                    //Verifica Associazione CONTENITORE COMPONENTE
                    if (!id_presa.equals(id_silo)) {

                        /// INSERIRE LA REGISTRAZIONE DI UN MESSAGGIO DI WARNING IN QUANTO SI E' CARICATO UN SILOS PRECEDENTEMENTE ASSOCIATO AD UN'ALTRA PRESA  //////////////////////////////////////////////////////////// NOTA 
                        //RICERCA COMPONENTE ASSOCIATO ALLA PRESA SELEZIONATA
                        ArrayList<Integer> result;

                        result = TrovaTuttiComponentiPerIdPresa(TrovaIdPresaPerPresa(elemLabelSimple[52].getText()));

                        if (result.size() > 0 && !id_silo.equals(ParametriGlobali.parametri.get(134))) {
                            //RESET COMPONENTE PRECEDENTEMENTE ASSOCIATO AL CONTENITORE
                            AggiornaValoreParComponenteOri(
                                    1,
                                    result.get(0),
                                    ParametriGlobali.parametri.get(64));
                        }

                        //ASSOCIAZIONE COMPONENTE CONTENITORE 
                        AggiornaValoreParComponenteOri(
                                1,
                                id_comp,
                                id_silo);
                    }

                    //Verifica Nuovo Codice Componente
                    if (!datiComp.get(5).equals(elemLabelSimple[47].getText())) {

                        // NUOVO CODICE COMPONENTE
                        //ASSOCIAZIONE COMPONENTE CONTENITORE 
                        AggiornaValoreParComponenteOri(
                                6,
                                id_comp,
                                elemLabelSimple[47].getText());

                        qResidua = 0;

                    }
                    String segno_operazione;

                    
                    indice = (Integer.parseInt(id_silo)-1);
                      
                    if (carica) { 
                    	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(507))) {
                    		
                    		GestoreIO_ModificaOut(
                    				Integer.toString(Integer.parseInt(USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A) + indice *3) + 
                    				OUTPUT_SEP_CHAR + 
                    				Integer.toString(Integer.parseInt(USCITA_LOGICA_BLOCCO_PORTA_COCLEA_A) + indice) + 
                    				OUTPUT_SEP_CHAR + 
                    				Integer.toString(Integer.parseInt(USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_A) + indice), 
                    				OUTPUT_TRUE_CHAR +  OUTPUT_SEP_CHAR +
                    				OUTPUT_TRUE_CHAR +  OUTPUT_SEP_CHAR + 
                    				OUTPUT_TRUE_CHAR);
                    	}
                    	 
                        segno_operazione = "1";
                        qResidua += qMateriaPrimaCaricata;
 
                        //Registra Ciclo Eseguita
                        id_ciclo_inserito = RegistraNuovoCiclo(
                                ParametriGlobali.parametri.get(126), //tipo_ciclo 
                                0, //id_ordine
                                id_comp, //id_prodotto
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
                                "",
                                "",
                                "");//info10
                        
                        //Registra Movimento di Magazzino
                        id_movimento_inserito = RegistraNuovoMovimentoMagazzino(
                                id_comp, //id_materiale
                                ParametriGlobali.parametri.get(117), //tipo_materiale
                                qMateriaPrimaCaricata, //qEffettivo
                                elemLabelSimple[47].getText(), //cod_ingresso_comp
                                DettagliSessione.getCodiceFigura(), //cod_operatore
                                segno_operazione, //segno_op
                                ParametriGlobali.parametri.get(121), //procedura
                                ParametriGlobali.parametri.get(145),//ParametriGlobali.parametri.get(122), //tipo_mov
                                ParametriGlobali.parametri.get(123), //descri_mov
                                id_silo, //id_silo
                                qMateriaPrimaCaricata, //qTeorico
                                id_ciclo_inserito,	//id_ciclo
                                ParametriGlobali.parametri.get(140), //origine_mov 
                                new Date(),			//DataMov
                                true,				//Abilitato
                                TrovaValoreParametriComponentePerIdComp(id_comp).get(9),					//Info1
                                TrovaValoreParametriComponentePerIdComp(id_comp).get(10),					//Info2 
                                "",					//Info3 					
                                "",					//Info4 
                                "",					//Info5 
                                "",					//Info6 
                                "",					//Info7 
                                "",					//Info8 
                                "",					//Info9 
                                ""); 				//Info10 
                        
                        
                        

                    } else {
                        
                        segno_operazione = "-1";
                        qResidua -= qMateriaPrimaCaricata;

                        //if (Benefit.findValoreParametriComponente(id_comp).get(1).equals("98")) {
                        //ASSOCIAZIONE COMPONENTE CONTENITORE 
                        AggiornaValoreParComponenteOri(
                                1,
                                id_comp,
                                ParametriGlobali.parametri.get(64));

                        //ASSOCIAZIONE COMPONENTE CONTENITORE 
                        AggiornaValoreParComponenteOri(
                                6,
                                id_comp,
                                "");

                        //}
                        //Registra Movimento di Magazzino
                       
                        //Registra Ciclo Eseguita
                        id_ciclo_inserito = RegistraNuovoCiclo(
                                ParametriGlobali.parametri.get(127), //tipo_ciclo 
                                0, //id_ordine
                                id_comp, //id_prodotto
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
                        
                         id_movimento_inserito = RegistraNuovoMovimentoMagazzino(
                                id_comp, //id_materiale
                                ParametriGlobali.parametri.get(117), //tipo_materiale
                                qMateriaPrimaCaricata, //qEffettivo
                                elemLabelSimple[47].getText(), //cod_ingresso_comp
                                DettagliSessione.getCodiceFigura(), //cod_operatore
                                segno_operazione, //segno_op
                                ParametriGlobali.parametri.get(124), //procedura
                                ParametriGlobali.parametri.get(119), //tipo_mov
                                ParametriGlobali.parametri.get(128), //descri_mov
                                id_silo, //id_silo
                                qMateriaPrimaCaricata, //qTeorico
                                id_ciclo_inserito,	//id_ciclo
                                ParametriGlobali.parametri.get(140), //origine_mov 
                                new Date(),			//DataMov
                                true,				//Abilitato
                                TrovaValoreParametriComponentePerIdComp(id_comp).get(9),					//Info1
                                TrovaValoreParametriComponentePerIdComp(id_comp).get(10),					//Info2 
                                "",					//Info3 					
                                "",					//Info4 
                                "",					//Info5 
                                "",					//Info6 
                                "",					//Info7 
                                "",					//Info8 
                                "",					//Info9 
                                ""); 				//Info10 
                        
                    }

                } catch (NumberFormatException e) {

                    SessionLogger.logger.log(Level.WARNING, "Errore durante la registrazione delle informazioni di carico e/o scarico del componente - e ={0}", e);

                }
                butInfo.setEnabled(false);
                butFreccia.setEnabled(false);
                tastiera.impostaVisibilitaTastiera(false);
                impostaVisibilitaAiuto(true);

                elemLabelSimple[53].setVisible(true);
                elemLabelSimple[53].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 870, ParametriSingolaMacchina.parametri.get(111)));

                break;
            case 7:
            	 
                impostaVisibilitaAiuto(false);
                labelImageAux[4].setVisible(true);
                tastiera.impostaVisibilitaTastiera(false);
                txtField.setVisible(true);
                butInfo.setVisible(true);
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 855, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 856, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);
                elemBut[0].setVisible(false);
                elemBut[1].setVisible(false);
                for (int i = 0; i < 46; i++) {
                    elemLabelSimple[i].setVisible(false);
                }
                for (int i = 1; i < 5; i++) {
                    elemLabelTitle[i].setVisible(false);
                }
                elemLabelTitle[5].setVisible(true);
                labelImageAux[0].setVisible(true);
                //Ricerca id Componenti
                ArrayList<Integer> compProdottiArray2 = TrovaIdComponentiTabellaComponentiPesatura();
                
                
                //Ricerca id Componenti
                ArrayList<Integer> compArray2 = TrovaIdComponentiAssegnati();
                //Dichiarazione Array Componenti
                String[] componenti2 = new String[Math.max(elemLabelPlus.length, compArray2.size())];
                int j2 = 0;
                //Lettura Nomi Componenti dal Dizionario in Funzione dell'Id
                for (int i = 0; i < componenti2.length; i++) {

                    if (i < compArray2.size()) {

                        for (Integer compProdottiArray1 : compProdottiArray2) {
                            if (compArray2.get(i).equals(compProdottiArray1)) {
                                //componenti2[j2] = TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compArray2.get(i), ParametriSingolaMacchina.parametri.get(111));
                                
                        		if (!TrovaValoreParametriComponentePerIdComp(compArray2.get(i)).get(9).equals("-") && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(512))) {
                 
                                	componenti2[j2] = HTML_STRINGA_INIZIO + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compArray2.get(i), ParametriSingolaMacchina.parametri.get(111))) 
                                	+ " ("+ TrovaValoreParametriComponentePerIdComp(compArray2.get(i)).get(9) + " " + TrovaValoreParametriComponentePerIdComp(compArray2.get(i)).get(10) +  ")"+HTML_STRINGA_FINE;
                            		
                                } else {
                        		 
                                	componenti2[j2] = TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compArray2.get(i), ParametriSingolaMacchina.parametri.get(111));
                            		
                                }
                        		 
                                j2++;
                                break;
                            }
                        }
                    }

                }   //Inizializzazione Array 
                for (int i = j2; i < componenti2.length; i++) {
                    componenti2[i] = "";

                }   //Inizializzazione Testo Elementi Lista Selezionabile
                elemLabelPlus[0].setText(componenti2[0]);
                elemLabelPlus[1].setText(componenti2[1]);
                elemLabelPlus[2].setText(componenti2[2]);
                elemLabelPlus[3].setText(componenti2[3]);
                elemLabelPlus[4].setText(componenti2[4]);
                elemLabelPlus[5].setText(componenti2[5]);
                elemLabelPlus[6].setText(componenti2[6]);
                elemLabelPlus[7].setText(componenti2[7]);
                txtField.setText("");
                inserisciControllaSelezione();
                definisciColoriLista(elemColor[4], elemColor[5], elemColor[6]);
                definisciGestoreLista(elemLabelPlus, 0);
                definisciLista(componenti2);
                startThreadControllo();
                //Impostazione Visibilità Button Freccia
                butFreccia.setVisible(true);
                for (JLabel elemLabelPlu : elemLabelPlus) {
                    elemLabelPlu.setVisible(true);
                }
                break;
            default:
                break;
        }
    }

    public void aggiornaDettagliInfoMateriePrime() {
 
        ArrayList<ArrayList<String>> datiComponenti = TrovaDettagliComponenti();
        
        for (int i = 0; (5 + i * 4) < 46; i++) {

            if (i < datiComponenti.size()) {
            	 
                elemLabelSimple[2 + i * 4].setText(TrovaPresaPerIdPresa(Integer.parseInt(datiComponenti.get(i).get(2))));
                
                if (!datiComponenti.get(i).get(11).equals("-") && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(512))) {
                
                	elemLabelSimple[3 + i * 4].setText(HTML_STRINGA_INIZIO + datiComponenti.get(i).get(11) + " " + datiComponenti.get(i).get(1) +HTML_STRINGA_FINE);
                	  
                } else {
                
                	elemLabelSimple[3 + i * 4].setText(HTML_STRINGA_INIZIO + datiComponenti.get(i).get(1) + HTML_STRINGA_FINE);
                }
                
                elemLabelSimple[4 + i * 4].setText(CalcolaQResiduaMovimento(
                        datiComponenti.get(i).get(7),
                        datiComponenti.get(i).get(0),
                        ParametriGlobali.parametri.get(121),
                        ParametriGlobali.parametri.get(124),
                        ParametriGlobali.parametri.get(133),
                        ParametriGlobali.parametri.get(148)));
                elemLabelSimple[5 + i * 4].setText(datiComponenti.get(i).get(7));
                 
                
            } else {
                elemLabelSimple[2 + i * 4].setText("-");
                elemLabelSimple[3 + i * 4].setText("-");
                elemLabelSimple[4 + i * 4].setText("-");
                elemLabelSimple[5 + i * 4].setText("-");

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

    //Verifica che il codice inserito corrisponda ad un movimento di magazzino per il prodotto
    public int verificaCodiceMovimento(String codice_componente) {

        return TrovaMovimentoMagazzinoMateriaPrima(
                codice_componente, //codice inserito
                id_comp, //id componente selezionato
                ParametriGlobali.parametri.get(117), //"RAW MATERIAL"
                ParametriGlobali.parametri.get(118), //"DELIVERY NOTE"
                ParametriGlobali.parametri.get(119), //"WAREHOUSE TRANSACTION"
                ParametriGlobali.parametri.get(120));       //"LOADING FOR PURCHASE"

    }

    public void controllaCodiceMateriaPrima() {

        if (txtFieldAuxList.get(0).getText() != null) {

            id_mov_ori = verificaCodiceMovimento(txtFieldAuxList.get(0).getText());

            silo_suggerito = "";
            if (id_mov_ori > 0) {

                q_residua = CalcolaQResiduaMovimento(
                        txtFieldAuxList.get(0).getText(),
                        Integer.toString(id_comp),
                        ParametriGlobali.parametri.get(121),
                        ParametriGlobali.parametri.get(124),
                        ParametriGlobali.parametri.get(133),
                        ParametriGlobali.parametri.get(148));
                
                silo_suggerito = TrovaSiloSuggeritoMovimentoMagazzinoMateriaPrima(txtFieldAuxList.get(0).getText(), ParametriGlobali.parametri.get(146));
                  

                //Modifica Impostazioni Visibilità Pannello
                modificaPannello(3);

            } else {

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(446))) {
                    
                    ///////////////////////////////////////////////
                    // IGNORA CONTROLLO MOVIMENTO DI MAGAZZINO  ///
                    ///////////////////////////////////////////////
                    
                    q_residua = "0";

                    //Modifica Impostazioni Visibilità Pannello
                    modificaPannello(3);
                    
                } else {

                    txtFieldAuxList.get(0).setText("");
                }

            }
        }
    }

    public void controllaQuantitaMateriaPrima() {

        try {
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                ///////////////////////////
                // CONVERSIONE DI PESO  ///
                ///////////////////////////
                qMateriaPrimaCaricata = (int) Double.parseDouble(RiconvertiPesoVisualizzato(txtFieldAuxList.get(1).getText(),
                        ParametriSingolaMacchina.parametri.get(338)));

            } else {

                ///////////////////////
                // SISTEMA METRICO  ///
                ///////////////////////
                qMateriaPrimaCaricata = (int) Double.parseDouble(txtFieldAuxList.get(1).getText()) * 1000;
            }

            tastiera.abilitaSoloNumeri(false);

        } catch (NumberFormatException e) {

            txtFieldAuxList.get(1).setText("");

        }
    }

    public void incrementaPresaVisualizzata(boolean incremento) {

        if (prese.size() > 0) {

            if (incremento) {

                ///////////////////////
                // INCREMENTO PRESA ///
                ///////////////////////
                id_presa_vis++;

                if (id_presa_vis >= prese.size()) {

                    id_presa_vis = 0;

                }
            } else {

                ///////////////////////
                // DECREMENTO PRESA ///
                ///////////////////////
                id_presa_vis--;

                if (id_presa_vis < 0) {

                    id_presa_vis = prese.size() - 1;

                }

            }
            elemLabelSimple[52].setText(prese.get(id_presa_vis));

            if (id_presa_vis == Integer.parseInt(datiComp.get(0)) - 1) {
                //Inizializzazione Colori Label Simple - Codice Materia Prima
                elemLabelSimple[52].setForeground(elemColor[7]);

            } else {//Inizializzazione Colori Label Simple - Codice Materia Prima
                elemLabelSimple[52].setForeground(elemColor[0]);
            }
        }
    }

    public void gestoreScambioPannello(int id_panel) {

        this.setVisible(false);

        if (id_panel == 0) {
            switch (index) {
                case 0: {

                    ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                    break;
                }
                case 1: {

                    if (pannelliCollegati.get(1) instanceof Pannello10_ScelteEffettuate) {
                        ((Pannello10_ScelteEffettuate) pannelliCollegati.get(1)).initPanel();
                    }
                    break;
                }
            }
        } else {
        	
            if (pannelliCollegati.get(2) instanceof Pannello36_ScaricoSilosMicro) {
                ((Pannello36_ScaricoSilosMicro) pannelliCollegati.get(2)).initPanel(id_comp,
                        CalcolaQResiduaMovimento(
                                txtFieldAuxList.get(0).getText(),
                                Integer.toString(id_comp),
                                ParametriGlobali.parametri.get(121),
                                ParametriGlobali.parametri.get(124),
                                ParametriGlobali.parametri.get(133),
                                ParametriGlobali.parametri.get(148)),
                        TrovaIdPresaPerPresa(elemLabelSimple[52].getText()),
                        elemLabelSimple[47].getText());
            }
        }
    }

}
