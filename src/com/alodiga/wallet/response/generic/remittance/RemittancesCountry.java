/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.response.generic.remittance;

import com.alodiga.wallet.model.*;
import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ltoro
 */
@Entity
@Table(name = "top_up_country")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TopUpCountry.findAll", query = "SELECT t FROM TopUpCountry t")
    , @NamedQuery(name = "TopUpCountry.findById", query = "SELECT t FROM TopUpCountry t WHERE t.id = :id")
    , @NamedQuery(name = "TopUpCountry.findByName", query = "SELECT t FROM TopUpCountry t WHERE t.name = :name")
    , @NamedQuery(name = "TopUpCountry.findByShortName", query = "SELECT t FROM TopUpCountry t WHERE t.shortName = :shortName")
    , @NamedQuery(name = "TopUpCountry.findByCode", query = "SELECT t FROM TopUpCountry t WHERE t.code = :code")
    , @NamedQuery(name = "TopUpCountry.findByAlternativeName1", query = "SELECT t FROM TopUpCountry t WHERE t.alternativeName1 = :alternativeName1")
    , @NamedQuery(name = "TopUpCountry.findByAlternativeName2", query = "SELECT t FROM TopUpCountry t WHERE t.alternativeName2 = :alternativeName2")
    , @NamedQuery(name = "TopUpCountry.findByAlternativeName3", query = "SELECT t FROM TopUpCountry t WHERE t.alternativeName3 = :alternativeName3")})
public class RemittancesCountry implements Serializable {

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
    @Size(max = 5)
    @Column(name = "shortName")
    private String shortName;
    @Size(max = 5)
    @Column(name = "code")
    private String code;
    @Size(max = 255)
    @Column(name = "alternativeName1")
    private String alternativeName1;
    @Size(max = 255)
    @Column(name = "alternativeName2")
    private String alternativeName2;
    @Size(max = 255)
    @Column(name = "alternativeName3")
    private String alternativeName3;

    public RemittancesCountry() {
    }

    public RemittancesCountry(Long id) {
        this.id = id;
    }

    public RemittancesCountry(Long id, String name) {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlternativeName1() {
        return alternativeName1;
    }

    public void setAlternativeName1(String alternativeName1) {
        this.alternativeName1 = alternativeName1;
    }

    public String getAlternativeName2() {
        return alternativeName2;
    }

    public void setAlternativeName2(String alternativeName2) {
        this.alternativeName2 = alternativeName2;
    }

    public String getAlternativeName3() {
        return alternativeName3;
    }

    public void setAlternativeName3(String alternativeName3) {
        this.alternativeName3 = alternativeName3;
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
        if (!(object instanceof RemittancesCountry)) {
            return false;
        }
        RemittancesCountry other = (RemittancesCountry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.TopUpCountry[ id=" + id + " ]";
    }
    
}
