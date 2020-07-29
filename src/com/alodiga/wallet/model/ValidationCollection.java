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
@Table(name = "validation_collection")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ValidationCollection.findAll", query = "SELECT v FROM ValidationCollection v")
    , @NamedQuery(name = "ValidationCollection.findById", query = "SELECT v FROM ValidationCollection v WHERE v.id = :id")
    , @NamedQuery(name = "ValidationCollection.findByName", query = "SELECT v FROM ValidationCollection v WHERE v.name = :name")
    , @NamedQuery(name = "ValidationCollection.findByStatus", query = "SELECT v FROM ValidationCollection v WHERE v.status = :status")
    , @NamedQuery(name = "ValidationCollection.findByStatusByLanguage", query = "SELECT v FROM ValidationCollection v WHERE v.status = 1 AND v.languageId.iso =:languageId AND v.countryId.id = :countryId")
    , @NamedQuery(name = "ValidationCollection.findByCountrId", query = "SELECT v FROM ValidationCollection v WHERE v.countryId.id = :countryId")
    , @NamedQuery(name = "ValidationCollection.findByAdditional", query = "SELECT v FROM ValidationCollection v WHERE v.additional = :additional")})
public class ValidationCollection implements Serializable {

    
            
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "status")
    private String status;
    @Size(max = 45)
    @Column(name = "additional")
    private String additional;
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Country countryId;
    @JoinColumn(name = "languageId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Language languageId;

    public ValidationCollection() {
    }

    public ValidationCollection(Long id) {
        this.id = id;
    }

    public ValidationCollection(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public Language getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Language languageId) {
        this.languageId = languageId;
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
        if (!(object instanceof ValidationCollection)) {
            return false;
        }
        ValidationCollection other = (ValidationCollection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.ValidationCollection[ id=" + id + " ]";
    }
    
}
