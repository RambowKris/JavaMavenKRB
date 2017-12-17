/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.RequestLog;

/**
 *
 * @author krb
 */
@Stateful
@Named
public class RequestLogEJB {

    @PersistenceContext
    EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void create(Integer timeIncoming, String requestContent, Integer timeOutgoing) {
        RequestLog id = new RequestLog(timeIncoming, requestContent, timeOutgoing);
        em.persist(id);
    }
}
