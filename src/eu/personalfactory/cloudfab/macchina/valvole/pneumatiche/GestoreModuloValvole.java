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
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneBinarioToInt;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_DELAY;
import java.io.IOException;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_IN_KEY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_OUT_KEY;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class GestoreModuloValvole {

    private static final EEIPClient eipClient = new EEIPClient();

    public GestoreModuloValvole(String ipAddress) throws InterruptedException, IOException, CIPException {

        /////////////////////////////
        //CREAZIONE NUOVO CLIENT  ///
        ///////////////////////////// 
        eipClient.setIpAddress(ipAddress);
        eipClient.RegisterSession();

        //////////////////////  
        // CONFIGURAZIONE  ///
        //////////////////////
        //Parameters for Originator -> Target communication
        eipClient.setO_T_InstanceID(MODULO_VALVOLE_OUT_KEY);
        eipClient.setO_T_Length(1);
        eipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
        eipClient.setO_T_ownerRedundant(false);
        eipClient.setO_T_priority(Priority.Urgent);
        eipClient.setO_T_variableLength(false);
        eipClient.setO_T_connectionType(ConnectionType.Point_to_Point);
        eipClient.setRequestedPacketRate_O_T(5000000);

        //Parameters for Target -> Originator communication
        eipClient.setT_O_InstanceID(MODULO_VALVOLE_IN_KEY);
        eipClient.setT_O_Length(16);
        eipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
        eipClient.setT_O_ownerRedundant(false);
        eipClient.setT_O_priority(Priority.Urgent);
        eipClient.setT_O_variableLength(false);
        eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
        eipClient.setRequestedPacketRate_T_O(5000000);
        
        eipClient.ListIdentity();

        eipClient.ForwardOpen();
        System.out.println("Pilota 1 - Run ");
        eipClient.ForwardOpen();
        eipClient.O_T_IOData[0] = CloudFab5_0.ConversioneBinarioToInt("00000001").byteValue();

        eipClient.ForwardClose();

    }

    public void modificaStatoUscita(Integer id, Boolean stato) {

        int byte_index = id / 8;
        int out_index = id - (id / 8) * 8;
        boolean res = true;
        System.out.println("byte index =" + byte_index);
        System.out.println("out index =" + out_index);
        try {

////            eipClient.ForwardOpen();
////            String stato_uscita = Byte.toString(eipClient.T_O_IOData[byte_index]);
////
////            while (stato_uscita.length() < 8) {
////
////                stato_uscita = stato_uscita + "0";
////
////            }
////            System.out.println("stato_uscita = " +stato_uscita);
////            Thread.sleep(100);
////            eipClient.ForwardClose();
            String stato_uscita = "00000000";
            String value = "0";
            if (stato) {
                value = "1";
            }

            String temp = "";
            for (int i = 0; i < stato_uscita.length(); i++) {

                if (i == out_index) {
                    temp += value;
                } else {
                    temp += stato_uscita.charAt(i);

                }

            }

            stato_uscita = "";
            for (int i = 0; i < temp.length(); i++) {
                stato_uscita += temp.charAt((temp.length() - 1) - i);

            }

            System.out.println("byte index =" + byte_index);
            System.out.println("out index =" + out_index);
            System.out.println("stato uscita =" + stato_uscita);
            System.out.println("ConversioneBinarioToInt(stato_uscita).byteValue() =" + ConversioneBinarioToInt(stato_uscita).byteValue());

            eipClient.ForwardOpen();
            eipClient.O_T_IOData[byte_index] = ConversioneBinarioToInt(stato_uscita).byteValue();

            eipClient.ForwardClose();

            Thread.sleep(600);

        } catch (CIPException | IOException | InterruptedException e) {
        }

    }

    public void inviaStringa(Integer id, String stato_uscite) throws CIPException, IOException, InterruptedException {

////        System.out.println("invia Stringa =" + stato_uscite);
////        //Thread.sleep(MODULO_VALVOLE_DELAY);
////        //eipClient.ForwardOpen();
////        eipClient.ForwardOpen();
////        eipClient.O_T_IOData[id] = ConversioneBinarioToInt(stato_uscite).byteValue();
////        Thread.sleep(MODULO_VALVOLE_DELAY);
////        eipClient.ForwardClose();
        new comunica(stato_uscite, eipClient).start();
    }

    public class comunica extends Thread {

        String stato_uscite;
        EEIPClient eipClient;

        public comunica(String stato_uscite, EEIPClient eipClient) {
            this.stato_uscite = stato_uscite;
            this.eipClient = eipClient;
        }

        @Override
        public void run() {

            try {
                System.out.println("invia Stringa =" + stato_uscite);
                this.sleep(MODULO_VALVOLE_DELAY);
                eipClient.ForwardOpen();
                eipClient.O_T_IOData[0] = ConversioneBinarioToInt(stato_uscite).byteValue();
                this.sleep(100);
                eipClient.ForwardClose();
            } catch (CIPException | IOException | InterruptedException ex) {
                Logger.getLogger(GestoreModuloValvole.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
