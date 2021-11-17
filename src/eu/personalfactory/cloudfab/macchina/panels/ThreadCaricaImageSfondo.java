/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMAGES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_SFONDI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_SFONDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_UNSEL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.jdom.Element;

/**
 *
 * @author francescodigaudio
 *
 * Loading dell'Immagine di Sfondo ed Assegnazione al Pannello
 *
 *
 */
public class ThreadCaricaImageSfondo extends Thread {

    //VARIABILI
    private MyStepPanel pannelloCorrente;

    //COSTRUTTORE
    public ThreadCaricaImageSfondo(MyStepPanel pannelloCorrente) {

        //Dichiarazione Variabile Pannello Corrente
        this.pannelloCorrente = pannelloCorrente;

        //Dichiarazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
         
        //Creazione Nuova Istanza JLabel
        pannelloCorrente.imageSfondo = new JLabel();
         
        //Lettura Chiave File Xml
        Element elm = pannelloCorrente.root.getChild(XML_IMAGE_SFONDO);
  
        //Lettura Immagine di Sfondo
        ImageIcon sfondo = new ImageIcon(ClassLoader.getSystemResource(PATH_IMAGES
                + RISOLUZIONE_LARGHEZZA_PANNELLO
                + "x"
                + RISOLUZIONE_ALTEZZA_PANNELLO
                + PATH_IMG_SFONDI
                + elm.getAttributeValue(XML_IMAGE_UNSEL)));

        //Impostazione Caratteristiche Label Immagine
        pannelloCorrente.imageSfondo.setBounds(0, 0, sfondo.getIconWidth(), sfondo.getIconHeight());

        //Assgnazione Immagine 
        pannelloCorrente.imageSfondo.setIcon(sfondo);

        //Impostazione Visibilità Immagine di Sfondo
        pannelloCorrente.imageSfondo.setVisible(true);

        //Aggiunta Immagine di Sfondo al Pannello
        pannelloCorrente.add(pannelloCorrente.imageSfondo);
 
    }
}
