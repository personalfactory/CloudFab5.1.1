
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.MazzettaColorataOri;
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
public class MazzettaColorataOriJpaController implements Serializable {
  
private Logger log = Logger.getLogger(MazzettaColorataOriJpaController.class);

  public MazzettaColorataOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }


  public MazzettaColorataOri findMazzettaColorataOri(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(MazzettaColorataOri.class, id);
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
   * @param oggetto MazzettaColorataOri da salvare
   * @author marilisa
   */
  public void merge(MazzettaColorataOri mazzettaColorataOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = mazzettaColorataOri.getIdMazCol();
      if (findMazzettaColorataOri(id) != null) {
        em.merge(mazzettaColorataOri);
      } else {
        em.persist(mazzettaColorataOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  /**
   * Metodo che seleziona le mazzette presenti nella tabella mazzetta_colorata_ori
   * @return Collection di MazzettaColorataOri
   */
  public Collection<MazzettaColorataOri> findMazzettaColorataOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("MazzettaColorataOri.findAll");
    
    return q.getResultList();

  }
}
