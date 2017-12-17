/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.support;

import framework.storage.FileStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author krb
 */
public class Crypto {

    public static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations, final int keyLength) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getObjectChecksum(Object data) {
        // Creates test file for checksum calculation
        String testFileName = "testFile.txt";
        FileStorage fs = new FileStorage();
        Path filePath = Paths.get(testFileName);
        String dataString = DataConverter.encodeToJson(data);
        fs.createFile(filePath, Arrays.asList(dataString));

        String checksum = null;
        try {
            //Use MD5 algorithm
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");

            //Get the checksum
            checksum = Crypto.getFileChecksum(md5Digest, filePath.toFile());
            //see checksum
//            System.out.println(checksum);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        fs.deleteFile(filePath);

        return checksum;
    }

    public static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public static String getChecksumOfByteArray(MessageDigest digest, byte[] object) throws IOException {
        digest.update(object, 0, object.length);

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static byte[] hmacSHA256(byte[] key, String data) {
        byte[] output = null;
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            output = sha256_HMAC.doFinal(data.getBytes("UTF8"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return output;
    }

    public static byte[] calculateHash(String algorithm, String message) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(message.getBytes());
            byte[] hash = md.digest();

            return hash;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static String calculateHashToHexString(String algorithm, String message) {
        byte[] hash = Crypto.calculateHash(algorithm, message);
        return DataConverter.byteArray2Hex(hash);
    }

    public static byte[] sha256Hash(String data) {
        return Crypto.calculateHash("SHA-256", data);
    }
}
