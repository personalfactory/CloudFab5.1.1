/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.processo.Processo;
import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabFrame;
import eu.personalfactory.cloudfab.macchina.utility.GestoreErrori;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.SwingConstants;

/**
 *
 * @author francescodigaudio Pannello Contenuto nel Finestra di Dialogo per la
 * Segnazione degli Errori che avvengono durante il Processo
 */
@SuppressWarnings("serial")
public class Pannello44_Errori extends MyStepPanel {

    //PARAMETRI PANNELLO
    private final int numImage = 1;
    public JDialog dialogFrame;
    public GestoreErrori gestoreErrori;

    //COSTRUTTORE
    public Pannello44_Errori() {

        super();
 
        dialogFrame = new JDialog(CloudFabFrame.getFRAME());
        dialogFrame.toFront();
          
        setLayer();

        gestoreErrori = new GestoreErrori(this);

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(4);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(3);
        impostaColori(2);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Colore e Posizione Testo Pulsanti
        for (int i = 0; i < elemLabelSimple.length - 1; i++) {

            elemLabelSimple[i].setForeground(elemColor[0]);
            elemLabelSimple[i].setVerticalAlignment(SwingConstants.CENTER);

        }

        elemLabelSimple[3].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, true).start();

    }

    //Impostazione Dialog Frame
    public void setDialogFrame() {

        //Dimensionamento Finestra
        dialogFrame.setBounds(
                0,
                0,
                imageSfondo.getIcon().getIconWidth(),
                imageSfondo.getIcon().getIconHeight());

        dialogFrame.setModal(true);

        //Operazione in Seguito alla Chiusura della Finestra
        dialogFrame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        //Centraggio della Finestra
        dialogFrame.setLocationRelativeTo(null);

        //Impostazione Ordine di Visualizzazione del Frame
        dialogFrame.setAlwaysOnTop(true);

        //Eliminazione Decorazioni Finestra
        dialogFrame.setUndecorated(true);

        //Aggiunta del Pannello al Frame
        dialogFrame.add(this);

        //Impostazione Visibilità Finestra Principale
        dialogFrame.setVisible(false);
    }

    //Imposta Oggetto Pulizia Classe Gestore Errori
    public void setPulizia(Pulizia pulizia) {

        gestoreErrori.pulizia = pulizia;
    } 

    //Imposta Oggetto Pulizia Classe Gestore Errori
    public void setProcesso(Processo processo) {

        gestoreErrori.processo = processo;
    }

    //Inizializzazione Frame
    public void init(String titleFinestra, ArrayList<String> titleBut, String message) {

        //Aggiornamento Label Title
        elemLabelTitle[0].setText(HTML_STRINGA_INIZIO
                + titleFinestra
                + HTML_STRINGA_FINE);

        for (int i = 0; i < titleBut.size(); i++) {

            if (!titleBut.get(i).equals("")) {

                elemLabelSimple[i].setVisible(true);

                //Aggiornamento Testo Label
                elemLabelSimple[i].setText(HTML_STRINGA_INIZIO
                        + titleBut.get(i)
                        + HTML_STRINGA_FINE);

                elemBut[i].setVisible(true);

            } else {

                //Aggiornamento Testo Label
                elemLabelSimple[i].setVisible(false);

                elemBut[i].setVisible(false);

            }
        }

        //Aggiornamento Testo Descrizione Errore
        elemLabelSimple[3].setText(HTML_STRINGA_INIZIO
                + message
                + HTML_STRINGA_FINE);

        //Aggiornamento Elementi Pannello
        repaint();
        updateUI();

        //FabCloudFrame.getFRAME().setAlwaysOnTop(false);
        dialogFrame.setVisible(true);
        dialogFrame.toFront();
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String s) {

        gestoreErrori.result = Integer.parseInt(s);
        //FabCloudFrame.getFRAME().setAlwaysOnTop(true);

        dialogFrame.setVisible(false);

    }
}
