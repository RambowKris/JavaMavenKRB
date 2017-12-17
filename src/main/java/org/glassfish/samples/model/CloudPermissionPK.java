/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author krb
 */
@Embeddable
public class CloudPermissionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 127)
    @Column(name = "TENANT_NAME")
    private String tenantName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 127)
    @Column(name = "SERVICE_NAME")
    private String serviceName;

    public CloudPermissionPK() {
    }

    public CloudPermissionPK(String tenantName, String serviceName) {
        this.tenantName = tenantName;
        this.serviceName = serviceName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
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
        hash += (tenantName != null ? tenantName.hashCode() : 0);
        hash += (serviceName != null ? serviceName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CloudPermissionPK)) {
            return false;
        }
        CloudPermissionPK other = (CloudPermissionPK) object;
        if ((this.tenantName == null && other.tenantName != null) || (this.tenantName != null && !this.tenantName.equals(other.tenantName))) {
            return false;
        }
        if ((this.serviceName == null && other.serviceName != null) || (this.serviceName != null && !this.serviceName.equals(other.serviceName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.CloudPermissionPK[ tenantName=" + tenantName + ", serviceName=" + serviceName + " ]";
    }
    
}
