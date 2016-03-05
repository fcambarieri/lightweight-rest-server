/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.enviroment;

import java.util.Map;
import java.util.Properties;

/**
 *
 * @author fcambarieri
 */
public interface Config {
    
    /**
     * @return The flat version of the config
     */
    Map<String, Object> flatten();

    /**
     * Converts the config to properties
     *
     * @return The properties
     */
    Properties toProperties();

    /**
     * Merge another config and return this config
     * @param toMerge The map to merge
     * @return This config
     */
    Config merge(Map<String,Object> toMerge);

}
