/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.processo;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaCodiceSacco;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaDataFineCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaStatoCodiceChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.AggiornaValoreParametriRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiNumChimicheCodiceSfusa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreValvolaAttuatoreMultiStadio;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RegistraNuovoCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaComponentiProdottoById;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDatiMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaGruppoValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdPresaPerPresa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIndiceTabellaRipristinoPerIdParRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaNumeroMisceleTabellaProcessoPerCodiceChimica;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreProdottoByIdProd;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaLunghezzaStringa;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RipulisciTabellaValoreRipristinoOri;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_COD_SACCHETTO_TRACC_DISAB; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MICRO_CHAR_PRESA_MICRO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import eu.personalfactory.cloudfab.macchina.bilancia.valvola.aperta.BilanciaSacchiValvolaAperta;
import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreRipristinoOri;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.TimeLineLogger;
import eu.personalfactory.cloudfab.macchina.microdosatore.Microdosatore_Inverter_2017;
import eu.personalfactory.cloudfab.macchina.panels.Pannello11_Processo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog; 
import eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants;
import eu.personalfactory.cloudfab.macchina.utility.ComponenteProdotto;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.macchina.utility.Prodotto;

/**
 * @author francescodigaudio
 *
 *         Questa Classe contiene i metodi e e gli oggetti necessari
 *         all'esecuzione del processo di realizzazione di un prodotto con
 *         Origami
 *
 */
public class Processo {

	// VARIABILI
	public Pannello11_Processo pannelloProcesso;
	public Timer timer = new Timer();
	public ArrayList<Integer> aliqScarico;
	public boolean miscelazioneCompletata, pesaSuccessivaCompletata, interrompiThreadControlloCocleaVuota,
			interrompiAnimConfezione, interrompiAnimMiscelatore, processoInterrottoManualmente, interrompiAnimCarico,
			interrompiThreadControlloInterruttoriManuali, pesoConfezioneRaggiunto, disattivaVariazioneVel,
			pesaComplementareCompletata, riaperturaValvola, confezionamentoInSecchio, interrompiComunicazioneSchedaIO2,
			interrompiThreadProcessoAttivaSvuotaValvolaRipetutoSecchi, dosaturaDirettaAttiva,
			attivaVelNormaleMiscelatore, attivaVelRapidaCarico, controlliInizialiInCorso,
			disattivaVarVelocitaMiscelatore, ignoraUlterioriSegnalazioniSacAggiuntivo;
	public double parPrestazioneMaterialeInizioPesaturaFine, parPrestazioneVelocitaScarico, parPrestazioneTempiScarico,
			materialeInizioPesaturaFine[], pesoDaRaggiungereConfezione[], parSecchioCorrettivoTempi[],
			parSecchioVelocita[];
	public int quantComponentePesata, numMiscelePesate, indiceCompInPesa, indiceConfezioneInPesa, numeroMateriePrime,
			parProdottoCorrettivoVelocita, parProdottoCorrettivoTempi, numConfezioniPerMiscela, limiti[][],
			tempoSvuotamentoTubo[], velocitaMiscelatoreFaseScarico[][], materialeMediamenteNelTubo[], tempiApertura[][],tempiChiusura[][];
	public String valorePesaCarico, pesoRealeSaccoDefinitoSoloPerOrigami4, valorePesaConfezioni, stringaConfigurazione,
			pesoRegistratoComponente;
	public ValoreRipristinoOri valRipristinoOri = new ValoreRipristinoOri();
	public String pesoVecchiImpianti[];
	public int counter_simulazione_peso;
	public double pesoMancanteConfezioni;
	public int numCompMicrodosatura;
	public String codChimicaSfusa = "";
	public boolean forzaPesoComponente, interrompiThreadVibro, forzaPesaAggiuntiva, ignoraConfigurazioneMicrodosatori;
	public String velComponente[];
	public String limComponente[];
	public int[] materialeDosaturaSoloVibro;
	public String curvaDosaggioCocleaExt[];
	public double pesoMancantePercCarico;
	public int iVelCaricoCorrente, ultimoPasso;
	public String velCaricoCorrente, idVibroFunghiSiloCorrente, idVibroPneumaticiSiloCorrente;
	public int corrAutoRegCarico, corrAutoRegCaricoPrec, counterConfigMicro;
	public boolean interruzioneControlloPesoCocleaVuota, interruzioneControlloPesoTastoReset,
			interruzioneControlloPesoRaggiunto, interruzioneControlloPesoInterruzioneManuale, interrompiAggiornamentoPesoRealeConfezione, 
			interrompiThreadControlloBloccoScarico, disabilitaCambioVelMiscelatoreBloccoScarico,
			raggiungimentoCondArrestoMotore, aspiratoreAttivato, threadFludificaPesaturaFineAttivo,
			threadApriChiudiValvolaAvviato, abilitaLineaDirettaMiscelatore,
			annullaInit, controlliInizialiEseguiti, eseguitoResetManuale, inControlloBilanciaCarico, resetProcesso,
			inControlloBilanciaConfezioni, vipaCommutaNettoBilanciaCarico, vipaCommutaLordoBilanciaCarico,
			vipaCommutaNettoBilanciaConfezioni, interrompiThreadProcessoAttivaSvuotaValvolaPesaturaFine,
			vipaCommutaLordoBilanciaConfezioni, autoRegVelAttiva, pulsanteSpeedAttivato, forzaturaVelManualeInCorso,
			threadInserimentoManualeInEsecuzione, svuotamento_valvova_da_eseguire, interrompiThreadGestoreSvuotaValvola,
			disattivaTracc, visibilitaMicro, cambioGestVelMixEseguito, interrompiThreadControlloContattoPiattaforma,
			threadPesaturaManualeEseguito;
	public List<Microdosatore_Inverter_2017> microdosatori_2017;
	public int indiceMicroVisibile = 0;
	public boolean interrompiControlloMicrodosatori, microdosaturaCompletata, ignoraControlloMicroSvuotaComponenti;
	public int counterMicroVis = 0;
	public ArrayList<ComponenteProdotto> componenti;
	public ArrayList<String> parametriDosaggioBilanciaSacchiValvolaAperta;
	public ArrayList<Boolean> esitoTestStatoContatti;
	public Prodotto prodotto;
	public BilanciaSacchiValvolaAperta bilanciaOMB;
	public boolean pesaturaManualeCompletata, interrompiLetturaPesoBilanciaManuale, azzeraBilanciaManuale,
			pesaturaManualeInCorso;
	public boolean microdosaturaAvviata;
	public boolean[] microdosatureEseguite;
	public boolean[] microdosatureEseguiteRegistrate;
	public boolean interrompiTrasmissioneMicrodosatori;
	public boolean controlloInizialeMicro; 

	// COSTRUTTORE
	@SuppressWarnings({})
	public Processo(Pannello11_Processo pannelloProcesso) {

		// Dichiarazione Pannello di Riferimento
		this.pannelloProcesso = pannelloProcesso;

		initVariabili();

		// Inzializzazione Logger di Processo
		ProcessoLogger.init();

		// Memorizzazione Log Processo
		ProcessoLogger.logger.info("Inizializzazione Processo");

		// Lettura dati relativi alla macchina per file di log
		ArrayList<String> datiStabilimento = TrovaDatiMacchina();

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO,
				"Id Stabilimento = {0}{1}Codice Stabilimento = {2}{3}Nome Stabilimento = {4}{5}Data  ={6}",
				new Object[] { datiStabilimento.get(0), LOG_CHAR_SEPARATOR, datiStabilimento.get(1), LOG_CHAR_SEPARATOR,
						datiStabilimento.get(2), LOG_CHAR_SEPARATOR, new Date() });

		// Memorizzazione Log Processo
		ProcessoLogger.logger
				.config("Creazione Oggetti Gestore Errori e Gestore Dialog per le Finestre di Segnalazione");

		((Pannello44_Errori) pannelloProcesso.pannelliCollegati.get(1)).setProcesso(this);

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Definizione Oggetti Gestore Inverter per Miscelatore e Carico");

		// Inizializzazione Variabili di Controllo
		resetProcesso = false;
		controlliInizialiEseguiti = false;
		idVibroFunghiSiloCorrente = "0";
		idVibroPneumaticiSiloCorrente = "0";
		controlloInizialeMicro = false;
		  
		 // Lettura Parametri Relativi al Carico
		 new ThreadProcessoLetturaParametriProcesso(this).start();

		 // Memorizzazione Log Processo
		 ProcessoLogger.logger.config("Fine Inizializzazione Processo");
			
 
	}

	// Inizializzazione Variabili
	private void initVariabili() {

		// Inizializzazione Variabili
		valorePesaCarico = "0";
		valorePesaConfezioni = "0";
		numCompMicrodosatura = 0;
		ignoraControlloMicroSvuotaComponenti = false;

	}

	// Analisi Stato Processo di Pesatura
	public void analizzaPesaComponenti() {

		/////////////////////////////
		// AVVIO PESATURA MANUALE ///
		/////////////////////////////

		// Avvio Thread Controllo Pesatura Manuale
		new ThreadProcessoGestorePesaturaManuale(this).start();

		// Lettura Numero Miscele Pesate da Database
		numMiscelePesate = Integer.parseInt(TrovaSingoloValoreParametroRipristino(62));

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO,
				"Analizza Pesa Componenti{0}IndiceComponenteInPesa ={1}{2}Numero di Miscele Pesate ={3}",
				new Object[] { LOG_CHAR_SEPARATOR, indiceCompInPesa, LOG_CHAR_SEPARATOR, numMiscelePesate });

		if (indiceCompInPesa >= numeroMateriePrime) {
 
			// Controllo Abilitazione Gestione
			if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(443))) {

				////////////////////////////////////////////////
				// CONTROLLO VELOCITA MISCELAZIONE ABILITATO ///
				////////////////////////////////////////////////

				inverter_mix.cambiaVelInverter(prodotto.getMixingSpeed());

			}
			
			// Avvia Controllo Pesatura Componenti con Microdosatori
		    analizzaPesaturaMicrodosatori();
 
			pannelloProcesso.resetVisPulsantiVibro();

		} else if ((verificaMicrodosatore(componenti.get(indiceCompInPesa).getPresa())
				|| Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(indiceCompInPesa)))
				|| componenti.get(indiceCompInPesa).getMetodoPesa().equals(ParametriGlobali.parametri.get(131))) {

			//////////////////////////////////////////////
			// COMPONENTE GIA' PESATO O DA NON PESARE ///
			//////////////////////////////////////////////
			// Incremento Indice Componente in Pesa
			indiceCompInPesa++;

			// Analisi Stato Processo di Pesa
			analizzaPesaComponenti();

		} else {

			////////////////////////////
			// COMPONENTE DA PESARE ///
			////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.INFO, "Avvio Pesa Componenti - IndiceComponenteInPesa ={0}",
					indiceCompInPesa);

			// Memorizzazione Log Processo
			TimeLineLogger.logger.log(Level.INFO, "Avvio Pesa Componenti - IndiceComponenteInPesa ={0}",
					indiceCompInPesa);

			// Aggiornamento Label Componenti su Pannello Processo
			pannelloProcesso.aggiornaLabelComponenti();

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Aggiornamento Label Componenti su Pannello Processo - Eseguito");

			new ThreadProcessoControlloPesoCarico(this).start();

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Analizza Pesa Componenti");

	}

	// Gestore Attivazione/Disattivazione Rele
	public void gestoreReleCoclee(Boolean value) {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Gestore Rele Coclee{0}Presa ={1}{2}Valore = {3}", new Object[] {
				LOG_CHAR_SEPARATOR, componenti.get(indiceCompInPesa).getPresa(), LOG_CHAR_SEPARATOR, value });

		try {

			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Calcolo indice uscita coclea da attivare
			String idUscitaColcea = Integer.toString(TrovaIdPresaPerPresa(componenti.get(indiceCompInPesa).getPresa()) - 1
					+ Integer.parseInt(USCITA_LOGICA_CONTATTORE_COCLEA_A));

			if (value) {
				// Attivazione Contattore Coclea
				GestoreIO_ModificaOut(idUscitaColcea, OUTPUT_TRUE_CHAR);
			} else {
				// Disattivazione Contattore Coclea
				GestoreIO_ModificaOut(idUscitaColcea, OUTPUT_FALSE_CHAR);

			}

			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Calcolo indice uscita vibro funghi
			idVibroFunghiSiloCorrente = Integer
					.toString(Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A)
							+ ((TrovaIdPresaPerPresa(componenti.get(indiceCompInPesa).getPresa()) - 1) * 3));
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Calcolo indice uscita vibro pneumatici
			idVibroPneumaticiSiloCorrente = Integer
					.toString(Integer.parseInt(USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A)
							+ ((TrovaIdPresaPerPresa(componenti.get(indiceCompInPesa).getPresa()) - 1) * 3));

		} catch (NumberFormatException e) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.SEVERE, "Errore Gestore Rele Coclee e ={0}", e);
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Gestore Rele Coclee");

	}

	// Finalizzazione Confezionamento
	public void fineConfezionamento() {

		// Aggiornamento Indice Pesa Confezioni Completata
		for (int i = 0; i < numConfezioniPerMiscela; i++) {

			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(51) + i, 51, "false",
					ParametriSingolaMacchina.parametri.get(15));

		}

		// Incremento idCiclo
		AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(79), 79,
				Integer.toString(Integer.parseInt(TrovaSingoloValoreParametroRipristino(79)) + 1),
				ParametriSingolaMacchina.parametri.get(15));

		// Aggiornamento Label Confezioni Pesate
		pannelloProcesso.aggiornaLabelConfezioniPesate(0);

		// Analizza lo Stato Attuale del Processo
		analizzaFineProcesso();

	}

	// Aggiornamento Gestore Processo
	public void aggiornaGestoreProcesso() {

		// Avvio Thread
		new ThreadProcessoGestoreProcesso(this).start();

	}

	// Controllo se il Processo è Concluso oppure è Necessario Riprendere dallo
	// Scarico dei Componenti
	public void analizzaFineProcesso() {

		// Aggiornamento Data Fine Ciclo
		AggiornaDataFineCiclo(Integer.parseInt(TrovaSingoloValoreParametroRipristino(83)));

		// Controllo Ultima Miscela
		if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(61))) {

			// Il Processo Viene Riportato alla Fase di Pesa Componenti Completata
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(54), 54, "1",
					ParametriSingolaMacchina.parametri.get(15));

			// Aggiornamento id_ciclo
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(83), 83,
					TrovaSingoloValoreParametroRipristino(91), ParametriSingolaMacchina.parametri.get(15));

		}

		aggiornaGestoreProcesso();

	}

	// Finalizzazione e Chiusura Metodi Processo
	public void finalizzazioneProcesso() {

		// Chiusura del Thread Controllo Interruttori Manuali
		interrompiThreadControlloInterruttoriManuali = true;

		if (!resetProcesso && !processoInterrottoManualmente) {

			processoInterrottoManualmente = false;

			if (!controlliInizialiInCorso) {
				 
				((Pannello45_Dialog) pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog.visualizzaMessaggio(1);
 
				// Eliminazione tabella Ripristino
				RipulisciTabellaValoreRipristinoOri();
				
				//new ThreadProcessoGestoreNastroFineProcesso().start();

			}

			// Scambio Pannello
			pannelloProcesso.gestoreScambioPannello();

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.info("Finalizzazione Processo");

	}

	// Registrazione Scarico Componenti su Database
	public void registraScaricoMateriali() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Registrazione Scarico Materiali");

		try {

			// Indice Pesa Componenti Completata
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(55), 55, "false",
					ParametriSingolaMacchina.parametri.get(15));

			// Indice Presenza di Materiale non Scaricato nella Pesa
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(56), 56, "false",
					ParametriSingolaMacchina.parametri.get(15));

			// Indice Presenza di Materiale non Scaricato nella Pesa
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(26), 26, "0",
					ParametriSingolaMacchina.parametri.get(15));

			// Cambio da "Formula Effettiva Componente in Pesa" a "Formula Effettiva
			// Componente in Miscelazione"
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(29), 29,
					TrovaSingoloValoreParametroRipristino(28), ParametriSingolaMacchina.parametri.get(15));

			// Reset Formula Effettiva Componente in Pesa
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(28), 28, "",
					ParametriSingolaMacchina.parametri.get(15));

			if (Integer.parseInt(TrovaSingoloValoreParametroRipristino(8)) > Integer
					.parseInt(TrovaSingoloValoreParametroRipristino(62))) {

				/////////////////////////////////////
				// PESA SUCCESSIVA DA EFFETTUARE ///
				/////////////////////////////////////
				// Aggiorna valore id_ciclo Pesa Successiva
				// Registrazione Nuovo Ciclo
				int nCiclo = RegistraNuovoCiclo(ParametriGlobali.parametri.get(129),
						Integer.parseInt(TrovaSingoloValoreParametroRipristino(92)),
						Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)),
						Integer.parseInt(TrovaSingoloValoreParametroRipristino(5)),
						Integer.parseInt(TrovaValoreProdottoByIdProd(
								Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)), 2)),
						Integer.parseInt(TrovaValoreProdottoByIdProd(
								Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)), 1)),
						Integer.parseInt(TrovaSingoloValoreParametroRipristino(9)), 0,
						TrovaValoreProdottoByIdProd(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)), 3),
						TrovaValoreProdottoByIdProd(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)), 4),
						TrovaValoreProdottoByIdProd(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)), 5),
						TrovaValoreProdottoByIdProd(Integer.parseInt(TrovaSingoloValoreParametroRipristino(1)), 6), "",
						"", "", "", "", "", "", "", "", "", TrovaSingoloValoreParametroRipristino(32),
						TrovaSingoloValoreParametroRipristino(99));

				TimeLineLogger.logger.log(Level.INFO, "Inserimento Ciclo Miscela Successiva - id_ciclo ={0}", nCiclo);

				// Aggiorna indice ciclo compoentni pesati
				AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(91), 91,
						Integer.toString(nCiclo), ParametriSingolaMacchina.parametri.get(15));

			}

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Registrazione Scarico Materiali Eseguita");

		} catch (NumberFormatException e) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.log(Level.SEVERE, "Registrazione Scarico Materiali - Fallita e ={0}", e);

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Registrazione Scarico Materiali");
	}

	// Interruzione Processo
	public void interrompiProcesso() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.warning("Chiamata Procedura Interruzione Processo");

		// Avvio Thread Inizializzazione Processo
		new ThreadProcessoInizializzaPannelloProcesso(pannelloProcesso).start();

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Disattivazione Uscite Digitali");

		GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
				// + USCITA_LOGICA_VIBRO_ELETTRICO_SILOS + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO + OUTPUT_SEP_CHAR + USCITA_LOGICA_SEGNALE_LUMINOSO_ROSSO
				+ OUTPUT_SEP_CHAR + USCITA_LOGICA_MOTORE_CHOPPER + OUTPUT_SEP_CHAR
				// + USCITA_LOGICA_MOTORE_VIBRO_BASE + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_NASTRO + OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_A + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_CONTATTORE_COCLEA_B + OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_C
				+ OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_D + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_CONTATTORE_COCLEA_E + OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_F
				+ OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_G + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_CONTATTORE_COCLEA_H + OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_I
				+ OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_J + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_CONTATTORE_COCLEA_K + OUTPUT_SEP_CHAR + USCITA_LOGICA_CONTATTORE_COCLEA_L
				+ OUTPUT_SEP_CHAR + USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE + OUTPUT_SEP_CHAR + USCITA_LOGICA_EV_BILANCIA_DI_CARICO
				+ OUTPUT_SEP_CHAR + USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA + OUTPUT_SEP_CHAR
				+ USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA + OUTPUT_SEP_CHAR
				// + USCITA_LOGICA_EV_SOFFIATORE_5_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
				// + USCITA_LOGICA_EV_SOFFIATORE_6_VALVOLA_SCARICO + OUTPUT_SEP_CHAR
				//// + USCITA_LOGICA_EV_VALVOLA_SCARICO_1 + OUTPUT_SEP_CHAR
				//// + USCITA_LOGICA_EV_VALVOLA_SCARICO_2 + OUTPUT_SEP_CHAR
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
				OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
////                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
////                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						// + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						// + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						// + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						// + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						// + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR
						+ OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
						+ OUTPUT_FALSE_CHAR);

		// Chiusura Valvola di Scarico
		if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

			////////////////////////////
			// ATTUATORE MULTISTADIO //
			////////////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0DEF);
			ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0DEF");
		} else {

			////////////////////
			// COMANDO UNICO //
			////////////////////
			GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_0_COMANDO_UNICO);
			ProcessoLogger.logger.log(Level.INFO, "Chiusura Vavola POS_0_COMANDO_UNICO");
		}

		pannelloProcesso.elemBut[14].setVisible(false);

		// Aggiornamento Ultimo Passo
		ultimoPasso = Integer.parseInt(TrovaSingoloValoreParametroRipristino(54));

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.CONFIG, "Aggiornamento Ultimo Passo ={0}", ultimoPasso);

		AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(46), 46, "false",
				ParametriSingolaMacchina.parametri.get(15));

		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(441))) {

			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(47), 47, "",
					ParametriSingolaMacchina.parametri.get(15));
		}
		///////////////
		switch (ultimoPasso) {

		case 1: {

			interrompiAnimCarico = true;
			break;
		}
		case 7: {
			interrompiMiscelazione();
			break;
		}

		case 8: { 
			interrompiMiscelazione();
			break;
		}

		}

		if (((Pannello44_Errori) pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(28) == 0) {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.warning("Riavvio Procedura in Seguito ad Interruzione Processo");

			processoInterrottoManualmente = false;

			new ThreadProcessoGestoreAvvioProcesso(this).start();

		} else {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.warning("Chiusura Procedura in Seguito ad Interruzione Processo");

			// Chiusura del Thread Controllo Interruttori Manuali
			interrompiThreadControlloInterruttoriManuali = true;


			pannelloProcesso.gestoreScambioPannello();

		}
	}

	// Interruzione Miscelazione
	public void interrompiMiscelazione() {
 
		// Memorizzazione Log Processo
		ProcessoLogger.logger.warning("Chiamata Procedura Interruzione Miscelazione");

		if (!abilitaLineaDirettaMiscelatore) {

			// Arresto Motore Miscelatore
			inverter_mix.avviaArrestaInverter(false);

			// Memorizzazione Log Processo
			ProcessoLogger.logger.config("Interruzione Miscelazione");

			// Attesa Tempo Delay Inverter
			try {
				ThreadProcessoMiscelazione.sleep(Integer.parseInt(ParametriGlobali.parametri.get(8)));
			} catch (InterruptedException ex) {
			}
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Disattivazione Uscita Logica Relè Motore Miscelatore");

		GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_FALSE_CHAR);

		interrompiAnimCarico = true;
	}

	// Procedura di Pesatura Confezioni
	public void analizzaPesaConfezioni() throws InterruptedException {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Analizza Pesa Confezioni{0}Indice Confezione in Pesa ={1}",
				new Object[] { LOG_CHAR_SEPARATOR, indiceConfezioneInPesa });

		if (indiceConfezioneInPesa >= numConfezioniPerMiscela) {

			///////////////////////////////////
			// PESATURA SACCHETI COMPLETATA ///
			///////////////////////////////////
			
			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Pesa Confezioni Completata");

			// Aggiornamento Indice Insacco Completato
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(67), 67, "true",
					ParametriSingolaMacchina.parametri.get(15));

			// Aggiornamento Indice Confezione In Pesa
			AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(49), 49, "0",
					ParametriSingolaMacchina.parametri.get(15));

			// Controllo Sacchetto Aggiuntivo
			if ((calcolaQResiduaStimata() > pesoDaRaggiungereConfezione[indiceConfezioneInPesa - 1]
					&& !ignoraUlterioriSegnalazioniSacAggiuntivo) || forzaPesaAggiuntiva) {

				forzaPesaAggiuntiva = false;

				ignoraUlterioriSegnalazioniSacAggiuntivo = Boolean
						.parseBoolean(ParametriSingolaMacchina.parametri.get(362));

				/////////////////////////////////////////////////////
				// CONDIZIONE DI SACCHETTO AGGIUNTIVO VERIFICATA ///
				/////////////////////////////////////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Condizioni di Sacchetto Aggiuntivo Raggiunte");

				// Visualizzazione Finestra Richiesta Insacco Confezione Aggiuntiva
				if (((Pannello45_Dialog) pannelloProcesso.pannelliCollegati.get(2)).gestoreDialog
						.visualizzaMessaggio(0) == 1) {

					////////////////////////////////////////////////
					// RICHIESTA CONFEZIONE AGGIUNTIVA IGNORATA ///
					////////////////////////////////////////////////
					// Finalizzazione Confezionamento
					fineConfezionamento();

					// Memorizzazione Log Processo
					ProcessoLogger.logger.info("Ignorata Richiesta Confezione Aggiuntiva");

				} else {

					/////////////////////////////////////////////////
					// RICHIESTA CONFEZIONE AGGIUNTIVA CONFERMATA ///
					/////////////////////////////////////////////////
					// Memorizzazione Log Processo
					ProcessoLogger.logger.info("Pesatura Sacchetto Aggiuntivo");

					// Riduzione indice confezione in Pesa
					indiceConfezioneInPesa--;

					// Aggiornamento parametro confezione pesata su database
					AggiornaValoreParametriRipristino(this,
							TrovaIndiceTabellaRipristinoPerIdParRipristino(51) + indiceConfezioneInPesa, 51, "false",
							ParametriSingolaMacchina.parametri.get(15));

					// Riattivazione Procedura di Pesa Confezioni
					analizzaPesaConfezioni();

				}
			} else {

				/////////////////////////////////////////////////////////
				// CONDIZIONE DI SACCHETTO AGGIUNTIVO NON VERIFICATA ///
				/////////////////////////////////////////////////////////
				TimeLineLogger.logger.log(Level.INFO, "Pesatura Confezioni Completata - id_ciclo ={0}",
						TrovaSingoloValoreParametroRipristino(83));

				// Finalizzazione Confezionamento
				fineConfezionamento();

			}

		} else {

			////////////////////////////////////////
			// PESATURA SACCHETI NON COMPLETATA ///
			////////////////////////////////////////
			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Pesa Sacchetti Non Completata");

			// Verifica se la Confezione è stata già Pesata
			if (Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(51).get(indiceConfezioneInPesa))) {

				//////////////////////////////
				// CONFEZIONE GIA' PESATA ///
				//////////////////////////////
				// Memorizzazione Log Processo
				ProcessoLogger.logger.config("Confezione Pesata");

				// Incremento indice confezione in Pesa
				indiceConfezioneInPesa++;

				// Riattivazione Procedura di Pesa Confezioni
				analizzaPesaConfezioni();

			} else {

				///////////////////////////
				// CONFEZIONE DA PESARE ///
				///////////////////////////
				
				// Verifica che il Codice Confezione sia Stato Inserito
				if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(46))
						&& !svuotamento_valvova_da_eseguire) {

					//////////////////////////////////////
					// CODICE CONFEZIONE NON INSERITO ///
					//////////////////////////////////////
					if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(441))) {

						//////////////////////////////////////
						// CONTEGGIO AUTOMATICO ABILITATO ///
						//////////////////////////////////////
						AggiornaCodiceSacco(this);

						// Interrompe Animazione Blocco Sacchetto
						interrompiAnimConfezione = true;

						pannelloProcesso.modificaPannello(13);

						// Impostazione Visibilità Sfondo Tastiera
						pannelloProcesso.labelImageAux[17].setVisible(false);

						pannelloProcesso.elemBut[1].setVisible(false);

						// Memorizzazione Log Processo
						ProcessoLogger.logger
								.config("Fine Loop Animazione Carico - Controllo Inserimento Codice Chimica");

						if (TrovaSingoloValoreParametroRipristino(54).equals("5")) {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Prima Confezione");

							// Avvio Gestore Processo
							aggiornaGestoreProcesso();

						} else {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Confezioni Successive");

							// Aggiorna Visualizzazione Panello
							pannelloProcesso.modificaPannello(5);

							// Riattivazione Controllo Blocco Sacchetto
							new ThreadProcessoControlloBloccoSacchetto(this).start();
						}

					} else if (disattivaTracc) {

						// Indice Codice Sacchetto Inserito
						AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(46), 46,
								"true", ParametriSingolaMacchina.parametri.get(15));

						// Codice Sacchetto
						AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(47), 47,
								DEFAULT_COD_SACCHETTO_TRACC_DISAB, ParametriSingolaMacchina.parametri.get(15));

						// Interrompe Animazione Blocco Sacchetto
						interrompiAnimConfezione = true;

						pannelloProcesso.modificaPannello(13);

						// Impostazione Visibilità Sfondo Tastiera
						pannelloProcesso.labelImageAux[17].setVisible(false);

						pannelloProcesso.elemBut[1].setVisible(false);

						// Memorizzazione Log Processo
						ProcessoLogger.logger
								.config("Fine Loop Animazione Carico - Controllo Inserimento Codice Chimica");

						if (TrovaSingoloValoreParametroRipristino(54).equals("5")) {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Prima Confezione");

							// Avvio Gestore Processo
							aggiornaGestoreProcesso();

						} else {

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Confezioni Successive");

							// Aggiorna Visualizzazione Panello
							pannelloProcesso.modificaPannello(5);

							// Riattivazione Controllo Blocco Sacchetto
							new ThreadProcessoControlloBloccoSacchetto(this).start();
						}

					} else {

						// Memorizzazione Log Processo
						ProcessoLogger.logger.config("Codice Confezione non Inserito");

						// Aggiornamento Pannello in modo da riattivare l'inserimento del codice
						// confezione
						pannelloProcesso.modificaPannello(4);

						// Riattiva l'Animazione Confezione
						new ThreadProcessoAnimazioneConfezione(this).start();
					}

				} else {

					// Memorizzazione Log Processo
					ProcessoLogger.logger.config("Codice Confezione Inserito");

					//////////////////////////////////
					// CODICE CONFEZIONE INSERITO ///
					//////////////////////////////////
					// Aggiornamento Indice Confezione in Pesa
					AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(49), 49,
							Integer.toString(indiceConfezioneInPesa), ParametriSingolaMacchina.parametri.get(15));

					if (!svuotamento_valvova_da_eseguire) {

						// SVUOTA VALVOLA PRIMA MISCELA DISABILITATO
						// Aggiornamento Stato Processo
						AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(54), 54,
								"9", ParametriSingolaMacchina.parametri.get(15));

					}

					// Aggiornamento visualizzazione pannello
					pannelloProcesso.aggiornaLabelPesaConfezioni("0");
					pannelloProcesso.modificaPannello(9);

					// Memorizzazione Log Processo
					ProcessoLogger.logger.info("Apertura Valvola di Scarico - Attivazione Vibro e Motore Miscelatore");

					// Apertura Totale Valvola
					if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(500))) {

						////////////////////////////
						// ATTUATORE MULTISTADIO //
						////////////////////////////
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100);
						ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100");
					} else {
						////////////////////
						// COMANDO UNICO //
						////////////////////
						GestoreValvolaAttuatoreMultiStadio(CloudFabConstants.POS_100_COMANDO_UNICO);
						ProcessoLogger.logger.log(Level.INFO, "Apertura Vavola POS_100_COMANDO_UNICO");
					}

					Thread.sleep(200);

					// Controllo confezionamento in sacco o in secchio
					if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(13))) {

						////////////////////////////////
						// CONFEZIONAMENTO IN SACCO ///
						////////////////////////////////
						if (prodotto.isVibroAttivo()) {

							GestoreIO_ModificaOut(
									USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
											+ USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO,
									OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_TRUE_CHAR);

						} else {

							////////////////////////////////
							// VIBRO VALVOLA DISATTIVATO //
							////////////////////////////////
							GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_TRUE_CHAR);

							// Memorizzazione Log Processo
							ProcessoLogger.logger.config("Confezionamento in Sacco - Apertura Vibro Base");

						}
					} else {

						/////////////////////////////////
						// CONFEZIONAMENTO IN SECCHIO ///
						/////////////////////////////////
						if (prodotto.isVibroAttivo()
								&& Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(224))) {

							///////////////////////////
							// VIBRO VALVOLA ATTIVO //
							///////////////////////////
							GestoreIO_ModificaOut(
									USCITA_LOGICA_MOTORE_MISCELATORE_RUN + OUTPUT_SEP_CHAR
											+ USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO,
									OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
											+ OUTPUT_TRUE_CHAR);

						} else {

							///////////////////////////////
							// VIBRO VALVOLA DISATTIVATO //
							///////////////////////////////
							GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_RUN, OUTPUT_TRUE_CHAR);

						}

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))
								&& (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321)))) {
							// Aperture ripetute svuota valvola
							new ThreadProcessoAttivaSvuotaValvolaRipetutoSecchi(this).start();
						}

						// Memorizzazione Log Processo
						ProcessoLogger.logger.info("Confezionamento in Secchio");

					}

					TimeLineLogger.logger.log(Level.INFO, "Avvio Insaccamento - id_ciclo ={0}",
							TrovaSingoloValoreParametroRipristino(83));

					// Aggiorna Istante Avvio Insacco
					AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(93), 93,
							new SimpleDateFormat(ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT).format(new Date()),
							ParametriSingolaMacchina.parametri.get(15));
					;

					// Avvio Thread Attesa Apertura Valvola di Scarico
					new ThreadProcessoAttendiAperturaValvolaScarico(this).start();

				}
			}
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Analizza Pesa Confezioni");

	}
	// Registrazione Chimica Utilizzata

	public void registraChimicaUsata() {

		if (pannelloProcesso.isVisible()) {

			if (!(prodotto.getNome().substring(0, ParametriSingolaMacchina.parametri.get(143).length())
					.equals(ParametriSingolaMacchina.parametri.get(143)))
					&& !(prodotto.getNome().substring(0, ParametriSingolaMacchina.parametri.get(302).length())
							.equals(ParametriSingolaMacchina.parametri.get(302)))
					&& !Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))) {

				try {

					if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))) {

						/////////////////////
						// CHIMICA SFUSA ///
						/////////////////////
						// Lettura Codice Chimica Sfusa
						String codiceChimica = TrovaSingoloValoreParametroRipristino(58);

						// Memorizzazione Log Processo
						ProcessoLogger.logger.config("Codice Chimica Sfusa");

						///////////////////////////////////////
						// CALCOLO NUMERO LOTTI REALIZZATI ///
						///////////////////////////////////////
						int numLottiRealizzati = TrovaNumeroMisceleTabellaProcessoPerCodiceChimica(codiceChimica);

						// Memorizzazione Log Processo
						ProcessoLogger.logger.log(Level.CONFIG, "Lotti Realizzati = {0}", numLottiRealizzati);

						/////////////////////////////////////////
						// CALCOLO NUMERO LOTTI REALIZZABILI ///
						/////////////////////////////////////////
						// Estrazione numero di Chimiche realizzabili con un codice chimica Sfusa
						int numLottiRealizzabili = EstraiNumChimicheCodiceSfusa(codiceChimica,
								ParametriGlobali.parametri.get(20).charAt(0));

						// Memorizzazione Log Processo
						ProcessoLogger.logger.log(Level.CONFIG, "Lotti Realizzati = {0}{1}Lotti Realizzabili = {2}",
								new Object[] { numLottiRealizzati, LOG_CHAR_SEPARATOR, numLottiRealizzabili });

						// Controllo Raggiunginemnto Limite Chimiche Realizzabili
						if (numLottiRealizzabili > 0 && numLottiRealizzati >= numLottiRealizzabili - 1) {

							///////////////////////////////////////////
							// LIMITE LOTTI REALIZZABILI RAGGIUNTO ///
							///////////////////////////////////////////
							AggiornaStatoCodiceChimica(codiceChimica, true);

							// Memorizzazione Log Processo
							ProcessoLogger.logger.info("Limite lotti Realizzaibili Raggiunto");

						}

					} else {

						////////////////////////////
						// CHIMICA CONFEZIONATA ///
						////////////////////////////
						// Memorizzazione Log Processo
						ProcessoLogger.logger.info("Chimica Confezionata");

						AggiornaStatoCodiceChimica(TrovaSingoloValoreParametroRipristino(58), true);

					}

					// Memorizzazione Log Processo
					ProcessoLogger.logger.config("Aggiornamento Tabella Chimica - Eseguito con Successo");

				} catch (Exception ex) {

					((Pannello44_Errori) pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(11);

					// Memorizzazione Log Processo
					ProcessoLogger.logger.log(Level.SEVERE, "Aggiornamento Tabella Chimica Fallito - e ={0}", ex);

				}
			} else {

				// Memorizzazione Log Processo
				ProcessoLogger.logger.info("Utilizzo Chimica Interna");

			}
		}
	}

	// Abilitazione Linea Diretta Miscealtore
	public void abilitaLineaDirettaMiscelatore() {

		/////////////////////
		// LINEA DIRETTA ///
		/////////////////////
		// Memorizzazione Log Processo
		ProcessoLogger.logger.warning("Abilitazione Linea Diretta Motore Miscelatore");

		GestoreIO_ModificaOut(USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER, OUTPUT_TRUE_CHAR);

	}

	// Verifica Pesatura con Microdosatore o con Coclea
	public boolean verificaMicrodosatore(String presa) {
		boolean result = false;

		if ((presa.length() > 1 && presa.charAt(0) == MICRO_CHAR_PRESA_MICRO.charAt(0))) {

			numCompMicrodosatura++;

			result = true;
		}
		return result;

	}

	// Avvia Controllo Pesatura Componenti con Microdosatori
	public void analizzaPesaturaMicrodosatori() {

		pannelloProcesso.elemBut[8].setVisible(false);
		pannelloProcesso.elemLabelSimple[27].setVisible(false);

		pannelloProcesso.labelImageAux[0].setVisible(false);
		pannelloProcesso.labelImageAux[1].setVisible(false);

		if (numCompMicrodosatura == 0) {
 
			// Registra fine Pesa Componenti
			finalizzaPesaComponenti();

		}

		if (!Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(55))
				&& (Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)) > 0)) {

			///////////////////////////
			// AVVIO MICRODOSATORI ///
			///////////////////////////
			// Avvio Thread Microdosatura
			new ThreadProcessoGestoreMicrodosaturaAvvio(this).start();

		}

		// Interruzione Thread di Controllo Coclea Vuota
		interrompiThreadControlloCocleaVuota = true;

	}

	public class ThreadFinalizzaPesatura extends Thread {

		Processo processo;

		public ThreadFinalizzaPesatura(Processo processo) {
			this.processo = processo;
		}

		@Override
		public void run() {

			// Memorizzazione Log Processo
			ProcessoLogger.logger.info("Avvio Thread Pesatura Completata ");

			boolean pesaturaCompletata = false;
			
			//Interruzione animazione carico
			interrompiAnimCarico = true;

			while (pannelloProcesso.isVisible() && !processoInterrottoManualmente && !resetProcesso
					&& !pesaturaCompletata) {

				boolean pesaturaComponentiDaCompletare = false;

				for (int i = 0; i < componenti.size(); i++) {

					///////////////////////////
					// PESATURA COMPONENTI ///
					///////////////////////////
					if (!Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(i))) {
						pesaturaComponentiDaCompletare = true;
					}

				}

				pesaturaCompletata = !pesaturaComponentiDaCompletare
						&& !pannelloProcesso.pannelliCollegati.get(3).isVisible();

				try {
					ThreadFinalizzaPesatura.sleep(200);
				} catch (InterruptedException ex) {
					Logger.getLogger(Processo.class.getName()).log(Level.SEVERE, null, ex);
				}

			}

			if (pesaturaCompletata) {

				// Incremento Numero Miscele Pesate
				numMiscelePesate++;

				// Aggiornamento Numero Miscele Pesate sul Pannello Processo
				pannelloProcesso.aggiornaLabelMiscelePesate(numMiscelePesate);

				//////////////////////////////////////////////////////////
				// MEMORIZZAZIONE PESA COMPONENTI DA SILOS COMPLETATA ////
				//////////////////////////////////////////////////////////
				// Incremento Miscele Pesate
				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(62), 62,
						Integer.toString(numMiscelePesate), ParametriSingolaMacchina.parametri.get(15));

				// Azzeramento Indice Pesa Componente Terminata
				for (int i = 0; i < numeroMateriePrime; i++) {
					AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(25) + i,
							25, "false", ParametriSingolaMacchina.parametri.get(15));
				}

				// Azzeramento Valore di Tara Bilancia Componenti
				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(52), 52, "0",
						ParametriSingolaMacchina.parametri.get(15));

				// Indice Pesa Componenti Completata
				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(55), 55,
						"true", ParametriSingolaMacchina.parametri.get(15));

				// Indice Presenza di Materiale non Scaricato nella Pesa
				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(56), 56,
						"true", ParametriSingolaMacchina.parametri.get(15));

				// Reinizializzazione Variabili
				indiceCompInPesa = 0;
				quantComponentePesata = 0;

			}

			// Lettura Ultimo Passo
			if (Integer.parseInt(TrovaSingoloValoreParametroRipristino(54)) == 6) {

				AggiornaValoreParametriRipristino(processo, TrovaIndiceTabellaRipristinoPerIdParRipristino(54), 54, "7",
						ParametriSingolaMacchina.parametri.get(15));
				 
			}
			
			// Aggiornamento Gestore Processo
			aggiornaGestoreProcesso();
		}
	}

	public void finalizzaPesaComponenti() {
		
		new ThreadFinalizzaPesatura(this).start();
		
	}

	// Inizializzaione Vel Miscelatore
	public void inizializzazioneVelMiscelatore() {

		//////////////////////////////////////////////
		// INIZIALIZZAZIONE VELOCITA' MISCELATORE ///
		//////////////////////////////////////////////
		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Inizializzazione Vel Miscelatore");

		String velIniziale;

		// Inizializzazione Vel Motore Miscelatore - Vel Normale
		if (!confezionamentoInSecchio) {

			////////////////////////////////
			// CONFEZIONAMENTO IN SACCO ///
			////////////////////////////////
			// Vel Iniziale
			velIniziale = VerificaLunghezzaStringa(velocitaMiscelatoreFaseScarico[indiceConfezioneInPesa][2], 4);

			if (!abilitaLineaDirettaMiscelatore) {

				// Impostazione Vel Iniziale Motore Miscelatore = Vel Normale
				inverter_mix.cambiaVelInverter(velIniziale);

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Inizializzazione Vel Miscelatore Eseguita - Valore ={0}",
						velIniziale);
			}

		} else {

			//////////////////////////////////
			// CONFEZIONAMENTO IN SECCHIO ///
			//////////////////////////////////
			// Vel Iniziale
			velIniziale = VerificaLunghezzaStringa((int) parSecchioVelocita[indiceConfezioneInPesa], 4);

			if (!abilitaLineaDirettaMiscelatore) {

				// Impostazione Vel Iniziale Motore Miscelatore
				inverter_mix.cambiaVelInverter(velIniziale);

				// Memorizzazione Log Processo
				ProcessoLogger.logger.log(Level.INFO, "Inizializzazione Vel Miscelatore Eseguita - Valore ={0}",
						velIniziale);

			}

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Fine Inizializzazione Cambio Vel Miscelatore");
	}

	// Calcola la quantita di materiali stimata all'interno del miscelatore
	private int calcolaQResiduaStimata() {

		// Memorizzazione Log Processo
		ProcessoLogger.logger.config("Procedura Calcola quantita Residua Componente nel Miscelatore");

		int result = 0;

		///////////////////////////////////
		// CALCOLO QUANTITA COMPONENTI ///
		///////////////////////////////////
		String formulaTeorica = TrovaSingoloValoreParametroRipristino(29);

		boolean charSepFound = false;
		String strTemp = "";

		if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {

			//////////////////////////////////
			// REGISTRAZIONE INFO STANDARD ///
			//////////////////////////////////
			for (int i = 0; i < formulaTeorica.length(); i++) {
				if (!charSepFound) {
					if (formulaTeorica.charAt(i) == ParametriGlobali.parametri.get(22).charAt(0)) {
						charSepFound = true;
					}
				} else if (formulaTeorica.charAt(i) == ParametriGlobali.parametri.get(21).charAt(0)) {
					charSepFound = false;
					result += Integer.parseInt(strTemp);
					strTemp = "";
				} else {
					strTemp += formulaTeorica.charAt(i);
				}

			}
			if (!strTemp.equals("")) {
				result += Integer.parseInt(strTemp);
			}

		} else {

			////////////////////////////////////
			// REGISTRAZIONE INFO AUSILIARIE ///
			////////////////////////////////////

			// Creazione Lista di sottostringhe
			ArrayList<String> substr = new ArrayList<>();
			for (int i = 0; i < formulaTeorica.length(); i++) {

				if (!charSepFound) {
					if (formulaTeorica.charAt(i) == ParametriGlobali.parametri.get(22).charAt(0)) {
						charSepFound = true;
					}
				} else if (formulaTeorica.charAt(i) == ParametriGlobali.parametri.get(21).charAt(0)) {
					charSepFound = false;
					substr.add(strTemp);
					strTemp = "";
				} else {
					strTemp += formulaTeorica.charAt(i);
				}

			}
			substr.add(strTemp);

			// Estrazione Peso Reale dalla sottostringa
			for (int i = 0; i < substr.size(); i++) {
				String res = "";
				for (int j = 0; j < substr.get(i).length(); j++) {

					if (substr.get(i).charAt(j) == ParametriGlobali.parametri.get(22).charAt(0)) {
						try {
							result += Integer.parseInt(res);
						} catch (NumberFormatException e) {
						}
						break;
					} else if (j == substr.get(i).length() - 1) {
						res += substr.get(i).charAt(j);
						try {
							result += Integer.parseInt(res);
						} catch (NumberFormatException e) {
						}
						break;
					} else {
						res += substr.get(i).charAt(j);
					}

				}

			}

		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.CONFIG, "Quantita totole componenti processo corrente ={0}", result);

		////////////////////////////////
		// AGGIUNTA QUANTITA CHIMICA ///
		////////////////////////////////
		// Lettura dei Componenti Relativi al Prodotto Selezionato
		List<?> componentiColl = TrovaComponentiProdottoById(prodotto.getId());

		// Calcolo Dimensione Totale Miscela
		for (Object o : componentiColl) {
			ComponentePesaturaOri componentiProdOri = (ComponentePesaturaOri) o;
			if (componentiProdOri.getIdComp() == Integer.parseInt(ParametriSingolaMacchina.parametri.get(301))) {
				result += componentiProdOri.getQuantita();
			}
		}

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.CONFIG, "Quantita chimica processo corrente ={0}", result);

		/////////////////////////////////////////////////////////
		// MEMORIZZAZIONE QUANTITA' TOTALE COMPONENTI PESATA ///
		/////////////////////////////////////////////////////////
		// Indice Pesa Componenti Completata
		AggiornaValoreParametriRipristino(this, TrovaIndiceTabellaRipristinoPerIdParRipristino(73), 73,
				Integer.toString(result + Integer.parseInt(TrovaSingoloValoreParametroRipristino(73))),
				ParametriSingolaMacchina.parametri.get(15));

		/////////////////////////////////////////////////////////////
		// LETTURA QUANTITA' TOTALE COMPONENTI PESATA AGGIORNATA ///
		/////////////////////////////////////////////////////////////
		result = Integer.parseInt(TrovaSingoloValoreParametroRipristino(73));

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Quantita totale componenti pesati ={0}", result);

		/////////////////////////////////////////////////
		// LETTURA QUANTITA' TOTALE SACCHETTI PESATA ///
		/////////////////////////////////////////////////
		result -= Integer.parseInt(TrovaSingoloValoreParametroRipristino(72));

		// Memorizzazione Log Processo
		ProcessoLogger.logger.log(Level.INFO, "Quantita Totale di Materiale Residuo Stimato ={0}", result);

		return result;

	}

	// Attivazione Vibro Coclea
	public void attivaVibroFunghiSiloCorrente(boolean value) {

		if (value) {

			GestoreIO_ModificaOut(idVibroFunghiSiloCorrente, OUTPUT_TRUE_CHAR);
		} else {

			GestoreIO_ModificaOut(idVibroFunghiSiloCorrente, OUTPUT_FALSE_CHAR);

		}
	}

	// Attivazione Vibro Coclea
	public void attivaVibroPneumaticiSiloCorrente(boolean value) {

		if (value) {

			GestoreIO_ModificaOut(idVibroPneumaticiSiloCorrente, OUTPUT_TRUE_CHAR);
		} else {

			GestoreIO_ModificaOut(idVibroPneumaticiSiloCorrente, OUTPUT_FALSE_CHAR);

		}
	}

}
