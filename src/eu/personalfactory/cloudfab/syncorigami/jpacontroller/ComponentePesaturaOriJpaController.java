/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
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
public class ComponentePesaturaOriJpaController implements Serializable {

    
    private Logger log = Logger.getLogger(ComponentePesaturaOriJpaController.class);
    
    
    public ComponentePesaturaOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComponentePesaturaOri componentePesaturaOri) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(componentePesaturaOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComponentePesaturaOri(componentePesaturaOri.getId()) != null) {
                throw new PreexistingEntityException("ComponentePesaturaOri " + componentePesaturaOri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComponentePesaturaOri componentePesaturaOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            componentePesaturaOri = em.merge(componentePesaturaOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = componentePesaturaOri.getId();
                if (findComponentePesaturaOri(id) == null) {
                    throw new NonexistentEntityException("The componentePesaturaOri with id " + id + " no longer exists.");
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
            ComponentePesaturaOri componentePesaturaOri;
            try {
                componentePesaturaOri = em.getReference(ComponentePesaturaOri.class, id);
                componentePesaturaOri.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The componentePesaturaOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(componentePesaturaOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComponentePesaturaOri> findComponentePesaturaOriEntities() {
        return findComponentePesaturaOriEntities(true, -1, -1);
    }

    public List<ComponentePesaturaOri> findComponentePesaturaOriEntities(int maxResults, int firstResult) {
        return findComponentePesaturaOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<ComponentePesaturaOri> findComponentePesaturaOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ComponentePesaturaOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ComponentePesaturaOri findComponentePesaturaOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComponentePesaturaOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getComponentePesaturaOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from ComponentePesaturaOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    public void merge(ComponentePesaturaOri componentePesaturaOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = componentePesaturaOri.getId();
      if (findComponentePesaturaOri(id) != null) {
        em.merge(componentePesaturaOri);
      } else {
        em.persist(componentePesaturaOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
    
}
