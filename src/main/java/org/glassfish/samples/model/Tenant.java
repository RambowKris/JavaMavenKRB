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
@Table(name = "TENANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tenant.findAll", query = "SELECT t FROM Tenant t")
    , @NamedQuery(name = "Tenant.findByTenant", query = "SELECT t FROM Tenant t WHERE t.tenant = :tenant")
    , @NamedQuery(name = "Tenant.findByCloudConfig", query = "SELECT t FROM Tenant t WHERE t.cloudConfig = :cloudConfig")})
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "TENANT")
    private String tenant;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "CLOUD_CONFIG")
    private String cloudConfig;

    public Tenant() {
    }

    public Tenant(String tenant) {
        this.tenant = tenant;
    }

    public Tenant(String tenant, String cloudConfig) {
        this.tenant = tenant;
        this.cloudConfig = cloudConfig;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getCloudConfig() {
        return cloudConfig;
    }

    public void setCloudConfig(String cloudConfig) {
        this.cloudConfig = cloudConfig;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tenant != null ? tenant.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tenant)) {
            return false;
        }
        Tenant other = (Tenant) object;
        if ((this.tenant == null && other.tenant != null) || (this.tenant != null && !this.tenant.equals(other.tenant))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.Tenant[ tenant=" + tenant + " ]";
    }
    
}
