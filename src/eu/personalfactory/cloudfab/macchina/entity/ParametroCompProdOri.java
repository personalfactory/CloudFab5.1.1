package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "parametro_comp_prod_ori", catalog = "origamidb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroCompProdOri.findAll", query = "SELECT p FROM ParametroCompProdOri p"),
})
public class ParametroCompProdOri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_par_comp", nullable = false)
    private Integer idParComp;
    @Size(max = 50)
    @Column(name = "nome_variabile", length = 50)
    private String nomeVariabile;
    @Size(max = 255)
    @Column(name = "descri_variabile", length = 255)
    private String descriVariabile;
    @Size(max = 255)
    @Column(name = "valore_base", length = 255)
    private String valoreBase;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ParametroCompProdOri() {
    }

    public ParametroCompProdOri(Integer idParComp) {
        this.idParComp = idParComp;
    }

    public ParametroCompProdOri(Integer idParComp, Date dtAbilitato) {
        this.idParComp = idParComp;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdParComp() {
        return idParComp;
    }

    public void setIdParComp(Integer idParComp) {
        this.idParComp = idParComp;
    }

    public String getNomeVariabile() {
        return nomeVariabile;
    }

    public void setNomeVariabile(String nomeVariabile) {
        this.nomeVariabile = nomeVariabile;
    }

    public String getDescriVariabile() {
        return descriVariabile;
    }

    public void setDescriVariabile(String descriVariabile) {
        this.descriVariabile = descriVariabile;
    }

    public String getValoreBase() {
        return valoreBase;
    }

    public void setValoreBase(String valoreBase) {
        this.valoreBase = valoreBase;
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
        hash += (idParComp != null ? idParComp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroCompProdOri)) {
            return false;
        }
        ParametroCompProdOri other = (ParametroCompProdOri) object;
        if ((this.idParComp == null && other.idParComp != null) || (this.idParComp != null && !this.idParComp.equals(other.idParComp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
    
}
