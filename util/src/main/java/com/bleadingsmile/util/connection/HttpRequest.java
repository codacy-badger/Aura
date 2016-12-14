package com.bleadingsmile.util.connection;


/**
 * Created by Larry Hsiao on 2016/11/2.
 */

public interface HttpRequest {
    HttpResponse request() throws Exception;
}
