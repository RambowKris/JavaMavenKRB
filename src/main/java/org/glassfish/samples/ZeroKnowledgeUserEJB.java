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
import org.glassfish.samples.model.ZeroKnowledgeUser;

/**
 *
 * @author krb
 */
@Stateful
@Named
public class ZeroKnowledgeUserEJB {

    @PersistenceContext
    EntityManager em;

    public void create(String userName, String hashValue, String g0, String r2) {
        ZeroKnowledgeUser zku = new ZeroKnowledgeUser(userName, hashValue, g0, r2);
        em.persist(zku);
    }

    public List getList() {
        return this.em.createNamedQuery("ZeroKnowledgeUser.findAll").getResultList();
    }

    public ZeroKnowledgeUser getZeroKnowledgeUserByUserName(String userName) {
        List<ZeroKnowledgeUser> result = this.getList();
        ZeroKnowledgeUser foundZKU = null;
        for (ZeroKnowledgeUser zku : result) {
            if (zku.getUserName().equalsIgnoreCase(userName)) {
                foundZKU = zku;
                break;
            }
        }
        return foundZKU;
    }

}
