
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import eu.personalfactory.cloudfab.macchina.entity.ValoreRipristinoOri;
import eu.personalfactory.cloudfab.macchina.loggers.SessionLogger;
import eu.personalfactory.cloudfab.macchina.panels.Pannello10_ScelteEffettuate;
import eu.personalfactory.cloudfab.macchina.panels.ScelteEffettuate;
import eu.personalfactory.cloudfab.macchina.processo.DettagliSessione;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaMaxIdCiclo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ENTITY_MANAGER_FACTORY;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.MAGAZZINO_COMPONENTI_COMP_NON_DEFINITO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.swing.JPanel;

/**
 *
 * @author francescodigaudio
 *
 * Registra le Informazioni nella Tabella Valori di Ripristino
 *
 *
 */
public class AggiornaValoriRipristino extends Thread {

    //VARIABILI
    private final ValoreRipristinoOri valRipristinoOri = new ValoreRipristinoOri();
    private int cursore;
    private final ScelteEffettuate scelte;
    private final JPanel panel;
    private int idProcesso;
    private EntityManager em;

    //COSTRUTTORE
    public AggiornaValoriRipristino(ScelteEffettuate sc, JPanel p) {
        scelte = sc;
        panel = p;

    }

    //Aggiorna i Record dell'Entità per la Registrazione su Database
    public void aggiornaRecord(int id_tab, int id_par, String value) {

        try {
            valRipristinoOri.setIdValoreRipristino(id_tab);
            valRipristinoOri.setIdParRipristino(id_par);
            valRipristinoOri.setValoreVariabile(value);
            valRipristinoOri.setIdProCorso(idProcesso);
            valRipristinoOri.setAbilitato(true);
            valRipristinoOri.setDtRegistrato(new Date());
            valRipristinoOri.setDtAbilitato(new Date());

            em.merge(valRipristinoOri);

        } catch (Exception ex) {
            
              //Memorizzazione Log Processo
            SessionLogger.logger.log(Level.SEVERE, "aggiornaValoreRipristino - Fallito - Errore e ={0}", ex);
 
        }
    }

    //Generazione Formula Prodotto in Funzione delle Informazioni Lette da Database
    private String creaFormulaProdotto() {

        String formulaProdotto = "";
        for (int i = 0; i < scelte.numeroComponenti; i++) {

            scelte.siglaComponente.add(scelte.presaComponente.get(i) + scelte.quantitaComponente.get(i));
            formulaProdotto += scelte.siglaComponente.get(i);
            if (i < scelte.numeroComponenti - 1) {
                formulaProdotto += ParametriGlobali.parametri.get(45);
            }

        }
        return formulaProdotto;

    }

    @Override
    public void run() {

        idProcesso = Integer.parseInt(ParametriSingolaMacchina.parametri.get(15)); //Benefit.findValoreParametroSingolaMacchina(15)));

        em = ENTITY_MANAGER_FACTORY.createEntityManager();

        em.getTransaction().begin();

        //idProdotto - Id del Prodotto Selezionato dall'Operatore
        cursore = 1;
        aggiornaRecord(cursore, 1, Integer.toString(scelte.idProdotto));

        //codProdotto - Codice del Prodotto Selezionato dall'Operatore
        cursore++;
        aggiornaRecord(cursore, 2, scelte.codiceProdotto);

        //nomeProdotto - Nome del Prodotto Selezionato dall'Operatore
        cursore++;
        aggiornaRecord(cursore, 3, scelte.nomeProdotto);

        //descriFamiglia - Descrizione della Famiglia del Prodotto Selezionato
        cursore++;
        aggiornaRecord(cursore, 4, scelte.descrizioneFamiglia);

        int categoria = scelte.idCategoria;
         
        if (!(TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(scelte.idProdotto, 3)).equals(ParametriGlobali.parametri.get(160))) {

            categoria = Integer.parseInt(TrovaValoreParProdottoMacchinaPerIdParametroPerIdProdotto(scelte.idProdotto, 3));
        }

        //idCategoria - Categoria del Prodotto Selezionato dall'Operatore
        cursore++;
        aggiornaRecord(cursore, 5, Integer.toString(categoria));

        //cliente - Cliente Eventualmente Associato alla Produzione
        cursore++;
        aggiornaRecord(cursore, 6, scelte.cliente);

        //pesoMiscela - Peso di Ogni Miscela di Prodotto
        cursore++;
        aggiornaRecord(cursore, 7, Double.toString(scelte.pesoMiscele));

        //numMiscele - Numero di Miscele Selezionate dall'Operatore
        cursore++;
        aggiornaRecord(cursore, 8, Integer.toString(scelte.numeroMiscele));

        //numSacchetti - Numero di Sacchetti per Miscela
        cursore++;
        aggiornaRecord(cursore, 9, Integer.toString(scelte.numeroSacchetti));

        //pesoSacchetto - Peso di Ogni Sacchetto di Prodotto
        cursore++;
        aggiornaRecord(cursore, 10, Integer.toString(scelte.pesoSacchetto));

        //numTotSacchetti - Numero Totale di Sacchetti da Realizzare
        cursore++;
        aggiornaRecord(cursore, 11, Integer.toString(scelte.numeroMiscele * scelte.numeroSacchetti));

        //cambioCemento - Tipologia di Cemento Bianco o Grigio
        cursore++;
        aggiornaRecord(cursore, 12, Boolean.toString(scelte.cambioCemento));

        //cambioContenitore - Tipologia di Contenitore Secchio o Sacchetto
        cursore++;
        aggiornaRecord(cursore, 13, Boolean.toString(scelte.cambioContenitore));

        //cambioConfChimica - Tipologia di Confezione Chimica Sacchetti o Sfusa
        cursore++;
        aggiornaRecord(cursore, 14, Boolean.toString(scelte.cambioConfezioneChimica));

        //numeroComponenti - Numero di Componenti Presenti nella Formula del Prodotto
        cursore++;
        aggiornaRecord(cursore, 15, Integer.toString(scelte.numeroComponenti));

        //idComponente - Id del Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 16, Integer.toString(scelte.idComponenti.get(i)));
        }

        //codiceComponente - Codice del Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 17, scelte.codiceComponente.get(i));
        }

        //descriComponente - Nome del Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 18, scelte.descriComponente.get(i));
        }

        //quantitaComponente - Quantità di Componente Presente nella Formula Teorica del Prodotto
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 19, Double.toString(scelte.quantitaComponente.get(i)));
        }

        //dosaturaFineDiretta - Valore in grammi al di Sotto del quale il Componente viene Pesato Direttamente a Bassa Vel
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 20, Double.toString(scelte.dosaturaFineDirettaComponente.get(i)));
        }

        //presaComponente - Presa Associata al Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 21, scelte.presaComponente.get(i));
        }

        //correttivoVelocita - Correttivo Velocita del Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 22, Double.toString(scelte.correttivoVelocita.get(i)));
        }

        //voloComponente - Volo del Componente quando viene Fermato il Motore della Coclea
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 23, Integer.toString(scelte.voloComponente.get(i)));
        }

        //intInverter - Quantità di Componente da Dosare a Vel Ridotta
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 24, Integer.toString(scelte.secondaVelocitaComponente.get(i)));
        }

        //pesaComponenteEseguita - Pesa Componente Eseguita
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 25, Boolean.toString(false));
        }

        //pesoEffettivoComponente - Peso Effettivo dell'Ultimo Componente Pesato
        cursore++;
        aggiornaRecord(cursore, 26, Integer.toString(0));

        //formulaTeoricaProdotto - Formula Teorica del Prodotto Selezionato
        cursore++;
        aggiornaRecord(cursore, 27, creaFormulaProdotto());

        //formulaEffettivaMiscelaInPesa - Formula Effettiva della Miscela Pesata
        cursore++;
        aggiornaRecord(cursore, 28, "");

        //formulaEffettivaMiscelaInLavorazione - Formula Effettiva della Miscela in Fase di Produzione
        cursore++;
        aggiornaRecord(cursore, 29, "");

        //colorato - Indica se il Prodotto è Colorato
        cursore++;
        aggiornaRecord(cursore, 30, Boolean.toString(scelte.colorato));

      //  if (scelte.colorato) {
        //id Colore Associato alla produzione 
        cursore++;
        aggiornaRecord(cursore, 32, Integer.toString(scelte.idColore));
       // } 

        //quantitaChimica - Quantita di Chimica per Ogni Miscela
        cursore++;
        aggiornaRecord(cursore, 43, Integer.toString(0));

        //contenitoriChimicaPesati - Numero di Contenitori di Chimica Sfusa Pesati
        cursore++;
        aggiornaRecord(cursore, 44, Integer.toString(0));

        //sacchettoBloccato - Indica che il Sacchetto è stato Bloccato
        cursore++;
        aggiornaRecord(cursore, 45, Boolean.toString(false));

        //codSacchettoInserito - Indica che il Codice del Sacchetto è Stato Inserito
        cursore++;
        aggiornaRecord(cursore, 46, Boolean.toString(false));

        //codSacchetto - Codice del Sacchetto Letto dall'Operatore
        cursore++;
        aggiornaRecord(cursore, 47, "");

        //tempoRiapertura - Tempo di Riapertura Valvola di Scarico
        cursore++;
        aggiornaRecord(cursore, 48, Integer.toString(0));

        //sacchettoInPesa - Sacchetto Attualmente in Pesa
        cursore++;
        aggiornaRecord(cursore, 49, Integer.toString(0));

        //pesoSacchetto - Peso da Ottenere per il Sacchetto
        cursore++;
        for (int i = 0; i < scelte.numeroSacchetti; i++) {
            aggiornaRecord(cursore, 50, Integer.toString(scelte.pesoSacchetto));
        }

        //pesaSacCompletata - Pesa del Sacchetto Completata
        for (int i = 0; i < scelte.numeroSacchetti; i++) {
            cursore++;
            aggiornaRecord(cursore, 51, Boolean.toString(false));
        }

        //taraPesaComponenti - Tara della Bilancia dei Componenti
        cursore++;
        aggiornaRecord(cursore, 52, Integer.toString(0));

        //taraPesaSacchetti - Tara della Bilancia dei Sacchi
        cursore++;
        aggiornaRecord(cursore, 53, Integer.toString(0));

        //passoCompletato - Ultimo Passo Completato
        cursore++;
        aggiornaRecord(cursore, 54, Integer.toString(0));

        //pesaComponentiCompletata - Indica che la Pesa dei Componenti è stata Completata
        cursore++;
        aggiornaRecord(cursore, 55, Boolean.toString(false));

        //materialePesa - Indica la Presenza di Materiale non Scaricato sulla Pesa Componenti
        cursore++;
        aggiornaRecord(cursore, 56, Boolean.toString(false));

        //codChimicaInserito - Indica che il Codice della Chimica è stato Inserito
        cursore++;
        aggiornaRecord(cursore, 57, Boolean.toString(false));

        //codiceChimica - Codice della Chimica Letto dall'Operatore
        cursore++;
        aggiornaRecord(cursore, 58, "");

        //miscelazioneCompletata - Indica che la Fase di Miscelazione è Stata Completata
        cursore++;
        aggiornaRecord(cursore, 59, Boolean.toString(false));

        //pesaSuccessivaCompletata - Indica che la Pesa dei Componenti della Miscela Successiva è Stata Completata
        cursore++;
        aggiornaRecord(cursore, 60, Boolean.toString(false));

        //ultimaMiscela - Indica che Quella in Corso è l'Ultima Miscela e non è Necessario Procede con Ulteriori Pesate 
        cursore++;
        aggiornaRecord(cursore, 61, Boolean.toString(false));

        //miscelePesate - Numero di MIscele Pesate
        cursore++;
        aggiornaRecord(cursore, 62, Integer.toString(0));

        //misceleCompletate - Numero di MIscele Completate
        cursore++;
        aggiornaRecord(cursore, 63, Integer.toString(0));

        //parPesaCompAggiornati - Indica che i Parametri Relativi alla Pesa Componenti sono Stati Aggiornati
        cursore++;
        aggiornaRecord(cursore, 64, Boolean.toString(false));

        //parPesaSacAggiornati - Indica che i Parametri Relativi alla Pesa Sacchetti sono Stati Aggiornati 
        cursore++;
        aggiornaRecord(cursore, 65, Boolean.toString(false));

        //memoDatabase - Indica che l'Aggiornamento dei Parametri su Database è Stato Eseguito Correttamente
        cursore++;
        aggiornaRecord(cursore, 66, Boolean.toString(false));

        //insaccoCompletato - Indica che la Fase di Insacco della Miscela è stata Completata
        cursore++;
        aggiornaRecord(cursore, 67, Boolean.toString(false));

        //numSacPesati - Numero di Sacchetti Pesati
        cursore++;
        aggiornaRecord(cursore, 68, "0");

        //numMisceleChimicaSfusa - Numero di Miscele Realizzabili con un Lotto di Chimica Sfusa
        cursore++;
        aggiornaRecord(cursore, 69, "0");

        //cruvaMicro - CurvaMicrodosatore
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 70, scelte.curvaVelComponente.get(i));
        }

        //memorizzaValore Iniziale Micro
        for (int i = 0; i < Integer.parseInt(ParametriSingolaMacchina.parametri.get(239)); i++) {
            cursore++;
            aggiornaRecord(cursore, 71, "0");
        }

        //Memorizzazione Valore Totale Peso Sacchetti Pesati 
        cursore++;
        aggiornaRecord(cursore, 72, "0");

        //Memorizzazione Valore Totale Peso Componenti Pesati 
        cursore++;
        aggiornaRecord(cursore, 73, "0");

        //Memorizzazione Nome Operatore
        cursore++;
        aggiornaRecord(cursore, 74, DettagliSessione.getCodiceFigura());

        String codComponenti = "";
        //Codice Componenti
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            if (!scelte.idComponenti.get(i).equals(
                    Integer.parseInt(ParametriSingolaMacchina.parametri.get(301)))) {

                if (!scelte.rifMagazzinoComponenti.get(i).equals("")) {

                    //// MODIFICA GIUGNO 2016  
                    ////////////////////////////////////////////
                    String cod_componente = "";

                    for (int j = 0; j < scelte.rifMagazzinoComponenti.get(i).length(); j++) {
                        if (scelte.rifMagazzinoComponenti.get(i).charAt(j) != ParametriGlobali.parametri.get(106).charAt(0)) {
                            cod_componente += scelte.rifMagazzinoComponenti.get(i).charAt(j);
                        } else {
                            break;
                        }
                    }

                    codComponenti += scelte.idComponenti.get(i)
                            + ParametriGlobali.parametri.get(103)
                            + cod_componente
                            + ParametriGlobali.parametri.get(104);
                } else {

                    codComponenti += scelte.idComponenti.get(i)
                            + ParametriGlobali.parametri.get(103)
                            + MAGAZZINO_COMPONENTI_COMP_NON_DEFINITO
                            + ParametriGlobali.parametri.get(104);
                }
            }
        }

        //Memorizzazione Codice Componenti
        cursore++;
        aggiornaRecord(cursore, 75, codComponenti.substring(0, codComponenti.length() - 1));

        //Memorizzazione Disabilita Ribalta Sacco
        cursore++;
        aggiornaRecord(cursore, 76, Boolean.toString(scelte.disabilitaRibalta));

        //Memorizzazione Abilita Scarica Valvola
        cursore++;
        aggiornaRecord(cursore, 77, Boolean.toString(scelte.abilitaScaricaValvola));

        String cod_colore = "";

        cursore++;
        aggiornaRecord(cursore, 78, cod_colore);

        //Id ciclo - Processo Ori
        String idCiclo = Integer.toString(Integer.parseInt(ParametriSingolaMacchina.parametri.get(365)) + 1);
        String maxCiclo = TrovaMaxIdCiclo();

        if (!maxCiclo.equals("")) {

            idCiclo = maxCiclo;
        }

        cursore++;
        aggiornaRecord(cursore, 79, idCiclo);

        //Memorizzazione Formula Teorica
        cursore++;
        aggiornaRecord(cursore, 80, "");

        //qStopMescola - quantita residua di componente superato il quale viene fermato il motore del mescolatore del microdosatore
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 81, Integer.toString(scelte.qStopMescolaComponente.get(i)));
        }

        //Memorizzazione Cambio Bilancia
        cursore++;
        aggiornaRecord(cursore, 82, Boolean.toString(scelte.cambioBilancia));

        //Id ciclo - Ciclo Ori
        cursore++;
        aggiornaRecord(cursore, 83, Integer.toString(scelte.id_ciclo));

        //Tolleranza in difetto sul peso del componente durante il dosaggio
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 84, scelte.tolleranzaDifettoDosaggioComponente.get(i));
        }

        //Tolleranza in eccesso sul peso del componente durante il dosaggio
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 85, scelte.tolleranzaEccessoDosaggioComponente.get(i));
        }

        //Metodo Pesa Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 86, scelte.metodo_pesa.get(i));
        }

        //Fluidificazione Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            boolean value = false;
            value = scelte.fluidificazione.get(i).equals("1");
            aggiornaRecord(cursore, 87, Boolean.toString(value));

        }
        //Valore Residuo Fluidificazione Componente
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 88, scelte.valore_residuo_flidificazione.get(i));
        }

        //Ordine Dosaggio
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 89, scelte.ordine_dosaggio.get(i));
        }

        //Step Dosaggio
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 90, scelte.step_dosaggio.get(i));
        }

        //Id ciclo Pesa Successivo - Ciclo Ori
        cursore++;
        aggiornaRecord(cursore, 91, Integer.toString(scelte.id_ciclo));

        //Id ordine in esecuzione
        cursore++;
        aggiornaRecord(cursore, 92, Integer.toString(scelte.ord_id_ordine));

        //Instante Inizio Insacco
        cursore++;
        aggiornaRecord(cursore, 93, new SimpleDateFormat(ISTANTE_INIZIO_CONFEZIONAMENTO_DATE_FORMAT).format(new Date()));

        //Step Dosaggio Tempi di Miscelazione
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 94, scelte.step_dosaggio_tempo_mix.get(i));
        }

        //Step Dosaggio Velocita di Miscelazione
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 95, scelte.step_dosaggio_vel_mix.get(i));
        }

        //Id tabella ordine in esecuzione
        cursore++;
        aggiornaRecord(cursore, 96, Integer.toString(scelte.ord_id_ordine_sm));

        //Quantita stop vibro
        for (int i = 0; i < scelte.numeroComponenti; i++) {
            cursore++;
            aggiornaRecord(cursore, 97, Integer.toString(scelte.qStopVibroComponente.get(i)));
        }

        //Id tabella ordine in esecuzione
        cursore++;
        aggiornaRecord(cursore, 98, scelte.codiceChimica);

       // if (scelte.colorato) {
            //id Colore Associato alla produzione 
            cursore++;
            aggiornaRecord(cursore, 99, Integer.toString(scelte.idAdditivo));
       // }

        //Id Movimento Magazzino Componente in Pesa
        cursore++;
        aggiornaRecord(cursore, 100, Integer.toString(0));

        //Codice Chimica Miscela Pesa Manuale
        cursore++;
        aggiornaRecord(cursore, 101, "");

        //Pesatura manuale chmiica sfusa eseguita
        cursore++;
        aggiornaRecord(cursore, 102, "false");

        //Pesatura manuale chmiica sfusa eseguita
        cursore++;
        aggiornaRecord(cursore, 103, "0");

        em.getTransaction().commit();

        ((Pannello10_ScelteEffettuate) panel).gestoreScambioPannello(1);
    }
}
