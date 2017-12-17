/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.storage;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import framework.support.DataConverter;
import framework.support.DataConverter;

/**
 *
 * @author krb
 */
public class FileStorage extends AbstractStorage {

    private Path file;
    private String fileName;

    public FileStorage() {
        this("dataStorage.txt");
    }

    public FileStorage(String fileName) {
        this.fileName = fileName;
        this.file = Paths.get(this.fileName);
        String test = "";
    }

    public String getAbsoluteFilePath(){
        return this.file.toAbsolutePath().toString();
    }
    
    public void deleteFile(Path file) {
        try {
            Files.delete(file);
        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
        }
    }

    public void createFile() {
        this.createFile(this.file, Arrays.asList(""));
    }

    public void createFile(Path file) {
        this.createFile(file, Arrays.asList(""));
    }

    public void createFile(Path file, List<String> lines) {
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
            this.success();
        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
            this.failure();
        }
    }

    public void store(String key, byte[] data) {
        if (!this.file.toFile().exists()) {
            this.createFile(this.file);
            if (!this.hasSuccess()) {
                return;
            }
        }

        try {
//            List<String> lines = Arrays.asList(key + DataConverter.encodeToJson(data));
//            Files.write(this.file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            Files.write(this.file, data);
            this.success();
        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
            this.failure();
        }
    }

    public Boolean exist(String key) {
        if (!this.file.toFile().exists()) {
            this.failure();
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(this.file);

            for (String line : lines) {
                if (line.substring(0, key.length()).matches(key)) {
                    this.success();
                    return true;
                }
            }

        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
        }
        this.failure();
        return false;
    }

    public byte[] find(String key) {
        if (!this.file.toFile().exists()) {
            this.failure();
            return null;
        }

        try {
            return Files.readAllBytes(this.file);
//            List<String> lines = Files.readAllLines(this.file);
//
//            for (String line : lines) {
//                if (line.substring(0, key.length()).matches(key)) {
//                    this.success();
//
//                    String json = line.substring(key.length());
//
//                    return json;
////                    return JsonConverter.getObjectFromJson(json, Object.class);
//                }
//            }

        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
        }
        this.failure();
        return null;
    }

    public void remove(String key) {
        if (!this.file.toFile().exists()) {
            this.failure();
            return;
        }

        ArrayList<String> newLines = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(this.file);

            for (String line : lines) {
                if (line.substring(0, key.length()).matches(key)) {
                    newLines.add(line);
                }
            }

            this.deleteFile(this.file);
            this.createFile(this.file, newLines);
            this.success();

        } catch (Exception e) {
            Class thisClass = this.getClass();
            String methodName = thisClass.getEnclosingMethod().getName();
            System.out.println("Error in " + thisClass.toString() + "." + methodName + ": " + e.getMessage());
            this.failure();
        }
    }
}
