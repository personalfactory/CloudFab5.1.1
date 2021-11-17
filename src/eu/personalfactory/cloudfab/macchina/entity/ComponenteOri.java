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
@Table(name = "componente_ori", catalog = "origamidb", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponenteOri.findAll", query = "SELECT c FROM ComponenteOri c")
})
public class ComponenteOri implements Serializable {

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idComp")
//    private Collection<ComponenteProdottoOri> componenteProdottoOriCollection;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idComp")
//    private Collection<ComponentePesaturaOri> componentePesaturaOriCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_comp", nullable = false)
    private Integer idComp;
    @Size(max = 50)
    @Column(name = "cod_componente", length = 50)
    private String codComponente;
    @Size(max = 255)
    @Column(name = "descri_componente", length = 255)
    private String descriComponente;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ComponenteOri() {
    }

    public ComponenteOri(Integer idComp) {
        this.idComp = idComp;
    }

    public ComponenteOri(Integer idComp, Date dtAbilitato) {
        this.idComp = idComp;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdComp() {
        return idComp;
    }

    public void setIdComp(Integer idComp) {
        this.idComp = idComp;
    }

    public String getCodComponente() {
        return codComponente;
    }

    public void setCodComponente(String codComponente) {
        this.codComponente = codComponente;
    }

    public String getDescriComponente() {
        return descriComponente;
    }

    public void setDescriComponente(String descriComponente) {
        this.descriComponente = descriComponente;
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
        hash += (idComp != null ? idComp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponenteOri)) {
            return false;
        }
        ComponenteOri other = (ComponenteOri) object;
        if ((this.idComp == null && other.idComp != null) || (this.idComp != null && !this.idComp.equals(other.idComp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }

//    @XmlTransient
//    public Collection<ComponentePesaturaOri> getComponenteProdottoOriCollection() {
//        return componentePesaturaOriCollection;
//    }
//
//    public void setComponenteProdottoOriCollection(Collection<ComponenteProdottoOri> componenteProdottoOriCollection) {
//        this.componenteProdottoOriCollection = componenteProdottoOriCollection;
//    }
//
//    @XmlTransient
//    public Collection<ComponentePesaturaOri> getComponentePesaturaOriCollection() {
//        return componentePesaturaOriCollection;
//    }
//
//    public void setComponentePesaturaOriCollection(Collection<ComponentePesaturaOri> componentePesaturaOriCollection) {
//        this.componentePesaturaOriCollection = componentePesaturaOriCollection;
//    }
    
    
}
