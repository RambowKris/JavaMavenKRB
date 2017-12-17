/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.ZeroKnowledgeSession;

/**
 *
 * @author krb
 */
@Stateful
public class ZeroKnowledgeSessionEJB {
    
    @PersistenceContext
    EntityManager em;
    
    public void create(String id, String userName, String a, String time) {
        ZeroKnowledgeSession zks = new ZeroKnowledgeSession(id, userName, a, time);
        em.persist(zks);
    }
    
    public List getList() {
        return this.em.createNamedQuery("ZeroKnowledgeSession.findAll").getResultList();
    }
    
    public ZeroKnowledgeSession getZeroKnowledgeSessionByUserNameAndId(String userName, String id) {
        List<ZeroKnowledgeSession> result = this.getList();
        ZeroKnowledgeSession foundZKS = null;
        for (ZeroKnowledgeSession zks : result) {
            if (zks.getId().equalsIgnoreCase(id) && zks.getUserName().equalsIgnoreCase(userName)) {
                foundZKS = zks;
                break;
            }
        }
        return foundZKS;
    }
    
    public void deleteZeroKnowledgeSessionById(String id) {
        List<ZeroKnowledgeSession> result = this.getList();
        ZeroKnowledgeSession foundZKS = null;
        for (ZeroKnowledgeSession zks : result) {
            if (zks.getId().equalsIgnoreCase(id)) {
                em.remove(zks);
                break;
            }
        }
    }
    
}
