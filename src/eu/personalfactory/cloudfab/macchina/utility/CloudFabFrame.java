package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.panels.MyStepPanel;
import eu.personalfactory.cloudfab.macchina.panels.Pannello45_Dialog;
import eu.personalfactory.cloudfab.macchina.gestore.password.CreaUtente;
import eu.personalfactory.cloudfab.macchina.gestore.password.GestorePassword;
import eu.personalfactory.cloudfab.macchina.io.GestoreIO;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_CreatePanels;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ImpostaVisPannelli;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_ModificaOut;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniStatoIngresso;
import eu.personalfactory.cloudfab.macchina.loggers.BilanceLogger;
import eu.personalfactory.cloudfab.macchina.loggers.InverterLogger;
import eu.personalfactory.cloudfab.macchina.loggers.MicroLogger;
import eu.personalfactory.cloudfab.macchina.loggers.SchedaIOLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello00_Principale;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ComunicationException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException;
import eu.personalfactory.cloudfab.syncorigami.process.RipristinoProcess;
import java.io.IOException;

import javax.swing.JFrame;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.loggers.SevereLogger;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.ClientPesaTLB4;
import eu.personalfactory.cloudfab.macchina.socket.modulo_pesa.IndirizziBilance;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.CreaPannelli;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreAggiornamentoRelease;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreArchiviazioneFileLog;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreSpostamentoFileLog;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.RiavviaCloudFab;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIdMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIpFtp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaIpOrigami;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaPasswordFileZipMacchina;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaConfigurazioneIp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.VerificaConnessioneRete;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EliminaCarattereVuotoiNephos;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.PingAddress;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BACKUP_USB_FILE_NAME_CHAR_SEP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.BACKUP_USB_FILE_NAME_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CreatePersistenceUnit;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ENTITY_MANAGER_FACTORY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_RETROAZIONE_MDUINO_MAIN_PANEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_RETROAZIONE_MDUINO_SILOS_PANEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_LOGICO_RETROAZIONE_MDUINO_SILOS_PANEL_AUX;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_00;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_01;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_02;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_09;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_10;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_14;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_15;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_16;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_17;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_18;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_19;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_28;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.LOADING_IMG_TEXT_TAB;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CONFEZIONI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_MANUALE; 
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_BILANCIA_CARICO_AUX;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_FALSE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.OUTPUT_TRUE_CHAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ADDRESS_PING_TIMEOUT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_RETROAZIONE_VERIFICA_MDUINO_MAIN_PANEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL_AUX;
import eu.personalfactory.cloudfab.syncorigami.utils.RipristinoUtils;
import eu.personalfactory.cloudfab.syncorigami.utils.SyncOrigamiConstants;
import eu.personalfactory.cloudfab.syncorigami.utils.UpdaterUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;
import eu.personalfactory.cloudfab.syncorigami.exceptions.MachineCredentialsNotFoundException;
import java.awt.HeadlessException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import static eu.personalfactory.cloudfab.macchina.io.GestoreIO.GestoreIO_OttieniValoreIngressoAnalogico;
import eu.personalfactory.cloudfab.macchina.io.IODevice;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_mix;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.inverter_screws;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEVICE_INGRESSO_PRESSOSTATO_MDUINO_MAINPANEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.INGRESSO_ANALOGICO_PRESSOSTATO_MDUINO_MAINPANEL;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.Inverter_Gefran_BDI50_Crea_Pannelli;
import static eu.personalfactory.cloudfab.macchina.socket.inv.GestoreInverterCom.Inverter_Gefran_BDI50_ImpostaVisPannelli;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.DEFAULT_VEL_INV;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.VIS_PANNELLI_DIR;

public class CloudFabFrame {

    private static final JFrame FRAME = new JFrame();
    private static final Logger log = Logger.getLogger(CloudFabFrame.class);
    private static final MyStepPanel pannelli[] = new MyStepPanel[60];
    private static boolean eseguitoRipristinoDaUSB;
    private static boolean connessioneInternet;

    public static JFrame getFRAME() {
        return FRAME;
    }

    @SuppressWarnings({})
    public CloudFabFrame(FabCloudLoadingFrame lFrame) throws IOException {

        super();

        eseguitoRipristinoDaUSB = false;

        try {

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inizializzazione logger della sessione
            SessionLogger.init();

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Aggiornamento LoadingPanel
            lFrame.lab_ver_mysl.setVisible(true);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Creazione Pannello Principale
            pannelli[0] = new Pannello00_Principale();

            SessionLogger.logger.info("Creazione Pannello Principale                            OK");

            //////////////////////////////////////////////////////////////// PROGRESS BAR
            lFrame.progress_bar.setValue(10);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Creazione Entity Manager Factory - Persistence Unit
            try {

                CreatePersistenceUnit();

                SessionLogger.logger.info("Creazione PersistenceUnit                            OK");

            } catch (Exception ex) {

                SessionLogger.logger.log(Level.INFO, "Errore durante la creazione delle Persistance Unit - ex{0}", ex);

                lFrame.setVisible(false);

                JOptionPane.showMessageDialog(lFrame, LOADING_IMG_TEXT_18 + "\n" + ex + "\n" + "\n" + LOADING_IMG_TEXT_19);

                new CreaUtente();

            }
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Aggiornamento LoadingPanel
            lFrame.lab_ver_mysl.setText(lFrame.lab_ver_mysl.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
            lFrame.lab_db_par.setVisible(true);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inizializzazione Parametri Singola Macchina
            ParametriSingolaMacchina.init();

            SessionLogger.logger.info("Lettura Parametri Macchina                             OK");
            //////////////////////////////////////////////////////////////// PROGRESS BAR
            lFrame.progress_bar.setValue(20);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Inizializzazione Parametri Globali
            ParametriGlobali.init();

            SessionLogger.logger.info("Lettura Parametri Globali                             OK");

            lFrame.lab_db_par.setText(lFrame.lab_db_par.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
            lFrame.lab_panels.setVisible(true);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Creazione Pannelli
            if (CreaPannelli(pannelli)) {

                /////////////////////////////////////////////////
                // CREAZIONE PANNELLI ESEGUITA CORRETTAMENTE  ///
                /////////////////////////////////////////////////
                lFrame.lab_panels.setText(lFrame.lab_panels.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);

                SessionLogger.logger.info("Creazione Pannelli                             OK");
            } else {

                ///////////////////////////////////////////
                // CREAZIONE PANNELLI ESEGUITA FALLITA  ///
                ///////////////////////////////////////////
                lFrame.lab_panels.setText(lFrame.lab_panels.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                SessionLogger.logger.severe("Creazione Pannelli Fallita");

            }

            lFrame.lab_logfiles.setVisible(true);

            //////////////////////////////////////////////////////////////// PROGRESS BAR
            lFrame.progress_bar.setValue(30);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Gestione File di Log
            //////////////////////////////////////////////////////////////////////////// Inizializzazione Loggers
            SchedaIOLogger.init();
            SevereLogger.init();
            BilanceLogger.init();
            InverterLogger.init();

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(389))) {

                MicroLogger.init();

            }
            //////////////////////////////////////////////////////////////////////////// Gestore Spostamenti File di log
            GestoreSpostamentoFileLog();

            SessionLogger.logger.info("Gestore Spostamento File di Log                             OK");

            //////////////////////////////////////////////////////////////////////////// Archiviazione Log
            GestoreArchiviazioneFileLog();

            SessionLogger.logger.log(Level.INFO, "Gestore Archiviazione File di Log                             OK");

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Configurazione IP
            lFrame.lab_logfiles.setText(lFrame.lab_logfiles.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
            lFrame.lab_ip.setVisible(true);

            if (VerificaConfigurazioneIp()) {
                lFrame.lab_ip.setText(lFrame.lab_ip.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);

                SessionLogger.logger.info("Verifica Indirizzo IP Macchina                             OK");
            } else {
                lFrame.lab_ip.setText(lFrame.lab_ip.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                SessionLogger.logger.log(Level.SEVERE, "Verifica Indirizzo IP Macchina Fallita IP:{0}", TrovaIpOrigami());
            }
            lFrame.lab_internet.setVisible(true);

            //////////////////////////////////////////////////////////////// PROGRESS BAR
            lFrame.progress_bar.setValue(40);

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Connessione Internet
            //lFrame.lab_internet.setVisible(true);
            connessioneInternet = VerificaConnessioneRete(TrovaIpFtp(), 2000);

            if (connessioneInternet) {

                //////////////////////////////////
                // CONNESSIONE INTERNET VALIDA  //
                //////////////////////////////////
                lFrame.lab_internet.setText(lFrame.lab_internet.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
                lFrame.lab_new_release.setVisible(true);

                SessionLogger.logger.info("Connessione Internet Verificata");

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Nuova Release da Installare
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(460))) {

                    ///////////////////////////////////////
                    // AGGIORNAMENTO RELEASE ABILITATO  ///
                    ///////////////////////////////////////
                    SessionLogger.logger.info("Ricerca nuova release");

                    RipristinoProcess.downloadInitFilesFromFTP(ENTITY_MANAGER_FACTORY);

                    lFrame.lab_new_release.setText(LOADING_IMG_TEXT_09 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_10);

                    if (GestoreAggiornamentoRelease(lFrame)) {

                        //////////////////////////////////////
                        // AGGIORNAMENTO RELEASE ESEGUITO  ///
                        //////////////////////////////////////
                        //Memorizzazione Logger della Sessione
                        SessionLogger.logger.info("Visualizzazione Pannello Aggiornamento Software");

                        lFrame.setVisible(false);

                        ((Pannello45_Dialog) pannelli[41]).gestoreDialog.visualizzaMessaggio(2);

                        //Memorizzazione Logger della Sessione
                        SessionLogger.logger.info("Riavvio Applicazione");

                        RiavviaCloudFab(ParametriGlobali.parametri.get(53));

                        //Memorizzazione Logger della Sessione
                        SessionLogger.logger.info("Spegnimento Applicazione");

                        System.exit(0);

                    }

                } else {

                    ///////////////////////////////////////
                    // AGGIORNAMENTO RELEASE DISABILITATO  ///
                    ///////////////////////////////////////
                    lFrame.lab_new_release.setText(lFrame.lab_new_release.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

                    SessionLogger.logger.info("Aggiornamento nuova release disabilitato");
                }

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Restore da pennsa usb
                lFrame.lab_restore_da_usb.setVisible(true);

                //////////////////////////////////////////////////////////////// PROGRESS BAR
                lFrame.progress_bar.setValue(50);

                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(373))) {

                    /////////////////////////////////////////
                    // INIZIALIZZAZIONE DA USB ABILITATA  ///
                    /////////////////////////////////////////
                    SessionLogger.logger.info("Rilevata nuova release da usb abilitata");

                    try {
                        //TENTATIVO DI LETTURA DA USB
                        if (RipristinoProcess.ripristinaInitDatabaseFromDir(
                                ENTITY_MANAGER_FACTORY,
                                ParametriSingolaMacchina.parametri.get(376))) {

                            ///////////////////////////////
                            // LETTURA DA USB ESEGUITA  ///
                            ///////////////////////////////
                            SessionLogger.logger.severe("Eseguito ripristino da USB");

                            lFrame.lab_restore_da_usb.setText(lFrame.lab_restore_da_usb.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);

                            eseguitoRipristinoDaUSB = true;
                        }

                    } catch (Exception e) {

                        log.error(e.toString());

                        SessionLogger.logger.severe("Errore durante il tentativo di ripristino da USB");

                        JOptionPane.showMessageDialog(lFrame, TrovaVocabolo(
                                ID_DIZIONARIO_MESSAGGI_MACCHINA, 832, ParametriSingolaMacchina.parametri.get(111)));

                        System.exit(0);
                    }

                } else {

                    /////////////////////////////////////////////
                    // INIZIALIZZAZIONE DA USB NON ABILITATA  ///
                    /////////////////////////////////////////////
                    lFrame.lab_restore_da_usb.setText(lFrame.lab_restore_da_usb.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

                    SessionLogger.logger.info("Inizializzazione da USB non abilitata");

                }

                //////////////////////////////////////////////////////////////// PROGRESS BAR
                lFrame.progress_bar.setValue(60);

                /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Verifica Restore da file
                lFrame.lab_restore_da_file.setVisible(true);
                if (RipristinoProcess.ripristinaBKPZeroFromDir(ENTITY_MANAGER_FACTORY)) {

                    //////////////////////////////
                    // RESTORE DA FILE ESEGUITO ///
                    //////////////////////////////
                    lFrame.lab_restore_da_file.setText(LOADING_IMG_TEXT_14 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_15);

                    SessionLogger.logger.severe("Eseguito ripristino da file");

                } else {
                    /////////////////////////////////////////////////////
                    // RESTORE DA FILE NON ESEGUITO - NON NECESSARIO  ///
                    /////////////////////////////////////////////////////
                    lFrame.lab_restore_da_file.setText(LOADING_IMG_TEXT_14 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_16);

                    SessionLogger.logger.info("Ripristino da file non eseguito - non necessario");
                }

                lFrame.lab_backup_su_usb.setVisible(true);

                ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Backup su pen drive
                if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(374)) && !eseguitoRipristinoDaUSB) {

                    //////////////////////////////////////////////////////////////////
                    // BACKUP SU USB ABILITATO - RIPRISTINO DA USB NON EFFETTUATO  ///
                    //////////////////////////////////////////////////////////////////
                    SessionLogger.logger.info("Backup da USB Abiiltato");
                    try {

                        //Scansione dei file di backup per recupero max id e cancellazione vecchi file
                        File dir = new File(ParametriSingolaMacchina.parametri.get(375));
                        File[] list = dir.listFiles();

                        if (list != null) {

                            List<File> lista_ordinata = Arrays.asList(list);

                            //CANCELLAZIONE VECCHI FILE DI BACKUP
                            if (list.length >= Integer.parseInt(ParametriGlobali.parametri.get(116))) {

                                //Ordinamento lista
                                Collections.sort(lista_ordinata);

                                //Cancellazione dei file
                                for (int i = 0; i < (lista_ordinata.size() - Integer.parseInt(ParametriGlobali.parametri.get(116))); i++) {

                                    File a = new File(lista_ordinata.get(i).getPath());
                                    a.delete();
                                }
                            }
                        }

                        //COSTRUZIONE NUOVO FILE DI BACKUP
                        //Costruzione del nome del file
                        String nome_file_bck
                                = TrovaIdMacchina()
                                + BACKUP_USB_FILE_NAME_CHAR_SEP
                                + new SimpleDateFormat(
                                        BACKUP_USB_FILE_NAME_DATE_FORMAT).format(new Date());

                        //Creazione file di backup
                        RipristinoUtils.createBackupSQL_alternative(
                                SyncOrigamiConstants.MACCHINA_DB_NAME,
                                GestorePassword.userName(),
                                GestorePassword.passWord(),
                                nome_file_bck,
                                ParametriSingolaMacchina.parametri.get(375));

                        //Compressione file di backup
                        UpdaterUtils.compress(
                                nome_file_bck + ".sql",
                                ParametriSingolaMacchina.parametri.get(375),
                                nome_file_bck,
                                ParametriSingolaMacchina.parametri.get(375),
                                TrovaPasswordFileZipMacchina());

                        //////////////////////////////
                        // BACKUP SU USB ESEGUITO  ///
                        ////////////////////////////// 
                        lFrame.lab_backup_su_usb.setText(LOADING_IMG_TEXT_17 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);

                        SessionLogger.logger.info("Eseguito Backup su USB");

                    } ///////////////////////////////////////////////////////////////////////////////////
                    ///////////////////////////////////////////////////////////////////////////////////
                    catch (IOException | InterruptedException | ProcessException | ZipException ex) {

                        /////////////////////////////
                        // BACKUP SU USB FALLITO  ///
                        ///////////////////////////// 
                        lFrame.lab_backup_su_usb.setText(LOADING_IMG_TEXT_17 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                        SessionLogger.logger.severe("Backup su USB fallito");

                        JOptionPane.showMessageDialog(lFrame, TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 833, ParametriSingolaMacchina.parametri.get(111)) + ex);

                    }
                } else {

                    ////////////////////////////////////
                    // BACKUP SU USB- NON ABILITATO  ///
                    //////////////////////////////////// 
                    SessionLogger.logger.info("Backup su USB non abilitato");

                    lFrame.lab_backup_su_usb.setText(LOADING_IMG_TEXT_17 + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

                }
            } else {

                ////////////////////////////////////
                // CONNESSIONE INTERNET NON VALIDA  //
                //////////////////////////////////////
                lFrame.lab_internet.setText(lFrame.lab_internet.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                SessionLogger.logger.log(Level.SEVERE, "Connessione Internet Non Funzionante");
            }

            //Lettura parametri singola macchina configurazione bilance 
            new IndirizziBilance(ParametriSingolaMacchina.parametri.get(466), ParametriSingolaMacchina.parametri.get(464));
            
            //CREAZIONE PROVIDER - COMUNICAZIONE BILANCE
            SessionLogger.logger.log(Level.INFO, "Creazione Server Socket Comunicazione Bilance\n"
                    + "\nIP_CARICO {0}\nIP_CONFEZIONI {1}\nIP_CHIMICA {2}\nIP_CARICO_AUX {3}",
                    new Object[]{IndirizziBilance.indirizzi.get(0),
                    		IndirizziBilance.indirizzi.get(1),
                    		IndirizziBilance.indirizzi.get(2),
                    		IndirizziBilance.indirizzi.get(3)});

            String valore_peso_letto = ""; 
            
            if (!IndirizziBilance.indirizzi.get(ID_BILANCIA_CARICO).equals("")) {
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Creazione Gestore Comunicazione Bilancia di Carico
                lFrame.lab_socket_bil_carico.setVisible(true);
                
                Boolean esitoCreaSocket = false; 
                

                if (PingAddress(IndirizziBilance.indirizzi.get(ID_BILANCIA_CARICO), ADDRESS_PING_TIMEOUT)) {

                	ClientPesaTLB4  pesaCarico = new ClientPesaTLB4(ID_BILANCIA_CARICO);
                	
                	esitoCreaSocket = pesaCarico.verficaConn(); //;providerManuale.creaSocket();
                	
                	if (esitoCreaSocket) {
                		
                		valore_peso_letto = pesaCarico.pesoLordo();

                  	}
                	pesaCarico.chiudi();
                	
                } else {

                	esitoCreaSocket = false; 

                }

                 
                if (esitoCreaSocket) {
                    ////////////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA DI CARICO RIUSCITA  ///
                    ////////////////////////////////////////////////////
                    lFrame.lab_socket_bil_carico.setText(lFrame.lab_socket_bil_carico.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00 + " ("+ valore_peso_letto + ")");
                } else {

                    //////////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA DI CARICO FALLITA  ///
                    ///////////////////////////////////////////////////
                    lFrame.lab_socket_bil_carico.setText(lFrame.lab_socket_bil_carico.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                }
            } else {
                ////////////////////////////////////////////////////////
                // CREAZIONE SOCKET BILANCIA DI CARICO NON PREVISTA  ///
                ////////////////////////////////////////////////////////
                lFrame.lab_socket_bil_carico.setText(lFrame.lab_socket_bil_carico.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Creazione Gestore Comunicazione Bilancia Confezioni
            lFrame.lab_socket_bil_conf.setVisible(true);
            valore_peso_letto = ""; 
            
            if (!IndirizziBilance.indirizzi.get(ID_BILANCIA_CONFEZIONI).equals("")) {
            	
            	  Boolean esitoCreaSocket = false; 

                  if (PingAddress(IndirizziBilance.indirizzi.get(ID_BILANCIA_CONFEZIONI), ADDRESS_PING_TIMEOUT)) {

                  	ClientPesaTLB4  pesaConfezioni = new ClientPesaTLB4(ID_BILANCIA_CONFEZIONI);
                  	
                  	esitoCreaSocket = pesaConfezioni.verficaConn(); //;providerManuale.creaSocket();
                  	if (esitoCreaSocket) {
                  		valore_peso_letto = pesaConfezioni.pesoLordo();
                  	}

                  	pesaConfezioni.chiudi();

                  } else {

                	  esitoCreaSocket = false; 

                  }
 
            	 
                if (esitoCreaSocket) {
                    /////////////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA CONFEZIONI RIUSCITA  ///
                    /////////////////////////////////////////////////////
                    lFrame.lab_socket_bil_conf.setText(lFrame.lab_socket_bil_conf.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00 + " ("+ valore_peso_letto + ")");
                } else {
 
                    ////////////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA CONFEZIONI FALLITA  ///
                    ///////////////////////////////////////////////////
                    lFrame.lab_socket_bil_conf.setText(lFrame.lab_socket_bil_conf.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                }
            } else {
                ////////////////////////////////////////////////////////
                // CREAZIONE SOCKET BILANCIA CONFEZIONI NON PREVISTA  ///
                ////////////////////////////////////////////////////////
                lFrame.lab_socket_bil_conf.setText(lFrame.lab_socket_bil_conf.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

            }
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Creazione Gestore Comunicazione Bilancia Chimica Colori
            lFrame.lab_socket_bil_chimica.setVisible(true);
            valore_peso_letto = ""; 
            if (!IndirizziBilance.indirizzi.get(ID_BILANCIA_MANUALE).equals("")) {
            	
            	 Boolean esitoCreaSocket = false; 

                 if (PingAddress(IndirizziBilance.indirizzi.get(ID_BILANCIA_MANUALE), ADDRESS_PING_TIMEOUT)) {

                 	ClientPesaTLB4  pesaManuale = new ClientPesaTLB4(ID_BILANCIA_MANUALE);

                 	esitoCreaSocket = pesaManuale.verficaConn(); 
                 	
                 	if (esitoCreaSocket) {
                
                 		valore_peso_letto = pesaManuale.pesoLordo();
                 		
                 		if (!valore_peso_letto.equals("")) {
	 
                 			valore_peso_letto = Double.toString(
                 					Integer.parseInt(valore_peso_letto) /
                 					Integer.parseInt(ParametriSingolaMacchina.parametri.get(223)));	 
                 		}
                	}

                 	pesaManuale.chiudi();
                 	
                 } else {

                 	esitoCreaSocket = false; 

                 }
  

                if (esitoCreaSocket) {
                    //////////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA CHIMICA RIUSCITA  ///
                    //////////////////////////////////////////////////
                    lFrame.lab_socket_bil_chimica.setText(lFrame.lab_socket_bil_chimica.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00 + " ("+ valore_peso_letto + ")");
                } else {

                    /////////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA CHIMICA FALLITA  ///
                    /////////////////////////////////////////////////
                    lFrame.lab_socket_bil_chimica.setText(lFrame.lab_socket_bil_chimica.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                }

            } else {
                /////////////////////////////////////////////////////////////
                // CREAZIONE SOCKET BILANCIA CHIMICA COLORI NON PREVISTA  ///
                /////////////////////////////////////////////////////////////
                lFrame.lab_socket_bil_chimica.setText(lFrame.lab_socket_bil_chimica.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Creazione Gestore Comunicazione Bilancia di Carico Ausiliaria
            lFrame.lab_socket_bil_carico_aux.setVisible(true);
            valore_peso_letto = "";
            if (!IndirizziBilance.indirizzi.get(ID_BILANCIA_CARICO_AUX).equals("")) {

           	 Boolean esitoCreaSocket = false; 

                if (PingAddress(IndirizziBilance.indirizzi.get(ID_BILANCIA_CARICO_AUX), ADDRESS_PING_TIMEOUT)) {

                	ClientPesaTLB4  pesaCaricoAux = new ClientPesaTLB4(ID_BILANCIA_CARICO_AUX);
                	
                	esitoCreaSocket = pesaCaricoAux.verficaConn(); //;providerManuale.creaSocket();
                	
                	if (esitoCreaSocket) {
                		
                		valore_peso_letto = pesaCaricoAux.pesoLordo();
                		
                	}

                	pesaCaricoAux.chiudi();
                	
                	
                	
                } else {

                	esitoCreaSocket = false; 

                }  

                if (esitoCreaSocket) {

                    ////////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA AUSILIARIA  ///
                    ////////////////////////////////////////////
                    lFrame.lab_socket_bil_carico_aux.setText(lFrame.lab_socket_bil_carico_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00 + " ("+ valore_peso_letto + ")");
                } else {

                    ///////////////////////////////////////////
                    // CREAZIONE SOCKET BILANCIA AUSILIARIA ///
                    ///////////////////////////////////////////
                    lFrame.lab_socket_bil_carico_aux.setText(lFrame.lab_socket_bil_carico_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                }

            } else {
                /////////////////////////////////////////////////////////
                // CREAZIONE SOCKET BILANCIA AUSILIARIA NON PREVISTA  ///
                ////////////////////////////////////////////////////////
                lFrame.lab_socket_bil_carico_aux.setText(lFrame.lab_socket_bil_carico_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

            }


            SessionLogger.logger.log(Level.INFO, "Avvio Server Socket Eseguito");

            SessionLogger.logger.log(Level.CONFIG, "Carattere di Separazione A stringa IO ={0}Carattere di Separazione B String IO ={1}"
                    + "Indirizzi periferiche di IO, solo ultimo numero ={2}"
                    + "Sottorete di riferimento indirizzi IP macchina e periferiche IO ={3}Porte di comunicazione periferiche ={4}{5}{6}"
                    + "Tipologie di periferche (0) Mduino - (1) EB80 ={7}Timeout di Comunicazione periferiche ={8}{9}{10}"
                    + "Descrizione identificative delle periferche{11}{12}{13}Id Periferca delle Uscite ={14}{15}{16}"
                    + "Posizione delle uscite sulle periferiche ={17}{18}{19}Id Periferca degli Ingressi = {20}{21}{22}"
                    + "Posizione degli ingressi sulle periferiche ={23}{24}{25}",
                    new Object[]{ParametriSingolaMacchina.parametri.get(468),
                        ParametriSingolaMacchina.parametri.get(469),
                        ParametriSingolaMacchina.parametri.get(470),
                        ParametriSingolaMacchina.parametri.get(464),
                        ParametriSingolaMacchina.parametri.get(471),
                        ParametriSingolaMacchina.parametri.get(472),
                        ParametriSingolaMacchina.parametri.get(473),
                        ParametriSingolaMacchina.parametri.get(474),
                        ParametriSingolaMacchina.parametri.get(475),
                        ParametriSingolaMacchina.parametri.get(476),
                        ParametriSingolaMacchina.parametri.get(477),
                        ParametriSingolaMacchina.parametri.get(478),
                        ParametriSingolaMacchina.parametri.get(479),
                        ParametriSingolaMacchina.parametri.get(480),
                        ParametriSingolaMacchina.parametri.get(481),
                        ParametriSingolaMacchina.parametri.get(482),
                        ParametriSingolaMacchina.parametri.get(483),
                        ParametriSingolaMacchina.parametri.get(484),
                        ParametriSingolaMacchina.parametri.get(485),
                        ParametriSingolaMacchina.parametri.get(486),
                        ParametriSingolaMacchina.parametri.get(487),
                        ParametriSingolaMacchina.parametri.get(488),
                        ParametriSingolaMacchina.parametri.get(489),
                        ParametriSingolaMacchina.parametri.get(490),
                        ParametriSingolaMacchina.parametri.get(491),
                        ParametriSingolaMacchina.parametri.get(492)});

            new GestoreIO(
                    ParametriSingolaMacchina.parametri.get(468),
                    ParametriSingolaMacchina.parametri.get(469),
                    ParametriSingolaMacchina.parametri.get(470),
                    ParametriSingolaMacchina.parametri.get(464),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(471) + ParametriSingolaMacchina.parametri.get(472) + ParametriSingolaMacchina.parametri.get(473)),
                    ParametriSingolaMacchina.parametri.get(474),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(475) + ParametriSingolaMacchina.parametri.get(476) + ParametriSingolaMacchina.parametri.get(477)),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(478) + ParametriSingolaMacchina.parametri.get(479) + ParametriSingolaMacchina.parametri.get(480)),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(481) + ParametriSingolaMacchina.parametri.get(482) + ParametriSingolaMacchina.parametri.get(483)),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(484) + ParametriSingolaMacchina.parametri.get(485) + ParametriSingolaMacchina.parametri.get(486)),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(487) + ParametriSingolaMacchina.parametri.get(488) + ParametriSingolaMacchina.parametri.get(489)),
                    EliminaCarattereVuotoiNephos(ParametriSingolaMacchina.parametri.get(490) + ParametriSingolaMacchina.parametri.get(491) + ParametriSingolaMacchina.parametri.get(492)),
                    ParametriSingolaMacchina.parametri.get(467));

            //Creazione Pannelli di Visualizzazione IO
            GestoreIO_CreatePanels();

            //Impostazione Visibilita Pannelli
            GestoreIO_ImpostaVisPannelli(VIS_PANNELLI_DIR);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Verfica retroazione Mduino Main Panel
            lFrame.lab_com_mduino_main_panel.setVisible(true);
         
            //Attivazione Uscita Retroazione verifica Mduino
            GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_MAIN_PANEL, OUTPUT_TRUE_CHAR);

            if (GestoreIO.getDevices().get(0).isEnaled()) {

                Thread.sleep(200);

                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_RETROAZIONE_MDUINO_MAIN_PANEL)) {
                    //////////////////////////////////////
                    // TEST MDUINO MAINPANEL SUPERATO  ///
                    /////////////////////////////////////
                    lFrame.lab_com_mduino_main_panel.setText(lFrame.lab_com_mduino_main_panel.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
                    SessionLogger.logger.info("Test Mduino MAINPANEL                      Superato");
                } else {
                    //////////////////////////////////////
                    // TEST MDUINO MAINPANEL FALLITO  ///
                    /////////////////////////////////////
                    lFrame.lab_com_mduino_main_panel.setText(lFrame.lab_com_mduino_main_panel.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                    SessionLogger.logger.warning("Test Mduino MAINPANEL                        Fallito");
                }

                //Disattivazione Uscita Retroazione verifica Mduino
                GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_MAIN_PANEL, OUTPUT_FALSE_CHAR);

                Thread.sleep(200);
            } else {

                //////////////////////////////////////
                // TEST MDUINO MAINPANEL NON PREVISTO  ///
                /////////////////////////////////////
                lFrame.lab_com_mduino_main_panel.setText(lFrame.lab_com_mduino_main_panel.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

                SessionLogger.logger.info("Test Mduino MAINPANEL                    Non Abilitato");
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Verfica retroazione Mduino Silos Panel
            lFrame.lab_com_mduino_silos_panel.setVisible(true);

            if (GestoreIO.getDevices().get(1).isEnaled()) {
                //Attivazione Uscita Retroazione verifica Mduino
                GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL, OUTPUT_TRUE_CHAR);

                Thread.sleep(200);

                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_RETROAZIONE_MDUINO_SILOS_PANEL)) {

                    //////////////////////////////////////
                    // TEST MDUINO SILOSPANEL SUPERATO ///
                    //////////////////////////////////////
                    lFrame.lab_com_mduino_silos_panel.setText(lFrame.lab_com_mduino_silos_panel.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
                    SessionLogger.logger.info("Test Mduino SILOSPANEL                    Superato");

                } else {

                    //////////////////////////////////////
                    // TEST MDUINO SILOSPANEL FALLITO  ///
                    //////////////////////////////////////
                    lFrame.lab_com_mduino_silos_panel.setText(lFrame.lab_com_mduino_silos_panel.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                    SessionLogger.logger.warning("Test Mduino SILOSPANEL                    Fallito");
                }

                //Disattivazione Uscita Retroazione verifica Mduino
                GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL, OUTPUT_FALSE_CHAR);

                Thread.sleep(200);
            } else {

                ///////////////////////////////////////////
                // TEST MDUINO SILOSPANEL NON PREVISTO  ///
                ///////////////////////////////////////////
                lFrame.lab_com_mduino_silos_panel.setText(lFrame.lab_com_mduino_silos_panel.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

                SessionLogger.logger.info("Test Mduino SILOSPANEL                    Non Abilitato");
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Verfica retroazione Mduino Silos Panel Aux
            lFrame.lab_com_mduino_silos_panel_aux.setVisible(true);

            if (GestoreIO.getDevices().get(2).isEnaled()) {
                //Attivazione Uscita Retroazione verifica Mduino
                GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL_AUX, OUTPUT_TRUE_CHAR);

                Thread.sleep(200);

                if (GestoreIO_OttieniStatoIngresso(INGRESSO_LOGICO_RETROAZIONE_MDUINO_SILOS_PANEL_AUX)) {

                    ///////////////////////////////////////////////
                    // TEST MDUINO SILOSPANELAUX SUPERATO  ///
                    //////////////////////////////////////////////
                    lFrame.lab_com_mduino_silos_panel_aux.setText(lFrame.lab_com_mduino_silos_panel_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);

                    SessionLogger.logger.info("Test Mduino SILOSPANELAUX                    Superato");

                } else {

                    ///////////////////////////////////////////////
                    // TEST MDUINO SILOSPANELAUX FALLITO  ///
                    //////////////////////////////////////////////
                    lFrame.lab_com_mduino_silos_panel_aux.setText(lFrame.lab_com_mduino_silos_panel_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                    SessionLogger.logger.warning("Test Mduino SILOSPANELAUX                    Fallito");

                }
                //Disattivazione Uscita Retroazione verifica Mduino
                GestoreIO_ModificaOut(USCITA_RETROAZIONE_VERIFICA_MDUINO_SILOS_PANEL_AUX, OUTPUT_FALSE_CHAR);

                Thread.sleep(200);
            } else {

                ///////////////////////////////////////////////
                // TEST MDUINO SILOSPANELAUX NON PREVISTO  ///
                //////////////////////////////////////////////
                lFrame.lab_com_mduino_silos_panel_aux.setText(lFrame.lab_com_mduino_silos_panel_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);

                SessionLogger.logger.info("Test Mduino SILOSPANELAUX                    Non Abilitato");
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Verfica pressione Impianto Pneumatico
            lFrame.lab_misura_pressione.setVisible(true);

            if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(498))) {
                String pressione_str = GestoreIO_OttieniValoreIngressoAnalogico(
                        INGRESSO_ANALOGICO_PRESSOSTATO_MDUINO_MAINPANEL,
                        DEVICE_INGRESSO_PRESSOSTATO_MDUINO_MAINPANEL);

                if (!pressione_str.equals("")) {

                    double pressione = Double.parseDouble(pressione_str);
                    double pressione_richiesta = Double.parseDouble(ParametriSingolaMacchina.parametri.get(493));

                    if (pressione >= pressione_richiesta) {

                        /////////////////////////////
                        // PRESSIONE SUFFUCIENTE  ///
                        ////////////////////////////
                        lFrame.lab_misura_pressione.setText(lFrame.lab_misura_pressione.getText()
                                + LOADING_IMG_TEXT_TAB
                                + LOADING_IMG_TEXT_28
                                + pressione
                                + ">="
                                + pressione_richiesta
                                + LOADING_IMG_TEXT_28
                                + LOADING_IMG_TEXT_TAB
                                + LOADING_IMG_TEXT_00);

                        SessionLogger.logger.log(Level.INFO, "Test Misura Pressione                     Superato - p={0}", pressione);
                    } else {

                        ///////////////////////////////
                        // PRESSIONE INSUFFICIENTE  ///
                        ///////////////////////////////
                        lFrame.lab_misura_pressione.setText(lFrame.lab_misura_pressione.getText()
                                + LOADING_IMG_TEXT_TAB
                                + LOADING_IMG_TEXT_28
                                + pressione
                                + ">="
                                + pressione_richiesta
                                + LOADING_IMG_TEXT_28
                                + LOADING_IMG_TEXT_TAB
                                + LOADING_IMG_TEXT_01);

                        SessionLogger.logger.log(Level.WARNING, "Test Misura Pressione                     Fallito - p={0}", pressione);

                    }
                } else {

                    ///////////////////////////////
                    // PRESSIONE NON RILEVATA  ///
                    ///////////////////////////////
                    lFrame.lab_misura_pressione.setText(lFrame.lab_misura_pressione.getText()
                            + LOADING_IMG_TEXT_TAB
                            + LOADING_IMG_TEXT_01);

                    SessionLogger.logger.warning("Test Misura Pressione                     Dato non ricevuto");
                }

                Thread.sleep(200);
            } else {

                //////////////////////////////////////
                // MISURA PRESSIONE NON ABILITATA  ///
                //////////////////////////////////////
                lFrame.lab_misura_pressione.setText(lFrame.lab_misura_pressione.getText()
                        + LOADING_IMG_TEXT_TAB
                        + LOADING_IMG_TEXT_02);

                SessionLogger.logger.info("Test Misura Pressione                     Non Abilitato");

            }

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Crea pannelli inverter
            Inverter_Gefran_BDI50_Crea_Pannelli();

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Impotsazione visibilità pannelli inverter
            Inverter_Gefran_BDI50_ImpostaVisPannelli(VIS_PANNELLI_DIR);
            
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Verfica Inverter
            lFrame.lab_inverter_mixer.setVisible(true);

            String rep_inverter_mixer =inverter_mix.cambiaVelInverter(DEFAULT_VEL_INV);

            if (!rep_inverter_mixer.equals("")) {

                lFrame.lab_inverter_mixer.setText(lFrame.lab_inverter_mixer.getText() + LOADING_IMG_TEXT_TAB + rep_inverter_mixer);

                SessionLogger.logger.log(Level.INFO, "Test INVERTER MIX                     Superato - rep={0}", rep_inverter_mixer);

            } else {
                lFrame.lab_inverter_mixer.setText(lFrame.lab_inverter_mixer.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                SessionLogger.logger.log(Level.WARNING, "Test INVERTER MIX                     Fallito - rep={0}", rep_inverter_mixer);
            }

            lFrame.lab_inverter_screws.setVisible(true);

            String rep_inverter_screws = inverter_screws.cambiaVelInverter(DEFAULT_VEL_INV);

            if (!rep_inverter_screws.equals("")) {

                lFrame.lab_inverter_screws.setText(lFrame.lab_inverter_screws.getText() + LOADING_IMG_TEXT_TAB + rep_inverter_screws);

                SessionLogger.logger.log(Level.INFO, "Test INVERTER SCREWS                     Superato - rep={0}", rep_inverter_mixer);

            } else {
                lFrame.lab_inverter_screws.setText(lFrame.lab_inverter_screws.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);

                SessionLogger.logger.log(Level.WARNING, "Test INVERTER SCREWS                     Fallito - rep={0}", rep_inverter_mixer);
            }

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  Configurazione e Verifica EB80
            ArrayList<IODevice> devices = GestoreIO.getDevices();
            ArrayList<Boolean> status = new ArrayList<>();
            for (int i = 0; i < devices.size(); i++) {

                if (devices.get(i).getType() == 1) {
                    if (devices.get(i).isEnaled()) {
                        status.add(devices.get(i).clientEB80.isConfigured());
                    }

                }

            }

            lFrame.lab_eb80_main.setVisible(true);

            if (status.size() > 0) {

                if (status.get(0)) {

                    ////////////////////////////////////////
                    // VERIFICA EB80 MAINPANEL SUPERATA  ///
                    ////////////////////////////////////////
                    lFrame.lab_eb80_main.setText(lFrame.lab_eb80_main.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);

                    SessionLogger.logger.log(Level.INFO, "Test Configurazione EB80 MAINPANEL                     Superato");

                } else {

                    ///////////////////////////////////////
                    // VERIFICA EB80 MIANPANEL FALLITA  ///
                    ///////////////////////////////////////
                    lFrame.lab_eb80_main.setText(lFrame.lab_eb80_main.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                    
                    SessionLogger.logger.log(Level.WARNING, "Test Configurazione EB80 MAINPANEL                     Fallito");
                }
            } else {

                //////////////////////////////////////////////
                // VERIFICA EB80 MIANPANEL NON ABILITATA  ///
                /////////////////////////////////////////////
                lFrame.lab_eb80_main.setText(lFrame.lab_eb80_main.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);
                
                SessionLogger.logger.log(Level.INFO, "Test Configurazione EB80 MAINPANEL                     Non Abilitato");
            }

            lFrame.lab_eb80_silos.setVisible(true);

            if (status.size() > 1) {
                if (status.get(1)) {

                    /////////////////////////////////////////
                    // VERIFICA EB80 SILOSPANEL SUPERATA  ///
                    /////////////////////////////////////////
                    lFrame.lab_eb80_silos.setText(lFrame.lab_eb80_silos.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
                    
                     SessionLogger.logger.log(Level.INFO, "Test Configurazione EB80 SILOSPANEL                     Superato");

                } else {

                    ////////////////////////////////////////
                    // VERIFICA EB80 SILOSPANEL FALLITA  ///
                    ////////////////////////////////////////
                    lFrame.lab_eb80_silos.setText(lFrame.lab_eb80_silos.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                    
                      SessionLogger.logger.log(Level.WARNING, "Test Configurazione EB80 SILOSPANEL                     Fallito");
                }
            } else {

                /////////////////////////////////////////
                // VERIFICA EB80 SILOSPANEL NON ABILITATA  ///
                /////////////////////////////////////////
                lFrame.lab_eb80_silos.setText(lFrame.lab_eb80_silos.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);
                
                  SessionLogger.logger.log(Level.INFO, "Test Configurazione EB80 SILOSPANEL                     Non Abilitato");
            }

            lFrame.lab_eb80_silos_aux.setVisible(true);
            if (status.size() > 2) {

                if (status.get(2)) {

                    /////////////////////////////////////////////
                    // VERIFICA EB80 SILOSPANEL AUX SUPERATA  ///
                    /////////////////////////////////////////////
                    lFrame.lab_eb80_silos_aux.setText(lFrame.lab_eb80_silos_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_00);
                    
                      SessionLogger.logger.log(Level.INFO, "Test Configurazione EB80 SILOSPANELAUX                     Superato");

                } else {

                    /////////////////////////////////////////////
                    // VERIFICA EB80 SILOSPANEL AUX FALLITA  ///
                    /////////////////////////////////////////////
                    lFrame.lab_eb80_silos_aux.setText(lFrame.lab_eb80_silos_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_01);
                    
                    SessionLogger.logger.log(Level.WARNING, "Test Configurazione EB80 SILOSPANELAUX                     Fallito");
                }
            } else {

                //////////////////////////////////////////////////
                // VERIFICA EB80 SILOSPANEL AUX NON ABILITATA  ///
                //////////////////////////////////////////////////
                lFrame.lab_eb80_silos_aux.setText(lFrame.lab_eb80_silos_aux.getText() + LOADING_IMG_TEXT_TAB + LOADING_IMG_TEXT_02);
                
                    SessionLogger.logger.log(Level.INFO, "Test Configurazione EB80 SILOSPANELAUX                     Non Abilitato");
            }
            
            
            try {
                //////////////////////////////////////////////////////////////// PROGRESS BAR
                lFrame.progress_bar.setValue(80);
                SessionLogger.logger.config("Fine Impostazione Frame Principale");

                sleep(5000);

                //Imposta Visibilità Pannello Principale
                setFrame();

                lFrame.setVisible(false);

                SessionLogger.logger.info("Inizio Nuova Sessione di Lavoro");
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(CloudFabFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            

        } catch (InitializeException | ComunicationException | ProcessException | MachineCredentialsNotFoundException | HeadlessException | NumberFormatException ex) {
            log.error(ex.toString());

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(CloudFabFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //Configurazione Finestra

    private static void setFrame() {

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.info("Configurazione Frame");

        //Assegnazione del Pannello al Frame
        FRAME.setContentPane((Pannello00_Principale) pannelli[0]);

        //Dimensionamento Finestra
        FRAME.setSize(RISOLUZIONE_LARGHEZZA_PANNELLO, RISOLUZIONE_ALTEZZA_PANNELLO);

        //Operazione in Seguito alla Chiusura della Finestra
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Centraggio della Finestra
        FRAME.setLocationRelativeTo(null);

        //Eliminazione Decorazioni Finestra
        FRAME.setUndecorated(true);

        FRAME.setAlwaysOnTop(true);

        //Impostazione Visibilità Finestra Principale
        FRAME.setVisible(true);

        //Inizializzazione Pannello 
        ((Pannello00_Principale) pannelli[0]).initPanel();

        //Memorizzazione Logger della Sessione
        SessionLogger.logger.info("Configurazione Frame -                             OK");

    }
}
