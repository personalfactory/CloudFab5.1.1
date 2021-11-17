/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;

import java.util.logging.Level;

import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloContattiValvole extends Thread {

    private Controllo controllo;

    //COSTRUTTORE
    public ThreadControlloContattiValvole(Controllo controllo) {

        //Dichiarazione Variabile di Controllo
        this.controllo = controllo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Contatti Valvole");

        boolean[] esitoTestContattoValvolaCarico = new boolean[2];
        boolean[] esitoTestContattoValvolaScarico = new boolean[2];

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 460, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 467, ParametriSingolaMacchina.parametri.get(111))));

        //Controllo Stato Iniziale Contatto Valvola di Carico
        esitoTestContattoValvolaCarico[0] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO)
                == ParametriSingolaMacchina.parametri.get(19).equals("1");

        //Imposta Visibilità Pulsanti 
        //controllo.pannelloControllo.elemLabelSimple[0].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[1].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[24].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[25].setForeground(controllo.pannelloControllo.elemColor[2]);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.log(Level.INFO, "Esito Controllo Stato Iniziale Contatto Valvola di Carico ={0}", esitoTestContattoValvolaCarico[0]);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatto Valvola Carico : Apertura Valvola");

        if (controllo.pannelloControllo.isVisible()) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_TRUE_CHAR);

            try {
                ThreadControlloContattiValvole.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(21)));
            } catch (InterruptedException ex) {
            }

        }

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatto Valvola Carico : Chiusura Valvola");

        esitoTestContattoValvolaCarico[1] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO)
                != ParametriSingolaMacchina.parametri.get(19).equals("1");

        if (controllo.pannelloControllo.isVisible()) {

            //Attivazione Elettrovalvola Attuatore Valvola di Carico
            GestoreIO_ModificaOut(USCITA_LOGICA_EV_BILANCIA_DI_CARICO,
                    OUTPUT_FALSE_CHAR);

        }

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.log(Level.INFO, "Esito Controllo Funzionamento Contatto Valvola di Carico :{0}", esitoTestContattoValvolaCarico[1]);

        //Controllo Stato Iniziale Contatto Valvola di Carico
        esitoTestContattoValvolaScarico[0] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO)
                == ParametriSingolaMacchina.parametri.get(20).equals("1");

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.log(Level.INFO, "Esito Controllo Stato Iniziale Contatto Valvola di Scarico :{0}", esitoTestContattoValvolaScarico[0]);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatto Valvola Scarico : Apertura Valvola");

        try {
            ThreadControlloContattiValvole.sleep(Integer.parseInt(ParametriSingolaMacchina.parametri.get(227)));
        } catch (InterruptedException ex) {
        }

        if (controllo.pannelloControllo.isVisible()) {
        	 //Apertura Totale Valvola
        	
        	if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

        		////////////////////////////
        		// ATTUATORE MULTISTADIO  //
        		////////////////////////////
        		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);
        		ControlloLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");
        	} else {


        		////////////////////
        		// COMANDO UNICO  //
        		////////////////////
        		GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);
        		ControlloLogger.logger.log(Level.INFO, "Apertura Vavola POS_100_COMANDO_UNICO");

        	}

            try {
                ThreadControlloContattiValvole.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(21)));
            } catch (InterruptedException ex) {
            }

        }

        esitoTestContattoValvolaScarico[1] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO)
                != ParametriSingolaMacchina.parametri.get(20).equals("1");

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatto Valvola Scarico : Chiusura Valvola");

        //Chiusura Valvola
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

        	////////////////////////////
        	// ATTUATORE MULTISTADIO  //
        	////////////////////////////
        	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF); 
        	ControlloLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
        } else {

        	////////////////////
        	// COMANDO UNICO  //
        	////////////////////
        	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
        	ControlloLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO UNICO");

        }

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.log(Level.INFO, "Esito Controllo Funzionamento Contatto Valvola di Scarico :{0}", esitoTestContattoValvolaScarico[1]);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatti Valvola Eseguito");

        String s1 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 461, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + ParametriGlobali.parametri.get(46)
                + " ";
        String s2 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 482, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + ParametriGlobali.parametri.get(46)
                + " ";
        String s3 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 462, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + ParametriGlobali.parametri.get(46)
                + " ";
        String s4 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 483, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + ParametriGlobali.parametri.get(46)
                + " ";

        if (esitoTestContattoValvolaCarico[0]) {

            s1 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111)));
        } else {
            s1 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111)));
        }

        if (esitoTestContattoValvolaCarico[1]) {

            s2 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111)));
        } else {
            s2 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111)));
        }

        if (esitoTestContattoValvolaScarico[0]) {

            s3 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111)));
        } else {
            s3 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111)));
        }

        if (esitoTestContattoValvolaScarico[1]) {

            s4 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111)));
        } else {
            s4 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111)));
        }

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 460, ParametriSingolaMacchina.parametri.get(111))),
                s1
                + HTML_BREAK_LINE
                + s2
                + HTML_BREAK_LINE
                + s3
                + HTML_BREAK_LINE
                + s4);

        //Registrazione Esito Test 
        controllo.esitoTest[0] = esitoTestContattoValvolaCarico[0];
        controllo.esitoTest[1] = esitoTestContattoValvolaCarico[1];
        controllo.esitoTest[2] = esitoTestContattoValvolaScarico[0];
        controllo.esitoTest[3] = esitoTestContattoValvolaScarico[1];

        if (esitoTestContattoValvolaCarico[0]
                && esitoTestContattoValvolaCarico[1]
                && esitoTestContattoValvolaScarico[0]
                && esitoTestContattoValvolaScarico[1]) {

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Contatti Valvole Superato");

            controllo.pannelloControllo.impostaLabelImage(2);

        } else {

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Contatti Valvole Fallito");

            controllo.pannelloControllo.impostaLabelImage(1);
        }

        //Impostazione Val Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Fine Controllo Contatti Valvole");
    }
}
