/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.exceptions;

import org.apache.log4j.Logger;


/**
 *
 * @author marilisa
 */
public class InitializeException extends Exception {
  
  private Logger log = Logger.getLogger(InitializeException.class);

  public InitializeException(String string) {
   
   log.error(string);
  }
  
}
