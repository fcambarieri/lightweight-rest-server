/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.config;

/**
 *
 * @author fcambarieri
 */
public interface BootStrap {

    /**
     * It's call after server creation with BeanManager already created
     * */
    void init ();

    /**
     * It's call after server before server it's destroy
     * */
    void destroy();
    
}
