/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.pulizia;

import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaValoreParametriComponentePerIdComp;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_ELEMENTI_STRINGA_VEL_IND_COMP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_FINE_STRINGA_VEL_IND_COMP;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.CARATTERE_STRINGA_VEL_IND_COMP;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import java.util.ArrayList;

/**
 *
 * @author imacdigaudio
 */
public class DettagliPulizia {

    private final int idComponente;
    private final int idPresa;
    private final int volo;
    private final double correttivoVel;
    private final int dosaturaDiretta;
    private final int QComponente;
    private final int QConfezione;
    private final int tempoMix;
    private final int secondaVel;
    private final int idOrdine;
    private final int idProdotto;

    

    private final String velMix;
    private final String codiceComponente;
    private final String curvaVel;

    private final ArrayList<String> velCarico;
    private final ArrayList<Integer> aliqCarico;
    private final ArrayList<Integer> aliqScarico;

    private int idCiclo;

    public DettagliPulizia(Integer idComponente, Integer qComponente, Integer tempoMix, String velMix, Integer qConfezione, int idOrdine, int idProdotto) {
        this.idComponente = idComponente;
        this.QComponente = qComponente;
        this.tempoMix = tempoMix*1000;
        this.velMix = velMix;
        this.QConfezione = qConfezione;
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;

        //Inizializzazione idCiclo
        idCiclo = 0;

        // Lettura Parametri Componente
        velCarico = new ArrayList<>();
        aliqCarico = new ArrayList<>();

        ArrayList<String> valoreParametri = TrovaValoreParametriComponentePerIdComp(idComponente);

        //idPresa
        idPresa = Integer.parseInt(valoreParametri.get(0));
        //secondaVelocita
        secondaVel = Integer.parseInt(valoreParametri.get(1));
        //volo
        volo = Integer.parseInt(valoreParametri.get(2));
        //presa
        correttivoVel = Double.parseDouble(valoreParametri.get(3));
        //presa
        dosaturaDiretta = Integer.parseInt(valoreParametri.get(4));
        //codice Componente
        codiceComponente = valoreParametri.get(5);
        //curva Velocità
        curvaVel = valoreParametri.get(6);

        //Decodifica curva velocità
        int index_charSep = this.curvaVel.indexOf(CARATTERE_STRINGA_VEL_IND_COMP);
        String strVelocità = this.curvaVel.substring(0, index_charSep);
        String strLimitiCambioVelocità = this.curvaVel.substring(index_charSep + 1, this.curvaVel.length());

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

                velCarico.add(vel_mod);

                temp = "";
            } else {
                temp += strVelocità.charAt(i);

            }
        }

        temp = "";
        for (int i = 0; i < strLimitiCambioVelocità.length(); i++) {

            if (strLimitiCambioVelocità.charAt(i) == CARATTERE_ELEMENTI_STRINGA_VEL_IND_COMP
                    || strLimitiCambioVelocità.charAt(i) == CARATTERE_FINE_STRINGA_VEL_IND_COMP) {

                aliqCarico.add(Integer.parseInt(temp));
                temp = "";
            } else {
                temp += strLimitiCambioVelocità.charAt(i);

            }
        }

        //Lettura Parametri Confezione
        aliqScarico = new ArrayList<>();

        int numPuntiRilievoScarico = Integer.parseInt(ParametriSingolaMacchina.parametri.get(48));
        for (int i = 0; i < numPuntiRilievoScarico; i++) {
            aliqScarico.add(Integer.parseInt(ParametriSingolaMacchina.parametri.get(49 + i)));

        }

    }

    ///////////////
    // GETTERS  ///
    ///////////////
    public int getIdComponente() {
        return idComponente;
    }

    public int getIdPresa() {
        return idPresa;
    }

    public int getVolo() {
        return volo;
    }

    public double getCorrettivoVel() {
        return correttivoVel;
    }

    public int getDosaturaDiretta() {
        return dosaturaDiretta;
    }

    public int getSecondaVel() {
        return secondaVel;
    }

    public String getCodiceComponente() {
        return codiceComponente;
    }

    public String getCurvaVel() {
        return curvaVel;
    }

    public ArrayList<String> getVelCarico() {
        return velCarico;
    }

    public ArrayList<Integer> getAliqCarico() {
        return aliqCarico;
    }

    public int getQComponente() {
        return QComponente;
    }

    public int getQConfezione() {
        return QConfezione;
    }

    public int getTempoMix() {
        return tempoMix;
    }

    public String getVelMix() {
        return velMix;
    }

    public ArrayList<Integer> getAliqScarico() {
        return aliqScarico;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdOrdine() {
        return idOrdine;
    }
    
    public int getIdProdotto() {
        return idProdotto;
    }
}
