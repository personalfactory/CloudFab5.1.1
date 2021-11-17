
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.inv;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_InviaComunicazioneRS485;
import eu.personalfactory.cloudfab.macchina.loggers.InverterLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneBinarioToInt;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneHextoBinario;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConversioneStringToHex;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUMERO_RIP_ESEGUI_COMANDO_RS485;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_ESEGUI_COMANDO_RS485;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;
 
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.SostituisciCarattereStringaPosizione;

/**
 *
 * @author francescodigaudio
 */
public class Inverter_Gefran_BDI50 {

	private String address;
	private String device_id;
	private String name;
	private int direction;

	///////////////////////////////
	// PARAMETERI GEFRAN BDI50 ///
	///////////////////////////////
	private final String CAR_INIZIO_STRINGA = ":";
//	private final String CAR_CR = "\\r";
	// private final String CAR_LF = "\\n";
	private final String PAR_READ = "03";
	private final String PAR_WRITE = "06";
	// private final String PAR_WRITE_MULTI = "10";

////    private final String COMMAND_REGISTER_2501H = "3701";
////    private final String COMMAND_REGISTER_2502H = "3702";
////    private final String OPERATION_STATE_REGISTER_2520H = "3732";
////    private final String ABNORMITY_DEF_2521H = "3733";
////    private final String TERMINAL_RELE_STATE_2522H = "3734";
////    private final String FREQUENCY_COMMAND_2523H = "3735";
////    private final String OUTPUT_FREQUENCY_2524H = "3736";
////    private final String OUTPUT_VOLTAGE_COMMAND_2525H = "3737";
////    private final String DC_VOLTAGE_COMMAND_2526H = "3738";
////    private final String OUTPUT_CURRENT_2527H = "3739";
////    private final String OUTPUT_POWER_2529H = "3741";
////    private final String PID_FEEDBACK_252AH = "3742";
////    private final String PID_INPUT_252BH = "3743";
////    private final String TM2_AVI_INPUT_VALUE_252CH = "3744";
////    private final String TM2_ACI_INPUT_VALUE_252DH = "3745";
////    private final String BDI_IDENTIFICATION_252FH = "3747";
////    private final String INVERTER_TEMPERATURE_2531H = "3749";
////    private final String RATIO_INVERTER_MOTOR_CURRENT_2532H = "3750";
	private final String COMMAND_REGISTER_2501H = "2501";
	private final String COMMAND_REGISTER_2502H = "2502";
	private final String OPERATION_STATE_REGISTER_2520H = "2520";
	private final String ABNORMITY_DEF_2521H = "2521";
	private final String TERMINAL_RELE_STATE_2522H = "2522";
	private final String FREQUENCY_COMMAND_2523H = "2523";
	private final String OUTPUT_FREQUENCY_2524H = "2524";
	private final String OUTPUT_VOLTAGE_COMMAND_2525H = "2525";
	private final String DC_VOLTAGE_COMMAND_2526H = "2526";
	private final String OUTPUT_CURRENT_2527H = "2527";
	private final String OUTPUT_POWER_2529H = "2529";
	private final String PID_FEEDBACK_252AH = "252A";
	private final String PID_INPUT_252BH = "252B";
	private final String TM2_AVI_INPUT_VALUE_252CH = "252C";
	private final String TM2_ACI_INPUT_VALUE_252DH = "252D";
	private final String BDI_IDENTIFICATION_252FH = "252F";
	private final String INVERTER_TEMPERATURE_2531H = "2531";
	private final String RATIO_INVERTER_MOTOR_CURRENT_2532H = "2532";

//	private final String RUN_VALUE = "1";
//	private final String STOP_VALUE = "0";
//	private final String REV_VALUE = "1";
//	private final String FWD_VALUE = "0";

	boolean temporanea_sviluppo_ASCII_RTU = false; /// (false ASCII - true RTU)
	boolean temporanea_sviluppo_conversione_to_HEX_ASCII = true; /// (false ASCII - true RTU)

	private final String[] ABNORMITY_MESSAGES = { "00 NORMAL", "01 OH - INVERTER OVER HEAT",
			"02 CO - OVER CURRENT AT STOP", "03 LV - UNDER VOLTAGE", "04 OV - OVER VOLTAGE", "05 RESERVED",
			"06 bb EXTERNAL BB", "07 CTE - CPU ERROR BY EXTERNAL SIGNAL", "08 PDER - PID FEEDBACK SIGNAL LOST",
			"09 EPR - EPROM ABNORMAL", "10 ATER - PARAMETERS AUTO MEASURE ERROR", "11 OL3 - OVER TORQUE",
			"12 OL2 - INVERTER OVER LOAD", "13 OL1 - MOTOR OVER LOAD", "14 EFO - EXTERNAL COMMUNICATION ERROR",
			"15 ES - EXTERNAL STOP", "16 LOC - PARAMETERS LOCKED", "17 RESERVED",
			"18 OC-C OVER CURRENT AT CONSTANT SPEED", "19 OC-A OVER CURRENT DURING ACCELERATING",
			"20 OC-D OVER CURRENT DURING DECELRATING", "21 OC-S OVER CURRENT AT STARTING TO RUN", "22 RESERVED",
			"23 LV-C UNDER VOLTAGE DURING RUNNING", "24 OV-C OVER VOLTAGE DURING DECELERATING",
			"25 OH-C OVER HEAT DURING RUNNING", "26 STP0 STOP AT 0 SPEED", "27 STP1 DIRECT START MALFUNCTION",
			"28 STP2 CONTROL PANEL EMERGENCY", "29 Err1 - KEYPAD OPERATION ERROR", "30 Err2 - PARAMETER SETTING ERROR",
			"31 Err4 - ANALOG TRANSFERRED ERROR", "32 Err5 - PARAMETER CHANGED DURING COMMUNICATION",
			"33 Err6 - COMMUNICATION FAILURE", "34 Err7 - PARAMETER SETTING ERROR", "35 Err8 - RESTORE FACTORY SETTING",
			"36 RESERVED", "37 RESERVED", "38 EPR1 - PARAMETERS COPY ERROR VIA COPY UNIT",
			"39 EPR2 - PARAMETERS COPY INCORRECT VIA COPY UNIT", "40 OVSP - INVERTER OVER SPEED",
			"41 PF - INPUT PHASE LOST", "42 HPERR - HORSEPOWER SETTING ERROR", "43 RESERVED",
			"44 OH4 - MOTOR TEMPERATURE OVERHEAT ERROR", "45 OH3 - MOTOR TEMPERATURE OVERHEAT ALARM",
			"46 CL OUTPUT CURRENT REACHES CURRENT LIMIT LEVEL" };

	public boolean command_run_stop = false;
	public boolean command_forward_reverse = false;
	public boolean command_abnormal_EFO = false;
	public boolean command_fault_reset = false;
	public boolean command_jog_forward = false;
	public boolean command_jog_reverse = false;
	public boolean command_multi_function_s1 = false;
	public boolean command_multi_function_s2 = false;
	public boolean command_multi_function_s3 = false;
	public boolean command_multi_function_s4 = false;
	public boolean command_multi_function_s5 = false;
	public boolean command_rele_r1 = false;

	public boolean operation_state = false;
	public boolean direction_state = false;
	public boolean inverter_operation = false;
	public boolean abnormal_state = false;
	public boolean data_setting_error = false;

	public String abnormity = "";
	public String frequency = "";
	public String output_frequency = "";
	public String output_voltage_command = "";
	public String dc_voltage_command = "";
	public String output_current = "";
	public String output_power = "";
	public String pid_feedback = "";
	public String pid_input = "";
	public String tm2_avi_input_value = "";
	public String tm2_aci_input_value = "";
	public String bdi_identification = "";
	public String inverter_temperature = "";
	public String ratio_inverter_motor_current = "";

	public boolean terminal_s1_status_read;
	public boolean terminal_s2_status_read;
	public boolean terminal_s3_status_read;
	public boolean terminal_s4_status_read;
	public boolean terminal_s5_status_read;
	public boolean terminal_s6_status_read;
	public boolean rele_r1_status_read;
	public boolean rele_r2_status_read;

	// Costruttore
	public Inverter_Gefran_BDI50(String name, String address, String device_id, String direction) {
		this.address = Integer.toHexString(Integer.parseInt(address)).toUpperCase();
		this.device_id = device_id;
		this.name = name;
		this.direction = Integer.parseInt(direction);

		while (this.address.length() < 2) {

			this.address = "0" + this.address;

		}

		InverterLogger.logger.log(Level.INFO, "Inverter = {0}" + "\nIndirizzo = {1}" + "\nPeriferica di Comando = {2}",
				new Object[] { name, address, device_id });

	}

	// Calcola Stringa Avvio Arresto Inverte
	// 0 FWD RUN
	// 1 REV RUN
	public String avviaArrestaInverter(Boolean value) {

		String to_send = "";

		if (value) {

			if (direction == 0) {

				// FWD
				to_send = "0000000000000001";

			} else if (direction == 1) {

				// REV
				to_send = "0000000000000011";
			}
		} else {

			to_send = "0000000000000000";
		}

		String res = eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));

		return res;

	}

	// Calcola Stringa Avvio Arresto Inverte
	// 1 FWD RUN
	// 0 REV RUN
	public String avviaArrestaInverterRotazioneInversa(Boolean value) {

		String to_send = "";

		if (value) {

			if (direction == 1) {
				// FWD
				to_send = "0000000000000001";

			} else if (direction == 0) {
				// REV
				to_send = "0000000000000011";
			}
		} else {

			to_send = "0000000000000000";
		}

		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));

	}

//	// Calcola Stringa Imposta Senso di Marcia
//	public String avviaArrestaInverter_REV(Boolean value) {
//
//		String to_send = "";
//
//		if (value) {
//			to_send = "0000000000000011";
//		} else {
//
//			to_send = "0000000000000000";
//		}
//
//		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
//				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));
//	}
//
//	// Calcola Stringa Avvio Arresto Inverter
//	public String avviaArrestaInverter_FWD(Boolean value) {
//
//		String to_send = "";
//
//		if (value) {
//			to_send = "0000000000000001";
//		} else {
//
//			to_send = "0000000000000000";
//		}
//
//		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
//				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));
//
//	}

	// Calcola Stringa Imposta Comando Abnormal EFO
	public String comandoAbnormalEFO() {

		String to_send = "0000000000000100";

		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));
	}

	// Calcola Stringa Reset Stato di Fault
	public String resetFault() {

		String to_send = "0000000000001000";

		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));
	}

	// Calcola Stringa Jog Forward
	public String jogForward() {

		String to_send = "0000000000010000";
		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));
	}

	// Calcola Stringa Jog Reverse
	public String jogReverse(Boolean value) {

		String to_send = "0000000000100000";

		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(to_send)), 4)));
	}

	// Calcola Stringa multi funzione S1 S2 S3 S4 S5
	public String multiFunzioneS(Integer index, Boolean value) {

		// String actual_command_register = leggiCommandRegister();
		String actual_command_register = "0000000000000000";

		if (value) {
			actual_command_register = SostituisciCarattereStringaPosizione(actual_command_register, "1", 9 - index);
		} else {
			actual_command_register = SostituisciCarattereStringaPosizione(actual_command_register, "0", 9 - index);
		}
		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(actual_command_register)), 4)));
	}

	// Calcola Stringa rele R1
	public String releR1(Boolean value) {

		// String actual_command_register = leggiCommandRegister();

		String actual_command_register = "0000100000000000";

		if (value) {
			actual_command_register = SostituisciCarattereStringaPosizione(actual_command_register, "1", 3);
		} else {
			actual_command_register = SostituisciCarattereStringaPosizione(actual_command_register, "0", 3);
		}

		return eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2501H, PAR_WRITE,
				ConversioneStringToHex(Integer.toString(ConversioneBinarioToInt(actual_command_register)), 4)));

	}

	// Cambio Vel Inverter
	public String cambiaVelInverter(String value) {

		value = ConversioneStringToHex(value, 4);

		String res = eseguiComando(costrusciMessaggioASCII(address, COMMAND_REGISTER_2502H, PAR_WRITE, value));

		return res;

	}

	public String eseguiComando(String msg_to_send) {
		String rep = "";
		
		if (!ABILITA_SIMULAZIONE_PROCESSO) {
			for (int i = 0; i < NUMERO_RIP_ESEGUI_COMANDO_RS485; i++) {
				try { 
					rep = GestoreIO_InviaComunicazioneRS485(device_id, msg_to_send);
					
					 
					Thread.sleep(FREQUENZA_ESEGUI_COMANDO_RS485);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return rep;

	}

	public String letturaParametro(String parametro_hex, String data_q_int) {

		return eseguiComando(
				costrusciMessaggioASCII(address, parametro_hex, PAR_READ, ConversioneStringToHex(data_q_int, 4)));

	}

	// Lettura stato command Register
//	public String leggiCommandRegister() {
//
////		String rep = letturaParametro(COMMAND_REGISTER_2501H, "1");
////
////		// String rep = eseguiComando(":0A0325230001");
////		// IMPLEMENTARE DECODIFICA RISPOSTA INVERTER
////		System.out.println("dato ricevuto command register=" + rep);
//
////        if (!"".equals(rep) && rep.length() > 13) {
////
////            rep = rep.substring(rep.indexOf(":"), rep.length());
////            rep = rep.substring(7, 11);
////
////            System.out.println("command rep1 =" + rep);
////            rep = ConversioneHextoBinario(rep, 4);
////            System.out.println("command rep2 =" + rep);
////
////            Boolean[] buffer_rec = new Boolean[16];
////
////            for (int i = 0; i < rep.length(); i++) {
////                buffer_rec[i] = rep.charAt(rep.length() - 1 - i) == '1';
////            }
////
////            try {
////
////                command_run_stop = buffer_rec[0];
////                command_forward_reverse = buffer_rec[1];
////                command_abnormal_EFO = buffer_rec[2];
////                command_fault_reset = buffer_rec[3];
////                command_jog_forward = buffer_rec[4];
////                command_jog_reverse = buffer_rec[5];
////                command_multi_function_s1 = buffer_rec[6];
////                command_multi_function_s2 = buffer_rec[7];
////                command_multi_function_s3 = buffer_rec[8];
////                command_multi_function_s4 = buffer_rec[9];
////                command_multi_function_s5 = buffer_rec[10];
////                command_rele_r1 = buffer_rec[12];
////
////            } catch (Exception e) {
////
////                InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del Command Register - e: {0}", e);
////            }
////        }
////		return rep;
//	}

	// Lettura stato operativo inverter
	public void leggiOperationState() {

		String rep = letturaParametro(OPERATION_STATE_REGISTER_2520H, "1");

		//System.out.println("Operation STate =" + rep);
		Boolean[] buffer_rec = new Boolean[16];
		rep = rep.replace(":", "");

		String function = rep.substring(2, 4);

		//System.out.println("† =" + function);

		if (!"".equals(rep) && rep.length() > 12 && function.equals("03")) {

			rep = rep.substring(6, 10);
			//System.out.println("Operation STate1 =" + rep);

			rep = ConversioneHextoBinario(rep, 4, 1);
			//System.out.println("Operation STate2 =" + rep);
			for (int i = 0; i < rep.length(); i++) {
				buffer_rec[i] = rep.charAt(rep.length() - 1 - i) == '1';
			}

			try {
				operation_state = buffer_rec[0];
				direction_state = buffer_rec[1];
				inverter_operation = buffer_rec[2];
				abnormal_state = buffer_rec[3];
				data_setting_error = buffer_rec[4];

				InverterLogger.logger.info("OPERATION STATE\n\n" + "operation_state =" + operation_state + "\n"
						+ "direction_state =" + direction_state + "\n" + "inverter_operation =" + inverter_operation
						+ "\n" + "abnormal_state =" + abnormal_state + "\n" + "data_setting_error ="
						+ data_setting_error);

			} catch (Exception e) {

				InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura dell Operation State - e: {0}", e);

			}
		}

	}

	// Lettura definizione abnormity
	public void leggiAbnormity() {

		String rep = letturaParametro(ABNORMITY_DEF_2521H, "0001");

		//System.out.println("REP =" + rep);

		if (!"".equals(rep) && rep.length() > 13) {

			try {

				rep = rep.substring(7, 11);

				int rep_id = ConversioneBinarioToInt(ConversioneHextoBinario(rep, 2, 1));

				//System.out.println("REP_ID" + rep_id);

				if (rep_id == 0) {
					abnormity = "00 - NORMAL";
				} else if (rep_id == 1) {
					abnormity = "01 - OH";
				} else if (rep_id == 2) {
					abnormity = "02 - OC";
				} else if (rep_id == 3) {
					abnormity = "03 - LV";
				} else if (rep_id == 4) {
					abnormity = "04 - OV";
				} else if (rep_id == 5) {
					abnormity = "05 - RES";
				} else if (rep_id == 6) {
					abnormity = "06 - bb";
				} else if (rep_id == 7) {
					abnormity = "07 - CTE";
				} else if (rep_id == 8) {
					abnormity = "08 - PDER";
				} else if (rep_id == 9) {
					abnormity = "09 - EPR";
				} else if (rep_id == 10) {
					abnormity = "10 - ATER";
				} else if (rep_id == 11) {
					abnormity = "11 - OL3";
				} else if (rep_id == 12) {
					abnormity = "12 - OL2";
				} else if (rep_id == 13) {
					abnormity = "13 - OL1";
				} else if (rep_id == 14) {
					abnormity = "14 - EFO";
				} else if (rep_id == 15) {
					abnormity = "15 - E.S";
				} else if (rep_id == 16) {
					abnormity = "16 - LOC";
				} else if (rep_id == 17) {
					abnormity = "17 - RES";
				} else if (rep_id == 18) {
					abnormity = "18 - OC-C";
				} else if (rep_id == 19) {
					abnormity = "19 - OC-A";
				} else if (rep_id == 20) {
					abnormity = "20 - OC-D";
				} else if (rep_id == 21) {
					abnormity = "21 - OC-S";
				} else if (rep_id == 22) {
					abnormity = "22 - RES";
				} else if (rep_id == 13) {
					abnormity = "23 - LV-C";
				} else if (rep_id == 14) {
					abnormity = "24 - OV-C";
				} else if (rep_id == 15) {
					abnormity = "25 - OC-C";
				} else if (rep_id == 16) {
					abnormity = "26 - STP0";
				} else if (rep_id == 17) {
					abnormity = "27 - STP1";
				} else if (rep_id == 18) {
					abnormity = "28 - STP2";
				} else if (rep_id == 19) {
					abnormity = "29 - Err1";
				} else if (rep_id == 20) {
					abnormity = "30 - Err2";
				} else if (rep_id == 21) {
					abnormity = "31 - Err4";
				} else if (rep_id == 22) {
					abnormity = "32 - Err5";
				} else if (rep_id == 19) {
					abnormity = "33 - Err6";
				} else if (rep_id == 20) {
					abnormity = "34 - Err7";
				} else if (rep_id == 21) {
					abnormity = "35 - Err8";
				} else if (rep_id == 22) {
					abnormity = "36 - RES";
				} else if (rep_id == 19) {
					abnormity = "37 - RES";
				} else if (rep_id == 20) {
					abnormity = "38 - EPR1";
				} else if (rep_id == 21) {
					abnormity = "39 - EPR2";
				} else if (rep_id == 22) {
					abnormity = "40 - OVSP";
				} else if (rep_id == 19) {
					abnormity = "41 - PF";
				} else if (rep_id == 20) {
					abnormity = "42 - HPERR";
				} else if (rep_id == 21) {
					abnormity = "43 - RES";
				} else if (rep_id == 22) {
					abnormity = "44 - OH4";
				} else if (rep_id == 21) {
					abnormity = "45 - OH3";
				} else if (rep_id == 22) {
					abnormity = "46 - CL";
				}

				if (rep_id < ABNORMITY_MESSAGES.length) {
					InverterLogger.logger
							.info("ABNORMITY\n\n" + "rep_id =" + abnormity + "\n" + ABNORMITY_MESSAGES[rep_id]);
				}
			} catch (Exception e) {

				InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura dell Operation State - e: {0}", e);

			}
		}

	}

	// Lettura stato terminal Rele
	public void leggiTerminalReleState() {

		String rep = letturaParametro(TERMINAL_RELE_STATE_2522H, "0001");

		if (rep.length() > 8) {
			rep = rep.substring(6, 8);
			rep = ConversioneHextoBinario(rep, 4, 1);

		}

		Boolean[] buffer_rec = new Boolean[8];
		if (!"".equals(rep)) {
			for (int i = 0; i < rep.length(); i++) {
				buffer_rec[i] = rep.charAt(rep.length() - 1 - i) == '1';
			}
			try {
				terminal_s1_status_read = buffer_rec[0];
				terminal_s2_status_read = buffer_rec[1];
				terminal_s3_status_read = buffer_rec[2];
				terminal_s4_status_read = buffer_rec[3];
				terminal_s5_status_read = buffer_rec[4];
				terminal_s6_status_read = buffer_rec[5];
				rele_r1_status_read = buffer_rec[6];
				rele_r2_status_read = buffer_rec[7];

				InverterLogger.logger.info("TERMINAL STATE\n\n" + "terminal_s1_status_read =" + terminal_s1_status_read
						+ "\n" + "terminal_s2_status_read =" + terminal_s2_status_read + "\n"
						+ "terminal_s3_status_read =" + terminal_s3_status_read + "\n" + "terminal_s4_status_read ="
						+ terminal_s4_status_read + "\n" + "terminal_s5_status_read =" + terminal_s5_status_read + "\n"
						+ "terminal_s6_status_read =" + terminal_s6_status_read + "\n" + "rele_r1_status_read ="
						+ rele_r1_status_read + "\n" + "rele_r2_status_read =" + rele_r2_status_read);

			} catch (Exception e) {
				InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del Terminals State - e: {0}", e);
			}
		}
	}

	// Lettura registro 2523H Frequency command (100/1 Hz)
	public void leggiFrequencyCommand() {
		String rep = letturaParametro(FREQUENCY_COMMAND_2523H, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				frequency = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("FREQUENCY COMMAND\n\n" + "frequency =" + frequency);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del Frequency Command- e: {0}", e);
		}

	}

	// Lettura registro 2524H Output Frequency (100/1 Hz)
	public void leggiOutputFrequency() {
		String rep = letturaParametro(OUTPUT_FREQUENCY_2524H, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				output_frequency = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("OUTPUT FREQUENCY\n\n" + "output_frequency =" + output_frequency);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del Output Frequency - e: {0}", e);
		}

	}

	// Lettura registro 2525H Output Voltage Command (10/1 V)
	public void leggiOutputVoltageCommand() {
		String rep = letturaParametro(OUTPUT_VOLTAGE_COMMAND_2525H, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				output_voltage_command = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger
						.info("OUTPUT VOLTAGE COMMAND\n\n" + "output_voltage_command =" + output_voltage_command);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura dell'Output Voltage Command - e: {0}",
					e);
		}
	}

	// Lettura registro 2526H DC Voltage Command (1/1 V)
	public void leggiDcVoltageCommand() {
		String rep = letturaParametro(DC_VOLTAGE_COMMAND_2526H, "0001");
		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				dc_voltage_command = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("DC VOLTAGE COMMAND\n\n" + "dc_voltage_command =" + dc_voltage_command);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del DC Voltage Command - e: {0}", e);
		}

	}

	// Lettura registro 2527H OutputCurrent (10/1 A)
	public void leggiOutputCurrent() {
		String rep = letturaParametro(OUTPUT_CURRENT_2527H, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				output_current = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("OUTPUT CURRENT\n\n" + "output_current =" + output_current);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del Output Current - e: {0}", e);
		}
	}

	// Lettura registro 2529H Output Power (10/1 kW)
	public void leggiOutputPower() {
		String rep = letturaParametro(OUTPUT_POWER_2529H, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				output_power = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("OUTPUT POWER\n\n" + "output_power =" + output_power);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del Output Power - e: {0}", e);
		}
	}

	// Lettura registro 252AH PID Feedback (100%/fmax, 10/1%)
	public void leggiPIDFeedback() {

		String rep = letturaParametro(PID_FEEDBACK_252AH, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				pid_feedback = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("PID FEEDBACK\n\n" + "pid_feedback =" + pid_feedback);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del PID Feedback - e: {0}", e);
		}
	}

	// Lettura registro 252BH PID Input (100%/fmax, 10/1%)
	public void leggiPIDInput() {
		String rep = letturaParametro(PID_INPUT_252BH, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				pid_feedback = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("PID INPUT\n\n" + "pid_input =" + pid_input);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del PID Input - e: {0}", e);
		}

	}

	// Lettura registro 252CH TM2 AVI input Value (1000/10V)*1
	public void leggiTM2AviInputValue() {
		String rep = letturaParametro(TM2_AVI_INPUT_VALUE_252CH, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				tm2_avi_input_value = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("TM2 AVI INPUT\n\n" + "tm2_avi_input_value =" + tm2_avi_input_value);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del TM2 AVI INPUT - e: {0}", e);
		}

	}

	// Lettura registro 252DH TM2 ACI input Value (1000/10V)*1
	public void leggiTM2AciInputValue() {
		String rep = letturaParametro(TM2_ACI_INPUT_VALUE_252DH, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				tm2_aci_input_value = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("TM2 ACI INPUT\n\n" + "tm2_aci_input_value =" + tm2_aci_input_value);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del TM2 ACI INPUT - e: {0}", e);
		}
	}

	// Lettura registro 252FH BDI Identification 0x0110
	public void leggiBDIIdentification() {
		String rep = letturaParametro(BDI_IDENTIFICATION_252FH, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				bdi_identification = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("BDI IDENTIFICATION\n\n" + "bdi_identification =" + bdi_identification);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura del BDI IDENTIFICATION - e: {0}", e);
		}
	}

	// Lettura registro 2531H Inverter Temperature (10°/C) temperature of heat sink
	// or IGBT
	public void leggiInverterTemperature() {
		String rep = letturaParametro(INVERTER_TEMPERATURE_2531H, "0001");

		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				inverter_temperature = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger
						.info("INVERTER TEMPERATURE\n\n" + "inverter_temperature =" + inverter_temperature);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura dell'Inverter Temperature - e: {0}", e);
		}
	}

	// Lettura registro 2532H Ratio of inverter and motor rated current (%)
	public void leggiRatioInverterMotorCurrent() {
		String rep = letturaParametro(RATIO_INVERTER_MOTOR_CURRENT_2532H, "0001");
		try {
			if (rep.length() > 8) {

				rep = rep.substring(6, 8);
				rep = ConversioneHextoBinario(rep, 4, 1);

				ratio_inverter_motor_current = Integer.toString(ConversioneBinarioToInt(rep));

				InverterLogger.logger.info("RATIO INVERTER MOTOR CURRENT\n\n" + "ratio_inverter_motor_current ="
						+ ratio_inverter_motor_current);

			}

		} catch (Exception e) {
			InverterLogger.logger.log(Level.SEVERE, "Errore Durante la lettura Ratio Inverter Motor Current - e: {0}",
					e);
		}
	}

	///////////////
	// PRIVATE ///
	///////////////
	// Calcola Longitudinal Redundancy Check
//	private String calcolaLRC_Old(String par, String value, String carReadWrite) {
//
//		///////////////////////////////////////
//		// CALCOLO LRC, MODIFICA Marzo2016 ///
//		///////////////////////////////////////
//		// Conversione Stringhe Parametri in Esadecimale
//		String indInverter_HEX = ConversioneStringToHex(address, 2);
//		String carReadWrite_HEX = ConversioneStringToHex(carReadWrite, 2);
//		String parA_HEX = ConversioneStringToHex(par.substring(0, 2), 2);
//		String parB_HEX = ConversioneStringToHex(par.substring(2, par.length()), 2);
//		String value_HEX = ConversioneStringToHex(value, 4);
//
//		// Conversione Stringhe Parametri in Decimale
//		int indInverter_DEC = Integer.parseInt(address);
//		int carReadWrite_DEC = Integer.parseInt(carReadWrite);
//		int parA_DEC = Integer.parseInt(par.substring(0, 2));
//		int parB_DEC = Integer.parseInt(par.substring(2, par.length()));
//
//		int value_DEC = Integer.parseInt(value);
//
//		// Fattore Correttivo per il Calcolo del LRC
//		int correttivo = 0;
//
//		for (int i = 0; i < 256; i++) {
//			if (value_DEC > (65535 - 256 * i)) {
//				correttivo = 65280 - 255 * i;
//				break;
//			} else if (value_DEC > (65251 - 255 * i)) {
//				correttivo = 65281 - 255 * i;
//				break;
//			}
//		}
//
//		// Calcolo Somma Modulo 256;
//		int sommaModulo256 = (int) (indInverter_DEC + carReadWrite_DEC + parA_DEC + parB_DEC + value_DEC - correttivo)
//				% 256;
//		// Calcolo Complemento a 2 negativo della somma
//		int complemento2NegativoSomma = 256 - sommaModulo256;
//		// Calcolo LRC
//		String LRC = ConversioneStringToHex(Integer.toString(complemento2NegativoSomma), 2);
//
//		InverterLogger.logger.log(Level.CONFIG,
//				"DECIMALI : indInverter_DEC ={0}"
//						+ " - carWrite_DEC ={1} - parA_DEC ={2} - parB_DEC ={3} - value_DEC ={4}",
//				new Object[] { indInverter_DEC, carReadWrite_DEC, parA_DEC, parB_DEC, value_DEC });
//
//		InverterLogger.logger.log(Level.CONFIG,
//				"ESADECIMALI: indInverter_HEX ={0} "
//						+ "- carWrite_HEX ={1} - parA_HEX ={2} - parB_HEX ={3} - value_HEX ={4}",
//				new Object[] { indInverter_HEX, carReadWrite_HEX, parA_HEX, parB_HEX, value_HEX });
//
//		InverterLogger.logger.log(Level.CONFIG,
//				"CALCOLO LRC: SommaModulo256 ={0}" + " - Complemento2NegativoSomma ={1} - LRC.toUpperCase() ={2}",
//				new Object[] { sommaModulo256, complemento2NegativoSomma, LRC.toUpperCase() });
//
//		return indInverter_HEX + carReadWrite_HEX + parA_HEX + parB_HEX + value_HEX + LRC.toUpperCase();
//
//	}

////    //Determina la Stringa da Trasmettere Secondo la Codifica Gefran
////    private String codificaStringa(String par, String value, String car_read_write) {
////        String result = "";
////        try {
////            //Memorizzazione File di Log Inverter
////            InverterLogger.logger.log(Level.CONFIG, "Parametro da Codificare ={0} - Valore ={1} - car_read_write ={2}",
////                    new Object[]{par, value, car_read_write});
////
////            if (temporanea_sviluppo_ASCII_RTU) {
////
////                result = costrusciMessaggioRTU(address, par, car_read_write, value);
////
////            } else {
////                result = costrusciMessaggioASCII(address, par, car_read_write, value);
////            }
////
////        } catch (Exception e) {
////
////            //Memorizzazione File di Log Inverter
////            InverterLogger.logger.log(Level.SEVERE, "Errore durante la codifica stringa inverter = {0}", e);
////        }
////        //Memorizzazione File di Log Inverter
////        InverterLogger.logger.log(Level.INFO, "Stringa Codificata = {0}", result);
////
////        return result;
////
////    }
	public String costrusciMessaggioASCII(String inv_address, String reg, String par_read_write, String data) {

		String result = CAR_INIZIO_STRINGA + inv_address + par_read_write + reg + data
				+ CalcoloLRC(inv_address + par_read_write + reg + data);

//        if (temporanea_sviluppo_conversione_to_HEX_ASCII) {
//            String temp = "";
//            for (int i = 0; i < result.length(); i++) {
//
//                temp += ConversioneStringToHex(Byte.toString((byte) result.charAt(i)), 2);
//               // temp += Byte.toString((byte) result.charAt(i));
//            }
//
//            result = temp;
//
//        }
 
		return result;

	}

	public String costrusciMessaggioRTU(String inv_address, String reg, String par_read_write, String data) {

		String res = inv_address + par_read_write + reg + data + calcoloCRC(inv_address + par_read_write + reg + data);

		return res;

	}

	///////////////////////////
	// GETTERS AND SETTERS ///
	///////////////////////////
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeviceId() {
		return device_id;
	}

	public void setDeviceId(String device_id) {
		this.device_id = device_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String calcoloCRC(String data) {

		String TRUE_BYTE = "1111111111111111";
		String XOR_CONST = "1010000000000001";

		String crc_hex_A = "";
		String crc_hex_B = "";
		String temp = "";

		for (int i = 0; i < data.length() / 2; i++) {

			String data_hex = data.substring(0 + i * 2, 2 + i * 2);

			// System.out.println("Esadecimale =" + data_hex);
			String data_bin = ConversioneHextoBinario(data_hex, 16, 2);
			// System.out.println("Binario =" + data_bin);

			String XOR = "";
			if (temp.equals("")) {
				XOR = XOR_CALC(TRUE_BYTE, data_bin);
			} else {
				XOR = XOR_CALC(temp, data_bin);
			}

			// System.out.println("XOR =" + XOR);
			// String SHIFT_XOR_1 = XOR.substring(XOR.length()-1, XOR.length())+
			// XOR.substring(0, XOR.length()-1);
			String SHIFT_XOR_1 = SHIFT_XOR_CALC(XOR, XOR_CONST);

			String SHIFT_XOR_2 = SHIFT_XOR_CALC(SHIFT_XOR_1, XOR_CONST);

			String SHIFT_XOR_3 = SHIFT_XOR_CALC(SHIFT_XOR_2, XOR_CONST);

			String SHIFT_XOR_4 = SHIFT_XOR_CALC(SHIFT_XOR_3, XOR_CONST);
			String SHIFT_XOR_5 = SHIFT_XOR_CALC(SHIFT_XOR_4, XOR_CONST);
			String SHIFT_XOR_6 = SHIFT_XOR_CALC(SHIFT_XOR_5, XOR_CONST);
			String SHIFT_XOR_7 = SHIFT_XOR_CALC(SHIFT_XOR_6, XOR_CONST);
			String SHIFT_XOR_8 = SHIFT_XOR_CALC(SHIFT_XOR_7, XOR_CONST);
			// System.out.println("SHIFT_XOR_8 =" + SHIFT_XOR_8);

			crc_hex_A = CloudFab5_0.ConversioneBinarioToHex(SHIFT_XOR_8.substring(8, SHIFT_XOR_8.length()));
			crc_hex_B = CloudFab5_0.ConversioneBinarioToHex(SHIFT_XOR_8.substring(0, 8));

			// System.out.println(crc_hex_A + crc_hex_B);
			temp = SHIFT_XOR_8;
			// System.out.println("\n\n");

		}

		return crc_hex_A + crc_hex_B;

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

		// System.out.println(result);
		return result;

	}

	public static String CalcoloLRC(String str) {

		int[] data = new int[str.length()];

		for (int i = 0; i < str.length() / 2; i++) {

			data[i] = Integer.parseInt(str.substring(0 + 2 * i, 2 + 2 * i), 16);
		}
		int somma = 0;
		for (int i = 0; i < data.length; i++) {

			somma += data[i];

		}

		String s1 = ConversioneHextoBinario(ConversioneStringToHex(Integer.toString(somma), 4), 8, 2);

		String compl = "";

		for (int i = 0; i < s1.length(); i++) {

			if (s1.charAt(i) == '1') {

				compl += '0';
			} else {
				compl += '1';
			}
		}

		String ret = Integer.toHexString(CloudFab5_0.ConversioneBinarioToInt(compl) + 1).toUpperCase();

		if (ret.length() > 2) {
			ret = ret.substring(ret.length() - 2, ret.length());
		}

		return ret;

	}

}
