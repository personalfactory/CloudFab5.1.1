/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.DecodificaPathSql;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametroSingolaMacchinaPerId;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEC_PATH_SQL_STR_INS_1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEC_PATH_SQL_STR_INS_2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEC_PATH_SQL_STR_REM_1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEC_PATH_SQL_STR_REM_2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_PATH_MYSQL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_PATH_MYSQLDUMP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.KEY_PROPERTIES_NUMERO_PARAMETRI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.KEY_PROPERTIES_PARAMETRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_FILE_DEFAULT_PARAMETRI_SINGOLA_MACCHINA;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author francescodigaudio
 */
public class ParametriSingolaMacchina {

    public static ArrayList<String> parametri;

    public static void init() {

        parametri = new ArrayList<>();

        //Lettura dei parametri dal File di Properties
        Properties prop = PropertyReader.loadProperties(PATH_FILE_DEFAULT_PARAMETRI_SINGOLA_MACCHINA, new ClassLoader() {
        });

        int numeroParametri = Integer.parseInt(prop.getProperty(KEY_PROPERTIES_NUMERO_PARAMETRI));

        //E' necessario per adeguare gli indici
        parametri.add("");

        for (int i = 0; i < numeroParametri; i++) {

            String valoreParametro;

            valoreParametro = TrovaValoreParametroSingolaMacchinaPerId(i + 1);


            if (valoreParametro.length() == 0) {

                ///////////////////////////////////////////
                // PARAMETRO NON PRESENTE NEL DATABASE  ///
                ///////////////////////////////////////////

                if (i == 229) {

                    ///////////////////////
                    // PATH MYSQL DUMP  ///
                    ///////////////////////

                    parametri.add(DEFAULT_PATH_MYSQLDUMP);


                } else if (i == 230) {

                    //////////////////
                    // PATH MYSQL  ///
                    //////////////////

                    parametri.add(DEFAULT_PATH_MYSQL);

                } else {

                    parametri.add(prop.getProperty(KEY_PROPERTIES_PARAMETRO + (i + 1)));
                    
                    
                }

            } else {

                ///////////////////////////////////////
                // PARAMETRO PRESENTE NEL DATABASE  ///
                ///////////////////////////////////////

                if (i == 229 || i == 230) {
                    
                    ///////////////////////////////
                    // PATH MYSQL E MYSQL DUMP  ///
                    ///////////////////////////////

                    parametri.add(DecodificaPathSql(valoreParametro,
                            DEC_PATH_SQL_STR_REM_1,
                            DEC_PATH_SQL_STR_REM_2,
                            DEC_PATH_SQL_STR_INS_1,
                            DEC_PATH_SQL_STR_INS_2));
                } else {

                    parametri.add(valoreParametro);
                }

            }

        }
         
    }
}
