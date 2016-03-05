/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.container;

import com.lrs.config.ApplicationConfig;
import io.undertow.servlet.api.DeploymentInfo;

/**
 *
 * @author fcambarieri
 */
public interface Application {

  /**
   * Get the deployment information for the type of app based on the server
   * configuration    *
   * @return deploymentInfo Deployment information for setting up undertow
   */
  DeploymentInfo getDeploymentInfo();

  void initializeConfig(ApplicationConfig config);
}
