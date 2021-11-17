/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

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
 * @author francescodigaudio 
 * Pannello Contenuto nella Finestra di Dialogo per la
 * Segnazione degli Errori che Avvengono durante le Procedure
 */
@SuppressWarnings("serial")
public class Pannello46_ErroriAggiornamento extends MyStepPanel {

    //PARAMETRI PANNELLO
    private final int numImage = 2;
    //VARIABILI
    public JDialog dialogFrame;
    public GestoreErrori gestoreErrori;

    //COSTRUTTORE
    public Pannello46_ErroriAggiornamento() {

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
        impostaDimLabelSimple(3);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(2);
        impostaColori(1);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Configurazione di Base Pannello
        configuraPannello();

        //Inizializzazione Colori e Posizione Testo Pulsanti
        for (int i = 0; i < elemLabelSimple.length - 1; i++) {

            elemLabelSimple[i].setVerticalAlignment(SwingConstants.CENTER);
            elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);

        }

        elemLabelSimple[2].setVerticalAlignment(SwingConstants.CENTER);
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

    //Inizializzazione Componenti Finestra
    public void init(String titleFinestra, ArrayList<String> titleBut, String message) {

        //Aggiornamento Testi Label Title
        elemLabelTitle[0].setText(HTML_STRINGA_INIZIO
                + titleFinestra
                + HTML_STRINGA_FINE);

        for (int i = 0; i < titleBut.size(); i++) {

            if (!titleBut.get(i).equals("")) {

                //Impostazione Visibilità Label Simple
                elemLabelSimple[i].setVisible(true);

                //Aggiornamento Testo Label Simple
                elemLabelSimple[i].setText(HTML_STRINGA_INIZIO
                        + titleBut.get(i)
                        + HTML_STRINGA_FINE);

                //Impostazione Visibilità Pulsanti
                elemBut[i].setVisible(true);

            } else {

                //Impostazione Visibilità Testo Pulsanti
                elemLabelSimple[i].setVisible(false);

                //Impostazione Visibilità Pulsanti
                elemBut[i].setVisible(false);

            }
        }

        //Aggiornamento Testo Descrizione Errore
        elemLabelSimple[2].setText(HTML_STRINGA_INIZIO
                + message
                + HTML_STRINGA_FINE);

        //Aggiornamento Elementi Grafici Pannello
        repaint();
        updateUI(); 
////        FabCloudFrame.getFRAME().setAlwaysOnTop(false);
        
        dialogFrame.setVisible(true);

    }

    //Gestione Pulsanti
    public void gestorePulsanti(String s) {

        gestoreErrori.result = Integer.parseInt(s); 
        
////        FabCloudFrame.getFRAME().setAlwaysOnTop(true);
        
        dialogFrame.setVisible(false);

    }
}
