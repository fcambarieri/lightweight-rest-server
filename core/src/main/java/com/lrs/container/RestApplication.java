/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.container;

import com.lrs.config.ApplicationConfig;
import com.lrs.servelt.RestDispacherServlet;
import com.lrs.servelt.RestExceptionHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ListenerInfo;
import org.jboss.weld.environment.servlet.Listener;

import javax.inject.Named;

/**
 *
 * @author fcambarieri
 */
@Named(value = "MainRestApp")
public class RestApplication implements Application {

  private ApplicationConfig config;

  @Override
  public DeploymentInfo getDeploymentInfo() {
    ListenerInfo listenerInfo = Servlets.listener(Listener.class);

    DeploymentInfo servletBuilder = Servlets.deployment()
            .setClassLoader(ClassLoader.getSystemClassLoader())
            .setContextPath("/")
            .setDeploymentName("RestDispacherServlet")
            .setResourceManager(new ClassPathResourceManager(ClassLoader.getSystemClassLoader()))
            .addServlets(
                    Servlets.servlet("RestDispacherServlet", RestDispacherServlet.class)
                    .addMapping("/*")
            )
            .addListener(listenerInfo);

    servletBuilder.setExceptionHandler(new RestExceptionHandler());

    return servletBuilder;
  }

  @Override
  public void initializeConfig(ApplicationConfig config) {
    this.config = config;
  }

}
