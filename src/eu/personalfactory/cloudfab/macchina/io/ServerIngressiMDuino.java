/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
/**
 *
 * @author francescodigaudio
 */
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_CHAR_FINALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_CHAR_INIZIALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_SERVER_MDUINO; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import eu.personalfactory.cloudfab.macchina.controllo.ThreadControlloValvolaScarico;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

public class ServerIngressiMDuino extends Thread {

	public static final int PORT = 1841; // porta al di fuori del range 1-1024 
	private final static Integer DIM_PACCHETTI = 32;
	GestoreIO gestore;
	private DatagramSocket socket;
	private boolean sel_vibro_on = false;

	public ServerIngressiMDuino(GestoreIO gestore) throws IOException, InterruptedException {

		this.gestore = gestore;
	}

	@Override
	public void run() {
		super.run(); //To change body of generated methods, choose Tools | Templates.

		try {
			socket = new DatagramSocket(PORT);
			//socket.setSoTimeout(10);
			
			boolean in_manuale= false;
			
			while(true){

				byte[] buffer_rec = new byte[DIM_PACCHETTI];
 
				DatagramPacket request = new DatagramPacket(buffer_rec, DIM_PACCHETTI);
				socket.receive(request);
				String rep = new String(buffer_rec);
				String reponse = rep;
				String msg_to_client = "";
				String indirizzo = request.getAddress().getHostAddress();
				if (reponse.contains(MDUINO_CHAR_FINALE)) {

					reponse = reponse.substring(0, reponse.indexOf(MDUINO_CHAR_FINALE) + 1);
 
					//Valida Messaggio ricevuto
					if (reponse.startsWith(MDUINO_CHAR_INIZIALE) && reponse.contains(MDUINO_CHAR_FINALE)) {

						//Pacchetto Formalmente corretto
						reponse = reponse.substring(MDUINO_CHAR_INIZIALE.length(), reponse.indexOf(MDUINO_CHAR_FINALE));
 
						if (reponse.length() > 2) {
 
							//String id_pacchetto = reponse.substring(0, 2);
							reponse = reponse.substring(2, reponse.length());

							gestore.GestoreIO_ModificaIn(reponse, indirizzo);

							//Risposta per il client
							msg_to_client = rep;
							 
							//////////////////////////////////////////////////////////////////////////////////////////////////////////////Invio risposta al client
							socket.send(new DatagramPacket(msg_to_client.getBytes(), // Buffer
									msg_to_client.getBytes().length, // Dimensione del buffer
									request.getAddress(), // Indirizzo del client
									request.getPort())); // Porta comunicazione client
							  
						}
					}
				}

				ServerIngressiMDuino.sleep(FREQUENZA_SERVER_MDUINO);

				
				/////////////////////////////////////////////////////////////////////////////////////////////////////// Gestore Selettore Manuale Valvola
				if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO)) {

					if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);
						ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
						ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_100");
					} else {  
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);
						ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
						ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_100_COMANDO_UNICO");
					}

					in_manuale = true;
				} else if (in_manuale){
					in_manuale = false;

					if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
						ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
						ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_0");
					} else {
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
						ThreadControlloValvolaScarico.sleep(TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA);
						ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");
					}
				}
				
//				/////////////////////////////////////////////////////////////////////////////////////////////////////// Gestore Selettore Vibro
//				if (!sel_vibro_on && GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO)) {
//					sel_vibro_on = true;
//					////////////////////////////////////////////////////////////////////////////////////////////////DISATTIVAZIONE CONTATTORE NASTRO - INTERVENTO FINEOCRSA
//					GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO, OUTPUT_TRUE_CHAR);
//				} else if (!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO)) {
//					sel_vibro_on = false;
//					////////////////////////////////////////////////////////////////////////////////////////////////DISATTIVAZIONE CONTATTORE NASTRO - INTERVENTO FINEOCRSA
//					GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO, OUTPUT_FALSE_CHAR);
//				}

				
			}

		} catch (SocketException ex) {
			Logger.getLogger(ServerIngressiMDuino.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException | InterruptedException ex) {
			Logger.getLogger(ServerIngressiMDuino.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}

