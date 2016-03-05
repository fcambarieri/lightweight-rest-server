/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.web.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 *
 * @author fcambarieri
 */
public class CdiServletRequestListener implements ServletRequestListener 
{ 
    private static final Logger LOG = Logger.getLogger(CdiServletRequestListener.class.getName()); 
    // private static final String CDI_REQ_CONTEXT = "cdiRequestContext"; 
 
    @Override 
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) 
    { 
        LOG.log(Level.FINER,"Request done."); 
        // context = container.getBeanManager().getContext(RequestScoped.class);   
    } 
 
    @Override 
    public void requestInitialized(ServletRequestEvent servletRequestEvent) 
    { 
        LOG.log(Level.FINER,"Incoming request."); 
//        servletRequestEvent.getServletRequest().setAttribute(CDI_REQ_CONTEXT, contextControl); 
//        contextControl.startContext(RequestScoped.class); 
    } 
 
}