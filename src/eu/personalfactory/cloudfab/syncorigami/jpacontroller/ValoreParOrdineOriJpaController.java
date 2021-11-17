/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParOrdineOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParOrdineOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.io.Serializable;
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
public class ValoreParOrdineOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(ValoreParOrdineOriJpaController.class);
      
    public ValoreParOrdineOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValoreParOrdineOri valoreParOrdineOri) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(valoreParOrdineOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findValoreParOrdineOri(valoreParOrdineOri.getId()) != null) {
                throw new PreexistingEntityException("ValoreParOrdineOri " + valoreParOrdineOri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValoreParOrdineOri valoreParOrdineOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            valoreParOrdineOri = em.merge(valoreParOrdineOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = valoreParOrdineOri.getId();
                if (findValoreParOrdineOri(id) == null) {
                    throw new NonexistentEntityException("The valoreParOrdineOri with id " + id + " no longer exists.");
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
            ValoreParOrdineOri valoreParOrdineOri;
            try {
                valoreParOrdineOri = em.getReference(ValoreParOrdineOri.class, id);
                valoreParOrdineOri.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valoreParOrdineOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(valoreParOrdineOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ValoreParOrdineOri> findValoreParOrdineOriEntities() {
        return findValoreParOrdineOriEntities(true, -1, -1);
    }

    public List<ValoreParOrdineOri> findValoreParOrdineOriEntities(int maxResults, int firstResult) {
        return findValoreParOrdineOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<ValoreParOrdineOri> findValoreParOrdineOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ValoreParOrdineOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ValoreParOrdineOri findValoreParOrdineOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreParOrdineOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoreParOrdineOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from ValoreParOrdineOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto ValoreParOrdineOri da salvare
   * @author marilisa
   */
  public void merge(ValoreParOrdineOri valoreParOrdineOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = valoreParOrdineOri.getId();
      if (findValoreParOrdineOri(id) != null) {
        em.merge(valoreParOrdineOri);
      } else {
        em.persist(valoreParOrdineOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
}
