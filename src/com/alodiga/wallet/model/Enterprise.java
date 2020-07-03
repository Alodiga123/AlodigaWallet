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
@Table(name = "enterprise")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enterprise.findAll", query = "SELECT e FROM Enterprise e"),
    @NamedQuery(name = "Enterprise.findById", query = "SELECT e FROM Enterprise e WHERE e.id = :id"),
    @NamedQuery(name = "Enterprise.findByName", query = "SELECT e FROM Enterprise e WHERE e.name = :name"),
    @NamedQuery(name = "Enterprise.findByUrl", query = "SELECT e FROM Enterprise e WHERE e.url = :url"),
    @NamedQuery(name = "Enterprise.findByEmail", query = "SELECT e FROM Enterprise e WHERE e.email = :email"),
    @NamedQuery(name = "Enterprise.findByAtcNumber", query = "SELECT e FROM Enterprise e WHERE e.atcNumber = :atcNumber"),
    @NamedQuery(name = "Enterprise.findByAddress", query = "SELECT e FROM Enterprise e WHERE e.address = :address"),
    @NamedQuery(name = "Enterprise.findByInvoiceAddress", query = "SELECT e FROM Enterprise e WHERE e.invoiceAddress = :invoiceAddress"),
    @NamedQuery(name = "Enterprise.findByEnabled", query = "SELECT e FROM Enterprise e WHERE e.enabled = :enabled"),
    @NamedQuery(name = "Enterprise.findByInfoEmail", query = "SELECT e FROM Enterprise e WHERE e.infoEmail = :infoEmail")})
public class Enterprise implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterpriseId")
    private Collection<Bank> bankCollection;
    private static final long serialVersionUID = 1L;
    public static final Long ALODIGA = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "atcNumber")
    private String atcNumber;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "invoiceAddress")
    private String invoiceAddress;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "infoEmail")
    private String infoEmail;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterpriseId")
    private Collection<PreferenceValue> preferenceValueCollection;
    @JoinColumn(name = "currencyId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Currency currencyId;
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Country countryId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterpriseId")
    private Collection<Product> productCollection;

    public Enterprise() {
    }

    public Enterprise(Long id) {
        this.id = id;
    }

    public Enterprise(Long id, String name, String url, String email, String atcNumber, String address, String invoiceAddress, boolean enabled) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.email = email;
        this.atcNumber = atcNumber;
        this.address = address;
        this.invoiceAddress = invoiceAddress;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAtcNumber() {
        return atcNumber;
    }

    public void setAtcNumber(String atcNumber) {
        this.atcNumber = atcNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getInfoEmail() {
        return infoEmail;
    }

    public void setInfoEmail(String infoEmail) {
        this.infoEmail = infoEmail;
    }

    @XmlTransient
    public Collection<PreferenceValue> getPreferenceValueCollection() {
        return preferenceValueCollection;
    }

    public void setPreferenceValueCollection(Collection<PreferenceValue> preferenceValueCollection) {
        this.preferenceValueCollection = preferenceValueCollection;
    }

    public Currency getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Currency currencyId) {
        this.currencyId = currencyId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    @XmlTransient
    public Collection<Product> getProductCollection() {
        return productCollection;
    }

    public void setProductCollection(Collection<Product> productCollection) {
        this.productCollection = productCollection;
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
        if (!(object instanceof Enterprise)) {
            return false;
        }
        Enterprise other = (Enterprise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Enterprise[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Bank> getBankCollection() {
        return bankCollection;
    }

    public void setBankCollection(Collection<Bank> bankCollection) {
        this.bankCollection = bankCollection;
    }
    
}
