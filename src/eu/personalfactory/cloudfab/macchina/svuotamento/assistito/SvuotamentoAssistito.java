/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.svuotamento.assistito;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiPesoVisualizzato;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_FREQ_THREAD_LETTURA_PESO_CONTROLLO_MANUALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BLOCCA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_A;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.microdosatore.Microdosatore_Inverter_2017;
import eu.personalfactory.cloudfab.macchina.panels.Pannello36_ScaricoSilosMicro;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class SvuotamentoAssistito {

    public Pannello36_ScaricoSilosMicro pannelloSvuotamento; 
    //public String outputBuffer[];
    public boolean interrompiComunicazioneSchedaIO, interrompiThreadCarica, interrompiConfezionamento;

    //COSTRUTTORE
    public SvuotamentoAssistito(Pannello36_ScaricoSilosMicro pannelloSvuotamento) {

        //Dichiarazione Pannello di Riferimento
        this.pannelloSvuotamento = pannelloSvuotamento; 

    }


    //Lettura Dati di Configurazione Scheda Relay da Database 
    public String configuraSchedaRelay() {

        String confSocket;

        //Lettura Parametri Ingresso
        confSocket = ParametriSingolaMacchina.parametri.get(57) + "_";

        for (int i = 0; i < Integer.parseInt(ParametriSingolaMacchina.parametri.get(57)); i++) {

            confSocket = confSocket + ParametriSingolaMacchina.parametri.get(60 + i) + ".";

        }

        //Lettura Parametri Uscite
        confSocket = confSocket + ParametriSingolaMacchina.parametri.get(58) + "_";

        for (int i = 0; i < Integer.parseInt(ParametriSingolaMacchina.parametri.get(58)); i++) {

            confSocket = confSocket + ParametriSingolaMacchina.parametri.get(76 + i) + ".";
        }

        //Lettura Parametri Uscite Coclee
        confSocket = confSocket + ParametriSingolaMacchina.parametri.get(59) + "_";

        for (int i = 0; i < Integer.parseInt(ParametriSingolaMacchina.parametri.get(59)); i++) {

            confSocket = confSocket + ParametriSingolaMacchina.parametri.get(92 + i) + ".";
        }

        return confSocket;
    }

    //Avvio Thread Carica da Silo
    public void avvioThreadCaricaDaSilo() {

        new ThreadCaricaDaSilo().start();
    }

    //Avvio Thread Carica da Silo
    public void avvioThreadCaricaDaMicro() {

        new ThreadCaricaDaMicro().start();
    }

    private class ThreadCaricaDaSilo extends Thread {

        @Override
        public void run() {

            interrompiThreadCarica = false;

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                    OUTPUT_TRUE_CHAR);

            try {

                int idPresa;
                String id;

                idPresa = pannelloSvuotamento.idPresa
                        + Integer.parseInt(ParametriSingolaMacchina.parametri.get(58));

                //Formattazione Stringa Uscita in Modo che Sia da 2 Caratteri
                if (Integer.toString(idPresa).length() == 1) {
                    id = "0" + Integer.toString(idPresa - 1);
                } else {
                    id = Integer.toString(idPresa - 1);
                }

                GestoreIO_ModificaOut(
                        id,
                        OUTPUT_TRUE_CHAR);

                //Attendi Attivazione Coclea
                ThreadCaricaDaSilo.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));

                // Creazione Client Bilancia
                ClientPesaTLB4 pesa = new ClientPesaTLB4(ID_BILANCIA_CARICO);

//                ClientPesaTLB4 pesa = new ClientPesaTLB4(
//                        ID_BILANCIA_CARICO,
//                        ParametriSingolaMacchina.parametri.get(150),
//                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));
                
                //Inizializzazione Velocità Inverter 
                inverter_screws.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(417));

                //invCoclee.avvioInverter(); 
                inverter_screws.avviaArrestaInverterRotazioneInversa(true);

                //Azzeramento Peso Bilancia
                pesa.commutaNetto();

                while (!interrompiThreadCarica
                        && pannelloSvuotamento.isVisible()) {

                    //lettura Peso Bilancia di Carico
                    String peso = pesa.pesoNetto();

                    try {
                        int peso_int = Integer.parseInt(peso);

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                            ///////////////////////////
                            // CONVERSIONE DI PESO  ///
                            ///////////////////////////
                            pannelloSvuotamento.elemLabelSimple[12].setText(ConvertiPesoVisualizzato(peso, ParametriSingolaMacchina.parametri.get(338)) + " " + ParametriSingolaMacchina.parametri.get(340));

                        } else {

                            /////////////////////
                            // SISTEMA METRICO ///
                            //////////////////////
                            pannelloSvuotamento.elemLabelSimple[12].setText(peso + " " + ParametriSingolaMacchina.parametri.get(340));
                        }

                        if (peso_int > Integer.parseInt(ParametriSingolaMacchina.parametri.get(416))) {
                            interrompiThreadCarica = true;

                        }

                    } catch (NumberFormatException e) {
                    }

                } //fine loop

                GestoreIO_ModificaOut(
                        id,
                        OUTPUT_FALSE_CHAR);

                pesa.chiudi();
 
                //Arresto inverter
                inverter_screws.avviaArrestaInverterRotazioneInversa(false);
                
            } catch (IOException ex) {
                Logger.getLogger(Pannello36_ScaricoSilosMicro.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
            }

            new ThreadApriValvolaDiCarico().start();

            pannelloSvuotamento.modificaVisualizzazioneFinePesatura();

        }

    }

    private class ThreadCaricaDaMicro extends Thread {

        @Override
        public void run() {

            int id_micro = pannelloSvuotamento.idPresa - 100;

            ///////////////////////////////////
            // MICRODOSATORE SERIE 1 -2017  ///
            ///////////////////////////////////
            Microdosatore_Inverter_2017 microdosatore_2017 = new Microdosatore_Inverter_2017(Integer.toString(id_micro + 1));

            //Memorizzazione Log Processo
            SessionLogger.logger.log(Level.INFO,
                    "{0}Microdosatore {1}{2}"
                    + "Indirizzo= {3}",
                    new Object[]{
                        LOG_CHAR_SEPARATOR,
                        id_micro,
                        LOG_CHAR_SEPARATOR,
                        microdosatore_2017.getMicroIPAddress()});

            /////////////////////////////////////
            // IMPOSTA VELOCITA ALTA E AVVIA  ///
            /////////////////////////////////////
            //Avvio Inverter
            microdosatore_2017.attivaMotoreCoclee(ParametriSingolaMacchina.parametri.get(368));

            microdosatore_2017.attivaMescola(true);

            while (!interrompiThreadCarica
                    && pannelloSvuotamento.isVisible()) {

                ////////////////////
                // LETTURA PESO  ///
                ////////////////////
                String rep = microdosatore_2017.leggiPeso();

                if (rep.length() > 0) {

                    rep = rep.substring(1, rep.length());

                    //Memorizzazione log
                    SessionLogger.logger.log(Level.INFO, "Risposta Ricevuta dal Microdosatore = {0}", rep);

                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                        ///////////////////////////
                        // CONVERSIONE DI PESO  ///
                        ///////////////////////////
                        pannelloSvuotamento.elemLabelSimple[12].setText(ConvertiPesoVisualizzato(rep, ParametriSingolaMacchina.parametri.get(338)) + " " + ParametriSingolaMacchina.parametri.get(340));

                    } else {

                        /////////////////////
                        // SISTEMA METRICO ///
                        //////////////////////
                        pannelloSvuotamento.elemLabelSimple[12].setText(rep + " " + ParametriSingolaMacchina.parametri.get(340));
                    }
                }

                try {
                    ThreadCaricaDaMicro.sleep(MICRO_FREQ_THREAD_LETTURA_PESO_CONTROLLO_MANUALE);
                } catch (InterruptedException ex) {
                    SessionLogger.logger.log(Level.SEVERE, "Errore durante lo sleep del thread - ex ={0}", ex);
                }

            } //End loop

            microdosatore_2017.attivaMescola(false);
            microdosatore_2017.attivaMotoreCoclee("0");

            new ThreadApriValvolaDiCarico().start();
        }

    }

    private class ThreadApriValvolaDiCarico extends Thread {

        @Override
        public void run() {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_TRUE_CHAR);

            int i = 0;
            while (i * 1000 < Integer.parseInt(ParametriGlobali.parametri.get(10))) {

                pannelloSvuotamento.aggiornaTempoValvola(i);
                i++;

                try {
                    ThreadApriValvolaDiCarico.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            pannelloSvuotamento.aggiornaTempoValvola(0);

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_FALSE_CHAR);

            try {
                ThreadApriValvolaDiCarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(429)));
            } catch (InterruptedException ex) {
                Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
            }

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO,
                    OUTPUT_FALSE_CHAR);

        }
    }

    public void avvioConfezionamento() {

        if (pannelloSvuotamento.bilanciaStandard) {

            new ThreadConfezionamentoBilanciaStandard().start();
        } else {
            new ThreadConfezionamentoBilanciaOMB().start();
        }

    }

    private class ThreadConfezionamentoBilanciaStandard extends Thread {

        @Override
        public void run() {

            interrompiConfezionamento = false;

            try {

            	ClientPesaTLB4 pesa = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
            	
                pesa.commutaNetto();

                int velInverter = Integer.parseInt(ParametriSingolaMacchina.parametri.get(426));

                //Memorizzazione log processo Pulizia
                SessionLogger.logger.log(Level.INFO, "Inizializzazione Vel Miscelatore - Valore ={0}", velInverter);
                
                inverter_mix.cambiaVelInverter(VerificaLunghezzaStringa(velInverter, 4));

                //Memorizzazione log processo Pulizia
                SessionLogger.logger.info("Comando Cambio Vel Inviato");

                //////////////////////////////////
                // CONTROLLO BLOCCO SACCHETTO  ///
                //////////////////////////////////
                boolean sacchettoBloccato = false;
                //Inizio loop
                while (!sacchettoBloccato
                        && !interrompiConfezionamento) {

                    try {
                        ThreadConfezionamentoBilanciaStandard.sleep(
                                Integer.parseInt(ParametriSingolaMacchina.parametri.get(213)));
                    } catch (InterruptedException ex) {
                    }

                    if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO)) {

                        //Disabilita Blocco Sacco 
                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

                            GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI,
                                    OUTPUT_TRUE_CHAR);

                        }
                        try {
                            ThreadConfezionamentoBilanciaStandard.sleep(100);

                            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                            	//Apertura Totale Valvola 
                            	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
                            		///////////////////////////
                            		// ATTUATORE MULTISTADIO //
                            		///////////////////////////
                            		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);  
                            		ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");
                            	} else {
                            		////////////////////
                            		// COMANDO UNICO  //
                            		////////////////////
                            		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);  
                            		ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100_COMANDO_UNICO");
                            	}


                                ThreadConfezionamentoBilanciaStandard.sleep(200);

                            }
                            ThreadConfezionamentoBilanciaStandard.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO + OUTPUT_SEP_CHAR
                                + USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
                                ///       + USCITA_LOGICA_MOTORE_VIBRO_BASE + OUTPUT_SEP_CHAR
                                + USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
                                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI,
                                OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                ////                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR);

                        //Avvio Inverter
                        inverter_mix.avviaArrestaInverterRotazioneInversa(true);

                        //Modifica Variabile di Controllo loop
                        sacchettoBloccato = true;

                    }

                }//fine loop

                
                //INIZIO LOOP
                while (!interrompiConfezionamento && !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP)) {

                    //lettura Peso Bilancia di Carico
                    String peso = pesa.pesoNetto();

                    try {
                        int peso_int = Integer.parseInt(peso);

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(342))) {

                            ///////////////////////////
                            // CONVERSIONE DI PESO  ///
                            ///////////////////////////
                            pannelloSvuotamento.elemLabelSimple[14].setText(ConvertiPesoVisualizzato(peso, ParametriSingolaMacchina.parametri.get(338)) + " " + ParametriSingolaMacchina.parametri.get(340));

                        } else {

                            /////////////////////
                            // SISTEMA METRICO ///
                            //////////////////////
                            pannelloSvuotamento.elemLabelSimple[14].setText(peso + " " + ParametriSingolaMacchina.parametri.get(340));
                        }

                        if (peso_int > Integer.parseInt(ParametriSingolaMacchina.parametri.get(427))) {
                            interrompiConfezionamento = true;

                        }

                    } catch (NumberFormatException e) {
                    }
                    try {
                        ThreadConfezionamentoBilanciaStandard.sleep(200);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                pesa.chiudi();

            } catch (IOException ex) {
                Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
            }

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BLOCCA_SACCO + OUTPUT_SEP_CHAR
                    + USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
                    ///// + USCITA_LOGICA_MOTORE_VIBRO_BASE + OUTPUT_SEP_CHAR
                    + USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO,
                    OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                    + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                    ///+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                    + OUTPUT_FALSE_CHAR);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI,
                        OUTPUT_FALSE_CHAR);

            }
            try {
                ThreadConfezionamentoBilanciaStandard.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                //Chiusura Valvola

				 if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {
	                	///////////////////////////
	                	// ATTUATORE MULTISTADIO //
	                	///////////////////////////
	                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
	                	PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
	                } else {
	                	////////////////////
	                	// COMANDO UNICO  //
	                	////////////////////
	                	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
	                	PuliziaLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
	                }

            }
            try {
                ThreadConfezionamentoBilanciaStandard.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Arressta Inverter
            inverter_mix.avviaArrestaInverterRotazioneInversa(false);

            try {
                ThreadConfezionamentoBilanciaStandard.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(430)));
            } catch (InterruptedException ex) {
                Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
            }

            pannelloSvuotamento.confezionamentoInEsecuzione = false;

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI,
                    OUTPUT_FALSE_CHAR);

            pannelloSvuotamento.elemLabelSimple[10].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 936, ParametriSingolaMacchina.parametri.get(111)));
            pannelloSvuotamento.svuotamentoAssistito.interrompiConfezionamento = true;

            pannelloSvuotamento.elemBut[5].setEnabled(true);
            pannelloSvuotamento.elemBut[6].setEnabled(true);
            pannelloSvuotamento.elemBut[7].setEnabled(true);
            pannelloSvuotamento.elemBut[8].setEnabled(true);

            pannelloSvuotamento.elemLabelSimple[8].setEnabled(true);
            pannelloSvuotamento.elemLabelSimple[9].setEnabled(true);
            pannelloSvuotamento.elemLabelSimple[10].setEnabled(true);
            pannelloSvuotamento.elemLabelSimple[11].setEnabled(true);

            pannelloSvuotamento.butFreccia.setEnabled(true);

        }
    }

    public void avvioCocleaRotazioneInversa(Boolean value) {

        int idPresa; 
        
        idPresa = (pannelloSvuotamento.idPresa-1)
              + Integer.parseInt(USCITA_LOGICA_CONTATTORE_COCLEA_A);
      

        if (value) {
        	GestoreIO_ModificaOut(
        			Integer.toString(idPresa),
        			OUTPUT_TRUE_CHAR);

        	//Inizializzazione Velocità Inverter 
        	inverter_screws.cambiaVelInverter(ParametriSingolaMacchina.parametri.get(417));

        	//Avvio Inverter Rotazione inversa
        	inverter_screws.avviaArrestaInverterRotazioneInversa(value);
        	
        } else {

        	GestoreIO_ModificaOut(
        			Integer.toString(idPresa),
        			OUTPUT_FALSE_CHAR);

        	//Avvio Inverter Rotazione inversa
        	inverter_screws.avviaArrestaInverterRotazioneInversa(value);

        }
    }
 

    private class ThreadConfezionamentoBilanciaOMB extends Thread {

        @Override
        public void run() {

            interrompiConfezionamento = false;

            //DA IMPLEMENTARE
            while (!interrompiConfezionamento) {
                try {
                    ThreadConfezionamentoBilanciaOMB.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SvuotamentoAssistito.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            pannelloSvuotamento.confezionamentoInEsecuzione = false;
        }
    }
}
