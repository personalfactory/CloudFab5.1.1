/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.CostruisciCodiceSacco;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiceProdottoPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciChimicaValidi;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaCodiciConfezioniPerCodiceProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author francescodigaudio
 *
 * Controllo Codici inseriti dall'Operatore
 */
public class ActionListenerControlloCodice implements ActionListener {

    //VARIABILI
    private Processo processo;

    //COSTRUTTORE
    public ActionListenerControlloCodice() {
    }

    public void SetProcesso(Processo processo) {
        this.processo = processo;

    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        switch (processo.pannelloProcesso.codType) {

            case 0: {

                //////////////////////
                // CODICE CHIMICA ////
                //////////////////////
                String codice = ae.getActionCommand().toUpperCase();

                //Reinizializza il Contenuto del text Field
                processo.pannelloProcesso.resetTextField();

                /////////////////////////////////////////////
                // CODICE RELATIVO A KIT CHIMICO SINGOLO  ///
                /////////////////////////////////////////////
                if (processo.prodotto.getNome().substring(
                        0,
                        ParametriSingolaMacchina.parametri.get(302).length())
                        .equals(
                                ParametriSingolaMacchina.parametri.get(302))
                        || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

                    String nomeProdotto = processo.prodotto.getNome();

                    for (int i = 0; i < nomeProdotto.length(); i++) {

                        if (nomeProdotto.charAt(i) == ParametriSingolaMacchina.parametri.get(303).charAt(0)) {
                            nomeProdotto = nomeProdotto.substring(i + 1, nomeProdotto.length());
                            break;

                        }

                    }

                    if (nomeProdotto.equals(codice)) {

                        ////////////////////////////////////////////
                        // CONTROLLO CODICE CHIMICA DISABILITATO  //
                        ////////////////////////////////////////////
                        //Indice Codice Chimica Inserito
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(57),
                                57,
                                "true",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Codice Chimica
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(58),
                                58,
                                codice,
                                ParametriSingolaMacchina.parametri.get(15));

                        processo.aggiornaGestoreProcesso();

                        //Impostazione Visibilità Sfondo Tastiera
                        processo.pannelloProcesso.labelImageAux[17].setVisible(false);

                        processo.pannelloProcesso.elemBut[1].setVisible(false);

                    } else {

                        //Codice Chimica Inserito non Valido
                        ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(8);
                    }

                } else if ( /////////////////////////////////////////
                        // CONTROLLO CODICE CHIMICA ABILITATO  //
                        /////////////////////////////////////////
                        //Controllo Lunghezza del Codice
                        codice.length() == Integer.parseInt(ParametriGlobali.parametri.get(14))
                        //Controllo Carattere Iniziale
                        && codice.substring(0, ParametriGlobali.parametri.get(15).length()).equals(
                                ParametriGlobali.parametri.get(15))
                        //Controllo Codice Prodotto
                        && codice.substring(ParametriGlobali.parametri.get(15).length(),
                                ParametriGlobali.parametri.get(15).length()
                                + processo.prodotto.getCodiceChimica().length()).equals(
                                processo.prodotto.getCodiceChimica())) {

                    String[] codiciChimica = TrovaCodiciChimicaValidi(processo.prodotto.getCodiceChimica());

                    boolean codiceValido = false;

                    for (String codiciChimica1 : codiciChimica) {
                        if (codice.equals(codiciChimica1)) {
                            codiceValido = true;

                            break;
                        }
                    }

                    if (codiceValido) {

                        //Indice Codice Chimica Inserito
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(57),
                                57,
                                "true",
                                ParametriSingolaMacchina.parametri.get(15));

                        //Codice Chimica
                        AggiornaValoreParametriRipristino(processo,
                                TrovaIndiceTabellaRipristinoPerIdParRipristino(58),
                                58,
                                codice,
                                ParametriSingolaMacchina.parametri.get(15));

                        processo.aggiornaGestoreProcesso();

                        //Impostazione Visibilità Sfondo Tastiera
                        processo.pannelloProcesso.labelImageAux[17].setVisible(false);

                        processo.pannelloProcesso.elemBut[1].setVisible(false);

                    } else {

                        //Codice Chimica Inserito non Valido
                        ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(8);
                    }

                } else {

                    //Codice Chimica Inserito non Valido
                    ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(8);

                }

                break;
            }

            case 1: {

                ///////////////////////////
                /// CODICE SACCHETTO   ////
                /////////////////////////// 
                String codice = ae.getActionCommand().toUpperCase();
                boolean codiceNonValido = false;
                boolean codiceValido = false;

                //Reinizializza il Contenuto del text Field
                processo.pannelloProcesso.resetTextField();
 
                if ( //Controllo Lunghezza del Codice
                        codice.length() == Integer.parseInt(ParametriGlobali.parametri.get(16))
                        //Controllo Carattere Iniziale
                        && codice.substring(0, ParametriGlobali.parametri.get(17).length()).equals(
                                ParametriGlobali.parametri.get(17))) {

                    //Controllo Codice Prodotto
                    if (!codice.substring(
                            ParametriGlobali.parametri.get(17).length(),
                            ParametriGlobali.parametri.get(17).length()
                            + processo.prodotto.getCodiceProdotto().length()).equals(
                            processo.prodotto.getCodiceProdotto())
                            && !codice.substring(
                                    ParametriGlobali.parametri.get(17).length(),
                                    ParametriGlobali.parametri.get(17).length()
                                    + processo.prodotto.getCodiceChimica().length()).equals(
                                    processo.prodotto.getCodiceChimica())) {

                        codiceNonValido = true;

                    } else {

                        //Controllo Codice Ripetuto
                        ArrayList<String> codiciSacchetti = TrovaCodiciConfezioniPerCodiceProdotto(TrovaCodiceProdottoPerIdProdotto(processo.prodotto.getId()));

                        for (int i = 0; i < codiciSacchetti.size(); i++) {

                            if (codice.equals(codiciSacchetti.get(i))) {

                                codiceNonValido = true;

                                break;
                            }

                        }

                    }

                } else {

                    codiceNonValido = true;
                }

                //Modifica Maggio 2015 (Per risolvere il problema dell'aggiornamento del Passo dopo il blocco sacco)
                if (processo.prodotto.getNome().substring(0, ParametriSingolaMacchina.parametri.get(302).length())
                        .equals(ParametriSingolaMacchina.parametri.get(302))
                        || processo.prodotto.getNome().substring(0, ParametriSingolaMacchina.parametri.get(143).length())
                                .equals(ParametriSingolaMacchina.parametri.get(143))
                        || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

                    codiceValido = true;
                    codiceNonValido = false;

                }
                ////////////

                if (codiceNonValido) {

                    if (((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(9) == 1) {

                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(448))) {
                            codice = CostruisciCodiceSacco(processo);
                        }
                        codiceValido = true; 
                    }

                } else {
                    codiceValido = true;
                }

                if (codiceValido) {

                    //Indice Codice Sacchetto Inserito
                    AggiornaValoreParametriRipristino(processo,
                            TrovaIndiceTabellaRipristinoPerIdParRipristino(46),
                            46,
                            "true",
                            ParametriSingolaMacchina.parametri.get(15));

                    //Codice Sacchetto
                    AggiornaValoreParametriRipristino(processo,
                            TrovaIndiceTabellaRipristinoPerIdParRipristino(47),
                            47,
                            codice,
                            ParametriSingolaMacchina.parametri.get(15));

                    //Interrompe Animazione Blocco Sacchetto
                    processo.interrompiAnimConfezione = true;

                    processo.pannelloProcesso.modificaPannello(13);

                    //Impostazione Visibilità Sfondo Tastiera
                    processo.pannelloProcesso.labelImageAux[17].setVisible(false);

                    processo.pannelloProcesso.elemBut[1].setVisible(false);

                    //Memorizzazione Logger Processo
                    ProcessoLogger.logger.log(Level.INFO, "Codice Sacco ={0}", codice);

                }
                break;
            }
        }

    }
}
