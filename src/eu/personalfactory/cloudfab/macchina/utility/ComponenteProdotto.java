/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.utility;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_ELEMENTI_STRINGA_VEL_IND_COMP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_FINE_STRINGA_VEL_IND_COMP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_STRINGA_VEL_IND_COMP;
import java.util.ArrayList;

/**
 *
 * @author Francesco Di Gaudio
 */
public class ComponenteProdotto {

    //Lettura Informazioni dalla Tabella di Ripristino
    private String nome;
    private int id;
    private String presa;
    private int quantità;
    private String correttivo_velocità;
    private int volo;
    private int tolleranza_difetto;
    private int tolleranza_eccesso;
    private int quantità_stop_mescolatore; 
    private int quantità_stop_vibro;
    private int quantità_seconda_velocità_inverter;
    private String curva_dosatura;
    private String metodo_pesa;

    private boolean fluidificazione;
    private int valore_residuo_fluidificazione;
    private int ordine_dosaggio;
    private int step_dosaggio;
    private ArrayList<String> velocità;

    private ArrayList<Integer> limiti_cambio_velocità;

    private String codice_movimento_magazzino;

    ///////////////////////////
    // GETTERS AND SETTERS  ///
    ///////////////////////////
    public ArrayList<String> getVelocità() {
        return velocità;
    }

    public void setVelocità(ArrayList<String> velocità) {
        this.velocità = velocità;
    }

    public ArrayList<Integer> getLimitiCambioVelocità() {
        return limiti_cambio_velocità;
    }

    public void setLimitiCambioVelocità(ArrayList<Integer> limiti_cambio_velocità) {
        this.limiti_cambio_velocità = limiti_cambio_velocità;
    }

    public String getNome() {
        return nome;
    }

    public int getValoreResiduoFluidificazione() {
        return valore_residuo_fluidificazione;
    }

    public void setValoreResiduoFluidificazione(int valore_residuo_flidificazione) {
        this.valore_residuo_fluidificazione = valore_residuo_flidificazione;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPresa() {
        return presa;
    }

    public void setPresa(String presa) {
        this.presa = presa;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
    }

    public String getCorrettivoVelocità() {
        return correttivo_velocità;
    }

    public void setCorrettivoVelocità(String correttivo_velocità) {
        this.correttivo_velocità = correttivo_velocità;
    }

    public int getVolo() {
        return volo;
    }

    public void setVolo(int volo) {

        //Correzione Volo con il parametro singola macchina 112 : Coefficiente tolleranza Carico
        this.volo = (int) (volo * (Double.parseDouble(ParametriSingolaMacchina.parametri.get(112)) / 100));

    }

    public int getTolleranzaDifetto() {
        return tolleranza_difetto;
    }

    public void setTolleranzaDifetto(int tolleranza_difetto) {
        this.tolleranza_difetto = tolleranza_difetto;
    }

    public int getTolleranzaEccesso() {
        return tolleranza_eccesso;
    }

    public void setTolleranzaEccesso(int tolleranza_eccesso) {
        this.tolleranza_eccesso = tolleranza_eccesso;
    }

    public int getQuantitàStopMescolatore() {
        return quantità_stop_mescolatore;
    }

    public void setQuantitàStopMescolatore(int quantità_stop_mescolatore) {
        this.quantità_stop_mescolatore = quantità_stop_mescolatore;
    }

    public int getQuantitàSecondaVelocitàInverter() {
        return quantità_seconda_velocità_inverter;
    }

    public void setQuantitàSecondaVelocitàInverter(int quantità_seconda_vel_inv) {
        this.quantità_seconda_velocità_inverter = quantità_seconda_vel_inv;
    }

    public String getCurvaDosatura() {
        return curva_dosatura;
    }

    public void setCurvaDosatura(String curva_dosatura) {

        this.curva_dosatura = curva_dosatura;

        ///////////////////////////////////////////////////////////////////////////////////
        //////////// INTERPRETAZIONE NUOVA STRINGA VELOCITA  COMPONENTE  //////////////////
        ///////////////////////////////////////////////////////////////////////////////////
        //Inizializzazione
        velocità = new ArrayList<>();
        limiti_cambio_velocità = new ArrayList<>();

        int index_charSep = this.curva_dosatura.indexOf(CARATTERE_STRINGA_VEL_IND_COMP);
        String strVelocità = this.curva_dosatura.substring(0, index_charSep);
        String strLimitiCambioVelocità = this.curva_dosatura.substring(index_charSep + 1, this.curva_dosatura.length());

        String temp = "";

        for (int i = 0; i < strVelocità.length(); i++) {

            if (strVelocità.charAt(i) == CARATTERE_ELEMENTI_STRINGA_VEL_IND_COMP
                    || strVelocità.charAt(i) == CARATTERE_FINE_STRINGA_VEL_IND_COMP) {

                int vel = (int) (Integer.parseInt(temp) * ((200 - Double.parseDouble(ParametriSingolaMacchina.parametri.get(112))) / 100));
                String vel_mod = Integer.toString(vel);
                while (vel_mod.length() < 4) {
                    vel_mod = "0" + vel_mod;

                }

                vel_mod = vel_mod.substring(0, vel_mod.length() - 2) + "00";

                velocità.add(vel_mod);

                temp = "";
            } else {
                temp += strVelocità.charAt(i);

            }
        }

        temp = "";
        for (int i = 0; i < strLimitiCambioVelocità.length(); i++) {

            if (strLimitiCambioVelocità.charAt(i) == CARATTERE_ELEMENTI_STRINGA_VEL_IND_COMP
                    || strLimitiCambioVelocità.charAt(i) == CARATTERE_FINE_STRINGA_VEL_IND_COMP) {

                limiti_cambio_velocità.add(Integer.parseInt(temp));
                temp = "";
            } else {
                temp += strLimitiCambioVelocità.charAt(i);

            }
        }

    }

    public String getMetodoPesa() {
        return metodo_pesa;
    }

    public void setMetodoPesa(String metodo_pesa) {
        this.metodo_pesa = metodo_pesa;
    }

    public boolean isFluidificazione() {
        return fluidificazione;
    }

    public void setFluidificazione(boolean fluidificazione) {
        this.fluidificazione = fluidificazione;
    }

    public int getOrdineDosaggio() {
        return ordine_dosaggio;
    }

    public void setOrdine_dosaggio(int ordine_dosaggio) {
        this.ordine_dosaggio = ordine_dosaggio;
    }

    public int getStepDosaggio() {
        return step_dosaggio;
    }

    public void setStepDosaggio(int step_dosaggio) {
        this.step_dosaggio = step_dosaggio;
    }

    public String getCodiceMovimento_Magazzino() {
        return codice_movimento_magazzino;
    }

    public void setCodiceMovimentoMagazzino(String codice_movimento_magazzino) {
        this.codice_movimento_magazzino = codice_movimento_magazzino;
    }
    
    public int getQuantitàStopVibro() {
        return quantità_stop_vibro;
    }

    public void setQuantitàStopVibro(int quantità_stop_vibro) {
        this.quantità_stop_vibro = quantità_stop_vibro;
    }
}
