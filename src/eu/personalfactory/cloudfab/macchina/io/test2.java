/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

import de.re.eeip.EEIPClient;
import de.re.eeip.cip.datatypes.ConnectionType;
import de.re.eeip.cip.datatypes.Priority;
import de.re.eeip.cip.datatypes.RealTimeFormat;
import de.re.eeip.cip.exception.CIPException;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_IN_KEY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_OUT_KEY;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Francesco Di Gaudio
 *
 */
public class test2 {

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, InterruptedException, CIPException {


    	 ClientEB80 clienteB80 = new ClientEB80("192.168.0.112", 1000, true);
    	
    	 ArrayList<String> messaggi = new ArrayList<>();
    	 messaggi.add("00001101");
    	 messaggi.add("11000000");
    	 messaggi.add("00000001");
    	 clienteB80.send(messaggi);
    	 
    	 Thread.sleep(5000);
    	 
    	 messaggi = new ArrayList<>();
    	 messaggi.add("11000000");
    	 messaggi.add("00000001");
    	 messaggi.add("00000001");
    	 clienteB80.send(messaggi);
    	 
    	 Thread.sleep(5000); 
    	 messaggi = new ArrayList<>();
    	 messaggi.add("00000000");
    	 messaggi.add("00000000");
    	 messaggi.add("00000000");
    	 clienteB80.send(messaggi);
    	 
        ///////////////////////////////////////////////////// Tentativo di comunicazione Explicit Messaging EB80
//        EEIPClient eipClient = new EEIPClient();
//
//        System.out.println("A");
//        //////////////////////  
//        // CONFIGURAZIONE  ///
//        ////////////////////// 
//        //Parameters for Originator -> Target communication
//        eipClient.setIpAddress("192.168.0.111");
//
//        eipClient.RegisterSession(); //"192.168.0.111", 0xAF12);
//       // eipClient.GetAttributeSingle(0x01,0x64, 0x65);
//
//		// Parameters for Originator -> Target communication
//		eipClient.setO_T_InstanceID(0x64); // Output Assembly 65hex
//		eipClient.setO_T_Length(3); // 4
//		eipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
//		eipClient.setO_T_ownerRedundant(false);
//		eipClient.setO_T_priority(Priority.Scheduled); // Urgent
//		eipClient.setO_T_variableLength(true);
//		eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
//		//eipClient.setRequestedPacketRate_O_T(500); // 2000 il valore standard è 500ms 
//		
//		
//		// Parameters for Target -> Originator communication
//		eipClient.setT_O_InstanceID(0x65); // Input Assembly 68hex
//		eipClient.setT_O_Length(1);
//		eipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
//		eipClient.setT_O_ownerRedundant(false);
//		eipClient.setT_O_priority(Priority.Urgent);
//		eipClient.setT_O_variableLength(false); // Urgent
//		eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
//		//eipClient.setRequestedPacketRate_T_O(500);
//   
//         
//        int tempo = 100;

//		System.out.println("1");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[0] = (byte) 1;
//		// eipClient.ForwardClose();
//
//		System.out.println("2");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[0] = (byte) 4;
//		eipClient.ForwardClose();
//
//		System.out.println("3");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[0] = (byte) 8;
//		eipClient.ForwardClose();
//
//		System.out.println("4");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[0] = (byte) 16;
//		eipClient.ForwardClose();
//
//		System.out.println("5");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[0] = (byte) 64;
//		eipClient.ForwardClose();
//
//		System.out.println("6");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[0] = (byte) 128;
//		eipClient.ForwardClose();
//
//		System.out.println("1");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[1] = (byte) 1;
//		eipClient.ForwardClose();
//
//		System.out.println("2");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[1] = (byte) 4;
//		eipClient.ForwardClose();
//
//		System.out.println("3");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[1] = (byte) 8;
//		eipClient.ForwardClose();
//
//		System.out.println("4");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[1] = (byte) 16;
//		eipClient.ForwardClose();
//
//		System.out.println("5");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[1] = (byte) 64;
//		eipClient.ForwardClose();
//
//		System.out.println("6");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[1] = (byte) 128;
//		eipClient.ForwardClose();
//
//		System.out.println("1");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[2] = (byte) 1;
//		eipClient.ForwardClose();
//
//		System.out.println("2");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[2] = (byte) 4;
//		eipClient.ForwardClose();
//
//		System.out.println("3");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[2] = (byte) 8;
//		eipClient.ForwardClose();
//
//		System.out.println("4");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[2] = (byte) 16;
//		eipClient.ForwardClose();
//
//		System.out.println("5");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[2] = (byte) 64;
//		eipClient.ForwardClose();
//
//		System.out.println("6");
//		eipClient.ForwardOpen();
//		Thread.sleep(tempo);
//		eipClient.O_T_IOData[2] = (byte) 128;
//		eipClient.ForwardClose();

//		eipClient.UnRegisterSession();

        System.out.println("E");
    }   
}
