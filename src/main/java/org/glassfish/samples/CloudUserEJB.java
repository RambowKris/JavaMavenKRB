/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples;

import framework.support.Crypto;
import framework.support.DataConverter;
import java.security.SecureRandom;
import java.util.List;
import javax.ejb.Stateful;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.glassfish.samples.model.CloudUser;

/**
 *
 * @author krb
 */
@Stateful
@Named
public class CloudUserEJB {

    @PersistenceContext
    EntityManager em;

    public void create(String userName, String tenant, String password) {
        CloudUser cu = new CloudUser(userName, tenant, password, "" + System.currentTimeMillis());
        em.persist(cu);
    }

    public List getList() {
        return this.em.createNamedQuery("CloudUser.findAll").getResultList();
    }

    public CloudUser getUserByUserName(String userName) {
        return this.em.find(CloudUser.class, userName);
    }

    public Boolean verifyUserPassword(String userName, String password) {
        CloudUser cu = getUserByUserName(userName);

        if (cu == null) {
            return false;
        }

//        int currentTime = (int) System.currentTimeMillis();
//        if (Integer.parseInt(cu.getExpirationTime()) < currentTime) {
//            return false;
//        }

        String passwordString = cu.getPassword();
        String[] passwordStrings = passwordString.split("\\$");
        int keyLength = Integer.parseInt(passwordStrings[0]);
        int iterations = Integer.parseInt(passwordStrings[1]);
        String salt = passwordStrings[2].substring(0, 128);
        String hash = passwordStrings[2].substring(128, 256);

        byte[] newHash = Crypto.hashPassword(password.toCharArray(), DataConverter.hex2ByteArray(salt), iterations, keyLength);
        String newHashString = DataConverter.byteArray2Hex(newHash);

        return hash.equalsIgnoreCase(newHashString);
    }

    public void updateUserPassword(String userName, String password) {
        CloudUser cu = getUserByUserName(userName);

        if (cu != null) {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[64];
            random.nextBytes(salt);

            int iterations = 200000;
            int keyLength = 512;

            byte[] hash = Crypto.hashPassword(password.toCharArray(), salt, iterations, keyLength);

            String saltString = DataConverter.byteArray2Hex(salt);
            String hashString = DataConverter.byteArray2Hex(hash);

//            System.out.println(password);
//            System.out.println(saltString);
//            System.out.println(iterations);
//            System.out.println(keyLength);
//            System.out.println(hashString);
            String passwordToSave = "" + keyLength + "$" + iterations + "$" + saltString + hashString;

            cu.setPassword(passwordToSave);

            int currentTime = (int) System.currentTimeMillis();
            int expirationTime = currentTime + (24 * 60 * 60 * 1000);
            cu.setExpirationTime("" + expirationTime);

            em.merge(cu);
        }
    }

}
