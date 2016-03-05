/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.container;

import com.lrs.config.ApplicationConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fcambarieri
 */
public abstract class BaseApplicationBuilder implements ApplicationBuilder {

  private static final Log log = LogFactory.getLog(BaseApplicationBuilder.class.getName());

  private ApplicationConfig config;

  public BaseApplicationBuilder() {

  }

  public BaseApplicationBuilder(ApplicationConfig config) {
    this.config = config;
  }

  @Override
  public ApplicationBuilder addConfig(ApplicationConfig config) {
    return createBuilder(config);
  }

  @Override
  public Application build() {
    Application application = null;
    try {
      application = getApplicationClass().newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      log.fatal("LightWeightServer:application_builder[status=failed]", e);
    }
    if (application != null) {
      application.initializeConfig(config);
    }
    return application;
  }

  public abstract ApplicationBuilder createBuilder(ApplicationConfig config);

  public abstract Class<? extends Application> getApplicationClass();

}
