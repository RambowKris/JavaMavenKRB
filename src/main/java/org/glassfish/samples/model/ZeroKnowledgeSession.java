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
@Table(name = "ZERO_KNOWLEDGE_SESSION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZeroKnowledgeSession.findAll", query = "SELECT z FROM ZeroKnowledgeSession z")
    , @NamedQuery(name = "ZeroKnowledgeSession.findById", query = "SELECT z FROM ZeroKnowledgeSession z WHERE z.id = :id")
    , @NamedQuery(name = "ZeroKnowledgeSession.findByUserName", query = "SELECT z FROM ZeroKnowledgeSession z WHERE z.userName = :userName")
    , @NamedQuery(name = "ZeroKnowledgeSession.findByA", query = "SELECT z FROM ZeroKnowledgeSession z WHERE z.a = :a")
    , @NamedQuery(name = "ZeroKnowledgeSession.findByTime", query = "SELECT z FROM ZeroKnowledgeSession z WHERE z.time = :time")})
public class ZeroKnowledgeSession implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USER_NAME")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "A")
    private String a;
    @Size(max = 255)
    @Column(name = "TIME")
    private String time;

    public ZeroKnowledgeSession() {
    }

    public ZeroKnowledgeSession(String id) {
        this.id = id;
    }

    public ZeroKnowledgeSession(String id, String userName, String a, String time) {
        this.id = id;
        this.userName = userName;
        this.a = a;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZeroKnowledgeSession)) {
            return false;
        }
        ZeroKnowledgeSession other = (ZeroKnowledgeSession) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.ZeroKnowledgeSession[ id=" + id + " ]";
    }
    
}
