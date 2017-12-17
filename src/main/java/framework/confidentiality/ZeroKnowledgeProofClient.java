/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.confidentiality;

import framework.network.ClientConnector;
import framework.support.Crypto;
import framework.support.DataConverter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author krb
 */
public class ZeroKnowledgeProofClient {

    private String username;
    private String hash;
    private BigInteger g0;
    private BigInteger r2;

    private BigInteger a;
    private String sessionId;

    private String authenticationToken;

    public void flow() {
        // discrete log problem
        try {
            String password = "";

            // Registering
            // client creates and sends to server
            BigInteger g0 = BigInteger.probablePrime(67, new SecureRandom());
            BigInteger r2 = new BigInteger(67, new SecureRandom());

            // client creates and sends to server
            String newHash = Crypto.calculateHashToHexString("SHA1", password);
            String username = "";

            BigInteger xNew = new BigInteger(newHash, 16);
            BigInteger yNew = g0.modPow(xNew, r2);
            // server storing username + hash/y + g0 + r2

            // Authenticating
            // value of a set by server
            BigInteger a = new BigInteger(12, new SecureRandom());

            // value of rx and t1 created by client
            BigInteger rx = new BigInteger(12, new SecureRandom());
            BigInteger t1 = g0.modPow(rx, r2);

            String newStringClient = ""; // string of y concat t1 concat a
            String cClient = Crypto.calculateHashToHexString("SHA1", newStringClient); // send to server

            // server
            // receives c(cClient) and wx
            BigInteger cBigInteger = new BigInteger(cClient, 16);
            BigInteger wxBigInteger = new BigInteger("wx", 16);

            BigInteger ta = yNew.modPow(cBigInteger, r2);
            BigInteger tb = g0.modPow(wxBigInteger, r2);
            BigInteger tc = ta.multiply(tb);
            BigInteger ts = tc.mod(r2);

            String newStringServer = ""; // string of y concat ts concat a
            String cServer = Crypto.calculateHashToHexString("SHA1", newStringServer);

            // authenticated if cClient equals cServer
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void generateClientUser() {
        String username = "Ferdinand";
        System.out.println("User Name: " + username);
        String password = "funnyguy";

        // client creates and sends to server
        String newHash = Crypto.calculateHashToHexString("SHA1", password);
        System.out.println("Hash: " + newHash);

        BigInteger g0 = BigInteger.probablePrime(67, new SecureRandom());
        System.out.println("g0: " + g0);
        BigInteger r2 = new BigInteger(67, new SecureRandom());
        System.out.println("r2: " + r2);
    }

    public void setUser(String username, String hash, String g0, String r2) {
        this.username = username;
        this.hash = hash;
        this.g0 = new BigInteger(g0);
        this.r2 = new BigInteger(r2);
    }

    public void clientStep1() {
        String keyName = this.username;
        String content = "";

        String url = "https://localhost/JavaMavenKRB/api/zkp/" + DataConverter.uriEncode(keyName, true);
        ClientConnector cc = new ClientConnector(url);
        cc.setRequestType("GET");

        Map<String, String> headers = new TreeMap<>();
        headers.put("Host", "localhost");
        cc.setHeaders(headers);

        cc.setContent(content.getBytes());

        try {
            cc.doRequest();

            System.out.println("Response Code: " + cc.getResponseCode());
            System.out.println("Response: " + cc.getResponse());

            if (199 < cc.getResponseCode() && cc.getResponseCode() < 300) {
                Map<String, List<String>> responseHeaders = cc.getResponseHeaders();
                List<String> cookieStrings = responseHeaders.get("Set-Cookie");
                for (String cookie : cookieStrings) {
                    String[] cookieValues = cookie.split("=");
                    if (cookieValues[0].equalsIgnoreCase("zkp")) {
                        this.sessionId = cookieValues[1];
                        break;
                    }
                }

                this.a = new BigInteger(cc.getResponse());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void clientStep2() {
        // value of rx and t1 created by client
        BigInteger rx = new BigInteger(12, new SecureRandom());
        BigInteger t1 = this.g0.modPow(rx, this.r2);

        String newStringClient = this.hash + t1 + this.a; // string of y concat t1 concat a
        String cClient = Crypto.calculateHashToHexString("SHA1", newStringClient); // send to server

        String keyName = this.username;
        String content = cClient;

        String queries = "wx=" + rx;
        String queryString = DataConverter.uriEncode(keyName, true) + "?" + queries;

        String url = "https://localhost/JavaMavenKRB/api/zkp/" + queryString;
        ClientConnector cc = new ClientConnector(url);
        cc.setRequestType("PUT");

        Map<String, String> headers = new TreeMap<>();
        headers.put("Host", "localhost");
        headers.put("cookie", "zkp=" + this.sessionId);
        cc.setHeaders(headers);

        cc.setContent(content.getBytes());

        try {
            cc.doRequest();

            System.out.println("Response Code: " + cc.getResponseCode());
            System.out.println("Response: " + cc.getResponse());

            if (199 < cc.getResponseCode() && cc.getResponseCode() < 300) {
                this.authenticationToken = new String(cc.getResponse());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ZeroKnowledgeProofClient zkpc = new ZeroKnowledgeProofClient();
//        zkpc.generateClientUser();
        zkpc.setUser("Ferdinand", "e5bb1bae5f577e9cd84f36eb6620ce22ef803877", "107358201586725427979", "40361334593289562479");
        zkpc.clientStep1();
        zkpc.clientStep2();
    }
}
