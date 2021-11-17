
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParCompOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class ValoreParCompOriJpaController implements Serializable {
    private Logger log = Logger.getLogger(ValoreParCompOriJpaController.class);

    public ValoreParCompOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
 * @param valoreParCompOri
 * @throws PreexistingEntityException
 * @throws Exception 
 */
    public void create(ValoreParCompOri valoreParCompOri) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(valoreParCompOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findValoreParCompOri(valoreParCompOri.getIdValComp()) != null) {
                throw new PreexistingEntityException("ValoreParCompOri " + valoreParCompOri + " already exists.", ex);
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
     * @param valoreParCompOri
     * @throws NonexistentEntityException
     * @throws Exception 
     */
    public void edit(ValoreParCompOri valoreParCompOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            valoreParCompOri = em.merge(valoreParCompOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = valoreParCompOri.getIdValComp();
                if (findValoreParCompOri(id) == null) {
                    throw new NonexistentEntityException("The valoreParCompOri with id " + id + " no longer exists.");
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
     * @param id
     * @return 
     */
    public ValoreParCompOri findValoreParCompOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreParCompOri.class, id);
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
   * @param oggetto ValoreParCompOri da salvare
   * @author marilisa
   */
  public void merge(ValoreParCompOri valoreParCompOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreParCompOri.getIdValComp();
      if (findValoreParCompOri(id) != null) {
        em.merge(valoreParCompOri);
      } else {
        em.persist(valoreParCompOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  
  /**
   * Metodo che restituisce i parametri componeneti nuovi
     * @param dtUltAgg
   * @param data di costruzione dell'ultimo aggiornamento della macchina
   * @return Una collection di valori singola macchina
   * Per recuperare i dati nuovi lato macchina da inviare al server 
   * si confronta la data dell'ultimo aggiornamento con il valore del campo dtModificaMac
   * Quando invece si riceve un valore dal server e si aggiorna in tabella si utilizza il campo
   * dtAbilitato per indicare la data di aggiornamento dal server del valore 
   */
    @SuppressWarnings("unchecked")
   public Collection<ValoreParCompOri> findValoreParCompOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
   
    Query q = em.createNamedQuery("ValoreParCompOri.findDatiNuovi");
    q.setParameter ("dtModificaMac",dtUltAgg);
    try {
       
       q.getResultList();

    } catch (NoResultException nre) {
      log.error("##### Nessun Risultato in findLastUpdateVersion");
      throw nre;
    } catch (Exception e) {
      log.error("##### eccezione inattesa su findLastUpdateVersion: " + e.toString());
    }

    return  q.getResultList();
    
     
  }    
   
   
   /**
   * Metodo che seleziona tutti i record della tabella valore_par_comp_ori
   * @return Collection di ValoreParCompOri
   */
    @SuppressWarnings("unchecked")
  public Collection<ValoreParCompOri> findValoreParCompOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ValoreParCompOri.findAll");
    
    return q.getResultList();

  }
}
