/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author francescodigaudio
 */
public class LoadingLogProperties {
 
        public LoadingLogProperties(String filename) throws IOException {
           
            Properties props = new Properties();
            props.load(ClassLoader.getSystemResourceAsStream(filename));
            PropertyConfigurator.configure(props);
        }
    }