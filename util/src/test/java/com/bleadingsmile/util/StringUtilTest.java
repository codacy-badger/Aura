package com.bleadingsmile.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Larry Hsiao on 2016/11/28.
 */
public class StringUtilTest {
    @Test
    public void checkNullString() throws Exception {
        String actual = StringUtil.replaceNull(null);
        assertEquals("", actual);
    }

    @Test
    public void checkNonNullString() throws Exception {
        String excepted = "this is excepted string";
        String actual = StringUtil.replaceNull(excepted);
        assertEquals(excepted, actual);
    }

    @Test
    public void checkEmptyString() throws Exception {
        String actual = "";
        assertEquals("", actual);
    }
}