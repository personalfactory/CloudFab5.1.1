/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.personalfactory.cloudfab.macchina.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Francesco Di Gaudio
 */
@Entity
@Table(name = "ordine_sing_mac_ori")
@XmlRootElement
@NamedQueries({
   /** @NamedQuery(name = "OrdineSingMacOri.findAll", query = "SELECT o FROM OrdineSingMacOri o")
    , @NamedQuery(name = "OrdineSingMacOri.findByIdOrdineSm", query = "SELECT o FROM OrdineSingMacOri o WHERE o.idOrdineSm = :idOrdineSm")
    , @NamedQuery(name = "OrdineSingMacOri.findByIdOrdine", query = "SELECT o FROM OrdineSingMacOri o WHERE o.idOrdine = :idOrdine")
    , @NamedQuery(name = "OrdineSingMacOri.findByIdProdotto", query = "SELECT o FROM OrdineSingMacOri o WHERE o.idProdotto = :idProdotto")
    , @NamedQuery(name = "OrdineSingMacOri.findByOrdineProduzione", query = "SELECT o FROM OrdineSingMacOri o WHERE o.ordineProduzione = :ordineProduzione")
    , @NamedQuery(name = "OrdineSingMacOri.findByContatore", query = "SELECT o FROM OrdineSingMacOri o WHERE o.contatore = :contatore")
    , @NamedQuery(name = "OrdineSingMacOri.findByNumPezzi", query = "SELECT o FROM OrdineSingMacOri o WHERE o.numPezzi = :numPezzi")
    , @NamedQuery(name = "OrdineSingMacOri.findByStato", query = "SELECT o FROM OrdineSingMacOri o WHERE o.stato = :stato")
    , @NamedQuery(name = "OrdineSingMacOri.findByAbilitato", query = "SELECT o FROM OrdineSingMacOri o WHERE o.abilitato = :abilitato")
    , @NamedQuery(name = "OrdineSingMacOri.findByDtProduzione", query = "SELECT o FROM OrdineSingMacOri o WHERE o.dtProduzione = :dtProduzione")
    , @NamedQuery(name = "OrdineSingMacOri.findByDtProgrammata", query = "SELECT o FROM OrdineSingMacOri o WHERE o.dtProgrammata = :dtProgrammata")
    , @NamedQuery(name = "OrdineSingMacOri.findByDtAbilitato", query = "SELECT o FROM OrdineSingMacOri o WHERE o.dtAbilitato = :dtAbilitato")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo1", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info1 = :info1")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo2", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info2 = :info2")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo3", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info3 = :info3")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo4", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info4 = :info4")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo5", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info5 = :info5")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo6", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info6 = :info6")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo7", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info7 = :info7")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo8", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info8 = :info8")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo9", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info9 = :info9")
    , @NamedQuery(name = "OrdineSingMacOri.findByInfo10", query = "SELECT o FROM OrdineSingMacOri o WHERE o.info10 = :info10"),*/
@NamedQuery(name = "OrdineSingMacOri.findDatiNuovi", query = "SELECT v FROM OrdineSingMacOri v WHERE v.dtProduzione > :dtProduzione")})
public class OrdineSingMacOri implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_ordine_sm")
    private Integer idOrdineSm;
    @Column(name = "id_ordine")
    private Integer idOrdine;
    @Column(name = "id_prodotto")
    private Integer idProdotto;
    @Column(name = "ordine_produzione")
    private Integer ordineProduzione;
    @Column(name = "contatore")
    private Integer contatore;
    @Column(name = "num_pezzi")
    private Integer numPezzi;
    @Size(max = 45)
    @Column(name = "stato")
    private String stato;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @Column(name = "dt_produzione")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtProduzione;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_programmata")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtProgrammata;
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
    @Size(max = 255)
    @Column(name = "info1")
    private String info1;
    @Size(max = 255)
    @Column(name = "info2")
    private String info2;
    @Size(max = 255)
    @Column(name = "info3")
    private String info3;
    @Size(max = 255)
    @Column(name = "info4")
    private String info4;
    @Size(max = 255)
    @Column(name = "info5")
    private String info5;
    @Size(max = 255)
    @Column(name = "info6")
    private String info6;
    @Size(max = 255)
    @Column(name = "info7")
    private String info7;
    @Size(max = 255)
    @Column(name = "info8")
    private String info8;
    @Size(max = 255)
    @Column(name = "info9")
    private String info9;
    @Size(max = 255)
    @Column(name = "info10")
    private String info10;

    public OrdineSingMacOri() {
    }

    public OrdineSingMacOri(Integer idOrdineSm) {
        this.idOrdineSm = idOrdineSm;
    }

    public OrdineSingMacOri(Integer idOrdineSm, Date dtProgrammata) {
        this.idOrdineSm = idOrdineSm;
        this.dtProgrammata = dtProgrammata;
    }

    public Integer getIdOrdineSm() {
        return idOrdineSm;
    }

    public void setIdOrdineSm(Integer idOrdineSm) {
        this.idOrdineSm = idOrdineSm;
    }

    public Integer getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Integer getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public Integer getOrdineProduzione() {
        return ordineProduzione;
    }

    public void setOrdineProduzione(Integer ordineProduzione) {
        this.ordineProduzione = ordineProduzione;
    }

    public Integer getContatore() {
        return contatore;
    }

    public void setContatore(Integer contatore) {
        this.contatore = contatore;
    }

    public Integer getNumPezzi() {
        return numPezzi;
    }

    public void setNumPezzi(Integer numPezzi) {
        this.numPezzi = numPezzi;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
    }

    public Date getDtProduzione() {
        return dtProduzione;
    }

    public void setDtProduzione(Date dtProduzione) {
        this.dtProduzione = dtProduzione;
    }

    public Date getDtProgrammata() {
        return dtProgrammata;
    }

    public void setDtProgrammata(Date dtProgrammata) {
        this.dtProgrammata = dtProgrammata;
    }

    public Date getDtAbilitato() {
        return dtAbilitato;
    }

    public void setDtAbilitato(Date dtAbilitato) {
        this.dtAbilitato = dtAbilitato;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public String getInfo3() {
        return info3;
    }

    public void setInfo3(String info3) {
        this.info3 = info3;
    }

    public String getInfo4() {
        return info4;
    }

    public void setInfo4(String info4) {
        this.info4 = info4;
    }

    public String getInfo5() {
        return info5;
    }

    public void setInfo5(String info5) {
        this.info5 = info5;
    }

    public String getInfo6() {
        return info6;
    }

    public void setInfo6(String info6) {
        this.info6 = info6;
    }

    public String getInfo7() {
        return info7;
    }

    public void setInfo7(String info7) {
        this.info7 = info7;
    }

    public String getInfo8() {
        return info8;
    }

    public void setInfo8(String info8) {
        this.info8 = info8;
    }

    public String getInfo9() {
        return info9;
    }

    public void setInfo9(String info9) {
        this.info9 = info9;
    }

    public String getInfo10() {
        return info10;
    }

    public void setInfo10(String info10) {
        this.info10 = info10;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOrdineSm != null ? idOrdineSm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrdineSingMacOri)) {
            return false;
        }
        OrdineSingMacOri other = (OrdineSingMacOri) object;
        if ((this.idOrdineSm == null && other.idOrdineSm != null) || (this.idOrdineSm != null && !this.idOrdineSm.equals(other.idOrdineSm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.OrdineSingMacOri[ idOrdineSm=" + idOrdineSm + " ]";
    }
    
}
