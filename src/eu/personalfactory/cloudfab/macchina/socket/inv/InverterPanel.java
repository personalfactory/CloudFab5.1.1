/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.inv;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdk.jfr.Frequency;

/**
 *
 * @author francescodigaudio
 */
public class InverterPanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int WINDOW_L = 400;
	private final int WINDOW_H = 750;
	private final int FREQ_AGG_PANELLI = 5000;
	private int row_height_counter = 5;

	////////////////////////////////////////////////////////////
	private static Inverter_Gefran_BDI50 inverter;

	Container container;

	int x_pos;
	int y_pos;

	ArrayList<JLabel> titles;
	ArrayList<JLabel> labels;
	ArrayList<JLabel> status;
	ArrayList<JButton> buttons;
	ArrayList<Boolean> buttons_status;

	String[] LABELS_COMMAND_TEXT = { "FUNZIONE RUN FWD", "FUNZIONE RUN REV", "ABNORMAL EFO", "FUNZIONE RESET",
			"FUNZIONE JOG FORWARD", "FUNZIONE JOG REVERSE", "FUNZIONE ON/OFF S1", "FUNZIONE ON/OFF S2",
			"FUNZIONE ON/OFF S3", "FUNZIONE ON/OFF S4", "FUNZIONE ON/OFF S5", "FUNZIONE ON/OFF RELE' R1", "AGGIORNA" };

	String[] BUTTONS_TEXT = { "RUN/STOP FWD", "RUN/STOP REV", "EFO", "RESET", "JOG FWD", "JOG REV", "ON/OFF", "ON/OFF",
			"ON/OFF", "ON/OFF", "ON/OFF", "ON/OFF" };

	String[] LABELS_PARAM_TEXT = { "OPERTATION STATE", "DIRECTION STATE", "INVERTER OPERATION", "ABNORMALE STATE",
			"DATA SETTING ERROR", "ABNORMITY", "TERMINAL S1 STATUS", "TERMINAL S2 STATUS", "TERMINAL S3 STATUS",
			"TERMINAL S4 STATUS", "TERMINAL S5 STATUS", "TERMINAL S6 STATUS", "RELE R1 STATUS", "RELE R2 STATUS",
			"FREQUENCY COMMAND", "OUTPUT FREQ", "OUTPUT VOLT COMMAND", "DC VOLT COMMAND", "OUTPUT CURRENT",
			"OUTPUT POWER", "PID FEEDBACK", "PID INPUT", "TM2 AVI INPUT VAL", "TM2 ACI INPUT VAE", "BDI IDENTIF",
			"INVERTER TEMP", "RATIO INV/MOT CURR" };

	public InverterPanel(Inverter_Gefran_BDI50 inverter, int x_pos, int y_pos) {

		InverterPanel.inverter = inverter;

		this.x_pos = x_pos;
		this.y_pos = y_pos;

		createContainer();

		setAlwaysOnTop(true);
	}

	private void createContainer() {

		titles = new ArrayList<>();
		labels = new ArrayList<>();
		status = new ArrayList<>();
		buttons = new ArrayList<>();
		buttons_status = new ArrayList<>();

		Font font_m = new Font("Arial", Font.BOLD, 10);
		Font font_s = new Font("Arial", Font.ITALIC, 8);
		Font font_ss = new Font("Arial", Font.CENTER_BASELINE, 6);

		setTitle(inverter.getName() + " - " + inverter.getAddress() + " - " + inverter.getDeviceId());
		setLocation(x_pos, y_pos);
		setLayout(null);
		// setUndecorated(true);

		container = getContentPane();

		titles.add(new JLabel());

		titles.get(titles.size() - 1).setText("COMMAND");
		titles.get(titles.size() - 1).setBounds(10, row_height_counter, 200, 20);
		titles.get(titles.size() - 1).setFont(font_m.deriveFont(font_m.getStyle() | Font.BOLD));
		titles.get(titles.size() - 1).setVisible(true);

		container.add(titles.get(0));

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < LABELS_COMMAND_TEXT.length - 1; i++) {

			labels.add(new JLabel());
			// status.add(new JLabel());
			buttons.add(new JButton());
			buttons_status.add(false);

			row_height_counter += 22;

			buttons.get(buttons.size() - 1).setVisible(true);
			labels.get(labels.size() - 1).setText(LABELS_COMMAND_TEXT[i]);
			buttons.get(buttons.size() - 1).setText(BUTTONS_TEXT[i]);

			labels.get(labels.size() - 1).setBounds(10, row_height_counter, 600, 20);
			buttons.get(buttons.size() - 1).setBounds(WINDOW_L - 120, row_height_counter - 10, 90, 24);

			labels.get(labels.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.ITALIC));
			// status.get(status.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() |
			// Font.BOLD));
			buttons.get(buttons.size() - 1).setFont(font_ss.deriveFont(font_ss.getStyle() | Font.CENTER_BASELINE));

			buttons.get(buttons.size() - 1).addActionListener(new ButtonAction(i));

			container.add(labels.get(labels.size() - 1));
			// container.add(status.get(status.size() - 1));
			container.add(buttons.get(buttons.size() - 1));

		}

		row_height_counter += 22;

		titles.add(new JLabel());

		titles.get(titles.size() - 1).setText("PARAMETRI");
		titles.get(titles.size() - 1).setBounds(10, row_height_counter, 200, 20);
		titles.get(titles.size() - 1).setFont(font_m.deriveFont(font_m.getStyle() | Font.BOLD));
		titles.get(titles.size() - 1).setVisible(true);

		container.add(titles.get(0));

		buttons.add(new JButton());
		buttons_status.add(false);

		row_height_counter += 2;

		buttons.get(buttons.size() - 1).setVisible(true);
		buttons.get(buttons.size() - 1).setText(LABELS_COMMAND_TEXT[12]);
		buttons.get(buttons.size() - 1).setBounds(WINDOW_L - 120, row_height_counter - 10, 90, 24);
		buttons.get(buttons.size() - 1).setFont(font_ss.deriveFont(font_ss.getStyle() | Font.CENTER_BASELINE));
		buttons.get(buttons.size() - 1).addActionListener(new ButtonAction(buttons.size() - 1));

		container.add(buttons.get(buttons.size() - 1));

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		for (int i = 0; i < LABELS_PARAM_TEXT.length; i++) {

			labels.add(new JLabel());
			status.add(new JLabel());
			row_height_counter += 14;

			labels.get(labels.size() - 1).setText(LABELS_PARAM_TEXT[i]);

			labels.get(labels.size() - 1).setBounds(10, row_height_counter, 600, 20);
			status.get(status.size() - 1).setBounds(WINDOW_L - 140, row_height_counter, 300, 20);

			labels.get(labels.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.ITALIC));
			status.get(status.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.BOLD));

			container.add(labels.get(labels.size() - 1));
			container.add(status.get(status.size() - 1));
			container.add(buttons.get(buttons.size() - 1));

		}

//		new AggiornaPannello(this).start();

		setSize(WINDOW_L, WINDOW_H);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(false);
		repaint();

	}

	public class ButtonAction implements ActionListener {

		// VARIABILI
		// private String id_uscita;
		// private boolean stato = false;
		private int index;

		// COSTRUTTORE
		public ButtonAction(int index) { // , String id_uscita) {
			// this.id_uscita = id_uscita;
			this.index = index;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (index) {
			case 0:
				if (!buttons_status.get(index)) {

					inverter.avviaArrestaInverter(true);
					buttons_status.set(index, true);

				} else {

					inverter.avviaArrestaInverter(false);
					buttons_status.set(index, false);
				}
				break;
			case 1:
				if (!buttons_status.get(index)) {

					inverter.avviaArrestaInverter(true);
					buttons_status.set(index, true);

				} else {

					inverter.avviaArrestaInverter(false);
					buttons_status.set(index, false);
				}
				break;
			case 2:
				inverter.comandoAbnormalEFO();
				break;
			case 3:
				inverter.resetFault();
				break;
			case 4:
				inverter.jogForward();
				break;
			case 5:
				inverter.jogReverse(true);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				if (!buttons_status.get(index)) {
					inverter.multiFunzioneS(0 + index - 6, true);
					buttons_status.set(index, true);
				} else {

					inverter.multiFunzioneS(0 + index - 6, false);
					buttons_status.set(index, false);
				}
				break;
			case 11:
				if (!buttons_status.get(index)) {
					inverter.releR1(true);
					buttons_status.set(index, true);
				} else {

					inverter.releR1(false);
					buttons_status.set(index, false);
				}
				break;
			case 12:
				new AggiornaPannelloInvereter().start();
				break;
			default:
				break;
			}
		}
	}

	private class AggiornaPannelloInvereter extends Thread {

		@Override
		public void run() {
 
			//////////////////////////////////////////////////////////////////////////////////////////////////////////// LETTURA
			//////////////////////////////////////////////////////////////////////////////////////////////////////////// OPERATION
			//////////////////////////////////////////////////////////////////////////////////////////////////////////// STATE
			//////////////////////////////////////////////////////////////////////////////////////////////////////////// REGISTER

			//////////////////////////////////////////////////////////////////////////////////// OPERATION
			//////////////////////////////////////////////////////////////////////////////////// STATE
			inverter.leggiOperationState();

			status.get(0).setText(Boolean.toString(inverter.operation_state));
			status.get(1).setText(Boolean.toString(inverter.direction_state));
			status.get(2).setText(Boolean.toString(inverter.inverter_operation));
			status.get(3).setText(Boolean.toString(inverter.abnormal_state));
			status.get(4).setText(Boolean.toString(inverter.data_setting_error));

//			//////////////////////////////////////////////////////////////////////////////////// ABNORMITY
//			inverter.leggiAbnormity();
//			status.get(5).setText(inverter.abnormity);
//
//			//////////////////////////////////////////////////////////////////////////////////// TERMINAL
//			//////////////////////////////////////////////////////////////////////////////////// STATE
//			inverter.leggiTerminalReleState();
//			status.get(6).setText(Boolean.toString(inverter.terminal_s1_status_read));
//			status.get(7).setText(Boolean.toString(inverter.terminal_s2_status_read));
//			status.get(8).setText(Boolean.toString(inverter.terminal_s3_status_read));
//			status.get(9).setText(Boolean.toString(inverter.terminal_s4_status_read));
//			status.get(10).setText(Boolean.toString(inverter.terminal_s5_status_read));
//			status.get(11).setText(Boolean.toString(inverter.terminal_s6_status_read));
//			status.get(12).setText(Boolean.toString(inverter.rele_r1_status_read));
//			status.get(13).setText(Boolean.toString(inverter.rele_r2_status_read));
//
//			//////////////////////////////////////////////////////////////////////////////////// FREQUENCY
//			//////////////////////////////////////////////////////////////////////////////////// COMMAND
//			inverter.leggiFrequencyCommand();
//			status.get(14).setText(inverter.frequency);
//
//			//////////////////////////////////////////////////////////////////////////////////// OUTPUT
//			//////////////////////////////////////////////////////////////////////////////////// FREQUENCY
//			inverter.leggiOutputFrequency();
//			status.get(14).setText(inverter.output_frequency);
//
//			//////////////////////////////////////////////////////////////////////////////////// OUTPUT
//			//////////////////////////////////////////////////////////////////////////////////// FREQUENCY
//			inverter.leggiOutputVoltageCommand();
//			status.get(16).setText(inverter.output_voltage_command);
//
//			//////////////////////////////////////////////////////////////////////////////////// DC
//			//////////////////////////////////////////////////////////////////////////////////// VOLTAGE
//			//////////////////////////////////////////////////////////////////////////////////// COMMAND
//			inverter.leggiDcVoltageCommand();
//			status.get(17).setText(inverter.dc_voltage_command);
//
//			/////////////////////////////////////////////////////////////////////////////////// OUTPUT
//			/////////////////////////////////////////////////////////////////////////////////// CURRENT
//			inverter.leggiOutputCurrent();
//			status.get(18).setText(inverter.output_current);
//
//			//////////////////////////////////////////////////////////////////////////////////// OUTPUT
//			//////////////////////////////////////////////////////////////////////////////////// POWER
//			inverter.leggiOutputPower();
//			status.get(19).setText(inverter.output_power);
//
//			//////////////////////////////////////////////////////////////////////////////////// PID
//			//////////////////////////////////////////////////////////////////////////////////// FEEDBACK
//			inverter.leggiPIDFeedback();
//			status.get(20).setText(inverter.pid_feedback);
//
//			//////////////////////////////////////////////////////////////////////////////////// PID
//			//////////////////////////////////////////////////////////////////////////////////// INPUT
//			inverter.leggiPIDInput();
//			status.get(21).setText(inverter.pid_input);
//
//			//////////////////////////////////////////////////////////////////////////////////// TM2
//			//////////////////////////////////////////////////////////////////////////////////// AVI
//			//////////////////////////////////////////////////////////////////////////////////// INPUT
//			//////////////////////////////////////////////////////////////////////////////////// VALUE
//			inverter.leggiTM2AviInputValue();
//			status.get(22).setText(inverter.tm2_avi_input_value);
//
//			/////////////////////////////////////////////////////////////////////////////////// TM2
//			/////////////////////////////////////////////////////////////////////////////////// ACI
//			/////////////////////////////////////////////////////////////////////////////////// DC
//			/////////////////////////////////////////////////////////////////////////////////// VOLTAGE
//			inverter.leggiTM2AciInputValue();
//			status.get(23).setText(inverter.tm2_aci_input_value);
//
//			//////////////////////////////////////////////////////////////////////////////////// BDI
//			//////////////////////////////////////////////////////////////////////////////////// IDENTIFICATION
//			inverter.leggiBDIIdentification();
//			status.get(24).setText(inverter.bdi_identification);
//
//			//////////////////////////////////////////////////////////////////////////////////// INVERTER
//			//////////////////////////////////////////////////////////////////////////////////// TEMPERATURE
//			inverter.leggiInverterTemperature();
//			status.get(25).setText(inverter.inverter_temperature);
//
//			//////////////////////////////////////////////////////////////////////////////////// RATIO
//			//////////////////////////////////////////////////////////////////////////////////// INVERTER
//			//////////////////////////////////////////////////////////////////////////////////// MOTOR
//			//////////////////////////////////////////////////////////////////////////////////// CURRENT
//			inverter.leggiRatioInverterMotorCurrent();
//			status.get(26).setText(inverter.ratio_inverter_motor_current);
 

		}

	}

}
