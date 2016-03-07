/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mapping;

import io.undertow.util.HttpString;
import java.util.Map;

/**
 *
 * @author fcambarieri
 */
public interface UrlMapping {


    /**
     *  @return uri pattern
     */
    String getPattern();
    
    /**
     * @return controller name
     */
    String getControllerName();

    /**
     * @return mapping HttpMethods to controller actions
     */
    Map<HttpString, String> getActions();


    /**
     * @return params from queryString and uri pattern
     * */
    Map getParamas();

    /**
     * @param params from queryString and uri pattern
     * */
    void setParams(Map params);


}
