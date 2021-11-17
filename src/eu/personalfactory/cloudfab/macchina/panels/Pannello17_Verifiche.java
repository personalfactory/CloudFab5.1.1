package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPreseAbilitate;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUMERO_PAGINE_VERIFICHE;

import java.util.logging.Level;

import javax.swing.SwingConstants;

import eu.personalfactory.cloudfab.macchina.controllo.Controllo;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;

@SuppressWarnings("serial")
public class Pannello17_Verifiche extends MyStepPanel {

	// PARAMETRI PANNELLO
	public final int numImage = 2;
	// VARIABILI
	public int codType, panelType, index;
	private Controllo controllo;
	public boolean serverIngressiIsConnected, resetForzato;
	private int idVerifiche;

	// COSTRUTTORE
	public Pannello17_Verifiche() {

		super();

		setLayer();
	}

	// Dichiarazioni Pannello
	private void setLayer() {

		// Dichiarazione File Parametri
		impostaXml();

		// Definizione Caratteristiche Label Pannello
		impostaDimLabelPlus(0);
		impostaDimLabelSimple(35);
		impostaDimLabelHelp(0);
		impostaDimLabelTitle(8);
		impostaDimLabelProg(0);
		impostaDimLabelBut(41);
		impostaColori(3);

		// Inizializzazione Colori Label Simple
		initColorLabelSimple(elemColor[0]);

		// Inizializzazione Colori Label Title
		initColorLabelTitle(elemColor[1]);

		// Inserimento Pulsante Button Freccia
		inserisciButtonFreccia();

		// Configurazione di Base Pannello
		configuraPannello();

		// Modifica la Visibilità di Default delle Righe di Aiuto
		impostaVisibilitaAiuto(true);

		// Impostazione Allineamento labelSimple
		for (int i = 0; i < 15; i++) {

			elemLabelSimple[i].setVerticalAlignment(SwingConstants.BOTTOM);
			elemLabelSimple[i].setHorizontalAlignment(SwingConstants.LEFT);
		}

		// Inserimento Immagini Ausiliarie
		new ThreadInserisciLabelImageAux(this, numImage, false).start();

		// Abilitazione Pulsante Avvio
		elemBut[29].setVisible(false);
		elemBut[30].setVisible(false);

		// Abilitazione Pulsante Avvio
		elemLabelSimple[26].setVisible(false);
		elemLabelSimple[27].setVisible(false);

		elemLabelSimple[30].setHorizontalAlignment(SwingConstants.LEFT);
		elemLabelSimple[31].setHorizontalAlignment(SwingConstants.RIGHT);
		elemLabelSimple[32].setHorizontalAlignment(SwingConstants.LEFT);
		elemLabelSimple[33].setHorizontalAlignment(SwingConstants.RIGHT);

	}

	// Inizializzazione Pannello
	public void initPanel() {

		// Inzializzazione Indice di Controllo Verifica Completa
		index = 0;
		idVerifiche = 0;
 
		// Impostazione Visibilità Pannello
		setPanelVisibile();

		// Inizializzazione Messaggi Esito Test
		impostaMessaggioEsito(
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 499,
						ParametriSingolaMacchina.parametri.get(111))),
				EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 500,
						ParametriSingolaMacchina.parametri.get(111))));

		// Impostazione Visibilità Freccia Indietro
		butFreccia.setVisible(true);

		// Nuova Istanza Procedura di Controllo
		controllo = new Controllo(this);

		// Inizializzazione Abilitazione e Visibilità Pulsanti
		inizializzaPulsanti();

		// Inizializzazione Immagini Ausiliarie Visualizzate
		impostaLabelImage(0);

		if (panelType == 1) {

			// VERIFICA SINGOLA
			elemBut[31].setEnabled(true);
			elemBut[32].setEnabled(true);
			elemBut[31].setVisible(true);
			elemBut[32].setVisible(true);
		} else {
			// VERIFICA COMPLETA
			elemBut[31].setVisible(false);
			elemBut[32].setVisible(false);
		}

		elemBut[35].setVisible(false);
		elemBut[36].setVisible(false);
		elemBut[37].setVisible(false);
		elemBut[38].setVisible(false); 
		elemBut[39].setVisible(false);
		elemBut[40].setVisible(false);
		elemLabelSimple[34].setVisible(false);

		impostaLabels();
		impostaAbilitazionePulsantiVersioniOrigami();

	}
	

	// Impostazione Visibilità Label Immagini Ausiliarie
	public void impostaLabelImage(int i) {

		switch (i) {

		case 0: {

			labelImageAux[0].setVisible(false);
			labelImageAux[1].setVisible(false);
			break;
		}

		case 1: {

			labelImageAux[0].setVisible(true);
			labelImageAux[1].setVisible(false);
			break;
		}

		case 2: {

			labelImageAux[0].setVisible(false);
			labelImageAux[1].setVisible(true);
			break;
		}
		}

	}

	// Inizializzazione Pulsanti
	public void inizializzaPulsanti() {

		// Impostazizone Visibilità Pulsanti START/STOP
		impostaVisibilitaStartStop(false);

		// Impostazione Validità Pulsante START
		impostaAbilitazionePulsanteAvvio(false);

		// Impostazione Validità Pulsante START
		impostaAbilitazionePulsanteArresto(false);

		// Impostazione Visbilità Pulsanti Controllo Test
		impostaVisibilitaPulsanteControlloTest(false);

		for (int i = 0; i < 17; i++) {

			// Impostazione Validità Pulsanti
			elemBut[i].setEnabled(false);

			// Impostazione Colore Testo Pulsanti
			elemLabelSimple[i].setForeground(elemColor[2]);

		}

		elemLabelSimple[24].setForeground(elemColor[2]);
		elemLabelSimple[25].setForeground(elemColor[2]);

		// Impostazione Validità Pulsanti
		elemBut[21].setVisible(false);

		// Impostazione Validità Pulsanti
		elemBut[22].setVisible(false);

		impostaVisibilitaPulsantiBilancia(false);

		// Impostazione Abilitazione Pulsanti
		impostaAbilitazionePulsanti(true);

		// Impostazione Visibilità Pulsanti Vibro
		impostaVisibilitaPulsantiVibro(false);

		// Impostazione Visibilità Pulsanti Coclee
		elemBut[21].setVisible(false);
		elemBut[22].setVisible(false);

		// Impostazione Visibilità Pulsanti Prese Avanti Dietro
		impostaVisibilitaPulsantiSaltaPrese(false);

	}

	private void impostaAbilitazionePulsantiVersioniOrigami() {

		elemBut[0].setEnabled(true);
		elemBut[1].setEnabled(true);
		elemBut[2].setEnabled(true);
		elemBut[3].setEnabled(true);
		elemBut[4].setEnabled(true);
		elemBut[5].setEnabled(true);
		elemBut[6].setEnabled(true);
		elemBut[7].setEnabled(true);
		elemBut[8].setEnabled(true);
		elemBut[9].setEnabled(true);
		elemBut[10].setEnabled(true);
		elemBut[11].setEnabled(true);
		elemBut[12].setEnabled(true);
		elemBut[13].setEnabled(true);
		elemBut[14].setEnabled(true);
		elemBut[15].setEnabled(true);
		elemBut[16].setEnabled(true);

		elemLabelSimple[0].setForeground(elemColor[0]);
		elemLabelSimple[1].setForeground(elemColor[0]);
		elemLabelSimple[2].setForeground(elemColor[0]);
		elemLabelSimple[3].setForeground(elemColor[0]);
		elemLabelSimple[4].setForeground(elemColor[0]);
		elemLabelSimple[5].setForeground(elemColor[0]);
		elemLabelSimple[6].setForeground(elemColor[0]);
		elemLabelSimple[7].setForeground(elemColor[0]);
		elemLabelSimple[8].setForeground(elemColor[0]);
		elemLabelSimple[9].setForeground(elemColor[0]);
		elemLabelSimple[10].setForeground(elemColor[0]);
		elemLabelSimple[11].setForeground(elemColor[0]);
		elemLabelSimple[12].setForeground(elemColor[0]);
		elemLabelSimple[13].setForeground(elemColor[0]);
		elemLabelSimple[14].setForeground(elemColor[0]);
		elemLabelSimple[15].setForeground(elemColor[0]);
		elemLabelSimple[16].setForeground(elemColor[0]);

		// Inizializzazione
		elemLabelSimple[24].setForeground(elemColor[0]);
		elemBut[27].setEnabled(true);

		elemLabelSimple[25].setForeground(elemColor[0]);
		elemBut[28].setEnabled(true);

		// Contatto Ribalta Sacco
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(294))) {

			if (idVerifiche == 0) {

				elemLabelSimple[24].setForeground(elemColor[2]);
				elemBut[27].setEnabled(false);

			}

		}

		// Aria Interno Valvola
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

			if (idVerifiche == 1) {

				elemLabelSimple[0].setForeground(elemColor[2]);
				elemBut[0].setEnabled(false);

			}

		}
		// Aria Svuota Tubo
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

			if (idVerifiche == 1) {

				elemLabelSimple[1].setForeground(elemColor[2]);
				elemBut[1].setEnabled(false);

			}

		}
		// Aria Pulisci Valvola
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

			if (idVerifiche == 1) {

				elemLabelSimple[24].setForeground(elemColor[2]);
				elemBut[27].setEnabled(false);
			}

		}
		
		// Aria Svuota Valvola
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

			if (idVerifiche == 1) {

				elemLabelSimple[2].setForeground(elemColor[2]);
				elemBut[2].setEnabled(false);

			}

		}

		// Contattore Nastro
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(450))) {

			if (idVerifiche == 1) {
				elemLabelSimple[6].setForeground(elemColor[2]);
				elemBut[6].setEnabled(false);
			} else {
				elemLabelSimple[6].setForeground(elemColor[1]);
				elemBut[6].setEnabled(true);
			}
		}

		// Vibratori Tramoggia
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(501))) {

			if (idVerifiche == 1) {

				elemLabelSimple[8].setForeground(elemColor[2]);
				elemBut[8].setEnabled(false);
			}

		}
		
		// Scuotitore
		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(503))) {

			if (idVerifiche == 0) {

				elemLabelSimple[5].setForeground(elemColor[2]);
				elemBut[5].setEnabled(false);

			}

		}

	}

	// Impostazione Messaggi Esito Test
	public void impostaMessaggioEsito(String titolo, String messaggio) {

		// Impostazione Testo Titolo Messaggi Esito Test
		elemLabelTitle[6].setText(titolo);

		// Impostazione Testo Messaggi Esito Test
		elemLabelSimple[17].setText(HTML_STRINGA_INIZIO + messaggio + HTML_STRINGA_FINE);

	}

	// Gestione Scambio Pannello Collegato
	public void gestoreInterruzioneThread() {

		// Interruzione Thread Comunicazione Scheda IO
		controllo.interrompiComunicazioneSchedaIO = true;

		// Scambio Pannello
		controllo.pannelloControllo.gestoreScambioPannello();

	}

	// Gestione Scambio Pannelli Collegati
	public void gestoreScambioPannello() {

		this.setVisible(false);

		((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

	}

	// Gestione Pulsanti
	public void gestorePulsanti(String button) {

		switch (Integer.parseInt(button)) {

		case 0: {

			if (idVerifiche == 0) {

				///////////////////////////////////////////////////
				// CONTROLLO CONTATTI VALVOLE CARICO E SCARICO ///
				///////////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloContattiValvole();

			} else {

				/////////////////////////////////////////////
				// CONTROLLO ELETTROVALVOLA FLUDIFICATORI ///
				/////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloElettrovalvolaFluidificatori();

			}

			break;
		}

		case 1: {

			if (idVerifiche == 0) {

				////////////////////////////////////
				// CONTROLLO CONTATTO SPORTELLO ///
				////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatto Sportello
				controllo.controlloContattoSportello();
			} else {

				/////////////////////////////////////////////////
				// CONTROLLO ELETTROVALVOLA ARIA SVUOTA TUBO ///
				/////////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloElettrovalvolaSvuotaTubo();

			}
			break;
		}

		case 2: {

			if (idVerifiche == 0) {

				//////////////////////////////////
				// CONTROLLO COCLEE DI CARICO ///
				//////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Coclee Carico
				controllo.controlloCocleeCarico();

			} else {

				////////////////////////////////////////////////////
				// CONTROLLO ELETTROVALVOLA ARIA SVUOTA VALVOLA ///
				////////////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloElettrovalvolaSvuotaValvola();
			}
			break;
		}

		case 3: {
			if (idVerifiche == 0) {

				////////////////////////////////////
				// CONTROLLO MOTORE MISCELATORE ///
				////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Motore Miscelatore
				controllo.controlloMotoreMiscelatore();
			} else {

			}
			break;
		}

		case 4: {
			if (idVerifiche == 0) {
				/////////////////////////////////////
				// CONTROLLO MOTORE VIBRO VALVOLA ///
				/////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Motore Vibro Valvola
				controllo.controlloMotoreVibroValvola();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 5: {
			if (idVerifiche == 0) {
				///////////////////////////
				// CONTROLLO SCUOTITORI ///
				///////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// //Controllo Motore Miscelatore
				controllo.controlloScuotitori();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 6: {
			if (idVerifiche == 0) {
				///////////////////////////////////////
				// CONTROLLO PULSANTI BLOCCA SACCO ///
				///////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Pulsanti Blocca Sacco
				controllo.controlloPulsantiBloccaSacco();
			} else {

				//////////////////////////////////
				// CONTROLLO CONTATTORE NASTRO ///
				///////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Motore Miscelatore
				controllo.controlloNastro();

			}
			break;
		}

		case 7: {
			if (idVerifiche == 0) {
				/////////////////////////////////
				// CONTROLLO SELETTORE VIBRO ///
				/////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Selettore Vibro
				controllo.controlloSelettoreVibro();
			} else {

				/////////////////////////////////////
				// CONTROLLO SOFFIATORI TRAMOGGIA ///
				/////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Motore Miscelatore
				controllo.controlloSoffiatoriTramoggia();

			}
			break;
		}

		case 8: {
			if (idVerifiche == 0) {

				///////////////////////////////////
				// CONTROLLO SELETTORE VALVOLA ///
				///////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Selettore Valvola
				controllo.controlloSelettoreValvola();

			} else {

				/////////////////////////////////////
				// CONTROLLO VIRBO TRAMOGGIA ///
				/////////////////////////////////////

				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Motore Miscelatore
				controllo.controlloVibroTramoggia();
			}
			break;
		}

		case 9: {
			if (idVerifiche == 0) {
				///////////////////////////////
				// CONTROLLO PULSANTE STOP ///
				///////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Pulsante Stop
				controllo.controlloPulsanteStop();
			} else {

				///////////////////////////
				// SEGNALATORE GIALLO 	///
				///////////////////////////

				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);
  
				// Controllo Motore Miscelatore
				controllo.controlloSeganalatoreGiallo();
			}
			break;
		}

		case 10: {
			if (idVerifiche == 0) {
				////////////////////////////////////
				// CONTROLLO BILANCIA DI CARICO ///
				////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Impostazione Visbilità Pulsanti Controllo Test
				impostaVisibilitaPulsanteControlloTest(true);

				// Controllo Bilancia Carico
				controllo.controlloBilanciaCarico();
			} else {

				////////////////////////
				//SEGNALATORE ROSSO  ///
				////////////////////////

				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);


				//Controllo Motore Miscelatore
				controllo.controlloSeganalatoreRosso();
			}
			break;
		}

		case 11: {
			if (idVerifiche == 0) {
				/////////////////////////////////////
				// CONTROLLO BILANCIA CONFEZIONI ///
				/////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Impostazione Visbilità Pulsanti Controllo Test
				impostaVisibilitaPulsanteControlloTest(true);

				// Controllo Bilancia Confezioni
				controllo.controlloBilanciaConfezioni();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 12: {
			if (idVerifiche == 0) {
				/////////////////////////////////
				// CONTROLLO BILANCIA COLORI ///
				/////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Impostazione Visbilità Pulsanti Controllo Test
				impostaVisibilitaPulsanteControlloTest(true);

				// Controllo Bilancia Colori
				controllo.controlloBilanciaColori();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 13: {
			if (idVerifiche == 0) {
				/////////////////////////////////////////////
				// CONTROLLO ATTUATORE VALVOLA DI CARICO ///
				/////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Attuatore Valvola di Carico
				controllo.controlloAttuatoreValvolaCarico();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 14: {
			if (idVerifiche == 0) {
				/////////////////////////////////////////////
				// CONTROLLO ATTUTORE VALVOLA DI SCARICO ///
				/////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Attuatore Valvola di Scarico
				controllo.controlloAttuatoreValvolaScarico();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 15: {
			if (idVerifiche == 0) {

				//////////////////////////////////////////
				// CONTROLLO ATTUTATORE RIBALTA SACCO ///
				//////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Attuatore Ribalta Sacco
				controllo.controlloAttuatoreRibaltaSacco();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;
		}

		case 16: {
			if (idVerifiche == 0) {
				////////////////////////////////////////
				// CONTROLLO ATTUATORE BLOCCA SACCO ///
				////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Attuatore Blocca Sacco
				controllo.controlloAttuatoreBloccaSacco();
			} else {

				// FUNZIONE NON DEFINITA
			}
			break;

		}

		case 17: {

			/////////////////////////////
			// PULSANTE TEST FALLITO ///
			/////////////////////////////
			// Aggiornamento Valore Variabile di Controllo
			controllo.testFallito = true;

			// Impostazione Visbilità Pulsanti Controllo Test
			impostaVisibilitaPulsanteControlloTest(false);

			break;

		}

		case 18: {

			/////////////////////////////
			// PULSANTE TEST SUPERATO ///
			/////////////////////////////
			// Aggiornamento Valore Variabile di Controllo
			controllo.testSuperato = true;

			// Impostazione Visbilità Pulsanti Controllo Test
			impostaVisibilitaPulsanteControlloTest(false);

			break;

		}

		case 19: {

			//////////////////////
			// PULSANTE START ///
			//////////////////////
			// Aggiornamento Valore Variabile di Controllo
			controllo.avvio = true;

			// Impostazione Validità Pulsante START
			impostaAbilitazionePulsanteAvvio(false);

			impostaVisibilitaPulsantiVibro(false);

			break;

		}

		case 20: {

			/////////////////////
			// PULSANTE STOP ///
			/////////////////////
			// Aggiornamento Valore Variabile di Controllo
			controllo.arresto = true;

			// Impostazione Validità Pulsante START
			impostaAbilitazionePulsanteArresto(false);

			break;

		}
		case 21: {

			/////////////////////
			// PULSANTE BYPASS ///
			/////////////////////
			// Aggiornamento Valore Variabile di Controllo
			controllo.attivaByPass = true;

			elemBut[21].setVisible(false);
			elemBut[22].setVisible(true);

			break;

		}
		case 22: {

			/////////////////////
			// PULSANTE INV ///
			/////////////////////
			// Aggiornamento Valore Variabile di Controllo
			controllo.attivaByPass = false;

			elemBut[21].setVisible(true);
			elemBut[22].setVisible(false);

			break;

		}

		case 23: {

			///////////////////////////
			// PULSANTE PESO NETTO ///
			///////////////////////////
			controllo.commutaNetto = true;

			break;

		}

		case 24: {

			///////////////////////////
			// PULSANTE PESO LORDO ///
			///////////////////////////
			controllo.commutaLordo = true;

			break;

		}

		case 25: {

			///////////////////////////
			// PULSANTE START VIBRO ///
			///////////////////////////
			controllo.avvioSoffiatori = true;

			elemBut[25].setEnabled(false);
			elemBut[26].setEnabled(true);
			impostaAbilitazionePulsanteAvvio(false);
			impostaAbilitazionePulsanteArresto(false);
			elemBut[21].setVisible(false);
			elemBut[22].setVisible(false);
			elemBut[33].setEnabled(false);
			elemBut[34].setEnabled(false);
			break;

		}
		case 26: {

			///////////////////////////
			// PULSANTE STOP VIBRO ///
			///////////////////////////
			controllo.arrestoSoffiatori = true;

			elemBut[25].setEnabled(true);
			elemBut[26].setEnabled(false);
			impostaAbilitazionePulsanteAvvio(true);
			elemBut[21].setVisible(true);
			elemBut[22].setVisible(false);
			elemBut[33].setEnabled(true);
			elemBut[34].setEnabled(false);

			break;

		}

		case 27: {
			if (idVerifiche == 0) {
				////////////////////////////////////////
				// CONTROLLO CONTATTO RIBALTA SACCO ///
				////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(false);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloContattoRibaltaSacco();
			} else {

				/////////////////////////////////////////////////////
				// CONTROLLO ELETTROVALVOLA ARIA PULISCI VALVOLA ///
				/////////////////////////////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloElettrovalvolaPulisciValvola();

			}
			break;
		}

		case 28: {
			if (idVerifiche == 0) {

				////////////////////////////
				// CONTROLLO ASPIRATORE ///
				////////////////////////////
				// Impostazione Visibilità Label Image Aux
				impostaLabelImage(0);

				// Impostazizone Visibilità Pulsanti START/STOP
				impostaVisibilitaStartStop(true);

				// Impostazione Validità Pulsanti Scelta Test
				impostaAbilitazionePulsanti(false);

				// Controllo Contatti Valvole
				controllo.controlloAspiratore();
			} else {

			}
			break;

		}

		case 29: {

			////////////////////////////////////////////
			// INCREMENTO ID PRESA CONTROLLO COCLEE ///
			////////////////////////////////////////////
			if (controllo.idPresa < TrovaPreseAbilitate().size() - 1) {

				// Incremento id
				controllo.idPresa++;

				controllo.aggiornaIdPresa();
			}
			break;

		}

		case 30: {

			////////////////////////////////////////////
			// INCREMENTO ID PRESA CONTROLLO COCLEE ///
			////////////////////////////////////////////
			if (controllo.idPresa > 0) {
				// Incremento id
				controllo.idPresa--;

				controllo.aggiornaIdPresa();
			}
			break;

		}

		case 31: {

			/////////////////////////////////
			// SCHERMATA VERIFICHE AVANTI ///
			/////////////////////////////////
			if (idVerifiche < NUMERO_PAGINE_VERIFICHE) {

				// Incremento id
				idVerifiche++;

				impostaLabels();
				impostaAbilitazionePulsantiVersioniOrigami();

			}
			break;

		}

		case 32: {

			/////////////////////////////////
			// SCHERMATA VERIFICHE DIETRO ///
			/////////////////////////////////
			if (idVerifiche > 0) {

				// Incremento id
				idVerifiche--;
				impostaLabels();
				impostaAbilitazionePulsantiVersioniOrigami(); 
			}
			break; 
		}

		case 33: {

			///////////////////////////
			// PULSANTE START VIBRO ///
			///////////////////////////
			controllo.avvioVibroPneumatici = true;

			elemBut[33].setEnabled(false);
			elemBut[34].setEnabled(true);
			impostaAbilitazionePulsanteAvvio(false);
			impostaAbilitazionePulsanteArresto(false);
			elemBut[21].setVisible(false);
			elemBut[22].setVisible(false);
			elemBut[25].setEnabled(false);
			elemBut[26].setEnabled(false);

			break;

		}
		case 34: {

			///////////////////////////
			// PULSANTE STOP VIBRO ///
			///////////////////////////
			controllo.arrestoVibroPneumatici = true;

			elemBut[33].setEnabled(true);
			elemBut[34].setEnabled(false);
			impostaAbilitazionePulsanteAvvio(true);
			elemBut[21].setVisible(true);
			elemBut[22].setVisible(false);
			elemBut[25].setEnabled(true);
			elemBut[26].setEnabled(false);

			break;

		}
		case 35: {

			///////////////////////////////////
			// POS_20 - VALVOLA DI SCARICO ///
			///////////////////////////////////

			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				////////////////////////////
				// ATTUATORE MULTISTADIO //
				////////////////////////////
				controllo.pannelloControllo.elemBut[35].setVisible(false);
				controllo.pannelloControllo.elemBut[36].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_20);
				ControlloLogger.logger.log(Level.INFO, "Apertura Vavola POS_20");
			}

			break;

		}
		case 36: {

			///////////////////////////////////
			// POS_53 - VALVOLA DI SCARICO ///
			///////////////////////////////////
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				////////////////////////////
				// ATTUATORE MULTISTADIO //
				////////////////////////////
				controllo.pannelloControllo.elemBut[36].setVisible(false);
				controllo.pannelloControllo.elemBut[37].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_53);
				ControlloLogger.logger.log(Level.INFO, "Apertura Vavola POS_53");
			}
			break;

		}
		case 37: {

			////////////////////////////////////
			// POS_100 - VALVOLA DI SCARICO ///
			////////////////////////////////////
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				////////////////////////////
				// ATTUATORE MULTISTADIO //
				////////////////////////////
				controllo.pannelloControllo.elemBut[37].setVisible(false);
				controllo.pannelloControllo.elemBut[38].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);
				ControlloLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");
				
			} else {
				////////////////////
				// COMANDO UNICO  //
				////////////////////
				controllo.pannelloControllo.elemBut[38].setVisible(true);
				controllo.pannelloControllo.elemBut[37].setVisible(false);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO); 
				ControlloLogger.logger.log(Level.INFO, "Comando Apertura Valvola POS_100_COMANDO_UNICO");
				
				
			}
			break;

		}

		case 38: {

			/////////////////////////////////
			// POS_0 - VALVOLA DI SCARICO ///
			/////////////////////////////////
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

				////////////////////////////
				// ATTUATORE MULTISTADIO //
				////////////////////////////
				controllo.pannelloControllo.elemBut[38].setVisible(false);
				controllo.pannelloControllo.elemBut[35].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
				ControlloLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0");
			} else { 

				////////////////////
				// COMANDO UNICO  //
				////////////////////
				controllo.pannelloControllo.elemBut[38].setVisible(false);
				controllo.pannelloControllo.elemBut[37].setVisible(true);
				GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO); 
				ControlloLogger.logger.log(Level.INFO, "Comando Chiusura Valvola POS_0_COMANDO_UNICO");
 
				
				
			}

			break;

		}
		case 39: {

			///////////////////////////
			// PULSANTE START VIBRO ///
			///////////////////////////
			controllo.avvioAttuatoreValvolaAspiratoreSilo = true;

			elemBut[33].setEnabled(false);
			elemBut[34].setEnabled(false);
			impostaAbilitazionePulsanteAvvio(false);
			impostaAbilitazionePulsanteArresto(false);
			elemBut[21].setVisible(false);
			elemBut[22].setVisible(false);
			elemBut[25].setEnabled(false);
			elemBut[26].setEnabled(false);
			elemBut[39].setEnabled(false);
			elemBut[40].setEnabled(true);

			break;

		}
		case 40: {

			///////////////////////////
			// PULSANTE STOP VIBRO ///
			///////////////////////////
			controllo.arrestoAttuatoreValvolaAspiratoreSilo = true;

			elemBut[33].setEnabled(true);
			elemBut[34].setEnabled(false);
			impostaAbilitazionePulsanteAvvio(true);
			elemBut[21].setVisible(true);
			elemBut[22].setVisible(false);
			elemBut[25].setEnabled(true);
			elemBut[26].setEnabled(false); 
			elemBut[39].setEnabled(true);
			elemBut[40].setEnabled(false);

			break;

		}

		}
	}

	// Impostazione Validità Pulsanti Scelta Test
	public void impostaAbilitazionePulsanti(boolean en) {

		if (panelType == 1) {

			////////////////////////
			// VERIFICA SINGOLA ///
			////////////////////////
			for (int i = 0; i < 17; i++) {
				elemBut[i].setEnabled(en);

				if (en) {
					elemLabelSimple[i].setForeground(elemColor[0]);

				} else {

					//if (idVerifiche == 0) {
						elemLabelSimple[i].setForeground(elemColor[2]);
					//}
				}

			}

			// Abilitazione Pulsanti Contatto Ribalta e Scambiatore
			elemBut[27].setEnabled(en);
			elemBut[28].setEnabled(en);

			// Impostazione Testo Contatto Ribalta e Scambiatore
			if (en) {
				elemLabelSimple[24].setForeground(elemColor[0]);
				elemLabelSimple[25].setForeground(elemColor[0]);

			} else {
				elemLabelSimple[24].setForeground(elemColor[2]);
				elemLabelSimple[25].setForeground(elemColor[2]);
			}

			// Abilitazione Pulsanti Contatto Ribalta e Scambiatore
			elemBut[31].setEnabled(en);
			elemBut[32].setEnabled(en);

			if (en) {
				impostaAbilitazionePulsantiVersioniOrigami();
			}
		} else {

			/////////////////////////
			// VERIFICA COMPLETA ///
			/////////////////////////
			if (en) {

				///////////////////////////
				// TEST DA SELEZIONARE ///
				///////////////////////////
				if (index < 24) {

					if (index < 17) {

						// TEST DA ESEGUIRE NON COMPLETATI
						elemLabelSimple[index].setForeground(elemColor[0]);
						elemBut[index].setEnabled(en);
						elemLabelSimple[index].setEnabled(en);
						index++;
					} else if (index == 17) {

						// Contatto Ribalta Sacco
						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(294))) {

							// PRESENZA CONTATTO RIBALTA SACCO
							abilitaTestContattoRibalta(en);
							index++;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// PRESENZA ASPIRATORE
							abilitaTestScambiatore(en);
							index += 2;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// PRESENZA ARIA FLUDIFICATORI
							abilitaTestFluidificatori(en);
							index += 3;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// PRESENZA ARIA SVUOTA TUBO
							abilitaTestSvuotaTubo(en);
							index += 4;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// PRESENZA ARIA PULISCI VALVOLA
							abilitaTestPulisciValvola(en);
							index += 5;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// PRESENZA CONTATTO ATTIVA ASPIRATORE
							abilitaTestConatattoAspiratore(en);
							index += 6;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 7;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}

					} else if (index == 18) {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(232))) {
							// ASSENZA CONTATTO RIBALTA SACCO
							// PRESENZA ASPIRATORE
							abilitaTestScambiatore(en);
							index += 1;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// PRESENZA ARIA FLUDIFICATORI
							abilitaTestFluidificatori(en);
							index += 2;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// PRESENZA ARIA SVUOTA TUBO
							abilitaTestSvuotaTubo(en);
							index += 3;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// PRESENZA ARIA PULISCI VALVOLA
							abilitaTestPulisciValvola(en);
							index += 4;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// PRESENZA CONTATTO ATTIVA ASPIRATORE
							abilitaTestConatattoAspiratore(en);
							index += 5;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 6;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}

					} else if (index == 19) {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// PRESENZA ARIA FLUDIFICATORI
							abilitaTestFluidificatori(en);
							index += 1;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// PRESENZA ARIA SVUOTA TUBO
							abilitaTestSvuotaTubo(en);
							index += 2;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// PRESENZA ARIA PULISCI VALVOLA
							abilitaTestPulisciValvola(en);
							index += 3;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// PRESENZA CONTATTO ATTIVA ASPIRATORE
							abilitaTestConatattoAspiratore(en);
							index += 4;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 5;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}

					} else if (index == 20) {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// PRESENZA ARIA SVUOTA TUBO
							abilitaTestSvuotaTubo(en);
							index += 1;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// PRESENZA ARIA PULISCI VALVOLA
							abilitaTestPulisciValvola(en);
							index += 2;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// PRESENZA CONTATTO ATTIVA ASPIRATORE
							abilitaTestConatattoAspiratore(en);
							index += 3;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 4;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}

					} else if (index == 21) {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// PRESENZA ARIA PULISCI VALVOLA
							abilitaTestPulisciValvola(en);
							index += 1;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// PRESENZA CONTATTO ATTIVA ASPIRATORE
							abilitaTestConatattoAspiratore(en);
							index += 2;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 3;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}
					} else if (index == 22) {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(323))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// PRESENZA CONTATTO ATTIVA ASPIRATORE
							abilitaTestConatattoAspiratore(en);
							index += 1;

						} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 2;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}
					} else if (index == 23) {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(337))) {

							// ASSENZA CONTATTO RIBALTA SACCO
							// ASSENZA ASPIRATORE
							// ASSENZA ARIA INTERNO VALVOLA
							// ASSENZA ARIA SVUOTA TUBO
							// ASSENZA ARIA PULISCI VALVOLA
							// ASSENZA CONTATTO ATTIVA ASPIRATORE
							// PRESENZA ARIA SVUOTA VALVOLA
							abilitaTestAriaSvuotaValvola(en);
							index += 1;

						} else {

							///////////////////////////////////
							// TEST DA ESEGUIRE COMPLETATI ///
							///////////////////////////////////
							// Interruzione Thread Comunicazione Scheda IO
							controllo.interrompiComunicazioneSchedaIO = true;

							// Visualizzazione Pannello Risultati Test
							visualizzaPannelloReport();

						}

					}
				} else {

					///////////////////////////////////
					// TEST DA ESEGUIRE COMPLETATI ///
					///////////////////////////////////
					// Interruzione Thread Comunicazione Scheda IO
					controllo.interrompiComunicazioneSchedaIO = true;

					// Visualizzazione Pannello Risultati Test
					visualizzaPannelloReport();
				}

			} else {

				////////////////////////
				// TEST SELEZIONATO ///
				////////////////////////
				for (int i = 0; i < 17; i++) {
					elemBut[i].setEnabled(en);
					elemLabelSimple[i].setForeground(elemColor[2]);

				}

				// Abilitazione Pulsanti Contatto Ribalta e Scambiatore
				elemBut[27].setEnabled(en);
				elemBut[28].setEnabled(en);
				elemLabelSimple[24].setForeground(elemColor[2]);
				elemLabelSimple[25].setForeground(elemColor[2]);

			}
		}

	}

	// Impostazizone Visibilità Pulsanti START/STOP
	public void impostaVisibilitaStartStop(boolean en) {

		elemBut[19].setVisible(en);
		elemBut[20].setVisible(en);

	}

	// Impostazione Validità Pulsante START
	public void impostaAbilitazionePulsanteAvvio(boolean en) {

		elemBut[19].setEnabled(en);

	}

	// Impostazione Validità Pulsante START
	public void impostaAbilitazionePulsanteArresto(boolean en) {

		elemBut[20].setEnabled(en);

	}

	// Impostazione Visbilità Pulsanti Controllo Test
	public void impostaVisibilitaPulsanteControlloTest(boolean vis) {

		elemBut[17].setVisible(vis);
		elemBut[18].setVisible(vis);

	}

	// Impostazione Visbilità Pulsanti Controllo Testg
	public void impostaVisibilitaPulsantiVibro(boolean vis) {

		elemBut[25].setVisible(vis);
		elemLabelSimple[22].setVisible(vis);
		elemBut[26].setVisible(vis);
		elemLabelSimple[23].setVisible(vis);

		// VISIBIITA PULSANTI VIBRO PNEUMATICI
		elemBut[33].setVisible(vis);
		elemBut[34].setVisible(vis);
		elemLabelSimple[28].setVisible(vis);
		elemLabelSimple[29].setVisible(vis);

	}

	// Impostazione Visbilità Pulsanti Bilance
	public void impostaVisibilitaPulsantiBilancia(boolean vis) {

		elemBut[23].setVisible(vis);
		elemBut[24].setVisible(vis);

		elemLabelSimple[20].setVisible(vis);
		elemLabelSimple[21].setVisible(vis);

	}

	// Impostazione Label Presa/Timer
	public void impostaLabelPresaTimer(String s) {
		elemLabelSimple[18].setText(s);
	}

	// Impostazione Label Peso Bilancia
	public void impostaLabelPesoBilancia(String s) {
		elemLabelSimple[19].setText(s);
	}

	// Impostazione Visibilità Label Peso Bilancia
	public void impostaVisLabelPesoBilancia(boolean vis) {
		elemLabelSimple[19].setVisible(vis);

	}

	// Visualizzazione Pannello con i Risultati dei Test
	public void visualizzaPannelloReport() {

		((Pannello18_Controllo_Report) pannelliCollegati.get(1)).esitoTest = controllo.esitoTest;
		((Pannello18_Controllo_Report) pannelliCollegati.get(1)).initPanel();

	}

	public void impostaVisibilitaPulsantiSaltaPrese(boolean vis) {
		if (controllo.pannelloControllo.panelType == 1) {

			// Abilitazione Pulsante Avvio
			controllo.pannelloControllo.elemBut[29].setVisible(vis);
			controllo.pannelloControllo.elemBut[30].setVisible(vis);

			// Abilitazione Pulsante Avvio
			controllo.pannelloControllo.elemLabelSimple[26].setVisible(vis);
			controllo.pannelloControllo.elemLabelSimple[27].setVisible(vis);

		}

	}

	public void impostaLabels() {

		if (idVerifiche == 0) {
			// Aggiornamento Testi Label tipo Title
			elemLabelTitle[0].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 415, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[1].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 416, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[2].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 417, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[3].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 418, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[4].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 419, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[5].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 420, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[7].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 747, ParametriSingolaMacchina.parametri.get(111)));

			// Aggiornamento Testi Label Tipo Simple
			elemLabelSimple[0].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 421, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[1].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 422, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[2].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 423, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[3].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 424, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[4].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 495, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[5].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1046, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[6].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 425, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[7].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 426, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[8].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 427, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[9].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 428, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[10].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 429, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[11].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 430, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[12].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 431, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[13].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 432, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[14].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 433, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[15].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 434, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[16].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 435, ParametriSingolaMacchina.parametri.get(111)));

			elemLabelSimple[20].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 717, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[21].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 718, ParametriSingolaMacchina.parametri.get(111)));

			elemLabelSimple[22].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 732, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[23].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 733, ParametriSingolaMacchina.parametri.get(111)));

			elemLabelSimple[24].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 736, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[25].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1048, ParametriSingolaMacchina.parametri.get(111)));

			elemLabelSimple[26].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 756, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[27].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 757, ParametriSingolaMacchina.parametri.get(111)));

			elemLabelSimple[28].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1059, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[29].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1060, ParametriSingolaMacchina.parametri.get(111)));

		} else {

			// Aggiornamento Testi Label tipo Title
			elemLabelTitle[1].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 783, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[2].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 783, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[3].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1006, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelTitle[4].setText("");
			elemLabelTitle[5].setText("");
			elemLabelTitle[7].setText("");

			// Aggiornamento Testi Label Tipo Simple
			elemLabelSimple[0].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 785, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[1].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 786, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[2].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 808, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[3].setText("");
			elemLabelSimple[4].setText("");
			elemLabelSimple[5].setText("");
			elemLabelSimple[6].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1024, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[7].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1055, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[8].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1056, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[9].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1061, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[10].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1063, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[11].setText("");
			elemLabelSimple[12].setText("");
			elemLabelSimple[13].setText("");
			elemLabelSimple[14].setText("");
			elemLabelSimple[15].setText("");
			elemLabelSimple[16].setText("");

			elemLabelSimple[20].setText("");
			elemLabelSimple[21].setText("");

			elemLabelSimple[22].setText("");
			elemLabelSimple[23].setText("");

			elemLabelSimple[24].setText(
					TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 787, ParametriSingolaMacchina.parametri.get(111)));
			elemLabelSimple[25].setText("");

			elemLabelSimple[26].setText("");
			elemLabelSimple[27].setText("");

		}

	}

	public void abilitaTestFluidificatori(boolean vis) {
		idVerifiche = 1;
		impostaLabels();
		elemLabelSimple[0].setForeground(elemColor[0]);
		elemBut[0].setEnabled(vis);
		elemLabelSimple[0].setEnabled(vis);

	}

	public void abilitaTestSvuotaTubo(boolean vis) {
		idVerifiche = 1;
		impostaLabels();
		elemLabelSimple[1].setForeground(elemColor[0]);
		elemBut[1].setEnabled(vis);
		elemLabelSimple[1].setEnabled(vis);
	}

	public void abilitaTestPulisciValvola(boolean vis) {
		idVerifiche = 1;
		impostaLabels();
		elemLabelSimple[24].setForeground(elemColor[0]);
		elemBut[27].setEnabled(vis);
		elemLabelSimple[24].setEnabled(vis);
	}

	public void abilitaTestConatattoAspiratore(boolean vis) {
		idVerifiche = 1;
		impostaLabels();
		elemLabelSimple[25].setForeground(elemColor[0]);
		elemBut[28].setEnabled(vis);
		elemLabelSimple[25].setEnabled(vis);
	}

	public void abilitaTestAriaSvuotaValvola(boolean vis) {
		idVerifiche = 1;
		impostaLabels();
		elemLabelSimple[2].setForeground(elemColor[0]);
		elemBut[2].setEnabled(vis);
		elemLabelSimple[2].setEnabled(vis);
	}

	public void abilitaTestContattoRibalta(boolean vis) {
		elemLabelSimple[24].setForeground(elemColor[0]);
		elemBut[27].setEnabled(vis);
		elemLabelSimple[24].setEnabled(vis);
	}

	public void abilitaTestScambiatore(boolean vis) {

		elemLabelSimple[25].setForeground(elemColor[0]);
		elemBut[28].setEnabled(vis);
		elemLabelSimple[25].setEnabled(vis);
	}
}
