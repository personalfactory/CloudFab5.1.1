/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;
 
import eu.personalfactory.cloudfab.macchina.panels.Pannello43_Processo_Pesatura_Manuale;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 

/**
 *
 * @author francescodigaudio
 * 
 * Controllo Codici Componenti Pesatura Manuale
 */ 
public class ActionListenerControlloCodiceChimicaSfusaPesaturaManuale implements ActionListener {
  
    private final Pannello43_Processo_Pesatura_Manuale pannelloPesaturaManuale;

    //COSTRUTTORE
    public ActionListenerControlloCodiceChimicaSfusaPesaturaManuale(Pannello43_Processo_Pesatura_Manuale pannelloPesaturaManuale) {
        this.pannelloPesaturaManuale = pannelloPesaturaManuale;
    } 
    @Override
    public void actionPerformed(ActionEvent ae) {

        if (!ae.getActionCommand().toUpperCase().equals("")) { 
            pannelloPesaturaManuale.verificaCodiceChimica();
              
        } 
    }
}
