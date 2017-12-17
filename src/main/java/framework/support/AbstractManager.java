/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.support;

/**
 *
 * @author krb
 */
public class AbstractManager {
        protected Boolean success;

    public void success() {
        this.success = true;
    }

    public void failure() {
        this.success = false;
    }

    public Boolean hasSuccess() {
        return this.success;
    }

}
