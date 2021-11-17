/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.MyStepPanel;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_A;

import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author francescodigaudio
 *
 * Questa Classe contiene i metodi e e gli oggetti necessari all'esecuzione del
 * processo di realizzazione di un prodotto con Origami
 *
 */
public class Pulizia {

    public MyStepPanel pannelloPulizia; 
    public String pesoVecchiImpianti[];
    public String valorePesaCarico, valorePesaConfezioni;  
    public ArrayList<Integer> aliqScarico;
    public boolean esitoTestStatoContatti[];
    public int indexValvolaCarico, pesoDaRaggiungereConfezione;
    public String stringaConfigurazione, pesoSacco;
    public boolean puliziaCompleta, bloccoSacchettoSingolo, interruzioneCocleaVuota,
            azzeraCarico, pesoRaggiunto, interrompiControlloCocleaVuota,
            interrompiComunicazioneSchedaIO, pesoConfezioneRaggiunto;
    public boolean svuotamentoPulizia; 
    public double pesoMancanteConfezioni;

    public boolean puliziaInterrottaManualmente;

    public DettagliPulizia dettagliPulizia;
    public int pesoSaccoSvuotamento;

    public String ultimoPesoBilanciaSacchetti;
    public String ultimoPesoBilanciaCarico;
     
    public boolean pos_20_impostata,  pos_53_impostata; 

    //COSTRUTTORE
    public Pulizia(Pannello38_Pulizia_Svuotamento pannello) {

        //Dichiarazione Pannello di Riferimento
        this.pannelloPulizia = pannello;

        ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).setPulizia(this);

    }

    //COSTRUTTORE
    public Pulizia(Pannello39_Pulizia_Automatica pannello) {

        //Dichiarazione Pannello di Riferimento
        this.pannelloPulizia = pannello;

        ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).setPulizia(this);

    }

    //COSTRUTTORE
    public Pulizia(Pannello40_Pulizia_Manuale pannello) {

        //Dichiarazione Pannello di Riferimento
        this.pannelloPulizia = pannello;

        ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).setPulizia(this);

    }

    public void init() {

        //Inizializzazione Variabile
        puliziaInterrottaManualmente = false;
        pos_20_impostata= false; 
        pos_53_impostata= false;
 
        //Inizializzazione Variabili
        initVariabili();

        PuliziaLogger.init();

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Inizializzazione Procedura");

        //LETTURA PARAMETRI RELATIVI AL CARICO 
        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Lettura Parametri di Pulizia");

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.log(Level.INFO, "Presa Default= {0}{1}"
                + "Tolleranza = {2}{3}"
                + "Volo = {4}{5}"
                + "Intervento Inverter = {6}{7}"
                + "Quantit\u00e0 da Caricare = {8}{9}"
                + "Tempo 5 Riapertura Valvola = {10}{11}"
                + "Tempo 4 Riapertura Valvola = {12}{13}"
                + "Tempo 3 Riapertura Valvola = {14}{15}"
                + "Tempo 2 Riapertura Valvola = {16}{17}"
                + "Tempo 1 Riapertura Valvola = {18}{19}"
                + "Tempo Svuota Tubo = {20}{21}"
                + "Materiale nel Tubo = {22}{23}"
                + "Materiale Inizio Pesatura Fine = {24}{25}"
                + "Limite 5 Peso Sacco = {26}{27}"
                + "Limite 4 Peso Sacco = {28}{29}"
                + "Limite 3 Peso Sacco = {30}{31}"
                + "Limite 2 Peso Sacco = {32}{33}"
                + "Limite 1 Peso Sacco = {34}{35}"
                + "Vel Rapida = {36}{37}Vel Lenta = {38}{39}"
                + "Vel Miscelazione = {40}{41}"
                + "Tempo Miscelazione = {42}{43}",
                new Object[]{
                    ParametriSingolaMacchina.parametri.get(114), //0
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(115), //2
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(116), //4
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(117), //6
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(118), //8
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(119), //10
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(120), //12
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(121), //14
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(122), //16
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(123), //18
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(124), //20
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(125), //22
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(126), //24
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(127), //26
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(128), //28
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(129), //30
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(130), //32
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(131), //34
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(132), //36
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(133), //38
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(134), //40
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(135)}); //42

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Lettura Parametri Pulizia Eseguita");

        pesoSaccoSvuotamento = Integer.parseInt(ParametriSingolaMacchina.parametri.get(136));

        pesoDaRaggiungereConfezione = (int) ((double) pesoSaccoSvuotamento - Double.parseDouble(ParametriSingolaMacchina.parametri.get(339)));

        ///// INSERIRE PROCEDURA DI CONTROLLO INIZIALE E AVVIO PROCEDURE DI PULIZIA
        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.info("Inizializzazione Procedura - Eseguita");

    }

    //PROCEDURA "PULIZIA AUTOMATICA"
    public void PuliziaAutomatica() {

        //Variabili di Controllo
        puliziaCompleta = true;
        bloccoSacchettoSingolo = false;
        valorePesaCarico = "0";

        if (pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Testo Messaggio Utente
            ((Pannello38_Pulizia_Svuotamento) pannelloPulizia).modificaTestoMessaggioUtente(3);

        } else if (pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Testo Messaggio Utente
            ((Pannello39_Pulizia_Automatica) pannelloPulizia).modificaTestoMessaggioUtente(3);

        } else if (pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Testo Messaggio Utente
            ((Pannello40_Pulizia_Manuale) pannelloPulizia).modificaTestoMessaggioUtente(3);

        }

        //Esecuzione Controlli Iniziali
        new ControlliInizialiPulizia(this).controlloContatti();

    }

    //PROCEDURA "BLOCCA SACCO"
    public void BloccaSacco() {

        //Variabile di Controllo
        bloccoSacchettoSingolo = true;

        //Avvio Thread di Controllo Blocco Sacco
        new ThreadPuliziaControlloBloccoSacchetto(this).start();

    }

    //PROCEDURA "APRI VALVOLA"
    public void ApriValvolaCarico() {

        if (pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Testo Messaggio Utente
            ((Pannello40_Pulizia_Manuale) pannelloPulizia).modificaTestoMessaggioUtente(2);

            //Avvio Thread Apertura Valvola di Carico
            new ThreadPuliziaApriValvolaCarico(this).start();

            //Impostazione Validità Pulsanti Pulizia
            ((Pannello40_Pulizia_Manuale) pannelloPulizia).impostaValiditaPulsantiPulizia(false);
        }
    }

    //PROCEDURA "APRI VIBRO VALVOLA DI CARICO"
    public void ApriVibroValvolaCarico() {

        if (pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            //Impostazione Testo Messaggio Utente
            ((Pannello40_Pulizia_Manuale) pannelloPulizia).modificaTestoMessaggioUtente(12);

            //Avvio Thread Apertura Valvola di Carico
            new ThreadPuliziaAttivaVibroTramoggia(this).start();

            //Impostazione Validità Pulsanti Pulizia
            ((Pannello40_Pulizia_Manuale) pannelloPulizia).impostaValiditaPulsantiPulizia(false);
        }
    }

    //Inizializzazione Variabili
    public void initVariabili() {

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Inzio Inizializzazione Variabili");

        //Inizializzazione Variabili
        valorePesaCarico = "0";
        valorePesaConfezioni = "0";
 
        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.info("Inizializzazione Variabili Eseguita");
 
    }

    //Analisi Stato Processo di Pesatura
    public void analizzaPesaComponenti() {

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Inizio Procedura Analizza Pesa Componente");

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.fine("Attivazione Rele Coclea");

        //Attivazione Rele
        gestoreReleCoclee(true);

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.fine("Avvio Thread Controllo Coclea Vuota");

        //Avvio Thread Controllo Coclea Vuota
        new ThreadPuliziaControlloCocleaVuota(this).start();

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.fine("Avvio controllo Pesa di Carico");

        //Avvio controllo Pesa di Carico
        new ThreadPuliziaControlloPesoCarico(this).start();

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Fine Procedura Analizza Peso Componente");

    }

    //Attivazione-Disattivazione Rele Coclee
    public void gestoreReleCoclee(boolean value) {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.INFO, "Gestore Rele Coclee{0}Presa ={1}{2}Valore = {3}", new Object[]{LOG_CHAR_SEPARATOR, TrovaPresaPerIdPresa(dettagliPulizia.getIdPresa()), LOG_CHAR_SEPARATOR, value});

        try { 
        	
            String id = Integer.toString(dettagliPulizia.getIdPresa()-1
					+ Integer.parseInt(USCITA_LOGICA_CONTATTORE_COCLEA_A));
  
            if (value) {
                GestoreIO_ModificaOut(id,
                        OUTPUT_TRUE_CHAR);
            } else {
                GestoreIO_ModificaOut(id,
                        OUTPUT_FALSE_CHAR);
            }

        } catch (NumberFormatException e) {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Errore Gestore Rele Coclee e ={0}", e);
        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Gestore Rele Coclee");

    }

    //Finalizzazione e Chiusura Metodi Pulizia
    public void finalizzaPulizia() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Finalizzazione Pulizia");

        if (pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Visibilità Dati Pulizia
            ((Pannello38_Pulizia_Svuotamento) pannelloPulizia).impostaVisibilitaDatiPulizia(false);

            //Impostazione Validità Pulsanti Pulizia
            ((Pannello38_Pulizia_Svuotamento) pannelloPulizia).impostaValiditaPulsantiPulizia(true);

        } else if (pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            ((Pannello39_Pulizia_Automatica) pannelloPulizia).initPannello();

        } else if (pannelloPulizia instanceof Pannello40_Pulizia_Manuale) {

            ((Pannello40_Pulizia_Manuale) pannelloPulizia).initPannello();

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Finalizzazione Pulizia Eseguita");

    }

    //Registrazione Processo di Pulizia Eseguito
    public void attivazioneScambiatoreAspiratore() {

        new ThreadPuliziaScambiatoreAttuatore(this).start();

        ///Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Attivazione Scambiatore Aspiratore");

    }

    //Test Funzionamento Pesa Carico
    public boolean controlloBilanciaCarico() {

        boolean esitoTest = false;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Controllo Bilancia di Carico");
 
        if (pannelloPulizia.isVisible()) {

            try { 
                
                //Creazione Client Pesa Carico
            	ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
            	
//            	ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(
//                        ID_BILANCIA_CARICO,
//                        ParametriSingolaMacchina.parametri.get(150),
//                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));
                          
                //Verifica Connessione Avvenuta
                if (!pesaCarico.verficaConn()) {
     
                    //////////////////////////////////////////
                    // CONNESSIONE SOCKET BILANCE FALLITA  ///
                    //////////////////////////////////////////
                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.severe("Connessione Bilancia di Carico Fallita");

                    esitoTest = false;

                    ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(17);

                    pesaCarico.chiudi();

                    //Finalizzazione Processo
                    finalizzaPulizia();

                } else { 
                    ///////////////////////////////////////////
                    // CONNESSIONE SOCKET BILANCE AVVENUTA  ///
                    ///////////////////////////////////////////
                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.info("Controllo Connessione Bilancia di Carico Superato");

                    esitoTest = true;

                    //Chiusura del Client
                    pesaCarico.chiudi();

                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.config("Chiusura Socket Pesa di Carico");

                }

            } catch (NumberFormatException | IOException ex) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.log(Level.SEVERE, "Errore Connessione Bilancia di Carico e ={0}", ex);

                ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(17);

                finalizzaPulizia();
            }

        } 
        
        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Controllo Bilancia di Carico");

        return esitoTest;
    }

    //Test Funzionamento Bilancia Confezioni
    public boolean controlloBilanciaConfezionamento() {

        boolean esitoTest = false;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Controllo Bilancia Confezionamento");

        if (pannelloPulizia.isVisible()) {

            try {
                //Creazione Client di Pesa
            	ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
            	
//            	ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(
//                        ID_BILANCIA_CONFEZIONI,
//                        ParametriSingolaMacchina.parametri.get(150),
//                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

                //Verifica Connessione Avvenuta
                if (!pesaConfezioni.verficaConn()) {

                    //////////////////////////////////////////
                    // CONNESSIONE SOCKET BILANCE FALLITA  ///
                    //////////////////////////////////////////
                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.severe("Connessione Bilancia Confezioni Fallita");

                    esitoTest = false;

                    ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(17);

                    finalizzaPulizia();

                    pesaConfezioni.chiudi();

                } else {

                    ///////////////////////////////////////////
                    // CONNESSIONE SOCKET BILANCE AVVENUTA  ///
                    ///////////////////////////////////////////
                    esitoTest = true;

                    //Memorizzazione Log Processo
                    PuliziaLogger.logger.info("Controllo Connessione Bilancia Confezioni Superato");

                    //Chiusura del Client
                    pesaConfezioni.chiudi();

                }
            } catch (NumberFormatException | IOException ex) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.log(Level.SEVERE, "Errore Connessione Bilancia di Confezioni e ={0}", ex);

                ((Pannello44_Errori) pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(17);

                finalizzaPulizia();

            }

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Controllo Bilancia Confezionamento");

        return esitoTest;

    }

    //Finalizza Procedura di Pulizia Corrente
    public void finalizzaProcedura() {

        //Inizializzazione Variabili di Controllo
        interrompiComunicazioneSchedaIO = true;

    }

    ///////////////////////
    // MAIN PROCEDURES  ///
    ///////////////////////
    //PROCEDURA "SVUOTA MISCELATORE"
    public void SvuotamentoMiscelatore(int velInverter) {

        //Variabili di Controllo
        puliziaCompleta = false;
        bloccoSacchettoSingolo = false;
        valorePesaConfezioni = "0";

        //Inizializzazione Vel Miscelatore
        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.log(Level.INFO, "Inizializzazione Vel Miscelatore - Valore ={0}", velInverter);

        //Variazione Vel 
        inverter_mix.cambiaVelInverter(VerificaLunghezzaStringa(velInverter, 4));

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.info("Comando Cambio Vel Inviato");
 
        if (controlloBilanciaCarico() && controlloBilanciaConfezionamento()) {

            
            
            if (pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

                //Inizializzazione e impostazione visibilità Dati Pulizia
                ((Pannello38_Pulizia_Svuotamento) pannelloPulizia).impostaVisibilitaDatiPulizia(true);

                ((Pannello38_Pulizia_Svuotamento) pannelloPulizia).setVelMiscelatore(Integer.toString(velInverter));

            }

            //Avvio Thread Controllo Blocco Sacchetto
            new ThreadPuliziaControlloBloccoSacchetto(this).start();

        }
    }

}
