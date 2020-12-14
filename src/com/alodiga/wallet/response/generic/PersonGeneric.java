/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.wallet.response.generic;


import com.alodiga.wallet.common.model.Country;
import com.alodiga.wallet.common.model.PersonType;
import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author ltoro
 * @see Este método se desarrolla por que la respuesta de person devuelve un ciclo infinito. 
 */
public class PersonGeneric implements Serializable {
 
    private Long id;
    private String email;
    private String webSite;
    private Date createDate;
    private Date updateDate;
    private Country countryId;
    private PersonType personTypeId;

    public PersonGeneric(Long id, String email, String webSite, Date createDate, Date updateDate, Country countryId, PersonType personTypeId) {
        this.id = id;
        this.email = email;
        this.webSite = webSite;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.countryId = countryId;
        this.personTypeId = personTypeId;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public PersonType getPersonTypeId() {
        return personTypeId;
    }

    public void setPersonTypeId(PersonType personTypeId) {
        this.personTypeId = personTypeId;
    }
   
    
    
    
   
}
