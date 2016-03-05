/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fcambarieri
 */
public class RequestUtils {
    
    /**
     * Transform queryString into params
     * @param req
     * @return Map of queryString values
     */
    public static Map<String, String> buildParams(HttpServletRequest req) {
        return buildParams(req.getQueryString());
    }
    
    /**
     * Transform queryString into params
     * @param req
     * @return Map of queryString values
     */
    public static Map<String, String> buildParams(String queryString) {
        Map<String, String> params = new HashMap<String, String>();
        if (queryString == null || queryString.isEmpty()) {
            return params;
        }
        
        for(String pares : queryString.split("&")) {
            String[] param = pares.split("=");
            if (param.length == 2) {
                params.put(param[0], param[1]);
            }
        }
        
        return params;
    }
    
    public static String getAdminId(HttpServletRequest req, Map<String, String> params) {
        String adminId = req.getHeader("X-Admin-Id");
        if (adminId == null) {
            adminId = params.get("admin.id");
        }
        return adminId;
    }
}
