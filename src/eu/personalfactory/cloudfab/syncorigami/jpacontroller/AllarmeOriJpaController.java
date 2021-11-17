/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.AllarmeOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
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
 * @author francescodigaudio
 */
public class AllarmeOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(AllarmeOriJpaController.class);

    public AllarmeOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AllarmeOri allarmeOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(allarmeOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AllarmeOri allarmeOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            allarmeOri = em.merge(allarmeOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = allarmeOri.getIdAllarme();
                if (findAllarmeOri(id) == null) {
                    throw new NonexistentEntityException("The allarmeOri with id " + id + " no longer exists.");
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
            AllarmeOri allarmeOri;
            try {
                allarmeOri = em.getReference(AllarmeOri.class, id);
                allarmeOri.getIdAllarme();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The allarmeOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(allarmeOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AllarmeOri> findAllarmeOriEntities() {
        return findAllarmeOriEntities(true, -1, -1);
    }

    public List<AllarmeOri> findAllarmeOriEntities(int maxResults, int firstResult) {
        return findAllarmeOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<AllarmeOri> findAllarmeOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from AllarmeOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AllarmeOri findAllarmeOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AllarmeOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getAllarmeOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from AllarmeOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Metodo che consente di salvare un oggetto sul db, verifica l'esistenza
     * dell'oggetto nel db effettua un insert se l'oggetto non esiste altrimenti
     * effettua un update
     *
     * @param oggetto AllarmeOri da salvare
     * @author marilisa
     */
    public void merge(AllarmeOri allarmeOri) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Integer id = 0;
            id = allarmeOri.getIdAllarme();
            if (findAllarmeOri(id) != null) {
                em.merge(allarmeOri);
            } else {
                em.persist(allarmeOri);
            }
            em.getTransaction().commit();

        } catch (SecurityException ex) {
            log.error(ex.getStackTrace());
        } catch (IllegalStateException ex) {
            log.error(ex.getStackTrace());
        }

    }

}
