 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

import eu.personalfactory.cloudfab.macchina.loggers.BilanceLogger;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 *
 * @author francescodigaudio
 *
 */
public class GestorePesaUDP {

    //VARIABILI
    public String host;
    private int port;
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private byte[] receiveData;
    private int lenString; 
    
    //COSTRUTTORE
    public GestorePesaUDP(String host, int port, int timeout, int lenString, int dataGramDim) {
 
        this.port = port;
        this.lenString = lenString;
        receiveData = new byte[dataGramDim];


        try {
            //Creazione Datagram Socket
            clientSocket = new DatagramSocket(port);

            clientSocket.setSoTimeout(timeout);

            //Memorizzazione Log Bilance
            BilanceLogger.logger.config("Creazione del Datagram Socket............OK");

            IPAddress = InetAddress.getByName(host);

            //Memorizzazione Log Bilance
            BilanceLogger.logger.config("Creazione InetAddress " + host + " ............OK");


        } catch (SocketException | UnknownHostException ex) {

           //Memorizzazione Log Bilance
            BilanceLogger.logger.severe("Errore durante la creazione del DatagramSocket..." + ex);

        }

    }

    //Invio Stringa msg sulla Porta Seriale Aperta
    public String sendToSocket(String msg) {

        String result = "";

        msg += "\n";

        //Dichiarazione Buffer in Ricezione
        byte[] sendData = msg.getBytes(Charset.defaultCharset());

        //Creazione del Pacchetto
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

        try {

            ///////////////////////////
            // INVIO DEL PACCHETTO  ///
            ///////////////////////////

            //Invio del Pacchetto
            clientSocket.send(sendPacket);

            ///////////////////////////////
            // RICEZIONE DEL PACCHETTO  ///
            /////////////////////////////// 
            
            //Dichiarazioned del Pacchetto in ricezione
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            //Ricezione del Pacchetto
            clientSocket.receive(receivePacket);

            //Conversione da Byte a String
            String msgRec = new String(receivePacket.getData());

            int count = 0;
            boolean inTrovato = false;
              
            //Validazione Stringa Ricevuta
            for (int i = 0; i < msgRec.length(); i++) {

                if (msgRec.charAt(i) == '&') {

                    result += msgRec.charAt(i);
                    inTrovato = true;
                    count++;

                } else if (inTrovato) {
                    result += msgRec.charAt(i);
                    count++;

                }
                if (count >= lenString) {

                    break;
                }
            }
             
        } catch (IOException ex) {
            
           
        }

        return result;


    }
}
