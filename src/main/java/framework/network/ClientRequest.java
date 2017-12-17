/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author krb
 */
public class ClientRequest {

    private String command = null;
    private String service = null;
    private HttpServletRequest httpRequest = null;

    private String keyName = null;
    private byte[] data = null;
    private String authentication = null;

    public ClientRequest(HttpServletRequest httpRequest, String command, byte[] data) {
        this.httpRequest = httpRequest;
        String[] splittedPathInfo = this.httpRequest.getPathInfo().split("/");
        this.service = splittedPathInfo[1];
        this.keyName = splittedPathInfo[2];
        for (int i = 3; i < splittedPathInfo.length; i++) {
            this.keyName += "/" + splittedPathInfo[i];
        }

        this.command = command;
        this.data = data;

        this.init();
    }

    private void init() {
//        StringBuffer jb = new StringBuffer();
//        String line = null;
//        try {
////            this.data = IOUtils.toString(this.httpRequest.getReader());
//            BufferedReader reader = this.httpRequest.getReader();
//            while ((line = reader.readLine()) != null) {
//                jb.append(line);
//            }
//            this.data = jb.toString();
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }

        for (String headerName : Collections.list(this.httpRequest.getHeaderNames())) {
            if (headerName.equalsIgnoreCase("authentication")) {
                this.authentication = this.httpRequest.getHeader("authentication");
            }
        }

    }

    public String getAuthentication() {
        return this.authentication;
    }

    public String getService() {
        return this.service;
    }

    public String getCommand() {
        return this.command;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
