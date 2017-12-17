/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.support;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author krb
 */
public class ExecuteCommandLine {

    public static InputStream execute(String command) {
        String result = null;

        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            InputStream is = pr.getInputStream();
            int exitVal = pr.waitFor();
//            System.out.println("Process exitValue: " + exitVal);
            return is;

//            result = IOUtils.toString(is);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(ExecuteCommandLine.execute("cmd /c mvn -Dtest=CryptoTest test"));
//        System.out.println(ExecuteCommandLine.execute("cmd /c mvn test"));

//        ExecuteCommandLine.execute("cmd /c SET GOOGLE_APPLICATION_CREDENTIALS=C:\\Users\\krb.SECOMEA\\Documents\\Private\\DTU\\Master-Thesis\\krb-bookshelf-d80093b2c390");
//        System.out.println(System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
//        System.out.println(ExecuteCommandLine.execute("cmd /c echo %GOOGLE_APPLICATION_CREDENTIALS%"));
//        System.out.println(ExecuteCommandLine.execute("cmd /c type %GOOGLE_APPLICATION_CREDENTIALS%"));
//        System.out.println(ExecuteCommandLine.execute("cmd /c dir"));
//        System.out.println(ExecuteCommandLine.execute("cmd /c echo fun"));
    }
}
