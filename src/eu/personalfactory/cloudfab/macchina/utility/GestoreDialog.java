/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoContaTempoArrestoInverter;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoContaTempoRiavvioInverter;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoPannelloPrincipaleRiavvioPeriferiche;
import eu.personalfactory.cloudfab.macchina.processo.ThreadProcessoSacchettoAggiuntivo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import java.util.ArrayList;

/**
 *
 * @author francescodigaudio
 *
 * Gestisce la Visualizzazione dei Messaggi di Errore Durante il Processo
 */
public class GestoreDialog {

    public int result;
    private ArrayList<String> titleBut;
    public Pannello45_Dialog pannello;

    public GestoreDialog(Pannello45_Dialog pannello) {
        this.pannello = pannello;
    }

    public int visualizzaMessaggio(int e) {

        pannello.valPulsanti(true);

        switch (e) {

            case 0: {

                pannello.setVisImageAux(0);

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 317, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 318, ParametriSingolaMacchina.parametri.get(111))));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 320, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + Integer.parseInt(
                                ParametriGlobali.parametri.get(26) //Benefit.findValoreParametroGlobale(26)
                        ) / 1000
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 321, ParametriSingolaMacchina.parametri.get(111)));

                new ThreadProcessoSacchettoAggiuntivo(this).start();

                pannello.init(
                        EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 319, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 1: {

                pannello.setVisImageAux(1);

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 323, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + TrovaGruppoValoreParametroRipristino(3).get(0)
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 324, ParametriSingolaMacchina.parametri.get(111)));

                pannello.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 200, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 2: {

                pannello.setVisImageAux(2);

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 653, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 650, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 652, ParametriSingolaMacchina.parametri.get(111)));

                pannello.init(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 651, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 3: {

                pannello.setVisImageAux(3);

                titleBut = new ArrayList<>();
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 669, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 678, ParametriSingolaMacchina.parametri.get(111))));

                int timer = Integer.parseInt(
                        ParametriGlobali.parametri.get(85));

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 667, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + timer
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 668, ParametriSingolaMacchina.parametri.get(111)));

                new ThreadProcessoPannelloPrincipaleRiavvioPeriferiche(this).start();

                pannello.init(
                        EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 671, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 4: {

                pannello.setVisImageAux(3);

                titleBut = new ArrayList<>();
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 674, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + Integer.parseInt(
                                ParametriGlobali.parametri.get(86)) / 1000
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 675, ParametriSingolaMacchina.parametri.get(111)));

                new ThreadProcessoContaTempoArrestoInverter(this).start();

                pannello.init(
                        EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 671, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

            case 5: {

                pannello.setVisImageAux(3);

                titleBut = new ArrayList<>();
                titleBut.add("");
                titleBut.add("");

                String message
                        = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 676, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + Integer.parseInt(
                                ParametriGlobali.parametri.get(87)) / 1000
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 677, ParametriSingolaMacchina.parametri.get(111)));

                new ThreadProcessoContaTempoRiavvioInverter(this).start();

                pannello.init(
                        EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 671, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            case 6: {

                pannello.setVisImageAux(4);
                titleBut = new ArrayList<>();

                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 1023, ParametriSingolaMacchina.parametri.get(111))));
            
                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1022, ParametriSingolaMacchina.parametri.get(111))));

                String message = ParametriSingolaMacchina.parametri.get(462);

                pannello.init(EstraiStringaHtml(
                        TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 1021, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
            
            
             case 7: {

                pannello.setVisImageAux(5);
                titleBut = new ArrayList<>();

                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 158, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(""); 

                String message = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 771, ParametriSingolaMacchina.parametri.get(111)));

                pannello.init(EstraiStringaHtml(
                        TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 183, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }
             
               case 8: {

                pannello.setVisImageAux(5);
                titleBut = new ArrayList<>();

                titleBut.add(EstraiStringaHtml(TrovaVocabolo(
                        ID_DIZIONARIO_MESSAGGI_MACCHINA, 158, ParametriSingolaMacchina.parametri.get(111))));
                titleBut.add(""); 

                String message = EstraiStringaHtml(TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 772, ParametriSingolaMacchina.parametri.get(111)));

                pannello.init(EstraiStringaHtml(
                        TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 183, ParametriSingolaMacchina.parametri.get(111))),
                        titleBut, message);

                break;
            }

        }
        return result;

    }
}
