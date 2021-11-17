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
@Table(name = "ciclo_processo_ori")
@XmlRootElement
/**@NamedQueries({
    @NamedQuery(name = "CicloProcessoOri.findAll", query = "SELECT c FROM CicloProcessoOri c"),
    @NamedQuery(name = "CicloProcessoOri.findById", query = "SELECT c FROM CicloProcessoOri c WHERE c.id = :id"),
    @NamedQuery(name = "CicloProcessoOri.findByIdCiclo", query = "SELECT c FROM CicloProcessoOri c WHERE c.idCiclo = :idCiclo"),
    @NamedQuery(name = "CicloProcessoOri.findByIdProcesso", query = "SELECT c FROM CicloProcessoOri c WHERE c.idProcesso = :idProcesso"),
    @NamedQuery(name = "CicloProcessoOri.findByDtInizioProcesso", query = "SELECT c FROM CicloProcessoOri c WHERE c.dtInizioProcesso = :dtInizioProcesso"),
    @NamedQuery(name = "CicloProcessoOri.findByDtFineProcesso", query = "SELECT c FROM CicloProcessoOri c WHERE c.dtFineProcesso = :dtFineProcesso"),
    @NamedQuery(name = "CicloProcessoOri.findByDtAbilitato", query = "SELECT c FROM CicloProcessoOri c WHERE c.dtAbilitato = :dtAbilitato"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo1", query = "SELECT c FROM CicloProcessoOri c WHERE c.info1 = :info1"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo2", query = "SELECT c FROM CicloProcessoOri c WHERE c.info2 = :info2"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo3", query = "SELECT c FROM CicloProcessoOri c WHERE c.info3 = :info3"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo4", query = "SELECT c FROM CicloProcessoOri c WHERE c.info4 = :info4"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo5", query = "SELECT c FROM CicloProcessoOri c WHERE c.info5 = :info5"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo6", query = "SELECT c FROM CicloProcessoOri c WHERE c.info6 = :info6"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo7", query = "SELECT c FROM CicloProcessoOri c WHERE c.info7 = :info7"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo8", query = "SELECT c FROM CicloProcessoOri c WHERE c.info8 = :info8"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo9", query = "SELECT c FROM CicloProcessoOri c WHERE c.info9 = :info9"),
    @NamedQuery(name = "CicloProcessoOri.findByInfo10", query = "SELECT c FROM CicloProcessoOri c WHERE c.info10 = :info10"),
})*/
@NamedQuery(name = "CicloProcessoOri.findDatiNuovi", query = "SELECT c FROM CicloProcessoOri c WHERE c.dtAbilitato > :dtAbilitato")
public class CicloProcessoOri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_ciclo")
    private Integer idCiclo;
    @Column(name = "id_processo")
    private Integer idProcesso;
    @Column(name = "dt_inizio_processo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInizioProcesso;
    @Column(name = "dt_fine_processo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFineProcesso;
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
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
    @Size(max = 255)
    @Column(name = "info6")
    private String info6;
    @Size(max = 255)
    @Column(name = "info7")
    private String info7;
    @Size(max = 255)
    @Column(name = "info8")
    private String info8;
    @Size(max = 255)
    @Column(name = "info9")
    private String info9;
    @Size(max = 255)
    @Column(name = "info10")
    private String info10;

    public CicloProcessoOri() {
    }

    public CicloProcessoOri(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public Integer getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Integer idProcesso) {
        this.idProcesso = idProcesso;
    }

    public Date getDtInizioProcesso() {
        return dtInizioProcesso;
    }

    public void setDtInizioProcesso(Date dtInizioProcesso) {
        this.dtInizioProcesso = dtInizioProcesso;
    }

    public Date getDtFineProcesso() {
        return dtFineProcesso;
    }

    public void setDtFineProcesso(Date dtFineProcesso) {
        this.dtFineProcesso = dtFineProcesso;
    }

    public Date getDtAbilitato() {
        return dtAbilitato;
    }

    public void setDtAbilitato(Date dtAbilitato) {
        this.dtAbilitato = dtAbilitato;
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

    public String getInfo6() {
        return info6;
    }

    public void setInfo6(String info6) {
        this.info6 = info6;
    }

    public String getInfo7() {
        return info7;
    }

    public void setInfo7(String info7) {
        this.info7 = info7;
    }

    public String getInfo8() {
        return info8;
    }

    public void setInfo8(String info8) {
        this.info8 = info8;
    }

    public String getInfo9() {
        return info9;
    }

    public void setInfo9(String info9) {
        this.info9 = info9;
    }

    public String getInfo10() {
        return info10;
    }

    public void setInfo10(String info10) {
        this.info10 = info10;
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
        if (!(object instanceof CicloProcessoOri)) {
            return false;
        }
        CicloProcessoOri other = (CicloProcessoOri) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.CicloProcessoOri[ id=" + id + " ]";
    }
    
}
