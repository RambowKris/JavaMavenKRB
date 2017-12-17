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
@Table(name = "ZERO_KNOWLEDGE_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZeroKnowledgeUser.findAll", query = "SELECT z FROM ZeroKnowledgeUser z")
    , @NamedQuery(name = "ZeroKnowledgeUser.findByUserName", query = "SELECT z FROM ZeroKnowledgeUser z WHERE z.userName = :userName")
    , @NamedQuery(name = "ZeroKnowledgeUser.findByHashValue", query = "SELECT z FROM ZeroKnowledgeUser z WHERE z.hashValue = :hashValue")
    , @NamedQuery(name = "ZeroKnowledgeUser.findByG0", query = "SELECT z FROM ZeroKnowledgeUser z WHERE z.g0 = :g0")
    , @NamedQuery(name = "ZeroKnowledgeUser.findByR2", query = "SELECT z FROM ZeroKnowledgeUser z WHERE z.r2 = :r2")})
public class ZeroKnowledgeUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USER_NAME")
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "HASH_VALUE")
    private String hashValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "G_0")
    private String g0;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "R_2")
    private String r2;

    public ZeroKnowledgeUser() {
    }

    public ZeroKnowledgeUser(String userName) {
        this.userName = userName;
    }

    public ZeroKnowledgeUser(String userName, String hashValue, String g0, String r2) {
        this.userName = userName;
        this.hashValue = hashValue;
        this.g0 = g0;
        this.r2 = r2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getG0() {
        return g0;
    }

    public void setG0(String g0) {
        this.g0 = g0;
    }

    public String getR2() {
        return r2;
    }

    public void setR2(String r2) {
        this.r2 = r2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userName != null ? userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZeroKnowledgeUser)) {
            return false;
        }
        ZeroKnowledgeUser other = (ZeroKnowledgeUser) object;
        if ((this.userName == null && other.userName != null) || (this.userName != null && !this.userName.equals(other.userName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.ZeroKnowledgeUser[ userName=" + userName + " ]";
    }
    
}
