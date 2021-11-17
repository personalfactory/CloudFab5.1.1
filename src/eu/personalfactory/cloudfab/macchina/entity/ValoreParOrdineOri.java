/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Francesco Di Gaudio
 */
@Entity
@Table(name = "valore_par_ordine_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoreParOrdineOri.findAll", query = "SELECT v FROM ValoreParOrdineOri v")
    , @NamedQuery(name = "ValoreParOrdineOri.findById", query = "SELECT v FROM ValoreParOrdineOri v WHERE v.id = :id")
    , @NamedQuery(name = "ValoreParOrdineOri.findByIdParOrdine", query = "SELECT v FROM ValoreParOrdineOri v WHERE v.idParOrdine = :idParOrdine")
    , @NamedQuery(name = "ValoreParOrdineOri.findByIdOrdineSm", query = "SELECT v FROM ValoreParOrdineOri v WHERE v.idOrdineSm = :idOrdineSm")
    , @NamedQuery(name = "ValoreParOrdineOri.findByValore", query = "SELECT v FROM ValoreParOrdineOri v WHERE v.valore = :valore")
    , @NamedQuery(name = "ValoreParOrdineOri.findByAbilitato", query = "SELECT v FROM ValoreParOrdineOri v WHERE v.abilitato = :abilitato")
    , @NamedQuery(name = "ValoreParOrdineOri.findByDtAbilitato", query = "SELECT v FROM ValoreParOrdineOri v WHERE v.dtAbilitato = :dtAbilitato")})
public class ValoreParOrdineOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_par_ordine")
    private Integer idParOrdine;
    @Column(name = "id_ordine_sm")
    private Integer idOrdineSm;
    @Size(max = 255)
    @Column(name = "valore")
    private String valore;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ValoreParOrdineOri() {
    }

    public ValoreParOrdineOri(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdParOrdine() {
        return idParOrdine;
    }

    public void setIdParOrdine(Integer idParOrdine) {
        this.idParOrdine = idParOrdine;
    }

    public Integer getIdOrdineSm() {
        return idOrdineSm;
    }

    public void setIdOrdineSm(Integer idOrdineSm) {
        this.idOrdineSm = idOrdineSm;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoreParOrdineOri)) {
            return false;
        }
        ValoreParOrdineOri other = (ValoreParOrdineOri) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.ValoreParOrdineOri[ id=" + id + " ]";
    }
    
}
