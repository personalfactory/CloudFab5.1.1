
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreRipristinoOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class ValoreRipristinoOriJpaController implements Serializable {

  private Logger log = Logger.getLogger(ValoreRipristinoOriJpaController.class);
  
  public ValoreRipristinoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }

 
  public void edit(ValoreRipristinoOri valoreRipristinoOri) throws NonexistentEntityException, Exception {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      valoreRipristinoOri = em.merge(valoreRipristinoOri);
      em.getTransaction().commit();
    } catch (Exception ex) {
      String msg = ex.getLocalizedMessage();
      if (msg == null || msg.length() == 0) {
        Integer id = valoreRipristinoOri.getIdValoreRipristino();
        if (findValoreRipristinoOri(id) == null) {
          throw new NonexistentEntityException("The valoreRipristinoOri with id " + id + " no longer exists.");
        }
      }
      throw ex;
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }
  
  public ValoreRipristinoOri findValoreRipristinoOri(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(ValoreRipristinoOri.class, id);
    } finally {
      em.close();
    }
  }    
   
 //#############################################################################
 //############# METODI INVOCATI DA SYNCORIGAMI ################################
 //#############################################################################
   
  
  /**
   * Metodo che restituisce i parametri di ripristino nuovi
   * @param data di costruzione dell'ultimo aggiornamento della macchina
   * @return Una collection di valoriParRipristino
   * Per recuperare i dati nuovi lato macchina da inviare al server 
   * si confronta la data dell'ultimo aggiornamento con il valore del campo dtRegistrato
   * Quando invece si riceve un aggiornamento dal server e si aggiorna un record si utilizza il campo
   * dtAbilitato per indicare la data di aggiornamento del valore 
   */
  
  @SuppressWarnings("unchecked")
   public Collection<ValoreRipristinoOri> findValoreRipristinoNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
     try {
       
       Query q = em.createNamedQuery("ValoreRipristinoOri.findDatiNuovi");
       q.setParameter ("dtRegistrato",dtUltAgg);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }
   
   /**
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto ValoreRipristinoOriNew da salvare
   * @author marilisa
   */
  public void merge(ValoreRipristinoOri valoreRipristinoOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreRipristinoOri.getIdValoreRipristino();
      if (findValoreRipristinoOri(id) != null) {
        em.merge(valoreRipristinoOri);
      } else {
        em.persist(valoreRipristinoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException | IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   /**
   * Metodo che seleziona tutti i record della tabella valore_ripristino_ori
   * @return Collection di ValoreRipristinoOri
   */
  @SuppressWarnings("unchecked")
  public Collection<ValoreRipristinoOri> findValoreRipristinoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("ValoreRipristinoOri.findAll");
    
    return q.getResultList();

  }
   
   
}
