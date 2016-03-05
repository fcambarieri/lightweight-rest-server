/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mvc;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fcambarieri
 */
public abstract class AbstractController implements Controller {

    Map<String, Object> params;
    ControllerInterceptor interceptor;
    Log log = LogFactory.getLog("com.lrs.controller");
    
    @Override
    public void initialize() {
        
    }

    @Override
    public Object invoke(Object controller, String action) throws Throwable {
        String prefix = String.format("%s.%s", controller, action);
        if (interceptor != null) {
            log.debug(String.format("%s before intercept", prefix));
            interceptor.beforeIntercept();
        }


        
        
        if (interceptor != null) {
            log.debug(String.format("%s after intercept", prefix));
            interceptor.afterIntercept();
        }
        
        return null;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void setInterceptor(ControllerInterceptor ci) {
        this.interceptor = ci;
    }
    
    public void writeReponse(Response<String> response) {
        
    }
    
    
}
