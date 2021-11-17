/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.valvole.pneumatiche;

import de.re.eeip.EEIPClient;
import de.re.eeip.cip.datatypes.ConnectionType;
import de.re.eeip.cip.datatypes.Priority;
import de.re.eeip.cip.datatypes.RealTimeFormat;
import de.re.eeip.cip.exception.CIPException;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;
import static eu.personalfactory.cloudfab.macchina.valvole.pneumatiche.EB80_Test.gestoreValvole;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class Gestore_EB80 {

    public EEIPClient eeipClient = new EEIPClient();

    public Gestore_EB80(String IpAddress) throws IOException, CIPException {

        int OUT_KEY = 0x64;
        int IN_KEY = 0x65;
        /////////////////////////////
        //CREAZIONE NUOVO CLIENT  ///
        ///////////////////////////// 
       // eeipClient.setIpAddress(IpAddress);
        eeipClient.RegisterSession(IpAddress);

        //////////////////////
        // CONFIGURAZIONE  ///
        //////////////////////
        //Parameters for Originator -> Target communication
        eeipClient.setO_T_InstanceID(OUT_KEY);       //Output Assembly 65hex
        eeipClient.setO_T_Length(3);
        eeipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
        eeipClient.setO_T_ownerRedundant(false);
        eeipClient.setO_T_priority(Priority.Urgent);
        eeipClient.setO_T_variableLength(false);
        eeipClient.setO_T_connectionType(ConnectionType.Point_to_Point);
        //eeipClient.setRequestedPacketRate_O_T(5000000);

        //Parameters for Target -> Originator communication
        eeipClient.setT_O_InstanceID(IN_KEY);       //Input Assembly 68hex
        eeipClient.setT_O_Length(1);
        eeipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
        eeipClient.setT_O_ownerRedundant(false);
        eeipClient.setT_O_priority(Priority.Urgent);
        eeipClient.setT_O_variableLength(false);
        eeipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
        //eeipClient.setRequestedPacketRate_T_O(5000000);
  
        eeipClient.ForwardOpen();
    }

//    public void inviaMessaggio(String str) throws CIPException, IOException, InterruptedException {
//
//        new comunica(str).start();

//    }

//    private class comunica extends Thread {
//
//        private String msg;
//
//        public comunica(String msg) {
//            this.msg = msg;
//        }
//
//        @Override
//        public void run() {
    
    public void comunica(String msg) {
        
        int tempo = 200;
        int counter = 0;
        //eeipClient.RegisterSession();
        // while (counter < 5) {
        //System.out.println(eipClient.T_O_IOData[0]);
        //System.out.println(eipClient.O_T_IOData[0]);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        eeipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt(msg).byteValue();
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException ex) {
            Logger.getLogger(Gestore_EB80.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //eeipClient.ForwardClose();
        //eeipClient.ForwardClose();
        ////                    // gestoreValvole.inviaMessaggio("00000001");
////                    //System.out.println(eipClient.T_O_IOData[0]);
////                    //System.out.println(eipClient.O_T_IOData[0]);
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 1 - Stop");
////                    eeipClient.ForwardOpen();
////                    eeipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eeipClient.ForwardClose();
// gestoreValvole.inviaMessaggio("00000000");
        counter++;
//  }
//gestoreValvole.eeipClient.ForwardClose();
// eeipClient.UnRegisterSession();
    }

//    }
}
