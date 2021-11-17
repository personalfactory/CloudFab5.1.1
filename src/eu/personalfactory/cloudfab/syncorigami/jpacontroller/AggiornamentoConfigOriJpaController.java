package eu.personalfactory.cloudfab.syncorigami.jpacontroller;

import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoConfigOri;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import javax.persistence.*;
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;

/**
 *
 * @author francescodigaudio
 */
@SuppressWarnings("serial")
public class AggiornamentoConfigOriJpaController implements Serializable {

    private Logger log = Logger.getLogger(AggiornamentoConfigOriJpaController.class);

 
    public AggiornamentoConfigOriJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //##########################################################################
    //############## METODI INVOCATI DA SYNCORIGAMI ############################
    //##########################################################################
    public AggiornamentoConfigOri findAggiornamentoConfigOri(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AggiornamentoConfigOri.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Metodo che restituisce un oggetto AggiornamentoConfig corrispondente al
     * parametro passato
     *
     * @param String nome del parametro
     * @return AggiornamentoConfig
     */
    public AggiornamentoConfigOri findProperty(String parametro) {
        EntityManager em = getEntityManager(); 
       
        Query q = em.createNamedQuery("AggiornamentoConfigOri.findByParametro");
         q.setParameter("parametro", parametro);
        try {

            q.getSingleResult();

        } catch (NoResultException ex) {
            log.error("PROPRIETA' " + parametro + " NON TROVATA NEL DB : " + ex.toString());
            throw ex;
        } catch (NonUniqueResultException ex) {
            log.error("PROPRIETA' " + parametro + " DUPLICATA NELLA TABELLA aggiornamento_config_ori!");
            throw ex;
        }
        return (AggiornamentoConfigOri) q.getSingleResult();
    }

    /**
     * Metodo che seleziona tutte le proprietà presenti nella tabella
     * aggiornamento_config_ori
     *
     * @return Collection di AggiornamentoConfigOri
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    public Collection<AggiornamentoConfigOri> findAggiornamentoConfigOriAll() {

        EntityManager em = getEntityManager();

        Query q = em.createNamedQuery("AggiornamentoConfigOri.findAll");

        return q.getResultList();

    }

    /**
     * Metodo che consente di salvare un oggetto sul db, verifica l'esistenza
     * dell'oggetto nel db effettua un insert se l'oggetto non esiste altrimenti
     * effettua un update
     *
     * @param oggetto AggiornamentoConfigOri da salvare
     * @author marilisa
     */
    public void merge(AggiornamentoConfigOri aggiornamentoConfigOri) {

        try {
            EntityManager em = getEntityManager();
            em.getTransaction().begin();

            Integer id = aggiornamentoConfigOri.getId();
            if (findAggiornamentoConfigOri(id) != null) {
                em.merge(aggiornamentoConfigOri);
            } else {
                em.persist(aggiornamentoConfigOri);
            }
            em.getTransaction().commit();

        } catch (SecurityException | IllegalStateException ex) {
            log.error(ex.getStackTrace());
            log.error("ERRORE NEL MERGE DELL' OGGETTO : " + aggiornamentoConfigOri);
        }

    }
}
