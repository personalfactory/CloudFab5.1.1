/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_PATH;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.VERSIONE_CORRENTE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.loadMacchinaProperties;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabFrame;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudLoadingFrame;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.NonValidParamException;
import java.io.IOException;
import static java.lang.ClassLoader.getSystemResource;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;

/**
 *
 * @author Francesco Di Gaudio
 *
 */
public class CloudFab {
    /**
     * @param args the command line arguments
     * @throws
     * eu.personalfactory.cloudfab.syncorigami.exceptions.NonValidParamException
     * @throws
     * eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws javax.swing.UnsupportedLookAndFeelException
     * @throws org.jdom.JDOMException
     */
	
    public static void main(String[] args) throws
            NonValidParamException,
            InitializeException,
            IOException,
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            UnsupportedLookAndFeelException,
            JDOMException {

        //Lettura dei Parametri di Base
        loadMacchinaProperties();

        FabCloudLoadingFrame frame = new FabCloudLoadingFrame(
                new ImageIcon(getSystemResource(
                        LOADING_IMG_PATH)),
                        VERSIONE_CORRENTE);

        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

        Logger log = Logger.getLogger(CloudFab.class);

        log.info("Max Memory: " + Runtime.getRuntime().maxMemory());

        //Creazione della Sessione di Lavoro
        new DettagliSessione();

        //Creazione della finestra
        new CloudFabFrame(frame);
        
        
    }
}
