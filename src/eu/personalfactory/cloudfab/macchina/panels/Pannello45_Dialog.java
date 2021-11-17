/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabFrame;
import eu.personalfactory.cloudfab.macchina.utility.GestoreDialog;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author francescodigaudio Pannello Contenuto nel Finestra di Dialogo per la
 * Segnazione degli Errori che avvengono durante il Processo
 */
@SuppressWarnings("serial")
public class Pannello45_Dialog extends MyStepPanel {

    //PARAMETRI PANNELLO 
    private final int numImage = 6;
    //VARIABILI
    public JDialog dialogFrame;
    public GestoreDialog gestoreDialog;

    //COSTRUTTORE
    public Pannello45_Dialog() {

        super();

        dialogFrame = new JDialog(CloudFabFrame.getFRAME());
        dialogFrame.toFront();

        setLayer();

        gestoreDialog = new GestoreDialog(this);

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(3);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(2);
        impostaColori(2);

        //Inizializzazione Colori Label Title
        initColorLabelTitle(elemColor[1]);

        //Configurazione di Base Pannello
        configuraPannello();

        //Definizione Caratteristiche Label Simple
        for (int i = 0; i < elemLabelSimple.length - 1; i++) {

            //Impostazione Colore Label
            elemLabelSimple[i].setForeground(elemColor[0]);

            //Impostazione Allineamento Verticale
            elemLabelSimple[i].setVerticalAlignment(SwingConstants.CENTER);

        }
        //Impostazione Cursore Label Simple
        elemLabelSimple[2].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, false).start();

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
        dialogFrame.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

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

    //Inizializzazione Frame
    public void init(String titleFinestra, ArrayList<String> titleBut, String message) {

        //Aggiornamento Label Title
        elemLabelTitle[0].setText(HTML_STRINGA_INIZIO
                + titleFinestra
                + HTML_STRINGA_FINE);

        for (int i = 0; i < titleBut.size(); i++) {

            if (!titleBut.get(i).equals("")) {

                //Impostazione Visibilità Testo Pulsanti
                elemLabelSimple[i].setVisible(true);

                //Aggiornamento Testo Label
                elemLabelSimple[i].setText(HTML_STRINGA_INIZIO
                        + titleBut.get(i)
                        + HTML_STRINGA_FINE);

                //Impostazione Visibilità Pulsanti
                elemBut[i].setVisible(true);

            } else {

                //Aggiornamento Testo Label Simple
                elemLabelSimple[i].setVisible(false);

                //Impostazione Visibilità Pulsanti
                elemBut[i].setVisible(false);

            }
        }

        //Inizializzazione Messaggio Visualizzato
        setMessage(message);

        //Aggiornamento Elementi Pannello
        repaint();
        updateUI();
        //FabCloudFrame.getFRAME().setAlwaysOnTop(false);

        dialogFrame.setVisible(true);
        dialogFrame.toFront();

    }

    //Impostazione Immagine Ausiliaria Visualizzata
    public void setVisImageAux(int index) {

        for (int i = 0; i < numImage; i++) {

            if (i == index) {

                labelImageAux[i].setVisible(true);
            } else {
                labelImageAux[i].setVisible(false);
            }

        } 
    }

    //Impostazione Messaggio Visualizzato
    public void setMessage(String s) {

        elemLabelSimple[2].setText(HTML_STRINGA_INIZIO
                + s
                + HTML_STRINGA_FINE);

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String s) {

        gestoreDialog.result = Integer.parseInt(s);

        //FabCloudFrame.getFRAME().setAlwaysOnTop(true); 
        dialogFrame.setVisible(false);

    }

    //Iniziliazzione Visibilità Label Ausiliari
    public void initLabelAux() {

        for (JLabel labelImageAux1 : labelImageAux) {
            labelImageAux1.setVisible(false);
        }

    }

    //Imposta la validità dei Pulsanti
    public void valPulsanti(boolean enable) {

        for (JButton elemBut1 : elemBut) {
            elemBut1.setEnabled(enable);
        }

    }
}
