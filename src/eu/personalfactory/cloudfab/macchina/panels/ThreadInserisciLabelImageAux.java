/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMAGES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_SFONDI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_AUX;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_SFONDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_UNSEL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.jdom.Element;

/**
 *
 * @author francescodigaudio
 */
public class ThreadInserisciLabelImageAux extends Thread {

    //VARIABILI
    private MyStepPanel pannelloCorrente;
    private int numImage;
    private boolean defVis;
    private Element elm;

    //COSTRUTTORE
    public ThreadInserisciLabelImageAux(MyStepPanel pannelloCorrente, int numImage, boolean defVis) {

        //Dichiarazione variabili
        this.pannelloCorrente = pannelloCorrente;
        this.numImage = numImage;
        this.defVis = defVis;

        //Analisi Tipologia Pannello Corrente
        if (pannelloCorrente instanceof Pannello14_Configurazione_Parametri) {

            this.pannelloCorrente = (Pannello14_Configurazione_Parametri) pannelloCorrente;
        } else if (pannelloCorrente instanceof Pannello13_Configurazione_Generale) {

            this.pannelloCorrente = (Pannello13_Configurazione_Generale) pannelloCorrente;
        }

        this.numImage = numImage;

        //Dichiaraizone Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        pannelloCorrente.labelImageAux = new JLabel[numImage];

        for (int i = 0; i < numImage; i++) {

            pannelloCorrente.labelImageAux[i] = new JLabel();

            elm = pannelloCorrente.root.getChild(XML_IMAGE_AUX + i);

            //Lettura Immagine
            ImageIcon tabImg = new ImageIcon(ClassLoader.getSystemResource(PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + "x"
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_SFONDI
                    + elm.getAttributeValue(XML_IMAGE_UNSEL)));

            //Impostazione Caratteristiche Label Immagine Ausiliaria
            pannelloCorrente.labelImageAux[i].setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                    ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                    / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                    tabImg.getIconWidth(),
                    tabImg.getIconHeight());

            //Assegnazione Immagine
            pannelloCorrente.labelImageAux[i].setIcon(tabImg);
            
            //Impostazione Bordi
            pannelloCorrente.labelImageAux[i].setBorder(null);
            
            //Impostazione Allineamento Test
            pannelloCorrente.labelImageAux[i].setHorizontalAlignment(SwingConstants.LEFT);
            pannelloCorrente.labelImageAux[i].setVerticalAlignment(SwingConstants.TOP);
            
            //Impostazione Visibilità Label
            pannelloCorrente.labelImageAux[i].setVisible(defVis);
            
            //Impostazione Nome Label
            pannelloCorrente.labelImageAux[i].setName(Integer.toString(i));

            //Assegnazione Componente al Pannello
            pannelloCorrente.add(pannelloCorrente.labelImageAux[i]);


        }

        if (!(pannelloCorrente instanceof Pannello10_ScelteEffettuate)
                && !(pannelloCorrente instanceof Pannello20_Assistenza)) {

            pannelloCorrente.imageSfondo = new JLabel();

            //Lettura Chiave File Xml
            elm = pannelloCorrente.root.getChild(XML_IMAGE_SFONDO);

            //Lettura Immagine di Sfondo
            ImageIcon sfondo = new ImageIcon(ClassLoader.getSystemResource(PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + "x"
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_SFONDI
                    + elm.getAttributeValue(XML_IMAGE_UNSEL)));

            //Impostazione Caratteristiche Label Immagine Ausiliaria
            pannelloCorrente.imageSfondo.setBounds(0, 0, sfondo.getIconWidth(), sfondo.getIconHeight());

            //Assegnazione Immagine
            pannelloCorrente.imageSfondo.setIcon(sfondo);

            //Impostazione Visibilità Label
            pannelloCorrente.imageSfondo.setVisible(true);

            //Assegnazione Componente al Pannello
            pannelloCorrente.add(pannelloCorrente.imageSfondo);


        } 
        if (pannelloCorrente instanceof Pannello44_Errori) {

            //Dichiarazione Dialog Frame
            ((Pannello44_Errori) pannelloCorrente).setDialogFrame();
 
        } else if (pannelloCorrente instanceof Pannello45_Dialog) {

            //Dichiarazione Dialog Frame
            ((Pannello45_Dialog) pannelloCorrente).setDialogFrame();
 
        } else if (pannelloCorrente instanceof Pannello46_ErroriAggiornamento) {

            //Dichiarazione Dialog Frame
            ((Pannello46_ErroriAggiornamento) pannelloCorrente).setDialogFrame();
 
             
        }
 
    }
}
