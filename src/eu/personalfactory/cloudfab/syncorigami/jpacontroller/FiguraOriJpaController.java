/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.FiguraOri;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import eu.personalfactory.cloudfab.macchina.entity.FiguraTipoOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class FiguraOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(FiguraOriJpaController.class);
    
    
    public FiguraOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
    this.utx = utx;
    this.emf = emf;
  }
  private UserTransaction utx = null;
  private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FiguraOri figuraOri) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FiguraTipoOri idFiguraTipo = figuraOri.getIdFiguraTipo();
            if (idFiguraTipo != null) {
                idFiguraTipo = em.getReference(idFiguraTipo.getClass(), idFiguraTipo.getIdFiguraTipo());
                figuraOri.setIdFiguraTipo(idFiguraTipo);
            }
            em.persist(figuraOri);
            if (idFiguraTipo != null) {
                idFiguraTipo.getFiguraOriCollection().add(figuraOri);
                idFiguraTipo = em.merge(idFiguraTipo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFiguraOri(figuraOri.getIdFigura()) != null) {
                throw new PreexistingEntityException("FiguraOri " + figuraOri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FiguraOri figuraOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FiguraOri persistentFiguraOri = em.find(FiguraOri.class, figuraOri.getIdFigura());
            FiguraTipoOri idFiguraTipoOld = persistentFiguraOri.getIdFiguraTipo();
            FiguraTipoOri idFiguraTipoNew = figuraOri.getIdFiguraTipo();
            if (idFiguraTipoNew != null) {
                idFiguraTipoNew = em.getReference(idFiguraTipoNew.getClass(), idFiguraTipoNew.getIdFiguraTipo());
                figuraOri.setIdFiguraTipo(idFiguraTipoNew);
            }
            figuraOri = em.merge(figuraOri);
            if (idFiguraTipoOld != null && !idFiguraTipoOld.equals(idFiguraTipoNew)) {
                idFiguraTipoOld.getFiguraOriCollection().remove(figuraOri);
                idFiguraTipoOld = em.merge(idFiguraTipoOld);
            }
            if (idFiguraTipoNew != null && !idFiguraTipoNew.equals(idFiguraTipoOld)) {
                idFiguraTipoNew.getFiguraOriCollection().add(figuraOri);
                idFiguraTipoNew = em.merge(idFiguraTipoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = figuraOri.getIdFigura();
                if (findFiguraOri(id) == null) {
                    throw new NonexistentEntityException("The figuraOri with id " + id + " no longer exists.");
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
            FiguraOri figuraOri;
            try {
                figuraOri = em.getReference(FiguraOri.class, id);
                figuraOri.getIdFigura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The figuraOri with id " + id + " no longer exists.", enfe);
            }
            FiguraTipoOri idFiguraTipo = figuraOri.getIdFiguraTipo();
            if (idFiguraTipo != null) {
                idFiguraTipo.getFiguraOriCollection().remove(figuraOri);
                idFiguraTipo = em.merge(idFiguraTipo);
            }
            em.remove(figuraOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FiguraOri> findFiguraOriEntities() {
        return findFiguraOriEntities(true, -1, -1);
    }

    public List<FiguraOri> findFiguraOriEntities(int maxResults, int firstResult) {
        return findFiguraOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<FiguraOri> findFiguraOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from FiguraOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FiguraOri findFiguraOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FiguraOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getFiguraOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from FiguraOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void merge(FiguraOri figuraOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = figuraOri.getIdFigura();
      if (findFiguraOri(id) != null) {
        em.merge(figuraOri);
      } else {
        em.persist(figuraOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
    
}
