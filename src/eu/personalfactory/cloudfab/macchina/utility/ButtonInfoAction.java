package eu.personalfactory.cloudfab.macchina.utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import eu.personalfactory.cloudfab.macchina.panels.Tastiera;

public class ButtonInfoAction implements ActionListener {

    private final JLabel[] labels;
    private final Tastiera tastiera;

    //COSTRUTTORE
    public ButtonInfoAction(JLabel[] labels, Tastiera tastiera) {

        this.labels = labels;
        this.tastiera = tastiera;
    }

    //Impostazione Visibilità Label
    public void impostaVisibilita(boolean vis) {

        for (int i = 0; i < labels.length; i++) {

            labels[i].setVisible(vis);
        }

        tastiera.impostaVisibilitaTastiera(!vis);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         
        if (tastiera.getVisibilitaTastiera()) {

            impostaVisibilita(true);
        } else {
            impostaVisibilita(false);
        }

    }
}
