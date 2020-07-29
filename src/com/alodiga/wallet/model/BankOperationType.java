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
import javax.persistence.Id;
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
 * @author jose
 */
@Entity
@Table(name = "bank_operation_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BankOperationType.findAll", query = "SELECT b FROM BankOperationType b")
    , @NamedQuery(name = "BankOperationType.findById", query = "SELECT b FROM BankOperationType b WHERE b.id = :id")
    , @NamedQuery(name = "BankOperationType.findByName", query = "SELECT b FROM BankOperationType b WHERE b.name = :name")})
public class BankOperationType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankOperationTypeId")
    private Collection<BankOperation> bankOperationCollection;

    public BankOperationType() {
    }

    public BankOperationType(Long id) {
        this.id = id;
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

    @XmlTransient
    @JsonIgnore
    public Collection<BankOperation> getBankOperationCollection() {
        return bankOperationCollection;
    }

    public void setBankOperationCollection(Collection<BankOperation> bankOperationCollection) {
        this.bankOperationCollection = bankOperationCollection;
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
        if (!(object instanceof BankOperationType)) {
            return false;
        }
        BankOperationType other = (BankOperationType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.BankOperationType[ id=" + id + " ]";
    }
    
}
