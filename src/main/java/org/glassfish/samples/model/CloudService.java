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
@Table(name = "CLOUD_SERVICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CloudService.findAll", query = "SELECT c FROM CloudService c")
    , @NamedQuery(name = "CloudService.findByServiceName", query = "SELECT c FROM CloudService c WHERE c.serviceName = :serviceName")})
public class CloudService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 127)
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    public CloudService() {
    }

    public CloudService(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviceName != null ? serviceName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CloudService)) {
            return false;
        }
        CloudService other = (CloudService) object;
        if ((this.serviceName == null && other.serviceName != null) || (this.serviceName != null && !this.serviceName.equals(other.serviceName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.CloudService[ serviceName=" + serviceName + " ]";
    }
    
}
