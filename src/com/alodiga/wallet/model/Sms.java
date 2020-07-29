/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "sms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sms.findAll", query = "SELECT s FROM Sms s"),
    @NamedQuery(name = "Sms.findById", query = "SELECT s FROM Sms s WHERE s.id = :id"),
    @NamedQuery(name = "Sms.findByUserId", query = "SELECT s FROM Sms s WHERE s.userId = :userId"),
    @NamedQuery(name = "Sms.findByIntegratorName", query = "SELECT s FROM Sms s WHERE s.integratorName = :integratorName"),
    @NamedQuery(name = "Sms.findBySender", query = "SELECT s FROM Sms s WHERE s.sender = :sender"),
    @NamedQuery(name = "Sms.findByDestination", query = "SELECT s FROM Sms s WHERE s.destination = :destination"),
    @NamedQuery(name = "Sms.findByContent", query = "SELECT s FROM Sms s WHERE s.content = :content"),
    @NamedQuery(name = "Sms.findByCreationDate", query = "SELECT s FROM Sms s WHERE s.creationDate = :creationDate"),
    @NamedQuery(name = "Sms.findByAdditional", query = "SELECT s FROM Sms s WHERE s.additional = :additional"),
    @NamedQuery(name = "Sms.findByStatus", query = "SELECT s FROM Sms s WHERE s.status = :status")})
public class Sms implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "userId")
    private BigInteger userId;
    @Column(name = "integratorName")
    private String integratorName;
    @Column(name = "sender")
    private String sender;
    @Column(name = "destination")
    private String destination;
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "additional")
    private String additional;
    @Basic(optional = false)
    @Column(name = "status")
    private String status;

    public Sms() {
    }

    public Sms(Long id) {
        this.id = id;
    }

    public Sms(Long id, Date creationDate, String status) {
        this.id = id;
        this.creationDate = creationDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getIntegratorName() {
        return integratorName;
    }

    public void setIntegratorName(String integratorName) {
        this.integratorName = integratorName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof Sms)) {
            return false;
        }
        Sms other = (Sms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Sms[ id=" + id + " ]";
    }
    
}
