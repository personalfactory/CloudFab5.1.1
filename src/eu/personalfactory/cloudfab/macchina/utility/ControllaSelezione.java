package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.panels.Pannello06_SceltaDimContenitore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello22_Ricerca_Filtro_Generale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello42_SceltaAdditivo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello34_Gestione_Materie_Prime;
import eu.personalfactory.cloudfab.macchina.panels.Pannello15_Configurazione_Prese;
import eu.personalfactory.cloudfab.macchina.panels.Pannello24_Ricerca_Prodotti_Per_Cliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello41_SceltaColore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello25_Ricerca_Codici_Per_Cliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello29_RecuperaCodice;
import eu.personalfactory.cloudfab.macchina.panels.Pannello08_SceltaCliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello23_Ricerca_Codici_Chimica_Disponibili;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod_Alter;
import eu.personalfactory.cloudfab.macchina.panels.MyStepPanel;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroGen;
import eu.personalfactory.cloudfab.macchina.panels.Pannello05_SceltaNumMiscele;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroGen_Alter;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ApprossimaPesoConvertito;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.Arrotonda;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FATTORE_DIV_UNITA_MISURA_MISCELE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SCROLL_SENSIBILITA_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SCROLL_SENSIBILITA_Y;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 *
 * Thread di Controllo della Selezione
 *
 *
 */
public class ControllaSelezione extends Thread {

    //VARIABILI
    public boolean press, release, attributiDef, interrompi, setUnselectable;
    public String prValue, attributo, select;
    public MyStepPanel pannelloCorrente;
    public JTextField txtField;
    public JLabel[] labels;
    public GestoreEventiMouseLista mouseEvent;
    public GestoreScrollLista scrollEvent;
    public int indice, typeScroll;
    public String[] lista_FILTR_ORIG, lista_FILTR;
    public int[] lista_ATTR, lista_ATTR_ORIG;
    public Color colSelectable, colUnSelectable, colSelected;
    public final int FREQ = 50;

    @Override
    public void run() {

        scorriLista(indice);

        interrompi = false;

        while (!interrompi && pannelloCorrente.isVisible()) {

            //Temporizzatore Thread
            try {
                ControllaSelezione.sleep(FREQ);
            } catch (InterruptedException ex) {

            }

            switch (typeScroll) {
                case (0): {

                    ///////////////////////////
                    // SCROLLING VERTICALE  ///
                    ///////////////////////////˙
                    if (scrollEvent.isYDraggedDW()) {
                        if (scrollEvent.getDy() > SCROLL_SENSIBILITA_Y) {
                            scrollEvent.setDy(0);

                            if (indice < (lista_FILTR.length - labels.length)) {
                                indice++;
                            }
                            scorriLista(indice);
                        }
                    }
                    if (scrollEvent.isYDraggedUP()) {
                        if (scrollEvent.getDy() > SCROLL_SENSIBILITA_Y) {
                            scrollEvent.setDy(0);
                            if (indice > 0) {
                                indice--;
                            }
                            scorriLista(indice);
                        }
                    }
                    break;

                }

                case (1): {

                    /////////////////////////////
                    // SCROLLING ORIZZONTALE  ///
                    ///////////////////////////// 
                    if (scrollEvent.isXDraggedLF()) {
                        if (scrollEvent.getDx() > SCROLL_SENSIBILITA_X) {
                            scrollEvent.setDx(0);
                            if (indice > 0) {
                                indice--;
                            }
                            scorriLista(indice);
                        }
                    }
                    if (scrollEvent.isXDraggedRG()) {
                        if (scrollEvent.getDx() > SCROLL_SENSIBILITA_X) {
                            scrollEvent.setDx(0);
                            if (indice < (lista_FILTR.length - labels.length)) {
                                indice++;
                            }
                            scorriLista(indice);
                        }
                    }
                    break;
                }
            }

            //Pulsante Mouse Premuto sul Label
            if (!press) {
                if (mouseEvent.isPressed()) {

                    scrollEvent.setXYRif(mouseEvent.getX_POS(), mouseEvent.getY_POS());
                    if (attributiDef) {
                        if (!(mouseEvent.getSource().getName()).equals("0")) {
                            (mouseEvent.getSource()).setForeground(colSelected);
                            select = (mouseEvent.getSource()).getText();
                            attributo = (mouseEvent.getSource()).getName();
                        } else {
                            select = "";
                            attributo = "";
                        }
                    } else {
                        if (!setUnselectable) {
                            (mouseEvent.getSource()).setForeground(colSelected);
                            select = (mouseEvent.getSource()).getText();
                        }
                    }
                    press = true;
                    release = false;
                }
            }

            //Pulsante Mouse Rilasciato
            if (!release) {
                if (mouseEvent.isReleased()) {
                    release = true;
                    press = false;

                    if (!setUnselectable) {
                        if (!select.equals("")) {

                            interrompi = true;

                            gestorePannelli();
                        }
                    }
                }
            }

            //Controlla eventuali Cambiamenti sul Text Box e aggiorna la lista
            if (txtField.getText().equals("")) {
                if (!prValue.equals("")) {
                    System.arraycopy(lista_FILTR_ORIG, 0, lista_FILTR, 0, lista_FILTR.length);
                    if (attributiDef) {
                        System.arraycopy(lista_ATTR_ORIG, 0, lista_ATTR, 0, lista_ATTR.length);
                    }
                    scorriLista(0);
                    indice = 0;
                    prValue = "";
                }
            } else if (!txtField.getText().equals(prValue)) {
                int l = 0;
                prValue = txtField.getText();
                for (int k = 0; k < lista_FILTR_ORIG.length; k++) {
                    //if (Benefit.filtroStringa(txtField.getText(), lista_FILTR_ORIG[k])) { // la funzione filtroStringa è stata eleiminata in Benefit 5.0 e sostituita con contains
                    if (lista_FILTR_ORIG[k]!=null) {
                	if ((lista_FILTR_ORIG[k]).contains(txtField.getText())) {
                        lista_FILTR[l] = lista_FILTR_ORIG[k];
                        if (attributiDef) {
                            lista_ATTR[l] = lista_ATTR_ORIG[k];
                        }
                        l++;
                    }
                }
                    }
                for (int n = l; n < lista_FILTR.length; n++) {
                    lista_FILTR[n] = "";
                    if (attributiDef) {
                        lista_ATTR[n] = 0;
                    }
                }
                scorriLista(0);
                indice = 0;
            }
        }
         

    }

    public void scorriLista(int i) {
        select = "";
        for (int k = 0; k < labels.length; k++) {
            labels[k].setText(lista_FILTR[i + k]);

            if (attributiDef) {
                labels[k].setName(Integer.toString(lista_ATTR[i + k]));
            }

            if (pannelloCorrente instanceof Pannello06_SceltaDimContenitore && lista_ATTR[i + k] != 0) {

                //VISUALIZZAZIONE DIMENSIONE SACCHI
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                    ////////////////////////
                    // SISTEMA AMERICANO  //
                    ////////////////////////
                    String dMisc = Integer.toString(lista_ATTR[i + k]);

                    //Aggiornamento Testo Label Ausiliari
                    ((Pannello06_SceltaDimContenitore) pannelloCorrente).labelAux[k].setText(
                            ApprossimaPesoConvertito(
                            ConvertiPesoVisualizzato(dMisc, ParametriSingolaMacchina.parametri.get(338)))
                            + " "
                            + ParametriSingolaMacchina.parametri.get(340));

                } else {

                    //////////////////////////
                    // SISTEMA DECIMALE KG  //
                    //////////////////////////
                    String dMisc = Arrotonda(Double.toString(((double) lista_ATTR[i + k])
                            / FATTORE_DIV_UNITA_MISURA_MISCELE), 2, ".");

                    //Aggiornamento Testo Label Ausiliari
                    ((Pannello06_SceltaDimContenitore) pannelloCorrente).labelAux[k].setText(
                            dMisc
                            + " "
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 679, ParametriSingolaMacchina.parametri.get(111))));

                }
            }

        }

        //Aggiorna Graficamente i Label in Funzione che Siano Selezionabili o Meno
        if (attributiDef) {
            for (JLabel label : labels) {
                if (label.getName().equals("0")) {
                    label.setForeground(colUnSelectable);
                    label.setCursor(Cursor.getDefaultCursor());
                } else {
                    label.setForeground(colSelectable);
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        } else {

            for (JLabel label : labels) {
                if (!label.getText().equals("")) {
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    label.setForeground(colSelectable);
                } else {
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    label.setForeground(colUnSelectable);
                }
            }

        }

    }

    //Gestisce lo Scambio Fra i Pannelli in Funzione del Pannello

    public void gestorePannelli() {

        if (pannelloCorrente instanceof Pannello03_FiltroGen) {

            if (select.contains(HTML_STRINGA_INIZIO)) {

                select = EstraiStringaHtml(select);
            }
            pannelloCorrente.scelte.selezione = select;
            ((Pannello03_FiltroGen) pannelloCorrente).gestoreScambioPannello();

        } else if (pannelloCorrente instanceof Pannello03_FiltroGen_Alter) {

            if (select.contains(HTML_STRINGA_INIZIO)) {

                select = EstraiStringaHtml(select);
            }
            pannelloCorrente.scelte.selezione = select;
            pannelloCorrente.scelte.numeroChimicheDisponibili = Integer.parseInt(attributo);

            ((Pannello03_FiltroGen_Alter) pannelloCorrente).gestoreScambioPannello();
        } else if (pannelloCorrente instanceof Pannello03_FiltroProdCod) {
            pannelloCorrente.scelte.selezione = select;
            pannelloCorrente.scelte.numeroChimicheDisponibili = Integer.parseInt(attributo);
            ((Pannello03_FiltroProdCod) pannelloCorrente).gestoreScambioPannello();

        } else if (pannelloCorrente instanceof Pannello03_FiltroProdCod_Alter) {
            pannelloCorrente.scelte.selezione = select;
            pannelloCorrente.scelte.numeroChimicheDisponibili = Integer.parseInt(attributo);
            ((Pannello03_FiltroProdCod_Alter) pannelloCorrente).gestoreScambioPannello();

        } else if (pannelloCorrente instanceof Pannello05_SceltaNumMiscele) {
            pannelloCorrente.scelte.numeroMiscele = Integer.parseInt(select);
            ((Pannello05_SceltaNumMiscele) pannelloCorrente).gestoreScambioPannello();

        } else if (pannelloCorrente instanceof Pannello06_SceltaDimContenitore) {
            pannelloCorrente.scelte.numeroSacchetti = Integer.parseInt(select);
            pannelloCorrente.scelte.pesoSacchetto = Integer.parseInt(attributo);
            ((Pannello06_SceltaDimContenitore) pannelloCorrente).gestoreScambioPannello();

        } else if (pannelloCorrente instanceof Pannello08_SceltaCliente) {
            if (select.contains(HTML_STRINGA_INIZIO)) {

                select = EstraiStringaHtml(select);
            }
            pannelloCorrente.scelte.cliente = select;
            ((Pannello08_SceltaCliente) pannelloCorrente).gestoreScambioPannello();

        } else if (pannelloCorrente instanceof Pannello15_Configurazione_Prese) {
            ((Pannello15_Configurazione_Prese) pannelloCorrente).gestoreScambioPannello(select);
        } else if (pannelloCorrente instanceof Pannello22_Ricerca_Filtro_Generale) {
            ((Pannello22_Ricerca_Filtro_Generale) pannelloCorrente).gestoreScambioPannello(select);
        } else if (pannelloCorrente instanceof Pannello23_Ricerca_Codici_Chimica_Disponibili) {
            ((Pannello23_Ricerca_Codici_Chimica_Disponibili) pannelloCorrente).gestoreScambioPannello(select);
        } else if (pannelloCorrente instanceof Pannello24_Ricerca_Prodotti_Per_Cliente) {
            ((Pannello24_Ricerca_Prodotti_Per_Cliente) pannelloCorrente).gestoreScambioPannello(select);
        } else if (pannelloCorrente instanceof Pannello25_Ricerca_Codici_Per_Cliente) {
            ((Pannello25_Ricerca_Codici_Per_Cliente) pannelloCorrente).gestoreScambioPannello(select);
        } else if (pannelloCorrente instanceof Pannello29_RecuperaCodice) {
            ((Pannello29_RecuperaCodice) pannelloCorrente).gestorePulsanti(select);
        } else if (pannelloCorrente instanceof Pannello34_Gestione_Materie_Prime) {
            ((Pannello34_Gestione_Materie_Prime) pannelloCorrente).componenteSelezionato = select; 
            ((Pannello34_Gestione_Materie_Prime) pannelloCorrente).modificaPannello(2);
        } else if (pannelloCorrente instanceof Pannello41_SceltaColore) {
             
            pannelloCorrente.scelte.colorato = true;
            pannelloCorrente.scelte.nomeColoreSelezionato = select;
            ((Pannello41_SceltaColore) pannelloCorrente).gestoreScambioPannello();

        } if (pannelloCorrente instanceof Pannello42_SceltaAdditivo) {
             pannelloCorrente.scelte.additivato = true;
            pannelloCorrente.scelte.nomeAdditivoSelezionato = select;
            ((Pannello42_SceltaAdditivo) pannelloCorrente).gestoreScambioPannello();

        }

    }
}
