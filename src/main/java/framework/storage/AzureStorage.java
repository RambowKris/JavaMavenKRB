/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.storage;

import com.google.common.io.ByteStreams;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import framework.support.DataConverter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author krb
 */
public class AzureStorage extends AbstractStorage {

    private String storageConnectionString;
    private String bucketName;

    public AzureStorage(String cloudConfiguration, String bucketName) {
        Map<String, String> cloudConfigurationMappings = (Map<String, String>) DataConverter.getObjectFromJson(cloudConfiguration, Map.class);        
        this.storageConnectionString = "DefaultEndpointsProtocol=http;"
                //            = "DefaultEndpointsProtocol=https;"
                + "AccountName=" + cloudConfigurationMappings.get("accountName") + ";"
                + "AccountKey=" + cloudConfigurationMappings.get("accountKey") + ";";
//            + "EndpointSuffix=core.windows.net";        
        this.bucketName = bucketName;
    }
    
    public AzureStorage(String accountName, String accountKey, String bucketName) {
        this.storageConnectionString = "DefaultEndpointsProtocol=http;"
                //            = "DefaultEndpointsProtocol=https;"
                + "AccountName=" + accountName + ";"
                + "AccountKey=" + accountKey + ";";
//            + "EndpointSuffix=core.windows.net";
        this.bucketName = bucketName;
    }

    @Override
    public void store(String key, byte[] data) {
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(this.storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference(this.bucketName);
            container.createIfNotExists();

            // Upload an image file.
            CloudBlockBlob blob = container.getBlockBlobReference(key);
            blob.uploadFromByteArray(data, 0, data.length);
//            blob.uploadText(data);
            this.success();

            // Download the image file.
//            File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
//            blob.downloadToFile(destinationFile.getAbsolutePath());
        } catch (FileNotFoundException fileNotFoundException) {
            this.failure();
            System.out.print("FileNotFoundException encountered: ");
            System.out.println(fileNotFoundException.getMessage());
//            System.exit(-1);
        } catch (StorageException storageException) {
            this.failure();
            System.out.print("StorageException encountered: ");
            System.out.println(storageException.getMessage());
//            System.exit(-1);
        } catch (Exception e) {
            this.failure();
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
//            System.exit(-1);
        }
    }

    @Override
    public Boolean exist(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] find(String key) {
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference(bucketName);
            container.createIfNotExists();

            // Upload an image file.
            CloudBlockBlob blob = container.getBlockBlobReference(key);

            // Download the image file.
            InputStream is = blob.openInputStream();
            
//            String result = blob.downloadText();

            this.success();
            return ByteStreams.toByteArray(is);
        } catch (StorageException storageException) {
            System.out.print("StorageException encountered: ");
            System.out.println(storageException.getMessage());
//            System.exit(-1);
        } catch (Exception e) {
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
//            System.exit(-1);
        }
        this.failure();
        return null;
    }

    @Override
    public void remove(String key) {
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference(bucketName);
            container.createIfNotExists();

            // Upload an image file.
            CloudBlockBlob blob = container.getBlockBlobReference(key);

            // Download the image file.
            blob.delete();

            this.success();
        } catch (StorageException storageException) {
            this.failure();
            System.out.print("StorageException encountered: ");
            System.out.println(storageException.getMessage());
//            System.exit(-1);
        } catch (Exception e) {
            this.failure();
            System.out.print("Exception encountered: ");
            System.out.println(e.getMessage());
//            System.exit(-1);
        }
    }

}
