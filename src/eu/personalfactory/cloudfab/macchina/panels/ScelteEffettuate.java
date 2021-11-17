package eu.personalfactory.cloudfab.macchina.panels;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.ConteggiaNumeroParametriComponenti;
import java.util.ArrayList;

public class ScelteEffettuate {

    public String codiceProdotto, nomeProdotto, selezione, tipoFamiglia, descrizioneFamiglia, codiceChimica;
    public ArrayList<String> serie_colore, serie_additivo;
    public double pesoMiscele, fascia;
    public int pesoSacchetto, limiteColore, fattoreDivisore, numeroChimicheDisponibili,
            numeroMiscele, idCategoria, idProdotto, idColore, idAdditivo, numeroSacchetti, idCodice, //idMazzetta,
            numeroComponenti, id_ciclo;
    public boolean colorato, additivato, cambioContenitore, cambioConfezioneChimica, abilitaScaricaValvola, cambioBilancia, cambioCemento;
    public String cliente, codiceColoreSelezionato, codiceColoreModificato, nomeAdditivoSelezionato, nomeColoreSelezionato;
    public ArrayList<Double> correttivoVelocita;
    public ArrayList<Integer> idComponenti, idPresa, quantitaComponente, qStopMescolaComponente,
            secondaVelocitaComponente, voloComponente, qStopVibroComponente, dosaturaFineDirettaComponente;
    public ArrayList<String> codiceComponente, descriComponente, tolleranzaDifettoDosaggioComponente, tolleranzaEccessoDosaggioComponente,
            presaComponente, siglaComponente, curvaVelComponente, rifMagazzinoComponenti, metodo_pesa, fluidificazione, valore_residuo_flidificazione, 
            ordine_dosaggio, step_dosaggio, step_dosaggio_tempo_mix, step_dosaggio_vel_mix;
    public String[][] parametriComponente;
    public boolean disabilitaRibalta;
    public ArrayList<ArrayList<Integer>> compSostitutivi;

    //ORDINI
    public int ord_id_ordine_sm, ord_contatore, ord_num_pezzi, ord_id_ordine;
    ArrayList<String> dettagliOrdine;

    public void dimArrayDatiComponenti(int dim) {

        numeroComponenti = dim;
        parametriComponente = new String[dim][ConteggiaNumeroParametriComponenti()]; //Benefit.findValoreParametroGlobale(23))]; //24))];
        quantitaComponente = new ArrayList<>();
        idComponenti = new ArrayList<>();
        idPresa = new ArrayList<>();
        secondaVelocitaComponente = new ArrayList<>();
        correttivoVelocita = new ArrayList<>();
        voloComponente = new ArrayList<>();
        qStopMescolaComponente = new ArrayList<>();
        qStopVibroComponente = new ArrayList<>();
        dosaturaFineDirettaComponente = new ArrayList<>();
        codiceComponente = new ArrayList<>();
        descriComponente = new ArrayList<>();
        presaComponente = new ArrayList<>();
        siglaComponente = new ArrayList<>();
        curvaVelComponente = new ArrayList<>();
        rifMagazzinoComponenti = new ArrayList<>();
        tolleranzaDifettoDosaggioComponente = new ArrayList<>();
        tolleranzaEccessoDosaggioComponente = new ArrayList<>();

        metodo_pesa = new ArrayList<>();
        fluidificazione = new ArrayList<>();
        valore_residuo_flidificazione = new ArrayList<>();
        ordine_dosaggio = new ArrayList<>();
        step_dosaggio = new ArrayList<>();
        step_dosaggio_tempo_mix = new ArrayList<>();
        step_dosaggio_vel_mix = new ArrayList<>(); 
        serie_additivo = new ArrayList<>();
        serie_colore = new ArrayList<>();
    }

    public void inizializzaScelte() {

        dimArrayDatiComponenti(0);

        codiceProdotto = "";
        nomeProdotto = "";
        selezione = "";
        tipoFamiglia = "";
        descrizioneFamiglia = "";
        codiceChimica = "";
        cliente = "";
        codiceColoreSelezionato = "";
        codiceColoreModificato = "";
        nomeAdditivoSelezionato = "";
        nomeColoreSelezionato = "";

        colorato = false;
        additivato = false;
        cambioCemento = false;
        cambioContenitore = false;
        cambioConfezioneChimica = false;
        abilitaScaricaValvola = false;
        cambioBilancia = false;
        disabilitaRibalta = false;

        pesoMiscele = 0.0;
        fascia = 0.0;
        
        pesoSacchetto = 0;
        limiteColore = 0;
        fattoreDivisore = 0;
        numeroChimicheDisponibili = 0;
        numeroMiscele = 0;
        idCategoria = 0;
        idProdotto = 0;
        idColore = 0;
        idAdditivo = 0;
        numeroSacchetti = 0;
        idCodice = 0;
//        idMazzetta = 0;
        numeroComponenti = 0;
        id_ciclo = 0;

    }

}
