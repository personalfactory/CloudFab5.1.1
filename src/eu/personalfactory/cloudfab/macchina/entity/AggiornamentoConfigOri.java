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
@Table(name = "aggiornamento_config_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AggiornamentoConfigOri.findAll", query = "SELECT a FROM AggiornamentoConfigOri a"),
    @NamedQuery(name = "AggiornamentoConfigOri.findByParametro", query = "SELECT a FROM AggiornamentoConfigOri a WHERE a.parametro = :parametro")})
public class AggiornamentoConfigOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "parametro")
    private String parametro;
    @Size(max = 255)
    @Column(name = "valore")
    private String valore;
    @Size(max = 255)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 255)
    @Column(name = "descrizione")
    private String descrizione;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public AggiornamentoConfigOri() {
    }

    public AggiornamentoConfigOri(Integer id) {
        this.id = id;
    }

    public AggiornamentoConfigOri(Integer id, Date dtAbilitato) {
        this.id = id;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AggiornamentoConfigOri)) {
            return false;
        }
        AggiornamentoConfigOri other = (AggiornamentoConfigOri) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}

 