
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.MazzettaColSingMacOri;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author francescodigaudio
 */
@SuppressWarnings("serial")
public class MazzettaColSingMacOriJpaController implements Serializable {

    public MazzettaColSingMacOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }  
  
  
 //#############################################################################
 //############# METODI INVOCATI DA SYNCORIGAMI ################################
 //#############################################################################  
  /**
   * Metodo che restituisce le mazzette colorate sing mac nuove 
   * @param data di costruzione dell'ultimo aggiornamento della macchina
   * @return Una collection di MazzettaColSingMacOri
   * Per recuperare i dati nuovi lato macchina da inviare al server 
   * si confronta la data dell'ultimo aggiornamento con il valore del campo dtAbilitato
   * N.B. Questi dati viaggiano in un solo verso ovvero dalla macchina al server
   */
  public Collection<MazzettaColSingMacOri> findMazzettaColSingMacOriNew(Date dtUltAgg){
         
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("MazzettaColSingMacOri.findDatiNuovi");
       q.setParameter ("dtAbilitato",dtUltAgg);
     
      return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }    
  
  /**
   * Metodo che seleziona le maxzzette presenti nella tabella mazzetta_col_sing_mac_ori
   * @return Collection di MazzettaColSingMacOri
   */
  public Collection<MazzettaColSingMacOri> findMazzettaColSingMacOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("MazzettaColSingMacOri.findAll");
    
    return q.getResultList();

  }
  
}
