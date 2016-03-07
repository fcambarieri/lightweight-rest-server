package com.lrs.mapping;

import io.undertow.util.HttpString;
import io.undertow.util.Methods;

import javax.ws.rs.HttpMethod;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fcambarieri on 06/03/16.
 */
public abstract class AbstractMappingFactory implements UrlMappingFactory {

    private List<UrlMapping> mappings = new LinkedList<>();

    @Override
    public List<UrlMapping> createMapping() {
        defineMapping();
        return mappings;
    }

    protected abstract void defineMapping();

    public void GET(String pattern, Mapping mapping) {
        add(pattern, mapping, Methods.GET);
    }

    public void POST(String pattern, Mapping mapping) {
        add(pattern, mapping, Methods.POST);
    }

    public void add(String pattern, Mapping mapping, HttpString method) {
        DefaultUrlMapping uriMap = DefaultUrlMapping.build()
                .addPattern(pattern)
                .addController(mapping.getController())
                .addAction(method, mapping.getAction());
        mappings.add(uriMap);
    }

    public void DELETE(String pattern, Mapping mapping) {
        add(pattern,mapping, Methods.DELETE);
    }

    public void PUT(String pattern, Mapping mapping) {
        add(pattern, mapping, Methods.PUT);
    }

    public void add(String pattern, Mapping... mappings) {

        for(Mapping map : mappings) {
            DefaultUrlMapping uriMap = DefaultUrlMapping.build()
                    .addPattern(pattern)
                    .addController(map.getController())
                    .addAction(map.getHttpMethod(), map.getAction());

            this.mappings.add(uriMap);
        }

    }
}
