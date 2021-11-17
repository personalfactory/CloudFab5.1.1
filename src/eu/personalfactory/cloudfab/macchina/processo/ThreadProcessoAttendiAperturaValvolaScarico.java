/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TEMPO_RITARDO_ATTIVAZIONE_INVERTER;

/**
 *
 * @author francescodigaudio Attesa Apertura Valvola di Scarico
 */
public class ThreadProcessoAttendiAperturaValvolaScarico extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoAttendiAperturaValvolaScarico(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Attesa Apertura Valvola di Scarico");

        //Inizializzazione Variabile di Controllo
        boolean interrompiAttendiAperturaScarico = false;

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))
                && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                    + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                    OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                    + OUTPUT_TRUE_CHAR);

        }

        //Disabilita Controllo Apertura Valvola - Utile in caso di malfunzionamento del contatto
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(334))) {

            //CONTROLLO VALVOLA DISABILITATO
            interrompiAttendiAperturaScarico = true;

        }

        //Inizio loop
        while (!interrompiAttendiAperturaScarico
                && processo.pannelloProcesso.isVisible()
                && !processo.resetProcesso) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.fine("Loop Attendi Apertura Valvola di Scarico in Esecuzione");

            if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO)
                    != (ParametriSingolaMacchina.parametri.get(20)).equals("1")
                    || GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO)) {

                ProcessoLogger.logger.info("Valvola di Scarico Aperta");

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Valvola di Scarico Aperta");

                interrompiAttendiAperturaScarico = true;

            } else {

                try {
                    ThreadProcessoAttendiAperturaValvolaScarico.sleep(
                            Integer.parseInt(ParametriSingolaMacchina.parametri.get(196)));
                } catch (InterruptedException ex) {
                }

            }
        }//fine loop

        //Memorizzazione Log Processo
        ProcessoLogger.logger.fine("Fine Loop Attendi Apertura Valvola di Scarico");

        try {
            ThreadProcessoAttendiAperturaValvolaScarico.sleep(TEMPO_RITARDO_ATTIVAZIONE_INVERTER);
        } catch (InterruptedException ex) {

        }

        //Controllo Peso Confezioni
        new ThreadProcessoControlloPesoConfezioni(processo).start();

        //Avvio Animazione Miscelatore
        new ThreadProcessoAnimazioneMiscelatore(processo).start();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Procedura Attesa Apertura Valvola di Scarico");

    }
}
