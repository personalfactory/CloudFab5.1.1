/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;
 
import eu.personalfactory.cloudfab.macchina.panels.Pannello27_Configurazione_MateriePrime;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

/**
 *
 * @author francescodigaudio
 * 
 * Controllo Codici inseriti dall'Operatore
 */ 
public class ActionListenerInserimentoCodiceComponente implements ActionListener {
  
    private final Pannello27_Configurazione_MateriePrime pannello;

    //COSTRUTTORE
    public ActionListenerInserimentoCodiceComponente(Pannello27_Configurazione_MateriePrime pannello) {
        this.pannello = pannello;
    }
 
    @Override
    public void actionPerformed(ActionEvent ae) {

        if (!ae.getActionCommand().toUpperCase().equals("")) {
 
            if (!pannello.txtField.getText().equals("")) {
                pannello.registraCodiceComponente();
            }
            
            //Inizializzazione Testo Text Field
            pannello.txtField.setText("");

            //Scambio Pannello
            pannello.gestoreScambioPannello();

        }

    }
}
