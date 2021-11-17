/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.loggers;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class SessionLogger {

    public static final Logger logger = Logger.getLogger(SessionLogger.class.getName());
    private static DateFormat formatter;

    public static void init() {

        //adding custom handler
        logger.addHandler(new MyHandler());

        //Aggiornamento e formattazione data
        Date date = Calendar.getInstance().getTime();
        formatter = new SimpleDateFormat(SESSION_LOGGER_DATE_FORMAT);

        //Percorso del file di Log
        String path = SESSION_LOGGER_53_DIR_WORK
                + File.separator
                + SESSION_LOGGER_52_DIR_LOG
                + SESSION_LOGGER_101_NOME_DIR_TEMP_LOGGER
                + File.separator
                + LOG_SERIAL_SEPARATOR_PATTERN
                + SESSION_LOGGER_88_NOME_FILE_LOGGER
                + formatter.format(date)
                + SESSION_LOGGER_59_LOG_EXTENSION;

        //Dichiarazione del File e cancellazione esistenti
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }

        logger.setLevel(Level.parse(SESSION_LOGGER_LEVEL));

        try {

            //FileHandler file name with max size and number of log files limit
            Handler fileHandler = new FileHandler(path,
                    LOG_FILE_MAX_LENGHT,
                    LOG_FILE_MAX_NUM,
                    false);

            fileHandler.setFormatter(new SessionLoggerFormatter());

            logger.addHandler(fileHandler);

        } catch (SecurityException | IOException e) {

            System.err.println(e);
        }
    }

    public static class SessionLoggerFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {

            StringBuilder builder = new StringBuilder(1000);

            builder.append("[").append(SESSION_LOGGER_LEVEL).append("] - ");
            builder.append(new SimpleDateFormat(LOG_FILE_DATE_FORMATTER).format(new Date(record.getMillis()))).append(" - ");
            builder.append("(").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append(") : ");
            builder.append(formatMessage(record));
            builder.append("\n");

            return builder.toString();

        }
    }

}
