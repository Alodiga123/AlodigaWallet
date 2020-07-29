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
@Table(name = "bank_operation_mode")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BankOperationMode.findAll", query = "SELECT b FROM BankOperationMode b")
    , @NamedQuery(name = "BankOperationMode.findById", query = "SELECT b FROM BankOperationMode b WHERE b.id = :id")
    , @NamedQuery(name = "BankOperationMode.findByName", query = "SELECT b FROM BankOperationMode b WHERE b.name = :name")})
public class BankOperationMode implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankOperationModeId")
    private Collection<BankOperation> bankOperationCollection;

    public BankOperationMode() {
    }

    public BankOperationMode(Long id) {
        this.id = id;
    }

    public BankOperationMode(Long id, String name) {
        this.id = id;
        this.name = name;
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
        if (!(object instanceof BankOperationMode)) {
            return false;
        }
        BankOperationMode other = (BankOperationMode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.BankOperationMode[ id=" + id + " ]";
    }
    
}
