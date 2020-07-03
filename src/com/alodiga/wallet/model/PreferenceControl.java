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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.alodiga.wallet.genericEJB.AbstractWalletEntity;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "preference_control")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreferenceControl.findAll", query = "SELECT p FROM PreferenceControl p"),
    @NamedQuery(name = "PreferenceControl.findById", query = "SELECT p FROM PreferenceControl p WHERE p.id = :id"),
    @NamedQuery(name = "PreferenceControl.findByPreferenceId", query = "SELECT p FROM PreferenceControl p WHERE p.preferenceId = :preferenceId"),
    @NamedQuery(name = "PreferenceControl.findByCustomerId", query = "SELECT p FROM PreferenceControl p WHERE p.customerId = :customerId"),
    @NamedQuery(name = "PreferenceControl.findByUserId", query = "SELECT p FROM PreferenceControl p WHERE p.userId = :userId"),
    @NamedQuery(name = "PreferenceControl.findByParamValue", query = "SELECT p FROM PreferenceControl p WHERE p.paramValue = :paramValue"),
    @NamedQuery(name = "PreferenceControl.findByCreationDate", query = "SELECT p FROM PreferenceControl p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "PreferenceControl.findByAccessCounter", query = "SELECT p FROM PreferenceControl p WHERE p.accessCounter = :accessCounter")})
public class PreferenceControl extends AbstractWalletEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "preferenceId")
    private BigInteger preferenceId;
    @Column(name = "customerId")
    private BigInteger customerId;
    @Column(name = "userId")
    private BigInteger userId;
    @Basic(optional = false)
    @Column(name = "paramValue")
    private String paramValue;
    @Basic(optional = false)
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @Column(name = "accessCounter")
    private long accessCounter;
    @JoinColumn(name = "preferenceFieldId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PreferenceField preferenceFieldId;

    public PreferenceControl() {
    }

    public PreferenceControl(Long id) {
        this.id = id;
    }

    public PreferenceControl(Long id, String paramValue, Date creationDate, long accessCounter) {
        this.id = id;
        this.paramValue = paramValue;
        this.creationDate = creationDate;
        this.accessCounter = accessCounter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(BigInteger preferenceId) {
        this.preferenceId = preferenceId;
    }

    public BigInteger getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigInteger customerId) {
        this.customerId = customerId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getAccessCounter() {
        return accessCounter;
    }

    public void setAccessCounter(long accessCounter) {
        this.accessCounter = accessCounter;
    }

    public PreferenceField getPreferenceFieldId() {
        return preferenceFieldId;
    }

    public void setPreferenceFieldId(PreferenceField preferenceFieldId) {
        this.preferenceFieldId = preferenceFieldId;
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
        if (!(object instanceof PreferenceControl)) {
            return false;
        }
        PreferenceControl other = (PreferenceControl) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PreferenceControl[ id=" + id + " ]";
    }

	@Override
	public Object getPk() {
		return getId();
	}
    
}
