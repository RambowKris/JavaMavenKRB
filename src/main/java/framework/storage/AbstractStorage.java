/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.storage;

import framework.support.AbstractManager;

/**
 *
 * @author krb
 */
public abstract class AbstractStorage extends AbstractManager {

    public abstract void store(String key, byte[] data);

    public abstract Boolean exist(String key);

    public abstract byte[] find(String key);

    public abstract void remove(String key);

}
