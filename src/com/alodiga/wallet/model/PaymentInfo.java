/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "payment_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentInfo.findAll", query = "SELECT p FROM PaymentInfo p")
    ,
    @NamedQuery(name = "PaymentInfo.findById", query = "SELECT p FROM PaymentInfo p WHERE p.id = :id")
    ,
    @NamedQuery(name = "PaymentInfo.findByUserId", query = "SELECT p FROM PaymentInfo p WHERE p.userId = :userId AND p.enabled = 1")
    ,
    @NamedQuery(name = "PaymentInfo.findByCreditCardName", query = "SELECT p FROM PaymentInfo p WHERE p.creditCardName = :creditCardName")
    ,
    @NamedQuery(name = "PaymentInfo.findByCreditCardCVV", query = "SELECT p FROM PaymentInfo p WHERE p.creditCardCVV = :creditCardCVV")
    ,
    @NamedQuery(name = "PaymentInfo.findByCreditCardDate", query = "SELECT p FROM PaymentInfo p WHERE p.creditCardDate = :creditCardDate")
    ,
    @NamedQuery(name = "PaymentInfo.findByBeginningDate", query = "SELECT p FROM PaymentInfo p WHERE p.beginningDate = :beginningDate")
    ,
    @NamedQuery(name = "PaymentInfo.findByEndingDate", query = "SELECT p FROM PaymentInfo p WHERE p.endingDate = :endingDate")
    ,
@NamedQuery(name = "PaymentInfo.findByUserIdById", query = "SELECT p FROM PaymentInfo p WHERE p.userId = :userId AND p.id = :id")})
public class PaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "userId")
    private BigInteger userId;
    @Column(name = "creditCardName")
    private String creditCardName;
    @Column(name = "creditCardCVV")
    private String creditCardCVV;
    @Basic(optional = false)
    @Column(name = "creditCardDate")
    @Temporal(TemporalType.DATE)
    private Date creditCardDate;
    @Basic(optional = false)
    @Column(name = "beginningDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginningDate;
    @Column(name = "endingDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endingDate;
    @JoinColumn(name = "paymentTypeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PaymentType paymentTypeId;
    @JoinColumn(name = "paymentPatnerId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PaymentPatner paymentPatnerId;
    @JoinColumn(name = "creditCardTypeId", referencedColumnName = "id")

    @ManyToOne
    private CreditcardType creditCardTypeId;
    @Lob
    @Column(name = "creditCardNumber")
    private String creditCardNumber;
    @JoinColumn(name = "billingAddressId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Address billingAddressId;
    @OneToMany(mappedBy = "paymentInfoId")
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    private Collection<Transaction> transactionCollection;

    public PaymentInfo() {
    }

    public PaymentInfo(Long id) {
        this.id = id;
    }

    public PaymentInfo(Long id, Date creditCardDate, Date beginningDate) {
        this.id = id;
        this.creditCardDate = creditCardDate;
        this.beginningDate = beginningDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public String getCreditCardCVV() {
        return creditCardCVV;
    }

    public void setCreditCardCVV(String creditCardCVV) {
        this.creditCardCVV = creditCardCVV;
    }

    public Date getCreditCardDate() {
        return creditCardDate;
    }

    public void setCreditCardDate(Date creditCardDate) {
        this.creditCardDate = creditCardDate;
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

    public PaymentType getPaymentTypeId() {
        return paymentTypeId;
    }

    public void setPaymentTypeId(PaymentType paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
    }

    public PaymentPatner getPaymentPatnerId() {
        return paymentPatnerId;
    }

    public void setPaymentPatnerId(PaymentPatner paymentPatnerId) {
        this.paymentPatnerId = paymentPatnerId;
    }

    public CreditcardType getCreditCardTypeId() {
        return creditCardTypeId;
    }

    public void setCreditCardTypeId(CreditcardType creditCardTypeId) {
        this.creditCardTypeId = creditCardTypeId;
    }

    public Address getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Address billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @XmlTransient
    public Collection<Transaction> getTransactionCollection() {
        return transactionCollection;
    }

    public void setTransactionCollection(Collection<Transaction> transactionCollection) {
        this.transactionCollection = transactionCollection;
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
        if (!(object instanceof PaymentInfo)) {
            return false;
        }
        PaymentInfo other = (PaymentInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PaymentInfo[ id=" + id + " ]";
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

}
