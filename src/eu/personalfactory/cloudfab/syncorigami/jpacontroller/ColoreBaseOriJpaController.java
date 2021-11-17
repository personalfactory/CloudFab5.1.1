
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ColoreBaseOri;
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
public class ColoreBaseOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(ColoreBaseOriJpaController.class);

    public ColoreBaseOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public ColoreBaseOri findColoreBaseOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ColoreBaseOri.class, id);
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
   * @param oggetto ColoreBaseOri da salvare
   * @author marilisa
   */
  public void merge(ColoreBaseOri coloreBaseOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = coloreBaseOri.getIdColoreBase();
      if (findColoreBaseOri(id) != null) {
        em.merge(coloreBaseOri);
      } else {
        em.persist(coloreBaseOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  
  /**
   * Metodo che seleziona tutti i colori base presenti nella tabella colore_base_ori
   * @return Collection di ColoreBaseOri
   */
  public Collection<ColoreBaseOri> findColoreBaseOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ColoreBaseOri.findAll");
    
    return q.getResultList();

  }
    
    
}
