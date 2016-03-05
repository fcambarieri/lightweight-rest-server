/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mapping.impl;

import com.lrs.mapping.DefaultUrlMapping;
import com.lrs.mapping.UrlMapping;
import com.lrs.mapping.UrlMappingFactory;
import io.undertow.util.Methods;

import java.util.Arrays;
import java.util.List;
import javax.inject.Named;

/**
 *
 * @author fcambarieri
 */
@Named
public class ServerMappingFactory implements UrlMappingFactory {

    @Override
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
    }
    
}
