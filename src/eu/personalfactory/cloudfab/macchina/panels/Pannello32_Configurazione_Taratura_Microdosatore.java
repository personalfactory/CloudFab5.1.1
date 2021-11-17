package eu.personalfactory.cloudfab.macchina.panels;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Pannello32_Configurazione_Taratura_Microdosatore extends MyStepPanel {

    //PARAMETRI PANNELLO
    private final int numImage = 10;
////////    //VARIABILI
////////    private int microDosatoreSelezionato;
////////    private int indexLabelAux; 
////////    private double fattoreZero;
////////    private double fattoreFondoScala; 
    @SuppressWarnings("unused")
	private boolean scambioPannello = false;
////////    private String valoreInputAnalogico = "";
    //COSTRUTTORE

    public Pannello32_Configurazione_Taratura_Microdosatore() {

        super();

        setLayer();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(0);
        impostaDimLabelSimple(11);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(8);
        impostaDimLabelProg(0);
        impostaDimLabelBut(3);
        impostaColori(3);

        //Inizializzazione Colori Label Tipo Simple
        initColorLabelSimple(elemColor[0]);

        //Inizializzazione Colori Label Tipo Help
        initColorLabelHelp(elemColor[1]);

        //Inizializzazione Colori Label Tipo Title
        initColorLabelTitle(elemColor[2]);

        //Inserimento Pulsante Button Freccia
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();
 
        //Avvio Thread Caricamento Immagini Ausiliarie
        new ThreadInserisciLabelImageAux(this, numImage, true).start();

        //Impostazione Allineamento Label Tipo Simple
        for (int i = 0; i < elemLabelSimple.length; i++) {

            if (i < 3) {

                elemLabelSimple[i].setHorizontalAlignment(SwingConstants.CENTER);

            } else {

                elemLabelSimple[i].setHorizontalAlignment(SwingConstants.RIGHT);
            }

            if (i < 10) {

                elemLabelSimple[i].setVerticalAlignment(SwingConstants.BOTTOM);

            } else {

                elemLabelSimple[i].setVerticalAlignment(SwingConstants.TOP);

            }
        }

        //Impostazione Allineamento Label Tipo Title
        for (int i = 0; i < elemLabelTitle.length; i++) {

            elemLabelTitle[i].setHorizontalAlignment(SwingConstants.LEFT);

            elemLabelTitle[i].setVerticalAlignment(SwingConstants.BOTTOM);
        }

    }
//////////////////////////
//////////////////////////    //Inizializzazione Pannello
//////////////////////////    public void initPanel(int microDosatoreSelezionato) {
//////////////////////////
//////////////////////////        //Inizializzazione Testo Text Field
//////////////////////////        txtField.setText("");
//////////////////////////
//////////////////////////        //Inizializzazione Variabile di Controllo
//////////////////////////        this.microDosatoreSelezionato = microDosatoreSelezionato;
//////////////////////////
//////////////////////////        //Impostazione Visibilità Button Freccia
//////////////////////////        butFreccia.setVisible(true);
//////////////////////////
//////////////////////////        //Lettura Vocaboli Traducibili da Database
//////////////////////////        new LeggiDizionario().start();
//////////////////////////
//////////////////////////
//////////////////////////        elemLabelTitle[0].setText(
//////////////////////////                FabCloudConstants.HTML_STRINGA_INIZIO
//////////////////////////                + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 731, ParametriSingolaMacchina.parametri.get(111))) + " "
//////////////////////////                + FabCloudConstants.CHAR_PRESA_MICRODOSATORE + microDosatoreSelezionato
//////////////////////////                + FabCloudConstants.HTML_STRINGA_FINE);
//////////////////////////
//////////////////////////
//////////////////////////        //Impostazione Visibilità Pannello
//////////////////////////        setPanelVisibile();
//////////////////////////
//////////////////////////        //Impostazione Visibilità Messsaggi Aiuto 
//////////////////////////        impostaVisibilitaAiuto(true);
//////////////////////////
//////////////////////////        //Impostazione Visibilità 
//////////////////////////        impostaVisButAvanti(true);
//////////////////////////        impostaVisButAnnulla(false);
//////////////////////////
//////////////////////////        //Inizializzazione Indice Label Aux
//////////////////////////        indexLabelAux = 0;
//////////////////////////
//////////////////////////        //Impostazione Visibilità Label Aux
//////////////////////////        impostaVisComponenti();
//////////////////////////
//////////////////////////        //Aggiornamento Testo Label Tipo Simple
//////////////////////////        elemLabelSimple[10].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 613, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////
//////////////////////////
//////////////////////////        //Aggiornamento Testo Label Simple
//////////////////////////        elemLabelSimple[4].setText(ParametriSingolaMacchina.parametri.get(247));
//////////////////////////        elemLabelSimple[8].setText(ParametriSingolaMacchina.parametri.get(247));
//////////////////////////
//////////////////////////        new ThreadLeggiIngressoAnalogico(this).start();
//////////////////////////
//////////////////////////    }
//////////////////////////    //Lettura Vocaboli Traducibili da Database
//////////////////////////
//////////////////////////    private class LeggiDizionario extends Thread {
//////////////////////////
//////////////////////////        @Override
//////////////////////////        public void run() {
//////////////////////////
//////////////////////////
//////////////////////////            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {
//////////////////////////
//////////////////////////                //Impostazione Lingua Pannello
//////////////////////////                language = ParametriSingolaMacchina.parametri.get(111);
//////////////////////////
//////////////////////////                //Aggiornamento Testi Label Tipo Title
//////////////////////////                elemLabelTitle[1].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 607, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelTitle[2].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 608, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelTitle[3].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 607, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelTitle[4].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 609, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelTitle[5].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 610, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelTitle[6].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 611, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelTitle[7].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 612, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////
//////////////////////////                //Aggiornamento Testi Label Tipo Help
//////////////////////////                elemLabelHelp[0].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 601, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelHelp[1].setText(FabCloudConstants.HTML_STRINGA_INIZIO
//////////////////////////                        + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 602, ParametriSingolaMacchina.parametri.get(111))) + " :"
//////////////////////////                        + FabCloudConstants.HTML_BREAK_LINE
//////////////////////////                        + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 603, ParametriSingolaMacchina.parametri.get(111)))
//////////////////////////                        + FabCloudConstants.HTML_BREAK_LINE
//////////////////////////                        + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 604, ParametriSingolaMacchina.parametri.get(111)))
//////////////////////////                        + FabCloudConstants.HTML_BREAK_LINE
//////////////////////////                        + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 605, ParametriSingolaMacchina.parametri.get(111)))
//////////////////////////                        + FabCloudConstants.HTML_BREAK_LINE
//////////////////////////                        + Benefit.estraiStringaHtml(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 606, ParametriSingolaMacchina.parametri.get(111)))
//////////////////////////                        + FabCloudConstants.HTML_BREAK_LINE
//////////////////////////                        + FabCloudConstants.HTML_STRINGA_FINE);
//////////////////////////
//////////////////////////                //Aggiornamento Testi Label Tipo Simple
//////////////////////////                elemLabelSimple[0].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 598, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelSimple[1].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 599, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////                elemLabelSimple[2].setText(Benefit.findVocabolo(FabCloudConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA, 600, ParametriSingolaMacchina.parametri.get(111)));
//////////////////////////
//////////////////////////            }
//////////////////////////
//////////////////////////        }
//////////////////////////    }
//////////////////////////
//////////////////////////    //Impostazione Visibilità Pulsanti "AVANTI"
//////////////////////////    private void impostaVisButAvanti(boolean vis) {
//////////////////////////
//////////////////////////        elemBut[1].setVisible(vis);
//////////////////////////        elemBut[2].setVisible(!vis);
//////////////////////////
//////////////////////////        elemLabelSimple[1].setVisible(vis);
//////////////////////////        elemLabelSimple[2].setVisible(!vis);
//////////////////////////
//////////////////////////    }
//////////////////////////
//////////////////////////    //Impostazione Visibilità Pulsante "ANNULLA"
//////////////////////////    private void impostaVisButAnnulla(boolean vis) {
//////////////////////////
//////////////////////////        elemBut[0].setVisible(vis);
//////////////////////////        elemLabelSimple[0].setVisible(vis);
//////////////////////////
//////////////////////////    }
//////////////////////////
//////////////////////////    //Impostazione Visibilità Componenti Pannello
//////////////////////////    private void impostaVisComponenti() {
//////////////////////////
//////////////////////////        switch (indexLabelAux) {
//////////////////////////            case 0: {
//////////////////////////
//////////////////////////                //Impostazione Visibilità Immagini Ausiliarie
//////////////////////////                labelImageAux[0].setVisible(true);
//////////////////////////                labelImageAux[1].setVisible(false);
//////////////////////////                labelImageAux[2].setVisible(false);
//////////////////////////                labelImageAux[3].setVisible(false);
//////////////////////////                labelImageAux[4].setVisible(false);
//////////////////////////                labelImageAux[5].setVisible(false);
//////////////////////////                labelImageAux[6].setVisible(false);
//////////////////////////                labelImageAux[7].setVisible(true);
//////////////////////////                labelImageAux[8].setVisible(false);
//////////////////////////                labelImageAux[9].setVisible(false);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Label Tipo Title
//////////////////////////                elemLabelTitle[1].setVisible(true);
//////////////////////////                elemLabelTitle[2].setVisible(false);
//////////////////////////                elemLabelTitle[3].setVisible(false);
//////////////////////////                elemLabelTitle[4].setVisible(false);
//////////////////////////                elemLabelTitle[5].setVisible(false);
//////////////////////////                elemLabelTitle[6].setVisible(false);
//////////////////////////                elemLabelTitle[7].setVisible(false);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Label Tipo Simple
//////////////////////////                elemLabelSimple[3].setVisible(true);
//////////////////////////                elemLabelSimple[4].setVisible(false);
//////////////////////////                elemLabelSimple[5].setVisible(false);
//////////////////////////                elemLabelSimple[6].setVisible(false);
//////////////////////////                elemLabelSimple[7].setVisible(false);
//////////////////////////                elemLabelSimple[8].setVisible(false);
//////////////////////////                elemLabelSimple[9].setVisible(false);
//////////////////////////                elemLabelSimple[10].setVisible(true);
//////////////////////////
//////////////////////////                indexLabelAux++;
//////////////////////////
//////////////////////////                fattoreZero = 0;
//////////////////////////
//////////////////////////                fattoreFondoScala = 1;
//////////////////////////
//////////////////////////                break;
//////////////////////////            }
//////////////////////////            case 1: {
//////////////////////////
//////////////////////////                //Impostazione Visibilità Immagini Ausiliarie
//////////////////////////                labelImageAux[0].setVisible(false);
//////////////////////////                labelImageAux[1].setVisible(true);
//////////////////////////                labelImageAux[2].setVisible(true);
//////////////////////////                labelImageAux[3].setVisible(false);
//////////////////////////                labelImageAux[4].setVisible(false);
//////////////////////////                labelImageAux[5].setVisible(false);
//////////////////////////                labelImageAux[6].setVisible(false);
//////////////////////////                labelImageAux[7].setVisible(false);
//////////////////////////                labelImageAux[8].setVisible(true);
//////////////////////////                labelImageAux[9].setVisible(false);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Label Title
//////////////////////////                elemLabelTitle[1].setVisible(false);
//////////////////////////                elemLabelTitle[2].setVisible(true);
//////////////////////////                elemLabelTitle[3].setVisible(true);
//////////////////////////                elemLabelTitle[4].setVisible(false);
//////////////////////////                elemLabelTitle[5].setVisible(false);
//////////////////////////                elemLabelTitle[6].setVisible(false);
//////////////////////////                elemLabelTitle[7].setVisible(false);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Label Simple
//////////////////////////                elemLabelSimple[3].setVisible(false);
//////////////////////////                elemLabelSimple[4].setVisible(true);
//////////////////////////                elemLabelSimple[5].setVisible(true);
//////////////////////////                elemLabelSimple[6].setVisible(false);
//////////////////////////                elemLabelSimple[7].setVisible(false);
//////////////////////////                elemLabelSimple[8].setVisible(false);
//////////////////////////                elemLabelSimple[9].setVisible(false);
//////////////////////////                elemLabelSimple[10].setVisible(false);
//////////////////////////
//////////////////////////                indexLabelAux++;
//////////////////////////                 
//////////////////////////                fattoreZero = Double.parseDouble(valoreInputAnalogico);
//////////////////////////
//////////////////////////                break;
//////////////////////////            }
//////////////////////////            case 2: {
//////////////////////////
//////////////////////////                //Impostazione Visibilità Immagini Ausiliarie
//////////////////////////                labelImageAux[0].setVisible(false);
//////////////////////////                labelImageAux[1].setVisible(false);
//////////////////////////                labelImageAux[2].setVisible(false);
//////////////////////////                labelImageAux[3].setVisible(true);
//////////////////////////                labelImageAux[4].setVisible(true);
//////////////////////////                labelImageAux[5].setVisible(true);
//////////////////////////                labelImageAux[6].setVisible(true);
//////////////////////////                labelImageAux[7].setVisible(false);
//////////////////////////                labelImageAux[8].setVisible(false);
//////////////////////////                labelImageAux[9].setVisible(true);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Label Title
//////////////////////////                elemLabelTitle[1].setVisible(false);
//////////////////////////                elemLabelTitle[2].setVisible(false);
//////////////////////////                elemLabelTitle[3].setVisible(false);
//////////////////////////                elemLabelTitle[4].setVisible(true);
//////////////////////////                elemLabelTitle[5].setVisible(true);
//////////////////////////                elemLabelTitle[6].setVisible(true);
//////////////////////////                elemLabelTitle[7].setVisible(true);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Label Simple
//////////////////////////                elemLabelSimple[3].setVisible(false);
//////////////////////////                elemLabelSimple[4].setVisible(false);
//////////////////////////                elemLabelSimple[5].setVisible(false);
//////////////////////////                elemLabelSimple[6].setVisible(true);
//////////////////////////                elemLabelSimple[7].setVisible(true);
//////////////////////////                elemLabelSimple[8].setVisible(true);
//////////////////////////                elemLabelSimple[9].setVisible(true);
//////////////////////////                elemLabelSimple[10].setVisible(false);
//////////////////////////
//////////////////////////                if (!elemLabelSimple[5].getText().equals("0")) {
//////////////////////////
//////////////////////////                    fattoreFondoScala = Double.parseDouble( 
//////////////////////////                            Double.toString(Double.parseDouble(elemLabelSimple[4].getText())
//////////////////////////                            / Double.parseDouble(elemLabelSimple[5].getText())));
//////////////////////////
//////////////////////////                } else {
//////////////////////////
//////////////////////////                    fattoreFondoScala = 1;
//////////////////////////                }
//////////////////////////
//////////////////////////                elemLabelSimple[6].setText(Double.toString(Benefit.TroncaDouble(fattoreZero, FabCloudConstants.DECIMALI_MICRO_TARATURA)));
//////////////////////////                elemLabelSimple[7].setText(Double.toString(Benefit.TroncaDouble(fattoreFondoScala, FabCloudConstants.DECIMALI_MICRO_TARATURA)));
//////////////////////////
//////////////////////////                indexLabelAux = 0;
//////////////////////////
//////////////////////////                impostaVisButAvanti(false);
//////////////////////////
//////////////////////////                break;
//////////////////////////            }
//////////////////////////        }
//////////////////////////
//////////////////////////    }
//////////////////////////
    //Gestione Pulsanti
    public void gestorePulsanti(String button) {
//////////////////////////
//////////////////////////        switch (button) {
//////////////////////////
//////////////////////////            case "0": {
//////////////////////////
//////////////////////////                //Impostazione Visibilità Button Freccia
//////////////////////////                butFreccia.setVisible(true);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Pulsante "ANNULLA"
//////////////////////////                impostaVisButAnnulla(false);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Pulsante "AVANTI"
//////////////////////////                impostaVisButAvanti(true);
//////////////////////////
//////////////////////////                //Inizializzazione Indice Label Aux
//////////////////////////                indexLabelAux = 0;
//////////////////////////
//////////////////////////                //Impostazione Visibilità Componenti
//////////////////////////                impostaVisComponenti();
//////////////////////////
//////////////////////////                break;
//////////////////////////            }
//////////////////////////
//////////////////////////            case "1": {
//////////////////////////
//////////////////////////                //Impostazione Visibilità Button Freccia
//////////////////////////                butFreccia.setVisible(false);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Pulsante "ANNULLA"
//////////////////////////                impostaVisButAnnulla(true);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Componenti
//////////////////////////                impostaVisComponenti();
//////////////////////////
//////////////////////////                break;
//////////////////////////            }
//////////////////////////
//////////////////////////            case "2": {
//////////////////////////
//////////////////////////                //Impostazione Visibilità Button Freccia
//////////////////////////                butFreccia.setVisible(true);
//////////////////////////
//////////////////////////                //Impostazione Visibilità Pulsante "ANNULLA"
//////////////////////////                impostaVisButAnnulla(false);
//////////////////////////
//////////////////////////                //Inizializzazione Indice Label Aux
//////////////////////////                indexLabelAux = 0;
//////////////////////////
//////////////////////////                //Registrazione Modifiche
//////////////////////////                registraModifiche();
//////////////////////////
//////////////////////////                //Scambio Pannello
//////////////////////////                gestoreScambioPannello();
//////////////////////////
//////////////////////////                break;
//////////////////////////            }
//////////////////////////        }
//////////////////////////
    }
//////////////////////////
//////////////////////////    //Registrazione Risultati Taratura su Database
//////////////////////////    private void registraModifiche() {
////////////////////////////////        Benefit.AggiornaValoreParSingMacOri(4 + microDosatoreSelezionato * 2, Integer.toString(fattoreZero));
////////////////////////////////        Benefit.AggiornaValoreParSingMacOri(5 + microDosatoreSelezionato * 2, Double.toString(fattoreFondoScala));
//////////////////////////////        Benefit.AggiornaValoreParSingMacOri(4 + Integer.parseInt(ParametriSingolaMacchina.parametri.get(180 + microDosatoreSelezionato)) * 2, Integer.toString(fattoreZero));
//////////////////////////////        Benefit.AggiornaValoreParSingMacOri(5 + Integer.parseInt(ParametriSingolaMacchina.parametri.get(180 + microDosatoreSelezionato)) * 2, Double.toString(fattoreFondoScala));
//////////////////////////    }
//////////////////////////
    //Gestione Scambio Pannelli Collegati
    public void gestoreScambioPannello() {

        scambioPannello = true;
    }
//////////////////////////
//////////////////////////    //Thread Visualizzazione e Modifica Informazioni
//////////////////////////    private class ThreadLeggiIngressoAnalogico extends Thread {
//////////////////////////
//////////////////////////        private MyStepPanel pannello;
//////////////////////////
//////////////////////////        public ThreadLeggiIngressoAnalogico(MyStepPanel pannello) {
//////////////////////////            this.pannello = pannello;
//////////////////////////        }
//////////////////////////
//////////////////////////        @Override
//////////////////////////        public void run() {
//////////////////////////            try {
//////////////////////////
//////////////////////////                Microdosatore micro = new Microdosatore(microDosatoreSelezionato);
//////////////////////////
//////////////////////////                String res;
//////////////////////////
//////////////////////////                while (pannello.isVisible()
//////////////////////////                        && !scambioPannello) {
//////////////////////////
//////////////////////////                    try {
//////////////////////////
//////////////////////////                        ThreadLeggiIngressoAnalogico.sleep(FabCloudConstants.FREQ_THREAD_RICHIESTA_PESO_MICRO_TARATURA);
//////////////////////////
//////////////////////////                    } catch (InterruptedException ex) {
//////////////////////////                        SessionLogger.logger.severe("Errore durante l'attesa del tempo di sleep - ex =" + ex);
//////////////////////////                    }
//////////////////////////
//////////////////////////                    res = micro.richiestaValore();
//////////////////////////
//////////////////////////                    valoreInputAnalogico  = micro.estraiValoreIngressoAnalogico(res);
//////////////////////////                   
//////////////////////////                    elemLabelSimple[3].setText(
//////////////////////////                            Double.toString(
//////////////////////////                            Benefit.TroncaDouble(
//////////////////////////                            Double.parseDouble(valoreInputAnalogico), FabCloudConstants.DECIMALI_MICRO_TARATURA)));
//////////////////////////
//////////////////////////                    elemLabelSimple[5].setText(
//////////////////////////                            Double.toString(
//////////////////////////                            Benefit.TroncaDouble(Double.parseDouble(valoreInputAnalogico) - fattoreZero,FabCloudConstants.DECIMALI_MICRO_TARATURA)));
//////////////////////////            
//////////////////////////                   elemLabelSimple[9].setText(
//////////////////////////                           Double.toString(
//////////////////////////                            Benefit.TroncaDouble(
//////////////////////////                           ((Double.parseDouble(valoreInputAnalogico) - fattoreZero)*fattoreFondoScala),FabCloudConstants.DECIMALI_MICRO)));
//////////////////////////                    
//////////////////////////                }
//////////////////////////
//////////////////////////                //Chiusura Connessione Micro
//////////////////////////                micro.chiudiConnessione();
//////////////////////////
//////////////////////////                ////////////////////////
//////////////////////////                // SCAMBIO PANNELLO  ///
//////////////////////////                ////////////////////////
//////////////////////////
//////////////////////////                pannello.setVisible(false);
//////////////////////////
//////////////////////////                ((Pannello43_Configurazione_Microdosatura) pannelliCollegati.get(0)).initPanel();
//////////////////////////
//////////////////////////                scambioPannello = false;
//////////////////////////                
//////////////////////////            } catch (SocketException | UnknownHostException ex) {
//////////////////////////                SessionLogger.logger.severe("Errore durante la creazione dell'oggetto microdosatore - ex =" + ex);
//////////////////////////            }
//////////////////////////
//////////////////////////        }
//////////////////////////    }
}