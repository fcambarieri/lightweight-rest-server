/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



/**
 *
 * @author fcambarieri
 * @param <T>
 */
public class Holder<T> {
    
    private Map<Integer, T> instances = new ConcurrentHashMap<Integer, T>();
    private T singleton;
    private String name;
    
    public Holder(String name) {
        this.name = name;
    }

    public T get() {
        return get(false);
    }

    public T get(boolean mappedOnly) {
        T t = instances.get(getClassLoaderId());
        if (t != null) {
            return t;
        }

        t = lookupSecondary();
        if (t != null) {
            return t;
        }

//        t = instances.get(System.identityHashCode(getClass().getClassLoader()));
        if (t == null && !mappedOnly) {
            t = singleton;
        }
        return t;
    }

    protected T lookupSecondary() {
        // override in subclass if needed
        return null;
    }

    public void set(T t) {
        int id = getClassLoaderId();
        int thisClassLoaderId = System.identityHashCode(getClass().getClassLoader());
        if (t == null) {
            instances.remove(id);
            instances.remove(thisClassLoaderId);
        }
        else {
            instances.put(id, t);
            instances.put(thisClassLoaderId, t);
        }
        singleton = t;
    }

    private int getClassLoaderId() {
        return System.identityHashCode(Thread.currentThread().getContextClassLoader());
    }
}

