package com.lrs.utils;

/**
 * Created by fcambarieri on 27/02/16.
 * @author fcambarieri
 */
public class ExceptionUtils {

    /**
     * Introspects the Throwable to obtain the cause.
     * */
    public static Throwable cause(Throwable t) {
        Throwable cause = null;
        Throwable result = t;

        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }

    /**
     * Introspects the Throwable to obtain the cause.
     * */
    public static String getCauseMessage(Throwable t) {
        String defaultMessage = t.getMessage();
        Throwable cause = cause(t);
        if (cause != null) {
            defaultMessage = cause.getMessage();
        }
        return defaultMessage;
    }
}
