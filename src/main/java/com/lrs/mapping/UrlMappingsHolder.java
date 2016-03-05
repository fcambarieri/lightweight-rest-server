/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mapping;

import com.lrs.context.ApplicationContext;
import com.lrs.mapping.router.MethodlessRouter;
import com.lrs.mapping.router.Routed;
import io.undertow.util.HttpString;
import java.util.Collections;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author fcambarieri
 */

@Named("urlMappingsHolder")
public class UrlMappingsHolder {

    private static final transient Log LOG = LogFactory.getLog(UrlMappingsHolder.class);

    /*private final LinkedHashMap<String, UrlMapping> urlMappings = new LinkedHashMap<String, UrlMapping>();
    private final LinkedHashMap<String, UrlMapping> litteralMappings = new LinkedHashMap<String, UrlMapping>();
    private final LinkedHashMap<Pattern, UrlMapping> compiledMapping = new LinkedHashMap<Pattern, UrlMapping>();
*/
    //@Inject private ApplicationContext applicationContext;
    private MethodlessRouter<UrlMapping> router = new MethodlessRouter<UrlMapping> ();

    
    public UrlMappingsHolder() {
    }

    UrlMappingsHolder add(UrlMapping mapping) {
        if (mapping != null) {
            router.pattern(mapping.getPattern(), mapping);
        }
        return this;
    }
    
    /*UrlMappingsHolder add(String pattern, String controller, Map<HttpString, String> actions) {
        this.urlMappings.put(pattern, new DefaultUrlMapping(pattern, controller, actions));
        return this;
    }*/

    @PostConstruct
    public void init () {
        LOG.debug("Initializing Mapping holder");
        List<UrlMappingFactory> mappingFactories =  ApplicationContext.context.getBeans(UrlMappingFactory.class);
        mappingFactories.stream().forEach((factory) -> {
            LOG.debug("Adding Mapping from -> " + factory.getClass().getName() );
            //urlMappings.putAll(factory.createMapping());
            factory.createMapping().forEach((mapping) -> {
                LOG.debug(String.format("UriRex: %s - Controller: %s actions:%s", mapping.getPattern(), mapping.getControllerName(), mapping.getActions().toString()));
                //add(mapping);
                router.pattern(mapping.getPattern(), mapping);
            });
            LOG.debug("End Mapping from -> " + factory.getClass().getName() );
        });
        
        buildRegEx();
        LOG.debug("All mapping were added!");
    }
    
    public void buildRegEx() {
        /*urlMappings.keySet().stream().forEach((sRegEx) -> {
            try {
                Pattern pattern = Pattern.compile(sRegEx);
                UrlMapping r = urlMappings.get(sRegEx);
                compiledMapping.put(pattern, r);
                
            } catch (Exception e) {
                litteralMappings.put(sRegEx, urlMappings.get(sRegEx));
            }
        });*/
    }
    
    public UrlMapping urlMapping(String uri) {
        /*for (Pattern pattern : compiledMapping.keySet()) {
            if(pattern.matcher(uri).matches()){
                return compiledMapping.get(pattern);
            }
        }
        return null;*/
        Routed<UrlMapping> routed = router.route(uri);
        if (routed != null) {
            UrlMapping mapping = routed.target();
            mapping.setParams(routed.params());
            return mapping;
        }

        return null;

    }
    

    
    

}
