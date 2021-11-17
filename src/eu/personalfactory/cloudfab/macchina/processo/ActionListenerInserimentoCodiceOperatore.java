/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.panels.Pannello00_Principale;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author francescodigaudio
 *
 * Controllo Codici inseriti dall'Operatore
 */
public class ActionListenerInserimentoCodiceOperatore implements ActionListener {

    private final Pannello00_Principale pannello;

    //COSTRUTTORE
    public ActionListenerInserimentoCodiceOperatore(Pannello00_Principale pannello) {
        this.pannello = pannello;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
 
        pannello.controllaCodiceOperatore(ae.getActionCommand().toUpperCase());
    }
}
