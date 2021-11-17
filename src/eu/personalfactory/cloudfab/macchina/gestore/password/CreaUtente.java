/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.gestore.password;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FONT;
import eu.personalfactory.cloudfab.macchina.utility.PropertyReader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author francescodigaudio
 */
public class CreaUtente extends JFrame {

    private static final long serialVersionUID = 1L;
    //Parametri
    private final String DEFAULT_DATABASE_PROP = "default.database";
    private final String DEFAULT_HOST_PROP = "default.host";
    private final String DEFAULT_ROOT_USER_PROP = "default.root.user";
    private final String DEFAULT_ROOT_PASSWORD_PROP = "default.root.password";
    private final String MESSAGE_BUTTON_PROP = "message.button";
    private final String MESSAGE_TITLE_PROP = "message.title";
    private final String MESSAGE_SUCCESS_PROP = "message.success";
    private final String MESSAGE_FAILURE_PROP = "message.failure";
    private final String IMAGE_PATH_PROP = "image.path";
    //Variabili
    private final String DEFAULT_DATABASE;
    private final String DEFAULT_HOST;
    private final String DEFAULT_ROOT_USER;
    private final String DEFAULT_ROOT_PASSWORD;
    private final String MESSAGE_BUTTON;
    private final String MESSAGE_TITLE;
    private final String MESSAGE_SUCCESS;
    private final String MESSAGE_FAILURE;
    private final String IMAGE_PATH;
    private MyLabel label00;

    //COSTRUTTORE
    public CreaUtente() {
        
         
        String fileProperties = "eu/personalfactory/cloudfab/macchina/gestore/password/creaUtente.properties";

        Properties prop = PropertyReader.loadProperties(fileProperties, new ClassLoader() {
        });


        DEFAULT_DATABASE = prop.getProperty(DEFAULT_DATABASE_PROP);
        DEFAULT_HOST = prop.getProperty(DEFAULT_HOST_PROP);
        DEFAULT_ROOT_USER = prop.getProperty(DEFAULT_ROOT_USER_PROP);
        DEFAULT_ROOT_PASSWORD = prop.getProperty(DEFAULT_ROOT_PASSWORD_PROP);
        MESSAGE_BUTTON = prop.getProperty(MESSAGE_BUTTON_PROP);
        MESSAGE_TITLE = prop.getProperty(MESSAGE_TITLE_PROP);
        MESSAGE_SUCCESS = prop.getProperty(MESSAGE_SUCCESS_PROP);
        MESSAGE_FAILURE = prop.getProperty(MESSAGE_FAILURE_PROP);
        IMAGE_PATH = prop.getProperty(IMAGE_PATH_PROP);

        setFrame();

        creaUtente();
         
    }
 
    //Impostazione Frame

    private void setFrame() {
          
        
        //Lettura Immagine 
        ImageIcon imgIco = new ImageIcon(ClassLoader.getSystemResource(IMAGE_PATH));
         
        label00 = new MyLabel(10, 90, 300, 18, "", this);

        //Titoli
        MyLabelTitle title00 = new MyLabelTitle(10, 10, 340, 24, MESSAGE_TITLE, this);

        //Pulsanti 
        MyButton button00 = new MyButton(0, 290, 60, MESSAGE_BUTTON, this);

        //Label su cui caricare l'immagine
        JLabel lab = new JLabel(imgIco);

        lab.setLayout(null);
          
        //Impostazione visibilità Label
        lab.setVisible(true);

        //Aggiunta al Frame
        add(lab);
           
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
         
        setVisible(true);
        
        repaint();
         
        
 
    }

    //Tentativo di Creazione Utente
    private void creaUtente() {

        GestorePassword.creaUtente(DEFAULT_ROOT_USER, DEFAULT_ROOT_PASSWORD, GestorePassword.userName(),
                GestorePassword.passWord(),
                DEFAULT_DATABASE,
                DEFAULT_HOST);

        if (GestorePassword.testConnessione(
                GestorePassword.userName(),
                GestorePassword.passWord(),
                DEFAULT_DATABASE,
                DEFAULT_HOST)) {

            label00.setForeground(Color.GREEN);
            label00.setText(MESSAGE_SUCCESS); 
        } else {
            label00.setForeground(Color.RED);
            label00.setText(MESSAGE_FAILURE);
        }
    }

    private void gestorePulsanti(String buttonName) {

        Runtime.getRuntime().exit(0);
    }

    @SuppressWarnings("serial")
    private class MyButton extends JButton {

        private int DIM_BUT_LARGHEZZA = 100;
        private int DIM_BUT_ALTEZZA = 60;

        public MyButton(int id, int x, int y, String txt, JFrame frame) {

            setBounds(x, y, DIM_BUT_LARGHEZZA, DIM_BUT_ALTEZZA);

            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            setName(Integer.toString(id));

            addActionListener(new ButtonAction());

            setText(txt);

            frame.add(this);

        }
    }

    @SuppressWarnings("serial")
    private class MyLabel extends JLabel {

        //Costruttore
        public MyLabel(int x, int y, int l, int a, String txt, JFrame frame) {

            setBounds(x, y, l, a);

            setText(txt);

            setFont(new Font(FONT, Font.ITALIC, a));
 
            frame.add(this);

            setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    @SuppressWarnings("serial")
    private class MyLabelTitle extends JLabel {
 

        //Costruttore
        public MyLabelTitle(int x, int y, int l, int f, String txt, JFrame frame) {

            setBounds(x, y, l, f);

            setText(txt);

            setFont(new Font(FONT, Font.BOLD, f));

            setForeground(Color.WHITE);

            frame.add(this);

            setHorizontalAlignment(SwingConstants.LEFT);
        }
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String nameButton = ((JButton) e.getSource()).getName();

            gestorePulsanti(nameButton);

        }
    }
}
