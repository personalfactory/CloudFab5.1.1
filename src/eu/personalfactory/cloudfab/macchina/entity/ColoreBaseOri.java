package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity; 
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "colore_base_ori")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ColoreBaseOri.findAll", query = "SELECT c FROM ColoreBaseOri c"),
  @NamedQuery(name = "ColoreBaseOri.findAllAbilitato", query = "SELECT c FROM ColoreBaseOri c WHERE c.abilitato=1")
})
public class ColoreBaseOri implements Serializable {
    
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id_colore_base")
  private Integer idColoreBase;
  @Size(max = 50)
  @Column(name = "nome_colore_base")
  private String nomeColoreBase;
  @Size(max = 50)
  @Column(name = "cod_colore_base")
  private String codColoreBase;
  @Column(name = "costo_colore_base")
  private Double costoColoreBase;
  @Column(name = "toll_perc")
  private Double tollPerc;
  @Column(name = "abilitato")
  private Boolean abilitato;
  @NotNull
  @Column(name = "dt_abilitato")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtAbilitato;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idColoreBase")
  private Collection<MazzettaColorataOri> mazzettaColorataOriCollection;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idColoreBase")
  private Collection<MazzettaColSingMacOri> mazzettaColSingMacOriCollection;

  public ColoreBaseOri() {
  }

  public ColoreBaseOri(Integer idColoreBase) {
    this.idColoreBase = idColoreBase;
  }

  public ColoreBaseOri(Integer idColoreBase, Date dtAbilitato) {
    this.idColoreBase = idColoreBase;
    this.dtAbilitato = dtAbilitato;
  }

  public Integer getIdColoreBase() {
    return idColoreBase;
  }

  public void setIdColoreBase(Integer idColoreBase) {
    this.idColoreBase = idColoreBase;
  }

  public String getNomeColoreBase() {
    return nomeColoreBase;
  }

  public void setNomeColoreBase(String nomeColoreBase) {
    this.nomeColoreBase = nomeColoreBase;
  }

  public String getCodColoreBase() {
    return codColoreBase;
  }

  public void setCodColoreBase(String codColoreBase) {
    this.codColoreBase = codColoreBase;
  }

  public Double getCostoColoreBase() {
    return costoColoreBase;
  }

  public void setCostoColoreBase(Double costoColoreBase) {
    this.costoColoreBase = costoColoreBase;
  }

  public Double getTollPerc() {
    return tollPerc;
  }

  public void setTollPerc(Double tollPerc) {
    this.tollPerc = tollPerc;
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
  public Collection<MazzettaColorataOri> getMazzettaColorataOriCollection() {
    return mazzettaColorataOriCollection;
  }

  public void setMazzettaColorataOriCollection(Collection<MazzettaColorataOri> mazzettaColorataOriCollection) {
    this.mazzettaColorataOriCollection = mazzettaColorataOriCollection;
  }

  @XmlTransient
  public Collection<MazzettaColSingMacOri> getMazzettaColSingMacOriCollection() {
    return mazzettaColSingMacOriCollection;
  }

  public void setMazzettaColSingMacOriCollection(Collection<MazzettaColSingMacOri> mazzettaColSingMacOriCollection) {
    this.mazzettaColSingMacOriCollection = mazzettaColSingMacOriCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idColoreBase != null ? idColoreBase.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ColoreBaseOri)) {
      return false;
    }
    ColoreBaseOri other = (ColoreBaseOri) object;
    if ((this.idColoreBase == null && other.idColoreBase != null) || (this.idColoreBase != null && !this.idColoreBase.equals(other.idColoreBase))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return DTEntityExtStatic.objToString(this);
  }

    
}
