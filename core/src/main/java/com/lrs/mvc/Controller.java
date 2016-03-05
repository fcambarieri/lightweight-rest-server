/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mvc;

import java.util.Map;

/**
 *
 * @author fcambarieri
 */
public interface Controller {

    /**
     * Initialize the controller class
     */
    void initialize();
    
    
     /**
     * Invokes a controller action on the given controller instance
     *
     * @param controller The controller instance
     * @param action The action
     * @return The result of the action
     * @throws Throwable Thrown when an error occurs invoking the action
     */
    Object invoke(Object controller, String action) throws Throwable;
    
    
    /**
     * 
     * @return params
     */
    Map<String, Object> getParams();
    
    
    void setInterceptor(ControllerInterceptor ci);
    
}
