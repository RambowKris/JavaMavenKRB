/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import framework.confidentiality.SystemConfiguration;
import framework.support.DataConverter;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.SystemConfig;

/**
 *
 * @author krb
 */
@Stateful
public class SystemConfigEJB {

    @PersistenceContext
    EntityManager em;

    public SystemConfiguration getSystemConfiguration() {
        SystemConfig systemConfig = (SystemConfig) this.em.createNamedQuery("SystemConfig.findAll").getResultList().get(0);

        return (SystemConfiguration) DataConverter.getObjectFromJson(systemConfig.getConfig(), SystemConfiguration.class);
    }
}
