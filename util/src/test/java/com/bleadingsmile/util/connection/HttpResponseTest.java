package com.bleadingsmile.util.connection;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

/**
 * Created by Larry Hsiao on 2016/11/20.
 */
public class HttpResponseTest {
    @Test
    public void fieldCode() throws Exception {
        Field field = HttpResponse.class.getField("code");
        assertEquals(int.class, field.getType());
    }

    @Test
    public void fieldBodyBytes() throws Exception {
        Field field = HttpResponse.class.getField("bodyBytes");
        assertEquals(byte[].class, field.getType());
    }
}