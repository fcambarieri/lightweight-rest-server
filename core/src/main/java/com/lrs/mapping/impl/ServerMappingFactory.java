/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mapping.impl;

import com.lrs.mapping.*;
import io.undertow.util.Methods;

import java.util.Arrays;
import java.util.List;
import javax.inject.Named;

/**
 *
 * @author fcambarieri
 */
@Named
public class ServerMappingFactory extends AbstractMappingFactory/*implements UrlMappingFactory*/ {
    @Override
    protected void defineMapping() {
        GET("/ping", new Mapping("ping", "renderPong"));
        GET("/ping2", new Mapping("ping", "pong"));
    }

   /* @Override
    public List<UrlMapping> createMapping() {
        DefaultUrlMapping ping = DefaultUrlMapping.build()
                .addPattern("/ping")
                .addController("ping")
                .addAction(Methods.GET, "renderPong");

        DefaultUrlMapping ping2 = DefaultUrlMapping.build()
                .addPattern("/ping2")
                .addController("ping")
                .addAction(Methods.GET, "pong");
        
        return Arrays.asList(new DefaultUrlMapping[]{ ping, ping2 });
    }*/
    
}
