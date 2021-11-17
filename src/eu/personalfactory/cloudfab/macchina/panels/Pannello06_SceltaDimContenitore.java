package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RiconvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiProdottoById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSoluzioniSacchettiPerIdCategoria;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_INCREMENTO_MODIFICA_DIMENSIONE_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_FONT;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudFont;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.Cursor;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.jdom.Element;

@SuppressWarnings("serial")
public class Pannello06_SceltaDimContenitore extends MyStepPanel {

    //VARIABILI
    public JLabel[] labelAux;
    private Double dimMiscela = 0.0;
    private String[] soluzioniSac;
    private int[] dimensioniSacAttr;
    private int correttivoPesoSacchi, correttivoLista;

    //COSTRUTTORE
    public Pannello06_SceltaDimContenitore() {

        super();

        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(6);
        impostaDimLabelSimple(0);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(2);
        impostaDimLabelProg(1);
        impostaDimLabelBut(4);
        impostaColori(6);

        //Inizializzazione Colori Label Help
        initColorLabelHelp(elemColor[4]);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[5]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Impostazione Allineamento Label Selezionabili
        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setHorizontalAlignment(SwingConstants.RIGHT);
            elemLabelPlu.setVerticalAlignment(SwingConstants.BOTTOM);
        }

        //Impostazionedimensione Lista con Funzioni di Scroll e Selection
        labelAux = new JLabel[elemLabelPlus.length];

        //Creazione e Inserimento Label Ausiliari
        for (int j = 0; j < labelAux.length; j++) {

            labelAux[j] = new JLabel();

            //Lettura chiave file xml
            Element elm = root.getChild("labAux" + j);

            //Posizionamento e dimenzionameno dei Label
            labelAux[j].setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                    ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

            //Impostazione Font
            labelAux[j].setFont(FabCloudFont.setDimensione(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

            //Impostazione Visibilità Label Ausiliari
            labelAux[j].setVisible(true);

            //Impostazione Cursore
            labelAux[j].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            //Impostazione Colore
            labelAux[j].setForeground(elemColor[3]);

            //Aggiunta Componenti al Pannello
            add(labelAux[j]);

        }

        //Avvio Thread Caricamento Immagini di sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        correttivoPesoSacchi =0;
        correttivoLista = 0;
        
        //Inizializzazione Lista Elementi Selezionabili
        initLista();

        //Inizializzaione Label Ausiliari
        initLabelAux();

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(false);

        //Inserimento Controllo Selezione
        inserisciControllaSelezione();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

        //Lettura Elementi Lista Selezionabile
        new LeggiLista().start();

        //Impostazione Visibilità Pannello
        setPanelVisibile();

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
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 52, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 53, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 49, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 50, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Label Tipo Prog
                elemLabelProg[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 51, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Lettura Elementi Lista Selezionabile
    private class LeggiLista extends Thread {

        @Override
        public void run() {

            
          
            dimensionaSacchetti();
            
            
            //Correzione Scorrimento con Pulsanti   ///////////////////////feb 2019
            for (int i=0; i<elemLabelPlus.length; i++){
               elemLabelPlus[i].setText(""); 
                labelAux[i].setText("");
            } 
            String [] soluzioniSacCorr = null;
            int[] dimensioniSacAttrCorr = null; 
            if (correttivoLista >0 && correttivoLista<soluzioniSac.length){ 
                soluzioniSacCorr = new String [soluzioniSac.length];
                dimensioniSacAttrCorr = new int [soluzioniSac.length]; 
                int j =0; 
                for (int i =correttivoLista; i<soluzioniSac.length; i++){
                    soluzioniSacCorr[j] = soluzioniSac[i];
                    dimensioniSacAttrCorr[j] = dimensioniSacAttr[i];
                    j++;  
                } 
                for (int k =j; k<soluzioniSacCorr.length; k++){
                    soluzioniSacCorr[k] = "";
                    dimensioniSacAttrCorr[k] = 0;
                    j++;  
                } 
            } else {
                 correttivoLista =0; 
            } 
            if (soluzioniSacCorr!=null){
                soluzioniSac = soluzioniSacCorr; 
                dimensioniSacAttr = dimensioniSacAttrCorr;
            } 
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            definisciColoriLista(elemColor[0], elemColor[1], elemColor[2]);
            definisciGestoreLista(elemLabelPlus, 1);
            definisciLista(soluzioniSac);
            definisciListaAttributi(dimensioniSacAttr);
            startThreadControllo();
            
 
            //Impostazione Visibilità Button Freccia
            butFreccia.setVisible(true);

        }
    }

    //Inizializzazione Elementi Lista Selezionabile
    public void initLista() {

        //Aggiornamento Testi
        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setText("");
        }
        //Aggiornamento Visibilità
        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setVisible(true);
        }

    }

    //Inizializzazione Elementi Label Ausiliari
    public void initLabelAux() {

        //Aggiornamento Testo
        for (JLabel labelAux1 : labelAux) {
            labelAux1.setText("");
        }

    }

    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        this.setVisible(false);

        ((Pannello07_SceltaTipoContenitore) pannelliCollegati.get(1)).scelte = scelte;
        ((Pannello07_SceltaTipoContenitore) pannelliCollegati.get(1)).initPanel();

    }

    //Calcolo dimensione sacchetti
    private void dimensionaSacchetti() {

        //Inizializzazione Dimensione Miscela
        dimMiscela = 0.0;

        //Lettura dei Componenti Relativi al Prodotto Selezionato
        List<?> componentiColl = TrovaComponentiProdottoById(scelte.idProdotto);

        //Calcolo Dimensione Totale Miscela
        for (Object o : componentiColl) {
            ComponentePesaturaOri componentiProdOri = (ComponentePesaturaOri) o;
            dimMiscela += componentiProdOri.getQuantita();
        }

        //Correttivo Peso Totale Miscela
        dimMiscela -= (Double.parseDouble(ParametriSingolaMacchina.parametri.get(344)) + correttivoPesoSacchi);

        ///
        /////Correttivo Peso Totale Miscela
        ///dimMiscela -= Double.parseDouble(ParametriGlobali.parametri.get(24));
        //Ricerca Soluzioni Esistenti su Database
        int[] result = TrovaSoluzioniSacchettiPerIdCategoria(scelte.idCategoria);

        //Dichiarazione array
        soluzioniSac = new String[Math.max(elemLabelPlus.length, result.length)];
        dimensioniSacAttr = new int[Math.max(elemLabelPlus.length, result.length)];

        //Array Soluzioni Valide Confezioni
        for (int i = 0; i < result.length; i++) {
            soluzioniSac[i] = Integer.toString(result[i]);

        }
        //Array Dimensioni Valide Confezioni
        for (int i = 0; i < result.length; i++) {

            dimensioniSacAttr[i] = (int) (dimMiscela / Double.parseDouble(soluzioniSac[i]));

        }

        //Inizializzazione Elementi Lista Selezionabile
        for (int i = result.length; i < elemLabelPlus.length; i++) {
            soluzioniSac[i] = "";
        }

        //Aggiornamento Elementi Lista Selezionabile
        for (int i = 0; i < elemLabelPlus.length; i++) {
            elemLabelPlus[i].setText(soluzioniSac[i]);
        }

        //Memorizzazione Valore Peso Totale Miscela
        scelte.pesoMiscele = dimMiscela;

        //Approssimazione dimensione sacchi
        //Aggiornamento Elementi Lista Selezionabile
        if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(341))) {
            for (int i = 0; i < dimensioniSacAttr.length; i++) {

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                    ////////////////////////
                    // CONVERSIONE PESO  ///
                    ////////////////////////
                    String s = ConvertiPesoVisualizzato(
                            Integer.toString(dimensioniSacAttr[i]),
                            ParametriSingolaMacchina.parametri.get(338));

                    boolean trovato = false;
                    @SuppressWarnings("unused")
					String intero = "";
                    String decimale = "";
                    for (int j = 0; j < s.length(); j++) {
                        if (!trovato) {

                            if (s.charAt(j) != '.') {
                                intero += s.charAt(j);
                            }
                            trovato = s.charAt(j) == '.';
                        } else {
                            decimale += s.charAt(j);
                        }

                    }

                    if (!decimale.equals("")) {

                        if (Integer.parseInt(decimale) > 500) {

                            dimensioniSacAttr[i] -= Double.parseDouble(RiconvertiPesoVisualizzato("0." + Integer.toString(Integer.parseInt(decimale) - 500),
                                    ParametriSingolaMacchina.parametri.get(338)));
                        } else {
                            dimensioniSacAttr[i] -= Double.parseDouble(RiconvertiPesoVisualizzato("0." + decimale,
                                    ParametriSingolaMacchina.parametri.get(338)));
                        }

                    }

                } else //////////////////////
                // SISTEMA METRICO  ///
                ///////////////////////
                 if (dimensioniSacAttr[i] % 1000 > 0) {

                        if (dimensioniSacAttr[i] % 1000 < 500) {

                            dimensioniSacAttr[i] -= dimensioniSacAttr[i] % 1000;
                        } else {
                            dimensioniSacAttr[i] -= dimensioniSacAttr[i] % 1000;
                            dimensioniSacAttr[i] += 500;
                        }

                    }

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        //pannelloAvanti = null;
        if (button.equals(elemBut[0].getName())) {

            ///////////////////////////
            // PANNELLO PRODUZIONE  ///
            /////////////////////////// 
            controllaSelezione.interrompi = true;

            correttivoPesoSacchi += DEFAULT_INCREMENTO_MODIFICA_DIMENSIONE_SACCO;

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Elementi Lista Selezionabile
            new LeggiLista().start();

        } else if (button.equals(elemBut[1].getName())) {

            /////////////////////
            // AGGIORNAMENTO  ///
            /////////////////////
            controllaSelezione.interrompi = true;
            correttivoPesoSacchi -= DEFAULT_INCREMENTO_MODIFICA_DIMENSIONE_SACCO;

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Elementi Lista Selezionabile
            new LeggiLista().start();

        } else if (button.equals(elemBut[2].getName())) {
 
            controllaSelezione.interrompi = true;

            correttivoLista ++;

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Elementi Lista Selezionabile
            new LeggiLista().start();

        } else if (button.equals(elemBut[3].getName())) {
 
            controllaSelezione.interrompi = true;
             correttivoLista --;

            //Inserimento Controllo Selezione
            inserisciControllaSelezione();

            //Lettura Elementi Lista Selezionabile
            new LeggiLista().start();

        }

    }
}
