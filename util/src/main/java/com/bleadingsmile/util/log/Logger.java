package com.bleadingsmile.util.log;

/**
 * Created by 1500242A on 2016/8/31.
 */
public abstract class Logger {
    public abstract void info(String message);
    public abstract void error(String message);
    public abstract void debug(String message);
    public abstract void warning(String message);
}
