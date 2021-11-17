package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiIndirizziBilance;

import java.util.ArrayList;

public class IndirizziBilance {
	
	public static ArrayList<String> indirizzi; 

	public IndirizziBilance(String stringa_indirizzi_bilance, String stringa_sottorete) {
		
		indirizzi = EstraiIndirizziBilance(stringa_indirizzi_bilance,stringa_sottorete);
		 
	}
	 
}
