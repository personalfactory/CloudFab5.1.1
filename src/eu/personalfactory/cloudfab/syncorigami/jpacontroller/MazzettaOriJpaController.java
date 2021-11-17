
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;

import eu.personalfactory.cloudfab.macchina.entity.MazzettaOri;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class MazzettaOriJpaController implements Serializable {
    
    private Logger log = Logger.getLogger(MazzettaOriJpaController.class);

    public MazzettaOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public MazzettaOri findMazzettaOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MazzettaOri.class, id);
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
   * @param oggetto MazzettaOri da salvare
   * @author marilisa
   */
  public void merge(MazzettaOri mazzettaOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = mazzettaOri.getIdMazzetta();
      if (findMazzettaOri(id) != null) {
        em.merge(mazzettaOri);
      } else {
        em.persist(mazzettaOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
 
  /**
   * Metodo che seleziona le mazzette presenti nella tabella mazzetta_ori
   * @return Collection di MazzettaOri
   */
  public Collection<MazzettaOri> findMazzettaOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("MazzettaOri.findAll");
    
    return q.getResultList();

  }
}
