/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.container;

import java.util.Iterator;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jboss.weld.literal.NamedLiteral;

/**
 *
 * @author fcambarieri
 */
public class ApplicationBuilderFactory {

  @Inject
  @Any
  Instance<ApplicationBuilder> builder;

  public ApplicationBuilder getBuilder(String appType) {
    appType = new StringBuilder(appType).append("_builder").toString();
    return builder.select(new NamedLiteral(appType)).get();
  }

  public Iterator<ApplicationBuilder> list() {
    return builder.iterator();
  }
}
