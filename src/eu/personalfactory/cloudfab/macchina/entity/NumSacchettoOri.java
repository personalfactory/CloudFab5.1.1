package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "num_sacchetto_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NumSacchettoOri.findAll", query = "SELECT n FROM NumSacchettoOri n"),
})

public class NumSacchettoOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_num_sac")
    private Integer idNumSac;
    @Column(name = "num_sacchetti")
    private Integer numSacchetti;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @JoinColumn(name = "id_cat", referencedColumnName = "id_cat")
    @ManyToOne(optional = false)
    private CategoriaOri idCat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idNumSac")
    private Collection<ValoreParSacchettoOri> valoreParSacchettoOriCollection;

    public NumSacchettoOri() {
    }

    public NumSacchettoOri(Integer idNumSac) {
        this.idNumSac = idNumSac;
    }

    public NumSacchettoOri(Integer idNumSac, Date dtAbilitato) {
        this.idNumSac = idNumSac;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdNumSac() {
        return idNumSac;
    }

    public void setIdNumSac(Integer idNumSac) {
        this.idNumSac = idNumSac;
    }

    public Integer getNumSacchetti() {
        return numSacchetti;
    }

    public void setNumSacchetti(Integer numSacchetti) {
        this.numSacchetti = numSacchetti;
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

    public CategoriaOri getIdCat() {
        return idCat;
    }

    public void setIdCat(CategoriaOri idCat) {
        this.idCat = idCat;
    }

    @XmlTransient
    public Collection<ValoreParSacchettoOri> getValoreParSacchettoOriCollection() {
        return valoreParSacchettoOriCollection;
    }

    public void setValoreParSacchettoOriCollection(Collection<ValoreParSacchettoOri> valoreParSacchettoOriCollection) {
        this.valoreParSacchettoOriCollection = valoreParSacchettoOriCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumSac != null ? idNumSac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NumSacchettoOri)) {
            return false;
        }
        NumSacchettoOri other = (NumSacchettoOri) object;
        if ((this.idNumSac == null && other.idNumSac != null) || (this.idNumSac != null && !this.idNumSac.equals(other.idNumSac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
