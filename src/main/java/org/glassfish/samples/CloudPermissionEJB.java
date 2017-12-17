/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.CloudPermission;
import org.glassfish.samples.model.Tenant;

/**
 *
 * @author krb
 */
@Stateful
@Named
public class CloudPermissionEJB {

    @PersistenceContext
    EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public List getList() {
        return this.em.createNamedQuery("CloudPermission.findAll").getResultList();
    }

    public List getCloudPermissionsByTenantName(String tenantName) {
        return this.em.createNamedQuery("CloudPermission.findByTenantName").setParameter("tenantName", tenantName).getResultList();
    }

    public Boolean tenantHasPermissionTo(Tenant tenant, String serviceName) {
        List<CloudPermission> tenantPermissions = this.getCloudPermissionsByTenantName(tenant.getTenant());

        for (CloudPermission cloudPermission : tenantPermissions) {
            if (cloudPermission.getCloudPermissionPK().getServiceName().equalsIgnoreCase(serviceName)) {
                return true;
            }
        }
        return false;
    }

}
