package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateTypeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ProcessException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateContentException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.DatiAggiornamentoNotFoundException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateVersionException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.ComunicationException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InitializeException;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.entity.MacchinaOri;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreArchiviazioneLogDopoAggiornamento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.GestoreSpostamentoFileLog;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ImportaDati4_0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EsportaDati4_0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.SynchroFileFTP4_0;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ENTITY_MANAGER_FACTORY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_BREAK_LINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_PROGRESS_BAR;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ABILITA_SWITCH_RETE;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.MacchinaOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import eu.personalfactory.cloudfab.syncorigami.process.MacchinaProcess;
import eu.personalfactory.cloudfab.syncorigami.process.RipristinoProcess;
import eu.personalfactory.cloudfab.syncorigami.utils.EmailSender;
import eu.personalfactory.cloudfab.syncorigami.utils.SyncOrigamiConstants;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPListParseException;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.swing.BorderFactory;
import javax.swing.JLabel; 
import javax.swing.JProgressBar;
import org.apache.log4j.Logger;
import org.jdom.Element;

@SuppressWarnings("serial")
public class Pannello12_Aggiornamento extends MyStepPanel {

    private static Logger log = Logger.getLogger(Pannello12_Aggiornamento.class);
    private static boolean terminaProgressBar = true;
    private static boolean erroreBloccante = false;
    private static boolean terminaThread = false;
    private static boolean erroreInvioLog = false;
    private static boolean erroreInvioBkpSql = false;
    private static boolean erroreInvioBkpXml = false;
    private JProgressBar colorProgressBar;
    private Element elm;
    private String[] lista = new String[14];
    private ArrayList<String> messageList;
    private boolean eseguito;
    //####### NOTIFICHE EMAIL ####################################################
//  private static final String mailSenderUser = "syncorigami@isolmix.com";
//  private static final String mailSenderPassword = "syncorigamimail";
//  private static final String mailHost = "smtp.gmail.com";
//  private static final String mailAddressSender = "syncorigami@isolmix.com";
//  private static final String mailAddressReceiver = "syncorigami@isolmix.com";
//  private static final String mailObject = "ERRORE SYNCORIGAMI MACCHINA";
//  private static final String mailPathFile = "dist/log/syncorigami.log";
    //############################################################################

    //COSTRUTTORE
    public Pannello12_Aggiornamento() {

        super();

        setLayer();

        new ThreadCaricaImageSfondo(this).start();

    }

    //Dichiarazioni Pannello
    private void setLayer() {

        //Dichiarazione File Parametri
        impostaXml();

        //Definizione Caratteristiche Label Pannello
        impostaDimLabelPlus(19);
        impostaDimLabelSimple(2);
        impostaDimLabelHelp(2);
        impostaDimLabelTitle(1);
        impostaDimLabelProg(0);
        impostaDimLabelBut(3);
        impostaColori(7);

        //Inizializzazione Colore Label Title
        initColorLabelTitle(elemColor[6]);

        //Inserimento Pulsante Freccia Indietro
        inserisciButtonFreccia();

        //Configurazione di Base Pannello
        configuraPannello();

        //Dichiarazione e Definizione Progress Bar
        elm = root.getChild(XML_PROGRESS_BAR);

        colorProgressBar = new JProgressBar();

        //Impostazione Colore Bordi
        colorProgressBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //Impostazione Dimensioni Progress Bar
        colorProgressBar.setBounds(
                ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

        //Impostazione Colore Progress Bar
        colorProgressBar.setForeground(elemColor[2]);

        //Impostazione Visibilità Progress Bar
        colorProgressBar.setVisible(true);

        //Impostazione Abilitazione Progress Bar
        colorProgressBar.setEnabled(true);

        //Aggiunta Componente al Pannello
        add(colorProgressBar);

        //Impostazione Colori Label Simple
        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setForeground(elemColor[0]);
        }

        //Impostazione Colori Label Help
        for (JLabel elemLabelHelp1 : elemLabelHelp) {
            elemLabelHelp1.setForeground(elemColor[1]);
        }

        //Modifica la Visibilità di Default delle Righe di Aiuto
        impostaVisibilitaAiuto(true);

        inserisciControllaSelezione();

    }

    //Inizializzazione Pannello
    public void initPanel() {

        //Spostamento File di Log
        GestoreSpostamentoFileLog();

        //Variabile di Controllo per l'aggiornamento del dizionario
        eseguito = false;

        new LeggiDizionario().start();

        colorProgressBar.setValue(0);

        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setText("");
        }

        messageList = new ArrayList<>();

        impostaVisibilitaAiuto(false);

        butFreccia.setVisible(true);

        setPanelVisibile();

        initLabelMessage();
        
       System.out.println("510 =" + ParametriSingolaMacchina.parametri.get(510));
       System.out.println("511 =" + ParametriSingolaMacchina.parametri.get(511)); 
       
        if (ABILITA_SWITCH_RETE) {
        	
        	try {
				Runtime.getRuntime().exec(ParametriSingolaMacchina.parametri.get(511));
				
			} catch (IOException e1) {

				SessionLogger.logger.severe("Errore durante lo switching della rete");
			}
        	
        }

        terminaProgressBar = true;
        
        //Imposta Visibilita Pulsante Synchro4.0
        elemBut[2].setVisible(Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(508)));	
         
    }

    // Inizializzazione visualizzazione Pannello
    public void initVisualizzazione() {

        colorProgressBar.setValue(0);

        for (JLabel elemLabelPlu : elemLabelPlus) {
            elemLabelPlu.setText("");
        }

        messageList = new ArrayList<>();

        controllaSelezione.lista_FILTR = new String[elemLabelPlus.length];

        for (int i = 0; i < elemLabelPlus.length; i++) {

            controllaSelezione.lista_FILTR[i] = "";

        }

        controllaSelezione.scorriLista(0);

    }

    //Lettura Informazioni da Database
    private class LeggiDizionario extends Thread {

        @Override
        public void run() {

            if (!ParametriSingolaMacchina.parametri.get(111).equals(language)) {

                //Impostazione Lingua del Pannello
                language = ParametriSingolaMacchina.parametri.get(111);

                //Aggiornamento Testo Label Tipo Help
                elemLabelHelp[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 11, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelHelp[1].setText(HTML_STRINGA_INIZIO
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 209, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_BREAK_LINE
                        + HTML_BREAK_LINE
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 210, ParametriSingolaMacchina.parametri.get(111)))
                        + HTML_STRINGA_FINE);

                //Aggiornamento Testo Label Tipo Title
                elemLabelTitle[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 207, ParametriSingolaMacchina.parametri.get(111)));

                //Aggiornamento Testo Pulsanti
                elemLabelSimple[0].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 208, ParametriSingolaMacchina.parametri.get(111)));
                elemLabelSimple[1].setText(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 316, ParametriSingolaMacchina.parametri.get(111)));

            }

        }
    }

    //Upload aggiornamento su FTP per server Download aggiornamenti da FTP da server
    private class EseguiAggiornamento extends Thread {

        private Pannello12_Aggiornamento panelAgg;
//    //###########################################################################
//    //#################### OGGETTO EMAIL SENDER #################################
//    //###########################################################################
//    EmailSender email = new EmailSender(mailSenderUser,
//            mailSenderPassword,
//            mailHost,
//            mailAddressSender,
//            mailAddressReceiver,
//            mailObject,
//            mailPathFile);
//    //Recupero l'id della macchina per inserirlo nella mail di notifica in caso di errore
//    MacchinaOriJpaController mjc = new MacchinaOriJpaController(null, FabCloudConstants.ENTITY_MANAGER_FACTORY);
//    MacchinaOri macchina = mjc.findMacchinaOri();

        public EseguiAggiornamento(Pannello12_Aggiornamento panelAgg, boolean erroreBloccante) {
            this.panelAgg = panelAgg;
        }

        @Override
        public void run() {

            //########################################################################
            //#################### NOTIFICHE VIA E-MAIL ##############################
            //########################################################################
            try {
                SyncOrigamiConstants.loadMailPropertiesFromDb(ENTITY_MANAGER_FACTORY);
            } catch (InitializeException ex) {
                log.warn(ex + "IMPOSSIBILE RECUPERARE I PARAMETRI DI NOTIFICA MAIL");
            }

            EmailSender email = new EmailSender(SyncOrigamiConstants.MAIL_SENDER_USER,//mailSenderUser
                    SyncOrigamiConstants.MAIL_SENDER_PASSWORD,//mailSenderPassword,
                    SyncOrigamiConstants.MAIL_HOST,//mailHost,
                    SyncOrigamiConstants.MAIL_ADDRESS_SENDER,//mailAddressSender,
                    SyncOrigamiConstants.MAIL_ADDRESS_RECEIVER,//mailAddressReceiver,
                    SyncOrigamiConstants.MAIL_OBJECT,//mailObject,
                    SyncOrigamiConstants.MAIL_PATH_FILE// mailPathFile
            );
            //Recupero l'id della macchina per inserirlo nella mail di notifica in caso di errore
            MacchinaOriJpaController mjc = new MacchinaOriJpaController(null, ENTITY_MANAGER_FACTORY);
            MacchinaOri macchina = mjc.findMacchinaOri();
            //########################################################################

            while (!erroreBloccante) {

                try {

                    log.info("############################################################");

                    SyncOrigamiConstants.loadMacchinaPropertiesFromDb(ENTITY_MANAGER_FACTORY);

                    log.info("######## PROPRIETA' CARICATE DAL DATABASE ########");

                    try {

                        log.info("############################################################");

                        MacchinaProcess.uploadUpdate(ENTITY_MANAGER_FACTORY, panelAgg);
                        colorProgressBar.setValue(50);

                        log.info("######## FINE METODO MacchinaProcess.uploadUpdate ########");

                    } catch (InitializeException ex) {
                        //####### NOTIFICA ERRORE CON E-MAIL ###############################
                        try {
                            email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.uploadUpdate " + ex.toString());
                        } catch (AddressException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (NoSuchProviderException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (MessagingException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        }
                        //##################################################################
                        //ERRORE IN UPLOAD 
                        //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 438, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                    } catch (ProcessException ex) {

                        //####### NOTIFICA ERRORE CON E-MAIL ###############################
                        /**try {
                            email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.uploadUpdate " + ex.toString());
                        } catch (AddressException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (NoSuchProviderException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (MessagingException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        }*/
                        //##################################################################

                        //ERRORE IN UPLOAD 
                        //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 438, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                    } catch (ComunicationException ex) {
                        //####### NOTIFICA ERRORE CON E-MAIL ###############################
                        /**try {
                            email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.uploadUpdate " + ex.toString());
                        } catch (AddressException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (NoSuchProviderException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (MessagingException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        }*/
                        //##################################################################

                        //ERRORE DI COMUNICAZIONE! VERIFICARE LA CONNESSIONE AD INTERNET E RIPROVARE
                        //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 449, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                    } catch (DatiAggiornamentoNotFoundException ex) {
                        //####### NOTIFICA ERRORE CON E-MAIL ###############################
                       /** try {
                            email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.uploadUpdate " + ex.toString());
                        } catch (AddressException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (NoSuchProviderException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        } catch (MessagingException ex1) {
                            log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                        }*/
                        //##################################################################

                        //ERRORE IN UPLOAD 
                        //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 438, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                    } finally {

                        try {
                            log.info("############################################################");

                            MacchinaProcess.downloadAllUpdates(
                                    ENTITY_MANAGER_FACTORY,
                                    panelAgg);
                            colorProgressBar.setValue(100);

                            log.info("####### FINE METODO  MacchinaProcess.downloadAllUpdates #######");

                        } catch (InvalidUpdateContentException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;
                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                           /** try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################
                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (InvalidUpdateTypeException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /*try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (InvalidUpdateVersionException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (InitializeException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (ProcessException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (ComunicationException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE DI COMUNICAZIONE! VERIFICARE LA CONNESSIONE AD INTERNET E RIPROVARE
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 449, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (PreexistingEntityException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (NonexistentEntityException ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } catch (Exception ex) {
                            erroreBloccante = true;
                            terminaProgressBar = true;

                            //####### NOTIFICA ERRORE CON E-MAIL #############################
                            /**try {
                                email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + "--METODO: MacchinaProcess.downloadAllUpdates " + ex.toString());
                            } catch (AddressException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (NoSuchProviderException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            } catch (MessagingException ex1) {
                                log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                            }*/
                            //################################################################

                            //ERRORE NEL DOWNLOAD
                            //SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 437, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                            panelAgg.setVisible(false);
                            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
                        } finally {

                            visButton(true);
                            erroreBloccante = true;
                            terminaProgressBar = true;
                        }

                    }//END FINALLY uploadUpdate

                } catch (InitializeException ex) {
                    erroreBloccante = true;
                    terminaProgressBar = true;

                    //####### NOTIFICA ERRORE CON E-MAIL #############################           
                    /**try {
                        email.sendEmail("MACCHINA : " + macchina.getIdMacchina() + " " + macchina.getDescriStab() + " SyncOrigamiConstants.loadMacchinaPropertiesFromDb " + ex.toString());
                    } catch (AddressException ex1) {
                        log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                    } catch (NoSuchProviderException ex1) {
                        log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                    } catch (MessagingException ex1) {
                        log.warn(ex + "IMPOSSIBILE INVIARE NOTIFICHE VIA MAIL " + ex1);
                    }*/
                    //################################################################

                    //ERRORE DI INIZIALIZZAZIONE
                    ///SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                    ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                            + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 439, ParametriSingolaMacchina.parametri.get(111)))
                            + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                            + "<br/>" + ex.toString() + "</html>",
                            "",//But0 vuoto
                            EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    panelAgg.setVisible(false);
                    ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

                } finally {

                    visButton(true);
                    erroreBloccante = true;
                    terminaProgressBar = true;
                }
            }//END WHILE
        }//END RUN
    }//END THREAD

    /**
     * Gestore di Crescita Automatica della Progress Bar *
     */
    private class AvviaProgressBar extends Thread {

        private final Pannello12_Aggiornamento pannelloCorrente;

        public AvviaProgressBar(
                Pannello12_Aggiornamento pannelloCorrente,
                boolean terminaProgressBar) {
            this.pannelloCorrente = pannelloCorrente;
        }

        @Override
        public void run() {
            while (pannelloCorrente.isVisible() & !terminaProgressBar) {

                if (colorProgressBar.getValue() < 90) {
                    colorProgressBar.setValue(colorProgressBar.getValue() + 1);
                }
                try {
                    AvviaProgressBar.sleep(5000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    //Costruisce un file xml contenente tutti i dati presenti su origamidb e lo invia sul server FTP
    private class InviaBackup extends Thread {

        private final Pannello12_Aggiornamento panelAgg;

        public InviaBackup(Pannello12_Aggiornamento panelAgg,
                boolean erroreInvioLog,
                boolean erroreInvioBkpSql,
                boolean erroreInvioBkpXml,
                boolean terminaThread) {
            this.panelAgg = panelAgg;
        }

        @Override
        public void run() {

            //Esco dal thread solo se l'ultimo metodo fallisce
            while (!terminaThread) {

                try {

                    //INVIO RICHIESTA DI ASSISTENZA IN CORSO
                    panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 444, ParametriSingolaMacchina.parametri.get(111)));

                    panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 445, ParametriSingolaMacchina.parametri.get(111)));

                    log.info("############################################################");

                    SyncOrigamiConstants.loadMacchinaPropertiesFromDb(ENTITY_MANAGER_FACTORY);

                    log.info("####### CARICATE PROPRIETA' DAL DATABASE ###################");

                    try {

                        log.info("#################INIZIO UPLOAD LOG SU FTP ###################");

                        RipristinoProcess.uploadAllLogFile(
                                ENTITY_MANAGER_FACTORY,
                                panelAgg);

                        log.info("################## CARICATO LOG SUL FTP #####################");

                    } catch (InitializeException ex) {
                        erroreInvioLog = true;

                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    } catch (ProcessException ex) {
                        erroreInvioLog = true;

                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    } catch (ComunicationException ex) {
                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                        erroreInvioLog = true;

                    } catch (IOException ex) {
                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                        erroreInvioLog = true;

                    } catch (InterruptedException ex) {
                        erroreInvioLog = true;

                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    } finally {
                        colorProgressBar.setValue(33);
                        try {
                            log.info("############ INIZIO UPLOAD BACKUP #########################");

                            RipristinoProcess.uploadBackupZeroSQL(ENTITY_MANAGER_FACTORY,
                                    panelAgg);
                            log.info("########### CARICATO BACKUP SQL SUL FTP ####################");

                        } catch (InitializeException ex) {
                            erroreInvioBkpSql = true;
                            terminaProgressBar = true;

                            //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 442, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                        } catch (ProcessException ex) {
                            erroreInvioBkpSql = true;
                            terminaProgressBar = true;

                            //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 442, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                        } catch (ComunicationException ex) {
                            erroreInvioBkpSql = true;
                            terminaProgressBar = true;

                            //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 442, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                        } catch (IOException ex) {
                            erroreInvioBkpSql = true;
                            terminaProgressBar = true;

                            //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 442, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                        } catch (InterruptedException ex) {
                            erroreInvioBkpSql = true;
                            terminaProgressBar = true;

                            //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 442, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                        } finally {
                            colorProgressBar.setValue(66);

////                            try {
////                                log.info("############################################################");
////
////                                RipristinoProcess.uploadBackupXML(FabCloudConstants.ENTITY_MANAGER_FACTORY, panelAgg);
////                                colorProgressBar.setValue(100);
////
////                                log.info("######### CARICATO FILE XML DI BACKUP SUL FTP #####################");
////
////                            } catch (InitializeException ex) {
////                                gestoreErrori.visualizzaErroreAggiornamento( "<html><br/>" + "IMPOSSIBILE INVIARE UN FILE XML DI BACKUP!<br/> CONTATTARE L'ASSISTENZA" + "<br/>" + ex.toString() + "</html>");
////                                erroreInvioBkpXml = true;
////                                terminaProgressBar = true;
////                            } catch (ProcessException ex) {
////                                gestoreErrori.visualizzaErroreAggiornamento( "<html><br/>" + "IMPOSSIBILE INVIARE UN FILE XML DI BACKUP!<br/> CONTATTARE L'ASSISTENZA" + "<br/>" + ex.toString() + "</html>");
////                                erroreInvioBkpXml = true;
////                                terminaProgressBar = true;
////                            } catch (ComunicationException ex) {
////                                gestoreErrori.visualizzaErroreAggiornamento( "<html><br/>" + "IMPOSSIBILE INVIARE UN FILE XML DI BACKUP!<br/> CONTATTARE L'ASSISTENZA" + "<br/>" + ex.toString() + "</html>");
////                                erroreInvioBkpXml = true;
////                                terminaProgressBar = true;
////                            } finally {
                            gestoreInvioFileBackup();

                            colorProgressBar.setValue(100);
                            terminaThread = true;
                            terminaProgressBar = true;

                            //################# REPORT INVIO DEI FILE ###############################
                            if (erroreInvioLog) {
                                //INVIO FILE DI LOG FALLITO
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 450, ParametriSingolaMacchina.parametri.get(111)));
                            } else {
                                //INVIATO FILE DI LOG
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 446, ParametriSingolaMacchina.parametri.get(111)));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                //Aggiunto Di Gaudio 24 Luglio
                                GestoreArchiviazioneLogDopoAggiornamento();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            }
                            if (erroreInvioBkpSql) {
                                //INVIO FILE DI BACKUP SQL FALLITO
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 451, ParametriSingolaMacchina.parametri.get(111)));

                            } else {
                                //INVIATO FILE SQL
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 447, ParametriSingolaMacchina.parametri.get(111)));
                            }
//                                if (erroreInvioBkpXml) {
//                                    panelAgg.inserisciRiga("<html><br/>INVIO FILE DI BACKUP XML FALLITO</html>");
//                                } else {
//                                    panelAgg.inserisciRiga("<html><br/>INVIATO FILE XML </html>");
//                                }

                            //PROCESSO DI INVIO TERMINATO
                            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 448, ParametriSingolaMacchina.parametri.get(111)));
                            visButton(true);

//                            }
                        }//END FINALLY uploadBackupZeroSQL

                    }//END FINALLY uploadAllLogFile

                } catch (InitializeException ex) {
                    terminaThread = true;
                    terminaProgressBar = true;

                    //ERRORE DI INIZIALIZZAZIONE SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                    ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento(
                            "<html><br/>"
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 439, ParametriSingolaMacchina.parametri.get(111)))
                            + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                            + "<br/>" + ex.toString() + "</html>",
                            "",//But0 vuoto
                            EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

                } finally {
                    colorProgressBar.setValue(100);
                    terminaThread = true;
                    terminaProgressBar = true;
                    visButton(true);
                }

            }//END WHILE

            //Esco dal thread solo se l'ultimo metodo fallisce
            while (!terminaThread) {

                try {

                    //INVIO RICHIESTA DI ASSISTENZA IN CORSO
                    panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 444, ParametriSingolaMacchina.parametri.get(111)));

                    panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 445, ParametriSingolaMacchina.parametri.get(111)));

                    log.info("############################################################");

                    SyncOrigamiConstants.loadMacchinaPropertiesFromDb(ENTITY_MANAGER_FACTORY);

                    log.info("####### CARICATE PROPRIETA' DAL DATABASE ###################");

                    try {

                        log.info("#################INIZIO UPLOAD LOG SU FTP ###################");

                        RipristinoProcess.uploadAllLogFile(
                                ENTITY_MANAGER_FACTORY,
                                panelAgg);

                        log.info("################## CARICATO LOG SUL FTP #####################");

                    } catch (InitializeException ex) {
                        erroreInvioLog = true;

                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                	EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    } catch (ProcessException ex) {
                        erroreInvioLog = true;

                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    } catch (ComunicationException ex) {
                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                        erroreInvioLog = true;

                    } catch (IOException ex) {
                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                        erroreInvioLog = true;

                    } catch (InterruptedException ex) {
                        erroreInvioLog = true;

                        //IMPOSSIBILE INVIARE IL FILE DI LOG! PREMERE OK PER CONTINUARE
                        ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 441, ParametriSingolaMacchina.parametri.get(111)))
                                + "<br/>" + ex.toString() + "</html>",
                                "",//But0 vuoto
                                EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    } finally {
                        colorProgressBar.setValue(33);
                        try {
                            log.info("############ INIZIO UPLOAD BACKUP #########################");

                            RipristinoProcess.uploadBackupZeroSQL(ENTITY_MANAGER_FACTORY,
                                    panelAgg);
                            log.info("########### CARICATO BACKUP SQL SUL FTP ####################");

                        } catch (InitializeException | ProcessException | ComunicationException | IOException | InterruptedException ex) {
                            erroreInvioBkpSql = true;
                            terminaProgressBar = true;

                            //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                            ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento("<html><br/>"
                                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 442, ParametriSingolaMacchina.parametri.get(111)))
                                    + "<br/>" + ex.toString() + "</html>",
                                    "",//But0 vuoto
                                    EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                        } //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                        //But0 vuoto
                        //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                        //But0 vuoto
                        //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                        //But0 vuoto
                        //IMPOSSIBILE INVIARE UN FILE SQL DI BACKUP! CONTATTARE L'ASSISTENZA
                        //But0 vuoto
                        finally {

                            colorProgressBar.setValue(66);

////                            try {
////                                log.info("############################################################");
////
////                                RipristinoProcess.uploadBackupXML(FabCloudConstants.ENTITY_MANAGER_FACTORY, panelAgg);
////                                colorProgressBar.setValue(100);
////
////                                log.info("######### CARICATO FILE XML DI BACKUP SUL FTP #####################");
////
////                            } catch (InitializeException ex) {
////                                gestoreErrori.visualizzaErroreAggiornamento( "<html><br/>" + "IMPOSSIBILE INVIARE UN FILE XML DI BACKUP!<br/> CONTATTARE L'ASSISTENZA" + "<br/>" + ex.toString() + "</html>");
////                                erroreInvioBkpXml = true;
////                                terminaProgressBar = true;
////                            } catch (ProcessException ex) {
////                                gestoreErrori.visualizzaErroreAggiornamento( "<html><br/>" + "IMPOSSIBILE INVIARE UN FILE XML DI BACKUP!<br/> CONTATTARE L'ASSISTENZA" + "<br/>" + ex.toString() + "</html>");
////                                erroreInvioBkpXml = true;
////                                terminaProgressBar = true;
////                            } catch (ComunicationException ex) {
////                                gestoreErrori.visualizzaErroreAggiornamento( "<html><br/>" + "IMPOSSIBILE INVIARE UN FILE XML DI BACKUP!<br/> CONTATTARE L'ASSISTENZA" + "<br/>" + ex.toString() + "</html>");
////                                erroreInvioBkpXml = true;
////                                terminaProgressBar = true;
////                            } finally {
                            gestoreInvioFileBackup();

                            colorProgressBar.setValue(100);
                            terminaThread = true;
                            terminaProgressBar = true;

                            //################# REPORT INVIO DEI FILE ###############################
                            if (erroreInvioLog) {
                                //INVIO FILE DI LOG FALLITO
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 450, ParametriSingolaMacchina.parametri.get(111)));
                            } else {
                                //INVIATO FILE DI LOG
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 446, ParametriSingolaMacchina.parametri.get(111)));

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                //Aggiunto Di Gaudio 24 Luglio 
                                GestoreArchiviazioneLogDopoAggiornamento();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                                
                            }
                            if (erroreInvioBkpSql) {
                                //INVIO FILE DI BACKUP SQL FALLITO
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 451, ParametriSingolaMacchina.parametri.get(111)));

                            } else {
                                //INVIATO FILE SQL
                                panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 447, ParametriSingolaMacchina.parametri.get(111)));
                            }
//                                if (erroreInvioBkpXml) {
//                                    panelAgg.inserisciRiga("<html><br/>INVIO FILE DI BACKUP XML FALLITO</html>");
//                                } else {
//                                    panelAgg.inserisciRiga("<html><br/>INVIATO FILE XML </html>");
//                                }

                            //PROCESSO DI INVIO TERMINATO
                            panelAgg.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 448, ParametriSingolaMacchina.parametri.get(111)));
                            visButton(true);

//                            }
                        }//END FINALLY uploadBackupZeroSQL

                    }//END FINALLY uploadAllLogFile

                } catch (InitializeException ex) {
                    terminaThread = true;
                    terminaProgressBar = true;

                    //ERRORE DI INIZIALIZZAZIONE SE IL PROBLEMA PERSISTE CONTATTARE L'ASSISTENZA
                    ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaErroreAggiornamento(
                            "<html><br/>"
                            + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 439, ParametriSingolaMacchina.parametri.get(111)))
                            + "<br/>" + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 440, ParametriSingolaMacchina.parametri.get(111)))
                            + "<br/>" + ex.toString() + "</html>",
                            "",//But0 vuoto
                            EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));

                    ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

                } finally {

                    colorProgressBar.setValue(100);
                    terminaThread = true;
                    terminaProgressBar = true;
                    visButton(true);
                }

            }//END WHILE

        }
    }

    //Gestione Pulsanti
    public void gestorePulsanti(String button) {

        switch (Integer.parseInt(button)) {

            case 0: {

                ////////////////////////////////////
                // PULSANTE AVVIA AGGIORNAMENTO  ///
                ////////////////////////////////////
                eseguito = true;

                //Disabilitazione pulsanti
                visButton(false);

                //Inizializzo la variabile rrore
                erroreBloccante = false;
                terminaProgressBar = false;

                initList();
                initVisualizzazione();

                /////initLabelMessage();
                new AvviaProgressBar(this, terminaProgressBar).start();

                new EseguiAggiornamento(this, erroreBloccante).start();
  
                break;
            }

            case 1: {

                //////////////////////
                // PULSANTE AIUTO  ///
                //////////////////////
                //Spostamento File di Log
                GestoreSpostamentoFileLog();

                int opzione = ((Pannello46_ErroriAggiornamento) pannelliCollegati.get(1)).gestoreErrori.visualizzaRichiestaAggiornamento(
                        "<html><br/>"
                        + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 452, ParametriSingolaMacchina.parametri.get(111)))
                        + "</html>",
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 129, ParametriSingolaMacchina.parametri.get(111))),
                        EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 322, ParametriSingolaMacchina.parametri.get(111))));
                if (opzione == 1) {

                    //ANNULLA = 0
                    //OK = 1
                    //Disabilita pulsanti
                    visButton(false);

                    //Inizializzo la variabile errore
                    terminaThread = false;
                    erroreInvioLog = false;
                    erroreInvioBkpSql = false;
                    erroreInvioBkpXml = false;

                    terminaProgressBar = false;

                    initList();
                    initVisualizzazione();

                    ///initLabelMessage();
                    new AvviaProgressBar(this, terminaProgressBar).start();
                    new InviaBackup(this,
                            erroreInvioLog,
                            erroreInvioBkpSql,
                            erroreInvioBkpXml,
                            terminaThread).start();

                }
                break;
            }
            case 2: {

                ////////////////////////////
                // PULSANTE SYNCHRO 4.0  ///
                ////////////////////////////
            	
            	//Lettura File per inserire il movimento di magazzin
            	try {
					SynchroFileFTP4_0();
				} catch (FTPDataTransferException | FTPAbortedException | FTPListParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                  
            	//Lettura File per inserire il movimento di magazzino
            	ImportaDati4_0();
            	

            	//Esportazione Tabella movimenti_sing_mac_ori
            	EsportaDati4_0(); 
            	//Esportazione Tabella Processo
            	
            	
                break;
            }

        }
    }

    public void visButton(boolean vis) {

        butFreccia.setEnabled(vis);
        for (int i = 0; i < elemBut.length; i++) {
            elemBut[i].setEnabled(vis);

        }
    }

    public void setValoreProgBar(int k) {
        colorProgressBar.setValue(colorProgressBar.getValue() + 1);

    }

    public void initList() {

        messageList = new ArrayList<>();

        lista = new String[elemLabelPlus.length];

        for (int i = 0; i < elemLabelPlus.length; i++) {

            lista[i] = "";
        }

        controllaSelezione.indice = 0;

    }

    public void initLabelMessage() {

        initList();

        inserisciControllaSelezione();

        if (messageList.size() > elemLabelPlus.length) {
            controllaSelezione.indice = lista.length - elemLabelPlus.length;
        }
        definisciColoriLista(elemColor[3], elemColor[4], elemColor[5]);
        definisciGestoreLista(elemLabelPlus, 0);
        definisciLista(lista);
        startThreadControllo();
        rendiListaUnselectable();
        butFreccia.setVisible(true);

    }

    public void inserisciRiga(String s) {

        messageList.add(s);

        controllaSelezione.lista_FILTR = new String[Math.max(elemLabelPlus.length, messageList.size())];

        for (int i = 0; i < messageList.size(); i++) {

            controllaSelezione.lista_FILTR[i] = messageList.get(i);
        }

        for (int i = messageList.size(); i < elemLabelPlus.length; i++) {

            controllaSelezione.lista_FILTR[i] = "";

        }

        if (messageList.size() > elemLabelPlus.length) {
            controllaSelezione.indice = messageList.size() - elemLabelPlus.length;
        }

        controllaSelezione.scorriLista(controllaSelezione.indice);

    }

    public void gestoreInvioFileBackup() {

        ////////////////////////////////////////////////////////////////////
        // COPIA DEI FILE DI BACKUP - DIGAUDIO 9-2016  /////////////////////
        ////////////////////////////////////////////////////////////////////
        boolean invioEseguito = false;

        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(377))) {

            try {

                invioEseguito = RipristinoProcess.uploadAllBackupFiles(ENTITY_MANAGER_FACTORY, this);

            } catch (ProcessException | ComunicationException | IOException | InterruptedException | InitializeException ex) {

                SessionLogger.logger.severe("ERRORE DURANTE LA TRASMISSIONE DEI FILE DI BACKUP");

            }

            if (!invioEseguito) {
                //INVIO FILE DI BACKUP FALLITO
                this.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 831, ParametriSingolaMacchina.parametri.get(111)));

            } else {

                //INVIO FILE DI LOG ESEGUITO
                this.inserisciRiga(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 830, ParametriSingolaMacchina.parametri.get(111)));

            }

        }

    }

    /**
     * Gestione Scambio Pannello Collegato *
     */
    public void gestoreScambioPannello() {

    	try {
    		Runtime.getRuntime().exec(ParametriSingolaMacchina.parametri.get(110));
			
		} catch (IOException e1) {

			SessionLogger.logger.severe("Errore durante lo switching della rete");
		}
    	
    	
        if (eseguito) {
            ParametriGlobali.init();
            ParametriSingolaMacchina.init();
        }

        this.setVisible(false);
        ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();

    }
}
