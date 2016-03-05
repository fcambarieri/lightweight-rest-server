package com.lrs.mvc;

import com.lrs.mvc.annotation.Before;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by fcambarieri on 03/03/16.
 */
@Named("ping")
@Singleton
public class PingController {

    private Log log = LogFactory.getLog(PingController.class);

    private static int id = 0;

    public PingController() {
        log.debug(String.format("creating ping %d", id));
        id++;
    }

    public RequestHandle pong = request-> {
       return Response.createBuilder().setBody("pong").setHttpCode(200).build();
    };

    public Response<String> renderPong() {
        return Response.createBuilder().setBody("pong").setHttpCode(200).build();
    }

}
