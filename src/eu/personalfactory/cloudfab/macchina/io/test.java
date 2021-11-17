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
public class test {

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, InterruptedException, CIPException {

//        String default_parametro_464 = "192.168.0.";
//        String default_parametro_465 = "100";
//        String default_parametro_466 = "120_121_122_123";
//        String default_parametro_467 = "1.1.0.1.1.0";
//        String default_parametro_468 = ".";
//        String default_parametro_469 = "$";
//        String default_parametro_470 = "101.102.103.110.111.112";
//        String default_parametro_471 = "1840.1840.1840.1880.1880.1880";
//        String default_parametro_472 = "";
//        String default_parametro_473 = "";
//        String default_parametro_474 = "0.0.0.1.1.1";
//        String default_parametro_475 = "100.100.100.2000.2000.2000";
//        String default_parametro_476 = "";
//        String default_parametro_477 = "";
//        String default_parametro_478 = "MD_MAINP.MD_SILOSP.MD_SILOSPAUX.EB80_MAINP.EB80_SILOSP.EB80_SILOSAUX";
//        String default_parametro_479 = "";
//        String default_parametro_480 = "";
//        String default_parametro_481 = "00000000000000001111112222221111112222221111112222221123333333333333333333334444";
//        String default_parametro_482 = "44444444444444555555555555555555";
//        String default_parametro_483 = "";
//        String default_parametro_484 = "52389$10$11$121AEF76CF12345612345679BDF$1179BDE$118ACE$10$128ACE$10$12$13$13$13123456789ABCDEF$10$11$12$13$14$151234";
//        String default_parametro_485 = "56789ABCDEF$10$11$12123456789ABCDEF$10$11$12";
//        String default_parametro_486 = "";
//        String default_parametro_487 = "000000000000011111122222211111122222112";
//        String default_parametro_488 = "";
//        String default_parametro_489 = "";
//        String default_parametro_490 = "576413289ABCD123456123456789ABC789ABCDD";
//        String default_parametro_491 = "";
//        String default_parametro_492 = "";
//        String default_parametro_493 = "7.5";
//        String default_parametro_494 = "mix.screws";
//        String default_parametro_495 = "10.10";
//        String default_parametro_496 = "0.1";
//        String default_parametro_497 = "1.1";

        ///////////////////////////////////////////////////// Tentativo di comunicazione Explicit Messaging EB80
        EEIPClient eipClient = new EEIPClient();

        System.out.println("A");
        //////////////////////  
        // CONFIGURAZIONE  ///
        ////////////////////// 
        //Parameters for Originator -> Target communication
        eipClient.setIpAddress("192.168.0.111");

		// Parameters for Originator -> Target communication
		eipClient.setO_T_InstanceID(0x64); // Output Assembly 65hex
		eipClient.setO_T_Length(3); // 4
		eipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
		eipClient.setO_T_ownerRedundant(false);
		eipClient.setO_T_priority(Priority.Urgent); // Urgent
		eipClient.setO_T_variableLength(false);
		//eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
		eipClient.setRequestedPacketRate_O_T(3000); // 2000 il valore standard è 500ms 
		
		
		// Parameters for Target -> Originator communication
		eipClient.setT_O_InstanceID(0x65); // Input Assembly 68hex
		eipClient.setT_O_Length(1);
		eipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
		eipClient.setT_O_ownerRedundant(false);
		eipClient.setT_O_priority(Priority.Urgent);
		eipClient.setT_O_variableLength(false); // Urgent
		//eipClient.setT_O_connectionType(ConnectionType.Multicast);
		eipClient.setRequestedPacketRate_T_O(3000);
        System.out.println("C");
        
        eipClient.RegisterSession("192.168.0.111");
        
        System.out.println("C1");
        eipClient.ForwardOpen();
        
        System.out.println(CloudFab5_0.ConversioneBinarioToInt("11111111"));
          
        eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("11111111").byteValue();
        
        eipClient.ForwardClose();
        
        System.out.println("D");
        
       //  eipClient.getAssemblyObject().setInstance(0x64, new byte[]{0x00, 0x00, 0x01 });   //100DEC 0x64HEX
        Thread.sleep(200);
        //eipClient.getAssemblyObject().setInstance(0x64, new byte[]{0x00, 0x00, 0x00});   //100DEC 0x64HEX 
        Thread.sleep(200);
        eipClient.UnRegisterSession();

        System.out.println("E");
        /////////////////////////////////////////////////////////////////////////////////////

////        new GestoreIO(
////                default_parametro_468,
////                default_parametro_469,
////                default_parametro_470,
////                default_parametro_464,
////                default_parametro_471 + default_parametro_472 + default_parametro_473,
////                default_parametro_474,
////                default_parametro_475 + default_parametro_476 + default_parametro_477,
////                default_parametro_478 + default_parametro_479 + default_parametro_480,
////                default_parametro_481 + default_parametro_482 + default_parametro_483,
////                default_parametro_484 + default_parametro_485 + default_parametro_486,
////                default_parametro_487 + default_parametro_488 + default_parametro_489,
////                default_parametro_490 + default_parametro_491 + default_parametro_492,
////                default_parametro_467);
////        ClientEB80 eb80 = new ClientEB80("192.168.0.110", 2000);
////
////        System.out.println("Pilota 1 - Run ");
////
////        int a = 1000;
////        int i = 0;
////
////        ArrayList<String> messaggi;
////
////        while (i < 2) {
////            messaggi = new ArrayList<>();
//
//            Integer byte_1 = Integer.parseInt("00000001", 2);
//            byte[] out = new byte[]{byte_1.byteValue()};
//            eb80.eipClient.getAssemblyObject().setInstance(0x64, out) 
//            Thread.sleep(a);
//            
//            byte_1 = Integer.parseInt("00000000", 2);
//            out = new byte[]{byte_1.byteValue()};
//            eb80.eipClient.getAssemblyObject().setInstance(0x64, out);
//            Thread.sleep(a);
//        
        // eipClient.RegisterSession("192.168.0.110");
        //eipClient.ForwardOpen();
        //
////            messaggi.add("00000001");
////            messaggi.add("00000000");
////            messaggi.add("00000000");
////            eb80.send(messaggi);
////            //Thread.sleep(a);
////
////            //Thread.sleep(20);
////            //eipClient.ForwardClose();
////            //eipClient.UnRegisterSession();
////
////            messaggi = new ArrayList<>();
////           // eipClient.RegisterSession("192.168.0.110");
////            //eipClient.ForwardOpen();
////            //Thread.sleep(40);
////            messaggi.add("00000000");
////            messaggi.add("00000000");
////            messaggi.add("00000000");
////            eb80.send(messaggi);
////            Thread.sleep(a);
        //Thread.sleep(40);
        //eipClient.ForwardClose();
        //eipClient.UnRegisterSession();
////       messaggi = new ArrayList<>();
////       messaggi.add("00000010");
////        messaggi.add("00000000");
////        messaggi.add("00000000");
////        eb80.send(messaggi);
////        Thread.sleep(a);
////
////        messaggi = new ArrayList<>();
////        messaggi.add("00000000");
////        messaggi.add("00000000");
////        messaggi.add("00000000");
////        eb80.send(messaggi);
////        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00001000");
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00010000");
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00100000");
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("01000000");
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("10000000");
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000001");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000010");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000100");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00001000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00010000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00100000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("01000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("10000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//        
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        messaggi.add("00000001");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//        
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        messaggi.add("00000010");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//        
//             
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        messaggi.add("00000100");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//        
//             
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        messaggi.add("00001000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
//        
//        
//        messaggi = new ArrayList<>();
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        messaggi.add("00000000");
//        eb80.send(messaggi);
//        Thread.sleep(a);
////            i++;
////        }
////
////         eb80.eipClient.ForwardClose();
////           eb80.eipClient.UnRegisterSession();
        // eb80.eipClient.ForwardClose();
        // 
        //eb80.eipClient.getAssemblyObject().setInstance(0x66, new byte[] { 0x01 });
////        
////        System.out.println("Pilota 1 - Stop ");
////        eb80.eipClient.ForwardOpen();
////        eb80.eipClient.O_T_IOData[0] = ConversioneBinarioToInt("000000000000000000000").byteValue();
////
////        eb80.eipClient.ForwardClose();
////        //Creazione Pannelli di Visualizzazione IO
////        GestoreIO_CreatePanels();
////
////        //Impostazione Visibilita Pannelli
////        GestoreIO_ImpostaVisPannelli(true);
////  
        //Attivazione Uscita Retroazione verifica Mduino
        //GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_MAIN_PANEL, OUTPUT_TRUE_CHAR);
        ///       System.out.println("AVVIO UDP SERVER");
////        UdpServerNuovo udp_server = new UdpServerNuovo(1860);
////
////        System.out.println("AVVIO LETTURA");
////
////        udp_server.startReading();
//////////        System.out.println("AVVIO UDP CLIENT");
//////////        
//////////        String IP = "192.168.1.177";
//////////        int PORT = 1840;
//////////        String msg = "CD";
//////////
//////////        UdpClientNuovo udp_client = new UdpClientNuovo(IP, PORT, 5000);
////////// 
//////////        System.out.println(udp_client.sendData(msg));
//////////         
//////////        udp_client.clientSocket.close();
////        udp_server.serverSocket.close();
//////// 
//////    final String CHAR_SEP_A = ".";
//////    final String CHAR_SEP_B = "$";
//////    final String CHAR_TRUE = "1";
//////    final String PERIFERICHE_IP = "101.102.103.120.121.122";
//////
//////    final String SUBNET = "192.168.1.";
//////    final String PERIFERICHE_PORT = "1840.1840.1840.1880.1880.1880";
//////    final String PERIFERICHE_TYPE = "0.0.0.1.1.1";
//////    final String PERIFERICHE_TIMEOUT = "100.100.100.1000.1000.1000";
//////    final String PERIFERICHE_NAME_COD = "MD_MAINP.MD_SILOSP.MD_SILOSPAUX.EB80_MAINP.EB80_SILOSP.EB80_SILOSAUX";
//////
//////    final String USCITA_PERIFERICA = "00110000033333333333330000111111222222";
//////    final String USCITA_POS = "121234567123456789ABCD89AB345678123456";
//////    final String INGRESSI_PERIFERICA = "00000000111111222222000";
//////    final String INGRESSI_POS = "123456781234561234569AB";
////        ArrayList<String> lista_ip = new ArrayList<>();
////        Enumeration e = NetworkInterface.getNetworkInterfaces();
////        while (e.hasMoreElements()) {
////            NetworkInterface n = (NetworkInterface) e.nextElement();
////            Enumeration ee = n.getInetAddresses();
////            while (ee.hasMoreElements()) {
////                InetAddress i = (InetAddress) ee.nextElement();
////                String Ip = i.getHostAddress();
////                if (Ip.contains(".")) {
////                    lista_ip.add(i.getHostAddress());
////                }
////            }
////        }
////        String site = "www.google.it";
////        try (Socket socket = new Socket()) {
////            InetSocketAddress addr = new InetSocketAddress(site, 80);
////            socket.connect(addr, 3000);
////            System.out.println(socket.isConnected());
////        } catch (Exception e){}
////    
////        GestoreIO gestore = new GestoreIO(
////                ".",
////                "$",
////                "1",
////                "101.102.103.120.121.122",
////                "192.168.1.",
////                "1840.1840.1840.1880.1880.1880",
////                "0.0.0.1.1.1",
////                "100.100.100.1000.1000.1000",
////                "MD_MAINP.MD_SILOSP.MD_SILOSPAUX.EB80_MAINP.EB80_SILOSP.EB80_SILOSAUX",
////                "00110000033333333333330000111111222222",
////                "121234567123456789ABCD89AB345678123456",
////                "00000000111111222222000",
////                "123456781234561234569AB"
////        );
////
////        gestore.createPanels();
////
////        gestore.impostaVisPannelli(true);
//////
//////        //gestore.modificaOut("5.6.0.22", "1.1.1.1");
//////        while (true) {
//////            //test funzionamento IO
//////            Thread.sleep(500);
//////            gestore.modificaOut("0", "1");
//////
//////            Thread.sleep(20);
//////            System.out.println("STATO USCITA TRUE - STATO INGRESSO =" + gestore.ottieniStatoIngresso("4"));
//////
//////            Thread.sleep(500);
//////            gestore.modificaOut("0", "0");
//////            Thread.sleep(20);
//////            System.out.println("STATO USCITA FALSE - STATO INGRESSO =" + gestore.ottieniStatoIngresso("4"));
//////        }
////            
////             public static String USCITA_LOGICA_CONTATTORE_MOTORE_MISCELATORE = "0";
////    public static String USCITA_LOGICA_CONTATTORE_MOTORE_VIBRO_VALVOLA = "1";
////    public static String USCITA_LOGICA_CONTATTORE_ATTIVA_INVERTER_COCLEE = "2";
////    public static String USCITA_LOGICA_CONTATTORE_LINEA_DIRETTA_COCLEE = "3";
////    public static String USCITA_LOGICA_CONTATTORE_MOTORE_VIBRO_BASE = "4"; 
////    public static String USCITA_LOGICA_CONTATTORE_LINEA_DIRETTA_MISCELATORE = "5";
////    public static String USCITA_LOGICA_SEGNALATORE_ACUSTICO = "6"; 
////    public static String USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE = "7"; 
////    public static String USCITA_LOGICA_COMANDO_UNICO_VIBRO_COCLEE = "8";
////    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// PNEUMATICHE  
////    public static String USCITA_LOGICA_EV_VALVOLA_CARICO = "9"; 
////    public static String USCITA_LOGICA_EV_VALVOLA_SCARICO = "10";
////    public static String USCITA_LOGICA_EV_BLOCCA_SACCO = "11";
////    public static String USCITA_LOGICA_EV_RIBALTA_SACCO = "12"; 
////    public static String USCITA_LOGICA_EV_ARIA_SVUOTA_TUBO = "13";
////    public static String USCITA_LOGICA_EV_ARIA_FLUDIFICATORI = "14";
////    public static String USCITA_LOGICA_EV_ARIA_PULISCI_VALVOLA = "15";
////    public static String USCITA_LOGICA_EV_ARIA_SVUOTA_VALVOLA = "16";
////    public static String USCITA_LOGICA_EV_A1_ASPIRATORE_BILANCIA_OMB = "17";
////    public static String USCITA_LOGICA_EV_A2_ASPIRATORE_BILANCIA_SCARICO = "18";
////    public static String USCITA_LOGICA_EV_A3_ASPIRATORE_BILANCIA_CARICO = "19";
////    public static String USCITA_LOGICA_EV_A4_ASPIRATORE_MISCELATORE = "20";
////    public static String USCITA_LOGICA_EV_VIBRO_TRAMOGGIA = "21";
////    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// NASTRO  
////    public static String USCITA_LOGICA_NASTRO = "22";
////    public static String USCITA_LOGICA_VIBRO_NASTRO = "23";
////    public static String USCITA_LOGICA_VIBRO_ELETTRICO_SILOS = "24";
////    public static String USCITA_LOGICA_VALVOLA_ALTERNATIVE = "25"; 
////          - Aggiustre la memorizzazione dello stato effettivo dopo aver ricevuto la risposta dal server Mduino
////          - Creare Thread per visualizzare il cambiamento di stato
////          - Creare server per ingressi su GestoreIO in modo da ricevere i cambiamenti di stato dal Mduino
        //     Thread.sleep(200);
        // }
    }
}
