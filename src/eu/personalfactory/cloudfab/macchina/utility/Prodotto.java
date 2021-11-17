/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaSingoloValoreParametroRipristino;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametroProdottoPerIdProd;

/**
 *
 * @author Francesco Di Gaudio
 */
public class Prodotto {

    private int id;
    private String codice_prodotto;
    private String codice_chimica;
    private String nome;
    private String famiglia;
    private int id_categoria;
    private int mixing_time;
    private String mixing_speed;
    private final boolean vibro_attivo;
    private boolean aria_condotto_attiva;
    private boolean aria_interno_valvola_attiva;
    private boolean aria_pulisci_valvola_attiva;
    private String curva_dosaggio_bilanciaOMB;
    private int quantità_stop_coclea_bilanciaOMB;
    private int tolleranza_eccesso;
    private int tolleranza_difetto;
    //private final int correttivo_vel_insacco;
    //private final int correttivo_temp_insacco;

    //COSTRUTTORE
    public Prodotto(int id) {

        //Id Prodotto
        this.id = id;

        //Codice Prodotto
        codice_prodotto = TrovaSingoloValoreParametroRipristino(2);

        //Codice Prodotto
        codice_chimica = TrovaSingoloValoreParametroRipristino(98);

        //Nome Prodotto
        nome = TrovaSingoloValoreParametroRipristino(3);

        //Famiglia
        famiglia = TrovaSingoloValoreParametroRipristino(4);

        //Categoria
        id_categoria = Integer.parseInt(TrovaSingoloValoreParametroRipristino(5));

        /// ArrayList<String> parametri = Benefit.findParametriProdottoById(id);
        //tempo di Miscelazione
        mixing_time = Integer.parseInt(TrovaValoreParametroProdottoPerIdProd(id, 1)); //Integer.parseInt(parametri.get(0));

        //Velocità di miscelazione
        mixing_speed = TrovaValoreParametroProdottoPerIdProd(id, 2); //parametri.get(1);

////        //Abilitazione aria condotto valvola 
////        correttivo_vel_insacco = Integer.parseInt(parametri.get(3));
////
////        //Abilitazione aria interno valvola
////        correttivo_temp_insacco= Integer.parseInt(parametri.get(34));
        //Vibro Valvola Attivo
        if (Boolean.parseBoolean(ParametriSingolaMacchina.parametri.get(324))) {

            ////////////////////////////////////////
            // GESTIONE VIBRO VALVOLA ABILITATA  ///
            ////////////////////////////////////////
            vibro_attivo = !Boolean.parseBoolean(TrovaValoreParametroProdottoPerIdProd(id, 5));  //!Boolean.parseBoolean(parametri.get(4));
        } else {

            ////////////////////////////////////////
            // GESTIONE VIBRO VALVOLA DISABILITA  ///
            /////////////////////////////////////////
            vibro_attivo = true;
        }

        
        //Abilitazione aria pulisci valvola
        aria_pulisci_valvola_attiva = Boolean.parseBoolean(TrovaValoreParametroProdottoPerIdProd(id, 6)); //Boolean.parseBoolean(parametri.get(5));

        //Curva di dosaggio bilancia sacchia a valvola aperta
        curva_dosaggio_bilanciaOMB = TrovaValoreParametroProdottoPerIdProd(id, 7); //parametri.get(6);

        //Volo bilancia sacchia a valvola aperta
        quantità_stop_coclea_bilanciaOMB = Integer.parseInt(TrovaValoreParametroProdottoPerIdProd(id, 8)); //Integer.parseInt(parametri.get(7));

        //Tolleranza in eccesso Pesatura
        tolleranza_eccesso = Integer.parseInt(TrovaValoreParametroProdottoPerIdProd(id, 9)); //Integer.parseInt(parametri.get(8));

        //Tolleranza in difetto Pesatura
        tolleranza_difetto = Integer.parseInt(TrovaValoreParametroProdottoPerIdProd(id, 10)); //Integer.parseInt(parametri.get(9));

        //Abilitazione aria condotto valvola 
        aria_condotto_attiva = Boolean.parseBoolean(TrovaValoreParametroProdottoPerIdProd(id, 11)); //Boolean.parseBoolean(parametri.get(10));

        //Abilitazione aria interno valvola
        aria_interno_valvola_attiva = Boolean.parseBoolean(TrovaValoreParametroProdottoPerIdProd(id, 12)); //Boolean.parseBoolean(parametri.get(11));

    }

    ///////////////////////////
    // GETTERS AND SETTERS  ///
    ///////////////////////////
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMixingTime() {
        return mixing_time;
    }

    public void setMixingTime(int mixing_time) {
        this.mixing_time = mixing_time;
    }

    public String getMixingSpeed() {
        return mixing_speed;
    }

    public void setMixingSpeed(String mixing_speed) {
        this.mixing_speed = mixing_speed;
    }

    public boolean isVibroAttivo() {
        return vibro_attivo;
    }

////    public void setVibroAttivo(boolean vibro_attivo) {
////        this.vibro_attivo = vibro_attivo;
////    }
    public boolean isAriaCondottoAttiva() {
        return aria_condotto_attiva;
    }

    public void setAriaCondottoAttiva(boolean aria_condatto_attiva) {
        this.aria_condotto_attiva = aria_condatto_attiva;
    }

    public boolean isAriaInternoValvolaAttiva() {
        return aria_interno_valvola_attiva;
    }

    public void setAriaInternoValvolaAttiva(boolean aria_interno_valvola_attiva) {
        this.aria_interno_valvola_attiva = aria_interno_valvola_attiva;
    }

    public boolean isAriaPulisciValvolaAttiva() {
        return aria_pulisci_valvola_attiva;
    }

    public void setAriaPulisciValvolaAttiva(boolean aria_pulisci_valvola_attiva) {
        this.aria_pulisci_valvola_attiva = aria_pulisci_valvola_attiva;
    }

    public String getCurvaDosaggioBilanciaOMB() {
        return curva_dosaggio_bilanciaOMB;
    }

    public void setCurvaDosaggioBilanciaOMB(String curva_dosaggio_bilanciaOMB) {
        this.curva_dosaggio_bilanciaOMB = curva_dosaggio_bilanciaOMB;
    }

    public int getQuantitàStopCocleaBilanciaOMB() {
        return quantità_stop_coclea_bilanciaOMB;
    }

    public void setQuantitàStopCocleaBilanciaOMB(int quantità_stop_coclea_bilanciaOMB) {
        this.quantità_stop_coclea_bilanciaOMB = quantità_stop_coclea_bilanciaOMB;
    }

////    public String getCodice() {
////        return codice;
////    }
////    public void setCodice(String codice) {
////        this.codice = codice;
////    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFamiglia() {
        return famiglia;
    }

    public void setFamiglia(String famiglia) {
        this.famiglia = famiglia;
    }

    public int getIdCategoria() {
        return id_categoria;
    }

    public void setIdCategoria(int categoria) {
        this.id_categoria = categoria;
    }

    public int getTolleranzaEccesso() {
        return tolleranza_eccesso;
    }

    public void setTolleranzaEccesso(int tolleranza_eccesso) {
        this.tolleranza_eccesso = tolleranza_eccesso;
    }

    public int getTolleranzaDifetto() {
        return tolleranza_difetto;
    }

    public void setTolleranzaDifetto(int tolleranza_difetto) {
        this.tolleranza_difetto = tolleranza_difetto;
    }

    public String getCodiceProdotto() {
        return codice_prodotto;
    }

    public void setCodice_prodotto(String codice_prodotto) {
        this.codice_prodotto = codice_prodotto;
    }

    public String getCodiceChimica() {
        return codice_chimica;
    }

    public void setCodice_chimica(String codice_chimica) {
        this.codice_chimica = codice_chimica;
    }

}
