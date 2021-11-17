package eu.personalfactory.cloudfab.macchina.utility;

import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;  
import java.util.Date; 
import java.text.DateFormat;  

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_TIPO_MATERIALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_OPERAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_PROCEDURA_ADOTTATA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_TIPO_MOV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_DESCRI_MOV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_PESO_TEORICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_ID_CICLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MOV_IN_ORIGINE_MOV;
public class MovimentiIN {
	  
	public ArrayList<Integer> idMateriale;  		//ID COMPONENTE NEL NOSTRO CLOUD
	public ArrayList<Integer> quantita; 			//QUANTITA DI MATERIALE RESA DISPONIBILE AL MAGAZZINO (CARICO) QUANTITA DI MATERIALE UTILIZZATO (MOVIMENTI DI SCARICO PER PRODUZIONE SULLA MACCHINA)
	public ArrayList<String> codIngressoComp; 		//CODICE MOVIMENTO DI MAGAZZINO IN INGRESSO (CODICE A BARRE CHE CARICHERA' L'OPERATORE
	public ArrayList<String> codOperatore; 			//CODICE OPERATORE CHE HA ESEGUITO IL CARICO DEL MOVIMENTO DI MAGAZZINO					
	public ArrayList<String> dtMov; 				//DATA DEL MOVIMENTO DI MAGAZZINO	
	public ArrayList<String> silo; 					//SILO IN CUI SI CONSIGLIA DI CARICARE IL MATERIALE (SECONDO INDICAZIONI DEL RESPOSABILE DI PRODUZIONE)					
	public ArrayList<Integer> abilitato; 			//ABILITAZIONE MOVIMENTO (PUO' ESSERE UTILIZZATO PER DISABILITARE I MOVIMENTI RELATIVI A MATERIE PRIME NON PIU' DISPONIBILI)					
	public ArrayList<String> info1;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info2;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info3;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info4;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info5;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info6;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info7;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info8;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info9;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	public ArrayList<String> info10;  				//INFORMAZIONI ACCESSORIE E NOTE PER L'OPERATORE
	private String dateFormat;
	 
	public MovimentiIN(String dateFormat) {
		
		super(); 
		this.dateFormat = dateFormat;
		idMateriale = new ArrayList<>();
		quantita = new ArrayList<>();
		codIngressoComp = new ArrayList<>();
		codOperatore = new ArrayList<>();
		dtMov = new ArrayList<>();
		silo = new ArrayList<>();
		abilitato = new ArrayList<>(); 
		info1 = new ArrayList<>();
		info2 = new ArrayList<>();
		info3 = new ArrayList<>();
		info4 = new ArrayList<>();
		info5 = new ArrayList<>();
		info6 = new ArrayList<>();
		info7 = new ArrayList<>();
		info8 = new ArrayList<>();
		info9 = new ArrayList<>();
		info10 = new ArrayList<>();
	 
	}
	 
	public void registraMovimenti() {

		for (int i = 0; i < idMateriale.size(); i++) {

			try {
				  
				//Coversione data da String
				String string = dtMov.get(i);
				DateFormat format = new SimpleDateFormat(dateFormat, Locale.ITALIAN);
				Date date = format.parse(string); 
				
				//Inserimento Movimento nel dabatase
				RegistraNuovoMovimentoMagazzino(idMateriale.get(i), MOV_IN_TIPO_MATERIALE, quantita.get(i),
						codIngressoComp.get(i), codOperatore.get(i), MOV_IN_OPERAZIONE, MOV_IN_PROCEDURA_ADOTTATA, MOV_IN_TIPO_MOV, MOV_IN_DESCRI_MOV,
						silo.get(i), MOV_IN_PESO_TEORICO, MOV_IN_ID_CICLO, MOV_IN_ORIGINE_MOV, date, true, info1.get(i),
						info2.get(i), info3.get(i), info4.get(i), info5.get(i), info6.get(i), info7.get(i),
						info8.get(i), info9.get(i), info10.get(i));
			 
			} catch (Exception e) {
				
				System.out.println("Errore Durante la registra<ione del Movimento");
			}

		}

	}
}
