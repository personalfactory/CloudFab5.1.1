package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.IGNORA_CONTROLLI_INIZIALI;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ControlliInizialiPulizia {

    private final Pulizia pulizia;

    //COSTRUTTORE
    public ControlliInizialiPulizia(Pulizia pulizia) {

        //Dichiarazione Variabile Processo
        this.pulizia = pulizia;

        //Inizializzazione Array Esito Test
        pulizia.esitoTestStatoContatti = new boolean[3];

    }

    //Controllo Stato Inziale Contatti 
    public void controlloContatti() {

        PuliziaLogger.logger.config("Controlli Iniziali");

		if (IGNORA_CONTROLLI_INIZIALI) {

			///////////////////////////////
			// IGNORA CONTROLLI INIZIALI //
			///////////////////////////////
			completaProceduraControlloBilancie();
			
		} else {
			////////////////////////
			// PROCEDURA STANDARD //
			////////////////////////

			// Controllo Stato Iniziale Contatti
			controlloStatoIniziale();
		}

    }

    //Controllo Stato Iniziale Contatti
    public void controlloStatoIniziale() {

        PuliziaLogger.logger.config("Controllo Stato Iniziale Contatti  di Sicurezza");

        //Controllo Stato Iniziale Contatto di Sicurezza Sportello Miscelatore
        pulizia.esitoTestStatoContatti[0] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE)
                == ParametriSingolaMacchina.parametri.get(18).equals("1");

        //Controllo Stato Iniziale Contatto Valvola di Carico
        pulizia.esitoTestStatoContatti[1] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO)
                == ParametriSingolaMacchina.parametri.get(19).equals("1");

        //Controllo Stato Iniziale Contatto Valvola di Scarico
        pulizia.esitoTestStatoContatti[2] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO)
                == ParametriSingolaMacchina.parametri.get(20).equals("1");

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.log(Level.CONFIG, "Esito Controllo Contatto Sicurezza Sportello Miscelatore :{0}{1}"
                + "Esito Controllo Contatto Sicurezza Valvola di Carico :{2}{3}"
                + "Esito Controllo Contatto Sicurezza Valvola di Scarico :{4}",
                new Object[]{
                    pulizia.esitoTestStatoContatti[0],
                    LOG_CHAR_SEPARATOR,
                    pulizia.esitoTestStatoContatti[1],
                    LOG_CHAR_SEPARATOR,
                    pulizia.esitoTestStatoContatti[2]});

        if (pulizia.esitoTestStatoContatti[0]
                && pulizia.esitoTestStatoContatti[1]
                && pulizia.esitoTestStatoContatti[2]) {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.info("Controllo Stato Iniziale Contatti Superato");

            controlloImpiantoPneumatico_AperturaValvola();

        } else {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.severe("Controllo Stato Iniziale Contatti Fallito");

            if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(24) == 0) {

                ////////////////////////
                // RIPETI CONTROLLO  ///
                ////////////////////////
                //Memorizzazione log processo Pulizia
                PuliziaLogger.logger.warning("Ripetizione Controllo Contatti di Sicurezza");

                controlloContatti();

            } else {

                ////////////////////////////////
                // FINALIZZAZIONE PROCEDURA  ///
                ////////////////////////////////
                //Memorizzazione log processo Pulizia
                PuliziaLogger.logger.config("Finalizzazione Procedura Controlli Iniziali");

                pulizia.finalizzaPulizia();

            }

        }
    }

    //Controllo Impianto Pneumatico Parte A - Apertura Valvola di Carico e Avvio Temporizzatore
    private void controlloImpiantoPneumatico_AperturaValvola() {

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(298))) {
            ////////////////////////////////////////////////
            // TEST IMPIANTO CONTATTO VALVOLA DI CARICO  ///
            ////////////////////////////////////////////////
            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Apertura Valvola Carico");

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_TRUE_CHAR);

        } else {

            /////////////////////////////////////////////////
            // TEST IMPIANTO CONTATTO VALVOLA DI SCARICO  ///
            /////////////////////////////////////////////////
            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Apertura Valvola Scarico");

            //Apertura Totale Valvola
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

            	///////////////////////////
            	// ATTUATORE MULTISTADIO //
            	///////////////////////////

            	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);  
            	PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");
            } else {

            	////////////////////
            	// COMANDO UNICO  //
            	////////////////////

            	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);  
            	PuliziaLogger.logger.log(Level.INFO, "Apertura Vavola POS_100_COMANDO_UNICO");

            }
            
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

        }

        Timer timer = new Timer();
        TimerTask task = new ControlloTimer();
        timer.schedule(task, Integer.parseInt(ParametriSingolaMacchina.parametri.get(21)));

    }

    //Controllo Impianto Pneumatico Parte B - Verifica Stato Contatto a Valvola Aperta e Chiusura Valvola di Carico
    private void controlloImpiantoPneumatico_ChiusuraValvola() {

        boolean res;
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(298))) {

            ////////////////////////////////////////////////
            // TEST IMPIANTO CONTATTO VALVOLA DI CARICO  ///
            ////////////////////////////////////////////////
            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Chiusura Valvola Carico");

            res = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO) != ParametriSingolaMacchina.parametri.get(19).equals("1");

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_FALSE_CHAR);

        } else {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Chiusura Valvola Scarico");

            res = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO) != ParametriSingolaMacchina.parametri.get(20).equals("1");

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
             
            ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_0DEF");

        }

        //Analizza Stato Contatto Valvola di Scarico
        if (res || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(334))) {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.info("Controllo Impianto Pneumatico Superato");

            new ThreadControlloBilanciaCarico().start();

        } else {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.severe("Controllo Impianto Pneumatico Fallito");

            if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(1) == 0) {

                //////////////
                // RIPETI  ///
                //////////////
                //Memorizzazione log processo Pulizia
                PuliziaLogger.logger.warning("Ripetizione Controllo Contatti di Sicurezza");

                controlloContatti();

            } else {

                /////////////////
                // FINALIZZA  ///
                /////////////////
                //Memorizzazione log processo Pulizia
                PuliziaLogger.logger.warning("Finalizzazione Procedura Controlli Iniziali");

                pulizia.finalizzaPulizia();

            }
        }

    }

    //Test Presenza Materiale Pesa Carico
    private class ThreadControlloBilanciaCarico extends Thread {

        @Override
        public void run() {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Inizio Controllo Bilancia di Carico");

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.log(Level.CONFIG, "Indirizzo Modulo = {0}{1}"
                    + "Indirizzo Socket Pesa = {2}{3}"
                    + "Porta Socket Pesa = {4}",
                    new Object[]{
                        ID_BILANCIA_CARICO,
                        LOG_CHAR_SEPARATOR,
                        ParametriSingolaMacchina.parametri.get(150),
                        LOG_CHAR_SEPARATOR,
                        ParametriSingolaMacchina.parametri.get(151)});

            try {
                //Creazione Client Pesa Carico
            	ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);

//            	ClientPesaTLB4 pesaCarico = new ClientPesaTLB4(
//                        ID_BILANCIA_CARICO,
//                        ParametriSingolaMacchina.parametri.get(150),
//                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));
            	
                if (!pesaCarico.verficaConn()) {

                    ///////////////////////////////////////////////
                    // LETTURA DALLA BILANCIA DI CARICO FALLITA ///
                    ///////////////////////////////////////////////
                    if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(17) == 0) {

                        pulizia.finalizzaPulizia();
                    }

                } else {

                    /////////////////////////////////////////////////////////////
                    // LETTURA DALLA BILANCIA DI CARICO ESEGUITA CON SUCCESSO ///
                    /////////////////////////////////////////////////////////////
                    try {
                        ThreadControlloBilanciaCarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(187)));
                    } catch (InterruptedException ex) {
                    }

                    //Commutazione Peso Lordo
                    pesaCarico.commutaLordo();

                    try {
                        ThreadControlloBilanciaCarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(187)));
                    } catch (InterruptedException ex) {
                    }

                    //Lettura del Peso Lordo
                    int pesoLordoPesaCarico = Integer.parseInt(pesaCarico.pesoLordo());

                    //Azzeramento Modulo di Pesa
                    pesaCarico.commutaNetto();

                    try {
                        ThreadControlloBilanciaCarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(187)));
                    } catch (InterruptedException ex) {
                    }

                    //Chiusura del Client
                    pesaCarico.chiudi();

                    try {
                        ThreadControlloBilanciaCarico.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(187)));
                    } catch (InterruptedException ex) {
                    }

                    if (Math.abs(pesoLordoPesaCarico)
                            > Math.abs(Integer.parseInt(ParametriSingolaMacchina.parametri.get(22)))) {

                        if (Math.abs(pesoLordoPesaCarico) < Math.abs(
                                Integer.parseInt(ParametriSingolaMacchina.parametri.get(22))
                                + Integer.parseInt(ParametriSingolaMacchina.parametri.get(22))
                                * Integer.parseInt(ParametriSingolaMacchina.parametri.get(23)) / 100)) {

                            //Memorizzazione log processo Pulizia
                            PuliziaLogger.logger.log(Level.WARNING, "Presenza non Eccessiva di Materiale sulla Pesa di Carico ={0}", pesoLordoPesaCarico);

                            int res = ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(2);

                            if (res == 0) {

                                //////////////
                                // IGNORA  ///
                                //////////////
                                //Memorizzazione log processo Pulizia
                                PuliziaLogger.logger.warning("Errore Presenza di Materiale sulla Pesa di Carico Ignorato");

                                //Ignora Errore
                                new ThreadControlloBilanciaConfezionamento().start();

                            } else {
                                if (res == 1) {

                                    //////////////
                                    // RIPETI  ///
                                    //////////////
                                    //Memorizzazione log processo Pulizia
                                    PuliziaLogger.logger.warning("Ripetizione Controllo Bilancia di Carico");

                                    //Riprova
                                    new ThreadRiavvioControlloBilanciaCarico().start();

                                } else {

                                    /////////////////
                                    // FINALIZZA  ///
                                    /////////////////
                                    //Memorizzazione log processo Pulizia
                                    PuliziaLogger.logger.config("Finalizzazione Procedura di Controllo Bilancia di Carico");

                                    //Finalizzazione Procedura
                                    pulizia.finalizzaPulizia();
                                }

                            }
                        } else {

                            //Memorizzazione log processo Pulizia
                            PuliziaLogger.logger.log(Level.SEVERE, "Presenza Eccessiva di Materiale sulla Pesa di Carico ={0}", pesoLordoPesaCarico);

                            if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(3) == 0) {

                                //////////////
                                // RIPETI  ///
                                //////////////
                                //Memorizzazione log processo Pulizia
                                PuliziaLogger.logger.warning("Ripetizione Controllo Bilancia di Carico");

                                //Riprova
                                new ThreadRiavvioControlloBilanciaCarico().start();

                            } else {

                                /////////////////
                                // FINALIZZA  ///
                                /////////////////
                                //Memorizzazione log processo Pulizia
                                PuliziaLogger.logger.config("Finalizzazione Procedura di Controllo Bilancia di Carico");

                                //Annulla Processo in Corso
                                pulizia.finalizzaPulizia();
                            }

                        }

                    } else {

                        //Memorizzazione log processo Pulizia
                        PuliziaLogger.logger.log(Level.INFO, "Controllo Bilancia di Carico Superato - Valore ={0}", pesoLordoPesaCarico);

                        //Controllo Bilancia di Confezionamento
                        new ThreadControlloBilanciaConfezionamento().start();

                    }

                }

            } catch (IOException ex) {

                //Memorizzazione log processo Pulizia
                PuliziaLogger.logger.log(Level.CONFIG, "Errore durante la creazione del Client Pesa e={0}", ex);

            }

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Fine Controllo Bilancia di Carico");
        }
    }

    //Test Materiale Presente Bilancia di Confezionamento
    private class ThreadControlloBilanciaConfezionamento extends Thread {

        @Override
        public void run() {

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Inzio Controllo Bilancia di Confezionamento");

            try {

            	ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
            	
//            	ClientPesaTLB4 pesaConfezioni = new ClientPesaTLB4(
//                        ID_BILANCIA_CONFEZIONI,
//                        ParametriSingolaMacchina.parametri.get(150),
//                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

                try {
                    ThreadControlloBilanciaConfezionamento.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
                } catch (InterruptedException ex) {
                }

                //Lettura del Peso Lordo
                int pesoLordoPesaConfezioni = Integer.parseInt(pesaConfezioni.pesoLordo());

                try {
                    ThreadControlloBilanciaConfezionamento.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
                } catch (InterruptedException ex) {
                }

                //Chiusura del Socket
                pesaConfezioni.chiudi();

                try {
                    ThreadControlloBilanciaConfezionamento.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(194)));
                } catch (InterruptedException ex) {
                }

                if (Math.abs(pesoLordoPesaConfezioni)
                        > Math.abs(Integer.parseInt(
                                ParametriSingolaMacchina.parametri.get(24)))) {

                    if (Math.abs(pesoLordoPesaConfezioni)
                            < Math.abs(
                                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(24))
                                    + Integer.parseInt(ParametriSingolaMacchina.parametri.get(24))
                                    * Integer.parseInt(ParametriSingolaMacchina.parametri.get(25)) / 100)) {

                        //Memorizzazione log processo Pulizia
                        PuliziaLogger.logger.log(Level.WARNING, "Presenza non Eccessiva di Materiale sulla Pesa di Confezionamento - Valore ={0}", pesoLordoPesaConfezioni);

                        int res = ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(4);

                        if (res == 0) {

                            //////////////
                            // IGNORA  ///
                            //////////////
                            //Memorizzazione log processo Pulizia
                            PuliziaLogger.logger.warning("Errore Presenza di Materiale sulla Pesa di Confezionamento Ignorato");

                            //Ignora Errore
                            completaProceduraControlloBilancie();

                        } else {
                            if (res == 1) {

                                //////////////
                                // RIPETI  ///
                                //////////////
                                //Memorizzazione log processo Pulizia
                                PuliziaLogger.logger.warning("Ripetizione Controllo Bilancia di Confezionamento");

                                //Riprova
                                new ThreadRiavvioControlloBilanciaConfezionamento().start();

                            } else {

                                /////////////////
                                // FINALIZZA  ///
                                /////////////////
                                //Memorizzazione log processo Pulizia
                                PuliziaLogger.logger.warning("Finalizzazione Procedura Controllo Bilancia di Confezionamento");

                                //Annulla Processo in Corso
                                pulizia.finalizzaPulizia();
                            }
                        }

                    } else {

                        //Memorizzazione log processo Pulizia
                        PuliziaLogger.logger.log(Level.SEVERE, "Presenza Eccessiva di Materiale sulla Pesa di Confezionamento - Valore ={0}", pesoLordoPesaConfezioni);

                        if (((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(5) == 0) {

                            //////////////
                            // RIPETI  ///
                            //////////////
                            //Memorizzazione log processo Pulizia
                            PuliziaLogger.logger.warning("Ripetizione Controllo Bilancia di Confezionamento");

                            //Riprova
                            new ThreadRiavvioControlloBilanciaConfezionamento().start();

                        } else {

                            /////////////////
                            // FINALIZZA  ///
                            ///////////////// 
                            //Memorizzazione log processo Pulizia
                            PuliziaLogger.logger.warning("Finalizzazione Procedura Controllo Bilancia di Confezionamento");

                            //Annulla Processo in Corso
                            pulizia.finalizzaPulizia();
                        }

                    }
                } else {

                    //Memorizzazione log processo Pulizia
                    PuliziaLogger.logger.log(Level.INFO, "Controllo Bilancia di Confezionamento Superato - Valore ={0}", pesoLordoPesaConfezioni);

                    completaProceduraControlloBilancie();

                }

            } catch (IOException ex) {

                //Memorizzazione log processo Pulizia
                PuliziaLogger.logger.log(Level.SEVERE, "Errore Durante la Creazione del Client Pesa e={0}", ex);
            }

            //Memorizzazione log processo Pulizia
            PuliziaLogger.logger.config("Fine Controllo Bilancia di Confezionamento");
        }
    }

    //Completamento Procedura
    public void completaProceduraControlloBilancie() {

        //Memorizzazione log processo Pulizia
        PuliziaLogger.logger.config("Fine Procedura Controllo Bilance");

        if (pulizia.pannelloPulizia instanceof Pannello38_Pulizia_Svuotamento) {

            //Impostazione Visibiltà Messaggio Utente
            ((Pannello38_Pulizia_Svuotamento) pulizia.pannelloPulizia).impostaVisibilitaDatiPulizia(true);

        } else if (pulizia.pannelloPulizia instanceof Pannello39_Pulizia_Automatica) {

            //Impostazione Visibiltà Messaggio Utente
            ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).impostaVisibilitaDatiPulizia(true);

        }

        //Avvio Segnalazione Acustica
        new ThreadPuliziaAvvioSegnaleAcustico(pulizia).start();

    }

    //Temporizzatore Controllo Impianto Pneumatico B
    private class ControlloTimer extends TimerTask {

        @Override
        public void run() {

            //Completamento Test Impianto Pneumatico
            controlloImpiantoPneumatico_ChiusuraValvola();
        }
    }

    //Thread Riavvio Controllo Bilancia Confezioni
    private class ThreadRiavvioControlloBilanciaConfezionamento extends Thread {

        //COSTRUTTORE
        public ThreadRiavvioControlloBilanciaConfezionamento() {

            //Impostazione Nome Thread Corrente
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public void run() {
            try {
                ThreadRiavvioControlloBilanciaConfezionamento.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(188)));
            } catch (InterruptedException ex) {
            }

            new ThreadControlloBilanciaConfezionamento().start();
        }
    }

    //Thread Riavvio Controllo Bilancia Carico
    private class ThreadRiavvioControlloBilanciaCarico extends Thread {

        //COSTRUTTORE
        public ThreadRiavvioControlloBilanciaCarico() {

            //Impostazione Nome Thread Corrente
            this.setName(this.getClass().getSimpleName());
        }

        @Override
        public void run() {
            try {
                ThreadRiavvioControlloBilanciaCarico.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(189)));
            } catch (InterruptedException ex) {
            }

            new ThreadControlloBilanciaCarico().start();
        }
    }
}
