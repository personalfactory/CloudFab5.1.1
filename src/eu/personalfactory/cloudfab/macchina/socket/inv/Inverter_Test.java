/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.inv;

import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneHextoBinario;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneStringToHex;

/**
 *
 * @author francescodigaudio
 */
public class Inverter_Test {

    //Sottorete Dispositivi Origami
    static String default_parametro_464 = "192.168.0.";
    //Carattere di separazione A Stringa Configurazione Mduino
    static String default_parametro_468 = ".";
    //Carattere di separazione B Stringa Configurazione Mduino
    static String default_parametro_469 = "$";
    //Indirizzi periferiche 
    static String default_parametro_470 = "101.102.103.110.111.112";
    //Porte di comunicazione periferiche (sotto stringa A)
    static String default_parametro_471 = "1840.1840.1840.1880.1880.1880";
    //Porte di comunicazione periferiche (sotto stringa B)
    static String default_parametro_472 = "";
    //Porte di comunicazione periferiche (sotto stringa C)
    static String default_parametro_473 = "";
    //Tipo di periferiche
    static String default_parametro_474 = "0.0.0.1.1.1";
    //Timeout di Coumincazione periferiche (sotto stringa A)
    static String default_parametro_475 = "100.100.100.2000.2000.2000";
    //Timeout di Coumincazione periferiche (sotto stringa B)
    static String default_parametro_476 = "";
    //Timeout di Coumincazione periferiche (sotto stringa C)
    static String default_parametro_477 = "";
    //Descrizione periferiche (sotto stringa A)
    static String default_parametro_478 = "MD_MAINP.MD_SILOSP.MD_SILOSPAUX.EB80_MAINP.EB80_SILOSP.EB80_SILOSAUX";
    //Descrizione periferiche periferiche (sotto stringa B)
    static String default_parametro_479 = "";
    //Descrizione periferiche periferiche (sotto stringa C)
    static String default_parametro_480 = "";
    //Uscita periferica - su quale periferica si trova l'uscita (sotto stringa A) 
    static String default_parametro_481 = "00000000000000001111112222221111112222221111112222221123333333333333333333334444";
    //Uscita periferica - su quale periferica si trova l'uscita (sotto stringa B)
    static String default_parametro_482 = "44444444444444555555555555555555";
    //Uscita periferica - su quale periferica si trova l'uscita (sotto stringa C)
    static String default_parametro_483 = "";
    //Uscita posizione - quale posizione l'uscita occupa sulla periferica (sotto stringa A)
    static String default_parametro_484 = "52389$10$11$121AEF76CF13245612345679BDF$1179BDE$118ACE$10$128ACE$10$12$13$13$13123456789ABCDEF$10$11$12$13$14$151234";
    //Uscita posizione - quale posizione l'uscita occupa sulla periferica (sotto stringa B)
    static String default_parametro_485 = "56789ABCDEF$10$11$12123456789ABCDEF$10$11$12";
    //Uscita posizione - quale posizione l'uscita occupa sulla periferica (sotto stringa C)
    static String default_parametro_486 = "";
    //Ingressi periferica - su quale periferica si trova l'ingresso (sotto stringa A) 
    static String default_parametro_487 = "000000000000011111122222211111122222112";
    //Ingressi periferica - su quale periferica si trova l'ingresso (sotto stringa B) 
    static String default_parametro_488 = "";
    //Ingressi periferica - su quale periferica si trova l'ingresso (sotto stringa C) 
    static String default_parametro_489 = "";
    //ingressi posizione - quale posizione l'ingresso occupa sulla periferica (sotto stringa A)
    static String default_parametro_490 = "576413289ABCD123456123456789ABC789ABCDD";
    //ingressi posizione - quale posizione l'ingresso o\\\\\\\\\\\\\\\\\ ccupa sulla periferica (sotto stringa B)
    static String default_parametro_491 = "";
    //ingressi posizione - quale posizione l'ingresso occupa sulla periferica (sotto stringa C)
    static String default_parametro_492 = "";
    //Valore di pressione espresso in bar minimo richiesto per il funzionamento della macchina
    static String default_parametro_493 = "7.5";
    //Nomi Inverter
    static String default_parametro_494 = "mix.screws";
    //Indirizzi Inverter
    static String default_parametro_495 = "10.10";
    //Periferiche Inverter
    static String default_parametro_496 = "0.1";

    public static void main(String[] args) throws Exception {

////        new GestoreIO(
////                default_parametro_468,
////                default_parametro_469,
////                default_parametro_470,
////                default_parametro_464,
////                default_parametro_471 + default_parametro_472 + default_parametro_473,
////                default_parametro_474,
////                default_parametro_475 + default_parametro_476 + default_parametro_477,
////                default_parametro_478 + default_parametro_479 + default_parametro_480,
////                default_parametro_481 + default_parametro_482 + default_parametro_483,
////                default_parametro_484 + default_parametro_485 + default_parametro_486,
////                default_parametro_487 + default_parametro_488 + default_parametro_489,
////                default_parametro_490 + default_parametro_491 + default_parametro_492);
////
////        //Creazione Pannelli di Visualizzazione IO
////        GestoreIO_CreatePanels();
////
////        //Impostazione Visibilita Pannelli
////        GestoreIO_ImpostaVisPannelli(true);
////
////        //Lettura Parametri Inverter
////        ArrayList<String> nomi = EstraiDatiStringaParametri(default_parametro_494, default_parametro_468);
////        ArrayList<String> indirizzi = EstraiDatiStringaParametri(default_parametro_495, default_parametro_468);
////        ArrayList<String> periferiche = EstraiDatiStringaParametri(default_parametro_496, default_parametro_468);
//// 
////        ArrayList<InverterPanel> pannelli_inv = new ArrayList<>();
////        
////        for (int i = 0; i < nomi.size(); i++) {
////
////            Inverter_Gefran_BDI50 inverter = new Inverter_Gefran_BDI50(nomi.get(i), indirizzi.get(i), periferiche.get(i));
////
////            pannelli_inv.add(new InverterPanel(inverter, 40 + 25 * i, 200 + 25 * i));
////        
////        }
      
Inverter_Gefran_BDI50 inverter = new Inverter_Gefran_BDI50("", "01", "01", "0");
////System.out.println("ASCII = " + inverter.costrusciMessaggioASCII("10", "0000", "08", "A537")); 

String messaggio = inverter.costrusciMessaggioASCII("01", "2501", "03", "0001"); 
 

System.out.println("ASCII = " + messaggio);
        
      //  calcoloLRC("010301000A");
      //  calcoloCRC("010325230001"); 
    }

    public static byte calcoloCRC(String data) {

        byte checksum = 0;

        String TRUE_BYTE = "1111111111111111";
        String XOR_CONST = "1010000000000001";

        String crc_hex_A = "";
        String crc_hex_B = "";
        String temp = "";

        for (int i = 0; i < data.length() / 2; i++) {

            String data_hex = data.substring(0 + i * 2, 2 + i * 2);

            // System.out.println("Esadecimale =" + data_hex);
            String data_bin = ConversioneHextoBinario(data_hex, 16,2);
            //System.out.println("Binario =" + data_bin);

            String XOR = "";
            if (temp.equals("")) {
                XOR = XOR_CALC(TRUE_BYTE, data_bin);
            } else {
                XOR = XOR_CALC(temp, data_bin);
            }

            //System.out.println("XOR =" + XOR);
            // String SHIFT_XOR_1 = XOR.substring(XOR.length()-1, XOR.length())+ XOR.substring(0, XOR.length()-1);
            String SHIFT_XOR_1 = SHIFT_XOR_CALC(XOR, XOR_CONST);

            String SHIFT_XOR_2 = SHIFT_XOR_CALC(SHIFT_XOR_1, XOR_CONST);

            String SHIFT_XOR_3 = SHIFT_XOR_CALC(SHIFT_XOR_2, XOR_CONST);

            String SHIFT_XOR_4 = SHIFT_XOR_CALC(SHIFT_XOR_3, XOR_CONST);
            String SHIFT_XOR_5 = SHIFT_XOR_CALC(SHIFT_XOR_4, XOR_CONST);
            String SHIFT_XOR_6 = SHIFT_XOR_CALC(SHIFT_XOR_5, XOR_CONST);
            String SHIFT_XOR_7 = SHIFT_XOR_CALC(SHIFT_XOR_6, XOR_CONST);
            String SHIFT_XOR_8 = SHIFT_XOR_CALC(SHIFT_XOR_7, XOR_CONST);
            //System.out.println("SHIFT_XOR_8 =" + SHIFT_XOR_8);

            crc_hex_A = CloudFab5_0.ConversioneBinarioToHex(SHIFT_XOR_8.substring(8, SHIFT_XOR_8.length()));
            crc_hex_B = CloudFab5_0.ConversioneBinarioToHex(SHIFT_XOR_8.substring(0, 8));

            //System.out.println(crc_hex_A + crc_hex_B);
            temp = SHIFT_XOR_8;
            //System.out.println("\n\n");

        }

        System.out.println("CRC-16 MODBUS = " + crc_hex_A + crc_hex_B);

        return checksum;

    }

    public static String XOR_CALC(String a, String b) {

        String result = "";
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                result += "0";

            } else {
                result += "1";
            }

        }
        return result;

    }

    public static String SHIFT_XOR_CALC(String SHIFT_XOR_1, String xor_const) {

        String result = "0" + SHIFT_XOR_1.substring(0, SHIFT_XOR_1.length() - 1);

        if (SHIFT_XOR_1.charAt(15) == '1') {
            result = XOR_CALC(xor_const, result);
        }

        //System.out.println(result);
        return result;

    }
   
    public static void calcoloLRC(String str) {
 
        int [] data = new int[str.length()];
         
        for (int i=0; i<str.length()/2; i++){
         
          data[i] = Integer.parseInt(str.substring(0+2*i, 2+2*i),16);
        }
        int somma = 0;
        for (int i = 0; i < data.length; i++) {
 
            somma += data[i];

        }
         
         String s1 = ConversioneHextoBinario(ConversioneStringToHex(Integer.toString(somma), 2), 8,2);
     
        String compl = "";

        for (int i = 0; i < s1.length(); i++) {

            if (s1.charAt(i) == '1') {

                compl += '0';
            } else {
                compl += '1';
            } 
        }
         ;
         System.out.println("LRC =" + Integer.toHexString(CloudFab5_0.ConversioneBinarioToInt(compl) + 1).toUpperCase()); 

    }
}
