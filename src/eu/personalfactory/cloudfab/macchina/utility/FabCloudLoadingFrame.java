package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_03;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_04;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_05;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_06;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_07;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_08;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_09;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_13;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_14;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_17;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_20;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_21;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_22;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_23;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_24;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_25;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_26;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_27;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_29;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_30;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_31;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_32;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_33;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_34;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.System.exit;
import javax.swing.JButton;
import javax.swing.JProgressBar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author francescodigaudio
 */
@SuppressWarnings("serial")
public class FabCloudLoadingFrame extends JFrame {

    //Variabili
    private final ImageIcon imgIco;
    private final String ver;
    JLabel lab,
            lab_versione,
            lab_ver_mysl,
            lab_db_par,
            lab_panels,
            lab_logfiles,
            lab_internet,
            lab_ip,
            lab_new_release,
            lab_restore_da_usb,
            lab_restore_da_file,
            lab_backup_su_usb,
            lab_socket_bil_carico,
            lab_socket_bil_conf,
            lab_socket_bil_chimica,
            lab_socket_bil_carico_aux,
            lab_com_mduino_main_panel,
            lab_com_mduino_silos_panel,
            lab_com_mduino_silos_panel_aux,
            lab_misura_pressione,
            lab_inverter_mixer,
            lab_inverter_screws,
            lab_eb80_main,
            lab_eb80_silos,
            lab_eb80_silos_aux;

    JProgressBar progress_bar;

    private final int TEXT_SIZE = 13;
    private final int ROW_SIZE = 14;

    //COSTRUTTORE
    public FabCloudLoadingFrame(ImageIcon imgIco, String ver) {

        this.imgIco = imgIco;
        this.ver = ver;

        //Impostazione Frame
        setFrame();
    }

    //Impostazione Frame
    private void setFrame() {

        JButton pulsanteExit = new JButton(LOADING_IMG_TEXT_31);
        pulsanteExit.setVisible(true);
        pulsanteExit.setBounds(390, 110, 100, 40);
        pulsanteExit.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        pulsanteExit.setForeground(Color.BLACK);
        pulsanteExit.addActionListener(new ExitButton());

        //Label su cui caricare l'immagine
        lab = new JLabel(imgIco);
        lab.setHorizontalAlignment(SwingConstants.LEFT);
        lab.setVerticalAlignment(SwingConstants.TOP);
        lab.setBounds(0, 0, imgIco.getIconWidth(), imgIco.getIconHeight());
        lab.setVisible(true);

        //Label Versione
        lab_versione = new JLabel(ver);
        lab_versione.setVisible(true);
        lab_versione.setBounds(40, 80, 200, 18);
        lab_versione.setFont(new Font(FONT, Font.PLAIN, 12));
        lab_versione.setVisible(true);

        //Label Verifica Connessione Mysql
        lab_ver_mysl = new JLabel(LOADING_IMG_TEXT_03);
        lab_ver_mysl.setVisible(true);
        lab_ver_mysl.setBounds(28, 120 + ROW_SIZE * 0, 600, 18);
        lab_ver_mysl.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_ver_mysl.setForeground(Color.WHITE);
        lab_ver_mysl.setVisible(false);

        //Label Verifica Caricamento Parametri Db
        lab_db_par = new JLabel(LOADING_IMG_TEXT_04);
        lab_db_par.setVisible(true);
        lab_db_par.setBounds(28, 120 + ROW_SIZE * 1, 600, 18);
        lab_db_par.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_db_par.setForeground(Color.WHITE);
        lab_db_par.setVisible(false);

        //Label Verifica Caricamento Pannelli
        lab_panels = new JLabel(LOADING_IMG_TEXT_05);
        lab_panels.setVisible(true);
        lab_panels.setBounds(28, 120 + ROW_SIZE * 2, 600, 18);
        lab_panels.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_panels.setForeground(Color.WHITE);
        lab_panels.setVisible(false);

        //Label Verifica Caricamento LogFiles
        lab_logfiles = new JLabel(LOADING_IMG_TEXT_06);
        lab_logfiles.setVisible(true);
        lab_logfiles.setBounds(28, 120 + ROW_SIZE * 3, 600, 18);
        lab_logfiles.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_logfiles.setForeground(Color.WHITE);
        lab_logfiles.setVisible(false);

        //Label Verifica Ip
        lab_ip = new JLabel(LOADING_IMG_TEXT_07);
        lab_ip.setVisible(true);
        lab_ip.setBounds(28, 120 + ROW_SIZE * 4, 600, 18);
        lab_ip.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_ip.setForeground(Color.WHITE);
        lab_ip.setVisible(false);

        //Label Verifica Connessione Internet
        lab_internet = new JLabel(LOADING_IMG_TEXT_08);
        lab_internet.setVisible(true);
        lab_internet.setBounds(28, 120 + ROW_SIZE * 5, 600, 18);
        lab_internet.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_internet.setForeground(Color.WHITE);
        lab_internet.setVisible(false);

        //Label Verifica nuova Release
        lab_new_release = new JLabel(LOADING_IMG_TEXT_09);
        lab_new_release.setVisible(true);
        lab_new_release.setBounds(28, 120 + ROW_SIZE * 6, 600, 18);
        lab_new_release.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_new_release.setForeground(Color.WHITE);
        lab_new_release.setVisible(false);

        //Label restore da usb
        lab_restore_da_usb = new JLabel(LOADING_IMG_TEXT_13);
        lab_restore_da_usb.setVisible(true);
        lab_restore_da_usb.setBounds(28, 120 + ROW_SIZE * 7, 600, 18);
        lab_restore_da_usb.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_restore_da_usb.setForeground(Color.WHITE);
        lab_restore_da_usb.setVisible(false);

        //Label restore da file
        lab_restore_da_file = new JLabel(LOADING_IMG_TEXT_14);
        lab_restore_da_file.setVisible(true);
        lab_restore_da_file.setBounds(28, 120 + ROW_SIZE * 8, 600, 18);
        lab_restore_da_file.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_restore_da_file.setForeground(Color.WHITE);
        lab_restore_da_file.setVisible(false);

        //Label Backup su usb
        lab_backup_su_usb = new JLabel(LOADING_IMG_TEXT_17);
        lab_backup_su_usb.setVisible(true);
        lab_backup_su_usb.setBounds(28, 120 + ROW_SIZE * 9, 600, 18);
        lab_backup_su_usb.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_backup_su_usb.setForeground(Color.WHITE);
        lab_backup_su_usb.setVisible(false);

        //Label Creazione Socket Bilancia Carico
        lab_socket_bil_carico = new JLabel(LOADING_IMG_TEXT_20);
        lab_socket_bil_carico.setVisible(true);
        lab_socket_bil_carico.setBounds(28, 120 + ROW_SIZE * 10, 600, 18);
        lab_socket_bil_carico.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_socket_bil_carico.setForeground(Color.WHITE);
        lab_socket_bil_carico.setVisible(false);

        //Label Creazione Socket Bilancia Confezionamento
        lab_socket_bil_conf = new JLabel(LOADING_IMG_TEXT_21);
        lab_socket_bil_conf.setVisible(true);
        lab_socket_bil_conf.setBounds(28, 120 + ROW_SIZE * 11, 600, 18);
        lab_socket_bil_conf.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_socket_bil_conf.setForeground(Color.WHITE);
        lab_socket_bil_conf.setVisible(false);

        //Label Crezione Socket Bilancia Chimica
        lab_socket_bil_chimica = new JLabel(LOADING_IMG_TEXT_22);
        lab_socket_bil_chimica.setVisible(true);
        lab_socket_bil_chimica.setBounds(28, 120 + ROW_SIZE * 12, 600, 18);
        lab_socket_bil_chimica.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_socket_bil_chimica.setForeground(Color.WHITE);
        lab_socket_bil_chimica.setVisible(false);

        //Label Creazione Socket Bilancia Carico Ausiliaria
        lab_socket_bil_carico_aux = new JLabel(LOADING_IMG_TEXT_23);
        lab_socket_bil_carico_aux.setVisible(true);
        lab_socket_bil_carico_aux.setBounds(28, 120 + ROW_SIZE * 13, 600, 18);
        lab_socket_bil_carico_aux.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_socket_bil_carico_aux.setForeground(Color.WHITE);
        lab_socket_bil_carico_aux.setVisible(false);

        //Label Verifica Contatto Retroazione IO MDuino Main Panel
        lab_com_mduino_main_panel = new JLabel(LOADING_IMG_TEXT_24);
        lab_com_mduino_main_panel.setVisible(true);
        lab_com_mduino_main_panel.setBounds(28, 120 + ROW_SIZE * 14, 600, 18);
        lab_com_mduino_main_panel.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_com_mduino_main_panel.setForeground(Color.WHITE);
        lab_com_mduino_main_panel.setVisible(false);

        //Label Verifica Contatto Retroazione IO MDuino Silos Panel
        lab_com_mduino_silos_panel = new JLabel(LOADING_IMG_TEXT_25);
        lab_com_mduino_silos_panel.setVisible(true);
        lab_com_mduino_silos_panel.setBounds(28, 120 + ROW_SIZE * 15, 600, 18);
        lab_com_mduino_silos_panel.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_com_mduino_silos_panel.setForeground(Color.WHITE);
        lab_com_mduino_silos_panel.setVisible(false);

        //Label Verifica Contatto Retroazione IO MDuino Silos Panel Aux
        lab_com_mduino_silos_panel_aux = new JLabel(LOADING_IMG_TEXT_26);
        lab_com_mduino_silos_panel_aux.setVisible(true);
        lab_com_mduino_silos_panel_aux.setBounds(28, 120 + ROW_SIZE * 16, 600, 18);
        lab_com_mduino_silos_panel_aux.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_com_mduino_silos_panel_aux.setForeground(Color.WHITE);
        lab_com_mduino_silos_panel_aux.setVisible(false);

        //Label Verifica Pressione Impianto Pneumatico
        lab_misura_pressione = new JLabel(LOADING_IMG_TEXT_27);
        lab_misura_pressione.setVisible(true);
        lab_misura_pressione.setBounds(28, 120 + ROW_SIZE * 17, 600, 18);
        lab_misura_pressione.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_misura_pressione.setForeground(Color.WHITE);
        lab_misura_pressione.setVisible(false);

        //Label Verifica Pressione Impianto Pneumatico
        lab_inverter_mixer = new JLabel(LOADING_IMG_TEXT_29);
        lab_inverter_mixer.setVisible(true);
        lab_inverter_mixer.setBounds(28, 120 + ROW_SIZE * 18, 600, 18);
        lab_inverter_mixer.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_inverter_mixer.setForeground(Color.WHITE);
        lab_inverter_mixer.setVisible(false);

        //Label Verifica Pressione Impianto Pneumatico
        lab_inverter_screws = new JLabel(LOADING_IMG_TEXT_30);
        lab_inverter_screws.setVisible(true);
        lab_inverter_screws.setBounds(28, 120 + ROW_SIZE * 19, 600, 18);
        lab_inverter_screws.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_inverter_screws.setForeground(Color.WHITE);
        lab_inverter_screws.setVisible(false);

        
         //Label Verifica EB80 Main Panel
        lab_eb80_main = new JLabel(LOADING_IMG_TEXT_32);
        lab_eb80_main.setVisible(true);
        lab_eb80_main.setBounds(28, 120 + ROW_SIZE * 20, 600, 18);
        lab_eb80_main.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_eb80_main.setForeground(Color.WHITE);
        lab_eb80_main.setVisible(false);
        
         //Label Verifica EB80 Silos Panel
        lab_eb80_silos = new JLabel(LOADING_IMG_TEXT_33);
        lab_eb80_silos.setVisible(true);
        lab_eb80_silos.setBounds(28, 120 + ROW_SIZE * 21, 600, 18);
        lab_eb80_silos.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_eb80_silos.setForeground(Color.WHITE);
        lab_eb80_silos.setVisible(false);
        
         //Label Verifica EB80 Silos Panel Aux
        lab_eb80_silos_aux = new JLabel(LOADING_IMG_TEXT_34);
        lab_eb80_silos_aux.setVisible(true);
        lab_eb80_silos_aux.setBounds(28, 120 + ROW_SIZE * 22, 600, 18);
        lab_eb80_silos_aux.setFont(new Font(FONT, Font.ITALIC, TEXT_SIZE));
        lab_eb80_silos_aux.setForeground(Color.WHITE);
        lab_eb80_silos_aux.setVisible(false);
        
        
        
        progress_bar = new JProgressBar();
        progress_bar.setBounds(0, 100, 496, 5);
        progress_bar.setForeground(Color.GREEN);

        add(lab_versione);
        add(lab_ver_mysl);
        add(lab_db_par);
        add(lab_panels);
        add(lab_logfiles);
        add(lab_ip);
        add(lab_internet);
        add(lab_new_release);
        add(lab_restore_da_usb);
        add(lab_backup_su_usb);
        add(lab_restore_da_file);
        add(lab_socket_bil_carico);
        add(lab_socket_bil_conf);
        add(lab_socket_bil_chimica);
        add(lab_socket_bil_carico_aux);
        add(lab_com_mduino_main_panel);
        add(lab_com_mduino_silos_panel);
        add(lab_com_mduino_silos_panel_aux);
        add(lab_misura_pressione);
        add(lab_inverter_mixer);
        add(lab_inverter_screws);
        add(lab_eb80_main);
        add(lab_eb80_silos);
        add(lab_eb80_silos_aux);

        add(pulsanteExit);

        add(progress_bar);
        add(lab);

        //Impostazione Look & Feel
        setDefaultLookAndFeelDecorated(false);

        //Dimensionamento Frame
        setSize(imgIco.getIconWidth(), imgIco.getIconHeight());

        //Impostazione Operazione alla Chiusura
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Centraggio Finestra
        setLocationRelativeTo(null);

        //Disabilitazione Decorazioni finestra
        setUndecorated(true);

        //Impostazione Ordinamento Frame
        setAlwaysOnTop(true);

        //Impostazione Visibilità Frame
        setVisible(true);

    }

    private class ExitButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            exit(0);
        }

    }
}
