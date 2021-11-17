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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "valore_allarme_ori")
@XmlRootElement
@NamedQueries({
    /**@NamedQuery(name = "ValoreAllarmeOri.findAll", query = "SELECT v FROM ValoreAllarmeOri v"),
    @NamedQuery(name = "ValoreAllarmeOri.findById", query = "SELECT v FROM ValoreAllarmeOri v WHERE v.id = :id"),
    @NamedQuery(name = "ValoreAllarmeOri.findByValore", query = "SELECT v FROM ValoreAllarmeOri v WHERE v.valore = :valore"),
    @NamedQuery(name = "ValoreAllarmeOri.findByIdTabellaRif", query = "SELECT v FROM ValoreAllarmeOri v WHERE v.idTabellaRif = :idTabellaRif"),
    @NamedQuery(name = "ValoreAllarmeOri.findByAbilitato", query = "SELECT v FROM ValoreAllarmeOri v WHERE v.abilitato = :abilitato"),
    @NamedQuery(name = "ValoreAllarmeOri.findByDtAbilitato", query = "SELECT v FROM ValoreAllarmeOri v WHERE v.dtAbilitato = :dtAbilitato"),*/
@NamedQuery(name = "ValoreAllarmeOri.findDatiNuovi", query = "SELECT v FROM ValoreAllarmeOri v WHERE v.dtAbilitato > :dtAbilitato")})

public class ValoreAllarmeOri implements Serializable {
    @Size(max = 255)
    @Column(name = "info1")
    private String info1;
    @Size(max = 255)
    @Column(name = "info2")
    private String info2;
    @Size(max = 255)
    @Column(name = "info3")
    private String info3;
    @Size(max = 255)
    @Column(name = "info4")
    private String info4;
    @Size(max = 255)
    @Column(name = "info5")
    private String info5;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "valore")
    private String valore;
    @Column(name = "id_tabella_rif")
    private Integer idTabellaRif;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
       
    @Column(name = "id_allarme")
    private Integer idAllarme;

    public ValoreAllarmeOri() {
    }

    public Integer getIdAllarme() {
        return idAllarme;
    }

    public void setIdAllarme(Integer idAllarme) {
        this.idAllarme = idAllarme;
    }
    

    public ValoreAllarmeOri(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public Integer getIdTabellaRif() {
        return idTabellaRif;
    }

    public void setIdTabellaRif(Integer idTabellaRif) {
        this.idTabellaRif = idTabellaRif;
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
        if (!(object instanceof ValoreAllarmeOri)) {
            return false;
        }
        ValoreAllarmeOri other = (ValoreAllarmeOri) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.ValoreAllarmeOri[ id=" + id + " ]";
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public String getInfo3() {
        return info3;
    }

    public void setInfo3(String info3) {
        this.info3 = info3;
    }

    public String getInfo4() {
        return info4;
    }

    public void setInfo4(String info4) {
        this.info4 = info4;
    }

    public String getInfo5() {
        return info5;
    }

    public void setInfo5(String info5) {
        this.info5 = info5;
    }
    
}
