
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.MacchinaOri;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class MacchinaOriJpaController implements Serializable {

  private Logger log = Logger.getLogger(MacchinaOriJpaController.class);

  public MacchinaOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
  public MacchinaOri findMacchinaOri(Integer id) {
    EntityManager em = getEntityManager();
    try {
      return em.find(MacchinaOri.class, id);
    } finally {
      em.close();
    }
  }

  
 //#############################################################################
 //############# METODI INVOCATI DA SYNCORIGAMI ################################
 //#############################################################################   
  
  /**
   * Metodo che restituisce l'ultima versione di un dato tipo di aggiornamento della macchina 
   * @param Stringa che indica il tipo aggiornamento(IN/OUT) 
   * @return Un oggetto Integer ovvero l'ultima versione dell'aggiornamento di tipo passato come parametro
   */
  public Integer findLastUpdateVersion(String tipo) {

    EntityManager em = getEntityManager();

    Query q = em.createQuery("SELECT MAX(a.versione) FROM AggiornamentoOri a WHERE a.tipo= :tipo");
    q.setParameter("tipo", tipo);

    try {

      q.getSingleResult();

    } catch (NoResultException ex) {
      log.error("##### Nessun Risultato in findLastUpdateVersion : " +ex.toString());
      throw ex;
    } catch (NonUniqueResultException ex) {
      log.error("##### Versione duplicata in findLastUpdateVersion : " +ex.toString());
      throw ex;
    } catch (Exception e) {
      log.error("##### Eccezione inattesa su findLastUpdateVersion: " + e.toString());
    }

    return (Integer) q.getSingleResult();

  }

  /**
   * Metodo che restituisce il record presente nella tabella macchina 
   * @return Un oggetto MacchinaOri
   */
  public MacchinaOri findMacchinaOri() {
    EntityManager em = getEntityManager();
    MacchinaOri res = null;
    try {

      Query q = em.createNamedQuery("MacchinaOri.findAll");

      @SuppressWarnings("unchecked")
      Collection<MacchinaOri> c = (Collection<MacchinaOri>) q.getResultList();
      //QUESTO CODICE FA SCHIFO!!!!!
      if (c.size() != 1) {
        throw new NonUniqueResultException("MACCHINA NON UNICA O ASSENTE!!!!");
      } else {
        for (MacchinaOri m : c) {
          res = m;
        }
      }
      return res;
     
    } finally {
      em.close();
    }
  }

 /**
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto MacchinaOri da salvare
   * @author marilisa
   */  
  public void merge(MacchinaOri macchinaOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();
      
      Integer id = 0;
      id = macchinaOri.getIdMacchina();
      if (findMacchinaOri(id) != null ) {
          em.merge(macchinaOri);
      } else {
          em.persist(macchinaOri);
      }
      em.getTransaction().commit();
      
    } catch (SecurityException | IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   /**
   * Metodo che seleziona le informazioni della macchina presenti nella tabella macchina_ori
   * @return Collection di MacchinaOri
   */
  @SuppressWarnings("unchecked")
  public Collection<MacchinaOri> findMacchinaOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("MacchinaOri.findAll");
    
    
    return q.getResultList();

  }
}
