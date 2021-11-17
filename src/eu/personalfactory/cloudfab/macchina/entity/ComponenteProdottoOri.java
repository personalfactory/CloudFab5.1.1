package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "componente_prodotto_ori", catalog = "origamidb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponenteProdottoOri.findAll", query = "SELECT c FROM ComponenteProdottoOri c")
})
public class ComponenteProdottoOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_comp_prod", nullable = false)
    private Integer idCompProd;
    @Column(name = "quantita", precision = 22)
    private Double quantita;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @JoinColumn(name = "id_comp", referencedColumnName = "id_comp", nullable = false)
    @ManyToOne(optional = false)
    private ComponenteOri idComp;
    @JoinColumn(name = "id_prodotto", referencedColumnName = "id_prodotto", nullable = false)
    @ManyToOne(optional = false)
    private ProdottoOri idProdotto;

    public ComponenteProdottoOri() {
    }

    public ComponenteProdottoOri(Integer idCompProd) {
        this.idCompProd = idCompProd;
    }

    public ComponenteProdottoOri(Integer idCompProd, Date dtAbilitato) {
        this.idCompProd = idCompProd;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdCompProd() {
        return idCompProd;
    }

    public void setIdCompProd(Integer idCompProd) {
        this.idCompProd = idCompProd;
    }

    public Double getQuantita() {
        return quantita;
    }

    public void setQuantita(Double quantita) {
        this.quantita = quantita;
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

    public ComponenteOri getIdComp() {
        return idComp;
    }

    public void setIdComp(ComponenteOri idComp) {
        this.idComp = idComp;
    }

    public ProdottoOri getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(ProdottoOri idProdotto) {
        this.idProdotto = idProdotto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompProd != null ? idCompProd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponenteProdottoOri)) {
            return false;
        }
        ComponenteProdottoOri other = (ComponenteProdottoOri) object;
        if ((this.idCompProd == null && other.idCompProd != null) || (this.idCompProd != null && !this.idCompProd.equals(other.idCompProd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
