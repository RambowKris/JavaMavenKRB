/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author krb
 */
public class DataHandler {

    public static Map<String, byte[]> splitData(byte[] bytes, String[] keys) {
        return DataHandler.splitData(bytes, keys, "standard");
    }

    public static Map<String, byte[]> splitData(byte[] bytes, String[] keys, String splittingFormat) {
        int numberOfTotalBytes = bytes.length;
        int numberOfChunks = keys.length;
        int chunkSize = bytes.length % numberOfChunks == 0 ? (bytes.length / numberOfChunks) : (bytes.length / numberOfChunks) + 1;

        Map<String, byte[]> chunks = new TreeMap<>();
        if (splittingFormat.equalsIgnoreCase("standard")) {
//        System.out.println(chunkSize);

            int chunkNumber = 1;
            for (String cloud : keys) {
                chunks.put(cloud, Arrays.copyOfRange(bytes, (chunkNumber - 1) * chunkSize, chunkNumber * chunkSize > numberOfTotalBytes ? numberOfTotalBytes : chunkNumber * chunkSize));
                chunkNumber++;
            }
        } else if (splittingFormat.equalsIgnoreCase("picking")) {
            byte[][] splittedBytes = new byte[numberOfChunks][chunkSize];
            for (int chunkNumber = 0; chunkNumber < numberOfTotalBytes; chunkNumber++) {
                int keyNumber = chunkNumber % keys.length;
                int arrayPosition = chunkNumber == 0 ? 0 : chunkNumber / keys.length;
                splittedBytes[keyNumber][arrayPosition] = bytes[chunkNumber];
            }

            for (int k = 0; k < keys.length; k++) {
                chunks.put(keys[k], splittedBytes[k]);
            }
        }

        return chunks;
    }

    public static String bytesToString(byte[] bytes) {
        String jsonString = new String(bytes);
//        System.out.println(jsonString);
        return jsonString;
    }

    public static byte[] concatData(Map<String, byte[]> chunks) {
        return DataHandler.concatData(chunks, "standard", Arrays.copyOf(chunks.keySet().toArray(), chunks.keySet().toArray().length, String[].class));
//        return DataHandler.concatData(chunks, "standard", (String[]) chunks.keySet().toArray());
    }

    public static byte[] concatData(Map<String, byte[]> chunks, String[] keys) {
        return DataHandler.concatData(chunks, "standard", keys);
    }

    public static byte[] concatData(Map<String, byte[]> chunks, String splittingFormat) {
        return DataHandler.concatData(chunks, splittingFormat, Arrays.copyOf(chunks.keySet().toArray(), chunks.keySet().toArray().length, String[].class));
    }

    public static byte[] concatData(Map<String, byte[]> chunks, String splittingFormat, String[] keys) {
        int totalLength = 0;
        for (String keyName : keys) {
            byte[] currentChunk = chunks.get(keyName);
            if (currentChunk[currentChunk.length - 1] == 0) {
                totalLength += currentChunk.length - 1;
            } else {
                totalLength += currentChunk.length;
            }
        }
        byte[] totalBytes = new byte[totalLength];

        if (splittingFormat.equalsIgnoreCase("standard")) {
            int temporaryLength = 0;
            for (String keyName : keys) {
                byte[] currentChunk = chunks.get(keyName);
                int currentChunkLength = currentChunk.length;
                System.arraycopy(currentChunk, 0, totalBytes, temporaryLength, currentChunkLength);
                temporaryLength += currentChunkLength;
            }
        } else if (splittingFormat.equalsIgnoreCase("picking")) {
            int temporaryLength = 0;
            byte[] firstChunk = chunks.get(keys[0]);
            for (int arrayPosition = 0; arrayPosition < firstChunk.length; arrayPosition++) {
                for (String keyName : keys) {
                    byte currentByte = chunks.get(keyName)[arrayPosition];
                    if (currentByte == 0) {
                        break;
                    }
                    totalBytes[temporaryLength] = currentByte;
                    temporaryLength++;
                }
            }

        }

        return totalBytes;
//        return DataHandler.bytesToString(totalBytes);
    }

    public static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
