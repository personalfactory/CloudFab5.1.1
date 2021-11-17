package eu.personalfactory.cloudfab.macchina.panels;

import de.re.eeip.cip.exception.CIPException;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ChiudiEB80;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.REBOOT_COMMAND;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.SHUTDOWN_COMMAND;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.io.IOException;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello19_Spegnimento extends MyStepPanel {

    //VARIABILI
    public boolean esitoTest[];

    //COSTRUTTORE
    public Pannello19_Spegnimento() { 

        super();
 

        setLayer();
    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(3);
        impostaDimLabelHelp(0);
        impostaDimLabelTitle(0);
        impostaDimLabelProg(0);
        impostaDimLabelBut(3);
        impostaColori(1);

        //Inizializzazione Colori Label Simple
        initColorLabelSimple(elemColor[0]);

        //Inserimento Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Impostazione Visibilità Messaggi Aiuto
        impostaVisibilitaAiuto(true);

        //Impostazione Allineamento Label Simple
        for (int i = 0; i < elemLabelSimple.length; i++) {
            elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);
            elemLabelSimple[i].setVerticalAlignment(SwingConstants.CENTER);
        }

        //Avvio Thread Caricamento Immagine di Sfondo
        new ThreadCaricaImageSfondo(this).start();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Impostazione Visibilità Button Freccia
        butFreccia.setVisible(true);

        //Impostazione Visibilità Pannello
        setPanelVisibile();

        //Lettura Vocaboli Traducibili da Database
        new LeggiDizionario().start();

    }

    //Lettura Vocaboli Traducibili da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Simple
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 523, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 524, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[2].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 665, ParametriSingolaMacchina.parametri.get(111)));

            }
        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        switch (Integer.parseInt(button)) {

            case 0: {

            	
            	try {
            			GestoreIO_ChiudiEB80(); 
            	
            	} catch (CIPException ex) {
                } catch (IOException e) {
				}
            	
                try {

                    ///////////////////
                    // SPEGNIMENTO  ///
                    ///////////////////
                	
            
                    //Comando di Spegnimento
                	Runtime.getRuntime().exec(SHUTDOWN_COMMAND);

                } catch (Exception ex) {
                } 

                break;
            }
            case 1: {

            	try {
            		GestoreIO_ChiudiEB80(); 

            	} catch (CIPException ex) {
            	} catch (IOException e) {
            	}

            	try {

            		///////////////
            		// RIAVVIO  ///
            		///////////////

            		//Comando di Riavvio
            		Runtime.getRuntime().exec(REBOOT_COMMAND);

            	} catch (Exception ex) {}

            	break;
            }
        }

        //Chiusura Applicazione
        System.exit(0);

    }
}
