/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.context;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;


/**
 *
 * @author fcambarieri
 */
public final class ApplicationContext {

    private BeanManager manager ;

    public ApplicationContext() {
            init();
    }

    private void init ()  {

        if (manager == null) {
            manager = CDI.current().getBeanManager();
        }

        if (manager == null) {
            throw new RuntimeException("Bean Manager is null");
        }
    }

    public static final ApplicationContext context = new ApplicationContext();


    public BeanManager getBeanManager() {
        return manager;
    }

    /*public <T> T getBean(Class<T> type) {
        Instance<T> instance = this.container.select(type);
        if (instance != null) {
            return instance.get();
        }
        return null;
    }*/
    public <T> T getBean(Class<T> type) {
        BeanManager bm = getBeanManager();
        Iterator<Bean<?>> it = bm.getBeans(type).iterator();
        if (it.hasNext()) {
            Bean<?> bean = it.next();
            CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
            Object o = bm.getReference(bean, bean.getBeanClass(), ctx); // could be inlined with return
            return (T)o;
        }
        return null;
    }

    public Object getBean(String name) {
        BeanManager bm = getBeanManager();
        Iterator<Bean<?>> it = bm.getBeans(name).iterator();
        if (it.hasNext()) {
            Bean<?> bean = it.next();
            CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
            Object o = bm.getReference(bean, bean.getBeanClass(), ctx); // could be inlined with return
            return o;
        }
        return null;
    }

    public <T> List<T> getBeans(Class<T> type) {
        BeanManager bm = getBeanManager();
        Iterator<Bean<?>> it = bm.getBeans(type).iterator();
        List list = new LinkedList();
        while(it.hasNext()) {
            Bean bean = it.next();
            CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
            Object o = bm.getReference(bean, bean.getBeanClass(), ctx); // could be inlined with return
            list.add(o);
        }
        return list;
    }


}
