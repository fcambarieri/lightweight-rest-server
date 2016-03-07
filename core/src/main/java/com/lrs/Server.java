/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs;

import com.lrs.config.ApplicationConfig;
import com.lrs.config.BootStrap;
import com.lrs.config.Options;
import com.lrs.config.ServerConfig;
import com.lrs.container.Application;
import com.lrs.container.ApplicationBuilder;
import com.lrs.container.ApplicationBuilderFactory;
import com.lrs.enviroment.Environment;
import com.lrs.web.listeners.CdiServletRequestListener;
import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * @author fcambarieri
 */
public class Server {

    private static final Log logger = LogFactory.getLog(Server.class.getName());
    private Undertow server;
    private Weld weld;
    private WeldContainer container;
    private ServerConfig config;
    private List<Application> applications;

    /**
     * @param args
     * */
    public static void main(String[] args) {
        Server server = new Server(args);
        try {
            server.start();
        } catch (Throwable t) {
            logger.error("LightWeightServer:server:start[status=failed]", t);
            server.stop();
        }
    }


    /**
     * @param args
     * @see this#main(String[])
     * */
    public Server(String[] args) {
        initialize(args);
    }

    /**
     * @param args
     * @see this#main(String[])
     * */
    public void initialize(String[] args) {
        Options options = new Options(args);
        config = createConfig(options);
    }

    public ServerConfig createConfig(Options options) {
        logger.info("LightWeightServer:server:load_configuration[status=in_progress]");
        System.setProperty("org.jboss.weld.se.archive.isolation", "false");
        ServerConfig serverConfig = new ServerConfig(options);
        try {
            serverConfig.load();
            logger.info("LightWeightServer:server:load_configuration[status=complete, root=" + serverConfig.getRootPath() + "]");
        } catch (Throwable t) {
            logger.fatal("LightWeightServer:server:load_configuration[status=failed]", t);
            serverConfig = null;
        }
        return serverConfig;
    }

    public void start() throws Exception {
        logger.info("LightWeightServer:server:start[status=in_progress]");

        logger.info("LightWeightServer:server:load_cdi_container[status=in_progress]");
        weld = new Weld();
        container = weld.initialize();
        logger.info("LightWeightServer:server:load_cdi_container[status=complete]");

        Environment env = Environment.getCurrentEnvironment();
        logger.info(String.format("LightWeightServer:server:[enviroment=%s]", env.getName()));


        applications = loadApplicationDefinitions(config);


        logger.info("LightWeightServer:server:config_app_server[status=in_progress]");

        Undertow.Builder builder = Undertow.builder();
        builder.addHttpListener(config.getPort(), config.getHost());
        builder.setIoThreads(config.getIoThreads());
        builder.setWorkerThreads(config.getWorkerThreads());

        logger.info(String.format("LightWeightServer:server:config_app_server[port:%d, io_threads: %d, workers:%d]", config.getPort(), config.getIoThreads(), config.getWorkerThreads()));

        logger.info("LightWeightServer:server:config_app_server[status=complete]");

        List<DeploymentManager> managers = deployApplications(applications);
        for (DeploymentManager deploymentManager : managers) {
            builder.setHandler(deploymentManager.start());
        }

        server = builder.build();

        initializeBootstraps();

        server.start();
        logger.info("LightWeightServer:server:http[port=" + config.getPort() + ",host=" + config.getHost() + "]");
        logger.info("LightWeightServer:server:start[status=complete]");
    }

    /**
     * Setup the deployment managers for a given set of applications
     *
     * @param apps
     * @return list of deployment managers
     */
    public List<DeploymentManager> deployApplications(List<Application> apps) {
        List<DeploymentManager> deployManagers = new ArrayList<DeploymentManager>();
        for (Application app : apps) {

            DeploymentInfo deployInfo = app.getDeploymentInfo();

            logger.debug(String.format("LightWeightServer:server:app_deploy_%s[status=in_processs]", app.getClass().getSimpleName()));

            // Add default listeners
            ListenerInfo cdiListener = Servlets.listener(CdiServletRequestListener.class);
            deployInfo.addListener(cdiListener);

            DeploymentManager deploymentManager = Servlets.defaultContainer().addDeployment(deployInfo);
            try {
                deploymentManager.deploy();
                deployManagers.add(deploymentManager);

                logger.debug(String.format("LightWeightServer:server:app_deploy_%s[status=in_processs]", app.getClass().getSimpleName()));
            } catch (Exception e) {
                logger.error("LightWeightServer:server:start[status=app_deploy_fail,app_type=" + app.getClass().getSimpleName() + "]", e);
            }
        }
        return deployManagers;
    }

    /**
     * Loads up the application configuration for an application
     *
     * @param config
     * @return list of applications
     */
    public List<Application> loadApplicationDefinitions(ServerConfig config) {
        logger.info("LightWeightServer:server:load_app_definitions[status=in_progress]");
        ApplicationBuilderFactory factory = container.instance().select(ApplicationBuilderFactory.class).get();
        List<Application> apps = new ArrayList<Application>();
        List<ApplicationConfig> appConfigs = config.getApplicationConfigs();
        appConfigs.stream().forEach((appConfig) -> {
            ApplicationBuilder appBuilder = factory.getBuilder(appConfig.getType());
            Application app = appBuilder.addConfig(appConfig).build();
            // TODO add some debugging if app isn't able to be setup
            if (app != null) {
                logger.info("LightWeightServer:server:loaded_app_definition[app=" + appConfig.getName() + ", type=" + appConfig.getType() + "]");
                apps.add(app);
            }
        });
        logger.info("LightWeightServer:server:load_app_definitions[status=complete]");
        return apps;
    }

    public void stop() {
        logger.warn("LightWeightServer:server:stop[status=in_progress]");

        destroyBootstraps();

        if (server != null) {
            server.stop();
        }

        if (weld != null) {
            weld.shutdown();
        }
        logger.info("LightWeightServer:server:stop[status=complete]");
    }

    public  void initializeBootstraps() {
        logger.warn("LightWeightServer:server:initializeBootstraps[status=in_progress]");
        Iterator<BootStrap> bootStrapIterator = container.instance().select(BootStrap.class).iterator();
        while (bootStrapIterator.hasNext()) {
            bootStrapIterator.next().init();
        }
        logger.warn("LightWeightServer:server:initializeBootstraps[status=complete]");
    }

    public  void destroyBootstraps() {
        logger.warn("LightWeightServer:server:destroyBootstraps[status=in_progress]");
        Iterator<BootStrap> bootStrapIterator = container.instance().select(BootStrap.class).iterator();
        if (bootStrapIterator != null) {
            while (bootStrapIterator.hasNext()) {
                bootStrapIterator.next().destroy();
            }
        }
        logger.warn("LightWeightServer:server:destroyBootstraps[status=complete]");
    }

}
