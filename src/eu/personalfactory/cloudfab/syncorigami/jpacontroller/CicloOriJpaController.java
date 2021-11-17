/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.CicloOri;
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
public class CicloOriJpaController implements Serializable {

    public CicloOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;
  
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CicloOri cicloOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cicloOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CicloOri cicloOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cicloOri = em.merge(cicloOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cicloOri.getIdCiclo();
                if (findCicloOri(id) == null) {
                    throw new NonexistentEntityException("The cicloOri with id " + id + " no longer exists.");
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
            CicloOri cicloOri;
            try {
                cicloOri = em.getReference(CicloOri.class, id);
                cicloOri.getIdCiclo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cicloOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(cicloOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CicloOri> findCicloOriEntities() {
        return findCicloOriEntities(true, -1, -1);
    }

    public List<CicloOri> findCicloOriEntities(int maxResults, int firstResult) {
        return findCicloOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<CicloOri> findCicloOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from CicloOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CicloOri findCicloOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CicloOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getCicloOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from CicloOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public Collection<CicloOri> findCicloOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("CicloOri.findDatiNuovi");
       q.setParameter ("dtAbilitato",dtUltAgg);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }
    
    
}
