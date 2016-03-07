package com.lrs.mapping;

import com.lrs.utils.Assert;
import io.undertow.util.HttpString;

import java.util.Map;

/**
 * Created by fcambarieri on 06/03/16.
 */

public final class Mapping {

    private String controller;
    private String action;
    private HttpString httpMethod;

    public Mapping(String controller, String action) {
        Assert.notNull(controller, "controller is null");
        Assert.notNull(action, "action is null");
        this.action = action;
        this.controller = controller;
    }

    public Mapping(String controller, String action, HttpString httpMethod) {
        Assert.notNull(controller, "controller is null");
        Assert.notNull(action, "action is null");
        Assert.notNull(httpMethod, "httpMethod is null");
        this.action = action;
        this.controller = controller;
        this.httpMethod = httpMethod;
    }


    public String getAction() {
        return action;
    }

    public String getController() {
        return controller;
    }

    public HttpString getHttpMethod() {
        return httpMethod;
    }
}
