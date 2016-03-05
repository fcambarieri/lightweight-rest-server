/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.rest.exception;

/**
 *
 * @author fcambarieri
 */
public class RestException extends RuntimeException{

    private HttpCodeError httpCodeError;
    
    public RestException(String msg, HttpCodeError httpCodeError) {
        super(msg);
        this.httpCodeError = httpCodeError;
    }
    
    public RestException(String msg, Throwable ex, HttpCodeError httpCodeError) {
        super(msg, ex);
        this.httpCodeError = httpCodeError;
    }

    public HttpCodeError getHttpCodeError() {
        return httpCodeError;
    }
    
    
    
    public enum HttpCodeError {
        
        BAD_REQUEST(400, "bad request"),
        UNAUTHORIZED(400, "Unauthorized request"),
        FORBIDDEN(403, "Forbidden request"),
        NOT_FOUND(404, "not found"),
        CONFLICT(409, "conflict"),
        
        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
        SERVICE_UNAVAILABLE(500, "Service Unavailable");
        
        
        int code;
        private String errorCode;
        
        private HttpCodeError(int code, String errorCode) {
            this.code = code;
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public int getCode() {
            return code;
        }
    }
}
