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
import javax.persistence.Lob;
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
 * @author marilisa
 */
@Entity
@Table(name = "componente_pesatura_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponentePesaturaOri.findAll", query = "SELECT c FROM ComponentePesaturaOri c"),
    @NamedQuery(name = "ComponentePesaturaOri.findById", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.id = :id"),
    @NamedQuery(name = "ComponentePesaturaOri.findByIdProdotto", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.idProdotto = :idProdotto"),
    @NamedQuery(name = "ComponentePesaturaOri.findByIdComp", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.idComp = :idComp"),
    @NamedQuery(name = "ComponentePesaturaOri.findByMetodoPesa", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.metodoPesa = :metodoPesa"),
    @NamedQuery(name = "ComponentePesaturaOri.findByStepDosaggio", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.stepDosaggio = :stepDosaggio"),
    @NamedQuery(name = "ComponentePesaturaOri.findByOrdineDosaggio", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.ordineDosaggio = :ordineDosaggio"),
    @NamedQuery(name = "ComponentePesaturaOri.findByQuantita", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.quantita = :quantita"),
    @NamedQuery(name = "ComponentePesaturaOri.findByTollEccesso", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.tollEccesso = :tollEccesso"),
    @NamedQuery(name = "ComponentePesaturaOri.findByTollDifetto", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.tollDifetto = :tollDifetto"),
    @NamedQuery(name = "ComponentePesaturaOri.findByFluidificazione", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.fluidificazione = :fluidificazione"),
    @NamedQuery(name = "ComponentePesaturaOri.findByValoreResiduoFluidificazione", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.valoreResiduoFluidificazione = :valoreResiduoFluidificazione"),
    @NamedQuery(name = "ComponentePesaturaOri.findByAbilitato", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.abilitato = :abilitato"),
    @NamedQuery(name = "ComponentePesaturaOri.findByDtAbilitato", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.dtAbilitato = :dtAbilitato"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo1", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info1 = :info1"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo2", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info2 = :info2"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo3", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info3 = :info3"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo4", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info4 = :info4"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo5", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info5 = :info5"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo6", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info6 = :info6"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo7", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info7 = :info7"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo8", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info8 = :info8"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo9", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info9 = :info9"),
    @NamedQuery(name = "ComponentePesaturaOri.findByInfo10", query = "SELECT c FROM ComponentePesaturaOri c WHERE c.info10 = :info10")})
public class ComponentePesaturaOri implements Serializable {

    @Size(max = 255)
    @Column(name = "velocita_mix")
    private String velocitaMix;
    @Size(max = 255)
    @Column(name = "tempo_mix")
    private String tempoMix;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_prodotto")
    private Integer idProdotto;
    @Column(name = "id_comp")
    private Integer idComp;
    @Size(max = 45)
    @Column(name = "metodo_pesa")
    private String metodoPesa;
    @Size(max = 45)
    @Column(name = "step_dosaggio")
    private String stepDosaggio;
    @Column(name = "ordine_dosaggio")
    private Integer ordineDosaggio;
    @Column(name = "quantita")
    private Integer quantita;
    @Column(name = "toll_eccesso")
    private Integer tollEccesso;
    @Column(name = "toll_difetto")
    private Integer tollDifetto;
    @Size(max = 45)
    @Column(name = "fluidificazione")
    private String fluidificazione;
    @Column(name = "valore_residuo_fluidificazione")
    private Integer valoreResiduoFluidificazione;
    @Lob
    @Size(max = 65535)
    @Column(name = "note")
    private String note;
    @Column(name = "abilitato")
    private Boolean abilitato;
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
    
    public ComponentePesaturaOri() {
    }

    public ComponentePesaturaOri(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public Integer getIdComp() {
        return idComp;
    }

    public void setIdComp(Integer idComp) {
        this.idComp = idComp;
    }

    public String getMetodoPesa() {
        return metodoPesa;
    }

    public void setMetodoPesa(String metodoPesa) {
        this.metodoPesa = metodoPesa;
    }

    public String getStepDosaggio() {
        return stepDosaggio;
    }

    public void setStepDosaggio(String stepDosaggio) {
        this.stepDosaggio = stepDosaggio;
    }

    public Integer getOrdineDosaggio() {
        return ordineDosaggio;
    }

    public void setOrdineDosaggio(Integer ordineDosaggio) {
        this.ordineDosaggio = ordineDosaggio;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Integer getTollEccesso() {
        return tollEccesso;
    }

    public void setTollEccesso(Integer tollEccesso) {
        this.tollEccesso = tollEccesso;
    }

    public Integer getTollDifetto() {
        return tollDifetto;
    }

    public void setTollDifetto(Integer tollDifetto) {
        this.tollDifetto = tollDifetto;
    }

    public String getFluidificazione() {
        return fluidificazione;
    }

    public void setFluidificazione(String fluidificazione) {
        this.fluidificazione = fluidificazione;
    }

    public Integer getValoreResiduoFluidificazione() {
        return valoreResiduoFluidificazione;
    }

    public void setValoreResiduoFluidificazione(Integer valoreResiduoFluidificazione) {
        this.valoreResiduoFluidificazione = valoreResiduoFluidificazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getAbilitato() {
        return abilitato;
    }

    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComponentePesaturaOri)) {
            return false;
        }
        ComponentePesaturaOri other = (ComponentePesaturaOri) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.personalfactory.syncorigami.macchina.entity.ComponentePesaturaOri[ id=" + id + " ]";
    }

    public String getVelocitaMix() {
        return velocitaMix;
    }

    public void setVelocitaMix(String velocitaMix) {
        this.velocitaMix = velocitaMix;
    }

    public String getTempoMix() {
        return tempoMix;
    }

    public void setTempoMix(String tempoMix) {
        this.tempoMix = tempoMix;
    }
    
}
