package com.bleadingsmile.util.log;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Larry Hsiao on 2016/12/5.
 */
public class FileLogger extends Logger {
    private final String logPath;

    public FileLogger(String fileName) {
        this.logPath = fileName;
    }

    @Override
    public void info(String message) {
        appendLog("INFO: " + message);
    }

    @Override
    public void error(String message) {
        appendLog("ERROR: " + message);
    }

    @Override
    public void debug(String message) {
        appendLog("DEBUG: " + message);
    }

    @Override
    public void warning(String message) {
        appendLog("WARN:" + message);
    }

    private void appendLog(String message) {
        try {
            try (OutputStream out = new FileOutputStream(logPath, true)) {
                message = new SimpleDateFormat().format(new Date()) + " " + message + "\n";
                int len = message.getBytes().length;
                int rem = len;
                while (rem > 0) {
                    int n = Math.min(rem, 1024);
                    out.write(message.getBytes(), (len - rem), n);
                    rem -= n;
                }
            }
        } catch (Exception ignore) {
        }
    }
}
