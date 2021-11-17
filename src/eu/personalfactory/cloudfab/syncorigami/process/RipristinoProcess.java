/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.process;

import eu.personalfactory.cloudfab.syncorigami.utils.RipristinoUtils;
import eu.personalfactory.cloudfab.syncorigami.utils.MachineCredentials;
import eu.personalfactory.cloudfab.syncorigami.utils.SyncOrigamiConstants;
import eu.personalfactory.cloudfab.syncorigami.utils.UpdaterUtils;
import eu.personalfactory.cloudfab.syncorigami.utils.DataManagerM;
import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoOri;
import eu.personalfactory.cloudfab.macchina.entity.MacchinaOri;
import eu.personalfactory.cloudfab.macchina.gestore.password.GestorePassword;
import eu.personalfactory.cloudfab.macchina.panels.Pannello12_Aggiornamento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaDettagliFTPMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ENTITY_MANAGER_FACTORY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MACCHINA_DATA_SQL_RIPRISTINO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MACCHINA_ZIP_PASSWORD;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_USB_RIPRISTINO_TEMP;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ComunicationException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.MachineCredentialsNotFoundException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.MacchinaOriJpaController;
import it.sauronsoftware.ftp4j.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXBException;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;

/**
 *
 * @author Marilisa Tassone
 */
public class RipristinoProcess {

    private static Logger log = Logger.getLogger(RipristinoProcess.class);

    /**
     * Metodo che genera un file sql contenente un backup del database origamidb
     * Il file viene poi caricato sul server FTP nella cartella backup
     * dell'utente server
     *
     * @param EntityManagerFactory
     * @param Pannello19_Aggiornamento
     * @throws InitializeException
     * @throws ProcessException
     * @throws ComunicationException
     *
     */
    public static void uploadBackupZeroSQL(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InitializeException,
            ProcessException,
            ComunicationException,
            IOException,
            InterruptedException {

        //##########################################################################
        //####################### FASE DI INIZIALIZZAZIONE #########################
        //##########################################################################
        //ISTANZIA IL DATAMANAGER
        DataManagerM dataManager;
        try {
            dataManager = new DataManagerM(SyncOrigamiConstants.BKP_ZERO_FILE_PFX, emf);
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("IMPOSSIBILE RECUPERARE LE CREDENZIALI DELLA MACCHINA!!!");
        }
        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc;
        try {
            mc = dataManager.getMachineCredentials();
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }

        //##########################################################################
        //############## COSTRUZIONE DEL NOME DEL FILE DI SCAMBIO ##################
        //##########################################################################
        //COSTRUISCE IL NOME DEL FILE IN BASE ALLA DATA CORRENTE ED ALLA VERSIONE      
        String nomeFileOut;
        try {
            nomeFileOut = UpdaterUtils.generaNomeFileOut(dataManager.getMachineCredentials(),
                    SyncOrigamiConstants.BKP_ZERO_FILE_PFX);
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }

        //##########################################################################
        //################## COSTRUZIONE FILE SQL DI SCAMBIO #######################
        //##########################################################################
        //COSTRUISCE IL FILE SQL E LO METTE NELLA 
        //DIRECTORY CORRISPONDENTE
        String completePathToXMLFileOut = null;
        try {
//      log.info("Username db : " + GestorePassword.userName());
//      log.info("Password db : " + GestorePassword.passWord());

            //COSTRUISCE IL FILE XML
            completePathToXMLFileOut = RipristinoUtils.createBackupSQL(
                    SyncOrigamiConstants.MACCHINA_DB_NAME,
                    GestorePassword.userName(),
                    GestorePassword.passWord(),
                    nomeFileOut,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_GENERATE_DIR);

        } catch (IOException ex) {
            log.error("ERRORE NELLA CREAZIONE DEL FILE BCK0" + completePathToXMLFileOut + "\n" + ex.getMessage());
            throw ex;
        } catch (InterruptedException ex) {
            log.error("ERRORE NELLA CREAZIONE DEL FILE BCK0" + completePathToXMLFileOut + "\n" + ex.getMessage());
            throw ex;
        }
        //Definisco un oggetto aggiornamento
        AggiornamentoOri aggiornamentoOri = new AggiornamentoOri();

        aggiornamentoOri.setNomeFile(completePathToXMLFileOut);
        aggiornamentoOri.setTipo(SyncOrigamiConstants.BKP_ZERO_FILE_PFX);
        aggiornamentoOri.setDtAggiornamento(new Date());
        aggiornamentoOri.setVersione(mc.getLastUpdateVersion() + 1);

        //##########################################################################
        //#################### COSTRUZIONE FILE COMPRESSO ##########################
        //##########################################################################
        //COMPRIME IL FILE XML E LO METTE NELLA DIRECTORY CORRISPONDENTE
        String completePathToXMLCompressedFileOut = null;
        try {
            //nomeFileOut è senza estensione
            completePathToXMLCompressedFileOut = UpdaterUtils.compress(
                    nomeFileOut + ".sql",
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_GENERATE_DIR,
                    nomeFileOut,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                    mc.getZipPassword());

        } catch (ZipException ex) {
            log.error(ex);
            throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE COMPRESSO" + completePathToXMLFileOut + "\n");
        }

        log.info("completePathToXMLCompressedFileOut : " + completePathToXMLCompressedFileOut);

        //##########################################################################
        //##################### UPLOAD IN CARTELLA REMOTA DI BACKUP ################
        //##########################################################################
//    log.info("SyncOrigamiConstants.SERVER_FTP_SERVER_USER : " + SyncOrigamiConstants.SERVER_FTP_SERVER_USER);
//    log.info("SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD : " + SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD);
        log.info("SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR : " + SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR);
//    log.info("SyncOrigamiConstants.FTP_SERVER_NAME : " + SyncOrigamiConstants.FTP_SERVER_NAME);
        log.info("SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR : " + SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR);
        log.info("SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR : " + SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);
        log.info("SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_BKP_DIR : " + SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_BKP_DIR);

        //FA L'UPLOAD DEL FILE NELLA CARTELLA REMOTA (HOME DELLA MACCHINA) 
        String completePathTransferedFileOut;
        try {
            completePathTransferedFileOut = UpdaterUtils.uploadToFTP(
                    SyncOrigamiConstants.SERVER_FTP_SERVER_USER,
                    SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                    nomeFileOut + ".zip",
                    SyncOrigamiConstants.FTP_SERVER_NAME,
                    SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_BKP_DIR,//cartella di backup
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);

            log.info("completePathTransferedFileOut" + completePathTransferedFileOut);
        } catch (IOException ex) {
            throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + ex);
        } catch (FTPDataTransferException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + completePathToXMLCompressedFileOut);
        } catch (FTPAbortedException ex) {
            log.error(ex);
            throw new ComunicationException("COMUNICAZIONE INTERROTTA!!!");
        } catch (FTPIllegalReplyException ex) {
            log.error(ex);
            throw new ComunicationException("IL SERVER FTP NON RISPONDE CORRETTAMENTE!!!");
        } catch (FTPException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE FTP!!!" + ex.getMessage());
        }

        //###########################################################################
        //#################### REGISTRAZIONE AGGIORNAMENTO SUL DB ###################
        //###########################################################################
        //Se il processo di upload è andato a buon fine si registra l'aggiornamento nella 
        //tabella aggiornamento_ori di origamidb
//        dataManager.salvaAggiornamentoOriOut(aggiornamentoOri, emf);
//
    }

    /**
     * Metodo che genera un file xml contenente tutti i dati presenti su
     * origamidb Il file viene poi caricato sul server FTP nella cartella backup
     * dell'utente server
     *
     * @param EntityManagerFactory
     * @param Pannello19_Aggiornamento
     * @throws InitializeException
     * @throws ProcessException
     * @throws ComunicationException
     *
     */
    public static void uploadBackupXML(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InitializeException,
            ProcessException,
            ComunicationException {

        //##########################################################################
        //####################### FASE DI INIZIALIZZAZIONE #########################
        //##########################################################################
        //ISTANZIA IL DATAMANAGER
        DataManagerM dataManager;
        try {
            dataManager = new DataManagerM(SyncOrigamiConstants.BKP_FILE_PFX, emf);
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("IMPOSSIBILE RECUPERARE LE CREDENZIALI DELLA MACCHINA!!!");
        }

        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc;
        try {
            mc = dataManager.getMachineCredentials();
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }
        //##########################################################################
        //####################### GENERAZIONE AGGIORNAMENTO ########################
        //##########################################################################

        AggiornamentoOri aggiornamentoOri = dataManager.costruisciBackupDbMacchina(SyncOrigamiConstants.BKP_FILE_PFX, emf);

        //##########################################################################
        //############## COSTRUZIONE DEL NOME DEL FILE DI SCAMBIO ##################
        //##########################################################################
        //COSTRUISCE IL NOME DEL FILE IN BASE ALLA DATA CORRENTE ED ALLA VERSIONE      
        String nomeFileOut;
        try {
            nomeFileOut = UpdaterUtils.generaNomeFileOut(dataManager.getMachineCredentials(), SyncOrigamiConstants.BKP_FILE_PFX);
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }
        //##########################################################################
        //################## COSTRUZIONE FILE XML DI SCAMBIO #######################
        //##########################################################################

        //COSTRUISCE IL FILE XML A PARTIRE DA AggiornamentoORI E LO METTE NELLA 
        //DIRECTORY CORRISPONDENTE
        String completePathToXMLFileOut = null;
        try {
            //COSTRUISCE IL FILE XML
            completePathToXMLFileOut = UpdaterUtils.createDataTransferXML(
                    aggiornamentoOri,
                    nomeFileOut,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_GENERATE_DIR,
                    SyncOrigamiConstants.MACCHINA_JAXB_OUTPUT_PACKAGE_NAME);
            log.info("FILE GENERATO CON SUCCESSO: " + completePathToXMLFileOut);
        } catch (FileNotFoundException ex) {
            log.error("ERRORE NELLA CREAZIONE DEL FILE " + completePathToXMLFileOut + "\n" + ex.getMessage());
            throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE (Errore di creazione)" + completePathToXMLFileOut + "\n");
        } catch (JAXBException ex) {
            log.error("ERRORE NELLA GENERAZIONE DEL FILE (Errore JAXB)" + completePathToXMLFileOut + "\n" + ex.getMessage());
            throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE (Errore JAXB)" + completePathToXMLFileOut + "\n");
        }
        //Setto il nome del file nell'oggetto AggiornamentoOri
        aggiornamentoOri.setNomeFile(completePathToXMLFileOut);

        //##########################################################################
        //#################### COSTRUZIONE FILE COMPRESSO ##########################
        //##########################################################################
        //COMPRIME IL FILE XML E LO METTE NELLA DIRECTORY CORRISPONDENTE
        String completePathToXMLCompressedFileOut = null;
        try {

            //nomeFileOut è senza estensione
            completePathToXMLCompressedFileOut = UpdaterUtils.compress(
                    nomeFileOut + ".xml",
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_GENERATE_DIR,
                    nomeFileOut,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                    mc.getZipPassword());

        } catch (IOException ex) {
            log.error(ex);
            throw new ProcessException("ERRORE NELL' ELIMINAZIONE DEL FILE " + nomeFileOut + ".xml" + "\n");

        } catch (ZipException ex) {
            log.error(ex);
            throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE COMPRESSO" + completePathToXMLFileOut + "\n");
        }

        log.info("completePathToXMLCompressedFileOut : " + completePathToXMLCompressedFileOut);

        //##########################################################################
        //##################### UPLOAD IN CARTELLA REMOTA DI BACKUP ################
        //##########################################################################
        log.info("SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR :" + SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR);
        log.info("SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR :" + SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR);
        log.info("SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR : " + SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);
        log.info("SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_BKP_DIR : " + SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_BKP_DIR);

        //FA L'UPLOAD DEL FILE NELLA CARTELLA REMOTA (HOME DELLA MACCHINA)
        String completePathTransferedFileOut;

        try {
            completePathTransferedFileOut = UpdaterUtils.uploadToFTP(
                    SyncOrigamiConstants.SERVER_FTP_SERVER_USER,
                    SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                    nomeFileOut + ".zip",
                    SyncOrigamiConstants.FTP_SERVER_NAME,
                    SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_BKP_DIR,//cartella di backup
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);
            log.info("completePathTransferedFileOut :" + completePathTransferedFileOut);
        } catch (IOException ex) {
            throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + ex);
        } catch (FTPDataTransferException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + completePathToXMLCompressedFileOut);
        } catch (FTPAbortedException ex) {
            log.error(ex);
            throw new ComunicationException("COMUNICAZIONE INTERROTTA!!!");
        } catch (FTPIllegalReplyException ex) {
            log.error(ex);
            throw new ComunicationException("IL SERVER FTP NON RISPONDE CORRETTAMENTE!!!");
        } catch (FTPException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE FTP!!!" + ex.getMessage());
        }

        //###########################################################################
        //#################### REGISTRAZIONE AGGIORNAMENTO SUL DB ###################
        //###########################################################################
        //Se il processo di upload è andato a buon fine si registra l'aggiornamento nella 
        //tabella aggiornamento_ori di origamidb
//        dataManager.salvaAggiornamentoOriOut(aggiornamentoOri, emf);
//
//        panelAgg.inserisciRiga("Salvataggio di un backup xml in uscita sul db locale"); //("Salvataggio di un backup xml in uscita sul db locale");
    }

    /**
     *
     * Metodo che prende un file di backup zero nella cartella Ripristino del
     * progetto decomprime il file e ripristina il backup
     *
     * @param emf
     * @return
     *
     */
    public static boolean ripristinaBKPZeroFromDir(EntityManagerFactory emf) {

        //Inizializzo il percorso completo ed il nome dell'eventuale file di backup zero
        //String nomeFile = null;
        boolean fileBkpEiminato = false;
        boolean ripristinoEffettuato = false;
        try {

            //##################################################################
            //############### RICERCA FILE NELLA CARTELLA Ripristino ###########
            //##################################################################
            log.info("Ricerca di un file di backup0 nella cartella Ripristino");

            String fileBackupZero = RipristinoUtils.findFileFromDirRipristino(MACCHINA_DATA_SQL_RIPRISTINO);

            //##########################################################################
            //############### DECOMPRESSIONE DEL BACKUP ################################
            //##########################################################################
            //SE ESISTE UN FILE DI BACKUP ZERO (SCARICATO DA FTP OPPURE TROVATO IN Ripristino)
            //SI DECOMPRIMO E SI RIPRISTINA IL FILE SQL DI BKP   
            if (fileBackupZero != null) {
                
                //SI DECOMPRIME NELLA STESSA CARTELLA RIPRISTINO
                //NOTA : la password per decomprimere viene letta dal file di properties di fabCloud
                String fileDestCompletePathSql = null;

                try {
                    fileDestCompletePathSql = UpdaterUtils.uncompress(fileBackupZero,
                            fileBackupZero.replace(".zip", ".sql"),
                            MACCHINA_DATA_SQL_RIPRISTINO,
                            MACCHINA_ZIP_PASSWORD,
                            MACCHINA_DATA_SQL_RIPRISTINO);

                } catch (ZipException ex) {
                    log.error(ex);
                    throw new ProcessException("ERRORE NEL DECOMPRIMERE IL FILE " + fileBackupZero);
                }

                log.info("fileBackupZero :" + fileBackupZero);
                log.info("fileDestCompletePathSql :" + fileDestCompletePathSql);//Percorso al file .sql

                //##########################################################################
                //################### RIPRISTINO DEL BACKUP ################################
                //##########################################################################
                ripristinoEffettuato = RipristinoUtils.restoreDB(
                        GestorePassword.userName(),
                        GestorePassword.passWord(),
                        fileDestCompletePathSql);

                //##########################################################################
                //### ELIMINAZIONE DEI FILE DALLA CARTELLA Ripristino ######################
                //##########################################################################
                File dir = new File(MACCHINA_DATA_SQL_RIPRISTINO);
                File[] list = dir.listFiles();
                for (File list1 : list) {
                    fileBkpEiminato = list1.delete();
                    //NOTA : I file vengono effetivamento rimossi solo dopo del termine del programma
                }

            } else {
                log.info("FILE BKP0 NON TROVATO NELLA CARTELLA Ripristino");

            }
            //LOG DELLE OPERAZIONI EFFETTUATE
            if (ripristinoEffettuato) {
                log.info("RIPRISTINATO BACKUP!"); //("RIPRISTINO COMPLETATO");
            } else {
                log.error("IMPOSSIBILE RIPRISTINARE IL BACKUP!");
            }
            if (fileBkpEiminato) {
                log.info("FILE DI BKP0 RIMOSSO DALLA CARTELLA Ripristino!");
            } else {
                log.error("RIMOZIONE DEL FILE DI BKP0 FALLITA!");
            }
        } catch (ProcessException | IOException | InterruptedException ex) {
            log.error("Errore \"ripristinaBKPZeroFromDir\" - ex:" + ex);
        }

        return ripristinoEffettuato;

    }

    /**
     *
     * Metodo che prende un file di backup zero nella cartella Ripristino del
     * progetto decomprime il file e ripristina il backup
     *
     * @param EntityManagerFactory
     * @param backupDirName
     * @param Pannello19_Aggiornamento
     * @throws InitializeException
     * @throws ComunicationException
     * @throws ProcessException
     *
     */
    public static boolean ripristinaInitDatabaseFromDir(EntityManagerFactory emf, String backupDirName)
            throws InitializeException,
            ComunicationException,
            ProcessException,
            Exception {

        boolean fileBkpEiminato = false;
        boolean ripristinoEffettuato = false;

        //##################################################################
        //############### RICERCA FILE NELLA CARTELLA  #####################
        //##################################################################
        log.info("Ricerca di un file di backup0 nella cartella " + backupDirName);

        String fileBackupZero = RipristinoUtils.findFileFromDirRipristino(backupDirName);

        //##########################################################################
        //############### DECOMPRESSIONE DEL BACKUP ################################
        //##########################################################################
        //SI DECOMPRIMO E SI RIPRISTINA IL FILE SQL DI BKP   
        if (fileBackupZero != null) {

            //SI DECOMPRIME NELLA STESSA CARTELLA 
            //NOTA : la password per decomprimere viene letta dal file di properties di fabCloud
            String fileDestCompletePathSql = null;

            try {

                //System.err.println("FabCloudConstants.PATH_USB_RIPRISTINO_TEMP  =" + FabCloudConstants.PATH_USB_RIPRISTINO_TEMP);
                fileDestCompletePathSql = UpdaterUtils.uncompress_alternative(fileBackupZero,
                        fileBackupZero.replace(".zip", ".sql"),
                        PATH_USB_RIPRISTINO_TEMP,
                        //backupDirName,
                        MACCHINA_ZIP_PASSWORD,
                        backupDirName);

            } catch (ZipException ex) {
                log.error(ex);
                throw new ProcessException("ERRORE NEL DECOMPRIMERE IL FILE " + fileBackupZero);
            }

            //##########################################################################
            //################### RIPRISTINO DEL BACKUP ################################
            //##########################################################################
            ripristinoEffettuato = RipristinoUtils.restoreDB(
                    GestorePassword.userName(),
                    GestorePassword.passWord(),
                    fileDestCompletePathSql);

            //##########################################################################
            //### ELIMINAZIONE DEI FILE DALLA CARTELLA DI ORIGINE ######################
            //##########################################################################
            File dir = new File(PATH_USB_RIPRISTINO_TEMP);
            File[] list = dir.listFiles();
            for (File list1 : list) {
                fileBkpEiminato = list1.delete();
                //NOTA : I file vengono effetivamento rimossi solo dopo del termine del programma
            }

        }

        return ripristinoEffettuato;
    }

    /**
     * Metodo che si collega al ftp scarica il jar ed il bkp e li salva sulla
     * macchina
     *
     * @param emf
     * @throws MachineCredentialsNotFoundException
     * @throws InitializeException
     * @throws ComunicationException
     * @throws ProcessException
     */
    public static void downloadInitFilesFromFTP(EntityManagerFactory emf)
            throws MachineCredentialsNotFoundException, InitializeException, ComunicationException, ProcessException {

        //Inizializzo il percorso completo ed il nome dell'eventuale file di backup zero
        String nomeFile = null;
        String fileBackupZero = null;

        boolean fileBkpEiminato = false;
        boolean ripristinoEffettuato = false;

        //##########################################################################
        //####################### FASE DI INIZIALIZZAZIONE #########################
        //##########################################################################
        //Devo recuperare le credenziali per accedere al server FTP nella tabella macchina_ori
        MacchinaOriJpaController mjc = new MacchinaOriJpaController(null, emf);
        MacchinaOri m = mjc.findMacchinaOri();
        if (m == null) {
            log.error("IMPOSSIBILE RECUPERARE LE CREDENZIALI PER ACCEDERE AL FTP DAL DATABASE!!!");
            throw new MachineCredentialsNotFoundException("IMPOSSIBILE RECUPERARE LE CREDENZIALI PER ACCEDERE AL FTP!!!");
        }

//    log.info("m.getFtpPassword() : " + m.getFtpPassword());
//    log.info("m.getFtpUser() : " + m.getFtpUser());
        try {
            SyncOrigamiConstants.loadRipristinoPropertiesFromDb(ENTITY_MANAGER_FACTORY);
        } catch (InitializeException ex) {
            log.error(ex.toString());
            throw new InitializeException("ERRORE NEL CARICARE LE PROPRIETA' DAL DB " + ex);
        }

        //##########################################################################
        //################### CONNESSIONE AL SERVER FTP ############################
        //##########################################################################
        FTPClient downloadClient = null;
        try {
            downloadClient = UpdaterUtils.connettiClientFTP(m.getFtpUser(),
                    m.getFtpPassword(),
                    SyncOrigamiConstants.FTP_SERVER_NAME);
        } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE DURANTE LA CONNESSIONE AL SERVER FTP " + ex);
        }

        //##########################################################################
        //################### DOWNLOAD DALLA CARTELLA REMOTA DEL JAR ###############
        //##########################################################################
        log.info("####### @@@@@ TENTATIVO DI DOWNLOAD DEL JAR ##########");
        boolean downloadJarEffettuato = false;
        Collection<String> downloadedjarFiles = null;
        try {
            downloadedjarFiles = UpdaterUtils.downloadFileFromFTP(downloadClient,
                    SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_JAR_DIR,//sft
                    SyncOrigamiConstants.MACCHINA_JAR_FILE_IN_DOWNLOADED_DIR);
            downloadJarEffettuato = true;

        } catch (IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException | FTPListParseException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE NEL DOWNLOAD DALLA CARTELLA: " + SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_BKP0_DIR + " " + ex);
        }

        if (downloadedjarFiles == null) {

            log.info("####### NESSUN FILE JAR SCARICATO DAL FTP ##########");

        } else {

            for (String jF : downloadedjarFiles) {
                log.info("####### SCARICATO FILE JAR DAL FTP ##########");
                log.info("jarFile :" + jF);

            }

        }

        //##########################################################################
        //############# DOWNLOAD DALLA CARTELLA REMOTA DEL BACKUP ##################
        //##########################################################################
        log.info("####### @@@@@ TENTATIVO DI DOWNLOAD DEL BKP ##########");
        Collection<String> downloadedBkpFiles = null;
        try {
            //Attenzione il clientFTP dopo aver scaricato il jar si trova nella cartella sft
            //quindi per spostarsi nella cartella bkp0 bisogna aggiungere al path "../"
            downloadedBkpFiles = UpdaterUtils.downloadFileFromFTP(downloadClient,
                    "../" + SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_BKP0_DIR,//bkp0
                    SyncOrigamiConstants.MACCHINA_DATA_SQL_RIPRISTINO);
        } catch (IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException | FTPListParseException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE NEL DOWNLOAD DALLA CARTELLA: " + SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_BKP0_DIR + " " + ex);
        }

        if (downloadedBkpFiles == null) {

            log.info("####### NESSUN FILE DI BACKUP SCARICATO DAL FTP ##########");

        } else {

            for (String bF : downloadedBkpFiles) {
                log.info("####### SCARICATO FILE DI BACKUP DAL FTP ##########");
                log.info("file bkp :" + bF);

            }
        }
        UpdaterUtils.disconnettiClient(downloadClient);
        log.info("Client FTP disconnesso");
    }

    /**
     *
     * Metodo che scarica un file di backup zero dal server FTP se non trova il
     * file sul FTP lo cerca nella cartella Ripristino del progetto decomprime
     * il file e ripristina il backup
     *
     * @param EntityManagerFactory
     * @param Pannello19_Aggiornamento
     * @throws InitializeException
     * @throws ComunicationException
     * @throws ProcessException
     *
     */
//  public static void ripristinaBKPZeroFromFTP(EntityManagerFactory emf)
//          throws InitializeException,
//          ComunicationException,
//          ProcessException,
//          Exception {
//
//    //Inizializzo il percorso completo ed il nome dell'eventuale file di backup zero
//    String nomeFile = null;
//    String fileBackupZero = null;
//
//    boolean fileBkpEiminato = false;
//    boolean fileBkpEiminatoFTP = false;
//    boolean ripristinoEffettuato = false;
//
//    //##########################################################################
//    //####################### FASE DI INIZIALIZZAZIONE #########################
//    //##########################################################################
//
//    //Devo recuperare le credenziali per accedere al server FTP nella tabella macchinaOri
//    MacchinaOriJpaController mjc = new MacchinaOriJpaController(null, emf);
//    MacchinaOri m = mjc.findMacchinaOri();
//    if (m == null) {
//      log.error("IMPOSSIBILE RECUPERARE LE CREDENZIALI PER ACCEDERE AL FTP DAL DATABASE!!!");
//      throw new MachineCredentialsNotFoundException("IMPOSSIBILE RECUPERARE LE CREDENZIALI PER ACCEDERE AL FTP!!!");
//    }
//
////    log.info("m.getFtpPassword() : " + m.getFtpPassword());
////    log.info("m.getFtpUser() : " + m.getFtpUser());
//
//    try {
//      SyncOrigamiConstants.loadRipristinoPropertiesFromDb(FabCloudConstants.ENTITY_MANAGER_FACTORY);
//    } catch (InitializeException ex) {
//      log.error(ex.toString());
//      throw new InitializeException("ERRORE NEL CARICARE LE PROPRIETA' DAL DB " + ex);
//    }
//
//
//    log.info("RICERCA DI UN FILE DI RIPRISTINO SUL FTP"); //("RICERCA DI UN FILE DI RIPRISTINO SUL FTP");
//
//    //##########################################################################
//    //################### DOWNLOAD DALLA CARTELLA REMOTA #######################
//    //##########################################################################
//    Collection<String> downloadedFiles = null;
//    try {
//
//      //In realtà il file di backup è unico ma conviene usare il metodo generale
//      downloadedFiles = UpdaterUtils.downloadAllFromFTP(
//              m.getFtpUser(),
//              m.getFtpPassword(),
//              SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_BKP0_DIR,//bkp0
//              SyncOrigamiConstants.FTP_SERVER_NAME,
//              SyncOrigamiConstants.MACCHINA_DATA_SQL_RIPRISTINO);//MacchinaFTPDownloadIn
//
//    } catch (FileNotFoundException ex) {
//      log.error(ex);
//      throw new InitializeException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } catch (FTPDataTransferException ex) {
//      log.error(ex);
//      throw new ComunicationException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } catch (FTPAbortedException ex) {
//      log.error(ex);
//      throw new ComunicationException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } catch (FTPIllegalReplyException ex) {
//      log.error(ex);
//      throw new ComunicationException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } catch (IllegalStateException ex) {
//      log.error(ex);
//      throw new ComunicationException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } catch (FTPException ex) {
//      log.error(ex);
//      throw new ComunicationException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } catch (IOException ex) {
//      log.error(ex);
//      throw new ComunicationException("ERRORE NEL DOWNLOAD DEL FILE " + ex);
//    } finally {
//
//
//      //Se è stato scaricato il file dal server FTP 
//      //estraggo il nome del file e vado ad eliminarlo sul FTP
//      //TODO : Aggiungere un controllo sul numero di file trovati
//      for (String s : downloadedFiles) {
//        fileBackupZero = s;
//        nomeFile = RipristinoUtils.estraiNomeFileSenzaEstenzione(fileBackupZero);
//
//      }
//
//      //##########################################################################
//      //############### RIMOZIONE DEL FILE DAL FTP ###############################
//      //##########################################################################      
//
//
//      if (fileBackupZero != null) {
//
//        //Elimino il file dalla cartella bkp0 del server FTP
//        fileBkpEiminatoFTP = RipristinoUtils.removeFileFTP(
//                m.getFtpUser(),
//                m.getFtpPassword(),
//                SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_BKP0_DIR,//bkp0
//                SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_DIR_OLD,
//                SyncOrigamiConstants.FTP_SERVER_NAME,
//                nomeFile + ".zip");
//
//
//        fileBackupZero = RipristinoUtils.findFileFromDirRipristino(SyncOrigamiConstants.MACCHINA_DATA_SQL_RIPRISTINO);
//
//
//        //##########################################################################
//        //############### DECOMPRESSIONE DEL BACKUP ################################
//        //##########################################################################
//
//
//
//        //SI DECOMPRIME NELLA STESSA CARTELLA RIPRISTINO
//        //NOTA : la password per decomprimere viene letta dal file di properties di fabCloud
//        String fileDestCompletePathSql = null;
//
//        try {
//          fileDestCompletePathSql = UpdaterUtils.uncompress(
//                  fileBackupZero,
//                  fileBackupZero.replace(".zip", ".sql"),
//                  SyncOrigamiConstants.MACCHINA_DATA_SQL_RIPRISTINO,
//                  m.getZipPassword(),
//                  SyncOrigamiConstants.MACCHINA_DATA_SQL_RIPRISTINO);
//
//        } catch (ZipException ex) {
//          log.error(ex);
//          throw new ProcessException("ERRORE NEL DECOMPRIMERE IL FILE " + fileBackupZero);
//        }
//
//        log.info("fileBackupZero :" + fileBackupZero);
//        log.info("fileDestCompletePathSql :" + fileDestCompletePathSql);//Percorso al file .sql
//
//
//        //##########################################################################
//        //################### RIPRISTINO DEL BACKUP ################################
//        //##########################################################################
//
//        ripristinoEffettuato = RipristinoUtils.restoreDB(
//                GestorePassword.userName(),
//                GestorePassword.passWord(),
//                fileDestCompletePathSql);
//
//        //##########################################################################
//        //### ELIMINAZIONE DEI FILE DALLA CARTELLA Ripristino ######################
//        //##########################################################################
//
//        File dir = new File(SyncOrigamiConstants.MACCHINA_DATA_SQL_RIPRISTINO);
//        File[] list = dir.listFiles();
//        for (int i = 0; i < list.length; i++) {
//          fileBkpEiminato = list[i].delete();
//          //NOTA : I file vengono effetivamento rimossi solo dopo del termine del programma
//        }
//        //END if file not null 
//
//        //LOG DELLE OPERAZIONI EFFETTUATE
//        if (ripristinoEffettuato) {
//          log.info("RIPRISTINATO BACKUP!"); //("RIPRISTINO COMPLETATO");
//        } else {
//          log.error("IMPOSSIBILE RIPRISTINARE IL BACKUP!");
//        }
//        if (fileBkpEiminatoFTP) {
//          log.info("FILE DI BKP0 RIMOSSO DAL SERVER FTP!");
//        } else {
//          log.error("RIMOZIONE DEL FILE DI BKP0 DAL FTP FALLITA!");
//        }
//        if (fileBkpEiminato) {
//          log.info("FILE DI BKP0 RIMOSSO DALLA CARTELLA Ripristino!");
//        } else {
//          log.error("RIMOZIONE DEL FILE DI BKP0 FALLITA!");
//        }
//
//      } else {
//        log.info("FILE DI BKP0 NON TROVATO SUL SERVER FTP");
//      }
//    }//END FINALLY download
//
//  }
    /**
     * Cerca nella cartella dist/log della macchina il file di log
     * dell'aggiornamento e lo carica sul server FTP
     *
     * @param emf
     * @param panelAgg
     * @param outLogFilePfx
     * @param logFileName
     * @param outDirFtp
     * @throws InitializeException
     * @throws ProcessException
     * @throws ComunicationException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void uploadSyncLogFile(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg,
            String outLogFilePfx,
            String logFileName,
            String outDirFtp)
            throws InitializeException,
            ProcessException,
            ComunicationException,
            IOException,
            InterruptedException {

        //##########################################################################
        //####################### FASE DI INIZIALIZZAZIONE #########################
        //##########################################################################
        panelAgg.inserisciRiga("<html>" + "Invio file di log in corso..." + "</html>"); //("Invio di un file di backup in corso...");

        File fileLog = new File(SyncOrigamiConstants.MACCHINA_LOG_DIR + File.separator + logFileName);

        panelAgg.inserisciRiga("<html>" + "Recupero file di log");

        //##########################################################################
        //##################### UPLOAD IN CARTELLA REMOTA DI BACKUP ################
        //##########################################################################
        //FA L'UPLOAD DEL FILE NELLA CARTELLA REMOTA (HOME DELLA MACCHINA)
        String completePathTransferedFileOut = null;
        try {
            completePathTransferedFileOut = UpdaterUtils.uploadToFTP(
                    SyncOrigamiConstants.SERVER_FTP_SERVER_USER,
                    SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD,
                    SyncOrigamiConstants.MACCHINA_JAR_DIR + File.separator + SyncOrigamiConstants.MACCHINA_LOG_DIR,
                    fileLog.getName(),
                    SyncOrigamiConstants.FTP_SERVER_NAME,
                    outDirFtp,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);
            log.info("completePathTransferedFileOut" + completePathTransferedFileOut);

        } catch (IOException ex) {
            throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + ex);
        } catch (FTPDataTransferException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + completePathTransferedFileOut);
        } catch (FTPAbortedException ex) {
            log.error(ex);
            throw new ComunicationException("COMUNICAZIONE INTERROTTA!!!");
        } catch (FTPIllegalReplyException ex) {
            log.error(ex);
            throw new ComunicationException("IL SERVER FTP NON RISPONDE CORRETTAMENTE!!!");
        } catch (FTPException ex) {
            log.error(ex);
            throw new ComunicationException("ERRORE FTP!!!" + ex.getMessage());
        }

        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 359, ParametriSingolaMacchina.parametri.get(111))); //("Upload del file sul server FTP");

        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 361, ParametriSingolaMacchina.parametri.get(111))); //("INVIO COMPLETATO");

    }

    /**
     * Metodo che invia i file di log presenti nella cartella dist/log e li
     * carica sul FTP nella cartella log dell'utente server (aggiornamento)
     *
     * @param emf
     * @param panelAgg
     * @param outLogFilePfx
     * @param logFileName
     * @param outDirFtp
     * @throws InitializeException
     * @throws ProcessException
     * @throws ComunicationException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void uploadAllLogFile(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InitializeException,
            ProcessException,
            ComunicationException,
            IOException,
            InterruptedException {

        String prefissoNomeFile = null;
        //##########################################################################
        //################ RECUPERO IDENTITA' DELLA MACCHINA #######################
        //##########################################################################

        //ISTANZIA IL DATAMANAGER
        DataManagerM dataManager;
        try {
            dataManager = new DataManagerM(SyncOrigamiConstants.SYNC_FILE_LOG_PFX, emf);
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("IMPOSSIBILE RECUPERARE LE CREDENZIALI DELLA MACCHINA!!!");
        }

        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc;
        try {
            mc = dataManager.getMachineCredentials();
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }

        //######################################################################
        //########## RECUPERO DEI FILE DI LOG ##################################
        //######################################################################
        Collection<File> logFiles = null;
        logFiles = RipristinoUtils.findFileFromDirLog(SyncOrigamiConstants.MACCHINA_JAR_DIR
                + File.separator + SyncOrigamiConstants.MACCHINA_LOG_DIR
                + File.separator);

        //######################################################################
        //# PER CIASCUN FILE GENERIAMO IL NOME,COMPRIMIAMO E CARICHIAMO SU FTP #
        //######################################################################
        int contFile = 0;

        for (File f : logFiles) {
            log.info("INIZIO TRASFERIMENTO FILE (f.getName()): " + f.getName());

            //############## COSTRUZIONE DEL NOME DEL FILE DI SCAMBIO ##############
//
//      if (f.getName().equals(SyncOrigamiConstants.SYNCORIGAMI_LOG_FILE_NAME)) {
//        prefissoNomeFile = SyncOrigamiConstants.SYNC_FILE_LOG_PFX;
//      }
//      if (f.getName().equals(SyncOrigamiConstants.PROCESSO_LOG_FILE_NAME)) {
//        prefissoNomeFile = SyncOrigamiConstants.PROC_FILE_LOG_PFX;
//      }
            //COSTRUISCE IL NOME DEL FILE IN BASE ALLA DATA CORRENTE ED ALLA VERSIONE      
            String nomeFileOut;
            try {
                nomeFileOut = UpdaterUtils.generaNomeFileOut(dataManager.getMachineCredentials(), f.getName() //                prefissoNomeFile
                );
            } catch (MachineCredentialsNotFoundException ex) {
                log.error(ex);
                throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
            }

            //######################################################################
            //#################### COSTRUZIONE FILE COMPRESSO ######################
            //######################################################################
            //COMPRIME IL FILE DI LOG E LO METTE NELLA DIRECTORY MacchinaXMLDataZipOut
            String completePathToXMLCompressedFileOut = null;
            try {
                //nomeFileOut è senza estensione
                completePathToXMLCompressedFileOut = UpdaterUtils.compress(
                        f.getName(),
                        // GRAVE SVISTA             SyncOrigamiConstants.SYNCORIGAMI_LOG_FILE_NAME,
                        SyncOrigamiConstants.MACCHINA_JAR_DIR + File.separator + SyncOrigamiConstants.MACCHINA_LOG_DIR,
                        nomeFileOut,
                        SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                        mc.getZipPassword());

            } catch (ZipException ex) {
                log.error(ex);
                throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE COMPRESSO" + nomeFileOut + "\n");
            }

            log.info("completePathToXMLCompressedFileOut : " + completePathToXMLCompressedFileOut);

            //#####################################################################
            //######### UPLOAD NELLA CARTELLA REMOTA DEL FTP DI LOG ###############
            //#####################################################################
            String completePathTransferedFileOut = null;
            try {
                completePathTransferedFileOut = UpdaterUtils.uploadToFTP(
                        SyncOrigamiConstants.SERVER_FTP_SERVER_USER,
                        SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD,
                        SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                        nomeFileOut + ".zip",
                        SyncOrigamiConstants.FTP_SERVER_NAME,
                        SyncOrigamiConstants.MACCHINA_FTP_LOG_DIR,
                        SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);
                log.info("TRASFERITO FILE completePathTransferedFileOut : " + completePathTransferedFileOut);
                contFile++;

            } catch (IOException ex) {
                throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + ex);
            } catch (FTPDataTransferException ex) {
                log.error(ex);
                throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + completePathTransferedFileOut);
            } catch (FTPAbortedException ex) {
                log.error(ex);
                throw new ComunicationException("COMUNICAZIONE INTERROTTA!!!");
            } catch (FTPIllegalReplyException ex) {
                log.error(ex);
                throw new ComunicationException("IL SERVER FTP NON RISPONDE CORRETTAMENTE!!!");
            } catch (FTPException ex) {
                log.error(ex);
                throw new ComunicationException("ERRORE FTP!!!" + ex.getMessage());
            }

        }//FINE CICLO DEI FILE

        log.info("NUMERO DI FILE TRASFERITI SUL FTP " + contFile);
    }

    /**
     * Metodo che invia i file di backup presenti nella penna usb e li carica
     * sul FTP nella cartella backup dell'utente server (aggiornamento)
     *
     * DI GAUDIO
     *
     *
     * @param emf
     * @param panelAgg
     * @return
     * @throws
     * eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException
     * @throws
     * eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException
     * @throws
     * eu.personalfactory.cloudfab.syncorigami.exceptions.ComunicationException
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static Boolean uploadAllBackupFiles(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InitializeException,
            ProcessException,
            ComunicationException,
            IOException,
            InterruptedException {

        String destFolderName = ParametriGlobali.parametri.get(114);

        boolean result = false;

        //##########################################################################
        //################ RECUPERO IDENTITA' DELLA MACCHINA #######################
        //##########################################################################
        //ISTANZIA IL DATAMANAGER
        DataManagerM dataManager;
        try {

            dataManager = new DataManagerM(SyncOrigamiConstants.SYNC_FILE_LOG_PFX, emf);

        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("uploadAllBackupFiles - IMPOSSIBILE RECUPERARE LE CREDENZIALI DELLA MACCHINA!!!");
        }

        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc;
        try {
            mc = dataManager.getMachineCredentials();
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex);
            throw new InitializeException("uploadAllBackupFiles - ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }
        ArrayList<String> fileList = null;

        try {
            fileList = UpdaterUtils.getListFileFromFTP(TrovaDettagliFTPMacchina().get(0),
                    TrovaDettagliFTPMacchina().get(1),
                    destFolderName,
                    SyncOrigamiConstants.FTP_SERVER_NAME);
        } catch (FTPListParseException | FileNotFoundException | FTPDataTransferException | FTPAbortedException | FTPIllegalReplyException | IllegalStateException | FTPException ex) {
            log.error(ex);
            throw new InitializeException("uploadAllBackupFiles - ERRORE NEL RECUPERARE LA LISTA DEI FILE DI BACKUP PRESENTI SUL SERVER FTP");
        }

        //######################################################################
        //########## RECUPERO DEI FILE DI LOG ##################################
        //######################################################################
        String dirBackUpPath = ParametriSingolaMacchina.parametri.get(375);

        Collection<File> logFiles = new ArrayList<>();

        File dirBackup = new File(dirBackUpPath);

        log.info("uploadAllBackupFiles - Ricerca della lista dei file presenti nella cartella : " + dirBackUpPath);

        File[] list = dirBackup.listFiles();

        if (list != null) {
            for (File list1 : list) {
                if (list1.getName().contains(".zip")) {
                    log.info("uploadAllBackupFiles - Trovato list[i].getPath() : " + list1.getPath());
                    logFiles.add(list1);
                }
            }

            //######################################################################
            //# PER CIASCUN FILE GENERIAMO IL NOME,COMPRIMIAMO E CARICHIAMO SU FTP #
            //######################################################################
            int contFile = 0;

            for (File f : logFiles) {
                log.info("uploadAllBackupFiles - INIZIO TRASFERIMENTO FILE (f.getName()): " + f.getName());

                //#####################################################################
                //######### UPLOAD NELLA CARTELLA REMOTA DEL FTP DI LOG ###############
                //#####################################################################
                String completePathTransferedFileOut = null;
                try {

                    completePathTransferedFileOut = f.getName();

                    if (fileList != null && !fileList.contains(f.getName())) {

                        UpdaterUtils.uploadToFTPNoDelete(TrovaDettagliFTPMacchina().get(0),
                                TrovaDettagliFTPMacchina().get(1),
                                dirBackUpPath,
                                f.getName(),
                                SyncOrigamiConstants.FTP_SERVER_NAME,
                                destFolderName);
                    }
                    log.info("uploadAllBackupFiles - TRASFERITO FILE completePathTransferedFileOut : " + completePathTransferedFileOut);
                    contFile++;

                } catch (IOException ex) {
                    throw new ComunicationException("uploadAllBackupFiles - ERRORE NEL TRASFERIMENTO DEL FILE " + ex);
                } catch (FTPDataTransferException ex) {
                    log.error(ex);
                    throw new ComunicationException("uploadAllBackupFiles - ERRORE NEL TRASFERIMENTO DEL FILE " + completePathTransferedFileOut);
                } catch (FTPAbortedException ex) {
                    log.error(ex);
                    throw new ComunicationException("uploadAllBackupFiles - COMUNICAZIONE INTERROTTA!!!");
                } catch (FTPIllegalReplyException ex) {
                    log.error(ex);
                    throw new ComunicationException("uploadAllBackupFiles - IL SERVER FTP NON RISPONDE CORRETTAMENTE!!!");
                } catch (FTPException ex) {
                    log.error(ex);
                    throw new ComunicationException("uploadAllBackupFiles - ERRORE FTP!!!" + ex.getMessage());
                }

            }//FINE CICLO DEI FILE

            result = true;

            log.info("uploadAllBackupFiles - NUMERO DI FILE TRASFERITI SUL FTP " + contFile);
        }

        return result;
    }
}
