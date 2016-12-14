package com.bleadingsmile.util.connection;

import org.junit.Test;

import java.lang.reflect.Method;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Larry Hsiao on 2016/11/20.
 */
public class HttpRequestTest {

    @Test
    public void getUrl() throws Exception {
        HttpGet api = new HttpGet("https://google.com");
        HttpResponse response = api.request();
        assertEquals(HTTP_OK, response.code);
    }

    @Test
    public void getNotExistUrl() throws Exception {
        try {
            HttpGet api = new HttpGet("Https://google.comfd");
            api.request();
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void getUnknownProtocol() throws Exception {
        try {
            new HttpGet("telnet://google.com");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void getNonUriSchema() throws Exception {
        try {
            new HttpGet("https://:::");
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void methodSpec() throws Exception {
        Method method = HttpRequest.class.getMethod("request");
        Class exception = method.getExceptionTypes()[0];
        assertEquals(Exception.class, exception);
    }
}