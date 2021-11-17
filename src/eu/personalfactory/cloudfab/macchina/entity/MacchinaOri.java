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
@Table(name = "macchina_ori")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "MacchinaOri.findAll", query = "SELECT m FROM MacchinaOri m")
})
public class MacchinaOri implements Serializable {
   
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id_macchina")
  private Integer idMacchina;
  @Size(max = 50)
  @Column(name = "cod_stab")
  private String codStab;
  @Size(max = 50)
  @Column(name = "descri_stab")
  private String descriStab;
  @Size(max = 50)
  @Column(name = "ragso1")
  private String ragso1;
  @Size(max = 50)
  @Column(name = "user_origami")
  private String userOrigami;
  @Size(max = 50)
  @Column(name = "user_server")
  private String userServer;
  @Size(max = 50)
  @Column(name = "pass_origami")
  private String passOrigami;
  @Size(max = 50)
  @Column(name = "pass_server")
  private String passServer;
  
  @Size(max = 255)
  @Column(name = "ftp_user")
  private String ftpUser;
  
  @Size(max = 255)
  @Column(name = "ftp_password")
  private String ftpPassword;
  
  @Size(max = 255)
  @Column(name = "zip_password")
  private String zipPassword;

  @Column(name = "abilitato")
  private Boolean abilitato;

  @NotNull
  @Column(name = "dt_abilitato")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtAbilitato;
  
  public MacchinaOri() {
  }

  public MacchinaOri(Integer idMacchina) {
    this.idMacchina = idMacchina;
  }

  public MacchinaOri(Integer idMacchina, Date dtAbilitato) {
    this.idMacchina = idMacchina;
    this.dtAbilitato = dtAbilitato;
  }

  public Integer getIdMacchina() {
    return idMacchina;
  }

  public void setIdMacchina(Integer idMacchina) {
    this.idMacchina = idMacchina;
  }

  public String getCodStab() {
    return codStab;
  }

  public void setCodStab(String codStab) {
    this.codStab = codStab;
  }

  public String getDescriStab() {
    return descriStab;
  }

  public void setDescriStab(String descriStab) {
    this.descriStab = descriStab;
  }

  public String getRagso1() {
    return ragso1;
  }

  public void setRagso1(String ragso1) {
    this.ragso1 = ragso1;
  }

  public String getUserOrigami() {
    return userOrigami;
  }

  public void setUserOrigami(String userOrigami) {
    this.userOrigami = userOrigami;
  }

  public String getUserServer() {
    return userServer;
  }

  public void setUserServer(String userServer) {
    this.userServer = userServer;
  }

  public String getPassOrigami() {
    return passOrigami;
  }

  public void setPassOrigami(String passOrigami) {
    this.passOrigami = passOrigami;
  }

  public String getPassServer() {
    return passServer;
  }

  public void setPassServer(String passServer) {
    this.passServer = passServer;
  }
  
  public String getFtpPassword() {
    return ftpPassword;
  }

  public void setFtpPassword(String ftpPassword) {
    this.ftpPassword = ftpPassword;
  }

  public String getFtpUser() {
    return ftpUser;
  }

  public void setFtpUser(String ftpUser) {
    this.ftpUser = ftpUser;
  }

  public String getZipPassword() {
    return zipPassword;
  }

  public void setZipPassword(String zipPassword) {
    this.zipPassword = zipPassword;
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
    hash += (idMacchina != null ? idMacchina.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof MacchinaOri)) {
      return false;
    }
    MacchinaOri other = (MacchinaOri) object;
    if ((this.idMacchina == null && other.idMacchina != null) || (this.idMacchina != null && !this.idMacchina.equals(other.idMacchina))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return DTEntityExtStatic.objToString(this);
  }
 
}
