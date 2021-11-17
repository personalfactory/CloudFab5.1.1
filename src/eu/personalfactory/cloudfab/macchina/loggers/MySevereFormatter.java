/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.loggers;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_FILE_DATE_FORMATTER;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author francescodigaudio
 */
public class MySevereFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {

        StringBuilder builder = new StringBuilder(1000);

        if (record.getLevel().getName().equals("WARNING")
                || record.getLevel().getName().equals("SEVERE")) {

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
