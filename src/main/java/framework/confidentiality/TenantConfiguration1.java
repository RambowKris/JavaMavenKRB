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
public class TenantConfiguration1 {

    private String hostName;
    public String portNumber;
    public int age;
    private Map<String, String[]> clouds;

    public TenantConfiguration1() {

    }

    public TenantConfiguration1(String hostname, int portNumber, int age, Map<String, String[]> clouds) {
        this.hostName = hostname;
        this.portNumber = "" + portNumber;
        this.age = age;
        this.clouds = clouds;
    }

    public Map<String, String[]> getClouds() {
        return this.clouds;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPortNumber() {
        return Integer.parseInt(this.portNumber);
    }

    public int getAge() {
        return this.age;
    }
}
