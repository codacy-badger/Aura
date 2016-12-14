package com.bleadingsmile.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Larry Hsiao on 2016/11/20.
 */
public class MD5UtilTest {
    @Before
    public void setUp() throws Exception {
        String home = System.getProperty("user.home");
        File file = new File(home, "unitTestFile");
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write("this is a test string".getBytes());
    }

    @Test
    public void calculateMD5() throws Exception {
        String home = System.getProperty("user.home");
        File file = new File(home, "unitTestFile");
        String md5 = MD5Util.calculateMD5(file);

        final String exceptedMd5 = "486eb65274adb86441072afa1e2289f3";
        assertEquals(exceptedMd5, md5);
    }

    @Test
    public void isMd5Same_md5Empty() throws Exception {
        try {
            MD5Util.isMd5Same("", null);
            fail();
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void isMd5Same_fileNotExist() throws Exception {
        try {
            MD5Util.isMd5Same("123", null);
            fail();
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void isMd5Same() throws Exception {
        final String exceptedMd5 = "486eb65274adb86441072afa1e2289f3";
        String home = System.getProperty("user.home");
        File file = new File(home, "unitTestFile");
        boolean same = MD5Util.isMd5Same(exceptedMd5, file);
        assertTrue(same);
    }

    @Test
    public void isMd5NotSame() throws Exception {
        final String exceptedMd5 = "1234";
        String home = System.getProperty("user.home");
        File file = new File(home, "unitTestFile");
        boolean same = MD5Util.isMd5Same(exceptedMd5, file);
        assertFalse(same);
    }
}