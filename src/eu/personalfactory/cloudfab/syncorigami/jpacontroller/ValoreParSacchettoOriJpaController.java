
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParSacchettoOri;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author francescodigaudio
 */
@SuppressWarnings("serial")
public class ValoreParSacchettoOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(ValoreParSacchettoOriJpaController.class);
    
    public ValoreParSacchettoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ValoreParSacchettoOri findValoreParSacchettoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreParSacchettoOri.class, id);
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
   * @param oggetto ValoreParSacchettoOri da salvare
   * @author marilisa
   */
  public void merge(ValoreParSacchettoOri valoreParSacchettoOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreParSacchettoOri.getIdValParSac();
      if (findValoreParSacchettoOri(id) != null) {
        em.merge(valoreParSacchettoOri);
      } else {
        em.persist(valoreParSacchettoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  /**
   * Metodo che seleziona tutti i record della tabella valore_par_sacchetto_ori
   * @return Collection di ValoreParSacchettoOri
   */
    @SuppressWarnings("unchecked")
  public Collection<ValoreParSacchettoOri> findValoreParSacchettoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ValoreParSacchettoOri.findAll");
    
    return q.getResultList();

  }
  
}
 