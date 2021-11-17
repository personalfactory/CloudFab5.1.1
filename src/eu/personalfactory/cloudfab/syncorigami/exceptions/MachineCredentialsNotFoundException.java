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
@SuppressWarnings("serial")
public class MachineCredentialsNotFoundException extends Exception {
  private Logger log = Logger.getLogger(MachineCredentialsNotFoundException.class);

  public MachineCredentialsNotFoundException(String string) {
    log.error(string);
    
  }
}
