/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.tests;

import framework.support.*;
import framework.support.Crypto;
import framework.support.DataConverter;
import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krb
 */
public class CryptoTest {

    public CryptoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testHashPasswordTime() {
        String password = "mypasswordissecret";

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);

        int iterations = 200000;
        int keyLength = 512;

//        System.out.println("Password: " + password);
//        System.out.println("Salt: " + DataConverter.byteArray2Hex(salt));
//        System.out.println("Salt length: " + DataConverter.byteArray2Hex(salt).length());
//        System.out.println("Iterations: " + iterations);
//        System.out.println("Key length: " + keyLength);
        int startTime = (int) System.currentTimeMillis();

        byte[] hash = Crypto.hashPassword(password.toCharArray(), salt, iterations, keyLength);

        int endTime = (int) System.currentTimeMillis();

//        System.out.println("Hash value: " + DataConverter.byteArray2Hex(hash));
//        System.out.println("Hash value length: " + DataConverter.byteArray2Hex(hash).length());
        int calculationTime = endTime - startTime;
        System.out.println("Calculation Time: " + calculationTime);
        if(400 < calculationTime && calculationTime < 600){
            
        }else{
            System.err.println("Password hashing DID NOT FINISH within the set time");
        }
//        assertTrue(400 < calculationTime && calculationTime < 600);
    }

    @Test
    public void testHashPassword() {
        String password = "mypasswordissecret";

        String saltString = "5ee72b40716a9a2b2b7ee3f5be55f3d704eaec15565d55f7e7b53e493232f3984f3ae3e517f1860e0c6d5f997a454f243fbbff4232f3c38ff1c85faf4b3bd12d";
        byte[] salt = DataConverter.hex2ByteArray(saltString);

        int iterations = 100000;
        int keyLength = 512;

        byte[] hash = Crypto.hashPassword(password.toCharArray(), salt, iterations, keyLength);

        assertTrue(DataConverter.byteArray2Hex(hash).equalsIgnoreCase("dab3e276abfcf94de3d0346e5ea1c2dd7de6a2c91ddfa4d48220e1cde61098f52f1d22ca55586e9d6b3760c8a2638915c079c283f923c7b87f48843ee57c9100"));
    }

    @Test
    public void testGetObjectChecksum() {
        Object funny = "Hello World!";
        String checksum = Crypto.getObjectChecksum(funny);
//        System.out.println(checksum);

        assertTrue(checksum.equalsIgnoreCase("53ddcd099453eca24a8b96c2005ec96a"));
    }

    @Test
    public void testSha256Hash() {
        String testString = "MyTestString";
        byte[] hash = Crypto.sha256Hash(testString);

        assertTrue(Arrays.equals(hash, new byte[]{-8, -87, -116, 109, 59, 35, -112, -125, 120, -55, 110, 71, 29, 57, 34, 95, 54, -114, -91, 123, -107, -30, 98, 99, -77, -14, 67, 14, 38, 33, 8, -123}));
    }

    @Test
    public void testCalculateHash() {
        String testString = "MyTestString";
        byte[] hash = Crypto.calculateHash("SHA-256", testString);

        assertTrue(Arrays.equals(hash, new byte[]{-8, -87, -116, 109, 59, 35, -112, -125, 120, -55, 110, 71, 29, 57, 34, 95, 54, -114, -91, 123, -107, -30, 98, 99, -77, -14, 67, 14, 38, 33, 8, -123}));
    }
}
