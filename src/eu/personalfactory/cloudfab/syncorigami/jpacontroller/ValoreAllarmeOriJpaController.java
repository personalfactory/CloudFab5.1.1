/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreAllarmeOri;
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
 * @author francescodigaudio
 */
public class ValoreAllarmeOriJpaController implements Serializable {

     private Logger log = Logger.getLogger(ValoreAllarmeOriJpaController.class);
  
  public ValoreAllarmeOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;


    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ValoreAllarmeOri valoreAllarmeOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(valoreAllarmeOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ValoreAllarmeOri valoreAllarmeOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            valoreAllarmeOri = em.merge(valoreAllarmeOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = valoreAllarmeOri.getId();
                if (findValoreAllarmeOri(id) == null) {
                    throw new NonexistentEntityException("The valoreAllarmeOri with id " + id + " no longer exists.");
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
            ValoreAllarmeOri valoreAllarmeOri;
            try {
                valoreAllarmeOri = em.getReference(ValoreAllarmeOri.class, id);
                valoreAllarmeOri.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The valoreAllarmeOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(valoreAllarmeOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ValoreAllarmeOri> findValoreAllarmeOriEntities() {
        return findValoreAllarmeOriEntities(true, -1, -1);
    }

    public List<ValoreAllarmeOri> findValoreAllarmeOriEntities(int maxResults, int firstResult) {
        return findValoreAllarmeOriEntities(false, maxResults, firstResult);
    }

     @SuppressWarnings("unchecked")
    private List<ValoreAllarmeOri> findValoreAllarmeOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ValoreAllarmeOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ValoreAllarmeOri findValoreAllarmeOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreAllarmeOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoreAllarmeOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from ValoreAllarmeOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     @SuppressWarnings("unchecked")
    public Collection<ValoreAllarmeOri> findValoreAllarmeOriNew(Date dtUltAgg){
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("ValoreAllarmeOri.findDatiNuovi");
       q.setParameter ("dtAbilitato",dtUltAgg);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }    
}
