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
@Table(name = "sms_provider")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SmsProvider.findAll", query = "SELECT s FROM SmsProvider s")
    , @NamedQuery(name = "SmsProvider.findById", query = "SELECT s FROM SmsProvider s WHERE s.id = :id")
    , @NamedQuery(name = "SmsProvider.findByName", query = "SELECT s FROM SmsProvider s WHERE s.name = :name")
    , @NamedQuery(name = "SmsProvider.findByDescription", query = "SELECT s FROM SmsProvider s WHERE s.description = :description")
    , @NamedQuery(name = "SmsProvider.findByAddres", query = "SELECT s FROM SmsProvider s WHERE s.addres = :addres")
    , @NamedQuery(name = "SmsProvider.findByPort", query = "SELECT s FROM SmsProvider s WHERE s.port = :port")
    , @NamedQuery(name = "SmsProvider.findByWsdl", query = "SELECT s FROM SmsProvider s WHERE s.wsdl = :wsdl")
    , @NamedQuery(name = "SmsProvider.findByIntegrationType", query = "SELECT s FROM SmsProvider s WHERE s.integrationType = :integrationType")})
public class SmsProvider implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "name")
    private boolean name;
    @Size(max = 45)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "addres")
    private String addres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "port")
    private String port;
    @Size(max = 45)
    @Column(name = "wsdl")
    private String wsdl;
    @Size(max = 45)
    @Column(name = "integrationType")
    private String integrationType;
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Country countryId;

    public SmsProvider() {
    }

    public SmsProvider(Long id) {
        this.id = id;
    }

    public SmsProvider(Long id, boolean name, String addres, String port) {
        this.id = id;
        this.name = name;
        this.addres = addres;
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getWsdl() {
        return wsdl;
    }

    public void setWsdl(String wsdl) {
        this.wsdl = wsdl;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(String integrationType) {
        this.integrationType = integrationType;
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
        if (!(object instanceof SmsProvider)) {
            return false;
        }
        SmsProvider other = (SmsProvider) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.SmsProvider[ id=" + id + " ]";
    }
    
}
