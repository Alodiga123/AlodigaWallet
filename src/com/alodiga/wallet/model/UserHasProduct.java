/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * @author usuario
 */
@Entity
@Table(name = "user_has_product")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserHasProduct.findAll", query = "SELECT u FROM UserHasProduct u"),
    @NamedQuery(name = "UserHasProduct.findById", query = "SELECT u FROM UserHasProduct u WHERE u.id = :id"),
    @NamedQuery(name = "UserHasProduct.findByProductId", query = "SELECT u FROM UserHasProduct u WHERE u.productId = :productId"),
    @NamedQuery(name = "UserHasProduct.findByUserSourceIdAllProduct", query = "SELECT u FROM UserHasProduct u WHERE u.userSourceId = :userSourceId"),
    @NamedQuery(name = "UserHasProduct.findByUserSourceId", query = "SELECT u FROM UserHasProduct u WHERE u.userSourceId = :userSourceId AND u.productId = 3")})
public class UserHasProduct implements Serializable {
    
    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    private Timestamp beginningDate;
    
    private Timestamp endingDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "productId")
    private long productId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userSourceId")
    private long userSourceId;

    public UserHasProduct() {
    }

    public UserHasProduct(Long id) {
        this.id = id;
    }

    public UserHasProduct(Long id, long productId, long userSourceId) {
        this.id = id;
        this.productId = productId;
        this.userSourceId = userSourceId;
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

    public long getUserSourceId() {
        return userSourceId;
    }

    public void setUserSourceId(long userSourceId) {
        this.userSourceId = userSourceId;
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
        if (!(object instanceof UserHasProduct)) {
            return false;
        }
        UserHasProduct other = (UserHasProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.UserHasProduct[ id=" + id + " ]";
    }
    
    public Timestamp getBeginningDate() {
        return beginningDate;
}

    public void setBeginningDate(Timestamp beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Timestamp getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Timestamp endingDate) {
        this.endingDate = endingDate;
    }
    
}
