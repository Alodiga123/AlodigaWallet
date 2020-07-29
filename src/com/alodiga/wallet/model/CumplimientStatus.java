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
 * @author ltoro
 */
@Entity
@Table(name = "cumplimient_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CumplimientStatus.findAll", query = "SELECT c FROM CumplimientStatus c")
    , @NamedQuery(name = "CumplimientStatus.findById", query = "SELECT c FROM CumplimientStatus c WHERE c.id = :id")
    , @NamedQuery(name = "CumplimientStatus.findByValue", query = "SELECT c FROM CumplimientStatus c WHERE c.value = :value")})
public class CumplimientStatus implements Serializable {

    public static final Long IN_PROCESS = 1L;    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "value")
    private String value;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "complientStatusId")
    private Collection<Cumplimient> cumplimientCollection;

    public CumplimientStatus() {
    }

    public CumplimientStatus(Long id) {
        this.id = id;
    }

    public CumplimientStatus(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Cumplimient> getCumplimientCollection() {
        return cumplimientCollection;
    }

    public void setCumplimientCollection(Collection<Cumplimient> cumplimientCollection) {
        this.cumplimientCollection = cumplimientCollection;
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
        if (!(object instanceof CumplimientStatus)) {
            return false;
        }
        CumplimientStatus other = (CumplimientStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.CumplimientStatus[ id=" + id + " ]";
    }
    
}
