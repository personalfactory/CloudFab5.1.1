/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.controllo;

import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 */
public class ThreadControlloBilancia extends Thread {

    private Controllo controllo;
    private Integer idPesaCorrente;
    private String strBilanciaCorrente;
    

    //COSTRUTTORE
    public ThreadControlloBilancia(Controllo controllo, Integer idPesaCorrente, String strBilanciaCorrente) {

        //Dichiarazione Variabile Processo
        this.controllo = controllo;

        //Dichiarazione Variabili Controllo
        this.idPesaCorrente = idPesaCorrente;
        this.strBilanciaCorrente = strBilanciaCorrente;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Inizio Controllo Bilancia");

        //Impostazione Visbilità Pulsanti Controllo Test
        controllo.pannelloControllo.impostaVisibilitaPulsantiBilancia(true);
    	
        
        try {

			// Client Pesa
			ClientPesaTLB4 pesa = new ClientPesaTLB4(idPesaCorrente);

//        ClientPesaTLB4 pesa = new ClientPesaTLB4(
//                idPesaCorrente,
//                ParametriSingolaMacchina.parametri.get(150),
//                Integer.parseInt(ParametriSingolaMacchina.parametri.get(151)));

			// Inzializzazione Variabile Controllo
			boolean interrompiControlloPesa = false;

        //Impostazione Vis Label Peso Bilancia
        controllo.pannelloControllo.impostaVisLabelPesoBilancia(true);

        controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 477, ParametriSingolaMacchina.parametri.get(111)))
                + " "
                + EstraiStringaHtml(strBilanciaCorrente),
                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 481, ParametriSingolaMacchina.parametri.get(111))));

        //Inizio loop
        while (!interrompiControlloPesa
                && controllo.pannelloControllo.isVisible()) {

            try {

                ThreadControlloBilancia.sleep(100);

                if (controllo.commutaNetto) {

                    //////////////////////////
                    // COMMUTA PESO NETTO  ///
                    //////////////////////////
                    controllo.commutaNetto = false;

                    pesa.commutaNetto();

                } else if (controllo.commutaLordo) {

                    //////////////////////////
                    // COMMUTA PESO LORDO  ///
                    //////////////////////////
                    controllo.commutaLordo = false;

                    pesa.commutaLordo();

                } else if (idPesaCorrente != 2) {

                    //Impostazione Label Peso Bilancia
                    controllo.pannelloControllo.impostaLabelPesoBilancia(pesa.pesoNetto());

                } else {
                    //Impostazione Label Peso Bilancia
                    controllo.pannelloControllo.impostaLabelPesoBilancia(
                            Double.toString(
                                    Double.parseDouble(pesa.pesoNetto())
                                    / Integer.parseInt(ParametriSingolaMacchina.parametri.get(223))));
                }

            } catch (InterruptedException | NumberFormatException e) {
            }

            if (controllo.testSuperato) {
                controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 477, ParametriSingolaMacchina.parametri.get(111))),
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 477, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + ParametriGlobali.parametri.get(46)
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 463, ParametriSingolaMacchina.parametri.get(111))));

                //Memorizzazione Logger Procedure di Controllo
                ControlloLogger.logger.info("Controllo Bilancia Superato");

                controllo.pannelloControllo.impostaLabelImage(2);

                controllo.esitoTest[12 + idPesaCorrente] = true;

                interrompiControlloPesa = true;

                controllo.testSuperato = false;

            } else if (controllo.testFallito) {

                controllo.pannelloControllo.impostaMessaggioEsito(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 477, ParametriSingolaMacchina.parametri.get(111))),
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 477, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + ParametriGlobali.parametri.get(46)
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 464, ParametriSingolaMacchina.parametri.get(111))));

                controllo.esitoTest[12 + idPesaCorrente] = false;

                controllo.pannelloControllo.impostaLabelImage(1);

                //Memorizzazione Logger Procedure di Controllo
                ControlloLogger.logger.warning("Controllo Bilancia Fallito");

                interrompiControlloPesa = true;

                controllo.testFallito = false;

            }

        }//fine loop
        
        


        //Chiusura del Client
        pesa.chiudi();
        
        } catch (IOException ex) {

            //Memorizzazione Logger Procedure di Controllo
            ControlloLogger.logger.log(Level.SEVERE, "Errore durante la creazione del Client Pesa e={0}", ex);
        }

        //Impostazione Vis Label Peso Bilancia
        controllo.pannelloControllo.impostaVisLabelPesoBilancia(false);

        //Impostazione Val Pulsanti Scelta Test
        controllo.pannelloControllo.impostaAbilitazionePulsanti(true);

        //Impostazione Visibilita Pulsante Controllo Esito Test
        controllo.pannelloControllo.impostaVisibilitaPulsanteControlloTest(false);

        //Impostazione Visibilita Pulsante Bilance
        controllo.pannelloControllo.impostaVisibilitaPulsantiBilancia(false);

        //Memorizzazione Logger Procedure di Controllo
        ControlloLogger.logger.config("Fine Controllo Bilancia");
    }
}
