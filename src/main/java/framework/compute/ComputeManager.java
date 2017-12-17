/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.compute;

import framework.support.AbstractManager;
import framework.support.ExecutionManager;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author krb
 */
public class ComputeManager extends AbstractManager {

    public String getExecute(String first, String second) {
        this.success();
        return first + second;
    }

    public String putHaveFun(String first, String second) {
        String result = first.replaceAll(second, "");
        
        this.success();
        return result;
    }

    public static void main(String[] args) {
        ComputeManager computeManager = new ComputeManager();

        String keyName = "first";
        String data = "";

        String result = (String) ExecutionManager.executeMethodOnObject(computeManager, "execute", new Object[]{keyName, data});
        System.out.println(result);
    }
}
