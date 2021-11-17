/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

/**
 *
 * @author francescodigaudio
 */
import eu.personalfactory.cloudfab.macchina.loggers.BilanceLogger;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;

import java.io.*;
import java.net.*; 
import java.util.logging.Level;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ADDRESS_PING_TIMEOUT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_INDIRIZZO_DEFAULT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_COMMUTAZIONE_PESO_LORDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_COMMUTAZIONE_PESO_NETTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_CARATTERE_INIZIO_STRINGA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_PESO_LORDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_PESO_NETTO;  
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;

public class ClientPesaTLB4 {

	private GestorePesaTLB4EthernetIp tlb_4;
	private Boolean connesso = false;
//	private Socket requestSocket;
//	private BufferedReader in = null;
//	private PrintWriter out = null;  
 

	//COSTRUTTORE
	public ClientPesaTLB4(Integer idPesaCorrente) throws UnknownHostException, IOException {
	
		String address_tlb = IndirizziBilance.indirizzi.get(idPesaCorrente);

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.CONFIG, "Creazione Socket = " + address_tlb);

		tlb_4 = new GestorePesaTLB4EthernetIp(address_tlb);

		if (!ABILITA_SIMULAZIONE_PROCESSO) {
			
			connesso = tlb_4.creaEipClient(ADDRESS_PING_TIMEOUT);
			
		} else { 
			
			connesso = true; 
		}
		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.INFO, "Connessione Socket " + address_tlb + " .............." + connesso);

	}

	//Verifica Connessione
	public boolean verficaConn() { 

		BilanceLogger.logger.log(Level.INFO, "Verifica Connessione = " + connesso);
		
		return connesso;

	}

	//Commutazione Peso Lordo
	public String commutaNetto() {

		//////////////////////////////
		// COMMUTAZIONE PESO NETTO ///
		//////////////////////////////

		String rep = "";

		// Memorizzazione Log Bilance
		BilanceLogger.logger.config("Commutazione Peso Netto");

		// Preparazione Messaggio da Inviare
		String strToSend = codificaStringa(TLB4_INDIRIZZO_DEFAULT, TLB4_KWORD_COMMUTAZIONE_PESO_NETTO); // INDIRIZZO_TLB4[id],
					        
		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Commutazione Peso Netto - Stringa da Inviare ={0}", strToSend);

		int i = 0;
		boolean commEseguita = false;
		boolean commFallita = false;

		// Invio ripetuto stringa di commutazione
		while (!commEseguita && !commFallita) {

			boolean commPesoVal = false;

			rep = tlb_4.sendToSocket(strToSend);
		    
			commPesoVal = !rep.equals("");

			if (commPesoVal) {

				commEseguita = true;

				// Memorizzazione Log Bilance
				BilanceLogger.logger.config("Commutazione Peso Netto Eseguita");

			} else {

				i++;

				// Memorizzazione Log Bilance
				BilanceLogger.logger.log(Level.FINE, "i ={0}", i);

				if (i == Integer.parseInt(ParametriGlobali.parametri.get(91))) {

					// Memorizzazione Log Bilance
					BilanceLogger.logger.severe("Commutazione Peso Netto Fallita");

					commFallita = true;

				}
			}
		}

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.CONFIG, "Invio Messaggio = {0}", rep);

		return rep;
	}

	//Commutazione Peso Lordo
	public String commutaLordo() {


		//////////////////////////////
		// COMMUTAZIONE PESO LORDO ///
		//////////////////////////////
 
		String value = "";

		// Memorizzazione Log Bilance
		BilanceLogger.logger.config("Commutazione Peso Lordo");

		// Preparazione Messaggio da Inviare
		String strToSend = codificaStringa(TLB4_INDIRIZZO_DEFAULT, TLB4_KWORD_COMMUTAZIONE_PESO_LORDO);

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Commutazione Peso Lordo Stringa da Inviare ={0}", strToSend);

		int i = 0;
		boolean commEseguita = false;
		boolean commFallita = false;

		// Invio ripetuto stringa di commutazione
		while (!commEseguita && !commFallita) {

			boolean commPesoVal = false;

			// ommPesoVal = !pesaIpDedicato.get(0).sendToSocket(strToSend).equals("");
			value = tlb_4.sendToSocket(strToSend);
			commPesoVal = !value.equals("");

			if (commPesoVal) {

				commEseguita = true;

				// Memorizzazione Log Bilance
				BilanceLogger.logger.config("Commutazione Peso Lordo Eseguita");

			} else {
				i++;

				// Memorizzazione Log Bilance
				BilanceLogger.logger.log(Level.FINE, "i ={0}", i);

				if (i == Integer.parseInt(ParametriGlobali.parametri.get(91))) {

					// Memorizzazione Log Bilance
					BilanceLogger.logger.severe("Commutazione Peso Lordo Fallita");

					commFallita = true;
				}
			}
		}

		return value;

	}

	//Lettura Peso Netto
	public String pesoNetto() {


		/////////////////////////
		// LETTURA PESO NETTO ///
		/////////////////////////
		String value = "";

		// Preparazione Messaggio da Inviare
		String strToSend = codificaStringa(TLB4_INDIRIZZO_DEFAULT, TLB4_KWORD_LETTURA_PESO_NETTO);

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Lettura Peso Netto - Stringa da inviare = {0}", strToSend);

		String res = "";

		// res = pesaIpDedicato.get(0).sendToSocket(strToSend);
		res = tlb_4.sendToSocket(strToSend);

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Lettura Peso Netto - Stringa Ricevuta = {0}", res);

		value = res;

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Lettura Peso Netto - Stringa Validata ={0}", value);

		return value;
	}

	//Lettura Peso Lordo
	public String pesoLordo() {

		//////////////////////////
		// LETTURA PESO LORDO ///
		//////////////////////////

		String value = "";

		// Preparazione Messaggio da Inviare
		String strToSend = codificaStringa(TLB4_INDIRIZZO_DEFAULT, TLB4_KWORD_LETTURA_PESO_LORDO);

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Lettura Peso Lordo - Stringa da Inviare ={0}", strToSend);

		String res = "";

		// res = pesaIpDedicato.get(0).sendToSocket(strToSend);
		res = tlb_4.sendToSocket(strToSend);

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Lettura Peso Lordo - Stringa Ricevuta ={0}", res);

		if (!"".equals(res)) {
			value = res;
		}

		// Memorizzazione Log Bilance
		BilanceLogger.logger.log(Level.FINE, "Lettura Peso Lordo - Stringa Validata ={0}", value);

		return value;
	}

	//Chiusura Comunicazione
	public void chiudi() {

		BilanceLogger.logger.config("Tentativo di Chiusura Socket");

		tlb_4.close();

	}
//
//	//Comunica con il Server e ne Restituisce la Risposta
//	private String comunica(String msg) {
//
//		String rep = "";
//
//		try {
//
//			//Memorizzazione Log Bilance
//			BilanceLogger.logger.log(Level.CONFIG, "Creazione Socket - Eseguita");
//
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(requestSocket.getOutputStream()));
//			out = new PrintWriter(bw, true);
//			//Memorizzazione Log Bilance
//			BilanceLogger.logger.log(Level.CONFIG, "Output ed Input Stream Generati");
//
//			//Memorizzazione Log Bilance
//			BilanceLogger.logger.log(Level.CONFIG, "Inizio Scrittura");
//			out.println(msg);
//			out.flush(); 
//
//			// creazione stream di input da tastiera
//			in = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
//			//Memorizzazione Log Bilance
//			BilanceLogger.logger.log(Level.CONFIG, "Fine Scrittura - Inizio Lettura");
//			// String userInput; 
//			rep = in.readLine();
//
//			//Memorizzazione Log Bilance
//			BilanceLogger.logger.log(Level.CONFIG, "Fine Lettura");
//
//			///requestSocket.close();  
//		} catch (IOException ex) {
//
//			//Memorizzazione Log Bilance
//			BilanceLogger.logger.log(Level.SEVERE, "Comunicazione Fallita e ={0}", ex);
//
//
//
//		}
//		return rep;
//
//	} 

	// Determina la Stringa da Trasmettere Secondo la Codifica Laumas
	public String codificaStringa(String addr, String s) {

		CheckSum ck = new CheckSum(convertiStringa(addr + s));
		String strCK = ck.risHexString.toUpperCase();

		return TLB4_KWORD_CARATTERE_INIZIO_STRINGA + addr + s + strCK;
	}

	// Converte una Stringa in un Array di Interi
	public int[] convertiStringa(String s) {
		char c;
		int w;
		int[] arrayInteri = new int[100];

		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			w = Integer.valueOf(c);
			arrayInteri[i] = w;
		}
		return arrayInteri;

	}

}
