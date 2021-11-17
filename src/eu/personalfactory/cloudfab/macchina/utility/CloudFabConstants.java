package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.gestore.password.GestorePassword;

import java.io.IOException;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CloudFabConstants {

    public static final String VERSIONE_CORRENTE = "ver. 5.1.5";
    
    ///////////////////
    // CLOUDFAB5.0  ///
    ///////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Controlli inizialie
    public static boolean IGNORA_CONTROLLI_INIZIALI = true;
    public static boolean ABILITA_SIMULAZIONE_PROCESSO = true;
    public static String CODICE_COMPONENTE_ALTERNATIVO = "ALTER";
    public static final int DELAY_ATTIVAZIONE_USCITE_GESTORE_MODIFICA_OUT_EV = 10; 
    public static final int FREQUENZA_THREAD_CONTROLLO_INTERRUTTORI_MANUALI = 200;
    public static final int DELAY_RESET_INVERTER = 500;
    public static final int DELAY_SEND_RECEIVE_UDP = 10; 
    public static final int DELAY_SEND_MDUINO = 5;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Import dati movimenti magazzino da file (4.0)
    public static final String CSV_PATH_FILE_EXT =".csv";
    public static final String CSV_PATH_IMPORT = "./data_share/import/";
    public static final String CSV_PATH_IMPORT_DEST = "./data_share/import/temp/"; 
    public static final String CSV_PATH_EXPORT = "./data_share/export/";
    public static final String CSV_PATH_EXPORT_PROD ="prod_";
    public static final String CSV_PATH_EXPORT_MOV ="mod_"; 
    public static final String CSV_PATH_EXPORT_DATE_FORMAT ="dd_MM_yy_HH_mm"; 
    public static final String CSV_PATH_DB_EXPORT_DATE_FORMAT ="yyyy/dd/MM";
    public static final String CSV_CHAR_SEP = ";";
    public static final String CSV_DATE_FORMAT = "dd/MM/yy"; //";
    public static final String FTP_DOWNLOAD_DIR_PATH = "/share/toOrigami"; 
    public static final String FTP_UPLOAD_DIR_PATH = "/share/fromOrigami";	
    public static final String CSV_KEY_ID_MOV_ORI ="id_movimento_ori";	
    public static final String CSV_KEY_INEPHOS = "id_movimento_inephos";
    public static final String CSV_KEY_ID_MATERIALE = "id_materiale";
    public static final String CSV_KEY_TIPO_MATERIALE = "tipo_materiale";
	public static final String CSV_KEY_QUANTITA = "quantita";
	public static final String CSV_KEY_COD_INGRESSO = "cod_ingresso_comp";
	public static final String CSV_KEY_COD_OPERATORE = "cod_operatore";
	public static final String CSV_KEY_OPERAZIONE = "operazione";
	public static final String CSV_KEY_PROCEDURA_ADOTTATA = "procedura_adottata";
	public static final String CSV_KEY_TIPO_MOV = "tipo_mov";	
	public static final String CSV_KEY_DESCRI_MOVE = "descri_mov";
	public static final String CSV_KEY_DT_MOV = "dt_mov";
	public static final String CSV_KEY_SILO = "silo";
	public static final String CSV_KEY_PESO_TEORICO = "peso_teorico";
	public static final String CSV_KEY_ID_CICLO = "id_ciclo";
	public static final String CSV_KEY_DT_INIZIO_PROCEDURA = "dt_inizio_procedura";
	public static final String CSV_KEY_DT_FINE_PROCEDURA = "dt_fine_procedura";
	public static final String CSV_KEY_ABILITATO = "abilitato";
	public static final String CSV_KEY_ORIGINE_MOV = "origine_mov";
	public static final String CSV_KEY_INFO_1 = "info1";
	public static final String CSV_KEY_INFO_2 = "info2";
	public static final String CSV_KEY_INFO_3 = "info3";
	public static final String CSV_KEY_INFO_4 = "info4";
	public static final String CSV_KEY_INFO_5 = "info5";
	public static final String CSV_KEY_INFO_6 = "info6";
	public static final String CSV_KEY_INFO_7 = "info7";
	public static final String CSV_KEY_INFO_8 = "info8";
	public static final String CSV_KEY_INFO_9 = "info9";
	public static final String CSV_KEY_INFO_10 = "info10";
	 
	
	public static final String MOV_IN_TIPO_MATERIALE = "RAW MATERIAL"; 			//TIPO DI MATERIALE DESCRITTO NEL MOVIMENTO (MATERIA PRIMA, KIT CHIMICO) 
	public static final String MOV_IN_OPERAZIONE = "1"; 						//SEGNO DELL'OPERAZIONE (1 CARICO -1 SCARICO)
	public static final String MOV_IN_PROCEDURA_ADOTTATA ="DELIVERY NOTE"; 		//TIPO DI PROCEDURA (SILO LOADING, SILO UNLOADING, KIT MANUAL LOADING)
	public static final String MOV_IN_TIPO_MOV= "WAREHOUSE IN";  				//TIPOLOGIA DI MOVIMENTO (PROCESSING IN - PROCESSING OUT)
	public static final String MOV_IN_DESCRI_MOV = "LOADING FOR PURCHASE"; 		//DESCRIZIONE TIPO DI MOVIMENTO (LOADING FOR PROCESSING, UNLOADING FOR PRODUCTION)			
	public static final Integer MOV_IN_PESO_TEORICO = 0; 						//PESO TEORICO (MOVIMENTI DI SCARICO PER PRODUZIONE SULLA MACCHINA)					
	public static final Integer MOV_IN_ID_CICLO=0;   							//RIFERIMENTO AL CICLO DI PRODUZIONE CHE HA UTILIZZATO LA MATERIA PRIMA DESCRITTA NEL MOVIMENTO  (MOVIMENTI DI SCARICO PER PRODUZIONE SULLA MACCHINA)					
	public static final String MOV_IN_ORIGINE_MOV="GESTIONALE_PRIVATO"; 		//PROVENIENZA DEL MOVIMENTO (NOSTRO CLOUD - MACCHINA - VOSTRO GESTIONALE) IN MODO DA DIFFERENZIARE L'ORIGINE DEL MOVIMENTO 					
	
    
    //VISIBILITA' INIZIALE PANNELLI GESTIONE DIRETTA
    public static final Boolean VIS_PANNELLI_DIR = false;
    
    //ABILITA SCAMIO RETI
    public static final Boolean ABILITA_SWITCH_RETE = true;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final int POS_0DEF = 0;
    public static final int POS_20 = 1;
    public static final int POS_53 = 2;
    public static final int POS_100 = 3;
    public static final int POS_0_COMANDO_UNICO = 4;
    public static final int POS_100_COMANDO_UNICO = 5;
    public static final int PULIZIA_LIMITE_POS53 = 10000;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final int TEMPO_INTERVALLI_APERTURA_VERIFICA_VALVOLA = 160; 
    public static final int FREQUENZA_LETTURA_PESO_BILANCIA_CONFEZIONI = 40; 
    public static final int FREQUENZA_LETTURA_PESO_BILANCIA_CARICO = 40;
    public static final int FREQUENZA_LETTURA_PESO_BILANCIA_MANUALE = 200;
    public static final int FREQUENZA_SERVER_MDUINO = 10; //300
    public static final int NUMERO_RIP_ESEGUI_COMANDO_RS485 = 2;
    public static final int FREQUENZA_ESEGUI_COMANDO_RS485 = 40;
    
    public static final int TEMPO_STABILIAZZAZIONE_BILANCIA_CARICO_DOPO_APERTURA = 800; 
    public static final int TEMPO_RITARDO_ATTIVAZIONE_INVERTER = 300;
    public static final int TEMPO_VOLO_MATERIALE= 2000;
    public static final int TEMPO_APERTURA_VALOLA_SCARICO_MATERIE_PRIME= 5000;
    public static final int TEMPO_CHIUSURA_VALOLA_SCARICO_MATERIE_PRIME= 1500;
    public static final int TEMPO_STABILIAZZIONE_PESO_FINE_PESA_CONFEZIONI = 1500; 
    //public static final int TEMPO_LAMPEGGIAMENTO_NASTRO = 2000;

    public static final int RIPETIZIONI_AZZERAMENTO = 3;

    
    public static final String LOADING_IMG_TEXT_TAB = "           ";
    public static final String LOADING_IMG_TEXT_00 = "Ok";
    public static final String LOADING_IMG_TEXT_01 = "Failed";
    public static final String LOADING_IMG_TEXT_02 = "Not Enabled";
    public static final String LOADING_IMG_TEXT_03 = "Database connection";
    public static final String LOADING_IMG_TEXT_04 = "Parameters";
    public static final String LOADING_IMG_TEXT_05 = "Software Configuration";
    public static final String LOADING_IMG_TEXT_06 = "Log Files";
    public static final String LOADING_IMG_TEXT_07 = "Ip Configuration";
    public static final String LOADING_IMG_TEXT_08 = "Internet Connection";
    public static final String LOADING_IMG_TEXT_09 = "Release Download";
    public static final String LOADING_IMG_TEXT_10 = "Download";
    public static final String LOADING_IMG_TEXT_11 = "Copied";
    public static final String LOADING_IMG_TEXT_12 = "No Files";
    public static final String LOADING_IMG_TEXT_13 = "Restore from USB";
    public static final String LOADING_IMG_TEXT_14 = "Restore from File";
    public static final String LOADING_IMG_TEXT_15 = "Done";
    public static final String LOADING_IMG_TEXT_16 = "No files";
    public static final String LOADING_IMG_TEXT_17 = "USB Backup";
    public static final String LOADING_IMG_TEXT_18 = "ERROR ON DATABASE CONNECTION - EX:";
    public static final String LOADING_IMG_TEXT_19 = "NEW USER CREATION...";

    public static final String LOADING_IMG_TEXT_20 = "Loading Scale Socket";
    public static final String LOADING_IMG_TEXT_21 = "Packaging Scale Socket";
    public static final String LOADING_IMG_TEXT_22 = "Chemicals Scale Socket";
    public static final String LOADING_IMG_TEXT_23 = "Loading Aux Scale Socket";

    public static final String LOADING_IMG_TEXT_24 = "Mduino Main Panel";
    public static final String LOADING_IMG_TEXT_25 = "Mduino Silos Panel";
    public static final String LOADING_IMG_TEXT_26 = "Mduino Silos Panel Aux";
    public static final String LOADING_IMG_TEXT_27 = "Pneumatic System Pressure";
    public static final String LOADING_IMG_TEXT_28 = "bar";
    public static final String LOADING_IMG_TEXT_29 = "Inverter Mixer Com";
    public static final String LOADING_IMG_TEXT_30 = "Inverter Screws Com";
    public static final String LOADING_IMG_TEXT_31 = "Close";

    public static final String LOADING_IMG_TEXT_32 = "EB80 MainPanel";
    public static final String LOADING_IMG_TEXT_33 = "EB80 SiloPanel";
    public static final String LOADING_IMG_TEXT_34 = "EB80 SiloPanelAux";

    public static final String DEVICE_PANEL_TEXT_00 = "USCITE DIGITALI";
    public static final String DEVICE_PANEL_TEXT_01 = "INGRESSI DIGITALI";

    public static final String SESSION_LOGGER_DATE_FORMAT = "-dd_MM_yyyy_HH-mm-ss";
    public static final String SESSION_LOGGER_52_DIR_LOG = "log";
    public static final String SESSION_LOGGER_53_DIR_WORK = "dist";
    public static final String SESSION_LOGGER_101_NOME_DIR_TEMP_LOGGER = "_temp";
    public static final String SESSION_LOGGER_88_NOME_FILE_LOGGER = "session";
    public static final String SESSION_LOGGER_59_LOG_EXTENSION = ".log";
    public static final String SESSION_LOGGER_LEVEL = "INFO";

    public static final String LOADING_IMG_PATH = "eu/personalfactory/cloudfab/macchina/images/standard/loading.png";

    public static int INTERVALLO_CONTROLLO_SCAMBIO_LINEE_ASPIRATORE = 2000;
    public static int NUMERO_MAX_USCITE_MDUINO = 28;
    public static int NUMERO_MAX_USCITE_EB80 = 32;
    public static int INGRESSO_ANALOGICO_PRESSOSTATO_MDUINO_MAINPANEL = 2;
    public static int DEVICE_INGRESSO_PRESSOSTATO_MDUINO_MAINPANEL = 0;

    //EB80 
    public static int MODULO_VALVOLE_OUT_KEY = 0x64;
    public static int MODULO_VALVOLE_IN_KEY = 0x65;
    public static int MODULO_VALVOLE_DELAY = 40;

    public static char CHAR_SEP_ID_BILANCIA = '_';
    public static int ID_BILANCIA_CARICO = 0;
    public static int ID_BILANCIA_CONFEZIONI = 1;
    public static int ID_BILANCIA_MANUALE = 2;
    public static int ID_BILANCIA_CARICO_AUX = 3;
    
    //TLB4 
    public static String TLB4_INDIRIZZO_DEFAULT = "01";
    public static final String TLB4_CHARSET_USCITE_TLB4 = "ABCabc";
    public static final String TLB4_CARATTERE_SEPARAZIONE = "_";
    public static final String TLB4_KWORD_CARATTERE_INIZIO_STRINGA = "$"; 
    public static final String TLB4_KWORD_IMPOSTAZIONE_LOW_RES = "LOW";
    public static final String TLB4_KWORD_IMPOSTAZIONE_MODO_STANDARD = "STD";
    public static final String TLB4_KWORD_IMPOSTAZIONE_HIGH_RES = "HIGH";
    public static final String TLB4_KWORD_COMMUTAZIONE_PESO_LORDO = "GROSS";
    public static final String TLB4_KWORD_COMMUTAZIONE_PESO_NETTO = "NET";
    public static final String TLB4_KWORD_LETTURA_PESO_NETTO = "n";
    public static final String TLB4_KWORD_LETTURA_PESO_LORDO = "t";
    public static final String TLB4_KWORD_LETTURA_LETTURA_MODO_LOW_RES = "l";
    public static final String TLB4_KWORD_LETTURA_LETTURA_MODO_HIGH_RES = "h";
    public static final String TLB4_KWORD_LETTURA_STATUS_REGISTER = "STREG";
    public static final String TLB4_KWORD_LETTURA_STATO_INGRESSI = "IN";
    public static final String TLB4_KWORD_COMANDO_USCITE = "O";
    public static final String TLB4_KWORD_LETTURA_SET_POINTS = "LSP";
    public static final String TLB4_KWORD_IMPOSTAZIONE_SET_POINT = "s";
    public static final String TLB4_KWORD_CALIBRAZIONE_AZZERAMENTO_DELLA_TARA = "CAL_AZTAR";
    public static final String TLB4_KWORD_CALIBRAZIONE_REGISTRAZIONE_PESO_CAMPIONE = "CAL_P";
    public static final int ADDRESS_PING_TIMEOUT = 2000;  //2000
    public static final int NUM_BILANCE_UTILIZZABILI = 4;
    
 
    //MDUINO
    public static final Integer MDUINO_DIM_PACCHETTI = 32;
    public static final String MDUINO_CHAR_FINALE = "£";
    public static final String MDUINO_CHAR_INIZIALE = "$";
    public static final String MDUINO_CHAR_INPUT_ANALOGICO = "%";
    public static final String MDUINO_CHAR_RS485 = "@";
    public static final Integer MDUINO_NUM_RIP_MAX = 2;
    public static final String OUTPUT_TRUE_CHAR = "1";
    public static final String OUTPUT_FALSE_CHAR = "0";
    public static final String OUTPUT_SEP_CHAR = ".";
      
    
    //VELOCITA' INIZIALE INVERTER
    public static final String DEFAULT_VEL_INV = "5000";
    
    public static final String[] NOME_USCITE = {
        "MOTORE_MISCELATORE_RUN",
        "MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER",
        "MOTORE_VIBRO_VALVOLA_SCARICO",
        "RETROAZIONE_VERIFICA_MDUINO_MAIN_PANEL",
        "RESET_SILOS_PANEL",
        "RESET_MOVAHOP",
        "RESET_BILANCIA_OMB",
        "SEGNALE_LUMINOSO_GIALLO",
        "SEGNALE_LUMINOSO_ROSSO",
        "MOTORE_CHOPPER",
        "NASTRO",
        "CONTATTORE_ATTIVA_ASPIRATORE",
        "SCUOTITORI_BILANCIA_SACCHETTI",
        "CONTATTORE_COCLEA_A",
        "CONTATTORE_COCLEA_B",
        "CONTATTORE_COCLEA_C",
        "CONTATTORE_COCLEA_D",
        "CONTATTORE_COCLEA_E",
        "CONTATTORE_COCLEA_F",
        "CONTATTORE_COCLEA_G",
        "CONTATTORE_COCLEA_H",
        "CONTATTORE_COCLEA_I",
        "CONTATTORE_COCLEA_J",
        "CONTATTORE_COCLEA_K",
        "CONTATTORE_COCLEA_L",
        "BLOCCO_PORTA_COCLEA_A",
        "BLOCCO_PORTA_COCLEA_B",
        "BLOCCO_PORTA_COCLEA_C",
        "BLOCCO_PORTA_COCLEA_D",
        "BLOCCO_PORTA_COCLEA_E",
        "BLOCCO_PORTA_COCLEA_F",
        "BLOCCO_PORTA_COCLEA_G",
        "BLOCCO_PORTA_COCLEA_H",
        "BLOCCO_PORTA_COCLEA_I",
        "BLOCCO_PORTA_COCLEA_J",
        "BLOCCO_PORTA_COCLEA_K",
        "BLOCCO_PORTA_COCLEA_L",
        "SEGNALE_LUMINOSO_COCLEA_A",
        "SEGNALE_LUMINOSO_COCLEA_B",
        "SEGNALE_LUMINOSO_COCLEA_C",
        "SEGNALE_LUMINOSO_COCLEA_D",
        "SEGNALE_LUMINOSO_COCLEA_E",
        "SEGNALE_LUMINOSO_COCLEA_F",
        "SEGNALE_LUMINOSO_COCLEA_G",
        "SEGNALE_LUMINOSO_COCLEA_H",
        "SEGNALE_LUMINOSO_COCLEA_I",
        "SEGNALE_LUMINOSO_COCLEA_J",
        "SEGNALE_LUMINOSO_COCLEA_K ",
        "SEGNALE_LUMINOSO_COCLEA_L",
        "COCLEE_SPLIT_LINEA_DIRETTA_INVERTER",
        "RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL",
        "RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL_AUX",
        "EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO",
        "EV_ASPIRATORE_LINEA_MISCELATORE",
        "EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI",
        "EV_ASPIRATORE_LINEA_MANUALE",
        "EV_BILANCIA_DI_CARICO",
        "EV_RIBALTA_SACCO",
        "EV_BLOCCA_SACCO",
        "EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO",
        "EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO",
        "EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA",
        "EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_OGIVA",
        "EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA",
        "EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI",
        "EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO",
        "USCITA_LOGICA_EV_VALVOLA_SCARICO_COMANDO_UNICO",
        "EV_VALVOLA_SCARICO_1",
        "EV_VALVOLA_SCARICO_2",
        "EV_VALVOLA_SCARICO_3",
        "EV_VALVOLA_SCARICO_4",
        "EV_BILANCIA_DI_CARICO_AUSILIARIA",
        "EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO_AUSILIARIA",
        "EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO_AUSILIARIA",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_A",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_B",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_B",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_B",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_C",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_C",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_C",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_D",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_D",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_D",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_E",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_E",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_E",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_F",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_F",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_F",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_G",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_G",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_G",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_H",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_H",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_H",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_I",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_I",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_I",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_J",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_J",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_J",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_K",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_K",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_K",
        "EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_L",
        "EV_SERIE_VIBRATORI_FUNGHI_SILO_L",
        "EV_SERIE_VIBRATORI_PNEUMATICI_SILO_L"};
 
    public static final String[] NOME_INGRESSI = {
        "CONTATTO SPORTELLO MISCELATORE",
        "CONTATTO VALVOLA CARICO",
        "CONTATTO VALVOLA SCARICO",
        "SERIE PULSANTI BLOCCA SACCO",
        "LOGICO PULSANTE STOP",
        "LOGICO SELETTORE MANUALE VIBRO",
        "SELETTORE VALVOLA SCARICO",
        "CONTATTO RIBALTA SACCO",
        "CONTATTO BLOCCA SACCO",
        "FINECORSA NASTRO",
        "ABILITAZIONE NASTRO",
        "RETROAZIONE MDUINO MAIN PANEL",
        "CONTATTO COCLEA A",
        "CONTATTO COCLEA B",
        "CONTATTO COCLEA C",
        "CONTATTO COCLEA D",
        "CONTATTO COCLEA E",
        "CONTATTO COCLEA F",
        "CONTATTO COCLEA G",
        "CONTATTO COCLEA H",
        "CONTATTO COCLEA I",
        "CONTATTO COCLEA J",
        "CONTATTO COCLEA K ",
        "CONTATTO COCLEA L",
        "SELETTORE DISABILITA COCLEA A",
        "SELETTORE DISABILITA COCLEA B",
        "SELETTORE DISABILITA COCLEA C",
        "SELETTORE DISABILITA COCLEA D",
        "SELETTORE DISABILITA COCLEA E",
        "SELETTORE DISABILITA COCLEA F",
        "SELETTORE DISABILITA COCLEA G",
        "SELETTORE DISABILITA COCLEA H",
        "SELETTORE DISABILITA COCLEA I",
        "SELETTORE DISABILITA COCLEA J ",
        "SELETTORE DISABILITA COCLEA K",
        "SELETTORE DISABILITA COCLEA L",
        "RETROAZIONE MDUINO SILOS PANEL",
        "RETROAZIONE MDUINO SILOS PANEL AUX"};

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// VARIABILI
    public static EntityManagerFactory ENTITY_MANAGER_FACTORY;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// COSTANTI DEL PROGRAMMA  

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Risoluzione
    public static int RISOLUZIONE_LARGHEZZA_PANNELLO = 1024;
    public static int RISOLUZIONE_ALTEZZA_PANNELLO = 768;
    public static int RISOLUZIONE_RIFERIMENTO_LARGHEZZA = 1024;
    public static int RISOLUZIONE_RIFERIMENTO_ALTEZZA = 768;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Font del Testo
    public static String FONT = "Trebuchet_MS_Bold.ttf";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Connessione al database
    public static String DATABASE_HOSTNAME = "localhost";
    public static int DATABASE_PORT = 3306;
    public static String DATABASE_NAME = "origamidb";
    public static String DATABASE_PERSISTENCE_UNIT = "FabCloudPU";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Percorsi 
    public static String PATH_FONT = "eu/personalfactory/cloudfab/macchina/fonts/";
    public static String PATH_PANELS_DATA = "eu/personalfactory/cloudfab/macchina/panels/data/";
    public static String PATH_IMAGES = "eu/personalfactory/cloudfab/macchina/images";
    public static String PATH_IMG_TASTIERA = "/tastiera/";
    public static String PATH_IMG_SFONDI = "/sfondi/";
    public static String PATH_IMG_PULSANTI = "/pulsanti/";
    public static String PATH_BROWSER;
    public static String PATH_VIDEO_CHAT;
    public static String PATH_FILE_DEFAULT_MESSAGGI_MACCHINA = "eu/personalfactory/cloudfab/macchina/resource/messaggi";
    public static String PATH_FILE_DEFAULT_PARAMETRI_SINGOLA_MACCHINA = "eu/personalfactory/cloudfab/macchina/resource/parametri_macchina";
    public static String PATH_FILE_DEFAULT_PARAMETRI_GLOBALI = "eu/personalfactory/cloudfab/macchina/resource/parametri_globali";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Chiavi File Properties
    public static String KEY_PROPERTIES_MESSAGGI_MACCHINA = "default.messaggi.macchina.";
    public static String KEY_PROPERTIES_PARAMETRO = "default.parametro.";
    public static String KEY_PROPERTIES_NUMERO_PARAMETRI = "numero.parametri";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Mappatura File Xml
    public static String XML_NAME = "name";
    public static String XML_NUM = "num";
    public static String XML_IMAGE = "labImage";
    public static String XML_IMAGE_UNSEL = "img";
    public static String XML_IMAGE_SEL = "imgS";
    public static String XML_IMAGE_SFONDO = "sfondoImg";
    public static String XML_IMAGE_LOADING = "loadingImg";
    public static String XML_IMAGE_AUX = "labImageAux";
    public static String XML_CORD_X = "x";
    public static String XML_CORD_Y = "y";
    public static String XML_DIMENSION_LARG = "l";
    public static String XML_DIMENSION_ALT = "a";
    public static String XML_FONT = "f";
    public static String XML_LABEL_PLUS = "labPlus";
    public static String XML_LABEL_SIMPLE = "labSimple";
    public static String XML_LABEL_HELP = "labHelp";
    public static String XML_LABEL_TITLE = "labTitle";
    public static String XML_LABEL_PROGRESS = "labProg";
    public static String XML_LABEL_AUX = "labAux";
    public static String XML_TEXT_FIELD = "textField";
    public static String XML_AREA_SCAMBIO = "AreaScambio";
    public static String XML_BUTTON_INFO = "butInfo";
    public static String XML_BUTTON_FRECCIA = "butFreccia";
    public static String XML_TASTO = "tasto";
    public static String XML_BUTTON = "But";
    public static String XML_LABEL_LARG_HELP_MESSAGE = "larghHelp";
    public static String XML_PROGRESS_BAR = "progBar";
    public static String XML_COLORE = "colore";
    public static String XML_COLORE_RED = "red";
    public static String XML_COLORE_GREEN = "green";
    public static String XML_COLORE_BLUE = "blue";
    public static String XML_PANEL_REDUCED = "panelReduced";
    public static String XML_OPACITY = "opacity";
    public static String XML_OPACITY_VALUE = "value";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Mappatura File Html
    public static String HTML_STRINGA_INIZIO = "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
    public static String HTML_STRINGA_FINE = "</html>";
    public static String HTML_BREAK_LINE = "<br>";
    public static String HTML_INTERLINEA = "<br/><font size=1><br></font>";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Id Dizionari
    public static int ID_DIZIONARIO_PRODOTTI = 1;
    public static int ID_DIZIONARIO_COLORI_BASE = 2;
    public static int ID_DIZIONARIO_MESSAGGI_MACCHINA = 3;
    public static int ID_DIZIONARIO_COMPONENTI = 4;
    public static int ID_DIZIONARIO_FAMIGLIE = 5;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Ingressi - Uscite Logiche

    /////////////////////////////////////////////////////////////////////////////////////////////////// USCITE MAIN PANEL 
    public static String USCITA_LOGICA_MOTORE_MISCELATORE_RUN = "0";
    public static String USCITA_LOGICA_MOTORE_MISCELATORE_SPLIT_LINEA_DIRETTA_INVERTER = "1";
    public static String USCITA_LOGICA_MOTORE_VIBRO_VALVOLA_SCARICO = "2";
    public static String USCITA_RETROAZIONE_VERIFICA_MDUINO_MAIN_PANEL = "3";
    public static String USCITA_LOGICA_RESET_SILOS_PANEL = "4";
    public static String USCITA_LOGICA_RESET_QUADRO_PIANO = "5";
    public static String USCITA_LOGICA_RESET_BILANCIA_OMB = "6";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_GIALLO = "7";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_ROSSO = "8";
    public static String USCITA_LOGICA_MOTORE_CHOPPER = "9";
    public static String USCITA_LOGICA_NASTRO = "10";
    public static String USCITA_LOGICA_CONTATTORE_ATTIVA_ASPIRATORE = "11";
    public static String USCITA_LOGICA_SCUOTITORI_BILANCIA_SACCHETTI = "12";
    //////////////////////////////////////////////////////////////////////////////////////////////////// USCITE SILOS PANEL / AUX
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_A = "13";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_B = "14";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_C = "15";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_D = "16";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_E = "17";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_F = "18";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_G = "19";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_H = "20";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_I = "21";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_J = "22";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_K = "23";
    public static String USCITA_LOGICA_CONTATTORE_COCLEA_L = "24";

    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_A = "25";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_B = "26";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_C = "27";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_D = "28";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_E = "29";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_F = "30";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_G = "31";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_H = "32";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_I = "33";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_J = "34";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_K = "35";
    public static String USCITA_LOGICA_BLOCCO_PORTA_COCLEA_L = "36";

    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_A = "37";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_B = "38";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_C = "39";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_D = "40";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_E = "41";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_F = "42";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_G = "43";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_H = "44";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_I = "45";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_J = "46";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_K = "47";
    public static String USCITA_LOGICA_SEGNALE_LUMINOSO_COCLEA_L = "48";
    public static String USCITA_LOGICA_COCLEE_SPLIT_LINEA_DIRETTA_INVERTER = "49";
    public static String USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL = "50";
    public static String USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL_AUX = "51";

    ////////////////////////////////////////////////////////////////////////////////////////////////////// PNEUMATICHE  
    public static String USCITA_LOGICA_EV_ASPIRATORE_LINEA_TRAMOGGIA_DI_CARICO = "52";
    public static String USCITA_LOGICA_EV_ASPIRATORE_LINEA_MISCELATORE = "53";
    public static String USCITA_LOGICA_EV_ASPIRATORE_LINEA_BILANCIA_SACCHETTI = "54";
    public static String USCITA_LOGICA_EV_ASPIRATORE_LINEA_MANUALE = "55";
    public static String USCITA_LOGICA_EV_BILANCIA_DI_CARICO = "56";
    public static String USCITA_LOGICA_EV_RIBALTA_SACCO = "57";
    public static String USCITA_LOGICA_EV_BLOCCA_SACCO = "58";
    public static String USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO = "59";
    public static String USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO = "60";
    public static String USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_VALVOLA = "61";
    public static String USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_PULISCI_OGIVA = "62";
    public static String USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_VALVOLA = "63";
    public static String USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_FLUIDIFICATORI = "64";
    public static String USCITA_LOGICA_EV_SOFFIATORE_VALVOLA_SCARICO_SVUOTA_TUBO = "65";
    public static String USCITA_LOGICA_EV_VALVOLA_SCARICO_COMANDO_UNICO = "66";
    public static String USCITA_LOGICA_EV_VALVOLA_SCARICO_1 = "67";
    public static String USCITA_LOGICA_EV_VALVOLA_SCARICO_2 = "68";
    public static String USCITA_LOGICA_EV_VALVOLA_SCARICO_3 = "69";
    public static String USCITA_LOGICA_EV_VALVOLA_SCARICO_4 = "70";
    public static String USCITA_LOGICA_EV_BILANCIA_DI_CARICO_AUSILIARIA = "71";
    public static String USCITA_LOGICA_EV_SERIE_VIBRO_PNEUMATICI_VALVOLA_DI_CARICO_AUSILIARIA = "72";
    public static String USCITA_LOGICA_EV_SERIE_VIBRO_FUNGHI_VALVOLA_DI_CARICO_AUSILIARIA = "73";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_A = "74";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_A = "75";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_A = "76";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_B = "77";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_B = "78";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_B = "79";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_C = "80";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_C = "81";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_C = "82";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_D = "83";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_D = "84";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_D = "85";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_E = "86";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_E = "87";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_E = "88";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_F = "89";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_F = "90";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_F = "91";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_G = "92";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_G = "93";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_G = "94";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_H = "95";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_H = "96";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_H = "97";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_I = "98";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_I = "99";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_I = "100";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_J = "101";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_J = "102";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_J = "103";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_K = "104";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_K = "105";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_K = "106";
    public static String USCITA_LOGICA_EV_ATTUATORE_VALVOLA_ASPIRATORE_SILO_L = "107";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_FUNGHI_SILO_L = "108";
    public static String USCITA_LOGICA_EV_SERIE_VIBRATORI_PNEUMATICI_SILO_L = "109";

    ////////////////////////////////////////////////////////////////////////////////////////////////////// INGRESSI 
    /////////////////////////////////////////////////////////////////////////////////////////////////// INGRESSI MAIN PANEL
    public static int INGRESSO_LOGICO_CONTATTO_SPORTELLO_MISCELATORE = 0;
    public static int INGRESSO_LOGICO_CONTATTO_VALVOLA_CARICO = 1;
    public static int INGRESSO_LOGICO_CONTATTO_VALVOLA_SCARICO = 2;
    public static int INGRESSO_LOGICO_SERIE_PULSANTI_BLOCCA_SACCO = 3;
    public static int INGRESSO_LOGICO_PULSANTE_STOP = 4;
    public static int INGRESSO_LOGICO_SELETTORE_MANUALE_VIBRO = 5;
    public static int INGRESSO_LOGICO_SELETTORE_VALVOLA_SCARICO = 6;
    public static int INGRESSO_LOGICO_CONTATTO_RIBALTA_SACCO = 7;
    public static int INGRESSO_LOGICO_CONTATTO_BLOCCA_SACCO_ = 8;
    public static int INGRESSO_LOGICO_FINECORSA_NASTRO = 9;
    public static int INGRESSO_LOGICO_ABILITAZIONE_NASTRO = 10;
    public static int INGRESSO_LOGICO_RETROAZIONE_MDUINO_MAIN_PANEL = 11;

    /////////////////////////////////////////////////////////////////////////////////////////////////// INGRESSI SILOS PANEL / AUX
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_A = 12;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_B = 13;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_C = 14;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_D = 15;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_E = 16;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_F = 17;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_G = 18;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_H = 19;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_I = 20;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_J = 21;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_K = 22;
    public static int INGRESSO_LOGICO_CONTATTO_COCLEA_L = 23;

    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_A = 24;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_B = 25;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_C = 26;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_D = 27;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_E = 28;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_F = 29;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_G = 30;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_H = 31;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_I = 32;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_J = 33;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_K = 34;
    public static int INGRESSO_LOGICO_SELETTORE_DISABILITA_COCLEA_L = 35;

    public static int INGRESSO_LOGICO_RETROAZIONE_MDUINO_SILOS_PANEL = 36;
    public static int INGRESSO_LOGICO_RETROAZIONE_MDUINO_SILOS_PANEL_AUX = 37;
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Immagine Caricamento
    public static String LOADING_IMAGE_NAME = "10.gif";
    public static Double LOADING_IMAGE_CORDINATE_X = 0.0;
    public static Double LOADING_IMAGE_CORDINATE_Y = 320.0;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Scroll e Cursori
    public static int SCROLL_SENSIBILITA_X = 2;
    public static int SCROLL_SENSIBILITA_Y = 2;
    public static int SCROLL_TOLLERANZA_X = 5;
    public static int SCROLL_TOLLERANZA_Y = 5;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Ripristino del Backup Sql
    public static String MACCHINA_DATA_SQL_RIPRISTINO = "Ripristino";
    public static String MACCHINA_ZIP_PASSWORD = "macchinaXY_pwd";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Runtime Command
    public static String COMANDO_CHIUSURA_APPLICAZIONE;
    public static String NOME_BROWSER;
    public static String NOME_VIDEO_CHAT;
    public static String SHUTDOWN_COMMAND;
    public static String REBOOT_COMMAND;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// File Eseguibile
    public static String FILE_EXECUTABLE_PREFIX_JAR = "java -jar";
    public static String FILE_EXECUTABLE_PREFIX_RAM_S = "-Xms";
    public static String FILE_EXECUTABLE_PREFIX_RAM_X = "-Xmx";
    public static String FILE_EXECUTABLE_SUFFIX_RAM = "m";
    public static String FILE_EXECUTABLE_NAME = "fabCloud";
    public static String FILE_EXECUTABLE_EXENSION;
    public static String FILE_EXECUTABLE_EXENSION_CD = "cd";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Parametri Pannelli
    public static int FATTORE_DIV_UNITA_MISURA_MISCELE = 1000;
    public static String TIPO_TEXT_FIELD_COLORE = "NTYPE";
    public static int NUMERO_ERRORI_PRESE = 3;
    public static int NUMERO_PRESE_VISUALIZZATE = 5;
    public static int LUNGHEZZA_RIGA_FORMULA_COLORE = 45;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Thread Interruttori Manuali
    public static int THREAD_CONTROLLO_INTERRUTTORI_MANUALI = 2;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Lunghezza Stringa Ingressi
    public static int LUNGHEZZA_STRINGA_INGRESSI = 16;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Comunicazione UDP
    public static int DATAGRAM_PACKET_DIM = 32;
    public static String UDP_CHAR_BEGIN = "$";
    public static String UDP_CHAR_END = "&";
    public static String UDP_CHARSET = "0123456789ABCDEF";
    public static String UPD_PACKET_ID_MAX = "9999";
    public static int UDP_SENDING_REP = 10;
    public static String UDP_CHAR_PACKET_OUT = "O";
    public static String UDP_CHAR_PACKET_IN = "I";
    public static int UDP_BINARIO_LEN = 4;
    public static String UDP_CHAR_PACKET_IN_SECONDO_ARDUINO = "J";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// SimulazionePeso      
    public static boolean SIMULAZIONE_PESO_ABILITATA = false;
    public static int SIMULAZIONE_PESO_INCREMENTO = 60;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Temporizzatori 
    public static int FRAZIONAMENTO_TEMPI = 2;
    public static int TEMPORIZZATORE_CONTROLLO_SBLOCCO_SACCO_ORIGAMI4 = 1000;
    public static int TEMPORIZZATORE_CONTROLLO_PESO_ORIGAMI4 = 40;
    public static int TEMPORIZZATORE_CONTROLLO_APRI_CHIUDI_VALVOLA = 160;
    public static int TEMPORIZZATORE_CONTROLLO_ASPIRATORE = 2000;
    public static int TEMPORIZZATORE_RESET_LINEA_MICRO = 3000;
    public static int TEMPORIZZATORE_SEGNALE_ACUSTICO = 2000;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Loggers
    public static int LOG_FILE_MAX_LENGHT = 5242880;
    public static int LOG_FILE_MAX_NUM = 10;
    public static String LOG_FILE_DATE_FORMATTER = "hh:mm:ss:SSS";
    public static String LOG_CHAR_SEPARATOR = "###";
    public static int LOG_PESATURA_INTERVALLO_REP_CONFIG = 4;
    public static int LOG_PESATURA_INTERVALLO_REP_INFO = 6;
    public static String LOG_SERIAL_SEPARATOR_PATTERN = "%g-";
    public static int LOG_APPROSSIMAZIONE_NUM_CAR = 3;
    public static String LOG_APPROSSIMAZIONE_CAR = ".";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// BufferIO 
    public static int BUFFER_IO_MAX = 6;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Decodifica path sql
    public static String DEC_PATH_SQL_STR_REM_1 = "-";
    public static String DEC_PATH_SQL_STR_REM_2 = "_";
    public static String DEC_PATH_SQL_STR_INS_1 = "\\";
    public static String DEC_PATH_SQL_STR_INS_2 = "\"";
    public static String DEFAULT_PATH_MYSQL;
    public static String DEFAULT_PATH_MYSQLDUMP;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Magazzino Componenti    
    public static String MAGAZZINO_COMPONENTI_COMP_NON_DEFINITO = "COD_ND";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// File Eseguibile  
    public static String FILE_ESEGUIBILE_STRINGA_A = "#!/bin/bash";
    public static String FILE_ESEGUIBILE_STRINGA_B = "cd";
    public static String FILE_ESEGUIBILE_STRINGA_C = "java -jar -Xms";
    public static String FILE_ESEGUIBILE_STRINGA_D = "m -Xmx";
    public static String FILE_ESEGUIBILE_STRINGA_E = "m dist/";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Controllo Chiusura Valvola Fine Processo
    public static int TEMPO_COMANDO_CHIUS_VALV_FINE_PESATURA = 800;
    public static int FREQ_CONTR_CHIUS_VALV_FINE_PESATURA = 40;
    public static int RIP_CONTR_CHIUS_VALV_FINE_PESATURA = 5;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Carattere Separazione Stringa Prese Macchina
    public static String CHAR_SEP_STRINGA_PRESE_MACCHINA = "_";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Ripetizioni
    public static int RIPETIZIONI_INVIO_AGGIORNAMENTO_PASSO = 5;
    public static int RIPETIZIONI_AGGIORNAMENTO_STATO_SVUOTA_VALVOLA = 5;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifiche
    public static int NUMERO_PAGINE_VERIFICHE = 1;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Conversione Peso
    public static int DECIMALI_CONVERSIONE_PESO = 3;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Modifica dimensione sacco
    public static int DEFAULT_INCREMENTO_MODIFICA_DIMENSIONE_SACCO = 300;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Valori Default Parametri Prodotto
    public static String DEFAULT_VELOCITA_MISCELAZIONE = "5000";
    public static String DEFAULT_TEMPO_MISCELAZIONE = "120000";
    public static String DEFAULT_CORRETTIVO_VEL_PRODOTTO = "100";
    public static String DEFAULT_CORRETTIVO_TEMPI_PRODOTTO = "100";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Ordini
    public static String TAB_ORDINI_DATE_FORMAT = "yyyy-MM-dd";
    public static String DEF_STATE_ORDINE_VALIDO = "0";
    public static String DEF_STATE_ORDINE_SOSPESO = "1";
    public static String DEF_STATE_ORDINE_EVASO = "1";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Microdosatore Serie 1 - 2017
    public static char MICRO_CHAR_SEP = '_';
    public static char MICRO_CHAR_END = '&';
    public static char MICRO_CHAR_PTYPE_LETTURAPESO = 'L';
    public static char MICRO_CHAR_PTYPE_AVVIOPESATURA = 'A';
    public static char MICRO_CHAR_PTYPE_VERIFICA = 'T';
    public static char MICRO_CHAR_PTYPE_INIT = 'I';
    public static char MICRO_CHAR_PTYPE_CONFIG0 = 'C';
    public static char MICRO_CHAR_PTYPE_CONFIG1 = 'V';
    public static char MICRO_CHAR_PTYPE_CONFIG2 = 'P';
    public static char MICRO_CHAR_PTYPE_COPERCHIO = 'K';
    public static char MICRO_CHAR_PTYPE_MESCOLA = 'M';
    public static char MICRO_CHAR_PTYPE_MOTORECOCLEE = 'R';
    public static char MICRO_CHAR_PTYPE_CAMBIO_VEL = 'S';
    public static char MICRO_CHAR_PTYPE_MOTORECOCLEE_ROTAZIONE_INVERSA = 'W';
    public static char MICRO_CHAR_CONF_TRUE = 't';
    public static char MICRO_CHAR_CONF_FALSE = 'f';
    public static int MICRO_CHECK_SUM_LEN = 2;
    public static char MICRO_CHAR_SEP_CURVA_VEL1 = '.';
    public static char MICRO_CHAR_SEP_CURVA_VEL2 = '/';
    public static int MICRO_DIFFERENZIALE_MICRO_VUOTO = 20;
    public static int MICRO_NUMERO_RIP_MICRO_VUOTO = 10;
    public static int MICRO_NUMERO_RIP_TENTATIVI_CONFIG = 10;
    public static int MICRO_QUANTITA_DIFF_TOTALE_RICHIESTO = 1000;
    public static int MICRO_NUM_CHAR_INPUTS = 1;
    public static char MICRO_CHAR_PTYPE_AGGIORNA_STATO_CONTATTO = 'D';
    public static int MICRO_FREQUENZA_THREAD_CONTROLLO_CONTATTO = 200;
    public static int MICRO_DIM_PACK_UDP = 1024;
    public static int MICRO_NUM_RIP_INVIO_PACCHETTO = 5;
    public static String MICRO_CHAR_PRESA_MICRO = "Z";
    public static int MICRO_FREQ_THREAD_CONTROLLO_MICRODOSATURE_ESEGUITE = 200;
    public static int FREQ_THREAD_CONTROLLO_FINE_CARICO_MATERIE_PRIME = 1000;
    public static int MICRO_FREQ_AGGIORNAMENTO_STATO_MICRO = 30;
    public static int MICRO_FREQ_THREAD_LETTURA_PESO_CONTROLLO_MANUALE = 800;
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Bilancia sacchi a valvola aperta
    public static int BILANCIA_OMB_DIM_PACK_UDP = 64;
    public static char BILANCIA_OMB_CHAR_SEP = '_';
    public static char BILANCIA_OMB_CHAR_END = '&';
    public static char BILANCIA_OMB_CHAR_PTYPE_LETTURAPESO = 'L';
    public static char BILANCIA_OMB_CHAR_PTYPE_AVVIOPESATURA = 'A';
    public static char BILANCIA_OMB_CHAR_PTYPE_VERIFICA = 'T';
    public static char BILANCIA_OMB_CHAR_PTYPE_INIT = 'I';
    public static char BILANCIA_OMB_CHAR_PTYPE_CONFIG0 = 'C';
    public static char BILANCIA_OMB_CHAR_PTYPE_CONFIG1 = 'V';
    public static char BILANCIA_OMB_CHAR_PTYPE_CONFIG2 = 'P';
    public static char BILANCIA_OMB_CHAR_PTYPE_AVVIO_SECCHI = 'J';
    public static char BILANCIA_OMB_CHAR_SEP_CURVA_VEL1 = '.';
    public static char BILANCIA_OMB_CHAR_SEP_CURVA_VEL2 = '/';
    public static char BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA = 'R';
    public static char BILANCIA_OMB_CHAR_PTYPE_MOTORECOCLEA_ROTAZIONE_INVERSA = 'W';
    public static char BILANCIA_OMB_CHAR_PTYPE_CAMBIO_VEL = 'S';
    public static char BILANCIA_OMB_CHAR_PTYPE_BLOCCA_SACCO = 'B';
    public static char BILANCIA_OMB_CHAR_CONF_TRUE = 't';
    public static char BILANCIA_OMB_CHAR_CONF_FALSE = 'f';
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// CloudFab4.0
    public static int DEFAULT_LUNGHEZZA_COD_SACCO = 6;
    public static int FREQUENZA_THREAD_RIP_AVVIA_MICRO = 40;
    public static int PULIZIA_MAX_VEL = 12000;
    public static int FREQUENZA_ATTIVAZIONE_RIPETUTE_ARIA = 100;
    public static int FREQUENZA_DISATTIVAZIONE_RIPETUTE_ARIA = 400;
    public static int NUMERO_RIPETUTE_ARIA = 5;
    public static int FREQUENZA_LETTURA_PESO_BILANCIAOMB = 300;
    public static int TEMPORIZZATORE_APRI_CHIUDI_VALVOLA_OMB = 800;
    public static int AZZERAMENTO_BIL_CARICO_NUM_RIP = 10;
    public static int AZZERAMENTO_BIL_CARICO_VALORE_RIF = 500;
    public static String CARATTERE_PRESA_NON_DEFINITA = "-";
    public static char CARATTERE_STRINGA_VEL_IND_COMP = '.';
    public static char CARATTERE_ELEMENTI_STRINGA_VEL_IND_COMP = '/';
    public static char CARATTERE_FINE_STRINGA_VEL_IND_COMP = '!';
    public static String DEFAULT_COD_SACCHETTO_TRACC_DISAB = "TRACC_DISABILITATA";
    public static int TEMPORIZZATORE_ARIA_FORMA_SACCO = 800;
    public static int TIPO_PROCESSO_PRODUZIONE = 1;
    public static int TIPO_PROCESSO_PULIZIA_AUTOMATICA = 2;
    public static int TIPO_PROCESSO_SVUOTAMENTO = 3;
    public static String PATH_USB_RIPRISTINO_TEMP = "Ripristino/.temp";
    public static String BACKUP_USB_FILE_NAME_CHAR_SEP = "_";
    public static String BACKUP_USB_FILE_NAME_DATE_FORMAT = "MMddyyyyHHmmss";
    public static String ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Password SuperUser
    public static String CODICE_SUPERUSER = "$SPF1978_";
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Database
    public static String PATH_FILE_DEFAULT_VERIFICA_DATABASE = "lista_verifica.txt";
    public static String DEFAULT_MSG_LISTA_PRODOTTI = "LISTA PRODOTTI";
    public static String DEFAULT_MSG_LISTA_COMPONENTI = "LISTA COMPONENTI";
    public static String DEFAULT_MSG_LISTA_LOTTI = "LISTA CODICI LOTTO";

    public static void loadMacchinaProperties() throws IOException {

        // Lettura dei parametri dipendenti dal sistema operativo
        if (System.getProperty(
                "os.name").toLowerCase().contains("mac")) {

            ///////////
            // MAC  ///
            ///////////
            PATH_BROWSER = "/Applications/Firefox.app/Contents/MacOS/Firefox";
            PATH_VIDEO_CHAT = "/Applications/Skype.app/Contents/MacOS/teamviewer";
            COMANDO_CHIUSURA_APPLICAZIONE = "killall";
            NOME_BROWSER = "Firefox";
            NOME_VIDEO_CHAT = "teamviewer";
            SHUTDOWN_COMMAND = "shutdown -h now";
            REBOOT_COMMAND = "shutdown -r now";
            FILE_EXECUTABLE_EXENSION = ".command";

            DEFAULT_PATH_MYSQLDUMP = "/usr/local/mysql/bin/mysqldump";
            DEFAULT_PATH_MYSQL = "/usr/local/mysql/bin/mysql";

        } else if (System.getProperty(
                "os.name").toLowerCase().contains("win")) {

            ///////////////
            // WINDOWS  ///
            ///////////////
            PATH_BROWSER = "\"C:\\Programmi\\Mozilla Firefox\\firefox.exe\"";
            PATH_VIDEO_CHAT = "\"C:\\Programmi\\Skype\\Phone\\skype.exe\"";
            COMANDO_CHIUSURA_APPLICAZIONE = "taskkill /IM";
            NOME_BROWSER = "firefox.exe";
            NOME_VIDEO_CHAT = "Skype.exe";
            SHUTDOWN_COMMAND = "shutdown.exe -s -t 0";
            REBOOT_COMMAND = "shutdown.exe -s -t 0";
            FILE_EXECUTABLE_EXENSION = ".bat";

            DEFAULT_PATH_MYSQLDUMP = "\"C:\\Programmi\\MySQL\\MySQL Server 5.6\\bin\\mysqldump\"";
            DEFAULT_PATH_MYSQL = "\"C:\\Programmi\\MySQL\\MySQL Server 5.6\\bin\\mysql\"";

        } else if (System.getProperty(
                "os.name").toLowerCase().contains("nix")
                || System.getProperty("os.name").toLowerCase().contains("nux")
                || System.getProperty("os.name").toLowerCase().indexOf("aix") > 0) {

            /////////////
            // LINUX  ///
            /////////////
            PATH_BROWSER = "firefox";
            PATH_VIDEO_CHAT = "teamviewer";
            COMANDO_CHIUSURA_APPLICAZIONE = "killall";
            NOME_BROWSER = "firefox";
            NOME_VIDEO_CHAT = "teamviewer";
            SHUTDOWN_COMMAND = "shutdown -h 0";
            REBOOT_COMMAND = "shutdown -r 0";
            FILE_EXECUTABLE_EXENSION = ".sh";

            DEFAULT_PATH_MYSQLDUMP = "mysqldump";
            DEFAULT_PATH_MYSQL = "mysql";

        }
    }

    public static void CreatePersistenceUnit() {

        // Generazione di una Map contenente le proprietà
        HashMap<String, String> connnectionProperties = new HashMap<>();

        //Impostazione runtime di username e password
        connnectionProperties.put("hibernate.connection.username", GestorePassword.userName());
        connnectionProperties.put("hibernate.connection.password", GestorePassword.passWord());
        connnectionProperties.put("hibernate.connection.url",
                "jdbc:mysql://" + DATABASE_HOSTNAME
                + ":" + DATABASE_PORT
                + "/" + DATABASE_NAME);
      
        connnectionProperties.put("hibernate.show_sql", "false"); 

        //Dichiarazione dell'entityManager
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(DATABASE_PERSISTENCE_UNIT, connnectionProperties);

    }
}
