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
@Table(name = "mazzetta_colorata_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MazzettaColorataOri.findAll", query = "SELECT m FROM MazzettaColorataOri m"),
})
public class MazzettaColorataOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_maz_col")
    private Integer idMazCol;
    @Column(name = "quantita")
    private Integer quantita;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @NotNull
    @Column(name = "dt_abilitato")//, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @JoinColumn(name = "id_colore_base", referencedColumnName = "id_colore_base")
    @ManyToOne(optional = false)
    private ColoreBaseOri idColoreBase;
    @JoinColumn(name = "id_colore", referencedColumnName = "id_colore")
    @ManyToOne(optional = false)
    private ColoreOri idColore;
    @JoinColumn(name = "id_mazzetta", referencedColumnName = "id_mazzetta")
    @ManyToOne(optional = false)
    private MazzettaOri idMazzetta;

    public MazzettaColorataOri() {
    }

    public MazzettaColorataOri(Integer idMazCol) {
        this.idMazCol = idMazCol;
    }

    public MazzettaColorataOri(Integer idMazCol, Date dtAbilitato) {
        this.idMazCol = idMazCol;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdMazCol() {
        return idMazCol;
    }

    public void setIdMazCol(Integer idMazCol) {
        this.idMazCol = idMazCol;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
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

    public ColoreBaseOri getIdColoreBase() {
        return idColoreBase;
    }

    public void setIdColoreBase(ColoreBaseOri idColoreBase) {
        this.idColoreBase = idColoreBase;
    }

    public ColoreOri getIdColore() {
        return idColore;
    }

    public void setIdColore(ColoreOri idColore) {
        this.idColore = idColore;
    }

    public MazzettaOri getIdMazzetta() {
        return idMazzetta;
    }

    public void setIdMazzetta(MazzettaOri idMazzetta) {
        this.idMazzetta = idMazzetta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMazCol != null ? idMazCol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MazzettaColorataOri)) {
            return false;
        }
        MazzettaColorataOri other = (MazzettaColorataOri) object;
        if ((this.idMazCol == null && other.idMazCol != null) || (this.idMazCol != null && !this.idMazCol.equals(other.idMazCol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
