/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.valvole.pneumatiche;

import de.re.eeip.cip.exception.CIPException;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class EB80_Test {

    static Gestore_EB80 gestoreValvole;

    public static void main(String[] args) throws InterruptedException, IOException, CIPException {

        gestoreValvole = new Gestore_EB80("192.168.0.110");

//        //gestoreValvole.eeipClient.ForwardOpen();
//        gestoreValvole.comunica("00000001");
        //gestoreValvole.eeipClient.ForwardClose();
//
//        Thread.sleep(20000);
//        // gestoreValvole.eeipClient.ForwardOpen();
//        gestoreValvole.comunica("00000000");
//
//        Thread.sleep(20000);
//
//        //gestoreValvole.eeipClient.ForwardClose();
//        gestoreValvole.eeipClient.UnRegisterSession();

new thread().start();
    }

    public static class thread extends Thread {

        @Override
        public void run() {
            try {
                int counter =0;
                while (counter<5) {
                    try {
                        //gestoreValvole.eeipClient.ForwardOpen();
                        gestoreValvole.comunica("000000000000000000001");
                        //gestoreValvole.eeipClient.ForwardClose();
                        
                        this.sleep(1000);
                        // gestoreValvole.eeipClient.ForwardOpen();
                        gestoreValvole.comunica("000000000000000000000");
                        
                        this.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(EB80_Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    counter++;
                }

                gestoreValvole.eeipClient.ForwardClose();
                gestoreValvole.eeipClient.UnRegisterSession();
            } catch (IOException ex) {
                Logger.getLogger(EB80_Test.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CIPException ex) {
                Logger.getLogger(EB80_Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
