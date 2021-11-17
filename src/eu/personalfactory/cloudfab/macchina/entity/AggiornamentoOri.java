package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Collection;
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
@Table(name = "aggiornamento_ori")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AggiornamentoOri.findAll", query = "SELECT a FROM AggiornamentoOri a")
})
public class AggiornamentoOri implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  private Integer id;
  @Size(max = 10)
  @Column(name = "tipo")
  private String tipo;
  @NotNull
  @Column(name = "dt_aggiornamento")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtAggiornamento;
  @Size(max = 255)
  @Column(name = "nome_file")
  private String nomeFile;
  @Column(name = "versione")
  private Integer versione;

  @Transient
  private Collection daInserire;
  @Transient 
  private Collection daCancellare;

  public Collection getDaCancellare() {
    return daCancellare;
  }

  public void setDaCancellare(Collection daCancellare) {
    this.daCancellare = daCancellare;
  }

  public Collection getDaInserire() {
    return daInserire;
  }

  public void setDaInserire(Collection daInserire) {
    this.daInserire = daInserire;
  }
  
  public AggiornamentoOri() {
  }

  public AggiornamentoOri(Integer id) {
    this.id = id;
  }

  public AggiornamentoOri(Integer id, Date dtAggiornamento) {
    this.id = id;
    this.dtAggiornamento = dtAggiornamento;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public Date getDtAggiornamento() {
    return dtAggiornamento;
  }

  public void setDtAggiornamento(Date dtAggiornamento) {
    this.dtAggiornamento = dtAggiornamento;
  }

  public String getNomeFile() {
    return nomeFile;
  }

  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public Integer getVersione() {
    return versione;
  }

  public void setVersione(Integer versione) {
    this.versione = versione;
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
    if (!(object instanceof AggiornamentoOri)) {
      return false;
    }
    AggiornamentoOri other = (AggiornamentoOri) object;
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
