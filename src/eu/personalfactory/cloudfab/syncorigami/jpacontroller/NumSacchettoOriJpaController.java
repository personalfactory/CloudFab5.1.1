
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.NumSacchettoOri;
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
public class NumSacchettoOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(NumSacchettoOriJpaController.class);
    
    public NumSacchettoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public NumSacchettoOri findNumSacchettoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NumSacchettoOri.class, id);
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
   * @param oggetto NumSacchettoOri da salvare
   * @author marilisa
   */
  public void merge(NumSacchettoOri numSacchettoOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = numSacchettoOri.getIdNumSac();
      if (findNumSacchettoOri(id) != null) {
        em.merge(numSacchettoOri);
      } else {
        em.persist(numSacchettoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  
  /**
   * Metodo che seleziona tutti i record della tabella num_sacchetto_ori
   * @return Collection di NumSacchettoOri
   */
    @SuppressWarnings("unchecked")
  public Collection<NumSacchettoOri> findNumSacchettoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("NumSacchettoOri.findAll");
    
    return q.getResultList();

  }
 }
