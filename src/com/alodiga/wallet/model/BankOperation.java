/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jose
 */
@Entity
@Table(name = "bank_operation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BankOperation.findAll", query = "SELECT b FROM BankOperation b"), 
    @NamedQuery(name = "BankOperation.findById", query = "SELECT b FROM BankOperation b WHERE b.id = :id"),
    @NamedQuery(name = "BankOperation.findByUserSourceId", query = "SELECT b FROM BankOperation b WHERE b.userSourceId = :userSourceId"),
    @NamedQuery(name = "BankOperation.findByBankOperationNumber", query = "SELECT b FROM BankOperation b WHERE b.bankOperationNumber = :bankOperationNumber"),
    @NamedQuery(name = "BankOperation.findByAdditional", query = "SELECT b FROM BankOperation b WHERE b.additional = :additional"),
    @NamedQuery(name = "BankOperation.findByAdditional2", query = "SELECT b FROM BankOperation b WHERE b.additional2 = :additional2")})

public class BankOperation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "userSourceId")
    private BigInteger userSourceId;
    @Size(max = 40)
    @Column(name = "bankOperationNumber")
    private String bankOperationNumber;
    @Size(max = 500)
    @Column(name = "additional")
    private String additional;
    @Size(max = 500)
    @Column(name = "additional2")
    private String additional2;
    @JoinColumn(name = "commisionId", referencedColumnName = "id")
    @ManyToOne
    private Commission commisionId;
    @JoinColumn(name = "productId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product productId;
    @JoinColumn(name = "transactionId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Transaction transactionId;
    @JoinColumn(name = "bankId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bank bankId;
    @JoinColumn(name = "bankOperationTypeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BankOperationType bankOperationTypeId;
    @JoinColumn(name = "bankOperationModeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BankOperationMode bankOperationModeId;

    public BankOperation() {
    }

    public BankOperation(Long id) {
        this.id = id;
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

    public String getBankOperationNumber() {
        return bankOperationNumber;
    }

    public void setBankOperationNumber(String bankOperationNumber) {
        this.bankOperationNumber = bankOperationNumber;
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

    public Commission getCommisionId() {
        return commisionId;
    }

    public void setCommisionId(Commission commisionId) {
        this.commisionId = commisionId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Transaction getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Transaction transactionId) {
        this.transactionId = transactionId;
    }

    public Bank getBankId() {
        return bankId;
    }

    public void setBankId(Bank bankId) {
        this.bankId = bankId;
    }

    public BankOperationType getBankOperationTypeId() {
        return bankOperationTypeId;
    }

    public void setBankOperationTypeId(BankOperationType bankOperationTypeId) {
        this.bankOperationTypeId = bankOperationTypeId;
    }

    public BankOperationMode getBankOperationModeId() {
        return bankOperationModeId;
    }

    public void setBankOperationModeId(BankOperationMode bankOperationModeId) {
        this.bankOperationModeId = bankOperationModeId;
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
        if (!(object instanceof BankOperation)) {
            return false;
        }
        BankOperation other = (BankOperation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.BankOperation[ id=" + id + " ]";
    }
    
}
