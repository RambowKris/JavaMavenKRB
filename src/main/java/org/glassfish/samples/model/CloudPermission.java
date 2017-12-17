/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author krb
 */
@Entity
@Table(name = "CLOUD_PERMISSION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CloudPermission.findAll", query = "SELECT c FROM CloudPermission c")
    , @NamedQuery(name = "CloudPermission.findByTenantName", query = "SELECT c FROM CloudPermission c WHERE c.cloudPermissionPK.tenantName = :tenantName")
    , @NamedQuery(name = "CloudPermission.findByServiceName", query = "SELECT c FROM CloudPermission c WHERE c.cloudPermissionPK.serviceName = :serviceName")})
public class CloudPermission implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CloudPermissionPK cloudPermissionPK;

    public CloudPermission() {
    }

    public CloudPermission(CloudPermissionPK cloudPermissionPK) {
        this.cloudPermissionPK = cloudPermissionPK;
    }

    public CloudPermission(String tenantName, String serviceName) {
        this.cloudPermissionPK = new CloudPermissionPK(tenantName, serviceName);
    }

    public CloudPermissionPK getCloudPermissionPK() {
        return cloudPermissionPK;
    }

    public void setCloudPermissionPK(CloudPermissionPK cloudPermissionPK) {
        this.cloudPermissionPK = cloudPermissionPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cloudPermissionPK != null ? cloudPermissionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CloudPermission)) {
            return false;
        }
        CloudPermission other = (CloudPermission) object;
        if ((this.cloudPermissionPK == null && other.cloudPermissionPK != null) || (this.cloudPermissionPK != null && !this.cloudPermissionPK.equals(other.cloudPermissionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.CloudPermission[ cloudPermissionPK=" + cloudPermissionPK + " ]";
    }
    
}
