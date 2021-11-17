
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParSingMacOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class ValoreParSingMacOriJpaController implements Serializable {

  private Logger log = Logger.getLogger(ValoreParSingMacOriJpaController.class);
  
  public ValoreParSingMacOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
 * @param valoreParSingMacOri
 * @throws PreexistingEntityException
 * @throws Exception 
 */
  public void create(ValoreParSingMacOri valoreParSingMacOri) throws PreexistingEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(valoreParSingMacOri);
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (findValoreParSingMacOri(valoreParSingMacOri.getIdValParSm()) != null) {
        throw new PreexistingEntityException("ValoreParSingMacOri " + valoreParSingMacOri + " already exists.", ex);
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
 * @param valoreParSingMacOri
 * @throws NonexistentEntityException
 * @throws Exception 
 */
  public void edit(ValoreParSingMacOri valoreParSingMacOri) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      valoreParSingMacOri = em.merge(valoreParSingMacOri);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = valoreParSingMacOri.getIdValParSm();
        if (findValoreParSingMacOri(id) == null) {
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

  

  public ValoreParSingMacOri findValoreParSingMacOri(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(ValoreParSingMacOri.class, id);
    } finally {
      em.close();
    }
  }
   
  
 //#############################################################################
 //############# METODI INVOCATI DA SYNCORIGAMI ################################
 //#############################################################################
   /**
   * Metodo che restituisce i parametri sing mac nuovi
   * @param data di costruzione dell'ultimo aggiornamento della macchina
   * @return Una collection di valori singola macchina
   * Per recuperare i dati nuovi lato macchina da inviare al server 
   * si confronta la data dell'ultimo aggiornamento con il valore del campo dtModificaMac
   * Quando invece si riceve un valore dal server e si aggiorna in tabella si utilizza il campo
   * dtAbilitato per indicare la data di aggiornamento dal server del valore 
   */
  @SuppressWarnings("unchecked")
   public Collection<ValoreParSingMacOri> findValoreParSingMacOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
   
    Query q = em.createNamedQuery("ValoreParSingMacOri.findDatiNuovi");
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
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto ValoreParSingMacOri da salvare
   * @author marilisa
   */
  public void merge(ValoreParSingMacOri valoreParSingMacOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreParSingMacOri.getIdValParSm();
      if (findValoreParSingMacOri(id) != null) {
        em.merge(valoreParSingMacOri);
      } else {
        em.persist(valoreParSingMacOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
  /**
   * Metodo che seleziona tutti i record della tabella valore_par_sing_mac_ori
   * @return Collection di ValoreParSingMacOri
   */
  @SuppressWarnings("unchecked")
  public Collection<ValoreParSingMacOri> findValoreParSingMacOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ValoreParSingMacOri.findAll");
    
    return q.getResultList();

  }
}
