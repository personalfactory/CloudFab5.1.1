/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.inv;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiDatiStringaParametri;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;

/**
 *
 * @author francescodigaudio
 */
public class GestoreInverterCom {

    //Lettura Parametri Inverter
    private static final ArrayList<String> nomi = EstraiDatiStringaParametri(ParametriSingolaMacchina.parametri.get(494), ParametriSingolaMacchina.parametri.get(468));
    private static final ArrayList<String> indirizzi = EstraiDatiStringaParametri(ParametriSingolaMacchina.parametri.get(495), ParametriSingolaMacchina.parametri.get(468));
    private static final ArrayList<String> periferiche = EstraiDatiStringaParametri(ParametriSingolaMacchina.parametri.get(496), ParametriSingolaMacchina.parametri.get(468));
    private static final ArrayList<String> verso_rotazione = EstraiDatiStringaParametri(ParametriSingolaMacchina.parametri.get(497), ParametriSingolaMacchina.parametri.get(468));

    
    private static final ArrayList<InverterPanel> pannelli_inv = new ArrayList<>();

    //Dichiarazione Inverter Miscelatore
    public static Inverter_Gefran_BDI50 inverter_mix = new Inverter_Gefran_BDI50(nomi.get(0), indirizzi.get(0), periferiche.get(0), verso_rotazione.get(0));

    //Dichiarazione Inverter Coclee
    public static Inverter_Gefran_BDI50 inverter_screws = new Inverter_Gefran_BDI50(nomi.get(1), indirizzi.get(1), periferiche.get(1), verso_rotazione.get(1));

    public static void Inverter_Gefran_BDI50_Crea_Pannelli() {

        pannelli_inv.add(new InverterPanel(inverter_mix, 40, 200)); 
        pannelli_inv.add(new InverterPanel(inverter_screws, 65, 225));
    }
    
     public static void Inverter_Gefran_BDI50_ImpostaVisPannelli(Boolean vis) {
         
        for (int i=0; i<pannelli_inv.size(); i++){
        	pannelli_inv.get(i).setVisible(vis);
        
        } 
    }
     
            
}
