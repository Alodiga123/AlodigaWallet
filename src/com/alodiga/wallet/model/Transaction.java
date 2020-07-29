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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "transaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t"),
    @NamedQuery(name = "Transaction.findById", query = "SELECT t FROM Transaction t WHERE t.id = :id"),
    @NamedQuery(name = "Transaction.findByUserSourceId", query = "SELECT t FROM Transaction t WHERE t.userSourceId = :userSourceId OR t.userDestinationId = :userDestinationId ORDER BY t.id DESC"),
    @NamedQuery(name = "Transaction.findByUserDestinationId", query = "SELECT t FROM Transaction t WHERE t.userDestinationId = :userDestinationId"),
    @NamedQuery(name = "Transaction.findByCreationDate", query = "SELECT t FROM Transaction t WHERE t.creationDate = :creationDate"),
    @NamedQuery(name = "Transaction.findByAmount", query = "SELECT t FROM Transaction t WHERE t.amount = :amount"),
    @NamedQuery(name = "Transaction.findByTransactionStatus", query = "SELECT t FROM Transaction t WHERE t.transactionStatus = :transactionStatus"),
    @NamedQuery(name = "Transaction.findByTotalTax", query = "SELECT t FROM Transaction t WHERE t.totalTax = :totalTax"),
    @NamedQuery(name = "Transaction.findByTotalAmount", query = "SELECT t FROM Transaction t WHERE t.totalAmount = :totalAmount"),
    @NamedQuery(name = "Transaction.findByPromotionAmount", query = "SELECT t FROM Transaction t WHERE t.promotionAmount = :promotionAmount"),
    @NamedQuery(name = "Transaction.findByTotalAlopointsUsed", query = "SELECT t FROM Transaction t WHERE t.totalAlopointsUsed = :totalAlopointsUsed"),
    @NamedQuery(name = "Transaction.findByTopUpDescription", query = "SELECT t FROM Transaction t WHERE t.topUpDescription = :topUpDescription"),
    @NamedQuery(name = "Transaction.findByBillPaymentDescription", query = "SELECT t FROM Transaction t WHERE t.billPaymentDescription = :billPaymentDescription"),
    @NamedQuery(name = "Transaction.findByExternalId", query = "SELECT t FROM Transaction t WHERE t.externalId = :externalId"),
    @NamedQuery(name = "Transaction.findByAdditional", query = "SELECT t FROM Transaction t WHERE t.additional = :additional"),
    @NamedQuery(name = "Transaction.findByAdditional2", query = "SELECT t FROM Transaction t WHERE t.additional2 = :additional2")})
public class Transaction implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalTax")
    private Float totalTax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalAmount")
    private float totalAmount;
    @Column(name = "promotionAmount")
    private Float promotionAmount;
    @Column(name = "totalAlopointsUsed")
    private Float totalAlopointsUsed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionId")
    private Collection<BankOperation> bankOperationCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "userSourceId")
    private BigInteger userSourceId;
    @Column(name = "userDestinationId")
    private BigInteger userDestinationId;
    @Basic(optional = false)
    @Column(name = "concept")
    private String concept;
    @Basic(optional = false)
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Basic(optional = false)
    @Column(name = "amount")
    private float amount;
    @Basic(optional = false)
    @Column(name = "transactionStatus")
    private String transactionStatus;
    @Column(name = "topUpDescription")
    private String topUpDescription;
    @Column(name = "billPaymentDescription")
    private String billPaymentDescription;
    @Column(name = "externalId")
    private String externalId;
    @Column(name = "additional")
    private String additional;
    @Column(name = "additional2")
    private String additional2;
    @JoinColumn(name = "transactionTypeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TransactionType transactionTypeId;
    @JoinColumn(name = "transactionSourceId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TransactionSource transactionSourceId;
    @JoinColumn(name = "paymentInfoId", referencedColumnName = "id")
    @ManyToOne
    private PaymentInfo paymentInfoId;
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ManyToOne
    private Product productId;
    @JoinColumn(name = "closeId", referencedColumnName = "id")
    @ManyToOne
    private Close closeId;
    @OneToMany(mappedBy = "transactionId")
    private Collection<PromotionItem> promotionItemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionId")
    private Collection<CommissionItem> commissionItemCollection;
    @OneToMany(mappedBy = "transactionId")
    private Collection<BalanceHistory> balanceHistoryCollection;
    
    //Only by result transaction list by APP
    @Transient
    private String commisionAmount;
    @Transient
    private String destinationUser;
    @Transient
    private String transactionType;
    

    

    public Transaction() {
    }

    public Transaction(Long id) {
        this.id = id;
    }

    public Transaction(Long id, Date creationDate, float amount, String transactionStatus, float totalTax, float promotionAmount, float totalAlopointsUsed) {
        this.id = id;
        this.creationDate = creationDate;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.totalTax = totalTax;
        this.promotionAmount = promotionAmount;
        this.totalAlopointsUsed = totalAlopointsUsed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(BigInteger userSourceId) {
        this.userSourceId = userSourceId;
    }

    public BigInteger getUserDestinationId() {
        return userDestinationId;
    }

    public void setUserDestinationId(BigInteger userDestinationId) {
        this.userDestinationId = userDestinationId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }


    public String getTopUpDescription() {
        return topUpDescription;
    }

    public void setTopUpDescription(String topUpDescription) {
        this.topUpDescription = topUpDescription;
    }

    public String getBillPaymentDescription() {
        return billPaymentDescription;
    }

    public void setBillPaymentDescription(String billPaymentDescription) {
        this.billPaymentDescription = billPaymentDescription;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getAdditional2() {
        return additional2;
    }

    public void setAdditional2(String additional2) {
        this.additional2 = additional2;
    }

    public TransactionType getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(TransactionType transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public TransactionSource getTransactionSourceId() {
        return transactionSourceId;
    }

    public void setTransactionSourceId(TransactionSource transactionSourceId) {
        this.transactionSourceId = transactionSourceId;
    }

    public PaymentInfo getPaymentInfoId() {
        return paymentInfoId;
    }

    public void setPaymentInfoId(PaymentInfo paymentInfoId) {
        this.paymentInfoId = paymentInfoId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Close getCloseId() {
        return closeId;
    }

    public void setCloseId(Close closeId) {
        this.closeId = closeId;
    }

    @XmlTransient
    public Collection<PromotionItem> getPromotionItemCollection() {
        return promotionItemCollection;
    }

    public void setPromotionItemCollection(Collection<PromotionItem> promotionItemCollection) {
        this.promotionItemCollection = promotionItemCollection;
    }

    @XmlTransient
    public Collection<CommissionItem> getCommissionItemCollection() {
        return commissionItemCollection;
    }

    public void setCommissionItemCollection(Collection<CommissionItem> commissionItemCollection) {
        this.commissionItemCollection = commissionItemCollection;
    }

    @XmlTransient
    public Collection<BalanceHistory> getBalanceHistoryCollection() {
        return balanceHistoryCollection;
    }

    public void setBalanceHistoryCollection(Collection<BalanceHistory> balanceHistoryCollection) {
        this.balanceHistoryCollection = balanceHistoryCollection;
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
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Transaction[ id=" + id + " ]";
    }    
    
    //Only response APP

    public String getCommisionAmount() {
        return commisionAmount;
    }

    public void setCommisionAmount(String commisionAmount) {
        this.commisionAmount = commisionAmount;
    }

    public String getDestinationUser() {
        return destinationUser;
    }

    public void setDestinationUser(String destinationUser) {
        this.destinationUser = destinationUser;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Float totalTax) {
        this.totalTax = totalTax;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Float getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(Float promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public Float getTotalAlopointsUsed() {
        return totalAlopointsUsed;
    }

    public void setTotalAlopointsUsed(Float totalAlopointsUsed) {
        this.totalAlopointsUsed = totalAlopointsUsed;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<BankOperation> getBankOperationCollection() {
        return bankOperationCollection;
    }

    public void setBankOperationCollection(Collection<BankOperation> bankOperationCollection) {
        this.bankOperationCollection = bankOperationCollection;
    }
    
    
    
}
