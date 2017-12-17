/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.storage;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageClass;
import com.google.cloud.storage.StorageOptions;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.Map;
import framework.support.DataConverter;
import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author krb
 */
public class GcStorage extends AbstractStorage {

    private String projectId;
    private String apiKey;
    private String bucketName;
    private String filePath;
    private String region;
//    private String filePath = "C:\\Users\\krb.SECOMEA\\Documents\\Private\\DTU\\Master-Thesis\\projectStorageTest\\MyAWSApplication-42add0b402bb.json";

    private ServiceAccountCredentials serviceAccountCredentials;

        public GcStorage(String cloudConfiguration, String bucketName) {
        try {
            InputStream stream = new ByteArrayInputStream(cloudConfiguration.getBytes(StandardCharsets.UTF_8.name()));
            this.serviceAccountCredentials = ServiceAccountCredentials.fromStream(stream);
//            stream.close();
//            this.serviceAccountCredentials = ServiceAccountCredentials.fromStream(new FileInputStream(this.filePath));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.getCause());
        }
        this.bucketName = bucketName;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Storage openStorage() {
        Storage storage = null;
        try {
//            InputStream stream = new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8.name()));
//            this.serviceAccountCredentials = ServiceAccountCredentials.fromStream(stream);
//            this.serviceAccountCredentials = ServiceAccountCredentials.fromStream(new FileInputStream(this.filePath));            

            FileInputStream fileInputStream = new FileInputStream(this.filePath);
            ServiceAccountCredentials serviceAccountCredentials = ServiceAccountCredentials.fromStream(fileInputStream);
            storage = StorageOptions.newBuilder()
                    .setCredentials(serviceAccountCredentials)
                    .build()
                    .getService();
//            fileInputStream.close();
        } catch (Exception e) {

        }
        return storage;
    }

    @Override
    public void store(String key, byte[] data) {
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(this.serviceAccountCredentials)
                    .build()
                    .getService();

                BlobId blobId = BlobId.of(this.bucketName, key);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
                Blob blob = storage.create(blobInfo, data);
//                Blob blob = storage.create(blobInfo, data.getBytes());

                this.success = true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            this.success = false;
        }

    }

    @Override
    public Boolean exist(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] find(String key) {
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(this.serviceAccountCredentials)
                    .build()
                    .getService();

            BlobId blobId = BlobId.of(this.bucketName, key);
            Blob blob = storage.get(blobId);

//            String content = null;
            if (blob != null) {
//                byte[] prevContent = blob.getContent();
//                content = new String(prevContent, "UTF8");
                this.success = true;
                return blob.getContent();
//                return content;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.success = false;
        return null;
    }

    @Override
    public void remove(String key) {
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(this.serviceAccountCredentials)
                    .build()
                    .getService();

            BlobId blobId = BlobId.of(this.bucketName, key);
            this.success = storage.delete(blobId);
        } catch (Exception e) {
            this.success = false;
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
//            Bucket newBucket = storage.create(BucketInfo.newBuilder(bucketName)
//                    // See here for possible values: http://g.co/cloud/storage/docs/storage-classes
//                    .setStorageClass(StorageClass.COLDLINE)
//                    // Possible values: http://g.co/cloud/storage/docs/bucket-locations#location-mr
//                    .setLocation("eu")
//                    .build());

//            Page<Bucket> buckets = storage.list();
//            for (Bucket bucket : buckets.iterateAll()) {
//                System.out.println(bucket.getName());
//            }
//            String foundFileContent = new String(blob.getContent(), "UTF-8");
//            System.out.println(foundFileContent);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
