/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.io;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEVICE_PANEL_TEXT_00;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEVICE_PANEL_TEXT_01;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author francescodigaudio
 */
public class IODevicePanel extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IODevice device;
    ArrayList<IO> IO_Output;
    ArrayList<IO> IO_Input;
    int row_height_counter = 5;
    int X_POS, Y_POS;
    ArrayList<JLabel> titles;
    ArrayList<JLabel> labelsOutput;
    ArrayList<JLabel> statusOutput;
    ArrayList<JLabel> labelsInput;
    ArrayList<JLabel> statusInput;
    ArrayList<JButton> buttonStatus;

    Container container;

    private final int WINDOW_L = 400;

    public IODevicePanel(IODevice device, ArrayList<IO> IO_Output, ArrayList<IO> IO_Input, int X_POS, int Y_POS) {
        this.device = device;
        this.IO_Output = IO_Output;
        this.IO_Input = IO_Input;
        this.X_POS = X_POS;
        this.Y_POS = Y_POS;

        createContainer();
        
        setAlwaysOnTop(true);
    }

    private void createContainer() {

        titles = new ArrayList<>();
        labelsOutput = new ArrayList<>();
        statusOutput = new ArrayList<>();
        labelsInput = new ArrayList<>();
        statusInput = new ArrayList<>();

        buttonStatus = new ArrayList<>();

        Font font_m = new Font("Arial", Font.BOLD, 10);
        Font font_s = new Font("Arial", Font.ITALIC, 8);
        Font font_ss = new Font("Arial", Font.CENTER_BASELINE, 6);

        setTitle(device.getName() + " - " + device.getIpAddress() + " - " + device.getPort() + " - " + device.getType());
        setLocation(X_POS, Y_POS);
        setLayout(null);
        //setUndecorated(true);

        container = getContentPane();

        titles.add(new JLabel());
        titles.get(0).setText(DEVICE_PANEL_TEXT_00);
        titles.get(0).setBounds(10, row_height_counter, 200, 20);
        titles.get(0).setFont(font_m.deriveFont(font_m.getStyle() | Font.BOLD));
        titles.get(0).setVisible(true);

        container.add(titles.get(0));

        for (int j = 0; j < IO_Output.size(); j++) {

            if (IO_Output.get(j).getDevice().equals(device.getId())) {

                row_height_counter += 22;

                labelsOutput.add(new JLabel());
                statusOutput.add(new JLabel());
                buttonStatus.add(new JButton());

                labelsOutput.get(labelsOutput.size() - 1).setVisible(true);
                statusOutput.get(statusOutput.size() - 1).setVisible(true);
                buttonStatus.get(buttonStatus.size() - 1).setVisible(true);

                labelsOutput.get(labelsOutput.size() - 1).setText(
                        IO_Output.get(j).getPosition() + " - "
                        + IO_Output.get(j).getName());

                buttonStatus.get(buttonStatus.size() - 1).setText("I");

                if (IO_Output.get(j).getDev_state()) {
                    statusOutput.get(statusOutput.size() - 1).setForeground(Color.GREEN);
                } else {
                    statusOutput.get(statusOutput.size() - 1).setForeground(Color.RED);
                }
                statusOutput.get(statusOutput.size() - 1).setText(Boolean.toString(IO_Output.get(j).getDev_state()));

                labelsOutput.get(labelsOutput.size() - 1).setBounds(10, row_height_counter, 600, 20);
                statusOutput.get(statusOutput.size() - 1).setBounds(WINDOW_L - 60, row_height_counter, 600, 20);
                buttonStatus.get(buttonStatus.size() - 1).setBounds(WINDOW_L - 120, row_height_counter, 40, 20);

                labelsOutput.get(labelsOutput.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.ITALIC));
                statusOutput.get(statusOutput.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.BOLD));
                buttonStatus.get(buttonStatus.size() - 1).setFont(font_ss.deriveFont(font_ss.getStyle() | Font.CENTER_BASELINE));
                
                buttonStatus.get(buttonStatus.size() - 1).addActionListener(new ButtonAction((buttonStatus.size() - 1), Integer.toString(j)));

                container.add(labelsOutput.get(labelsOutput.size() - 1));
                container.add(statusOutput.get(statusOutput.size() - 1));
                container.add(buttonStatus.get(buttonStatus.size() - 1));

            }

        }
        row_height_counter += 30;
        titles.add(new JLabel());
        titles.get(1).setText(DEVICE_PANEL_TEXT_01);
        titles.get(1).setBounds(10, row_height_counter, 600, 20);
        titles.get(1).setVisible(true);
        titles.get(1).setFont(font_m.deriveFont(font_m.getStyle() | Font.BOLD));
        container.add(titles.get(1));

        for (int j = 0; j < IO_Input.size(); j++) {

            if (IO_Input.get(j).getDevice().equals(device.getId())) {

                row_height_counter += 22;

                labelsInput.add(new JLabel());
                statusInput.add(new JLabel());

                labelsInput.get(labelsInput.size() - 1).setVisible(true);
                statusInput.get(statusInput.size() - 1).setVisible(true);

                labelsInput.get(labelsInput.size() - 1).setText(
                        IO_Input.get(j).getPosition() + " - "
                        + IO_Input.get(j).getName());

                if (IO_Input.get(j).getDev_state()) {

                    statusInput.get(statusInput.size() - 1).setForeground(Color.GREEN);
                } else {
                    statusInput.get(statusInput.size() - 1).setForeground(Color.RED);
                }
                statusInput.get(statusInput.size() - 1).setText(Boolean.toString(IO_Input.get(j).getDev_state()));

                labelsInput.get(labelsInput.size() - 1).setBounds(10, row_height_counter, 600, 20);
                statusInput.get(statusInput.size() - 1).setBounds(WINDOW_L - 60, row_height_counter, 600, 20);

                labelsInput.get(labelsInput.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.ITALIC));
                statusInput.get(labelsInput.size() - 1).setFont(font_s.deriveFont(font_s.getStyle() | Font.BOLD));

                container.add(labelsInput.get(labelsInput.size() - 1));
                container.add(statusInput.get(statusInput.size() - 1));
            }

        }

        setSize(WINDOW_L, row_height_counter + 60);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        repaint();

    }

    public class ButtonAction implements ActionListener {

        //VARIABILI
        private String id_uscita;
        private boolean stato = false;
        private int index;

        //COSTRUTTORE
        public ButtonAction(int index, String id_uscita) {
            this.id_uscita = id_uscita;
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

             if (stato) {
                stato = false;
                GestoreIO.GestoreIO_ModificaOut(id_uscita, OUTPUT_FALSE_CHAR);
                buttonStatus.get(index).setText("I");
            } else {

                stato = true;

                GestoreIO.GestoreIO_ModificaOut(id_uscita, OUTPUT_TRUE_CHAR);
                buttonStatus.get(index).setText("O");
            }

        }
    }

}
