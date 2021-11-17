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
public class GestorePesaUDPModbus {

    //VARIABILI
    public String host;
    private int port;
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private byte[] receiveData; 
    
    //COSTRUTTORE
    public GestorePesaUDPModbus(String host, int port, int timeout, int dataGramDim) {
 
        this.port = port;
        receiveData = new byte[dataGramDim];


        try {
            //Creazione Datagram Socket
            clientSocket = new DatagramSocket(port);

            clientSocket.setSoTimeout(timeout);

            //Memorizzazione Log Bilance
            System.err.println("Creazione del Datagram Socket............OK");

            IPAddress = InetAddress.getByName(host);

            //Memorizzazione Log Bilance
            System.err.println("Creazione InetAddress " + host + " ............OK");


        } catch (SocketException | UnknownHostException ex) {

           //Memorizzazione Log Bilance
            System.err.println("Errore durante la creazione del DatagramSocket..." + ex);

        }

    }

    //Invio Stringa msg sulla Porta Seriale Aperta
    public String sendToSocket(String msg) {

        String result = "";

        msg += "\n";

        //Dichiarazione Buffer in Ricezione
        byte[] sendData = msg.getBytes(Charset.defaultCharset());

        System.err.println("Invio del Pacchetto ="+ msg);
        
        //Creazione del Pacchetto
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

        try {

            ///////////////////////////
            // INVIO DEL PACCHETTO  ///
            ///////////////////////////

            //Invio del Pacchetto
            clientSocket.send(sendPacket);
            
            System.err.println("Pacchetto Inviato");

            ///////////////////////////////
            // RICEZIONE DEL PACCHETTO  ///
            ///////////////////////////////

            //Dichiarazioned del Pacchetto in ricezione
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            //Ricezione del Pacchetto
            clientSocket.receive(receivePacket);
            

            //Conversione da Byte a String
            String msgRec = new String(receivePacket.getData());
            
            System.err.println("Lunghezza Pacchetto Ricevuto =" + msgRec.length());
   
            //Validazione Stringa Ricevuta
            for (int i = 0; i < msgRec.length(); i++) {

              //  if (msgRec.charAt(i) == '&') {
//
                    result += msgRec.charAt(i);
                  //  inTrovato = true;
                  //  count++;
//
         //       } else if (inTrovato) {
         //           result += msgRec.charAt(i);
         //           count++;
//
         //       }
         //       if (count >= lenString) {
//
         //           break;
          //      }
            }
            
            
        } catch (IOException ex) {
            
            System.err.println("Errore durante la ricezione del pacchetto");
        }
        
        
        System.err.println("Pacchetto Ricevuto =" + result);

        return result;


    }
}
