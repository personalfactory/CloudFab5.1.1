/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.panels.Pannello46_ErroriAggiornamento;
import eu.personalfactory.cloudfab.macchina.processo.Processo;
import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaOrdiniNonEvasi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import java.util.ArrayList;

/**
 *
 * @author francescodigaudio
 *
 * Gestisce la Visualizzazione dei Messaggi di Errore Durante il Processo
 */
public class GestoreErrori {

    public Processo processo;
    public Pulizia pulizia;
    public int result;
    private ArrayList<String> titleBut;
    private Pannello44_Errori pannelloErrori;
    private Pannello46_ErroriAggiornamento pannelloErroriAgg;

    //COSTRUTTORE
    public GestoreErrori(Pannello44_Errori pannello) {

        this.pannelloErrori = pannello;

    }

    //COSTRUTTORE
    public GestoreErrori(Pannello46_ErroriAggiornamento pannello) {

        this.pannelloErroriAgg = pannello;

    }

    //Visualizzazione Errori relativi all'Aggiornamento
    public int visualizzaErroreAggiornamento(String message, String labelBut0, String labelBut1) {

        titleBut = new ArrayList<>();
        titleBut.add(labelBut0);//Valore restituito 0
        titleBut.add(labelBut1);//Valore restituito 1
        pannelloErroriAgg.labelImageAux[0].setVisible(false);
        pannelloErroriAgg.labelImageAux[1].setVisible(true);
        pannelloErroriAgg.init(EstraiStringaHtml(TrovaVocabolo(
                ID_DIZIONARIO_MESSAGGI_MACCHINA, 648, ParametriSingolaMacchina.parametri.get(111))), titleBut, message);
        return result;
    }

    //Visualizzazione Richiesta di Assistenza Relativa all'Aggiornamento
    public int visualizzaRichiestaAggiornamento(String message, String labelBut0, String labelBut1) {

        titleBut = new ArrayList<>();
        titleBut.add(labelBut0);// Valore restituito 0
        titleBut.add(labelBut1);// Valore restituito 1 
        pannelloErroriAgg.labelImageAux[0].setVisible(true);
        pannelloErroriAgg.labelImageAux[1].setVisible(false);
        pannelloErroriAgg.init(EstraiStringaHtml(TrovaVocabolo(
                ID_DIZIONARIO_MESSAGGI_MACCHINA, 649, ParametriSingolaMacchina.parametri.get(111))), titleBut, message);

        return result;
    }

    public int visualizzaErrore(int e) {

        switch (e) {

            case 0: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 217, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 163, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 164, ParametriSingolaMacchina.parametri.get(111)))
                        + " : "
                        + processo.esitoTestStatoContatti.get(0)
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 165, ParametriSingolaMacchina.parametri.get(111)))
                        + processo.esitoTestStatoContatti.get(1)
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 166, ParametriSingolaMacchina.parametri.get(111)))
                        + processo.esitoTestStatoContatti.get(2);

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(447))) {

                    ///////////////////////////////////////////
                    // CONTROLLO CONTATTI COCLEE ABILITATO  ///
                    ///////////////////////////////////////////
                    message += HTML_BREAK_LINE
                            + EstraiStringaHtml(TrovaVocabolo(
                                    ID_DIZIONARIO_MESSAGGI_MACCHINA, 879, ParametriSingolaMacchina.parametri.get(111))) 
                            + " "
                            + processo.esitoTestStatoContatti.get(3)
                            + ", "
                            + processo.esitoTestStatoContatti.get(4)
                            + ", "
                            + processo.esitoTestStatoContatti.get(5)
                            + ", "
                            + processo.esitoTestStatoContatti.get(6)
                            + ", "
                            + processo.esitoTestStatoContatti.get(7)
                            + ", "
                            + processo.esitoTestStatoContatti.get(8)
                            + ", "
                            + processo.esitoTestStatoContatti.get(9)
                            + ", "
                            + processo.esitoTestStatoContatti.get(10)
                            + ", "
                            + processo.esitoTestStatoContatti.get(11)
                            + ", "
                            + processo.esitoTestStatoContatti.get(12)
                            + ", "
                            + processo.esitoTestStatoContatti.get(13)
                            + ", "
                            + processo.esitoTestStatoContatti.get(14);
                }

                message += " "
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 218, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 219, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 1: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 221, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 167, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 222, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 223, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 2: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 230, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 228, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 179, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 229, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 3: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 231, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 180, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 232, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 4: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 230, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 233, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 181, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 234, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 5: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 235, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 182, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 236, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 6: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 237, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 185, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 238, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);
                break;

            }
            case 7: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 230, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 240, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 188, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 241, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 242, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 8: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 243, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 192, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 244, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 245, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 246, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "4 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 9: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 230, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 247, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 193, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 248, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 249, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 10: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 250, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 197, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 238, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 11: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 251, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 198, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 238, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 12: {
                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 252, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 199, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 238, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 13: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 203, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 253, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 183, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 14: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 204, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 254, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 183, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 15: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 205, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 255, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 183, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 16: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 239, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 206, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 256, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 183, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }

            case 17: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 224, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 225, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 226, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 227, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);
                break;
            }

            case 18: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 283, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 284, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 282, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 286, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 287, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 288, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 289, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 290, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 299, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);
                break;
            }

            case 19: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 291, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 292, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 282, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 294, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 295, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 296, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 297, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 298, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 299, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 20: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 283, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 284, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 285, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 282, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 286, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 287, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 288, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 289, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 290, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 299, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 21: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 291, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 292, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 293, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 282, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 294, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 295, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 296, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 297, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 298, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 299, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 22: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 300, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 301, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 302, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 303, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 304, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 305, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 306, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 307, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 23: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 308, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 309, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 310, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 302, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 311, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 312, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 313, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 314, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 315, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 307, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 24: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 217, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 163, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 164, ParametriSingolaMacchina.parametri.get(111)))
                        + " : "
                        + pulizia.esitoTestStatoContatti[0]
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 165, ParametriSingolaMacchina.parametri.get(111)))
                        + pulizia.esitoTestStatoContatti[1]
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 166, ParametriSingolaMacchina.parametri.get(111)))
                        + pulizia.esitoTestStatoContatti[2]
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 218, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 219, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 25: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 404, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 405, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 407, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 408, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 409, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 406, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 26: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 404, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 407, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 410, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 411, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 412, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 406, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 27: {

                // RICORDATI CHE LE STRINGHE VANNO PASSATE SENZA HTML
                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 626, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 672, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 673, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 627, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 628, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 629, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 630, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 631, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 28: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 632, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 633, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 634, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 635, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 636, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 666, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 637, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 29: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 638, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 639, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 640, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 641, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 642, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 30: {

                // RICORDATI CHE LE STRINGHE VANNO PASSATE SENZA HTML
                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 643, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 644, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 645, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 646, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 647, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 31: {

                // RICORDATI CHE LE STRINGHE VANNO PASSATE SENZA HTML
                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 661, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 660, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 662, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 663, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 664, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 32: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 681, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 682, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 683, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }

            case 33: {

                //////////////////////////////////
                // ERRORE MICRODOSATORE VUOTO  ///
                //////////////////////////////////
                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 688, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 689, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 690, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 691, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 692, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 630, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 631, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 34: {

                /////////////////////////////////////////
                // MESSAGGIO AVVERTIMENTO INVENTARIO  ///
                /////////////////////////////////////////
                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 704, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 705, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 707, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 708, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 706, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 35: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 404, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 405, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 407, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 750, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 751, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 406, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 36: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 779, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 780, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 781, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 782, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 37: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 822, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 823, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 824, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 825, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 38: {

                titleBut = new ArrayList<>();

                //FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 213, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 826, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 827, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 216, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "1 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 828, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "2 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 829, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + "3 - "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 220, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 39: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 834, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                //RICERCA ORDINI INEVASI
                ArrayList<ArrayList<String>> ordini_inevasi = TrovaOrdiniNonEvasi();
                String str_ordini_inevasi = "";
                if (ordini_inevasi.size() > 0) {

                    for (int i = 0; i < ordini_inevasi.size(); i++) {
                        str_ordini_inevasi
                                += EstraiStringaHtml(TrovaVocabolo(
                                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 837, ParametriSingolaMacchina.parametri.get(111)))
                                + " ='"
                                + ordini_inevasi.get(i).get(0)
                                + "'  "
                                + EstraiStringaHtml(TrovaVocabolo(
                                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 838, ParametriSingolaMacchina.parametri.get(111)))
                                + " ='"
                                + EstraiStringaHtml(TrovaVocabolo(
                                        ID_DIZIONARIO_PRODOTTI, Integer.parseInt(ordini_inevasi.get(i).get(2)), ParametriSingolaMacchina.parametri.get(111)))
                                + "'  "
                                + EstraiStringaHtml(TrovaVocabolo(
                                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 839, ParametriSingolaMacchina.parametri.get(111)))
                                + " ='"
                                + ordini_inevasi.get(i).get(5)
                                + " /"
                                + ordini_inevasi.get(i).get(4)
                                + "'"
                                + HTML_BREAK_LINE;
                    }
                    String message
                            = EstraiStringaHtml(TrovaVocabolo(
                                    ID_DIZIONARIO_MESSAGGI_MACCHINA, 836, ParametriSingolaMacchina.parametri.get(111)))
                            + HTML_BREAK_LINE
                            + str_ordini_inevasi;

                    pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                            ID_DIZIONARIO_MESSAGGI_MACCHINA, 835, ParametriSingolaMacchina.parametri.get(111))),
                            titleBut, message);

                    break;

                }
            }

            case 40: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 848, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 850, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE;

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 849, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 41: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 848, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 971, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 972, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 42: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 846, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1004, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        //+ processo.microdosatori_2017.get(processo.counterConfigMicro).getPresaComponente()
                        + "Z1"
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1005, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 43: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 846, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 212, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1012, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1013, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 211, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
            case 44: {

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 158, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 846, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 214, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1017, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 215, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1018, ParametriSingolaMacchina.parametri.get(111)));

                pannelloErrori.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 1019, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;

            }
        }
        return result;
    }

}
