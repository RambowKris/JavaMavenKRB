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
@Table(name = "INTEGRITY_DATA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IntegrityData.findAll", query = "SELECT i FROM IntegrityData i")
    , @NamedQuery(name = "IntegrityData.findByKeyName", query = "SELECT i FROM IntegrityData i WHERE i.keyName = :keyName")
    , @NamedQuery(name = "IntegrityData.findByChecksum", query = "SELECT i FROM IntegrityData i WHERE i.checksum = :checksum")})
public class IntegrityData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "KEY_NAME")
    @Id
    private String keyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "CHECKSUM")
    private String checksum;

    public IntegrityData() {
    }

    public IntegrityData(String keyName) {
        this.keyName = keyName;
    }

    public IntegrityData(String keyName, String checksum) {
        this.keyName = keyName;
        this.checksum = checksum;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (keyName != null ? keyName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IntegrityData)) {
            return false;
        }
        IntegrityData other = (IntegrityData) object;
        if ((this.keyName == null && other.keyName != null) || (this.keyName != null && !this.keyName.equals(other.keyName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.samples.model.IntegrityData[ key_name=" + keyName + " ]";
    }

}
