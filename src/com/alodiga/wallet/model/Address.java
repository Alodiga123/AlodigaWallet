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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

//test Githubs

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.findById", query = "SELECT a FROM Address a WHERE a.id = :id"),
    @NamedQuery(name = "Address.findByAddress", query = "SELECT a FROM Address a WHERE a.address = :address"),
    @NamedQuery(name = "Address.findByZipCode", query = "SELECT a FROM Address a WHERE a.zipCode = :zipCode"),
    @NamedQuery(name = "Address.findByStateName", query = "SELECT a FROM Address a WHERE a.stateName = :stateName"),
    @NamedQuery(name = "Address.findByCountyName", query = "SELECT a FROM Address a WHERE a.countyName = :countyName"),
    @NamedQuery(name = "Address.findByCityName", query = "SELECT a FROM Address a WHERE a.cityName = :cityName")})
public class Address implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressId")
    private Collection<Cumplimient> cumplimientCollection;
    

    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "zipCode")
    private String zipCode;
    @Column(name = "stateName")
    private String stateName;
    @Column(name = "countyName")
    private String countyName;
    @Column(name = "cityName")
    private String cityName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billingAddressId")
    private Collection<PaymentInfo> paymentInfoCollection;
    @JoinColumn(name = "stateId", referencedColumnName = "id")
    @ManyToOne
    private State stateId;
    @JoinColumn(name = "countyId", referencedColumnName = "id")
    @ManyToOne
    private County countyId;
    @JoinColumn(name = "countryId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Country countryId;
    @JoinColumn(name = "cityId", referencedColumnName = "id")
    @ManyToOne
    private City cityId;

    public Address() {
    }

    public Address(Long id) {
        this.id = id;
    }

    public Address(Long id, String address, String zipCode) {
        this.id = id;
        this.address = address;
        this.zipCode = zipCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @XmlTransient
    public Collection<PaymentInfo> getPaymentInfoCollection() {
        return paymentInfoCollection;
    }

    public void setPaymentInfoCollection(Collection<PaymentInfo> paymentInfoCollection) {
        this.paymentInfoCollection = paymentInfoCollection;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public County getCountyId() {
        return countyId;
    }

    public void setCountyId(County countyId) {
        this.countyId = countyId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public City getCityId() {
        return cityId;
    }

    public void setCityId(City cityId) {
        this.cityId = cityId;
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
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Address[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Cumplimient> getCumplimientCollection() {
        return cumplimientCollection;
    }

    public void setCumplimientCollection(Collection<Cumplimient> cumplimientCollection) {
        this.cumplimientCollection = cumplimientCollection;
    }
    
}
