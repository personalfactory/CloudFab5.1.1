package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.io.GestoreIO;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.processo.ActionListenerInserimentoCodiceOperatore;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;

import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.Inverter_Gefran_BDI50_ImpostaVisPannelli;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParSingMacOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConteggioGiorniUltimoAggiornamento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDettagliOperatore;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CODICE_SUPERUSER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Pannello00_Principale extends MyStepPanel {

	// Parametri del Pannello
	private final int numImage = 1;

	private boolean messaggioAggVisualizzato = false;

	// COSTRUTTORE
	public Pannello00_Principale() {

		super();

		setLayer();

	}

	// Dichiarazioni Pannello
	private void setLayer() {

		// Dichiarazione File Parametri
		impostaXml();

		// Definizione Caratteristiche Label Pannello
		impostaDimLabelPlus(0);
		impostaDimLabelSimple(8);
		impostaDimLabelHelp(2);
		impostaDimLabelTitle(2);
		impostaDimLabelProg(0);
		impostaDimLabelBut(10);
		impostaColori(4);

		// Inizializzazione Colori dei Label
		initColorLabelSimple(elemColor[0]);
		initColorLabelHelp(elemColor[1]);
		initColorLabelTitle(elemColor[2]);

		// Inizializzazione Validità Pulsanti
		impostaValButton(false);

		// Configurazione di Base Pannello
		configuraPannello();

		// Inserimento Area di Testo
		inserisciTextField();

		// Inserimento Tastiera per il Login dell'Operatore
		inserisciTastiera();

		// Inizializzazione Validità Tastiera
		tastiera.impostaValiditaTastiera(false);

		// Inserimento Action Listener per il Login dell'Operatore
		txtField.addActionListener(new ActionListenerInserimentoCodiceOperatore(this));

		// Inserimento Immagini Ausiliarie
		new ThreadInserisciLabelImageAux(this, numImage, false).start();

		elemBut[9].setVisible(false);
	}

	// Inizializzazione Pannello
	public void initPanel() {

		// Impostazione Visibilità Tastiera
		tastiera.impostaValiditaTastiera(true);

		// Impostazione Validità Tastiera
		impostaValButton(true);

		if (DettagliSessione.getCodiceFigura().equals("")) {
			ImpostaVisibilitaPannello(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(208)));
		} else {

			ImpostaVisibilitaPannello(false);
		}

		// Avvio Thread di Lettura Messaggi Macchina da Database
		new LeggiDizionario().start();

		// Impostazione Visibilità Pannello
		setPanelVisibile();

		/////////////////////
		// AGGIORNAMENTO ///
		/////////////////////
		elemBut[0].setEnabled(true);
		elemBut[1].setEnabled(true);
		elemBut[4].setEnabled(true);
		elemBut[5].setEnabled(true);
		elemBut[6].setEnabled(true);
		elemBut[7].setEnabled(true);

		visualizzaMessaggiOperatore();

		visualizzaMessaggiUltimoAggiornamento();
		

	}

	public void visualizzaMessaggiUltimoAggiornamento() {

		if (Boolean.parseBoolean((ParametriSingolaMacchina.parametri.get(307))) && !messaggioAggVisualizzato) {

			messaggioAggVisualizzato = true;

			int ggUltimoAggiornamento = ConteggioGiorniUltimoAggiornamento();

			if (ggUltimoAggiornamento >= 0) {

				/////////////////////////////////////////////////////////
				// TROPPI GIORNI TRASCORSI DALL'ULTIMO AGGIORNAMENTO ///
				/////////////////////////////////////////////////////////
				SessionLogger.logger.log(Level.INFO, "Giorni trascorsi dall''ultimo aggiornamento ={0}",
						ggUltimoAggiornamento);

				if (ggUltimoAggiornamento > Integer.parseInt(ParametriSingolaMacchina.parametri.get(306))) {

					((Pannello45_Dialog) pannelliCollegati.get(8)).gestoreDialog.visualizzaMessaggio(7);
				}

			} else {

				//////////////////////////////////////////////////////////////////////
				// DATA DEL COMPUTER ANTERIORE A QUELLA DELL'ULTIMO AGGIORNAMENTO ///
				//////////////////////////////////////////////////////////////////////
				SessionLogger.logger.log(Level.SEVERE,
						"Data incongruente rispetto alla data Ultimo aggiornamento = {0}", ggUltimoAggiornamento);

				((Pannello45_Dialog) pannelliCollegati.get(8)).gestoreDialog.visualizzaMessaggio(8);

			}
		}

	}

	public void visualizzaMessaggiOperatore() {

		if (!ParametriSingolaMacchina.parametri.get(462).equals("_")) {

			if (((Pannello45_Dialog) pannelliCollegati.get(8)).gestoreDialog.visualizzaMessaggio(6) == 0) {
				// inizializza messaggio
				AggiornaValoreParSingMacOri(462, "_");
			}
		}

	}

	public void controllaCodiceOperatore(String codice) {

		if (!codice.equals("")) {

			if (codice.equals(CODICE_SUPERUSER)) {

				DettagliSessione.setIdFigura(0);
				DettagliSessione.setIdFiguraTipo(0);
				DettagliSessione.setFigura("");
				DettagliSessione.setNominativoFigura("");
				DettagliSessione.setCodiceFigura(codice);
				DettagliSessione.setSuperUser(true);

				// Impostazione Visibilità Pannello
				ImpostaVisibilitaPannello(false);

			} else if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(387))) {

				ArrayList<String> dettagliOperatore = TrovaDettagliOperatore(codice);

				if (dettagliOperatore.size() > 0) {

					DettagliSessione.setIdFigura(Integer.parseInt(dettagliOperatore.get(0)));
					DettagliSessione.setIdFiguraTipo(Integer.parseInt(dettagliOperatore.get(1)));
					DettagliSessione.setFigura(dettagliOperatore.get(2));
					DettagliSessione.setNominativoFigura(dettagliOperatore.get(3));
					DettagliSessione.setCodiceFigura(dettagliOperatore.get(4));
					DettagliSessione.setSuperUser(false);

					// Impostazione Visibilità Pannello
					ImpostaVisibilitaPannello(false);

				}
			} else {

				DettagliSessione.setIdFigura(0);
				DettagliSessione.setIdFiguraTipo(0);
				DettagliSessione.setFigura("");
				DettagliSessione.setNominativoFigura("");
				DettagliSessione.setCodiceFigura(codice);
				DettagliSessione.setSuperUser(false);

				// Impostazione Visibilità Pannello
				ImpostaVisibilitaPannello(false);
			}

			// Inizializzazione Testo Text Field
			txtField.setText("");
		}
	}

	// Lettura Informazioni da Database
	private class LeggiDizionario extends Thread {

		@Override
		public void run() {

			if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

				// Impostazione lingua del Pannello
				language = ParametriSingolaMacchina.parametri.get(111);

				// Aggiornamento Testo Label
				elemLabelSimple[0].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 1, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[1].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 2, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[2].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 3, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[3].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 4, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[4].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 5, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[5].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 6, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[6].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 7, ParametriSingolaMacchina.parametri.get(111)));
				elemLabelSimple[7].setText(
						TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 8, ParametriSingolaMacchina.parametri.get(111)));

				// Messaggio di Aiuto
				elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11,
						ParametriSingolaMacchina.parametri.get(111)));
				elemLabelHelp[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 620,
						ParametriSingolaMacchina.parametri.get(111)));

				// Titoli
				elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 618,
						ParametriSingolaMacchina.parametri.get(111)));
				elemLabelTitle[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 619,
						ParametriSingolaMacchina.parametri.get(111)));

			}
		}
	}

	// Gestione Visibilità Elementi del Pannello
	public void ImpostaVisibilitaPannello(boolean vis) {

		// Visibilità Pulsanti
		for (int i = 0; i < elemBut.length - 2; i++) {

			elemBut[i].setVisible(!vis);
			elemLabelSimple[i].setVisible(!vis);
		}

		// Visibilità Pulsante Invio
		elemBut[8].setVisible(vis);

		// Visibilità Text Label Inserimento Codice Operatore
		txtField.setVisible(vis);

		// Visibilità Text Label Inserimento Codice Operatore
		txtField.setFocusable(true);

		// Visibilità Immagine di Sfondo Pannello
		imageSfondo.setVisible(!vis);

		// Visibilità Immagine di Sfondo Inserimento Codice Operatore
		labelImageAux[0].setVisible(vis);

		// Visibilità Messaggi di Aiuto
		for (JLabel elemLabelHelp1 : elemLabelHelp) {
			elemLabelHelp1.setVisible(vis);
		}

		// Visibilità Tastiera
		tastiera.impostaVisibilitaTastiera(vis);

		// Visibilità Label Title
		for (JLabel elemLabelTitle1 : elemLabelTitle) {
			elemLabelTitle1.setVisible(vis);
		}
	}

	// Impostazione Validità Pulsanti
	public void impostaValButton(boolean val) {

		for (JButton elemBut1 : elemBut) {
			elemBut1.setEnabled(val);
		}

	}

	// Gestione Pulsanti
	public void gestorePulsanti(String button) {

		// pannelloAvanti = null;
		if (button.equals(elemBut[0].getName())) {

			///////////////////////////
			// PANNELLO PRODUZIONE ///
			///////////////////////////
			gestoreScambioPannello(pannelliCollegati.get(0));

		} else if (button.equals(elemBut[2].getName())) {

			/////////////////////
			// AGGIORNAMENTO ///
			/////////////////////
			// if (Benefit.findIdMacchinaOri() > 0 &&
			///////////////////// !Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(373)))
			///////////////////// {
			gestoreScambioPannello(pannelliCollegati.get(1));

			// }
		} else if (button.equals(elemBut[5].getName())) {

			//////////////////////
			// CONFIGURAZIONE ///
			//////////////////////
			gestoreScambioPannello(pannelliCollegati.get(2));

		} else if (button.equals(elemBut[1].getName())) {

			///////////////
			// PULIZIA ///
			///////////////
			gestoreScambioPannello(pannelliCollegati.get(3));

		} else if (button.equals(elemBut[6].getName())) {

			/////////////////
			// CONTROLLO ///
			/////////////////
			gestoreScambioPannello(pannelliCollegati.get(4));

		} else if (button.equals(elemBut[3].getName())) {

			///////////////////
			// SPEGNIMENTO ///
			///////////////////
			gestoreScambioPannello(pannelliCollegati.get(5));

		} else if (button.equals(elemBut[7].getName())) {

			//////////////////
			// ASSISTENZA ///
			//////////////////
			gestoreScambioPannello(pannelliCollegati.get(6));

		} else if (button.equals(elemBut[4].getName())) {

			///////////////
			// RICERCA ///
			///////////////
			gestoreScambioPannello(pannelliCollegati.get(7));

		} else if (button.equals(elemBut[8].getName())) {

			txtField.postActionEvent();

		} else if (button.equals(elemBut[9].getName())) {

			///////////////////////////////////
			// IMPOSTA VIS COMANDI MANUALI ///
			////////////////////////////////////
			if (GestoreIO.getVisPannelli()) {
				// Visibilità comandi manuali IO
				GestoreIO.GestoreIO_ImpostaVisPannelli(false);
				// Visibilità Comandi Inverter
				Inverter_Gefran_BDI50_ImpostaVisPannelli(false);
			} else {
				// Visibilità comandi manuali IO
				GestoreIO.GestoreIO_ImpostaVisPannelli(true);
				// Visibilità Comandi Inverter
				Inverter_Gefran_BDI50_ImpostaVisPannelli(true);

			}

		}

	}

	// Gestione Scambio Pannello Collegato
	public void gestoreScambioPannello(MyStepPanel pannello) {

		if (pannello != null) {
			this.setVisible(false);
			if (pannello instanceof Pannello01_SceltaProduzione) {
				((Pannello01_SceltaProduzione) pannello).initPanel();
			} else if (pannello instanceof Pannello12_Aggiornamento) {
				((Pannello12_Aggiornamento) pannello).initPanel();
			} else if (pannello instanceof Pannello13_Configurazione_Generale) {
				((Pannello13_Configurazione_Generale) pannello).initPanel(false);
			} else if (pannello instanceof Pannello16_Controllo) {
				((Pannello16_Controllo) pannello).initPanel();
			} else if (pannello instanceof Pannello19_Spegnimento) {
				((Pannello19_Spegnimento) pannello).initPanel();
			} else if (pannello instanceof Pannello20_Assistenza) {
				((Pannello20_Assistenza) pannello).initPanel();
			} else if (pannello instanceof Pannello21_Ricerca) {
				((Pannello21_Ricerca) pannello).initPanel();
			} else if (pannello instanceof Pannello37_Pulizia) {
				((Pannello37_Pulizia) pannello).initPanel();
			}

		}
	}
}
