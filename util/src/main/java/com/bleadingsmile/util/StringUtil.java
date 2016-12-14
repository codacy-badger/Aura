package com.bleadingsmile.util;

/**
 * Created by Larry Hsiao on 2016/11/28.
 */
public class StringUtil {
    public static String replaceNull(String target){
        return target == null || target.isEmpty()? "":target;
    }
}
