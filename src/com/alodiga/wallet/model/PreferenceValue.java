/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
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
@Table(name = "preference_value")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PreferenceValue.findAll", query = "SELECT p FROM PreferenceValue p"),
    @NamedQuery(name = "PreferenceValue.findById", query = "SELECT p FROM PreferenceValue p WHERE p.id = :id"),
    @NamedQuery(name = "PreferenceValue.findByPreferenceFieldId", query = "SELECT p FROM PreferenceValue p WHERE p.preferenceFieldId.id = :preferenceFieldId and p.endingDate is null and p.enterpriseId.id = 1"),
    @NamedQuery(name = "PreferenceValue.findByValue", query = "SELECT p FROM PreferenceValue p WHERE p.value = :value"),
    @NamedQuery(name = "PreferenceValue.findByBeginningDate", query = "SELECT p FROM PreferenceValue p WHERE p.beginningDate = :beginningDate"),
    @NamedQuery(name = "PreferenceValue.findByEndingDate", query = "SELECT p FROM PreferenceValue p WHERE p.endingDate = :endingDate")})
public class PreferenceValue extends AbstractWalletEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @Basic(optional = false)
    @Column(name = "beginningDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginningDate;
    @Column(name = "endingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingDate;
    @JoinColumn(name = "preferenceFieldId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PreferenceField preferenceFieldId;
    @JoinColumn(name = "enterpriseId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Enterprise enterpriseId;

    public PreferenceValue() {
    }

    public PreferenceValue(Long id) {
        this.id = id;
    }

    public PreferenceValue(Long id, String value, Date beginningDate) {
        this.id = id;
        this.value = value;
        this.beginningDate = beginningDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public PreferenceField getPreferenceFieldId() {
        return preferenceFieldId;
    }

    public void setPreferenceFieldId(PreferenceField preferenceFieldId) {
        this.preferenceFieldId = preferenceFieldId;
    }

    public Enterprise getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Enterprise enterpriseId) {
        this.enterpriseId = enterpriseId;
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
        if (!(object instanceof PreferenceValue)) {
            return false;
        }
        PreferenceValue other = (PreferenceValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PreferenceValue[ id=" + id + " ]";
    }

	@Override
	public Object getPk() {
		return getId();
	}
    
}
