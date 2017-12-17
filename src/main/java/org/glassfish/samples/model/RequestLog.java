/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author krb
 */
@Entity
@Table(name = "REQUEST_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestLog.findAll", query = "SELECT r FROM RequestLog r")
    , @NamedQuery(name = "RequestLog.findByTimeIncoming", query = "SELECT r FROM RequestLog r WHERE r.timeIncoming = :timeIncoming")
    , @NamedQuery(name = "RequestLog.findByRequestContent", query = "SELECT r FROM RequestLog r WHERE r.requestContent = :requestContent")
    , @NamedQuery(name = "RequestLog.findByTimeOutgoing", query = "SELECT r FROM RequestLog r WHERE r.timeOutgoing = :timeOutgoing")})
public class RequestLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIME_INCOMING")
    private Integer timeIncoming;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1023)
    @Column(name = "REQUEST_CONTENT")
    private String requestContent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TIME_OUTGOING")
    private Integer timeOutgoing;

    public RequestLog() {
    }

    public RequestLog(Integer timeIncoming) {
        this.timeIncoming = timeIncoming;
    }

    public RequestLog(Integer timeIncoming, String requestContent) {
        this.timeIncoming = timeIncoming;
        this.requestContent = requestContent;
    }

    public RequestLog(Integer timeIncoming, String requestContent, Integer timeOutgoing) {
        this.timeIncoming = timeIncoming;
        this.requestContent = requestContent;
        this.timeOutgoing = timeOutgoing;
    }

    public Integer getTimeIncoming() {
        return timeIncoming;
    }

    public void setTimeIncoming(Integer timeIncoming) {
        this.timeIncoming = timeIncoming;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public Integer getTimeOutgoing() {
        return timeOutgoing;
    }

    public void setTimeOutgoing(Integer timeOutgoing) {
        this.timeOutgoing = timeOutgoing;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeIncoming != null ? timeIncoming.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestLog)) {
            return false;
        }
        RequestLog other = (RequestLog) object;
        if ((this.timeIncoming == null && other.timeIncoming != null) || (this.timeIncoming != null && !this.timeIncoming.equals(other.timeIncoming))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.RequestLog[ timeIncoming=" + timeIncoming + " ]";
    }
    
}
