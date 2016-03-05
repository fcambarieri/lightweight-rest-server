/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.mapping;

import java.util.List;

/**
 *
 * @author fcambarieri
 */
public interface UrlMappingFactory {
    
    List<UrlMapping> createMapping();
}
