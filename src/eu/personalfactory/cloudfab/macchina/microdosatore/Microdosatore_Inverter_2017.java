/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.microdosatore;

import eu.personalfactory.cloudfab.macchina.loggers.MicroLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneHextoBinario;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_CONF_FALSE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_CONF_TRUE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_END;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_AGGIORNA_STATO_CONTATTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_AVVIOPESATURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_CAMBIO_VEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_CONFIG0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_CONFIG1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_CONFIG2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_INIT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_LETTURAPESO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_MESCOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_MOTORECOCLEE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_MOTORECOCLEE_ROTAZIONE_INVERSA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PTYPE_VERIFICA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_SEP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_SEP_CURVA_VEL1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_SEP_CURVA_VEL2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHECK_SUM_LEN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_DIM_PACK_UDP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_NUM_CHAR_INPUTS;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_NUM_RIP_INVIO_PACCHETTO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francescodigaudio
 */
public class Microdosatore_Inverter_2017 {

    private DatagramSocket udpSocket;
    private InetAddress microIPAddress;
    private int portaMicrodosatore;
    private int timeoutMicrodosatore;
    private byte[] receiveData;

    private String nomeComponente;

    private int quantitaRichiesta;
    private Integer idComponente;

    private Integer pesoIniziale;

    private String presaComponente;
    private Integer idComponenteInPeso;
    private String statoMicrodosatura;
    private Integer pesoDesiderato;
    private Integer pesoObiettivo;

    private Integer pesoCorrente;
    private boolean microdosatoreVuoto;
    private int tolleranzaInEccesso;
    private int tolleranzaInDifetto;
    private int idMovimentoMagazzino;
    private int pesoComponente;
    private boolean statoContatto;

    //COSTRUTTORE
    public Microdosatore_Inverter_2017(String indexMicro) {

        receiveData = new byte[MICRO_DIM_PACK_UDP];

        try {

            microIPAddress = InetAddress.getByName(ParametriSingolaMacchina.parametri.get(241) + Integer.toString(Integer.parseInt(ParametriSingolaMacchina.parametri.get(242)) + Integer.parseInt(indexMicro) - 1));

            portaMicrodosatore = Integer.parseInt(ParametriSingolaMacchina.parametri.get(243));

            udpSocket = new DatagramSocket();

            udpSocket.setSoTimeout(Integer.parseInt(ParametriSingolaMacchina.parametri.get(244)));

            //Memorrizzazione Log Sessione
            MicroLogger.logger.log(Level.INFO, "{3}   Indirizzo Microdosatore ={0} - Porta ={1} - Timeout ={2}", new Object[]{microIPAddress, portaMicrodosatore, timeoutMicrodosatore, microIPAddress});

            //Inizializzazione Variabili
            pesoComponente = 0;

            //Memorrizzazione Log Sessione
            MicroLogger.logger.log(Level.INFO, "{0}   Creazione Datagram Socket Microdosatore - Eseguita", microIPAddress);

        } catch (UnknownHostException ex) {

            //Memorrizzazione Log Sessione
            MicroLogger.logger.log(Level.SEVERE, "{1}   Errore Durante la Creazione del Datagram Socket Microdosatore - Socket Exception{0}", new Object[]{ex, microIPAddress});
        } catch (SocketException ex) {
            Logger.getLogger(Microdosatore_Inverter_2017.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Memorizzazione log

        //Memorizzazione log
    }

    //COMUNICAZIONE MICRODOSATORE private!!
    private String invioStringa(String str_to_send) {

        boolean interrompiInvio = false;
        int i = 0;
        String stringa_ricevuta_valida = "";
        //Inizio loop
        while (!interrompiInvio) {

            String stringa_ver = "";
            try {
                // Invio Stringa
                udpSocket.send(new DatagramPacket((str_to_send).getBytes(), (str_to_send).getBytes().length, microIPAddress, portaMicrodosatore));

                receiveData = new byte[MICRO_DIM_PACK_UDP];

                //Ricezione Stringa
                udpSocket.receive(new DatagramPacket(receiveData, receiveData.length));

                String stringa_ricevuta = new String(receiveData);

                //Memorizzazione log
                MicroLogger.logger.log(Level.CONFIG, "{1}   Dal Server:{0}", new Object[]{stringa_ricevuta, microIPAddress});

                stringa_ver = verifica_stringa(stringa_ricevuta, str_to_send);

            } catch (Exception ex) {

                //Memorizzazione log
                MicroLogger.logger.log(Level.SEVERE, "{1}   dal Microdosatore:{0}", new Object[]{ex, microIPAddress});
            }

            //Verifica Stringa  
            if (stringa_ver.length() > 0) {

                ///////////////////////////////////
                // INVIO ESEGUITO CON SUCCESSO  ///
                ///////////////////////////////////
                stringa_ricevuta_valida = stringa_ver;

                interrompiInvio = true;

                //Memorizzazione log
                MicroLogger.logger.log(Level.CONFIG, "{0}   Invio eseguito con successo", new Object[]{microIPAddress});

            } else if (i > MICRO_NUM_RIP_INVIO_PACCHETTO) {

                //////////////////////////////////////////
                // NUM_MAX RIPETIZIONI INVIO SUPERATE  ///
                //////////////////////////////////////////
                interrompiInvio = true;

                //Memorizzazione log
                MicroLogger.logger.log(Level.WARNING, "{1}   Invio Fallito dopo {0} tentativi", new Object[]{i, microIPAddress});

            } else {

                ////////////////////////////////////////
                // INVIO NON ESEGUITO CORRETTAMENTE  ///
                ////////////////////////////////////////
                //Memorizzazione log
                MicroLogger.logger.log(Level.SEVERE, "{0}   Ripetizione Invio Necessario", microIPAddress);

                i++;

            }

        }//Fine loop

        stringa_ricevuta_valida = leggi_stato_ingresso_contatto_sportello(stringa_ricevuta_valida);

        return stringa_ricevuta_valida;

    }

    //LEGGI STATO INGRESSO CONTATTO
    private String leggi_stato_ingresso_contatto_sportello(String stringa_ricevuta_valida) {

        if (stringa_ricevuta_valida.length() > 0) {

            //Lettura caratteri ingressi microdosatore
            String HEX_INPUT_STATE = stringa_ricevuta_valida.substring(0, MICRO_NUM_CHAR_INPUTS);

            //Eliminazione caratteri ingressi microdosatore
            stringa_ricevuta_valida = stringa_ricevuta_valida.substring(MICRO_NUM_CHAR_INPUTS, stringa_ricevuta_valida.length());

            //Conversione esadecimale binario
            String BIN_INPUT_STATE = ConversioneHextoBinario(HEX_INPUT_STATE, MICRO_NUM_CHAR_INPUTS,1);

            //Correzione lunghezza stringa
            while (BIN_INPUT_STATE.length() < MICRO_NUM_CHAR_INPUTS * 4) {

                BIN_INPUT_STATE = "0" + BIN_INPUT_STATE;

            }
            
            setStatoContatto(BIN_INPUT_STATE.charAt(BIN_INPUT_STATE.length()-1)==ParametriSingolaMacchina.parametri.get(457).charAt(0));

        }

        return stringa_ricevuta_valida;
    }

    //VERIFICA STRINGA RICEVUTA
    private String verifica_stringa(String str_rec, String str_sent) {
        String result = "";

        /////////////////////////////////////////////
        // VERIFICA INTEGRITA PACCHETTO RICEVUTO  ///
        /////////////////////////////////////////////
        int pos = str_rec.indexOf(MICRO_CHAR_END);

        if (pos > 0) {
            try {

                int len = Integer.parseInt(str_rec.substring(pos + 1, pos + 3));
                int len_pacchetto = str_rec.substring(0, pos).length();

                if (len == len_pacchetto) {

////                    if (Integer.parseInt(str_rec.substring(pos - FabCloudConstants.CHECK_SUM_LEN, pos))
////                            == (str_sent.length() - (FabCloudConstants.CHECK_SUM_LEN + 1))) {
                    //PACCHETTO INVIATO E RICEVUTO CORRETTAMENTE
                    result = str_rec.substring(0, pos - (MICRO_CHECK_SUM_LEN + 1));

                    //Memorizzazione log
                    MicroLogger.logger.log(Level.CONFIG, "{0}   Pacchetto Inviato e ricevuto con successo", microIPAddress);

////                    }
                }

            } catch (NumberFormatException e) {
                //Memorizzazione log
                MicroLogger.logger.log(Level.SEVERE, "{2}   {0} Errore durante la conversione - e:{1}",
                        new Object[]{
                            Microdosatore_Inverter_2017.class.getName(), e, microIPAddress});
            }

        }

        return result;

    }

    //CREAZIONE STRINGA DA INVIARE
    public String creaStringa(String str_info, char p_type) {

        String res = p_type + str_info;

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   str_info = {0}", new Object[]{str_info, microIPAddress});

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   p_type = {0}", new Object[]{p_type, microIPAddress});

        String res_len = Integer.toString(res.length());
        //Verifa lunghezza CheckSum
        while (res_len.length() < MICRO_CHECK_SUM_LEN) {
            res_len = "0" + res_len;

        }
        res = res + MICRO_CHAR_END + res_len;

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   res = {0}", new Object[]{res, microIPAddress});

        return res;
    }

    //LETTURA PESO MICRODOSATORE - IL PRIMO CARATTERE TRASMESSO E' LO STATO DEL MICRODOSATORE
    public String leggiPeso() {

        String result = "";

        String rep = invioStringa(creaStringa("", MICRO_CHAR_PTYPE_LETTURAPESO));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_LETTURAPESO) {

            result = rep.substring(1, rep.length());
        }
        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}    Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;
    }
    
    //LETTURA PESO MICRODOSATORE - IL PRIMO CARATTERE TRASMESSO E' LO STATO DEL MICRODOSATORE
    public String aggiornaStatoContatto() {

        String result = "";

        String rep = invioStringa(creaStringa("", MICRO_CHAR_PTYPE_AGGIORNA_STATO_CONTATTO));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_AGGIORNA_STATO_CONTATTO) {

            result = rep.substring(1, rep.length());
        }
        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}    Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;
    }

    //Avvio Procedura di Pesatura Microdosatore
    public String avviaPesatura() {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{0} avviaPesatura()", microIPAddress);

        String result = "";

        String rep = invioStringa(creaStringa("", MICRO_CHAR_PTYPE_AVVIOPESATURA));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}    Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_AVVIOPESATURA) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}    Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;
    }

    //Verifica Comunicazione Dispositivo
    public String verificaDispositivo() {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{0} verificaDispositivo()", microIPAddress);

        String result = "";

        String rep = invioStringa(creaStringa("", MICRO_CHAR_PTYPE_VERIFICA));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_VERIFICA) {
            int pos = rep.indexOf(MICRO_CHAR_SEP);

            result = rep.substring(1, pos);

        }

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;

    }

    //Inizializzazione Dispositivo
    public boolean inizializzaDispositivo() {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{0} inizializzaDispositivo()", microIPAddress);

        boolean result = false;
        String rep = invioStringa(creaStringa("", MICRO_CHAR_PTYPE_INIT));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0) {
            result = (rep.charAt(0) == MICRO_CHAR_PTYPE_INIT);
        }

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;

    }

    //Configurazione Microdosatore
    public boolean configuraMicro(Integer pesoDesiderato, Integer qStopCoclee, Integer qStopMescola, String curvaVelocita, Integer qStopVibro) {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{4}   configuraMicro()   pesoDesiderato = {0} qStopCoclee = {1} qStopMescola = {2} curvaVelocita = {3} qStopVibro = {5}",
                new Object[]{pesoDesiderato, qStopCoclee, qStopMescola, curvaVelocita, microIPAddress, qStopVibro});

        boolean result = false;

        this.pesoDesiderato = pesoDesiderato;

        try {

            //Verifica Disposito 
            String peso = verificaDispositivo();

            //Separazione Stringa Vel e Strina Peso
            int pos = curvaVelocita.indexOf(MICRO_CHAR_SEP_CURVA_VEL1);
            String curva_vel = curvaVelocita.substring(0, pos - 1);
            String curva_peso = curvaVelocita.substring(pos + 1, curvaVelocita.length() - 1);

            if (peso.length() > 0) {

                //Memorizzazione log
                MicroLogger.logger.log(Level.INFO, "{1}  configuraMicro() Verifica Disposito ........OK - peso = {0} ",
                        new Object[]{peso, microIPAddress});

                //////////////////////////////////////
                // PREPARAZIONE PACCHETTO CONFIG 0 ///
                //////////////////////////////////////
                pesoObiettivo = Integer.parseInt(peso) - pesoDesiderato;

                String rec = invioStringa(creaStringa(Integer.toString(pesoObiettivo)
                        + MICRO_CHAR_SEP
                        + qStopCoclee
                        + MICRO_CHAR_SEP
                        + qStopMescola
                        + MICRO_CHAR_SEP
                        + qStopVibro, MICRO_CHAR_PTYPE_CONFIG0));
                if (!rec.equals("")) {
                    //VALIDAZIONE LETTURA PACCHETTO CONFIG 0 
                    if (rec.charAt(0) == MICRO_CHAR_PTYPE_CONFIG0) {

                        rec = rec.substring(1, rec.length());

                        if (Integer.parseInt(Character.toString(rec.charAt(0))) == Integer.toString(pesoObiettivo).length()
                                && Integer.parseInt(Character.toString(rec.charAt(1))) == Integer.toString(qStopCoclee).length()
                                && Integer.parseInt(Character.toString(rec.charAt(2))) == Integer.toString(qStopMescola).length()) {

                            //Memorizzazione log
                            MicroLogger.logger.log(Level.INFO, "{0}   configuraMicro()    Invio Pacchetto 0 ........OK", microIPAddress);

                            //PREPARAZIONE PACCHETTO CONFIG 1
                            String temp = "";
                            ArrayList<String> vel = new ArrayList<>();
                            ArrayList<Boolean> check_invio = new ArrayList<>();
                            for (int i = 0; i < curva_vel.length(); i++) {

                                if (curva_vel.charAt(i) == MICRO_CHAR_SEP_CURVA_VEL2) {

                                    vel.add(temp.substring(0, temp.length() - 2));
                                    temp = "";

                                } else {

                                    temp = temp + curva_vel.charAt(i);
                                }
                            }
                            if (!"".equals(temp)) {
                                vel.add(temp.substring(0, temp.length() - 2));
                            }

                            for (int i = 0; i < vel.size(); i++) {

                                rec = invioStringa(creaStringa(vel.get(i), MICRO_CHAR_PTYPE_CONFIG1));

                                if (!rec.equals("")) {
                                    check_invio.add(!(rec.charAt(0) != MICRO_CHAR_PTYPE_CONFIG1
                                            | Integer.parseInt(rec.substring(1, rec.length())) != i));
                                }
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
                                MicroLogger.logger.log(Level.INFO, "{0}  configuraMicro()   Invio Pacchetto 1 ........OK", microIPAddress);

                                //PREPARAZIONE PACCHETTO CONFIG 2
                                temp = "";
                                ArrayList<String> pesi = new ArrayList<>();
                                check_invio = new ArrayList<>();
                                for (int i = 0; i < curva_peso.length(); i++) {
                                    if (curva_peso.charAt(i) == MICRO_CHAR_SEP_CURVA_VEL2) {
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
                                    rec = invioStringa(creaStringa(pesi.get(i), MICRO_CHAR_PTYPE_CONFIG2));

                                    if (!rec.equals("")) {
                                        check_invio.add(!(rec.charAt(0) != MICRO_CHAR_PTYPE_CONFIG2
                                                | Integer.parseInt(rec.substring(1, rec.length())) != i));
                                    }
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
                                    MicroLogger.logger.log(Level.INFO, "{0}   configuraMicro()   Invio Pacchetto 2 ........OK", microIPAddress);
                                    result = true;

                                } else {

                                    //Memorizzazione log
                                    MicroLogger.logger.log(Level.SEVERE, "{0}    Invio Pacchetto 2 ........FALLITO", microIPAddress);
                                }

                            } else {

                                //Memorizzazione log
                                MicroLogger.logger.log(Level.SEVERE, "{0}   Invio Pacchetto 1 ........FALLITO", microIPAddress);

                            }

                        }

                    } else {
                        //Memorizzazione log
                        MicroLogger.logger.log(Level.SEVERE, "{0}   Invio Pacchetto 0 ........FALLITO", microIPAddress);
                    }
                }
            } else {
                //Memorizzazione log
                MicroLogger.logger.log(Level.SEVERE, "{0}   Verifica Disposito ........FALLITO", microIPAddress);

            }

        } catch (NumberFormatException e) {

        }

        return result;
    }

////    //ATTIVA COPERCHIO MICRO
////    public String attivaCoperchio(boolean value) {
////        
////        //Memorizzazione log
////        MicroLogger.logger.log(Level.INFO, "{1}   attivaCoperchio()   valore = {0}", new Object[]{value, microIPAddress});
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
////        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});
////
////        if (rep.length() > 0 && rep.charAt(0) == FabCloudConstants.MICRO_CHAR_PTYPE_COPERCHIO) {
////
////            result = rep.substring(1, rep.length());
////        }
////
////        //Memorizzazione log
////        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});
////
////        return result;
////
////    }
    //ATTIVA MESCOLA MICRO
    public String attivaMescola(boolean value) {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{1}   attivaMescola()   valore = {0}", new Object[]{value, microIPAddress});

        String result = "";
        String value_to_send;

        if (value) {
            value_to_send = Character.toString(MICRO_CHAR_CONF_TRUE);

        } else {
            value_to_send = Character.toString(MICRO_CHAR_CONF_FALSE);
        }

        String rep = invioStringa(creaStringa(value_to_send, MICRO_CHAR_PTYPE_MESCOLA));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_MESCOLA) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;

    }

    //ATTIVA MOTORE COCLEE - vel=0 COCLEE STOP / vel>0 cambio velocità e start inverter
    public String attivaMotoreCoclee(String vel) {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{1}   attivaMotoreCoclee()   valore = {0}", new Object[]{vel, microIPAddress});

        String result = "";

        String rep = invioStringa(creaStringa(vel, MICRO_CHAR_PTYPE_MOTORECOCLEE));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_MOTORECOCLEE) {

            result = rep.substring(1, rep.length());
        }
        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;

    }

    //ATTIVA MOTORE COCLEE CON ROTAZIONE INVERSA
    public String attivaMotoreCocleeRotazioneInversa(String vel) {

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{1}   attivaMotoreCocleeRotazioneInversa()   valore = {0}", new Object[]{vel, microIPAddress});
        String result = "";
        String rep = invioStringa(creaStringa(vel, MICRO_CHAR_PTYPE_MOTORECOCLEE_ROTAZIONE_INVERSA));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_MOTORECOCLEE_ROTAZIONE_INVERSA) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;

    }

    //Cambio velocità
    public String cambioVel(String value) {

        String result = "";

        //Memorizzazione log
        MicroLogger.logger.log(Level.INFO, "{1}   cambioVel()   valore = {0}", new Object[]{value, microIPAddress});

        String rep = invioStringa(creaStringa(value, MICRO_CHAR_PTYPE_CAMBIO_VEL));

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa inviata = {0}", new Object[]{rep, microIPAddress});

        if (rep.length() > 0 && rep.charAt(0) == MICRO_CHAR_PTYPE_CAMBIO_VEL) {

            result = rep.substring(1, rep.length());
        }

        //Memorizzazione log
        MicroLogger.logger.log(Level.CONFIG, "{1}   Stringa ricevuta = {0}", new Object[]{result, microIPAddress});

        return result;

    }

////    public void avviaThreadControlloMicrodosatoreVuoto(MyStepPanel pannelloCorrente) {
////
////        new threadControlloMicrodosatoreVuoto(pannelloCorrente).start();
////
////    }
////    private void sleep(int i) {
////        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
////    }
//////    //Thread Controllo Coclea Vuota
//////    private class threadControlloMicrodosatoreVuoto extends Thread {
//////
//////        MyStepPanel pannelloCorrente;
//////
//////        public threadControlloMicrodosatoreVuoto(MyStepPanel pannelloCorrente) {
//////            this.pannelloCorrente = pannelloCorrente;
//////        }
//////
//////        @Override
//////        public void run() {
//////
//////            interrompiThreadControllaMicrodosatoreVuoto = false;
//////            int counter_micro_vuoto = 0;
//////            microdosatoreVuoto = false;
//////
//////            while (!interrompiThreadControllaMicrodosatoreVuoto && pannelloCorrente.isVisible()) {
//////
//////                if ((pesoCorrenteMicrodosatorePrec - pesoCorrenteMicrodosatore) > FabCloudConstants.MICRO_DIFFERENZIALE_MICRO_VUOTO) {
//////                    counter_micro_vuoto = 0;
//////
//////                } else {
//////
//////                    counter_micro_vuoto++;
//////                }
//////
//////                interrompiThreadControllaMicrodosatoreVuoto = (counter_micro_vuoto > FabCloudConstants.MICRO_NUMERO_RIP_MICRO_VUOTO);
//////                microdosatoreVuoto = true;
//////
//////                try {
//////                    threadControlloMicrodosatoreVuoto.sleep(1000);
//////                } catch (InterruptedException ex) {
//////                    Logger.getLogger(Microdosatore_Inverter_2017.class.getName()).log(Level.SEVERE, null, ex);
//////                }
//////
//////            } //fine loop
//////
//////            inizializzaDispositivo();
//////
//////        }
//////
//////    }
    //Restituisce l'indirizzo del microdosatore
    public InetAddress getMicroIPAddress() {
        return microIPAddress;
    }

    public String getNomeComponente() {
        return nomeComponente;
    }

    public void setNomeComponente(String nomeComponente) {
        this.nomeComponente = nomeComponente;
    }

    public int getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public String getPresaComponente() {
        return presaComponente;
    }

    public void setPresaComponente(String presaComponente) {
        this.presaComponente = presaComponente;
    }

    public Integer getIdComponenteInPeso() {
        return idComponenteInPeso;
    }

    public void setIdComponenteInPeso(Integer idComponenteInPeso) {
        this.idComponenteInPeso = idComponenteInPeso;
    }

    public void setQuantitaRichiesta(int quantitaRichiesta) {
        this.quantitaRichiesta = quantitaRichiesta;
    }

    public String getStatoMicrodosatura() {
        return statoMicrodosatura;
    }

    public void setStatoMicrodosatura(String statoMicrodosatura) {
        this.statoMicrodosatura = statoMicrodosatura;
    }

////    public int getPesoCorrenteMicrodosatore() {
////        return pesoCorrenteMicrodosatore;
////    }
////
////    public void setPesoCorrenteMicrodosatore(int pesoCorrenteMicrodosate) {
////        this.pesoCorrenteMicrodosatore = pesoCorrenteMicrodosate;
////    }
    public boolean isMicrodosatoreVuoto() {
        return microdosatoreVuoto;
    }

    public void setMicrodosatoreVuoto(boolean microdosatoreVuoto) {
        this.microdosatoreVuoto = microdosatoreVuoto;
    }

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    public Integer getPesoDesiderato() {
        return pesoDesiderato;
    }

    public void setPesoDesiderato(Integer pesoDesiderato) {
        this.pesoDesiderato = pesoDesiderato;
    }

    public Integer getPesoObiettivo() {
        return pesoObiettivo;
    }

    public void setPesoObiettivo(Integer pesoObiettivo) {
        this.pesoObiettivo = pesoObiettivo;
    }

    public int getTolleranzaInEccesso() {
        return tolleranzaInEccesso;
    }

    public void setTolleranzaInEccesso(int tolleranzaInEccesso) {
        this.tolleranzaInEccesso = tolleranzaInEccesso;
    }

    public int getTolleranzaInDifetto() {
        return tolleranzaInDifetto;
    }

    public void setTolleranzaInDifetto(int tolleranzaInDifetto) {
        this.tolleranzaInDifetto = tolleranzaInDifetto;
    }

    public int getIdMovimentoMagazzino() {
        return idMovimentoMagazzino;
    }

    public void setIdMovimentoMagazzino(int idMovimentoMagazzino) {
        this.idMovimentoMagazzino = idMovimentoMagazzino;
    }

    public Integer getPesoIniziale() {
        return pesoIniziale;
    }

    public void setPesoIniziale(Integer pesoIniziale) {
        this.pesoIniziale = pesoIniziale;
    }

    public Integer getPesoCorrente() {
        return pesoCorrente;
    }

    public void setPesoCorrente(Integer pesoCorrente) {
        this.pesoCorrente = pesoCorrente;
    }

    public int getPesoComponente() {
        return pesoComponente;
    }

    public void setPesoComponente(int pesoComponente) {
        this.pesoComponente = pesoComponente;
    }

    public boolean isStatoContatto() {
        return statoContatto;
    }

    public void setStatoContatto(boolean statoContatto) {
        this.statoContatto = statoContatto;
    }
}
