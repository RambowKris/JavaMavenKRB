/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import java.util.List;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.DbStorage;

/**
 *
 * @author krb
 */
@Stateful
@Named
public class DbStorageEJB {

    @PersistenceContext
    EntityManager em;
    
//    @Inject
//    IntegrityData integrityData;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public List getList() {
        return this.em.createNamedQuery("DbStorage.findAll").getResultList();

//        return this.em.createNamedQuery("IntegrityData.findByKeyName").setParameter("keyName", "TestObject").getResultList().get(0);
    }

    public void create(String keyName, String dataValue) {
        DbStorage ds = new DbStorage(keyName, dataValue);
        em.persist(ds);
    }
    
    public String getValue(String keyName) {
        List<DbStorage> result = this.getList();
        String value = null;
        for (DbStorage dbStorage : result) {
            if (dbStorage.getKeyName().equalsIgnoreCase(keyName)) {
                value = dbStorage.getDataValue();
                break;
            }
        }
        return value;
    }
    
}
