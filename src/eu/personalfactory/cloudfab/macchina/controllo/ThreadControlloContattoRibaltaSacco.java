/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_RIBALTA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_RIBALTA_SACCO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloContattoRibaltaSacco extends Thread {

    private Controllo controllo;

    //COSTRUTTORE
    public ThreadControlloContattoRibaltaSacco(Controllo controllo) {

        //Dichiarazione Variabile di Controllo
        this.controllo = controllo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Contatta Ribalta Sacco");

        boolean[] esitoTestContattoRibaltaSacco = new boolean[2];

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 738, ParametriSingolaMacchina.parametri.get(111))),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 739, ParametriSingolaMacchina.parametri.get(111))));

        esitoTestContattoRibaltaSacco[0] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_RIBALTA_SACCO)
                == ParametriSingolaMacchina.parametri.get(288).equals("1");

        //Imposta Visibilità Pulsanti 
        controllo.pannelloControllo.elemLabelSimple[0].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[1].setForeground(controllo.pannelloControllo.elemColor[2]);
        controllo.pannelloControllo.elemLabelSimple[25].setForeground(controllo.pannelloControllo.elemColor[2]);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.log(Level.INFO, "Esito Controllo Stato Iniziale Contatto Ribalta Sacco ={0}", esitoTestContattoRibaltaSacco[0]);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatto Ribalta Sacco : Attivazione Attuatore");

        if (controllo.pannelloControllo.isVisible()) {

            //Disattivazione Elettrovalvola Attuatore Ribalta Sacco
            GestoreIO_ModificaOut(USCITA_LOGICA_EV_RIBALTA_SACCO,
                    OUTPUT_TRUE_CHAR);

            try {
                ThreadControlloContattoRibaltaSacco.sleep(
                        Integer.parseInt(ParametriSingolaMacchina.parametri.get(289)));
            } catch (InterruptedException ex) {
            }

        }

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Controllo Contatto Ribalta Sacco : Disattivazione Attuatore");

        esitoTestContattoRibaltaSacco[1] = GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_RIBALTA_SACCO)
                != ParametriSingolaMacchina.parametri.get(288).equals("1");

        if (controllo.pannelloControllo.isVisible()) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_RIBALTA_SACCO,
                    OUTPUT_FALSE_CHAR);

        }

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.log(Level.INFO, "Esito Controllo Funzionamento Contatto RibaltaSacco :{0}", esitoTestContattoRibaltaSacco[1]);

        String s1 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 740, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + ParametriGlobali.parametri.get(46)
                + " ";
        String s2 = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 741, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + ParametriGlobali.parametri.get(46)
                + " ";

        if (esitoTestContattoRibaltaSacco[0]) {

            s1 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111)));
        } else {
            s1 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111)));
        }

        if (esitoTestContattoRibaltaSacco[1]) {

            s2 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111)));
        } else {
            s2 += EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111)));
        }

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 738, ParametriSingolaMacchina.parametri.get(111))),
                s1
                + HTML_BREAK_LINE
                + s2);

        //Registrazione Esito Test 
        controllo.esitoTest[22] = esitoTestContattoRibaltaSacco[0];
        controllo.esitoTest[23] = esitoTestContattoRibaltaSacco[1];

        if (esitoTestContattoRibaltaSacco[0]
                && esitoTestContattoRibaltaSacco[1]) {

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Contatto Ribalta Sacco Superato");

            controllo.pannelloControllo.impostaLabelImage(2);

        } else {

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.info("Controllo Contatto Ribalta Sacco Valvole Fallito");

            controllo.pannelloControllo.impostaLabelImage(1);
        }

        //Impostazione Val Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Fine Controllo Contatto Ribalta Sacco");
    }
}
