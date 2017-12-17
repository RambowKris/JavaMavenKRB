/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.network;

import com.google.common.io.ByteStreams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author krb
 */
public class ClientConnector {

    private String url;
    private HttpURLConnection connection;
    private String requestType = "GET";
    private byte[] content = null;
    private Map<String, String> headers = null;
    private Map<String, List<String>> responseHeaders = null;

    private byte[] response = null;
    private int responseCode = -1;

    public ClientConnector(String url) {
        this.url = url;
    }

    public String getURL() {
        return this.url;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getResponse() {
        return this.response;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this.responseHeaders;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void run() {
        try {
            this.doRequest();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void doRequest() throws MalformedURLException, IOException {
//    public void doRequest(String requestType, Map<String, Object> parameters) {

        URL url = new URL(this.url);

//        System.out.println(this.headers);
//        System.exit(0);
        this.connection = (HttpURLConnection) url.openConnection();

        this.connection.setRequestMethod(this.requestType);
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            this.connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        if (!this.requestType.equalsIgnoreCase("get")) {
            this.connection.setDoOutput(true);

//            String content = this.mapToJsonString(parameters);
            OutputStream os = this.connection.getOutputStream();
            os.write(this.content);
            os.flush();
        }

        this.connection.setInstanceFollowRedirects(true);

        this.executeRequest();

    }

    private void executeRequest() throws IOException {
        this.responseCode = connection.getResponseCode();

        this.responseHeaders = this.connection.getHeaderFields();

//        BufferedReader br = new BufferedReader(new InputStreamReader(
//                (this.connection.getInputStream())));

        this.response = ByteStreams.toByteArray(this.connection.getInputStream());        
//        this.response = null;        

//        String responseString = "";
//        System.out.println("Output from Server .... \n");
//        String output;
//        while ((output = br.readLine()) != null) {
//            responseString += output + "\n";
////            System.out.println(output);
//        }
        
//        this.response = responseString.getBytes();
        this.connection.disconnect();
    }

    public String toString() {
        String fileName = "";

        String renderString = "";
        renderString += this.requestType + " /" + fileName + " HTTP/1.1\n";

        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            renderString += entry.getKey() + ": " + entry.getValue() + "\n";
        }

        return renderString;
    }
}
