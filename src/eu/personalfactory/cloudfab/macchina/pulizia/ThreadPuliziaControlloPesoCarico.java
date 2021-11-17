
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoMovimentoMagazzino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPresaPerIdPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_CONFIG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_PESATURA_INTERVALLO_REP_INFO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 *
 * Controlla il Raggiungimento del Peso Componentente Gestisce il Cambio di Vel
 * delle Coclee e l'Intervento della Seconda Velocita' per i Vecchi Impianti
 * Esegue la lettura del Peso dal Modulo di Pesa Aggiorna il Database quando la
 * Pesa del Componente Viene Completata
 *
 */
public class ThreadPuliziaControlloPesoCarico extends Thread {

    private final Pulizia pulizia;
    private int indexVel;
    private int iCount;
    private int qPesata;
    private ClientPesaTLB4 pesaCarico;
    private double pesoMancante, percentualePeso;
    private boolean interrompiAggiornaPesoReale;
    private int counterLog;
    private boolean esecuzioneCambioVel;

    //COSTRUTTORE
    public ThreadPuliziaControlloPesoCarico(Pulizia pulizia) {

        //Dichiarazione Variabile Processo
        this.pulizia = pulizia;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Controllo Peso Carico");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.INFO, "Inizializzazione :{0}Presa Componente :{1}{2}Tolleranza Componente :{3}{4}Volo Componente :{5}{6}Intervento Inverter Componente :{7}{8}Quantita'' Componente :{9}",
                new Object[]{
                    LOG_CHAR_SEPARATOR,
                    TrovaPresaPerIdPresa(pulizia.dettagliPulizia.getIdPresa()),
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(115),
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(116),
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(117),
                    LOG_CHAR_SEPARATOR,
                    ParametriSingolaMacchina.parametri.get(118)});

        /////////////////////////////////
        // INIZILIZZAZIONE VARIABILI  ///
        /////////////////////////////////
        counterLog = 0;

        //Inzializzazione Variabile di Controllo Peso
        pulizia.pesoRaggiunto = false;

        //Inizializzazione Valore Peso Carico
        qPesata = 0;
        pulizia.valorePesaCarico = "0";

        //Inizializzazione Indice di Vel
        indexVel = 0;
        iCount = 0;
        interrompiAggiornaPesoReale = false;
        try {

            //////////////////////////////////////
            // INIZIALIZZAZIONE CLIENT DI PESA ///
            //////////////////////////////////////
            //Creazione Client Pesa Carico
            pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);

            
//            pesaCarico = new ClientPesaTLB4(
//                    ID_BILANCIA_CARICO,
//                    ParametriSingolaMacchina.parametri.get(150),
//                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));
            
            
            ///Memorizzazione Log Processo
            PuliziaLogger.logger.config("Inizializzazione Client di Pesa Eseguita");

            //Reimpostazione Vel di Base Inverter 
            inverter_screws.cambiaVelInverter(
                    pulizia.dettagliPulizia.getVelCarico().get(pulizia.dettagliPulizia.getVelCarico().size() - 2));

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.INFO, "Invo Comando Cambio Vel Inverter Eseuito - Valore = {0}", pulizia.dettagliPulizia.getVelCarico().get(pulizia.dettagliPulizia.getVelCarico().size() - 2));

            //Avvio Inverter 
            inverter_screws.avviaArrestaInverter(true);
            
            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Avvio Inverter");

        } catch (IOException ex) {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Errore Durante la Creazione del Client Pesa e={0}", ex);

        }

        //Inizio loop
        while (!pulizia.pesoRaggiunto
                && pulizia.pannelloPulizia.isVisible()
                && !pulizia.puliziaInterrottaManualmente) {

            //Controllo Peso Componente 
            controlloPesoComponente();

        } //fine loop

        //Registra Movimento Magazzino Scarico Componente
        RegistraNuovoMovimentoMagazzino(pulizia.dettagliPulizia.getIdComponente(), //id_materiale,
                ParametriGlobali.parametri.get(117), //tipo_materiale,
                Integer.parseInt(pulizia.ultimoPesoBilanciaCarico), //qEffettivo,
                pulizia.dettagliPulizia.getCodiceComponente(), //cod_ingresso_comp,
                DettagliSessione.getCodiceFigura(), //cod_operatore,
                "-1", //segno_op,
                ParametriGlobali.parametri.get(124), //procedura,
                ParametriGlobali.parametri.get(119), //tipo_mov,
                ParametriGlobali.parametri.get(150), TrovaPresaPerIdPresa(pulizia.dettagliPulizia.getIdPresa()), //id_silo,
                pulizia.dettagliPulizia.getQComponente(), //qTeorico,
                pulizia.dettagliPulizia.getIdCiclo(),   //id_ciclo
                ParametriGlobali.parametri.get(140), //origine_mov 
                new Date(),			//DataMov
                true,				//Abilitato
                TrovaValoreParametriComponentePerIdComp(pulizia.dettagliPulizia.getIdComponente()).get(9),					//Info1
                TrovaValoreParametriComponentePerIdComp(pulizia.dettagliPulizia.getIdComponente()).get(10),					//Info2 
                "",					//Info3 					
                "",					//Info4 
                "",					//Info5 
                "",					//Info6 
                "",					//Info7 
                "",					//Info8 
                "",					//Info9 
                ""); 				//Info10 
                                                                                      //int id_ciclo

        ////////////////////////
        // ARRESTO INVERTER  ///
        ////////////////////////
        
        pesaCarico.chiudi();
        
        
        //Arresto Inverter 
        inverter_screws.avviaArrestaInverter(false);

        if (pulizia.puliziaInterrottaManualmente) {

            ////////////////////////////////////
            /// LOOP INTERROTTO MANUALMENTE  ///
            ////////////////////////////////////
            //Memorizzazione Log Processo
            PuliziaLogger.logger.warning("Loop Interrotto Manualmente");

            //Finalizzazione Controllo Peso Carico per Peso Desiderato Raggiunto
            finalizzaInterruzioneManuale();

        } else if (!pulizia.interruzioneCocleaVuota) {

            ///////////////////////////////////////////////////
            /// LOOP INTERROTTO PER RAGGIUNGIMENTO DEL PESO ///
            ///////////////////////////////////////////////////
            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Loop Interrotto per Raggiungimento del Peso");

            //Finalizzazione Controllo Peso Carico per Peso Desiderato Raggiunto
            finalizzaControlloPesoCaricoRaggiunto();

        } else {

            /////////////////////////////////////////
            /// LOOP INTERROTTO PER COCLEA VUOTA  ///
            /////////////////////////////////////////
            //Memorizzazione Log Processo
            PuliziaLogger.logger.warning("Loop Interrotto per Coclea Vuota");

            //Finalizzazione Controllo Peso Carico per Peso Coclea Vuota
            finalizzaControlloPesoCocleaVuota();

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Procedura Controllo Peso Carico");

    }

    public void controlloPesoComponente() {

        //Controllo e Validazione Valore Peso Carico
        letturaPesoCarico();

        //Controllo Peso Raggiunto
        if (pesoMancante <= 0) {

            //////////////////////////////////
            // PESO DESIDERATO RAGGIUNTO /////
            //////////////////////////////////
            //Interruzione del loop corrente
            pulizia.pesoRaggiunto = true;

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Peso Desiderato Componente Raggiunto");

        } else {

            ///////////////////////////////////
            // PESO DESIDERATO NON RAGGIUNTO //
            ///////////////////////////////////
            if (!esecuzioneCambioVel) {

                //Gestore Cambio Vel Coclea
                new ThreadGestoreCambioVelocita(percentualePeso).start();

            }

        }

    }

    //Procedura di lettura del Peso sulla Bilancia di Carico
    public void letturaPesoCarico() {

        //Lettura del peso Netto
        String socketRep = "";

        //Lettura del peso Netto
        socketRep = pesaCarico.pesoNetto();

        try {

            int valoreDaValidare = Integer.parseInt(socketRep) - qPesata;

            if (!socketRep.equals("0")) {

                if (Math.abs(valoreDaValidare - Integer.parseInt(pulizia.valorePesaCarico))
                        <= Integer.parseInt(ParametriSingolaMacchina.parametri.get(174))) {

                    pulizia.valorePesaCarico = Integer.toString(valoreDaValidare);
                    iCount = 0;
                } else {

                    if (iCount > Integer.parseInt(ParametriSingolaMacchina.parametri.get(171))) {

                        pulizia.valorePesaCarico = Integer.toString(valoreDaValidare);

                        //Memorizzazione Log Processo
                        PuliziaLogger.logger.log(Level.WARNING, "Valore di Peso Riagganciato dopo {0} tentativi", iCount);

                        iCount = 0;
                    } else {

                        if (valoreDaValidare > 0) {
                            iCount++;
                        } else {
                            iCount = 0;
                        }
                    }

                }
            }

        } catch (NumberFormatException e) {
        }

        //Aggiornamento Label Visualizzazione Peso Pannello Processo
        ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).aggiornaLabelPesaCarico(pulizia.valorePesaCarico);

        //Calcolo del Peso Mancante
        pesoMancante = pulizia.dettagliPulizia.getQComponente()
                - pulizia.dettagliPulizia.getVolo()
                - Double.parseDouble(pulizia.valorePesaCarico);

        //Calcolo del Peso Mancante Percentuale
        percentualePeso = (pesoMancante
                / Double.parseDouble(ParametriGlobali.parametri.get(31))) * 100;

        memorizzaPesoLog(socketRep);
    }

    //Gestore Memorizzazione Peso Letto su File di Log
    private void memorizzaPesoLog(String socketRep) {

        //Memorizzazione sul File Log
        if (counterLog <= Math.max(LOG_PESATURA_INTERVALLO_REP_CONFIG,
                LOG_PESATURA_INTERVALLO_REP_INFO)) {

            counterLog++;

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.FINE, "Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}{5}Peso Mancante Percentuale = {6}",
                    new Object[]{socketRep, LOG_CHAR_SEPARATOR, pulizia.valorePesaCarico, LOG_CHAR_SEPARATOR, pesoMancante, LOG_CHAR_SEPARATOR, percentualePeso});

            if (counterLog % LOG_PESATURA_INTERVALLO_REP_CONFIG == 0) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.log(Level.CONFIG, "Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}{5}Peso Mancante Percentuale = {6}",
                        new Object[]{socketRep, LOG_CHAR_SEPARATOR, pulizia.valorePesaCarico, LOG_CHAR_SEPARATOR, pesoMancante, LOG_CHAR_SEPARATOR, percentualePeso});

            }

            if (counterLog % LOG_PESATURA_INTERVALLO_REP_INFO == 0) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.log(Level.INFO, "Peso Letto dal Socket = {0}{1}Valore Pesa Carico = {2}{3}Peso Mancante = {4}{5}Peso Mancante Percentuale = {6}",
                        new Object[]{socketRep, LOG_CHAR_SEPARATOR, pulizia.valorePesaCarico, LOG_CHAR_SEPARATOR, pesoMancante, LOG_CHAR_SEPARATOR, percentualePeso});

            }

        } else {

            //Inizializzazione Variabile
            counterLog = 0;

        }

    }

    //Gestione Cambi Velocita Inverter
    private class ThreadGestoreCambioVelocita extends Thread {

        private final Double percentualePeso;

        public ThreadGestoreCambioVelocita(Double percentualePeso) {
            this.percentualePeso = percentualePeso;
        }

        @Override
        public void run() {

            //Controllo quantita da pesare > quantita da pesare in dosatura diretta
            if (Integer.parseInt(pulizia.valorePesaCarico)
                    > Integer.parseInt(ParametriGlobali.parametri.get(32))) {

                for (int i = 0; i < pulizia.dettagliPulizia.getAliqCarico().size(); i++) {

                    if (percentualePeso
                            < pulizia.dettagliPulizia.getAliqCarico().get(i)) {

                        if (indexVel != i) {

                            ///////////////////////
                            // CAMBIO VELOCITA  ///
                            ///////////////////////
                            indexVel = i;

                            esecuzioneCambioVel = true;

                            String velCoclee = pulizia.dettagliPulizia.getVelCarico().get(indexVel);

                            //Cambio Vel
                            //pulizia.invCarico.cambioVelInverter(velCoclee);
////                            GestoreIO_InviaComunicazioneRS485(
////                                    inverter_screws.device,
////                                    inverter_screws.cambiaVelInverter(velCoclee));
                            inverter_screws.cambiaVelInverter(velCoclee);

                            //Memorizzazione Log Processo
                            PuliziaLogger.logger.log(Level.INFO, "Comando Cambio Vel Coclee - Nuova Vel ={0}", velCoclee);

                            break;
                        }
                        break;
                    }
                }
            }

            esecuzioneCambioVel = false;

        }
    }

////    //Gestione Cambi Velocita Inverter
////    public void ThreadGestoreCambioVelocita(Double percentualePeso) {
////
////
////        //Controllo quantita da pesare > quantita da pesare in dosatura diretta
////        if (Integer.parseInt(pulizia.valorePesaCarico)
////                > Integer.parseInt(ParametriGlobali.parametri.get(32))) {
////
////            for (int i = 0; i < pulizia.aliqCarico.size(); i++) {
////
////                if (percentualePeso
////                        < pulizia.aliqCarico.get(i)) {
////
////                    if (indexVel != i) {
////
////                        indexVel = i;
////
////                        String velCoclee = pulizia.velCarico.get(indexVel);
////
////                        //Cambio Vel
////                        pulizia.invCarico.cambioVelInverter(velCoclee);
////
////                        //Memorizzazione Log Processo
////                        PuliziaLogger.logger.info("Comando Cambio Vel Coclee - Nuova Vel =" + velCoclee);
////
////                        break;
////                    }
////                    break;
////                }
////            }
////        }
////
////    }
    //Finalizzazione Procedura Controllo Peso per Peso Raggiunto
    public void finalizzaInterruzioneManuale() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Finalizzazione Pulizia per Interruzione Manuale");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Interruzione Controllo Coclea Vuota");

        //Interruzione Thread di Controllo Coclea Vuota
        pulizia.interrompiControlloCocleaVuota = true;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Disattivazione Uscite Coclee");
        //Disattivazione Relay Coclea
        pulizia.gestoreReleCoclee(false);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Disattivazione Uscite Coclee Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Attesa Delay Inverter");
        //Attesa Delay Inverter
        try {
            ThreadPuliziaControlloPesoCarico.sleep(
                    Integer.parseInt(ParametriGlobali.parametri.get(8)));
        } catch (InterruptedException ex) {
        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Attesa Delay Inverter Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Chiusura Client di Pesa");

        //Chiusura del Client
        pesaCarico.chiudi();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Chiusura Client di Pesa Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Finalizzazione Pulizia per Interruzione Manuale Eseguita");

    }

    //Finalizzazione Procedura Controllo Peso per Peso Raggiunto
    public void finalizzaControlloPesoCaricoRaggiunto() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Finalizzazione Puliza per Peso Carico Raggiunto");

        /////////////////////////////////////////////
        // AVVIO THREAD AGGIORNAMENTO PESO REALE  ///
        /////////////////////////////////////////////
        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Avvio thread Aggiornamento Peso Reale");

        new ThreadAggiornaPesoRealeComponente().start();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Interruzione Controllo Coclea Vuota");

        //Interruzione Thread di Controllo Coclea Vuota
        pulizia.interrompiControlloCocleaVuota = true;

        /////////////////////////////
        // ATTESA VOLO MATERIALE  ///
        /////////////////////////////
        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Attesa Volo Materiale");

        //Inizializzazione Variabili di controllo tempo volo
        int timer = 0;
        int timeDelay = Integer.parseInt(ParametriGlobali.parametri.get(8));
        int timeVolo = Integer.parseInt(ParametriGlobali.parametri.get(9));

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.INFO, "Attesa Tempo Volo Materiale = {0}", Math.max(timeVolo, timeDelay));

        //Attesa Deposito Volo Materiale
        //Inizio loop
        while (timer < Math.max(timeVolo, timeDelay)) {

            //Aggiornamento Valore di Peso Visualizzato
            if (!pulizia.valorePesaCarico.equals("0")) {

                //Aggiornamento Label Visualizzazione Peso Pannello Processo
                ((Pannello39_Pulizia_Automatica) pulizia.pannelloPulizia).aggiornaLabelPesaCarico(pulizia.valorePesaCarico);

            }

            try {
                ThreadPuliziaControlloPesoCarico.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(187)));
            } catch (InterruptedException ex) {
            }

            timer += Integer.parseInt(ParametriSingolaMacchina.parametri.get(187));

        }//fine loop

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Attesa Volo Materiale Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Disattivazione Uscite Coclee");

        //Disattivazione Relay Coclea
        pulizia.gestoreReleCoclee(false);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Disattivazione Uscite Coclee Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.log(Level.INFO, "Aggiornamento Fattore Correttivo della Pesa - Quantit\u00e0 Pesata = {0}", pulizia.ultimoPesoBilanciaCarico);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Interruzione Aggiornamento Peso Reale");

        //Interruzione Thread Aggiornamento Peso Reale
        interrompiAggiornaPesoReale = true;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizializzazione Indice Valvola di Carico");

        //Inizializzazione Indice Valvola di Carico
        pulizia.indexValvolaCarico = 0;

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Avvio Procedura Scarico Materiali");

        //Avvio Procedura Scarico Materiali
        new ThreadPuliziaScaricoMateriali(pulizia).start();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Pesa Componente Completata");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Finalizzazione Puliza per Peso Carico Raggiunto Eseguita");

    }

    //Finalizzazione Procedura Controllo Peso per interruzione Manuale
    public void finalizzaControlloPesoCocleaVuota() {

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Inizio Finalizzazione Pulizia per Coclea Vuota");

        ///////////////////////////////
        // CHIUSURA CLIENT DI PESA  ///
        ///////////////////////////////
        //Memorizzazione Log Processo
        PuliziaLogger.logger.config(" Chiusura Client di Pesa");

        //Chiusura del Client
        pesaCarico.chiudi();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Chiusura Client di Pesa Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Attesa Tempo Delay Inverter");

        //Attesa Delay Inverter
        try {
            ThreadPuliziaControlloPesoCarico.sleep(
                    Integer.parseInt(ParametriGlobali.parametri.get(8)));
        } catch (InterruptedException ex) {
        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Attesa Tempo Delay Inverter Eseguita");

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Disattivazione Uscite Coclee");

        //Disattivazione Relay Coclea
        pulizia.gestoreReleCoclee(false);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.info("Disattivazione Uscite Coclee Eseguita");

        //Visualizzazione Message di Errore Coclea Vuota
        ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(27);

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Riattivazione Procedura di Controllo Pesa Componenti");

        //Analisi Stato Processo di Pesa
        pulizia.analizzaPesaComponenti();

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Finalizzazione Pulizia per Coclea Vuota Eseguita");

    }

    private class ThreadAggiornaPesoRealeComponente extends Thread {

        @Override
        public void run() {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Inizio Procedura Aggiornamento Peso Reale Componente");

            //Inizio loop 
            while (!interrompiAggiornaPesoReale) {

                letturaPesoCarico();

                //Memorizzazione Log Processo
                PuliziaLogger.logger.finer("Thread Aggiornamento Peso Reale Componente in Esecuzione");

            }//Fine loop

            //Memorizzazione Log Processo
            PuliziaLogger.logger.finer("Fine Thread Aggiornamento Peso Reale Componente");

            /////////////////////////////////////////////////
            // COMMUTAZIONE PESO NETTO  E CHIUSURA CLIENT ///
            /////////////////////////////////////////////////
            //Memorizzazione Log Processo
            PuliziaLogger.logger.fine("Commutazione Peso Netto");

            //Commutazione Peso Netto
            pesaCarico.commutaNetto();

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Commutazione Peso Netto Eseguita");

            //Chiusura del Client
            pesaCarico.chiudi();

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Chiusura Client di Pesa Eseguita");

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Fine Procedura Aggiornamento Peso Reale Componente");
        }
    }
}
