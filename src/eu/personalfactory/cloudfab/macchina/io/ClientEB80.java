package eu.personalfactory.cloudfab.macchina.io;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.PingAddress;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_IN_KEY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MODULO_VALVOLE_OUT_KEY;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author francescodigaudio
 */
import de.re.eeip.EEIPClient;
import de.re.eeip.cip.datatypes.ConnectionType;
import de.re.eeip.cip.datatypes.Priority;
import de.re.eeip.cip.datatypes.RealTimeFormat;
import de.re.eeip.cip.exception.CIPException;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;

public class ClientEB80 {

	private boolean configured;
	private boolean enabled;
	private String ip_address;
	boolean messaggio_da_inviare;
	int messaggio = 0;
	ArrayList<String> messaggi_list = new ArrayList<>();
	
	public EEIPClient eipClient;

	// Costruttore
	public ClientEB80(String ip_address, int timeout, boolean enabled) {

		this.ip_address = ip_address;
		this.enabled = enabled;

		////////////////////////////////////////////////////////////////////////////// Inizializzazione
		configured = false;

		if (enabled) {

			/////////////////////////////
			// CREAZIONE NUOVO CLIENT ///
			/////////////////////////////

			try {
				if (PingAddress(ip_address, timeout)) {

					eipClient = new EEIPClient();

					eipClient.setIpAddress(ip_address);

					//////////////////////
					// CONFIGURAZIONE ///
					////////////////////// 

					eipClient.RegisterSession();
					
					// Parameters for Originator -> Target communication
					eipClient.setO_T_InstanceID(MODULO_VALVOLE_OUT_KEY); // Output Assembly 65hex
					eipClient.setO_T_Length(3); // 4
					eipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
					eipClient.setO_T_ownerRedundant(true);
					eipClient.setO_T_priority(Priority.Urgent); // Urgent
					eipClient.setO_T_variableLength(true);
					eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
					eipClient.setRequestedPacketRate_O_T(2000); // 2000 il valore standard è
					
					// Parameters for Target -> Originator communication
					eipClient.setT_O_InstanceID(MODULO_VALVOLE_IN_KEY); // Input Assembly 68hex
					eipClient.setT_O_Length(0);
					eipClient.setT_O_RealTimeFormat(RealTimeFormat.ZeroLength);
					eipClient.setT_O_ownerRedundant(true);
					eipClient.setT_O_priority(Priority.Scheduled);
					eipClient.setT_O_variableLength(false); // Urgent
					eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
					//eipClient.setRequestedPacketRate_T_O(00);
				
					configured = true; 
					
					eipClient.ForwardOpen(); 
					
					new ThreadComunicazione().start();
					
				} else {

					SessionLogger.logger.severe("EB80 Periferica non raggiungibile - indirizzo =" + ip_address);

				}
			} catch (IOException e) {
				configured = false;
			} catch (CIPException e) {
			}
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////// Invio
	//////////////////////////////////////////////////////////////////////////////////////////// dati
	//////////////////////////////////////////////////////////////////////////////////////////// al
	//////////////////////////////////////////////////////////////////////////////////////////// Server
	//////////////////////////////////////////////////////////////////////////////////////////// UDP
	public Boolean send(ArrayList<String> messaggi) {
		Boolean result = false;

        if (configured && enabled) {

            for (int i = 0; i < messaggi.size(); i++) {

            //	System.out.println(CloudFab5_0.ConversioneBinarioToInt(messaggi.get(i)).byteValue());
            	
                eipClient.O_T_IOData[i] = CloudFab5_0.ConversioneBinarioToInt(messaggi.get(i)).byteValue();

                result = true;
            }

        }
        
		return result;
	}
	 
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ThreadAggiornaPanels
	private class ThreadComunicazione extends Thread {
		@Override
		public void run() {


			try { 
				while (!Thread.interrupted()) { 

					Thread.sleep(2000);
				}

				eipClient.ForwardClose();
				eipClient.UnRegisterSession();

			} catch (CIPException e) {
			} catch (IOException e) {
			} catch (InterruptedException e) {
			}
		}
	}

    //////////////////////////////////////////////////////////////////////////////////// Codifica Messaggio da Inviare al EB80
	public ArrayList<String> codificaMessaggioUscite(String str_da_codificare) {

		int counter = 0;
		ArrayList<String> str_to_send = new ArrayList<>();
		String temp = "";
		 
		for (int i = 0; i < str_da_codificare.length(); i++) {

			if (counter == 8) {

				str_to_send.add(temp);
				temp = "";
				counter = 0; 
			}

			temp = str_da_codificare.charAt(i) + temp;
			counter++; 
		}
		 
		str_to_send.add(temp);

		return str_to_send;

	}

//	private void riavviaSessione() {
//
//		if (enabled) {
//			try {
//
//				eipClient.UnRegisterSession();
//
//				Thread.sleep(500);
//
//				eipClient.RegisterSession();
//
//			} catch (IOException e) {
//
//			} catch (InterruptedException e) {
//			}
//		}
//
//	}

	///////////////////////////
	// GETTERS AND SETTERS ///
	///////////////////////////
	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
}
