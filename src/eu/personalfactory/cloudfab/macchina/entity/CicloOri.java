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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "ciclo_ori")
@XmlRootElement
@NamedQueries({
    /**@NamedQuery(name = "CicloOri.findAll", query = "SELECT c FROM CicloOri c"),
    @NamedQuery(name = "CicloOri.findByIdCiclo", query = "SELECT c FROM CicloOri c WHERE c.idCiclo = :idCiclo"),
    @NamedQuery(name = "CicloOri.findByTipoCiclo", query = "SELECT c FROM CicloOri c WHERE c.tipoCiclo = :tipoCiclo"),
    @NamedQuery(name = "CicloOri.findByDtInizioCiclo", query = "SELECT c FROM CicloOri c WHERE c.dtInizioCiclo = :dtInizioCiclo"),
    @NamedQuery(name = "CicloOri.findByDtFineCiclo", query = "SELECT c FROM CicloOri c WHERE c.dtFineCiclo = :dtFineCiclo"),
    @NamedQuery(name = "CicloOri.findByIdOrdine", query = "SELECT c FROM CicloOri c WHERE c.idOrdine = :idOrdine"),
    @NamedQuery(name = "CicloOri.findByIdProdotto", query = "SELECT c FROM CicloOri c WHERE c.idProdotto = :idProdotto"),
    @NamedQuery(name = "CicloOri.findByIdCat", query = "SELECT c FROM CicloOri c WHERE c.idCat = :idCat"),
    @NamedQuery(name = "CicloOri.findByVelocitaMix", query = "SELECT c FROM CicloOri c WHERE c.velocitaMix = :velocitaMix"),
    @NamedQuery(name = "CicloOri.findByTempoMix", query = "SELECT c FROM CicloOri c WHERE c.tempoMix = :tempoMix"),
    @NamedQuery(name = "CicloOri.findByNumSacchi", query = "SELECT c FROM CicloOri c WHERE c.numSacchi = :numSacchi"),
    @NamedQuery(name = "CicloOri.findByNumSacchiAggiuntivi", query = "SELECT c FROM CicloOri c WHERE c.numSacchiAggiuntivi = :numSacchiAggiuntivi"),
    @NamedQuery(name = "CicloOri.findByVibroAttivo", query = "SELECT c FROM CicloOri c WHERE c.vibroAttivo = :vibroAttivo"),
    @NamedQuery(name = "CicloOri.findByAriaCondScarico", query = "SELECT c FROM CicloOri c WHERE c.ariaCondScarico = :ariaCondScarico"),
    @NamedQuery(name = "CicloOri.findByAriaInternoValvola", query = "SELECT c FROM CicloOri c WHERE c.ariaInternoValvola = :ariaInternoValvola"),
    @NamedQuery(name = "CicloOri.findByAriaPulisciValvola", query = "SELECT c FROM CicloOri c WHERE c.ariaPulisciValvola = :ariaPulisciValvola"),
    @NamedQuery(name = "CicloOri.findByDtAbilitato", query = "SELECT c FROM CicloOri c WHERE c.dtAbilitato = :dtAbilitato"),
    @NamedQuery(name = "CicloOri.findByInfo1", query = "SELECT c FROM CicloOri c WHERE c.info1 = :info1"),
    @NamedQuery(name = "CicloOri.findByInfo2", query = "SELECT c FROM CicloOri c WHERE c.info2 = :info2"),
    @NamedQuery(name = "CicloOri.findByInfo3", query = "SELECT c FROM CicloOri c WHERE c.info3 = :info3"),
    @NamedQuery(name = "CicloOri.findByInfo4", query = "SELECT c FROM CicloOri c WHERE c.info4 = :info4"),
    @NamedQuery(name = "CicloOri.findByInfo5", query = "SELECT c FROM CicloOri c WHERE c.info5 = :info5"),
    @NamedQuery(name = "CicloOri.findByInfo6", query = "SELECT c FROM CicloOri c WHERE c.info6 = :info6"),
    @NamedQuery(name = "CicloOri.findByInfo7", query = "SELECT c FROM CicloOri c WHERE c.info7 = :info7"),
    @NamedQuery(name = "CicloOri.findByInfo8", query = "SELECT c FROM CicloOri c WHERE c.info8 = :info8"),
    @NamedQuery(name = "CicloOri.findByInfo9", query = "SELECT c FROM CicloOri c WHERE c.info9 = :info9"),
    @NamedQuery(name = "CicloOri.findByInfo10", query = "SELECT c FROM CicloOri c WHERE c.info10 = :info10"),*/
    @NamedQuery(name = "CicloOri.findDatiNuovi", query = "SELECT c FROM CicloOri c WHERE c.dtAbilitato > :dtAbilitato")})
public class CicloOri implements Serializable {

    @Size(max = 255)
    @Column(name = "id_serie_colore")
    private String idSerieColore;
    @Size(max = 255)
    @Column(name = "id_serie_additivo")
    private String idSerieAdditivo;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ciclo")
    private Integer idCiclo;
    @Size(max = 255)
    @Column(name = "tipo_ciclo")
    private String tipoCiclo;
    @Column(name = "dt_inizio_ciclo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInizioCiclo;
    @Column(name = "dt_fine_ciclo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFineCiclo;
    @Column(name = "id_ordine")
    private Integer idOrdine;
    @Column(name = "id_prodotto")
    private Integer idProdotto;
    @Column(name = "id_cat")
    private Integer idCat;
    @Column(name = "velocita_mix")
    private Integer velocitaMix;
    @Column(name = "tempo_mix")
    private Integer tempoMix;
    @Column(name = "num_sacchi")
    private Integer numSacchi;
    @Column(name = "num_sacchi_aggiuntivi")
    private Integer numSacchiAggiuntivi;
    @Size(max = 45)
    @Column(name = "vibro_attivo")
    private String vibroAttivo;
    @Size(max = 45)
    @Column(name = "aria_cond_scarico")
    private String ariaCondScarico;
    @Size(max = 45)
    @Column(name = "aria_interno_valvola")
    private String ariaInternoValvola;
    @Size(max = 45)
    @Column(name = "aria_pulisci_valvola")
    private String ariaPulisciValvola;
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

    public CicloOri() {
    }

    public CicloOri(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public Integer getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public String getTipoCiclo() {
        return tipoCiclo;
    }

    public void setTipoCiclo(String tipoCiclo) {
        this.tipoCiclo = tipoCiclo;
    }

    public Date getDtInizioCiclo() {
        return dtInizioCiclo;
    }

    public void setDtInizioCiclo(Date dtInizioCiclo) {
        this.dtInizioCiclo = dtInizioCiclo;
    }

    public Date getDtFineCiclo() {
        return dtFineCiclo;
    }

    public void setDtFineCiclo(Date dtFineCiclo) {
        this.dtFineCiclo = dtFineCiclo;
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

    public Integer getIdCat() {
        return idCat;
    }

    public void setIdCat(Integer idCat) {
        this.idCat = idCat;
    }

    public Integer getVelocitaMix() {
        return velocitaMix;
    }

    public void setVelocitaMix(Integer velocitaMix) {
        this.velocitaMix = velocitaMix;
    }

    public Integer getTempoMix() {
        return tempoMix;
    }

    public void setTempoMix(Integer tempoMix) {
        this.tempoMix = tempoMix;
    }

    public Integer getNumSacchi() {
        return numSacchi;
    }

    public void setNumSacchi(Integer numSacchi) {
        this.numSacchi = numSacchi;
    }

    public Integer getNumSacchiAggiuntivi() {
        return numSacchiAggiuntivi;
    }

    public void setNumSacchiAggiuntivi(Integer numSacchiAggiuntivi) {
        this.numSacchiAggiuntivi = numSacchiAggiuntivi;
    }

    public String getVibroAttivo() {
        return vibroAttivo;
    }

    public void setVibroAttivo(String vibroAttivo) {
        this.vibroAttivo = vibroAttivo;
    }

    public String getAriaCondScarico() {
        return ariaCondScarico;
    }

    public void setAriaCondScarico(String ariaCondScarico) {
        this.ariaCondScarico = ariaCondScarico;
    }

    public String getAriaInternoValvola() {
        return ariaInternoValvola;
    }

    public void setAriaInternoValvola(String ariaInternoValvola) {
        this.ariaInternoValvola = ariaInternoValvola;
    }

    public String getAriaPulisciValvola() {
        return ariaPulisciValvola;
    }

    public void setAriaPulisciValvola(String ariaPulisciValvola) {
        this.ariaPulisciValvola = ariaPulisciValvola;
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
        hash += (idCiclo != null ? idCiclo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CicloOri)) {
            return false;
        }
        CicloOri other = (CicloOri) object;
        if ((this.idCiclo == null && other.idCiclo != null) || (this.idCiclo != null && !this.idCiclo.equals(other.idCiclo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.CicloOri[ idCiclo=" + idCiclo + " ]";
    }

    public String getIdSerieColore() {
        return idSerieColore;
    }

    public void setIdSerieColore(String idSerieColore) {
        this.idSerieColore = idSerieColore;
    }

    public String getIdSerieAdditivo() {
        return idSerieAdditivo;
    }

    public void setIdSerieAdditivo(String idSerieAdditivo) {
        this.idSerieAdditivo = idSerieAdditivo;
    }
    
}
