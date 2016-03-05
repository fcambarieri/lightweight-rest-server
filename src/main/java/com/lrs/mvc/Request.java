package com.lrs.mvc;

import com.lrs.rest.exception.RestException;
import com.lrs.utils.Assert;
import io.undertow.util.Methods;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fcambarieri on 02/03/16.
 */
public final class Request {

    HttpServletRequest delegated;

    final ObjectMapper mapper = JsonFactory.create();

    private Map params;

    public Request(HttpServletRequest request, Map params) {
        Assert.notNull(request, "Request cant be null");
        Assert.notNull(params, "params cant be null");
        this.delegated = request;
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        Enumeration<String> enumeration = delegated.getHeaderNames();
        Map<String, String> headers = new LinkedHashMap<>();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            headers.put(key, delegated.getHeader(key));
        }
        return headers;
    }

    public HttpServletRequest getDelegate() {
        return delegated;
    }

    public Map getJSON() {
        String method = delegated.getMethod();
        if (method.equals(Methods.POST.toString()) || method.equals(Methods.PUT.toString())) {
            /*strBody = delegated.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);*/
            try {
               Map body = (Map)mapper.fromJson(delegated.getReader());
                return body;
            } catch(Exception e) {
                throw new RestException("Error on parsing delegated body to json", e, RestException.HttpCodeError.BAD_REQUEST);
            }


        }
        return null;
    }

    public String getHeader(String name) {
        return delegated.getHeader(name);
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    /**
     * @return request cookies (or empty Map if cookies aren't present)
     */
    public Map<String, String> cookies() {
        Map<String, String> result = new HashMap<>();
        Cookie[] cookies = delegated.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                result.put(cookie.getName(), cookie.getValue());
            }
        }
        return result;
    }

    /**
     * Gets cookie by name.
     *
     * @param name name of the cookie
     * @return cookie value or null if the cookie was not found
     */
    public String cookie(String name) {
        Cookie[] cookies = delegated.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
