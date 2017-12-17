/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework.support;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author krb
 */
public class DataConverter {

    public static String escapeBashEchoString(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        sb.append('"');
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '!':
                    sb.append("\"'!'\"");
                    break;
                case '$':
                    sb.append("\\$");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();

    }

    public static String escapeString(String input, String prefix) {
        char[] allowed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray();
        char[] charArray = input.toString().toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : charArray) {
            Boolean foundInAllowed = false;
            for (char a : allowed) {
                if (c == a) {
                    result.append(a);
                    foundInAllowed = true;
                    break;
                }
            }
            if (!foundInAllowed) {
                result.append(prefix);
                result.append(c);
            } else {
                foundInAllowed = false;
            }
        }
        return result.toString();
    }

    public static String escapeVarString(String origin) {
        StringBuilder sb = new StringBuilder(origin.length());
        sb.append('"');
        for (char c : origin.toCharArray()) {
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static String uriEncode(String input, boolean encodeSlash) {
        String urlEncoded = null;
        try {
            urlEncoded = URLEncoder.encode(input, StandardCharsets.UTF_8.toString());

            if (!encodeSlash) {
                urlEncoded = urlEncoded.replaceAll("%2F", "/");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return urlEncoded;
    }

    public static String encodeToJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static Object getObjectFromJson(String json, Class className) {
        Gson gson = new Gson();
        return gson.fromJson(json, className);
    }

    public static String inputStreamToString(InputStream is) {
        String result = null;

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
            result = sb.toString();
        }

        return result;
    }

    public static InputStream stringToInputStream(String string) {
        try {
            return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
        }

        return null;
    }

    public static String stringToBinary(String str) {
        String result = "";
        BigInteger bval = new BigInteger(str.getBytes());
        result = bval.toString(2);

        return result;
    }

    public static String[] shiftArray(String[] strings, boolean shiftRight) {
        int arraySize = strings.length;
        int lastPosition = arraySize - 1;
        String temp;

        if (shiftRight) {
            temp = strings[lastPosition];
            for (int i = (lastPosition - 1); i >= 0; i--) {
                strings[i + 1] = strings[i];
            }
            strings[0] = temp;
        } else {
            temp = strings[0];
            for (int i = 0; i < lastPosition; i++) {
                strings[i] = strings[i + 1];
            }
            strings[lastPosition] = temp;
        }

        return strings;
    }

    public static String binaryToString(String binary) {
        String result = "";

        byte[] bval = new BigInteger(binary, 2).toByteArray();
        try {
            result = new String(bval);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return result;
    }

    public static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static byte[] hex2ByteArray(String hexString) {
        return new BigInteger(hexString, 16).toByteArray();
    }

}
