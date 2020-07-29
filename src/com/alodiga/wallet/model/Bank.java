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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "bank")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bank.findAll", query = "SELECT b FROM Bank b"),
    @NamedQuery(name = "Bank.findById", query = "SELECT b FROM Bank b WHERE b.id = :id"),
    @NamedQuery(name = "Bank.findByName", query = "SELECT b FROM Bank b WHERE b.name = :name"),
    @NamedQuery(name = "Bank.findByCountryIdBank", query = "SELECT b FROM Bank b WHERE b.countryId.id = :countryId"),
    @NamedQuery(name = "Bank.findGroupByCountry", query = "SELECT b FROM Bank b GROUP BY b.countryId.id"),
    @NamedQuery(name = "Bank.findByAba", query = "SELECT b FROM Bank b WHERE b.aba = :aba")})
public class Bank implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Collection<BankOperation> bankOperationCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "aba")
    private String aba;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankId")
    private Collection<UserHasBank> userHasBankCollection;
    @JoinColumn(name = "enterpriseId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Enterprise enterpriseId;
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Country countryId;

    public Bank() {
    }

    public Bank(Long id) {
        this.id = id;
    }

    public Bank(Long id, String name, String aba) {
        this.id = id;
        this.name = name;
        this.aba = aba;
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

    public String getAba() {
        return aba;
    }

    public void setAba(String aba) {
        this.aba = aba;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<UserHasBank> getUserHasBankCollection() {
        return userHasBankCollection;
    }

    public void setUserHasBankCollection(Collection<UserHasBank> userHasBankCollection) {
        this.userHasBankCollection = userHasBankCollection;
    }

    public Enterprise getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Enterprise enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
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
        if (!(object instanceof Bank)) {
            return false;
        }
        Bank other = (Bank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.Bank[ id=" + id + " ]";
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
