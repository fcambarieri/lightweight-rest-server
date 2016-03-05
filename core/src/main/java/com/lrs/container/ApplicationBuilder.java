/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.container;

import com.lrs.config.ApplicationConfig;

/**
 *
 * @author fcambarieri
 */
public interface ApplicationBuilder {

  ApplicationBuilder addConfig(ApplicationConfig config);

  Application build();
}
