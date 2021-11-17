/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.bilancia.valvola.aperta;

import eu.personalfactory.cloudfab.macchina.loggers.BilanciaOMBLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_CONF_FALSE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_CONF_TRUE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_END;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_AVVIO_SECCHI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_CAMBIO_VEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_CONFIG0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_CONFIG1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_CONFIG2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_INIT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_LETTURAPESO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA_ROTAZIONE_INVERSA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_PTYPE_VERIFICA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_SEP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_SEP_CURVA_VEL1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_CHAR_SEP_CURVA_VEL2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BILANCIA_OMB_DIM_PACK_UDP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_AVVIOPESATURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_LETTURAPESO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHECK_SUM_LEN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_DIM_PACK_UDP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_NUM_RIP_INVIO_PACCHETTO;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class BilanciaSacchiValvolaAperta {

    private DatagramSocket udpSocket;
    private InetAddress IpAddress;
    private int porta;
    private int timeout;
    private byte[] receiveData;
    private String statoPesatura;

    //COSTRUTTORE
    public BilanciaSacchiValvolaAperta(String Ip, String porta, String timeout) {

        receiveData = new byte[MICRO_DIM_PACK_UDP];

        try {

            IpAddress = InetAddress.getByName(Ip);

            this.porta = Integer.parseInt(porta);

            this.timeout = Integer.parseInt(timeout);

            //Memorrizzazione Log Sessione
            BilanciaOMBLogger.logger.log(Level.CONFIG, "{3}   Indirizzo Bilancia Sacchi Valvola Aperta ={0} - Porta ={1} - Timeout ={2}", new Object[]{IpAddress, porta, timeout, IpAddress});

            udpSocket = new DatagramSocket();

            udpSocket.setSoTimeout(this.timeout);

            //Memorrizzazione Log Sessione
            BilanciaOMBLogger.logger.log(Level.INFO, "{0}   Creazione Datagram Socket Bilancia Sacchi a Valvola Aperta - Eseguita", IpAddress);

        } catch (UnknownHostException ex) {

            //Memorrizzazione Log Sessione
            BilanciaOMBLogger.logger.log(Level.SEVERE, "{1}   Errore Durante la Creazione del Datagram Socket Bilancia Sacchi a Valvola Aperta - Socket Exception{0}", new Object[]{ex, IpAddress});

        } catch (SocketException ex) {
            //Memorizzazione log
            BilanciaOMBLogger.logger.log(Level.SEVERE, "{2}   {0} Errore Durante la Creazione del Datagram Socket Bilancia Sacchi a Valvola Aperta - Socket Exception - e:{1}",
                    new Object[]{
                        BilanciaSacchiValvolaAperta.class.getName(), ex, IpAddress});
        }

    }
    
    public void closeConnection(){
    
        udpSocket.close();
    
    }

    //COMUNICAZIONE BILANCIA SACCHI A VALVOLA APERTA
    private String invioStringa(String str_to_send) {

        boolean interrompiInvio = false;
        int i = 0;
        String stringa_ricevuta;
        String stringa_ricevuta_valida = "";

        try {
            //Inizio loop
            while (!interrompiInvio) {

                // Invio Stringa
                udpSocket.send(new DatagramPacket((str_to_send).getBytes(), (str_to_send).getBytes().length, IpAddress, porta));

                receiveData = new byte[BILANCIA_OMB_DIM_PACK_UDP];

                //Ricezione Stringa
                udpSocket.receive(new DatagramPacket(receiveData, receiveData.length));

                stringa_ricevuta = new String(receiveData);

                //Memorizzazione log
                BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Dal Server:{0}", new Object[]{stringa_ricevuta, IpAddress});

                String stringa_ver = verifica_stringa(stringa_ricevuta, str_to_send);
 
                if (stringa_ver.length() > 0) {
                    stringa_ricevuta_valida = stringa_ver;
                }
                //Verifica Stringa  
                if (stringa_ver.length() > 0) {

                    ///////////////////////////////////
                    // INVIO ESEGUITO CON SUCCESSO  ///
                    ///////////////////////////////////
                    stringa_ricevuta_valida = stringa_ver;
                    interrompiInvio = true;

                    //Memorizzazione log
                    BilanciaOMBLogger.logger.log(Level.CONFIG, "{0}   Invio eseguito con successo", new Object[]{IpAddress});

                } else if (i > MICRO_NUM_RIP_INVIO_PACCHETTO) {

                    //////////////////////////////////////////
                    // NUM_MAX RIPETIZIONI INVIO SUPERATE  ///
                    //////////////////////////////////////////
                    interrompiInvio = true;

                    //Memorizzazione log
                    BilanciaOMBLogger.logger.log(Level.SEVERE, "{1}   Invio Fallito dopo {0} tentativi", new Object[]{i, IpAddress});

                } else {

                    ////////////////////////////////////////
                    // INVIO NON ESEGUITO CORRETTAMENTE  ///
                    ////////////////////////////////////////
                    i++;

                    //Memorizzazione log
                    BilanciaOMBLogger.logger.log(Level.SEVERE, "{0}   Ripetizione Invio Necessario", IpAddress);
                }

            }//Fine loop

        } catch (IOException ex) {

            //Memorizzazione log
            BilanciaOMBLogger.logger.log(Level.SEVERE, "{1}   Dal Server:{0}", new Object[]{ex, IpAddress});
        }

        return stringa_ricevuta_valida;

    }

    //VERIFICA STRINGA RICEVUTA
    private String verifica_stringa(String str_rec, String str_sent) {
        String result = "";

        /////////////////////////////////////////////
        // VERIFICA INTEGRITA PACCHETTO RICEVUTO  ///
        /////////////////////////////////////////////
        int pos = str_rec.indexOf(BILANCIA_OMB_CHAR_END);

        if (pos > 0) {
            try {

                int len = Integer.parseInt(str_rec.substring(pos + 1, pos + 3));
                int len_pacchetto = str_rec.substring(0, pos).length();

                if (len == len_pacchetto) {

                    if (Integer.parseInt(str_rec.substring(pos - MICRO_CHECK_SUM_LEN, pos))
                            == (str_sent.length() - (MICRO_CHECK_SUM_LEN + 1))) {

                        //PACCHETTO INVIATO E RICEVUTO CORRETTAMENTE
                        result = str_rec.substring(0, pos - (MICRO_CHECK_SUM_LEN + 1));

                        //Memorizzazione log
                        BilanciaOMBLogger.logger.log(Level.CONFIG, "{0}   Pacchetto Inviato e ricevuto con successo", IpAddress);

                    }

                }

            } catch (NumberFormatException e) {

                //Memorizzazione log
                BilanciaOMBLogger.logger.log(Level.SEVERE, "{2}   {0} Errore durante la conversione - e:{1}",
                        new Object[]{
                            BilanciaSacchiValvolaAperta.class.getName(), e, IpAddress});
            }

        }

        return result;

    }

    //CREAZIONE STRINGA DA INVIARE
    private String creaStringa(String str_info, char p_type) {

        String res = p_type + str_info;

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   str_info = {0}", new Object[]{str_info, IpAddress});

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   p_type = {0}", new Object[]{p_type, IpAddress});

        String res_len = Integer.toString(res.length());
        //Verifa lunghezza CheckSum
        while (res_len.length() < MICRO_CHECK_SUM_LEN) {
            res_len = "0" + res_len;

        }
        res = res + BILANCIA_OMB_CHAR_END + res_len;

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   res = {0}", new Object[]{res, IpAddress});

        return res;
    }

    //LETTURA PESO MICRODOSATORE - IL PRIMO CARATTERE TRASMESSO E' LO STATO DEL MICRODOSATORE
    public String leggiPeso() {

        String result = "";

        String rep = invioStringa(creaStringa("", BILANCIA_OMB_CHAR_PTYPE_LETTURAPESO));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, IpAddress});

        if (rep.length() > 0 && rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_LETTURAPESO) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}    Stringa ricevuta = {0}", new Object[]{result, IpAddress});

        return result;
    }

    //AVVIO PROCEDURA DI PESATURA BILANCIA SACCHI A VALVOLA APERTA
    public String avviaPesatura() {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "{0} avviaPesatura()", IpAddress);

        String result = "";

        String rep = invioStringa(creaStringa("", MICRO_CHAR_PTYPE_AVVIOPESATURA));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}    Stringa inviata = {0}", new Object[]{rep, IpAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_LETTURAPESO) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}    Stringa ricevuta = {0}", new Object[]{result, IpAddress});

        return result;
    }
    
    

    //AVVIO PROCEDURA DI PESATURA BILANCIA SACCHI A VALVOLA APERTA (PROCEDURA PER SECCHI)
    public boolean avviaProceduraPulsanteDueMani() {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "{0} avviaProceduraPulsanteDueMani()", IpAddress);

        boolean result = false;
        String rep = invioStringa(creaStringa("", BILANCIA_OMB_CHAR_PTYPE_AVVIO_SECCHI));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, IpAddress});

        if (rep.length() > 0) {
            result = (rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_AVVIO_SECCHI);
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, IpAddress});

        return result;

    } 
            
            
    //VERIFICA DISPOSITIVO
    public String verificaDispositivo() {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "{0} verificaDispositivo()", IpAddress);

        String result = "";

        String rep = invioStringa(creaStringa("", BILANCIA_OMB_CHAR_PTYPE_VERIFICA));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, IpAddress});

        if (rep.length() > 0 && rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_VERIFICA) {
            int pos = rep.indexOf(BILANCIA_OMB_CHAR_SEP);
            result = rep.substring(1, pos);
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, IpAddress});

        return result;

    }

    //INIZIALIZZAZIONE DISPOSITIVO
    public boolean inizializzaDispositivo() {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "{0} inizializzaDispositivo()", IpAddress);

        boolean result = false;
        String rep = invioStringa(creaStringa("", BILANCIA_OMB_CHAR_PTYPE_INIT));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, IpAddress});

        if (rep.length() > 0) {
            result = (rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_INIT);
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, IpAddress});

        return result;

    }

    //CONFIGURAZIONE BILANCIA SACCHI A VALVOLA APERTA
    public boolean configuraBilancia(Integer quantitàRichiesta, Integer qStopCoclee, String curvaVelocita) {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "configuraBilanciaOMB()   pesoDesiderato = {0} qStopCoclee = {1} curvaVelocita = {2}",
                new Object[]{quantitàRichiesta, qStopCoclee, curvaVelocita});

        boolean result = false;

        try {

            //Verifica Disposito 
            String peso = verificaDispositivo();

            //Separazione Stringa Vel e Strina Peso
            int pos = curvaVelocita.indexOf(BILANCIA_OMB_CHAR_SEP_CURVA_VEL1);
            String curva_vel = curvaVelocita.substring(0, pos - 1);
            String curva_peso = curvaVelocita.substring(pos + 1, curvaVelocita.length() - 1);

            if (peso.length() > 0) {

                //Memorizzazione log
                BilanciaOMBLogger.logger.log(Level.INFO, "configuraBilancia()   Verifica Disposito ........OK - peso = {0}", peso);

                //////////////////////////////////////
                // PREPARAZIONE PACCHETTO CONFIG 0 ///
                //////////////////////////////////////  
                String rec = invioStringa(creaStringa(Integer.toString(quantitàRichiesta)
                        + BILANCIA_OMB_CHAR_SEP
                        + qStopCoclee
                        + BILANCIA_OMB_CHAR_SEP, BILANCIA_OMB_CHAR_PTYPE_CONFIG0));

                //VALIDAZIONE LETTURA PACCHETTO CONFIG 0 
                if (rec.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_CONFIG0) {

                    rec = rec.substring(1, rec.length());

////                    if (Integer.parseInt(Character.toString(rec.charAt(0))) == Integer.toString(pesoObiettivo).length()
////                            && Integer.parseInt(Character.toString(rec.charAt(1))) == Integer.toString(qStopCoclee).length()
////                            && Integer.parseInt(Character.toString(rec.charAt(2))) == Integer.toString(qStopMescola).length()) {
                    if (Integer.parseInt(Character.toString(rec.charAt(0))) == Integer.toString(quantitàRichiesta).length()
                            && Integer.parseInt(Character.toString(rec.charAt(1))) == Integer.toString(qStopCoclee).length()) {

                        //Memorizzazione log
                        BilanciaOMBLogger.logger.log(Level.INFO, "configuraBilancia()    Invio Pacchetto 0 ........OK");

                        //PREPARAZIONE PACCHETTO CONFIG 1
                        String temp = "";
                        ArrayList<String> vel = new ArrayList<>();
                        ArrayList<Boolean> check_invio = new ArrayList<>();
                        for (int i = 0; i < curva_vel.length(); i++) {
                            if (curva_vel.charAt(i) == BILANCIA_OMB_CHAR_SEP_CURVA_VEL2) {
                                vel.add(temp);
                                temp = "";
                            } else {

                                temp = temp + curva_vel.charAt(i);
                            }
                        }
                        if (!"".equals(temp)) {
                            vel.add(temp);
                        }
                        for (int i = 0; i < vel.size(); i++) {
                            rec = invioStringa(creaStringa(vel.get(i), BILANCIA_OMB_CHAR_PTYPE_CONFIG1));

                            check_invio.add(!(rec.charAt(0) != BILANCIA_OMB_CHAR_PTYPE_CONFIG1
                                    | Integer.parseInt(rec.substring(1, rec.length())) != i));
                        }

                        //VALIDAZIONE LETTURA PACCHETTO CONFIG 1 
                        boolean invio_eseguito = true;

                        for (int i = 0; i < check_invio.size(); i++) {

                            if (!check_invio.get(i)) {
                                invio_eseguito = false;
                            }
                        }

                        if (invio_eseguito) {

                            //Memorizzazione log
                            BilanciaOMBLogger.logger.log(Level.INFO, "configuraBilancia()   Invio Pacchetto 1 ........OK");

                            //PREPARAZIONE PACCHETTO CONFIG 2
                            temp = "";
                            ArrayList<String> pesi = new ArrayList<>();
                            check_invio = new ArrayList<>();
                            for (int i = 0; i < curva_peso.length(); i++) {
                                if (curva_peso.charAt(i) == BILANCIA_OMB_CHAR_SEP_CURVA_VEL2) {
                                    pesi.add(temp);
                                    temp = "";
                                } else {
                                    temp = temp + curva_peso.charAt(i);
                                }
                            }
                            if (!"".equals(temp)) {
                                pesi.add(temp);
                            }
                            for (int i = 0; i < pesi.size(); i++) {
                                rec = invioStringa(creaStringa(pesi.get(i), BILANCIA_OMB_CHAR_PTYPE_CONFIG2));

                                check_invio.add(!(rec.charAt(0) != BILANCIA_OMB_CHAR_PTYPE_CONFIG2
                                        | Integer.parseInt(rec.substring(1, rec.length())) != i));
                            }

                            //VALIDAZIONE LETTURA PACCHETTO CONFIG 1
                            invio_eseguito = true;

                            for (int i = 0; i < check_invio.size(); i++) {

                                if (!check_invio.get(i)) {
                                    invio_eseguito = false;
                                }
                            }

                            if (invio_eseguito) {
                                //Memorizzazione log
                                BilanciaOMBLogger.logger.log(Level.INFO, "configuraMicro()   Invio Pacchetto 2 ........OK");
                                result = true;

                            } else {

                                //Memorizzazione log
                                BilanciaOMBLogger.logger.log(Level.SEVERE, "Invio Pacchetto 2 ........FALLITO");
                            }

                        } else {

                            //Memorizzazione log
                            BilanciaOMBLogger.logger.log(Level.SEVERE, "Invio Pacchetto 1 ........FALLITO");

                        }

                    }

                } else {
                    //Memorizzazione log
                    BilanciaOMBLogger.logger.log(Level.SEVERE, "Invio Pacchetto 0 ........FALLITO");
                }
            } else {
                //Memorizzazione log
                BilanciaOMBLogger.logger.log(Level.SEVERE, "Verifica Disposito ........FALLITO");

            }

        } catch (NumberFormatException e) {

        }

        return result;
    }

////    //ATTIVA COPERCHIO MICRO
////    public String attivaCoperchio(boolean value) {
////        
////        //Memorizzazione log
////        BilanciaOMBLogger.logger.log(Level.INFO, "{1}   attivaCoperchio()   valore = {0}", new Object[]{value, IpAddress});
////
////        String result = "";
////        String value_to_send;
////
////        if (value) {
////            value_to_send = Character.toString(FabCloudConstants.MICRO_CHAR_CONF_TRUE);
////
////        } else {
////            value_to_send = Character.toString(FabCloudConstants.MICRO_CHAR_CONF_FALSE);
////        }
////
////        String rep = invioStringa(creaStringa(value_to_send, FabCloudConstants.MICRO_CHAR_PTYPE_COPERCHIO));
////
////        //Memorizzazione log
////        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, IpAddress});
////
////        if (rep.length() > 0 && rep.charAt(0) == FabCloudConstants.MICRO_CHAR_PTYPE_COPERCHIO) {
////
////            result = rep.substring(1, rep.length());
////        }
////
////        //Memorizzazione log
////        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, IpAddress});
////
////        return result;
////
////    }
////
////    //ATTIVA MESCOLA MICRO
////    public String attivaMescola(boolean value) {
////         
////        //Memorizzazione log
////        BilanciaOMBLogger.logger.log(Level.INFO, "{1}   attivaMescola()   valore = {0}", new Object[]{value, IpAddress});
////
////        String result = "";
////        String value_to_send;
////
////        if (value) {
////            value_to_send = Character.toString(FabCloudConstants.MICRO_CHAR_CONF_TRUE);
////
////        } else {
////            value_to_send = Character.toString(FabCloudConstants.MICRO_CHAR_CONF_FALSE);
////        }
////
////        String rep = invioStringa(creaStringa(value_to_send, FabCloudConstants.MICRO_CHAR_PTYPE_MESCOLA));
////
////        //Memorizzazione log
////        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, IpAddress});
////
////        if (rep.length() > 0 && rep.charAt(0) == FabCloudConstants.MICRO_CHAR_PTYPE_MESCOLA) {
////
////            result = rep.substring(1, rep.length());
////        }
////
////        //Memorizzazione log
////        BilanciaOMBLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, IpAddress});
////
////        return result;
////
////    }
    //ATTIVA MOTORE COCLEA - vel=0 COCLEE STOP / vel>0 cambio velocità e start inverter
    public String attivaMotoreCoclee(String vel) {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "attivaMotoreCoclea()   valore = {0}", vel);

        String result = "";

        String rep = invioStringa(creaStringa(vel, BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa inviata = {0}", rep);

        if (rep.length() > 0 && rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA) {

            result = rep.substring(1, rep.length());
        }
        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa ricevuta = {0}", result);

        return result;

    }

    //ATTIVA MOTORE COCLEA CON ROTAZIONE INVERSA
    public String attivaMotoreCocleeRotazioneInversa(String vel) {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "attivaMotoreCocleaRotazioneInversa()   valore = {0}", vel);
        String result = "";
        String rep = invioStringa(creaStringa(vel, BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA_ROTAZIONE_INVERSA));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa inviata = {0}", rep);

        if (rep.length() > 0 && rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA_ROTAZIONE_INVERSA) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa ricevuta = {0}", result);

        return result;

    }

    //Cambio velocità
    public String cambioVel(String value) {

        String result = "";

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "cambioVel()   valore = {0}", value);

        String rep = invioStringa(creaStringa(value, BILANCIA_OMB_CHAR_PTYPE_CAMBIO_VEL));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa inviata = {0}", rep);

        if (rep.length() > 0 && rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_CAMBIO_VEL) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa ricevuta = {0}", result);

        return result;

    }

    //ABILITA BLOCCO SACCO true = abiliato - false = disabilitato/sblocca
    public String abilitazioneBloccoSacco(boolean value) {

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.INFO, "abilitazione blocco sacco()   valore = {0}", value);

        String result = "";

        char str_to_send;

        if (value) {
            str_to_send = BILANCIA_OMB_CHAR_CONF_TRUE;
        } else {
            str_to_send = BILANCIA_OMB_CHAR_CONF_FALSE;
        }
        String rep = invioStringa(creaStringa(Character.toString(str_to_send), BILANCIA_OMB_CHAR_PTYPE_BLOCCA_SACCO));

        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa inviata = {0}", rep);

        if (rep.length() > 0 && rep.charAt(0) == BILANCIA_OMB_CHAR_PTYPE_BLOCCA_SACCO) {
 
            result = rep.substring(1, rep.length()); 
        }
        //Memorizzazione log
        BilanciaOMBLogger.logger.log(Level.CONFIG, "Stringa ricevuta = {0}", result);

        return result;

    }

    ///////////////////////////
    // GETTERS AND SETTERS  ///
    ///////////////////////////
    public String getStatoPesatura() {
        return statoPesatura;
    }

    public void setStatoPesatura(String statoPesatura) {
        this.statoPesatura = statoPesatura;
    }

}
