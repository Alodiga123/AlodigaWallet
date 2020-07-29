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
@Table(name = "userWS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserWS.findAll", query = "SELECT u FROM UserWS u")
    , @NamedQuery(name = "UserWS.findByUsuario", query = "SELECT u FROM UserWS u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "UserWS.findByPassword", query = "SELECT u FROM UserWS u WHERE u.password = :password")
    , @NamedQuery(name = "UserWS.findByUserANDPassword", query = "SELECT u FROM UserWS u")})
public class UserWS implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "password")
    private String password;

    public UserWS() {
    }

    public UserWS(String usuario) {
        this.usuario = usuario;
    }

    public UserWS(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserWS)) {
            return false;
        }
        UserWS other = (UserWS) object;
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alodiga.wallet.model.UserWS[ usuario=" + usuario + " ]";
    }
    
}
