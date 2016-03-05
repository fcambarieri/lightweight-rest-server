/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.config;

import java.util.Map;

/**
 *
 * @author fcambarieri
 */
public class ApplicationConfig {

  private String name;
  private String rootUrlPath = "/";
  private String appFile = "app_config.yml";
  private ServerConfig serverConfig;
    private String type;

  private String rootPath;

  public ApplicationConfig() {
  }

  public ApplicationConfig(ServerConfig serverConfig) {
    this.serverConfig = serverConfig;
  }

  public String getRootPath() {
    return rootPath;
  }

  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  public String getAppFile() {
    return appFile;
  }

  public void setAppFile(String appFile) {
    this.appFile = appFile;
  }

  public String getRootUrlPath() {
    return rootUrlPath;
  }

  public void setRootUrlPath(String rootUrlPath) {
    this.rootUrlPath = rootUrlPath;
  }

  public String getName() {
    return name;
  }

  public void setName(String appName) {
    this.name = appName;
  }

  public ServerConfig getServerConfig() {
    return this.serverConfig;
  }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
