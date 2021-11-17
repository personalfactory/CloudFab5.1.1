/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Francesco Di Gaudio
 */
@Entity
@Table(name = "figura_tipo_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FiguraTipoOri.findAll", query = "SELECT f FROM FiguraTipoOri f")
    , @NamedQuery(name = "FiguraTipoOri.findByIdFiguraTipo", query = "SELECT f FROM FiguraTipoOri f WHERE f.idFiguraTipo = :idFiguraTipo")
    , @NamedQuery(name = "FiguraTipoOri.findByFigura", query = "SELECT f FROM FiguraTipoOri f WHERE f.figura = :figura")
    , @NamedQuery(name = "FiguraTipoOri.findByAbilitato", query = "SELECT f FROM FiguraTipoOri f WHERE f.abilitato = :abilitato")
    , @NamedQuery(name = "FiguraTipoOri.findByDtAbilitato", query = "SELECT f FROM FiguraTipoOri f WHERE f.dtAbilitato = :dtAbilitato")})
public class FiguraTipoOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_figura_tipo")
    private Integer idFiguraTipo;
    @Size(max = 255)
    @Column(name = "figura")
    private String figura;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @OneToMany(mappedBy = "idFiguraTipo")
    private Collection<FiguraOri> figuraOriCollection;

    public FiguraTipoOri() {
    }

    public FiguraTipoOri(Integer idFiguraTipo) {
        this.idFiguraTipo = idFiguraTipo;
    }

    public Integer getIdFiguraTipo() {
        return idFiguraTipo;
    }

    public void setIdFiguraTipo(Integer idFiguraTipo) {
        this.idFiguraTipo = idFiguraTipo;
    }

    public String getFigura() {
        return figura;
    }

    public void setFigura(String figura) {
        this.figura = figura;
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

    @XmlTransient
    public Collection<FiguraOri> getFiguraOriCollection() {
        return figuraOriCollection;
    }

    public void setFiguraOriCollection(Collection<FiguraOri> figuraOriCollection) {
        this.figuraOriCollection = figuraOriCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFiguraTipo != null ? idFiguraTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FiguraTipoOri)) {
            return false;
        }
        FiguraTipoOri other = (FiguraTipoOri) object;
        if ((this.idFiguraTipo == null && other.idFiguraTipo != null) || (this.idFiguraTipo != null && !this.idFiguraTipo.equals(other.idFiguraTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.FiguraTipoOri[ idFiguraTipo=" + idFiguraTipo + " ]";
    }
    
}
