/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ValoreParProdMacOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class ValoreParProdMacOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(ValoreParProdMacOriJpaController.class);

    public ValoreParProdMacOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ValoreParProdMacOri findValoreParProdMacOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ValoreParProdMacOri.class, id);
        } finally {
            em.close();
        }
    }

    public int getValoreParProdMacOriCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from ValoreParProdMacOri as o");
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
     * @param oggetto ValoreParProdMacOri da salvare
     * @author marilisa
     */
    public void merge(ValoreParProdMacOri valoreParProdMacOri) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Integer id = 0;
            id = valoreParProdMacOri.getIdValPm();
            if (findValoreParProdMacOri(id) != null) {
                em.merge(valoreParProdMacOri);
            } else {
                em.persist(valoreParProdMacOri);
            }
            em.getTransaction().commit();

        } catch (SecurityException ex) {
            log.error(ex.getStackTrace());
        } catch (IllegalStateException ex) {
            log.error(ex.getStackTrace());
        }

    }

    /**
     * Utilizzato
     *
     * @param valoreParProdMacOri
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(ValoreParProdMacOri valoreParProdMacOri) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            valoreParProdMacOri = em.merge(valoreParProdMacOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = valoreParProdMacOri.getIdValPm();
                if (findValoreParProdMacOri(id) == null) {
                    throw new NonexistentEntityException("The valoreParProdMacOri with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Utilizzato
     *
     * @param valoreParProdMacOri
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void create(ValoreParProdMacOri valoreParProdMacOri) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(valoreParProdMacOri);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findValoreParProdMacOri(valoreParProdMacOri.getIdValPm()) != null) {
                throw new PreexistingEntityException("ValoreParProdMacOri " + valoreParProdMacOri + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * Metodo che seleziona tutti i record della tabella valore_par_prod_ori
     *
     * @return Collection di ValoreProdottoOri
     */
    @SuppressWarnings("unchecked")
    public Collection<ValoreParProdMacOri> findValoreParProdMacOriAll() {

        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("ValoreParProdMacOri.findAll");

        return q.getResultList();

    }

}
