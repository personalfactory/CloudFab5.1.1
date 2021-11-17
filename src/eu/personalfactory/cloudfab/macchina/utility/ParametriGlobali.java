/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametroGlobalePerId;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.KEY_PROPERTIES_NUMERO_PARAMETRI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.KEY_PROPERTIES_PARAMETRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_FILE_DEFAULT_PARAMETRI_GLOBALI;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author francescodigaudio
 */
public class ParametriGlobali {

    public static ArrayList<String> parametri;

    public static void init() {

        parametri = new ArrayList<>();

        //Lettura dei parametri dal File di Properties
        Properties prop = PropertyReader.loadProperties(PATH_FILE_DEFAULT_PARAMETRI_GLOBALI, new ClassLoader() {
        });

        int numeroParametri = Integer.parseInt(prop.getProperty(KEY_PROPERTIES_NUMERO_PARAMETRI));

        //E' necessario per adeguare gli indici
        parametri.add("");

        for (int i = 0; i < numeroParametri; i++) {

            String valoreParametro;

            valoreParametro = TrovaValoreParametroGlobalePerId(i + 1);

            if (valoreParametro.length() == 0) {

                parametri.add(prop.getProperty(KEY_PROPERTIES_PARAMETRO + (i + 1)));

            } else {

                parametri.add(valoreParametro);

            }
        }
    }
}
