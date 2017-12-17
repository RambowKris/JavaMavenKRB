/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.model;

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
 * @author krb
 */
@Entity
@Table(name = "CLOUD_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CloudUser.findAll", query = "SELECT c FROM CloudUser c")
    , @NamedQuery(name = "CloudUser.findByUserName", query = "SELECT c FROM CloudUser c WHERE c.userName = :userName")
    , @NamedQuery(name = "CloudUser.findByTenant", query = "SELECT c FROM CloudUser c WHERE c.tenant = :tenant")
    , @NamedQuery(name = "CloudUser.findByPassword", query = "SELECT c FROM CloudUser c WHERE c.password = :password")
    , @NamedQuery(name = "CloudUser.findByExpirationTime", query = "SELECT c FROM CloudUser c WHERE c.expirationTime = :expirationTime")})
public class CloudUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USER_NAME")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TENANT")
    private String tenant;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 511)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "EXPIRATION_TIME")
    private String expirationTime;

    public CloudUser() {
    }

    public CloudUser(String userName) {
        this.userName = userName;
    }

    public CloudUser(String userName, String password, String tenant, String expirationTime) {
        this.userName = userName;
        this.password = password;
        this.tenant = tenant;
        this.expirationTime = expirationTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantName() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CloudUser)) {
            return false;
        }
        CloudUser other = (CloudUser) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.CloudUser[ userName=" + userName + " ]";
    }

}
