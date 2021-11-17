
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.PresaOri;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class PresaOriJpaController implements Serializable {
    
    private Logger log = Logger.getLogger(PresaOriJpaController.class);

    public PresaOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public PresaOri findPresaOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PresaOri.class, id);
        } finally {
            em.close();
        }
    }

  
 //#############################################################################
 //############# METODI INVOCATI DA SYNCORIGAMI ################################
 //#############################################################################
    
    /**
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto PresaOri da salvare
   * @author marilisa
   */
  public void merge(PresaOri presaOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = presaOri.getIdPresa();
      if (findPresaOri(id) != null) {
        em.merge(presaOri);
      } else {
        em.persist(presaOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   /**
   * Metodo che seleziona tutti i record della tabella presa_ori
   * @return Collection di PresaOri
   */
    @SuppressWarnings("unchecked")
  public Collection<PresaOri> findPresaOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("PresaOri.findAll");
    
    return q.getResultList();

  }
}
