package com.bleadingsmile.util.connection;

import java.net.HttpURLConnection;

/**
 * Created by Larry Hsiao on 2016/11/20.
 */

public class HttpGet extends HttpApi {
    public HttpGet(String urlString) {
        super(urlString);
    }

    @Override
    protected void setupConnection(HttpURLConnection connection) throws Exception {
    }
}
