/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "valore_par_prod_mac_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoreParProdMacOri.findAll", query = "SELECT v FROM ValoreParProdMacOri v")
    
})
public class ValoreParProdMacOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_val_pm")
    private Integer idValPm;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_par_pm")
    private int idParPm;

    @Size(max = 255)
    @Column(name = "valore_variabile")
    private String valoreVariabile;

    @Column(name = "abilitato")
    private Boolean abilitato;
    //@Basic(optional = false)
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    @JoinColumn(name = "id_prodotto", referencedColumnName = "id_prodotto")
    @ManyToOne(optional = false)
    private ProdottoOri idProdotto;

    public ValoreParProdMacOri() {
    }

    public ValoreParProdMacOri(Integer idValPm) {
        this.idValPm = idValPm;
    }

    public ValoreParProdMacOri(Integer idValPm, Date dtAbilitato) {
        this.idValPm = idValPm;

        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdValPm() {
        return idValPm;
    }

    public void setIdValPm(Integer idValPm) {
        this.idValPm = idValPm;
    }

    public int getIdParPm() {
        return idParPm;
    }

    public void setIdParPm(int idParPm) {
        this.idParPm = idParPm;
    }

    public ProdottoOri getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(ProdottoOri idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getValoreVariabile() {
        return valoreVariabile;
    }

    public void setValoreVariabile(String valoreVariabile) {
        this.valoreVariabile = valoreVariabile;
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
        hash += (idValPm != null ? idValPm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoreParProdMacOri)) {
            return false;
        }
        ValoreParProdMacOri other = (ValoreParProdMacOri) object;
        if ((this.idValPm == null && other.idValPm != null) || (this.idValPm != null && !this.idValPm.equals(other.idValPm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.syncorigami.macchina.entity.ValoreParProdMacOri[ idValPm=" + idValPm + " ]";
    }

}
