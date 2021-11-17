/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.utils;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 *
 * @author divinotaras
 */
public class FTPFileTranferingListener implements FTPDataTransferListener {

  @Override
	public void started() {
		// Trasferimento avviato
    //System.out.println("Trasferimento avviato!");
	}
  
  @Override
	public void transferred(int length) {
		// Altri length byte sono stati trasferiti da quando questo metodo
		// è stato richiamanto l'ultima volta
    //System.out.println("Trasferiti altri " + length + "!");
	}

	@Override
  public void completed() {
		// Trasferimento completato
   // System.out.println("Trasferimento completato!");
	}

	@Override
  public void aborted() {
		// Trasferimento annullato
   // System.out.println("Trasferimento annullato!");
	}
  
  @Override
	public void failed() {
		// Trasferimento fallito
    //System.out.println("Trasferimento fallito!");
	}

}