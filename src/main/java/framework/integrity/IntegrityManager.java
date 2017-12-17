/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.integrity;

import framework.support.AbstractManager;
import framework.support.Crypto;
import framework.support.DataConverter;
import framework.support.ExecuteCommandLine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.glassfish.samples.IntegrityDataEJB;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 *
 * @author krb
 */
public class IntegrityManager extends AbstractManager {

    private IntegrityDataEJB integrityDataBean;

    public IntegrityManager(IntegrityDataEJB integrityDataBean) {
        this.integrityDataBean = integrityDataBean;
    }

    public Boolean checkDataIntegrity(String command, String keyName, byte[] data) {
        if (command.equalsIgnoreCase("find")) {
            String storedChecksum = this.integrityDataBean.getValue(keyName);

            try {
                MessageDigest md5Digest = MessageDigest.getInstance("MD5");
                String dataString = DataConverter.encodeToJson(data);
                String checksum = Crypto.getChecksumOfByteArray(md5Digest, dataString.getBytes());
                return storedChecksum.equalsIgnoreCase(checksum);
            } catch (Exception e) {
            }
        } else if (command.equalsIgnoreCase("store")) {
            try {
                MessageDigest md5Digest = MessageDigest.getInstance("MD5");
//                String dataString = DataConverter.encodeToJson(data);
                String checksum = Crypto.getChecksumOfByteArray(md5Digest, data);
                this.integrityDataBean.create(keyName, checksum);
                return true;
            } catch (Exception e) {
            }

        } else if (command.equalsIgnoreCase("remove")) {
            this.integrityDataBean.remove(keyName);
            return true;
        }

        return false;
    }

    public Boolean checkServiceIntegrity(String className, String command) {
        if (className.isEmpty() || command.isEmpty()) {
            return false;
        }

        try {
            String testCommand = "test" + command.substring(0, 1).toUpperCase() + command.substring(1);
            String testClassName = "framework.tests." + className.substring(0, 1).toUpperCase() + className.substring(1) + "Test";
            Class testClass = Class.forName(testClassName);

            Request request = Request.method(testClass, testCommand);
            Result result = new JUnitCore().run(request);

//            for (Failure failure : result.getFailures()) {
//                System.out.println(failure.toString());
//            }
            return result.wasSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            System.err.println("Cause: "+e.getCause());
            System.err.println("Stacktrace: "+e.getStackTrace().toString());
            return false;
        }
    }

    public Boolean runTest(String className, String command) {
//        String executeCommand = "cmd /c mvn -Dtest=" + className + "Test#test" + command + " surefire:test";
        String executeCommand = "cmd /c mvn -Dtest=" + className + "Test#test" + command + " test";

        InputStream is = ExecuteCommandLine.execute(executeCommand);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (br.ready()) {
                String line = br.readLine();
                if (line.indexOf("[INFO] BUILD SUCCESS") != -1 ? true : false) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        IntegrityManager im = new IntegrityManager(null);
        String command = "execute";
        String className = "ComputeManager";
//        String command = "Sha256Hash";
//        String className = "Crypto";

        Boolean result = im.checkServiceIntegrity(className, command);

        System.out.println("Testing " + command + " of " + className + " gives result: " + result);

//        Result result = JUnitCore.runClasses(ComputeManagerTest.class);
//        Result result = JUnitCore.runClasses(StorageManagerTest.class);
//        for (Failure failure : result.getFailures()) {
//            System.out.println(failure.toString());
//        }
//        System.out.println(result.wasSuccessful());
    }
}
