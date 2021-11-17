/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneBinarioToHex;
import java.util.ArrayList;

/**
 *
 * @author francescodigaudio
 */
public class tempo {

    public static void main(String[] args) {

        String messaggio = "0000000000000000001";
        System.out.println("messaggio =" + messaggio);
      
        String result = "";

        ArrayList<String> tempo = new ArrayList<>();

        while (messaggio.length() > 3) {

            tempo.add(messaggio.substring(0, 4));
            messaggio = messaggio.substring(4, messaggio.length());

        }

        while (messaggio.length() < 4) {
            messaggio = "0" + messaggio;

        }

        tempo.add(messaggio);

        for (int i = 0; i < tempo.size(); i++) {

            result += ConversioneBinarioToHex(tempo.get(i));

        }
////        System.out.println("g =" + messaggio);
////        if (!messaggio.equals("")) {
////
////            //msg_prev = msg;
////            for (int i = 0; i < messaggio.length(); i++) {
////
////                temp += messaggio.charAt(i);
////                if (index == 3) {
////
////                    index = 0;
////                    result += ConversioneBinarioToHex(temp);
////                    temp = "";
////
////                } else {
////                    index++;
////                }
////
////            }
////
////            // msg = "";
////        }
//        result += ConversioneBinarioToHex(temp);

        System.out.println("Result = " + result);
    }
}
