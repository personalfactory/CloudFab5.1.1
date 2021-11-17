
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ParametroGlobMacOri;
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
public class ParametroGlobMacOriJpaController implements Serializable {

  private Logger log = Logger.getLogger(ParametroGlobMacOriJpaController.class);
  
  public ParametroGlobMacOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }
  
  public ParametroGlobMacOri findParametroGlobMacOri(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(ParametroGlobMacOri.class, id);
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
   * @param oggetto ParametroGlobMacOri da salvare
   * @author marilisa
   */
  public void merge(ParametroGlobMacOri parametroGlobMacOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = parametroGlobMacOri.getIdParGm();
      if (findParametroGlobMacOri(id) != null) {
        em.merge(parametroGlobMacOri);
      } else {
        em.persist(parametroGlobMacOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  /**
   * Metodo che seleziona tutti i record della tabella parametro_glob_mac_ori
   * @return Collection di ParametroCompProdOri
   */
  @SuppressWarnings("unchecked")
  public Collection<ParametroGlobMacOri> findParametroGlobMacOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ParametroGlobMacOri.findAll");
    
    return q.getResultList();

  }
   
} 