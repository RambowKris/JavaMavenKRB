/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.storage;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.ByteStreams;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import framework.network.AWSrequestor;
import framework.network.ClientConnector;
import framework.support.DataConverter;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author krb
 */
public class AwsStorage extends AbstractStorage {

    private String awsSecretAccessKey;
    private String awsAccessKey;
    private String region;

    private String bucketName;

    public AwsStorage(String cloudConfiguration, String bucketName) {
        Map<String, String> cloudConfigurationMappings = (Map<String, String>) DataConverter.getObjectFromJson(cloudConfiguration, Map.class);        
        this.awsAccessKey = cloudConfigurationMappings.get("awsAccessKey");
        this.awsSecretAccessKey = cloudConfigurationMappings.get("awsSecretAccessKey");
        this.region = cloudConfigurationMappings.get("region");                
        this.bucketName = bucketName;
    }
    
    public AwsStorage(String awsAccessKey, String awsSecretAccessKey, String region, String bucketName) {
        this.awsAccessKey = awsAccessKey;
        this.awsSecretAccessKey = awsSecretAccessKey;
        this.region = region;
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public void store(String key, byte[] data) {
        String serviceString = "s3";
        String regionString = this.region;
        String url = "http://" + this.bucketName + "." + serviceString + ".amazonaws.com/" + DataConverter.uriEncode(key, false);

        ClientConnector cc = new ClientConnector(url);
        String httpMethod = "PUT";
        cc.setRequestType(httpMethod);
        cc.setContent(data);

        Map<String, String> headers = new TreeMap<>();
//        headers.put("x-amz-storage-class", "REDUCED_REDUNDANCY");
//        headers.put("x-amz-acl", "bucket-owner-full-control");
//        headers.put("Content-Type", "text/plain");
//        headers.put("x-amz-storage-class", "");
//        headers.put("x-amz-server-side-encryption", "");
//        headers.put("x-amz-meta-", "");
//        headers.put("x-amz-website-redirect-location", "");
//        headers.put("Range", "bytes=0-9");
        cc.setHeaders(headers);

        AWSrequestor awsr = new AWSrequestor(cc);
        awsr.setDate(new Date());

        awsr.addAdditionalHeaderTitles(new String[]{"Date", "Host", "x-amz-content-sha256", "x-amz-date"});

        String terminationString = "aws4_request";
        String algorithm = "AWS4-HMAC-SHA256";
        awsr.prepare(regionString, serviceString, algorithm, terminationString, this.awsAccessKey, this.awsSecretAccessKey);

        awsr.doRequest();
        int responseCode = cc.getResponseCode();
        this.success = 199 < responseCode && responseCode < 300;
    }

    @Override
    public Boolean exist(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] find(String key) {
        AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(this.awsAccessKey, this.awsSecretAccessKey));
        try {
            S3Object object = s3Client.getObject(
                    new GetObjectRequest(this.bucketName, key));
            InputStream objectData = object.getObjectContent();
            byte[] result = ByteStreams.toByteArray(objectData);
            objectData.close();

            this.success();
            return result;
        } catch (AmazonServiceException ase) {
            this.failure();
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            this.failure();
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            this.failure();
            System.out.println("Caught an IOException.");
            System.out.println("Error Message: " + ioe.getMessage());
        }
        return null;

//        String serviceString = "s3";
//        String regionString = "eu-central-1";
////        String bucketName = "krbfirstbucket";
//        String url = "http://" + this.bucketName + "." + serviceString + ".amazonaws.com/" + DataConverter.uriEncode(key, false);
//
//        ClientConnector cc = new ClientConnector(url);
//        String httpMethod = "GET";
//        cc.setRequestType(httpMethod);
//
//        Map<String, String> headers = new TreeMap<>();
////        headers.put("x-amz-storage-class", "REDUCED_REDUNDANCY");
////        headers.put("Content-Type", "text/plain");
////        headers.put("x-amz-storage-class", "");
////        headers.put("x-amz-server-side-encryption", "");
////        headers.put("x-amz-meta-", "");
////        headers.put("x-amz-website-redirect-location", "");
////        headers.put("Range", "bytes=0-9");
//        cc.setHeaders(headers);
//
//        AWSrequestor awsr = new AWSrequestor(cc);
//        awsr.setDate(new Date());
//
//        awsr.addAdditionalHeaderTitles(new String[]{"Date", "Host", "x-amz-content-sha256", "x-amz-date"});
//
//        String terminationString = "aws4_request";
//        String algorithm = "AWS4-HMAC-SHA256";
//        awsr.prepare(regionString, serviceString, algorithm, terminationString, this.awsAccessKey, this.awsSecretAccessKey);
//
//        awsr.doRequest();
//        int responseCode = cc.getResponseCode();
//
//        this.success = 199 < responseCode && responseCode < 300;
//        return this.success ? cc.getResponse() : null;
    }

    @Override
    public void remove(String key) {
        AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(this.awsAccessKey, this.awsSecretAccessKey));
        try {
            s3Client.deleteObject(new DeleteObjectRequest(this.bucketName, key));
            this.success();
        } catch (AmazonServiceException ase) {
            this.failure();
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            this.failure();
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        }
//        this.success = 199 < responseCode && responseCode < 300;
    }
//
//    @Override
//    public void remove(String key) {
//        String serviceString = "s3";
//        String regionString = "eu-central-1";
//        String url = "http://" + this.bucketName + "." + serviceString + ".amazonaws.com/" + DataConverter.uriEncode(key, false);
//
//        ClientConnector cc = new ClientConnector(url);
//        String httpMethod = "DELETE";
//        cc.setRequestType(httpMethod);
//
//        Map<String, String> headers = new TreeMap<>();
//        headers.put("Content-Type", "text/plain");
//        headers.put("Content-Length", "0");
//
//        cc.setHeaders(headers);
//
//        AWSrequestor awsr = new AWSrequestor(cc);
//        awsr.setDate(new Date());
//
//        awsr.addAdditionalHeaderTitles(new String[]{"Date", "Host"});
//
//        String terminationString = "aws4_request";
//        String algorithm = "AWS4-HMAC-SHA256";
//        awsr.prepare(regionString, serviceString, algorithm, terminationString, this.awsAccessKey, this.awsSecretAccessKey);
//
//        awsr.doRequest();
//        int responseCode = cc.getResponseCode();
//
//        this.success = 199 < responseCode && responseCode < 300;
//    }

}
