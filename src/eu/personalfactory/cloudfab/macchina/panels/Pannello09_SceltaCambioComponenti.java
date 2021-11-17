package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiAlternativi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataAdditivPerSerieAdditivi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaListaOrdinataColoriPerSerieColori;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSerieAdditivoProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSerieColoreProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello09_SceltaCambioComponenti extends MyStepPanel {

    //VARIABILI  
    private final int numImage = 4;
    private ArrayList<ArrayList<Integer>> compAlternativi;

    //COSTRUTTORE
    public Pannello09_SceltaCambioComponenti() {

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
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(1);
        impostaDimLabelBut(9);
        impostaColori(5);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[2]);

        //Inserimento Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Vel Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, true).start();

        elemLabelSimple[0].setHorizontalAlignment(SwingConstants.CENTER);
        elemLabelSimple[1].setHorizontalAlignment(SwingConstants.CENTER);
        elemLabelSimple[2].setHorizontalAlignment(SwingConstants.CENTER);
        elemLabelSimple[3].setHorizontalAlignment(SwingConstants.CENTER);
        elemLabelSimple[4].setHorizontalAlignment(SwingConstants.CENTER);
        elemLabelSimple[0].setVerticalAlignment(SwingConstants.CENTER);
        elemLabelSimple[1].setVerticalAlignment(SwingConstants.CENTER);
        elemLabelSimple[2].setVerticalAlignment(SwingConstants.CENTER);
        elemLabelSimple[3].setVerticalAlignment(SwingConstants.CENTER);
        elemLabelSimple[4].setVerticalAlignment(SwingConstants.CENTER);

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Vocaboli Traducibili da Database
        new TrovaCompAlternativi().start();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

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
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 996, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 997, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 998, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 68, ParametriSingolaMacchina.parametri.get(111)));

                elemLabelSimple[4].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 999, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Trova Componenti Alternativi
    private class TrovaCompAlternativi extends Thread {

        @Override
        public void run() {

            //Ricerca Componenti Alternativi
            compAlternativi = TrovaComponentiAlternativi(scelte.idProdotto);

            //Inizializzazione Testi
            elemLabelSimple[0].setText("-");
            elemLabelSimple[1].setText("-");
            elemLabelSimple[2].setText("-");
            elemLabelSimple[3].setText("-");

            //Inizializzazione Colori
            elemLabelSimple[0].setForeground(elemColor[3]);
            elemLabelSimple[1].setForeground(elemColor[3]);
            elemLabelSimple[2].setForeground(elemColor[3]);
            elemLabelSimple[3].setForeground(elemColor[3]);

            //Inizializzazione Abilitazione Pulsanti
            elemBut[0].setEnabled(false);
            elemBut[1].setEnabled(false);
            elemBut[2].setEnabled(false);
            elemBut[3].setEnabled(false);
            elemBut[4].setEnabled(false);
            elemBut[5].setEnabled(false);
            elemBut[6].setEnabled(false);
            elemBut[7].setEnabled(false);

            //Assegnazione Testo Label e Abilitazione Pulsanti
            for (int i = 0; i < compAlternativi.size(); i++) {
                elemLabelSimple[i].setText(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compAlternativi.get(i).get(0), ParametriSingolaMacchina.parametri.get(111)));
                elemBut[0 + i * 2].setEnabled(true);
                elemBut[1 + i * 2].setEnabled(true);

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        if (button.equals("8")) {

            ////////////////
            // CONFERMA  ///
            ////////////////
            scelte.compSostitutivi = new ArrayList<>();

            for (int i = 0; i < compAlternativi.size(); i++) {

                int id = 0;
                ArrayList<Integer> arrayCompSost = new ArrayList<>();

                arrayCompSost.add(compAlternativi.get(i).get(0));

                for (int j = 0; j < compAlternativi.get(i).size(); j++) {

                    if (TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compAlternativi.get(i).get(j), ParametriSingolaMacchina.parametri.get(111)).equals(elemLabelSimple[i].getText())) {
                        id = j;
                    }
                }
                arrayCompSost.add(compAlternativi.get(i).get(id));

                scelte.compSostitutivi.add(arrayCompSost);

            }

            gestoreScambioPannello();

        } else {

            ///////////////////////////////////
            // PULSANTI SCAMBIO COMPONENTI  ///
            ///////////////////////////////////
            int indexBut = Integer.parseInt(button) / 2;
            int signBut = Integer.parseInt(button) % 2;

            int index = 0;

            for (int i = 0; i < compAlternativi.get(indexBut).size(); i++) {

                if (TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compAlternativi.get(indexBut).get(i), ParametriSingolaMacchina.parametri.get(111)).equals(elemLabelSimple[indexBut].getText())) {
                    index = i;
                }
            }

            if (signBut == 0) {

                if (index > 0) {
                    index--;
                } else {
                    index = compAlternativi.get(indexBut).size();
                }

            } else {
                if (index < compAlternativi.get(indexBut).size() - 1) {
                    index++;
                } else {
                    index = 0;
                }
            }

            if (index >= 0 && index < compAlternativi.get(indexBut).size()) {
                //Modifica colori
                if (index == 0) {
                    elemLabelSimple[indexBut].setForeground(elemColor[3]);
                } else {

                    elemLabelSimple[indexBut].setForeground(elemColor[4]);
                }

                //Modifica Testi
                elemLabelSimple[indexBut].setText(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, compAlternativi.get(indexBut).get(index), ParametriSingolaMacchina.parametri.get(111)));
            }
        }
    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        scelte.serie_additivo = new ArrayList<>();
        scelte.serie_colore = new ArrayList<>();

        scelte.serie_colore = TrovaSerieColoreProdotto(scelte.idProdotto);
        scelte.serie_additivo = TrovaSerieAdditivoProdotto(scelte.idProdotto);

        if ((scelte.serie_colore.get(0).equals(ParametriGlobali.parametri.get(155))
                || scelte.serie_colore.get(0).equals(""))
                || ((TrovaListaOrdinataColoriPerSerieColori(scelte.serie_colore)).length == 0)) {

            if ((scelte.serie_additivo.get(0).equals(ParametriGlobali.parametri.get(155))
                    || scelte.serie_additivo.get(0).equals(""))
                    || (TrovaListaOrdinataAdditivPerSerieAdditivi(scelte.serie_additivo)).length == 0) {

                ((Pannello10_ScelteEffettuate) pannelliCollegati.get(1)).scelte = scelte;
                ((Pannello10_ScelteEffettuate) pannelliCollegati.get(1)).initPanel();

            } else {
                ((Pannello42_SceltaAdditivo) pannelliCollegati.get(3)).initPanel();
                ((Pannello42_SceltaAdditivo) pannelliCollegati.get(3)).scelte = scelte;
                ((Pannello42_SceltaAdditivo) pannelliCollegati.get(3)).scelte.serie_additivo = scelte.serie_additivo;
            }

        } else {

            ((Pannello41_SceltaColore) pannelliCollegati.get(2)).initPanel();
            ((Pannello41_SceltaColore) pannelliCollegati.get(2)).scelte = scelte;
            ((Pannello41_SceltaColore) pannelliCollegati.get(2)).scelte.serie_colore = scelte.serie_colore;

        }

    }
}
