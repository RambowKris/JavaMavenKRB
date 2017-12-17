/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.confidentiality;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import org.glassfish.samples.CloudUserEJB;
import org.glassfish.samples.IntegrityDataEJB;
import org.glassfish.samples.model.CloudUser;

/**
 *
 * @author krb
 */
public class AuthenticationManager {

//    @EJB
    private CloudUserEJB cloudUserBean;

    private CloudUser cloudUser;

    public AuthenticationManager(CloudUserEJB cloudUserBean) {
        this.cloudUserBean = cloudUserBean;
    }

    public Boolean authenticate(String authenticationData) {
        if(authenticationData==null){
            return false;
        }
        
        String userName = authenticationData.split("=")[0];
        
        this.cloudUser = this.cloudUserBean.getUserByUserName(userName);

        if (this.cloudUser == null) {
            return false;
        }

        String token = authenticationData.split("=")[1];
        return this.cloudUserBean.verifyUserPassword(userName, token);
    }

    public String getTenantName(){
        return this.cloudUser.getTenantName();
    }
}
