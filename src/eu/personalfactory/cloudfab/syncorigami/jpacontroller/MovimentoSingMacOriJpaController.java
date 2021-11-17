/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.MovimentoSingMacOri;
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
public class MovimentoSingMacOriJpaController implements Serializable {

     private Logger log = Logger.getLogger(MovimentoSingMacOriJpaController.class);

    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    public MovimentoSingMacOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;


    public MovimentoSingMacOriJpaController(Object object, EntityManagerFactory emf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  

    public void create(MovimentoSingMacOri movimentoSingMacOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(movimentoSingMacOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimentoSingMacOri movimentoSingMacOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            movimentoSingMacOri = em.merge(movimentoSingMacOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimentoSingMacOri.getIdMovOri();
                if (findMovimentoSingMacOri(id) == null) {
                    throw new NonexistentEntityException("The movimentoSingMacOri with id " + id + " no longer exists.");
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
            MovimentoSingMacOri movimentoSingMacOri;
            try {
                movimentoSingMacOri = em.getReference(MovimentoSingMacOri.class, id);
                movimentoSingMacOri.getIdMovOri();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentoSingMacOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(movimentoSingMacOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimentoSingMacOri> findMovimentoSingMacOriEntities() {
        return findMovimentoSingMacOriEntities(true, -1, -1);
    }

    public List<MovimentoSingMacOri> findMovimentoSingMacOriEntities(int maxResults, int firstResult) {
        return findMovimentoSingMacOriEntities(false, maxResults, firstResult);
    }

     @SuppressWarnings("unchecked")
    private List<MovimentoSingMacOri> findMovimentoSingMacOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from MovimentoSingMacOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MovimentoSingMacOri findMovimentoSingMacOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimentoSingMacOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoSingMacOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from MovimentoSingMacOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
      /**
   * Metodo che consente di salvare un oggetto sul db, 
   * verifica l'esistenza dell'oggetto nel db 
   * effettua un insert se l'oggetto non esiste altrimenti effettua un update
   * @param oggetto MovimentoSingMacOri da salvare
   * @author marilisa
   */
  public void merge(MovimentoSingMacOri movimentoSingMacOri) {
    EntityManager em = null;

    try {
      em = getEntityManager();
      em.getTransaction().begin();

      Integer idMovInephos = 0;
      idMovInephos = movimentoSingMacOri.getIdMovInephos();
      if (findMovimentoSingMacOri(idMovInephos) != null) {
        
          em.merge(movimentoSingMacOri);
      } else {
         em.persist(movimentoSingMacOri);
      }
      em.getTransaction().commit();

    } catch (SecurityException ex) {
      log.error(ex.getStackTrace());
    } catch (IllegalStateException ex) {
      log.error(ex.getStackTrace());
    }

  }

     @SuppressWarnings("unchecked")
     public Collection<MovimentoSingMacOri> findMovimentoSingMacOriNew(Date dtUltAgg,String origineMov){
     EntityManager em = getEntityManager();
     try {
     
       Query q = em.createNamedQuery("MovimentoSingMacOri.findDatiNuovi");
       q.setParameter ("dtMov",dtUltAgg);
       q.setParameter ("origineMov",origineMov);
       return  q.getResultList();
            
    } finally {
      em.close();
    }
     
  }       



}
