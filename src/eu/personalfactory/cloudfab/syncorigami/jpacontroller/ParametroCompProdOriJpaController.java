
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ParametroCompProdOri;
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
public class ParametroCompProdOriJpaController implements Serializable {
    
    private Logger log = Logger.getLogger(ParametroCompProdOriJpaController.class);

    public ParametroCompProdOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public ParametroCompProdOri findParametroCompProdOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ParametroCompProdOri.class, id);
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
   * @param oggetto ParametroCompProd da salvare
   * @author marilisa
   */
  public void merge(ParametroCompProdOri parametroCompProdOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = parametroCompProdOri.getIdParComp();
      if (findParametroCompProdOri(id) != null) {
        em.merge(parametroCompProdOri);
      } else {
        em.persist(parametroCompProdOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException | IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  /**
   * Metodo che seleziona tutti i record della tabella parametro_comp_prod_ori
   * @return Collection di ParametroCompProdOri
   */
    @SuppressWarnings("unchecked")
  public Collection<ParametroCompProdOri> findParametroCompProdOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ParametroCompProdOri.findAll");
    
    return q.getResultList();

  }
}
