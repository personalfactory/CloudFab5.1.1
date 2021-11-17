/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.process;

import eu.personalfactory.cloudfab.macchina.panels.Pannello12_Aggiornamento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ENTITY_MANAGER_FACTORY;
import eu.personalfactory.cloudfab.syncorigami.exceptions.DatiAggiornamentoNotFoundException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ComunicationException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateContentException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateTypeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateVersionException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.NonValidParamException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class StartSync {

    /**
     * @param args the command line arguments args[0] definisce il
     * sottoprogramma da caricare args[1..n] parametri 1...n dei programmi
     * args[1]= idMacchina
     *
     * ATTENZIONE: le eccezioni non vanno più rilanciate, ma gestite (Es.
     * salvare l'errore nel db) TOGLIERE DAL THROWS del metodo main!!!!!!
     *
     * Per chiamare il programma sulla macchina basta importare la libreria
     * SyncOrigami e chiamare i metodi statici : es.
     * StartSync.downloadAggiornamentiDaServer() es.
     * StartSync.uploadAggiornamentiDaSingolaMacchina() Per richiamare il
     * programma da shell #java -jar SyncOrigamiMacchina.jar
     * uploadAggiornamentoDaServerPerMacchina #java -jar SyncOrigamiMacchina.jar
     * downloadAggiornamentiDaMacchine 1
     */
    private static Logger log = Logger.getLogger(StartSync.class);
    private static final String DOWNLOAD_AGGIORNAMENTI_DA_SERVER = "downloadAggiornamentiDaServer";
    private static final String UPLOAD_AGGIORNAMENTO_DA_MACCHINA_PER_SERVER = "uploadAggiornamentoDaMacchinaPerServer";
    private static final String UPLOAD_BACKUP_DA_MACCHINA_PER_SERVER = "uploadBackupDaMacchinaPerServer";
//  private static final String macchinaPersistenceUnit = "MACCHINA_PU";
    private static final EntityManagerFactory emf = ENTITY_MANAGER_FACTORY;

    public static void main(String[] args,
            Pannello12_Aggiornamento panelAgg)
            throws
            NonValidParamException,
            ProcessException,
            Exception {

//##############################################################################
//############ CONTROLLO SUL PRIMO PARAMETRO DI INPUT ##########################
//##############################################################################

        if (!(args[0].equals(UPLOAD_AGGIORNAMENTO_DA_MACCHINA_PER_SERVER))//solo per test
                && !(args[0].equals(DOWNLOAD_AGGIORNAMENTI_DA_SERVER))//solo per test
                && !(args[0].equals(UPLOAD_BACKUP_DA_MACCHINA_PER_SERVER)))//solo per test
        {
            throw new NonValidParamException("ERRORE SUL PRIMO ARGOMENTO :IL NOME DEL METODO DA ESEGUIRE NON E' VALIDO!");
        }

        //##########################################################################
        //### CARICA SUL FTP UN AGGIORN PER IL SERVER PROVENIENTE DALLA MACCHINA ###
        //##########################################################################

        // Gli if servono solo per il test, sulla mcchina vengono chiamati direttamente i metodi statici
        if (args[0].equals(UPLOAD_AGGIORNAMENTO_DA_MACCHINA_PER_SERVER)) {

            try {

                uploadAggiornamentoDaMacchinaPerServer(emf, panelAgg);

            } catch (DatiAggiornamentoNotFoundException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (NonValidParamException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (ProcessException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (InitializeException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (ComunicationException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            }

        }

        //##########################################################################
        //### CARICA SUL FTP UN BACKUP PER IL SERVER PROVENIENTE DALLA MACCHINA ####
        //##########################################################################

        // Gli if servono solo per il test, sulla mcchina vengono chiamati direttamente i metodi statici
        if (args[0].equals(UPLOAD_BACKUP_DA_MACCHINA_PER_SERVER)) {

            try {
                uploadBackupDaMacchinaPerServer(emf, panelAgg);

            } catch (NonValidParamException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (ProcessException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (InitializeException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            } catch (ComunicationException ex) {
                throw ex;
                //AGGIUNGERE METODO PER SALVARE L'ECCEZIONE
            }

        }

        //##########################################################################
        //### SCARICA DAL FTP GLI AGGIORN PER LA MACCHINA PROVENIENTI DAL SERVER ###
        //##########################################################################

        if (args[0].equals(DOWNLOAD_AGGIORNAMENTI_DA_SERVER)) {

            try {

                downloadAggiornamentiDaServer(emf, panelAgg);

            } catch (ProcessException ex) {
                throw ex;
            } catch (InvalidUpdateVersionException ex) {
                throw ex;
            } catch (InvalidUpdateTypeException ex) {
                throw ex;
            } catch (InitializeException ex) {
                throw ex;
            } catch (ComunicationException ex) {
                throw ex;
            } catch (InvalidUpdateContentException ex) {
                throw ex;

            }

        }
    }

    public static void downloadAggiornamentiDaServer(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws
            InitializeException,
            ComunicationException,
            ProcessException,
            InvalidUpdateContentException,
            InvalidUpdateTypeException,
            InvalidUpdateVersionException, 
            PreexistingEntityException, 
            NonexistentEntityException, 
            Exception {

        try {

            MacchinaProcess.downloadAllUpdates(emf, panelAgg);
            
        } catch (InitializeException ex) {
            throw ex;
        } catch (ComunicationException ex) {
            throw ex;
        } catch (ProcessException ex) {
            throw ex;
        } catch (InvalidUpdateContentException ex) {
            throw ex;
        } catch (InvalidUpdateTypeException ex) {
            throw ex;
        } catch (InvalidUpdateVersionException ex) {
            throw ex;
        } catch (PreexistingEntityException ex) {
            throw ex;
        } catch (NonexistentEntityException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void uploadAggiornamentoDaMacchinaPerServer(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws NonValidParamException,
            InitializeException,
            ProcessException,
            ComunicationException,
            DatiAggiornamentoNotFoundException {

        MacchinaProcess.uploadUpdate(emf, panelAgg);

    }

    //Metodo da invocare sulla macchina non necessita del main
    public static void uploadBackupDaMacchinaPerServer(EntityManagerFactory emf, Pannello12_Aggiornamento panelAgg)
            throws NonValidParamException,
            InitializeException,
            ProcessException,
            ComunicationException {


        RipristinoProcess.uploadBackupXML(emf, panelAgg);

    }
}
