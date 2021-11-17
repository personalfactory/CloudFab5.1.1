
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParProdOri;
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
public class ValoreParProdOriJpaController implements Serializable {

     private Logger log = Logger.getLogger(ValoreParProdOriJpaController.class);
      
    public ValoreParProdOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    public ValoreParProdOri findValoreParProdOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreParProdOri.class, id);
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
   * @param oggetto ValoreParProdOri da salvare
   * @author marilisa
   */
  public void merge(ValoreParProdOri valoreParProdOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreParProdOri.getIdValParPr();
      if (findValoreParProdOri(id) != null) {
        em.merge(valoreParProdOri);
      } else {
        em.persist(valoreParProdOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  /**
   * Metodo che seleziona tutti i record della tabella valore_par_prod_ori
   * @return Collection di ValoreParProdOri
   */
     @SuppressWarnings("unchecked")
  public Collection<ValoreParProdOri> findValoreParProdOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ValoreParProdOri.findAll");
    
    return q.getResultList();

  }
  
}
