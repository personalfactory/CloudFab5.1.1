
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;
 
import eu.personalfactory.cloudfab.macchina.entity.ProcessoOri;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class ProcessoOriJpaController implements Serializable {

  public ProcessoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
   * Metodo che restituiscei i processi nuovi
     * @param dtUltAgg
   * @return Una collection di processi
   * Per recuperare i processi nuovi lato macchina da inviare al server 
   * si confronta la data dell'ultimo aggiornamento con il valore del campo dtProduzione
   * N.B. Questi dati viaggiano in un solo verso ovvero dalla macchina al server
   */
  
  @SuppressWarnings("unchecked")
   public Collection<ProcessoOri> findProcessoOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("ProcessoOri.findDatiNuovi");
       q.setParameter ("dtProduzione",dtUltAgg);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }       
    /**
   * Metodo che seleziona tutti i record della tabella processo_ori
   * @return Collection di ProcessoOri
   */
  @SuppressWarnings("unchecked")
  public Collection<ProcessoOri> findProcessoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ProcessoOri.findAll");
    
    return q.getResultList();

  }
}
