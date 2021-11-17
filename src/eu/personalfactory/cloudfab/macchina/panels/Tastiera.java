package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMAGES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_TASTIERA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_PANELS_DATA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_SEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_UNSEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_NAME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_NUM;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_TASTO;
import eu.personalfactory.cloudfab.macchina.utility.GestorePulsantiTastiera;
import java.awt.Cursor;
import java.awt.Insets;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

@SuppressWarnings("serial")
public class Tastiera extends JPanel {

    //VARIABILI
    private boolean visibilitaTastiera;
    private JPanel panel;
    private GestorePulsantiTastiera gestPulsanti;

    //Elementi per la Lettura da File Xml
    private Document doc = null;
    private Element root;

    //Definizione Pulsanti
    private int numeroPulsanti;
    private JButton[] pulsanti; 

    //Dichiarazioni Tastiera
    public void setTastiera() {

        //Dichiarazione File Parametri
        impostaXml("tastiera.XML");

        //Dichiarazione Gestore Eventi
        gestPulsanti = new GestorePulsantiTastiera();

        //Lettura chiave "num" - Numero di Pulsanti della Tastiera
        Element elm = root.getChild(XML_NUM);
        numeroPulsanti = Integer.parseInt(elm.getAttributeValue(XML_NUM));

        //Dichiarazione Array Pulsanti
        pulsanti = new JButton[numeroPulsanti];

        //Dichiarazione Singoli Pulsanti
        for (int i = 0; i < numeroPulsanti; i++) {

            //Lettura chiave file xml 
            elm = root.getChild(XML_TASTO + i);

            //Creazione del Nuovo JButton
            pulsanti[i] = new JButton();

            //Impostazione Coordinate e Dimensioni
            pulsanti[i].setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                    ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                            / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

            //Impostazione Immagine di Default
            pulsanti[i].setDisabledIcon(new ImageIcon(
                    ClassLoader.getSystemResource(PATH_IMAGES
                            + RISOLUZIONE_LARGHEZZA_PANNELLO
                            + XML_CORD_X
                            + RISOLUZIONE_ALTEZZA_PANNELLO
                            + PATH_IMG_TASTIERA
                            + elm.getAttributeValue(XML_IMAGE_UNSEL))));

            pulsanti[i].setIcon(new ImageIcon(ClassLoader.getSystemResource(PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + XML_CORD_X
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_TASTIERA
                    + elm.getAttributeValue(XML_IMAGE_UNSEL))));

            //Impostazione Immagine Visualizzata al Passaggio del Mouse
            pulsanti[i].setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource(PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + XML_CORD_X
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_TASTIERA
                    + elm.getAttributeValue(XML_IMAGE_SEL))));

            //Impostazione Trasparenza
            pulsanti[i].setContentAreaFilled(false);
            pulsanti[i].setBorderPainted(false);
            pulsanti[i].setFocusPainted(false);
            pulsanti[i].setMargin(new Insets(0, 0, 0, 0));

            //Impostazione Cursore Visualizzato al Passaggio del Mouse
            pulsanti[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            //Impostazione Nome Pulsante 
            pulsanti[i].setName(elm.getAttributeValue(XML_NAME));

            //Inserimento Action Listener
            pulsanti[i].addActionListener(gestPulsanti);

            //Aggiunta Pulsanti al Pannello **/
            panel.add(pulsanti[i]);

        } 
        
        //Impostazione Visibilità Tastiera
        impostaVisibilitaTastiera(false);
    }

    //Impostazione Visibilità Tastiera
    public void impostaVisibilitaTastiera(boolean vis) {

        visibilitaTastiera = vis;
        for (int i = 0; i < numeroPulsanti; i++) {
            pulsanti[i].setVisible(vis);
        }

////        testoPulsante.setVisible(false);
    }

    //Assegnazione Text Field
    public void assegnaTextField(JTextField txtField) {
        gestPulsanti.setAreaTesto(txtField);
    }

    //Restituisce lo Stato di Visibilità della Tastiera
    public boolean getVisibilitaTastiera() {
        return visibilitaTastiera;
    }

    //Impostazione Elementi Lettura da file Xml
    public void impostaXml(String xmlFileName) {

        String xmlFile = PATH_PANELS_DATA + xmlFileName;

        SAXBuilder builder = new SAXBuilder();
        try {
            doc = builder.build(ClassLoader.getSystemResource(xmlFile));
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Pannello03_FiltroProdCod.class.getName()).log(Level.SEVERE, null, ex);
        }
        root = doc.getRootElement();
    }

    //Modifica Validita Tastiera
    public void impostaValiditaTastiera(boolean vis) {

        for (int i = 0; i < numeroPulsanti; i++) {
            pulsanti[i].setEnabled(vis);
        }

    }

    //Restituisce l'array dei Pulsanti
    public JButton[] getPulsanti() {
        return pulsanti;
    }

    //Assegnazione Pannello
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    //Abilita - Disabilita solo numeri
    public void abilitaSoloNumeri(boolean vis) {

        if (!vis) {
            for (int i = 0; i < pulsanti.length; i++) {
                pulsanti[i].setEnabled(!vis);
            }

        } else {
            
            for (int i = 0; i < pulsanti.length; i++) {
                pulsanti[i].setEnabled(!vis);
            }

            for (int i = 0; i < 13; i++) {
                pulsanti[i].setEnabled(vis);
            }
        }

    }

}
