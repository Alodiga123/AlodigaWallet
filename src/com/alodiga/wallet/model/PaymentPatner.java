/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "payment_patner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentPatner.findAll", query = "SELECT p FROM PaymentPatner p"),
    @NamedQuery(name = "PaymentPatner.findById", query = "SELECT p FROM PaymentPatner p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentPatner.findByName", query = "SELECT p FROM PaymentPatner p WHERE p.name = :name"),
    @NamedQuery(name = "PaymentPatner.findByUrlSubmit", query = "SELECT p FROM PaymentPatner p WHERE p.urlSubmit = :urlSubmit"),
    @NamedQuery(name = "PaymentPatner.findByPaymentUser", query = "SELECT p FROM PaymentPatner p WHERE p.paymentUser = :paymentUser"),
    @NamedQuery(name = "PaymentPatner.findByEncriptionKey", query = "SELECT p FROM PaymentPatner p WHERE p.encriptionKey = :encriptionKey"),
    @NamedQuery(name = "PaymentPatner.findByUrlResponse", query = "SELECT p FROM PaymentPatner p WHERE p.urlResponse = :urlResponse"),
    @NamedQuery(name = "PaymentPatner.findByUrlConfirmation", query = "SELECT p FROM PaymentPatner p WHERE p.urlConfirmation = :urlConfirmation"),
    @NamedQuery(name = "PaymentPatner.findByTestMode", query = "SELECT p FROM PaymentPatner p WHERE p.testMode = :testMode"),
    @NamedQuery(name = "PaymentPatner.findByEnabled", query = "SELECT p FROM PaymentPatner p WHERE p.enabled = :enabled")})
public class PaymentPatner implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "urlSubmit")
    private String urlSubmit;
    @Column(name = "paymentUser")
    private String paymentUser;
    @Column(name = "encriptionKey")
    private String encriptionKey;
    @Column(name = "urlResponse")
    private String urlResponse;
    @Column(name = "urlConfirmation")
    private String urlConfirmation;
    @Basic(optional = false)
    @Column(name = "testMode")
    private boolean testMode;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentPatnerId")
    private Collection<PaymentInfo> paymentInfoCollection;
    @JoinColumn(name = "integrationTypeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PaymentIntegrationType integrationTypeId;

    public PaymentPatner() {
    }

    public PaymentPatner(Long id) {
        this.id = id;
    }

    public PaymentPatner(Long id, String name, boolean testMode, boolean enabled) {
        this.id = id;
        this.name = name;
        this.testMode = testMode;
        this.enabled = enabled;
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

    public String getUrlSubmit() {
        return urlSubmit;
    }

    public void setUrlSubmit(String urlSubmit) {
        this.urlSubmit = urlSubmit;
    }

    public String getPaymentUser() {
        return paymentUser;
    }

    public void setPaymentUser(String paymentUser) {
        this.paymentUser = paymentUser;
    }

    public String getEncriptionKey() {
        return encriptionKey;
    }

    public void setEncriptionKey(String encriptionKey) {
        this.encriptionKey = encriptionKey;
    }

    public String getUrlResponse() {
        return urlResponse;
    }

    public void setUrlResponse(String urlResponse) {
        this.urlResponse = urlResponse;
    }

    public String getUrlConfirmation() {
        return urlConfirmation;
    }

    public void setUrlConfirmation(String urlConfirmation) {
        this.urlConfirmation = urlConfirmation;
    }

    public boolean getTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    

    @XmlTransient
    public Collection<PaymentInfo> getPaymentInfoCollection() {
        return paymentInfoCollection;
    }

    public void setPaymentInfoCollection(Collection<PaymentInfo> paymentInfoCollection) {
        this.paymentInfoCollection = paymentInfoCollection;
    }

    public PaymentIntegrationType getIntegrationTypeId() {
        return integrationTypeId;
    }

    public void setIntegrationTypeId(PaymentIntegrationType integrationTypeId) {
        this.integrationTypeId = integrationTypeId;
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
        if (!(object instanceof PaymentPatner)) {
            return false;
        }
        PaymentPatner other = (PaymentPatner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PaymentPatner[ id=" + id + " ]";
    }
    
}
