package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_LETTURA_PESO_BILANCIA_CARICO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoControlloBilanciaCaricoFinePesatura extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloBilanciaCaricoFinePesatura(Processo processo) {
        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Controllo Bilancia Carico Fine Pesatura");

        int pesoLordoCarico = 0;

        //Creazione Cliente Pesa Carico
        ClientPesaTLB4 pesaCarico;
        try {

        	  pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
        	  
//            pesaCarico = new ClientPesaTLB4(
//                    ID_BILANCIA_CARICO,
//                    ParametriSingolaMacchina.parametri.get(150),
//                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

            Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);
            
            //Lettura del Peso Lordo
            pesoLordoCarico = 0;
            int counter = 0;

            if (pesaCarico.verficaConn()) {

            	//Inizio loop
            	while (pesoLordoCarico == 0
            			&& counter < Integer.parseInt(ParametriSingolaMacchina.parametri.get(170))
            			&& !processo.resetProcesso) {

            		//Memorizzazione Log Processo
            		ProcessoLogger.logger.fine("Loop Controllo Peso Lordo in Esecuzione");

            		try {
            			pesoLordoCarico = Integer.parseInt(pesaCarico.pesoLordo());
            		} catch (NumberFormatException e) {
            		}

            		//Memorizzazione Log Processo
            		ProcessoLogger.logger.log(Level.FINE, "Pesa Carico - Peso Lordo ={0}", pesoLordoCarico);

            		counter++;

            		Thread.sleep(FREQUENZA_LETTURA_PESO_BILANCIA_CARICO);

            	}//fine loop
            }

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.CONFIG, "Peso Lordo Carico ={0}", pesoLordoCarico);

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Chiusura Socket Carico");

            //Chiusura del Client
            pesaCarico.chiudi();

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Socket Carico Chiuso");

        } catch (IOException ex) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Errore durante la Creazione del Client Pesa  e ={0}", ex);
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                RegistraAllarme(8,
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 896, ParametriSingolaMacchina.parametri.get(111)))
                        + " ="
                        + ex,
                        TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
            }
        } catch (InterruptedException e1) {
		}

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Verifica Peso Lordo ={0} > Valore di Riferimento ={1}",
                new Object[]{
                    pesoLordoCarico,
                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(47))
                    + Integer.parseInt(ParametriSingolaMacchina.parametri.get(22))});

        if (pesoLordoCarico
                > Integer.parseInt(ParametriSingolaMacchina.parametri.get(47))
                + Integer.parseInt(ParametriSingolaMacchina.parametri.get(22))) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.warning("Materiale non Completamente Scaricato dalla Valvola Superiore");
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                RegistraAllarme(9,
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 897, ParametriSingolaMacchina.parametri.get(111)))
                        + " ="
                        + pesoLordoCarico,
                        TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
            }

            if (!processo.processoInterrottoManualmente) {

                if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(7) == 0) {

                    //////////////////////
                    // RIPETI SCARICO  ///
                    //////////////////////
                	
                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.warning("Ripetizione Scarico Materiali");

                    //Riprova Scarico Materiali
                    new ThreadProcessoScaricoMateriali(processo).start();

                } else {

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.warning("Errore Peso Eccessivo Ignorato dall'Operatore");

                    if (!processo.processoInterrottoManualmente) {

                        //Registrazione Scarico Componenti su Database
                        processo.registraScaricoMateriali();

                        processo.aggiornaGestoreProcesso();
                    }

                }
            }

        } else {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.info("Controllo Bilancia di Carico Fine Pesatura Superato");

            TimeLineLogger.logger.log(Level.INFO, "Fine Procedura Scarico Materiali  - id_ciclo={0}", TrovaSingoloValoreParametroRipristino(91));

            if (!processo.processoInterrottoManualmente) {

                //Registrazione Scarico Componenti su Database
                processo.registraScaricoMateriali();

                processo.aggiornaGestoreProcesso();

            }

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Controllo Bilancia Carico Fine Pesatura");

    }
}
