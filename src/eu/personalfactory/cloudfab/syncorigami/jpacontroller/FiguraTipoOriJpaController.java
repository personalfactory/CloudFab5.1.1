/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import eu.personalfactory.cloudfab.macchina.entity.FiguraOri;
import eu.personalfactory.cloudfab.macchina.entity.FiguraTipoOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class FiguraTipoOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(FiguraTipoOriJpaController.class);

    public FiguraTipoOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FiguraTipoOri figuraTipoOri) throws PreexistingEntityException, Exception {
        if (figuraTipoOri.getFiguraOriCollection() == null) {
            figuraTipoOri.setFiguraOriCollection(new ArrayList<FiguraOri>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<FiguraOri> attachedFiguraOriCollection = new ArrayList<FiguraOri>();
            for (FiguraOri figuraOriCollectionFiguraOriToAttach : figuraTipoOri.getFiguraOriCollection()) {
                figuraOriCollectionFiguraOriToAttach = em.getReference(figuraOriCollectionFiguraOriToAttach.getClass(), figuraOriCollectionFiguraOriToAttach.getIdFigura());
                attachedFiguraOriCollection.add(figuraOriCollectionFiguraOriToAttach);
            }
            figuraTipoOri.setFiguraOriCollection(attachedFiguraOriCollection);
            em.persist(figuraTipoOri);
            for (FiguraOri figuraOriCollectionFiguraOri : figuraTipoOri.getFiguraOriCollection()) {
                FiguraTipoOri oldIdFiguraTipoOfFiguraOriCollectionFiguraOri = figuraOriCollectionFiguraOri.getIdFiguraTipo();
                figuraOriCollectionFiguraOri.setIdFiguraTipo(figuraTipoOri);
                figuraOriCollectionFiguraOri = em.merge(figuraOriCollectionFiguraOri);
                if (oldIdFiguraTipoOfFiguraOriCollectionFiguraOri != null) {
                    oldIdFiguraTipoOfFiguraOriCollectionFiguraOri.getFiguraOriCollection().remove(figuraOriCollectionFiguraOri);
                    oldIdFiguraTipoOfFiguraOriCollectionFiguraOri = em.merge(oldIdFiguraTipoOfFiguraOriCollectionFiguraOri);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFiguraTipoOri(figuraTipoOri.getIdFiguraTipo()) != null) {
                throw new PreexistingEntityException("FiguraTipoOri " + figuraTipoOri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FiguraTipoOri figuraTipoOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FiguraTipoOri persistentFiguraTipoOri = em.find(FiguraTipoOri.class, figuraTipoOri.getIdFiguraTipo());
            Collection<FiguraOri> figuraOriCollectionOld = persistentFiguraTipoOri.getFiguraOriCollection();
            Collection<FiguraOri> figuraOriCollectionNew = figuraTipoOri.getFiguraOriCollection();
            Collection<FiguraOri> attachedFiguraOriCollectionNew = new ArrayList<FiguraOri>();
            for (FiguraOri figuraOriCollectionNewFiguraOriToAttach : figuraOriCollectionNew) {
                figuraOriCollectionNewFiguraOriToAttach = em.getReference(figuraOriCollectionNewFiguraOriToAttach.getClass(), figuraOriCollectionNewFiguraOriToAttach.getIdFigura());
                attachedFiguraOriCollectionNew.add(figuraOriCollectionNewFiguraOriToAttach);
            }
            figuraOriCollectionNew = attachedFiguraOriCollectionNew;
            figuraTipoOri.setFiguraOriCollection(figuraOriCollectionNew);
            figuraTipoOri = em.merge(figuraTipoOri);
            for (FiguraOri figuraOriCollectionOldFiguraOri : figuraOriCollectionOld) {
                if (!figuraOriCollectionNew.contains(figuraOriCollectionOldFiguraOri)) {
                    figuraOriCollectionOldFiguraOri.setIdFiguraTipo(null);
                    figuraOriCollectionOldFiguraOri = em.merge(figuraOriCollectionOldFiguraOri);
                }
            }
            for (FiguraOri figuraOriCollectionNewFiguraOri : figuraOriCollectionNew) {
                if (!figuraOriCollectionOld.contains(figuraOriCollectionNewFiguraOri)) {
                    FiguraTipoOri oldIdFiguraTipoOfFiguraOriCollectionNewFiguraOri = figuraOriCollectionNewFiguraOri.getIdFiguraTipo();
                    figuraOriCollectionNewFiguraOri.setIdFiguraTipo(figuraTipoOri);
                    figuraOriCollectionNewFiguraOri = em.merge(figuraOriCollectionNewFiguraOri);
                    if (oldIdFiguraTipoOfFiguraOriCollectionNewFiguraOri != null && !oldIdFiguraTipoOfFiguraOriCollectionNewFiguraOri.equals(figuraTipoOri)) {
                        oldIdFiguraTipoOfFiguraOriCollectionNewFiguraOri.getFiguraOriCollection().remove(figuraOriCollectionNewFiguraOri);
                        oldIdFiguraTipoOfFiguraOriCollectionNewFiguraOri = em.merge(oldIdFiguraTipoOfFiguraOriCollectionNewFiguraOri);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = figuraTipoOri.getIdFiguraTipo();
                if (findFiguraTipoOri(id) == null) {
                    throw new NonexistentEntityException("The figuraTipoOri with id " + id + " no longer exists.");
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
            FiguraTipoOri figuraTipoOri;
            try {
                figuraTipoOri = em.getReference(FiguraTipoOri.class, id);
                figuraTipoOri.getIdFiguraTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The figuraTipoOri with id " + id + " no longer exists.", enfe);
            }
            Collection<FiguraOri> figuraOriCollection = figuraTipoOri.getFiguraOriCollection();
            for (FiguraOri figuraOriCollectionFiguraOri : figuraOriCollection) {
                figuraOriCollectionFiguraOri.setIdFiguraTipo(null);
                figuraOriCollectionFiguraOri = em.merge(figuraOriCollectionFiguraOri);
            }
            em.remove(figuraTipoOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FiguraTipoOri> findFiguraTipoOriEntities() {
        return findFiguraTipoOriEntities(true, -1, -1);
    }

    public List<FiguraTipoOri> findFiguraTipoOriEntities(int maxResults, int firstResult) {
        return findFiguraTipoOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<FiguraTipoOri> findFiguraTipoOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from FiguraTipoOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FiguraTipoOri findFiguraTipoOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FiguraTipoOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getFiguraTipoOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from FiguraTipoOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    
    public void merge(FiguraTipoOri figuraTipoOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer id = 0;
      id = figuraTipoOri.getIdFiguraTipo();
      if (findFiguraTipoOri(id) != null) {
        em.merge(figuraTipoOri);
      } else {
        em.persist(figuraTipoOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }
    
}
