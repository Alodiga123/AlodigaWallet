/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
 * @author ltoro
 */
@Entity
@Table(name = "cumplimient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cumplimient.findAll", query = "SELECT c FROM Cumplimient c")
    , @NamedQuery(name = "Cumplimient.findById", query = "SELECT c FROM Cumplimient c WHERE c.id = :id")
    , @NamedQuery(name = "Cumplimient.findByUserSourceId", query = "SELECT c FROM Cumplimient c WHERE c.userSourceId = :userSourceId")
    , @NamedQuery(name = "Cumplimient.findByIsKYC", query = "SELECT c FROM Cumplimient c WHERE c.isKYC = :isKYC")
    , @NamedQuery(name = "Cumplimient.findByIsAML", query = "SELECT c FROM Cumplimient c WHERE c.isAML = :isAML")
    , @NamedQuery(name = "Cumplimient.findByBeginningDate", query = "SELECT c FROM Cumplimient c WHERE c.beginningDate = :beginningDate")
    , @NamedQuery(name = "Cumplimient.findByEndingDate", query = "SELECT c FROM Cumplimient c WHERE c.endingDate = :endingDate")
    , @NamedQuery(name = "Cumplimient.findByAprovedDate", query = "SELECT c FROM Cumplimient c WHERE c.aprovedDate = :aprovedDate")
    , @NamedQuery(name = "Cumplimient.findByAMLPercent", query = "SELECT c FROM Cumplimient c WHERE c.aMLPercent = :aMLPercent")
    , @NamedQuery(name = "Cumplimient.findByAgentComplientId", query = "SELECT c FROM Cumplimient c WHERE c.agentComplientId = :agentComplientId")
    , @NamedQuery(name = "Cumplimient.findByAdditional", query = "SELECT c FROM Cumplimient c WHERE c.additional = :additional")})
public class Cumplimient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userSourceId")
    private long userSourceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isKYC")
    private boolean isKYC;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IsAML")
    private boolean isAML;
    @Basic(optional = false)
    @NotNull
    @Column(name = "beginningDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginningDate;
    @Column(name = "endingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingDate;
    @Column(name = "aprovedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aprovedDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMLPercent")
    private Float aMLPercent;
    @Lob
    @Column(name = "imgDocumentDate")
    private byte[] imgDocumentDate;
    @Lob
    @Column(name = "imgProfile")
    private byte[] imgProfile;
    @Column(name = "agentComplientId")
    private BigInteger agentComplientId;
    @Size(max = 45)
    @Column(name = "additional")
    private String additional;
    @JoinColumn(name = "addressId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Address addressId;
    @JoinColumn(name = "complientStatusId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CumplimientStatus complientStatusId;

    public Cumplimient() {
    }

    public Cumplimient(Long id) {
        this.id = id;
    }

    public Cumplimient(Long id, long userSourceId, boolean isKYC, boolean isAML, Date beginningDate) {
        this.id = id;
        this.userSourceId = userSourceId;
        this.isKYC = isKYC;
        this.isAML = isAML;
        this.beginningDate = beginningDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(long userSourceId) {
        this.userSourceId = userSourceId;
    }

    public boolean getIsKYC() {
        return isKYC;
    }

    public void setIsKYC(boolean isKYC) {
        this.isKYC = isKYC;
    }

    public boolean getIsAML() {
        return isAML;
    }

    public void setIsAML(boolean isAML) {
        this.isAML = isAML;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public Date getAprovedDate() {
        return aprovedDate;
    }

    public void setAprovedDate(Date aprovedDate) {
        this.aprovedDate = aprovedDate;
    }

    public Float getAMLPercent() {
        return aMLPercent;
    }

    public void setAMLPercent(Float aMLPercent) {
        this.aMLPercent = aMLPercent;
    }

    public byte[] getImgDocumentDate() {
        return imgDocumentDate;
    }

    public void setImgDocumentDate(byte[] imgDocumentDate) {
        this.imgDocumentDate = imgDocumentDate;
    }

    public byte[] getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(byte[] imgProfile) {
        this.imgProfile = imgProfile;
    }

    public BigInteger getAgentComplientId() {
        return agentComplientId;
    }

    public void setAgentComplientId(BigInteger agentComplientId) {
        this.agentComplientId = agentComplientId;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public CumplimientStatus getComplientStatusId() {
        return complientStatusId;
    }

    public void setComplientStatusId(CumplimientStatus complientStatusId) {
        this.complientStatusId = complientStatusId;
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
        if (!(object instanceof Cumplimient)) {
            return false;
        }
        Cumplimient other = (Cumplimient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.Cumplimient[ id=" + id + " ]";
    }
    
}
