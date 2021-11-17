package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.CategoriaOri;
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
@SuppressWarnings("serial")
public class CategoriaOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(CategoriaOriJpaController.class);

    public CategoriaOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Utilizzato
     *
     * @param id
     * @return
     */
    public CategoriaOri findCategoriaOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoriaOri.class, id);
        } finally {
            em.close();
        }
    }

    //#############################################################################
    //############# METODI INVOCATI DA SYNCORIGAMI ################################
    //#############################################################################   
    /**
     * Metodo che consente di salvare un oggetto sul db, verifica l'esistenza
     * dell'oggetto nel db effettua un insert se l'oggetto non esiste altrimenti
     * effettua un update
     *
     * @param oggetto CategoriaOri da salvare
     * @author marilisa
     */
    public void merge(CategoriaOri categoriaOri) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Integer id = 0;
            id = categoriaOri.getIdCat();
            if (findCategoriaOri(id) != null) {
                em.merge(categoriaOri);
            } else {
                em.persist(categoriaOri);
            }
            em.getTransaction().commit();

        } catch (SecurityException ex) {
            log.error(ex.getStackTrace());
        } catch (IllegalStateException ex) {
            log.error(ex.getStackTrace());
        }

    }

    /**
     * Metodo che seleziona tutte le categorie presenti nella tabella
     * categoria_ori
     *
     * @return Collection di CategoriaOri
     */
    @SuppressWarnings("unchecked")
    public Collection<CategoriaOri> findCategoriaOriAll() {

        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("CategoriaOri.findAll");

        return q.getResultList();

    }
}
