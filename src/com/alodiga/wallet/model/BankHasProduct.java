/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ltoro
 */
@Entity
@Table(name = "bank_has_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BankHasProduct.findAll", query = "SELECT b FROM BankHasProduct b")
    , @NamedQuery(name = "BankHasProduct.findById", query = "SELECT b FROM BankHasProduct b WHERE b.id = :id")
    , @NamedQuery(name = "BankHasProduct.findByProductId", query = "SELECT b FROM BankHasProduct b WHERE b.productId = :productId")
    , @NamedQuery(name = "BankHasProduct.findByProductIdAndBankId", query = "SELECT b FROM BankHasProduct b WHERE b.productId = :productId AND b.bankId = :bankId")
    , @NamedQuery(name = "BankHasProduct.findByBankId", query = "SELECT b FROM BankHasProduct b WHERE b.bankId = :bankId")})
public class BankHasProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "productId")
    private long productId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bankId")
    private long bankId;

    public BankHasProduct() {
    }

    public BankHasProduct(Long id) {
        this.id = id;
    }

    public BankHasProduct(Long id, long productId, long bankId) {
        this.id = id;
        this.productId = productId;
        this.bankId = bankId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
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
        if (!(object instanceof BankHasProduct)) {
            return false;
        }
        BankHasProduct other = (BankHasProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.BankHasProduct[ id=" + id + " ]";
    }
    
}
