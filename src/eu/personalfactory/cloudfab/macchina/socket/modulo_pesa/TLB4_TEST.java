/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

import de.re.eeip.EEIPClient;
import de.re.eeip.cip.exception.CIPException;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AnalizzaStatusRegister;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.DecodificaStatusRegister;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imacdigaudio
 */
public class TLB4_TEST {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws de.re.eeip.cip.exception.CIPException
     */
    static ArrayList<String> statoEV;
    static byte[] digitalInputs;
    static GestorePesaTLB4EthernetIp pesa1,pesa2;

    static EEIPClient eipClient = new EEIPClient();

    public static void main(String[] args) throws InterruptedException, IOException, CIPException {

        pesa1 = new GestorePesaTLB4EthernetIp("192.168.0.121");
        pesa2 = new GestorePesaTLB4EthernetIp("192.168.0.122");

        //pesa.sendToSocket("$02STD06");
        //System.out.println(pesa.sendToSocket("Oabc"));

        new letturaPesoNetto().start();
        //new letturaNettoLordo().start();
        //new letturaStandardLowHigh().start();
        //new letturaStatus().start();
        //new letturaIngressi().start();
        //new attivaUscite().start();
        //new letturaSetPoint().start();
        //new impostaSetPoint().start();
        //new letturaSetPoint().start();
        //new calibrazione().start();

    }

    private static class letturaPesoNetto extends Thread {

        int freq = 500;

        @Override
        public void run() {

            while (true) {

                try {

                    System.out.println(pesa1.sendToSocket("$01n6C"));

                    this.sleep(freq);

                    System.out.println(pesa2.sendToSocket("$01n6C"));

                    this.sleep(freq);

                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private static class letturaNettoLordo extends Thread {

        int freq = 5;
        int freq2 = 1000;

        @Override
        public void run() {

            while (true) {

                try {
                    pesa1.sendToSocket("$02NET6C");
                    this.sleep(freq2);

                    System.out.print(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);
                    System.out.print("-");
                    System.out.print(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);
                    System.out.print("-");

                    System.out.println(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);

                    pesa1.sendToSocket("$02GROSS6C");
                    this.sleep(freq2);

                    System.out.print(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);
                    System.out.print("-");

                    System.out.print(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);
                    System.out.print("-");
                    System.out.println(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);

                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private static class letturaStandardLowHigh extends Thread {

        int freq = 5;

        @Override
        public void run() {

            while (true) {

                try {

                    System.out.println("IMPOSTAZIONE MODO STANDARD");
                    pesa1.sendToSocket("STD");

                    System.out.println(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);
                    System.out.println(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);
                    System.out.println(pesa1.sendToSocket("$02n6C"));
                    this.sleep(freq);

                    System.out.println("IMPOSTAZIONE MODO LOW RES");
                    pesa1.sendToSocket("LOW");

                    System.out.println(pesa1.sendToSocket("l"));
                    this.sleep(freq);
                    System.out.println(pesa1.sendToSocket("l"));
                    this.sleep(freq);
                    System.out.println(pesa1.sendToSocket("l"));
                    this.sleep(freq);

                    System.out.println("IMPOSTAZIONE MODO HIGH RES");
                    pesa1.sendToSocket("HIGH");

                    System.out.println(pesa1.sendToSocket("h"));
                    this.sleep(freq);
                    System.out.println(pesa1.sendToSocket("h"));
                    this.sleep(freq);
                    System.out.println(pesa1.sendToSocket("h"));
                    this.sleep(freq);

                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private static class letturaStatus extends Thread {

        int freq = 1000;

        @Override
        public void run() {

            while (true) {

                try {
                    AnalizzaStatusRegister(DecodificaStatusRegister(pesa1.sendToSocket("STREG")));
                    letturaStatus.sleep(freq);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private static class letturaIngressi extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    letturaIngressi.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(pesa1.sendToSocket("IN"));
            }

        }

    }

    private static class attivaUscite extends Thread {

        int freq = 200;

        @Override
        public void run() {
            while (true) {

                System.out.println(pesa1.sendToSocket("OA"));

                try {
                    attivaUscite.sleep(freq);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(pesa1.sendToSocket("OaB"));
                try {
                    attivaUscite.sleep(freq);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(pesa1.sendToSocket("ObC"));

                try {
                    attivaUscite.sleep(freq);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(pesa1.sendToSocket("Oc"));
                try {
                    attivaUscite.sleep(freq);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TLB4_TEST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

    private static class letturaSetPoint extends Thread {

        int freq = 5;

        @Override
        public void run() {

            System.out.println(pesa1.sendToSocket("LSP"));

        }

    }

    private static class impostaSetPoint extends Thread {

        int freq = 5;

        @Override
        public void run() {
            System.out.println(pesa1.sendToSocket("s11000"));

            System.out.println(pesa1.sendToSocket("LSP"));
            System.out.println(pesa1.sendToSocket("s22000"));

            System.out.println(pesa1.sendToSocket("LSP"));
            System.out.println(pesa1.sendToSocket("s310000"));

            System.out.println(pesa1.sendToSocket("LSP"));

        }

    }

    private static class calibrazione extends Thread {

        int freq = 5;

        @Override
        public void run() {
            
            System.out.println(pesa1.sendToSocket("CAL_AZTAR"));
            
            System.out.println(pesa1.sendToSocket("CAL_P2010"));
////
////            System.out.println(pesa.sendToSocket("LSP"));
////            System.out.println(pesa.sendToSocket("s20"));
////
////            System.out.println(pesa.sendToSocket("LSP"));
////            System.out.println(pesa.sendToSocket("s30"));
////
////            System.out.println(pesa.sendToSocket("LSP"));

        }

    }

    
}
