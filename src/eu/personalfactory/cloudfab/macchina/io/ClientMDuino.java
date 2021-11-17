package eu.personalfactory.cloudfab.macchina.io;

/**
 *
 * @author francescodigaudio
 */
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneBinarioToHex;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneStringToHex;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DELAY_SEND_RECEIVE_UDP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DELAY_SEND_MDUINO;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientMDuino {

    public String ip;
    public int port;
    private InetAddress address;
    private DatagramSocket socket;
    public int id_pac; 

    private Integer dim_pacchetti;
    private String char_finale;
    private String char_iniziale;

    //Costruttore
    public ClientMDuino(String ip, int port, int timeout, Integer dim_pacchetti, String char_iniziale, String char_finale) {

        this.ip = ip;
        this.port = port;
        this.dim_pacchetti = dim_pacchetti;
        this.char_iniziale = char_iniziale;
        this.char_finale = char_finale; 

        ////////////////////////////////////////////////////////////////////////////// Inizializzazione
       
        id_pac = 0;

        try {
            address = InetAddress.getByName(ip);
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            
        } catch (UnknownHostException ex) {

        } catch (SocketException e) { 
		}

    }

    //////////////////////////////////////////////////////////////////////////////////////////// Invio dati al Server UDP
    public String send(String messaggio) {
        String result = "";
        
        
        try {

            if (!messaggio.equals("")) {
            	  
                //Calcolo contatore pacchetti
                String counter = contaIdPacchetto();

                String toSend = char_iniziale + counter + messaggio + char_finale;
                  
                
                //Dichiarazione buffer da inviare
                byte[] sendData = (toSend).getBytes();
                 
                
                //Invio Pacchetto
                socket.send(new DatagramPacket(sendData, sendData.length, address, port));

                byte[] buffer = new byte[dim_pacchetti];
 
            	Thread.sleep(DELAY_SEND_RECEIVE_UDP);
            	 
                //Pacchetto di Risposta
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
 
                //Lettura Risposata dal Server
                socket.receive(response);
 
                String quote = new String(buffer, 0, response.getLength()); 
                 
                //Verifica pacchetto ricevuto 
                if (quote.contains(char_iniziale) && quote.contains(char_finale)) {
                
                    //Pacchetto Formalmente Corretto 
                    quote = quote.substring(quote.indexOf(char_iniziale) + 1, quote.indexOf(char_finale));
                 
                    String id_pacchetto = quote.substring(0, 2);
                    quote = quote.substring(2, quote.length());
                    
                    if (id_pacchetto.equals(counter)) {

                        //Pacchetto Inviato e ricevuto correttamente  
                        result = quote;

                    } else {
                        id_pac--;
                    }

                }  
                
                Thread.sleep(DELAY_SEND_MDUINO);
            }

        } catch (IOException | InterruptedException ex) {
        	 
        } 
         

        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////// Codifica Messaggio da Inviare al MDuino
    public String codificaMessaggioUscite(String messaggio) {
 
        String result = "";
        ArrayList<String> temp = new ArrayList<>();
        
        while (messaggio.length() > 3) {

            temp.add(messaggio.substring(0, 4));
            messaggio = messaggio.substring(4, messaggio.length());

        }
  
        temp.add(messaggio);

        for (int i = 0; i < temp.size(); i++) {

            result += ConversioneBinarioToHex(temp.get(i));

        }
        
        return result;

    }
    //////////////////////////////////////////////////////////////////////////////////// Restituisce Id Pacchetto

    public String contaIdPacchetto() {
        String result = ConversioneStringToHex(Integer.toString(id_pac), 2);

        if (id_pac == 0xFF) {
            id_pac = 0;
        } else {
            id_pac++;
        }
        
        return result.substring(0,2);

    }

    ///////////////////////////////////////////////////////////////////////////////////// Chiusura datagram socket
    public void chiudiSocket() {

        socket.disconnect();
        socket.close();
    }
    
}
