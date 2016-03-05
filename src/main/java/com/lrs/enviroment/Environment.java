/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.enviroment;


import com.lrs.utils.CollectionUtils;
import com.lrs.utils.Holder;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author fcambarieri
 */
public enum Environment {

    /**
     * The development environment
     */
    DEVELOPMENT,
    /**
     * The production environment
     */
    PRODUCTION,
    /**
     * The test environment
     */
    TEST,
    /**
     * A custom environment
     */
    CUSTOM;

    public static final String INITIALIZING = "lrs.env.initializing";
    private static final String PRODUCTION_ENV_SHORT_NAME = "prod";
    private static final String DEVELOPMENT_ENVIRONMENT_SHORT_NAME = "dev";
    private static final String TEST_ENVIRONMENT_SHORT_NAME = "test";
    /**
     * Constant used to resolve the environment via
     * System.getProperty(Environment.KEY)
     */
    public static String KEY = "lrs.env";
    @SuppressWarnings("unchecked")
    private static Map<String, String> envNameMappings = CollectionUtils.<String, String>newMap(
            DEVELOPMENT_ENVIRONMENT_SHORT_NAME, Environment.DEVELOPMENT.getName(),
            PRODUCTION_ENV_SHORT_NAME, Environment.PRODUCTION.getName(),
            TEST_ENVIRONMENT_SHORT_NAME, Environment.TEST.getName());
    
    private static Holder<Environment> cachedCurrentEnvironment = new Holder<Environment>("Environment");
    private static final boolean DEVELOPMENT_MODE = getCurrent() == DEVELOPMENT;

    private static boolean initializingState = false;
    private String name;

    Environment() {
        initialize();
    }

    private void initialize() {
        name = toString().toLowerCase(Locale.ENGLISH);
    }

    /**
     * Returns the current environment which is typcally either DEVELOPMENT,
     * PRODUCTION or TEST. For custom environments CUSTOM type is returned.
     *
     * @return The current environment.
     */
    public static Environment getCurrent() {
        Environment current = cachedCurrentEnvironment.get();
        if (current != null) {
            return current;
        }

        return resolveCurrentEnvironment();
    }

    private static Environment resolveCurrentEnvironment() {
        String envName = System.getProperty(Environment.KEY);

        if (isBlank(envName)) {
            Metadata metadata = Metadata.getCurrent();
            if (metadata != null) {
                envName = metadata.getEnvironment();
            }
            if (isBlank(envName)) {
                return DEVELOPMENT;
            }
        }

        Environment env = getEnvironment(envName);
        if (env == null) {
            try {
                env = Environment.valueOf(envName.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }
        if (env == null) {
            env = Environment.CUSTOM;
            env.setName(envName);
        }
        return env;
    }

    public static void cacheCurrentEnvironment() {
        cachedCurrentEnvironment.set(resolveCurrentEnvironment());
    }

    /**
     * @see #getCurrent()
     * @return the current environment
     */
    public static Environment getCurrentEnvironment() {
        return getCurrent();
    }

    /**
     * Returns true if the application is running in development mode (within
     * grails run-app)
     *
     * @return true if the application is running in development mode
     */
    public static boolean isDevelopmentMode() {
        return DEVELOPMENT_MODE;
    }

    /**
     * @return Return true if the environment has been set as a System property
     */
    public static boolean isSystemSet() {
        return System.getProperty(KEY) != null;
    }

    /**
     * Returns the environment for the given short name
     *
     * @param shortName The short name
     * @return The Environment or null if not known
     */
    public static Environment getEnvironment(String shortName) {
        final String envName = envNameMappings.get(shortName);
        if (envName != null) {
            return Environment.valueOf(envName.toUpperCase());
        } else {
            try {
                return Environment.valueOf(shortName.toUpperCase());
            } catch (IllegalArgumentException ise) {
                return null;
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * @return the name of the environment
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return Whether interactive mode is enabled
     */
    public static boolean isInitializing() {
        return initializingState;
    }

    public static void setInitializing(boolean initializing) {
        initializingState = initializing;
        System.setProperty(INITIALIZING, String.valueOf(initializing));
    }
}
