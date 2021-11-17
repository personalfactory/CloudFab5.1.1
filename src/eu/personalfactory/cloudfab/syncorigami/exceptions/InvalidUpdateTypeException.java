/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.exceptions;

import org.apache.log4j.Logger;



/**
 * Eccezione che viene generata durante la validazione dell'aggiornamento
 * qualora si verifichi un errore sul tipo dell'aggiornaemntoOri
 * @author marilisa
 */
public class InvalidUpdateTypeException extends Exception {
  
private Logger log = Logger.getLogger(InvalidUpdateTypeException.class);
        
  public InvalidUpdateTypeException(String string) {
    log.error(string);
  }
  
}
