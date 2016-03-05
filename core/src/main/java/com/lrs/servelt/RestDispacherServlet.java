/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.servelt;

import com.lrs.mvc.DispatcherManager;
import com.lrs.mvc.Request;
import com.lrs.mvc.Response;
import com.lrs.mapping.UrlMapping;
import com.lrs.mapping.UrlMappingsHolder;
import com.lrs.rest.exception.RestException;
import com.lrs.utils.RequestUtils;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;


/**
 *
 * @author fcambarieri
 */
public class RestDispacherServlet extends HttpServlet{

    private Log log = LogFactory.getLog(RestDispacherServlet.class);

    @Inject DispatcherManager dispatcherManager;
    @Inject private UrlMappingsHolder holder;
    
    @Override
    public void init(final ServletConfig config) throws ServletException {
        
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, Methods.DELETE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, Methods.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, Methods.POST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, Methods.PUT);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doService(req, resp, Methods.OPTIONS);
    }

    private final ObjectMapper mapper = JsonFactory.create();
    
    public void doService(HttpServletRequest request, HttpServletResponse response, HttpString method) throws ServletException, IOException  {
        long start = System.currentTimeMillis();

        try {
            String uri = request.getRequestURI();

            if (uri.equals("/favicon.ico")) {
                return;
            }

            UrlMapping urlMapping = holder.urlMapping(uri);
            if (urlMapping == null) {
                log.warn(String.format("Uri %s not found", uri));
                throw new RestException(String.format("URI %s not found", uri), RestException.HttpCodeError.NOT_FOUND);
            }

            String action = urlMapping.getActions().get(method);
            if (action == null) {
                log.warn(String.format("Uri %s not found for method %", uri, method.toString()));
                throw new RestException(String.format("Bad request method %s", method.toString()), RestException.HttpCodeError.BAD_REQUEST);
            }

            Map params = buildParams(request, urlMapping);

            dispatcherManager.dispatch(new Request(request, params), new Response(response), urlMapping.getControllerName(), action);
            //dispatcherManager.dispatch(urlMapping.getControllerName(), action, params, body);

        } finally {
            long end = System.currentTimeMillis() - start;
            response.setHeader("X-Time", String.valueOf(end));
        }

    }

    public Map buildParams(HttpServletRequest request, UrlMapping mapping) {
        Map params = new LinkedHashMap<>();
        params.putAll(RequestUtils.buildParams(request));
        params.putAll(mapping.getParamas());
        return params;
    }
}
