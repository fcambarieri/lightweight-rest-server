/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.enviroment;


import com.lrs.utils.Holder;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fcambarieri
 */
public class Metadata implements Config {

    private static Holder<Reference<Metadata>> holder = new Holder<Reference<Metadata>>("Metadata");
    public static final String BUILD_INFO_FILE = "META-INF/MANIFEST.MF";
    public static final String FILE = "application.properties";
    public static final String APPLICATION_VERSION = "app.version";
    public static final String APPLICATION_NAME = "app.name";
    private File metadataFile;
    private Map<String, Object> delegateMap;

    private Metadata() {
        delegateMap = new LinkedHashMap();
        loadFromDefault();
    }

    private Metadata(File f) {
        metadataFile = f;
        delegateMap = new LinkedHashMap();
        loadFromFile(f);
    }

    private Metadata(InputStream inputStream) {
        delegateMap = new LinkedHashMap();
        loadFromInputStream(inputStream);
    }

    /*private Metadata(Map<String, String> properties) {
     getConfigMap().putAll(properties);
     afterLoading();
     }*/
    public File getMetadataFile() {
        return metadataFile;
    }

    /**
     * Resets the current state of the Metadata so it is re-read.
     */
    public static void reset() {
        Metadata m = getFromMap();
        if (m != null) {
            m.getConfigMap().clear();
            m.afterLoading();
        }
    }

    private void afterLoading() {
        Map map = new HashMap();
        // allow override via system properties
        map.putAll(System.getProperties());
        delegateMap.putAll(map);
        
    }

    /**
     * @return the metadata for the current application
     */
    public static Metadata getCurrent() {
        Metadata m = getFromMap();
        if (m == null) {
            m = new Metadata();
            holder.set(new SoftReference<Metadata>(m));
        }
        return m;
    }

    private void loadFromDefault() {
        InputStream input = null;
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            input = classLoader.getResourceAsStream(FILE);
            if (input == null) {
                input = classLoader.getResourceAsStream(FILE);
            }
            if (input != null) {
                loadProperties(input);
            }

            input = classLoader.getResourceAsStream(BUILD_INFO_FILE);
            if (input != null) {
                loadAndMerge(input);
            } else {
                // try WAR packaging resolve
                input = classLoader.getResourceAsStream("../../" + BUILD_INFO_FILE);
                if (input != null) {
                    loadAndMerge(input);
                }
            }
            afterLoading();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load application metadata:" + e.getMessage(), e);
        } finally {
            closeQuietly(input);
        }
    }

    private void loadAndMerge(InputStream input) {
        try {
            Properties props = new Properties();
            props.load(input);
            mergeMap(props, true);
        } catch (Throwable e) {
            // ignore
        }
    }

    private void loadFromInputStream(InputStream inputStream) {
        //loadYml(inputStream);
        afterLoading();
    }

    private void loadFromFile(File file) {
        if (file != null && file.exists()) {
            FileInputStream input = null;
            try {
                input = new FileInputStream(file);
                //loadYml(input);
                afterLoading();
            } catch (Exception e) {
                throw new RuntimeException("Cannot load application metadata:" + e.getMessage(), e);
            } finally {
                closeQuietly(input);
            }
        }
    }

    /**
     * Loads a Metadata instance from a Reader
     *
     * @param inputStream The InputStream
     * @return a Metadata instance
     */
    public static Metadata getInstance(InputStream inputStream) {
        Metadata m = new Metadata(inputStream);
        holder.set(new FinalReference<Metadata>(m));
        return m;
    }

    /**
     * Loads and returns a new Metadata object for the given File.
     *
     * @param file The File
     * @return A Metadata object
     */
    public static Metadata getInstance(File file) {
        Reference<Metadata> ref = holder.get();
        if (ref != null) {
            Metadata metadata = ref.get();
            if (metadata != null && metadata.getMetadataFile() != null && metadata.getMetadataFile().equals(file)) {
                return metadata;
            }
            createAndBindNew(file);
        }
        return createAndBindNew(file);
    }

    private static Metadata createAndBindNew(File file) {
        Metadata m = new Metadata(file);
        holder.set(new FinalReference<Metadata>(m));
        return m;
    }

    /**
     * Reloads the application metadata.
     *
     * @return The metadata object
     */
    public static Metadata reload() {
        File f = getCurrent().metadataFile;
        if (f != null && f.exists()) {
            return getInstance(f);
        }

        return f == null ? new Metadata() : new Metadata(f);
    }

    private static Metadata getFromMap() {
        Reference<Metadata> metadata = holder.get();
        return metadata == null ? null : metadata.get();
    }

    @Override
    public Map<String, Object> flatten() {
        return delegateMap;
    }

    @Override
    public Properties toProperties() {
        Properties prop = new Properties();
        prop.putAll(delegateMap);
        return prop;
    }

    @Override
    public Config merge(Map<String, Object> toMerge) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map getConfigMap() {
        return this.delegateMap;
    }

    private void loadProperties(InputStream input) {
        try {
            Properties prop = new Properties();
            prop.load(input);
            prop.forEach((k,v)-> delegateMap.put((String) k, v));
        } catch (IOException ex) {
            Logger.getLogger(Metadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void mergeMap(Map map, boolean f) {
        delegateMap.putAll(map);
    }

    static class FinalReference<T> extends SoftReference<T> {

        private T ref;

        public FinalReference(T t) {
            super(t);
            ref = t;
        }

        @Override
        public T get() {
            return ref;
        }
    }
    
    private static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            }
            catch (Exception ignored) {
                // ignored
            }
        }
    }
    
        /**
     * @return The environment the application expects to run in
     */
    public String getEnvironment() {
        return getProperty(Environment.KEY, String.class);
    }
    
    public Object getProperty(String name) {
        if (!delegateMap.containsKey(name)) {
            return new LinkedHashMap<Object, Object>();
        }
        return get(name);
    }
    
    public Object get(String name) {
        return delegateMap.get(name);
    }
    
     
    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        T  value = getProperty(key, targetType);
        if(value == null) {
            throw new IllegalStateException("Property " + key +" not found");
        }
        return value;
    }
    
    public <T> T getProperty(String name, Class<T> requiredType) {
        return convertToType(getProperty(name), requiredType );
    }
    
    protected <T> T convertToType(Object value, Class<T> requiredType) {
        if(value instanceof Map && ((Map)value).isEmpty()) {
            return null;
        }
        else if(requiredType.isInstance(value)) {
            return (T)value;
        }
        if(requiredType==String.class) {
            return (T) String.valueOf(value);
        } else if(requiredType==Boolean.class) {
            Boolean booleanObject = toBooleanObject(String.valueOf(value));
            return (T) (booleanObject != null ? booleanObject : Boolean.FALSE);
        } else if(requiredType==Integer.class) {
            if(value instanceof Number) {
                return (T) Integer.valueOf(((Number)value).intValue());
            } else {
                return (T) Integer.valueOf(String.valueOf(value));
            }
        } else if(requiredType==Long.class) {
            if(value instanceof Number) {
                return (T) Long.valueOf(((Number)value).longValue());
            } else {
                return (T) Long.valueOf(String.valueOf(value));
            }
        } else if(requiredType==Double.class) {
            if(value instanceof Number) {
                return (T) Double.valueOf(((Number)value).doubleValue());
            } else {
                return (T) Double.valueOf(String.valueOf(value));
            }
        } else if(requiredType==BigDecimal.class) {
            return (T) new BigDecimal(String.valueOf(value));
        } else {
            return convertToOtherTypes(value, requiredType);
        }
    }
    
    protected <T> T convertToOtherTypes(Object value, Class<T> requiredType) {
        throw new RuntimeException("conversion to $requiredType.name not implemented");
    }
    
    /**
     * toBooleanObject method ported from org.apache.commons.lang.BooleanUtils.toBooleanObject to Groovy code
     * @param str
     * @return
     */
    private static Boolean toBooleanObject(String str) {
        if (str.equals("true")) {
            return Boolean.TRUE;
        }
        if (str == null) {
            return null;
        }
        int strlen = str.length();
        if (strlen==0) {
            return null;
        } else if (strlen == 1) {
            char ch0 = str.charAt(0);
            if ((ch0 == 'y' || ch0 == 'Y') ||
                (ch0 == 't' || ch0 == 'T')) {
                return Boolean.TRUE;
            }
            if ((ch0 == 'n' || ch0 == 'N') ||
                (ch0 == 'f' || ch0 == 'F')) {
                return Boolean.FALSE;
            }
        } else if (strlen == 2) {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            if ((ch0 == 'o' || ch0 == 'O') &&
                (ch1 == 'n' || ch1 == 'N') ) {
                return Boolean.TRUE;
            }
            if ((ch0 == 'n' || ch0 == 'N') &&
                (ch1 == 'o' || ch1 == 'O') ) {
                return Boolean.FALSE;
            }
        } else if (strlen == 3) {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            if ((ch0 == 'y' || ch0 == 'Y') &&
                (ch1 == 'e' || ch1 == 'E') &&
                (ch2 == 's' || ch2 == 'S') ) {
                return Boolean.TRUE;
            }
            if ((ch0 == 'o' || ch0 == 'O') &&
                (ch1 == 'f' || ch1 == 'F') &&
                (ch2 == 'f' || ch2 == 'F') ) {
                return Boolean.FALSE;
            }
        } else if (strlen == 4) {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            char ch3 = str.charAt(3);
            if ((ch0 == 't' || ch0 == 'T') &&
                (ch1 == 'r' || ch1 == 'R') &&
                (ch2 == 'u' || ch2 == 'U') &&
                (ch3 == 'e' || ch3 == 'E') ) {
                return Boolean.TRUE;
            }
        } else if (strlen == 5) {
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            char ch3 = str.charAt(3);
            char ch4 = str.charAt(4);
            if ((ch0 == 'f' || ch0 == 'F') &&
                (ch1 == 'a' || ch1 == 'A') &&
                (ch2 == 'l' || ch2 == 'L') &&
                (ch3 == 's' || ch3 == 'S') &&
                (ch4 == 'e' || ch4 == 'E') ) {
                return Boolean.FALSE;
            }
        }
        return null;
    }
}
