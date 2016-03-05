/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mapping;

import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fcambarieri
 */
public final class DefaultUrlMapping implements UrlMapping {

    private String pattern;
    private String controllerName;
    private Map<HttpString, String> actions;
    private Map params;
    
    public DefaultUrlMapping() {
        
    }
    
    public DefaultUrlMapping(String pattern, String controllerName, Map<HttpString, String> actions) {
        this.pattern = pattern;
        this.controllerName = controllerName;
        this.actions = actions;
    }

    /**
     * @return the pattern
     */
    @Override
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the controllerName
     */
    @Override
    public String getControllerName() {
        return controllerName;
    }

    /**
     * @param controllerName the controllerName to set
     */
    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    /**
     * @return the actions
     */
    @Override
    public Map<HttpString, String> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(Map<HttpString, String> actions) {
        this.actions = actions;
    }
    
    public static DefaultUrlMapping build() {
        DefaultUrlMapping newOne = new DefaultUrlMapping();
        return newOne;
    }
    
    public DefaultUrlMapping addPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }
    
    public DefaultUrlMapping addController(String controller) {
        this.controllerName = controller;
        return this;
    }
    
    public DefaultUrlMapping addAction(HttpString http, String action) {
        if (this.actions == null) {
            this.actions = new HashMap<HttpString, String>();
        }
        this.actions.put(http, action);
        return this;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    @Override
    public Map getParamas() {
        return params;
    }
}
