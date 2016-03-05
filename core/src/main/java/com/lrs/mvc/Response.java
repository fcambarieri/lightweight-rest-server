/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mvc;

import com.lrs.utils.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fcambarieri
 * @param <T>
 */
public class Response<T> {

    Map<String, String> headers;
    String contentType;
    int httpCode;
    T body;
    HttpServletResponse response;
    private boolean sent = false;

    public Response() {
    }

    public Response(HttpServletResponse response) {
        Assert.notNull(response, "response must not be null");
        this.response = response;
    }

    public Response(int httpCode, T body) {
        this.httpCode = httpCode;
        this.body = body;
    }

    public void setBody(T body) {
        this.body = body;;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public T getBody() {
        return body;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static ErrorBuilder creatErrorBuilder() {
        return new ErrorBuilder();
    }

    public Response setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getContentType() {
        return contentType;
    }


    public boolean hasDelegatedSetted() {
        return response != null;
    }
    public void send() throws IOException {
        if (sent) {
            return;
        }

        Assert.notNull(response, "response is null");
        Assert.notNull(httpCode, "status is null");
        response.setStatus(httpCode);
        response.setHeader("Content-Type", getContentType());
        if (httpCode != 204) {
            Assert.notNull(body, "response is null");
            response.getWriter().write(body.toString());
        }
        sent = true;
    }

    public static class Builder<T> {

        private Response<T> response;

        public Builder() {
            this.response = new Response();
        }

        public Builder setBody(T body) {
            this.response.setBody(body);
            return this;
        }

        public Builder setHttpCode(int httpCode) {
            this.response.setHttpCode(httpCode);
            return this;
        }

        public Builder setContentType(String contentType) {
            this.response.setContentType(contentType);
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.response.setHeaders(headers);
            return this;
        }

        public T getBody() {
            return this.response.getBody();
        }

        public Response build() {
            return this.response;
        }

    }

    public static class ErrorBuilder extends Builder<Map> {

        void createErrorMap(String message, String error, Throwable cause) {

            checkMap();

            getBody().put("message", message);
            getBody().put("error", error);
            getBody().put("cause", cause != null ? cause.getStackTrace() : Collections.EMPTY_LIST);
        }

        public ErrorBuilder setError(String message) {
            checkMap();
            getBody().put("message", message);
            return this;
        }

        public ErrorBuilder setMessage(String message) {
            checkMap();
            getBody().put("message", message);
            return this;
        }

        public ErrorBuilder setCause(Throwable cause) {
            checkMap();
            getBody().put("cause", cause != null ? cause.getStackTrace() : Collections.EMPTY_LIST);
            return this;
        }

        void checkMap() {
            if (getBody() == null) {
                setBody(new HashMap<>());
            }
        }



    }

}


