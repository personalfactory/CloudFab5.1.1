package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "categoria_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoriaOri.findAll", query = "SELECT c FROM CategoriaOri c")
})
public class CategoriaOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_cat")
    private Integer idCat;
    @Size(max = 100)
    @Column(name = "nome_categoria")
    private String nomeCategoria;
    @Size(max = 255)
    @Column(name = "descri_categoria")
    private String descriCategoria;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCat")
    private Collection<ProdottoOri> prodottoOriCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCat")
    private Collection<NumSacchettoOri> numSacchettoOriCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCat")
    private Collection<ValoreParProdOri> valoreParProdOriCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCat")
    private Collection<ValoreParSacchettoOri> valoreParSacchettoOriCollection;

    public CategoriaOri() {
    }

    public CategoriaOri(Integer idCat) {
        this.idCat = idCat;
    }

    public CategoriaOri(Integer idCat, Date dtAbilitato) {
        this.idCat = idCat;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdCat() {
        return idCat;
    }

    public void setIdCat(Integer idCat) {
        this.idCat = idCat;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getDescriCategoria() {
        return descriCategoria;
    }

    public void setDescriCategoria(String descriCategoria) {
        this.descriCategoria = descriCategoria;
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
    public Collection<ProdottoOri> getProdottoOriCollection() {
        return prodottoOriCollection;
    }

    public void setProdottoOriCollection(Collection<ProdottoOri> prodottoOriCollection) {
        this.prodottoOriCollection = prodottoOriCollection;
    }

    @XmlTransient
    public Collection<NumSacchettoOri> getNumSacchettoOriCollection() {
        return numSacchettoOriCollection;
    }

    public void setNumSacchettoOriCollection(Collection<NumSacchettoOri> numSacchettoOriCollection) {
        this.numSacchettoOriCollection = numSacchettoOriCollection;
    }

    @XmlTransient
    public Collection<ValoreParProdOri> getValoreParProdOriCollection() {
        return valoreParProdOriCollection;
    }

    public void setValoreParProdOriCollection(Collection<ValoreParProdOri> valoreParProdOriCollection) {
        this.valoreParProdOriCollection = valoreParProdOriCollection;
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
        hash += (idCat != null ? idCat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriaOri)) {
            return false;
        }
        CategoriaOri other = (CategoriaOri) object;
        if ((this.idCat == null && other.idCat != null) || (this.idCat != null && !this.idCat.equals(other.idCat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
