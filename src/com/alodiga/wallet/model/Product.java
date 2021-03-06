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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
    @NamedQuery(name = "Product.findByTaxInclude", query = "SELECT p FROM Product p WHERE p.taxInclude = :taxInclude"),
    @NamedQuery(name = "Product.findByEnabled", query = "SELECT p FROM Product p WHERE p.enabled = :enabled"),
    @NamedQuery(name = "Product.findByReferenceCode", query = "SELECT p FROM Product p WHERE p.referenceCode = :referenceCode"),
    @NamedQuery(name = "Product.findByRatesUrl", query = "SELECT p FROM Product p WHERE p.ratesUrl = :ratesUrl"),
    @NamedQuery(name = "Product.findByAccessNumberUrl", query = "SELECT p FROM Product p WHERE p.accessNumberUrl = :accessNumberUrl"),
    @NamedQuery(name = "Product.findByIsFree", query = "SELECT p FROM Product p WHERE p.isFree = :isFree"),
    @NamedQuery(name = "Product.findByIsAlocashProduct", query = "SELECT p FROM Product p WHERE p.isAlocashProduct = :isAlocashProduct")})
public class Product implements Serializable {
    
    public static final Long ALOCOIN_PRODUCT = 1L ;
    public static final Long ALODIGA_BALANCE = 2L ;
    public static final Long PREPAID_CARD = 3L ;
    
    @OneToMany(mappedBy = "productId")
    private Collection<Withdrawal> withdrawalCollection;
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
    @Column(name = "taxInclude")
    private boolean taxInclude;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @Basic(optional = false)
    @Column(name = "referenceCode")
    private String referenceCode;
    @Column(name = "ratesUrl")
    private String ratesUrl;
    @Column(name = "accessNumberUrl")
    private String accessNumberUrl;
    @Basic(optional = false)
    @Column(name = "isFree")
    private boolean isFree;
    @Basic(optional = false)
    @Column(name = "isAlocashProduct")
    private boolean isAlocashProduct;
    @OneToMany(mappedBy = "productId")
    private Collection<Transaction> transactionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<Promotion> promotionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<Commission> commissionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<ProductData> productDataCollection;
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Category categoryId;
    @JoinColumn(name = "productIntegrationTypeId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductIntegrationType productIntegrationTypeId;
    @JoinColumn(name = "enterpriseId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Enterprise enterpriseId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productId")
    private Collection<BalanceHistory> balanceHistoryCollection;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id, String name, boolean taxInclude, boolean enabled, String referenceCode, boolean isFree, boolean isAlocashProduct) {
        this.id = id;
        this.name = name;
        this.taxInclude = taxInclude;
        this.enabled = enabled;
        this.referenceCode = referenceCode;
        this.isFree = isFree;
        this.isAlocashProduct = isAlocashProduct;
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

    public boolean getTaxInclude() {
        return taxInclude;
    }

    public void setTaxInclude(boolean taxInclude) {
        this.taxInclude = taxInclude;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getRatesUrl() {
        return ratesUrl;
    }

    public void setRatesUrl(String ratesUrl) {
        this.ratesUrl = ratesUrl;
    }

    public String getAccessNumberUrl() {
        return accessNumberUrl;
    }

    public void setAccessNumberUrl(String accessNumberUrl) {
        this.accessNumberUrl = accessNumberUrl;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean isFree) {
        this.isFree = isFree;
    }

    public boolean getIsAlocashProduct() {
        return isAlocashProduct;
    }

    public void setIsAlocashProduct(boolean isAlocashProduct) {
        this.isAlocashProduct = isAlocashProduct;
    }

    @XmlTransient
    public Collection<Transaction> getTransactionCollection() {
        return transactionCollection;
    }

    public void setTransactionCollection(Collection<Transaction> transactionCollection) {
        this.transactionCollection = transactionCollection;
    }

    @XmlTransient
    public Collection<Promotion> getPromotionCollection() {
        return promotionCollection;
    }

    public void setPromotionCollection(Collection<Promotion> promotionCollection) {
        this.promotionCollection = promotionCollection;
    }

    @XmlTransient
    public Collection<Commission> getCommissionCollection() {
        return commissionCollection;
    }

    public void setCommissionCollection(Collection<Commission> commissionCollection) {
        this.commissionCollection = commissionCollection;
    }

    @XmlTransient
    public Collection<ProductData> getProductDataCollection() {
        return productDataCollection;
    }

    public void setProductDataCollection(Collection<ProductData> productDataCollection) {
        this.productDataCollection = productDataCollection;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public ProductIntegrationType getProductIntegrationTypeId() {
        return productIntegrationTypeId;
    }

    public void setProductIntegrationTypeId(ProductIntegrationType productIntegrationTypeId) {
        this.productIntegrationTypeId = productIntegrationTypeId;
    }

    public Enterprise getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Enterprise enterpriseId) {
        this.enterpriseId = enterpriseId;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Product[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Withdrawal> getWithdrawalCollection() {
        return withdrawalCollection;
    }

    public void setWithdrawalCollection(Collection<Withdrawal> withdrawalCollection) {
        this.withdrawalCollection = withdrawalCollection;
    }
    
}
