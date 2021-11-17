/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.utils;

import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException;
import it.sauronsoftware.ftp4j.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Marilisa Tassone
 */
public class RipristinoUtils {

    private static Logger log = Logger.getLogger(RipristinoUtils.class);

    /**
     * Crea file xml a partire dall'oggetto objectOut, utilizzando le classi del
     * package jaxbPackageName (dove deve essere inserito il file jaxb.index) e
     * lo salva nella cartella destFileFolder
     *
     * @param fileName nome del file senza estensione
     * @param destFileFolder cartella nella quale meorizzare il file
     * @return
     * @throws FileNotFoundException
     *
     */
    public static String createBackupSQL(
            String dbName,
            String dbUserName,
            String dbPassword,
            String fileName,
            String destFileFolder //MacchinaXMLDataOut - ServerXMLDataOut
    ) throws IOException, InterruptedException, ProcessException {

        final String pathToFile = destFileFolder + File.separator + fileName + ".sql";
        log.info("Il path completo al file sql da creare è : " + pathToFile);

        String executeCmd = ParametriSingolaMacchina.parametri.get(230) + " -u " + dbUserName + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r " + pathToFile;

        Process runtimeProcess;

        log.error("Sto per eseguire il backup");

        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        } catch (IOException ex) {
            log.error("ERRORE NELL'ESECUZIONE DEL BACKUP");
            throw ex;
        }
        int processComplete;
        try {
            processComplete = runtimeProcess.waitFor();
        } catch (InterruptedException ex) {
            log.error("ERRORE NELL'ESECUZIONE DEL BACKUP");
            throw ex;
        }

        if (processComplete == 0) {
            log.info("Backup created successfully");
            return pathToFile;
        } else {
            throw new ProcessException("IMPOSSIBILE CREARE IL BACKUP");

        }

    }

    public static String createBackupSQL_alternative(
            String dbName,
            String dbUserName,
            String dbPassword,
            String fileName,
            String destFileFolder //MacchinaXMLDataOut - ServerXMLDataOut
    ) throws IOException, InterruptedException, ProcessException {

        final String pathToFile = destFileFolder + File.separator + fileName + ".sql";

        log.info("Il path completo al file sql da creare è : " + pathToFile);

        String executeCmd = ParametriSingolaMacchina.parametri.get(230) + " -u " + dbUserName + " -p" + dbPassword + " --add-drop-database -B " + dbName + " -r " + pathToFile;

        Process runtimeProcess;

        log.error("Sto per eseguire il backup");

        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        } catch (IOException ex) {
            log.error("ERRORE NELL'ESECUZIONE DEL BACKUP");
            throw ex;
        }
        int processComplete;
        try {
            processComplete = runtimeProcess.waitFor();
        } catch (InterruptedException ex) {
            log.error("ERRORE NELL'ESECUZIONE DEL BACKUP");
            throw ex;
        }

        if (processComplete == 0) {
            log.info("Backup created successfully");
            return pathToFile;
        } else {
            throw new ProcessException("IMPOSSIBILE CREARE IL BACKUP");

        }

    }

    /**
     * Metodo che restituisce il path all'ultimo file presente nella cartella
     * passata come parametro
     * N.B. Se è presente più di un file zip, prende l'ultimo
     *
     * @param dirName
     * @return
     * @throws ProcessException
     */
    public static String findFileFromDirRipristino(String dirName) throws ProcessException {
        String fileBackupZero = null;

        File dir = new File(dirName);
        File[] list = dir.listFiles();

        if (list != null) {
            for (File list1 : list) {
                log.info("dir.listFiles : " + list1);
                if (list1.getName().contains(".zip")) {
                    fileBackupZero = list1.getPath();
                    log.info("list[i].getPath() : " + list1.getPath());
                    log.info("Trovato file list[i].getPath()");
                }
            }
        }
        return fileBackupZero;
    }
//##############################################################################

    /**
     *
     * Metodo che restituisce i file .log contenuti nella cartella dist/log
     *
     * @param dirPath
     * @return
     * @throws ProcessException
     */
    public static Collection<File> findFileFromDirLog(String dirPath) {

        Collection<File> logFiles = new ArrayList<File>();

        String prefissoNomeFile = null;
        File dirLog = new File(dirPath);

        log.info("Ricerca della lista dei file presenti nella cartella : " + dirPath);

        File[] list = dirLog.listFiles();

        for (int i = 0; i < list.length; i++) {
            if (list[i].getName().contains(".log")) {
                log.info(" Trovato list[i].getPath() : " + list[i].getPath());

                logFiles.add(list[i]);
            }
        }

        return logFiles;
    }

    public static boolean restoreDB(
            String dbUserName,
            String dbPassword,
            String source) throws IOException, InterruptedException {

        String[] executeCmd = new String[]{ParametriSingolaMacchina.parametri.get(231), "--user=" + dbUserName, "--password=" + dbPassword, "origamidb", "-e", " source " + source};

        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
        } catch (IOException ex) {
            log.error("ERRORE NEL RIPRISTINARE IL BACKUP ZERO!");
            throw ex;
        }
        int processComplete;
        try {
            processComplete = runtimeProcess.waitFor();
        } catch (InterruptedException ex) {
            log.error("ERRORE NEL RIPRISTINARE IL BACKUP ZERO!");
            throw ex;
        }

        if (processComplete == 0) {
            log.info("Backup restored successfully");
            return true;
        } else {
            log.info("Could not restore the backup");
        }
        return false;
    }

////    public static void sendMail (String dest, String mitt, String oggetto, String testoEmail)
////      throws MessagingException
////  {
////    // Creazione di una mail session
////    Properties props = new Properties();
////    props.put("mail.smtp.host", "smtp.mioprovider.it");
////    Session session = Session.getDefaultInstance(props);
////
////    // Creazione del messaggio da inviare
////    MimeMessage message = new MimeMessage(session);
////    message.setSubject(oggetto);
////    message.setText(testoEmail);
////
////    // Aggiunta degli indirizzi del mittente e del destinatario
////    InternetAddress fromAddress = new InternetAddress(mitt);
////    InternetAddress toAddress = new InternetAddress(dest);
////    message.setFrom(fromAddress);
////    message.setRecipient(Message.RecipientType.TO, toAddress);
////
////    // Invio del messaggio
////    Transport.send(message);
////  }
    //#####################################################################
    /**
     * Metodo che consente di eliminare il file di bkp0 dalla cartella bkp0 del
     * server FTP e di spostare il file nella cartella old
     *
     * @param ftpServerUser
     * @param ftpServerPassword
     * @param remoteFileFolder
     * @param ftpServerName
     * @param remoteBackupDir
     * @param processedFileName
     * @return
     * @throws FTPListParseException
     * @throws FileNotFoundException
     * @throws FTPDataTransferException
     * @throws FTPAbortedException
     * @throws IOException
     * @throws FTPIllegalReplyException
     * @throws IllegalStateException
     * @throws FTPException
     */
    public static boolean removeFileFTP(
            String ftpServerUser,
            String ftpServerPassword,
            String remoteFileFolder,//bkp0
            String remoteBackupDir,//old
            String ftpServerName,
            String processedFileName) throws IOException, FTPIllegalReplyException, FTPException {
        log.info("Inizio metodo removeFileFTP");
        FTPClient ftpClient = new FTPClient();
        boolean fileRemoved = false;

        try {
            ftpClient.connect(ftpServerName);
            ftpClient.login(ftpServerUser, ftpServerPassword);
            log.info("Connessione e login al server FTP");
        } catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException ex) {
            throw ex;
        }

        //configurazione del client
        ftpClient.setPassive(true); // Server passivo
        ftpClient.setType(FTPClient.TYPE_BINARY);

        //verifichiamo in quale directory siamo
        //Lato macchina: siamo nella home dell'utente macchina e dobbiamo spostarci nella cartella s2m
        String dir;
        try {
            dir = ftpClient.currentDirectory();
            //Ci spostiamo nella cartella bkp0/.
            ftpClient.changeDirectory(remoteFileFolder);
            //ORA SIAMO NEL FOLDER REMOTO
            log.info("CARTELLA REMOTA: " + ftpClient.currentDirectory());
            //Prendiamo la lista dei file da scaricare
            //            FTPFile[] list = ftpClient.list();
//            ftpClient.deleteFile(processedFileName);

            //Attenzione: il comando rename se ha due dirpath differenti sposta il file!!! 
            ftpClient.rename(processedFileName, remoteBackupDir + SyncOrigamiConstants.FTP_FILE_SEPARATOR + processedFileName);
            fileRemoved = true;
            log.info("RINOMINATO FILE : " + processedFileName + " IN " + remoteBackupDir + SyncOrigamiConstants.FTP_FILE_SEPARATOR + processedFileName);
            log.info("File rimosso dalla cartella remota : " + ftpClient.currentDirectory());
        } catch (IllegalStateException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (FTPIllegalReplyException ex) {
            throw ex;
        } catch (FTPException ex) {
            throw ex;
        }

        return fileRemoved;

    }

    /**
     * Metodo che dato il percordo di un file (Cartella/file.est) estrae il nome
     * del file esclusa la cartella e senza estenzione
     *
     * @param completePathFile
     * @return
     */
    public static String estraiNomeFileSenzaEstenzione(String completePathFile) {
        //Estraggo il nome del file senza il nome della cartella
        String splitRegex = Pattern.quote(System.getProperty("file.separator"));
        String fileCompletePath[] = completePathFile.split(splitRegex);
        //Estraggo il nome del file senza estensione .zip
        String nomeFileMenoEst[] = fileCompletePath[1].split("\\.");
        String nomeFile = nomeFileMenoEst[0];
        log.info("nomeFileMenoEst :" + nomeFile);

        return nomeFile;
    }

    /**
     * Verifica la connessione ad internet TESTARE timeout 3 secondi
     *
     * @return
     * @throws IOException
     */
    public static boolean checkConn() throws IOException {
        String site = "www.google.it";
        try (Socket socket = new Socket()) {
            InetSocketAddress addr = new InetSocketAddress(site, 80);
            try {
                socket.connect(addr, 3000);
            } catch (IOException ex) {
                log.error("Impossibile collegarsi ad internet");

            }
            return socket.isConnected();
        }
    }
}
