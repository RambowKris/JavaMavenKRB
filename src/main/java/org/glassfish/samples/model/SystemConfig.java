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
@Table(name = "SYSTEM_CONFIG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemConfig.findAll", query = "SELECT s FROM SystemConfig s")
    , @NamedQuery(name = "SystemConfig.findByConfig", query = "SELECT s FROM SystemConfig s WHERE s.config = :config")})
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16383)
    @Column(name = "CONFIG")
    private String config;

    public SystemConfig() {
    }

    public SystemConfig(String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (config != null ? config.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemConfig)) {
            return false;
        }
        SystemConfig other = (SystemConfig) object;
        if ((this.config == null && other.config != null) || (this.config != null && !this.config.equals(other.config))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.SystemConfig[ config=" + config + " ]";
    }
    
}
