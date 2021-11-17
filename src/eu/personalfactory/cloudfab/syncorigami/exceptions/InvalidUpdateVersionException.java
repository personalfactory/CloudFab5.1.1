/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.exceptions;

import org.apache.log4j.Logger;

/**
 * Eccezione che viene generata durante la validazione dell'aggiornamento
 * qualora si verifichi un errore nella versione dell'aggiornaemntoOri
 * @author marilisa
 */
@SuppressWarnings("serial")
public class InvalidUpdateVersionException extends Exception {

  private Logger log = Logger.getLogger(InvalidUpdateVersionException.class);

  public InvalidUpdateVersionException(String string) {
    log.error(string);
  }
}
