/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.confidentiality;

import java.util.Map;

/**
 *
 * @author krb
 */
public class TenantConfiguration {

    private String splitFunction;
    private String[] storageClouds;

    public TenantConfiguration() {

    }

    public TenantConfiguration(String splitFunction, String[] storageClouds) {
        this.splitFunction = splitFunction;
        this.storageClouds = storageClouds;
    }

    public String[] getStorageClouds() {
        return this.storageClouds;
    }

    public String getSplitFunction() {
        return this.splitFunction;
    }
}
