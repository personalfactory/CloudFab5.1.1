/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.OrdineSingMacOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class OrdineSingMacOriJpaController implements Serializable {

   private Logger log = Logger.getLogger(OrdineSingMacOriJpaController.class);

    public OrdineSingMacOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;


    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrdineSingMacOri ordineSingMacOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ordineSingMacOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrdineSingMacOri ordineSingMacOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ordineSingMacOri = em.merge(ordineSingMacOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ordineSingMacOri.getIdOrdineSm();
                if (findOrdineSingMacOri(id) == null) {
                    throw new NonexistentEntityException("The ordineSingMacOri with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrdineSingMacOri ordineSingMacOri;
            try {
                ordineSingMacOri = em.getReference(OrdineSingMacOri.class, id);
                ordineSingMacOri.getIdOrdineSm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ordineSingMacOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(ordineSingMacOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrdineSingMacOri> findOrdineSingMacOriEntities() {
        return findOrdineSingMacOriEntities(true, -1, -1);
    }

    public List<OrdineSingMacOri> findOrdineSingMacOriEntities(int maxResults, int firstResult) {
        return findOrdineSingMacOriEntities(false, maxResults, firstResult);
    }

   @SuppressWarnings("unchecked")
    private List<OrdineSingMacOri> findOrdineSingMacOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from OrdineSingMacOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public OrdineSingMacOri findOrdineSingMacOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrdineSingMacOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrdineSingMacOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from OrdineSingMacOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
   // ############################################################################
    /**
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto OrdineSingMacOri da salvare
   * @author marilisa
   */
  public void merge(OrdineSingMacOri ordineSingMacOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = ordineSingMacOri.getIdOrdineSm();
      if (findOrdineSingMacOri(id) != null) {
        em.merge(ordineSingMacOri);
      } else {
        em.persist(ordineSingMacOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
  
   @SuppressWarnings("unchecked")
  public Collection<OrdineSingMacOri> findOrdineSingMacOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("OrdineSingMacOri.findDatiNuovi");
       q.setParameter ("dtProduzione",dtUltAgg);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }       
  
  
    
}
