/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_F;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_G;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_H;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_I;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_J;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_K;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_CONTATTORE_COCLEA_L;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_F;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_G;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_H;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_I;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_J;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_K;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_L;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_BILANCIA_DI_CARICO_AUSILIARIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_RIBALTA_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_F;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_G;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_H;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_I;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_J;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_K;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_L;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_F;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_G;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_H;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_I;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_J;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_K;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_L;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO_AUSILIARIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO_AUSILIARIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_CHOPPER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_RUN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_NASTRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_SEGNALE_LUMINOSO_ROSSO;

import java.util.logging.Level;
import java.util.logging.Logger;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

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
public class ThreadProcessoResetProcesso extends Thread {

    Processo processo;

    public ThreadProcessoResetProcesso(Processo processo) {
        this.processo = processo;
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.warning("Chiamata Procedura Reset Processo");

        processo.resetProcesso = true;

        //Avvio Thread Inizializzazione Processo
        new ThreadProcessoInizializzaPannelloProcesso(processo.pannelloProcesso).start();

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Disattivazione Uscite Digitali");

        GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
                //+ USCITA_LOGICA_VIBRO_ELETTRICO_SILOS + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_SEGNALE_LUMINOSO_ROSSO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_MOTORE_CHOPPER + OUTPUT_SEP_CHAR
                //+ USCITA_LOGICA_MOTORE_VIBRO_BASE + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_NASTRO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_A + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_B + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_C + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_D + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_E + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_F + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_G + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_H + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_I + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_J + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_K + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_COCLEA_L + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_BILANCIA_DI_CARICO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA + OUTPUT_SEP_CHAR
                //+ USCITA_LOGICA_EV_SOFFIATORE_5_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
                //+ USCITA_LOGICA_EV_SOFFIATORE_6_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
//                + USCITA_LOGICA_EV_VALVOLA_SCARICO_1 + OUTPUT_SEP_CHAR
//                + USCITA_LOGICA_EV_VALVOLA_SCARICO_2 + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_RIBALTA_SACCO + OUTPUT_SEP_CHAR
               // + USCITA_LOGICA_EV_VIBRATORI_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_BILANCIA_DI_CARICO_AUSILIARIA + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO_AUSILIARIA + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO_AUSILIARIA + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_B + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_B + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_B + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_C + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_C + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_C + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_D + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_D + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_D + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_E + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_E + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_E + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_F + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_F + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_F + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_G + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_G + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_G + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_H + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_H + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_H + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_I + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_I + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_I + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_J + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_J + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_J + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_K + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_K + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_K + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_L + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_L + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_L + OUTPUT_SEP_CHAR
                + USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE,
                OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
               // + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
               // + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
               // + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
               // + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
               // + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
//                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
//                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                + OUTPUT_FALSE_CHAR);
        
        try {
        	
            Thread.sleep(200);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadProcessoResetProcesso.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Chiusura Valvola
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

        	///////////////////////////
        	// ATTUATORE MULTISTADIO //
        	///////////////////////////

        	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);  
        	ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
        } else {

        	////////////////////
        	// COMANDO UNICO  //
        	////////////////////

        	GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);  
        	ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");

        }
 
        processo.pannelloProcesso.elemBut[14].setVisible(false);

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Interruzione Procedure in Esecuzione");

        processo.aspiratoreAttivato = false;
 
        //Interruzione Thread Controllo Peso
        processo.interruzioneControlloPesoTastoReset = true;

        //Interruzione Animazione Carico    
        processo.interrompiAnimCarico = true;

        //Interruzione Thread Controllo Interruttori Manuali
        processo.interrompiThreadControlloInterruttoriManuali = true;

        //Interruzione Thread Controllo Interruttori Manuali
        processo.interrompiThreadControlloContattoPiattaforma = true;

        //Interuzione Miscelazione 
        processo.interrompiMiscelazione();

        //Interruzione Animazione Carico    
        processo.interrompiLetturaPesoBilanciaManuale = true;

        //Creazione Gestore Dialog
        if (((Pannello45_Dialog) processo.pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(3) == 1) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.warning("Chiusura Procedura in Seguito a Reset Processo");

            processo.pannelloProcesso.gestoreScambioPannello();

        } else {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.warning("Riavvio Procedura in Seguito a Reset Processo");

            processo.resetProcesso = false;

            processo.eseguitoResetManuale = true;

//            //Reset Linea Microdosaotri
//            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(459))) {
                //Avvio Thread Configurazione Micro dopo ripristino
                new ThreadProcessoGestoreMicrodosaturaConfigurazione(processo).start();
//            }

            //Aggiornamento Indice Sacchetto Bloccato su Database
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(46),
                    46,
                    "false",
                    ParametriSingolaMacchina.parametri.get(15));

        }

    }
}
