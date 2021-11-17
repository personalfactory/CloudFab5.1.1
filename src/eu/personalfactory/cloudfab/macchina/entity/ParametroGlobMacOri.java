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
@Table(name = "parametro_glob_mac_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroGlobMacOri.findAll", query = "SELECT p FROM ParametroGlobMacOri p"),
})
public class ParametroGlobMacOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_par_gm")
    private Integer idParGm;
    @Size(max = 255)
    @Column(name = "nome_variabile")
    private String nomeVariabile;
    @Size(max = 255)
    @Column(name = "descri_variabile")
    private String descriVariabile;
    @Size(max = 255)
    @Column(name = "valore_variabile")
    private String valoreVariabile;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ParametroGlobMacOri() {
    }

    public ParametroGlobMacOri(Integer idParGm) {
        this.idParGm = idParGm;
    }

    public ParametroGlobMacOri(Integer idParGm, Date dtAbilitato) {
        this.idParGm = idParGm;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdParGm() {
        return idParGm;
    }

    public void setIdParGm(Integer idParGm) {
        this.idParGm = idParGm;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParGm != null ? idParGm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametroGlobMacOri)) {
            return false;
        }
        ParametroGlobMacOri other = (ParametroGlobMacOri) object;
        if ((this.idParGm == null && other.idParGm != null) || (this.idParGm != null && !this.idParGm.equals(other.idParGm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
