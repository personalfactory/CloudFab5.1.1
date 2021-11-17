/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.utils;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class MachineCredentials implements Comparable {
  
  private static Logger log = Logger.getLogger(MachineCredentials.class);
  

  private Integer idMacchina;
  private Integer lastUpdateVersion;
  private Date lastUpdateDate;
  private String ftpUser;
  private Date newRemoteUpdateTs;
  private String currentUpdateFileName;
  private String currentUpdateFileNameCompletePath;
  private String ftpPassword;
  private String zipPassword;
  private Integer newRemoteUpdateVersion;
//  private String updateType;
//
//  public String getUpdateType() {
//    return updateType;
//  }
//
//  public void setUpdateType(String updateType) {
//    this.updateType = updateType;
//  }
  
  

  /**
   * 
   * @return il precorso locale completo del file di aggiornamento 
   */
  public String getCurrentUpdateFileNameCompletePath() {
    return currentUpdateFileNameCompletePath;
  }
  
  /**
   * Setta il precorso locale completo del file di aggiornamento
   * @param currentUpdateFileNameCompletePath 
   */
  public void setCurrentUpdateFileNameCompletePath(String currentUpdateFileNameCompletePath) {
    this.currentUpdateFileNameCompletePath = currentUpdateFileNameCompletePath;
  }
  
  /**
   * 
   * @return il nome del file corrispondete all'aggiornamento in ingresso corrente 
   */
  public String getCurrentUpdateFileName() {
    return currentUpdateFileName;
  }

  /**
   * Memorizza il nome del file corrente per l'aggiornamento
   * @param currentUpdateFileName 
   */
  public void setCurrentUpdateFileName(String currentUpdateFileName) {
    this.currentUpdateFileName = currentUpdateFileName;
  }
  
  /**
   * 
   * @return ritorna la versione dell'aggiornamento comunicata dalla macchina 
   * nel nome del file 
   */
  public Date getNewRemoteUpdateTs() {
    return newRemoteUpdateTs;
  }

  public void setNewRemoteUpdateTs(Date newRemoteUpdateTs) {
    this.newRemoteUpdateTs = newRemoteUpdateTs;
  }
  
  /**
   * 
   * @return ritorna la data locale dell'aggiornamento comunicata dalla macchina 
   * nel nome del file  
   */
  public Integer getNewRemoteUpdateVersion() {
    return newRemoteUpdateVersion;
  }

  public void setNewRemoteUpdateVersion(Integer newRemoteUpdateVersion) {
    this.newRemoteUpdateVersion = newRemoteUpdateVersion;
  }  

  public Integer getLastUpdateVersion() {
    return lastUpdateVersion;
  }

  public void setLastUpdateVersion(Integer lastUpdateVersion) {
    this.lastUpdateVersion = lastUpdateVersion;
  }

   public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date lastUpdateDate) {
    this.lastUpdateDate = lastUpdateDate;
  }

  public Integer getIdMacchina() {
    return idMacchina;
  }

  public void setIdMacchina(Integer idMacchina) {
    this.idMacchina = idMacchina;
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
  
  @Override
  public String toString() {
    return DTEntityExtStatic.objToString(this);
  }

  @Override
  public int compareTo(Object obj) {
    
    final MachineCredentials other = (MachineCredentials) obj;
    
    if (this.idMacchina < other.idMacchina){
      return -1;
    }
    if (this.idMacchina > other.idMacchina){
      return 1;
    }
    if (this.idMacchina == other.idMacchina){
      //andiamo a controllare il numero di versione
      
      if (this.newRemoteUpdateVersion < other.newRemoteUpdateVersion){
        return -1;
      }
      if (this.newRemoteUpdateVersion > other.newRemoteUpdateVersion){
        return 1;
      }     
    }
    log.error("TROVATI DUE AGGIORNAMENTI DELLA STESSA "
                + "MACCHINA(" + idMacchina + ") CON LA STESSA VERSIONE ( " + 
                newRemoteUpdateVersion + ") !!!");
    return 0;         
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final MachineCredentials other = (MachineCredentials) obj;
    if (this.idMacchina != other.idMacchina && (this.idMacchina == null || !this.idMacchina.equals(other.idMacchina))) {
      return false;
    }
    if ((this.ftpUser == null) ? (other.ftpUser != null) : !this.ftpUser.equals(other.ftpUser)) {
      return false;
    }
    if ((this.ftpPassword == null) ? (other.ftpPassword != null) : !this.ftpPassword.equals(other.ftpPassword)) {
      return false;
    }
    if ((this.zipPassword == null) ? (other.zipPassword != null) : !this.zipPassword.equals(other.zipPassword)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 79 * hash + (this.idMacchina != null ? this.idMacchina.hashCode() : 0);
    hash = 79 * hash + (this.ftpUser != null ? this.ftpUser.hashCode() : 0);
    hash = 79 * hash + (this.ftpPassword != null ? this.ftpPassword.hashCode() : 0);
    hash = 79 * hash + (this.zipPassword != null ? this.zipPassword.hashCode() : 0);
    return hash;
  }
  
  
  
  
  
  
  
}
