/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

import de.re.eeip.cip.exception.CIPException;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneStringToHex;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author imacdigaudio
 */
public class TrasmettitoreHX711 {

    public static String ip = "192.168.1.177";
    public static int port = 1840;
    public static int timeout = 2000;
    private static InetAddress address;
    private static DatagramSocket socket;
    static boolean threadAvviato = false;
    static boolean interrompiThread = false;
    static int index = 0;
    //public String msg;
    //public String msg_prev;
    public static int id_pac;

    private static final Integer dim_pacchetti = 24;
    private static final String char_finale = "&";
    private static final String char_iniziale = "$";

    private static JLabel req;
    private static JLabel rep;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws de.re.eeip.cip.exception.CIPException
     */
    public static void main(String[] args) throws IOException, CIPException {

        address = InetAddress.getByName(ip);
        socket = new DatagramSocket();
        socket.setSoTimeout(timeout);

        JFrame f = new JFrame("Trasmettitore di peso HX711");

        Container c = f.getContentPane();                               // recupera il "muro grezzo" 
        // cioè il riquadro dei contenuti senza barra dei menù                                                                    
        // per impostare le dimensioni e la posizione:                
        f.setSize(400, 250);                      // misure in pixel: larghezza, altezza
        f.setLocation(200, 100);              // (0,0) angolo sup. sin                            
        JPanel p = new JPanel();
        p.setBackground(Color.lightGray);       // sfondo del pannello colorato    
        c.add(p);                  // aggiunge il pannello               
        f.setVisible(true);    // mostra il frame (dimensioni 300x150)   -  deprecato show()
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      // rende attiva l'icona di chi

        p.setLayout(null);
        req = new JLabel("richiesta");
        req.setBounds(10, 10, 200, 30);

        rep = new JLabel("0");
        rep.setBounds(200, 10, 200, 30);

        JButton avvioLetturaContinua = new JButton("Media");
        avvioLetturaContinua.setBounds(10, 50, 120, 30);
        avvioLetturaContinua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                index = 0;
                verificaThread();

            }
        });

        JButton avvioLetturaCella1 = new JButton("Cella 1");
        avvioLetturaCella1.setBounds(10, 85, 120, 30);
        avvioLetturaCella1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                index = 1;
                verificaThread();

            }
        });

        JButton avvioLetturaCella2 = new JButton("Cella 2 ");
        avvioLetturaCella2.setBounds(10, 120, 120, 30);
        avvioLetturaCella2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                verificaThread();
                index = 2;

            }
        });

        JButton avvioLetturaCella3 = new JButton("Cella 3");
        avvioLetturaCella3.setBounds(10, 155, 120, 30);
        avvioLetturaCella3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                verificaThread();
                index = 3;

            }
        });

        JButton abilitaSchermo = new JButton("Abilita Schermo");
        abilitaSchermo.setBounds(140, 50, 120, 30);
        abilitaSchermo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("E"));

            }
        });

        JButton disabilitaSchermo = new JButton("Disabilita Schermo");
        disabilitaSchermo.setBounds(140, 85, 120, 30);
        disabilitaSchermo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("D"));

            }
        });
        JButton visualizzaMediaNonTarato = new JButton("Media Non Tarata");
        visualizzaMediaNonTarato.setBounds(140, 155, 120, 30);
        visualizzaMediaNonTarato.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("S"));

            }
        });
        JButton visualizzaMediaTarato = new JButton("Media Tarata");
        visualizzaMediaTarato.setBounds(140, 120, 120, 30);
        visualizzaMediaTarato.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("T51752.7_0.012"));
            }
        });

        JButton displayCella1 = new JButton("Vis Dato Cella1");
        displayCella1.setBounds(270, 50, 120, 30);
        displayCella1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("I"));

            }
        });

        JButton displayCella2 = new JButton("Vis Dato Cella2");
        displayCella2.setBounds(270, 85, 120, 30);
        displayCella2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("J"));

            }
        });

        JButton displayCella3 = new JButton("Vis Dato Cella3");
        displayCella3.setBounds(270, 120, 120, 30);
        displayCella3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;
                rep.setText(send("T51752.7_0.012"));
            }
        });

        JButton stopLetturaContinua = new JButton("Stop");
        stopLetturaContinua.setBounds(270, 155, 120, 30);
        stopLetturaContinua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                interrompiThread = true;

            }
        });

        p.add(req);
        p.add(rep);
        p.add(stopLetturaContinua);
        p.add(avvioLetturaContinua);
        p.add(avvioLetturaCella1);
        p.add(avvioLetturaCella2);
        p.add(avvioLetturaCella3);
        p.add(abilitaSchermo);
        p.add(disabilitaSchermo);
        p.add(visualizzaMediaTarato);
        p.add(visualizzaMediaNonTarato);
        p.add(displayCella1);
        p.add(displayCella2);
        p.add(displayCella3);

        f.setVisible(true);

////////////            System.out.print("\n\nValore Medio \t\t");
////////////            System.out.println(send("M"));
////////////            Thread.sleep(20);
//////////////            System.out.print("Cella 1\t\t"); System.out.println(send("A")); Thread.sleep(20);
//////////////            System.out.print("Cella 2\t\t"); System.out.println(send("B")); Thread.sleep(20);
//////////////            System.out.print("Cella 3\t\t"); System.out.println(send("C")); Thread.sleep(20);
////////////
//////////////            System.out.println(send("E"));
//////////////            Thread.sleep(5000);
//////////////            System.out.println(send("T51752.7_0.012"));
//////////////            Thread.sleep(5000);
//////////////             System.out.println(send("S"));
//////////////             Thread.sleep(5000);
//////////////            System.out.println(send("I"));
//////////////            Thread.sleep(2000);
//////////////            System.out.println(send("J"));
//////////////            Thread.sleep(2000);
//////////////            System.out.println(send("K"));
//////////////            
////////////////            Thread.sleep(2000);
////////////////            System.out.println(send("D"));
////////////
//////////////           System.out.println(send("D")); Thread.sleep(1000);
////////////////            System.out.println(send("T-23000_23.23")); Thread.sleep(1000);   
////////////////            System.out.println(send("S")); Thread.sleep(1000);   
////////////////            System.out.println(send("I")); Thread.sleep(1000);   
////////////////            System.out.println(send("J")); Thread.sleep(1000);   
////////////////            System.out.println(send("K")); Thread.sleep(1000);   
////////////////            System.out.println(send("M")); Thread.sleep(1000);   
////////////              }
////////////        } catch (Exception ex) {
////////////        }
        //System.out.println(pesa.sendToSocket("Oabc"));
        //new letturaPesoNetto().start();
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

    private static void verificaThread() {
        if (!threadAvviato) {
            new ThreadComunica().start();
        }
    }

    private static class ThreadComunica extends Thread {

        @Override
        public void run() {

            threadAvviato = true;
            interrompiThread = false;
            while (!interrompiThread) {

                try {

                    switch (index) {
                        case 0:
                            req.setText("Valore Medio");
                            rep.setText(send("M"));
                            break;
////////////            
////////////             while (true) {
////////////            System.out.print("\n\nValore Medio \t\t");
////////////            System.out.println(send("M"));
////////////            Thread.sleep(20);
//////////////            System.out.print("Cella 1\t\t"); System.out.println(send("A")); Thread.sleep(20);
//////////////            System.out.print("Cella 2\t\t"); System.out.println(send("B")); Thread.sleep(20);
//////////////            System.out.print("Cella 3\t\t"); System.out.println(send("C")); Thread.sleep(20);
////////////
//////////////            System.out.println(send("E"));
//////////////            Thread.sleep(5000);
//////////////            System.out.println(send("T51752.7_0.012"));
//////////////            Thread.sleep(5000);
//////////////             System.out.println(send("S"));
//////////////             Thread.sleep(5000);
//////////////            System.out.println(send("I"));
//////////////            Thread.sleep(2000);
//////////////            System.out.println(send("J"));
//////////////            Thread.sleep(2000);
//////////////            System.out.println(send("K"));
//////////////            
////////////////            Thread.sleep(2000);
////////////////            System.out.println(send("D"));
////////////
//////////////           System.out.println(send("D")); Thread.sleep(1000);
////////////////            System.out.println(send("T-23000_23.23")); Thread.sleep(1000);
////////////////            System.out.println(send("S")); Thread.sleep(1000);
////////////////            System.out.println(send("I")); Thread.sleep(1000);
////////////////            System.out.println(send("J")); Thread.sleep(1000);
////////////////            System.out.println(send("K")); Thread.sleep(1000);
////////////////            System.out.println(send("M")); Thread.sleep(1000);   
////////////              }
                        case 1:
                            req.setText("Valore Cella 1");
                            rep.setText(send("A"));
                            break;
                        case 2:
                            req.setText("Valore Cella 2");
                            rep.setText(send("B"));
                            break;
                        case 3:
                            req.setText("Valore Cella 3");
                            rep.setText(send("C"));
                            break;
                        default:
                            break;
                    }

                    this.sleep(10);
                } catch (InterruptedException ex) {
                }

            }
            threadAvviato = false;

        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////// Invio dati al Server UDP
    public static String send(String messaggio) {
        String result = "";

        try {

            if (!messaggio.equals("")) {

                //Calcolo contatore pacchetti
                String counter = contaIdPacchetto();
                //Dichiarazione buffer da inviare
                byte[] sendData = (char_iniziale + counter + messaggio + char_finale).getBytes();

                //Invio Pacchetto
                socket.send(new DatagramPacket(sendData, sendData.length, address, port));

                byte[] buffer = new byte[dim_pacchetti];

                Thread.sleep(20);

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

                Thread.sleep(2);
            }

        } catch (IOException | InterruptedException ex) {
            System.out.println(ex);
        }
        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////// Restituisce Id Pacchetto
    public static String contaIdPacchetto() {
        String result = ConversioneStringToHex(Integer.toString(id_pac), 2);

        if (id_pac == 0xFF) {
            id_pac = -1;
        } else {
            id_pac++;
        }
        return result;

    }
}
