
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ComponenteProdottoOri;
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
public class ComponenteProdottoOriJpaController implements Serializable {
    
    private Logger log = Logger.getLogger(ComponenteProdottoOriJpaController.class);

    public ComponenteProdottoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public ComponenteProdottoOri findComponenteProdottoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComponenteProdottoOri.class, id);
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
   * @param oggetto ProdottoOri da salvare
   * @author marilisa
   */
  public void merge(ComponenteProdottoOri componenteProdottoOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = componenteProdottoOri.getIdCompProd();
      if (findComponenteProdottoOri(id) != null) {
        em.merge(componenteProdottoOri);
      } else {
        em.persist(componenteProdottoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   /**
   * Metodo che seleziona tutti i ComponenteProdottoOri presenti nella tabella componente_prodotto_ori
   * @return Collection di ComponenteProdottoOri
   */
    @SuppressWarnings("unchecked")
  public Collection<ComponenteProdottoOri> findComponenteProdottoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ComponenteProdottoOri.findAll");
    
    return q.getResultList();

  }
  
}
