/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "creditcard_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CreditcardType.findAll", query = "SELECT c FROM CreditcardType c"),
    @NamedQuery(name = "CreditcardType.findById", query = "SELECT c FROM CreditcardType c WHERE c.id = :id"),
    @NamedQuery(name = "CreditcardType.findByName", query = "SELECT c FROM CreditcardType c WHERE c.name = :name"),
    @NamedQuery(name = "CreditcardType.findByEnabled", query = "SELECT c FROM CreditcardType c WHERE c.enabled = :enabled"),
    @NamedQuery(name = "CreditcardType.findByEnabledTrue", query = "SELECT c FROM CreditcardType c WHERE c.enabled = 1")})
public class CreditcardType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @Basic(optional = false)
    @Column(name = "lengh")
    private String lengh;
    @OneToMany(mappedBy = "creditCardTypeId")
    private Collection<PaymentInfo> paymentInfoCollection;

    public CreditcardType() {
    }

    public CreditcardType(Long id) {
        this.id = id;
    }

    public CreditcardType(Long id, String name, boolean enabled, String lengh) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.lengh = lengh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLengh() {
        return lengh;
    }

    public void setLengh(String lengh) {
        this.lengh = lengh;
    }

    
    @XmlTransient
    public Collection<PaymentInfo> getPaymentInfoCollection() {
        return paymentInfoCollection;
    }

    public void setPaymentInfoCollection(Collection<PaymentInfo> paymentInfoCollection) {
        this.paymentInfoCollection = paymentInfoCollection;
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
        if (!(object instanceof CreditcardType)) {
            return false;
        }
        CreditcardType other = (CreditcardType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.CreditcardType[ id=" + id + " ]";
    }
    
}
