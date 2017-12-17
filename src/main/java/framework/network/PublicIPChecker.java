/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author krb
 */
public class PublicIPChecker {

    public static String getPublicIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            return in.readLine().trim(); //you get the IP as a String
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }

//        String ip = in.readLine(); //you get the IP as a String
//        System.out.println(ip);
    }
    
    public static void main(String[] args) {
        System.out.println(PublicIPChecker.getPublicIP());
    }
}
