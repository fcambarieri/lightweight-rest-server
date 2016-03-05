package com.lrs.mvc;

/**
 * Created by fcambarieri on 03/03/16.
 */
@FunctionalInterface
public interface RequestHandle {

    Response handle(Request request);
}
