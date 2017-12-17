/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.confidentiality;

import java.util.List;
import java.util.Map;

/**
 *
 * @author krb
 */
public class SystemConfiguration {

    private Map<String,String> cloudConfigurations;

    public SystemConfiguration() {

    }

    public String getCloudConfiguration(String cloudName){
        return this.cloudConfigurations.get(cloudName);
    }

}
