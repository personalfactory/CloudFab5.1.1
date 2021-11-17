/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.inv;

import eu.personalfactory.cloudfab.macchina.loggers.InverterLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneStringToHex;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class Inverter_Gefran_old {

    public final String address;
    public final String device;
    public final String name; 

    ///////////////////////////////
    // PARAMETERI GEFRAN ADV50  ///
    ///////////////////////////////
    private final String PAR_AVVIO_ARRESTO_ADV50 = "3200";
    private final String VALUE_AVVIO_ADV50 = "0002";
    private final String VALUE_ARRESTO_ADV50 = "0001";
    private final String PAR_CAMBIO_VEL_ARRESTO_ADV50 = "0212";
    private final String CAR_INIZIO_STRINGA_ADV50 = ":";
    private final String CAR_CR_ADV50 = "\\r";
    private final String CAR_LF_ADV50 = "\\n";
    private final String PAR_READ_ADV50 = "03";
    private final String PAR_WRITE_ADV50 = "06";
    private final String PAR_WRITE_MULTI_ADV50 = "10";
    private final String PAR_ROT_INVERSA_ADV50 = "0002";

    ///////////////////////////////
    // PARAMETERI GEFRAN BDI50  ///
    ///////////////////////////////
    private final String PAR_AVVIO_ARRESTO_BDI50 = "3701";
    private final String VALUE_AVVIO_BDI50 = "0001";
    private final String VALUE_ARRESTO_BDI50 = "0000";
    private final String PAR_CAMBIO_VEL_ARRESTO_BDI50 = "3702";
    private final String CAR_INIZIO_STRINGA_BDI50 = ":";
    private final String CAR_CR_BDI50 = "\\r";
    private final String CAR_LF_BDI50 = "\\n";
    private final String PAR_READ_BDI50 = "03";
    private final String PAR_WRITE_BDI50 = "06";
    private final String PAR_WRITE_MULTI_BDI50 = "10";
    private final String PAR_ROT_INVERSA_BDI50 = "0034";

    ArrayList<String> lista_par_avvio_arresto = new ArrayList<>();
    ArrayList<String> lista_value_avvio = new ArrayList<>();
    ArrayList<String> lista_value_arresto = new ArrayList<>();
    ArrayList<String> lista_par_cambio_vel = new ArrayList<>();
    ArrayList<String> lista_car_inizio_stringa = new ArrayList<>();
    ArrayList<String> lista_car_cr = new ArrayList<>();
    ArrayList<String> lista_car_lf = new ArrayList<>();
    ArrayList<String> lista_par_read = new ArrayList<>();
    ArrayList<String> lista_par_write = new ArrayList<>();
    ArrayList<String> lista_par_write_multi = new ArrayList<>();
    ArrayList<String> lista_par_rot_inversa = new ArrayList<>();

    private final String par_avvio_arresto, value_avvio, value_arresto,
            par_cambio_vel, car_inizio_stringa, car_cr, car_lf, par_read,
            par_write, par_write_multi, par_rotazione_inversa;

    //Costruttore
    public Inverter_Gefran_old(String name, String address, String device, String type) {
        this.address = address;
        this.device = device;
        this.name = name; 

        lista_par_avvio_arresto.add(PAR_AVVIO_ARRESTO_ADV50);
        lista_par_avvio_arresto.add(PAR_AVVIO_ARRESTO_BDI50);

        lista_value_avvio.add(VALUE_AVVIO_ADV50);
        lista_value_avvio.add(VALUE_AVVIO_BDI50);

        lista_value_arresto.add(VALUE_ARRESTO_ADV50);
        lista_value_arresto.add(VALUE_ARRESTO_BDI50);

        lista_par_cambio_vel.add(PAR_CAMBIO_VEL_ARRESTO_ADV50);
        lista_par_cambio_vel.add(PAR_CAMBIO_VEL_ARRESTO_BDI50);

        lista_car_inizio_stringa.add(CAR_INIZIO_STRINGA_ADV50);
        lista_car_inizio_stringa.add(CAR_INIZIO_STRINGA_BDI50);

        lista_car_cr.add(CAR_CR_ADV50);
        lista_car_cr.add(CAR_CR_BDI50);

        lista_car_lf.add(CAR_LF_ADV50);
        lista_car_lf.add(CAR_LF_BDI50);

        lista_par_read.add(PAR_READ_ADV50);
        lista_par_read.add(PAR_READ_BDI50);

        lista_par_write.add(PAR_WRITE_ADV50);
        lista_par_write.add(PAR_WRITE_BDI50);

        lista_par_write_multi.add(PAR_WRITE_MULTI_ADV50);
        lista_par_write_multi.add(PAR_WRITE_MULTI_BDI50);

        lista_par_rot_inversa.add(PAR_ROT_INVERSA_ADV50);
        lista_par_rot_inversa.add(PAR_ROT_INVERSA_BDI50);

        par_avvio_arresto = lista_par_avvio_arresto.get(Integer.parseInt(type));
        value_avvio = lista_value_avvio.get(Integer.parseInt(type));
        value_arresto = lista_value_arresto.get(Integer.parseInt(type));
        par_cambio_vel = lista_par_cambio_vel.get(Integer.parseInt(type));
        car_inizio_stringa = lista_car_inizio_stringa.get(Integer.parseInt(type));
        car_cr = lista_car_cr.get(Integer.parseInt(type));
        car_lf = lista_car_lf.get(Integer.parseInt(type));
        par_read = lista_par_read.get(Integer.parseInt(type));
        par_write = lista_par_write.get(Integer.parseInt(type));
        par_write_multi = lista_par_write_multi.get(Integer.parseInt(type));
        par_rotazione_inversa = lista_par_rot_inversa.get(Integer.parseInt(type));
  
        InverterLogger.logger.log(Level.INFO, "Inverter = {0}"
                + "\nIndirizzo = {1}"
                + "\nPeriferica di Comando = {2}",
                new Object[]{name, address, device});

        InverterLogger.logger.log(Level.INFO, "Parametri Avvio = {0}"
                + "\nValore Avvio = {1}"
                + "\nValore Arresto = {2}"
                + "\nParametro Cambio Velocit"
                + "\u00e0 = {3}"
                + "\nCaratter Inizio Stringa = {4}"
                + "\nCarattere CR = {5}"
                + "\nCarattere LF = {6}"
                + "\nParametro Lettura = {7}"
                + "\nParametro Scrittura = {8}"
                + "\nParametro Scrittura Multipla = {9}",
                new Object[]{par_avvio_arresto, value_avvio, value_arresto,
                    par_cambio_vel, car_inizio_stringa, car_cr, car_lf,
                    par_read, par_write, par_write_multi});

    }

    //Calcola Longitudinal Redundancy Check
    public String calcolaLRC(String par, String value, String carReadWrite) {

        ///////////////////////////////////////
        // CALCOLO LRC, MODIFICA Marzo2016  ///
        ///////////////////////////////////////
        //Conversione Stringhe Parametri in Esadecimale
        String indInverter_HEX = ConversioneStringToHex(address, 2);
        String carReadWrite_HEX = ConversioneStringToHex(carReadWrite, 2);
        String parA_HEX = ConversioneStringToHex(par.substring(0, 2), 2);
        String parB_HEX = ConversioneStringToHex(par.substring(2, par.length()), 2);
        String value_HEX = ConversioneStringToHex(value, 4);

        //Conversione Stringhe Parametri in Decimale
        int indInverter_DEC = Integer.parseInt(address);
        int carReadWrite_DEC = Integer.parseInt(carReadWrite);
        int parA_DEC = Integer.parseInt(par.substring(0, 2));
        int parB_DEC = Integer.parseInt(par.substring(2, par.length()));
        int value_DEC = Integer.parseInt(value);

        //Fattore Correttivo per il Calcolo del LRC
        int correttivo = 0;

        for (int i = 0; i < 256; i++) {
            if (value_DEC > (65535 - 256 * i)) {
                correttivo = 65280 - 255 * i;
                break;
            } else if (value_DEC > (65251 - 255 * i)) {
                correttivo = 65281 - 255 * i;
                break;
            }
        }

        //Calcolo Somma Modulo 256;
        int sommaModulo256 = (int) (indInverter_DEC + carReadWrite_DEC + parA_DEC + parB_DEC + value_DEC - correttivo) % 256;
        //Calcolo Complemento a 2 negativo della somma
        int complemento2NegativoSomma = 256 - sommaModulo256;
        //Calcolo LRC
        String LRC = ConversioneStringToHex(Integer.toString(complemento2NegativoSomma), 2);

        InverterLogger.logger.log(Level.CONFIG, "DECIMALI : indInverter_DEC ={0}"
                + " - carWrite_DEC ={1} - parA_DEC ={2} - parB_DEC ={3} - value_DEC ={4}",
                new Object[]{indInverter_DEC, carReadWrite_DEC, parA_DEC, parB_DEC, value_DEC});

        InverterLogger.logger.log(Level.CONFIG, "ESADECIMALI: indInverter_HEX ={0} "
                + "- carWrite_HEX ={1} - parA_HEX ={2} - parB_HEX ={3} - value_HEX ={4}",
                new Object[]{indInverter_HEX, carReadWrite_HEX, parA_HEX, parB_HEX, value_HEX});

        InverterLogger.logger.log(Level.CONFIG, "CALCOLO LRC: SommaModulo256 ={0}"
                + " - Complemento2NegativoSomma ={1} - LRC.toUpperCase() ={2}",
                new Object[]{sommaModulo256, complemento2NegativoSomma, LRC.toUpperCase()});

        return indInverter_HEX + carReadWrite_HEX + parA_HEX + parB_HEX + value_HEX + LRC.toUpperCase();

    }

    //Determina la Stringa da Trasmettere Secondo la Codifica Gefran
    public String codificaStringa(String par, String value, String car_read_write) {

        //Memorizzazione File di Log Inverter
        InverterLogger.logger.log(Level.CONFIG, "Parametro da Codificare ={0} - Valore ={1} - car_read_write ={2}", 
                new Object[]{par, value, car_read_write});

        String result = car_inizio_stringa + calcolaLRC(par, value, car_read_write);

        //Memorizzazione File di Log Inverter
        InverterLogger.logger.log(Level.INFO, "Stringa Codificata = {0}", result);

        return result;

    }

    //Calcola Stringa Avvio Inverter
    public String avviaInverter() {

        return codificaStringa(par_avvio_arresto, value_avvio, par_write); //  + car_cr + car_lf;

    }

    //Arresto Inverter
    public String arrestaInverter() {

        return codificaStringa(par_avvio_arresto, value_arresto, par_write); //  + car_cr + car_lf;

    }

    //Cambio Vel Inverter
    public String cambiaVelInverter(String value) {

        return codificaStringa(par_cambio_vel, value, par_write); //  + car_cr + car_lf;

    }

    public String letturaParametro(String Par) {

        ////////////////////////
        /// DA IMPLEMENTARE  ///
        ////////////////////////
////        String res;
////        
////        res = sendToSocket(
////                codificaStringa(
////                convStringToHex(indRS485,2) 
////                + STR_READ 
////                 + Par 
////                + "0001"));
////        
////        
////        System.out.println(res);
////        System.out.println(res.substring(9, 13));
////        
////        System.out.println(res.length());
////        
////        res.substring(9, 13);
        return "NON IMPLEMENTATO";

    }

    public String modificaParametro(String Par, String value) {

        ////////////////////////
        /// DA IMPLEMENTARE  ///
        ////////////////////////
////        String res;
////        
////        res = sendToSocket(
////                codificaStringa(
////                convStringToHex(indRS485,2) 
////                + STR_READ 
////                + Par 
////                + "0001"));
////        
////        
////        System.out.println(res);
////        System.out.println(res.substring(9, 13));
////        
////        System.out.println(res.length());
////        
////        res.substring(9, 13);
        return "NON IMPLEMENTATO";

    }

    //Cambio Vel Inverter
    public String avviaInverterRotazioneInversa() {

        return codificaStringa(par_avvio_arresto, par_rotazione_inversa, par_write); // + car_cr + car_lf;

    }

}
