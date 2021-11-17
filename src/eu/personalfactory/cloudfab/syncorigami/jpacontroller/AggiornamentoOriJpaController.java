
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoOri;
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
public class AggiornamentoOriJpaController implements Serializable {

  private Logger log = Logger.getLogger(AggiornamentoOriJpaController.class);

  public AggiornamentoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
 * @param aggiornamentoOri 
 */
  public void create(AggiornamentoOri aggiornamentoOri) {
    EntityManager em = null;
    try {
      em = getEntityManager();
      em.getTransaction().begin();
      em.persist(aggiornamentoOri);
      em.getTransaction().commit();
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

//  public AggiornamentoOri findAggiornamentoOri(Integer id) {
//    EntityManager em = getEntityManager();
//    try {
//      return em.find(AggiornamentoOri.class, id);
//    } finally {
//      em.close();
//    }
//  }

  
  //############################################################################
  //################### METODI INVOCATI DA SYNCORIGAMI #########################
  //############################################################################
  
  
  /**
   * Metodo che restituisce la data dell'ultimo aggiornamento effettuato dalla macchina
   * @param Stringa che indica il tipo di aggiornamento (IN/OUT)
   * @return data di costruzione dell'ultimo aggiornamento
    */
  public Date recuperaDtUltimoAggiornamento(String tipo) {

    EntityManager em = getEntityManager();

    Query q = em.createQuery("SELECT MAX(a.dtAggiornamento) FROM AggiornamentoOri a WHERE a.tipo= :tipo )");
    q.setParameter("tipo", tipo);

    try {

      q.getSingleResult();

    } catch (NoResultException ex) {
      log.error("##### Nessun Risultato in recuperaDtUltimoAggiornamento : "+ex.toString());
      throw ex;
    } catch (NonUniqueResultException ex) {
      log.error("##### Data ultimo aggiornamento duplicata in recuperaDtUltimoAggiornamento : "+ex.toString());
      throw ex;
    }

    return (Date) q.getSingleResult();
  }

  /**
   * Metodo che restituisce l'ultima versione di un dato tipo 
   * di aggiornamentoOri della macchina 
   * @param Stringa che indica il tipo aggiornamento(IN/OUT) 
   * @return Un oggetto Integer ovvero l'ultima versione dell'aggiornamentoOri
   * 
   */
  //TODO : Gestire il caso del primo aggiornamento
  
  public Integer findLastUpdateVersion(String tipo) {

    EntityManager em = getEntityManager();

    Query q = em.createQuery("SELECT MAX(a.versione) FROM AggiornamentoOri a WHERE  a.tipo= :tipo");
    q.setParameter("tipo", tipo);

    try {

      q.getSingleResult();

    } catch (NoResultException ex) {
      log.error("##### Nessun Risultato in findLastUpdateVersion");
      //Se non si  
      throw ex;
    } catch (NonUniqueResultException ex) {
      log.error("##### Versione duplicata in findLastUpdateVersion");
      throw ex;
    } catch (Exception e) {
      log.error("##### eccezione inattesa su findLastUpdateVersion: " + e.toString());
    }

    return (Integer) q.getSingleResult();

  }
  
  /**
   * Metodo che seleziona tutti gli aggiornamenti presenti nella tabella aggiornamento_ori
   * @return Collection di AggiornamentoOri
   */
  @SuppressWarnings("unchecked")
  public Collection<AggiornamentoOri> findAggiornamentoOriAll() {

    EntityManager em = getEntityManager();

    Query q = em.createNamedQuery("AggiornamentoOri.findAll");
    
    return q.getResultList();

  }

}//End
