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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ltoro
 */
@Entity
@Table(name = "user_has_card")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserHasCard.findAll", query = "SELECT u FROM UserHasCard u")
    , @NamedQuery(name = "UserHasCard.findById", query = "SELECT u FROM UserHasCard u WHERE u.id = :id")
    , @NamedQuery(name = "UserHasCard.findByUserId", query = "SELECT u FROM UserHasCard u WHERE u.userId = :userId")
    , @NamedQuery(name = "UserHasCard.findByUserIdAndParentId", query = "SELECT u FROM UserHasCard u WHERE u.userId = :userId AND u.cardId.parentId = NULL")
    , @NamedQuery(name = "UserHasCard.findByUserIdAndCardNumber", query = "SELECT u FROM UserHasCard u WHERE u.userId = :userId AND u.cardId.id = :cardNumber ")
    , @NamedQuery(name = "UserHasCard.findByAditional", query = "SELECT u FROM UserHasCard u WHERE u.aditional = :aditional")}) 
public class UserHasCard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "userId")
    private long userId;
    @Size(max = 45)
    @Column(name = "aditional")
    private String aditional;
    @JoinColumn(name = "cardId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Card cardId;

    public UserHasCard() {
    }

    public UserHasCard(Long id) {
        this.id = id;
    }

    public UserHasCard(Long id, long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAditional() {
        return aditional;
    }

    public void setAditional(String aditional) {
        this.aditional = aditional;
    }

    public Card getCardId() {
        return cardId;
    }

    public void setCardId(Card cardId) {
        this.cardId = cardId;
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
        if (!(object instanceof UserHasCard)) {
            return false;
        }
        UserHasCard other = (UserHasCard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.UserHasCard[ id=" + id + " ]";
    }
    
}
