/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMAGE_CORDINATE_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMAGE_CORDINATE_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMAGE_NAME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMAGES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_SFONDI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author francescodigaudio
 *
 * Inserimento Immagine di Loading
 *
 *
 *
 */
public class ThreadInserisciLoadingImage extends Thread {

    //VARIABILI
    private MyStepPanel pannelloCorrente;

    //COSTRUTTORE
    public ThreadInserisciLoadingImage(MyStepPanel pannelloCorrente) {

        //Dichiarazione Pannello Corrente
        this.pannelloCorrente = pannelloCorrente;

        //Dichiarazione Tipo Variabile
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        pannelloCorrente.loadingImg = new JLabel();

        ImageIcon img = new ImageIcon(
                ClassLoader.getSystemResource(PATH_IMAGES
                + RISOLUZIONE_LARGHEZZA_PANNELLO
                + "x"
                + RISOLUZIONE_ALTEZZA_PANNELLO
                + PATH_IMG_SFONDI
                + LOADING_IMAGE_NAME));

        pannelloCorrente.loadingImg.setBounds(ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / LOADING_IMAGE_CORDINATE_X),
                ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / LOADING_IMAGE_CORDINATE_Y),
                img.getIconWidth(), img.getIconHeight());

        //Impostazione Immagine
        pannelloCorrente.loadingImg.setIcon(img);

        //Impostazione Trasparenza
        pannelloCorrente.loadingImg.setOpaque(true);

        //Impostazione Visibilità
        pannelloCorrente.loadingImg.setVisible(false);

        //Assegnazione Componente al Pannello
        pannelloCorrente.add(pannelloCorrente.loadingImg);

    }
}