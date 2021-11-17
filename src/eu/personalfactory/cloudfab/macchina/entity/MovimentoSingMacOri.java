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
@Table(name = "movimento_sing_mac_ori")
@XmlRootElement
@NamedQueries({
    /**@NamedQuery(name = "MovimentoSingMacOri.findAll", query = "SELECT m FROM MovimentoSingMacOri m"),
    @NamedQuery(name = "MovimentoSingMacOri.findByIdMovOri", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.idMovOri = :idMovOri"),
    @NamedQuery(name = "MovimentoSingMacOri.findByIdMovInephos", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.idMovInephos = :idMovInephos"),
    @NamedQuery(name = "MovimentoSingMacOri.findByIdMateriale", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.idMateriale = :idMateriale"),
    @NamedQuery(name = "MovimentoSingMacOri.findByTipoMateriale", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.tipoMateriale = :tipoMateriale"),
    @NamedQuery(name = "MovimentoSingMacOri.findByQuantita", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.quantita = :quantita"),
    @NamedQuery(name = "MovimentoSingMacOri.findByCodIngressoComp", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.codIngressoComp = :codIngressoComp"),
    @NamedQuery(name = "MovimentoSingMacOri.findByCodOperatore", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.codOperatore = :codOperatore"),
    @NamedQuery(name = "MovimentoSingMacOri.findByOperazione", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.operazione = :operazione"),
    @NamedQuery(name = "MovimentoSingMacOri.findByProceduraAdottata", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.proceduraAdottata = :proceduraAdottata"),
    @NamedQuery(name = "MovimentoSingMacOri.findByTipoMov", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.tipoMov = :tipoMov"),
    @NamedQuery(name = "MovimentoSingMacOri.findByDescriMov", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.descriMov = :descriMov"),
    @NamedQuery(name = "MovimentoSingMacOri.findByDtMov", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.dtMov = :dtMov"),
    @NamedQuery(name = "MovimentoSingMacOri.findBySilo", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.silo = :silo"),
    @NamedQuery(name = "MovimentoSingMacOri.findByPesoTeorico", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.pesoTeorico = :pesoTeorico"),
    @NamedQuery(name = "MovimentoSingMacOri.findByIdCiclo", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.idCiclo = :idCiclo"),
    @NamedQuery(name = "MovimentoSingMacOri.findByDtInizioProcedura", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.dtInizioProcedura = :dtInizioProcedura"),
    @NamedQuery(name = "MovimentoSingMacOri.findByDtFineProcedura", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.dtFineProcedura = :dtFineProcedura"),
    @NamedQuery(name = "MovimentoSingMacOri.findByAbilitato", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.abilitato = :abilitato"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo1", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info1 = :info1"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo2", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info2 = :info2"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo3", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info3 = :info3"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo4", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info4 = :info4"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo5", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info5 = :info5"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo6", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info6 = :info6"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo7", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info7 = :info7"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo8", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info8 = :info8"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo9", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info9 = :info9"),
    @NamedQuery(name = "MovimentoSingMacOri.findByInfo10", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.info10 = :info10"),*/
    @NamedQuery(name = "MovimentoSingMacOri.findDatiNuovi", query = "SELECT m FROM MovimentoSingMacOri m WHERE m.dtMov >:dtMov AND origineMov=:origineMov")
})
public class MovimentoSingMacOri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    //Il campo id_mov_ori della tabella movimento_sing_mac_ori è autoincrement
                //quindi quando si salva l'oggetto gli viene assegnato un valore automatico
                //Attenzione però nell'entità MovimentoSingMacOri il campo id_mov_ori NON DEVE essere impostato come automatico
                //Anche se sul db lo è
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_mov_ori")
    private Integer idMovOri;
    @Column(name = "id_mov_inephos")
    private Integer idMovInephos;
    @Column(name = "id_materiale")
    private Integer idMateriale;
    @Size(max = 255)
    @Column(name = "tipo_materiale")
    private String tipoMateriale;
    @Column(name = "quantita")
    private Integer quantita;
    @Size(max = 255)
    @Column(name = "cod_ingresso_comp")
    private String codIngressoComp;
    @Size(max = 255)
    @Column(name = "cod_operatore")
    private String codOperatore;
    @Size(max = 45)
    @Column(name = "operazione")
    private String operazione;
    @Size(max = 255)
    @Column(name = "procedura_adottata")
    private String proceduraAdottata;
    @Size(max = 45)
    @Column(name = "tipo_mov")
    private String tipoMov;
    @Size(max = 255)
    @Column(name = "descri_mov")
    private String descriMov;
    @Column(name = "dt_mov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtMov;
    @Size(max = 45)
    @Column(name = "silo")
    private String silo;
    @Column(name = "peso_teorico")
    private Integer pesoTeorico;
    @Column(name = "id_ciclo")
    private Integer idCiclo;
    @Column(name = "dt_inizio_procedura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInizioProcedura;
    @Column(name = "dt_fine_procedura")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtFineProcedura;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @Size(max = 45)
    @Column(name = "origine_mov")
    private String origineMov;
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

    public MovimentoSingMacOri() {
    }

    public MovimentoSingMacOri(Integer idMovOri) {
        this.idMovOri = idMovOri;
    }

    public Integer getIdMovOri() {
        return idMovOri;
    }

    public void setIdMovOri(Integer idMovOri) {
        this.idMovOri = idMovOri;
    }

    public Integer getIdMovInephos() {
        return idMovInephos;
    }

    public void setIdMovInephos(Integer idMovInephos) {
        this.idMovInephos = idMovInephos;
    }

    public Integer getIdMateriale() {
        return idMateriale;
    }

    public void setIdMateriale(Integer idMateriale) {
        this.idMateriale = idMateriale;
    }

    public String getTipoMateriale() {
        return tipoMateriale;
    }

    public void setTipoMateriale(String tipoMateriale) {
        this.tipoMateriale = tipoMateriale;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public String getCodIngressoComp() {
        return codIngressoComp;
    }

    public void setCodIngressoComp(String codIngressoComp) {
        this.codIngressoComp = codIngressoComp;
    }

    public String getCodOperatore() {
        return codOperatore;
    }

    public void setCodOperatore(String codOperatore) {
        this.codOperatore = codOperatore;
    }

    public String getOperazione() {
        return operazione;
    }

    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }

    public String getProceduraAdottata() {
        return proceduraAdottata;
    }

    public void setProceduraAdottata(String proceduraAdottata) {
        this.proceduraAdottata = proceduraAdottata;
    }

    public String getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(String tipoMov) {
        this.tipoMov = tipoMov;
    }

    public String getDescriMov() {
        return descriMov;
    }

    public void setDescriMov(String descriMov) {
        this.descriMov = descriMov;
    }

    public Date getDtMov() {
        return dtMov;
    }

    public void setDtMov(Date dtMov) {
        this.dtMov = dtMov;
    }

    public String getSilo() {
        return silo;
    }

    public void setSilo(String silo) {
        this.silo = silo;
    }

    public Integer getPesoTeorico() {
        return pesoTeorico;
    }

    public void setPesoTeorico(Integer pesoTeorico) {
        this.pesoTeorico = pesoTeorico;
    }

    public Integer getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(Integer idCiclo) {
        this.idCiclo = idCiclo;
    }

    public Date getDtInizioProcedura() {
        return dtInizioProcedura;
    }

    public void setDtInizioProcedura(Date dtInizioProcedura) {
        this.dtInizioProcedura = dtInizioProcedura;
    }

    public Date getDtFineProcedura() {
        return dtFineProcedura;
    }

    public void setDtFineProcedura(Date dtFineProcedura) {
        this.dtFineProcedura = dtFineProcedura;
    }

    public Boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
    }

    public String getOrigineMov() {
        return origineMov;
    }

    public void setOrigineMov(String origineMov) {
        this.origineMov = origineMov;
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
        hash += (idMovOri != null ? idMovOri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimentoSingMacOri)) {
            return false;
        }
        MovimentoSingMacOri other = (MovimentoSingMacOri) object;
        if ((this.idMovOri == null && other.idMovOri != null) || (this.idMovOri != null && !this.idMovOri.equals(other.idMovOri))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.fabcloud.macchina.entity.MovimentoSingMacOri[ idMovOri=" + idMovOri + " ]";
    }
    
}
