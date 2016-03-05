package com.lrs.container;

import com.lrs.config.ApplicationConfig;

import javax.inject.Named;

/**
 * Created by fcambarieri on 27/02/16.
 */
@Named("rest_builder")
public class RestApplicationBuilder extends BaseApplicationBuilder{

    public RestApplicationBuilder() {

    }

    public RestApplicationBuilder(ApplicationConfig config) {
        super(config);
    }

    @Override
    public ApplicationBuilder createBuilder(ApplicationConfig config) {
        return new RestApplicationBuilder(config);
    }

    @Override
    public Class<? extends Application> getApplicationClass() {
        return RestApplication.class;
    }
}
