/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraAllarme;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_F;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_G;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_H;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_I;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_J;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_K;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_COCLEA_L;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_PULSANTE_STOP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.THREAD_CONTROLLO_INTERRUTTORI_MANUALI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_THREAD_CONTROLLO_INTERRUTTORI_MANUALI;

import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoControlloInterruttoriManuali extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoControlloInterruttoriManuali(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Controllo Interruttori Manuali");

        //Inizializzazione Variabile di Controllo
        processo.interrompiThreadControlloInterruttoriManuali = false;

        processo.processoInterrottoManualmente = false;
        
        Boolean stato_def_contatti_coclee = Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(502));

        int counter = 0;

        //Inizio loop
        while (!processo.interrompiThreadControlloInterruttoriManuali
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {


        	try {
        		ThreadProcessoControlloInterruttoriManuali.sleep(FREQUENZA_THREAD_CONTROLLO_INTERRUTTORI_MANUALI);
        	} catch (InterruptedException ex) {
        	}

        	if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_PULSANTE_STOP)) {

        		///////////////////////////////
        		// PREMUTO IL PULSANTE STOP ///
        		///////////////////////////////

        		if (counter > THREAD_CONTROLLO_INTERRUTTORI_MANUALI) {

        			counter = 0;

        			//Memorizzazione Log Processo
        			ProcessoLogger.logger.warning("Premuto il Pulsante Stop");

        			//Lettura Ultimo Passo
        			int ultimoPasso = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54));

        			if (ultimoPasso == 1 || ultimoPasso == 2 || ultimoPasso == 3 || ultimoPasso == 4 || ultimoPasso == 5
        					|| ultimoPasso == 6 || ultimoPasso == 7 || ultimoPasso == 8) {

        				//Memorizzazione Log Processo
        				ProcessoLogger.logger.warning("Interruzione Confermata - Pulsante \"STOP\"");
        				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
        					//Registrazione Allarme su database
        					RegistraAllarme(6,
        							EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 900, ParametriSingolaMacchina.parametri.get(111))),
        							TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
        				}

        				/////////////////
        				// INTERROMPI  //
        				/////////////////

        				//Interruzione del Thread Corrente
        				processo.interrompiThreadControlloInterruttoriManuali = true;

        				//Variabile di Controllo Interruzione Manuale
        				processo.processoInterrottoManualmente = true;

        				//Interruzione Controllo Peso
        				processo.interruzioneControlloPesoInterruzioneManuale = true;

        				//Interruzione Processo
        				processo.interrompiProcesso();
        			}
        		} else {

        			counter++;

        			//Memorizzazione Log Processo
        			ProcessoLogger.logger.log(Level.FINE, "Interruzione Manuale - Premuto il Pulsante ''STOP'' - counter = {0}", counter);

        		}

        	} else if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE)) {

        		////////////////////////
        		// APERTO SPORTELLO  ///
        		////////////////////////

        		if (counter > THREAD_CONTROLLO_INTERRUTTORI_MANUALI) {

        			counter = 0;

        			int ultimoPasso = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54));

        			if (ultimoPasso != 4 && !processo.threadInserimentoManualeInEsecuzione) {

        				//Memorizzazione Log Processo
        				ProcessoLogger.logger.warning("Interruzione - Sportello Aperto");
        				if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
        					//Registrazione Allarme su database
        					RegistraAllarme(5,
        							EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 901, ParametriSingolaMacchina.parametri.get(111))),
        							TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
        				}

        				/////////////////
        				// INTERROMPI  //
        				/////////////////

        				//Interruzione del Thread Corrente
        				processo.interrompiThreadControlloInterruttoriManuali = true;

        				//Variabile di Controllo Interruzione Manuale
        				processo.processoInterrottoManualmente = true;

        				//Interruzione Controllo Peso
        				processo.interruzioneControlloPesoInterruzioneManuale = true;

        				//Interruzione Processo
        				processo.interrompiProcesso();

        			}
        		} else {

        			counter++;

        			//Memorizzazione Log Processo
        			ProcessoLogger.logger.log(Level.FINE, "Interruzione Manuale - Aperto Sportello Miscelatore - counter = {0}", counter);

        		} 

        	} else if ((Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(447)))
        			&& (!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_A)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_B)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_C)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_D)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_E)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_F)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_G)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_H)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_I)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_J)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_K)==stato_def_contatti_coclee
        			|| !GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_L)==stato_def_contatti_coclee)) {

        		/////////////////////////////
        		// CONTATTO COCLEA ATTIVO  //
        		/////////////////////////////
        		if (counter > THREAD_CONTROLLO_INTERRUTTORI_MANUALI) {

        			counter = 0;

        			String statoContatti
        			= Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_A)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_B)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_C)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_D)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_E)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_F)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_G)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_H)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_I)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_J)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_K)).substring(0, 1)
        			+ Boolean.toString(!GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_COCLEA_L)).substring(0, 1);

        			//Memorizzazione Log Processo
        			ProcessoLogger.logger.log(Level.WARNING, "Interruzione processo - Rilevato Contatto Coclea Attivo ={0}", statoContatti);

        			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
        				//Registrazione Allarme su database
        				RegistraAllarme(4,
        						statoContatti,
        						TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
        			}

        			/////////////////
        			// INTERROMPI  //
        			/////////////////
        			//Interruzione del Thread Corrente
        			processo.interrompiThreadControlloInterruttoriManuali = true;

        			//Variabile di Controllo Interruzione Manuale
        			processo.processoInterrottoManualmente = true;

        			//Interruzione Controllo Peso
        			processo.interruzioneControlloPesoInterruzioneManuale = true;

        			//Interruzione Processo
        			processo.interrompiProcesso();

        		} else {

        			counter++;

        			//Memorizzazione Log Processo
        			ProcessoLogger.logger.log(Level.FINE, "Interruzione processo - Rilevato Contatto Coclea Attivo - counter = {0}", counter);

        		}
        	}  else {

        		counter = 0;

        	}
        }//fine loop

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Controllo Interruttori Manuali");

    }
}
