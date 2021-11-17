
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ClienteOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author francescodigaudio
 */
@SuppressWarnings("serial")
public class ClienteOriJpaController implements Serializable {

    public ClienteOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClienteOri clienteOri) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(clienteOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClienteOri clienteOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            clienteOri = em.merge(clienteOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clienteOri.getId();
                if (findClienteOri(id) == null) {
                    throw new NonexistentEntityException("The clienteOri with id " + id + " no longer exists.");
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
            ClienteOri clienteOri;
            try {
                clienteOri = em.getReference(ClienteOri.class, id);
                clienteOri.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clienteOri with id " + id + " no longer exists.", enfe);
            }
            em.remove(clienteOri);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClienteOri> findClienteOriEntities() {
        return findClienteOriEntities(true, -1, -1);
    }

    public List<ClienteOri> findClienteOriEntities(int maxResults, int firstResult) {
        return findClienteOriEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<ClienteOri> findClienteOriEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from ClienteOri as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ClienteOri findClienteOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClienteOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from ClienteOri as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
