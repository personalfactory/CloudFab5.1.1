package eu.personalfactory.cloudfab.macchina.entity;

import it.divinotaras.jpa.entitysupport.DTEntityExtStatic;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author marilisa
 */
@Entity
@Table(name = "prodotto_ori")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProdottoOri.findAll", query = "SELECT p FROM ProdottoOri p")
})
public class ProdottoOri implements Serializable {

    @Size(max = 255)
    @Column(name = "serie_additivo")
    private String serieAdditivo;

    @Size(max = 255)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 255)
    @Column(name = "serie_colore")
    private String serieColore;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProdotto")
    private Collection<ComponenteProdottoOri> componenteProdottoOriCollection;

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_codice")
    private int idCodice;
   
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_prodotto")
    private Integer idProdotto;
    @Size(max = 50)
    @Column(name = "cod_prodotto")
    private String codProdotto;
    @Size(max = 255)
    @Column(name = "nome_prodotto")
    private String nomeProdotto;
    @Size(max = 50)
    @Column(name = "tipo_famiglia")
    private String tipoFamiglia;
    @Size(max = 255)
    @Column(name = "descri_famiglia")
    private String descriFamiglia;
    @Size(max = 50)
    @Column(name = "colorato")
    private String colorato;
    @Size(max = 50)
    @Column(name = "lim_colore")
    private String limColore;
    @Size(max = 50)
    @Column(name = "fattore_div")
    private String fattoreDiv;
    @Size(max = 50)
    @Column(name = "fascia")
    private String fascia;
    @Column(name = "abilitato")
    private Boolean abilitato;
    @JoinColumn(name = "id_cat", referencedColumnName = "id_cat")
    @ManyToOne(optional = false)
    private CategoriaOri idCat;
    @JoinColumn(name = "id_mazzetta", referencedColumnName = "id_mazzetta")
    @ManyToOne(optional = false)
    private MazzettaOri idMazzetta;
    @NotNull
    @Column(name = "dt_abilitato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAbilitato;
     
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProdotto")
    private Collection<ValoreProdottoOri> valoreProdottoOriCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProdotto")
    private Collection<ValoreParProdMacOri> valoreParProdMacOriCollection;

    public ProdottoOri() {
    }

    public ProdottoOri(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public ProdottoOri(Integer idProdotto, Date dtAbilitato) {
        this.idProdotto = idProdotto;
        this.dtAbilitato = dtAbilitato;
    }

    public Integer getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getCodProdotto() {
        return codProdotto;
    }

    public void setCodProdotto(String codProdotto) {
        this.codProdotto = codProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public Integer getIdCodice() {
        return idCodice;
    }

    public void setIdCodice(Integer idCodice) {
        this.idCodice = idCodice;
    }

    public String getTipoFamiglia() {
        return tipoFamiglia;
    }

    public void setTipoFamiglia(String tipoFamiglia) {
        this.tipoFamiglia = tipoFamiglia;
    }

    public String getDescriFamiglia() {
        return descriFamiglia;
    }

    public void setDescriFamiglia(String descriFamiglia) {
        this.descriFamiglia = descriFamiglia;
    }

    public String getColorato() {
        return colorato;
    }

    public void setColorato(String colorato) {
        this.colorato = colorato;
    }

    public String getLimColore() {
        return limColore;
    }

    public void setLimColore(String limColore) {
        this.limColore = limColore;
    }

    public String getFattoreDiv() {
        return fattoreDiv;
    }

    public void setFattoreDiv(String fattoreDiv) {
        this.fattoreDiv = fattoreDiv;
    }

    public String getFascia() {
        return fascia;
    }

    public void setFascia(String fascia) {
        this.fascia = fascia;
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

    public CategoriaOri getIdCat() {
        return idCat;
    }

    public void setIdCat(CategoriaOri idCat) {
        this.idCat = idCat;
    }

    public MazzettaOri getIdMazzetta() {
        return idMazzetta;
    }

    public void setIdMazzetta(MazzettaOri idMazzetta) {
        this.idMazzetta = idMazzetta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProdotto != null ? idProdotto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProdottoOri)) {
            return false;
        }
        ProdottoOri other = (ProdottoOri) object;
        if ((this.idProdotto == null && other.idProdotto != null) || (this.idProdotto != null && !this.idProdotto.equals(other.idProdotto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DTEntityExtStatic.objToString(this);
    }
 

    @XmlTransient
    public Collection<ComponenteProdottoOri> getComponenteProdottoOriCollection() {
        return componenteProdottoOriCollection;
    }

    public void setComponenteProdottoOriCollection(Collection<ComponenteProdottoOri> componenteProdottoOriCollection) {
        this.componenteProdottoOriCollection = componenteProdottoOriCollection;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSerieColore() {
        return serieColore;
    }

    public void setSerieColore(String serieColore) {
        this.serieColore = serieColore;
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

    public String getSerieAdditivo() {
        return serieAdditivo;
    }

    public void setSerieAdditivo(String serieAdditivo) {
        this.serieAdditivo = serieAdditivo;
    }
}
