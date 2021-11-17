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
import eu.personalfactory.cloudfab.macchina.io.ClientEB80;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imacdigaudio
 */
public class EB80Metalwork {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws de.re.eeip.cip.exception.CIPException
     */
    static EEIPClient eipClient = new EEIPClient();

    public static void main(String[] args) throws InterruptedException, IOException, CIPException {

        //Register Session (Wago-Device 750-352 IP-Address: 192.168.178.66) 
        //we use the Standard Port for Ethernet/IP TCP-connections 0xAF12
        //eipClient.RegisterSession("192.168.0.110");
        //new leggiPeso().start();/
        int OUT_KEY = 0x64;
        int IN_KEY = 0x65;
        /////////////////////////////
        //CREAZIONE NUOVO CLIENT  ///
        ///////////////////////////// 
        eipClient.setIpAddress("192.168.0.110");
        eipClient.RegisterSession();

        //////////////////////
        // CONFIGURAZIONE  ///
        //////////////////////
        //Parameters for Originator -> Target communication
        eipClient.setO_T_InstanceID(OUT_KEY);       //Output Assembly 65hex
        eipClient.setO_T_Length(2);
        eipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
        eipClient.setO_T_ownerRedundant(false);
        eipClient.setO_T_priority(Priority.Urgent);
        eipClient.setO_T_variableLength(false);
        eipClient.setO_T_connectionType(ConnectionType.Point_to_Point);
        eipClient.setRequestedPacketRate_O_T(2000);

        //Parameters for Target -> Originator communication
        eipClient.setT_O_InstanceID(IN_KEY);       //Input Assembly 68hex
        eipClient.setT_O_Length(16);
        eipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
        eipClient.setT_O_ownerRedundant(false);
        eipClient.setT_O_priority(Priority.Urgent);
        eipClient.setT_O_variableLength(false);
        eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
        eipClient.setRequestedPacketRate_T_O(2000);

        new comunica().start();

    }

    private static class comunica extends Thread {

        @Override
        public void run() {

            int tempo = 1000;
            try {
//
//                eipClient.ForwardOpen();
                //System.out.println("Peso Netto");
                //Integer byte_1 = 7;
                //System.out.println("Peso Lordo");
                //Integer byte_1 = 9;

                int counter = 0;
                while (counter < 1) {

                    //System.out.println(eipClient.T_O_IOData[0]);
                    //System.out.println(eipClient.O_T_IOData[0]);
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    System.out.println("Pilota 1 - Run ");
                    eipClient.ForwardOpen();
                    Integer value = CloudFab5_0.ConversioneBinarioToInt("1111111111");
                    eipClient.O_T_IOData[2] = value.byteValue();
                    this.sleep(tempo);
                    eipClient.ForwardClose();

                    eipClient.ForwardOpen();
                    System.out.println("Prova Lettura");
                    byte[] data;
                        try {
                            data = eipClient.getAssemblyObject().getInstance(0x65);
                            System.out.print("diagnostica =\t" + eipClient.T_O_IOData[0]); 
                            Thread.sleep(500);
                        } catch (CIPException | IOException ex) {
                        }
                  
                    eipClient.ForwardClose();

                    //System.out.println(eipClient.T_O_IOData[0]);
                    //System.out.println(eipClient.O_T_IOData[0]);
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    System.out.println("Pilota 1 - Stop");
                    eipClient.ForwardOpen();
                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
                    this.sleep(tempo);
                    eipClient.ForwardClose();

////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 2 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000010").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 2 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 3 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000100").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 3  - Stop ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 4 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00001000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 4  - Stop ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 5 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00010000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 5 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 6 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00100000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 6 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 7 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("01000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 7 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                       ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 8 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("10000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 8 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                    
////                    
////                    
////                    
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 9 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000001").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 9 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 10 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000010").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 10 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 11 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000100").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 11 - Stop ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 12 - Run");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00001000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 12  - Stop ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 13 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00010000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 13 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 14 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00100000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 14 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 15 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("01000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 15 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                       ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 16 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("10000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 16 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[1] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                       ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 17 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[2] = CloudFab5_0.ConversioneBinarioToInt("00000001").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 17 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[2] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////                    
////                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 18 - Run ");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[2] = CloudFab5_0.ConversioneBinarioToInt("00000010").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
////
////                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////                    System.out.println("Pilota 18 - Stop");
////                    eipClient.ForwardOpen();
////                    eipClient.O_T_IOData[2] = CloudFab5_0.ConversioneBinarioToInt("00000000").byteValue();
////                    this.sleep(tempo);
////                    eipClient.ForwardClose();
                    counter++;

                }
                System.out.println("Fine");

                eipClient.UnRegisterSession();

            } catch (CIPException | IOException | InterruptedException ex) {
                Logger.getLogger(EB80Metalwork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}

//    private static class provaThread extends Thread {
//
//        @Override
//        public void run() {
//            try {
//
//                //System.out.println("Peso Netto");
//                //Integer byte_1 = 7;
//                //System.out.println("Peso Lordo");
//                //Integer byte_1 = 9;
//                while (true) {
//
//                    try {
//                        this.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    System.out.println("Modo Standard");
//                    Integer byte_1 = 27;
//                    byte[] out = new byte[]{byte_1.byteValue()};
//
//                    eipClient.getAssemblyObject().setInstance(0x64, out);
//
//                    try {
//                        this.sleep(200);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    //System.out.println(eeipClient.GetAttributeAll(0x04,0x65));
//                    digitalInputs = eipClient.getAssemblyObject().getInstance(0x65);
//
//                    int sLordo = 1;
//                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//                        sLordo = -1;
//
//                    }
//                    int sNetto = 1;
//                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//                        sNetto = -1;
//
//                    }
//                    System.out.println("Peso Lordo: " + sLordo * (EEIPClient.ToUint(new byte[]{digitalInputs[0], digitalInputs[1], digitalInputs[2], digitalInputs[3]})));
//                    System.out.println("Peso Netto: " + sNetto * (EEIPClient.ToUint(new byte[]{digitalInputs[4], digitalInputs[5], digitalInputs[6], digitalInputs[7]})));
//
//////                    System.out.println(("Registro di Scambio: " + (EEIPClient.ToUint(new byte[]{digitalInputs[8], digitalInputs[9], digitalInputs[10], digitalInputs[11]}))));
//////                    System.out.println("Status Register: " + 
//////                              EEIPClient.ToBool(digitalInputs[12], 0)
//////                            + EEIPClient.ToBool(digitalInputs[12], 1)
//////                            + EEIPClient.ToBool(digitalInputs[12], 2)
//////                            + EEIPClient.ToBool(digitalInputs[12], 3)
//////                            + EEIPClient.ToBool(digitalInputs[12], 4)
//////                            + EEIPClient.ToBool(digitalInputs[12], 5)
//////                            + EEIPClient.ToBool(digitalInputs[12], 6)
//////                            + EEIPClient.ToBool(digitalInputs[12], 7)
//////                    );
//////
//////                    System.out.println("Status Register: " + 
//////                              EEIPClient.ToBool(digitalInputs[13], 0)
//////                            + EEIPClient.ToBool(digitalInputs[13], 1)
//////                            + EEIPClient.ToBool(digitalInputs[13], 2)
//////                            + EEIPClient.ToBool(digitalInputs[13], 3)
//////                            + EEIPClient.ToBool(digitalInputs[13], 4)
//////                            + EEIPClient.ToBool(digitalInputs[13], 5)
//////                            + EEIPClient.ToBool(digitalInputs[13], 6)
//////                            + EEIPClient.ToBool(digitalInputs[13], 7));
//                    try {
//                        this.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    System.out.println("Modo Low Res");
//                    byte_1 = 24;
//                    out = new byte[]{byte_1.byteValue()};
//                    eipClient.getAssemblyObject().setInstance(0x64, out);
//                    try {
//                        this.sleep(200);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    //System.out.println(eeipClient.GetAttributeAll(0x04,0x65));
//                    digitalInputs = eipClient.getAssemblyObject().getInstance(0x65);
//
//////                    sLordo = 1;
//////                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//////                        sLordo = -1;
//////
//////                    }
//////                    sNetto = 1;
//////                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//////                        sNetto = -1;
//////
//////                    }
//                    System.out.println("0x0000 0x0001 :" + EEIPClient.ToUshort(new byte[]{digitalInputs[0], digitalInputs[1]}));
//                    System.out.println("0x0002 0x0003 :" + EEIPClient.ToUshort(new byte[]{digitalInputs[2], digitalInputs[3]}));
//                    System.out.println("0x0004 0x0005 :" + EEIPClient.ToUshort(new byte[]{digitalInputs[4], digitalInputs[5]}));
//                    System.out.println("0x0006 0x0007 :" + EEIPClient.ToUshort(new byte[]{digitalInputs[6], digitalInputs[7]}));
//
////////                    System.out.println(("Registro di Scambio: " + (EEIPClient.ToUint(new byte[]{digitalInputs[8], digitalInputs[9], digitalInputs[10], digitalInputs[11]}))));
////////                    System.out.println("Status Register: " + 
////////                              EEIPClient.ToBool(digitalInputs[12], 0)
////////                            + EEIPClient.ToBool(digitalInputs[12], 1)
////////                            + EEIPClient.ToBool(digitalInputs[12], 2)
////////                            + EEIPClient.ToBool(digitalInputs[12], 3)
////////                            + EEIPClient.ToBool(digitalInputs[12], 4)
////////                            + EEIPClient.ToBool(digitalInputs[12], 5)
////////                            + EEIPClient.ToBool(digitalInputs[12], 6)
////////                            + EEIPClient.ToBool(digitalInputs[12], 7)
////////                    );
////////
////////                    System.out.println("Status Register: " + 
////////                              EEIPClient.ToBool(digitalInputs[13], 0)
////////                            + EEIPClient.ToBool(digitalInputs[13], 1)
////////                            + EEIPClient.ToBool(digitalInputs[13], 2)
////////                            + EEIPClient.ToBool(digitalInputs[13], 3)
////////                            + EEIPClient.ToBool(digitalInputs[13], 4)
////////                            + EEIPClient.ToBool(digitalInputs[13], 5)
////////                            + EEIPClient.ToBool(digitalInputs[13], 6)
////////                            + EEIPClient.ToBool(digitalInputs[13], 7));
//                    try {
//                        this.sleep(1000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    System.out.println("Modo HiRes");
//                    byte_1 = 25;
//                    out = new byte[]{byte_1.byteValue()};
//                    eipClient.getAssemblyObject().setInstance(0x64, out);
//                    try {
//                        this.sleep(200);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    //System.out.println(eeipClient.GetAttributeAll(0x04,0x65));
//                    digitalInputs = eipClient.getAssemblyObject().getInstance(0x65);
//
//                    sLordo = 1;
//                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//                        sLordo = -1;
//
//                    }
//                    sNetto = 1;
//                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//                        sNetto = -1;
//
//                    }
//
//                    System.out.println("0x0000 0x0003 :" + sLordo * (EEIPClient.ToUint(new byte[]{digitalInputs[0], digitalInputs[1], digitalInputs[2], digitalInputs[3]})));
//                    System.out.println("0x0004 0x0007 :" + sLordo * (EEIPClient.ToUint(new byte[]{digitalInputs[4], digitalInputs[5], digitalInputs[6], digitalInputs[7]})));
//                    System.out.println("0x0008 0x000B :" + sLordo * (EEIPClient.ToUint(new byte[]{digitalInputs[8], digitalInputs[9], digitalInputs[10], digitalInputs[11]})));
//                    System.out.println("0x000C 0x000F :" + sLordo * (EEIPClient.ToUint(new byte[]{digitalInputs[12], digitalInputs[13], digitalInputs[14], digitalInputs[15]})));
//
////////                    System.out.println(("Registro di Scambio: " + (EEIPClient.ToUint(new byte[]{digitalInputs[8], digitalInputs[9], digitalInputs[10], digitalInputs[11]}))));
////////                    System.out.println("Status Register: " + 
////////                              EEIPClient.ToBool(digitalInputs[12], 0)
////////                            + EEIPClient.ToBool(digitalInputs[12], 1)
////////                            + EEIPClient.ToBool(digitalInputs[12], 2)
////////                            + EEIPClient.ToBool(digitalInputs[12], 3)
////////                            + EEIPClient.ToBool(digitalInputs[12], 4)
////////                            + EEIPClient.ToBool(digitalInputs[12], 5)
////////                            + EEIPClient.ToBool(digitalInputs[12], 6)
////////                            + EEIPClient.ToBool(digitalInputs[12], 7)
////////                    );
////////
////////                    System.out.println("Status Register: " + 
////////                              EEIPClient.ToBool(digitalInputs[13], 0)
////////                            + EEIPClient.ToBool(digitalInputs[13], 1)
////////                            + EEIPClient.ToBool(digitalInputs[13], 2)
////////                            + EEIPClient.ToBool(digitalInputs[13], 3)
////////                            + EEIPClient.ToBool(digitalInputs[13], 4)
////////                            + EEIPClient.ToBool(digitalInputs[13], 5)
////////                            + EEIPClient.ToBool(digitalInputs[13], 6)
////////                            + EEIPClient.ToBool(digitalInputs[13], 7));
//                }
//                //When done, we unregister the session
//                //eeipClient.UnRegisterSession();
//            } catch (IOException ex) {
//                Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (CIPException ex) {
//                Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//
//    }
//
//    private static class leggiPeso extends Thread {
//
//        @Override
//        public void run() {
//            try {
//
//                //System.out.println("Peso Netto");
//                //Integer byte_1 = 7;
//                //System.out.println("Peso Lordo");
//                //Integer byte_1 = 9;
//                while (true) {
//
//                    try {
//                        this.sleep(200);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                    //System.out.println("Modo Standard");
////                    Integer byte_1 = 255;
////                    byte[] out = new byte[]{byte_1.byteValue()};
//                    eipClient.ForwardOpen();
//                    eipClient.SetAttributeSingle(0x03, 1, 0x27, new byte[]{(byte) 0xFF});
//                    eipClient.ForwardClose();
//////                    try {
//////                        this.sleep(200);
//////                    } catch (InterruptedException ex) {
//////                        Logger.getLogger(ProvaEthernetIPLaumas3.class.getName()).log(Level.SEVERE, null, ex);
//////                    }
//                    //System.out.println(eipClient.GetAttributeAll(0x44,0x65).length);
//                    digitalInputs = eipClient.getAssemblyObject().getInstance(0x65);
//
//                    //          int sLordo = 1;
//                    //          if (EEIPClient.ToBool(digitalInputs[13], 0)) {
//                    //               sLordo = -1;
////
//                    // }
////                    int sNetto = 1;
////                    if (EEIPClient.ToBool(digitalInputs[13], 0)) {
////                        sNetto = -1;
////                        //}
////                        System.out.println(digitalInputs[0]
////                                + "\t" + digitalInputs[1]
////                                + "\t" + digitalInputs[2]
////                                + "\t" + digitalInputs[3]
////                                + "\t" + digitalInputs[4]
////                                + "\t" + digitalInputs[5]
////                                + "\t" + digitalInputs[6]
////                                + "\t" + digitalInputs[7]
////                        );
//                    //System.out.println("Peso Lordo: " + sLordo * (EEIPClient.ToUint(new byte[]{digitalInputs[0], digitalInputs[1], digitalInputs[2], digitalInputs[3]})));
//                    //System.out.println("Peso Netto: " + sNetto * (EEIPClient.ToUint(new byte[]{digitalInputs[4], digitalInputs[5], digitalInputs[6], digitalInputs[7]})));
//////                        System.out.println(EEIPClient.ToUint(new byte[]{digitalInputs[8], digitalInputs[7], digitalInputs[6], digitalInputs[5], digitalInputs[4], digitalInputs[3], digitalInputs[2], digitalInputs[1], digitalInputs[0]}));
//                }
//                //When done, we unregister the session
//                //eeipClient.UnRegisterSession();
//            } catch (IOException ex) {
//                Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (CIPException ex) {
//                Logger.getLogger(ProvaEthernetIP3.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//
//    }
//
//}
