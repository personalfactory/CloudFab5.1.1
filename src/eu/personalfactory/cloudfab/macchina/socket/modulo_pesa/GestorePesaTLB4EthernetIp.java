/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.socket.modulo_pesa;

import de.re.eeip.EEIPClient;
import de.re.eeip.cip.datatypes.ConnectionType;
import de.re.eeip.cip.datatypes.Priority;
import de.re.eeip.cip.datatypes.RealTimeFormat;
import de.re.eeip.cip.exception.CIPException;
import eu.personalfactory.cloudfab.macchina.loggers.BilanceLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.CodificaStatusRegister;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_CARATTERE_SEPARAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_CHARSET_USCITE_TLB4;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_CALIBRAZIONE_AZZERAMENTO_DELLA_TARA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_COMANDO_USCITE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_COMMUTAZIONE_PESO_LORDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_COMMUTAZIONE_PESO_NETTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_IMPOSTAZIONE_HIGH_RES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_IMPOSTAZIONE_LOW_RES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_IMPOSTAZIONE_MODO_STANDARD;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_IMPOSTAZIONE_SET_POINT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_LETTURA_MODO_HIGH_RES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_LETTURA_MODO_LOW_RES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_PESO_LORDO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_PESO_NETTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_SET_POINTS;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_STATO_INGRESSI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TLB4_KWORD_LETTURA_STATUS_REGISTER;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SIMULAZIONE_PROCESSO;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imacdigaudio
 */
public class GestorePesaTLB4EthernetIp {

	/**
	 * @param args the command line arguments
	 * @throws java.io.IOException
	 * @throws de.re.eeip.cip.exception.CIPException
	 */
	// static ArrayList<String> statoEV;
	byte[] rep;
	byte[] rep2;
	String host;
	public EEIPClient eipClient;

	/////////////////////////////////////////////////
	// TLB4 - IMPOSTAZIONI MANUALI COMUNICAZIONE ///
	/////////////////////////////////////////////////
	private final Integer TLB4_SERIVCE_READ = 0x0E;
	private final Integer TLB4_SERVICE_WRITE = 0x10;
	private final Integer TLB4_CLASS_READ = 0x04;
	private final Integer TLB4_CLASS_WRITE = 0x04;
	private final Integer TLB4_INSTANCE_READ = 0x65;
	private final Integer TLB4_INSTANCE_WRITE = 0x64;
	private final Integer TLB4_ATTRIBUTE_READ = 0x03;
	private final Integer TLB4_ATTRIBUTE_WRITE = 0x03;

	//////////////////////////////////////
	// TLB4 - COMANDI COMMAND REGISTER ///
	//////////////////////////////////////
	private final Integer TLB4_REG_NESSUN_COMANDO = 0;
	private final Integer TLB4_REG_ATTIVA_TARA_SEMIAUTOMATICA_PESO_NETTO = 7;
	private final Integer TLB4_REG_ZERO_SEMIAUTOMATICO = 8;
	private final Integer TLB4_REG_DISATTIVA_TARA_SEMIAUTOMATICA_PESO_LORDO = 9;
	private final Integer TLB4_BLOCCO_TASTIERA = 21;
	private final Integer TLB4_SBLOCCO_TASTIERA_DISPLAY = 22;
	private final Integer TLB4_BLOCCO_TASTIERA_DISPLAY = 23;
	private final Integer TLB4_MODO_4X_DIV_LOW_RES = 24;
	private final Integer TLB4_MODO_4X_DIV_HIGH_RES = 25;
	private final Integer TLB4_MODO_STANDARD = 27;
	private final Integer TLB4_LETTURA_TARA_PREDETERMINATA = 87;
	private final Integer TLB4_SCRITTURA_TARA_PREDETERMINTA = 88;
	private final Integer TLB4_LETTURA_SETPOINT_1 = 90;
	private final Integer TLB4_LETTURA_SETPOINT_2 = 91;
	private final Integer TLB4_LETTURA_SETPOINT_3 = 92;
	private final Integer TLB4_SCRITTURA_SETPOINT_1 = 93;
	private final Integer TLB4_SCRITTURA_SETPOINT_2 = 94;
	private final Integer TLB4_SCRITTURA_SETPOINT_3 = 95;
	private final Integer TLB4_SALVA_DATI_IN_EPROM = 99;
	private final Integer TLB4_AZZERAMENTO_TARA_CALIBRAZIONE = 100;
	private final Integer TLB4_SALVA_PESO_CAMPIONE_CALIBRAZIONE = 101;
	private final Integer TLB4_LETTURA_PESO_CAMPIONE = 102;
	private final Integer TLB4_SCRITTURA_PESO_CAMPIONE = 103;
	private final Integer TLB4_STAMPA_PESO_ATTUALE = 110;
	private final Integer TLB4_ATTIVA_TARA_PREDETERMINATA = 111;

//////////////    private final String OUT_STR_TLB4 = "ABCabc";
//////////////    private final String CHAR_SEP_TLB4 = "_"; 
//////////////    private final String TLB4_KWORD_IMPOSTAZIONE_LOW_RES = "LOW"; 
//////////////    private final String TLB4_KWORD_IMPOSTAZIONE_MODO_STANDARD = "STD"; 
//////////////    private final String TLB4_KWORD_IMPOSTAZIONE_HIGH_RES = "HIGH"; 
//////////////    private final String TLB4_KWORD_COMMUTAZIONE_PESO_LORDO = "GROSS"; 
//////////////    private final String TLB4_KWORD_COMMUTAZIONE_PESO_NETTO = "NET"; 
//////////////    private final String TLB4_KWORD_LETTURA_PESO_NETTO = "n"; 
//////////////    private final String TLB4_KWORD_LETTURA_PESO_LORDO = "t"; 
//////////////    private final String TLB4_KWORD_LETTURA_LETTURA_MODO_LOW_RES = "l";
//////////////    private final String TLB4_KWORD_LETTURA_LETTURA_MODO_HIGH_RES = "h";
//////////////    private final String TLB4_KWORD_LETTURA_STATUS_REGISTER = "STREG";                     
//////////////    private final String TLB4_KWORD_LETTURA_STATO_INGRESSI = "IN";
//////////////    private final String TLB4_KWORD_COMANDO_USCITE = "O";
//////////////    private final String TLB4_KWORD_LETTURA_SET_POINTS = "LSP";
//////////////    private final String TLB4_KWORD_IMPOSTAZIONE_SET_POINT= "s";
//////////////    private final String TLB4_KWORD_CALIBRAZIONE_AZZERAMENTO_DELLA_TARA  = "CAL_AZTAR";
//////////////    private final String TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE = "CAL_P";
	///////////////////////
	// STATUS REGISTER ///
	///////////////////////
	// Bit 0 -- ERRORE CELLA DI CARICO
	// Bit 1 -- AVARIA DEL CONVERTITORE AD
	// Bit 2 -- PESO MASSIMO SUPERATO DI 9 DIVISIONI
	// Bit 3 -- PESO LORDO SUPERIORE AL 110% DEL FONDO SCALA
	// Bit 4 -- PESO LORDO OLTRE 999999 o inferiore a -999999
	// Bit 5 -- PESO NETTO OLTRE 999999 o inferiore a -999999
	// Bit 6 --
	// Bit 7 -- SEGNO NEGATIVO PESO LORDO
	// Bit 8 -- SEGNO NEGATIVO PESO NETTO
	// Bit 9 -- SEGNO NEGATIVO PESO PICCO
	// Bit 10 -- VISUALIZZAZIONE IN NETTO
	// Bit 11 -- STABILITA PESO
	// Bit 12 -- PESO ENTRO ±1⁄4 DIVISIONE ATTORNO ALLO ZERO
	// Bit 13 --
	// Bit 14 --
	// Bit 15 -- REFERENCE CELLE NON COLLEGATO
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// N.B.
	// 1) per la codifica dello status register usiamo la seguente codifica. Inviamo
	/////////////////////// solo
	// in caso di true con lettera maiuscola per indicare la posizione, ad esempio 3
	/////////////////////// posizione positiva,
	// stringa codificata con la funzione CodificaStatusRegister "C"
	// per la decodifica utilizziamo la funziona DecodificaStatusRegister in Benefit
	//
	// 2) per mantenere la compatibilità con le versioni percedenti di CloudFab le
	/////////////////////// stringhe commutazione netto, lordo e lettura
	// peso vengono tradotte dal protocollo ASCII e vengono individuate dal
	/////////////////////// carattere iniziale $.
	//
	// PAROLE CHIAVE
	// - "LOW"; IMPOSTAZIONE MODO LOW RES
	// - "STD": IMPOSTAZIONE MODO STANDARD
	// - "HIGH": IMPOSTAZIONE HIGH RES
	// - "GROSS": COMMUTAZIONE PESO LORDO. Viene estrapolato dalla stringa ASCII
	/////////////////////// utilizzata nelle precedenti versioni di cloudFab
	// - "NET": COMMUTAZIONE PESO NETTO. Viene estrapolato dalla stringa ASCII
	/////////////////////// utilizzata nelle precedenti versioni di cloudFab
	// - "n": LETTURA PESO NETTO. Viene estrapolato dalla stringa ASCII utilizzata
	/////////////////////// nelle precedenti versioni di cloudFab
	// - "t": LETTURA PESO LORDO. Viene estrapolato dalla stringa ASCII utilizzata
	/////////////////////// nelle precedenti versioni di cloudFab
	// - "l": LETTURA LETTURA MODO LOW RES. I valori delle celle vengono inviati
	/////////////////////// sotto forma di stringa separata da un carattere
	// di separazione
	// - "h": LETTURA LETTURA MODO HIGH RES
	// - "STREG": LETTURA STATUS REGISTER. Inviamo solo dati solo in caso di valore
	/////////////////////// positivo usando le lettere dell'alfabeto
	// per individuare la posizione
	// - "IN": LETTURA STATO INGRESSI
	// - "O": COMANDO USCITE. Utilizziamo le lettere ABC per attivare le uscite
	/////////////////////// 1,2,3 e abc per disattivarle. Le uscite devono
	// essere impostate come PLC sul TLB
	// - "LSP": LETTURA SET POINTS. Restituisce una stringa con i valori dei
	/////////////////////// setpoint separati dal carattere di separazione CHAR_SEP
	// - "s": IMPOSTAZIONE SET POINT. Il primo valore dopo il carattere di controllo
	/////////////////////// s indica il setpoint quindi 1,2 o 3. I rimanenti
	// caratteri costituiscono il valore da impostare per il setpoint
	// - "CAL_AZTAR": CALIBRAZIONE - AZZERAMENTO DELLA TARA
	// - "CAL_P": CALIBRAZIONE - REGISTRAZIONE PESO CAMPIONE. Il primo valore dopo
	/////////////////////// la stringa di controllo CAL_P rappresenta il
	// valore del peso campione da impostare
	//
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////
	// COMMAND REGISTER - POSSIBILI COMANDI DA INVIARE ///
	//////////////////////////////////////////////////////
	// 0 NESSUN COMANDO
	// 1
	// 6
	// 7 ATTIVA TARA SEMIAUTOMATICA (VISUALIZZAZIONE NETTO)
	// 8 ZERO SEMIAUTOMATICO
	// 9 DISATTIVA TARA SEMIAUTOMATICA (VISUALIZZAZIONE LORDO)
	// 20
	// 21 BLOCCO TASTIERA
	// 22 SBLOCCO TASTIERA E DISPLAY
	// 23 BLOCCO TASTIERA E DISPLAY
	// 24 MODO: 4x DIVISIONI LOWRES
	// 25 MODO: 4x DIVISIONI HIRES
	// 26
	// 27 MODO: STANDARD
	// 86
	// 87** LETTURA TARA PREDETERMINATA
	// 88** SCRITTURA TARA PREDETERMINATA
	// 89
	// 90** LETTURA SETPOINT 1
	// 91** LETTURA SETPOINT 2
	// 92** LETTURA SETPOINT 3
	// 93** SCRITTURA SETPOINT 1
	// 94** SCRITTURA SETPOINT 2
	// 95** SCRITTURA SETPOINT 3
	// 98
	// 99 SALVA DATI IN EEPROM
	// 100 AZZERAMENTO DELLA TARA per calibrazione
	// 101 SALVA PESO CAMPIONE PER CALIBRAZIONE
	// 102** LETTURA PESO CAMPIONE
	// 103** SCRITTURA PESO CAMPIONE
	// 110 STAMPA PESO ATTUALE
	// 111
	// 130 ATTIVA TARA PREDETERMINATA
	// 131
	// COSTRUTTORE

	int ping_timeout = 0;

	public GestorePesaTLB4EthernetIp(String host) {

		this.host = host;

		BilanceLogger.logger.log(Level.INFO, "host bilancia ={0}", host);

	}

	public boolean creaEipClient(Integer ping_timeout) {

		boolean result = false;

		this.ping_timeout = ping_timeout;

        //Dichiarazione Client Ethernet/IP
        eipClient = new EEIPClient();
        
        try {

        //	InetAddress inet = InetAddress.getByName(host);
 
        	//if (inet.isReachable(ping_timeout)) {

        		//Impostazione Host - Porta di Comunicazione = Standard Port for Ethernet/IP TCP-connections 0xAF12
        		eipClient.RegisterSession(host);

        		//////////////////////
        		// CONFIGURAZIONE  ///
        		//////////////////////
        		//Parameters for Originator -> Target communication
        		eipClient.setO_T_InstanceID(101);       //Output Assembly 65hex
        		eipClient.setO_T_Length(1);
        		eipClient.setO_T_RealTimeFormat(RealTimeFormat.Header32Bit);
        		eipClient.setO_T_ownerRedundant(false);
        		eipClient.setO_T_priority(Priority.Scheduled);
        		eipClient.setO_T_variableLength(false);
        		eipClient.setO_T_connectionType(ConnectionType.Point_to_Point);
        		//eipClient.setRequestedPacketRate_O_T(2000); // 2000 il valore standard è

        		//Parameters for Target -> Originator communication
        		eipClient.setT_O_InstanceID(100);       //Input Assembly 68hex
        		eipClient.setT_O_Length(8);
        		eipClient.setT_O_RealTimeFormat(RealTimeFormat.Modeless);
        		eipClient.setT_O_ownerRedundant(false);
        		eipClient.setT_O_priority(Priority.Scheduled);
        		eipClient.setT_O_variableLength(false);
        		eipClient.setT_O_connectionType(ConnectionType.Point_to_Point);
        		eipClient.O_T_IOData[0] = (byte) 0x64;
        		//eipClient.setRequestedPacketRate_T_O(2000); // 2000 il valore standard è
        		result = true;

        		//eipClient.ForwardOpen();
        		BilanceLogger.logger.log(Level.INFO, "Creazione Gestore Pesa TLB4 Eseguita Host: {0}", host);
        	//} else {
//
        		BilanceLogger.logger.log(Level.SEVERE, "Creazione Gestore Pesa TLB4 Fallita Host: {0} Indirizzo non raggiungibile!", host);
        	//}
        } catch (IOException ex) {

        	BilanceLogger.logger.log(Level.SEVERE, "Errore Creazione Gestore Pesa TLB4 Ethernet IP - ex {0}", ex);
        }

 
		return result;

	}

	public void chiudiEipClient() {

		//Impostazione Host - Porta di Comunicazione = Standard Port for Ethernet/IP TCP-connections 0xAF12
        try {
			eipClient.RegisterSession(host);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
    //Interfaccia di comunicazione con socket CloudFab
    public String sendToSocket(String msg) {
 
    	
        String result = "";
        String op;
          
        if (!ABILITA_SIMULAZIONE_PROCESSO) {
        	
        	//Compatibilità vecchie versioni software
        	if (msg.charAt(0) == '$') {

        		op = msg.substring(3, msg.length() - 2);
        	} else {

        		op = msg;
        	}

        	//////////////////////
        	// STRINGA USCITE  ///
        	//////////////////////
        	//Analizza Stringa uscite
        	String stato_uscite = "";
        	if (op.startsWith(TLB4_KWORD_COMANDO_USCITE)) {
        		stato_uscite = op.substring(TLB4_KWORD_COMANDO_USCITE.length(), op.length());
        		op = op.substring(0, TLB4_KWORD_COMANDO_USCITE.length());
        	}

        	////////////////
        	// SETPOINT  ///
        	////////////////
        	String idSetPoint = "";
        	String valSetPoint = "";
        	//Analizza Valore e id Setpoint
        	if (op.startsWith(TLB4_KWORD_IMPOSTAZIONE_SET_POINT)) {
        		idSetPoint = op.substring(TLB4_KWORD_IMPOSTAZIONE_SET_POINT.length() + 1, TLB4_KWORD_IMPOSTAZIONE_SET_POINT.length() + 2);
        		valSetPoint = op.substring(TLB4_KWORD_IMPOSTAZIONE_SET_POINT.length() + 2, op.length());
        		op = op.substring(0, TLB4_KWORD_IMPOSTAZIONE_SET_POINT.length());
        	}

        	////////////////
        	// SETPOINT  ///
        	//////////////// 
        	String valCalib = "";
        	//Analizza Valore e id Setpoint
        	if (op.startsWith(TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE)) {
        		valCalib = op.substring(TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE.length(), op.length());
        		op = op.substring(0, TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE.length());
        	}

        	switch (op) {

        	case TLB4_KWORD_IMPOSTAZIONE_LOW_RES:
        		////////////////////////////////
        		// IMPOSTAZIONE MODO LOW RES ///
        		////////////////////////////////

        		try {
        			//Scrittura Command Register
        			aggiornaCommandRegister(TLB4_MODO_4X_DIV_LOW_RES,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			result = TLB4_KWORD_IMPOSTAZIONE_LOW_RES;
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante l impostazione LOW RES Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_IMPOSTAZIONE_MODO_STANDARD:

        		/////////////////////////////////
        		// IMPOSTAZIONE MODO STANDARD ///
        		/////////////////////////////////

        		//eipClient.getAssemblyObject().setInstance(INSTANCE_WRITE, new byte[]{TLB4_MODO_STANDARD.byteValue()});
        		try {
        			//Scrittura Command Register
        			aggiornaCommandRegister(TLB4_MODO_STANDARD,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			result = TLB4_KWORD_IMPOSTAZIONE_MODO_STANDARD;
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante l impostazione modo standard Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_IMPOSTAZIONE_HIGH_RES:

        		try {
        			////////////////////////////
        			// IMPOSTAZIONE HIGH RES ///
        			////////////////////////////
        			//Scrittura Command Register
        			aggiornaCommandRegister(
        					TLB4_MODO_4X_DIV_HIGH_RES,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			result = TLB4_KWORD_IMPOSTAZIONE_HIGH_RES;
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante l impostazione HIGH RES Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_COMMUTAZIONE_PESO_LORDO:

        		//////////////////////////////
        		// COMMUTAZIONE PESO LORDO ///
        		//////////////////////////////
        		try {
        			//Scrittura Command Register
        			aggiornaCommandRegister(
        					TLB4_REG_DISATTIVA_TARA_SEMIAUTOMATICA_PESO_LORDO,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			result = TLB4_KWORD_COMMUTAZIONE_PESO_LORDO;
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la commutazione peso lordo Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_COMMUTAZIONE_PESO_NETTO:
        		//////////////////////////////
        		// COMMUTAZIONE PESO NETTO ///
        		//////////////////////////////

        		try {
        			//Scrittura Command Register
        			aggiornaCommandRegister(
        					TLB4_REG_ATTIVA_TARA_SEMIAUTOMATICA_PESO_NETTO,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la commutazione del peso netto Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		result = TLB4_KWORD_COMMUTAZIONE_PESO_NETTO;

        		break;

        	case TLB4_KWORD_LETTURA_PESO_NETTO:
        		/////////////////////////
        		// LETTURA PESO NETTO ///
        		/////////////////////////

        		try {

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        			int sNetto = 1;
        			if (EEIPClient.ToBool(rep[13], 0)) {
        				sNetto = -1;

        			}
        			result = Long.toString(sNetto * (EEIPClient.ToUint(new byte[]{rep[4], rep[5], rep[6], rep[7]})));

        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la lettura dei peso netto Bilancia ={0} -e ={1}", new Object[]{host, e});

        		}

        		break;
        	case TLB4_KWORD_LETTURA_PESO_LORDO:
        		/////////////////////////
        		// LETTURA PESO LORDO ///
        		/////////////////////////
        		try {
        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			int sLordo = 1;
        			if (EEIPClient.ToBool(rep[12], 7)) {
        				sLordo = -1;

        			}

        			result = Long.toString(sLordo * (EEIPClient.ToUint(new byte[]{rep[0], rep[1], rep[2], rep[3]})));
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la lettura dei peso Lordo Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_LETTURA_LETTURA_MODO_LOW_RES:
        		///////////////////////////////////
        		// LETTURA LETTURA MODO LOW RES ///
        		///////////////////////////////////

        		try {
        			//Scrittura Command Register
        			aggiornaCommandRegister(
        					TLB4_MODO_4X_DIV_LOW_RES,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			Integer cellaLow1 = EEIPClient.ToUshort(new byte[]{rep[0], rep[1]});
        			Integer cellaLow2 = EEIPClient.ToUshort(new byte[]{rep[2], rep[3]});
        			Integer cellaLow3 = EEIPClient.ToUshort(new byte[]{rep[4], rep[5]});
        			Integer cellaLow4 = EEIPClient.ToUshort(new byte[]{rep[6], rep[7]});

        			result = cellaLow1
        					+ TLB4_CARATTERE_SEPARAZIONE
        					+ cellaLow2
        					+ TLB4_CARATTERE_SEPARAZIONE
        					+ cellaLow3
        					+ TLB4_CARATTERE_SEPARAZIONE
        					+ cellaLow4;

        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la scrittura del command register Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_LETTURA_LETTURA_MODO_HIGH_RES:

        		///////////////////////////////////
        		// LETTURA LETTURA MODO HIGH RES ///
        		///////////////////////////////////
        		try {
        			//Scrittura command register
        			aggiornaCommandRegister(
        					TLB4_MODO_4X_DIV_HIGH_RES,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			Long cellaHigh1 = EEIPClient.ToUint(new byte[]{rep[0], rep[1], rep[2], rep[3]});
        			Long cellaHigh2 = EEIPClient.ToUint(new byte[]{rep[4], rep[5], rep[6], rep[7]});
        			Long cellaHigh3 = EEIPClient.ToUint(new byte[]{rep[8], rep[9], rep[10], rep[11]});
        			Long cellaHigh4 = EEIPClient.ToUint(new byte[]{rep[12], rep[13], rep[14], rep[15]});

        			result = cellaHigh1
        					+ TLB4_CARATTERE_SEPARAZIONE
        					+ cellaHigh2
        					+ TLB4_CARATTERE_SEPARAZIONE
        					+ cellaHigh3
        					+ TLB4_CARATTERE_SEPARAZIONE
        					+ cellaHigh4;
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la scrittura del command register Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_LETTURA_STATUS_REGISTER:

        		try {
        			///////////////////////////////
        			// LETTURA STATUS REGISTER  ///
        			///////////////////////////////
        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			for (int j = 0; j < 2; j++) {
        				for (int i = 0; i < 8; i++) {
        					if (EEIPClient.ToBool(rep[12 + j], i)) {
        						result += "1";
        					} else {
        						result += "0";
        					}
        				}

        			}

        			result = CodificaStatusRegister(result);
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la lettura dello status register Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_LETTURA_STATO_INGRESSI:

        		/////////////////////////////
        		// LETTURA STATO INGRESSI ///
        		/////////////////////////////
        		try {
        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			for (int i = 0; i < 2; i++) {
        				if (EEIPClient.ToBool(rep[14], i)) {
        					result += "1";
        				} else {
        					result += "0";
        				}
        			}
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la lettura dello stato ingressi Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}

        		break;

        	case TLB4_KWORD_COMANDO_USCITE:

        		//////////////////////
        		// COMANDO USCITE  ///
        		//////////////////////

        		try {
        			aggiornaCommandRegister(
        					null,
        					null,
        					stato_uscite,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			// LETTURA STATO USCITE AGGIORNATO 
        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);

        			for (int i = 0; i < 8; i++) {
        				if (EEIPClient.ToBool(rep[16], i)) {
        					result += "1";
        				} else {
        					result += "0";
        				}
        			}
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la scrittura del commando uscite Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_LETTURA_SET_POINTS:

        		//////////////////////////
        		// LETTURA SET POINTS  ///
        		//////////////////////////
        		try {
        			aggiornaCommandRegister(
        					TLB4_LETTURA_SETPOINT_1,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        			result += EEIPClient.ToUshort(new byte[]{rep[0x08], rep[0x09], rep[0x0A], rep[0x0B]}) + TLB4_CARATTERE_SEPARAZIONE;

        			aggiornaCommandRegister(
        					TLB4_LETTURA_SETPOINT_2,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        			result += EEIPClient.ToUshort(new byte[]{rep[0x08], rep[0x09], rep[0x0A], rep[0x0B]}) + TLB4_CARATTERE_SEPARAZIONE;

        			aggiornaCommandRegister(
        					TLB4_LETTURA_SETPOINT_3,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        			result += EEIPClient.ToUshort(new byte[]{rep[0x08], rep[0x09], rep[0x0A], rep[0x0B]});
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la lettura set point Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_IMPOSTAZIONE_SET_POINT:
        		//////////////////////////////
        		// IMPOSTAZIONE SET POINT  ///
        		//////////////////////////////
        		try {
        			if (null != idSetPoint) {
        				int v[];
        				switch (idSetPoint) {
        				case "1":
        					v = convertiValoreToHexByte32(valSetPoint);
        					aggiornaCommandRegister(
        							TLB4_SCRITTURA_SETPOINT_1,
        							null,
        							null,
        							null,
        							(byte) v[0],
        							(byte) v[1],
        							(byte) v[2],
        							(byte) v[3]);
        					break;
        				case "2":
        					v = convertiValoreToHexByte32(valSetPoint);
        					aggiornaCommandRegister(
        							TLB4_SCRITTURA_SETPOINT_2,
        							null,
        							null,
        							null,
        							(byte) v[0],
        							(byte) v[1],
        							(byte) v[2],
        							(byte) v[3]);
        					break;
        				case "3":
        					v = convertiValoreToHexByte32(valSetPoint);
        					aggiornaCommandRegister(
        							TLB4_SCRITTURA_SETPOINT_3,
        							null,
        							null,
        							null,
        							(byte) v[0],
        							(byte) v[1],
        							(byte) v[2],
        							(byte) v[3]);
        					break;
        				default:
        					break;
        				}
        			}
        			aggiornaCommandRegister(
        					TLB4_LETTURA_SETPOINT_3,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        			result += EEIPClient.ToUshort(new byte[]{rep[0x08], rep[0x09], rep[0x0A], rep[0x0B]});

        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la modifica dei setpoint Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_CALIBRAZIONE_AZZERAMENTO_DELLA_TARA:

        		/////////////////////////////////////////////
        		// CALIBRAZIONE - AZZERAMENTO DELLA TARA  ///
        		/////////////////////////////////////////////
        		try {
        			aggiornaCommandRegister(
        					TLB4_AZZERAMENTO_TARA_CALIBRAZIONE,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			result = TLB4_KWORD_CALIBRAZIONE_AZZERAMENTO_DELLA_TARA;
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante l azzeramento della tara Bilancia ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;

        	case TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE:

        		//////////////////////////////////////////////
        		// CALIBRAZIONE - SCRITTURA PESO CAMPIONE  ///
        		//////////////////////////////////////////////
        		//Conversione valore peso campione in 4 byte
        		try {
        			int c[] = convertiValoreToHexByte32(valCalib);

        			//Scrittura valore su registro di scambio
        			aggiornaCommandRegister(
        					TLB4_SCRITTURA_PESO_CAMPIONE,
        					null,
        					null,
        					null,
        					(byte) c[0],
        					(byte) c[1],
        					(byte) c[2],
        					(byte) c[3]);

        			//Memorizzazione peso campione
        			aggiornaCommandRegister(
        					TLB4_SALVA_PESO_CAMPIONE_CALIBRAZIONE,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			//Lettura peso Campione (0 - se la scrittura è andata a buon fine)
        			aggiornaCommandRegister(
        					TLB4_LETTURA_PESO_CAMPIONE,
        					null,
        					null,
        					null,
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue(),
        					(byte) TLB4_REG_NESSUN_COMANDO.byteValue());

        			rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        			result = Long.toString(EEIPClient.ToUshort(new byte[]{rep[0x08], rep[0x09], rep[0x0A], rep[0x0B]}));
        		} catch (Exception e) {

        			BilanceLogger.logger.log(Level.SEVERE, "Errore durante la scrittura del peso campione ={0} -e ={1}", new Object[]{host, e});
        		}
        		break;
        	default:
        		break;
        	}
        }
         
        return result;
    }

    // COMMAND REGISTER 
    //
    //  -------------------------------------------------------------------------------------
    //  |Dati In Ingresso allo strumento (Scrittura)        |Indirizzi – output assembly    |
    //  -------------------------------------------------------------------------------------
    //  |   Command Register [2 byte]                       |   0x0000-0x0001               |
    //  |   Comando delle Uscite Digitali [2 byte]          |   0x0002-0x0003               |
    //  |   Registro di Scambio [4 byte]                    |   0x0004-0x0007               |
    //  |                                                   |                               |
    //  -------------------------------------------------------------------------------------
    //Lo strumento dispone di due Registri di Scambio (uno in lettura ed uno in scrittura) 
    //da usare insieme al Command Register per accedere a questi valori. Le procedure da seguire sono le seguenti:
    //  - LETTURA: inviare al Command Register il comando di lettura del dato desiderato (es.: 90
    //      per “Lettura setpoint 1”) e leggere il contenuto del Registro di Scambio.
    //  - SCRITTURA: scrivere il valore che si desidera impostare nel Registro di Scambio ed inviare al
    //      Command Register il comando di scrittura nel dato desiderato (es.: 93 per “Scrittura setpoint 1”).
    //  Se è necessario eseguire lo stesso comando due volte di seguito, inviare il comando 0 tra un comando e l’altro.
    //Scrittura Command Register
    public void aggiornaCommandRegister(
            Integer Cmd1,
            Integer Cmd2,
            String Cmd3,
            Integer Cmd4,
            byte Cmd5,
            byte Cmd6,
            byte Cmd7,
            byte Cmd8) throws IOException, CIPException {

        //Lettura stato attuale uscite nell'array uscite
        rep = eipClient.getAssemblyObject().getInstance(TLB4_INSTANCE_READ);
        String[] output = new String[3];

        for (int i = 0; i < output.length; i++) {
            if (EEIPClient.ToBool(rep[16], i)) {
                output[i] = "1";
            } else {
                output[i] = "0";
            }
        }

        if (Cmd3 != null) {
            //Modifica array in funzione della stringa stato_uscite
            for (int i = 0; i < Cmd3.length(); i++) {
                int id = TLB4_CHARSET_USCITE_TLB4.indexOf(Cmd3.charAt(i));
                if (id < 3) {
                    output[id] = "1";
                } else {
                    output[id - 3] = "0";
                }
            }
        }

        //Copia array nella stringa uscite
        String strCmd3 = "";

        for (int i = output.length - 1; i >= 0; i--) {
            strCmd3 += output[i];

        }

        byte strCmd1 = TLB4_REG_NESSUN_COMANDO.byteValue();
        byte strCmd2 = TLB4_REG_NESSUN_COMANDO.byteValue();
        byte strCmd4 = TLB4_REG_NESSUN_COMANDO.byteValue();

        if (Cmd1 != null) {
            strCmd1 = Cmd1.byteValue();

        }
        if (Cmd2 != null) {
            strCmd2 = Cmd2.byteValue();

        }
        if (Cmd4 != null) {
            strCmd4 = Cmd4.byteValue();

        }

        //Scrittura Command Register
        byte[] out3 = new byte[]{
            strCmd1, //COMMAND REGISTER NESSUN COMANDO  byte 1
            strCmd2, //COMMAND REGISTER NESSUN COMANDO  byte 2
            ((Integer) Integer.parseInt(strCmd3, 2)).byteValue(), //COMMAND REGISTER USCITE DIGITALI byte 1
            strCmd4, //COMMAND REGISTER USCITE DIGITALI byte 2
            Cmd5, //REGISTRO DI SCAMBIO byte1
            Cmd6, //REGISTRO DI SCAMBIO byte2
            Cmd7, //REGISTRO DI SCAMBIO byte3
            Cmd8};//REGISTRO DI SCAMBIO byte4

        eipClient.getAssemblyObject().setInstance(TLB4_INSTANCE_WRITE, out3);

        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException ex) {
        }

        //Scrittura Command Register - Nessun Comando
        out3 = new byte[]{
            TLB4_REG_NESSUN_COMANDO.byteValue(),
            0,
            ((Integer) Integer.parseInt(strCmd3, 2)).byteValue()}; //COMMAND REGISTER NESSUN COMANDO  byte 2

        eipClient.getAssemblyObject().setInstance(TLB4_INSTANCE_WRITE, out3);

    }

    //Convertitore Stringa di interi in 4Byte
    private int[] convertiValoreToHexByte32(String val) {

        int[] result = new int[4];
        //Conversione da Stringa numero binario
        String valStr = Integer.toBinaryString(Integer.parseInt(val));

        //Adeguamento lunghezza stringa binario a divisone in 4 byte
        while (valStr.length() < 32) {
            valStr = "0" + valStr;
        }

        //Divisione strina in 4 String per conversione in byte
        String s1 = valStr.substring(0, 8);
        String s2 = valStr.substring(8, 16);
        String s3 = valStr.substring(16, 24);
        String s4 = valStr.substring(24, 32);

        //Conversione di esadecimale 
        String reg1 = Integer.toString(Integer.parseInt(s1, 2), 16);
        String reg2 = Integer.toString(Integer.parseInt(s2, 2), 16);
        String reg3 = Integer.toString(Integer.parseInt(s3, 2), 16);
        String reg4 = Integer.toString(Integer.parseInt(s4, 2), 16);

        //Assegnazione array di interi (Ordine invertito per necessita di protocollo nel registro di scambio)
        result[0] = Integer.decode("0x" + reg4);
        result[1] = Integer.decode("0x" + reg3);
        result[2] = Integer.decode("0x" + reg2);
        result[3] = Integer.decode("0x" + reg1);

        return result;
    }
     
    public void close() {
    	try {
			eipClient.UnRegisterSession();
		} catch (Exception e) { 
		}
    	
    	
    }

}
