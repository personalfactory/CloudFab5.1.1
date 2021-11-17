/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.loggers;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_FILE_DATE_FORMATTER;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author francescodigaudio
 */
public class LoggersFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(1000);

        if (ParametriSingolaMacchina.parametri.get(234).equals("INFO")
                || ParametriSingolaMacchina.parametri.get(234).equals("WARNING")
                || ParametriSingolaMacchina.parametri.get(234).equals("SEVERE")){
            
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(new SimpleDateFormat(LOG_FILE_DATE_FORMATTER).format(new Date(record.getMillis()))).append(" : ");
            builder.append(formatMessage(record));
            builder.append("\n"); 
             
        } else {

            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(new SimpleDateFormat(LOG_FILE_DATE_FORMATTER).format(new Date(record.getMillis()))).append(" - ");
            builder.append("(").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append(") : ");
            builder.append(formatMessage(record));
            builder.append("\n");
        }


        return builder.toString();
 
    }
}
