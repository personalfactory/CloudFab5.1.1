/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello17_Verifiche;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_MANUALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_A;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina; 

/**
 *
 * @author francescodigaudio
 */
public class Controllo {

    public Pannello17_Verifiche pannelloControllo;
    public boolean[] esitoTest;
    public int numControlli = 30;
    public boolean interrompiControlloPulsantiBloccaSacco, avvio, arresto,
            testSuperato, testFallito, interrompiComunicazioneSchedaIO;
    public String stringaConfigurazione;
    public boolean vipaCommutaNettoBilanciaCarico, vipaCommutaLordoBilanciaCarico,
            vipaCommutaNettoBilanciaConfezioni, vipaCommutaLordoBilanciaConfezioni,
            vipaCommutaNettoBilanciaChimicaColori, vipaCommutaLordoBilanciaChimicaColori;
    public String pesoVecchiImpianti[];
    public boolean attivaByPass;
    public boolean commutaNetto;
    public boolean commutaLordo;
    public boolean avvioSoffiatori, arrestoSoffiatori;
    public boolean avvioVibroPneumatici, arrestoVibroPneumatici;
    public boolean avvioAttuatoreValvolaAspiratoreSilo;
    public boolean arrestoAttuatoreValvolaAspiratoreSilo;
    public int idPresa;
    public int idUscita;
    public Boolean stato_ev1, stato_ev2, stato_ev3, stato_ev4; //, idVibro;

    //COSTRUTTORE
    public Controllo(Pannello17_Verifiche pannelloControllo) {

        //Dichiarazione Pannello di Riferimento
        this.pannelloControllo = pannelloControllo; 

        //Inizializzazione Variabili
        esitoTest = new boolean[numControlli];

        //Inizializzazione Logger Procedure di Controllo
        ControlloLogger.init(); 

    }
 
    //Controllo Contatto Ribalta Sacco
    public void controlloContattoRibaltaSacco() {

        new ThreadControlloContattoRibaltaSacco(this).start();

    }

    //Controllo Contatti Valvole
    public void controlloContattiValvole() {

        new ThreadControlloContattiValvole(this).start();

    }

    //Controllo Contatti Valvole
    public void controlloElettrovalvolaFluidificatori() {

        new ThreadControlloElettrovalvolaFluidifica(this).start();

    }

    //Controllo Contatti Valvole
    public void controlloElettrovalvolaSvuotaTubo() {

        new ThreadControlloElettrovalvolaSvuotaTubo(this).start();

    }

    //Controllo Contatti Valvole
    public void controlloElettrovalvolaSvuotaValvola() {

        new ThreadControlloElettrovalvolaSvuotaValvola(this).start();

    }

    //Controllo Contatti Valvole
    public void controlloElettrovalvolaPulisciValvola() {

        new ThreadControlloElettrovalvolaPulisciValvola(this).start();

    }

    //Controllo Contatto Sportello
    public void controlloContattoSportello() {

        new ThreadControlloContattoSportello(this).start();

    }

    //Controllo Contatti Valvole
    public void controlloAspiratore() {

        new ThreadControlloAspiratore(this).start();

    }

    //Controllo Coclee Carico
    public void controlloCocleeCarico() {

        new ThreadControlloMotoriCoclee(this).start();

    }

    //Controllo Motore Miscelatore
    public void controlloMotoreMiscelatore() {

        new ThreadControlloMotoreMiscelatore(this).start();

    }

    //Controllo Motore Vibro Valvola
    public void controlloMotoreVibroValvola() {

        new ThreadControlloMotoreVibroValvola(this).start();

    }

    //Controllo Motore Vibro Base
    public void controlloScuotitori() {

        new ThreadControlloScuotitori(this).start();

    }

    //Controllo Selettore Vibro
    public void controlloSelettoreVibro() {

        new ThreadControlloSelettoreVibro(this).start();
    }

    //Controllo Selettore Valvola
    public void controlloSelettoreValvola() {

        new ThreadControlloSelettoreValvola(this).start();
    }

    //Controllo Pulsante Stop
    public void controlloPulsanteStop() {

        new ThreadControlloPulsanteStop(this).start();
    }

    //Controllo Pulsanti Blocca Sacco
    public void controlloPulsantiBloccaSacco() {

        new ThreadControlloPulsantiBloccaSacco(this).start();
    }

    //Controllo Motore Vibro Base
    public void controlloNastro() {

        new ThreadControlloNastro(this).start();

    }

    //Controllo Bilancia Carico
    public void controlloBilanciaCarico() {

        new ThreadControlloBilancia(this, ID_BILANCIA_CARICO, TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 478, ParametriSingolaMacchina.parametri.get(111))).start();

    }

    // Controllo Bilancia Confezioni
    public void controlloBilanciaConfezioni() {

        new ThreadControlloBilancia(this, ID_BILANCIA_CONFEZIONI, TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 479, ParametriSingolaMacchina.parametri.get(111))).start();

    }

    //Controllo Bilancia Colori
    public void controlloBilanciaColori() {

        new ThreadControlloBilancia(this, ID_BILANCIA_MANUALE, TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 480, ParametriSingolaMacchina.parametri.get(111))).start();

    }

    //Controllo Attuatore Valvola di Carico
    public void controlloAttuatoreValvolaCarico() {

        new ThreadControlloAttuatoreValvolaCarico(this).start();

    }

    //Controllo Attuatore Valvola di Scarico
    public void controlloAttuatoreValvolaScarico() {

        new ThreadControlloValvolaScarico(this).start();

    }

    //Controllo Attuatore Ribalta Sacco
    public void controlloAttuatoreRibaltaSacco() {

        new ThreadControlloAttuatoreRibaltaSacco(this).start();

    }

    //Controllo Attuatore Blocca Sacco
    public void controlloAttuatoreBloccaSacco() {

        new ThreadControlloAttuatoreBloccaSacco(this).start();

    }
    
       //Controllo Attuatore Blocca Sacco
    public void controlloSoffiatoriTramoggia() {

        new ThreadControlloSoffiatoriTramoggia(this).start();

    }
    
       //Controllo Vibro Tramoggia
    public void controlloVibroTramoggia() {

        new ThreadControlloVibroTramoggia(this).start();

    }
    
    //Controllo Segnalatore Luminoso Rosso
    public void controlloSeganalatoreRosso() {

        new ThreadControlloSegnalatoreRosso(this).start();

    }
    

    //Controllo Segnalatore Luminoso Giallo
    public void controlloSeganalatoreGiallo() {

        new ThreadControlloSegnalatoreGiallo(this).start();

    }
  
  
  
    public void aggiornaIdPresa() {

////        //Individuazione Uscita Coclea
////        idUscita = idPresa + Integer.parseInt(ParametriSingolaMacchina.parametri.get(58));
        
        ////Individuazione Uscita Coclea
        idUscita = idPresa + Integer.parseInt(USCITA_LOGICA_CONTATTORE_COCLEA_A);
        
////        
////
////        //Formattazione Stringa Uscita in Modo che Sia da 2 Caratteri
////        if (Integer.toString(idUscita).length() == 1) {
////
////            id = "0" + Integer.toString(idUscita);
////        } else {
////
////            id = Integer.toString(idUscita);
////
////        }
////
////        //Individuazione Uscita Vibratori
////        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(284))) {
////
////            //////////////////////////////////
////            // VIBRATORI SINGOLI PRESENTI  ///
////            //////////////////////////////////
////            int idUscitaVibro = idUscita
////                    + Integer.parseInt(ParametriSingolaMacchina.parametri.get(59))
////                    + Integer.parseInt(ParametriSingolaMacchina.parametri.get(248));
////
////            //Formattazione Stringa Uscita in Modo che Sia da 2 Caratteri
////            if (Integer.toString(idUscitaVibro).length() == 1) {
////
////                idVibro = "0" + Integer.toString(idUscitaVibro);
////            } else {
////
////                idVibro = Integer.toString(idUscitaVibro);
////
////            }
////        }

        //Impostazione Label Presa/Timer
        pannelloControllo.impostaLabelPresaTimer(TrovaPresaPerIdPresa(idPresa + 1));

    }
}
