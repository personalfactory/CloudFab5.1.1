package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "valore_par_sing_mac_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoreParSingMacOri.findAll", query = "SELECT v FROM ValoreParSingMacOri v"),
    @NamedQuery(name = "ValoreParSingMacOri.findDatiNuovi", query = "SELECT v FROM ValoreParSingMacOri v WHERE v.dtModificaMac > :dtModificaMac "),
    @NamedQuery(name = "ValoreParSingMacOri.findByIdParSm", query = "SELECT v FROM ValoreParSingMacOri v WHERE v.idParSm = :idParSm"),
})
public class ValoreParSingMacOri implements Serializable {
    
    private static final long serialVersionUID = 1L;
    //Modificato con l'aggiornamento
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_val_par_sm")
    private Integer idValParSm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_par_sm")
    private int idParSm;
    //Modificato sia in locale che con l'aggiornamento
    @Size(max = 255)
    @Column(name = "valore_variabile")
    private String valoreVariabile;
    //Modificato con l'aggiornamento
    @Size(max = 255)
    @Column(name = "valore_iniziale")
    private String valoreIniziale;
    //Modificato con l'aggiornamento
    @Column(name = "abilitato")
    private Boolean abilitato;
    //Modificato con l'aggiornamento 
    @Column(name = "dt_valore_iniziale")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtValoreIniziale;
    //Modificato in locale (solo al primo insert dal server)
    @Column(name = "dt_modifica_mac")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtModificaMac;
    //Modificato solo in locale
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ValoreParSingMacOri() {
    }

    public ValoreParSingMacOri(Integer idValParSm) {
        this.idValParSm = idValParSm;
    }

    public ValoreParSingMacOri(Integer idValParSm, int idParSm, Date dtAbilitato) {
        this.idValParSm = idValParSm;
        this.idParSm = idParSm;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdValParSm() {
        return idValParSm;
    }

    public void setIdValParSm(Integer idValParSm) {
        this.idValParSm = idValParSm;
    }

    public int getIdParSm() {
        return idParSm;
    }

    public void setIdParSm(int idParSm) {
        this.idParSm = idParSm;
    }

    public String getValoreVariabile() {
        return valoreVariabile;
    }

    public void setValoreVariabile(String valoreVariabile) {
        this.valoreVariabile = valoreVariabile;
    }

    public String getValoreIniziale() {
        return valoreIniziale;
    }

    public void setValoreIniziale(String valoreIniziale) {
        this.valoreIniziale = valoreIniziale;
    }

    public Date getDtValoreIniziale() {
        return dtValoreIniziale;
    }

    public void setDtValoreIniziale(Date dtValoreIniziale) {
        this.dtValoreIniziale = dtValoreIniziale;
    }

    public Date getDtModificaMac() {
        return dtModificaMac;
    }

    public void setDtModificaMac(Date dtModificaMac) {
        this.dtModificaMac = dtModificaMac;
    }

    public Boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
    }

    public Date getDtAbilitato() {
        return dtAbilitato;
    }

    public void setDtAbilitato(Date dtAbilitato) {
        this.dtAbilitato = dtAbilitato;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idValParSm != null ? idValParSm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoreParSingMacOri)) {
            return false;
        }
        ValoreParSingMacOri other = (ValoreParSingMacOri) object;
        if ((this.idValParSm == null && other.idValParSm != null) || (this.idValParSm != null && !this.idValParSm.equals(other.idValParSm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
