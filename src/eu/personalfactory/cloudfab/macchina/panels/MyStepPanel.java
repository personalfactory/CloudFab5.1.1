package eu.personalfactory.cloudfab.macchina.panels;

import eu.personalfactory.cloudfab.macchina.utility.GestoreEventiMouseLista;
import eu.personalfactory.cloudfab.macchina.utility.CloudFabFrame;
import eu.personalfactory.cloudfab.macchina.utility.ButtonInfoAction;
import eu.personalfactory.cloudfab.macchina.utility.GestoreScambioPanelMouseListener;
import eu.personalfactory.cloudfab.macchina.utility.GestoreScrollLista;
import eu.personalfactory.cloudfab.macchina.utility.FabCloudFont;
import eu.personalfactory.cloudfab.macchina.utility.ButtonAction;
import eu.personalfactory.cloudfab.macchina.utility.ControllaSelezione;
import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoAltezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConvertiParametroRelativoLarghezza;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMAGES;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_PULSANTI;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_IMG_TASTIERA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.PATH_PANELS_DATA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_ALTEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_LARGHEZZA_PANNELLO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_ALTEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.RISOLUZIONE_RIFERIMENTO_LARGHEZZA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_AREA_SCAMBIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_BUTTON;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_BUTTON_FRECCIA;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_BUTTON_INFO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_COLORE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_COLORE_BLUE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_COLORE_GREEN;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_COLORE_RED;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_X;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_CORD_Y;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_ALT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_DIMENSION_LARG;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_FONT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_SEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_IMAGE_UNSEL;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_LABEL_HELP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_LABEL_PLUS;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_LABEL_PROGRESS;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_LABEL_SIMPLE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_LABEL_TITLE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_OPACITY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_OPACITY_VALUE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.XML_TEXT_FIELD;
import java.util.ArrayList; 
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

@SuppressWarnings("serial")
public class MyStepPanel extends JPanel {
 
    //Variabili
    public JLabel elemLabelPlus[], elemLabelSimple[], elemLabelHelp[], elemLabelTitle[],
            elemLabelProg[], loadingImg, labelImageAux[], imageSfondo;
    public JButton elemBut[];  
    public Color elemColor[];
    public ControllaSelezione controllaSelezione;
    public Element root;
    public JButton butFreccia, butInfo;
    public JTextField txtField = new JTextField();
    public Tastiera tastiera;
    public ScelteEffettuate scelte = new ScelteEffettuate();
    public String language;
    private GestoreEventiMouseLista mEvent;
    private GestoreScrollLista sEvent;
    private ImageIcon[] imageSel, imageUnsel; 
    public ArrayList<MyStepPanel> pannelliCollegati = new ArrayList<>();
 

    //Configurazione di Base Pannello
    public void configuraPannello() {

        //Eliminazione Impostazioni di Default per il Layout Manager
        setLayout(null);

        //Impostazione Iniziale Visibilità Layer Messaggi Aiuto
        impostaVisibilitaAiuto(false);

        //Gestore Evento Scorrimento dalla lista Prodotti 
        sEvent = new GestoreScrollLista();

        //Gestore Eventi Mouse
        mEvent = new GestoreEventiMouseLista();

        //Dichiarazione Gestori Eventi Mouse
        dichiaraGestoriEventiMouse(elemLabelPlus, mEvent, sEvent);

        //Loading Immagini Pulsanti
        caricaImagePulsanti();

        //Impostazione Allineamento Label Progresso
        impostaAllineamentoHelp();
        

    }

    //Dichiarazione Posizionamento Testo Messaggi d'aiuto
    private void impostaAllineamentoHelp() {

        for (int i = 1; i < elemLabelHelp.length; i++) {

            elemLabelHelp[i].setVerticalAlignment(SwingConstants.TOP);

        }

    }

    //Cambio Panello Dietro
    public void cambioPanelDietro() {

        this.setVisible(false);
        if (pannelliCollegati.get(0) instanceof Pannello00_Principale) {
            ((Pannello00_Principale) pannelliCollegati.get(0)).initPanel();
        } else if (pannelliCollegati.get(0) instanceof Pannello13_Configurazione_Generale) {
            ((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).initPanel(((Pannello13_Configurazione_Generale) pannelliCollegati.get(0)).editingProcesso);
        }


    }

    //Definizione Lista Filtrata di Elementi
    public void definisciLista(String[] list) {

        String[] listaOri = new String[list.length];
        String[] lista = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            listaOri[i] = list[i];
            lista[i] = list[i];

            if (lista.length > 0) {
                definisciGestoreLista_listaFILTR(lista, listaOri);
            }
        }
    }

    //Definizione Lista di Elementi
    public void definisciListaAttributi(int[] list) {

        int[] chimicheValideOri = new int[list.length];
        int[] chimicheValide = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            chimicheValideOri[i] = list[i];
            chimicheValide[i] = list[i];
            definisciGestoreLista_listaATTR(chimicheValide, chimicheValideOri);
        }

    }

    //Dichiarazione Gestori Eventi Mouse
    public void dichiaraGestoriEventiMouse(JLabel[] inputLabs, GestoreEventiMouseLista mEvent, GestoreScrollLista sEvent) {

        for (int i = 0; i < inputLabs.length; i++) {
            inputLabs[i].addMouseMotionListener(sEvent);
            inputLabs[i].addMouseListener(mEvent);
        }

    }

    //Dichiarazione Scrolling List
    public void definisciGestoreLista(JLabel[] lab, int tScroll) {

        //Dichiarazione tipologia di Scrolling Orizzontale 0 - Verticale 1
        controllaSelezione.attributiDef = false;
        controllaSelezione.select = "";
        controllaSelezione.prValue = "";
        controllaSelezione.typeScroll = tScroll;
        controllaSelezione.pannelloCorrente = this;
        controllaSelezione.txtField = txtField;
        controllaSelezione.labels = lab;
        controllaSelezione.mouseEvent = mEvent;
        controllaSelezione.scrollEvent = sEvent;
    }

    //Dichiarazione Lista Filtrata
    public void definisciGestoreLista_listaFILTR(String[] array, String[] arrayOri) {
        controllaSelezione.lista_FILTR = array;
        controllaSelezione.lista_FILTR_ORIG = arrayOri;
    }

    //Dichiarazione Lista Attributi
    public void definisciGestoreLista_listaATTR(int[] array, int[] arrayOri) {

        controllaSelezione.lista_ATTR = array;
        controllaSelezione.lista_ATTR_ORIG = arrayOri;
        controllaSelezione.attributiDef = true;
    }

    //Dichiarazione Colori Visualizzati
    public void definisciColoriLista(Color colorSel, Color colUnsel, Color colSelcted) {
        controllaSelezione.colSelectable = colorSel;
        controllaSelezione.colUnSelectable = colUnsel;
        controllaSelezione.colSelected = colSelcted;
    }

    //Impostazione dimensione lista con funzioni di Scroll e Selection
    public void impostaDimLabelPlus(int i) {
        elemLabelPlus = new JLabel[i];
        for (int j = 0; j < i; j++) {
            inserisciElemento(0, j);
        }
    }

    //Impostazione dimensione lista semplice
    public void impostaDimLabelSimple(int i) {
        elemLabelSimple = new JLabel[i];
        for (int j = 0; j < i; j++) {
            inserisciElemento(1, j);
        }
    }

    //Impostazione dimensione lista semplice relativa ai messaggi di aiuto
    public void impostaDimLabelHelp(int i) {
        elemLabelHelp = new JLabel[i];
        for (int j = 0; j < i; j++) {
            inserisciElemento(2, j);
        }
    }

    //Impostazione dimensione lista di label relativa ai Titoli
    public void impostaDimLabelTitle(int i) {
        elemLabelTitle = new JLabel[i];
        for (int j = 0; j < i; j++) {
            inserisciElemento(3, j);
        }
    }

    //Impostazione dimensione lista di label relativa al Progresso
    public void impostaDimLabelProg(int i) {
        elemLabelProg = new JLabel[i];
        for (int j = 0; j < i; j++) {
            inserisciElemento(4, j);
        }
    }

    //Impostazione dimensione della lista di label relativa ai Pulsanti
    public void impostaDimLabelBut(int i) {
        elemBut = new JButton[i];
        for (int j = 0; j < i; j++) {
            inserisciElemento(5, j);
        }
    }
 

    //Impostazione Visibilità Messaggi Aiuto
    public void impostaVisibilitaAiuto(boolean vis) {

        if (elemLabelHelp.length > 0) {
            for (int i = 0; i < elemLabelHelp.length; i++) {
                elemLabelHelp[i].setVisible(vis);
            }
        }
    }

    
     //Impostazione file Xml con i dati del pannello 
    public void impostaXml() {

        String xmlFile = PATH_PANELS_DATA + this.getClass().getName().replace(this.getClass().getPackage().getName()+".","")+".XML";

        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(ClassLoader.getSystemResource(xmlFile));

            root = doc.getRootElement();

        } catch (JDOMException | IOException ex) {
        }


    }

    //Definizione Area di Scambio con Pannello Alternativo
    public void inserisciAreaScambio() {

        //Lettura chiave file xml
        Element elm = root.getChild(XML_AREA_SCAMBIO);

        int x = ConvertiParametroRelativoLarghezza(
                RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_X)));

        int y = ConvertiParametroRelativoAltezza(
                RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y)));

        int l = ConvertiParametroRelativoLarghezza(
                RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG)));

        int a = ConvertiParametroRelativoAltezza(
                RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT)));

        //Gestore Scambio Pannello - Pannello Alternativo
        addMouseListener(new GestoreScambioPanelMouseListener(x, y, l, a, this));
    }

    //Inserimento Nuovo Elemento sul Pannello
    private void inserisciElemento(int type, int id) {
 
        switch (type) {
            case 0: {

                ////////////////////////////////////////////////////
                /// LABEL CON FUNZIONI DI SCROLLING E SELECTION  ///
                ////////////////////////////////////////////////////

                elemLabelPlus[id] = new JLabel();

                //Lettura chiave file xml 
                Element elm = root.getChild(XML_LABEL_PLUS + id);

                //Posizionamento e dimenzionameno dei Label
                elemLabelPlus[id].setBounds(ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                        ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

                //Settaggio Font
                elemLabelPlus[id].setFont(FabCloudFont.setDimensione(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

                //Inizializzazione Label
                elemLabelPlus[id].setText("");

                //Impostazione tipo Cursore
                elemLabelPlus[id].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
 
                add(elemLabelPlus[id]);

                break;
            }
            case 1: {

                ///////////////////////
                /// LABEL SEMPLICI  ///
                ///////////////////////

                elemLabelSimple[id] = new JLabel();

                //Lettura chiave file xml
                Element elm = root.getChild(XML_LABEL_SIMPLE + id);

                //Posizionamento e dimenzionameno dei Label
                elemLabelSimple[id].setBounds(ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                        ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

                //Settaggio Font
                elemLabelSimple[id].setFont(FabCloudFont.setDimensione(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

                //Impostazione tipo Cursore
                elemLabelSimple[id].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                //Inizializzazione Label
                elemLabelSimple[id].setText("");

                //Impostazione Posizione
                elemLabelSimple[id].setHorizontalAlignment(SwingConstants.LEFT);
                elemLabelSimple[id].setVerticalAlignment(SwingConstants.TOP);

                add(elemLabelSimple[id]);

                break;
            }
            case 2: {

                ////////////////////
                /// LABEL AIUTO  ///
                ////////////////////

                elemLabelHelp[id] = new JLabel();

                //Lettura chiave file xml
                Element elm = root.getChild(XML_LABEL_HELP + id);

                //Posizionamento e dimenzionameno dei Label
                elemLabelHelp[id].setBounds(ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                        ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                        ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

                //Settaggio Font
                elemLabelHelp[id].setFont(FabCloudFont.setDimensione(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

                //Inizializzazione Label
                elemLabelHelp[id].setText("");

                //Impostazione tipo Cursore
                elemLabelHelp[id].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                add(elemLabelHelp[id]);


                break;
            }
            case 3: {

                /////////////////////
                /// LABEL TITOLI  ///
                /////////////////////

                elemLabelTitle[id] = new JLabel();

                //Lettura chiave file xml 
                Element elm = root.getChild(XML_LABEL_TITLE + id);

                //Posizionamento e dimenzionameno dei Label 
                elemLabelTitle[id].setBounds(ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                        ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

                //Settaggio Font
                elemLabelTitle[id].setFont(FabCloudFont.setDimensione(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_FONT))));


                //Impostazione tipo Cursore 
                elemLabelTitle[id].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                //Inizializzazione Label 
                elemLabelTitle[id].setText("");

                add(elemLabelTitle[id]);

                break;
            }
            case 4: {

                //////////////////////////
                /// LABEL AVANZAMENTO  ///
                //////////////////////////

                elemLabelProg[id] = new JLabel();

                //Lettura chiave file xml 
                Element elm = root.getChild(XML_LABEL_PROGRESS + id);

                //Posizionamento e dimenzionameno dei Label 
                elemLabelProg[id].setBounds(ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                        ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

                //Settaggio Font 
                elemLabelProg[id].setFont(FabCloudFont.setDimensione(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

                //Impostazione tipo Cursore 
                elemLabelProg[id].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                elemLabelProg[id].setHorizontalAlignment(SwingConstants.CENTER);
                elemLabelProg[id].setVerticalAlignment(SwingConstants.CENTER);

                elemLabelProg[id].setForeground(Color.WHITE);

                add(elemLabelProg[id]);

                break;
            }
            case 5: {

                /////////////////
                /// PULSANTI  ///
                /////////////////

                elemBut[id] = new JButton();

                //Lettura chiave file xml 
                Element elm = root.getChild(XML_BUTTON + id);

                //Posizionamento e dimenzionameno del Button 
                elemBut[id].setBounds(ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                        ConvertiParametroRelativoLarghezza(
                        RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                        ConvertiParametroRelativoAltezza(
                        RISOLUZIONE_RIFERIMENTO_ALTEZZA
                        / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

                //Impostazione Trasparenza 
                elemBut[id].setContentAreaFilled(false);
                elemBut[id].setBorderPainted(false);
                elemBut[id].setFocusPainted(false);
                elemBut[id].setMargin(new Insets(0, 0, 0, 0));

                //Impostazione del Cursore Quando il Mouse Passa sull'Elemento 
                elemBut[id].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                elemBut[id].setName(Integer.toString(id));

                elemBut[id].addActionListener(new ButtonAction(this));

                add(elemBut[id]);
                break;
            }
            
            default: {

                //Memorizzazione File di Log Inverter
                SessionLogger.logger.severe("Attenzione: Errore Inserimento Label");

                break;
            }
        }

    }

    //Dichiarazione Immagini Pulsanti
    public void caricaImagePulsanti() {

        //Loading Image Pulsanti
        imageSel = new ImageIcon[elemBut.length];
        imageUnsel = new ImageIcon[elemBut.length];

        for (int i = 0; i < elemBut.length; i++) {

            //Lettura chiave file xml
            Element elm = root.getChild(XML_BUTTON + (i));

            
            //Image Selezionabile 
            imageSel[i] = new ImageIcon(
                    ClassLoader.getSystemResource(
                    PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + "x"
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_PULSANTI
                    + elm.getAttributeValue(XML_IMAGE_UNSEL)));

            //Image Cursore sull'Immagine 
            imageUnsel[i] = new ImageIcon(
                    ClassLoader.getSystemResource(PATH_IMAGES
                    + RISOLUZIONE_LARGHEZZA_PANNELLO
                    + "x"
                    + RISOLUZIONE_ALTEZZA_PANNELLO
                    + PATH_IMG_PULSANTI
                    + elm.getAttributeValue(XML_IMAGE_SEL)));

        }

        impostaImageDefault();

    }

    //Impostazione Immagini di Base Pulsanti
    public void impostaImageDefault() {

        //Impostazione Immagine Normalmente Visualizzata 
        for (int i = 0; i < elemBut.length; i++) {

            elemBut[i].setDisabledIcon(imageSel[i]);
            elemBut[i].setIcon(imageSel[i]);
            elemBut[i].setPressedIcon(imageUnsel[i]);
            elemBut[i].updateUI();

        }
    }

    //Inserimento Controllo Selezione 
    public void inserisciControllaSelezione() {

        controllaSelezione = new ControllaSelezione();

    }

    //Modifica Selezionabilità di una Lista
    public void rendiListaUnselectable() {

        controllaSelezione.setUnselectable = true;

    }

    //Inserimento Tastiera sul Pannello
    public void inserisciTastiera() {
        tastiera = new Tastiera();
        tastiera.setPanel(this);
        tastiera.setTastiera();
        tastiera.assegnaTextField(txtField);
        tastiera.impostaVisibilitaTastiera(true);
    }

    //Dichiarazione Area di Testo per Inserimento Caratteri
    public void inserisciTextField() {

        //Lettura chiave file xml 
        Element elm = root.getChild(XML_TEXT_FIELD);

        //Posizionamento e dimenzionameno dei Label 
        txtField.setBounds(ConvertiParametroRelativoLarghezza(
                RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_X))),
                ConvertiParametroRelativoAltezza(
                RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y))),
                ConvertiParametroRelativoLarghezza(
                RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG))),
                ConvertiParametroRelativoAltezza(
                RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT))));

        //Settaggio Font 
        txtField.setFont(FabCloudFont.setDimensione(
                RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_FONT))));

        //Settaggio Trasparenze 
        txtField.setOpaque(false);

        //Settaggio Bordi 
        txtField.setBorder(null);

        //Impostazione Nome TextField
        txtField.setName("BASETXTFIELD");

        //Text Field
        add(txtField);

    }

    //Avvia il Thread di COntrolllo VIsibilità e Inizializza il Pannello
    public void startThreadControllo() {
        controllaSelezione.start();
    }

    //Rende Visibilie il Pannello Corrente
    public void setPanelVisibile() {

        this.setVisible(true);
        CloudFabFrame.getFRAME().setContentPane(this);
        this.updateUI();
        this.repaint();
        this.requestFocus();
    }

    //Definizione Area di Scambio con Pannello Alternativo
    public void impostaColori(int numColori) {

        elemColor = new Color[numColori];

        for (int i = 0; i < numColori; i++) {

            //Lettura chiave file xml
            Element elm = root.getChild(XML_COLORE + i);

            int r = Integer.parseInt(elm.getAttributeValue(XML_COLORE_RED));
            int g = Integer.parseInt(elm.getAttributeValue(XML_COLORE_GREEN));
            int b = Integer.parseInt(elm.getAttributeValue(XML_COLORE_BLUE));

            elemColor[i] = new Color(r, g, b);

        }
    }
    
    public float regolazioneOpacity(){
    
            //Lettura chiave file xml
            Element elm = root.getChild(XML_OPACITY);

            return Float.parseFloat(elm.getAttributeValue(XML_OPACITY_VALUE)); 
    
    }

    //Inizializzazione Colore Label Simple
    public void initColorLabelSimple(Color color) {

        //Settaggio Colore Label Simple Font
        for (JLabel elemLabelSimple1 : elemLabelSimple) {
            elemLabelSimple1.setForeground(color);
        }
    }
     

    //Inizializzazione Colore Label Help
    public void initColorLabelHelp(Color color) {

        //Settaggio Colore Label Simple Font
        for (JLabel elemLabelHelp1 : elemLabelHelp) {
            elemLabelHelp1.setForeground(color);
        }

    }

    //Inizializzazione Colore Label Title
    public void initColorLabelTitle(Color color) {

        //Settaggio Colore Label Simple Font
        for (int i = 0; i < elemLabelTitle.length; i++) {

            elemLabelTitle[i].setForeground(color);

        }

    }

    //Dichiarazione Button Freccia
    public void inserisciButtonFreccia() {

        butFreccia = new JButton();

        //Lettura chiave file xml
        Element elm = root.getChild(XML_BUTTON_FRECCIA);

        int x = ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_X)));

        int y = ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y)));

        int l = ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG)));

        int a = ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT)));


        //Impostazione Coordinate e Dimensioni
        butFreccia.setBounds(x, y, l, a);

        //Impostazione Immagini
        ImageIcon imgIco = new ImageIcon(ClassLoader.getSystemResource(
                PATH_IMAGES
                + RISOLUZIONE_LARGHEZZA_PANNELLO
                + "x"
                + RISOLUZIONE_ALTEZZA_PANNELLO
                + PATH_IMG_PULSANTI
                + elm.getAttributeValue(XML_IMAGE_UNSEL)));

        ImageIcon imgIcoSel = new ImageIcon(ClassLoader.getSystemResource(
                PATH_IMAGES
                + RISOLUZIONE_LARGHEZZA_PANNELLO
                + "x"
                + RISOLUZIONE_ALTEZZA_PANNELLO
                + PATH_IMG_PULSANTI
                + elm.getAttributeValue(XML_IMAGE_SEL)));

        //Impostazione Immagine Normalmente Visualizzata
        butFreccia.setDisabledIcon(imgIco);
        butFreccia.setIcon(imgIco);

        butFreccia.setPressedIcon(imgIcoSel);

        //Impostazione Trasparenza
        butFreccia.setContentAreaFilled(false);
        butFreccia.setBorderPainted(false);
        butFreccia.setFocusPainted(false);
        butFreccia.setMargin(new Insets(0, 0, 0, 0));

        //Impostazione del Cursore quando il Mouse passa sull'Elemento 
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        butFreccia.setName(XML_BUTTON_FRECCIA);

        //Gestore Evento Freccia
        butFreccia.addActionListener(new ButtonAction(this));


        //Iniziazlizzazione Visibilità 
        butFreccia.setVisible(false);

        //Aggiunta Componente al Pannello
        add(butFreccia);

    }

    //Dichirazione Button Info
    public void inserisciButtonInfo() {

        butInfo = new JButton();

        //Lettura chiave file xml
        Element elm = root.getChild(XML_BUTTON_INFO);

        int x = ConvertiParametroRelativoLarghezza(
                RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_X)));

        int y = ConvertiParametroRelativoAltezza(
                RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_CORD_Y)));

        int l = ConvertiParametroRelativoLarghezza(RISOLUZIONE_RIFERIMENTO_LARGHEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_LARG)));

        int a = ConvertiParametroRelativoAltezza(RISOLUZIONE_RIFERIMENTO_ALTEZZA
                / Double.parseDouble(elm.getAttributeValue(XML_DIMENSION_ALT)));


        //Impostazione Coordinate e Dimensioni
        butInfo.setBounds(x, y, l, a);

        //Impostazione Immagini
        ImageIcon imgIco = new ImageIcon(ClassLoader.getSystemResource(
                PATH_IMAGES
                + RISOLUZIONE_LARGHEZZA_PANNELLO
                + "x"
                + RISOLUZIONE_ALTEZZA_PANNELLO
                + PATH_IMG_TASTIERA
                + elm.getAttributeValue(XML_IMAGE_UNSEL)));

        ImageIcon imgIcoSel = new ImageIcon(ClassLoader.getSystemResource(
                PATH_IMAGES
                + RISOLUZIONE_LARGHEZZA_PANNELLO
                + "x"
                + RISOLUZIONE_ALTEZZA_PANNELLO
                + PATH_IMG_TASTIERA
                + elm.getAttributeValue(XML_IMAGE_SEL)));

        //Impostazione Immagini normalmente Visualizzata 
        butInfo.setDisabledIcon(imgIco);
        butInfo.setIcon(imgIco);

        butInfo.setPressedIcon(imgIcoSel);

        //Impostazione Trasparenza
        butInfo.setContentAreaFilled(false);
        butInfo.setBorderPainted(false);
        butInfo.setFocusPainted(false);
        butInfo.setMargin(new Insets(0, 0, 0, 0));

        //Impostazione del Cursore quando il Mouse passa sull'Elemento
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //Gestore Eventi Tasto Info
        butInfo.addActionListener(new ButtonInfoAction(elemLabelHelp, tastiera));

        //Aggiunta Componente al Pannello
        add(butInfo);


    }
    
     
}
