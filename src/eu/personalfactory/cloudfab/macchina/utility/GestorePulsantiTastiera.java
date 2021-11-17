package eu.personalfactory.cloudfab.macchina.utility;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;

public class GestorePulsantiTastiera implements ActionListener {

    //VARIABILI
    private JTextField areaTesto;
  
    @Override
    public void actionPerformed(ActionEvent arg0) {
        
        JButton pressed = (JButton) arg0.getSource();
        String c = pressed.getName();
         

        if (areaTesto.getName().equals("NTYPE")) {
            if (c.equals("CANC")) {
                areaTesto.setText("0");
            } else {
                if (c.equals("BACK")) {
                    if (areaTesto.getText().length() == 1) {
                        areaTesto.setText("0");
                    } else {
                        areaTesto.setText(areaTesto.getText().substring(0, areaTesto.getText().length() - 1));
                    }

                } else {
                    if (!areaTesto.getText().equals("0")) {
                        if (areaTesto.getText().length()
                                < Integer.parseInt(ParametriGlobali.parametri.get(49))) {

                            areaTesto.setText(areaTesto.getText() + c);

                        }

                    } else {
                        areaTesto.setText(c);
                    }

                }
            }

        } else if (c.equals("CANC")) {
            areaTesto.setText("");
        } else {
            if (c.equals("BACK")) {
                if (areaTesto.getText().length() > 0) {
                    areaTesto.setText(areaTesto.getText().substring(0,
                            areaTesto.getText().length() - 1));
                }
            } else {

                areaTesto.setText(areaTesto.getText() + c);

            }
        }

    }

    public JTextField getAreaTesto() {
        return areaTesto;
    }

    public void setAreaTesto(JTextField areaTesto) {
        this.areaTesto = areaTesto;
    }
}
