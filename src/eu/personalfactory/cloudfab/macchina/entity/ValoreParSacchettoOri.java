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
@Table(name = "valore_par_sacchetto_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValoreParSacchettoOri.findAll", query = "SELECT v FROM ValoreParSacchettoOri v")
}) 
public class ValoreParSacchettoOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_val_par_sac")
    private Integer idValParSac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_par_sac")
    private int idParSac;
    @Size(max = 255)
    @Column(name = "nome_par_sac")
    private String nomeParSac;
    @Column(name = "sacchetto")
    private Integer sacchetto;
    @Size(max = 255)
    @Column(name = "valore_variabile")
    private String valoreVariabile;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @JoinColumn(name = "id_cat", referencedColumnName = "id_cat")
    @ManyToOne(optional = false)
    private CategoriaOri idCat;
    @JoinColumn(name = "id_num_sac", referencedColumnName = "id_num_sac")
    @ManyToOne(optional = false)
    private NumSacchettoOri idNumSac;

    public ValoreParSacchettoOri() {
    }

    public ValoreParSacchettoOri(Integer idValParSac) {
        this.idValParSac = idValParSac;
    }

    public ValoreParSacchettoOri(Integer idValParSac, int idParSac, Date dtAbilitato) {
        this.idValParSac = idValParSac;
        this.idParSac = idParSac;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdValParSac() {
        return idValParSac;
    }

    public void setIdValParSac(Integer idValParSac) {
        this.idValParSac = idValParSac;
    }

    public int getIdParSac() {
        return idParSac;
    }

    public void setIdParSac(int idParSac) {
        this.idParSac = idParSac;
    }

    public String getNomeParSac() {
        return nomeParSac;
    }

    public void setNomeParSac(String nomeParSac) {
        this.nomeParSac = nomeParSac;
    }

    public Integer getSacchetto() {
        return sacchetto;
    }

    public void setSacchetto(Integer sacchetto) {
        this.sacchetto = sacchetto;
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

    public NumSacchettoOri getIdNumSac() {
        return idNumSac;
    }

    public void setIdNumSac(NumSacchettoOri idNumSac) {
        this.idNumSac = idNumSac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idValParSac != null ? idValParSac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValoreParSacchettoOri)) {
            return false;
        }
        ValoreParSacchettoOri other = (ValoreParSacchettoOri) object;
        if ((this.idValParSac == null && other.idValParSac != null) || (this.idValParSac != null && !this.idValParSac.equals(other.idValParSac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
