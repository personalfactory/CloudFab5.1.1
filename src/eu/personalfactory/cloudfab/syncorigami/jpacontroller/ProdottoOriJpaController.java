
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
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
public class ProdottoOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(ProdottoOriJpaController.class);
       
    public ProdottoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
   
    public ProdottoOri findProdottoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProdottoOri.class, id);
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
     * @param prodottoOri
   * @author marilisa
   */
  public void merge(ProdottoOri prodottoOri) {
 
    try {
      EntityManager em = getEntityManager();
      em.getTransaction().begin();

      Integer id = prodottoOri.getIdProdotto();
      if (findProdottoOri(id) != null) {
        em.merge(prodottoOri);
      } else {
        em.persist(prodottoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException | IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   /**
   * Metodo che seleziona tutti i record della tabella prodotto_ori
   * @return Collection di ProdottoOri
   */
    @SuppressWarnings("unchecked")
  public Collection<ProdottoOri> findProdottoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ProdottoOri.findAll");
    
    return q.getResultList();

  }
  
}
