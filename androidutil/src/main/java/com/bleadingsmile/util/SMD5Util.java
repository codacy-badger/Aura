package com.bleadingsmile.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SMD5Util {
    private static final String INVALID_MD5 = "";

    private SMD5Util() {
    }

    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            return false;
        }

        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
            return false;
        }

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMD5(File updateFile) {
        try {
            return calculateMD5(new FileInputStream(updateFile));
        } catch (FileNotFoundException e) {
            return INVALID_MD5;
        }
    }

    public static String calculateMD5(InputStream inputStream) {
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int    read;
            while ((read = inputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[]     md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String     output = bigInt.toString(16);
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            return INVALID_MD5;
        } catch (NoSuchAlgorithmException e) {
            return INVALID_MD5;
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }catch (IOException ignore){
            }
        }
    }
}