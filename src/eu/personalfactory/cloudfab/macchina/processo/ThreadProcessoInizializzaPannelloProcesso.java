/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello11_Processo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

/**
 *
 * @author francescodigaudio
 */
public class ThreadProcessoInizializzaPannelloProcesso extends Thread {

    private final Pannello11_Processo pannelloProcesso;

    //COSTRUTTORE 
    public ThreadProcessoInizializzaPannelloProcesso(Pannello11_Processo pannelloProcesso) {

        //Dichiarazione Variabile Pannello
        this.pannelloProcesso = pannelloProcesso;

        //Impostazione Nome Thread Corrente
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizializzazione Pannello Processo");

        //Impostazione Iniziale Visibilità Tastiera
        pannelloProcesso.tastiera.impostaVisibilitaTastiera(false);

        //Impostazione Iniziale Visibilità Pulsanti 
        for (int i = 0; i < pannelloProcesso.elemBut.length; i++) {
            pannelloProcesso.elemBut[i].setVisible(false);
        }

        pannelloProcesso.impostaLabelTempoResiduoMiscelazione("");

        //Impostazione Iniziale Visibilità Pulsante Reset
        pannelloProcesso.elemLabelTitle[1].setText("");

        //Impostazione Iniziale Visibilità Labels
        for (int i = 0; i < pannelloProcesso.elemLabelSimple.length; i++) {

            pannelloProcesso.elemLabelSimple[i].setVisible(false);
        }

        for (int i = 0; i < 24; i++) {
            pannelloProcesso.elemLabelSimple[i].setText("");
        }

        if (!pannelloProcesso.valutaPresaDirettaEseguito) {
            pannelloProcesso.valutaPresaDirettaEseguito = true;
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(233))) {
                //Impostazione Visibilità Pulsante Linea Diretta
                pannelloProcesso.elemBut[5].setVisible(true);
                pannelloProcesso.elemLabelSimple[30].setVisible(true);
            }

            //Impostazione Visibilità Pulsante No Tracciabilita
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(361))) {
                //Impostazione Visibilità Disabilita Tracciabilita
                pannelloProcesso.elemBut[11].setVisible(true);
            }
        }

        //Impostazione Iniziale Visibilità Miscele da Pesare
        pannelloProcesso.elemLabelSimple[14].setVisible(true);
        pannelloProcesso.elemLabelSimple[14].setText(TrovaSingoloValoreParametroRipristino(8));

        //Impostazione Iniziale Visibilità Miscele Pesate
        pannelloProcesso.elemLabelSimple[15].setVisible(true);
        pannelloProcesso.elemLabelSimple[15].setText(TrovaSingoloValoreParametroRipristino(62));

        //Impostazione Iniziale Visibilità Confezioni da Pesare
        pannelloProcesso.elemLabelSimple[19].setVisible(true);
        pannelloProcesso.elemLabelSimple[19].setText(TrovaSingoloValoreParametroRipristino(9));

        //Impostazione Iniziale Visibilità Confezioni Pesate
        pannelloProcesso.elemLabelSimple[18].setVisible(true);
        pannelloProcesso.elemLabelSimple[18].setText(TrovaSingoloValoreParametroRipristino(68));

        //Impostazione Iniziale Visibilità TextField Inserimento Codici
        pannelloProcesso.txtField.setVisible(false);

        //Impostazione Visibilità Label Title
        pannelloProcesso.elemLabelTitle[3].setVisible(false);
        pannelloProcesso.elemLabelTitle[4].setVisible(false);
        pannelloProcesso.elemLabelTitle[7].setVisible(false);
        pannelloProcesso.elemLabelTitle[8].setVisible(false);
        pannelloProcesso.elemLabelTitle[13].setVisible(false);
        pannelloProcesso.elemLabelTitle[14].setVisible(false);
        pannelloProcesso.elemLabelTitle[15].setVisible(false);
        pannelloProcesso.elemLabelTitle[18].setVisible(false);

        pannelloProcesso.elemLabelTitle[16].setVisible(true);
        pannelloProcesso.elemLabelTitle[16].setText(HTML_STRINGA_INIZIO
                + TrovaSingoloValoreParametroRipristino(2)
                + " - "
                + TrovaSingoloValoreParametroRipristino(3)
                + HTML_STRINGA_FINE);

        //Impostazione Iniziale Visibilità Immagini Ausiliarie
        for (int i = 0; i < pannelloProcesso.labelImageAux.length; i++) {

            pannelloProcesso.labelImageAux[i].setVisible(false);
        }

        pannelloProcesso.labelImageAux[26].setVisible(true);
        pannelloProcesso.elemLabelSimple[32].setVisible(false);
        pannelloProcesso.elemLabelSimple[33].setVisible(false);
        pannelloProcesso.elemLabelSimple[34].setVisible(false);
        pannelloProcesso.elemLabelSimple[35].setVisible(false);
        pannelloProcesso.elemLabelSimple[36].setVisible(false); 
        pannelloProcesso.elemLabelSimple[37].setVisible(false);
        
        pannelloProcesso.elemLabelTitle[19].setVisible(false);
        pannelloProcesso.elemLabelTitle[20].setVisible(false);
        pannelloProcesso.elemLabelTitle[21].setVisible(false);
        pannelloProcesso.elemLabelTitle[22].setVisible(false);
        pannelloProcesso.elemLabelTitle[23].setVisible(false);
        
        //Impostazione Iniziale Visibilità Pulsante Reset
        pannelloProcesso.elemBut[2].setVisible(true);
        pannelloProcesso.elemLabelSimple[26].setVisible(true);
        
        pannelloProcesso.elemLabelSimple[35].setForeground(pannelloProcesso.elemColor[9]);
        pannelloProcesso.elemLabelSimple[37].setForeground(pannelloProcesso.elemColor[9]);

        pannelloProcesso.txtFieldAuxList.get(0).setVisible(false);
         
        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Inizializzazione Pannello Processo");
         

    }
}
