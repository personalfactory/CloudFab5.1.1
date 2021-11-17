
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;


import eu.personalfactory.cloudfab.macchina.entity.ValoreProdottoOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
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
public class ValoreProdottoOriJpaController implements Serializable {

     private Logger log = Logger.getLogger(ValoreProdottoOriJpaController.class);
      
    public ValoreProdottoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    public ValoreProdottoOri findValoreProdottoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreProdottoOri.class, id);
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
   * @param oggetto ValoreProdottoOri da salvare
   * @author marilisa
   */
  public void merge(ValoreProdottoOri valoreProdottoOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreProdottoOri.getIdValPr();
      if (findValoreProdottoOri(id) != null) {
        em.merge(valoreProdottoOri);
      } else {
        em.persist(valoreProdottoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  
  
  /**
 * Utilizzato
 * @param valoreProdottoOri
 * @throws NonexistentEntityException
 * @throws Exception 
 */
  public void edit(ValoreProdottoOri valoreProdottoOri) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      valoreProdottoOri = em.merge(valoreProdottoOri);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = valoreProdottoOri.getIdValPr();
        if (findValoreProdottoOri(id) == null) {
          throw new NonexistentEntityException("The valoreParSingMacOri with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
  
  /**
 * Utilizzato
 * @param valoreProdottoOri
 * @throws PreexistingEntityException
 * @throws Exception 
 */
  public void create(ValoreProdottoOri valoreProdottoOri) throws PreexistingEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(valoreProdottoOri);
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (findValoreProdottoOri(valoreProdottoOri.getIdValPr()) != null) {
        throw new PreexistingEntityException("valoreProdottoOri " + valoreProdottoOri + " already exists.", ex);
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }


/**
   * Metodo che seleziona tutti i record della tabella valore_prodotto_ori
   * @return Collection di ValoreProdottoOri
   */
     @SuppressWarnings("unchecked")
  public Collection<ValoreProdottoOri> findValoreProdottoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ValoreProdottoOri.findAll");
    
    return q.getResultList();

  }

}
