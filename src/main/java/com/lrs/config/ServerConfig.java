/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.config;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * @author fcambarieri
 */
public class ServerConfig {

    private String rootPath;
    private int port = 8080;
    private String host = "0.0.0.0";
    private int ioThreads;
    private int workerThreads;

    private List<ApplicationConfig> applicationConfigs;

    private Options options;

    public ServerConfig(Options options) {
        this.options = options;
    }

    @SuppressWarnings("rawtypes")
    public void load() throws Exception {
        rootPath = options.getValue(Options.Key.ROOT_PATH);
        String configFile = options.getValue(Options.Key.CONFIG_FILE);
        if (rootPath == null) {
            rootPath = Paths.get("").toAbsolutePath().toString();
        }
        StringBuilder fileName = new StringBuilder(rootPath).append(File.separator).append(configFile);
        File file = new File(fileName.toString());
        FileInputStream fis = new FileInputStream(file);

        Yaml yaml = new Yaml();
        Map yamlResults = (Map) yaml.load(fis);

        Map serverConfig = (Map) yamlResults.get("server");
        host = serverConfig.get("host").toString();
        port = Integer.parseInt(serverConfig.get("port").toString());


        ioThreads = Math.max(Runtime.getRuntime().availableProcessors(), 2) * 2;
        workerThreads = ioThreads * 8;

        if (serverConfig.get("ioThreads") != null) {
            ioThreads = Integer.parseInt(serverConfig.get("ioThreads").toString());
        }


        if (serverConfig.get("workerThreads") != null) {
            workerThreads = Integer.parseInt(serverConfig.get("workerThreads").toString());
        }


        List apps = (List) yamlResults.get("apps");
        applicationConfigs = new ArrayList<ApplicationConfig>();
        for (Object app : apps) {
            Map appYaml = (Map) app;
            ApplicationConfig appConfig = new ApplicationConfig(this);
            appConfig.setName(appYaml.get("name").toString());
            appConfig.setRootUrlPath(appYaml.get("url_path").toString());
            appConfig.setAppFile(appYaml.get("app_file").toString());
            appConfig.setType(appYaml.get("type").toString());
            appConfig.setRootPath(rootPath);
            applicationConfigs.add(appConfig);
        }

    }

    public List<ApplicationConfig> getApplicationConfigs() {
        return applicationConfigs;
    }

    public void setApplicationConfigs(List<ApplicationConfig> applicationConfigs) {
        this.applicationConfigs = applicationConfigs;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        String envPort = System.getenv("PORT");

        if (envPort != null && "[0-9]+".matches(envPort)) {
            port = Integer.parseInt(envPort);
        }

        String paramPort = System.getProperty("port");

        if (paramPort != null && "\\d{4}".matches(paramPort)) {
            port = Integer.parseInt(paramPort);
        }


        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public int getIoThreads() {
        return ioThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }
}
