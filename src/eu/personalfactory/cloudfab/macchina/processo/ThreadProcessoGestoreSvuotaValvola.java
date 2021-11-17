/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FRAZIONAMENTO_TEMPI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoGestoreSvuotaValvola extends Thread {

    private final Processo processo;

    //COSTRUTTORE
    public ThreadProcessoGestoreSvuotaValvola(Processo processo) {

        //Dichiarazione Variabile Processo
        this.processo = processo;

        //Impostazione Nome del Thread
        this.setName(this.getClass().getSimpleName());

    }

    @Override
    public void run() {

        try {
        	
        	ClientPesaTLB4  pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
        	 
            int tempoApertura_def = Integer.parseInt(ParametriSingolaMacchina.parametri.get(353));
            int tempoChiusura_def = Integer.parseInt(ParametriSingolaMacchina.parametri.get(354));

            int tempoApertura = Integer.parseInt(ParametriSingolaMacchina.parametri.get(353));
            int tempoChiusura = Integer.parseInt(ParametriSingolaMacchina.parametri.get(354));

            ProcessoLogger.logger.log(Level.INFO,
                    "Avvio Thread Gestione Svuota Valvola - tempoApertura ={0}- tempoChiusura ={1}",
                    new Object[]{tempoApertura, tempoChiusura});

            processo.interrompiThreadGestoreSvuotaValvola = false;

            while (processo.pesoMancanteConfezioni
                    >= Integer.parseInt(ParametriSingolaMacchina.parametri.get(335))
                    && !processo.interrompiThreadGestoreSvuotaValvola) {

                if (processo.pesoMancanteConfezioni > Integer.parseInt(ParametriSingolaMacchina.parametri.get(354))) {
                    tempoApertura = (int) (tempoApertura_def * ((double) processo.pesoMancanteConfezioni / processo.pesoDaRaggiungereConfezione[processo.indiceConfezioneInPesa]));
                    tempoChiusura = (int) (tempoChiusura_def * ((double) processo.pesoMancanteConfezioni / processo.pesoDaRaggiungereConfezione[processo.indiceConfezioneInPesa]));
                }

                ProcessoLogger.logger.log(Level.INFO,
                        "Ricalcolo Tempi Fluidificatori - tempoApertura ={0}- tempoChiusura ={1}",
                        new Object[]{tempoApertura, tempoChiusura});

                //MODIFICA Settembre 2021
                
                //Lettura PesoCarico
                int pesoCarico = 0;
                try {
                	Integer.parseInt(pesaCarico.pesoLordo());

                } catch (NumberFormatException e) {}
                
                
                if (pesoCarico>-20000) {
                
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {
                    
                    GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                                OUTPUT_TRUE_CHAR);
                }
                }
                ProcessoLogger.logger.info("Apertura Elettrovalvola Svuota Valvola");

                //Aspetta Tempo di Chiusura 
                boolean interrompiTimerApertura = false;

                int numeroRipetizioniApertura = tempoApertura / FRAZIONAMENTO_TEMPI;
                int counterApertura = 0;

                //Inizio loop 
                while (!interrompiTimerApertura
                        && !processo.interrompiThreadGestoreSvuotaValvola
                        && processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso
                        && !processo.pesoConfezioneRaggiunto) {

                    counterApertura++;

                    if (counterApertura >= numeroRipetizioniApertura) {

                        interrompiTimerApertura = true;

                    }
                    try {
                        ThreadProcessoGestoreSvuotaValvola.sleep(FRAZIONAMENTO_TEMPI);
                    } catch (InterruptedException ex) {
                    }

                }//fine loop

                boolean interrompiTimerChiusura = false;

                int numeroRipetizioniChiusura = tempoChiusura / FRAZIONAMENTO_TEMPI;
                int counterChiusura = 0;

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Chiusura Elettrovalvola Fludificatori");

                if (processo.pannelloProcesso.isVisible()
                        && !processo.resetProcesso
                        && !processo.pesoConfezioneRaggiunto) {

                    if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

                         GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                                OUTPUT_FALSE_CHAR);
                        
                    }

                    //Inizio loop 
                    while (!interrompiTimerChiusura
                            && !processo.interrompiThreadGestoreSvuotaValvola
                            && processo.pannelloProcesso.isVisible()
                            && !processo.resetProcesso
                            && !processo.pesoConfezioneRaggiunto) {

                        counterChiusura++;

                        if (counterChiusura >= numeroRipetizioniChiusura) {

                            interrompiTimerChiusura = true;

                        }
                        try {
                            ThreadProcessoGestoreSvuotaValvola.sleep(FRAZIONAMENTO_TEMPI);
                        } catch (InterruptedException ex) {
                        }
                    }
                }

            }//fine loop
            

        	pesaCarico.chiudi();

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {
                
                   GestoreIO_ModificaOut(USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA,
                                OUTPUT_FALSE_CHAR);
            }

            //Memorizzazione Log Processo
            ProcessoLogger.logger.info("Fine Thread Gestione Svuota - Chiusura Elettrovalvola Svuota Valvola");

        } catch (NumberFormatException e) {
            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Errore durante l''esecuzione del Thread Gestore Svuota Valvola - e:{0}", e);

        } catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    }

}
