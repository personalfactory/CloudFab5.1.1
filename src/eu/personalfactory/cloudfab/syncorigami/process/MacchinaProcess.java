/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.process;

import eu.personalfactory.cloudfab.syncorigami.utils.MachineCredentials;
import eu.personalfactory.cloudfab.syncorigami.utils.FileUtils;
import eu.personalfactory.cloudfab.syncorigami.utils.SyncOrigamiConstants;
import eu.personalfactory.cloudfab.syncorigami.utils.UpdaterUtils;
import eu.personalfactory.cloudfab.syncorigami.utils.DataManagerM;
import eu.personalfactory.cloudfab.syncorigami.exceptions.DatiAggiornamentoNotFoundException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ComunicationException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateContentException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateTypeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateVersionException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.MachineCredentialsNotFoundException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException;
import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoOri;
import eu.personalfactory.cloudfab.macchina.panels.Pannello12_Aggiornamento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXBException;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author marilisa
 */
public class MacchinaProcess {

    private static Logger log = Logger.getLogger(MacchinaProcess.class);

    /**
     * Metodo che genera un file xml di aggiornamento e lo carica sul FTP
     *
     * @param EntityManagerFactory
     * @param Pannello19_Aggiornamento
     * @throws InitializeException
     * @throws ProcessException
     * @throws ComunicationException
     * @throws DatiAggiornamentoNotFoundException
     */
    public static void uploadUpdate(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InitializeException,
            ProcessException,
            ComunicationException,
            DatiAggiornamentoNotFoundException {


        log.info("######## INIZIO METODO MacchinaProcess.uploadUpdate ########");

        //Visualizzazione stringa relativa al processo in corso
        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 332, ParametriSingolaMacchina.parametri.get(111)));
        //"PROCESSO DI UPLOAD SU SERVER FTP"; 

        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 333, ParametriSingolaMacchina.parametri.get(111)));
        //"Connessione al server";

        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 334, ParametriSingolaMacchina.parametri.get(111)));
        //"Caricamento proprietà dal database";

        log.info("ISTANZIO IL DATAMANAGER");

        //ISTANZIA IL DATAMANAGER
        DataManagerM dataManager;
        try {
            dataManager = new DataManagerM(
                    SyncOrigamiConstants.OUT_FILE_PFX,
                    emf);

        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex.toString());
            throw new InitializeException("IMPOSSIBILE RECUPERARE LE CREDENZIALI DELLA MACCHINA!!!");
        }

        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc;
        try {
            mc = dataManager.getMachineCredentials();
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex.toString());
            throw new InitializeException("ERRORE NEL RECUPERARE LE CREDENZIALI DELLA MACCHINA");
        }

        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 335, ParametriSingolaMacchina.parametri.get(111))); //"Recupero credenziali");
        //##########################################################################
        //####################### GENERAZIONE AGGIORNAMENTO ########################
        //##########################################################################

        //COSTRUISCE L'AGGIORNAMENTO PRENDENDO I DATI NUOVI DALLA Macchina
        AggiornamentoOri aggiornamentoOri = dataManager.costruisciAggiornamento(
                SyncOrigamiConstants.OUT_FILE_PFX,
                emf,
                panelAgg);


        if (aggiornamentoOri.getDaInserire() == null || aggiornamentoOri.getDaInserire().isEmpty()) {
            log.info("NON CI SONO DATI NUOVI PER L'AGGIORNAMENTO");
            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 336, ParametriSingolaMacchina.parametri.get(111))); //("Non ci sono aggiornamenti da inviare al server");

        } else {

            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 337, ParametriSingolaMacchina.parametri.get(111))); //"Selezione dati nuovi");
            log.info("Selezione dati nuovi");

            //##########################################################################
            //############## COSTRUZIONE DEL NOME DEL FILE DI SCAMBIO ##################
            //##########################################################################

            //COSTRUISCE IL NOME DEL FILE IN BASE ALLA DATA CORRENTE ED ALLA VERSIONE      
            String nomeFileOut;
            try {
                nomeFileOut = UpdaterUtils.generaNomeFileOut(
                        dataManager.getMachineCredentials(),
                        SyncOrigamiConstants.OUT_FILE_PFX);
            } catch (MachineCredentialsNotFoundException ex) {
                log.error(ex);
                log.error(ex.toString());
                throw new InitializeException("ERRORE NELLA GENERAZIONE DEL NOME FILE OUT");
            }
            log.info("GENERATO NOME DEL FILE : " + nomeFileOut);

            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 338, ParametriSingolaMacchina.parametri.get(111)));//("Generazione nome del file xml");

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

                //Setto il nome del file nell'oggetto AggiornamentoOri
                aggiornamentoOri.setNomeFile(completePathToXMLFileOut);


            } catch (FileNotFoundException ex) {
                log.error(ex.toString());
                throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE (Errore di creazione)" + completePathToXMLFileOut + "\n");
            } catch (JAXBException ex) {
                log.error(ex.toString());
                throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE (Errore JAXB)" + completePathToXMLFileOut + "\n");
            }

            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 339, ParametriSingolaMacchina.parametri.get(111))); //("Creazione del file xml ");
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

                //completePathToXMLCompressedFileOut = updater.compress(nomeFileOut, mc.getZipPassword());
            } catch (IOException ex) {
                throw new ProcessException("ERRORE NELL' ELIMINAZIONE DEL FILE XML :" + nomeFileOut + ".xml" + "\n");
            } catch (ZipException ex) {
                log.error(ex.toString());
                throw new ProcessException("ERRORE NELLA CREAZIONE DEL FILE COMPRESSO" + completePathToXMLFileOut + "\n");
            }

            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 340, ParametriSingolaMacchina.parametri.get(111))); //("Compressione del file xml");

            log.info("completePathToXMLCompressedFileOut : " + completePathToXMLCompressedFileOut);

            //##########################################################################
            //##################### UPLOAD IN CARTELLA REMOTA ##########################
            //##########################################################################

//            log.info("SyncOrigamiConstants.SERVER_FTP_SERVER_USER : " + SyncOrigamiConstants.SERVER_FTP_SERVER_USER);
//            log.info("SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD : " + SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD);
            log.info("SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR : " + SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR);
//            log.info("SyncOrigamiConstants.FTP_SERVER_NAME : " + SyncOrigamiConstants.FTP_SERVER_NAME);
            log.info("SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR : " + SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR);
            log.info("SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR : " + SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);

            //FA L'UPLOAD DEL FILE NELLA CARTELLA REMOTA (HOME DELLA MACCHINA)
            String completePathTransferedFileOut;
            try {
                completePathTransferedFileOut = UpdaterUtils.uploadToFTP(
                        SyncOrigamiConstants.SERVER_FTP_SERVER_USER,
                        SyncOrigamiConstants.SERVER_FTP_SERVER_PASSWORD,
                        SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_COMPRESS_DIR,
                        nomeFileOut + ".zip",
                        SyncOrigamiConstants.FTP_SERVER_NAME,
                        SyncOrigamiConstants.MACCHINA_FTP_SERVER_OUT_DIR,
                        SyncOrigamiConstants.MACCHINA_XML_DATA_OUT_TRANSFERED_DIR);

                log.info("completePathTransferedFileOut : " + completePathTransferedFileOut);
                log.info("UPLOAD COMPLETATO !");

            } catch (IOException ex) {
                log.error(ex.toString());
                throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE ");
            } catch (FTPDataTransferException ex) {
                log.error(ex.toString());
                throw new ComunicationException("ERRORE NEL TRASFERIMENTO DEL FILE " + completePathToXMLCompressedFileOut);
            } catch (FTPAbortedException ex) {
                log.error(ex.toString());
                throw new ComunicationException("COMUNICAZIONE INTERROTTA!!!");
            } catch (FTPIllegalReplyException ex) {
                log.error(ex.toString());
                throw new ComunicationException("IL SERVER FTP NON RISPONDE CORRETTAMENTE!!!");
            } catch (FTPException ex) {
                log.error(ex.toString());
                throw new ComunicationException("ERRORE FTP!!!" + ex.getMessage());
            }

            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 341, ParametriSingolaMacchina.parametri.get(111)));//("Trasferimento sul server ");

            //###########################################################################
            //#################### REGISTRAZIONE AGGIORNAMENTO SUL DB ###################
            //###########################################################################
            //Solo se il processo di upload è andato a buon fine si registra l'aggiornamento nella 
            //tabella aggiornamento_ori di origamidb

            dataManager.salvaAggiornamentoOriOut(aggiornamentoOri, emf);

            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 342, ParametriSingolaMacchina.parametri.get(111)));//("Registrazione aggiornamento in uscita per il server");
        }

    }

    /**
     *
     * Metodo che scarica tutti gli aggiornamenti provenienti dal server,
     * processa, valida e salva nel db
     *
     * @param EntityManagerFactory
     * @param Pannello19_Aggiornamento
     * @throws InitializeException
     * @throws ComunicationException
     * @throws ProcessException
     * @throws InvalidUpdateContentException
     * @throws InvalidUpdateTypeException
     * @throws InvalidUpdateVersionException
     */
    public static void downloadAllUpdates(EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InitializeException,
            ComunicationException,
            ProcessException,
            InvalidUpdateContentException,
            InvalidUpdateTypeException,
            InvalidUpdateVersionException,
            PreexistingEntityException,
            NonexistentEntityException,
            Exception {

        panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 343, ParametriSingolaMacchina.parametri.get(111))); //("PROCESSO DI DOWNLOAD DAL SERVER FTP");

        //##########################################################################
        //####################### FASE DI INIZIALIZZAZIONE #########################
        //##########################################################################
        log.info("PROCESSO DI DOWNLOAD DAL SERVER FTP");

        log.info("INIZIO METODO  MacchinaProcess.downloadAllUpdates");

        //ISTANZIA IL DATAMANAGER
        DataManagerM dataManager;
        try {
            dataManager = new DataManagerM(
                    SyncOrigamiConstants.IN_FILE_PFX,
                    emf);
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex.toString());
            throw new InitializeException("IMPOSSIBILE RECUPERARE LE CREDENZIALI DELLA MACCHINA!!!");
        }

        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc;
        try {
            mc = dataManager.getMachineCredentials();
        } catch (MachineCredentialsNotFoundException ex) {
            log.error(ex.toString());
            throw new InitializeException("ERRORE NEL RECUPERARE LE INFORMAZIONI RELATIVE ALLA MACCHINA");
        }

        //##########################################################################
        //################### DOWNLOAD DALLA CARTELLA REMOTA ########################
        //##########################################################################
        //TODO: MARI Prima di scaricare i file bisogna controllare che l'id_macchina 
        //contenuto nel nome del file sia uguale a quello contenuto nella tabella macchina 

        Collection<String> downloadedFiles = null;
        try {
            downloadedFiles = UpdaterUtils.downloadAllFromFTP(
                    mc.getFtpUser(),
                    mc.getFtpPassword(),
                    SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_DIR,
                    SyncOrigamiConstants.FTP_SERVER_NAME,
                    SyncOrigamiConstants.MACCHINA_XML_DATA_IN_DOWNLOADED_DIR);
        } catch (FTPListParseException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
            throw new InitializeException(ex.getMessage());
        } catch (FTPDataTransferException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        } catch (FTPAbortedException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        } catch (FTPIllegalReplyException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        } catch (IllegalStateException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        } catch (FTPException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new ComunicationException(ex.getMessage());
        }


        if (downloadedFiles.isEmpty()) {
            log.info("Non sono stati trovati aggiornamenti da scaricare");
            log.info("AGGIORNAMENTO TERMINATO");
            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 344, ParametriSingolaMacchina.parametri.get(111)));//("Non sono stati trovati aggiornamenti da scaricare");
            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 345, ParametriSingolaMacchina.parametri.get(111))); //("AGGIORNAMENTO TERMINATO");
        } else {


            //########## MODIFICHE MARI ################################################
            for (String s : downloadedFiles) {
                log.info("Scaricato file: " + s);
            }
            //Ordino i nomi dei file, poichè l'id_macchina è sempre lo stesso 
            //l'ordine delle stringhe è determinato dalla versione
            Collections.sort((List<String>) downloadedFiles);

            //######## FINE MODIFICHE MARI #############################################

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 346, ParametriSingolaMacchina.parametri.get(111))) //("Scaricati num.  "
                    + Integer.toString(downloadedFiles.size())
                    + HTML_STRINGA_FINE);

            log.info(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 346, ParametriSingolaMacchina.parametri.get(111))) //("Scaricati num.  "
                    + Integer.toString(downloadedFiles.size())
                    + HTML_STRINGA_FINE);

            //Per ognuno dei file scaricati decomprimiamo e se la decompressione è andata
            //a buon fine spostiamo il file xml nella cartella dei decompressi e il file
            //compresso dalla cartella degli scaricati a quella dei compressi in ingresso

            Collection<String> xmlIns = new ArrayList<>();
            int contFileDecomp = 0;
            for (String fileIn : downloadedFiles) {
                try {
                    String fileDestCompletePath = UpdaterUtils.uncompress(
                            fileIn,
                            fileIn.replace(".zip", ".xml"),
                            SyncOrigamiConstants.MACCHINA_XML_DATA_IN_UNCOMPRESS_DIR,
                            mc.getZipPassword(),
                            SyncOrigamiConstants.MACCHINA_XML_DATA_IN_DOWNLOADED_DIR);

                    //Spostiamo il file compresso nel folder compressed
                    FileUtils.copyFile(
                            new File(fileIn),
                            new File(fileIn.replace(SyncOrigamiConstants.MACCHINA_XML_DATA_IN_DOWNLOADED_DIR, SyncOrigamiConstants.MACCHINA_XML_DATA_IN_COMPRESS_DIR)));
                    FileUtils.delete(new File(fileIn));

                    xmlIns.add(fileDestCompletePath);

                    log.info("File compresso spostato dalla cartella MacchinaFTPDownloadIn alla cartella MacchinaXMLDataIn");

                } catch (IOException ex) {
                    log.error(ex.toString());
                    throw new ProcessException("IMPOSSIBILE SPOSTARE L'AGGIORNAMENTO " + fileIn + "NELLA CARTELLA DI BACKUP");
                } catch (ZipException ex) {
                    log.error(ex.toString());
                    throw new ProcessException("ERRORE NEL DECOMPRIMERE L'AGGIORNAMENTO " + fileIn);
                }
                contFileDecomp++;
                panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 347, ParametriSingolaMacchina.parametri.get(111))) //"Decompresso file num : " 
                        + Integer.toString(contFileDecomp)
                        + HTML_STRINGA_FINE);
                log.info(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 347, ParametriSingolaMacchina.parametri.get(111))) //"Decompresso file num : " 
                        + Integer.toString(contFileDecomp)
                        + HTML_STRINGA_FINE);
            }

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 348, ParametriSingolaMacchina.parametri.get(111))) //("Totale file decompressi num : " 
                    + Integer.toString(contFileDecomp)
                    + HTML_STRINGA_FINE);
            log.info(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 348, ParametriSingolaMacchina.parametri.get(111))) //("Totale file decompressi num : " 
                    + Integer.toString(contFileDecomp)
                    + HTML_STRINGA_FINE);

            int contFile = 0;
            Object o = null;
            for (String fileIn : xmlIns) {
                try {
                    o = UpdaterUtils.retrieveData(
                            SyncOrigamiConstants.MACCHINA_JAXB_OUTPUT_PACKAGE_NAME,
                            SyncOrigamiConstants.XSD_MACCHINA_FILE_NAME,
                            fileIn);
                    //LOG
                    log.info("OGGETTO DI TIPO: " + o.getClass());
                    log.info(o.getClass().cast(o).toString());
                    @SuppressWarnings("unchecked")
                    Collection<Object> c = ((AggiornamentoOri) o).getDaInserire();

                    for (Object obj : c == null ? new ArrayList() : c) {
                        log.info(obj.getClass().cast(obj).toString());
                    }


                } catch (SAXException | JAXBException ex) {
                    throw ex;
                }

                //FINE LOG

                //##########################################################################
                //################### VALIDAZIONE DEI DATI E SALVATAGGIO NEL DB ############
                //##########################################################################

                try {

                    dataManager.validaAggiornamentoOriIn(o, SyncOrigamiConstants.IN_FILE_PFX, emf, panelAgg);

                } catch (InvalidUpdateVersionException ex) {
                    throw ex;
                } catch (InvalidUpdateTypeException ex) {
                    throw ex;
                } catch (InvalidUpdateContentException ex) {
                    throw ex;
                }

                panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 349, ParametriSingolaMacchina.parametri.get(111))) //"Validazione file : " 
                        + fileIn
                        + HTML_STRINGA_FINE);

                log.info(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 349, ParametriSingolaMacchina.parametri.get(111))) //"Validazione file : " 
                        + fileIn
                        + HTML_STRINGA_FINE);

                AggiornamentoOri aggiornamentoOri = (AggiornamentoOri) o;
                log.info("fileIn :" + fileIn);
                aggiornamentoOri.setNomeFile(fileIn);

                //Estraggo il nome del file senza la cartella
                String splitRegex = Pattern.quote(System.getProperty("file.separator"));
                String nomeFile[] = fileIn.split(splitRegex);

                //Estraggo il nome del file senza estensione .xml
                String nomeFileMenoEst[] = nomeFile[1].split("\\.");

                mc.setCurrentUpdateFileName(nomeFileMenoEst[0]);
                log.info("mc.getCurrentUpdateFileName : " + mc.getCurrentUpdateFileName());

                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 350, ParametriSingolaMacchina.parametri.get(111))); //("Salvataggio dati in corso ...");

                try {
                    dataManager.SalvaDatiAggiornamentoOriIn(aggiornamentoOri, emf, panelAgg);
                } catch (PreexistingEntityException ex) {
                    throw ex;
                } catch (NonexistentEntityException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw ex;
                }

                boolean aggiornamentoCompleto = false;
                aggiornamentoCompleto = UpdaterUtils.renameFileFTP(
                        mc.getFtpUser(),
                        mc.getFtpPassword(),
                        SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_DIR,//s2m
                        SyncOrigamiConstants.FTP_SERVER_NAME,
                        SyncOrigamiConstants.MACCHINA_FTP_SERVER_IN_DIR_OLD,//old
                        mc.getCurrentUpdateFileName());

                if (aggiornamentoCompleto) {
                    //Salvo nella tabella AggiornamentoOri
                    dataManager.salvaAggiornamentoOriIn(aggiornamentoOri, emf);
                    contFile++;
                    log.info("AGGIORNAMENTO COMPLETO! ");

                    //########### MODIFICA MARI 18-12 
                    // Se l'aggiornamento è andato a buon fine 
                    //Elimino il file dalla cartella MacchinaXMLDataIn
                    if (FileUtils.delete(new File(fileIn))) {
                        log.info("Eliminato file : " + fileIn);
                    }


                    panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 351, ParametriSingolaMacchina.parametri.get(111))) //
                            + contFile
                            + HTML_STRINGA_FINE);

                    panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 352, ParametriSingolaMacchina.parametri.get(111))); //("AGGIORNAMENTO COMPLETATO");

                } else {
                    throw new ProcessException("Aggiornamento non completato : il file " + mc.getCurrentUpdateFileName()
                            + " è stato processato ma non spostato nella cartella old");

                }
            }


        }






    }
}
