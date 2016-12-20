package com.bleadingsmile.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by larryhsiao on 2016/12/16.
 */

public class FileUtil {
    public static boolean deleteDirectory(final File directory) throws IOException {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return true;
            }
            for (File child : files) {
                deleteDirectory(child);
            }
        }
        return directory.delete();
    }

}
