/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_ATTIVAZIONE_RIPETUTE_ARIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FREQUENZA_DISATTIVAZIONE_RIPETUTE_ARIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUMERO_RIPETUTE_ARIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoAttivaSvuotaValvolaRipetutoSecchi extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoAttivaSvuotaValvolaRipetutoSecchi(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        processo.interrompiThreadProcessoAttivaSvuotaValvolaRipetutoSecchi = false;
        int attivazioneEseguite = 0;

        ProcessoLogger.logger.log(Level.INFO, "Avvio Thread  Attiva Svuota Valvola Ripetuto Secchi");
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

            //Attivazione aria pulisci valvola
            GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA,
                    OUTPUT_TRUE_CHAR);

        }
        ProcessoLogger.logger.info("Apertura Elettrovalvola Pulisci Valvola");

        while (attivazioneEseguite < NUMERO_RIPETUTE_ARIA
                && !processo.interrompiThreadProcessoAttivaSvuotaValvolaRipetutoSecchi
                && !processo.pesoConfezioneRaggiunto) {
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                //Attivazione aria svuota valvola
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                        OUTPUT_TRUE_CHAR);
            }
            ProcessoLogger.logger.info("Apertura Elettrovalvola Svuota Valvola");

            //Aspetta Tempo di Chiusura 
            boolean interrompiTimerApertura = false;

            int numeroRipetizioniApertura = FREQUENZA_ATTIVAZIONE_RIPETUTE_ARIA / FRAZIONAMENTO_TEMPI;
            int counterApertura = 0;

            //Inizio loop 
            while (!interrompiTimerApertura
                    && !processo.interrompiThreadProcessoAttivaSvuotaValvolaRipetutoSecchi
                    && processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso
                    && !processo.pesoConfezioneRaggiunto) {

                counterApertura++;

                if (counterApertura >= numeroRipetizioniApertura) {

                    interrompiTimerApertura = true;

                }
                try {
                    ThreadProcessoAttivaSvuotaValvolaRipetutoSecchi.sleep(FRAZIONAMENTO_TEMPI);
                } catch (InterruptedException ex) {
                }

            }//fine loop
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                        OUTPUT_FALSE_CHAR);
            }

            ProcessoLogger.logger.info("Apertura Elettrovalvola Svuota Valvola");

            boolean interrompiTimerChiusura = false;

            int numeroRipetizioniChiusura = FREQUENZA_DISATTIVAZIONE_RIPETUTE_ARIA / FRAZIONAMENTO_TEMPI;
            int counterChiusura = 0;

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso
                    && !processo.pesoConfezioneRaggiunto) {

                //Inizio loop 
                while (!interrompiTimerChiusura
                        && !processo.interrompiThreadProcessoAttivaSvuotaValvolaRipetutoSecchi
                        && processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso
                        && !processo.pesoConfezioneRaggiunto) {

                    counterChiusura++;

                    if (counterChiusura >= numeroRipetizioniChiusura) {

                        interrompiTimerChiusura = true;

                    }
                    try {
                        ThreadProcessoAttivaSvuotaValvolaRipetutoSecchi.sleep(FRAZIONAMENTO_TEMPI);
                    } catch (InterruptedException ex) {
                    }

                }

            }

            attivazioneEseguite++;

        }//fine loop
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

            GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                    OUTPUT_FALSE_CHAR);

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.info("ThreadProcessoAttivaSvuotaValvolaRipetutoSecchi - Chiusura Elettrovalvola Svuota Tubo");

    }

}
