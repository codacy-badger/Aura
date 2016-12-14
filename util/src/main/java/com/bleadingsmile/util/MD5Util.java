package com.bleadingsmile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static boolean isMd5Same(String md5, File updateFile) throws IOException, NoSuchAlgorithmException {
        if (md5 == null || md5.isEmpty() || updateFile == null) {
            throw new IllegalArgumentException();
        }
        String calculatedDigest = calculateMD5(updateFile);
        return md5.equalsIgnoreCase(calculatedDigest);
    }

    public static String calculateMD5(File updateFile) throws IOException, NoSuchAlgorithmException {
        return calculateMD5(new FileInputStream(updateFile));
    }

    public static String calculateMD5(InputStream inputStream) throws IOException, NoSuchAlgorithmException {
        try (InputStream tryInputStream = inputStream) {
            MessageDigest digest;
            digest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = tryInputStream.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        }
    }
}