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
 * @author Francesco Di Gaudio 
 */
@Entity
@Table(name = "figura_ori")
@XmlRootElement
/**@NamedQueries({
      @NamedQuery(name = "FiguraOri.findAll", query = "SELECT f FROM FiguraOri f")
    , @NamedQuery(name = "FiguraOri.findByIdFigura", query = "SELECT f FROM FiguraOri f WHERE f.idFigura = :idFigura")
    , @NamedQuery(name = "FiguraOri.findByNominativo", query = "SELECT f FROM FiguraOri f WHERE f.nominativo = :nominativo")
    , @NamedQuery(name = "FiguraOri.findByCodice", query = "SELECT f FROM FiguraOri f WHERE f.codice = :codice")
    , @NamedQuery(name = "FiguraOri.findByAbilitato", query = "SELECT f FROM FiguraOri f WHERE f.abilitato = :abilitato")
    , @NamedQuery(name = "FiguraOri.findByDtAbilitato", query = "SELECT f FROM FiguraOri f WHERE f.dtAbilitato = :dtAbilitato")
     })*/
public class FiguraOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_figura")
    private Integer idFigura;
    @Size(max = 255)
    @Column(name = "nominativo")
    private String nominativo;
    @Size(max = 255)
    @Column(name = "codice")
    private String codice;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @JoinColumn(name = "id_figura_tipo", referencedColumnName = "id_figura_tipo")
    @ManyToOne
    private FiguraTipoOri idFiguraTipo;

    public FiguraOri() {
    }

    public FiguraOri(Integer idFigura) {
        this.idFigura = idFigura;
    }

    public Integer getIdFigura() {
        return idFigura;
    }

    public void setIdFigura(Integer idFigura) {
        this.idFigura = idFigura;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
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
   

    public FiguraTipoOri getIdFiguraTipo() {
        return idFiguraTipo;
    }

    public void setIdFiguraTipo(FiguraTipoOri idFiguraTipo) {
        this.idFiguraTipo = idFiguraTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFigura != null ? idFigura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FiguraOri)) {
            return false;
        }
        FiguraOri other = (FiguraOri) object;
        if ((this.idFigura == null && other.idFigura != null) || (this.idFigura != null && !this.idFigura.equals(other.idFigura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.FiguraOri[ idFigura=" + idFigura + " ]";
    }
    
}
