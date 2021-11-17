/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.exceptions;

import org.apache.log4j.Logger;

/**
 * Questa eccezione che viene generata 
 * qualora il file xml contenga informazioni non valide
 * @author marilisa
 */
public class InvalidUpdateContentException extends Exception {
  
Logger log = Logger.getLogger(InvalidUpdateContentException.class);
  
public InvalidUpdateContentException(String string) {
    log.error(string);
  }
  
}
