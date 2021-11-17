package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.panels.Pannello14_Configurazione_Parametri;
import eu.personalfactory.cloudfab.macchina.panels.Pannello06_SceltaDimContenitore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello32_Configurazione_Taratura_Microdosatore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello43_Processo_Pesatura_Manuale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello10_ScelteEffettuate;
import eu.personalfactory.cloudfab.macchina.panels.Pannello07_SceltaTipoContenitore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello28_Inventario;
import eu.personalfactory.cloudfab.macchina.panels.Pannello19_Spegnimento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello42_SceltaAdditivo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello33_SceltaScaricaValvola;
import eu.personalfactory.cloudfab.macchina.panels.Pannello34_Gestione_Materie_Prime;
import eu.personalfactory.cloudfab.macchina.panels.Pannello20_Assistenza;
import eu.personalfactory.cloudfab.macchina.panels.Pannello13_Configurazione_Generale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello31_Comando_Microdosatori;
import eu.personalfactory.cloudfab.macchina.panels.Pannello02_SceltaFiltro;
import eu.personalfactory.cloudfab.macchina.panels.Pannello01_SceltaProduzione;
import eu.personalfactory.cloudfab.macchina.panels.Pannello21_Ricerca;
import eu.personalfactory.cloudfab.macchina.panels.Pannello12_Aggiornamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello46_ErroriAggiornamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello05_SceltaNumMiscele;
import eu.personalfactory.cloudfab.macchina.panels.Pannello11_Processo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello30_ConfermaRecuperaCodice;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.panels.Pannello00_Principale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello04_SceltaTipoChimica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod;
import eu.personalfactory.cloudfab.macchina.panels.Pannello37_Pulizia;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_Ordini;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import eu.personalfactory.cloudfab.macchina.panels.Pannello22_Ricerca_Filtro_Generale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta;
import eu.personalfactory.cloudfab.macchina.panels.Pannello36_ScaricoSilosMicro;
import eu.personalfactory.cloudfab.macchina.panels.Pannello09_SceltaCambioComponenti;
import eu.personalfactory.cloudfab.macchina.panels.Pannello17_Verifiche;
import eu.personalfactory.cloudfab.macchina.panels.MyStepPanel;
import eu.personalfactory.cloudfab.macchina.panels.Pannello16_Controllo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello41_SceltaColore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello29_RecuperaCodice;
import eu.personalfactory.cloudfab.macchina.panels.Pannello08_SceltaCliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello27_Configurazione_MateriePrime;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_BUTTON_FRECCIA;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonAction implements ActionListener {

    //VARIABILI
    private final JPanel pannelloCorrente; 

    //COSTRUTTORE
    public ButtonAction(JPanel pannCorr) {
        pannelloCorrente = pannCorr; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         
        String nameButton = ((JButton) e.getSource()).getName();
        
        if (nameButton.equals(XML_BUTTON_FRECCIA)) { 
            
            if (pannelloCorrente instanceof Pannello17_Verifiche) {  
                ((Pannello17_Verifiche) pannelloCorrente).gestoreInterruzioneThread();
            } else if (pannelloCorrente instanceof Pannello12_Aggiornamento) {  
                ((Pannello12_Aggiornamento) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello20_Assistenza) {
                ((Pannello20_Assistenza) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello30_ConfermaRecuperaCodice) {
                ((Pannello30_ConfermaRecuperaCodice) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello13_Configurazione_Generale) {
                ((Pannello13_Configurazione_Generale) pannelloCorrente).gestoreScambioPannello(0);
            } else if (pannelloCorrente instanceof Pannello32_Configurazione_Taratura_Microdosatore) {
                ((Pannello32_Configurazione_Taratura_Microdosatore) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello34_Gestione_Materie_Prime) {
                ((Pannello34_Gestione_Materie_Prime) pannelloCorrente).gestoreScambioPannello(0);
            } else if (pannelloCorrente instanceof Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta) {
                ((Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello36_ScaricoSilosMicro) {
                ((Pannello36_ScaricoSilosMicro) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello38_Pulizia_Svuotamento) {
                ((Pannello38_Pulizia_Svuotamento) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello39_Pulizia_Automatica) {
                ((Pannello39_Pulizia_Automatica) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello40_Pulizia_Manuale) {
                ((Pannello40_Pulizia_Manuale) pannelloCorrente).gestoreScambioPannello();
            } else if (pannelloCorrente instanceof Pannello10_ScelteEffettuate) {
                ((Pannello10_ScelteEffettuate) pannelloCorrente).gestoreScambioPannello(0);
            }else{
                ((MyStepPanel) pannelloCorrente).cambioPanelDietro();
            } 
         
        }  else{
            
         if (pannelloCorrente instanceof Pannello00_Principale) { ((Pannello00_Principale) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello01_SceltaProduzione)      { ((Pannello01_SceltaProduzione) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello02_SceltaFiltro) { ((Pannello02_SceltaFiltro) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello03_Ordini) { ((Pannello03_Ordini) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello03_FiltroProdCod) { ((Pannello03_FiltroProdCod) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello05_SceltaNumMiscele) { ((Pannello05_SceltaNumMiscele) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello06_SceltaDimContenitore) { ((Pannello06_SceltaDimContenitore) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello07_SceltaTipoContenitore) { ((Pannello07_SceltaTipoContenitore) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello08_SceltaCliente) { ((Pannello08_SceltaCliente) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello09_SceltaCambioComponenti) { ((Pannello09_SceltaCambioComponenti) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello04_SceltaTipoChimica) { ((Pannello04_SceltaTipoChimica) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello10_ScelteEffettuate)  { ((Pannello10_ScelteEffettuate) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello11_Processo) { ((Pannello11_Processo) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello12_Aggiornamento) { ((Pannello12_Aggiornamento) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello44_Errori) { ((Pannello44_Errori) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello45_Dialog) { ((Pannello45_Dialog) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello13_Configurazione_Generale) { ((Pannello13_Configurazione_Generale) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello45_Dialog) { ((Pannello45_Dialog) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello14_Configurazione_Parametri) { ((Pannello14_Configurazione_Parametri) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello16_Controllo) { ((Pannello16_Controllo) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello17_Verifiche) { ((Pannello17_Verifiche) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello19_Spegnimento) { ((Pannello19_Spegnimento) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello20_Assistenza) { ((Pannello20_Assistenza) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello21_Ricerca) { ((Pannello21_Ricerca) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello46_ErroriAggiornamento) { ((Pannello46_ErroriAggiornamento) pannelloCorrente).gestorePulsanti(nameButton); }  
         else if (pannelloCorrente instanceof Pannello22_Ricerca_Filtro_Generale) { ((Pannello22_Ricerca_Filtro_Generale) pannelloCorrente).gestorePulsanti(nameButton); }  
         else if (pannelloCorrente instanceof Pannello27_Configurazione_MateriePrime) { ((Pannello27_Configurazione_MateriePrime) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello28_Inventario) { ((Pannello28_Inventario) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello29_RecuperaCodice) { ((Pannello29_RecuperaCodice) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello30_ConfermaRecuperaCodice) { ((Pannello30_ConfermaRecuperaCodice) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello31_Comando_Microdosatori) { ((Pannello31_Comando_Microdosatori) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello32_Configurazione_Taratura_Microdosatore) { ((Pannello32_Configurazione_Taratura_Microdosatore) pannelloCorrente).gestorePulsanti(nameButton); } 
         else if (pannelloCorrente instanceof Pannello33_SceltaScaricaValvola) { ((Pannello33_SceltaScaricaValvola) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello34_Gestione_Materie_Prime) { ((Pannello34_Gestione_Materie_Prime) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta) { ((Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello36_ScaricoSilosMicro) { ((Pannello36_ScaricoSilosMicro) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello37_Pulizia) { ((Pannello37_Pulizia) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello38_Pulizia_Svuotamento) { ((Pannello38_Pulizia_Svuotamento) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello39_Pulizia_Automatica) { ((Pannello39_Pulizia_Automatica) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello40_Pulizia_Manuale) { ((Pannello40_Pulizia_Manuale) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello41_SceltaColore) { ((Pannello41_SceltaColore) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello42_SceltaAdditivo) { ((Pannello42_SceltaAdditivo) pannelloCorrente).gestorePulsanti(nameButton); }
         else if (pannelloCorrente instanceof Pannello43_Processo_Pesatura_Manuale) { ((Pannello43_Processo_Pesatura_Manuale) pannelloCorrente).gestorePulsanti(nameButton); }
        } 
    }
    
}


