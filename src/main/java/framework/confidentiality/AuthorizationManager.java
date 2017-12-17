/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.confidentiality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.samples.CloudPermissionEJB;
import org.glassfish.samples.TenantEJB;
import org.glassfish.samples.model.Tenant;

/**
 *
 * @author krb
 */
public class AuthorizationManager {

    private TenantEJB tenantBean;
    private CloudPermissionEJB cloudPermissionBean;

    private Tenant tenant;

    public AuthorizationManager(
            TenantEJB tenantBean,
            CloudPermissionEJB cloudPermissionBean
    ) {
        this.tenantBean = tenantBean;
        this.cloudPermissionBean = cloudPermissionBean;
    }

    public Boolean authorize(String tenantName, String service) {

        this.tenant = this.tenantBean.getTenantByTenantName(tenantName);
        if (tenant == null) {
            return false;
        }

        if (!this.cloudPermissionBean.tenantHasPermissionTo(this.tenant, service)) {
            return false;
        }

        return true;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

}
