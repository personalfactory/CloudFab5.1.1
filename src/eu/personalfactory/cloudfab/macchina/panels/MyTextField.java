package eu.personalfactory.cloudfab.macchina.panels;

import java.awt.Font;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MyTextField extends JTextField {

    //COSTRUTTORE
    MyTextField(Font f, int x, int y, int l, int a) {
        
        //Impostazione Dimensioni
        setBounds(x, y, l, a);
        
        //Impostazione Font
        setFont(f);
        
        //Impostazione Trasparenze
        setOpaque(false);
        
        //Impostazione bordi
        setBorder(null);
    }
}
