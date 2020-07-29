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

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "commission_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommissionItem.findAll", query = "SELECT c FROM CommissionItem c"),
    @NamedQuery(name = "CommissionItem.findById", query = "SELECT c FROM CommissionItem c WHERE c.id = :id"),
    @NamedQuery(name = "CommissionItem.findByAmount", query = "SELECT c FROM CommissionItem c WHERE c.amount = :amount"),
    @NamedQuery(name = "CommissionItem.findByProcessedDate", query = "SELECT c FROM CommissionItem c WHERE c.processedDate = :processedDate"),
    @NamedQuery(name = "CommissionItem.findByIsResidual", query = "SELECT c FROM CommissionItem c WHERE c.isResidual = :isResidual")})
public class CommissionItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "amount")
    private float amount;
    @Basic(optional = false)
    @Column(name = "processedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processedDate;
    @Basic(optional = false)
    @Column(name = "isResidual")
    private boolean isResidual;
    @JoinColumn(name = "transactionId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Transaction transactionId;
    @JoinColumn(name = "commissionId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Commission commissionId;

    public CommissionItem() {
    }

    public CommissionItem(Long id) {
        this.id = id;
    }

    public CommissionItem(Long id, float amount, Date processedDate, boolean isResidual) {
        this.id = id;
        this.amount = amount;
        this.processedDate = processedDate;
        this.isResidual = isResidual;
    }

    public CommissionItem(float amount, Date processedDate, Transaction transactionId, Commission commissionId) {
        this.amount = amount;
        this.processedDate = processedDate;
        this.transactionId = transactionId;
        this.commissionId = commissionId;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public boolean getIsResidual() {
        return isResidual;
    }

    public void setIsResidual(boolean isResidual) {
        this.isResidual = isResidual;
    }

    public Transaction getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Transaction transactionId) {
        this.transactionId = transactionId;
    }

    public Commission getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(Commission commissionId) {
        this.commissionId = commissionId;
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
        if (!(object instanceof CommissionItem)) {
            return false;
        }
        CommissionItem other = (CommissionItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.CommissionItem[ id=" + id + " ]";
    }
    
}
