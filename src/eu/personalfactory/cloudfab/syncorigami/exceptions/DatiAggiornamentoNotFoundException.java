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
public class DatiAggiornamentoNotFoundException extends Exception {
  
  private Logger log = Logger.getLogger(DatiAggiornamentoNotFoundException.class);

  public DatiAggiornamentoNotFoundException(String string) {
   log.error(string);
  }
  
}