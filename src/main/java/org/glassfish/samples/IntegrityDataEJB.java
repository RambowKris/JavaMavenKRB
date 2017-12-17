/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.IntegrityData;

/**
 *
 * @author krb
 */
@Stateful
@Named
public class IntegrityDataEJB {

    @PersistenceContext
    EntityManager em;

//    @Inject
//    IntegrityData integrityData;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public List getList() {
        return this.em.createNamedQuery("IntegrityData.findAll").getResultList();

//        return this.em.createNamedQuery("IntegrityData.findByKeyName").setParameter("keyName", "TestObject").getResultList().get(0);
    }

    public void create(String keyName, String checksum) {
        IntegrityData id = new IntegrityData(keyName, checksum);
        em.persist(id);
    }

    public IntegrityData getIntegrityData(String keyName) {
        List<IntegrityData> result = this.getList();
        for (IntegrityData integrityData : result) {
            if (integrityData.getKeyName().equalsIgnoreCase(keyName)) {
                return integrityData;
            }
        }
        return null;
    }

    public String getValue(String keyName) {
        List<IntegrityData> result = this.getList();
        String value = null;
        for (IntegrityData integrityData : result) {
            if (integrityData.getKeyName().equalsIgnoreCase(keyName)) {
                value = integrityData.getChecksum();
                return value;
            }
        }
        return value;
    }

    public void remove(String keyName) {
        IntegrityData id = this.getIntegrityData(keyName);
        this.em.remove(id);
    }
}
