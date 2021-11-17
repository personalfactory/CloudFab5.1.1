package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "chimica_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChimicaOri.findAll", query = "SELECT c FROM ChimicaOri c")})
public class ChimicaOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_chimica")
    private Integer idChimica;
    @Size(max = 255)
    @Column(name = "cod_chimica")
    private String codChimica;
    @Column(name = "stato_ch")
    private Boolean statoCh;
    @Column(name = "num_bolla")
    private Integer numBolla;
    @Size(max = 50)
    @Column(name = "cod_lotto")
    private String codLotto;
    @Size(max = 255)
    @Column(name = "descri_formula")
    private String descriFormula;
    @Column(name = "dt_bolla")
    @Temporal(TemporalType.DATE)
    private Date dtBolla;
    @NotNull
    @Column(name = "dt_abilitato") //, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;

    public ChimicaOri() {
    }

    public ChimicaOri(Integer idChimica) {
        this.idChimica = idChimica;
    }

    public ChimicaOri(Integer idChimica, Date dtAbilitato) {
        this.idChimica = idChimica;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdChimica() {
        return idChimica;
    }

    public void setIdChimica(Integer idChimica) {
        this.idChimica = idChimica;
    }

    public String getCodChimica() {
        return codChimica;
    }

    public void setCodChimica(String codChimica) {
        this.codChimica = codChimica;
    }

    public Boolean getStatoCh() {
        return statoCh;
    }

    public void setStatoCh(Boolean statoCh) {
        this.statoCh = statoCh;
    }

    public Integer getNumBolla() {
        return numBolla;
    }

    public void setNumBolla(Integer numBolla) {
        this.numBolla = numBolla;
    }

    public String getCodLotto() {
        return codLotto;
    }

    public void setCodLotto(String codLotto) {
        this.codLotto = codLotto;
    }

    public String getDescriFormula() {
        return descriFormula;
    }

    public void setDescriFormula(String descriFormula) {
        this.descriFormula = descriFormula;
    }

    public Date getDtBolla() {
        return dtBolla;
    }

    public void setDtBolla(Date dtBolla) {
        this.dtBolla = dtBolla;
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
        hash += (idChimica != null ? idChimica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChimicaOri)) {
            return false;
        }
        ChimicaOri other = (ChimicaOri) object;
        if ((this.idChimica == null && other.idChimica != null) || (this.idChimica != null && !this.idChimica.equals(other.idChimica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
}
