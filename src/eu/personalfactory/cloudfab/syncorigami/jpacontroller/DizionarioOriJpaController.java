package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.DizionarioOri;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
@SuppressWarnings("serial")
public class DizionarioOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(DizionarioOriJpaController.class);

    public DizionarioOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    public DizionarioOri findDizionarioOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DizionarioOri.class, id);
        } finally {
            em.close();
        }
    }

    //#############################################################################
    //############# METODI INVOCATI DA SYNCORIGAMI ################################
    //#############################################################################   
    /**
     * Metodo che consente di salvare un oggetto sul db verifica l'esistenza
     * dell'oggetto nel db effettua un insert se l'oggetto non esiste altrimenti
     * effettua un update
     *
     * @param oggetto DizionarioOri da salvare
     * @author marilisa
     */
    public void merge(DizionarioOri dizionarioOri) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            Integer id = 0;
            id = dizionarioOri.getIdDizionario();
            if (findDizionarioOri(id) != null) {
                em.merge(dizionarioOri);
            } else {
                em.persist(dizionarioOri);
            }
            em.getTransaction().commit();

        } catch (SecurityException ex) {
            log.error(ex.getStackTrace());
        } catch (IllegalStateException ex) {
            log.error(ex.getStackTrace());
        }

    }

    /**
     * Metodo che seleziona tutti i vocaboli presenti nella tabella
     * dizionario_ori
     *
     * @return Collection di DizionarioOri
     */
    @SuppressWarnings("unchecked")
    public Collection<DizionarioOri> findDizionarioOriAll() {

        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("DizionarioOri.findAll");

        return q.getResultList();

    }

}
