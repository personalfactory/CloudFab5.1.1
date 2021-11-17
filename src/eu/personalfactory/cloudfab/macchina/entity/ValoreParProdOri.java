package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
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
@Table(name = "valore_par_prod_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoreParProdOri.findAll", query = "SELECT v FROM ValoreParProdOri v")
})
public class ValoreParProdOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_val_par_pr")
    private Integer idValParPr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_par_prod")
    private int idParProd;
    @Size(max = 255)
    @Column(name = "valore_variabile")
    private String valoreVariabile;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @JoinColumn(name = "id_cat", referencedColumnName = "id_cat")
    @ManyToOne(optional = false)
    private CategoriaOri idCat;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ValoreParProdOri() {
    }

    public ValoreParProdOri(Integer idValParPr) {
        this.idValParPr = idValParPr;
    }

    public ValoreParProdOri(Integer idValParPr, int idParProd, Date dtAbilitato) {
        this.idValParPr = idValParPr;
        this.idParProd = idParProd;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdValParPr() {
        return idValParPr;
    }

    public void setIdValParPr(Integer idValParPr) {
        this.idValParPr = idValParPr;
    }

    public int getIdParProd() {
        return idParProd;
    }

    public void setIdParProd(int idParProd) {
        this.idParProd = idParProd;
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

    public CategoriaOri getIdCat() {
        return idCat;
    }

    public void setIdCat(CategoriaOri idCat) {
        this.idCat = idCat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idValParPr != null ? idValParPr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoreParProdOri)) {
            return false;
        }
        ValoreParProdOri other = (ValoreParProdOri) object;
        if ((this.idValParPr == null && other.idValParPr != null) || (this.idValParPr != null && !this.idValParPr.equals(other.idValParPr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
