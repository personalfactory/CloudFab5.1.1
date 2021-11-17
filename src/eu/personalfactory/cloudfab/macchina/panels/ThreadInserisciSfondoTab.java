/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMAGES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_SFONDI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_AUX;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_UNSEL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.jdom.Element;

/**
 *
 * @author francescodigaudio
 *
 *
 * Dichiarazione Immagini di Sfondo Tab
 *
 *
 */
public class ThreadInserisciSfondoTab extends Thread {

    private Pannello10_ScelteEffettuate pannelloCorrente;

    //COSTRUTTORE
    public ThreadInserisciSfondoTab(Pannello10_ScelteEffettuate pannelloCorrente) {

        //Dichiarazione Variabili del Thread
        this.pannelloCorrente = pannelloCorrente;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        for (int i = 0; i < pannelloCorrente.imageSfondoTab.length; i++) {

            pannelloCorrente.imageSfondoTab[i] = new JLabel();

            //Lettura chiave file xml
            Element elm = pannelloCorrente.root.getChild(XML_IMAGE_AUX + i);
            ImageIcon tabImg = new ImageIcon(ClassLoader.getSystemResource(PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + "x"
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_SFONDI
                    + elm.getAttributeValue(XML_IMAGE_UNSEL)));

            //Impostazione Caratteristiche Label
            pannelloCorrente.imageSfondoTab[i].setBounds(0,
                    0,
                    RISOLUZIONE_LARGHEZZA_PANNELLO,
                    RISOLUZIONE_LARGHEZZA_PANNELLO);

            //Impostazione Immagine
            pannelloCorrente.imageSfondoTab[i].setIcon(tabImg);

            //Impostazione Visibilità
            pannelloCorrente.imageSfondoTab[i].setVisible(false);

            //Impostazine Bordi
            pannelloCorrente.imageSfondoTab[i].setBorder(null);

            //Impostazione Allineamento
            pannelloCorrente.imageSfondoTab[i].setHorizontalAlignment(SwingConstants.LEFT);
            pannelloCorrente.imageSfondoTab[i].setVerticalAlignment(SwingConstants.TOP);

            //Assegnazione Componente al Pannello
            pannelloCorrente.add(pannelloCorrente.imageSfondoTab[i]);

        }
 
    }
}