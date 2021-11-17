
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ColoreOri;
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
public class ColoreOriJpaController implements Serializable {
    
    private Logger log = Logger.getLogger(ColoreOriJpaController.class);
    
    public ColoreOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    /**
     * Utilizzato
     * @param id
     * @return 
     */
    public ColoreOri findColoreOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ColoreOri.class, id);
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
   * @param oggetto ColoreOri da salvare
   * @author marilisa
   */
  public void merge(ColoreOri coloreOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = coloreOri.getIdColore();
      if (findColoreOri(id) != null) {
        em.merge(coloreOri);
      } else {
        em.persist(coloreOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   /**
   * Metodo che seleziona tutti i colori presenti nella tabella colore_ori
   * @return Collection di ColoreOri
   */
  public Collection<ColoreOri> findColoreOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ColoreOri.findAll");
    
    return q.getResultList();

  }
}
