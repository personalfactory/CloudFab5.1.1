/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.CicloProcessoOri;
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

/**
 *
 * @author marilisa
 */
public class CicloProcessoOriJpaController implements Serializable {

  public CicloProcessoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CicloProcessoOri cicloProcessoOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cicloProcessoOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CicloProcessoOri cicloProcessoOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cicloProcessoOri = em.merge(cicloProcessoOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cicloProcessoOri.getId();
                if (findCicloProcessoOri(id) == null) {
                    throw new NonexistentEntityException("The cicloProcessoOri with id " + id + " no longer exists.");
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
            CicloProcessoOri cicloProcessoOri;
            try {
                cicloProcessoOri = em.getReference(CicloProcessoOri.class, id);
                cicloProcessoOri.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cicloProcessoOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(cicloProcessoOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CicloProcessoOri> findCicloProcessoOriEntities() {
        return findCicloProcessoOriEntities(true, -1, -1);
    }

    public List<CicloProcessoOri> findCicloProcessoOriEntities(int maxResults, int firstResult) {
        return findCicloProcessoOriEntities(false, maxResults, firstResult);
    }

  @SuppressWarnings("unchecked")
    private List<CicloProcessoOri> findCicloProcessoOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from CicloProcessoOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CicloProcessoOri findCicloProcessoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CicloProcessoOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getCicloProcessoOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from CicloProcessoOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    
  @SuppressWarnings("unchecked")
    public Collection<CicloProcessoOri> findCicloProcessoOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("CicloProcessoOri.findDatiNuovi");
       q.setParameter ("dtAbilitato",dtUltAgg);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }       
}
