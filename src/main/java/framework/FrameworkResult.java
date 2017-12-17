/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author krb
 */
public class FrameworkResult {
    
    private String message;
    private Boolean success;

    public void failure(){
        this.success = false;
    }
    
    public void success(){
        this.success = true;
    }
    
    public Boolean hasSuccess(){
        return this.success;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }    
}
