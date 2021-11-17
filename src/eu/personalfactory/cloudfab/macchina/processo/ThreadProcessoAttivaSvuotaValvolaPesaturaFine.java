/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoAttivaSvuotaValvolaPesaturaFine extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoAttivaSvuotaValvolaPesaturaFine(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine = false;

        int frequenza = Integer.parseInt(ParametriSingolaMacchina.parametri.get(358));
        int num_aperture = Integer.parseInt(ParametriSingolaMacchina.parametri.get(359));
        int aperture_eseguite = 0;

        ProcessoLogger.logger.log(Level.INFO, "Avvio Thread Svuotamento Valvola Pesatura Fine  - Frequenza={0} - numero aperture ={1}", new Object[]{frequenza, num_aperture});

        while (processo.pesoMancanteConfezioni > 0
                && num_aperture > aperture_eseguite
                && !processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine) {

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                // Attivazione aria svuota valvola
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                        OUTPUT_TRUE_CHAR);
            }

            ProcessoLogger.logger.info("Apertura Elettrovalvola Svuota Valvola");

            //Aspetta Tempo di Chiusura 
            boolean interrompiTimerApertura = false;

            int numeroRipetizioniApertura = frequenza / FRAZIONAMENTO_TEMPI;
            int counterApertura = 0;

            //Inizio loop 
            while (!interrompiTimerApertura
                    && !processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine
                    && processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso
                    && !processo.pesoConfezioneRaggiunto) {

                counterApertura++;

                if (counterApertura >= numeroRipetizioniApertura) {

                    interrompiTimerApertura = true;

                }
                try {
                    ThreadProcessoAttivaSvuotaValvolaPesaturaFine.sleep(FRAZIONAMENTO_TEMPI);
                } catch (InterruptedException ex) {
                }

            }//fine loop

            boolean interrompiTimerChiusura = false;

            int numeroRipetizioniChiusura = frequenza / FRAZIONAMENTO_TEMPI;
            int counterChiusura = 0;

            //Memorizzazione Log Processo
            ProcessoLogger.logger.info("Chiusura Elettrovalvola Svuota Valvola");

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso
                    && !processo.pesoConfezioneRaggiunto) {
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                            OUTPUT_FALSE_CHAR);
                }
                //Inizio loop 
                while (!interrompiTimerChiusura
                        && !processo.interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine
                        && processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso
                        && !processo.pesoConfezioneRaggiunto) {

                    counterChiusura++;

                    if (counterChiusura >= numeroRipetizioniChiusura) {

                        interrompiTimerChiusura = true;

                    }
                    try {
                        ThreadProcessoAttivaSvuotaValvolaPesaturaFine.sleep(FRAZIONAMENTO_TEMPI);
                    } catch (InterruptedException ex) {
                    }

                }

            }

            aperture_eseguite++;

        }//fine loop
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                    OUTPUT_FALSE_CHAR);
        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("ThreadProcessoAttivaSvuotaValvolaPesaturaFine - Chiusura Elettrovalvola Svuota Valvola");

    }

}
