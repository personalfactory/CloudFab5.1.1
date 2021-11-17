package eu.personalfactory.cloudfab.syncorigami.utils;

import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoConfigOri;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.AggiornamentoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ParametroGlobMacOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreRipristinoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreAllarmeOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.OrdineSingMacOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.MovimentoSingMacOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.CicloOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.CicloProcessoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ProcessoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.MacchinaOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreParSingMacOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreParCompOriJpaController;
import eu.personalfactory.cloudfab.macchina.entity.CicloOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParSingMacOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParCompOri;
import eu.personalfactory.cloudfab.macchina.entity.ProcessoOri;
import eu.personalfactory.cloudfab.macchina.entity.CicloProcessoOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreAllarmeOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreRipristinoOri;
import eu.personalfactory.cloudfab.macchina.entity.MovimentoSingMacOri;
import eu.personalfactory.cloudfab.macchina.entity.MacchinaOri;
import eu.personalfactory.cloudfab.macchina.entity.OrdineSingMacOri;
import eu.personalfactory.cloudfab.macchina.entity.AggiornamentoOri;
import eu.personalfactory.cloudfab.macchina.entity.AllarmeOri;
import eu.personalfactory.cloudfab.macchina.entity.CategoriaOri;
import eu.personalfactory.cloudfab.macchina.entity.ChimicaOri;
import eu.personalfactory.cloudfab.macchina.entity.ComponenteOri;
import eu.personalfactory.cloudfab.macchina.entity.ComponentePesaturaOri;
import eu.personalfactory.cloudfab.macchina.entity.ComponenteProdottoOri;
import eu.personalfactory.cloudfab.macchina.entity.DizionarioOri;
import eu.personalfactory.cloudfab.macchina.entity.FiguraOri;
import eu.personalfactory.cloudfab.macchina.entity.FiguraTipoOri;
import eu.personalfactory.cloudfab.macchina.entity.NumSacchettoOri;
import eu.personalfactory.cloudfab.macchina.entity.ParametroCompProdOri;
import eu.personalfactory.cloudfab.macchina.entity.ParametroGlobMacOri;
import eu.personalfactory.cloudfab.macchina.entity.PresaOri;
import eu.personalfactory.cloudfab.macchina.entity.ProdottoOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParOrdineOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParProdMacOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParProdOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreParSacchettoOri;
import eu.personalfactory.cloudfab.macchina.entity.ValoreProdottoOri;
import eu.personalfactory.cloudfab.macchina.panels.Pannello12_Aggiornamento;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.EstraiStringaHtml;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFab5_0.TrovaVocabolo;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_FINE;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.HTML_STRINGA_INIZIO;
import static eu.personalfactory.cloudfab.macchina.utility.CloudFabConstants.ID_DIZIONARIO_MESSAGGI_MACCHINA;
import eu.personalfactory.cloudfab.macchina.utility.ParametriGlobali;
import eu.personalfactory.cloudfab.macchina.utility.ParametriSingolaMacchina;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateContentException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateTypeException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.InvalidUpdateVersionException;
import eu.personalfactory.cloudfab.syncorigami.exceptions.MachineCredentialsNotFoundException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.AggiornamentoConfigOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.AllarmeOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.CategoriaOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ChimicaOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ComponenteOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ComponentePesaturaOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ComponenteProdottoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.DizionarioOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.FiguraOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.FiguraTipoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.NumSacchettoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ParametroCompProdOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.PresaOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ProdottoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreParOrdineOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreParProdMacOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreParProdOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreParSacchettoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.ValoreProdottoOriJpaController;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.NonexistentEntityException;
import eu.personalfactory.cloudfab.syncorigami.jpacontroller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import org.apache.log4j.Logger;

/**
 *
 * @author marilisa
 */
public class DataManagerM {

    //private EntityManagerFactory emf;
    private MachineCredentials machineCredentials;

    private static Logger log = Logger.getLogger(DataManagerM.class);

    public DataManagerM(
            String filePfx,
            EntityManagerFactory emf) throws MachineCredentialsNotFoundException {

        //RECUPERA LE CREDENZIALI DELLA MACCHINA
        MachineCredentials mc = new MachineCredentials();
        MacchinaOriJpaController mjc = new MacchinaOriJpaController(null, emf);
        AggiornamentoOriJpaController ajc = new AggiornamentoOriJpaController(null, emf);
        MacchinaOri m = mjc.findMacchinaOri();

        if (m == null) {
            log.error("Oggetto MacchinaOri nullo!");
            throw new MachineCredentialsNotFoundException("IMPOSSIBILE RECUPERARE L'OGGETTO MacchinaOri DAL DB!!!");
        }
        Integer ultimaVersione = null;

        Date dataUltAgg = null;

        try {
            ultimaVersione = mjc.findLastUpdateVersion(filePfx);
            log.info("Versione dell'ultimo aggiornamento : " + ultimaVersione);

            dataUltAgg = ajc.recuperaDtUltimoAggiornamento(filePfx);
            log.info("Data dell'ultimo aggiornamento : " + dataUltAgg);

        } catch (NoResultException nre) {
            //In questo caso stampo il log senza generare l'eccezione 
            //Potrebbe trattarsi del primo aggiornamento 
            log.info("Nessun Risultato in findLastUpdateVersion potrebbe trattarsi del primo aggiornamento");

        } catch (NonUniqueResultException nure) {
            log.error("DATA O VERSIONE DELL'ULTIMO AGGIORNAMENTO " + filePfx + "DUPLICATI IN findLastUpdateVersion!");
            throw nure;
        }

        //Se si tratta del primo aggiornamento (e non solo) 
        //assegno il valore iniziale alla versione e alla data
        if (ultimaVersione == null | dataUltAgg == null) {
            log.info("Non è stata trovata alcuna versione o data di ultimo aggiornamento");
            //NON HA TROVATO VERSIONI DI AGGIORNAMENTO...SETTA L'ULIMA VERSIONE A 0
            ultimaVersione = 0;
            log.info("Ultima versione impostata = " + ultimaVersione);

            dataUltAgg = SyncOrigamiConstants.DATA_DEFAULT;
            log.info("Data di default impostata = " + SyncOrigamiConstants.DATA_DEFAULT);
        }

        mc.setIdMacchina(m.getIdMacchina());
        mc.setLastUpdateDate(dataUltAgg);
        mc.setLastUpdateVersion(ultimaVersione);
        mc.setNewRemoteUpdateVersion(ultimaVersione + 1);
        mc.setFtpPassword(m.getFtpPassword());
        mc.setFtpUser(m.getFtpUser());
        mc.setZipPassword(m.getZipPassword());

//        log.info( "CREATO MachineCredentials : " + mc.toString());
        this.machineCredentials = mc;
    }

    /**
     * Metodo che restituisce le credenziali della macchina
     *
     * @return Un oggetto MachineCredentials che contiene tutte le credenziali
     * della macchina
     *
     */
    public MachineCredentials getMachineCredentials()
            throws MachineCredentialsNotFoundException {
        return machineCredentials;
    }

    /**
     * Costruisce un oggetto AggiornamentoOri contenente i dati nuovi lato
     * macchina
     *
     * @return Aggiornamento completo per il server
     *
     */
    @SuppressWarnings("unchecked")
    public AggiornamentoOri costruisciAggiornamento(String outFilePfx,
            EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg) {

        log.info("INIZIO COSTRUZIONE AGGIORNAMENTO");

        AggiornamentoOri aggiornamentoOriOut = new AggiornamentoOri();

        AggiornamentoOriJpaController aggiornamentoOriJc = new AggiornamentoOriJpaController(null, emf);
        MacchinaOriJpaController macchinaOriJc = new MacchinaOriJpaController(null, emf);
        ProcessoOriJpaController processoOriJc = new ProcessoOriJpaController(null, emf);
        ValoreRipristinoOriJpaController valoreRipristinoOriJc = new ValoreRipristinoOriJpaController(null, emf);
        ValoreParSingMacOriJpaController valoreParSingMacOriJc = new ValoreParSingMacOriJpaController(null, emf);
        ValoreParCompOriJpaController valoreParCompOriJc = new ValoreParCompOriJpaController(null, emf);

        ParametroGlobMacOriJpaController parametroGlobMacOriJc = new ParametroGlobMacOriJpaController(null, emf);

        ValoreAllarmeOriJpaController valoreAllarmeOriJc = new ValoreAllarmeOriJpaController(null, emf);

        MovimentoSingMacOriJpaController movimentoSingMacOriJc = new MovimentoSingMacOriJpaController(null, emf);

        CicloOriJpaController cicloOriJc = new CicloOriJpaController(null, emf);

        CicloProcessoOriJpaController cicloProcessoOriJc = new CicloProcessoOriJpaController(null, emf);

        OrdineSingMacOriJpaController ordineSingMacOriJc = new OrdineSingMacOriJpaController(null, emf);

        aggiornamentoOriOut.setDaInserire(new ArrayList());

        //Recupero i nuovi dati della tab processo_ori e li aggiungo alla collection DaInserire    
        Collection<ProcessoOri> processoOriColl = processoOriJc.findProcessoOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(processoOriColl);
        log.info(" Ricerca nuovi processi ");

        //Recupero i nuovi dati della tab valore_ripristino_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreRipristinoOri> valoreRipristinoOriColl = valoreRipristinoOriJc.findValoreRipristinoNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(valoreRipristinoOriColl);
        log.info(" Ricerca nuovi parametri di ripristino ");

        //Recupero i nuovi dati della tab valore_par_sing_mac_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreParSingMacOri> valoreParSingMacOriColl = valoreParSingMacOriJc.findValoreParSingMacOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(valoreParSingMacOriColl);
        log.info(" Ricerca nuovi parametri singola macchina ");

        //Recupero i nuovi dati della tab valore_par_comp_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreParCompOri> valoreParCompOriColl = valoreParCompOriJc.findValoreParCompOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(valoreParCompOriColl);
        log.info(" Ricerca nuovi valori parametri componente ");

        //Recupero i nuovi dati della tab valore_allarme_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreAllarmeOri> valoreAllarmeOriColl = valoreAllarmeOriJc.findValoreAllarmeOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(valoreAllarmeOriColl);
        log.info("Ricerca nuovi valori allarme");

        //Inserire in parametri globali 140
        Integer idParOrigineMov = 140;
        String strParOrigineMov = "";
        strParOrigineMov = ParametriGlobali.parametri.get(140);
        //strParOrigineMov=parametroGlobMacOriJc.findParametroGlobMacOri(idParOrigineMov).getValoreVariabile();

        Collection<MovimentoSingMacOri> movimentoSingMacOriColl = movimentoSingMacOriJc.findMovimentoSingMacOriNew(machineCredentials.getLastUpdateDate(), strParOrigineMov);
        aggiornamentoOriOut.getDaInserire().addAll(movimentoSingMacOriColl);
        log.info("Ricerca nuovi movimenti singola macchina");

        //Recupero i nuovi dati della tab ciclo_ori e li aggiungo alla collection DaInserire    
        Collection<CicloOri> cicloOriColl = cicloOriJc.findCicloOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(cicloOriColl);
        log.info(" Ricerca nuovi cicli ");

        //Recupero i nuovi dati della tab ciclo_ori e li aggiungo alla collection DaInserire    
        Collection<CicloProcessoOri> cicloProcessoOriColl = cicloProcessoOriJc.findCicloProcessoOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(cicloProcessoOriColl);
        log.info(" Ricerca nuovi ciclo processo ");

        //Recupero i nuovi dati della tab ciclo_ori e li aggiungo alla collection DaInserire    
        Collection<OrdineSingMacOri> ordineSingMacOriColl = ordineSingMacOriJc.findOrdineSingMacOriNew(machineCredentials.getLastUpdateDate());
        aggiornamentoOriOut.getDaInserire().addAll(ordineSingMacOriColl);
        log.info(" Ricerca nuovi ordineSingMac");

        //Se fin qui la collection contiene dati nuovi aggiungo anche la macchina 
        //altrimenti la collection deve risultare vuota e il file xml non deve essere generato
        if (!aggiornamentoOriOut.getDaInserire().isEmpty()) {

            log.info(" Collection di dati daInserire non vuota ");
            //Recupero le informazioni della macchinaOri e le inserisco nel file xml    
            //L'eccezione nel caso la tabella macchina non contenga nessun record è gestita nel machineCredentials
            MacchinaOri macchinaOri = macchinaOriJc.findMacchinaOri(machineCredentials.getIdMacchina());
            aggiornamentoOriOut.getDaInserire().add(macchinaOri);

            log.info(" Inserite nel file le informazioni della macchina ");

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 362, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + machineCredentials.getNewRemoteUpdateVersion()
                    + HTML_STRINGA_FINE);//"Versione aggiornamento in uscita : " 
            log.info(" Versione aggiornamento in uscita : " + machineCredentials.getNewRemoteUpdateVersion());

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 363, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + Integer.toString(processoOriColl.size())
                    + HTML_STRINGA_FINE);//"Numero di Processi Inviati :
            log.info(" Numero di Processi Inviati : " + processoOriColl.size());

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 365, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + Integer.toString(valoreRipristinoOriColl.size())
                    + HTML_STRINGA_FINE);//Numero di valori di ripristino inviati :
            log.info(" Numero di valori di ripristino inviati : " + valoreRipristinoOriColl.size());

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 366, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + Integer.toString(valoreParSingMacOriColl.size())
                    + HTML_STRINGA_FINE);//Numero di valori singola macchina inviati : 
            log.info(" Numero di valori singola macchina inviati : " + valoreParSingMacOriColl.size());

            panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                    + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 367, ParametriSingolaMacchina.parametri.get(111)))
                    + " "
                    + Integer.toString(valoreParCompOriColl.size())
                    + HTML_STRINGA_FINE);//Numero di valori dei parametri componenti inviati : 
            log.info(" Numero di valori dei parametri componenti inviati : " + valoreParCompOriColl.size());

        } else {

////            Collection c = aggiornamentoOriOut.getDaInserire();
////            int count = 0;
////
////            for (Object o : c) {
////                if (o != null) {
////                    log.info(o.getClass().cast(o).toString());
////                } else {
////                    log.info("AGGIORNAMENTO_OUT - TROVATO/I n. " + count++ + " OGGETTO/I NULL!!!");
////                }
////            }
            log.info("Ultima versione di aggiornamento : " + machineCredentials.getLastUpdateVersion());
        }

        //Setto i campi dell'oggetto aggiornamento
        //Calcolo la nuova versione
        aggiornamentoOriOut.setTipo(outFilePfx);
        aggiornamentoOriOut.setDtAggiornamento(new Date());
        aggiornamentoOriOut.setVersione(machineCredentials.getLastUpdateVersion() + 1);

        log.info(" Tipo di aggiornamento costruito : " + aggiornamentoOriOut.getTipo());
        log.info(" Data dell'aggiornamento costruito : " + aggiornamentoOriOut.getDtAggiornamento());
        log.info(" Versione dell'aggiornamento costruito : " + aggiornamentoOriOut.getVersione());
        log.info(" FINE COSTRUZIONE AGGIORNAMENTO ");
        return aggiornamentoOriOut;
    }

    /**
     * Metodo che salva l'oggetto AggiornamentoOri nella tabella
     * aggiornamento_ori di origamidb
     *
     * @param aggiornamento
     */
    public void salvaAggiornamentoOriOut(AggiornamentoOri aggiornamentoOri, EntityManagerFactory emf) {

        AggiornamentoOriJpaController aggiornamentoOriJc = new AggiornamentoOriJpaController(null, emf);

        aggiornamentoOriJc.create(aggiornamentoOri);

        log.info("SALVATO NUOVO AGGIORNAMENTO_ORI IN USCITA NELLA TABELLA aggiornamentoOri:" + aggiornamentoOri.toString());

    }

    /**
     * TODO : IMPLEMENTARE Questo metodo viene eseguito lato macchina a seguito
     * del download dell'xml inviato dal server in questo caso legge i dati
     * contenuti nell'oggetto aggiornamentoOri valida i dati
     *
     * @param aggiornamentoOri
     * @return VOID
     * @throws
     */
    public void validaContenutoAggiornamentoOriIn(AggiornamentoOri aggiornamentoOri, EntityManagerFactory emf) {

        MacchinaOriJpaController macchinaOriJc = new MacchinaOriJpaController(null, emf);

        //Validazione collection dati contenuti nell'oggetto aggiornamentoOri
        //TODO : Prima di inserire o modificare i valori relativi alle foreign key controllare la corrispondenza
        for (Object obj : aggiornamentoOri.getDaInserire()) {

            if (obj instanceof MacchinaOri) {
                MacchinaOri macchinaOri = (MacchinaOri) obj;
            }
            //Controllo lunghezza e/o struttura dei cod_chimica
            if (obj instanceof ChimicaOri) {
                ChimicaOri chimicaOri = (ChimicaOri) obj;
            }
            if (obj instanceof CategoriaOri) {
                CategoriaOri categoriaOri = (CategoriaOri) obj;
            }
            if (obj instanceof ParametroCompProdOri) {
                ParametroCompProdOri parametroCompProdOri = (ParametroCompProdOri) obj;
            }
            if (obj instanceof ProdottoOri) {
                ProdottoOri prodottoOri = (ProdottoOri) obj;
//verificare la lunghezza del codice
            }
            if (obj instanceof ComponenteProdottoOri) {
                ComponenteProdottoOri componenteProdottoOri = (ComponenteProdottoOri) obj;
            }
            if (obj instanceof PresaOri) {
                PresaOri presaOri = (PresaOri) obj;
            }
            if (obj instanceof DizionarioOri) {
                DizionarioOri dizionarioOri = (DizionarioOri) obj;
            }
            if (obj instanceof NumSacchettoOri) {
                NumSacchettoOri numSacchettoOri = (NumSacchettoOri) obj;
            }
            if (obj instanceof ParametroGlobMacOri) {
                ParametroGlobMacOri parametroGlobMacOri = (ParametroGlobMacOri) obj;
            }
            if (obj instanceof ValoreParProdOri) {
                ValoreParProdOri valoreParProdOri = (ValoreParProdOri) obj;
            }
            if (obj instanceof ValoreParSacchettoOri) {
                ValoreParSacchettoOri valoreParSacchettoOri = (ValoreParSacchettoOri) obj;
            }
            if (obj instanceof ValoreRipristinoOri) {
                ValoreRipristinoOri valoreRipristinoOri = (ValoreRipristinoOri) obj;
            }
            if (obj instanceof ValoreParSingMacOri) {
                ValoreParSingMacOri valoreParSingMacOri = (ValoreParSingMacOri) obj;
            }
            //############ 21-10-2015 ##########################################
            if (obj instanceof ValoreProdottoOri) {
                ValoreProdottoOri valoreProdottoOri = (ValoreProdottoOri) obj;
            }
            if (obj instanceof ValoreParProdMacOri) {
                ValoreParProdMacOri valoreParProdMacOri = (ValoreParProdMacOri) obj;
            }
            //######################### NOVEMBRE 2017 ########################à
            if (obj instanceof MovimentoSingMacOri) {

                MovimentoSingMacOri movimentoSingMacOri = (MovimentoSingMacOri) obj;
            }
            if (obj instanceof OrdineSingMacOri) {
                OrdineSingMacOri ordineSingMacOri = (OrdineSingMacOri) obj;
            }
            if (obj instanceof ValoreParOrdineOri) {
                ValoreParOrdineOri valoreParOrdineOri = (ValoreParOrdineOri) obj;
            }
            if (obj instanceof AllarmeOri) {
                AllarmeOri allarmeOri = (AllarmeOri) obj;
            }
            if (obj instanceof ComponentePesaturaOri) {
                ComponentePesaturaOri componentePesaturaOri = (ComponentePesaturaOri) obj;
            }
            if (obj instanceof FiguraTipoOri) {
                FiguraTipoOri figuraTipoOri = (FiguraTipoOri) obj;
            }
            if (obj instanceof FiguraOri) {
                FiguraOri figuraOri = (FiguraOri) obj;
            }

        }

    }

    /**
     * Metodo che valida i campi di un oggetto AggiornamentoOri,
     *
     * @param Object, tipo aggiornamento
     * @return true se l'oggetto è valido, false se non è valido
     * @throws
     * InvalidUpdateTypeException,InvalidUpdateContentException,InvalidUpdateVersionException
     */
//TODO : 0) Controllare che si  tratti di un oggetto di tipo AggiornamentoOri
//       1) Controllare che l'id della macchina sia corretto, 
//          nella tabella aggiornamento_ori non è presente l'id_macchina, 
//          quindi si può leggere dal nome del file prima di scaricarlo.
//       2) Controllare che tipo dell'aggiornamento sia di tipo IN per la macchina.
//       3) Controllare che la versione dell'aggiornamento sia successiva all'ultima versione 
//          presente nella tabella aggiornamento_ori
//          si può anche leggere dal nome del file
//       4) Gestire anche il caso del primo aggiornamento
    public Boolean validaAggiornamentoOriIn(Object obj,
            String inFilePfx,
            EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg)
            throws InvalidUpdateTypeException,
            InvalidUpdateContentException,
            InvalidUpdateVersionException {

        log.info("INIZIO METODO validaAggiornamentoOriIn");
        AggiornamentoOriJpaController aggiornamentoOriJc = new AggiornamentoOriJpaController(null, emf);

        //Controllo che si tratti di un oggetto di tipo AggiornamentoOri
        //Eccezione bloccante
        if (!(obj instanceof AggiornamentoOri)) {
            throw new InvalidUpdateContentException("Oggetto diverso da AggiornamentoOri!");
        }
        AggiornamentoOri aggiornamentoOri = (AggiornamentoOri) obj;

        //Controllo che l'oggetto AggiornamentoOri da salvare abbia il tipo corretto
        //Eccezione bloccante
        if (!aggiornamentoOri.getTipo().equals(inFilePfx)) {
            throw new InvalidUpdateTypeException("Tipo di aggiornamento errato!");
        }

        //Controllo che l'oggetto AggiornamentoOri da salvare abbia la versione successiva all'ultima
        Integer ultimaVersione = null;
        try {
            ultimaVersione = aggiornamentoOriJc.findLastUpdateVersion(inFilePfx);
        } catch (NoResultException ex) {
            throw ex;
        } catch (NonUniqueResultException ex) {
            throw ex;
        }
        //Se l'ultima versione presente nel db è null vuol dire che non sono presenti aggiornamenti nella tabella aggiornamento_ori
        //Si sta effettuando il primo aggiornamento, dunque setto l'ultima versione a zero
        if (ultimaVersione == null) {
            ultimaVersione = 0;
            log.info("L' ultima versione d'aggiornamento risulta null ");
            log.info("Ultima versione d'aggiornamento impostata a zero ");
        }
        //Eccezione bloccante
        if (!aggiornamentoOri.getVersione().equals(ultimaVersione + 1)) {

            throw new InvalidUpdateVersionException("Versione errata d'aggiornamento ");
        }

        return true;
    }

    /**
     * Questo metodo viene eseguito lato macchina a seguito del download
     * dell'xml inviato dal server in questo caso salva i dati contenuti
     * nell'oggetto aggiornamentoOri sul database della macchina a seguito della
     * validazione
     *
     * @param aggiornamentoOri
     * @return VOID
     * @throws
     */
    public void SalvaDatiAggiornamentoOriIn(AggiornamentoOri aggiornamentoOri,
            EntityManagerFactory emf,
            Pannello12_Aggiornamento panelAgg) throws PreexistingEntityException, NonexistentEntityException, Exception {

        MacchinaOriJpaController macchinaOriJc = new MacchinaOriJpaController(null, emf);
        AggiornamentoConfigOriJpaController aggiornamentoConfigOriJc = new AggiornamentoConfigOriJpaController(null, emf);
        ChimicaOriJpaController chimicaOriJc = new ChimicaOriJpaController(null, emf);
        ComponenteOriJpaController componenteOriJc = new ComponenteOriJpaController(null, emf);
        CategoriaOriJpaController categoriaOriJc = new CategoriaOriJpaController(null, emf);
        ParametroCompProdOriJpaController parametroCompProdOriJc = new ParametroCompProdOriJpaController(null, emf);
        ProdottoOriJpaController prodottoOriJc = new ProdottoOriJpaController(null, emf);
        ComponenteProdottoOriJpaController componenteProdottoOriJc = new ComponenteProdottoOriJpaController(null, emf);
        PresaOriJpaController presaOriJc = new PresaOriJpaController(null, emf);
        DizionarioOriJpaController dizionarioOriJc = new DizionarioOriJpaController(null, emf);
        NumSacchettoOriJpaController numSacchettoOriJc = new NumSacchettoOriJpaController(null, emf);
        ParametroGlobMacOriJpaController parametroGlobMacOriJc = new ParametroGlobMacOriJpaController(null, emf);
        ValoreParProdOriJpaController valoreParProdOriJc = new ValoreParProdOriJpaController(null, emf);
        ValoreParSacchettoOriJpaController valoreParSacchettoOriJc = new ValoreParSacchettoOriJpaController(null, emf);
        ValoreRipristinoOriJpaController valoreRipristinoOriJc = new ValoreRipristinoOriJpaController(null, emf);
        ValoreParSingMacOriJpaController valoreParSingMacOriJc = new ValoreParSingMacOriJpaController(null, emf);
        ValoreParCompOriJpaController valoreParCompOriJc = new ValoreParCompOriJpaController(null, emf);
        ValoreProdottoOriJpaController valoreProdottoOriJc = new ValoreProdottoOriJpaController(null, emf);
        ValoreParProdMacOriJpaController valoreParProdMacOriJc = new ValoreParProdMacOriJpaController(null, emf);

        AllarmeOriJpaController allarmeOriJc = new AllarmeOriJpaController(null, emf);
        MovimentoSingMacOriJpaController movimentoSingMacOriJc = new MovimentoSingMacOriJpaController(null, emf);
        ComponentePesaturaOriJpaController componentePesaturaOriJc = new ComponentePesaturaOriJpaController(null, emf);
        OrdineSingMacOriJpaController ordineSingMacOriJc = new OrdineSingMacOriJpaController(null, emf);
        ValoreParOrdineOriJpaController valoreParOrdineOriJc = new ValoreParOrdineOriJpaController(null, emf);
        FiguraOriJpaController figuraOriJc = new FiguraOriJpaController(null, emf);
        FiguraTipoOriJpaController figuraTipoOriJc = new FiguraTipoOriJpaController(null, emf);

        log.info("################ INIZIO SALVATAGGIO DATI AGGIORNAMENTO_ORI ######################");

        //VARIABILI UTILI AL LOG PER CONOSCERE IL NUMERO DI RECORD AGGIORNATI
        int countMacchinaOri = 0;
        int countAggiornamentoConfigOri = 0;
        int countChimicaOri = 0;
        int countComponenteOri = 0;
        int countColoreOri = 0;
        int countColoreBaseOri = 0;
        int countMazzettaOri = 0;
        int countMazzettaColorataOri = 0;
        int countCategoriaOri = 0;
        int countParametroCompProdOri = 0;
        int countProdottoOri = 0;
        int countComponenteProdottoOri = 0;
        int countDizionarioOri = 0;
        int countNumSacchettoOri = 0;
        int countParametroGlobMacOri = 0;
        int countPresaOri = 0;
        int countValoreParProdOri = 0;
        int countValoreParSacchettoOri = 0;
        int countValoreRipristinoOri = 0;
        int countValoreParSingMacOri = 0;
        int countValoreParCompOri = 0;
        int countValoreProdottoOri = 0;
        int countValoreParProdMacOri = 0;
        int countMovSingMacOri = 0;
        int countComponentePesaturaOri = 0;
        int countAllarmeOri = 0;
        int countOrdineSingMacOri = 0;
        int countValoreParOrdineOri = 0;
        int countFiguraTipoOri = 0;
        int countFiguraOri = 0;

        for (Object obj : aggiornamentoOri.getDaInserire()) {

            if (obj instanceof MacchinaOri) {
                MacchinaOri macchinaOri = (MacchinaOri) obj;
                macchinaOriJc.merge(macchinaOri);
                countMacchinaOri++;
                log.info("NUMERO " + countMacchinaOri + " MacchinaOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countMacchinaOri + " MacchinaOri ");
            }

            if (obj instanceof AggiornamentoConfigOri) {

                AggiornamentoConfigOri aggiornamentoConfigOri = (AggiornamentoConfigOri) obj;
                aggiornamentoConfigOriJc.merge(aggiornamentoConfigOri);
                countAggiornamentoConfigOri++;
                log.info("NUMERO " + countAggiornamentoConfigOri + " AggiornamentoConfigOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countAggiornamentoConfigOri + " AggiornamentoConfigOri ");
            }

            if (obj instanceof ChimicaOri) {
                ChimicaOri chimicaOri = (ChimicaOri) obj;
                chimicaOriJc.merge(chimicaOri);
                countChimicaOri++;
                log.info("NUMERO " + countChimicaOri + " ChimicaOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countChimicaOri + " ChimicaOri ");
            }

            if (obj instanceof ComponenteOri) {
                ComponenteOri componenteOri = (ComponenteOri) obj;
                componenteOriJc.merge(componenteOri);
                countComponenteOri++;
                log.info("NUMERO " + countComponenteOri + " ComponenteOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countComponenteOri + " ComponenteOri ");
            }

            if (obj instanceof CategoriaOri) {
                CategoriaOri categoriaOri = (CategoriaOri) obj;
                categoriaOriJc.merge(categoriaOri);
                countCategoriaOri++;
                log.info("NUMERO " + countCategoriaOri + " CategoriaOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countMazzettaColorataOri + " CategoriaOri");
            }

            if (obj instanceof ParametroCompProdOri) {
                ParametroCompProdOri parametroCompProdOri = (ParametroCompProdOri) obj;
                parametroCompProdOriJc.merge(parametroCompProdOri);
                countParametroCompProdOri++;
                log.info("NUMERO " + countParametroCompProdOri + " ParametroCompProdOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countParametroCompProdOri + " ParametroCompProdOri");
            }

            if (obj instanceof ProdottoOri) {
                ProdottoOri prodottoOri = (ProdottoOri) obj;
                prodottoOriJc.merge(prodottoOri);
                countProdottoOri++;
                log.info("NUMERO " + countProdottoOri + " ProdottoOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati  " + countProdottoOri + " ProdottoOri");
            }

            if (obj instanceof ComponenteProdottoOri) {
                ComponenteProdottoOri componenteProdottoOri = (ComponenteProdottoOri) obj;
                componenteProdottoOriJc.merge(componenteProdottoOri);
                countComponenteProdottoOri++;
                log.info("NUMERO " + countComponenteProdottoOri + " ComponenteProdottoOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countComponenteProdottoOri + " ComponenteProdottoOri");
            }

            if (obj instanceof PresaOri) {
                PresaOri presaOri = (PresaOri) obj;
                presaOriJc.merge(presaOri);
                countPresaOri++;
                log.info("NUMERO " + countPresaOri + " PresaOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countPresaOri + " PresaOri");
            }

            if (obj instanceof DizionarioOri) {
                DizionarioOri dizionarioOri = (DizionarioOri) obj;
                dizionarioOriJc.merge(dizionarioOri);
                countDizionarioOri++;
                log.info("NUMERO " + countDizionarioOri + " DizionarioOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countDizionarioOri + " DizionarioOri");
            }

            if (obj instanceof NumSacchettoOri) {
                NumSacchettoOri numSacchettoOri = (NumSacchettoOri) obj;
                numSacchettoOriJc.merge(numSacchettoOri);
                countNumSacchettoOri++;
                log.info("NUMERO " + countNumSacchettoOri + " NumSacchettoOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati  " + countNumSacchettoOri + " NumSacchettoOri");
            }

            if (obj instanceof ParametroGlobMacOri) {
                ParametroGlobMacOri parametroGlobMacOri = (ParametroGlobMacOri) obj;
                parametroGlobMacOriJc.merge(parametroGlobMacOri);
                countParametroGlobMacOri++;
                log.info("NUMERO " + countParametroGlobMacOri + " ParametroGlobMacOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati  " + countParametroGlobMacOri + " ParametroGlobMacOri");
            }

            if (obj instanceof ValoreParProdOri) {
                ValoreParProdOri valoreParProdOri = (ValoreParProdOri) obj;
                valoreParProdOriJc.merge(valoreParProdOri);
                countValoreParProdOri++;
                log.info("NUMERO " + countValoreParProdOri + " ValoreParProdOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreParProdOri + "  ValoreParProdOri");
            }

            if (obj instanceof ValoreParSacchettoOri) {
                ValoreParSacchettoOri valoreParSacchettoOri = (ValoreParSacchettoOri) obj;
                valoreParSacchettoOriJc.merge(valoreParSacchettoOri);
                countValoreParSacchettoOri++;
                log.info("NUMERO " + countValoreParSacchettoOri + " ValoreParSacchettoOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreParSacchettoOri + " ValoreParSacchettoOri");
            }
//TODO : forse è il caso di istanziare un nuovo oggetto  ValoreRipristinoOri 
            //settando solo i campi che si intendono modificare e poi salvarlo      
            if (obj instanceof ValoreRipristinoOri) {

                ValoreRipristinoOri valoreRipristinoOriServer = (ValoreRipristinoOri) obj;

                //Recupero dal database l'entità con id corrispondente a quello presente nell'aggiornamento
                ValoreRipristinoOri valoreRipristinoOri = valoreRipristinoOriJc.findValoreRipristinoOri(valoreRipristinoOriServer.getIdValoreRipristino());

                //Se l'entità esiste già nel db locale modifico solo i campi nuovi e salvo
                if (valoreRipristinoOri != null) {
//
                    valoreRipristinoOri.setValoreVariabile(valoreRipristinoOriServer.getValoreVariabile());
                    valoreRipristinoOri.setDtAbilitato(new Date());
//
                    //Modifico l'entità esistente
                    valoreRipristinoOriJc.edit(valoreRipristinoOri);

//                    
                } else {
//                    Se l'entità non è presente nel db                     
                    if (valoreRipristinoOri == null) {
                        throw new InvalidUpdateContentException("Attenzione l'oggetto ValoreRipristinoOri che si intende aggiornare non è presente nel db!");
                    }
                }
                countValoreRipristinoOri++;
                log.info("NUMERO " + countValoreRipristinoOri + " ValoreRipristinoOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreRipristinoOri + " ValoreRipristinoOri");

            }

            //TODO : forse è il caso di istanziare un nuovo oggetto  ValoreParSingMacOri 
            //settando solo i campi che si intendono modificare e poi salvarlo      
            if (obj instanceof ValoreParSingMacOri) {
                ValoreParSingMacOri valoreParSingMacOriServer = (ValoreParSingMacOri) obj;

                //Recupero dal database l'entità con id corrispondente a quello presente nell'aggiornamento
                ValoreParSingMacOri valoreParSingMacOri = valoreParSingMacOriJc.findValoreParSingMacOri(valoreParSingMacOriServer.getIdValParSm());
                //Se l'entità esiste già nel db locale modifico solo i campi nuovi e salvo
                if (valoreParSingMacOri != null) {

                    valoreParSingMacOri.setValoreVariabile(valoreParSingMacOriServer.getValoreVariabile());
                    valoreParSingMacOri.setValoreIniziale(valoreParSingMacOriServer.getValoreIniziale());
                    valoreParSingMacOri.setDtValoreIniziale(valoreParSingMacOriServer.getDtValoreIniziale());
                    valoreParSingMacOri.setDtAbilitato(new Date());

                    //Modifico l'entità esistente
                    valoreParSingMacOriJc.edit(valoreParSingMacOri);

                } else {
                    //Se l'entità non è presente nel db ne creo una nuova
                    //Assegno la data corrente al campo dt_abilitato
                    //il campo dt_modifica mac dovrebbe mantenere il valore di default
                    //e salvo
                    valoreParSingMacOriServer.setDtAbilitato(new Date());
                    valoreParSingMacOriJc.create(valoreParSingMacOriServer);

                }
                countValoreParSingMacOri++;
                log.info("NUMERO " + countValoreParSingMacOri + " ValoreParSingMacOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreParSingMacOri + " ValoreParSingMacOri");

            }

            if (obj instanceof ValoreParCompOri) {
                ValoreParCompOri valoreParCompOriServer = (ValoreParCompOri) obj;

                //Recupero dal database l'entità con id corrispondente a quello presente nell'aggiornamento
                ValoreParCompOri valoreParCompOri = valoreParCompOriJc.findValoreParCompOri(valoreParCompOriServer.getIdValComp());
                
                //Portare fuori!
                int idParPresa = 1;
                int idParCodComp = 6;

                //Se l'entità esiste già nel db locale modifico solo i campi nuovi e salvo
                if (valoreParCompOri != null) {

                //Modifica del 28-02-2018 da testare
                    //Se il suo id è diverso dalla presa e dal codice componente
                    //modifico solo i campi nuovi e salvo
                    if (valoreParCompOri.getIdParComp() != idParPresa && valoreParCompOri.getIdParComp() != idParCodComp) {

                        valoreParCompOri.setValoreVariabile(valoreParCompOriServer.getValoreVariabile());
                        valoreParCompOri.setDtAbilitato(new Date());

                        //Modifico l'entità esistente
                        valoreParCompOriJc.edit(valoreParCompOri);
                    }
                    
                } else {

                    //Se l'entità non è presente nel db ne creo una nuova
                    //Assegno la data corrente al campo dt_abilitato
                    //il campo dt_modifica mac dovrebbe mantenere il valore di default
                    //e salvo
                    valoreParCompOriServer.setDtAbilitato(new Date());
                    valoreParCompOriJc.create(valoreParCompOriServer);
//                    if (valoreParCompOri == null) {
//                        throw new InvalidUpdateContentException(" Attenzione l'oggetto ValoreParCompOri che si intende aggiornare non è presente nel db!");
//                    }
                }

                countValoreParCompOri++;
                log.info("NUMERO " + countValoreParCompOri + " ValoreParCompOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreParCompOri + " ValoreParCompOri");

            }

            if (obj instanceof ValoreProdottoOri) {
                ValoreProdottoOri valoreProdottoOriServer = (ValoreProdottoOri) obj;

                //Recupero dal database l'entità con id corrispondente a quello presente nell'aggiornamento
                ValoreProdottoOri valoreProdottoOri = valoreProdottoOriJc.findValoreProdottoOri(valoreProdottoOriServer.getIdValPr());
                //Se l'entità esiste già nel db locale modifico solo i campi nuovi e salvo
                if (valoreProdottoOri != null) {

                    valoreProdottoOri.setValoreVariabile(valoreProdottoOriServer.getValoreVariabile());
                    valoreProdottoOri.setDtAbilitato(new Date());

                    //Modifico l'entità esistente
                    valoreProdottoOriJc.edit(valoreProdottoOri);

                } else {
                    //Se l'entità non è presente nel db ne creo una nuova
                    //Assegno la data corrente al campo dt_abilitato e salvo
                    valoreProdottoOriServer.setDtAbilitato(new Date());
                    valoreProdottoOriJc.create(valoreProdottoOriServer);

                }
                countValoreProdottoOri++;
                log.info("NUMERO " + countValoreProdottoOri + " ValoreParSingMacOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreProdottoOri + " valoreProdottoOri");

            }

            if (obj instanceof ValoreParProdMacOri) {

                ValoreParProdMacOri valoreParProdMacOriServer = (ValoreParProdMacOri) obj;

                //Recupero dal database l'entità con id corrispondente a quello presente nell'aggiornamento
                ValoreParProdMacOri valoreParProdMacOri = valoreParProdMacOriJc.findValoreParProdMacOri(valoreParProdMacOriServer.getIdValPm());
                //Se l'entità esiste già nel db locale modifico solo i campi nuovi e salvo
                if (valoreParProdMacOri != null) {

                    valoreParProdMacOri.setValoreVariabile(valoreParProdMacOriServer.getValoreVariabile());
                    valoreParProdMacOri.setDtAbilitato(new Date());

                    //Modifico l'entità esistente
                    valoreParProdMacOriJc.edit(valoreParProdMacOri);

                } else {
                    //Se l'entità non è presente nel db ne creo una nuova
                    //Assegno la data corrente al campo dt_abilitato e salvo
                    valoreParProdMacOriServer.setDtAbilitato(new Date());
                    valoreParProdMacOriJc.create(valoreParProdMacOriServer);

                }

                countValoreParProdMacOri++;
                log.info("NUMERO " + countValoreParProdMacOri + " ValoreParProdMacOriJc AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreParProdMacOri + " valoreParProdMacOri");

            }

            if (obj instanceof AllarmeOri) {
                AllarmeOri allarmeOri = (AllarmeOri) obj;
                allarmeOriJc.merge(allarmeOri);
                countAllarmeOri++;
                log.info("NUMERO " + countAllarmeOri + " AllarmeOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countAllarmeOri + " AllarmeOri ");
            }

            if (obj instanceof MovimentoSingMacOri) {

                log.info("TROVATO MOVIMENTO #@m");

                MovimentoSingMacOri movimentoSingMacOri = (MovimentoSingMacOri) obj;
                movimentoSingMacOriJc.merge(movimentoSingMacOri);
                countMovSingMacOri++;
                log.info("NUMERO " + countMovSingMacOri + " MovimentoSingMacOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countMovSingMacOri + " MovimentoSingMacOri ");
            }

            if (obj instanceof ComponentePesaturaOri) {
                ComponentePesaturaOri componentePesaturaOri = (ComponentePesaturaOri) obj;
                componentePesaturaOriJc.merge(componentePesaturaOri);
                countComponentePesaturaOri++;
                log.info("NUMERO " + countComponentePesaturaOri + " ComponentePesaturaOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countComponentePesaturaOri + " ComponentePesaturaOri ");
            }

            if (obj instanceof OrdineSingMacOri) {
                OrdineSingMacOri ordineSingMacOri = (OrdineSingMacOri) obj;
                ordineSingMacOriJc.merge(ordineSingMacOri);
                countComponentePesaturaOri++;
                log.info("NUMERO " + countOrdineSingMacOri + " OrdineSingMacOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countOrdineSingMacOri + " OrdineSingMacOri ");
            }
            if (obj instanceof ValoreParOrdineOri) {
                ValoreParOrdineOri valoreParOrdineOri = (ValoreParOrdineOri) obj;
                valoreParOrdineOriJc.merge(valoreParOrdineOri);
                countValoreParProdOri++;
                log.info("NUMERO " + countValoreParOrdineOri + " ValoreParOrdineOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvati " + countValoreParOrdineOri + "  ValoreParOrdineOri");
            }

            if (obj instanceof FiguraTipoOri) {
                FiguraTipoOri figuraTipoOri = (FiguraTipoOri) obj;
                figuraTipoOriJc.merge(figuraTipoOri);
                countFiguraTipoOri++;
                log.info("NUMERO " + countFiguraTipoOri + " FiguraTipoOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countFiguraTipoOri + " FiguraTipoOri ");
            }

            if (obj instanceof FiguraOri) {
                FiguraOri figuraOri = (FiguraOri) obj;
                figuraOriJc.merge(figuraOri);
                countFiguraOri++;
                log.info("NUMERO " + countFiguraOri + " FiguraOri AGGIORNATO/I!");
                panelAgg.inserisciRiga("Salvato " + countFiguraOri + " FiguraOri ");
            }
        }

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 390, ParametriSingolaMacchina.parametri.get(111))) // "Report file : 
                + " "
                + aggiornamentoOri.getNomeFile()
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 368, ParametriSingolaMacchina.parametri.get(111))) //Numero di rercord di Macchina Aggiornati :
                + " "
                + countMacchinaOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 369, ParametriSingolaMacchina.parametri.get(111))) // "Numero di Parametri di configurazione dell'aggiornamento aggiornati :"
                + " "
                + countAggiornamentoConfigOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 370, ParametriSingolaMacchina.parametri.get(111))) // "Numero di Chimiche aggiornate :"
                + " "
                + countChimicaOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 371, ParametriSingolaMacchina.parametri.get(111))) // "Numero di Componenti aggiornati : 
                + " "
                + countComponenteOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 372, ParametriSingolaMacchina.parametri.get(111))) // "Numero di Colori aggiornati :"
                + " "
                + countColoreOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 373, ParametriSingolaMacchina.parametri.get(111))) // "Numero di Colori  Base aggiornati :"
                + " "
                + countColoreBaseOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 374, ParametriSingolaMacchina.parametri.get(111))) // "Numero di mazzette aggiornate :"
                + " "
                + countMazzettaOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 375, ParametriSingolaMacchina.parametri.get(111))) // "Numero di mazzette Colorate aggiornate :"
                + " "
                + countMazzettaColorataOri
                + HTML_STRINGA_FINE);
        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 376, ParametriSingolaMacchina.parametri.get(111))) // "Numero di categorie aggiornate :"
                + " "
                + countCategoriaOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 377, ParametriSingolaMacchina.parametri.get(111))) // "Numero di parametri componenti prodotto aggiornati :"
                + " "
                + countParametroCompProdOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 378, ParametriSingolaMacchina.parametri.get(111))) // "Numero di prodotti aggiornati :"
                + " "
                + countProdottoOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 379, ParametriSingolaMacchina.parametri.get(111))) // "Numero di componenti prodotto aggiornati :"
                + " "
                + countComponenteProdottoOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 380, ParametriSingolaMacchina.parametri.get(111))) // "Numero di prese aggiornate :"
                + " "
                + countPresaOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 381, ParametriSingolaMacchina.parametri.get(111))) // "Numero di vocaboli aggiornati :"
                + " "
                + countDizionarioOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 382, ParametriSingolaMacchina.parametri.get(111))) // "Numero di soluzioni confezione aggiornate :"
                + " "
                + countNumSacchettoOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 383, ParametriSingolaMacchina.parametri.get(111))) // "Numero di parametri globali macchina aggiornati :"
                + " "
                + countParametroGlobMacOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 384, ParametriSingolaMacchina.parametri.get(111))) // "Numero di valori dei parametri dei prodotti aggiornati :"
                + " "
                + countValoreParProdOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 385, ParametriSingolaMacchina.parametri.get(111))) // "Numero di valori dei parametri di confezione aggiornati :"
                + " "
                + countValoreParSacchettoOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 386, ParametriSingolaMacchina.parametri.get(111))) // "Numero di valori di ripristino aggiornati:"
                + " "
                + countValoreRipristinoOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 387, ParametriSingolaMacchina.parametri.get(111)))// "Numero di valori dei parametri singola macchina aggiornati :"
                + " "
                + countValoreParSingMacOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 388, ParametriSingolaMacchina.parametri.get(111)))// "Numero di valori dei parametri componenti aggiornati :"
                + " "
                + countValoreParCompOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 797, ParametriSingolaMacchina.parametri.get(111)))// "Numero di valori prodotto aggiornati :"
                + " "
                + countValoreProdottoOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 798, ParametriSingolaMacchina.parametri.get(111)))// "Numero di valori prodotto aggiornati :"
                + " "
                + countValoreParProdMacOri
                + HTML_STRINGA_FINE);

        panelAgg.inserisciRiga(HTML_STRINGA_INIZIO
                + EstraiStringaHtml(TrovaVocabolo(ID_DIZIONARIO_MESSAGGI_MACCHINA, 389, ParametriSingolaMacchina.parametri.get(111))) // "Fine report del File :"
                + " "
                + aggiornamentoOri.getNomeFile()
                + HTML_STRINGA_FINE);

        log.info("############ FINE SALVATAGGIO DATI SU ORIGAMIDB #############");
        log.info("############ VERSIONE AGGIORNAMENTO : " + machineCredentials.getNewRemoteUpdateVersion() + " ############");
        log.info("############ NOME FILE : " + aggiornamentoOri.getNomeFile() + " #############");

        log.info("NUMERO " + countMacchinaOri + " MacchinaOri AGGIORNATO/I!");
        log.info("NUMERO " + countAggiornamentoConfigOri + " AggiornamentoConfigOri AGGIORNATO/I!");
        log.info("NUMERO " + countChimicaOri + " ChimicaOri AGGIORNATO/I!");
        log.info("NUMERO " + countComponenteOri + " ComponenteOri AGGIORNATO/I!");
        log.info("NUMERO " + countColoreOri + " ColoreOri AGGIORNATO/I!");
        log.info("NUMERO " + countColoreBaseOri + " ColoreBaseOri AGGIORNATO/I!");
        log.info("NUMERO " + countMazzettaOri + " MazzettaOri AGGIORNATO/I!");
        log.info("NUMERO " + countMazzettaColorataOri + " MazzettaColorataOri AGGIORNATO/I!");
        log.info("NUMERO " + countCategoriaOri + " CategoriaOri AGGIORNATO/I!");
        log.info("NUMERO " + countParametroCompProdOri + " ParametroCompProdOri AGGIORNATO/I!");
        log.info("NUMERO " + countProdottoOri + " ProdottoOri AGGIORNATO/I!");
        log.info("NUMERO " + countComponenteProdottoOri + " ComponenteProdottoOri AGGIORNATO/I!");
        log.info("NUMERO " + countPresaOri + " PresaOri AGGIORNATO/I!");
        log.info("NUMERO " + countDizionarioOri + " DizionarioOri AGGIORNATO/I!");
        log.info("NUMERO " + countNumSacchettoOri + " NumSacchettoOri AGGIORNATO/I!");
        log.info("NUMERO " + countParametroGlobMacOri + " ParametroGlobMacOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreParProdOri + " ValoreParProdOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreParSacchettoOri + " ValoreParSacchettoOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreRipristinoOri + " ValoreRipristinoOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreParSingMacOri + " ValoreParSingMacOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreParCompOri + " ValoreParCompOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreProdottoOri + " ValoreProdottoOri AGGIORNATO/I!");
        log.info("NUMERO " + countValoreParProdMacOri + " ValoreParProdMacOri AGGIORNATO/I!");

        log.info("NUMERO " + countMovSingMacOri + " MovimentoSingMacOri AGGIORNATO/I!");
        log.info("NUMERO " + countAllarmeOri + " AllarmeOri AGGIORNATO/I!");
        log.info("NUMERO " + countComponentePesaturaOri + " ComponentePesaturaOri AGGIORNATO/I!");

    }

    /**
     * Metodo che salva le informazioni relative all'aggiornamento nella tabella
     * AggiornamentoOri
     *
     * @param aggiornamentoOri
     */
    public void salvaAggiornamentoOriIn(
            AggiornamentoOri aggiornamentoOri,
            EntityManagerFactory emf) {

        AggiornamentoOriJpaController aggiornamentoJc = new AggiornamentoOriJpaController(null, emf);

        //Setto la data corrente dell'aggiornamento ed il nome del file
        aggiornamentoOri.setDtAggiornamento(new Date());

        aggiornamentoJc.create(aggiornamentoOri);

        log.info("############ SALVATAGGIO AGGIORNAMENTO_ORI IN  EFFETTUATO ##############");
    }

    /**
     * Metodo che costruisce un oggetto AggiornamentoOri con una collection
     * contenente il backup di origamidb
     *
     * @param outFilePfx
     * @param emf
     * @return
     */
    @SuppressWarnings("unchecked")
    public AggiornamentoOri costruisciBackupDbMacchina(String outFilePfx, EntityManagerFactory emf) {

        AggiornamentoOri aggiornamentoOriOut = new AggiornamentoOri();

        AggiornamentoConfigOriJpaController aggiornamentoConfigOriJc = new AggiornamentoConfigOriJpaController(null, emf);
        AggiornamentoOriJpaController aggiornamentoOriJc = new AggiornamentoOriJpaController(null, emf);
        CategoriaOriJpaController categoriaOriJc = new CategoriaOriJpaController(null, emf);
        ChimicaOriJpaController chimicaJc = new ChimicaOriJpaController(null, emf);
        ComponenteOriJpaController componenteOriJc = new ComponenteOriJpaController(null, emf);
        ComponenteProdottoOriJpaController componenteProdottoOriJc = new ComponenteProdottoOriJpaController(null, emf);
        DizionarioOriJpaController dizionarioOriJc = new DizionarioOriJpaController(null, emf);
        MacchinaOriJpaController macchinaOriJc = new MacchinaOriJpaController(null, emf);
        NumSacchettoOriJpaController numSacchettoOriJc = new NumSacchettoOriJpaController(null, emf);
        ParametroCompProdOriJpaController parametroCompProdOriJc = new ParametroCompProdOriJpaController(null, emf);
        ParametroGlobMacOriJpaController parametroGlobMacOriJc = new ParametroGlobMacOriJpaController(null, emf);
        PresaOriJpaController presaOriJc = new PresaOriJpaController(null, emf);
        ProcessoOriJpaController processoOriJc = new ProcessoOriJpaController(null, emf);
        ProdottoOriJpaController prodottoOriJc = new ProdottoOriJpaController(null, emf);
        ValoreParProdOriJpaController valoreParProdOriJc = new ValoreParProdOriJpaController(null, emf);
        ValoreParCompOriJpaController valoreParCompOriJc = new ValoreParCompOriJpaController(null, emf);
        ValoreParSacchettoOriJpaController valoreParSacchettoOriJc = new ValoreParSacchettoOriJpaController(null, emf);
        ValoreParSingMacOriJpaController valoreParSingMacOriJc = new ValoreParSingMacOriJpaController(null, emf);
        ValoreRipristinoOriJpaController valoreRipristinoOriJc = new ValoreRipristinoOriJpaController(null, emf);
        //22-10-2015
        ValoreProdottoOriJpaController valoreProdottoOriJc = new ValoreProdottoOriJpaController(null, emf);
        ValoreParProdMacOriJpaController valoreParProdMacOriJc = new ValoreParProdMacOriJpaController(null, emf);

        aggiornamentoOriOut.setDaInserire(new ArrayList());

        //Recupero tutti i dati della tab macchina_ori e li aggiungo alla collection DaInserire    
        Collection<MacchinaOri> macchinaOriColl = macchinaOriJc.findMacchinaOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(macchinaOriColl);

        //Recupero tutti i dati della tab aggiornamento_config_ori e li aggiungo alla collection DaInserire    
        Collection<AggiornamentoConfigOri> aggiornamentoConfigOriColl = aggiornamentoConfigOriJc.findAggiornamentoConfigOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(aggiornamentoConfigOriColl);

        //Recupero tutti dati i della tab aggiornamento_ori e li aggiungo alla collection DaInserire    
        Collection<AggiornamentoOri> aggiornamentoOriColl = aggiornamentoOriJc.findAggiornamentoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(aggiornamentoOriColl);

        //Recupero tutti dati i della tab categoria_ori e li aggiungo alla collection DaInserire    
        Collection<CategoriaOri> categoriaOriColl = categoriaOriJc.findCategoriaOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(categoriaOriColl);

        //Recupero tutti dati i della tab chimica_ori e li aggiungo alla collection DaInserire    
        Collection<ChimicaOri> chimicaOriColl = chimicaJc.findChimicaOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(chimicaOriColl);

        //Recupero tutti dati i della tab componente_ori e li aggiungo alla collection DaInserire    
        Collection<ComponenteOri> componenteOriColl = componenteOriJc.findComponenteOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(componenteOriColl);

        //Recupero tutti dati i della tab componente_prodotto_ori e li aggiungo alla collection DaInserire    
        Collection<ComponenteProdottoOri> componenteProdottoOriColl = componenteProdottoOriJc.findComponenteProdottoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(componenteProdottoOriColl);

        //Recupero tutti dati i della tab dizionario_ori e li aggiungo alla collection DaInserire    
        Collection<DizionarioOri> dizionarioOriColl = dizionarioOriJc.findDizionarioOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(dizionarioOriColl);

        //Recupero tutti i dati della tab num_sacchetto_ori e li aggiungo alla collection DaInserire    
        Collection<NumSacchettoOri> numSacchettoOriColl = numSacchettoOriJc.findNumSacchettoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(numSacchettoOriColl);

        //Recupero tutti i dati della tab parametro_comp_prod_ori e li aggiungo alla collection DaInserire    
        Collection<ParametroCompProdOri> parametroCompProdOriColl = parametroCompProdOriJc.findParametroCompProdOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(parametroCompProdOriColl);

        //Recupero tutti i dati della tab parametro_glob_mac_ori e li aggiungo alla collection DaInserire    
        Collection<ParametroGlobMacOri> parametroGlobMacOriColl = parametroGlobMacOriJc.findParametroGlobMacOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(parametroCompProdOriColl);

        //Recupero tutti i dati della tab presa_ori e li aggiungo alla collection DaInserire    
        Collection<PresaOri> presaOriColl = presaOriJc.findPresaOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(presaOriColl);

        //Recupero tutti i dati della tab processo_ori e li aggiungo alla collection DaInserire    
        Collection<ProcessoOri> processoOriColl = processoOriJc.findProcessoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(processoOriColl);

        //Recupero tutti i dati della tab prodotto_ori e li aggiungo alla collection DaInserire    
        Collection<ProdottoOri> prodottoOriColl = prodottoOriJc.findProdottoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(processoOriColl);

        //Recupero tutti i dati dalla tab valore_par_prod_ori ed aggiungo alla collection DaInserire 
        Collection<ValoreParProdOri> valoreParProdOriColl = valoreParProdOriJc.findValoreParProdOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreParProdOriColl);

        //Recupero tutti i dati dalla tab valore_par_comp_ori ed aggiungo alla collection DaInserire 
        Collection<ValoreParCompOri> valoreParCompOriColl = valoreParCompOriJc.findValoreParCompOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreParCompOriColl);

        //Recupero tutti i dati dalla tab valore_par_sacchetto_ori ed aggiungo alla collection DaInserire 
        Collection<ValoreParSacchettoOri> valoreParSacchettoOriColl = valoreParSacchettoOriJc.findValoreParSacchettoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreParSacchettoOriColl);

        //Recupero tutti i dati dalla tab valore_ripristino_ori ed aggiungo alla collection DaInserire 
        Collection<ValoreRipristinoOri> valoreRipristinoOriColl = valoreRipristinoOriJc.findValoreRipristinoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreRipristinoOriColl);

        //Recupero tutti i dati della tab valore_par_sing_mac_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreParSingMacOri> valoreParSingMacOriColl = valoreParSingMacOriJc.findValoreParSingMacOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreParSingMacOriColl);

        //Recupero tutti i dati della tab valore_prodotto_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreProdottoOri> valoreProdottoOriColl = valoreProdottoOriJc.findValoreProdottoOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreProdottoOriColl);

        //Recupero tutti i dati della tab valore_par_prod_mac_ori e li aggiungo alla collection DaInserire    
        Collection<ValoreParProdMacOri> valoreParProdMacOriColl = valoreParProdMacOriJc.findValoreParProdMacOriAll();
        aggiornamentoOriOut.getDaInserire().addAll(valoreParProdMacOriColl);

//    Collection c = aggiornamentoOriOut.getDaInserire();
//    int count = 0;
//
//    for (Object o : c) {
//      if (o != null) {
//        log.info(o.getClass().cast(o).toString());
//      } else {
//        log.info("AGGIORNAMENTO_OUT - TROVATO/I n. " + count++ + " OGGETTO/I NULL!!!");
//      }
//    }
        log.info("DATA DI COSTRUZIONE DELL'ULTIMO BACKUP : " + machineCredentials.getLastUpdateDate().toString());
        log.info("ULTIMA VERSIONE AGGIORNAMENTO DI TIPO BACKUP : " + machineCredentials.getLastUpdateVersion());
        //Setto i campi dell'oggetto aggiornamento
        //Calcolo la nuova versione
        //Salvo nel db
        aggiornamentoOriOut.setTipo(outFilePfx);
        aggiornamentoOriOut.setDtAggiornamento(new Date());
        aggiornamentoOriOut.setVersione(machineCredentials.getLastUpdateVersion() + 1);

        return aggiornamentoOriOut;

    }
}
