/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.loggers;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_FILE_MAX_LENGHT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_FILE_MAX_NUM;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_SERIAL_SEPARATOR_PATTERN;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class SevereLogger {

    public static final Logger logger = Logger.getLogger("");
    private static DateFormat formatter;

    public static void init() {

        //adding custom handler
        logger.addHandler(new MyHandler());

        //Aggiornamento e formattazione data
        Date date = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat(ParametriGlobali.parametri.get(71));

        //Percorso del file di Log
        String path = ParametriGlobali.parametri.get(53)
                + File.separator
                + ParametriGlobali.parametri.get(52)
                + ParametriGlobali.parametri.get(101)
                + File.separator
                + LOG_SERIAL_SEPARATOR_PATTERN
                + ParametriGlobali.parametri.get(100)
                + formatter.format(date)
                + ParametriGlobali.parametri.get(59);

        //Dichiarazione del File e cancellazione esistenti
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }

        try {

            //FileHandler file name with max size and number of log files limit
            Handler fileHandler = new FileHandler(path,
                    LOG_FILE_MAX_LENGHT,
                    LOG_FILE_MAX_NUM,
                    true);

            fileHandler.setFormatter(new MySevereFormatter());

            logger.addHandler(fileHandler);
 
            logger.setLevel(java.util.logging.Level.OFF);
 
        } catch (SecurityException | IOException e) {

            System.err.println(e);
        }
    }
}