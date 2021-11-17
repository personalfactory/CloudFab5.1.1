/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.stampante;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Francesco Di Gaudio
 */
public class CitronixCIJ_Client {

    private final String address;
    private final int port;
    private final String nome_prodotto;
    private final String luogo_produzione;
    private final String contatore;
////    
////    private final String data;
////    private final String ora;
////    private final int numero_miscela;
////    private final int numero_sacco;
    private final String STR_MESSAGE_DATA = "MD";

    /**
     * Descrizione: This command provides the ability to download data to a
     * pre-existing message, selected for printing. Each data field is entered
     * and separated by a comma. The data is loaded in the order of which the
     * data fields were created. The comma after the MD and the comma before the
     * <CR> are optional. To use a comma as data, a TAB character (decimal 9)
     * may be used as the field separator. To use one data field with a comma as
     * data, a TAB must be used after the MD. Example string:
     * MD,xxxxxxxx,iiiiiiiiiiiiiii,nnnnnnnnnnn,bbbbbbbbbbbb,<CR>
     * Explanation: x =Field 1 Data i =Field 2 Data n =Field 3 Data b =Field 4
     * Data additional fields can be added by using the “,” as the separator.
     *
     *
     */
    private final String STR_CLEAR_BUFFER = "CL";       //  Esempio: CLY<CR>    Opzioni: Y/N        Descrizione: Clears current print buffer (to generate new one)
    private final String STR_CLEAN_MESSAGES = "CM";     //  Esempio CM<CR>      Opzioni: None       Descrizione: Clears message storage (deletes all messages).

    public CitronixCIJ_Client(
            String address, //Indirizzo Stampante
            int port, //Prota Stampante
            String nome_prodotto, //Nome Prodotto
            //String data, //Data
            String luogo_produzione, //Luogo di Produzione
            //String ora, //Ora Produzione
            //int numero_miscela, //Id Miscela
            //int numero_sacco //Id Sacco
            String contatore
    ) {
        this.address = address;
        this.port = port;
        this.nome_prodotto = nome_prodotto;
        this.luogo_produzione = luogo_produzione; 
        this.contatore = contatore;
////        this.data = data;
////        this.ora = ora;
////        this.numero_miscela = numero_miscela;
////        this.numero_sacco = numero_sacco;

        new InvioStringa(costruisciMessaggio()).start();

    }

    //Costruzione Messaggio
    private String costruisciMessaggio() {

        String result = STR_MESSAGE_DATA + ","
                + nome_prodotto + ","
                //+ data + ","
                + luogo_produzione + ","
                //+ ","
                //+ ora + ","
                + "" + ","
                //+ numero_sacco 
                + contatore 
                + "\n";

        return result;

    }

    //Inivio Messaggio
    public class InvioStringa extends Thread {

        private final String sentence;

        public InvioStringa(String sentence) {
            this.sentence = sentence;

        }

        @Override
        public void run() {

            try {

                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

                try (Socket clientSocket = new Socket(address, port)) {

                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
                    outToServer.writeBytes(sentence + "\n");
                    String reply = inFromServer.readLine(); 
                }

            } catch (IOException e) {

                System.err.println(this.getClass() + "Errore - e =" + e);
            }

        }

    }
}
