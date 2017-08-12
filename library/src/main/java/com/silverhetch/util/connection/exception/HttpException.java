package com.silverhetch.util.connection.exception;

import android.annotation.TargetApi;
import android.os.Build;

import java.lang.annotation.Target;

/**
 * Created by larryhsiao on 2017/6/6.
 */

public class HttpException extends RuntimeException {
    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
