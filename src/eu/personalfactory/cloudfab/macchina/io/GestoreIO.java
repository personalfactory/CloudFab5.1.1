/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

import de.re.eeip.cip.exception.CIPException;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiDatiStringaParametri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_CHAR_FINALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_CHAR_INIZIALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_CHAR_INPUT_ANALOGICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_CHAR_RS485;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_DIM_PACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MDUINO_NUM_RIP_MAX;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NOME_INGRESSI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NOME_USCITE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUMERO_MAX_USCITE_EB80;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUMERO_MAX_USCITE_MDUINO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DELAY_ATTIVAZIONE_USCITE_GESTORE_MODIFICA_OUT_EV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Francesco Di Gaudio
 */
public final class GestoreIO {

	static ArrayList<IODevice> devices;

	static ArrayList<IO> IO_Input;
	static ArrayList<IO> IO_Output;
	static ArrayList<IODevicePanel> panels;
	private static String char_sepA;
	static String char_sepB;
	static String periferiche_ip;
	static String periferiche_subnet;
	static String periferiche_port;
	static String periferiche_type;
	static String periferiche_timeout;
	static String periferiche_descri;
	static String uscita_periferica;
	static String uscita_posizione;
	static String ingresso_periferica;
	static String ingresso_posizione;
	static String periferiche_en;
	private static Boolean vis_pannelli;

	private static ArrayList<Integer> uscite_device;
	private static ArrayList<Integer> pos_uscite;
	private static ArrayList<Integer> ingressi_device;
	private static ArrayList<Integer> pos_ingressi;

	public GestoreIO(String char_sepA, // CHAR_SEP_A
			String char_sepB, // CHAR_SEP_B
			String periferiche_ip, // PERIFERICHE_IP
			String periferiche_subnet, // SUBNET
			String periferiche_port, // PERIFERICHE_PORT
			String periferiche_type, // PERIFERICHE_TYPE
			String periferiche_timeout, // PERIFERICHE_TIMEOUT
			String periferiche_descri, // PERIFERICHE_NAME_COD
			String uscita_periferica, // USCITA_PERIFERICA
			String uscita_posizione, // USCITA_POS
			String ingressi_periferica, // INGRESSI_PERIFERICA
			String ingressi_posizione, // INGRESSI_POS
			String periferiche_en) throws IOException, InterruptedException {

		////////////////////////////////////////////////////////////////////////////// Config
		GestoreIO.char_sepA = char_sepA;
		GestoreIO.char_sepB = char_sepB;
		GestoreIO.periferiche_ip = periferiche_ip;
		GestoreIO.periferiche_subnet = periferiche_subnet;
		GestoreIO.periferiche_port = periferiche_port;
		GestoreIO.periferiche_type = periferiche_type;
		GestoreIO.periferiche_timeout = periferiche_timeout;
		GestoreIO.periferiche_descri = periferiche_descri;
		GestoreIO.uscita_periferica = uscita_periferica;
		GestoreIO.uscita_posizione = uscita_posizione;
		GestoreIO.ingresso_periferica = ingressi_periferica;
		GestoreIO.ingresso_posizione = ingressi_posizione;
		GestoreIO.periferiche_en = periferiche_en;

		//////////////////////////////////////////////////////////////////////////////// Init
		devices = new ArrayList<>();
		IO_Input = new ArrayList<>();
		IO_Output = new ArrayList<>();

		//////////////////////////////////////////////////////////////////////////////// Lettura
		//////////////////////////////////////////////////////////////////////////////// Dati
		//////////////////////////////////////////////////////////////////////////////// Periferiche
		ArrayList<String> dati_periferiche_ip = EstraiDatiStringaParametri(periferiche_ip, char_sepA);
		ArrayList<String> dati_periferiche_port = EstraiDatiStringaParametri(periferiche_port, char_sepA);
		ArrayList<String> dati_periferiche_type = EstraiDatiStringaParametri(periferiche_type, char_sepA);
		ArrayList<String> dati_periferiche_timeout = EstraiDatiStringaParametri(periferiche_timeout, char_sepA);
		ArrayList<String> dati_periferiche_name = EstraiDatiStringaParametri(periferiche_descri, char_sepA);
		ArrayList<String> dati_periferiche_enabled = EstraiDatiStringaParametri(periferiche_en, char_sepA);

		for (int i = 0; i < dati_periferiche_ip.size(); i++) {

			devices.add(new IODevice(i, // id
					periferiche_subnet + dati_periferiche_ip.get(i), // IP ADDRESS
					Integer.parseInt(dati_periferiche_port.get(i)), // PORT
					Integer.parseInt(dati_periferiche_timeout.get(i)), // TIMEOUT
					Integer.parseInt(dati_periferiche_type.get(i)), // TYPE (0: MDUINO - 1: EB80)
					dati_periferiche_name.get(i), // NAME COD
					MDUINO_DIM_PACCHETTI, // DIMENSIONE PACCHETTI COMUNICAZIONE MDUINO
					MDUINO_CHAR_INIZIALE, // CARATTERE INZIALE PACCHETTI MDUINO
					MDUINO_CHAR_FINALE, Integer.parseInt(dati_periferiche_enabled.get(i)))); // CARATTERE FINALE
																								// PACCHETTI MDUINO

		}
		//////////////////////////////////////////////////////////////////////////////// Lettura
		//////////////////////////////////////////////////////////////////////////////// Relazione
		//////////////////////////////////////////////////////////////////////////////// Uscite
		//////////////////////////////////////////////////////////////////////////////// -
		//////////////////////////////////////////////////////////////////////////////// Periferica
		uscite_device = estrai_dati_IO(uscita_periferica, char_sepB);

		//////////////////////////////////////////////////////////////////////////////// Lettura
		//////////////////////////////////////////////////////////////////////////////// Posizione
		//////////////////////////////////////////////////////////////////////////////// Uscite
		//////////////////////////////////////////////////////////////////////////////// sulla
		//////////////////////////////////////////////////////////////////////////////// Periferica
		pos_uscite = estrai_dati_IO(uscita_posizione, char_sepB);

		//////////////////////////////////////////////////////////////////////////////// Lettura
		//////////////////////////////////////////////////////////////////////////////// Relazione
		//////////////////////////////////////////////////////////////////////////////// Ingressi
		//////////////////////////////////////////////////////////////////////////////// -
		//////////////////////////////////////////////////////////////////////////////// Periferica
		ingressi_device = estrai_dati_IO(ingressi_periferica, char_sepB);

		//////////////////////////////////////////////////////////////////////////////// Lettura
		//////////////////////////////////////////////////////////////////////////////// Posizione
		//////////////////////////////////////////////////////////////////////////////// Ingressi
		//////////////////////////////////////////////////////////////////////////////// sulla
		//////////////////////////////////////////////////////////////////////////////// Periferica
		pos_ingressi = estrai_dati_IO(ingressi_posizione, char_sepB);

		//////////////////////////////////////////////////////////////////////////////// Dichiarazione
		//////////////////////////////////////////////////////////////////////////////// Inizializzazione
		//////////////////////////////////////////////////////////////////////////////// Ingressi
		//////////////////////////////////////////////////////////////////////////////// e
		//////////////////////////////////////////////////////////////////////////////// Uscite
		for (int i = 0; i < ingressi_device.size(); i++) {
			try {
				IO_Input.add(new IO(0, NOME_INGRESSI[i], ingressi_device.get(i), pos_ingressi.get(i), false, false));
			} catch (Exception e) {
				SessionLogger.logger.severe("Errore Configurazione Ingressi Eseguire aggiornamento -e: " + e);
			}
		}

		for (int i = 0; i < uscite_device.size(); i++) {
			try {
				IO_Output.add(new IO(1, NOME_USCITE[i], uscite_device.get(i), pos_uscite.get(i), false, false));
			} catch (Exception e) {
				SessionLogger.logger.severe("Errore Configurazione Uscite Eseguire aggiornamento -e:" + e);
			}
		}

		//////////////////////////////////////////////////////////////////////////////// Assegnazione
		//////////////////////////////////////////////////////////////////////////////// riferimento
		//////////////////////////////////////////////////////////////////////////////// Posizione
		//////////////////////////////////////////////////////////////////////////////// Uscite
		//////////////////////////////////////////////////////////////////////////////// su
		//////////////////////////////////////////////////////////////////////////////// Device
		for (int j = 0; j < IO_Output.size(); j++) {
			devices.get(IO_Output.get(j).getDevice()).addToIndexOutput(j);
			devices.get(IO_Output.get(j).getDevice()).pos_output_dev.add(IO_Output.get(j).getPosition());
		}
		//////////////////////////////////////////////////////////////////////////////// Assegnazione
		//////////////////////////////////////////////////////////////////////////////// riferimento
		//////////////////////////////////////////////////////////////////////////////// Posizione
		//////////////////////////////////////////////////////////////////////////////// Ingressi
		//////////////////////////////////////////////////////////////////////////////// su
		//////////////////////////////////////////////////////////////////////////////// Device
		for (int j = 0; j < IO_Input.size(); j++) {
			devices.get(IO_Input.get(j).getDevice()).addToIndexInput(j);
			devices.get(IO_Input.get(j).getDevice()).pos_input_dev.add(IO_Input.get(j).getPosition());
		}

		//////////////////////////////////////////////////////////////////////////////// Avvio
		//////////////////////////////////////////////////////////////////////////////// Server
		//////////////////////////////////////////////////////////////////////////////// ricezioni
		//////////////////////////////////////////////////////////////////////////////// variazioni
		//////////////////////////////////////////////////////////////////////////////// ingressi
		avvioServerIngressiMduino();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// avvioServerIngressi
	public void avvioServerIngressiMduino() throws IOException, InterruptedException {

		new ServerIngressiMDuino(this).start();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// createPanels
	// Creazione pannelli per la visualizzazione di uscite e ingressi sulle
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// periferiche
	public static void GestoreIO_CreatePanels() {
		panels = new ArrayList<>();
		for (int i = 0; i < devices.size(); i++) {

			panels.add(new IODevicePanel(devices.get(i), IO_Output, IO_Input, (50 + (20 * i)), (50 + (20 * i))));
			panels.get(i).setEnabled(devices.get(i).isEnaled());
		}

		new ThreadAggiornaPanels().start();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// impostaVisPannelli
	public static void GestoreIO_ImpostaVisPannelli(boolean vis) {

		vis_pannelli = vis;
		for (int i = 0; i < panels.size(); i++) {

			if (devices.get(i).isEnaled()) {
				panels.get(i).setVisible(vis);
			} else {
				panels.get(i).setVisible(false);
			}

		}
	}

	// Client EthernetIP per gestire le comunicazioni verso HDMI
	public static boolean GestoreIO_ModificaOut(String uscite, String stato) {

		boolean result = false;

		// Estrapolazione lista uscite
		ArrayList<String> outputs = EstraiDatiStringaParametri(uscite, char_sepA);
		ArrayList<String> values = EstraiDatiStringaParametri(stato, char_sepA);
		ArrayList<Integer> device_changed = new ArrayList<>();
 
		for (int i = 0; i < outputs.size(); i++) {

			if (!device_changed.contains((uscite_device.get(Integer.parseInt(outputs.get(i)))))) {

				device_changed.add(uscite_device.get(Integer.parseInt(outputs.get(i))));

			}

			IO_Output.get(Integer.parseInt(outputs.get(i))).setSoft_state(values.get(i).equals(OUTPUT_TRUE_CHAR));
		}

		for (int i = 0; i < device_changed.size(); i++) {

			if (devices.get(device_changed.get(i)).getType() == 0) {

				//////////////
				// MDUINO ///
				//////////////
				String[] array_out = new String[NUMERO_MAX_USCITE_MDUINO];

				for (int k = 0; k < array_out.length; k++) {

					array_out[k] = "0";
				}
				String messaggio = "";
				for (int j = 0; j < IO_Output.size(); j++) {

					if (IO_Output.get(j).getDevice().equals(devices.get(device_changed.get(i)).getId())) {
						String v = "0";
						if (IO_Output.get(j).getPosition() != 99) {
							array_out[IO_Output.get(j).getPosition() - 1] = "0";
							if (IO_Output.get(j).getSoft_state()) {
								v = "1";
								array_out[IO_Output.get(j).getPosition() - 1] = "1";
							}
							messaggio += v;
						}
					}
				}

				// new threadInvio(device_changed.get(i), array_out).start();
				invoInstruzionePerifericaMduino(device_changed.get(i), array_out, messaggio);

			} else if (devices.get(device_changed.get(i)).getType() == 1) {

				///////////
				// EB80 //
				///////////
				String[] array_out = new String[NUMERO_MAX_USCITE_EB80];

				for (int k = 0; k < array_out.length; k++) {

					array_out[k] = "0";
				}
				String messaggio = "";
				for (int j = 0; j < IO_Output.size(); j++) {

					if (IO_Output.get(j).getDevice().equals(devices.get(device_changed.get(i)).getId())) {
						String v = "0";
						if (IO_Output.get(j).getPosition() != 99) {
							
							
							array_out[IO_Output.get(j).getPosition() - 1] = "0";
							if (IO_Output.get(j).getSoft_state()) {
								v = "1";
								array_out[IO_Output.get(j).getPosition() - 1] = "1";
							}
							messaggio = v + messaggio;
							
						}
					}
				}
 
				for (int j = 0; j < array_out.length; j++) {

				}
				// new threadInvio(device_changed.get(i), array_out).start();

				invoInstruzionePerifericaEB80(device_changed.get(i), array_out, messaggio);

			}
			
			try {
				Thread.sleep(DELAY_ATTIVAZIONE_USCITE_GESTORE_MODIFICA_OUT_EV);
			} catch (InterruptedException e) { 
			}

		}
		
		

		return result;

	}
	
	///////////////
	// FUNZIONI ///
	///////////////

	private static void invoInstruzionePerifericaMduino(Integer device_id, String[] array_out, String messaggio) {

		for (int i = 0; i < array_out.length; i++) {
		}

		String request = "";
		for (int k = 0; k < array_out.length; k++) {
			request += array_out[k];
		}
		boolean msg_inviato = false;
		boolean msg_da_non_inviare = false;
		int num_rip = 0;
		 
		if (!ABILITA_SIMULAZIONE_PROCESSO) {
			
			while (!msg_inviato && !msg_da_non_inviare) {

				String to_send = devices.get(device_id).clientMduino.codificaMessaggioUscite(request);
  
				if (!to_send.equals("")) {
 
					String reponse = devices.get(device_id).clientMduino.send(to_send);
					  
					if (!reponse.equals("") && reponse.equals(to_send)) {

						////////////////////////////////////////////////
						// MESSAGGIO INVIATO E RICEVUTO CORRETTAMENTE //
						////////////////////////////////////////////////
						msg_inviato = true;

						int index = 0;
						for (int i = 0; i < IO_Output.size(); i++) {
							if (IO_Output.get(i).getPosition() != 99) {
								if (IO_Output.get(i).getDevice().equals(device_id)) {

									IO_Output.get(i).setDev_state(messaggio.charAt(index) == '1');
									index++;
								}
							}
						}

					} else {
						if (num_rip < MDUINO_NUM_RIP_MAX) { 
							num_rip++;
						} else {
							msg_inviato = true;
						}
					}
				} else {
					msg_da_non_inviare = true;
				}
			}
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	private static void invoInstruzionePerifericaEB80(Integer device_id, String[] array_out, String messaggio) {

		String request = "";

		for (int k = 0; k < array_out.length; k++) {
			request += array_out[k];
		}
		boolean msg_inviato = false;
		boolean msg_da_non_inviare = false;
		int num_rip = 0;

		if (!ABILITA_SIMULAZIONE_PROCESSO) {
			
			while (!msg_inviato && !msg_da_non_inviare) {

				ArrayList<String> to_send = devices.get(device_id).clientEB80.codificaMessaggioUscite(request);
 
				// Verifica messaggio di risposta
				if (!to_send.equals("")) {

					// Invio stringa
					Boolean reponse = devices.get(device_id).clientEB80.send(to_send);
					 
					if (reponse) {

						/////////////////////////////////////////////////
						// MESSAGGIO INVIATO E RICEVUTO CORRETTAMENTE //
						/////////////////////////////////////////////////
						msg_inviato = true;
						int index = 0;

						for (int i = 0; i < IO_Output.size(); i++) {

							if (IO_Output.get(i).getPosition() != 99) {
								if (IO_Output.get(i).getDevice().equals(device_id)) {

									IO_Output.get(i).setDev_state(messaggio.charAt(index) == '1');
									index++;
								}
							}
						}

					} else {
						if (num_rip < MDUINO_NUM_RIP_MAX) {
							num_rip++;
						} else {
							msg_inviato = true;
						}
					}

				} else {
					msg_da_non_inviare = true;
				}
			}
		}
	}

	private static ArrayList<Integer> estrai_dati_IO(String str, String char_sep) {

		ArrayList<Integer> result = new ArrayList<>();

		for (int i = 0; i < str.length(); i++) {

			if (str.charAt(i) == char_sep.charAt(0)) {

				result.add(Integer.parseInt(str.substring(i + 1, i + 3), 16));
				i = i + 2;

			} else {
				result.add(Integer.parseInt(Character.toString(str.charAt(i)), 16));
			}
		}

		return result;

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ThreadAggiornaPanels
	private static class ThreadAggiornaPanels extends Thread {

		@Override
		public void run() {

			while (true) {
				if (panels.get(0).isVisible()) {

					////////////////////////
					// PANNELLI VISIBILI ///
					////////////////////////
					/////////////////////////////////////////////////////////////////////// USCITE
					for (int j = 0; j < IO_Output.size(); j++) {

						for (int i = 0; i < devices.get(IO_Output.get(j).getDevice()).pos_output_dev.size(); i++) {

							if (devices.get(IO_Output.get(j).getDevice()).isEnaled()) {

								if (devices.get(IO_Output.get(j).getDevice()).pos_output_dev.get(i)
										.equals(IO_Output.get(j).getPosition())) {

									if (IO_Output.get(j).getDev_state()) {
										panels.get(IO_Output.get(j).getDevice()).statusOutput.get(i)
												.setForeground(Color.GREEN);
									} else {
										panels.get(IO_Output.get(j).getDevice()).statusOutput.get(i)
												.setForeground(Color.RED);
									}
									panels.get(IO_Output.get(j).getDevice()).statusOutput.get(i)
											.setText(Boolean.toString(IO_Output.get(j).getDev_state()));
								}
							}
						}

					}

					/////////////////////////////////////////////////////////////////// INGRESSI
					for (int j = 0; j < IO_Input.size(); j++) {

						for (int i = 0; i < devices.get(IO_Input.get(j).getDevice()).pos_input_dev.size(); i++) {

							if (devices.get(IO_Output.get(j).getDevice()).isEnaled()) {
								if (devices.get(IO_Input.get(j).getDevice()).pos_input_dev.get(i)
										.equals(IO_Input.get(j).getPosition())) {

									if (IO_Input.get(j).getDev_state()) {
										panels.get(IO_Input.get(j).getDevice()).statusInput.get(i)
												.setForeground(Color.GREEN);
									} else {
										panels.get(IO_Input.get(j).getDevice()).statusInput.get(i)
												.setForeground(Color.RED);
									}
									panels.get(IO_Input.get(j).getDevice()).statusInput.get(i)
											.setText(Boolean.toString(IO_Input.get(j).getDev_state()));
								}
							}
						}

					}

					try {
						ThreadAggiornaPanels.sleep(10);
					} catch (InterruptedException ex) {
					}

				} else {

					////////////////////////
					// PANNELLI VISIBILI ///
					////////////////////////
					try {
						ThreadAggiornaPanels.sleep(5000);
					} catch (InterruptedException ex) {
					}
				}
			}

		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// modificaIn
	public void GestoreIO_ModificaIn(String str_in, String ip) {
 
		int device_id = -1;
		// Individuazione Dispositivo
		for (int i = 0; i < devices.size(); i++) {

			if (devices.get(i).getIpAddress().equals(ip)) {
				device_id = i;
				break;
			}
		}

		if (device_id >= 0) {

			///////////////////////////////
			// PEERIFERICA INDIVIDUATA ///
			///////////////////////////////
			String stato_input = GestoreIO_DecodificaStringaIngressi(str_in);
			 
			for (int i = 0; i < stato_input.length(); i++) {
				for (int k = 0; k < IO_Input.size(); k++) {
					if (i == (IO_Input.get(k).getPosition() - 1) && IO_Input.get(k).getDevice() == device_id) {

						IO_Input.get(k).setDev_state(stato_input.charAt(i) == '1');

					}
				}
			}
		}

	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// decodificaStringaIngressi
	// Decodifica Stringa ricevuta da Mduino

	private static String GestoreIO_DecodificaStringaIngressi(String str) {
		String result = "";

		for (int i = 0; i < str.length(); i++) {

			char temp = str.charAt(i);

			switch (temp) {
			case '0':
				result += "0000";
				break;
			case '1':
				result += "0001";
				break;
			case '2':
				result += "0010";
				break;
			case '3':
				result += "0011";
				break;
			case '4':
				result += "0100";
				break;
			case '5':
				result += "0101";
				break;
			case '6':
				result += "0110";
				break;
			case '7':
				result += "0111";
				break;
			case '8':
				result += "1000";
				break;
			case '9':
				result += "1001";
				break;
			case 'A':
				result += "1010";
				break;
			case 'B':
				result += "1011";
				break;
			case 'C':
				result += "1100";
				break;
			case 'D':
				result += "1101";
				break;
			case 'E':
				result += "1110";
				break;
			case 'F':
				result += "1111";
				break;
			case 'G':
				result += "000";
				break;
			case 'H':
				result += "001";
				break;
			case 'I':
				result += "010";
				break;
			case 'J':
				result += "011";
				break;
			case 'K':
				result += "100";
				break;
			case 'L':
				result += "101";
				break;
			case 'M':
				result += "110";
				break;
			case 'N':
				result += "111";
				break;
			case 'O':
				result += "00";
				break;
			case 'P':
				result += "01";
				break;
			case 'Q':
				result += "10";
				break;
			case 'R':
				result += "11";
				break;
			case 'S':
				result += "0";
				break;
			case 'T':
				result += "1";
				break;

			default:

			}

		}

		return result;

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ottieniStatoIngresso
	public static boolean GestoreIO_OttieniStatoIngresso(Integer i) {

		boolean stato = IO_Input.get(i).getDev_state();

		return stato;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// ottieniVolreIngressoAnalogico
	public static String GestoreIO_OttieniValoreIngressoAnalogico(Integer id_input, Integer id_device) {

		return devices.get(id_device).clientMduino.send(MDUINO_CHAR_INPUT_ANALOGICO + id_input);
	

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// inviaComunicazioneRS485
	public static String GestoreIO_InviaComunicazioneRS485(String id_device, String message) {
		String rep = devices.get(Integer.parseInt(id_device)).clientMduino.send(MDUINO_CHAR_RS485 + message);
	
		return rep;

	}

	///////////////////////////
	// GETTERS AND SETTERS ///
	///////////////////////////
	public static ArrayList<IODevice> getDevices() {
		return devices;
	}

	public static void setDevices(ArrayList<IODevice> devices) {
		GestoreIO.devices = devices;
	}

	public static Boolean getVisPannelli() {

		return vis_pannelli;
	}

	// Chiusura socket di connessione sui dispositivi
	public static void GestoreIO_ChiudiEB80() throws IOException, CIPException {

		for (int i = 0; i < devices.size(); i++) {

			if (devices.get(i).getType() == 0) {

				devices.get(i).clientMduino.chiudiSocket();

			} else if (devices.get(i).getType() == 1) {

				devices.get(i).clientEB80.eipClient.UnRegisterSession();
				devices.get(i).clientEB80.eipClient.ForwardClose();
			}

			SessionLogger.logger.log(Level.INFO, "Chiudura del socket del dispositivo ={0}", devices.get(i).getName());
		}

	}
}
