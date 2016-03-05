/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mvc;

import com.lrs.context.ApplicationContext;
import com.lrs.mvc.annotation.After;
import com.lrs.mvc.annotation.Before;
import com.lrs.utils.Assert;
import com.lrs.utils.ClassUtils;
import com.lrs.utils.ReflectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author fcambarieri
 */
@Named("dispatcherManager")
public class DispatcherManager {


    Log log = LogFactory.getLog(DispatcherManager.class);


    @PostConstruct
    public void init() {
        //ApplicationContext.context.
    }

    public void dispatch(Request request, Response response, String controllerBean, String action)  {

        Object controller = ApplicationContext.context.getBean(controllerBean);

        Assert.notNull(controller, String.format("ControllerBean %s not found", controllerBean));

        initVariables(controller, request, response);

        Method method = ReflectionUtils.findMethod(controller.getClass(), action);

        if (method != null) {

            log.debug(String.format("Invoking Method[start] %s.%s ", controllerBean, method.getName()));

            handleInterceptor(controller, Before.class);

            Object result = ReflectionUtils.invokeMethod(method, controller);

            handleInterceptor(controller, After.class);

            log.debug(String.format("Invoking Method[end] %s.%s ", controllerBean, method.getName()));

            handleResponse(response, result);

            return;
        }

        Field requestHandle = ReflectionUtils.findField(controller.getClass(), action, RequestHandle.class);
        if (requestHandle != null) {
            try {
                requestHandle.setAccessible(true);
                log.debug(String.format("Invoking Attribute[start] %s.%s ", controllerBean, requestHandle.getName()));

                handleInterceptor(controller, Before.class);

                RequestHandle handler = (RequestHandle)requestHandle.get(controller);

                handleInterceptor(controller, After.class);

                log.debug(String.format("Invoking Attribute[end] %s.%s ", controllerBean, requestHandle.getName()));

                Response resp =  handler.handle(request);

                handleResponse(response, resp);
                return;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        throw new IllegalArgumentException(String.format("ControllerBean %s has not field or method named %", action));

    }

    public void handleInterceptor(Object controller, Class<? extends Annotation> annotationClass) {
        Method method = ReflectionUtils.findMethod(controller.getClass(), annotationClass);
        if (method != null) {
            method.setAccessible(true);
            log.debug(String.format("Invoking Interceptor[start] %s Method: %s ", annotationClass.getName(), method.getName()));
            ReflectionUtils.invokeMethod(method, controller);
            log.debug(String.format("Invoking Interceptor[end] %s Method: %s ", annotationClass.getName(), method.getName()));
        }
    }

    private void handleResponse(Response response, Object result) {
        if (result != null) {
            log.debug("Handling response[start]");
            if (ClassUtils.isAssignableValue(Response.class, result)) {
                Response resp = (Response)result;
                if (resp.hasDelegatedSetted()) {
                    send(resp);
                } else {
                    response.setHttpCode(resp.getHttpCode());
                    response.setBody(resp.getBody());
                    send(response);
                }
            }
            log.debug("Handling response[end]");
        }
    }

    private void send(Response response) {
        try {
            Assert.notNull(response, "response is null");
            response.send();
        } catch (IOException e) {
            log.error("Error executing response", e);
            throw new RuntimeException("Error sending response", e);
        }
    }

    private void initVariables(Object object, Request request, Response response) {

        log.debug(String.format("initVariables[start]"));

        Field field = ReflectionUtils.findField(object.getClass(), "request");
        if (field != null) {
            ReflectionUtils.setField(field, object, request);
            log.debug(String.format("Setting fied:%s with request", field.getName()));
        }

        Field fieldResponse = ReflectionUtils.findField(object.getClass(), "response");
        if (field != null) {
            ReflectionUtils.setField(fieldResponse, object, response);
            log.debug(String.format("Setting fied:%s with response", fieldResponse.getName()));
        }

        Field fieldParams = ReflectionUtils.findField(object.getClass(), "params");
        if (field != null) {
            ReflectionUtils.setField(fieldParams, object, request.getParams());
            log.debug(String.format("Setting field: %s with response", fieldParams.getName()));
        }

        log.debug(String.format("initVariables[end]"));
    }


}
