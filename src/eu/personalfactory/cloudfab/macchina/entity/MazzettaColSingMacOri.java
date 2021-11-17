package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "mazzetta_col_sing_mac_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MazzettaColSingMacOri.findAll", query = "SELECT m FROM MazzettaColSingMacOri m"),
    @NamedQuery(name = "MazzettaColSingMacOri.findDatiNuovi", query = "SELECT m FROM MazzettaColSingMacOri m WHERE m.dtAbilitato > :dtAbilitato"),
})
public class MazzettaColSingMacOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_maz_sing_mac")
    private Integer idMazSingMac;
    @JoinColumn(name = "id_mazzetta", referencedColumnName = "id_mazzetta")
    @ManyToOne(optional = false)
    private MazzettaOri idMazzetta;
    @JoinColumn(name = "id_colore_base", referencedColumnName = "id_colore_base")
    @ManyToOne(optional = false)
    private ColoreBaseOri idColoreBase;
    @Column(name = "cod_colore")
    private String codColore;
    @Column(name = "quantita")
    private Integer quantita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "abilitato")
    private boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public MazzettaColSingMacOri() {
    }

    public MazzettaColSingMacOri(Integer idMazSingMac) {
        this.idMazSingMac = idMazSingMac;
    }

    public MazzettaColSingMacOri(Integer idMazSingMac, Date dtAbilitato) {
        this.idMazSingMac = idMazSingMac;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdMazSingMac() {
        return idMazSingMac;
    }

    public void setIdMazSingMac(Integer idMazSingMac) {
        this.idMazSingMac = idMazSingMac;
    }

    public Date getDtAbilitato() {
        return dtAbilitato;
    }

    public void setDtAbilitato(Date dtAbilitato) {
        this.dtAbilitato = dtAbilitato;
    }

    public MazzettaOri getIdMazzetta() {
        return idMazzetta;
    }

    public void setIdMazzetta(MazzettaOri idMazzetta) {
        this.idMazzetta = idMazzetta;
    }

    public ColoreBaseOri getIdColoreBase() {
        return idColoreBase;
    }

    public void setIdColoreBase(ColoreBaseOri idColoreBase) {
        this.idColoreBase = idColoreBase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMazSingMac != null ? idMazSingMac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MazzettaColSingMacOri)) {
            return false;
        }
        MazzettaColSingMacOri other = (MazzettaColSingMacOri) object;
        if ((this.idMazSingMac == null && other.idMazSingMac != null) || (this.idMazSingMac != null && !this.idMazSingMac.equals(other.idMazSingMac))) {
            return false;
        }
        return true;
    }

    public boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(boolean abilitato) {
        this.abilitato = abilitato;
    }

    public String getCodColore() {
        return codColore;
    }

    public void setCodColore(String codColore) {
        this.codColore = codColore;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
