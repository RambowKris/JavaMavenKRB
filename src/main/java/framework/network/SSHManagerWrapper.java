/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.network;

import edu.emory.mathcs.backport.java.util.Arrays;
import framework.support.DataConverter;
import framework.support.TestUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author krb
 */
public class SSHManagerWrapper {

    private SSHManager sshManager;

    private ArrayList<Integer> openSSHports;

    public SSHManagerWrapper(SSHManager sshManager) {
        this.sshManager = sshManager;

        this.openSSHports = new ArrayList<>();
    }

    public void setupGlassfish() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(" wget -c --header \"Cookie: oraclelicense=accept-securebackup-cookie\" http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.tar.gz");
//        commands.add("wget --no-cookies --header \"Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F\" http://download.oracle.com/otn-pub/java/jdk/8u144/jdk-8u144-linux-x64.tar.gz");
        commands.add("tar xzf jdk-8u144-linux-x64.tar.gz");
        commands.add("wget http://download.java.net/glassfish/4.1/release/glassfish-4.1.zip");
        commands.add("unzip glassfish-4.1.zip");
//        this.addJavaToGlassfish("glassfish4","AS_JAVA=\"/home/ec2-user/jdk1.8.0_131\"");
        commands.add("./asadmin start-domain domain1");
        commands.add("./asadmin restart-domain domain1");
        
        // Password in file: [PATH-TO-USER]/glassfish4/glassfish/domains/[DOMAIN_NAME]/config/local_password
        // war file: C:\Users\krb.SECOMEA\.m2\repository\com\mycompany\JavaMavenKRB\1.0-SNAPSHOT\JavaMavenKRB-1.0-SNAPSHOT.war
        // scp -i C:\\Users\\krb.SECOMEA\\autoGenPending.pem C:\\Users\\krb.SECOMEA\\.m2\\repository\\com\\mycompany\\JavaMavenKRB\\1.0-SNAPSHOT\\JavaMavenKRB-1.0-SNAPSHOT.war ec2-user@ec2-18-195-85-38.eu-central-1.compute.amazonaws.com
        
        commands.add("asadmin change-admin-password ");
        commands.add("asadmin enable-secure-admin");

        
        commands.add("asadmin create-jdbc-resource --connectionpoolid Derby jdbc/Derby");

        for (String command : commands) {
            String commandResult = this.sshManager.sendCommand(command);
        }

    }

    public void openIngoingSSHPort(int portNumber) {
        String errorMessage = this.sshManager.connect();

        if (errorMessage != null) {
            System.out.println(errorMessage);
        } else {

            String lsCommand = "ls";
            String lsResult = this.sshManager.sendCommand(lsCommand);
            System.out.println(lsResult);

//            String sudoCommand = "sudo -i";
//            String sudoResult = this.sshManager.sendCommand(sudoCommand);
//
//            String sudoCommand = "sudo su";
//            String sudoResult = this.sshManager.sendCommand(sudoCommand);
//
            String sshConfigFilePath = "/etc/ssh/sshd_config";

//            String catSSHConfigCommand = "sudo cat " + sshConfigFilePath;
            String catSSHConfigCommand = "cat " + sshConfigFilePath;
            String catSSHConfigResult = this.sshManager.sendCommand(catSSHConfigCommand).trim();
//            System.out.println(catSSHConfigResult);

            String newSSHConfigContent = this.addPortToSSHConfig(catSSHConfigResult, portNumber);
//            System.out.println("New SSH config: ");
//            System.out.println(newSSHConfigContent);

//            sshConfigFilePath = "sshConfig.txt";
            String writeSSHConfigCommand = "echo " + DataConverter.escapeBashEchoString(newSSHConfigContent) + "";
            String writeSSHConfigResult = this.sshManager.sendCommand(writeSSHConfigCommand).trim();
//            System.out.println("Echoed new SSH config: ");
//            System.out.println(writeSSHConfigResult);

            if (newSSHConfigContent.equalsIgnoreCase(writeSSHConfigResult)) {
//                System.out.println("Echo and new content are similar");
                String writeFinalSSHConfigCommand = "echo " + DataConverter.escapeBashEchoString(newSSHConfigContent) + " | sudo tee " + sshConfigFilePath;
                String writeFinalSSHConfigResult = this.sshManager.sendCommand(writeFinalSSHConfigCommand);
//                System.out.println(writeFinalSSHConfigCommand);
//                System.out.println(writeFinalSSHConfigResult);
            }

            String restartSSHServiceCommand = "sudo service sshd restart";
            String restartSSHServiceResult = this.sshManager.sendCommand(restartSSHServiceCommand);
            this.sshManager.close();

            System.out.println(StringUtils.join(", ", this.openSSHports));
        }
    }

    private String addPortToSSHConfig(String sshConfigContent, int portNumber) {
        String newSSHConfigContent = "";

        String delimiter = "\n";
        String[] lines = sshConfigContent.split(delimiter);
        for (String line : lines) {
            if (line.indexOf("#Port 22") != -1 ? true : false) {
                newSSHConfigContent += line + delimiter;
                this.openSSHports.add(portNumber);
                newSSHConfigContent += "Port " + portNumber + delimiter;
            } else if (line.indexOf("Port ") != -1 ? true : false) {
                int foundPortNumber = Integer.parseInt(line.substring(5));
                if (!this.openSSHports.contains(foundPortNumber)) {
                    this.openSSHports.add(foundPortNumber);
                    newSSHConfigContent += line + delimiter;
                }
            } else {
                newSSHConfigContent += line + delimiter;
            }
        }

        newSSHConfigContent = newSSHConfigContent.substring(0, newSSHConfigContent.length() - delimiter.length());

        return newSSHConfigContent;
    }

    public static void main(String[] args) {
        String userName = "ec2-user";
        String password = "";
        String connectionIP = "ec2-35-158-83-176.eu-central-1.compute.amazonaws.com";
        String privateKeyFile = "C:/Users/krb.SECOMEA/autoGenPending.pem";
        Path privateKeyPath = Paths.get(privateKeyFile);
        try {
            byte[] privateKey = Files.readAllBytes(privateKeyPath);

            SSHManagerWrapper sshManagerWrapper = new SSHManagerWrapper(new SSHManager(userName, password, connectionIP, "", privateKey));
            sshManagerWrapper.openIngoingSSHPort(22);
//            sshManagerWrapper.openIngoingSSHPort(2000);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
