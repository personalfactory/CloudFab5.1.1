/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.utils;

import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.AggiornamentoConfigOriJpaController;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import org.apache.log4j.Logger;

/**
 *
 * @author divinotaras
 */
public class SyncOrigamiConstants {

  private static Logger log = Logger.getLogger(SyncOrigamiConstants.class);
  //COSTANTI DEL FILE DI PROPERTIES
  private static final String SERVER_PFX = "server.";
  private static final String MACCHINA_PFX = "macchina.";
  private static final String SO_WINDOWS_PFX = "windows.";
  private static final String XML_SCHEMA_DEST_FILE_NAME_PROP = "xml.schema.dest.file.name";
  private static final String XML_DATA_OUT_GENERATE_DIR_PROP = "xml.data.out.generate.dir";
  private static final String XML_DATA_OUT_COMPRESS_DIR_PROP = "xml.data.out.compress.dir";
  private static final String XML_DATA_IN_COMPRESS_DIR_PROP = "xml.data.in.compress.dir";
  private static final String XML_DATA_IN_UNCOMPRESS_DIR_PROP = "xml.data.in.uncompress.dir";
  private static final String FTP_SERVER_OUT_DIR_PROP = "ftp.server.out.dir";
  private static final String FTP_SERVER_OUT_BKP_DIR_PROP = "ftp.server.out.bkp.dir";
  private static final String FTP_SERVER_IN_DIR_PROP = "ftp.server.in.dir";
  private static final String FTP_SERVER_IN_BKP_DIR_PROP = "ftp.server.in.bkp.dir";
  private static final String FTP_SERVER_IN_BKP0_DIR_PROP = "ftp.server.in.bkp0.dir";
  private static final String FTP_SERVER_IN_DIR_OLD_PROP = "ftp.server.in.dir.old";
  private static final String XML_DATA_IN_DOWNLOADED_DIR_PROP = "xml.data.in.downloaded.dir";
  private static final String XML_DATA_OUT_TRANSFERED_DIR_PROP = "xml.data.out.transfered.dir";
  //######## NOTIFICHE VIA MAIL #################################################
  private static final String FTP_SERVER_IN_JAR_DIR_PROP = "ftp.server.in.jar.dir";
  private static final String JAR_FILE_IN_DOWNLOADED_DIR_PROP = "jar.file.in.download.dir";
  private static final String MAIL_SENDER_USER_PROP = "mail.sender.user";
  private static final String MAIL_SENDER_PASSWORD_PROP = "mail.sender.password";
  private static final String MAIL_HOST_PROP = "mail.host";
  private static final String MAIL_ADDRESS_SENDER_PROP = "mail.address.sender";
  private static final String MAIL_ADDRESS_RECEIVER_PROP = "mail.address.receiver";
  private static final String MAIL_OBJECT_PROP = "mail.object";
  private static final String MAIL_PATH_FILE_PROP = "mail.path.file";
  //############################################################################
  
  private static final String JAXB_OUTPUT_PACKAGE_NAME_PROP = "jaxb.output.package.name";
  private static final String JAXB_INDEX_FILE_NAME_PROP = "jaxb.index.file.name";
  private static final String FIELD_DELIMITER_PROP = "field.delimiter";
  private static final String FTP_SERVER_NAME_PROP = "ftp.server.name";
  private static final String FTP_SERVER_PORT_PROP = "ftp.server.port";
  private static final String FTP_SERVER_MIRROR_LIST_PROP = "ftp.server.mirror.list";
  private static final String FTP_FILE_SEPARATOR_PROP = "ftp.file.separator";
  private static final String JAXB_INPUT_PACKAGE_NAME_PROP = "jaxb.package.name";
  private static final String XSD_MACCHINA_FILE_PROP = "xsd.macchina.file";
  private static final String XSD_SERVER_FILE_PROP = "xsd.server.file";
  private static final String OUT_FILE_PFX_PROP = "out.file.pfx";
  private static final String IN_FILE_PFX_PROP = "in.file.pfx";
  private static final String BKP_FILE_PFX_PROP = "bkp.file.pfx";
  private static final String BKP_ZERO_FILE_PFX_PROP = "bkp.zero.file.pfx";
  private static final String SERVER_FTP_SERVER_USER_PROP = "server.ftp.server.user";
  private static final String SERVER_FTP_SERVER_PASSWORD_PROP = "server.ftp.server.password";
  private static final String DATA_DEFAULT_PROP = "data.default";
  private static final String DB_NAME_PROP = "db.name";
  private static final String DATA_SQL_RIPRISTINO_PROP = "data.sql.ripristino";
  private static final String SYNCORIGAMI_LOG_FILE_NAME_PROP = "syncorigami.log.file.name";
  private static final String PROCESSO_LOG_FILE_NAME_PROP = "processo.log.file.name";
  private static final String LOG_DIR_PROP = "log.dir";
  private static final String JAR_DIR_PROP = "jar.dir";
  private static final String SYNC_FILE_LOG_PFX_PROP = "sync.file.log.pfx";
  private static final String PROC_FILE_LOG_PFX_PROP = "proc.file.log.pfx";
  private static final String FTP_LOG_DIR_PROP = "ftp.log.dir";
  private static final String LOG4J_FILE_NAME_PROP = "log4j.file.name";
  public static String OUT_FILE_PFX;
  public static String IN_FILE_PFX;
  public static String BKP_FILE_PFX;
  public static String BKP_ZERO_FILE_PFX;
  public static String FIELD_DELIMITER;
  public static String XSD_MACCHINA_FILE_NAME;
  public static String XSD_SERVER_FILE_NAME;
  public static String FTP_SERVER_NAME;
  public static String FTP_SERVER_PORT;
  public static String[] FTP_SERVER_MIRROR_LIST;
  public static String FTP_FILE_SEPARATOR;
  public static String JAXB_INDEX_FILE_NAME;
  public static Date DATA_DEFAULT;
  //VARIABILI INTERNE CHE CONTENGONO I DATI PRESENTI NEL FILE DI PROPERTIES PER IL SERVER
  public static String SERVER_XML_SCHEMA_DEST_FILE_NAME;
  public static String SERVER_XML_DATA_OUT_GENERATE_DIR;
  public static String SERVER_XML_DATA_OUT_COMPRESS_DIR;
  public static String SERVER_XML_DATA_IN_COMPRESS_DIR;
  public static String SERVER_XML_DATA_IN_UNCOMPRESS_DIR;
  //public static String SERVER_PERSISTENCE_UNIT;
  public static String SERVER_JAXB_INPUT_PACKAGE_NAME;
  public static String SERVER_JAXB_OUTPUT_PACKAGE_NAME;
  public static String SERVER_XML_DATA_IN_DOWNLOADED_DIR;
  public static String SERVER_XML_DATA_OUT_TRANSFERED_DIR;
  public static String SERVER_FTP_SERVER_OUT_DIR;
  public static String SERVER_FTP_SERVER_IN_DIR;
  public static String SERVER_FTP_SERVER_IN_BKP_DIR;
  public static String SERVER_FTP_SERVER_IN_DIR_OLD;
  //CREDENZIALI DI ACCESSO ALLA CARTELLA DEL FTP UTENTE SERVER (NON MACCHINA)
  public static String SERVER_FTP_SERVER_USER;
  public static String SERVER_FTP_SERVER_PASSWORD;
  //VARIABILI INTERNE CHE CONTENGONO I DATI PRESENTI NEL FILE DI PROPERTIES PER LA MACCHINA
  public static String MACCHINA_XML_SCHEMA_DEST_FILE_NAME;
  public static String MACCHINA_XML_DATA_OUT_GENERATE_DIR;
  public static String MACCHINA_XML_DATA_OUT_COMPRESS_DIR;
  public static String MACCHINA_XML_DATA_IN_COMPRESS_DIR;
  public static String MACCHINA_XML_DATA_IN_UNCOMPRESS_DIR;
  // public static String MACCHINA_PERSISTENCE_UNIT;
  public static String MACCHINA_JAXB_INPUT_PACKAGE_NAME;
  public static String MACCHINA_JAXB_OUTPUT_PACKAGE_NAME;
  public static String MACCHINA_XML_DATA_IN_DOWNLOADED_DIR;
  public static String MACCHINA_XML_DATA_OUT_TRANSFERED_DIR;
  public static String MACCHINA_FTP_SERVER_OUT_DIR;
  public static String MACCHINA_FTP_SERVER_OUT_BKP_DIR;
  public static String MACCHINA_FTP_SERVER_IN_DIR;
  public static String MACCHINA_FTP_SERVER_IN_DIR_OLD;
  public static String MACCHINA_FTP_SERVER_IN_BKP_DIR;
  public static String MACCHINA_FTP_SERVER_IN_BKP0_DIR;
  public static String MACCHINA_DB_NAME;
  public static String MACCHINA_DATA_SQL_RIPRISTINO;
  public static String MACCHINA_FTP_SERVER_IN_JAR_DIR;
  public static String MACCHINA_JAR_FILE_IN_DOWNLOADED_DIR;
  public static String SYNCORIGAMI_LOG_FILE_NAME;
  public static String PROCESSO_LOG_FILE_NAME;
  public static String MACCHINA_LOG_DIR;
  public static String MACCHINA_JAR_DIR;
  public static String SYNC_FILE_LOG_PFX;
  public static String PROC_FILE_LOG_PFX;
  public static String MACCHINA_FTP_LOG_DIR;
  public static String LOG4J_FILE_NAME;
  public static InputStream xsdM;
  public static InputStream xsdS;
  
  //  VARIABILI UTILI ALL'INVIO DELLE NOTIFICHE PER MAIL
  public static String MAIL_SENDER_USER;
  public static String MAIL_SENDER_PASSWORD;
  public static String MAIL_HOST;
  public static String MAIL_ADDRESS_SENDER;
  public static String MAIL_ADDRESS_RECEIVER;
  public static String MAIL_OBJECT;
  public static String MAIL_PATH_FILE;
  public static String WINDOWS_MAIL_PATH_FILE;
  

  //############################################################################
  //#################### CARICO LE PROPERTIES DAL DB ###########################
  //############################################################################
  /**
   * Metodo che carica le proprietà memorizzate nel db utilizzando il metodo
   * findProperty che fa una ricerca nella tabella aggiornamento_config per
   * ciascun parametro
   *
   * @param EntityManagerFactory
   * @throws InitializeException
   */
  public static void loadMacchinaPropertiesFromDb(EntityManagerFactory emf)
          throws InitializeException {

    log.info("######## INIZIO CARICAMENTO PROPRIETA' DAL DATABASE ########");

    //Memorizzo in una stringa il nome del sistema operativo 
    String os = System.getProperty("os.name");

    AggiornamentoConfigOriJpaController aggiornamentoConfigOriJc = new AggiornamentoConfigOriJpaController(null, emf);

    //loadCommonPropertiesFromDb(emf);

    //Carichiamo i valori dalla tabella aggiornamento_config
    try {
      //Attenzione in questo metodo non possiamo utilizzare il metodo loadCommonPropertiesFromDb come avvine 
      //nel metodo loadServerPropertiesFromDb, perchè il controller utilizzato nel metodo loadCommonPropertiesFromDb
      //è quello del server ( ovvero AggiornamentoConfigJpaController) invece il metodo loadMacchinaPropertiesFromDb
      //usa il controller della macchina
      //quindi carichiamo a mano anche le proprietà comuni

      //######################## Common Properties  ############################

      FIELD_DELIMITER = aggiornamentoConfigOriJc.findProperty(FIELD_DELIMITER_PROP).getValore();
      log.info("FIELD_DELIMITER: " + FIELD_DELIMITER);

      //A seconda del sistema operativo installato sulla macchina devo caricare proprietà diverse
      //ovvero le proprietà definiscono gli stessi percorsi ma con separatori diversi
      if (os.equals("Linux") | os.equals("Mac OS X")) { 

          MACCHINA_JAXB_INPUT_PACKAGE_NAME = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + JAXB_INPUT_PACKAGE_NAME_PROP).getValore();
    	  log.info("MACCHINA_JAXB_INPUT_PACKAGE_NAME = " + MACCHINA_JAXB_INPUT_PACKAGE_NAME);
        
        

        XSD_MACCHINA_FILE_NAME = aggiornamentoConfigOriJc.findProperty(XSD_MACCHINA_FILE_PROP).getValore();
        log.info("XSD_MACCHINA_FILE_NAME: " + XSD_MACCHINA_FILE_NAME);

      } else {
           
           
        MACCHINA_JAXB_INPUT_PACKAGE_NAME = aggiornamentoConfigOriJc.findProperty(SO_WINDOWS_PFX + MACCHINA_PFX + JAXB_INPUT_PACKAGE_NAME_PROP).getValore();
    	log.info("MACCHINA_JAXB_INPUT_PACKAGE_NAME per windows = " + MACCHINA_JAXB_INPUT_PACKAGE_NAME);

        XSD_MACCHINA_FILE_NAME = aggiornamentoConfigOriJc.findProperty(SO_WINDOWS_PFX + XSD_MACCHINA_FILE_PROP).getValore();
        log.info("XSD_MACCHINA_FILE_NAME per windows: " + XSD_MACCHINA_FILE_NAME);
      }

      XSD_MACCHINA_FILE_NAME = aggiornamentoConfigOriJc.findProperty(XSD_MACCHINA_FILE_PROP).getValore();
      log.info("XSD_MACCHINA_FILE_NAME: " + XSD_MACCHINA_FILE_NAME);

      FTP_SERVER_NAME = aggiornamentoConfigOriJc.findProperty(FTP_SERVER_NAME_PROP).getValore();
//            log.info( "FTP_SERVER_NAME = " + FTP_SERVER_NAME);

      FTP_SERVER_PORT = aggiornamentoConfigOriJc.findProperty(FTP_SERVER_PORT_PROP).getValore();
//            log.info( "FTP_SERVER_PORT = " + FTP_SERVER_PORT);

      //USATO PER CARICARE GLI AGGIORNAMENTI NELLA CARTELLA DEL SERVER
      SERVER_FTP_SERVER_USER = aggiornamentoConfigOriJc.findProperty(SERVER_FTP_SERVER_USER_PROP).getValore();
//            log.info( "SERVER_FTP_SERVER_USER = " + SERVER_FTP_SERVER_USER);

      //USATO PER CARICARE GLI AGGIORNAMENTI NELLA CARTELLA DEL SERVER
      SERVER_FTP_SERVER_PASSWORD = aggiornamentoConfigOriJc.findProperty(SERVER_FTP_SERVER_PASSWORD_PROP).getValore();
//            log.info( "SERVER_FTP_SERVER_PASSWORD = " + SERVER_FTP_SERVER_PASSWORD);

      String ftpServerMirrorListStr = aggiornamentoConfigOriJc.findProperty(FTP_SERVER_MIRROR_LIST_PROP).getValore();
      FTP_SERVER_MIRROR_LIST = ftpServerMirrorListStr.split(FIELD_DELIMITER);

      int count = 0;
      for (String s : FTP_SERVER_MIRROR_LIST) {
        log.info("server " + count + " = " + s);
        count++;
      }

      FTP_FILE_SEPARATOR = aggiornamentoConfigOriJc.findProperty(FTP_FILE_SEPARATOR_PROP).getValore();
      log.info("FTP_FILE_SEPARATOR = " + FTP_FILE_SEPARATOR);

      OUT_FILE_PFX = aggiornamentoConfigOriJc.findProperty(OUT_FILE_PFX_PROP).getValore();
      log.info("OUT_FILE_PFX = " + OUT_FILE_PFX);

      IN_FILE_PFX = aggiornamentoConfigOriJc.findProperty(IN_FILE_PFX_PROP).getValore();
      log.info("IN_FILE_PFX = " + IN_FILE_PFX);

      BKP_FILE_PFX = aggiornamentoConfigOriJc.findProperty(BKP_FILE_PFX_PROP).getValore();
      log.info("BKP_FILE_PFX = " + BKP_FILE_PFX);

      BKP_ZERO_FILE_PFX = aggiornamentoConfigOriJc.findProperty(BKP_ZERO_FILE_PFX_PROP).getValore();
      log.info("BKP_ZERO_FILE_PFX = " + BKP_ZERO_FILE_PFX);


      //Formatto la data di default
      DATA_DEFAULT = null;
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
      DATA_DEFAULT = sdf.parse(aggiornamentoConfigOriJc.findProperty(DATA_DEFAULT_PROP).getValore());
      log.info("DATA_DEFAULT: " + DATA_DEFAULT);
      //########################################################################


      MACCHINA_XML_SCHEMA_DEST_FILE_NAME = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_SCHEMA_DEST_FILE_NAME_PROP).getValore();
      log.info("MACCHINA_XML_SCHEMA_DEST_FILE_NAME = " + MACCHINA_XML_SCHEMA_DEST_FILE_NAME);

      MACCHINA_XML_DATA_OUT_GENERATE_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_OUT_GENERATE_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_OUT_GENERATE_DIR = " + MACCHINA_XML_DATA_OUT_GENERATE_DIR);

      MACCHINA_XML_DATA_OUT_COMPRESS_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_OUT_COMPRESS_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_OUT_COMPRESS_DIR = " + MACCHINA_XML_DATA_OUT_COMPRESS_DIR);

      MACCHINA_XML_DATA_IN_COMPRESS_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_IN_COMPRESS_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_IN_COMPRESS_DIR = " + MACCHINA_XML_DATA_IN_COMPRESS_DIR);

      MACCHINA_XML_DATA_IN_UNCOMPRESS_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_IN_UNCOMPRESS_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_IN_UNCOMPRESS_DIR = " + MACCHINA_XML_DATA_IN_UNCOMPRESS_DIR);

      MACCHINA_FTP_SERVER_OUT_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_OUT_DIR_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_OUT_DIR = " + MACCHINA_FTP_SERVER_OUT_DIR);

      MACCHINA_FTP_SERVER_OUT_BKP_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_OUT_BKP_DIR_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_OUT_BKP_DIR = " + MACCHINA_FTP_SERVER_OUT_BKP_DIR);

      MACCHINA_FTP_SERVER_IN_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_IN_DIR_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_IN_DIR = " + MACCHINA_FTP_SERVER_OUT_DIR);

      MACCHINA_FTP_SERVER_IN_BKP0_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_IN_BKP0_DIR_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_IN_BKP0_DIR = " + MACCHINA_FTP_SERVER_IN_BKP0_DIR);

      MACCHINA_FTP_SERVER_IN_DIR_OLD = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_IN_DIR_OLD_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_IN_DIR_OLD = " + MACCHINA_FTP_SERVER_IN_DIR_OLD);

      MACCHINA_XML_DATA_IN_DOWNLOADED_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_IN_DOWNLOADED_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_IN_DOWNLOADED_DIR = " + MACCHINA_XML_DATA_IN_DOWNLOADED_DIR);

      MACCHINA_XML_DATA_OUT_TRANSFERED_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_OUT_TRANSFERED_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_OUT_TRANSFERED_DIR = " + MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);

      MACCHINA_JAXB_OUTPUT_PACKAGE_NAME = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + JAXB_OUTPUT_PACKAGE_NAME_PROP).getValore();
      log.info("MACCHINA_JAXB_OUTPUT_PACKAGE_NAME = " + MACCHINA_JAXB_OUTPUT_PACKAGE_NAME);

      MACCHINA_DB_NAME = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + DB_NAME_PROP).getValore();
      log.info("MACCHINA_DB_NAME = " + MACCHINA_DB_NAME);

      MACCHINA_DATA_SQL_RIPRISTINO = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + DATA_SQL_RIPRISTINO_PROP).getValore();
      log.info("MACCHINA_DATA_SQL_RIPRISTINO = " + MACCHINA_DATA_SQL_RIPRISTINO);

      SYNCORIGAMI_LOG_FILE_NAME = aggiornamentoConfigOriJc.findProperty(SYNCORIGAMI_LOG_FILE_NAME_PROP).getValore();
      log.info("SYNCORIGAMI_LOG_FILE_NAME = " + SYNCORIGAMI_LOG_FILE_NAME);

      PROCESSO_LOG_FILE_NAME = aggiornamentoConfigOriJc.findProperty(PROCESSO_LOG_FILE_NAME_PROP).getValore();
      log.info("PROCESSO_LOG_FILE_NAME = " + PROCESSO_LOG_FILE_NAME);

      MACCHINA_LOG_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + LOG_DIR_PROP).getValore();
      log.info("MACCHINA_LOG_DIR = " + MACCHINA_LOG_DIR);

      MACCHINA_JAR_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + JAR_DIR_PROP).getValore();
      log.info("MACCHINA_JAR_DIR = " + MACCHINA_JAR_DIR);

      SYNC_FILE_LOG_PFX = aggiornamentoConfigOriJc.findProperty(SYNC_FILE_LOG_PFX_PROP).getValore();
      log.info("SYNC_FILE_LOG_PFX = " + SYNC_FILE_LOG_PFX);

      PROC_FILE_LOG_PFX = aggiornamentoConfigOriJc.findProperty(PROC_FILE_LOG_PFX_PROP).getValore();
      log.info("PROC_FILE_LOG_PFX = " + PROC_FILE_LOG_PFX);

      MACCHINA_FTP_LOG_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_LOG_DIR_PROP).getValore();
      log.info("FTP_LOG_DIR = " + MACCHINA_FTP_LOG_DIR);

      LOG4J_FILE_NAME = aggiornamentoConfigOriJc.findProperty(LOG4J_FILE_NAME_PROP).getValore();
      log.info("LOG4J_FILE_NAME = " + LOG4J_FILE_NAME);
       
      //Modifica Di Gaudio CloudFab5.0
      MACCHINA_JAXB_INPUT_PACKAGE_NAME = "eu/personalfactory/cloudfab/macchina/entity";
      MACCHINA_JAXB_OUTPUT_PACKAGE_NAME = "eu.personalfactory.cloudfab.macchina.entity";
        

    } catch (NoResultException ex) {
      throw new InitializeException("ERRORE NEL RECUPERARE LE PROPERTIES DAL DB");
    } catch (NonUniqueResultException ex) {
      throw new InitializeException("ERRORE NEL RECUPERARE LE PROPERTIES DAL DB");
    } catch (ParseException ex) {
      throw new InitializeException("ERRORE DI DEFINIZIONE DELLA DATA DI DEFAULT");
    }

    //############################## Common Properties #########################

    xsdM = UpdaterUtils.class.getClassLoader().getResourceAsStream(XSD_MACCHINA_FILE_NAME);

    if (xsdM != null) {
      log.info("FILE " + XSD_MACCHINA_FILE_NAME + " TROVATO");
    } else {
      throw new InitializeException("FILE " + XSD_MACCHINA_FILE_NAME + " NON TROVATO!!!");
    }

    //###########################################################################


  }

  /**
   * Metodo che carica solo le proprietà utili alla procedura di ripristino del
   * bkp0
   *
   * @param emf
   * @throws InitializeException
   */
  public static void loadRipristinoPropertiesFromDb(EntityManagerFactory emf)
          throws InitializeException {

    log.info("######## INIZIO CARICAMENTO PROPRIETA' DI RIPRISTINO DAL DATABASE ########");

    AggiornamentoConfigOriJpaController aggiornamentoConfigOriJc = new AggiornamentoConfigOriJpaController(null, emf);

    //Carichiamo i valori dalla tabella aggiornamento_config
    try {


      FTP_SERVER_NAME = aggiornamentoConfigOriJc.findProperty(FTP_SERVER_NAME_PROP).getValore();
      log.info("FTP_SERVER_NAME = " + FTP_SERVER_NAME);

      MACCHINA_FTP_SERVER_IN_BKP0_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_IN_BKP0_DIR_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_IN_BKP0_DIR = " + MACCHINA_FTP_SERVER_IN_BKP0_DIR);

      MACCHINA_FTP_SERVER_IN_DIR_OLD = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_IN_DIR_OLD_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_IN_DIR_OLD = " + MACCHINA_FTP_SERVER_IN_DIR_OLD);

      MACCHINA_XML_DATA_IN_DOWNLOADED_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + XML_DATA_IN_DOWNLOADED_DIR_PROP).getValore();
      log.info("MACCHINA_XML_DATA_IN_DOWNLOADED_DIR = " + MACCHINA_XML_DATA_IN_DOWNLOADED_DIR);

      MACCHINA_DB_NAME = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + DB_NAME_PROP).getValore();
      log.info("MACCHINA_DB_NAME = " + MACCHINA_DB_NAME);

      MACCHINA_DATA_SQL_RIPRISTINO = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + DATA_SQL_RIPRISTINO_PROP).getValore();
      log.info("MACCHINA_DATA_SQL_RIPRISTINO = " + MACCHINA_DATA_SQL_RIPRISTINO);

      FTP_FILE_SEPARATOR = aggiornamentoConfigOriJc.findProperty(FTP_FILE_SEPARATOR_PROP).getValore();
      log.info("FTP_FILE_SEPARATOR = " + FTP_FILE_SEPARATOR);

      //################## MODIFICHE 15-MAggio-2014 ###################################
      MACCHINA_FTP_SERVER_IN_JAR_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_SERVER_IN_JAR_DIR_PROP).getValore();
      log.info("MACCHINA_FTP_SERVER_IN_JAR_DIR = " + MACCHINA_FTP_SERVER_IN_JAR_DIR);

      MACCHINA_JAR_FILE_IN_DOWNLOADED_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + JAR_FILE_IN_DOWNLOADED_DIR_PROP).getValore();
      log.info("MACCHINA_JAR_IN_DOWNLOADED_DIR = " + MACCHINA_JAR_FILE_IN_DOWNLOADED_DIR);
      //##################################################################################



    } catch (NoResultException ex) {
      throw new InitializeException("ERRORE NEL RECUPERARE LE PROPERTIES DAL DB");
    } catch (NonUniqueResultException ex) {
      throw new InitializeException("ERRORE NEL RECUPERARE LE PROPERTIES DAL DB");
    }

  }

  public static void loadMailPropertiesFromDb(EntityManagerFactory emf)
          throws InitializeException {

    log.info("######## INIZIO CARICAMENTO PROPRIETA' MAIL DAL DATABASE ########");

    //Memorizzo in una stringa il nome del sistema operativo 
    String os = System.getProperty("os.name");

    AggiornamentoConfigOriJpaController aggiornamentoConfigOriJc = new AggiornamentoConfigOriJpaController(null, emf);

    //Carichiamo i valori dalla tabella aggiornamento_config
    try {


      MAIL_SENDER_USER = aggiornamentoConfigOriJc.findProperty(MAIL_SENDER_USER_PROP).getValore();
      log.info("MAIL_SENDER_USER = " + MAIL_SENDER_USER);
      
      MAIL_SENDER_PASSWORD = aggiornamentoConfigOriJc.findProperty(MAIL_SENDER_PASSWORD_PROP).getValore();
      log.info("MAIL_SENDER_PASSWORD = " + MAIL_SENDER_PASSWORD);
      
      MAIL_HOST = aggiornamentoConfigOriJc.findProperty(MAIL_HOST_PROP).getValore();
      log.info("MAIL_HOST = " + MAIL_HOST);
      
      MAIL_ADDRESS_SENDER = aggiornamentoConfigOriJc.findProperty(MAIL_ADDRESS_SENDER_PROP).getValore();
      log.info("MAIL_ADDRESS_SENDER = " + MAIL_ADDRESS_SENDER);
      
      MAIL_ADDRESS_RECEIVER = aggiornamentoConfigOriJc.findProperty(MAIL_ADDRESS_RECEIVER_PROP).getValore();
      log.info("MAIL_ADDRESS_RECEIVER = " + MAIL_ADDRESS_RECEIVER);
      
      MAIL_OBJECT = aggiornamentoConfigOriJc.findProperty(MAIL_OBJECT_PROP).getValore();
      log.info("MAIL_OBJECT = " + MAIL_OBJECT);
      
      MAIL_PATH_FILE = aggiornamentoConfigOriJc.findProperty(MAIL_PATH_FILE_PROP).getValore();
      log.info("MAIL_PATH_FILE = " + MAIL_PATH_FILE);

      //A seconda del sistema operativo installato sulla macchina devo caricare proprietà diverse
      //ovvero le proprietà definiscono gli stessi percorsi ma con separatori diversi
      if (os.equals("Linux") | os.equals("Mac OS X")) {

        MAIL_PATH_FILE = aggiornamentoConfigOriJc.findProperty(MAIL_PATH_FILE_PROP).getValore();
      log.info("MAIL_PATH_FILE per Linux= " + MAIL_PATH_FILE);

      } else {

         MAIL_PATH_FILE = aggiornamentoConfigOriJc.findProperty(SO_WINDOWS_PFX+MAIL_PATH_FILE_PROP).getValore();
      log.info("WMAIL_PATH_FILE per Windows= " + MAIL_PATH_FILE);
      }
    
      //########################################################################

      SYNCORIGAMI_LOG_FILE_NAME = aggiornamentoConfigOriJc.findProperty(SYNCORIGAMI_LOG_FILE_NAME_PROP).getValore();
      log.info("SYNCORIGAMI_LOG_FILE_NAME = " + SYNCORIGAMI_LOG_FILE_NAME);

      PROCESSO_LOG_FILE_NAME = aggiornamentoConfigOriJc.findProperty(PROCESSO_LOG_FILE_NAME_PROP).getValore();
      log.info("PROCESSO_LOG_FILE_NAME = " + PROCESSO_LOG_FILE_NAME);

      MACCHINA_LOG_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + LOG_DIR_PROP).getValore();
      log.info("MACCHINA_LOG_DIR = " + MACCHINA_LOG_DIR);

      MACCHINA_JAR_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + JAR_DIR_PROP).getValore();
      log.info("MACCHINA_JAR_DIR = " + MACCHINA_JAR_DIR);

      SYNC_FILE_LOG_PFX = aggiornamentoConfigOriJc.findProperty(SYNC_FILE_LOG_PFX_PROP).getValore();
      log.info("SYNC_FILE_LOG_PFX = " + SYNC_FILE_LOG_PFX);

      PROC_FILE_LOG_PFX = aggiornamentoConfigOriJc.findProperty(PROC_FILE_LOG_PFX_PROP).getValore();
      log.info("PROC_FILE_LOG_PFX = " + PROC_FILE_LOG_PFX);

      MACCHINA_FTP_LOG_DIR = aggiornamentoConfigOriJc.findProperty(MACCHINA_PFX + FTP_LOG_DIR_PROP).getValore();
      log.info("FTP_LOG_DIR = " + MACCHINA_FTP_LOG_DIR);

      LOG4J_FILE_NAME = aggiornamentoConfigOriJc.findProperty(LOG4J_FILE_NAME_PROP).getValore();
      log.info("LOG4J_FILE_NAME = " + LOG4J_FILE_NAME);


    } catch (NoResultException | NonUniqueResultException ex) {
      throw new InitializeException("ERRORE NEL RECUPERARE LE PROPRIETA' PER L'INVIO DELLE NOTIFICHE MAIL DAL DB");
    }




  }
}