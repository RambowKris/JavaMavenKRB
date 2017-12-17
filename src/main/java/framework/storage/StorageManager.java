/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.storage;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Storage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import framework.network.AWSrequestor;
import framework.network.ClientConnector;
import framework.storage.AbstractStorage;
import framework.support.Crypto;
import framework.support.DataConverter;
import framework.support.DataHandler;
import framework.support.ExecutionManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
//import Support.Reflection;

/**
 *
 * @author krb
 */
public class StorageManager {
    
    private Boolean success = true;
    private String[] parties;
    private String[] redundantParties;
    private String splitFunction;
    
    public StorageManager() {
        this.setSplitFunction("standard");
        this.setParties(new String[]{"Aws-krbfirstbucket", "Aws-krbshudluckyoutube", "Azure-krbfirstbucket", "Azure-krbshudluckyoutube", "Gc-krbfirstbucket", "Gc-krbshudluckyoutube"});
    }
    
    public void setParties(String[] parties) {
        this.parties = parties;
        
        this.redundantParties = this.parties.clone();
        DataConverter.shiftArray(this.redundantParties, true);
    }
    
    public void setSplitFunction(String splitFunction) {
        if (!Arrays.asList(new String[]{"standard", "picking"}).contains(splitFunction)) {
            this.failure();
            return;
        }
        this.splitFunction = splitFunction;
    }
    
    public void store(String key, byte[] data) {
        Map<String, byte[]> parts = DataHandler.splitData(data, parties, this.splitFunction);
        
        this.success = true;
        for (int p = 0; p < this.parties.length; p++) {
            byte[] storeData = parts.get(this.parties[p]);
            
            String part = this.parties[p];
            String[] cloud = part.split("-");
            String hashedKey = Crypto.getObjectChecksum(part + key);
//            System.out.println("Cloud: " + part + "\nKey: " + hashedKey + "\n");
            Object storage = ExecutionManager.createObject("framework.storage." + cloud[0] + "Storage", new Object[]{cloud[1]});
            ExecutionManager.executeMethodOnObject(storage, "store", new Object[]{hashedKey, storeData});
//            ExecutionManager.executeMethodOnObject(storage, "store", new Object[]{hashedKey, DataConverter.encodeToJson(storeData)});
            this.success &= (Boolean) ExecutionManager.executeMethodOnObject(storage, "hasSuccess", new Object[]{});
            
            String redundantPart = this.redundantParties[p];
            String[] redundantCloud = redundantPart.split("-");
            String hashedHashedKey = Crypto.getObjectChecksum(redundantPart + hashedKey);
//            System.out.println("Redundant Cloud: " + redundantPart + "\nRedundant Key: " + hashedHashedKey + "\n");
            Object redundantStorage = ExecutionManager.createObject("framework.storage." + redundantCloud[0] + "Storage", new Object[]{redundantCloud[1]});
            ExecutionManager.executeMethodOnObject(redundantStorage, "store", new Object[]{hashedHashedKey, storeData});
//            ExecutionManager.executeMethodOnObject(redundantStorage, "store", new Object[]{hashedHashedKey, DataConverter.encodeToJson(storeData)});
            this.success &= (Boolean) ExecutionManager.executeMethodOnObject(redundantStorage, "hasSuccess", new Object[]{});
        }
    }
    
    public byte[] find(String key) {
//        Object result = this.storage.find(key);

        Map<String, byte[]> parts = new TreeMap<String, byte[]>();
        for (int p = 0; p < this.parties.length; p++) {
            
            String part = this.parties[p];
            byte[] foundChunk = null;
            try {
                String[] cloud = part.split("-");
                String hashedKey = Crypto.getObjectChecksum(part + key);
//                System.out.println("Cloud: " + part + "\nKey: " + hashedKey + "\n");
                Object storage = ExecutionManager.createObject("framework.storage." + cloud[0] + "Storage", new Object[]{cloud[1]});
                byte[] chunk = (byte[]) ExecutionManager.executeMethodOnObject(storage, "find", new Object[]{hashedKey});
                
                if (chunk == null) {
                    String redundantPart = this.redundantParties[p];
                    String[] redundantCloud = redundantPart.split("-");
                    String hashedHashedKey = Crypto.getObjectChecksum(redundantPart + hashedKey);
//                    System.out.println("Redundant Cloud: " + redundantPart + "\nRedundant Key: " + hashedHashedKey + "\n");
                    Object redundantStorage = ExecutionManager.createObject("framework.storage." + redundantCloud[0] + "Storage", new Object[]{redundantCloud[1]});
                    chunk = (byte[]) ExecutionManager.executeMethodOnObject(redundantStorage, "find", new Object[]{hashedHashedKey});
                    foundChunk = chunk;
//                    foundChunk = (byte[]) DataConverter.getObjectFromJson(chunkString, byte[].class);
                    this.success &= (Boolean) ExecutionManager.executeMethodOnObject(redundantStorage, "hasSuccess", new Object[]{});
                } else {
                    foundChunk = chunk;
//                    foundChunk = (byte[]) DataConverter.getObjectFromJson(chunkString, byte[].class);
                    this.success &= (Boolean) ExecutionManager.executeMethodOnObject(storage, "hasSuccess", new Object[]{});
                }
                
                if (foundChunk == null) {
                    return null;
                }
                
                parts.put(part, foundChunk);
//                System.out.println("Found String: " + new String(foundChunk, "UTF8"));
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Failed at: " + part);
                this.failure();
                return null;
            }
        }
        
        byte[] result = DataHandler.concatData(parts, this.splitFunction);
//        String result = DataHandler.concatData(parts, this.splitFunction);
        
        return result;
    }
    
    public Boolean exist(String key) {
//        this.storage.exist(key);
//        return this.storage.hasSuccess();
        return false;
    }
    
    public void remove(String key) {

//        this.success = true;
        for (int p = 0; p < this.parties.length; p++) {
            String part = this.parties[p];
            String[] cloud = part.split("-");
            String hashedKey = Crypto.getObjectChecksum(part + key);
//            System.out.println("Cloud: " + part + "\nKey: " + hashedKey + "\n");
            Object storage = ExecutionManager.createObject("framework.storage." + cloud[0] + "Storage", new Object[]{cloud[1]});
            ExecutionManager.executeMethodOnObject(storage, "remove", new Object[]{hashedKey});
//            this.success &= (Boolean) ExecutionManager.executeMethodOnObject(storage, "hasSuccess", new Object[]{});

            String redundantPart = this.redundantParties[p];
            String[] redundantCloud = redundantPart.split("-");
            String hashedHashedKey = Crypto.getObjectChecksum(redundantPart + hashedKey);
//            System.out.println("Redundant Cloud: " + redundantPart + "\nRedundant Key: " + hashedHashedKey + "\n");
            Object redundantStorage = ExecutionManager.createObject("framework.storage." + redundantCloud[0] + "Storage", new Object[]{redundantCloud[1]});
            ExecutionManager.executeMethodOnObject(redundantStorage, "remove", new Object[]{hashedHashedKey});
//            this.success &= (Boolean) ExecutionManager.executeMethodOnObject(redundantStorage, "hasSuccess", new Object[]{});
        }
        this.success = true;
    }
    
    public void failure() {
        this.success = false;
    }
    
    public Boolean hasSuccess() {
        return this.success;
    }
    
    public static void main(String[] args) {
        try {
            Path path = Paths.get("C:\\Users\\krb.SECOMEA\\Desktop\\YouSee\\Traceroutes.PNG");
            InputStream is = new BufferedInputStream(new FileInputStream(path.toFile()));

//            content = new BufferedReader(new InputStreamReader(is))
//                    .lines().collect(Collectors.joining("\n"));
//            is.close();
//            BufferedReader r = new BufferedReader(new InputStreamReader(is));
//            ArrayList<String> lines = new ArrayList<>();
//            String line;
//            while ((line = r.readLine()) != null) {
//                lines.add(line);
//            }            
//            Files.write(path, lines, Charset.forName("UTF-8"));
            
            Path newPath = Paths.get("C:\\Users\\krb.SECOMEA\\Desktop\\YouSee\\ReceivedTraceroutes.PNG");
            Files.write(newPath, Arrays.asList(DataConverter.encodeToJson(DataConverter.inputStreamToString(is))), Charset.forName("UTF-8"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
    }
}
