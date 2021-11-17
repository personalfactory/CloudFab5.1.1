package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CHAR_SEP_ID_BILANCIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CHAR_SEP_STRINGA_PRESE_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CODICE_COMPONENTE_ALTERNATIVO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_CHAR_SEP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_ABILITATO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_COD_INGRESSO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_COD_OPERATORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_DESCRI_MOVE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_DT_FINE_PROCEDURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_DT_INIZIO_PROCEDURA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_DT_MOV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_ID_CICLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_ID_MATERIALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_ID_MOV_ORI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INEPHOS;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_10;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_3;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_4;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_5;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_6;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_7;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_8;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_INFO_9;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_OPERAZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_ORIGINE_MOV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_PESO_TEORICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_PROCEDURA_ADOTTATA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_QUANTITA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_SILO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_TIPO_MATERIALE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_KEY_TIPO_MOV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_DB_EXPORT_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_EXPORT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_EXPORT_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_EXPORT_MOV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_EXPORT_PROD;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_FILE_EXT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_IMPORT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CSV_PATH_IMPORT_DEST;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DATABASE_HOSTNAME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DATABASE_NAME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DATABASE_PORT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DECIMALI_CONVERSIONE_PESO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_LUNGHEZZA_COD_SACCO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ENTITY_MANAGER_FACTORY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_ESEGUIBILE_STRINGA_A;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_ESEGUIBILE_STRINGA_B;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_ESEGUIBILE_STRINGA_C;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_ESEGUIBILE_STRINGA_D;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_ESEGUIBILE_STRINGA_E;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_EXECUTABLE_EXENSION;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FILE_EXECUTABLE_NAME;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FTP_DOWNLOAD_DIR_PATH;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.FTP_UPLOAD_DIR_PATH;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_COMPONENTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_PRODOTTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.KEY_PROPERTIES_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_09;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_11;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_12;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_TAB;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOG_CHAR_SEPARATOR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.NUM_BILANCE_UTILIZZABILI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_SEP_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_FILE_DEFAULT_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.POS_0DEF;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.POS_0_COMANDO_UNICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.POS_100;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.POS_100_COMANDO_UNICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.POS_20;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.POS_53;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TAB_ORDINI_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TIPO_PROCESSO_PRODUZIONE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TIPO_PROCESSO_PULIZIA_AUTOMATICA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.TIPO_PROCESSO_SVUOTAMENTO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_VALVOLA_SCARICO_1;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_VALVOLA_SCARICO_2;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_VALVOLA_SCARICO_3;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_VALVOLA_SCARICO_4;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_LOGICA_EV_VALVOLA_SCARICO_COMANDO_UNICO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoConfigOri;
import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoOri;
import eu.personalfactory.cloudfab.macchina.entity.ChimicaOri;
import eu.personalfactory.cloudfab.macchina.entity.CicloOri;
import eu.personalfactory.cloudfab.macchina.entity.CicloProcessoOri;
import eu.personalfactory.cloudfab.macchina.entity.ComponenteOri;
import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import eu.personalfactory.cloudfab.macchina.entity.DizionarioOri;
import eu.personalfactory.cloudfab.macchina.entity.FiguraOri;
import eu.personalfactory.cloudfab.macchina.entity.MacchinaOri;
import eu.personalfactory.cloudfab.macchina.entity.MovimentoSingMacOri;
import eu.personalfactory.cloudfab.macchina.entity.NumSacchettoOri;
import eu.personalfactory.cloudfab.macchina.entity.OrdineSingMacOri;
import eu.personalfactory.cloudfab.macchina.entity.ParametroGlobMacOri;
import eu.personalfactory.cloudfab.macchina.entity.PresaOri;
import eu.personalfactory.cloudfab.macchina.entity.ProcessoOri;
import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreAllarmeOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParCompOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParOrdineOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParProdMacOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParSacchettoOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParSingMacOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreProdottoOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreRipristinoOri;
import eu.personalfactory.cloudfab.macchina.gestore.password.GestorePassword;
import eu.personalfactory.cloudfab.macchina.loggers.BilanceLogger;
import eu.personalfactory.cloudfab.macchina.loggers.ControlloLogger;
import eu.personalfactory.cloudfab.macchina.loggers.ProcessoLogger;
import eu.personalfactory.cloudfab.macchina.loggers.PuliziaLogger;
import eu.personalfactory.cloudfab.macchina.loggers.SchedaIOLogger;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.panels.MyStepPanel;
import eu.personalfactory.cloudfab.macchina.panels.Pannello01_SceltaProduzione;
import eu.personalfactory.cloudfab.macchina.panels.Pannello02_SceltaFiltro;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroGen;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroGen_Alter;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_FiltroProdCod_Alter;
import eu.personalfactory.cloudfab.macchina.panels.Pannello03_Ordini;
import eu.personalfactory.cloudfab.macchina.panels.Pannello04_SceltaTipoChimica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello05_SceltaNumMiscele;
import eu.personalfactory.cloudfab.macchina.panels.Pannello06_SceltaDimContenitore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello07_SceltaTipoContenitore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello08_SceltaCliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello09_SceltaCambioComponenti;
import eu.personalfactory.cloudfab.macchina.panels.Pannello10_ScelteEffettuate;
import eu.personalfactory.cloudfab.macchina.panels.Pannello11_Processo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello12_Aggiornamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello13_Configurazione_Generale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello14_Configurazione_Parametri;
import eu.personalfactory.cloudfab.macchina.panels.Pannello15_Configurazione_Prese;
import eu.personalfactory.cloudfab.macchina.panels.Pannello16_Controllo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello17_Verifiche;
import eu.personalfactory.cloudfab.macchina.panels.Pannello18_Controllo_Report;
import eu.personalfactory.cloudfab.macchina.panels.Pannello19_Spegnimento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello20_Assistenza;
import eu.personalfactory.cloudfab.macchina.panels.Pannello21_Ricerca;
import eu.personalfactory.cloudfab.macchina.panels.Pannello22_Ricerca_Filtro_Generale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello23_Ricerca_Codici_Chimica_Disponibili;
import eu.personalfactory.cloudfab.macchina.panels.Pannello24_Ricerca_Prodotti_Per_Cliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello25_Ricerca_Codici_Per_Cliente;
import eu.personalfactory.cloudfab.macchina.panels.Pannello26_Ricerca_Dettagli;
import eu.personalfactory.cloudfab.macchina.panels.Pannello27_Configurazione_MateriePrime;
import eu.personalfactory.cloudfab.macchina.panels.Pannello28_Inventario;
import eu.personalfactory.cloudfab.macchina.panels.Pannello29_RecuperaCodice;
import eu.personalfactory.cloudfab.macchina.panels.Pannello30_ConfermaRecuperaCodice;
import eu.personalfactory.cloudfab.macchina.panels.Pannello31_Comando_Microdosatori;
import eu.personalfactory.cloudfab.macchina.panels.Pannello32_Configurazione_Taratura_Microdosatore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello33_SceltaScaricaValvola;
import eu.personalfactory.cloudfab.macchina.panels.Pannello34_Gestione_Materie_Prime;
import eu.personalfactory.cloudfab.macchina.panels.Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta;
import eu.personalfactory.cloudfab.macchina.panels.Pannello36_ScaricoSilosMicro;
import eu.personalfactory.cloudfab.macchina.panels.Pannello37_Pulizia;
import eu.personalfactory.cloudfab.macchina.panels.Pannello38_Pulizia_Svuotamento;
import eu.personalfactory.cloudfab.macchina.panels.Pannello39_Pulizia_Automatica;
import eu.personalfactory.cloudfab.macchina.panels.Pannello40_Pulizia_Manuale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello41_SceltaColore;
import eu.personalfactory.cloudfab.macchina.panels.Pannello42_SceltaAdditivo;
import eu.personalfactory.cloudfab.macchina.panels.Pannello43_Processo_Pesatura_Manuale;
import eu.personalfactory.cloudfab.macchina.panels.Pannello44_Errori;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import eu.personalfactory.cloudfab.macchina.panels.Pannello46_ErroriAggiornamento;
import eu.personalfactory.cloudfab.macchina.panels.ScelteEffettuate;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import eu.personalfactory.cloudfab.macchina.processo.Processo;
import eu.personalfactory.cloudfab.macchina.pulizia.Pulizia;
import eu.personalfactory.cloudfab.syncorigami.utils.FTPFileTranferingListener;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class CloudFab5_0 {

    private static FileInputStream fileInputStream;
	private static FileOutputStream fileOutputStream;

	/**
     * Conversione Parametro Relativo Riferito alla Larghezza
     *
     * @param d
     * @return
     *
     */
    public static int ConvertiParametroRelativoLarghezza(double d) {
        return (int) (RISOLUZIONE_LARGHEZZA_PANNELLO / d);
    }

    /**
     * Conversione Parametro Relativo Riferito all'Altezza
     *
     * @param d
     *
     */
    public static int ConvertiParametroRelativoAltezza(double d) {
        return (int) (RISOLUZIONE_ALTEZZA_PANNELLO / d);
    }

    /**
     * Restiuisce la lista ordinata dei codici confezioni prodotti
     *
     *
     * @param codSacco
     * @return
     */
    public static String[] TrovaListaOrdinataCodiciConfezioni(String codSacco) {
        String[] lsOrdinataCodiciConfezioni;
        int result = 0;

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.codSacco!='" + codSacco + "' "
                + "ORDER BY p.codSacco");

        lsOrdinataCodiciConfezioni = new String[resultList.size()];
        for (Object o : resultList) {
            ProcessoOri processoOri = (ProcessoOri) o;
            lsOrdinataCodiciConfezioni[result] = processoOri.getCodSacco();
            result++;
        }
        return lsOrdinataCodiciConfezioni;
    }

    /**
     * Restiuisce la Lista Ordinata dei Codici Chimiche
     *
     *
     * @return
     */
    public static String[] TrovaListaOrdinataCodiciChimiche() {
        String[] lsOrdinataCodiciChimiche;
        int result = 0;

        List<?> resultList = EseguiQuery("FROM ChimicaOri c "
                + "ORDER BY c.codChimica");

        lsOrdinataCodiciChimiche = new String[resultList.size()];
        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            lsOrdinataCodiciChimiche[result] = chimicaOri.getCodChimica();
            result++;
        }
        return lsOrdinataCodiciChimiche;
    }

    /**
     * Restiuisce la Lista Ordinata dei Clienti
     *
     *
     * @param cliente
     * @return
     */
    public static String[] TrovaListaOrdinataClienti(String cliente) {
        String[] lsOrdinataClienti;
        int result = 0;

        List<?> resultList = EseguiQuery("SELECT DISTINCT p FROM ProcessoOri p "
                + "WHERE p.cliente NOT LIKE '%" + cliente + "%' "
                + "AND p.cliente <>'' "
                + "GROUP BY p.cliente "
                + "ORDER BY p.cliente");

        lsOrdinataClienti = new String[resultList.size()];
        for (Object o : resultList) {
            ProcessoOri processoOri = (ProcessoOri) o;
            lsOrdinataClienti[result] = processoOri.getCliente();
            result++;
        }
        return lsOrdinataClienti;
    }

    /**
     * Restituisce la lista Ordinata delle Date di Produzione senza Duplicazioni
     *
     * * @param dateFormat
     * @param dateFormat
     * @return
     */
    public static String[] TrovaListaOrdinataDtProduzione(String dateFormat) {
        String[] lsOrdinataDtProduzione;
        String[] swapArray;
        int i = 0;

        List<?> resultList = EseguiQuery("SELECT DISTINCT p FROM ProcessoOri p "
                + "GROUP BY p.dtProduzione "
                + "ORDER BY p.dtProduzione");

        lsOrdinataDtProduzione = new String[resultList.size()];
        swapArray = new String[resultList.size()];

        //Inizializzazione Array di Scambio per l'eliminazione dei duplicati
        for (int r = 0; r < swapArray.length; r++) {
            swapArray[r] = "";
        }

        for (Object o : resultList) {
            ProcessoOri processoOri = (ProcessoOri) o;

            lsOrdinataDtProduzione[i] = new SimpleDateFormat(dateFormat).format(processoOri.getDtProduzione());

            i++;
        }

        int j = 0;

        for (String lsOrdinataDtProduzione1 : lsOrdinataDtProduzione) {
            boolean presente = false;
            for (String swapArray1 : swapArray) {
                if (swapArray1.equalsIgnoreCase(lsOrdinataDtProduzione1)) {
                    presente = true;
                }
            }
            if (!presente) {
                swapArray[j] = lsOrdinataDtProduzione1.toUpperCase();
                j++;
            }
        }
        String[] result = new String[j];
        System.arraycopy(swapArray, 0, result, 0, j);
        return result;
    }

    /**
     * Restituisce il Numero di Chimiche Valide per un Determinato Codice
     * Prodotto
     *
     *
     * @param codChimica
     * @return
     */
    public static int TrovaNumCodiciValidi(String codChimica) {
        int numCodiciValidi = 0;
        List<?> resultList = EseguiQuery("SELECT DISTINCT COUNT (c) FROM ChimicaOri c "
                + "WHERE c.statoCh = " + false + " "
                + "AND c.codChimica like '%" + codChimica + "%'");

        if (!resultList.get(0).equals("")) {
            numCodiciValidi = Integer.parseInt(resultList.get(0).toString());
        }

        return numCodiciValidi;

    }

    /**
     * Restituisce i codici Chimica Validi per un Determinato Codice Prodotto
     *
     *
     * @param codChimica
     * @return
     */
    public static String[] TrovaCodiciChimicaValidi(String codChimica) {
        String[] listaCodiciValidi;

        List<?> resultList = EseguiQuery("FROM ChimicaOri c "
                + "WHERE c.statoCh = " + false + " "
                + "AND c.codChimica like '%" + codChimica + "%'");

        listaCodiciValidi = new String[resultList.size()];

        int result = 0;
        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            listaCodiciValidi[result] = chimicaOri.getCodChimica();
            result++;
        }
        return listaCodiciValidi;
    }

    /**
     * Restituisce i codici Chimica Validi
     *
     *
     * @param codProdotto
     * @return
     */
    public static ArrayList<String> TraovaTuttiCodiciChimicaValidiPerCodiceProdotto(String codProdotto) {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList;
        resultList = EseguiQuery("SELECT DISTINCT c FROM ChimicaOri c WHERE c.statoCh=0 AND c.codChimica LIKE '%" + codProdotto + "%' GROUP BY c.codChimica ORDER BY c.codChimica");

        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            result.add(chimicaOri.getCodChimica());

        }
        return result;
    }

    /**
     * Restituisce i codici Chimica Validi ai fini dell'Inventario
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaTuttiCodiciChimicaValidiInventario() {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT DISTINCT c FROM ChimicaOri c WHERE c.statoCh=0 OR c.statoCh=-1 GROUP BY c.codChimica ORDER BY c.codChimica");

        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            result.add(chimicaOri.getCodChimica());

        }
        return result;
    }

    /**
     * Restituisce i codici Chimica Validi ai fini dell'Inventario
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaTuttiCodiciChimicaRipristinabiliInventario() {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT DISTINCT c FROM ChimicaOri c WHERE c.statoCh="
                + ParametriGlobali.parametri.get(105)
                + " GROUP BY c.codChimica ORDER BY c.codChimica");

        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            result.add(chimicaOri.getCodChimica());

        }

        resultList = EseguiQuery("SELECT DISTINCT c FROM ChimicaOri c WHERE c.statoCh="
                + ParametriGlobali.parametri.get(105)
                + " GROUP BY c.codLotto ORDER BY c.codLotto");

        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            result.add(chimicaOri.getCodLotto());

        }
        return result;
    }

    /**
     * Restituisce i codici Chimica Validi ai fini dell'Inventario
     *
     *
     * @param codLotto
     * @return
     */
    public static ArrayList<String> TrovaCodiciChimicaPerCodiceLotto(String codLotto) {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT DISTINCT c FROM ChimicaOri c WHERE c.codLotto='" + codLotto + "' AND c.statoCh=0");

        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            result.add(chimicaOri.getCodChimica());

        }
        return result;
    }

    /**
     * Verifica presenza e usabilità codice chimica sfusa
     *
     *
     * @param codChimicaSfusa
     * @return
     */
    public static boolean VerificaCodiceChimicaSfusa(String codChimicaSfusa) {

        List<?> resultList = EseguiQuery("SELECT c FROM ChimicaOri c WHERE c.codChimica='" + codChimicaSfusa + "' AND c.statoCh=0");

        return resultList.size() > 0;
    }

    /**
     * Aggiorna Tabella Chimica a seguito dell'Inventario
     *
     *
     * @param connessione
     * @param codChimica
     * @param value
     */
    public static void AggiornaStatoCodiciChimicaInventariatiPerCodiceChimica(Connection connessione, String codChimica, String value) {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            if (connessione != null) {

                try ( Statement cmd = connessione.createStatement()) {
                    String query = "UPDATE chimica_ori SET stato_ch=" + value + " where stato_ch<>1 and cod_chimica ='" + codChimica + "'";
                    cmd.execute(query);
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            SessionLogger.logger.log(Level.WARNING, "Errore durante l''aggiornamento della tabella Chimica durante l''inventario - e={0}", e);

        }

    }

    /**
     * Aggiorna Tabella Chimica a seguito dell'Inventario in funzione del codice
     * lotto
     *
     *
     * @param connessione
     * @param codLotto
     * @param value
     */
    public static void AggiornaStatoCodiciChimicaInventariatiPerCodiceLotto(Connection connessione, String codLotto, String value) {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            if (connessione != null) {

                try ( Statement cmd = connessione.createStatement()) {
                    String query = "UPDATE chimica_ori SET stato_ch=" + value + " where stato_ch=-1 and cod_lotto ='" + codLotto + "'";
                    cmd.execute(query);
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            SessionLogger.logger.log(Level.WARNING, "Errore durante l''aggiornamento della tabella Chimica durante l''inventario - e={0}", e);

        }

    }

    /**
     * Ripristina i codici a seguito dell'Inventario in caso di inventario
     * errato
     *
     *
     * @param connessione
     */
    public static void RipristinaTuttiCodiciInventariati(Connection connessione) {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            if (connessione != null) {

                try ( Statement cmd = connessione.createStatement()) {
                    String query = "UPDATE chimica_ori SET stato_ch='0' where stato_ch ='-1'";
                    cmd.execute(query);
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            SessionLogger.logger.log(Level.WARNING, "Errore durante l''aggiornamento della tabella Chimica durante l''inventario - e={0}", e);

        }

    }

    /**
     * Dichiarazione connessione Mysql
     *
     *
     * @return
     */
    public static Connection ApriConnessioneMySql() {

        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager
                    .getConnection("jdbc:mysql://"
                            + DATABASE_HOSTNAME
                            + ":"
                            + DATABASE_PORT
                            + "/"
                            + DATABASE_NAME,
                            GestorePassword.userName(), GestorePassword.passWord());

        } catch (SQLException | ClassNotFoundException e) {
            SessionLogger.logger.log(Level.WARNING, "Errore durante l'apertura connessione durante l''inventario - e={0}", e);

        }
        return connection;
    }

    /**
     * Chiusura Connessione MySql
     *
     *
     * @param connessione
     */
    public static void ChiudiConnessioneMySql(Connection connessione) {
        try {
            connessione.close();
        } catch (SQLException ex) {
            SessionLogger.logger.log(Level.WARNING, "Errore durante la chiusura della connessione durante l''inventario - e={0}", ex);

        }
    }

    /**
     * Restituisce i codici Chimica usati per un Determinato Codice Prodotto
     *
     *
     * @param codProdotto
     * @return
     */
    public static ArrayList<String> TrovaTuttiCodiciChimicaUsatiPerCodiceProdotto(String codProdotto) {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT DISTINCT c FROM ChimicaOri c WHERE c.statoCh=1 AND c.codChimica LIKE '%" + codProdotto + "%' GROUP BY c.codChimica ORDER BY c.codChimica");

        for (Object o : resultList) {
            ChimicaOri chimicaOri = (ChimicaOri) o;
            result.add(chimicaOri.getCodChimica());

        }
        return result;
    }

    /**
     * Lista Ordinata di Codici Prodotto realizzati da un Determinato Cliente
     *
     * @param cliente
     * @return
     */
    public static String[] TrovaListaOrdinataCodiciProdottoPerCliente(String cliente) {
        String[] lsOrdinataCodiciByCliente;
        int i = 0;

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.cliente= '" + cliente + "' "
                + "GROUP BY p.codProdotto "
                + "ORDER BY p.codProdotto");

        lsOrdinataCodiciByCliente = new String[resultList.size()];

        for (Object o : resultList) {
            ProcessoOri processoOri = (ProcessoOri) o;
            lsOrdinataCodiciByCliente[i] = processoOri.getCodProdotto();
            i++;

        }
        return lsOrdinataCodiciByCliente;
    }

    /**
     * Lista Ordinata di Codici Prodotto realizzati da un Determinato Cliente
     *
     *
     * @param prodotto
     * @param cliente
     * @param idLingua
     * @return
     */
    public static String[] TrovaListaOrdinataCodiciConfezioniProdottePerCliente(String prodotto, String cliente, String idLingua) {
        String[] listaCodiciSaccoByCliente;
        int result = 0;

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.cliente= '" + cliente + "' "
                + "AND p.codProdotto= '" + TrovaCodiceProdottoPerNomeProdotto(prodotto, idLingua) + "' "
                + "GROUP BY p.codSacco "
                + "ORDER BY p.codSacco");

        listaCodiciSaccoByCliente = new String[resultList.size()];

        for (Object o : resultList) {
            ProcessoOri processoOri = (ProcessoOri) o;
            listaCodiciSaccoByCliente[result] = processoOri.getCodSacco();
            result++;
        }
        return listaCodiciSaccoByCliente;
    }

    /**
     * Lista di Codici Prodotto Realizzati in una Determinata Data
     *
     *
     * @param dtProduzione
     * @return
     */
    public static String[] TrovaListaOrdinataCodiciProdottiPerData(Date dtProduzione) {
        String[] lsOrdinataCodiciProdottoByData;
        int result = 0;
        String dateFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dtProduzione);

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.dtProduzione like '%" + dateFormatted.substring(0, 10) + "%' "
                + "GROUP BY p.codProdotto "
                + "ORDER BY p.codProdotto");

        lsOrdinataCodiciProdottoByData = new String[resultList.size()];

        for (Object o : resultList) {
            ProcessoOri processoOri = (ProcessoOri) o;
            lsOrdinataCodiciProdottoByData[result] = processoOri.getCodProdotto();
            result++;
        }

        return lsOrdinataCodiciProdottoByData;
    }

    /**
     * Restituisce il valore parametro singola macchina per idParMac
     *
     *
     * @param IdParSm
     * @return
     */
    public static String TrovaValoreParametroSingolaMacchinaPerId(int IdParSm) {
        String valoreParametro = "";

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParSingMacOri v "
                + "WHERE v.abilitato =1 "
                + "AND v.idParSm = " + IdParSm);

        for (Object o : resultList) {
            ValoreParSingMacOri valoreOri = (ValoreParSingMacOri) o;
            valoreParametro = valoreOri.getValoreVariabile();

        }
        return valoreParametro;
    }

    /**
     * Restituisce il valore parametro globale per idParGlobale
     *
     *
     * @param idParGlobale
     * @return
     */
    public static String TrovaValoreParametroGlobalePerId(int idParGlobale) {
        String valoreParametro = "";

        List<?> resultList = EseguiQuery("SELECT p FROM ParametroGlobMacOri p "
                + "WHERE p.abilitato =1 "
                + "AND p.idParGm = " + idParGlobale);

        for (Object o : resultList) {
            ParametroGlobMacOri parGlobOri = (ParametroGlobMacOri) o;
            valoreParametro = parGlobOri.getValoreVariabile();

        }
        return valoreParametro;
    }

    /**
     * Restituisce il valore parametro prodotto macchina, per idProdotto e per
     * idParametroPerMacchina
     *
     *
     * @param idProdotto
     * @param idParPm
     * @return
     */
    public static String TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(int idProdotto, int idParPm) {
        String valoreParametro = "";

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParProdMacOri v "
                + "WHERE v.idParPm = " + idParPm + " "
                + "AND v.idProdotto = " + idProdotto);

        for (Object o : resultList) {
            ValoreParProdMacOri valoreOri = (ValoreParProdMacOri) o;
            valoreParametro = valoreOri.getValoreVariabile();
        }
        return valoreParametro;
    }

    /**
     * Restituisce il Valore di un Parametro di Ripristino che ha molti recordi
     *
     *
     * @param idParRipristino
     * @return
     */
    public static ArrayList<String> TrovaGruppoValoreParametroRipristino(int idParRipristino) {
        ArrayList<String> valoreParametro = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreRipristinoOri v "
                + "WHERE v.abilitato=1 "
                + "AND v.idParRipristino= " + idParRipristino);

        for (Object o : resultList) {
            ValoreRipristinoOri valoreRipOri = (ValoreRipristinoOri) o;
            valoreParametro.add(valoreRipOri.getValoreVariabile());

        }

        return valoreParametro;
    }

    /**
     * Restituisce il Valore di un Parametro di Ripristino
     *
     *
     * @param idParRipristino
     * @return
     */
    public static String TrovaSingoloValoreParametroRipristino(int idParRipristino) {
        String result = "";

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreRipristinoOri v "
                + "WHERE v.abilitato=1 "
                + "AND v.idParRipristino= " + idParRipristino);

        for (Object o : resultList) {
            ValoreRipristinoOri valoreRipOri = (ValoreRipristinoOri) o;
            result = valoreRipOri.getValoreVariabile();

        }
        return result;
    }

    /**
     * Valuta la presenza del codice chimica nella formula del prodotto
     *
     *
     * @param idProdotto
     * @param idComponenteChimica
     * @return
     */
    public static Boolean TrovaComponenteChimicaPerIdProdotto(int idProdotto, String idComponenteChimica) {
        int result = 0;

        List<?> resultList = EseguiQuery("SELECT v FROM ComponentePesaturaOri v "
                + "WHERE v.abilitato=1 "
                + "AND v.idProdotto= " + idProdotto
                + " AND v.idComp= " + Integer.parseInt(idComponenteChimica));

        for (Object o : resultList) {
            ComponentePesaturaOri compoProdottoOri = (ComponentePesaturaOri) o;
            result = compoProdottoOri.getQuantita();

        }
        return result > 0;
    }

    /**
     * Restituisce le soluzioni possibili di numero sacchetti per una
     * determinata categoria
     *
     *
     * @param idCat
     * @return
     */
    public static int[] TrovaSoluzioniSacchettiPerIdCategoria(int idCat) {
        int[] soluzioniPossibili;
        int result = 0;

        List<?> resultList = EseguiQuery("SELECT n FROM NumSacchettoOri n, CategoriaOri c "
                + "WHERE c.idCat = " + idCat + " "
                + "AND n.idCat = c.idCat");

        soluzioniPossibili = new int[resultList.size()];
        for (Object o : resultList) {
            NumSacchettoOri soluzioniOri = (NumSacchettoOri) o;
            soluzioniPossibili[result] = soluzioniOri.getNumSacchetti();
            result++;
        }
        return soluzioniPossibili;
    }

    /**
     * Restituisce la Sigla Identificativa di una Prese in funzione dell'Id
     *
     *
     * @param idPresa
     * @return
     */
    public static String TrovaPresaPerIdPresa(int idPresa) {
        String presa = null;

        List<?> resultList = EseguiQuery("FROM PresaOri p "
                + "WHERE p.idPresa= " + idPresa + " "
                + "AND p.abilitato=1");

        for (Object o : resultList) {
            PresaOri presaOri = (PresaOri) o;
            presa = presaOri.getPresa();
        }
        return presa;
    }

    /**
     * Restituisce l'id di un Vocabolo Presente nel Benefit
     *
     * @param idDizTipo
     * @param vocabolo
     * @param idLingua
     * @return
     */
    public static int TrovaIdVocaboloPerIdDizionarioPerVocabolo(int idDizTipo, String vocabolo, String idLingua) {
        int res = 0;

        List<?> resultList = EseguiQuery("FROM DizionarioOri d "
                + "WHERE d.abilitato=1 "
                + "AND d.idDizTipo = " + idDizTipo + " "
                + "AND d.idLingua = " + idLingua + " "
                + "AND d.vocabolo = '" + vocabolo + "'");

        for (Object o : resultList) {
            DizionarioOri dizionarioOri = (DizionarioOri) o;
            res = dizionarioOri.getIdVocabolo();
        }
        return res;

    }

    /**
     * Restituisce l'Indice della Tabella di Ripristino in Funzione dell'Id
     * Parametro
     *
     *
     * @param idParRipristino
     * @return
     */
    public static int TrovaIndiceTabellaRipristinoPerIdParRipristino(int idParRipristino) {
        int result = 0;

        List<?> resultList = EseguiQuery("FROM ValoreRipristinoOri v "
                + "WHERE abilitato=1 "
                + "AND v.idParRipristino= " + idParRipristino);

        for (Object o : resultList) {
            ValoreRipristinoOri valoreRipOri = (ValoreRipristinoOri) o;
            if (result == 0) {
                result = valoreRipOri.getIdValoreRipristino();
            }
        }

        return result;
    }

    /**
     * Restistuisce l'Id di una Presa in funzione della Sigla Presa
     *
     *
     * @param presa
     * @return
     */
    public static int TrovaIdPresaPerPresa(String presa) {
        int result = 0;

        List<?> resultList = EseguiQuery("FROM PresaOri p "
                + "WHERE p.presa = '" + presa + "' "
                + "AND p.abilitato=1");

        for (Object o : resultList) {
            PresaOri presaRipOri = (PresaOri) o;
            result = presaRipOri.getIdPresa();
        }

        return result;
    }

    /**
     * Restituisce la lista dei Codici Sacchetto Corrispondenti ad un Certo
     * Codice Prodotto
     *
     *
     * @param codProdotto
     * @return
     */
    public static ArrayList<String> TrovaCodiciConfezioniPerCodiceProdotto(String codProdotto) {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.codProdotto = '" + codProdotto + "'");

        for (Object o : resultList) {
            ProcessoOri procRipOri = (ProcessoOri) o;
            result.add(procRipOri.getCodSacco());
        }

        return result;
    }

    /**
     * Restituisce la lista dei Codici Sacchetto Corrispondenti ad un Certo
     * Codice Prodotto
     *
     *
     * @param idCat
     * @param numero_sacchetti
     * @return
     */
    public static String[][] TrovaTuttiValoriParametroConfezionePerIdCategoriaPerNumeroSacchetti(int idCat, int numero_sacchetti) {

        String[][] result;
        int idNumSac = TrovaIdNumeroSacchettiPerIdCategoriaPerNumeroSacchetti(idCat, numero_sacchetti);

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParSacchettoOri v, CategoriaOri c, NumSacchettoOri n "
                + "WHERE c.idCat = " + idCat + " "
                + "AND v.idCat = c.idCat "
                + "AND n.idNumSac = " + idNumSac + " "
                + "AND v.idNumSac = n.idNumSac "
                + "ORDER BY v.idParSac");

        result = new String[(resultList.size() / numero_sacchetti) + 1][numero_sacchetti];

        int i = 0;
        int j = 1;

        for (Object o : resultList) {
            i++;
            ValoreParSacchettoOri paramSac = (ValoreParSacchettoOri) o;
            if (i > numero_sacchetti) {
                i = 1;
                j++;
            }

            result[j][paramSac.getSacchetto() - 1] = paramSac.getValoreVariabile();

        }

        return result;
    }

    /**
     * Restituisce l'idNumSac in Funzione della Categoria e del Numero di
     * Sacchetti Scelto dall'Operatore
     *
     *
     * @param idCat
     * @param numSacchetti
     * @return
     */
    public static int TrovaIdNumeroSacchettiPerIdCategoriaPerNumeroSacchetti(int idCat, int numSacchetti) {
        int result = 0;

        List<?> resultList = EseguiQuery("SELECT n FROM NumSacchettoOri n, CategoriaOri c "
                + "WHERE c.idCat = " + idCat + " "
                + "AND n.idCat = c.idCat "
                + "AND n.numSacchetti = " + numSacchetti);

        for (Object o : resultList) {
            NumSacchettoOri idSac = (NumSacchettoOri) o;
            result = idSac.getIdNumSac();
        }

        return result;
    }

    /**
     * Verifica l'esistenza di un Codice nella Tabella Chimica
     *
     *
     * @param codChimica
     * @return
     */
    public static boolean VerificaPresenzaValiditaCodiceChimica(String codChimica) {

        List<?> resultList = EseguiQuery("SELECT DISTINCT COUNT (c) FROM ChimicaOri c "
                + "WHERE c.codChimica = '" + codChimica + "' "
                + "AND c.statoCh =0");

        return (Long) resultList.get(0) > 0;

    }

    /**
     * Restituisce il numero di codici chimica presenti nella tabella processo
     *
     *
     * @param codChimica
     * @return
     */
    public static int TrovaNumeroMisceleTabellaProcessoPerCodiceChimica(String codChimica) {

        List<?> resultList = EseguiQuery("SELECT DISTINCT p FROM ProcessoOri p "
                + "WHERE p.codChimica like '%" + codChimica + "%' "
                + "GROUP BY p.codChimica");

        return resultList.size();

    }

    /**
     * Restituisce l'indice Massimo dei Codici Chimica Presenti nella Tabella
     * Processo
     *
     *
     * @param codChimica
     * @return
     */
    public static int TrovaMaxMisceleTabellaProcessoPerCodChimica(String codChimica) {

        int result = 0;

        List<?> resultList = EseguiQuery("SELECT p FROM ProcessoOri p WHERE p.dtProduzione = (SELECT MAX(p.dtProduzione) "
                + " FROM ProcessoOri p WHERE p.codChimica like '%" + codChimica + "%' )");

        for (Object o : resultList) {
            ProcessoOri proc = (ProcessoOri) o;

            int index = Integer.parseInt(proc.getCodChimica().substring(
                    codChimica.length() + ParametriGlobali.parametri.get(20).length(), //21).length(),//findValoreParametroGlobale(20).length(), //21).length(),
                    proc.getCodChimica().length()));
            if (index > result) {
                result = index;

            }
        }

        return result;

    }

    /**
     * Restituisce la lista dei codici chimica sfusa presenti nella tabella
     * Processo quindi utilizzati almeno una volta
     *
     *
     * @param codiceProdotto
     * @return codici
     */
    public static ArrayList<String> TrovaListaCodiciChimicaSfusaTabProcessoPerCodProdotto(String codiceProdotto) {

        List<?> resultList = EseguiQuery("SELECT p FROM ProcessoOri p where p.codChimica like '%" + codiceProdotto + "%' GROUP BY p.codChimica");

        ArrayList<String> codici = new ArrayList<>();

        for (Object o : resultList) {

            ProcessoOri proc = (ProcessoOri) o;

            String cod = EstraiCodiceChimciaSfusa(proc.getCodChimica());

            if (!codici.contains(cod)) {
                codici.add(cod);
            }
        }

        return codici;

    }

    /**
     * Decodifica codice chimica sfusa ottenuto dalla tabella processo
     *
     * @param codice_chimica_sfusa
     * @return codici
     */
    public static String EstraiCodiceChimciaSfusa(String codice_chimica_sfusa) {

        String res = "";
        for (int i = 0; i < codice_chimica_sfusa.length(); i++) {
            if (codice_chimica_sfusa.charAt(i) == ParametriGlobali.parametri.get(20).charAt(0)) {
                break;
            } else {

                res += codice_chimica_sfusa.charAt(i);
            }
        }

        return res;
    }

    /**
     * Restituisce l'indice massimo di miscele realizzabile per un determinato
     * codice chimica sfusa
     *
     *
     * @param codChimicaSfusa
     * @return
     */
    public static int TrovaMisceleDisponibiliPerCodChimicaSfusa(String codChimicaSfusa) {

        int result = 0;
        String numMisceleRealizzabili = "";
        int numMisceleRealizzate = TrovaMaxMisceleTabellaProcessoPerCodChimica(codChimicaSfusa);

        for (int i = codChimicaSfusa.length() - 1; i >= 0; i--) {
            if (codChimicaSfusa.charAt(i) == '.') {
                break;
            } else {
                numMisceleRealizzabili = codChimicaSfusa.charAt(i) + numMisceleRealizzabili;
            }
        }

        try {
            result = Integer.parseInt(numMisceleRealizzabili) - numMisceleRealizzate;
        } catch (NumberFormatException e) {
            SessionLogger.logger.log(Level.SEVERE, "Errore durante la conversione a Intger - e:{0}", e);
        }
        return result;

    }

    /**
     * Restituisce il numero di miscele realizzabili per codice prodotto
     *
     *
     * @param codiceProdotto
     * @return
     */
    public static int TrovaNumeroMisceleRealizzabiliChimicaSfusaPerCodProdotto(String codiceProdotto) {

        //Lista codici Chimica Validi
        String[] listaCodici = TrovaCodiciChimicaValidi(
                codiceProdotto.substring(0, Integer.parseInt(ParametriGlobali.parametri.get(47))));

        int numMisceleRealizzabili = 0;
        for (String listaCodici1 : listaCodici) {
            if (listaCodici1.substring(0, 1).equals(ParametriGlobali.parametri.get(19))) {
                numMisceleRealizzabili += TrovaMisceleDisponibiliPerCodChimicaSfusa(listaCodici1);
            }

        }
        return numMisceleRealizzabili;
    }

    /**
     * Restituisce le informazioni relative alla macchina
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaDatiMacchina() {
        ArrayList<String> datiMacchina = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM MacchinaOri m");

        for (Object o : resultList) {
            MacchinaOri macchinaOri = (MacchinaOri) o;

            datiMacchina.add(Integer.toString(macchinaOri.getIdMacchina()));
            datiMacchina.add(macchinaOri.getCodStab());
            datiMacchina.add(macchinaOri.getDescriStab());
            datiMacchina.add(macchinaOri.getRagso1());
            datiMacchina.add(macchinaOri.getDtAbilitato().toString());

        }

        return datiMacchina;
    }

    /**
     * Restituisce la lista delle lingue abilitate
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaLingueDisponibili() {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT DISTINCT(d) FROM DizionarioOri d "
                + "WHERE d.abilitato=1 "
                + "GROUP BY d.nomeLingua "
                + "ORDER BY d.idLingua");

        for (Object o : resultList) {
            DizionarioOri dizionarioOri = (DizionarioOri) o;
            result.add(HTML_STRINGA_INIZIO + dizionarioOri.getNomeLingua() + HTML_STRINGA_FINE);
        }

        return result;
    }

    /**
     * Restituisce le Lingue Presenti sulla Macchina
     *
     *
     * @param nomeLingua
     * @return
     */
    public static String TrovaIdLinguaPerNomeLingua(String nomeLingua) {

        List<?> resultList = EseguiQuery("SELECT DISTINCT(d.idLingua) FROM DizionarioOri d "
                + "WHERE d.nomeLingua = '" + nomeLingua + "' ");

        return resultList.get(0).toString();

    }

    /**
     * Aggiornamento Stato Codice Chimica
     *
     *
     * @param codChimica
     * @param state
     */
    public static void AggiornaStatoCodiceChimica(String codChimica, boolean state) {

        List<?> resultList = EseguiQuery("FROM ChimicaOri c "
                + "WHERE c.codChimica = '" + codChimica + "'");

        ChimicaOri chimicaOri = null;
        for (Object o : resultList) {
            chimicaOri = (ChimicaOri) o;
            chimicaOri.setStatoCh(state);
        }

        EditEntity(chimicaOri);

    }

    /**
     * Restituisce l'id ciclo massimo
     *
     *
     * @return
     */
    public static String TrovaMaxIdCiclo() {

        String result = "";
        List<?> processoColl = EseguiQuery("SELECT p FROM ProcessoOri p WHERE p.info1 = (SELECT MAX(CAST(p.info1,int)) FROM ProcessoOri p))");
        try {
            for (Object o : processoColl) {
                ProcessoOri procColl = (ProcessoOri) o;
                result = Integer.toString(Integer.parseInt(procColl.getInfo1()) + 1);
            }
        } catch (NumberFormatException e) {
        }

        return result;

    }

    /**
     * Restituisce i dati relativi ad un componente per un certo prodotto
     *
     *
     * @param idProdotto
     * @param idColore
     * @param idAdditivo
     * @param idComp
     * @return
     */
    public static List<?> TrovaComponentiProdottoPerIdProdPerIdComponente(int idProdotto, int idColore, int idAdditivo, int idComp) {

        List<?> resulList = EseguiQuery("SELECT c FROM ComponentePesaturaOri c, ProdottoOri p, ComponenteOri k "
                + "WHERE (p.idProdotto = " + idProdotto + " "
                + "OR p.idProdotto = " + idColore + ""
                + "OR p.idProdotto = " + idAdditivo + ")"
                + "AND c.idProdotto = p.idProdotto "
                + "AND k.idComp = " + idComp + " "
                + "AND c.idComp = k.idComp "
                + "AND c.abilitato=1");

        return resulList;

    }

    /**
     * Restituisce l'id del padre di un prodotto identificato dal codice
     *
     *
     * @param codProdotto
     * @return
     */
    public static int TrovaIdPadrePerCodiceProdotto(String codProdotto) {
        int result = 0;

        List<?> resultList = EseguiQuery("SELECT p FROM ProdottoOri p "
                + "WHERE p.codProdotto = '" + codProdotto + "' "
                + "AND p.abilitato=1");

        for (Object o : resultList) {
            ProdottoOri prodOri = (ProdottoOri) o;
            result = Integer.parseInt(prodOri.getColorato());
        }

        return result;

    }

    /**
     * Restituisce tutti i dati relativi ai componenti presenti nella formula in
     * funzione di un IdComponente
     *
     *
     * @param idComp
     * @return
     */
    public static List<?> TrovaTuttiValoriParametriCompPerIdComponente(int idComp) {

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idComp = " + idComp + " "
                + "AND v.abilitato=1 "
                + "ORDER BY idParComp");

        return resultList;

    }

    /**
     * Restituisce di un singolo parametro relativo ad un componente
     *
     *
     * @param idComp
     * @param idPar
     * @return
     */
    public static String TrovaSingoloValoreParametroCompPerIdComp(int idComp, int idPar) {
        String result = "";

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idComp = " + idComp + " "
                + "AND v.idParComp= " + idPar + " "
                + "AND v.abilitato=1 "
                + "ORDER BY idParComp");

        for (Object o : resultList) {

            ValoreParCompOri valorePar = (ValoreParCompOri) o;

            result = valorePar.getValoreVariabile();

        }

        return result;

    }

    /**
     * Restituisce le Informazioni Relative ad un Determinato Codice Chimica
     *
     *
     * @param codiceChimica
     * @return
     */
    public static List<?> TrovaDettagliCodiceChimica(String codiceChimica) {

        List<?> resultList = EseguiQuery("FROM ChimicaOri c "
                + "WHERE c.codChimica ='" + codiceChimica + "' "
                + "AND c.statoCh=0");

        return resultList;

    }

    /**
     * Restituisce le Informazioni Relative ad un Determinato Codice Chimica
     *
     *
     * @param codiceChimica
     * @return
     */
    public static List<?> TrovaDettagliCodiceChimicaUsato(String codiceChimica) {

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.codChimica ='" + codiceChimica + "' ");

        return resultList;

    }

    /**
     * Restituisce le informazioni relative ad un determinato codice confezione
     *
     * * @param codiceConfezione
     * @param codiceConfezione
     * @return
     */
    public static List<?> TrovaDettagliCodiceConfezione(String codiceConfezione) {

        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.codSacco ='" + codiceConfezione + "' ");

        return resultList;

    }

    /**
     * Registra su Database la Modifica di un Valore Parametro Singola Macchina
     *
     * * @param idParSm
     * @param idParSm
     * @param value
     */
    public static void AggiornaValoreParSingMacOri(int idParSm, String value) {

        ValoreParSingMacOri valoreParSingMacOri = null;

        List<?> resultList = EseguiQuery("FROM ValoreParSingMacOri v "
                + "WHERE v.idParSm = " + idParSm + " "
                + "AND abilitato=1");

        for (Object o : resultList) {
            valoreParSingMacOri = (ValoreParSingMacOri) o;
            valoreParSingMacOri.setValoreVariabile(value);
            valoreParSingMacOri.setDtModificaMac(new Date());
        }

        if (valoreParSingMacOri != null) {
            EditEntity(valoreParSingMacOri);
        }
    }

    /**
     * Arrotonda cifre dopo la virgola di un dato double passato come string
     *
     *
     *
     * @param stringa_da_elaborare
     * @param carattere_virgola
     * @param numero_caratteri_dopo_virgola
     * @return
     */
    public static String Arrotonda(String stringa_da_elaborare, int numero_caratteri_dopo_virgola, String carattere_virgola) {
        String res = stringa_da_elaborare;

        for (int i = 0; i < stringa_da_elaborare.length(); i++) {
            if (stringa_da_elaborare.charAt(i) == carattere_virgola.charAt(0)) {
                if (numero_caratteri_dopo_virgola < stringa_da_elaborare.length() - 1 - i) {
                    res = stringa_da_elaborare.substring(0, i + numero_caratteri_dopo_virgola + 1);
                }
                break;
            }
        }
        return res;
    }

    /**
     * Verifica presenza processi in sospeso valutando lo stato della tabella
     * valori parametri di ripristino. Se piena, processo da ripristinare.
     *
     * @return
     */
    public static boolean VerificaPresenzaValoreParRipristinoAbilitati() {

        List<?> resultList = EseguiQuery("FROM ValoreRipristinoOri v "
                + "WHERE abilitato=1");

        return resultList.size() > 0;
    }

    /**
     * Restituisce Valore Parametro di Ripristino per id
     *
     * @param id
     * @return
     */
    public static ValoreRipristinoOri TrovaSingoloValoreParRipristinoPerId(Integer id) {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        em.getTransaction().begin();

        ValoreRipristinoOri valoreRipOri = em.getReference(ValoreRipristinoOri.class, id);

        em.getTransaction().commit();

        return valoreRipOri;

    }

    /**
     * Elimina tutti i dati presenti della tabella ValoreParRipristinoOri
     *
     */
    public static void RipulisciTabellaValoreRipristinoOri() {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        em.getTransaction().begin();

        Query q = em.createQuery("DELETE ValoreRipristinoOri v");

        q.executeUpdate();
        em.getTransaction().commit();

    }

    /**
     * Modifica in Valore di un parametro della tabella ValoreParRipristino
     *
     *
     * @param id
     * @param value
     * @param idProcesso
     */
    public static void AggiornaValoreParametroRipristino(int id, String value, String idProcesso) {

        ValoreRipristinoOri valRipristinoOri = new ValoreRipristinoOri();

        valRipristinoOri.setIdValoreRipristino(TrovaIndiceTabellaRipristinoPerIdParRipristino(id));
        valRipristinoOri.setIdParRipristino(id);
        valRipristinoOri.setValoreVariabile(value);
        valRipristinoOri.setIdProCorso(Integer.parseInt(idProcesso));
        valRipristinoOri.setAbilitato(true);
        valRipristinoOri.setDtRegistrato(new Date());
        valRipristinoOri.setDtAbilitato(new Date());
        EditEntity(valRipristinoOri);

    }

    /**
     * Restistuisce la lista delle prese abilitate
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaPrese() {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM PresaOri p "
                + "WHERE p.idPresa!=" + Integer.parseInt(ParametriGlobali.parametri.get(64)) + " "
                + "AND p.abilitato=1");

        ArrayList<String> prese = TrovaPreseAbilitatePerLaMacchina();
        for (Object o : resultList) {
            PresaOri presaRipOri = (PresaOri) o;
            for (int i = 0; i < prese.size(); i++) {
                if (presaRipOri.getPresa().equals(prese.get(i))) {
                    result.add(presaRipOri.getPresa());
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Restistuisce la lista delle prese abilitate per la macchina
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaPreseAbilitatePerLaMacchina() {

        ArrayList<String> prese = new ArrayList<>();
        String preseAbilitate = ParametriSingolaMacchina.parametri.get(293);

        String temp = "";
        for (int i = 0; i < preseAbilitate.length(); i++) {

            if (preseAbilitate.charAt(i) != CHAR_SEP_STRINGA_PRESE_MACCHINA.charAt(0)) {
                temp += preseAbilitate.charAt(i);
            } else {
                if (Integer.parseInt(temp) < (Integer.parseInt(ParametriSingolaMacchina.parametri.get(2)
                        + 100
                        + Integer.parseInt(ParametriSingolaMacchina.parametri.get(239))))) {

                    prese.add(TrovaPresaPerIdPresa(Integer.parseInt(temp)));
                    temp = "";
                } else {
                    break;
                }
            }
        }
        prese.add(TrovaPresaPerIdPresa(Integer.parseInt(temp)));

        return prese;
    }

    /**
     * Restistuisce la lista degli id Componenti
     *
     *
     * @return
     */
    public static ArrayList<Integer> TrovaTuttiIdComponenti() {

        ArrayList<Integer> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ComponenteOri c "
                + "WHERE c.idComp!=" + Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)) + " "//findValoreParametroGlobale(25)) + " "
                + "AND c.abilitato =1");

        for (Object o : resultList) {
            ComponenteOri compOri = (ComponenteOri) o;
            result.add(compOri.getIdComp());
        }

        return result;
    }

    /**
     * Restistuisce la lista degli idComponenti presenti nella tabella
     * componente_pesatura
     *
     *
     * @return
     */
    public static ArrayList<Integer> TrovaIdComponentiTabellaComponentiPesatura() {

        ArrayList<Integer> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ComponentePesaturaOri c "
                + "GROUP BY c.idComp");

        for (Object o : resultList) {
            ComponentePesaturaOri compProdottoOri = (ComponentePesaturaOri) o;
            result.add(compProdottoOri.getIdComp());
        }

        return result;
    }
    

    /**
     * Restistuisce la lista degli idComponenti presenti nella tabella
     * componente_pesatura
     *
     *
     * @return
     */
    public static ArrayList<Integer> TrovaIdComponentiTabellaComponentiPesaturaConChimica() {

    	 ArrayList<Integer> lista_codici_prodotti_validi = new ArrayList<>();
    	 ArrayList<Integer> lista_componenti = new ArrayList<>();
    	 ArrayList<Integer> lista_componenti_validi = new ArrayList<>();
    	 //////////////////////////////////////////////////////////////////////////////////////////////// RICERCA PRODOTTI CON CHIMICA
    	 List<?> res1 = EseguiQuery("FROM ChimicaOri c WHERE c.statoCh=0 GROUP BY c.descriFormula");
          
         for (Object o : res1) {
             ChimicaOri chimicaOri = (ChimicaOri) o;
             lista_codici_prodotti_validi.add(TrovaIdProdottoPerCodProdotto(chimicaOri.getCodChimica().substring(1,6)));
         } 
        
         //////////////////////////////////////////////////////////////////////////////////////////////// RICERCA PRODOTTI CHIMICA INTERNA O COMPONENTI ALTERNATIVI
         List<?> res2 = EseguiQuery("FROM ProdottoOri p WHERE p.nomeProdotto LIKE '%"+ ParametriSingolaMacchina.parametri.get(143) +"%' OR"
         		+ " p.nomeProdotto LIKE '%"+ CODICE_COMPONENTE_ALTERNATIVO +"%'");
 
         for (Object o : res2) {
             ProdottoOri prodottoOri = (ProdottoOri) o;
             lista_codici_prodotti_validi.add(prodottoOri.getIdProdotto());
              
         } 
         
         List<?> res3 = EseguiQuery("FROM ProdottoOri p WHERE p.idProdotto<>p.colorato");

         for (Object o : res3) {
        	 ProdottoOri prodottoOri = (ProdottoOri) o; 
        	 
        	 List<?> res4 = EseguiQuery("FROM ChimicaOri c WHERE c.statoCh=0 AND c.codChimica LIKE '%"+ TrovaCodiceProdottoPerIdProdotto(Integer.parseInt(prodottoOri.getColorato())) +"%' GROUP BY c.descriFormula");
 
        	 if(res4.size()>0){  
        		 lista_codici_prodotti_validi.add(prodottoOri.getIdProdotto()); 
        	 } 
         }
         
        //////////////////////////////////////////////////////////////////////////////////////////////// RICERCA COMPONENTI SULLA LISTA PRODOTTI
         for (int i=0; i<lista_codici_prodotti_validi.size(); i++) {
 
        	 List<?> res5 = EseguiQuery("FROM ComponentePesaturaOri c WHERE c.idProdotto ="+ 
        			 lista_codici_prodotti_validi.get(i) 
        	 + " GROUP BY c.idComp");
           
        	 for (Object o : res5) {
        		 ComponentePesaturaOri compPesaturaOri = (ComponentePesaturaOri) o;

        		 int id_comp = compPesaturaOri.getIdComp(); 
        		 
        		 if (!lista_componenti.contains(id_comp)&&id_comp!=Integer.parseInt(ParametriSingolaMacchina.parametri.get(301))){ 
        			 lista_componenti.add(id_comp); 
        		 } 
        	 }
         }
          
         ////////////////////////////////////////////////////////////////////////////////////////////////ESCLUSIONE COMPONENTI GIA' ASSEGNATI
         for (int i=0; i<lista_componenti.size(); i++) { 
        	 List<?> res6 = EseguiQuery("FROM ValoreParCompOri v WHERE v.idComp ="+ 
        			 lista_componenti.get(i) + " AND v.idParComp=1");

        	 for (Object o : res6) {
        		 ValoreParCompOri valoreParCompOri = (ValoreParCompOri) o; 
        		 String id_comp = valoreParCompOri.getValoreVariabile(); 

        		 if (id_comp.equals(ParametriGlobali.parametri.get(64))) { 
        			 lista_componenti_validi.add(lista_componenti.get(i));
        		 } 
        	 }  
         } 
    	 
        return lista_componenti_validi;
    }

    /**
     * Restistuisce tutti gli Id eventualmente assegnati ad una Presa
     *
     *
     * @param idPresa
     * @return
     */
    public static ArrayList<Integer> TrovaTuttiComponentiPerIdPresa(int idPresa) {

        ArrayList<Integer> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idParComp =1 "
                + "AND v.valoreVariabile = " + idPresa + " "
                + "AND v.abilitato=1");

        for (Object o : resultList) {
            ValoreParCompOri valoreParOri = (ValoreParCompOri) o;
            result.add(valoreParOri.getIdComp());
        }

        return result;
    }

    /**
     * Restistuisce un singolo Id assegnato ad una Presa
     *
     *
     * @param idPresa
     * @return
     */
    public static Integer TrovaSingoloComponentePerIdPresa(int idPresa) {

        Integer result = 0;

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idParComp =1 "
                + "AND v.valoreVariabile = " + idPresa + " "
                + "AND v.abilitato=1");

        for (Object o : resultList) {
            ValoreParCompOri valoreParOri = (ValoreParCompOri) o;
            result = valoreParOri.getIdComp();
        }

        return result;
    }

    /**
     * Modifica Valore Parametro Componente
     *
     *
     * @param idParComp
     * @param idComp
     * @param value
     */
    public static void AggiornaValoreParComponenteOri(int idParComp, int idComp, String value) {

        ValoreParCompOri valoreParCompOri = null;

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idComp = " + idComp + " "
                + "AND v.idParComp = " + idParComp + " "
                + "AND v.abilitato=1");

        for (Object o : resultList) {
            valoreParCompOri = (ValoreParCompOri) o;
            valoreParCompOri.setValoreVariabile(value);
            valoreParCompOri.setDtModificaMac(new Date());
        }

        EditEntity(valoreParCompOri);
    }

    /**
     * Restituisce il Vocabolo Desiderato in Funzione dell'Id in Tabella
     *
     *
     * @param idDiz
     * @param idVoc
     * @param idLingua
     * @return
     */
    public static String TrovaVocabolo(Integer idDiz, Integer idVoc, String idLingua) {

        String result;

        if (idDiz == ID_DIZIONARIO_MESSAGGI_MACCHINA) {

            String vocabolo = TrovaVocaboloPerIdDizTipoPerIdVocabolo(idLingua,
                    idDiz,
                    idVoc);

            if (vocabolo.length() == 0) {
                Properties prop = PropertyReader.loadProperties(
                        PATH_FILE_DEFAULT_MESSAGGI_MACCHINA, new ClassLoader() {
                });
                vocabolo = prop.getProperty(KEY_PROPERTIES_MESSAGGI_MACCHINA + idVoc);
            }
            result = vocabolo;
        } else {
            result = TrovaVocaboloPerIdDizTipoPerIdVocabolo(idLingua,
                    idDiz,
                    idVoc);
        }

        return HTML_STRINGA_INIZIO
                + result + HTML_STRINGA_FINE;

    }

    /**
     * Restituisce il Valore di un Parametro Singola Macchina in Base all'id
     *
     *
     * @param idLingua
     * @param idDizTipo
     * @param idVocabolo
     * @return
     */
    public static String TrovaVocaboloPerIdDizTipoPerIdVocabolo(String idLingua, int idDizTipo, int idVocabolo) {
        String result = "";

        List<?> resultList = EseguiQuery("FROM DizionarioOri d "
                + "WHERE d.idLingua = " + idLingua + " "
                + "AND d.idDizTipo = " + idDizTipo + " "
                + "AND d.idVocabolo = " + idVocabolo + " "
                + "AND d.abilitato=1");

        for (Object o : resultList) {
            DizionarioOri dizionarioOri = (DizionarioOri) o;

            result = dizionarioOri.getVocabolo();

        }
        return result;
    }

    /**
     * Restitusce il Valore di Vel Corretto in Funzione del Componente
     *
     *
     * @param valoreVel
     * @param fattoreCorrettivo
     * @return
     */
    public static String ElaboraVelInverter(String valoreVel, String fattoreCorrettivo) {
        double result;
        String result_string;
        result = Double.parseDouble(valoreVel) * Double.parseDouble(fattoreCorrettivo);

        result_string = Integer.toString((int) result);

        return result_string;

    }

    /**
     * Aggiorna i Record della tabella Valore Parametro Ripristino
     *
     *
     * @param processo
     * @param id_tab
     * @param id_par
     * @param value
     * @param idProcesso
     */
    public static void AggiornaValoreParametriRipristino(Processo processo, int id_tab, int id_par, String value, String idProcesso) {

        processo.valRipristinoOri.setIdValoreRipristino(id_tab);
        processo.valRipristinoOri.setIdParRipristino(id_par);
        processo.valRipristinoOri.setValoreVariabile(value);
        processo.valRipristinoOri.setIdProCorso(Integer.parseInt(idProcesso));
        processo.valRipristinoOri.setAbilitato(true);
        processo.valRipristinoOri.setDtRegistrato(new Date());
        processo.valRipristinoOri.setDtAbilitato(new Date());

        EditEntity(processo.valRipristinoOri);

    }

    /**
     *
     * Merge sulla entity e commit
     *
     * @param entity
     */
    public static void EditEntity(Object entity) {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();

        em.getTransaction().begin();

        em.merge(entity);

        em.getTransaction().commit();

    }

    /**
     * Esecuzione query Hibernate
     *
     *
     * @param query
     * @return
     */
    public static List<?> EseguiQuery(String query) {

        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery(query);
        List<?> resutlList = q.getResultList();
        em.getTransaction().commit();
        return resutlList;
    }

    /**
     * Verifica che la lunghezza della stringa velocità inverter sia almeno 4
     * caratteri (la lunghezza è comunque specificata nel parametro len
     *
     *
     * @param int_str
     * @param len
     * @return
     */
    public static String VerificaLunghezzaStringa(int int_str, int len) {
        String str = Integer.toString(int_str);

        while (str.length() < len) {
            str = "0" + str;
        }

        return str;

    }

    /**
     *
     * Estrae stringa htlm iniziale e finale necessarie per la gestione delle
     * lingue asiatiche
     *
     *
     * @param s
     * @return
     */
    public static String EstraiStringaHtml(String s) {
        String res = s;

        if (s.charAt(0) == HTML_STRINGA_INIZIO.charAt(0)) {
            res = s.substring(
                    HTML_STRINGA_INIZIO.length(),
                    s.length() - HTML_STRINGA_FINE.length());
        }

        return res;

    }

    /**
     * Gestisce lo spostamento dei file di log
     *
     */
    public static void GestoreSpostamentoFileLog() {

        //Percorso della cartella Temporanea
        String dir = ParametriGlobali.parametri.get(53)
                + File.separator
                + ParametriGlobali.parametri.get(52)
                + ParametriGlobali.parametri.get(101);

        //Verifica Esistenza e/o Creazione della Directory
        File f = new File(dir);

        if (!f.exists()) {
            //////////////////////////////
            // CARTELLA NON ESISTENTE  ///
            //////////////////////////////
            f.mkdir();

        } else {
            //////////////////////////
            // CARTELLA ESISTENTE  ///
            //////////////////////////
            // Filtro File con estensione .log
            FileExtFilter fef = new FileExtFilter(ParametriGlobali.parametri.get(59));

            String[] list = f.list(fef);

            if (list != null) {

                for (String list1 : list) {
                    if (SpostaFileLog(ParametriGlobali.parametri.get(53) + File.separator + ParametriGlobali.parametri.get(52)
                            + ParametriGlobali.parametri.get(101) + File.separator + list1, ParametriGlobali.parametri.get(53)
                            + File.separator + ParametriGlobali.parametri.get(52) + File.separator + list1, list1)) {
                    }
                }
            }

        }

    }

    /**
     *
     * Esegue lo spostamento dei file di log dalla cartella temp
     *
     * @param orig
     * @param dest
     * @param fileName
     * @return
     */
    public static boolean SpostaFileLog(String orig, String dest, String fileName) {

        boolean result = false;
        File source = new File(orig);
        File destination = new File(dest);

        try {
            //Individuazione Directory dei File
            File dir = new File(ParametriGlobali.parametri.get(53) + File.separator + ParametriGlobali.parametri.get(52));
            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.CONFIG, "Directory dei file ={0}", dir);
            // Filtro File con estensione .log
            FileExtFilter fef = new FileExtFilter(ParametriGlobali.parametri.get(59));
            String[] list = dir.list(fef);

            boolean filePresente = false;
            for (String list1 : list) {
                if (list1.contains(fileName)) {
                    filePresente = true;
                    //Memorizzazione Log di Sessione
                    SessionLogger.logger.log(Level.CONFIG, "Rilevato file già presente");
                }
            }

            if (!filePresente) {
                //Memorizzazione Log di Sessione
                SessionLogger.logger.log(Level.CONFIG, "Cancellazione del file");
                Files.move(source.toPath(), destination.toPath());
                result = true;
            }

        } catch (IOException ex) {
            //Memorizzazione Log di Sessione
            SessionLogger.logger.log(Level.CONFIG, "Errore durante lo spostamento del file di log - e ={0}", ex);
        }

        return result;

    }

    /**
     *
     * Verifica che un file descritto da un determinato percorso (path) è vuoto
     * o meno
     *
     * @param path
     * @return
     */
    public static boolean VerificaFileVuoto(String path) {

        boolean result = false;
        //Memorizzazione Log di Sessione
        SessionLogger.logger.log(Level.CONFIG, "Verifica Lunghezza del File {0}", path);
        File source = new File(path);

        if (source.exists()) {
            long file_size = source.length();
            //Memorizzazione Log di Sessione
            SessionLogger.logger.log(Level.CONFIG, "Dimensione del File {0} = {1}", new Object[]{path, file_size});

            if (file_size == 0) {
                //Memorizzazione Log di Sessione
                SessionLogger.logger.config("File Vuoto");
                result = true;
            }
        } else {
            //Memorizzazione Log di Sessione
            SessionLogger.logger.warning("File Inesistente");
        }

        return result;

    }

    /**
     * Gestore archiviazione file di log dopo l'aggiornamento
     *
     */
    public static void GestoreArchiviazioneLogDopoAggiornamento() {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura di Gestione File di Log");

        //Individuazione Directory dei File
        File dir = new File(ParametriGlobali.parametri.get(53) + File.separator + ParametriGlobali.parametri.get(52));

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Directory dei file ={0}", dir);

        // Filtro File con estensione .log
        FileExtFilter fef = new FileExtFilter(ParametriGlobali.parametri.get(59));

        String[] list = dir.list(fef);

        if (list != null) {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.FINE, "Numero di file di log trovati ={0}", list.length);

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Inizio Cancellazione");

            //Scansione ed eliminazione file di log
            for (int i = 0; i < list.length; i++) {

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.log(Level.FINE, "File {0} = {1}", new Object[]{i, list[i]});

                if (list[i].contains(ParametriGlobali.parametri.get(54))
                        || list[i].contains(ParametriGlobali.parametri.get(55))
                        || list[i].contains(ParametriGlobali.parametri.get(56))
                        || list[i].contains(ParametriGlobali.parametri.get(57))
                        || list[i].contains(ParametriGlobali.parametri.get(84))
                        || list[i].contains(ParametriGlobali.parametri.get(88))
                        || list[i].contains(ParametriGlobali.parametri.get(99))
                        || list[i].contains(ParametriGlobali.parametri.get(100))) {

                    CancellazioneFileLog(list[i]);

                } else {

                    //Memorizzazione Logger della Sessione
                    SessionLogger.logger.fine("File escluso dalla cancellazione");

                }

            }

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Fine Cancellazione");

        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura di Gestione File di Log");

    }

    /**
     * Cancellazione File di Log
     *
     * @param name
     *
     */
    public static void CancellazioneFileLog(String name) {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura Cancellazione Singolo File di Log");

        //Individuazione del file da cancellare
        File fileToDelete = new File(ParametriGlobali.parametri.get(53) + File.separator + ParametriGlobali.parametri.get(52) + File.separator + name);

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Tentativo di cancellazione file ={0}", fileToDelete.getName());

        try {

            fileToDelete.delete();

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Cancellazione Eseguita con Successo");

        } catch (Exception e) {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.WARNING, "Cancellazione Fallita - e = {0}", e);
        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura Cancellazione Singolo File di Log");

    }

    /**
     * Gestore installazione nuova release programma
     *
     * @param lFrame
     * @return
     *
     */
    public static Boolean GestoreAggiornamentoRelease(FabCloudLoadingFrame lFrame) {

        //////////////////////////////////////////////////////////////////////////
        // VERIFICA PRESENZA FILE NELLA CARTELLA DEGLI AGGIORNAMENTI SOFTWARE  ///
        //////////////////////////////////////////////////////////////////////////
        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura di Gestione Aggiornamenti Programma");

        Boolean result = false;

        //Individuazione Directory dei File
        File dir = new File(ParametriGlobali.parametri.get(72));

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Directory dei file ={0}", dir);

        //Filtro File con estensione .jar
        FileExtFilter fef = new FileExtFilter(ParametriGlobali.parametri.get(73));

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Individuazione File con filtro ={0}", ParametriGlobali.parametri.get(73));

        String[] list = dir.list(fef);

        if (list != null) {

            if (list.length > 0) {

                /////////////////////////////////////////////////////////////////////
                // RILEVATO ALMENO UN FILE NELLA CARTELLA AGGIORNAMENTI SOFTWARE  ///
                /////////////////////////////////////////////////////////////////////
                //Memorizzazione Logger della Sessione
                SessionLogger.logger.log(Level.CONFIG, "Numero di file trovati ={0}", list.length);

                String percorso = dir.getAbsolutePath().substring(0, dir.getAbsolutePath().length()
                        - (ParametriGlobali.parametri.get(72).length() + 1));

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.log(Level.CONFIG, "Percorso del file .jar ={0}", percorso);

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.config("Inizio Scansione File di Aggiornamento...");

                ///////////////////////////////////////////
                // INDIVIDUAZIONE VERSIONE PIU RECENTE  ///
                ///////////////////////////////////////////
                //Dichiarazione fileLogDate
                String[] strVer = new String[list.length];

                //Scansione file di aggiornamento
                for (int i = 0; i < list.length; i++) {
                    if (list[i].startsWith(ParametriGlobali.parametri.get(74))) {
                        list[i] = list[i].replace(ParametriGlobali.parametri.get(74), "");
                        list[i] = list[i].replace(ParametriGlobali.parametri.get(73), "");

                        String temp = "";
                        strVer[i] = "";
                        for (int j = 0; j < list[i].length(); j++) {

                            if (list[i].charAt(j) == '.' || j == list[i].length() - 1) {

                                if (j == list[i].length() - 1) {
                                    temp += list[i].charAt(j);
                                }

                                while (temp.length() < 3) {
                                    temp = "0" + temp;
                                }
                                strVer[i] += temp;
                                temp = "";
                            } else {

                                temp += list[i].charAt(j);
                            }
                        }
                    }
                }

                //Adeguamento lunghezza stringa versione 
                int lMax = 0;
                for (String strVer1 : strVer) {
                    if (strVer1.length() > lMax) {
                        lMax = strVer1.length();
                    }
                }
                for (int i = 0; i < list.length; i++) {
                    while (strVer[i].length() < lMax) {
                        strVer[i] += "0";
                    }
                }

                //Individuazione ultima versione
                int idMax = 0;
                String strMax = "0";

                for (int i = 0; i < list.length; i++) {
                    if (Float.parseFloat(strVer[i]) > Float.parseFloat(strMax)) {

                        strMax = strVer[i];
                        idMax = i;
                    }
                }

                String ultimaVersione = ParametriGlobali.parametri.get(74) + list[idMax] + ParametriGlobali.parametri.get(73);

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.log(Level.CONFIG, "Ultima Versione Rilevata = {0}", ultimaVersione);

                //////////////////////
                // COPIA DEI FILE  ///
                //////////////////////
                //Installazione Aggiornamento
                TrasferimentoFileJar(ultimaVersione);

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.config("Copia del File - Eseguita --- Scrittura File Eseguibile");

                //Editing collegamento eseguibile
                GestoreFileCollegamentoEseguibile(ultimaVersione, percorso);

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.config("Scrittura File Eseguibile - Eseguita --- Cancellazione del File - Eseguita");

                lFrame.lab_new_release.setText(LOADING_IMG_TEXT_09 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_11);
                ///////////////////////////////
                // CANCELLAZIONE FILES JAR  ///
                ///////////////////////////////
                //Scansione file di aggiornamento
                for (String list1 : list) {
                    //Cancellazione File di Log
                    CancellazioneFileJar(list1);
                }

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.config("Cancellazione del File - Eseguita");

                try {

                    //Memorizzazione Logger della Sessione
                    SessionLogger.logger.config("Aggiornamento Versione del Software su Database");

                    AggiornaValoreParSingMacOri(211, ultimaVersione.substring(0, ultimaVersione.length()
                            - ParametriGlobali.parametri.get(73).length()));

                    //Memorizzazione Logger della Sessione
                    SessionLogger.logger.config("Aggiornamento Eseguito Correttamente");

                } catch (Exception e) {

                    //Memorizzazione Logger della Sessione
                    SessionLogger.logger.log(Level.CONFIG, "Aggiornamento Fallito - e ={0}", e);
                }

                result = true;

            } else {

                lFrame.lab_new_release.setText(LOADING_IMG_TEXT_09 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_12);

                /////////////////////////////////////////////
                // NESSUN FILE RILEVATO DI AGGIORNAMENTO  ///
                /////////////////////////////////////////////
                //Memorizzazione Logger della Sessione
                SessionLogger.logger.config("Nessun aggiornamento disponibile");

            }
            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Fine Scansione File di Aggiornamento");

        } else {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.warning("list == null - Directory non Trovata");

            boolean success = (new File(ParametriGlobali.parametri.get(72))).mkdir();

            if (success) {

                ///Memorizzazione Logger della Sessione
                SessionLogger.logger.log(Level.CONFIG, "Creata Directory = {0}", ParametriGlobali.parametri.get(72));

            } else {

                //Memorizzazione Logger della Sessione
                SessionLogger.logger.log(Level.CONFIG, "Impossibile Creare Directory = {0}", ParametriGlobali.parametri.get(72));
            }

        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura di Gestione Aggiornamenti Programma");

        return result;
    }

    /**
     * Cancellazione File Jar Aggiornamento
     *
     * @param file_name
     *
     */
    public static void CancellazioneFileJar(String file_name) {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio procedura Cancellazione file .jar");

        File fileToDelete = new File(ParametriGlobali.parametri.get(72) + File.separator
                + ParametriGlobali.parametri.get(74) + file_name + ParametriGlobali.parametri.get(73));

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Nome del file da cancellare = {0}", fileToDelete.getName());

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Percorso del file da cancellare = {0}", fileToDelete.getAbsolutePath());

        try {
            fileToDelete.delete();
            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Cancellazione Eseguita Con Successo");
        } catch (Exception e) {
            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.WARNING, "Cancellazione Fallita - e = {0}", e);
        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine procedura Cancellazione file .jar");
    }

    /**
     * Spostamento del nuovo jar nella cartella dist
     *
     *
     * @param file_name
     */
    public static void TrasferimentoFileJar(String file_name) {

        String file_name_source = ParametriGlobali.parametri.get(72) + File.separator + file_name;
        String file_name_dest = ParametriGlobali.parametri.get(53) + File.separator + file_name;
        File sfile = new File(file_name_source);
        File dfile = new File(file_name_dest);

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Inizio Procedura Installazione Aggiornamenti Software{0}Nome del file sorgente ={1}{2}Nome del file destinazione ={3}",
                new Object[]{
                    LOG_CHAR_SEPARATOR,
                    file_name_source,
                    LOG_CHAR_SEPARATOR,
                    file_name_dest});

        try {
            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Tentativo di Trasferimento...");

            FileChannel dest;

            fileInputStream = new FileInputStream(sfile);
			try ( FileChannel src = fileInputStream.getChannel()) {
                fileOutputStream = new FileOutputStream(dfile);
				dest = fileOutputStream.getChannel();
                src.transferTo(0, src.size(), dest);
            }

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Tentativo di Trasferimento Eseguito con Successo");

            dest.close();

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Chiusura file di destinazione");

        } catch (IOException e) {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.SEVERE, "Tentativo di Trasferimento Fallito - e ={0}", e);

        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura Installazione Aggiornamenti Software");
    }

    /**
     * Modifica del file eseguibile per lanciare l'applicazione
     *
     * @param file_name
     * @param path
     *
     */
    public static void GestoreFileCollegamentoEseguibile(String file_name, String path) {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura Creazione Collegamento Eseguibile Applicazione");

        String fileName = FILE_EXECUTABLE_NAME + FILE_EXECUTABLE_EXENSION;

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Nome del file da editare ={0}", fileName);

        //Verifica esistenza File ed eventuale creazione
        try {
            File file = new File(fileName);

            if (file.exists()) {
                file.delete();
            }

            //Creazione Nuovo file
            file.createNewFile();

            //Impostazione Permessi Esecuzione del file
            file.setExecutable(true);

            //Impostazione Permessi Scrittura del file
            file.setWritable(true);

            //Impostazione Permessi Lettura del file
            file.setReadable(true);

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Tentativo di Scrittura su file");

            //DataInputStream in;
            BufferedWriter out;

            StringBuilder fileContent = new StringBuilder();

            fileContent.append(FILE_ESEGUIBILE_STRINGA_A)
                    .append(System.getProperty("line.separator"));
            fileContent.append(FILE_ESEGUIBILE_STRINGA_B)
                    .append(" ")
                    .append(path)
                    .append(System.getProperty("line.separator"));
            fileContent.append(FILE_ESEGUIBILE_STRINGA_C)
                    .append(ParametriGlobali.parametri.get(75))
                    .append(FILE_ESEGUIBILE_STRINGA_D)
                    .append(ParametriGlobali.parametri.get(75))
                    .append(FILE_ESEGUIBILE_STRINGA_E)
                    .append(file_name);

            FileWriter fstreamWrite = new FileWriter(fileName);
            out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Tentativo di Scrittura su file Eseguito con Successo");

            out.flush();
            out.close();

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Chiusura Streaming dei file");

        } catch (IOException e) {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.WARNING, "Tentativo di Scrittura su file - Fallito - e ={0}", e);
        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura Creazione Collegamento Eseguibile Applicazione");

    }

    /**
     * Riavvio applicazione ad installazione completata
     *
     * @param workDir
     *
     */
    public static void RiavviaCloudFab(String workDir) {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura di Riavvio Applicazione");

        //Individuazione Directory dei File
        File dir = new File(workDir);

        String percorso = dir.getAbsolutePath().substring(0, dir.getAbsolutePath().length()
                - (workDir.length()));

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "File Eseguibile = {0}{1}{2}", new Object[]{percorso, FILE_EXECUTABLE_NAME, FILE_EXECUTABLE_EXENSION});

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Tentativo di Riavvio...");

        try {
            //Esecuzione Eseguibile
            Runtime.getRuntime().exec(percorso + FILE_EXECUTABLE_NAME + FILE_EXECUTABLE_EXENSION);

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.config("Riavvio Eseguito");

        } catch (IOException e) {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.SEVERE, "Riavvio Fallito - e ={0}", e);

        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura di Riavvio Applicazione");

    }

    /**
     * Sposta di una posizione verso lo zero i valori del buffer
     *
     * @param outputBuffer
     * @return
     *
     */
    public static String[] SwapBuffer(String[] outputBuffer) {
        String tempo = "";
        for (int i = 0; i < outputBuffer.length - 1; i++) {

            if (!outputBuffer[i + 1].equals("")) {
                outputBuffer[i] = outputBuffer[i + 1];
            } else {

                outputBuffer[i] = "";
                break;
            }

        }
        for (int i = 0; i < outputBuffer.length - 1; i++) {

            tempo += outputBuffer[i];

        }

        ControlloLogger.logger.info(tempo);

        outputBuffer[outputBuffer.length - 1] = "";

        return outputBuffer;
    }

    /**
     * SRestituisce il numero di elementi non "" all'interno di un buffer
     *
     * @param outputBuffer
     * @return
     *
     */
    public static int ContaElementiNonNulli(String[] outputBuffer) {

        int result = 0;
        for (String outputBuffer1 : outputBuffer) {
            if (outputBuffer1.equals("")) {
                break;
            } else {
                result++;
            }
        }

        return result;
    }

    /**
     * Validazione valore ricevuto dalla bilancia per filtrare valori errati
     *
     * @param strFromSocket
     * @param carattereIniziale
     * @param carattereFinaleNetto
     * @param carattereFinaleLordo
     * @param strLenght
     * @param charset
     * @param indirizzoPesa
     * @return
     *
     */
    public static String ValidaValoreBilancia(
            String strFromSocket,
            String carattereIniziale,
            String carattereFinaleNetto,
            String carattereFinaleLordo,
            Integer strLenght,
            String charset,
            String indirizzoPesa) {

        //Inizializzazione variabili 
        String result = "";
        boolean chIn = false;

        BilanceLogger.logger.config(strFromSocket);

        try {
            //Verifica che la Stringa ricevuta sia diversa da 0
            if (strFromSocket.length() > 0) {

                /////////////////////////
                // STRINGA NON NULLA  ///
                /////////////////////////
                //Validazione della Stringa 
                if (strFromSocket.contains(carattereIniziale) //Verifica presenza carattere iniziale 
                        //Verifica presenza carattere "n" o carattere "t"
                        && (strFromSocket.contains(carattereFinaleNetto) | strFromSocket.contains(carattereFinaleLordo))
                        //Verifica lunghezza della stringa
                        && strFromSocket.length() == strLenght) {

                    //////////////////////////////////
                    // STRINGA FORMALMENTE VALIDA  ///
                    //////////////////////////////////
                    for (int i = 0; i < strFromSocket.length(); i++) {

                        if (strFromSocket.charAt(i) == carattereIniziale.charAt(0)) {

                            //////////////////////////////////
                            // CARATTERE INIZIALE TROVATO  ///
                            //////////////////////////////////
                            chIn = true;

                        } else if (chIn) {
                            if (strFromSocket.charAt(i) != carattereFinaleNetto.charAt(0)
                                    && strFromSocket.charAt(i) != carattereFinaleLordo.charAt(0)) {

                                /////////////////////////////
                                // VALORI DI PESO VALIDI  ///
                                /////////////////////////////
                                //Costruzione stringa estrattta
                                result += strFromSocket.charAt(i);

                            } else {
                                //////////////////////////////////////////
                                // CARATTERE DI FINE STRINGA RILEVATO  ///
                                //////////////////////////////////////////
                                break;
                            }
                        }
                    }

                    //////////////////////////
                    // VERIFICA INDIRIZZO  ///
                    //////////////////////////
                    if (result.startsWith(indirizzoPesa)) {

                        //Estrazione dell'indirizzo dalla Stringa
                        result = result.substring(2, result.length());

                        if (result.length() == 6) {
                            for (int i = 0; i < result.length(); i++) {
                                if (!charset.contains(Character.toString(result.charAt(i)))) {
                                    result = "";
                                    break;
                                }
                            }
                        } else {
                            result = "";
                        }
                    } else {
                        result = "";
                    }
                }
            }
        } catch (Exception e) {

            //Memorizzazione Logger della Sessione
            SessionLogger.logger.log(Level.WARNING, "Errore di conversione del Valore letto {0}", e);

        }

        return result;

    }

    /**
     * Verifica l'avvenuta commutazione peso lordo peso netto
     *
     * @param strToSocket
     * @param strFromSocket
     * @param chInStrToSocket
     * @param chFinStrToSocket
     * @param chInStrFromSocket
     * @param chFinStrFromSocket
     * @return
     */
    public static boolean ValidaCommutazionePeso(String strToSocket,
            String strFromSocket,
            String chInStrToSocket,
            String chFinStrToSocket,
            String chInStrFromSocket,
            String chFinStrFromSocket) {

        boolean result = false;
        boolean strToSocketValida = false;
        boolean strFromSocketValida = false;

        //Validazione della Stringa Inviata 
        //Validazione della Stringa 
        if (strToSocket.contains(chInStrToSocket) && strToSocket.contains(chFinStrToSocket)) {

            strToSocketValida = true;

        }

        //Validazione della Stringa Ricevuta 
        //Validazione della Stringa  
        if (strFromSocket.contains(chInStrFromSocket) && strFromSocket.contains(chFinStrFromSocket)) {

            strFromSocketValida = true;

        }

        if (strToSocketValida && strFromSocketValida) {

            //Estrazione Indirizzo 
            boolean chInTrovato = false;
            String indirizzo = "";
            int count = 0;
            for (int i = 0; i < strToSocket.length(); i++) {

                if (chInTrovato) {
                    indirizzo += strToSocket.charAt(i);
                    count++;
                }
                if (count == 2) {
                    break;
                }

                if (strToSocket.charAt(i) == chInStrToSocket.charAt(0)) {

                    chInTrovato = true;
                }

            }

            String strPrevista = chInStrFromSocket + chInStrFromSocket + indirizzo + chFinStrFromSocket;

            result = strFromSocket.contains(strPrevista);

        }

        return result;

    }

    /**
     * Conversione da String a Esadecimale
     *
     *
     * @param strToConv
     * @param lunghezzaRisultato
     * @return
     *
     */
    public static String ConversioneStringToHex(String strToConv, int lunghezzaRisultato) {

        String Hex = Integer.toHexString(Integer.parseInt(strToConv)).toUpperCase();

        while (Hex.length() < lunghezzaRisultato) {
            Hex = "0" + Hex;
        }

        return Hex;

    }
    //Inizializzazione Scheda Relay 

    /**
     * Conversione da Binario ad esadecimale
     *
     * @param binario
     * @return
     *
     */
    public static String ConversioneBinarioToHex(String binario) {

        String t = "";
        int j = 0;
        int decimale = 0;

        for (int i = binario.length() - 1; i >= 0; i--) {
            if (Integer.parseInt(t + binario.charAt(i)) == 1) {
                decimale += Math.pow(2, j);
            }
            j++;
        }

        return Integer.toHexString(decimale).toUpperCase();

    }

    /**
     * Conversione da Binario ad esadecimale
     *
     * @param binario
     * @return
     *
     */
    public static Integer ConversioneBinarioToInt(String binario) {

        String t = "";
        int j = 0;
        int decimale = 0;

        for (int i = binario.length() - 1; i >= 0; i--) {
            if (Integer.parseInt(t + binario.charAt(i)) == 1) {
                decimale += Math.pow(2, j);
            }
            j++;
        }

        return decimale;

    }

////    /**
////     * Conversione da Esadecimale a Binario il parametro len definisce la
////     * lunghezza del valore binario
////     *
////     * @param hex
////     * @param len
////     * @return
////     *
////     */
////    public static String ConversioneHextoBinario(String hex, int len) {
////
////        String result = "";
////
////        while (hex.length() > 0) {
////
////            int n = Integer.parseInt(hex.substring(0, 1), 16);
////            String binary = Integer.toBinaryString(n);
////            hex = hex.substring(1, hex.length());
////            while (binary.length() < len) {
////                binary = "0" + binary;
////            }
////            result += binary;
////        }
////        return result;
////
////    }
    /**
     * Restituzione Id Pacchetto UDP
     *
     * @param id
     * @param pckCounterMax
     * @return
     *
     */
    public static String OttieniIdPacchettoUDP(String id, String pckCounterMax) {

        int counter = Integer.parseInt(id);

        //Controllo se l'id è superiore al valore massimmo
        if (counter >= Integer.parseInt(pckCounterMax)) {

            ////////////////////////////////////////////////////////
            // CONTATORE SUPERIORE AL VALORE MASSIMO CONSENTITO  ///
            ////////////////////////////////////////////////////////
            //Azzeramento Contatore
            counter = 0;

        }

        //Incremento contatore
        counter++;

        //Conversione in Stringa
        String result = Integer.toString(counter);

        //Aggiustamento lunghezza Stringa
        while (result.length() < pckCounterMax.length()) {

            result = "0" + result;

        }

        return result;
    }

    /**
     *
     * Validazione Stringa ricevuta comunicazione UDP
     *
     * @param msgRec
     * @param charset
     * @param charBegin
     * @param charEnd
     * @return
     *
     */
    public static String ValidaStringaRicevutaUDPClient(String msgRec, String charset, String charBegin, String charEnd) {

        String result = "";

        boolean charInizialeTrovato = false;
        boolean charFinaleTrovato = false;

        for (int i = 0; i < msgRec.length(); i++) {

            if (msgRec.charAt(i) == charBegin.charAt(0)) {

                charInizialeTrovato = true;

            } else if (msgRec.charAt(i) == charEnd.charAt(0)) {

                charFinaleTrovato = true;

                break;
            } else {

                result += msgRec.charAt(i);

            }
        }

        if (!charInizialeTrovato || !charFinaleTrovato) {

            result = "";

        }

        for (int i = 0; i < result.length(); i++) {

            if (!charset.contains(Character.toString(result.charAt(i)))) {
                result = "";
                break;
            }

        }

        return result;
    }

    /**
     * Validazione Stringa ricevuta comunicazione UDP
     *
     * @param msgRec
     * @param charset
     * @param charBegin
     * @param charEnd
     * @param strBegin
     * @param strBeginSecArduino
     * @return
     *
     */
    public static String ValidaStringaRicevutaUDPServer(String msgRec, String charset, String charBegin, String charEnd, String strBegin, String strBeginSecArduino) {

        String result = "";

        boolean charInizialeTrovato = false;
        boolean charFinaleTrovato = false;

        for (int i = 0; i < msgRec.length(); i++) {

            if (msgRec.charAt(i) == charBegin.charAt(0)) {

                charInizialeTrovato = true;

            } else if (msgRec.charAt(i) == charEnd.charAt(0)) {

                charFinaleTrovato = true;

                break;
            } else {

                result += msgRec.charAt(i);

            }
        }

        if (!charInizialeTrovato || !charFinaleTrovato) {

            result = "";

        }

        if (result.charAt(0) != strBegin.charAt(0) && result.charAt(0) != strBeginSecArduino.charAt(0)) {

            result = "";
        } else {
            for (int i = 1; i < result.length(); i++) {

                if (!charset.contains(Character.toString(result.charAt(i)))) {
                    result = "";
                    break;
                }

            }
        }

        return result;
    }

    /**
     * Decodifica Percorso File sql per la piattaforma Windows
     *
     * @param stringa
     * @param strToRem1
     * @param strToRem2
     * @param strToIns1
     * @param strToIns2
     * @return
     */
    public static String DecodificaPathSql(String stringa, String strToRem1, String strToRem2, String strToIns1, String strToIns2) {
        String result = "";

        for (int i = 0; i < stringa.length(); i++) {
            if (stringa.charAt(i) == strToRem1.charAt(0)) {
                result += strToIns1;
            } else {
                result += stringa.charAt(i);
            }
        }

        result = result.replaceAll(strToRem2, strToIns2);

        return result;

    }

    /**
     * Estrae il numero di lotti realizzabili con un Codice Chimica Sfusa
     *
     * @param codChimica
     * @param cSep
     * @return
     *
     */
    public static int EstraiNumChimicheCodiceSfusa(String codChimica, char cSep) {

        int res = 0;
        String nChimiche = "";
        int j = 0;
        for (int i = 0; i < codChimica.length(); i++) {

            if (j < 2) {
                if (codChimica.charAt(i) == cSep) {
                    j++;

                }
            } else {

                nChimiche += codChimica.charAt(i);
            }

        }

        if (!nChimiche.equals("")) {

            res = Integer.parseInt(nChimiche);
        }

        return res;

    }

    /**
     * Elimina tutti gli elementi dal buffer
     *
     * @param outputBuffer
     * @return
     */
    public static String[] SvuotaBuffer(String[] outputBuffer) {

        for (int i = 0; i < outputBuffer.length; i++) {

            outputBuffer[i] = "";
        }
        return outputBuffer;

    }

    /**
     * Gestore Archiviazione File di Log
     *
     *
     *
     */
    public static void GestoreArchiviazioneFileLog() {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura di Puliza Vecchi File di log");

        //////////////////////////////////
        // INDIVIDUAZIONE FILE DI LOG  ///
        //////////////////////////////////
        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Inizio Procedura di Puliza Vecchi File di log");

        //Individuazione Directory dei File
        File dir = new File(ParametriGlobali.parametri.get(53) + File.separator + ParametriGlobali.parametri.get(52));

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.CONFIG, "Directory dei file ={0}", dir);

        // Filtro File con estensione .log
        FileExtFilter fef = new FileExtFilter(ParametriGlobali.parametri.get(59));

        String[] listvoid = dir.list(fef);

        ArrayList<Date> fileLogDate = new ArrayList<>();

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.log(Level.FINE, "Numero di file Trovati ={0}", listvoid.length);

        //////////////////////////////////////
        // ELIMINAZIONE FILE DI LOG VUOTI  ///
        //////////////////////////////////////
        //Eliminazione file vuoti
        for (String listvoid1 : listvoid) {
            String fileName = ParametriGlobali.parametri.get(53) + File.separator + ParametriGlobali.parametri.get(52)
                    + File.separator + listvoid1;
            if (VerificaFileVuoto(fileName)) {
                CancellazioneFileLog(listvoid1);
            }
        }

        ///////////////////////////////////
        // RILETTURA FILE DI LOG VUOTI  ///
        ///////////////////////////////////
        String[] list = dir.list(fef);

        if (list != null) {

            //////////////////////////////////////
            // RILEVATO ALMENO UN FILE DI LOG  ///
            //////////////////////////////////////
            DateFormat formatter = new SimpleDateFormat(ParametriGlobali.parametri.get(71));

            for (String list1 : list) {
                if (list1.contains(ParametriGlobali.parametri.get(56)) || list1.contains(ParametriGlobali.parametri.get(84)) || list1.contains(ParametriGlobali.parametri.get(57)) || list1.contains(ParametriGlobali.parametri.get(88)) || list1.contains(ParametriGlobali.parametri.get(54)) || list1.contains(ParametriGlobali.parametri.get(55)) || list1.contains(ParametriGlobali.parametri.get(99)) || list1.contains(ParametriGlobali.parametri.get(100))) {
                    //////////////////////////////////////////////////////////////////////
                    // RILEVATO FILE DI LOG CONTENENTE ALMENO UNA DELLE PAROLE CHIAVE  ///
                    //////////////////////////////////////////////////////////////////////
                    String s = list1;
                    //Eliminazione parole chiave
                    s = s.replace(ParametriGlobali.parametri.get(56), "");
                    s = s.replace(ParametriGlobali.parametri.get(84), "");
                    s = s.replace(ParametriGlobali.parametri.get(57), "");
                    s = s.replace(ParametriGlobali.parametri.get(88), "");
                    s = s.replace(ParametriGlobali.parametri.get(54), "");
                    s = s.replace(ParametriGlobali.parametri.get(55), "");
                    s = s.replace(ParametriGlobali.parametri.get(99), "");
                    s = s.replace(ParametriGlobali.parametri.get(100), "");
                    ////////////////////////////////////////////////////////
                    // RECUPERO LISTA DI DATE PRESENTI NEI FILE DI LOG   ///
                    ////////////////////////////////////////////////////////
                    String temp = s;
                    for (int j = 0; j < temp.length(); j++) {

                        s = s.substring(1, s.length());

                        if (temp.charAt(j) == '-') {
                            break;
                        }

                    }
                    //Modifica 4 Settembre
                    s = s.replace(".log", "");
                    try {
                        Date date = formatter.parse(s);

                        fileLogDate.add(date);

                    } catch (ParseException ex) {

                        //Memorizzazione Logger della Sessione
                        SessionLogger.logger.log(Level.SEVERE, "Errore durante l''estrazione della data dal file di log - e={0}", ex);
                    }
                }
            }

            //Controllo date rilevate
            if (fileLogDate.size() > 0) {

                for (int i = 0; i < fileLogDate.size(); i++) {
					fileLogDate.get(i);
					boolean flag = false;
                    for (int j = 0; j < fileLogDate.size() - 1; j++) {
                        if (fileLogDate.get(j).compareTo(fileLogDate.get(j + 1)) > 0) {
                            Date d = fileLogDate.get(j);
                            fileLogDate.set(j, fileLogDate.get(j + 1));
                            fileLogDate.set(j + 1, d);
                            flag = true;
                        }

                    }

                    if (!flag) {
                        break;
                    }
				}

                //Controllo condizione avvio eleiminazione forzata
                if (list.length > Integer.parseInt(ParametriGlobali.parametri.get(95))) {

                    //////////////////////////////
                    // CANCELLAZIONE DEI FILE  ///
                    //////////////////////////////
                    //Numero di file da cancellare
                    int fileToDelete = list.length - Integer.parseInt(ParametriGlobali.parametri.get(96));

                    //Memorizzazione Logger della Sessione
                    SessionLogger.logger.config("Inizio Cancellazione");

                    int fileDeleted = 0;
                    int i = 0;

                    if (fileToDelete > fileDeleted) {

                        //Memorizzazione Logger della Sessione
                        SessionLogger.logger.log(Level.INFO, "Limite numero di file avvio cancellazione forzata ={0}{1}Numero di file di log trovati ={2}{3}Numero di file da cancellare forzatamente ={4}",
                                new Object[]{
                                    Integer.parseInt(ParametriGlobali.parametri.get(95)),
                                    LOG_CHAR_SEPARATOR,
                                    list.length,
                                    LOG_CHAR_SEPARATOR,
                                    list.length - Integer.parseInt(ParametriGlobali.parametri.get(96))});

                        //Inizio loop
                        while (fileDeleted < fileToDelete && i < list.length) {

                            //Memorizzazione Logger della Sessione
                            SessionLogger.logger.log(Level.CONFIG, "File {0} = {1}", new Object[]{i, list[i]});

                            if (list[i].contains(ParametriGlobali.parametri.get(54))
                                    || list[i].contains(ParametriGlobali.parametri.get(55))
                                    || list[i].contains(ParametriGlobali.parametri.get(56))
                                    || list[i].contains(ParametriGlobali.parametri.get(57))
                                    || list[i].contains(ParametriGlobali.parametri.get(84))
                                    || list[i].contains(ParametriGlobali.parametri.get(88))
                                    || list[i].contains(ParametriGlobali.parametri.get(99))
                                    || list[i].contains(ParametriGlobali.parametri.get(100))) {

                                //Memorizzazione Logger della Sessione
                                SessionLogger.logger.config("File Proposto per la Cancellazione");

                                for (int j = 0; j < fileToDelete; j++) {

                                    //Memorizzazione Logger della Sessione
                                    SessionLogger.logger.log(Level.CONFIG, "Verifica presenza data  ={0}", fileLogDate.get(j));

                                    if (list[i].contains(formatter.format(fileLogDate.get(j)))) {

                                        //Memorizzazione Logger della Sessione
                                        SessionLogger.logger.log(Level.CONFIG, "Cancellazione del File ={0}", list[i]);

                                        CancellazioneFileLog(list[i]);

                                        //Memorizzazione Logger della Sessione
                                        SessionLogger.logger.config("File Cancellato");

                                        fileDeleted++;

                                        break;

                                    } else {

                                        //Memorizzazione Logger della Sessione
                                        SessionLogger.logger.config("File non contenente data da cancellare");

                                    }
                                }

                            } else {

                                //Memorizzazione Logger della Sessione
                                SessionLogger.logger.config("File escluso dalla cancellazione");

                            }

                            i++;

                        }//file loop
                    }
                    //Memorizzazione Logger della Sessione
                    SessionLogger.logger.log(Level.INFO, "File Cancellati ={0}{1}Cancellazione Completata", new Object[]{fileDeleted, LOG_CHAR_SEPARATOR});

                }
            }
        }

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.config("Fine Procedura di Cancellazione File di Log");

    }

    /**
     * Conteggia il numero dei parametri definiti per componente
     *
     *
     * @return
     */
    public static int ConteggiaNumeroParametriComponenti() {

        List<?> valParComp = EseguiQuery("SELECT DISTINCT v FROM ValoreParCompOri v "
                + "GROUP BY v.idParComp");

        return valParComp.size();

    }

    /**
     * Controlla se l'idPresa è fra quelli abilitati
     *
     * @param idPresa
     * @return
     *
     */
    public static Boolean VerificaPresaAbilitata(Integer idPresa) {
        boolean res = false;

        for (int j = 0; j < TrovaPreseAbilitatePerLaMacchina().size(); j++) {

            if (TrovaPresaPerIdPresa(idPresa).equals(TrovaPreseAbilitatePerLaMacchina().get(j))) {
                res = true;
                break;
            }

        }
        return res;
    }

    /**
     * Restiuisce il numero di giorni trascorsi dall'ultimo aggiornamento
     *
     *
     * @return
     */
    public static int ConteggioGiorniUltimoAggiornamento() {

        int res = 0;
        try {
            Date dateUpdate = new Date();

            List<?> resultList = EseguiQuery("FROM AggiornamentoOri a "
                    + "ORDER BY a.dtAggiornamento");

            for (Object o : resultList) {
                AggiornamentoOri agg = (AggiornamentoOri) o;
                dateUpdate = agg.getDtAggiornamento();
            }

            //Aggiornamento e formattazione data
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat formatterAnni = new SimpleDateFormat("YYYY");
            SimpleDateFormat formatterMesi = new SimpleDateFormat("MM");
            SimpleDateFormat formatterGiorni = new SimpleDateFormat("dd");

            int anni1 = Integer.parseInt(formatterAnni.format(date));
            int anni2 = Integer.parseInt(formatterAnni.format(dateUpdate));

            int mesi1 = Integer.parseInt(formatterMesi.format(date));
            int mesi2 = Integer.parseInt(formatterMesi.format(dateUpdate));

            int giorni1 = Integer.parseInt(formatterGiorni.format(date));
            int giorni2 = Integer.parseInt(formatterGiorni.format(dateUpdate));

            //Avviso Aggiornamento 
            res = (anni1 - anni2) * 360 + (giorni1 + (mesi1 - mesi2) * 30) - giorni2;

        } catch (NumberFormatException e) {

            SessionLogger.logger.log(Level.SEVERE, "Errore durante il calcolo dei giorni trascorsi dall''ultimo aggiornamento - e:{0}", e);
        }
        return res;

    }

    /**
     * Conversione visualizzazione del peso per la gestione del sistema di
     * misura americano
     *
     * @param valoreDaConvertire
     * @param fattoreConversione
     * @return
     *
     */
    public static String ConvertiPesoVisualizzato(String valoreDaConvertire, String fattoreConversione) {

        String result = valoreDaConvertire;

        try {
            Double valoreConv = Double.parseDouble(valoreDaConvertire);
            Double fattoreConv = Double.parseDouble(fattoreConversione);
            result = Double.toString(valoreConv / fattoreConv);

            if (result.contains(("."))) {
                boolean found = false;
                int j = 0;
                String res = "";
                for (int i = 0; i < result.length(); i++) {
                    if (found) {
                        j++;
                    } else {
                        found = (result.charAt(i) == '.');
                    }
                    if (j > DECIMALI_CONVERSIONE_PESO) {
                        break;
                    } else {
                        res += result.charAt(i);
                    }
                }
                result = res;
            }
        } catch (NumberFormatException e) {
            SessionLogger.logger.log(Level.SEVERE, "Errore durante la conversione del peso Visualizzato - e:{0}", e);
        }

        return result;
    }

    /**
     * Approssimazione peso convertito
     *
     * @param valore_da_approssimare
     * @return
     */
    public static String ApprossimaPesoConvertito(String valore_da_approssimare) {

        double temp = Math.pow(10, 2);
        return Double.toString(Math.round((Double.parseDouble(valore_da_approssimare) * temp) / temp));

    }

    /**
     * Approssima valore di velocità per evitare che i parametri correttivi
     * impostabili per macchina e per prodotto diano risultati di velocità non
     * sensati e/o non utilizzabili
     *
     * @param valore
     * @return
     */
    public static int ApprossimaValoreVelInverter(int valore) {

        String valoreDaApprossimare = Integer.toString(valore / 100);
        double temp = Math.pow(10, 2);
        String res = Integer.toString((int) Math.round((Double.parseDouble(valoreDaApprossimare) * temp) / temp));
        res += "00";

        return Integer.parseInt(res);

    }

    /**
     * Riconversione peso visualizzato
     *
     * @param valoreDaConvertire
     * @param fattoreConversione
     * @return
     */
    public static String RiconvertiPesoVisualizzato(String valoreDaConvertire, String fattoreConversione) {
        String result = valoreDaConvertire;

        try {
            Double valoreConv = Double.parseDouble(valoreDaConvertire);
            Double fattoreConv = Double.parseDouble(fattoreConversione);
            result = Double.toString(valoreConv * fattoreConv);

            if (result.contains(("."))) {
                boolean found = false;
                int j = 0;
                String res = "";
                for (int i = 0; i < result.length(); i++) {
                    if (found) {
                        j++;
                    } else {
                        found = (result.charAt(i) == '.');
                    }
                    if (j > DECIMALI_CONVERSIONE_PESO) {
                        break;
                    } else {
                        res += result.charAt(i);
                    }
                }
                result = res;
            }
        } catch (NumberFormatException e) {
            SessionLogger.logger.log(Level.SEVERE, "Errore durante la conversione del peso Visualizzato - e:{0}", e);
        }

        return result;
    }

    /**
     * Adatta dato per la gestione della virgola nel peso teorico
     *
     * @param valoreDaConvertire
     * @return
     */
    public static String AdattaInfoPesoTeorico(int valoreDaConvertire) {

        String str = Integer.toString(valoreDaConvertire);
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                break;
            } else {

                result += str.charAt(i);
            }
        }
        return result;
    }

    /**
     * Restituisce l'id della macchina dalla tabella macchina_ori
     *
     *
     * @return
     */
    public static int TrovaIdMacchina() {
        int id_macchina = 0;

        List<?> resultList = EseguiQuery("SELECT v FROM MacchinaOri v ");

        for (Object o : resultList) {
            MacchinaOri macchinaOri = (MacchinaOri) o;
            id_macchina = macchinaOri.getIdMacchina();

        }
        return id_macchina;
    }

    /**
     * Restituisce la password dei file compressi dalla cartella macchina_ori
     *
     *
     * @return
     */
    public static String TrovaPasswordFileZipMacchina() {
        String result = "";

        List<?> resultList = EseguiQuery("SELECT v FROM MacchinaOri v ");

        for (Object o : resultList) {
            MacchinaOri macchinaOri = (MacchinaOri) o;
            result = macchinaOri.getZipPassword();

        }
        return result;
    }

    /**
     * Restituisce l'Utente e la password FTP dalla tabella macchina_ori
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaDettagliFTPMacchina() {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT v FROM MacchinaOri v ");

        for (Object o : resultList) {
            MacchinaOri macchinaOri = (MacchinaOri) o;
            result.add(macchinaOri.getFtpUser());
            result.add(macchinaOri.getFtpPassword());

        }
        return result;
    }

    /**
     * Restituisce gli ordini da eseguire per la data Ordierna o in un
     * specificato range di giorni
     *
     *
     * @param numGiorni
     * @return
     */
    public static ArrayList<ArrayList<ArrayList<String>>> TrovaOrdini(int numGiorni) {

        ArrayList<ArrayList<ArrayList<String>>> result = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        GregorianCalendar cal = new GregorianCalendar(Locale.ITALIAN);
        cal.add(Calendar.DATE, -numGiorni - 1);

        for (int i = 0; i < numGiorni * 2 + 1; i++) {
            cal.add(Calendar.DATE, +1);
            dates.add(cal.get(Calendar.YEAR) + "-"
                    + VerificaLunghezzaStringa(cal.get(Calendar.MONTH) + 1, 2) + "-"
                    + VerificaLunghezzaStringa(cal.get(Calendar.DATE), 2));

        }

        for (int i = 0; i < dates.size(); i++) {

            List<?> resultList = EseguiQuery("SELECT o FROM OrdineSingMacOri o WHERE o.dtProgrammata LIKE '%" + dates.get(i) + "%' AND o.abilitato=1 ORDER BY o.ordineProduzione");

            ArrayList<ArrayList<String>> record_dates = new ArrayList<>();
            ArrayList<String> record;

            if (resultList.size() > 0) {

                for (Object o : resultList) {
                    OrdineSingMacOri ordini = (OrdineSingMacOri) o;

                    record = new ArrayList<>();

                    record.add(dates.get(i)); //Date
                    record.add(Integer.toString(ordini.getIdOrdineSm())); //id
                    record.add(Integer.toString(ordini.getIdOrdine()));  //id_ordine
                    record.add(Integer.toString(ordini.getIdProdotto())); //id_prodotto
                    record.add(Integer.toString(ordini.getOrdineProduzione())); //ordine_produzione
                    record.add(Integer.toString(ordini.getNumPezzi())); //num_pezzi
                    record.add(Integer.toString(ordini.getContatore())); //contatore
                    record.add(ordini.getStato()); //stato
                    //    record.add(Boolean.toString(ordini.getAbilitato())); //abilitato
                    //    record.add(ordini.getDtProgrammata().toString());    //dt_programmata
                    //    record.add((ordini.getDtAbilitato()).toString());    //dt_abilitato
                    //    record.add(ordini.getDtProduzione().toString());     //dt_produzione
                    //    record.add(ordini.getInfo1());  //info1
                    //    record.add(ordini.getInfo2());  //info2
                    //    record.add(ordini.getInfo3());  //info3
                    //    record.add(ordini.getInfo4());  //info4
                    //    record.add(ordini.getInfo5());  //info5
                    //    record.add(ordini.getInfo6());  //info6
                    //    record.add(ordini.getInfo7());  //info7
                    //    record.add(ordini.getInfo8());  //info8
                    //    record.add(ordini.getInfo9());  //info9
                    //    record.add(ordini.getInfo10()); //info10
                    record_dates.add(record);
                }
            } else {
                record = new ArrayList<>();
                record.add(dates.get(i)); //Date
                record_dates.add(record);

            }

            result.add(record_dates);
        }

        return result;
    }

    /**
     * Restituisce il numero degli ordini non evasi
     *
     *
     * @return
     */
    public static int ConteggioOrdiniNonEvasi() {

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(TAB_ORDINI_DATE_FORMAT);
        String dataStr = sdf.format(new Date());

        List<?> resultList = EseguiQuery("SELECT o FROM OrdineSingMacOri o WHERE o.stato=0 AND o.dtProgrammata<'" + dataStr + "' AND o.abilitato=1 ORDER BY o.ordineProduzione");

        return resultList.size();

    }

    /**
     * Restituisce gli ordini non evasi
     *
     *
     * @return
     */
    public static ArrayList<ArrayList<String>> TrovaOrdiniNonEvasi() {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat(); // creo l'oggetto
        sdf.applyPattern(TAB_ORDINI_DATE_FORMAT);
        String dataStr = sdf.format(new Date()); // data corrente (20 febbraio 2014) 

        List<?> resultList = EseguiQuery("SELECT o FROM OrdineSingMacOri o WHERE o.stato=0 AND o.dtProgrammata <'" + dataStr + "' AND o.abilitato=1 ORDER BY o.ordineProduzione");

        for (Object o : resultList) {
            OrdineSingMacOri ordini = (OrdineSingMacOri) o;
            ArrayList<String> record = new ArrayList<>();
            record.add(Integer.toString(ordini.getIdOrdineSm())); //id
            record.add(Integer.toString(ordini.getIdOrdine()));  //id_ordine
            record.add(Integer.toString(ordini.getIdProdotto())); //id_prodotto
            record.add(Integer.toString(ordini.getOrdineProduzione())); //ordine_produzione
            record.add(Integer.toString(ordini.getNumPezzi())); //num_pezzi
            record.add(Integer.toString(ordini.getContatore())); //contatore
            record.add(ordini.getStato()); //stato

            result.add(record);
        }
        return result;
    }

    /**
     * Restituisce dettagli di un ordine in produzione
     *
     *
     * @param id_ordine
     * @return
     */
    public static ArrayList<String> TrovaTuttiDettagliOrdinePerIdOrdine(int id_ordine) {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParOrdineOri v WHERE v.idOrdineSm='" + id_ordine + "' AND v.abilitato=1 ORDER BY v.idParOrdine");

        for (Object o : resultList) {
            ValoreParOrdineOri valoreParOrdini = (ValoreParOrdineOri) o;
            result.add(valoreParOrdini.getValore());
        }
        return result;
    }

    /**
     * Restituisce il valore di un parametro ordine per uno specificiato
     * id_ordine
     *
     *
     * @param id_ordine
     * @param id_par_ordine
     * @return
     */
    public static String TrovavaloreParOrdinePerIdOrdinePerIdPar(int id_ordine, int id_par_ordine) {

        String result = "";

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParOrdineOri v WHERE v.idOrdineSm='" + id_ordine + "' AND v.idParOrdine='" + id_par_ordine + "' AND v.abilitato=1 ORDER BY v.idParOrdine");

        for (Object o : resultList) {
            ValoreParOrdineOri valoreParOrdini = (ValoreParOrdineOri) o;
            result = valoreParOrdini.getValore();
        }

        return result;
    }

    /**
     * Modifica stato ordine
     *
     * @param id
     * @param value
     */
    public static void AggiornaStatoOrdine(int id, String value) {

        /**
         * Aggiornamento Tabella Chimica *
         */
        OrdineSingMacOri ordini = null;

        List<?> resultList = EseguiQuery("FROM OrdineSingMacOri o "
                + "WHERE o.idOrdineSm = " + id + "");

        for (Object o : resultList) {

            ordini = (OrdineSingMacOri) o;

            //Aggiornamento Stato Chimica
            ordini.setStato(value);

            //Aggiornamento Data
            ordini.setDtProduzione(new Date());

        }

        EditEntity(ordini);

    }

    /**
     * Restituisce dettagli componenti
     *
     *
     * @return
     */
    public static ArrayList<ArrayList<String>> TrovaDettagliComponenti() {

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> componente;
        List<?> valoreParCompColl = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.valoreVariabile <> '99' "
                + "AND v.idParComp=1 "
                + "AND v.abilitato=1 "
                + "ORDER BY v.valoreVariabile");

        for (Object ob1 : valoreParCompColl) {

            componente = new ArrayList<>();
            ValoreParCompOri valoreParComp = (ValoreParCompOri) ob1;

            List<?> valoreParCompByIdColl = EseguiQuery("FROM ValoreParCompOri v "
                    + "WHERE v.idComp = '" + valoreParComp.getIdComp() + "' "
                    + "ORDER BY v.idParComp");

            componente.add(Integer.toString(valoreParComp.getIdComp())); 
            componente.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, valoreParComp.getIdComp(), ParametriSingolaMacchina.parametri.get(111))));

            for (Object ob2 : valoreParCompByIdColl) {

                ValoreParCompOri valoreParCompById = (ValoreParCompOri) ob2;

                componente.add(valoreParCompById.getValoreVariabile());
            }
             
            result.add(componente);
        }

        return result;

    }

    /**
     * Restituisce Id del movimento di magazzino della materia prima se presente
     *
     *
     * @param codice_movimento
     * @param id_materia_prima
     * @param procedura_adottata
     * @param tipo_materiale
     * @param tipo_mov
     * @param descri_mov
     * @return
     */
    public static int TrovaMovimentoMagazzinoMateriaPrima(String codice_movimento, int id_materia_prima, String tipo_materiale, String procedura_adottata, String tipo_mov, String descri_mov) {

        int result = 0;

        List<?> movimentoSingMacColl = EseguiQuery("FROM MovimentoSingMacOri m "
                + "WHERE m.idMateriale = '" + id_materia_prima + "' "
                + "AND m.codIngressoComp ='" + codice_movimento + "' "
                //+ "AND m.tipoMateriale='" + tipo_materiale + "' "
                //+ "AND m.proceduraAdottata='" + procedura_adottata + "' "
                //+ "AND m.tipoMov='" + tipo_mov + "' "
                //+ "AND m.descriMov='" + descri_mov + "' "
                + "AND m.abilitato=1");

        for (Object ob : movimentoSingMacColl) {

            MovimentoSingMacOri movimentoSingMac = (MovimentoSingMacOri) ob;

            result = movimentoSingMac.getIdMovOri();
        }

        return result;

    }

    /**
     * Restituisce Id del movimento di magazzino della materia prima se presente
     *
     *
     * @param codice_movimento
     * @param id_materia_prima
     * @param procedura_adottata
     * @param tipo_materiale
     * @param tipo_mov
     * @param descri_mov
     * @return
     */
    public static String TrovaSiloSuggeritoMovimentoMagazzinoMateriaPrima(String codice_movimento, String tipo_mov) {

        String result = "";

        List<?> movimentoSingMacColl = EseguiQuery("FROM MovimentoSingMacOri m "
                + "WHERE m.codIngressoComp ='" + codice_movimento + "' "
                +  "AND m.operazione=1 AND m.tipoMov='" + tipo_mov + "' ");
        
        for (Object ob : movimentoSingMacColl) {

            MovimentoSingMacOri movimentoSingMac = (MovimentoSingMacOri) ob;

            result = movimentoSingMac.getSilo();
        }

        return result;

    }
    
    
    
    /**
     * Registra un nuovo movimento di magazzino
     *
     *
     * @param id_materiale
     * @param tipo_materiale
     * @param qEffettivo
     * @param cod_ingresso_comp
     * @param cod_operatore
     * @param segno_op
     * @param procedura
     * @param tipo_mov
     * @param descri_mov
     * @param id_silo
     * @param qTeorico
     * @param id_ciclo
     * @return
     */
    public static int RegistraNuovoMovimentoMagazzino(
            int id_materiale,
            String tipo_materiale,
            int qEffettivo,
            String cod_ingresso_comp,
            String cod_operatore,
            String segno_op,
            String procedura,
            String tipo_mov,
            String descri_mov,
            String id_silo,
            int qTeorico,
            int id_ciclo,
            String origineMov,
            Date dataMov,
            Boolean abilitato, 
            String info1,
            String info2,
            String info3,
            String info4,
            String info5,
            String info6,
            String info7,
            String info8,
            String info9,
            String info10) {

        int result = 1;
 
        List<?> movimentoSingMacColl = EseguiQuery("FROM MovimentoSingMacOri m "
                + "WHERE m.idMovOri = (SELECT MAX (m.idMovOri) FROM m)");

        for (Object ob : movimentoSingMacColl) {

            MovimentoSingMacOri movimentoSingMac = (MovimentoSingMacOri) ob; 
            result = movimentoSingMac.getIdMovOri() + 1;
        } 
        
        //Creazione nuova Entity
        MovimentoSingMacOri movimentoOri = new MovimentoSingMacOri();
        movimentoOri.setIdMovOri(result);                       //id_mov_ori
        movimentoOri.setIdMovInephos(0);                        //id_mov_inephos
        movimentoOri.setIdMateriale(id_materiale);              //id_materiale
        movimentoOri.setTipoMateriale(tipo_materiale);          //tipo_materiale 
        movimentoOri.setQuantita(qEffettivo);                   //quantita
        movimentoOri.setCodIngressoComp(cod_ingresso_comp);     //cod_ingresso_comp 
        movimentoOri.setCodOperatore(cod_operatore);            //cod_operatore
        movimentoOri.setOperazione(segno_op);                   //operazione
        movimentoOri.setProceduraAdottata(procedura);           //procedura_adottata
        movimentoOri.setTipoMov(tipo_mov);                      //tipo_mov
        movimentoOri.setDescriMov(descri_mov);                  //descri_mov
        movimentoOri.setDtMov(dataMov);                      	//dt_mov
        movimentoOri.setSilo(id_silo);                          //silo
        movimentoOri.setOrigineMov(origineMov); //MACCHINA
        movimentoOri.setPesoTeorico(qTeorico);                  //peso_teorico
        movimentoOri.setIdCiclo(id_ciclo);                      //id_ciclo
        movimentoOri.setDtInizioProcedura(new Date());          //dt_inizio_procedura
        movimentoOri.setDtFineProcedura(new Date());            //dt_fine_procedura
        movimentoOri.setAbilitato(abilitato);                   //abilitato
        movimentoOri.setInfo1(info1);                           //info1
        movimentoOri.setInfo2(info2);                           //info2
        movimentoOri.setInfo3(info3);                           //info3
        movimentoOri.setInfo4(info4);                           //info4
        movimentoOri.setInfo5(info5);                           //info5
        movimentoOri.setInfo6(info6);                           //info6
        movimentoOri.setInfo7(info7);                           //info7
        movimentoOri.setInfo8(info8);                           //info8
        movimentoOri.setInfo9(info9);                           //info9
        movimentoOri.setInfo10(info10);                         //info10
 
        EditEntity(movimentoOri);
 
        return result;

    }

    /**
     * Modifica data fine movimento magazzino
     *
     * @param id_mov_ori
     */
    public static void AggiornaDataFineMovimentoMagazzino(int id_mov_ori) {

        MovimentoSingMacOri movimentoOri = null;

        List<?> resultList = EseguiQuery("FROM MovimentoSingMacOri o "
                + "WHERE o.idMovOri = '" + id_mov_ori + "'");

        for (Object o : resultList) {

            movimentoOri = (MovimentoSingMacOri) o;

            //Aggiornamento Data Fine Procedura
            movimentoOri.setDtFineProcedura(new Date());
            //Aggiornamento Data Ultima Modifica Movimento Magazzino
            movimentoOri.setDtMov(new Date());

        }

        EditEntity(movimentoOri);

    }

    /**
     * Modifica data fine ciclo
     *
     * @param id_ciclo
     */
    public static void AggiornaDataFineCiclo(int id_ciclo) {

        CicloOri cicloOri = null;

        List<?> resultList = EseguiQuery("FROM CicloOri c "
                + "WHERE c.idCiclo = " + id_ciclo + "");

        for (Object o : resultList) {

            cicloOri = (CicloOri) o;

            //Aggiornamento Data Fine Procedura
            cicloOri.setDtFineCiclo(new Date());
            //Aggiornamento Data Ultima Modifica Movimento Magazzino
            cicloOri.setDtAbilitato(new Date());

        }

        EditEntity(cicloOri);

    }

    /**
     * Restituisce le informazioni relative all'operatore registrato al login
     *
     * @param codice
     * @return
     */
    public static ArrayList<String> TrovaDettagliOperatore(String codice) {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM FiguraOri f "
                + "WHERE f.codice = '" + codice + "' AND f.abilitato=1");

        for (Object o : resultList) {

            FiguraOri figuraOri = (FiguraOri) o;
            result.add(Integer.toString(figuraOri.getIdFigura()));
            result.add(Integer.toString(figuraOri.getIdFiguraTipo().getIdFiguraTipo()));
            result.add(figuraOri.getIdFiguraTipo().getFigura());
            result.add(figuraOri.getNominativo());
            result.add(figuraOri.getCodice());

        }
        return result;

    }

    /**
     * Restituisce la lista dei componenti assegnati ad un silo
     *
     *
     * @return
     */
    public static ArrayList<Integer> TrovaIdComponentiAssegnati() {

        ArrayList<Integer> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idParComp=1 AND v.valoreVariabile !=" + Integer.parseInt(ParametriGlobali.parametri.get(64)) + " "
                + "AND v.abilitato =1");

        for (Object o : resultList) {
            ValoreParCompOri valoreParCompOri = (ValoreParCompOri) o;
            result.add(valoreParCompOri.getIdComp());
        }

        return result;
    }

    /**
     * Restituisce la quantita residua di un componente di magazzino
     * calcolandolo dalla tabella dei movimenti di magazzino
     *
     *
     * @param cod_ingresso_comp
     * @param id_materiale
     * @param procedura_carico
     * @param procedura_scarico_silos
     * @param procedura_scarico_micro
     * @param procedura_scarico_manuale
     * @return
     */
    public static String CalcolaQResiduaMovimento(String cod_ingresso_comp, String id_materiale, String procedura_carico, String procedura_scarico_silos, String procedura_scarico_micro, String procedura_scarico_manuale) {

        int result = 0;

        List<?> resultList = EseguiQuery("FROM MovimentoSingMacOri m "
                + "WHERE m.codIngressoComp='" + cod_ingresso_comp + "' "
                + "AND m.idMateriale='" + id_materiale + "' "
                + "AND (m.proceduraAdottata ='" + procedura_carico + "' "
                + "OR m.proceduraAdottata ='" + procedura_scarico_micro + "' "
                + "OR m.proceduraAdottata ='" + procedura_scarico_manuale + "' "
                + "OR m.proceduraAdottata ='" + procedura_scarico_silos + "') "
                + "AND m.abilitato =1 ");
 
        for (Object o : resultList) {
            MovimentoSingMacOri movCompOri = (MovimentoSingMacOri) o;
            result += movCompOri.getQuantita() * Integer.parseInt(movCompOri.getOperazione());
        }
        
		if (result > 0) {
			result = result / 1000;
		} else result = 0; 

        return Integer.toString(result);
    }

    /**
     * Registrazione procedura produzione, pulizia, carico e scarico silos
     *
     *
     * @param tipo_ciclo
     * @param id_ordine
     * @param id_prodotto
     * @param id_categoria
     * @param vel_mix
     * @param tempo_mix
     * @param numero_sacchi
     * @param numero_sacchi_aggiuntivi
     * @param vibro_attivo
     * @param aria_condotta_scarico
     * @param aria_interno_valvola
     * @param aria_pulisci_valvola
     * @param info_1
     * @param info_2
     * @param info_3
     * @param info_4
     * @param info_5
     * @param info_6
     * @param info_7
     * @param info_8
     * @param info_9
     * @param info_10
     * @param id_serie_colore
     * @param id_serie_additivo
     * @return
     */
    public static int RegistraNuovoCiclo(
            String tipo_ciclo,
            int id_ordine,
            int id_prodotto,
            int id_categoria,
            int vel_mix,
            int tempo_mix,
            int numero_sacchi,
            int numero_sacchi_aggiuntivi,
            String vibro_attivo,
            String aria_condotta_scarico,
            String aria_interno_valvola,
            String aria_pulisci_valvola,
            String info_1,
            String info_2,
            String info_3,
            String info_4,
            String info_5,
            String info_6,
            String info_7,
            String info_8,
            String info_9,
            String info_10,
            String id_serie_colore,
            String id_serie_additivo) {

        //Creazione nuova Entity
        CicloOri cicloOri = new CicloOri();

        //cicloOri.setIdCiclo();                                        //id_ciclo
        cicloOri.setTipoCiclo(tipo_ciclo);                              //tipo_ciclo
        cicloOri.setDtInizioCiclo(new Date());                          //dt_inizio_ciclo
        cicloOri.setDtFineCiclo(new Date());                            //dt_fine_ciclo
        cicloOri.setIdOrdine(id_ordine);                                //id_ordine
        cicloOri.setIdProdotto(id_prodotto);                            //id_prodotto
        cicloOri.setIdCat(id_categoria);                                //id_cat
        cicloOri.setVelocitaMix(vel_mix);                               //velocita_mix
        cicloOri.setTempoMix(tempo_mix);                                //tempo_mix
        cicloOri.setNumSacchi(numero_sacchi);                           //num_sacchi
        cicloOri.setNumSacchiAggiuntivi(numero_sacchi_aggiuntivi);      //num_sacchi_aggiuntivi
        cicloOri.setVibroAttivo(vibro_attivo);                          //vibro_attivo
        cicloOri.setAriaCondScarico(aria_condotta_scarico);             //aria_cond_scarico
        cicloOri.setAriaInternoValvola(aria_interno_valvola);           //aria_interno_valvola
        cicloOri.setAriaPulisciValvola(aria_pulisci_valvola);           //aria_pulisci_valvola
        cicloOri.setDtAbilitato(new Date());                            //dt_abilitato
        cicloOri.setIdSerieColore(id_serie_colore);                     //serie_colore
        cicloOri.setIdSerieAdditivo(id_serie_additivo);                 //serie_additivo
        cicloOri.setInfo1(info_1);                                      //info1
        cicloOri.setInfo2(info_2);                                      //info2
        cicloOri.setInfo3(info_3);                                      //info3
        cicloOri.setInfo4(info_4);                                      //info4
        cicloOri.setInfo5(info_5);                                      //info5
        cicloOri.setInfo6(info_6);                                      //info6
        cicloOri.setInfo7(info_7);                                      //info7
        cicloOri.setInfo8(info_8);                                      //info8
        cicloOri.setInfo9(info_9);                                      //info9
        cicloOri.setInfo10(info_10);                                    //info10

        EditEntity(cicloOri);

        int result = 0;

        List<?> cicloOriColl = EseguiQuery("FROM CicloOri c "
                + "WHERE c.idCiclo = (SELECT MAX (c.idCiclo) FROM c)");

        for (Object ob : cicloOriColl) {

            CicloOri cicloOriidMax = (CicloOri) ob;

            result = cicloOriidMax.getIdCiclo();
        }

        return result;

    }

    /**
     * Restituisce la lista della prese non assegnate
     *
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaPreseAbilitateNonAssegnate() {

        ArrayList<Integer> result = new ArrayList<>();

        List<?> resultList1 = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idParComp='1' "
                + "AND v.valoreVariabile !='" + ParametriGlobali.parametri.get(64) + "' "
                + "AND v.valoreVariabile !='" + ParametriGlobali.parametri.get(134) + "' "
                + "AND v.abilitato =1 ");

        for (Object o : resultList1) {
            ValoreParCompOri valoreParCompOri = (ValoreParCompOri) o;
            result.add(Integer.parseInt(valoreParCompOri.getValoreVariabile()));
        }

        ArrayList<String> prese_result = new ArrayList<>();

        List<?> resultList2 = EseguiQuery("FROM PresaOri p "
                + "WHERE p.idPresa!=" + Integer.parseInt(ParametriGlobali.parametri.get(64)) + " "
                //+ "AND p.idPresa!=" + Integer.parseInt(ParametriGlobali.parametri.get(134)) + " "
                + "AND p.abilitato=1");

        ArrayList<String> prese = TrovaPreseAbilitatePerLaMacchina();

        for (Object o : resultList2) {
            PresaOri presaRipOri = (PresaOri) o;
            for (int i = 0; i < prese.size(); i++) {
                if (presaRipOri.getPresa().equals(prese.get(i))) {

                    boolean presaAssegnata = false;
                    for (int j = 0; j < result.size(); j++) {

                        if (presaRipOri.getPresa().equals(TrovaPresaPerIdPresa(result.get(j)))) {
                            presaAssegnata = true;
                            break;
                        }
                    }

                    if (!presaAssegnata) {
                        prese_result.add(presaRipOri.getPresa());
                    }
                    break;
                }
            }
        }

        return prese_result;

    }

    /**
     * Registrazione allarme
     *
     *
     * @param id_allarme
     * @param valore
     * @param id_tabella_rif
     * @param info1
     * @param info2
     * @param info3
     * @param info4
     * @param info5
     */
    public static void RegistraAllarme(
            int id_allarme,
            String valore,
            String id_tabella_rif,
            String info1,
            String info2,
            String info3,
            String info4,
            String info5) {

        //Creazione nuova Entity
        ValoreAllarmeOri valoreOri = new ValoreAllarmeOri();
        // AllarmeOri allarmeOri = new AllarmeOri(id_allarme);

        valoreOri.setIdAllarme(id_allarme);//id_allarme
        valoreOri.setValore(valore);//valore
        valoreOri.setIdTabellaRif(Integer.parseInt(id_tabella_rif));//id_tabella_rif
        valoreOri.setAbilitato(true);//abilitato
        valoreOri.setDtAbilitato(new Date());//dt_abilitato
        valoreOri.setInfo1(info1);//info1
        valoreOri.setInfo2(info2);//info2
        valoreOri.setInfo3(info3);//info3
        valoreOri.setInfo4(info4);//info4
        valoreOri.setInfo5(info5);//info5

        EditEntity(valoreOri);

    }

    /**
     * Codifica il buffer in uscita per il thread ThreadComunicazioniUDP
     *
     *
     *
     * @param output
     * @param strToControl
     * @return
     */
    public static boolean[] CodificaOutputBuffer(boolean[] output, String strToControl) {

        SchedaIOLogger.logger.log(Level.FINE, "strToControl :{0}", strToControl);

        //Inizio Loop
        while (strToControl.length() > 0) {

            int indice = Integer.parseInt(strToControl.substring(0, 2));

            boolean valore = strToControl.substring(2, 3).equals("t");

            output[indice] = valore;

            strToControl = strToControl.substring(3, strToControl.length());

        } //fine loop

        String s = "";
        for (int i = 0; i < output.length; i++) {

            s += output[i] + ",";

        }

        SchedaIOLogger.logger.log(Level.FINE, "Array Uscite :{0}", s);

        return output;

    }

    /**
     * Controllo del peso ottenuto per un determinato componente a fine dosaggio
     *
     *
     *
     * @param peso_desiderato
     * @param tolleranza_difetto
     * @param peso_attuale
     * @param tolleranza_eccesso
     * @param nome_componente
     */
    public static void ControlloTolleranzediPesoComponentiProcesso(
            double peso_desiderato,
            String peso_attuale,
            int tolleranza_eccesso,
            int tolleranza_difetto,
            String nome_componente) {

        int errore = Integer.parseInt(peso_attuale) - (int) peso_desiderato;

        if (errore >= 0 && errore > tolleranza_eccesso) {

            //////////////////////
            // PESO ECCESSIVO  ///
            //////////////////////
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                RegistraAllarme(3,
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 908, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + EstraiStringaHtml(nome_componente)
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 909, ParametriSingolaMacchina.parametri.get(111)))
                        + " = " + (errore - tolleranza_eccesso),
                        TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
            }

        } else if (Math.abs(errore) > tolleranza_difetto) {

            ///////////////////////
            // PESO IN DIFETTO  ///
            ///////////////////////
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                RegistraAllarme(3,
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 910, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + EstraiStringaHtml(nome_componente)
                        + " - "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 909, ParametriSingolaMacchina.parametri.get(111)))
                        + " = " + (errore - tolleranza_difetto),
                        TrovaSingoloValoreParametroRipristino(91), "", "", "", "", "");
            }
        }

    }

    public static ArrayList<ComponenteProdotto> LetturaDettagliComponenti() {

        ArrayList<ComponenteProdotto> componenti = new ArrayList<>();

        //Lettura Informazioni dalla Tabella di Ripristino
        ArrayList<String> array_nome = TrovaGruppoValoreParametroRipristino(18);
        ArrayList<String> array_id = TrovaGruppoValoreParametroRipristino(16);
        ArrayList<String> array_presa = TrovaGruppoValoreParametroRipristino(21);
        ArrayList<String> array_quantità = TrovaGruppoValoreParametroRipristino(19);
        ArrayList<String> array_correttivi_velocità = TrovaGruppoValoreParametroRipristino(22);
        ArrayList<String> array_volo = TrovaGruppoValoreParametroRipristino(23);
        ArrayList<String> array_tolleranze_difetto = TrovaGruppoValoreParametroRipristino(84);
        ArrayList<String> array_tolleranze_eccesso = TrovaGruppoValoreParametroRipristino(85);
        ArrayList<String> array_quantità_stop_mescola = TrovaGruppoValoreParametroRipristino(81);
        ArrayList<String> array_quantità_stop_vibro = TrovaGruppoValoreParametroRipristino(97);
        ArrayList<String> array_seconda_velocita = TrovaGruppoValoreParametroRipristino(24);
        ArrayList<String> array_curve_dosatura = TrovaGruppoValoreParametroRipristino(70);
        ArrayList<String> array_metodi_pesa = TrovaGruppoValoreParametroRipristino(86);
        ArrayList<String> array_fluificazione = TrovaGruppoValoreParametroRipristino(87);
        ArrayList<String> array_valori_fluidificazione = TrovaGruppoValoreParametroRipristino(88);
        ArrayList<String> array_oridne_dosaggio = TrovaGruppoValoreParametroRipristino(89);
        ArrayList<String> array_step_dosaggio = TrovaGruppoValoreParametroRipristino(90);

        for (int i = 0; i < Integer.parseInt(TrovaSingoloValoreParametroRipristino(15)); i++) {

            ComponenteProdotto componente = new ComponenteProdotto();

            componente.setNome(array_nome.get(i)); //Nome del Componente
            componente.setId(Integer.parseInt(array_id.get(i))); //Id del Componente
            componente.setPresa(array_presa.get(i)); //Presa del Componente
            componente.setQuantità((int) Double.parseDouble(array_quantità.get(i))); //Quantita componente prevista nella formula
            componente.setCorrettivoVelocità(array_correttivi_velocità.get(i)); //Correttivo Velocità di Carico
            componente.setVolo(Integer.parseInt(array_volo.get(i))); //Volo Componente
            componente.setTolleranzaDifetto(Integer.parseInt(array_tolleranze_difetto.get(i))); //Tolleranza in difetto sul dosaggio
            componente.setTolleranzaEccesso(Integer.parseInt(array_tolleranze_eccesso.get(i))); //Tolleranza in eccesso sul dosaggio 
            componente.setQuantitàStopMescolatore(Integer.parseInt(array_quantità_stop_mescola.get(i)));//Quantita stop mescola microdosatori 
            componente.setQuantitàStopVibro(Integer.parseInt(array_quantità_stop_vibro.get(i)));//Quantita stop vibro microdosatori 
            componente.setQuantitàSecondaVelocitàInverter(Integer.parseInt(array_seconda_velocita.get(i))); //Intervento seconda velocià inverter origami 4
            componente.setCurvaDosatura(array_curve_dosatura.get(i)); //Curva dosatura
            componente.setMetodoPesa(array_metodi_pesa.get(i)); //Metoto di pesa SILOS o MANUALE
            componente.setFluidificazione(Boolean.parseBoolean(array_fluificazione.get(i))); //Abilitazione - Disabilitazione Fluidificazione 
            componente.setValoreResiduoFluidificazione(Integer.parseInt(array_valori_fluidificazione.get(i))); //Valore Residuo Fluidificazione
            componente.setOrdine_dosaggio(Integer.parseInt(array_oridne_dosaggio.get(i))); //Ordine di dosaggio
            componente.setStepDosaggio(Integer.parseInt(array_step_dosaggio.get(i)));  //Step di dosaggio

            componente.setCodiceMovimentoMagazzino(TrovaCodiceMovimentoMagazzinoPerIdComp(Integer.parseInt(array_id.get(i))));

            componenti.add(componente);
        }

////        if (Boolean.parseBoolean(Benefit.findSingoloValoreParametroRipristino(14))
////                && Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(461))) {
////
////            ComponenteProdotto componente = new ComponenteProdotto();
////
////            componente.setNome(Benefit.estraiStringaHtml(Benefit.findVocabolo(
////                    FabCloudConstants.ID_DIZIONARIO_COMPONENTI,
////                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)),
////                    ParametriSingolaMacchina.parametri.get(111))));  //Nome del Componente
////            componente.setId(Integer.parseInt(ParametriSingolaMacchina.parametri.get(301))); //Id del Componente
////            componente.setPresa(Benefit.findPresaById(Integer.parseInt(ParametriGlobali.parametri.get(134)))); //Presa del Componente
////            componente.setQuantità(0); //Quantita componente prevista nella formula
////            componente.setCorrettivoVelocità("1.0"); //Correttivo Velocità di Carico
////            componente.setVolo(0); //Volo Componente 
////            componente.setTolleranzaDifetto(0); //Tolleranza in difetto sul dosaggio
////            componente.setTolleranzaEccesso(0); //Tolleranza in eccesso sul dosaggio 
////            componente.setQuantitàStopMescolatore(0);//Quantita stop mescola microdosatori 
////            componente.setQuantitàStopVibro(0);//Quantita stop vibro microdosatori 
////            componente.setQuantitàSecondaVelocitàInverter(0); //Intervento seconda velocià inverter origami 4
////            componente.setCurvaDosatura("0/0!.0!"); //Curva dosatura 
////            componente.setMetodoPesa(ParametriGlobali.parametri.get(131)); //Metoto di pesa SILOS o MANUALE 
////            componente.setFluidificazione(false); //Abilitazione - Disabilitazione Fluidificazione 
////            componente.setValoreResiduoFluidificazione(0); //Valore Residuo Fluidificazione
////            componente.setOrdine_dosaggio(0); //Ordine di dosaggio
////            componente.setStepDosaggio(0);  //Step di dosaggio 
////            componente.setCodiceMovimentoMagazzino("");
////
////            componenti.add(componente);
////
////        }
        return componenti;

    }

    /**
     * Restituisce i parametri associati al prodotto
     *
     * @param id_prodotto
     * @param id_par
     * @return
     *
     *
     */
    public static String TrovaValoreParametroProdottoPerIdProd(int id_prodotto, int id_par) {

        String result = "";

        List<?> resultList = EseguiQuery("FROM ValoreProdottoOri v "
                + "WHERE v.idProdotto='" + id_prodotto + "' "
                + "AND v.idParProd ='" + id_par + "' "
                + "AND v.abilitato =1 ORDER BY v.idParProd");

        for (Object o : resultList) {
            ValoreProdottoOri valProdottoOri = (ValoreProdottoOri) o;
            result = valProdottoOri.getValoreVariabile();
        }

        return result;
    }

    /**
     * Restituisce il codice del movimento associato al Compoennte
     *
     * @param id_comp
     * @return
     *
     *
     */
    public static String TrovaCodiceMovimentoMagazzinoPerIdComp(int id_comp) {

        String result = "";

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idComp='" + id_comp + "' "
                + "AND v.abilitato =1 AND idParComp=6");

        for (Object o : resultList) {
            ValoreParCompOri valParCompOri = (ValoreParCompOri) o;
            result = valParCompOri.getValoreVariabile();
        }

        return result;
    }

    /**
     * Restituisce l'id dell'ultimo processo inserito
     *
     * @return int
     *
     *
     */
    public static int TrovaMaxIdProcesso() {

        int result = 0;
        List<?> processoColl = EseguiQuery("SELECT p FROM ProcessoOri p WHERE p.idProcesso = (SELECT MAX(p.idProcesso) FROM ProcessoOri p))");

        for (Object o : processoColl) {
            ProcessoOri procColl = (ProcessoOri) o;
            result = procColl.getIdProcesso();
        }

        return result;

    }

    /**
     * Registra l'informazioni relative all'ultimo ciclo eseguito
     *
     * @param idCiclo
     * @param data_inizio
     */
    public static void RegistraCiclo(int idCiclo, String data_inizio) {

        int maxIdProcesso = TrovaMaxIdProcesso();

        CicloProcessoOri cicloProcessoOri = new CicloProcessoOri();

        cicloProcessoOri.setIdCiclo(idCiclo);
        cicloProcessoOri.setIdProcesso(maxIdProcesso);
        cicloProcessoOri.setDtAbilitato(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat(ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT);
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(data_inizio);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            cicloProcessoOri.setDtInizioProcesso(timestamp);
        } catch (ParseException ex) {
            cicloProcessoOri.setDtInizioProcesso(new Date());
        }
        cicloProcessoOri.setDtFineProcesso(new Date());
        cicloProcessoOri.setInfo1("");
        cicloProcessoOri.setInfo2("");
        cicloProcessoOri.setInfo3("");
        cicloProcessoOri.setInfo4("");
        cicloProcessoOri.setInfo5("");
        cicloProcessoOri.setInfo6("");
        cicloProcessoOri.setInfo7("");
        cicloProcessoOri.setInfo8("");
        cicloProcessoOri.setInfo9("");
        cicloProcessoOri.setInfo10("");

        EditEntity(cicloProcessoOri);
    }

    /**
     * Registra le informazioni relative all'ultimo processo eseguito
     *
     *
     * @param processo
     */
    public static void RegistrazioneProcesso(Processo processo) {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Inizio Procedura Registrazione Processo");

        try {

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso) {

                //Creazione nuova Entity
                ProcessoOri processoOri = new ProcessoOri();

                //Cliente Associato alla Produzione
                processoOri.setCliente(TrovaSingoloValoreParametroRipristino(6));

                //Codice Chimica
                String codiceChimica = TrovaSingoloValoreParametroRipristino(58);

                if (Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(14))) {

                    //Correzione 5/03/2014
                    if (processo.codChimicaSfusa.equals("")) {

                        processo.codChimicaSfusa = codiceChimica
                                + ParametriGlobali.parametri.get(20)
                                + (TrovaMaxMisceleTabellaProcessoPerCodChimica(codiceChimica) + 1);

                    }

                    codiceChimica = processo.codChimicaSfusa;

                }

                processoOri.setCodChimica(codiceChimica);

                //Registra Operatore
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(208))) {

                    processoOri.setCodOperatore(TrovaSingoloValoreParametroRipristino(74));
                }

                //Registra Magazzino Componenti
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(209))) {

                    processoOri.setCodCompIn(TrovaSingoloValoreParametroRipristino(75));

                } else {
                    processoOri.setCodCompIn("");
                }

                //Codice Colore - NON UTILIZZATO
                ///////////////////////////////
                // NESSUN COLORE ASSOCIATO  ///
                //////////////////////////////
                processoOri.setCodColore("");

                //Formula Effettiva Miscela Corrente
                processoOri.setCodCompPeso(TrovaSingoloValoreParametroRipristino(29));

                //Codice Prodotto
                processoOri.setCodProdotto(TrovaCodiceProdottoPerIdProdotto(processo.prodotto.getId()));

                //Codice Sacchetto
                processoOri.setCodSacco(TrovaSingoloValoreParametroRipristino(47));

                //Data di Produzione
                processoOri.setDtProduzione(new Date());

                try {
                    //Peso Reale Sacchetto
                    processoOri.setPesoRealeSacco(Integer.parseInt(processo.valorePesaConfezioni));
                } catch (NumberFormatException ex) {

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.log(Level.SEVERE, "Registrazione Processo - Errore conversione valore peso confezioni =" + ex);

                    processoOri.setPesoRealeSacco(0);

                }
                
                //Tipo Processo
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {

                    ///////////////////////////////////////////////
                    // REGISTRAZIONE INFO AGGIUNTIVE ABILITATA  ///
                    ///////////////////////////////////////////////
                    //TIpo Processo
                    processoOri.setTipoProcesso(TIPO_PROCESSO_PRODUZIONE);

                    try {
                        //Info1Id Ciclo
                        processoOri.setInfo1(TrovaSingoloValoreParametroRipristino(79));
                    } catch (Exception e) {
                        ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante la memorizzazione del processo - Info 1 - e:{0}", e);

                    }

                    try {
                        String valueVibroAttivo = "0";
                        String valueAbilitaCondottoScarico = "0";
                        String valueAbilitaAriaInternoValvola = "0";
                        String valueAbilitaAriaPulisciValvola = "0";

                        //Controllo Abilita Vibro
                        if (processo.prodotto.isVibroAttivo()) {
                            valueVibroAttivo = "1";

                        }
                        //Controllo Abilitazione Aria Condotto Scarico
                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(318))) {

                            valueAbilitaCondottoScarico = "1";

                        }
                        //Controllo Abilitazione Aria Interno Valvola 
                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(319))) {

                            valueAbilitaAriaInternoValvola = "1";
                        }
                        //Controllo Abilitazione Aria Pulisci Valvola
                        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(321))) {

                            valueAbilitaAriaPulisciValvola = "1";
                        }

                        //Info2 - Id Categoria - numero_totale_sacchetti - velocita miscelazione - tempo miscelazione - vibro attivo - aria attiva
                        String infoMix
                                = processo.prodotto.getIdCategoria() //Categoria
                                + ParametriGlobali.parametri.get(112)
                                + TrovaSingoloValoreParametroRipristino(9) //Numero sacchi per miscela
                                + ParametriGlobali.parametri.get(112)
                                + processo.prodotto.getMixingSpeed() //Velocita Miscelazione
                                + ParametriGlobali.parametri.get(112)
                                + processo.prodotto.getMixingTime()//Tempo Miscelazione
                                + ParametriGlobali.parametri.get(112)
                                + valueVibroAttivo //Vibro Attivo
                                + ParametriGlobali.parametri.get(112)
                                + valueAbilitaCondottoScarico //Abilitazione Aria Condotto Scarico
                                + ParametriGlobali.parametri.get(112)
                                + valueAbilitaAriaInternoValvola //Abilitazione Aria Interno Valvola
                                + ParametriGlobali.parametri.get(112)
                                + valueAbilitaAriaPulisciValvola;   //Abilitazione Aria Pulisci Valvola

                        processoOri.setInfo2(infoMix);

                    } catch (Exception e) {

                        processoOri.setInfo2("");
                        ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante la memorizzazione del processo - Info 2 - e:{0}", e);

                    }
                    
                    try {
                        //info3 - Id Num Sacchetto - peso teorico - errore
                        String infoSacco
                                = processo.indiceConfezioneInPesa
                                + ParametriGlobali.parametri.get(113)
                                + TrovaSingoloValoreParametroRipristino(10)
                                + ParametriGlobali.parametri.get(113)
                                + Integer.toString(Integer.parseInt(processo.valorePesaConfezioni)
                                        - Integer.parseInt(TrovaSingoloValoreParametroRipristino(10)));

                        processoOri.setInfo3(infoSacco);
                        
                    } catch (NumberFormatException e) {

                        processoOri.setInfo3("");
                        ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante la memorizzazione del processo - Info 3 - e:{0}", e);

                    }

                    try {
                        //info4 - Formula Teorica
                        processoOri.setInfo4(TrovaSingoloValoreParametroRipristino(80));

                    } catch (Exception e) {
                        processoOri.setInfo4("");
                        ProcessoLogger.logger.log(Level.SEVERE, "Errore Durante la memorizzazione del processo - Info 4 - e:{0}", e);

                    }
                    //Inizializzazione
                    processoOri.setInfo5("");
                    processoOri.setInfo6("");
                    
                    
					try {

						if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(512))) {
							// ALIAS CODICE PRODOTTO
							processoOri.setInfo5(TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(
									processo.prodotto.getId(), 4));

							// ALIAS NOME PRODOTTO
							processoOri.setInfo6(TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(
									processo.prodotto.getId(), 5));
						}
					} catch (Exception ex) {
						ProcessoLogger.logger
								.severe("Errore durante la registrazione degli alias nome e codice prodotto");
					}
                   
                    //NON UTILIZZATI
                    processoOri.setInfo7("");
                    processoOri.setInfo8("");
                    processoOri.setInfo9("");
                    processoOri.setInfo10("");

                }

                EditEntity(processoOri);

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Registrazione Processo - Eseguita con Successo");

                ////////////////////////////////////////////////
                // AGGIORNAMENTO TABELLA CICLO_PROCESSO_ORI  ///
                ////////////////////////////////////////////////
                try {
                    //Registrazione Associazione Ciclo Processo
                    RegistraCiclo(Integer.parseInt(TrovaSingoloValoreParametroRipristino(83)), TrovaSingoloValoreParametroRipristino(93));
                } catch (NumberFormatException ex) {

                    //Memorizzazione Log Processo
                    ProcessoLogger.logger.log(Level.SEVERE, "Registrazione Ciclo Processo - Fallita - Errore e ={0}", ex);

                }
                ////////////////////////////////////////////////
                // AGGIORNAMENTO TABELLA ORDINI  ///
                ////////////////////////////////////////////////
                //Registrazione Associazione Ciclo Processo
                AggiornaContatoreTabellaOrdine(TrovaSingoloValoreParametroRipristino(96));

            }

        } catch (Exception ex) {

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso) {

                ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(10);
            }

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Registrazione Processo - Fallita - Errore e ={0}", ex);

        }

        ////////////////////////////////////////////////
        // MEMORIZZAZIONE QUANTITA PESATA SACCHETTI  ///
        ////////////////////////////////////////////////
        try {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.config("Aggiornamento Peso Totale Sacchetti Pesati");

            //Indice Pesa Componenti Completata
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(72),
                    72,
                    Integer.toString(Integer.parseInt(processo.valorePesaConfezioni)
                            + Integer.parseInt(TrovaSingoloValoreParametroRipristino(72))),
                    ParametriSingolaMacchina.parametri.get(15));

        } catch (NumberFormatException ex) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Aggiornamento Peso Totale Sacchetti Pesati Fallito - Errore e ={0}", ex);

        }

        try {

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso) {

                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Memorizzazione Pesa Sacchetti Completata");

                //Reset Indice Sacchetto Bloccato
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(45),
                        45,
                        "0",
                        ParametriSingolaMacchina.parametri.get(15));

                //Reset Indice Lettura Sacchetto Eseguita
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(46),
                        46,
                        "false",
                        ParametriSingolaMacchina.parametri.get(15));

                if (!Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(441))) {
                    //Reset Codice Sacchetto
                    AggiornaValoreParametriRipristino(processo,
                            TrovaIndiceTabellaRipristinoPerIdParRipristino(47),
                            47,
                            "",
                            ParametriSingolaMacchina.parametri.get(15));
                }
                //Azzeramento Tempo Riapertura Valvola
                AggiornaValoreParametriRipristino(processo,
                        TrovaIndiceTabellaRipristinoPerIdParRipristino(48),
                        48,
                        "0",
                        ParametriSingolaMacchina.parametri.get(15));

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Memorizzazione Pesa Sacchetti Completata Eseguita con Successo");
            }

        } catch (Exception ex) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Errore Memorizzazione Pesa Sacchetti Completata e ={0}", ex);

        }

        //Analisi errore di pesatura
        //Peso Reale Sacchetto
        if (Integer.parseInt(processo.valorePesaConfezioni)
                >= Integer.parseInt(TrovaSingoloValoreParametroRipristino(10))
                && (Integer.parseInt(processo.valorePesaConfezioni) - Integer.parseInt(TrovaSingoloValoreParametroRipristino(10)) > processo.prodotto.getTolleranzaEccesso())) {

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                //////////////////////////////
                // ERRORE PESO IN ECCESSO  ///
                //////////////////////////////
                RegistraAllarme(3,
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 911, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + (Integer.parseInt(processo.valorePesaConfezioni) - Integer.parseInt(TrovaSingoloValoreParametroRipristino(10)))
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 912, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + processo.indiceConfezioneInPesa,
                        TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
            }

        } else if (Integer.parseInt(processo.valorePesaConfezioni)
                < Integer.parseInt(TrovaSingoloValoreParametroRipristino(10))
                && (Integer.parseInt(TrovaSingoloValoreParametroRipristino(10)) - Integer.parseInt(processo.valorePesaConfezioni) > processo.prodotto.getTolleranzaDifetto())) {

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(411))) {
                //////////////////////////////
                // ERRORE PESO IN DIFETTO  ///
                //////////////////////////////
                RegistraAllarme(3,
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 913, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + (Integer.parseInt(TrovaSingoloValoreParametroRipristino(10)) - Integer.parseInt(processo.valorePesaConfezioni))
                        + " "
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 912, ParametriSingolaMacchina.parametri.get(111)))
                        + " "
                        + processo.indiceConfezioneInPesa,
                        TrovaSingoloValoreParametroRipristino(83), "", "", "", "", "");
            }

        }

        try {

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso) {

                //Memorizzazione Log Processo
                ProcessoLogger.logger.config("Incremento Id Processo in Corso");

                AggiornaValoreParSingMacOri(15,
                        Integer.toString(
                                Integer.parseInt((ParametriSingolaMacchina.parametri.get(15))) + 1));

                //Memorizzazione Log Processo
                ProcessoLogger.logger.info("Incremento Id Processo In Corso - Eseguito con Successo");

            }
        } catch (NumberFormatException ex) {

            if (processo.pannelloProcesso.isVisible()
                    && !processo.resetProcesso) {

                ((Pannello44_Errori) processo.pannelloProcesso.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(12);

            }

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Incremento Id Processo In Corso - Fallito e ={0}", ex);

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config(
                "Fine Procedura Registrazione Processo");

    }

    /**
     * Aggiorna il numero di confezioni prodotto per determinato ordineordine
     *
     *
     * @param id_ordine
     */
    public static void AggiornaContatoreTabellaOrdine(String id_ordine) {

        ///////////////////////////////////
        // Aggiornamento Tabella Ordini ///
        ///////////////////////////////////
        List<?> resultList = EseguiQuery("FROM OrdineSingMacOri o "
                + "WHERE o.idOrdineSm = " + id_ordine + "");

        for (Object o : resultList) {

            OrdineSingMacOri ordini = (OrdineSingMacOri) o;

            //Aggiornamento Stato Chimica
            ordini.setContatore(ordini.getContatore() + 1);
            // Aggiornamento Data Odierna
            ordini.setDtProduzione(new Date());

            if (Objects.equals(ordini.getContatore(), ordini.getNumPezzi())) {
                ordini.setStato("1");
            }

            EditEntity(ordini);
        }

    }

    /**
     * Aggiorna dati di relativi ad un movimento di magazzino in corso
     *
     * @param id_mov_ori
     * @param peso
     */
    public static void AggiornaDatiMovimentoMagazzino(int id_mov_ori, int peso) {

        MovimentoSingMacOri movimentoOri = null;

        List<?> resultList = EseguiQuery("FROM MovimentoSingMacOri o "
                + "WHERE o.idMovOri = " + id_mov_ori + "");

        for (Object o : resultList) {

            movimentoOri = (MovimentoSingMacOri) o;

            //Aggiornamento peso effettivo
            movimentoOri.setQuantita(peso);
            //Aggiornamento Data Fine Procedura
            movimentoOri.setDtFineProcedura(new Date());
            //Aggiornamento Data Ultima Modifica Movimento Magazzino
            movimentoOri.setDtMov(new Date());

        }

        EditEntity(movimentoOri);

    }

    /**
     * Restituisce il codice di un componente identificato da idComp
     *
     * @param idComp
     * @return
     */
    public static String TrovaCodiceComponentePerIdComp(int idComp) {

        String result = "";
        List<?> componenteColl = EseguiQuery("SELECT c FROM ComponenteOri c WHERE c.idComp = '" + idComp + "'");

        for (Object o : componenteColl) {
            ComponenteOri procColl = (ComponenteOri) o;
            result = procColl.getCodComponente();
        }

        return result;

    }

    /**
     * Registrazione pesatura componente eseguita tramite microdosatore
     *
     *
     * @param processo
     * @param id_comp
     * @param peso
     * @param id_componente_in_peso
     */
    public static void RegistraPesaturaComponenteMicrodosatura(Processo processo, int id_comp, int peso, int id_componente_in_peso) {

        //Memorizzazione Log Processo
        ProcessoLogger.logger.log(Level.INFO, "Registrazione Microdosatura Componente{0} Completata", id_comp);

        try {

            //Costruzione e Memorizzazione Formula Effettiva
            String formEffettiva = TrovaSingoloValoreParametroRipristino(28);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {
                /////////////////////////////////////////////////
                // REGISTRAZIONE INFO AGGIUNTIVE ABILITITATA  ///
                /////////////////////////////////////////////////

                if (formEffettiva.equals("")) {
                    formEffettiva = formEffettiva
                            + id_comp
                            + ParametriGlobali.parametri.get(22)
                            + AdattaInfoPesoTeorico(peso) //"1200"
                            + ParametriGlobali.parametri.get(22)
                            + AdattaInfoPesoTeorico(processo.componenti.get(id_componente_in_peso).getQuantità());
                } else {
                    formEffettiva = formEffettiva
                            + ParametriGlobali.parametri.get(21)
                            + id_comp
                            + ParametriGlobali.parametri.get(22)
                            + AdattaInfoPesoTeorico(peso) //"1200"
                            + ParametriGlobali.parametri.get(22)
                            + AdattaInfoPesoTeorico(processo.componenti.get(id_componente_in_peso).getQuantità());
                }
            } else {

                /////////////////////////////////////////////////////
                // REGISTRAZIONE INFO AGGIUNTIVE NON ABILITITATA  ///
                /////////////////////////////////////////////////////
                if (formEffettiva.equals("")) {
                    formEffettiva = formEffettiva
                            + id_comp
                            + ParametriGlobali.parametri.get(22)
                            + peso; //"1222";
                } else {
                    formEffettiva = formEffettiva
                            + ParametriGlobali.parametri.get(21)
                            + id_comp
                            + ParametriGlobali.parametri.get(22)
                            + peso;//"1222";
                }
            }

            //Aggiornamento Record Formula Effettiva
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(28),
                    28,
                    formEffettiva,
                    ParametriSingolaMacchina.parametri.get(15));

            //Aggiornamento dei Parametri Ripristino Pesa Componenti
            AggiornaValoreParametriRipristino(processo,
                    TrovaIndiceTabellaRipristinoPerIdParRipristino(25)
                    + id_componente_in_peso,
                    25,
                    "true",
                    ParametriSingolaMacchina.parametri.get(15));

            //Memorizzazione Log Processo
            ProcessoLogger.logger.info("Registrazione Microdosatura Componentente Completata - Eseguita");

        } catch (NumberFormatException e) {

            //Memorizzazione Log Processo
            ProcessoLogger.logger.log(Level.SEVERE, "Registrazione Microdosatura Componentente Completata - Fallita  e ={0}", e);

        }

        //Memorizzazione Log Processo
        ProcessoLogger.logger.config("Fine Registrazione Microdosatura Componente");
    }

    /**
     * Registrazione procedura di svuotamente miscelatore eseguita
     *
     * @param pulizia
     * @param vel_miscelatore
     * @param id_ciclo
     * @param id_ordine
     * @param data_inizio
     * @param idProdotto
     */
    public static void RegistraSvuotamentoMiscelatore(Pulizia pulizia, String vel_miscelatore, int id_ciclo, int id_ordine, Date data_inizio, int idProdotto) {

        try {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Inizio Registrazione Svuotamento Miscelatore");

            ProcessoOri processoOri = new ProcessoOri();

            //Codice Prodotto
            if (idProdotto != 0) {

                processoOri.setCodProdotto(TrovaCodiceProdottoPerIdProdotto(idProdotto));
            } else {

                processoOri.setCodProdotto(ParametriGlobali.parametri.get(78));
            }

            //Codice Chimica 
            processoOri.setCodChimica(ParametriGlobali.parametri.get(80));

            //Codice Sacco
            processoOri.setCodSacco(ParametriGlobali.parametri.get(82));

            //Peso Reale Sacco
            processoOri.setPesoRealeSacco(Integer.parseInt(pulizia.pesoSacco));

            //Cod Comp Peso
            processoOri.setCodCompPeso(ParametriGlobali.parametri.get(83));

            //Codice Colore
            processoOri.setCodColore(ParametriGlobali.parametri.get(81));

            //Codice Cliente
            processoOri.setCliente(ParametriGlobali.parametri.get(40));

            //Data di Produzione
            processoOri.setDtProduzione(new Date());

            //Registra Operatore
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(208))) {

                processoOri.setCodOperatore(DettagliSessione.getCodiceFigura());
            }

            //Registra Magazzino Componenti
            processoOri.setCodCompIn("");

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {

                processoOri.setTipoProcesso(TIPO_PROCESSO_SVUOTAMENTO);

                //Tipo Processo
                String idCiclo = Integer.toString(Integer.parseInt(ParametriSingolaMacchina.parametri.get(365)) + 1);

                //id ciclo produttivo
                String maxCiclo = TrovaMaxIdCiclo();

                if (!maxCiclo.equals("")) {

                    idCiclo = maxCiclo;
                }

                //Velocità Miscelatore
                processoOri.setInfo1(idCiclo);

                //Peso Desiderato Confezione
                processoOri.setInfo2(vel_miscelatore);
                processoOri.setInfo3("1_" + Integer.toString(
                        pulizia.pesoDaRaggiungereConfezione
                        + Integer.parseInt(ParametriSingolaMacchina.parametri.get(125))));

                //Peso Teorico Componenti
                processoOri.setInfo4("");

                //Non Utilizzato
                processoOri.setInfo5("");

            }

            EditEntity(processoOri);

            //Memorizzazione Log Processo
            PuliziaLogger.logger.info("Registrazione Svuotamento Eseguito");

        } catch (NumberFormatException ex) {

            ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(10);

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Errore Registrazione Svuotamento e={0}", ex);

        }

        ///Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Registrazione Svuotamento");

        try {
            ////////////////////////////////////////////////
            // AGGIORNAMENTO TABELLA CICLO_PROCESSO_ORI  ///
            ////////////////////////////////////////////////

            DateFormat df = new SimpleDateFormat(ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT);

            String reportDate = df.format(data_inizio);

            RegistraCiclo(id_ciclo, reportDate);

            ////////////////////////////////////
            // AGGIORNAMENTO TABELLA ORDINI  ///
            ////////////////////////////////////
            //Registrazione Associazione Ciclo Processo
            AggiornaContatoreTabellaOrdine(Integer.toString(id_ordine));

            // Aggiornamento Data Fine Ciclo
            AggiornaDataFineCiclo(id_ciclo);

        } catch (NumberFormatException ex) {

            if (pulizia.pannelloPulizia.isVisible()) {

                ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(10);
            }

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Registrazione Processo - Fallita - Errore e ={0}", ex);

        }

        try {

            if (pulizia.pannelloPulizia.isVisible()) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Incremento Id Processo in Corso");

                AggiornaValoreParSingMacOri(15,
                        Integer.toString(
                                Integer.parseInt((ParametriSingolaMacchina.parametri.get(15))) + 1));

                //Memorizzazione Log Processo
                PuliziaLogger.logger.info("Incremento Id Processo In Corso - Eseguito con Successo");

            }
        } catch (NumberFormatException ex) {

            if (pulizia.pannelloPulizia.isVisible()) {

                ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(12);

            }

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Incremento Id Processo In Corso - Fallito e ={0}", ex);

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Registrazione Processo");

    }

    /**
     * Registrazione procedura di svuotamento a seguito di pulizia eseguita
     *
     *
     * @param pulizia
     * @param vel_miscelatore
     * @param id_ciclo
     * @param id_ordine
     * @param data_inizio
     * @param idProdotto
     * @param idPresa
     * @param pesoTComp
     * @param pesoRComp
     * @param pesoRConf
     */
    public static void RegistraSvuotamentoMiscelatorePulizia(Pulizia pulizia, String vel_miscelatore, int id_ciclo, int id_ordine, Date data_inizio, int idProdotto, int idPresa, int pesoTComp, String pesoRComp, int pesoRConf) {

        try {

            //Memorizzazione Log Processo
            PuliziaLogger.logger.config("Inizio Registrazione Svuotamento Miscelatore");

            ProcessoOri processoOri = new ProcessoOri();

            //Codice Prodotto
            if (idProdotto != 0) {

                processoOri.setCodProdotto(TrovaCodiceProdottoPerIdProdotto(idProdotto));
            } else {

                processoOri.setCodProdotto(ParametriGlobali.parametri.get(78));
            }

            //Codice Chimica 
            processoOri.setCodChimica(ParametriGlobali.parametri.get(80));

            //Codice Sacco
            processoOri.setCodSacco(ParametriGlobali.parametri.get(82));

            //Peso Reale Sacco
            processoOri.setPesoRealeSacco(pesoRConf);

            //Cod Comp Peso
            processoOri.setCodCompPeso(Integer.toString(idPresa) + ParametriGlobali.parametri.get(22) + pesoRComp + ParametriGlobali.parametri.get(22) + Integer.toString(pesoTComp));

            //Codice Colore
            processoOri.setCodColore(ParametriGlobali.parametri.get(81));

            //Codice Cliente
            processoOri.setCliente(ParametriGlobali.parametri.get(40));

            //Data di Produzione
            processoOri.setDtProduzione(new Date());

            //Registra Operatore
            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(208))) {

                processoOri.setCodOperatore(DettagliSessione.getCodiceFigura());
            }

            //Registra Magazzino Componenti
            processoOri.setCodCompIn("");

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(364))) {

                processoOri.setTipoProcesso(TIPO_PROCESSO_PULIZIA_AUTOMATICA);

                //Tipo Processo
                String idCiclo = Integer.toString(Integer.parseInt(ParametriSingolaMacchina.parametri.get(365)) + 1);

                //id ciclo produttivo
                String maxCiclo = TrovaMaxIdCiclo();

                if (!maxCiclo.equals("")) {

                    idCiclo = maxCiclo;
                }

                //Velocità Miscelatore
                processoOri.setInfo1(idCiclo);

                //Peso Desiderato Confezione
                processoOri.setInfo2(vel_miscelatore);
                processoOri.setInfo3("1_" + Integer.toString(
                        pulizia.pesoDaRaggiungereConfezione
                        + Integer.parseInt(ParametriSingolaMacchina.parametri.get(125))));

                //Peso Teorico Componenti
                processoOri.setInfo4("");

                //Non Utilizzato
                processoOri.setInfo5("");

            }

            EditEntity(processoOri);

            //Memorizzazione Log Processo
            PuliziaLogger.logger.info("Registrazione Svuotamento Eseguito");

        } catch (NumberFormatException ex) {

            ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(10);

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Errore Registrazione Svuotamento e={0}", ex);

        }

        ///Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Registrazione Svuotamento");

        try {
            ////////////////////////////////////////////////
            // AGGIORNAMENTO TABELLA CICLO_PROCESSO_ORI  ///
            ////////////////////////////////////////////////

            DateFormat df = new SimpleDateFormat(ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT);

            String reportDate = df.format(data_inizio);

            RegistraCiclo(id_ciclo, reportDate);

            ////////////////////////////////////
            // AGGIORNAMENTO TABELLA ORDINI  ///
            ////////////////////////////////////
            //Registrazione Associazione Ciclo Processo
            AggiornaContatoreTabellaOrdine(Integer.toString(id_ordine));

            //Aggiornamento Data Fine Ciclo
            AggiornaDataFineCiclo(id_ciclo);

        } catch (NumberFormatException ex) {

            if (pulizia.pannelloPulizia.isVisible()) {

                ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(10);
            }

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Registrazione Processo - Fallita - Errore e ={0}", ex);

        }

        try {

            if (pulizia.pannelloPulizia.isVisible()) {

                //Memorizzazione Log Processo
                PuliziaLogger.logger.config("Incremento Id Processo in Corso");

                AggiornaValoreParSingMacOri(15,
                        Integer.toString(
                                Integer.parseInt((ParametriSingolaMacchina.parametri.get(15))) + 1));

                //Memorizzazione Log Processo
                PuliziaLogger.logger.info("Incremento Id Processo In Corso - Eseguito con Successo");

            }
        } catch (NumberFormatException ex) {

            if (pulizia.pannelloPulizia.isVisible()) {

                ((Pannello44_Errori) pulizia.pannelloPulizia.pannelliCollegati.get(1)).gestoreErrori.visualizzaErrore(12);

            }

            //Memorizzazione Log Processo
            PuliziaLogger.logger.log(Level.SEVERE, "Incremento Id Processo In Corso - Fallito e ={0}", ex);

        }

        //Memorizzazione Log Processo
        PuliziaLogger.logger.config("Fine Procedura Registrazione Processo");

    }

    /**
     * Restituisce la lista della prese non assegnate per procedure di pulizia
     *
     *
     *
     * @return
     */
    public static ArrayList<Integer> TrovaPreseAbilitateNonAssegnatePulizia() {

        ArrayList<Integer> result = new ArrayList<>();

        List<?> resultList1 = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idParComp='1' "
                + "AND v.valoreVariabile <" + ParametriGlobali.parametri.get(134) + " "
                + "AND v.abilitato =1 ");

        for (Object o : resultList1) {
            ValoreParCompOri valoreParCompOri = (ValoreParCompOri) o;
            result.add(Integer.parseInt(valoreParCompOri.getValoreVariabile()));
        }

        return result;

    }

    /**
     * Restituisce i valori dei parametri componenti per un componente
     * identificato da idComp
     *
     *
     * @param idComp
     * @return
     */
    public static ArrayList<String> TrovaValoreParametriComponentePerIdComp(int idComp) {
        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParCompOri v "
                + "WHERE v.idComp='" + idComp + "' "
                + "AND v.abilitato=1 "
                + "ORDER BY v.idParComp");

        for (Object o : resultList) {
            ValoreParCompOri valoreParCompOri = (ValoreParCompOri) o;
            result.add(valoreParCompOri.getValoreVariabile());
        }
        return result;
    }

    /**
     * Restituisce la lista dei componenti per IdProdotto e le relative quantita
     * per la procedura di pulizia
     *
     *
     * @param idProdotto
     * @return
     */
    public static ArrayList<Integer> TrovaComponentiPuliziaPerIdProdotto(int idProdotto) {

        ArrayList<Integer> res = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT c FROM ComponentePesaturaOri c "
                + "WHERE c.idProdotto = " + idProdotto + " "
                + "AND c.abilitato=1");

        for (Object o : resultList) {
            ComponentePesaturaOri compPesaturaOri = (ComponentePesaturaOri) o;
            res.add(compPesaturaOri.getIdComp());
            res.add(compPesaturaOri.getQuantita());
        }

        return res;

    }

    /**
     * Verifica la presenza di componenti da pesare tramite pesatura manuale
     *
     *
     * @param processo
     * @return
     */
    public static boolean VerificaPresenzaComponentiPesaturaManuale(Processo processo) {

        //ArrayList<String> res = new ArrayList<>();
        boolean findCompPesaMan = false;

        for (int i = 0; i < processo.componenti.size(); i++) {

            if ((processo.componenti.get(i).getMetodoPesa()).equals(ParametriGlobali.parametri.get(131))) {

                if ((Integer.toString(processo.componenti.get(i).getId())).equals(ParametriSingolaMacchina.parametri.get(301))
                        && !Boolean.parseBoolean(TrovaSingoloValoreParametroRipristino(102))) {

                    //////////////////////
                    // CHIMICA SFUSA  ///
                    /////////////////////
                    findCompPesaMan = true;
                    break;

                } else {

                    if (!(Integer.toString(processo.componenti.get(i).getId())).equals(ParametriSingolaMacchina.parametri.get(301))) {

                        //////////////////////////////////////////////////////
                        // COMPONENTI PESATURA MANUALE (ADDITIVI - COLORI  ///
                        //////////////////////////////////////////////////////
                        if (!Boolean.parseBoolean(TrovaGruppoValoreParametroRipristino(25).get(i))) {
                            findCompPesaMan = true;
                            break;
                        }

                    }
                }
            }
        }

        return findCompPesaMan;

    }

    /**
     * Restituisce il numero di confezioni di un determinato prodotto effettuate
     * in una determinata data
     *
     * @param codProdotto
     * @param dtProduzione
     * @return
     */
    public static int ConteggiaConfezioniEffettuate(String codProdotto, String dtProduzione) {

        List<?> resultList = EseguiQuery("SELECT p FROM ProcessoOri p "
                + "WHERE p.codProdotto = '" + codProdotto + "' "
                + "AND p.dtProduzione LIKE '%" + dtProduzione + "%' ");

        return resultList.size();

    }

    /**
     * Aggiorna dato relativo al codice sacco per un determianto processo
     *
     *
     * @param processo
     */
    public static void AggiornaCodiceSacco(Processo processo) {

        String codiceSacco = Integer.toString(Integer.parseInt(TrovaSingoloValoreParametroRipristino(47)) + 1);

        //Controlla lunghezza Codice 
        while (codiceSacco.length() < DEFAULT_LUNGHEZZA_COD_SACCO) {

            codiceSacco = "0" + codiceSacco;

        }

        //Indice Codice Sacchetto Inserito
        AggiornaValoreParametriRipristino(processo,
                TrovaIndiceTabellaRipristinoPerIdParRipristino(46),
                46,
                "true",
                ParametriSingolaMacchina.parametri.get(15));

        //Aggiorna Valore DataBase
        AggiornaValoreParametriRipristino(processo,
                TrovaIndiceTabellaRipristinoPerIdParRipristino(47),
                47,
                codiceSacco,
                ParametriSingolaMacchina.parametri.get(15));

    }

    /**
     * Costruisce codice sacco in caso di memorizzazione automatica
     *
     *
     * @param processo
     * @return
     */
    public static String CostruisciCodiceSacco(Processo processo) {

        String codiceSacco;

        // This is how to get today's date in Java
        Date today = new Date();

        //formatting date in Java using SimpleDateFormat
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        String date = DATE_FORMAT.format(today);

        SimpleDateFormat DATE_FORMAT_CODE = new SimpleDateFormat("yyMMdd");
        String date_code = DATE_FORMAT_CODE.format(today);

        String id_macchina = TrovaDatiMacchina().get(0);

        int counter = ConteggiaConfezioniEffettuate(processo.prodotto.getCodiceProdotto(), date) + 1;

        codiceSacco = Integer.toString(counter);

        String preCod = ParametriGlobali.parametri.get(17) + processo.prodotto.getCodiceProdotto() + id_macchina + date_code;

        while (codiceSacco.length() < (Integer.parseInt(ParametriGlobali.parametri.get(16)) - preCod.length())) {

            codiceSacco = "0" + codiceSacco;

        }

        codiceSacco = preCod + codiceSacco;

        return codiceSacco;

    }

    /**
     * Restituisce il valore prodotto in funzione dell'id prodotto e
     * dell'id_parametro_prodotto
     *
     *
     * @param idProd
     * @param idParProd
     * @return
     */
    public static String TrovaValoreProdottoByIdProd(int idProd, int idParProd) {
        String valoreParametro = "";

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreProdottoOri v "
                + "WHERE v.idParProd = " + idParProd + " "
                + "AND v.idProdotto = " + idProd);

        for (Object o : resultList) {
            ValoreProdottoOri valoreOri = (ValoreProdottoOri) o;
            valoreParametro = valoreOri.getValoreVariabile();
        }
        return valoreParametro;
    }

    /**
     * Restituisce la lista ordinata delle famiglie
     *
     * @return
     */
    public static String[] TrovaListaOrdinataFamiglie() {
        String[] lsOrdinataFamiglie;
        int result = 0;

        List<?> resultList = EseguiQuery("FROM ProdottoOri p WHERE p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "GROUP BY p.descriFamiglia "
                + "ORDER BY p.descriFamiglia");

        lsOrdinataFamiglie = new String[resultList.size()];

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            lsOrdinataFamiglie[result] = Integer.toString(prodottoOri.getIdCodice());
            result++;
        }
        return lsOrdinataFamiglie;
    }

    /**
     * Restituisce la lista dei prodotti
     *
     *
     * @param idLingua
     * @return
     */
    public static String[] TrovaListaProdotti(String idLingua) {

        String[] result;
        int index = 0;
        List<?> resultList = EseguiQuery("FROM ProdottoOri p WHERE p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "ORDER BY p.nomeProdotto");

        result = new String[resultList.size()];

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            result[index] = TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, prodottoOri.getIdProdotto(), idLingua);
            index++;
        }
        return result;
    }

    /**
     * Lista ordinata per nome dei prodotti
     *
     *
     * @return
     */
    public static List<?> TrovaListaOrdinataProdotti() {

        return EseguiQuery("FROM ProdottoOri p WHERE p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "ORDER BY p.nomeProdotto");

    }

    /**
     * Lista ordinata per codice dei codici
     *
     *
     * @return
     */
    public static List<?> TrovaListaOrdinataCodici() {

        return EseguiQuery("FROM ProdottoOri p WHERE p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "ORDER BY p.codProdotto");

    }

    /**
     * Lista ordinata per nome dei prodotti abilitati
     *
     *
     * @return
     */
    public static List<?> TrovaListaOrdinataCodiciProdottiAbilitati() {
        ArrayList<String> cod_prodotti = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "ORDER BY p.nomeProdotto ");

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            cod_prodotti.add(prodottoOri.getCodProdotto());
        }
        return cod_prodotti;

    }

    /**
     * Restituisce l'id del Prodotto in Funzione del Codice
     *
     *
     * @param codProdotto
     * @return
     */
    public static int TrovaIdProdottoByCodice(String codProdotto) {
        int idProd = 0;

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.codProdotto = '" + codProdotto + "' "
                + "AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='')");

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            idProd = prodottoOri.getIdProdotto();
        }
        return idProd;
    }

    /**
     * Restituisce il Nome e il Codice del Prodotto in Funzione della Famiglia
     *
     *
     * @param idCodice
     * @return
     */
    public static String[][] TrovaNomeProdottoCodiceByIdFamiglia(int idCodice) {
        String[][] nomeProdottoCodiceByFamiglia;
        int result = 0;

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idCodice = " + idCodice + " "
                + "AND p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "ORDER BY p.nomeProdotto");

        nomeProdottoCodiceByFamiglia = new String[2][resultList.size()];

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            nomeProdottoCodiceByFamiglia[0][result] = Integer.toString(prodottoOri.getIdProdotto());
            nomeProdottoCodiceByFamiglia[1][result] = prodottoOri.getCodProdotto();
            result++;
        }
        return nomeProdottoCodiceByFamiglia;
    }

    /**
     * Restituisce il Codice di un Prodotto in Funzione dell'id
     *
     *
     * @param idProdotto
     * @return
     */
    public static String TrovaCodiceByIdProdotto(int idProdotto) {
        String codice = null;

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idProdotto = " + idProdotto + " AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "AND p.abilitato=1");

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            codice = prodottoOri.getCodProdotto();
        }
        return codice;
    }

    /**
     * Restituisce i dati relativi ad un componente per un certo prodotto
     *
     *
     * @param idProdotto
     * @param idComp
     * @return
     */
    public static String TrovaQuantitaChimicaByProdottoByComp(int idProdotto, int idComp) {
        String result = null;
        List<?> resultList = EseguiQuery("SELECT c FROM ComponentePesaturaOri c, ProdottoOri p, ComponenteOri k "
                + "WHERE p.idProdotto = " + idProdotto + " "
                + "AND c.idProdotto = p.idProdotto "
                + "AND k.idComp = " + idComp + " "
                + "AND c.idComp = k.idComp "
                + "AND c.abilitato=1 "
                + "AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='')");

        for (Object o : resultList) {
            ComponentePesaturaOri compProdotto = (ComponentePesaturaOri) o;
            try {
                result = Integer.toString(compProdotto.getQuantita());
            } catch (Exception ex) {
            }
        }

        return result;

    }

    /**
     * Restituisce l'id del padre di un Prodotto identificato da un id
     *
     *
     * @param idProdotto
     * @return
     */
    public static int TrovaCodicePadreById(int idProdotto) {
        int result = 0;

        List<?> resultList = EseguiQuery("SELECT p FROM ProdottoOri p "
                + "WHERE p.idProdotto = " + idProdotto + " AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "AND p.abilitato=1");

        for (Object o : resultList) {
            ProdottoOri prodOri = (ProdottoOri) o;
            result = Integer.parseInt(prodOri.getColorato());
        }

        return result;

    }

    /**
     * Restituisce l'id del prodotto identificato tramite codice
     *
     *
     * @param codProdotto
     * @return
     */
    public static int TrovaIdProdottoPerCodProdotto(String codProdotto) {
        int result = 0;

       
        List<?> resultList = EseguiQuery("SELECT p FROM ProdottoOri p "
                + "WHERE p.codProdotto = '" + codProdotto + "' AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "AND p.abilitato=1");

        for (Object o : resultList) {
            ProdottoOri prodOri = (ProdottoOri) o;
            result = prodOri.getIdProdotto();
        }

        return result;

    }

    /**
     * Restituisce la Lista dei Componenti di un Prodotto per Id
     *
     *
     * @param idProdotto
     * @return
     */
    public static List<?> TrovaComponentiProdottoById(int idProdotto) {

        List<?> resuList = EseguiQuery("SELECT c FROM ComponentePesaturaOri c, ProdottoOri p "
                + "WHERE p.idProdotto = " + idProdotto + " "
                + "AND c.idProdotto = " + idProdotto + " AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "AND c.abilitato=1");

        return resuList;

    }

    /**
     * Restituisce un prodotto in funzione di un Id Prodotto
     *
     *
     * @param id_prodotto
     * @return
     */
    public static List<?> TrovaProdottoPerIdProdotto(int id_prodotto) {

        List<?> resuList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idProdotto = " + id_prodotto + " AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "AND p.abilitato=1");

        return resuList;

    }

    /**
     * Restituisce i Dati Relativi ai Componenti Presenti nella Formula
     *
     *
     * @param scelte
     * @return
     */
    public static List<?> TrovaValoreParametriComponenti(ScelteEffettuate scelte) {

        List<?> resultList = EseguiQuery("SELECT v FROM ValoreParCompOri v, ComponentePesaturaOri c, ProdottoOri p "
                + "WHERE (p.idProdotto='" + scelte.idProdotto + "' "
                + "OR p.idProdotto='" + scelte.idColore + "' "
                + "OR p.idProdotto='" + scelte.idAdditivo + "') "
                + "AND v.idParComp=1 "
                + "AND v.idComp=c.idComp "
                + "AND p.idProdotto=c.idProdotto "
                + "AND v.idComp!=" + Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)) + " "
                + "AND c.abilitato=1 "
                //+ "AND p.tipo ='" + ParametriGlobali.parametri.get(152) + "' "
                + "GROUP BY v.idComp "
                + "ORDER BY c.stepDosaggio ASC, c.ordineDosaggio ASC");

        return resultList;
    }

    /**
     * Restituisce il codice di un prodotto identificato per nome
     *
     *
     * @param nomeProdotto
     * @param idLingua
     * @return
     */
    public static String TrovaCodiceProdottoPerNomeProdotto(String nomeProdotto, String idLingua) {
        String codProdotto = null;

        List<?> resultList = TrovaProdottoPerIdProdotto(TrovaIdVocaboloPerIdDizionarioPerVocabolo(ID_DIZIONARIO_PRODOTTI, nomeProdotto, idLingua));

        for (Object o : resultList) {

            ProdottoOri prodottoOri = (ProdottoOri) o;
            codProdotto = prodottoOri.getCodProdotto();

        }

        return codProdotto;
    }

    /**
     * Restituisce il codice di un prodotto identificato per id
     *
     *
     * @param idProdotto
     * @return
     */
    public static String TrovaCodiceProdottoPerIdProdotto(int idProdotto) {
        String codProdotto = null;

        List<?> resultList = TrovaProdottoPerIdProdotto(idProdotto);

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            codProdotto = prodottoOri.getCodProdotto();
        }

        return codProdotto;
    }

    /**
     * Restituisce id del codice in funzione di un id prodotto
     *
     *
     * @param idProdotto
     * @return
     */
    public static String TrovaIdCodiceProdottoPerIdProdotto(String idProdotto) {

        String result = "";

        List<?> prodottoColl = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idProdotto = " + idProdotto + " "
                + "AND p.abilitato=1"
                + "AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') ");

        for (Object o : prodottoColl) {
            ProdottoOri prodotto = (ProdottoOri) o;
            result = Integer.toString(prodotto.getIdCodice());
        }

        return result;

    }

    /**
     * Restituisce l'id categoria di un prodotto identificato tramite id
     *
     *
     * @param idProdotto
     * @return
     */
    public static int TrovaIdCatPerIdProdotto(int idProdotto) {
        int idCategoria = 0;

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idProdotto = '" + idProdotto + "' "
                + "AND p.abilitato=1 "
                + "AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') ");

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            idCategoria = prodottoOri.getIdCat().getIdCat();
        }
        return idCategoria;
    }

    /**
     * Restituisce la serie colori associata ad un Prodotto
     *
     *
     * @param idProdotto
     * @return
     */
    public static ArrayList<String> TrovaSerieColoreProdotto(int idProdotto) {
        ArrayList<String> serieColore = new ArrayList<>();
        String serieColoreTemp = "";

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idProdotto = '" + idProdotto + "' "
                + "AND p.abilitato=1 "
                + "AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') ");

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            serieColoreTemp = prodottoOri.getSerieColore();
        }
        String strTemp = "";
        for (int i = 0; i < serieColoreTemp.length(); i++) {

            if (serieColoreTemp.charAt(i) == ParametriGlobali.parametri.get(157).charAt(0)) {
                serieColore.add(strTemp);
                strTemp = "";
            } else {
                strTemp += serieColoreTemp.charAt(i);
            }
        }
        serieColore.add(strTemp);

        return serieColore;
    }

    /**
     * Restituisce la serie additiv associata ad un prodotto
     *
     *
     * @param idProdotto
     * @return
     */
    public static ArrayList<String> TrovaSerieAdditivoProdotto(int idProdotto) {
        ArrayList<String> serieAdditivo = new ArrayList<>();
        String serieAdditivoTemp = "";

        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.idProdotto = '" + idProdotto + "' "
                + "AND p.abilitato=1 "
                + "AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') ");

        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            serieAdditivoTemp = prodottoOri.getSerieAdditivo();
        }

        String strTemp = "";
        for (int i = 0; i < serieAdditivoTemp.length(); i++) {

            if (serieAdditivoTemp.charAt(i) == ParametriGlobali.parametri.get(158).charAt(0)) {
                serieAdditivo.add(strTemp);
                strTemp = "";
            } else {
                strTemp += serieAdditivoTemp.charAt(i);
            }
        }
        serieAdditivo.add(strTemp);

        return serieAdditivo;
    }

    /**
     * Restiuisce la lista ordinata colori associati alla serie colori
     *
     *
     * @param serie_colori
     * @return
     */
    public static String[] TrovaListaOrdinataColoriPerSerieColori(ArrayList<String> serie_colori) {
        String[] lsOrdinataColori;
        int result = 0;
        String serie_colori_sq = "";
        if (serie_colori.size() > 0) {

            serie_colori_sq = "AND (";

            for (int i = 0; i < serie_colori.size(); i++) {

                if (i < serie_colori.size() - 1) {

                    serie_colori_sq += "p.serieColore ='" + serie_colori.get(i) + "' OR ";
                } else {
                    serie_colori_sq += "p.serieColore ='" + serie_colori.get(i) + "') ";
                }

            }

        }
        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.tipo ='" + ParametriGlobali.parametri.get(153) + "' "
                //+ "OR p.tipo ='" + ParametriGlobali.parametri.get(154) + "' "
                + serie_colori_sq
                + "AND p.abilitato=1");

        lsOrdinataColori = new String[resultList.size()];
        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            lsOrdinataColori[result] = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, prodottoOri.getIdProdotto(), ParametriSingolaMacchina.parametri.get(111)));
            result++;
        }
        return lsOrdinataColori;
    }

    /**
     * Restituisce la formula dei componenti da pesare per un determinato colore
     *
     *
     * @param idColore
     * @return
     */
    public static ArrayList<String> TrovaComponentiColoriPerIdColore(int idColore) {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT c FROM ComponentePesaturaOri c "
                + "WHERE c.idProdotto =" + idColore + " "
                + "AND c.abilitato=1");

        for (Object o : resultList) {
            ComponentePesaturaOri componentePesatura = (ComponentePesaturaOri) o;
            result.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, componentePesatura.getIdComp(), ParametriSingolaMacchina.parametri.get(111)))
                    + ": "
                    + ConvertiPesoVisualizzato(Double.toString(componentePesatura.getQuantita()), ParametriSingolaMacchina.parametri.get(338))
                    + ParametriSingolaMacchina.parametri.get(340));
        }

        return result;

    }

    /**
     * Restiuisce la lista ordinata additivi associata alla serie additivi
     *
     *
     * @param serie_additivi
     * @return
     */
    public static String[] TrovaListaOrdinataAdditivPerSerieAdditivi(ArrayList<String> serie_additivi) {

        String[] lsOrdinataAdditivi;
        int result = 0;

        String serie_additivi_sq = "";
        if (serie_additivi.size() > 0) {
            serie_additivi_sq = "AND  (";

            for (int i = 0; i < serie_additivi.size(); i++) {

                if (i < serie_additivi.size() - 1) {

                    serie_additivi_sq += "p.serieAdditivo ='" + serie_additivi.get(i) + "' OR ";
                } else {
                    serie_additivi_sq += "p.serieAdditivo ='" + serie_additivi.get(i) + "') ";
                }

            }

        }
        List<?> resultList = EseguiQuery("FROM ProdottoOri p "
                + "WHERE p.tipo ='" + ParametriGlobali.parametri.get(154) + "' "
                //+ "AND p.serieAdditivo ='" + serie_additivi + "' "
                + serie_additivi_sq
                + "AND p.abilitato=1");

        lsOrdinataAdditivi = new String[resultList.size()];
        for (Object o : resultList) {
            ProdottoOri prodottoOri = (ProdottoOri) o;
            lsOrdinataAdditivi[result] = EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_PRODOTTI, prodottoOri.getIdProdotto(), ParametriSingolaMacchina.parametri.get(111)));
            result++;
        }
        return lsOrdinataAdditivi;
    }

    /**
     * Restituisce la formula dei componenti da pesare per una determinata serie
     * additivi
     *
     *
     * @param idAdditivo
     * @return
     */
    public static ArrayList<String> TrovaComponentiAdditiviPerIdAdditivo(int idAdditivo) {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("SELECT c FROM ComponentePesaturaOri c "
                + "WHERE c.idProdotto =" + idAdditivo + " "
                + "AND c.idComp!=" + ParametriSingolaMacchina.parametri.get(301) + " "
                + "AND c.abilitato=1");

        for (Object o : resultList) {
            ComponentePesaturaOri componentePesatura = (ComponentePesaturaOri) o;
            result.add(EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_COMPONENTI, componentePesatura.getIdComp(), ParametriSingolaMacchina.parametri.get(111)))
                    + ": "
                    + ConvertiPesoVisualizzato(Double.toString(componentePesatura.getQuantita()), ParametriSingolaMacchina.parametri.get(338))
                    + ParametriSingolaMacchina.parametri.get(340));
        }

        return result;

    }

    /**
     * Restituisce la lista dei Codici Sacchetto Corrispondenti ad un Certo
     * Codice Prodotto
     *
     *
     * @param idProdotto
     * @return
     */
    public static ArrayList<ArrayList<Integer>> TrovaComponentiAlternativi(int idProdotto) {

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ComponentePesaturaOri c "
                + "WHERE c.idProdotto = " + idProdotto + " "
                + "AND c.info1 != '' AND c.abilitato=1");

        for (Object o : resultList) {

            ComponentePesaturaOri componentePersatura = (ComponentePesaturaOri) o;

            ArrayList<Integer> comp = new ArrayList<>();

            comp.add(componentePersatura.getIdComp());

            String str = componentePersatura.getInfo1();
            String temp = "";

            for (int i = 0; i < str.length(); i++) {

                if (str.charAt(i) == ParametriGlobali.parametri.get(156).charAt(0)) {

                    if (VerificaComponentiAlternativi(Integer.parseInt(temp))) {
                        //Verifica Presenza Componente
                        comp.add(Integer.parseInt(temp));
                    }
                    temp = "";

                } else {
                    temp += str.charAt(i);

                }
            }
            if (VerificaComponentiAlternativi(Integer.parseInt(temp))) {
                //Verifica Presenza Componente
                comp.add(Integer.parseInt(temp));
            }
            result.add(comp);

        }

        return result;
    }

    /**
     * Verifica che il componente sostitutivo sia abilitato
     *
     *
     * @param idComponente
     * @return
     */
    public static boolean VerificaComponentiAlternativi(int idComponente) {

        boolean resultComp = false;
        boolean resultComponentePesaturaOri = false;
        boolean resultProdotto = false;

        //Verifica Presenza Componenti
        List<?> resultList = EseguiQuery("FROM ComponenteOri c "
                + "WHERE c.idComp = " + idComponente + " "
                + "AND c.abilitato = 1");

        for (Object o : resultList) {
            ComponenteOri componente = (ComponenteOri) o;

            resultComp = (componente.getIdComp() == idComponente);

        }

        //Verifica Prodotto Abilitato e Componente Pesatura
        resultList = EseguiQuery("FROM ComponentePesaturaOri c "
                + "WHERE c.idComp = " + idComponente + " "
                + "AND c.abilitato = 1");

        ArrayList<Integer> idProdotto = new ArrayList<>();
        for (Object o : resultList) {
            ComponentePesaturaOri componente = (ComponentePesaturaOri) o;
            resultComponentePesaturaOri = (componente.getIdComp() == idComponente);
            idProdotto.add(componente.getIdProdotto());

        }

        for (int i = 0; i < idProdotto.size(); i++) {

            resultList = TrovaProdottoPerIdProdotto(idProdotto.get(i));
            if (resultProdotto) {
                break;
            }
            for (Object o : resultList) {
                @SuppressWarnings("unused")
				ProdottoOri prodotto = (ProdottoOri) o;
                resultProdotto = true;
                break;
            }

        }

        return resultComp && resultComponentePesaturaOri && resultProdotto;
    }

    /**
     * Verifica che il codice inserito corrisponda al componente selezionato
     *
     *
     * @param idComponente
     * @param codComponente
     * @return
     */
    public static boolean VerificaCodiceMateriaPrima(int idComponente, String codComponente) {

        List<?> resultList = EseguiQuery("FROM ValoreParCompOri v "
                + "WHERE v.idComp = " + idComponente + " "
                + "AND v.idParComp = 6 "
                + "AND v.valoreVariabile = '" + codComponente + "' "
                + "AND v.abilitato = 1");

        return resultList.size() > 0;

    }

    /**
     * Restituisce la lista ordinata dei prodotti con chimica
     *
     *
     * @return
     */
    public static ArrayList<ProdottoOri> TrovaListaOrdinataProdottiRealizzabili() {

        ArrayList<ProdottoOri> res = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ProdottoOri p WHERE p.abilitato=1 AND (p.tipo ='" + ParametriGlobali.parametri.get(152) + "' OR p.tipo ='') "
                + "ORDER BY p.nomeProdotto");

        for (Object o : resultList) {
            ProdottoOri prodotto = (ProdottoOri) o;

            String nomeProdottoControllo = EstraiStringaHtml(prodotto.getNomeProdotto());

            if ((TrovaNumCodiciValidi(prodotto.getCodProdotto()) > 0)
                    || (TrovaNumCodiciValidi(TrovaCodiceProdottoPerIdProdotto(Integer.parseInt(prodotto.getColorato()))) > 0)
                    || ((nomeProdottoControllo.length() - 1 > ParametriSingolaMacchina.parametri.get(143).length())
                    && ((nomeProdottoControllo.substring(0, ParametriSingolaMacchina.parametri.get(143).length()).equals(ParametriSingolaMacchina.parametri.get(143)))
                    || (nomeProdottoControllo.substring(0, ParametriSingolaMacchina.parametri.get(302).length()).equals(ParametriSingolaMacchina.parametri.get(302)))
                    || Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(419))))) {

                res.add(prodotto);
            }
        }
        return res;

    }

    /**
     * DecodificaCodiceChimicaSfusa Analizza il codice chimica e sfusa e
     * restituisce un array con codice lotto, il numero di produzioni
     * realizzabili e la quantità di chimica
     *
     * @param codiceChimicaSfusa
     * @return
     */
    public static ArrayList<String> DecodificaCodiceChimicaSfusa(String codiceChimicaSfusa) {

        boolean codTrovato = false;
        boolean qTrovata = false;
        String sacChimica = "";
        String codChimica = "";
        String qChimica = "";
        ArrayList<String> result = new ArrayList<>();

        char carattereSeparazione = ParametriGlobali.parametri.get(20).toCharArray()[0];

        for (int i = 0; i < codiceChimicaSfusa.length(); i++) {

            if (!codTrovato) {
                if (codiceChimicaSfusa.charAt(i) != carattereSeparazione) {

                    codChimica += codiceChimicaSfusa.charAt(i);

                } else {
                    codTrovato = true;
                }
            } else if (!qTrovata) {
                if (codiceChimicaSfusa.charAt(i) != carattereSeparazione) {

                    qChimica += codiceChimicaSfusa.charAt(i);

                } else {
                    qTrovata = true;
                }
            } else if (codiceChimicaSfusa.charAt(i) != carattereSeparazione) {

                sacChimica += codiceChimicaSfusa.charAt(i);

            }

        }

        result.add(codChimica);
        result.add(qChimica);
        result.add(sacChimica);

        return result;

    }

    /**
     * Restituisce la lista dei codici lotto disponibili
     *
     * @return
     */
    public static ArrayList<String> TrovaCodiciLotto() {

        ArrayList<String> result = new ArrayList<>();

        List<?> resultList = EseguiQuery("FROM ChimicaOri c "
                + "WHERE c.statoCh = 0 "
                + "GROUP BY c.codLotto "
                + "ORDER BY c.descriFormula");

        for (Object o : resultList) {

            ChimicaOri chimica = (ChimicaOri) o;

            result.add(chimica.getCodLotto());

        }

        return result;
    }

    /**
     * Restituisce un arraylist contente le prese abilitate per la macchina
     *
     *
     * @return
     */
    public static ArrayList<String> TrovaPreseAbilitate() {

        ArrayList<String> prese = new ArrayList<>();

        String preseAbilitate = ParametriSingolaMacchina.parametri.get(293);

        String temp = "";
        for (int i = 0; i < preseAbilitate.length(); i++) {

            if (preseAbilitate.charAt(i) != CHAR_SEP_STRINGA_PRESE_MACCHINA.charAt(0)) {
                temp += preseAbilitate.charAt(i);

            } else {

                if (Integer.parseInt(temp) < (Integer.parseInt(ParametriSingolaMacchina.parametri.get(2)
                ))) {

                    prese.add(TrovaPresaPerIdPresa(Integer.parseInt(temp)));

                    temp = "";

                } else {
                    break;
                }
            }
        }
        prese.add(TrovaPresaPerIdPresa(Integer.parseInt(temp)));

        return prese;
    }

//////////////////////    
/// FUNZIONI 5.0  ////
////////////////////// 

    /**
     * Codifica lo status Register LAUMAS TLB4
     *
     * @param s
     * @return
     */
    public static String CodificaStatusRegister(String s) {
        String result = "";
        String codifica = "ABCDEFGHIJKLMNOP";
        for (int i = 0; i < s.length(); i++) {
            if ('1' == s.charAt(i)) {
                result += codifica.charAt(i);
            }
        }
        return result;

    }

    /**
     * Decodifica lo status Register LAUMAS TLB4
     *
     *
     * @param s
     * @return
     */
    public static String DecodificaStatusRegister(String s) {
        String[] result = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
        String res = "";
        String codifica = "ABCDEFGHIJKLMNOP";
        for (int i = 0; i < s.length(); i++) {
            result[codifica.indexOf(s.charAt(i))] = "1";
        }

        for (String result1 : result) {
            res += result1;
        }
        return res;

    }

    /**
     * Analizza lo status Register LAUMAS TLB4
     *
     * @param s
     */
    public static void AnalizzaStatusRegister(String s) {
        String[] register = {
            "ERRORE CELLA DI CARICO",
            "AVARIA DEL CONVERTITORE AD",
            "PESO MASSIMO SUPERATO DI 9 DIVISIONI",
            "PESO LORDO SUPERIORE AL 110% DEL FONDO SCALA",
            "PESO LORDO OLTRE 999999 o inferiore a -999999",
            "PESO NETTO OLTRE 999999 o inferiore a -999999",
            "Bit 6 -- NON DEFINITO",
            "SEGNO NEGATIVO PESO LORDO",
            "SEGNO NEGATIVO PESO NETTO",
            "SEGNO NEGATIVO PESO PICCO",
            "VISUALIZZAZIONE IN NETTO",
            "STABILITA PESO",
            "PESO ENTRO ±1⁄4 DIVISIONE ATTORNO ALLO ZERO",
            "Bit 13 -- NON DEFINITO",
            "Bit 14 -- NON DEFINITO",
            "REFERENCE CELLE NON COLLEGATO"};

        for (int i = 0; i < s.length(); i++) {

            if (s.charAt(i) == '1') {

                System.out.println(register[i]);
            }
        }
    }

    /**
     * Verifica la connessione di rete su indirizzo di rete con un certo timeout
     *
     * @param site
     * @param timeout
     * @return
     */
    public static boolean VerificaConnessioneRete(String site, Integer timeout) {

        Boolean result = false;
        try ( Socket socket = new Socket()) {
            InetSocketAddress addr = new InetSocketAddress(site, 80);
            socket.connect(addr, timeout);
            result = socket.isConnected();
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * Verifica se una indirizzo IP è raggiungibile
     *
     * @param site
     * @param timeout
     * @return
     */
    public static boolean PingAddress(String ipAddress, Integer timeout) throws UnknownHostException, IOException {

        InetAddress address = InetAddress.getByName(ipAddress);
        return address.isReachable(timeout);

    }

    /**
     * Restituisce l'indirizzo del server FTP per eseguire la verifica della
     * connessione internet
     *
     * @return
     */
    public static String TrovaIpFtp() {
        String result = "";

        List<?> resultList = EseguiQuery("FROM AggiornamentoConfigOri c WHERE id=21");

        for (Object o : resultList) {
            AggiornamentoConfigOri aggiornamentoConfigOri = (AggiornamentoConfigOri) o;
            result = aggiornamentoConfigOri.getValore();
        }
        return result;

    }

    /**
     * Restituisce l'indirizzo della macchina per verificare la corretta
     * condigurazione della macchina
     *
     * @return
     */
    public static ArrayList<String> TrovaIpOrigami() {
        ArrayList<String> result;

        result = new ArrayList<>();
        Enumeration<?> e;
        try {
            e = NetworkInterface.getNetworkInterfaces();

            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration<InetAddress> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    String Ip = i.getHostAddress();
                    if (Ip.contains(".")) {
                        result.add(i.getHostAddress());
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return result;

    }

    /**
     * Verifica la configurazione dell'inidirzzo ip di Origami
     *
     *
     * @return
     */
    public static boolean VerificaConfigurazioneIp() {

        boolean result = false;
        ArrayList<String> lista_ip = TrovaIpOrigami();

        for (int i = 0; i < lista_ip.size(); i++) {

            if (lista_ip.get(i).equals(ParametriSingolaMacchina.parametri.get(464) + ParametriSingolaMacchina.parametri.get(465))) {

                result = true;

                break;

            }

        }

        return result;
    }

    /**
     *
     * Configurazione Pannelli e collegamenti fra pannelli
     *
     *
     * @param pannelli
     * @return
     */
    public static boolean CreaPannelli(MyStepPanel pannelli[]) {

        boolean creazioneEseguita = false;

        try {
            pannelli[1] = new Pannello01_SceltaProduzione();
            pannelli[2] = new Pannello02_SceltaFiltro();
            pannelli[3] = new Pannello03_FiltroGen();
            pannelli[4] = new Pannello03_FiltroGen_Alter();
            pannelli[5] = new Pannello03_FiltroProdCod();
            pannelli[6] = new Pannello03_FiltroProdCod_Alter();
            pannelli[7] = new Pannello04_SceltaTipoChimica();
            pannelli[8] = new Pannello05_SceltaNumMiscele();
            pannelli[9] = new Pannello06_SceltaDimContenitore();
            pannelli[10] = new Pannello07_SceltaTipoContenitore();
            pannelli[11] = new Pannello08_SceltaCliente();
            pannelli[12] = new Pannello09_SceltaCambioComponenti();
            pannelli[13] = new Pannello10_ScelteEffettuate();
            pannelli[14] = new Pannello11_Processo();
            pannelli[15] = new Pannello12_Aggiornamento();
            pannelli[16] = new Pannello13_Configurazione_Generale();
            pannelli[17] = new Pannello14_Configurazione_Parametri();
            pannelli[18] = new Pannello15_Configurazione_Prese();
            pannelli[19] = new Pannello16_Controllo();
            pannelli[20] = new Pannello17_Verifiche();
            pannelli[21] = new Pannello18_Controllo_Report();
            pannelli[22] = new Pannello19_Spegnimento();
            pannelli[23] = new Pannello20_Assistenza();
            pannelli[24] = new Pannello21_Ricerca();
            pannelli[25] = new Pannello22_Ricerca_Filtro_Generale();
            pannelli[26] = new Pannello23_Ricerca_Codici_Chimica_Disponibili();
            pannelli[27] = new Pannello24_Ricerca_Prodotti_Per_Cliente();
            pannelli[28] = new Pannello25_Ricerca_Codici_Per_Cliente();
            pannelli[29] = new Pannello26_Ricerca_Dettagli();
            pannelli[30] = new Pannello27_Configurazione_MateriePrime();
            pannelli[31] = new Pannello44_Errori();
            pannelli[32] = new Pannello45_Dialog();
            pannelli[33] = new Pannello46_ErroriAggiornamento();
            pannelli[34] = new Pannello28_Inventario();
            pannelli[35] = new Pannello29_RecuperaCodice();
            pannelli[36] = new Pannello30_ConfermaRecuperaCodice();
            pannelli[37] = new Pannello31_Comando_Microdosatori();
            pannelli[38] = new Pannello32_Configurazione_Taratura_Microdosatore();
            pannelli[39] = new Pannello33_SceltaScaricaValvola();
            pannelli[40] = new Pannello03_Ordini();
            pannelli[41] = new Pannello34_Gestione_Materie_Prime();
            pannelli[42] = new Pannello35_Gestore_Bilancia_Sacchi_Valvola_Aperta();
            pannelli[43] = new Pannello36_ScaricoSilosMicro();
            pannelli[44] = new Pannello37_Pulizia();
            pannelli[45] = new Pannello38_Pulizia_Svuotamento();
            pannelli[46] = new Pannello39_Pulizia_Automatica();
            pannelli[47] = new Pannello40_Pulizia_Manuale();
            pannelli[48] = new Pannello41_SceltaColore();
            pannelli[49] = new Pannello42_SceltaAdditivo();
            pannelli[50] = new Pannello43_Processo_Pesatura_Manuale();

            ////////////////////////////////
            // DEFINIZIONE COLLEGAMENTI  ///
            ////////////////////////////////
            pannelli[0].pannelliCollegati.add(pannelli[1]);
            pannelli[0].pannelliCollegati.add(pannelli[15]);
            pannelli[0].pannelliCollegati.add(pannelli[16]);
            pannelli[0].pannelliCollegati.add(pannelli[44]);
            pannelli[0].pannelliCollegati.add(pannelli[19]);
            pannelli[0].pannelliCollegati.add(pannelli[22]);
            pannelli[0].pannelliCollegati.add(pannelli[23]);
            pannelli[0].pannelliCollegati.add(pannelli[24]);
            pannelli[0].pannelliCollegati.add(pannelli[32]);

            pannelli[1].pannelliCollegati.add(pannelli[0]);
            pannelli[1].pannelliCollegati.add(pannelli[2]);
            pannelli[1].pannelliCollegati.add(pannelli[14]);
           // pannelli[1].pannelliCollegati.add(pannelli[31]);
           // pannelli[1].pannelliCollegati.add(pannelli[40]);

            pannelli[2].pannelliCollegati.add(pannelli[0]);
            pannelli[2].pannelliCollegati.add(pannelli[3]);
            pannelli[2].pannelliCollegati.add(pannelli[5]);

            pannelli[3].pannelliCollegati.add(pannelli[0]);
            pannelli[3].pannelliCollegati.add(pannelli[4]);

            pannelli[4].pannelliCollegati.add(pannelli[0]);
            pannelli[4].pannelliCollegati.add(pannelli[7]);

            pannelli[5].pannelliCollegati.add(pannelli[0]);
            pannelli[5].pannelliCollegati.add(pannelli[7]);
            pannelli[5].pannelliCollegati.add(pannelli[6]);
            pannelli[5].pannelliCollegati.add(pannelli[13]);

            pannelli[6].pannelliCollegati.add(pannelli[0]);
            pannelli[6].pannelliCollegati.add(pannelli[7]);
            pannelli[6].pannelliCollegati.add(pannelli[5]);

            pannelli[7].pannelliCollegati.add(pannelli[0]);
            pannelli[7].pannelliCollegati.add(pannelli[8]);

            pannelli[8].pannelliCollegati.add(pannelli[0]);
            pannelli[8].pannelliCollegati.add(pannelli[9]);
            pannelli[8].pannelliCollegati.add(pannelli[13]);

            pannelli[9].pannelliCollegati.add(pannelli[0]);
            pannelli[9].pannelliCollegati.add(pannelli[10]);

            pannelli[10].pannelliCollegati.add(pannelli[0]);
            pannelli[10].pannelliCollegati.add(pannelli[11]);
            pannelli[10].pannelliCollegati.add(pannelli[39]);

            pannelli[11].pannelliCollegati.add(pannelli[0]);
            pannelli[11].pannelliCollegati.add(pannelli[12]);
            pannelli[11].pannelliCollegati.add(pannelli[13]);
            pannelli[11].pannelliCollegati.add(pannelli[48]);
            pannelli[11].pannelliCollegati.add(pannelli[49]);

            pannelli[12].pannelliCollegati.add(pannelli[0]);
            pannelli[12].pannelliCollegati.add(pannelli[13]);
            pannelli[12].pannelliCollegati.add(pannelli[48]);
            pannelli[12].pannelliCollegati.add(pannelli[49]);

            pannelli[13].pannelliCollegati.add(pannelli[0]);
            pannelli[13].pannelliCollegati.add(pannelli[14]);
            pannelli[13].pannelliCollegati.add(pannelli[16]);

            pannelli[14].pannelliCollegati.add(pannelli[0]);
            pannelli[14].pannelliCollegati.add(pannelli[31]);
            pannelli[14].pannelliCollegati.add(pannelli[32]);
            pannelli[14].pannelliCollegati.add(pannelli[50]);

            pannelli[15].pannelliCollegati.add(pannelli[0]);
            pannelli[15].pannelliCollegati.add(pannelli[33]);

            pannelli[16].pannelliCollegati.add(pannelli[0]);
            pannelli[16].pannelliCollegati.add(pannelli[17]);
            pannelli[16].pannelliCollegati.add(pannelli[18]);
            pannelli[16].pannelliCollegati.add(pannelli[13]);
            pannelli[16].pannelliCollegati.add(pannelli[37]);
            pannelli[16].pannelliCollegati.add(pannelli[41]);
            pannelli[16].pannelliCollegati.add(pannelli[42]);

            pannelli[17].pannelliCollegati.add(pannelli[0]);

            pannelli[18].pannelliCollegati.add(pannelli[16]);
            pannelli[18].pannelliCollegati.add(pannelli[30]);

            pannelli[19].pannelliCollegati.add(pannelli[0]);
            pannelli[19].pannelliCollegati.add(pannelli[20]);

            pannelli[20].pannelliCollegati.add(pannelli[0]);
            pannelli[20].pannelliCollegati.add(pannelli[21]);
            pannelli[20].pannelliCollegati.add(pannelli[31]);

            pannelli[21].pannelliCollegati.add(pannelli[0]);

            pannelli[22].pannelliCollegati.add(pannelli[0]);

            pannelli[23].pannelliCollegati.add(pannelli[0]);

            pannelli[24].pannelliCollegati.add(pannelli[0]);
            pannelli[24].pannelliCollegati.add(pannelli[25]);
            pannelli[24].pannelliCollegati.add(pannelli[34]);
            pannelli[24].pannelliCollegati.add(pannelli[35]);

            pannelli[25].pannelliCollegati.add(pannelli[0]);
            pannelli[25].pannelliCollegati.add(pannelli[29]);
            pannelli[25].pannelliCollegati.add(pannelli[26]);
            pannelli[25].pannelliCollegati.add(pannelli[27]);

            pannelli[26].pannelliCollegati.add(pannelli[0]);
            pannelli[26].pannelliCollegati.add(pannelli[29]);

            pannelli[27].pannelliCollegati.add(pannelli[0]);
            pannelli[27].pannelliCollegati.add(pannelli[28]);

            pannelli[28].pannelliCollegati.add(pannelli[0]);
            pannelli[28].pannelliCollegati.add(pannelli[29]);

            pannelli[29].pannelliCollegati.add(pannelli[0]);

            pannelli[30].pannelliCollegati.add(pannelli[16]);

            pannelli[34].pannelliCollegati.add(pannelli[0]);
            pannelli[34].pannelliCollegati.add(pannelli[31]);

            pannelli[35].pannelliCollegati.add(pannelli[0]);
            pannelli[35].pannelliCollegati.add(pannelli[36]);

            pannelli[36].pannelliCollegati.add(pannelli[35]);

            pannelli[37].pannelliCollegati.add(pannelli[16]);
            pannelli[37].pannelliCollegati.add(pannelli[38]);

            pannelli[38].pannelliCollegati.add(pannelli[37]);

            pannelli[39].pannelliCollegati.add(pannelli[0]);
            pannelli[39].pannelliCollegati.add(pannelli[11]);

            pannelli[40].pannelliCollegati.add(pannelli[0]);
            pannelli[40].pannelliCollegati.add(pannelli[2]);
            pannelli[40].pannelliCollegati.add(pannelli[8]);
            pannelli[40].pannelliCollegati.add(pannelli[31]);
            pannelli[40].pannelliCollegati.add(pannelli[19]);
            pannelli[40].pannelliCollegati.add(pannelli[45]);
            pannelli[40].pannelliCollegati.add(pannelli[46]);

            pannelli[41].pannelliCollegati.add(pannelli[0]);
            pannelli[41].pannelliCollegati.add(pannelli[13]);
            pannelli[41].pannelliCollegati.add(pannelli[43]);

            pannelli[42].pannelliCollegati.add(pannelli[16]);

            pannelli[43].pannelliCollegati.add(pannelli[41]);

            pannelli[44].pannelliCollegati.add(pannelli[0]);
            pannelli[44].pannelliCollegati.add(pannelli[45]);
            pannelli[44].pannelliCollegati.add(pannelli[46]);
            pannelli[44].pannelliCollegati.add(pannelli[47]);

            pannelli[45].pannelliCollegati.add(pannelli[0]);
            pannelli[45].pannelliCollegati.add(pannelli[31]);
            pannelli[45].pannelliCollegati.add(pannelli[32]);

            pannelli[46].pannelliCollegati.add(pannelli[0]);
            pannelli[46].pannelliCollegati.add(pannelli[31]);
            pannelli[46].pannelliCollegati.add(pannelli[32]);

            pannelli[47].pannelliCollegati.add(pannelli[0]);
            pannelli[47].pannelliCollegati.add(pannelli[31]);
            pannelli[47].pannelliCollegati.add(pannelli[32]);

            pannelli[48].pannelliCollegati.add(pannelli[0]);
            pannelli[48].pannelliCollegati.add(pannelli[13]);
            pannelli[48].pannelliCollegati.add(pannelli[49]);

            pannelli[49].pannelliCollegati.add(pannelli[0]);
            pannelli[49].pannelliCollegati.add(pannelli[13]);

            pannelli[50].pannelliCollegati.add(pannelli[14]);
            creazioneEseguita = true;

        } catch (Exception ex) {

            SessionLogger.logger.log(Level.SEVERE, "Errore durante la creazione dei pannelli - ex:{0}", ex);
        }

        return creazioneEseguita;
    }

    /**
     * Estrazione indirizzi bilance
     *
     *
     * @param stringa_indirizzi_bilance: 120_121_122
     * @param stringa_sottorete 192.168.0.
     * @return 
     */
    public static ArrayList<String> EstraiIndirizziBilance(String stringa_indirizzi_bilance, String stringa_sottorete) {
 
        ArrayList<String> result = new ArrayList<>();
 
        String temp = "";
        for (int i = 0; i < stringa_indirizzi_bilance.length(); i++) {
            if (stringa_indirizzi_bilance.charAt(i) != CHAR_SEP_ID_BILANCIA) {
                temp += stringa_indirizzi_bilance.charAt(i);
            } else {
                result.add(stringa_sottorete + temp);
                temp = "";
            }
        }
        if (!temp.equals("")) {
            result.add(stringa_sottorete + temp);

        }

        while (result.size() < NUM_BILANCE_UTILIZZABILI) {

            result.add("");
        }
    	 
        return result;
    }

    /**
     * Estrazione i dati da una stringa di parametri separati da un carattere di
     * separazione passato come parametro insieme alla stringa da decodificare
     *
     *
     * @param str
     * @param char_sep
     * @return
     */
    public static ArrayList<String> EstraiDatiStringaParametri(String str, String char_sep) {
        ArrayList<String> result = new ArrayList<>();
        String temp = "";
        for (int i = 0; i < str.length(); i++) {

            if (str.charAt(i) != char_sep.charAt(0)) {
                if (!(str.substring(i, str.length())).contains(char_sep)) {
                    result.add(str.substring(i, str.length()));
                    break;
                } else {
                    temp += str.charAt(i);
                }
            } else {
                result.add(temp);
                temp = "";
            }
        }
        return result;
    }

    /**
     * Sostituisce un carattere in una stringa in una certa posizione
     *
     *
     * @param string
     * @param value
     * @param pos
     * @return
     */
    public static String SostituisciCarattereStringaPosizione(String string, String value, Integer pos) {

        String result = "";

        for (int i = 0; i < string.length(); i++) {

            if (i != pos) {
                result += string.charAt(i);
            } else {
                result += value;
            }
        }
        return result;

    }

    /**
     * Conversione da Esadecimale a Binario il parametro len definisce la
     * lunghezza del valore binario
     *
     * @param hex
     * @param len
     * @return
     *
     */
    public static String ConversioneHextoBinario(String hex, int len, int hex_n) {

        String result = "";

        while (hex.length() > 0) {

            int n = Integer.parseInt(hex.substring(0, hex_n), 16);
            String binary = Integer.toBinaryString(n);
            hex = hex.substring(hex_n, hex.length());
            while (binary.length() < len) {
                binary = "0" + binary;
            }
            result += binary;
        }
        return result;
    }

    /**
     * Gestore apertura valvola con diverse posizioni 0 - Valvola Chiusa 1 -
     * Valvola Aperta al 20% 2 Valvola Aperta al 40% 3 Valvola Aperta al 80% 4
     * Valvola Aperta
     *
     * @param id_pos
     *
     */
    public static void GestoreValvolaAttuatoreMultiStadio(int id_pos_str) {

    	 
    	
        switch (id_pos_str) {
            case POS_0DEF:
                // POS_0DEF - ATTUATORE 100% - VALVOLA 0%
            	//  1=OFF      2=ON       3=OFF        4=OFF
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_VALVOLA_SCARICO_1 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_2 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_3 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_4,
                        OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR);
                break;
            case POS_20:
                // POS_20 - ATTUATORE 80% - VALVOLA 20%
                //  1=OFF      2=OFF       3=ON        4=ON
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_VALVOLA_SCARICO_1 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_2 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_3 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_4,
                        OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR);
                break;
            case POS_53:
                // POS_53 - ATTUATORE 53% - VALVOLA 40%
                //  1=ON      2=OFF       3=OFF       4=ON
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_VALVOLA_SCARICO_1 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_2 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_3 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_4,
                        OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_TRUE_CHAR);
                break;
            case POS_100:
                // POS_100 - ATTUATORE 0% - VALVOLA 100%                
            	//  1=ON        2=OFF        3=OFF     4=OFF
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_VALVOLA_SCARICO_1 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_2 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_3 + OUTPUT_SEP_CHAR
                        + USCITA_LOGICA_EV_VALVOLA_SCARICO_4,
                        OUTPUT_TRUE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR + OUTPUT_SEP_CHAR
                                + OUTPUT_FALSE_CHAR);
                break;
                
            case POS_0_COMANDO_UNICO:
                // POS_0 - VALVOLA CHIUSA               
            	//  1=ON        2=OFF        3=OFF     4=OFF
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_VALVOLA_SCARICO_COMANDO_UNICO,
                        OUTPUT_FALSE_CHAR);
                break;
                
            case POS_100_COMANDO_UNICO:
                // POS_100 - VALVOLA APERTA
                GestoreIO_ModificaOut(USCITA_LOGICA_EV_VALVOLA_SCARICO_COMANDO_UNICO,
                        OUTPUT_TRUE_CHAR);
                break;
                
            default:
                break;
        }

    }
    
	/**
     *Elimina il carattere vuoto "-" nella stringhe divise in più righe
     *in quanto iNephos non gestisce il campo vuoto
     *
     * @param str
     *
     */
    public static String EliminaCarattereVuotoiNephos(String str) {
    	String res = "";
    	for (int i=0; i<str.length(); i++) {
    		if (str.charAt(i)==ParametriSingolaMacchina.parametri.get(499).charAt(0))
    		{
    			break;
    		} else {
    			res += str.charAt(i); 
    		}
    	}

    	return res;
    }
    
    
    /**
     * Legge informazioni da inserire come movimenti nella tabella 
     * movimenti_sing_mac_ori e restituisce un arrayList con le info
     * 
     *
     */
    public static void ImportaDati4_0() {
		  
		ArrayList<String> lista_file = new ArrayList<>();

		///////////////////////////////////////////////////////////////// LETTURA LISTA
		///////////////////////////////////////////////////////////////// DEI FILE
		///////////////////////////////////////////////////////////////// PRENSENTI
		///////////////////////////////////////////////////////////////// NELLA CARTELLA
		File[] list = new File(CSV_PATH_IMPORT).listFiles();
		for (int i = 0; i < list.length; i++) {
			if (!list[i].isDirectory())
				lista_file.add(list[i].getName());
		}

		try {

			MovimentiIN movIn = new MovimentiIN(CSV_DATE_FORMAT);
			ArrayList<String> dati_file = new ArrayList<>();

			for (int i = 0; i < lista_file.size(); i++) {
				
				if (lista_file.get(i).contains(CSV_PATH_FILE_EXT)) {
					
					///////////////////////////////////////////////////////////////// LETTURA LISTA
					///////////////////////////////////////////////////////////////// DELLE
					///////////////////////////////////////////////////////////////// INFORMAZIONI
					///////////////////////////////////////////////////////////////// DA OGNI FILE
					BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH_IMPORT + lista_file.get(i)));

					String line = reader.readLine();
					while (line != null) {

						///////////////////////////////////////////////////////// DECODIFICA FILE
						///////////////////////////////////////////////////////// (SISTEMARE IN FUNZIONE
						///////////////////////////////////////////////////////// DEL FILE IN INGRESSO)
						String temp = "";
						for (int j = 0; j < line.length(); j++) {
							if (line.charAt(j) == CSV_CHAR_SEP.charAt(0)) {
								dati_file.add(temp);
								//System.out.println(temp);
								temp = "";
							} else {
								temp += line.charAt(j);
							}
						}
						//System.out.println(temp);
						dati_file.add(temp);
						line = reader.readLine();
					}
					reader.close();
					
					///////////////////////////////////////////////////////// SPOSTAMENTO FILE			 	
					try {
						  
						@SuppressWarnings("resource")
						FileChannel source = new FileInputStream(new File(CSV_PATH_IMPORT + lista_file.get(i))).getChannel();
						@SuppressWarnings("resource")
						FileChannel destination = new FileOutputStream(new File(CSV_PATH_IMPORT_DEST + lista_file.get(i))).getChannel();
						if (destination != null && source != null) {
							destination.transferFrom(source, 0, source.size());
						}
						if (source != null) {
							source.close();
						}
						if (destination != null) {
							destination.close();
						}

						///////////////////////////////////////////// CANCELLAZIONE FILE
						new File(CSV_PATH_IMPORT + lista_file.get(i)).delete();

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			////////////////////////////////////////////////////////////////// Creazione
			////////////////////////////////////////////////////////////////// oggetto
			////////////////////////////////////////////////////////////////// MovimentoIN
			int cursore = 0; 
			 
			for (int i = 0; i < dati_file.size(); i++) {
 
				if (!dati_file.get(i).equals(CSV_KEY_ID_MOV_ORI) && !dati_file.get(i).equals(CSV_KEY_INEPHOS)
						&& !dati_file.get(i).equals(CSV_KEY_ID_MATERIALE)
						&& !dati_file.get(i).equals(CSV_KEY_TIPO_MATERIALE)
						&& !dati_file.get(i).equals(CSV_KEY_QUANTITA) && !dati_file.get(i).equals(CSV_KEY_COD_INGRESSO)
						&& !dati_file.get(i).equals(CSV_KEY_COD_OPERATORE)
						&& !dati_file.get(i).equals(CSV_KEY_OPERAZIONE)
						&& !dati_file.get(i).equals(CSV_KEY_PROCEDURA_ADOTTATA)
						&& !dati_file.get(i).equals(CSV_KEY_TIPO_MOV) && !dati_file.get(i).equals(CSV_KEY_DESCRI_MOVE)
						&& !dati_file.get(i).equals(CSV_KEY_DT_MOV) && !dati_file.get(i).equals(CSV_KEY_SILO)
						&& !dati_file.get(i).equals(CSV_KEY_PESO_TEORICO) && !dati_file.get(i).equals(CSV_KEY_ID_CICLO)
						&& !dati_file.get(i).equals(CSV_KEY_DT_INIZIO_PROCEDURA)
						&& !dati_file.get(i).equals(CSV_KEY_DT_FINE_PROCEDURA)
						&& !dati_file.get(i).equals(CSV_KEY_ABILITATO) && !dati_file.get(i).equals(CSV_KEY_ORIGINE_MOV)
						&& !dati_file.get(i).equals(CSV_KEY_INFO_1) && !dati_file.get(i).equals(CSV_KEY_INFO_2)
						&& !dati_file.get(i).equals(CSV_KEY_INFO_3) && !dati_file.get(i).equals(CSV_KEY_INFO_4)
						&& !dati_file.get(i).equals(CSV_KEY_INFO_5) && !dati_file.get(i).equals(CSV_KEY_INFO_6)
						&& !dati_file.get(i).equals(CSV_KEY_INFO_7) && !dati_file.get(i).equals(CSV_KEY_INFO_8)
						&& !dati_file.get(i).equals(CSV_KEY_INFO_9) && !dati_file.get(i).equals(CSV_KEY_INFO_10)) {

					if (cursore == 0 || cursore == 1 || cursore == 7 || cursore == 8 || cursore == 9 || cursore == 10
							|| cursore == 13 || cursore == 14 || cursore == 15 || cursore == 16 || cursore == 18) {
						cursore++;
					} else if (cursore == 2) {
						movIn.idMateriale.add(Integer.parseInt(dati_file.get(i)));
						cursore = 3;
					} else if (cursore == 3) {
						cursore = 4;
					} else if (cursore == 4) {
						movIn.quantita.add(Integer.parseInt(dati_file.get(i)));
						cursore = 5;
					} else if (cursore == 5) {
						movIn.codIngressoComp.add(dati_file.get(i));
						cursore = 6;
					} else if (cursore == 6) {
						movIn.codOperatore.add(dati_file.get(i));
						cursore = 7;
					} else if (cursore == 11) {
						movIn.dtMov.add(dati_file.get(i));
						cursore = 12;
					} else if (cursore == 12) {
						movIn.silo.add(dati_file.get(i));
						cursore = 13;
					} else if (cursore == 17) {
						movIn.abilitato.add(Integer.parseInt(dati_file.get(i)));
						cursore = 18;
					} else if (cursore == 19) {
						movIn.info1.add(dati_file.get(i));
						cursore = 20;
					} else if (cursore == 20) {
						movIn.info2.add(dati_file.get(i));
						cursore = 21;
					} else if (cursore == 21) {
						movIn.info3.add(dati_file.get(i));
						cursore = 22;
					} else if (cursore == 22) {
						movIn.info4.add(dati_file.get(i));
						cursore = 23;
					} else if (cursore == 23) {
						movIn.info5.add(dati_file.get(i));
						cursore = 24;
					} else if (cursore == 24) {
						movIn.info6.add(dati_file.get(i));
						cursore = 25;
					} else if (cursore == 25) {
						movIn.info7.add(dati_file.get(i));
						cursore = 26;
					} else if (cursore == 26) {
						movIn.info8.add(dati_file.get(i));
						cursore = 27;
					} else if (cursore == 27) {
						movIn.info9.add(dati_file.get(i));
						cursore = 28;
					} else if (cursore == 28) {
						movIn.info10.add(dati_file.get(i));
						cursore = 0;
					}
 

////				if (dati_file.get(i).equals(CSV_KEY_ID_MATERIALE)) {
////					cursore = 0;
////				} else if (dati_file.get(i).equals(CSV_KEY_QUANTITA)) {
////					cursore = 1;
////				} else if (dati_file.get(i).equals(CSV_KEY_COD_INGRESSO)) {
////					cursore = 2;
////				} else if (dati_file.get(i).equals(CSV_KEY_COD_OPERATORE)) {
////					cursore = 3;
////				} else if (dati_file.get(i).equals(CSV_KEY_DT_MOV)) {
////					cursore = 4;
////				} else if (dati_file.get(i).equals(CSV_KEY_SILO)) {
////					cursore = 5;
////				} else if (dati_file.get(i).equals(CSV_KEY_ABILITATO)) {
////					cursore = 6;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_1)) {
////					cursore = 7;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_2)) {
////					cursore = 8;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_3)) {
////					cursore = 9;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_4)) {
////					cursore = 10;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_5)) {
////					cursore = 11;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_6)) {
////					cursore = 12;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_7)) {
////					cursore = 13;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_8)) {
////					cursore = 14;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_9)) {
////					cursore = 15;
////				} else if (dati_file.get(i).equals(CSV_KEY_INFO_10)) {
////					cursore = 16;
////				} 
//
////				if (cursore == 0) {
////					movIn.idMateriale.add(Integer.parseInt(dati_file.get(i)));
////				} else if (cursore == 1) {
////					movIn.quantita.add(Integer.parseInt(dati_file.get(i)));
////				} else if (cursore == 2) {
////					movIn.codIngressoComp.add(dati_file.get(i));
////				} else if (cursore == 3) {
////					movIn.codOperatore.add(dati_file.get(i));
////				} else if (cursore == 4) {
////					movIn.dtMov.add(dati_file.get(i));
////				} else if (cursore == 5) {
////					movIn.silo.add(dati_file.get(i));
////				} else if (cursore == 6) {
////					movIn.abilitato.add(Integer.parseInt(dati_file.get(i)));
////				} else if (cursore == 7) {
////					movIn.info1.add(dati_file.get(i));
////				} else if (cursore == 8) {
////					movIn.info2.add(dati_file.get(i));
////				} else if (cursore == 9) {
////					movIn.info3.add(dati_file.get(i));
////				} else if (cursore == 10) {
////					movIn.info4.add(dati_file.get(i));
////				} else if (cursore == 11) {
////					movIn.info5.add(dati_file.get(i));
////				} else if (cursore == 12) {
////					movIn.info6.add(dati_file.get(i));
////				} else if (cursore == 13) {
////					movIn.info7.add(dati_file.get(i));
////				} else if (cursore == 14) {
////					movIn.info8.add(dati_file.get(i));
////				} else if (cursore == 15) {
////					movIn.info9.add(dati_file.get(i));
////				} else if (cursore == 16) {
////					movIn.info10.add(dati_file.get(i));
////				}
				}
			}
			
			for (int i=0; i<movIn.idMateriale.size(); i++) {
				
				System.out.println("movIn.idMateriale =" + movIn.idMateriale.get(i));
				
			}

			// Registra movimento di Magazzino
			movIn.registraMovimenti();

		} catch (Exception e) {
		}
 
	}
    
    /**
     * Legge informazioni da inserire come movimenti nella tabella 
     * movimenti_sing_mac_ori e restituisce un arrayList con le info
     * 
     *
     */
    public static void EsportaDati4_0() {
    	
    	//////////////////////////////////////////////////////////////////////////////////////ESTRAZIONE DATI PROCESSO
        ArrayList<String> lista_processi = new ArrayList<>(); 
        List<?> resultList = EseguiQuery("FROM ProcessoOri p "
                + "WHERE p.dtProduzione >= '"+ ParametriSingolaMacchina.parametri.get(509)  +"'" ); 
         
        lista_processi.add("ID PROCESSO" + CSV_CHAR_SEP.charAt(0) 
        + "CODIDCE PRODOTTO" + CSV_CHAR_SEP.charAt(0) 
        + "CODICE CHIMICA" + CSV_CHAR_SEP.charAt(0)
        + "CODICE SACCO" + CSV_CHAR_SEP.charAt(0) 
        + "PESO REALE SACCO" + CSV_CHAR_SEP.charAt(0) 
        + "CODICI COMPONENTI PESO" + CSV_CHAR_SEP.charAt(0)
        + "CODICE COLORE" + CSV_CHAR_SEP.charAt(0) 
        + "CODICE CLIENTE" + CSV_CHAR_SEP.charAt(0)
        + "DATA DI PRODUZIONE" + CSV_CHAR_SEP.charAt(0) 
        + "CODICE OPERATORE" +CSV_CHAR_SEP.charAt(0) 
        + "CODICE COMPONENTE IN" + CSV_CHAR_SEP.charAt(0)
        + "TIPO PROCESSO" + CSV_CHAR_SEP.charAt(0) 
        + "INFO 1" + CSV_CHAR_SEP.charAt(0)
        + "INFO 2" +  CSV_CHAR_SEP.charAt(0) 
        + "INFO 3" +  CSV_CHAR_SEP.charAt(0)
        + "INFO 4" +  CSV_CHAR_SEP.charAt(0) 
        + "INFO 5" +  CSV_CHAR_SEP.charAt(0)
        + "INFO 6" +  CSV_CHAR_SEP.charAt(0) 
        + "INFO 7" +  CSV_CHAR_SEP.charAt(0)
        + "INFO 8" +  CSV_CHAR_SEP.charAt(0) 
        + "INFO 9" +  CSV_CHAR_SEP.charAt(0)
        + "INFO 10");
        
        for (Object o : resultList) {
            ProcessoOri processo = (ProcessoOri) o;
           lista_processi.add(processo.getIdProcesso() + " " + CSV_CHAR_SEP.charAt(0) + processo.getCodProdotto()
					+ CSV_CHAR_SEP.charAt(0) + processo.getCodChimica() + CSV_CHAR_SEP.charAt(0)
					+ processo.getCodSacco() + CSV_CHAR_SEP.charAt(0) + processo.getPesoRealeSacco()
					+ CSV_CHAR_SEP.charAt(0) + processo.getCodCompPeso() + CSV_CHAR_SEP.charAt(0)
					+ processo.getCodColore() + CSV_CHAR_SEP.charAt(0) + processo.getCliente() + CSV_CHAR_SEP.charAt(0)
					+ processo.getDtProduzione() + CSV_CHAR_SEP.charAt(0) + processo.getCodOperatore()
					+ CSV_CHAR_SEP.charAt(0) + processo.getCodCompIn() + CSV_CHAR_SEP.charAt(0)
					+ processo.getTipoProcesso() + CSV_CHAR_SEP.charAt(0) + processo.getInfo1() + CSV_CHAR_SEP.charAt(0)
					+ processo.getInfo2() + CSV_CHAR_SEP.charAt(0) + processo.getInfo3() + CSV_CHAR_SEP.charAt(0)
					+ processo.getInfo4() + CSV_CHAR_SEP.charAt(0) + processo.getInfo5() + CSV_CHAR_SEP.charAt(0)
					+ processo.getInfo6() + CSV_CHAR_SEP.charAt(0) + processo.getInfo7() + CSV_CHAR_SEP.charAt(0)
					+ processo.getInfo8() + CSV_CHAR_SEP.charAt(0) + processo.getInfo9() + CSV_CHAR_SEP.charAt(0)
					+ processo.getInfo10());
		} 
         
        //////////////////////////////////////////////////////////////////////////////////////SCRITTURA SU FILE .csv
        String s = (new SimpleDateFormat(CSV_PATH_EXPORT_DATE_FORMAT)).format(new Date());
        
		try (PrintWriter writer = new PrintWriter(new File(CSV_PATH_EXPORT + CSV_PATH_EXPORT_PROD +s + CSV_PATH_FILE_EXT))) {
			StringBuilder sb = new StringBuilder(); 
			for (int i=0; i<lista_processi.size(); i++) { 
				sb.append(lista_processi.get(i)); 
				sb.append("\n");  
			}
			writer.write(sb.toString());  

		} catch (FileNotFoundException e) { 
			SessionLogger.logger.severe("Errore durante la scrittura del file ="+ e);
		}
 
		//////////////////////////////////////////////////////////////////////////////////////ESTRAZIONE DATI MOVIMENTI
		ArrayList<String> lista_movimenti = new ArrayList<>();
		resultList = EseguiQuery("FROM MovimentoSingMacOri m " + "WHERE m.dtFineProcedura >= '"
				+ ParametriSingolaMacchina.parametri.get(509) + "'");
 
		lista_movimenti.add( 
				"ID MOVIMENTO" + CSV_CHAR_SEP.charAt(0) +
				"ID MOVIMENTO INEPHOS" +  CSV_CHAR_SEP.charAt(0) +
				"ID MATERIALE" +  CSV_CHAR_SEP.charAt(0) +
				"TIPO MATERIALE" + CSV_CHAR_SEP.charAt(0) +
				"QUANTITA" + CSV_CHAR_SEP.charAt(0) +
				"CODICE INGRESSO COMPONENTE" +  CSV_CHAR_SEP.charAt(0) +
				"CODICE OPERATORE" +  CSV_CHAR_SEP.charAt(0) +
				"OPERAZIONE" +  CSV_CHAR_SEP.charAt(0) +
				"PROCEDURA ADOTTATA" +  CSV_CHAR_SEP.charAt(0) +
				"TIPO MOVIMENTO" +  CSV_CHAR_SEP.charAt(0) +
				"DESCRIZIONE MOVIMENTO" +  CSV_CHAR_SEP.charAt(0) +
				"DATA MOVIMENTO" +  CSV_CHAR_SEP.charAt(0) +
				"SILO" +  CSV_CHAR_SEP.charAt(0) +
				"PESO TEORICO" + CSV_CHAR_SEP.charAt(0) +
				"ID CICLO" +  CSV_CHAR_SEP.charAt(0) +
				"INIZIO PROCEDURA" +  CSV_CHAR_SEP.charAt(0) +
				"FINE PROCEDURA" +  CSV_CHAR_SEP.charAt(0) +
				"ABILITATO" +  CSV_CHAR_SEP.charAt(0) +
				"ORIGINE MOVIMENTO" +  CSV_CHAR_SEP.charAt(0) + 
				"INFO 1" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 2" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 3" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 4" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 5" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 6" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 7" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 8" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 9" +  CSV_CHAR_SEP.charAt(0) +
				"INFO 10" +  CSV_CHAR_SEP.charAt(0) );
		 
		for (Object o : resultList) {
			MovimentoSingMacOri movimenti = (MovimentoSingMacOri) o;
			lista_movimenti.add( 
					movimenti.getIdMovOri()  + " " + CSV_CHAR_SEP.charAt(0) +
					movimenti.getIdMovInephos() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getIdMateriale() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getTipoMateriale() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getQuantita() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getCodIngressoComp()+ CSV_CHAR_SEP.charAt(0) +
					movimenti.getCodOperatore()+ CSV_CHAR_SEP.charAt(0) +
					movimenti.getOperazione() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getProceduraAdottata() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getTipoMov() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getDescriMov() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getDtMov() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getSilo() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getPesoTeorico() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getIdCiclo() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getDtInizioProcedura() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getDtFineProcedura() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getAbilitato() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getOrigineMov() + CSV_CHAR_SEP.charAt(0) + 
					movimenti.getInfo1() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo2() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo3() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo4()+ CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo5() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo6() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo7() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo8() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo9() + CSV_CHAR_SEP.charAt(0) +
					movimenti.getInfo10() + CSV_CHAR_SEP.charAt(0) );
		}

		//////////////////////////////////////////////////////////////////////////////////////SCRITTURA SU FILE .csv
		s = (new SimpleDateFormat(CSV_PATH_EXPORT_DATE_FORMAT)).format(new Date());

		try (PrintWriter writer = new PrintWriter(
				new File(CSV_PATH_EXPORT + CSV_PATH_EXPORT_MOV + s + CSV_PATH_FILE_EXT))) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < lista_movimenti.size(); i++) {
				sb.append(lista_movimenti.get(i));
				sb.append("\n");
			}
			writer.write(sb.toString());

		} catch (FileNotFoundException e) {
			SessionLogger.logger.severe("Errore durante la scrittura del file =" + e);
		}
		
		//////////////////////////////////////////////////////////////////////////////////////AGGIORNAMENTO DATA ULTIMA ESPORTAZIONE DATI
		AggiornaValoreParSingMacOri(509,  (new SimpleDateFormat(CSV_PATH_DB_EXPORT_DATE_FORMAT)).format(new Date())); 
        
    	
    }
    
    /**
     * Scarica i file in uscita e scarica i file in 
     * ingresso dalla cartella della macchina sull'FTP 
     * @throws FTPDataTransferException 
     * @throws FTPAbortedException 
     * @throws FTPListParseException 
     * 
     *
     */
    public static void SynchroFileFTP4_0() throws FTPDataTransferException, FTPAbortedException, FTPListParseException {

		////////////////////////
		// DOWNLOAD FILE FTP ///
		////////////////////////

		ArrayList<String> dati_connessione = TrovaDettagliFTPMacchina();

		FTPClient client = new FTPClient();
		
		try {

			// Connessione al server Ftp
			client.connect(TrovaIpFtp());

			// Accesso all'FTP
			client.login(dati_connessione.get(0), dati_connessione.get(1));

			// Configurazione del client
			client.setPassive(true); // Server passivo
			client.setType(FTPClient.TYPE_BINARY);

			// Download lato macchina
			Collection<String> downloadedFiles = new ArrayList<>();

			try {

				// Spostamento nella cartella da cui scaricare i files
				client.changeDirectory(FTP_DOWNLOAD_DIR_PATH);

				// Prendiamo la lista dei file da scaricare
				FTPFile[] list = null;
				list = client.list();

				for (FTPFile f : list) {

					String destFileName = CSV_PATH_IMPORT + f.getName();

					if (f.getType() == FTPFile.TYPE_FILE) {

						client.download(f.getName(), new java.io.File(destFileName), new FTPFileTranferingListener());

						// Alla fine aggiorniamo il vettore risultato
						downloadedFiles.add(destFileName);

						// Eliminiamo il file dal FTP
						client.deleteFile(f.getName());

					} else {
						if (f.getType() == FTPFile.TYPE_DIRECTORY) {
							SessionLogger.logger.log(Level.WARNING,
									"Download file 4_0 - Errore Trovata Directory : " + f.getName());
						}
						if (f.getType() == FTPFile.TYPE_LINK) {
							SessionLogger.logger.log(Level.WARNING,
									"Download file 4_0 - Errore Trovato file : " + f.getName());
						}
					}
				}

				SessionLogger.logger.log(Level.WARNING, "Download file 4_0 - COMPLETATO METODO downloadFileFromFTP!");

			} catch (IllegalStateException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : IMPOSSIBILE EFFETTUARE IL DOWNLOAD DAL FTP");
				throw ex;
			} catch (IOException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : IMPOSSIBILE EFFETTUARE IL DOWNLOAD DAL FTP");
				throw ex;
			} catch (FTPIllegalReplyException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : IMPOSSIBILE EFFETTUARE IL DOWNLOAD DAL FTP");
				throw ex;
			} catch (FTPException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : IMPOSSIBILE EFFETTUARE IL DOWNLOAD DAL FTP");
				throw ex;
			} catch (FTPDataTransferException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : IMPOSSIBILE EFFETTUARE IL TRASFERIMENTO DEI FILE");
				throw ex;
			} catch (FTPAbortedException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : CONNESSIONE CON FTP INTERROTTA");
				throw ex;
			} catch (FTPListParseException ex) {
				SessionLogger.logger.log(Level.WARNING,
						"Download file 4_0 - Exception " + ex + " : IMPOSSIBILE EFFETTUARE IL DOWNLOAD DEI FILE");
				throw ex;
			}

			///////////////////////
			// UPLOAD FILE FTP ///
			///////////////////////

			client.changeDirectory(FTP_UPLOAD_DIR_PATH);

			File[] list = new File(CSV_PATH_EXPORT).listFiles();
			for (int i = 0; i < list.length; i++) {
				if (!list[i].isDirectory() && !list[i].getName().startsWith(".")) {

					///////////////////////////////////////////////////////// UPLOAD FILE
					client.upload(new java.io.File(CSV_PATH_EXPORT + list[i].getName()),
							new FTPFileTranferingListener());

					///////////////////////////////////////////// CANCELLAZIONE FILE
					new File(CSV_PATH_EXPORT + list[i].getName()).delete();
				}
			}

			client.disconnect(true);

		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException ex) {
			SessionLogger.logger.log(Level.WARNING, "Download file 4_0 - Exception " + ex + " : Errore di CONNESSIONE");
		}
	}
}

