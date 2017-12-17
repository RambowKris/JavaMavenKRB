/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.network;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import framework.support.Crypto;
import framework.support.DataConverter;
import java.util.List;

/**
 *
 * @author krb
 */
public class AWSrequestor {

    private ClientConnector clientConnector;

    private String canonicalRequest;
    private String stringToSign;
    private byte[] signingKey;
    private byte[] signature;
    private String signatureString;
    private String authorizationString;

    private String credentialScope;

    private Date date = null;
    private String dateString = null;
    private String requestDateString;
    private String region;
    private String service;
    private String algorithm;
    private String terminationString;

    private String awsAccessKey;
    private String awsSecretAccessKey;

    private String hashedPayloadString = null;

    private String headerString;
    private String headerTitles;
    private String[] additionalHeaderTitles;

    private SimpleDateFormat headerDateFormat;

    public AWSrequestor(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;

        this.headerDateFormat = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        this.headerDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public void prepare(String region, String service, String algorithm, String terminationString, String awsAccessKey, String awsSecretAccessKey) {
        this.region = region;
        this.service = service;
        this.algorithm = algorithm;
        this.terminationString = terminationString;
        this.awsAccessKey = awsAccessKey;
        this.awsSecretAccessKey = awsSecretAccessKey;

        this.extractDate();

        this.addMissingHeaders();

        this.createCanonicalRequest();

        this.createStringToSign();

        this.createSigningKey();

        this.createSignature();

        this.createAuthorizationHeader();

        Map<String, String> headers = this.clientConnector.getHeaders();
        headers.put("Authorization", this.authorizationString);
        this.clientConnector.setHeaders(headers);

    }

    public void doRequest() {
        try {
            this.clientConnector.doRequest();

//            System.out.println("Response Code: " + this.clientConnector.getResponseCode());
//            System.out.println("Response Headers: ");
            String headerString = " ";
        for (Map.Entry<String, List<String>> entry : this.clientConnector.getResponseHeaders().entrySet()) {
            headerString = entry.getKey() + ": ";
            for(String value : entry.getValue()){
                headerString += value+"; ";
            }
            headerString += "\n";
        }
//            System.out.println(headerString);
//            System.out.println("Response: " + this.clientConnector.getResponse());
        } catch (Exception e) {
            System.err.println("AWS could not do request: \n" + e.getMessage());
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private void createDateStrings() {
        String awsDateFormat = "yyyyMMdd";
        SimpleDateFormat awsDateFormatter = new SimpleDateFormat(awsDateFormat);
        awsDateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.dateString = awsDateFormatter.format(this.date);

        SimpleDateFormat requestDateFormatted = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        requestDateFormatted.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.requestDateString = requestDateFormatted.format(this.date);
    }

    public void addAdditionalHeaderTitles(String[] headerTitles) {
        this.additionalHeaderTitles = headerTitles;
    }

    private void extractDate() {

        if (this.date != null) {
            this.createDateStrings();

            return;
        }

        this.date = new Date();

        Map<String, String> headers = this.clientConnector.getHeaders();
        String dateHeaderTitle = "date";
        if (headers.containsKey(dateHeaderTitle)) {
            try {
                this.date = this.headerDateFormat.parse(headers.get(dateHeaderTitle));
            } catch (Exception e) {
                System.err.println("Could not parse date\n" + e.getMessage());
            }
        }

        this.createDateStrings();
    }

    private void addMissingHeaders() {
        Map<String, String> headers = this.clientConnector.getHeaders();
        for (String headerTitle : this.additionalHeaderTitles) {
            headers.put(headerTitle, this.createHeaderValue(headerTitle));
        }

        this.clientConnector.setHeaders(headers);

        this.headerString = this.headerTitles = "";
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.headerString += entry.getKey().toLowerCase() + ":" + entry.getValue() + "\n";
            this.headerTitles += entry.getKey().toLowerCase() + ";";
        }
        this.headerTitles = this.headerTitles.substring(0, this.headerTitles.length() - 1);
    }

    private String createHeaderValue(String headerTitle) {
        String value = null;

        switch (headerTitle.toLowerCase()) {
            case "date":
                value = this.headerDateFormat.format(this.date);
                break;
            case "host":
                value = this.clientConnector.getURL().split("/")[2];
                break;
            case "content-length":
                value = "" + this.clientConnector.getContent().length;
                break;
            case "x-amz-content-sha256":
                try {
                    byte[] hashedPayload = Crypto.sha256Hash(new String(this.clientConnector.getContent()));
                    this.hashedPayloadString = DatatypeConverter.printHexBinary(hashedPayload).toLowerCase();

                    value = this.hashedPayloadString;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                break;
            case "x-amz-date":
                value = this.requestDateString;
                break;
            default:
                break;
        }

        return value;
    }

    private void createSigningKey() {
        this.signingKey = null;
        try {
            byte[] dateKey = Crypto.hmacSHA256(("AWS4" + this.awsSecretAccessKey).getBytes("UTF8"), this.dateString);
            byte[] dateRegionKey = Crypto.hmacSHA256(dateKey, this.region);
            byte[] dateRegionServiceKey = Crypto.hmacSHA256(dateRegionKey, this.service);
            this.signingKey = Crypto.hmacSHA256(dateRegionServiceKey, this.terminationString);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getSignature() {
        return this.signatureString;
    }

    private void createSignature() {
        this.signature = Crypto.hmacSHA256(this.signingKey, this.stringToSign);
        this.signatureString = DatatypeConverter.printHexBinary(this.signature).toLowerCase();
    }

    public String getAuthorizationHeader() {
        return this.authorizationString;
    }

    private void createAuthorizationHeader() {
        this.authorizationString = this.algorithm + " Credential=" + this.awsAccessKey + "/" + this.credentialScope + ",SignedHeaders=" + this.headerTitles + ",Signature=" + this.signatureString;
    }

    public String getCanonicalRequest() {
        return this.canonicalRequest;
    }

    private void createCanonicalRequest() {
        String canonicalQueryString = "";
        String[] domains = this.clientConnector.getURL().split("(?=/)",4);
        String canonicalURI = domains[domains.length - 1];
        String signedHeaders = this.headerTitles;

        if (this.hashedPayloadString == null) {
            byte[] hashedPayload = Crypto.sha256Hash(new String(this.clientConnector.getContent()));
            this.hashedPayloadString = DatatypeConverter.printHexBinary(hashedPayload).toLowerCase();
        }

        this.canonicalRequest
                = this.clientConnector.getRequestType() + "\n"
                + canonicalURI + "\n"
                + canonicalQueryString + "\n"
                + this.headerString + "\n"
                + signedHeaders + "\n"
                + this.hashedPayloadString;
    }

    private void createCredentialScope() {
        this.credentialScope = this.dateString + "/" + this.region + "/" + this.service + "/" + this.terminationString;
    }

    public String getStringToSign() {
        return this.stringToSign;
    }

    private void createStringToSign() {
        this.createCredentialScope();

        byte[] hashedRequest = Crypto.sha256Hash(canonicalRequest);
        String hashedRequestString = DatatypeConverter.printHexBinary(hashedRequest).toLowerCase();

        this.stringToSign = this.algorithm + "\n"
                + this.requestDateString + "\n"
                + this.credentialScope + "\n"
                + hashedRequestString;
    }
    
    public String toString(){
        return this.clientConnector.toString();
    }
}
