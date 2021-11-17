package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.ComponenteOri;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author francescodigaudio
 */
@SuppressWarnings("serial")
public class ComponenteOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(ComponenteOriJpaController.class);

    public ComponenteOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public ComponenteOri findComponenteOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComponenteOri.class, id);
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
     * @param componenteOri
     * @author marilisa
     */
    public void merge(ComponenteOri componenteOri) {
        EntityManager em;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Integer id = componenteOri.getIdComp();
            if (findComponenteOri(id) != null) {
                em.merge(componenteOri);
            } else {
                em.persist(componenteOri);
            }
            em.getTransaction().commit();

        } catch (SecurityException | IllegalStateException ex) {
            log.error(ex.getStackTrace());
        }

    }

    /**
     * Metodo che seleziona tutti i ComponenteOri presenti nella tabella
     * componente_ori
     *
     * @return Collection di ComponenteOri
     */
    @SuppressWarnings("unchecked")
    public Collection<ComponenteOri> findComponenteOriAll() {

        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("ComponenteOri.findAll");

        return q.getResultList();

    }

}
