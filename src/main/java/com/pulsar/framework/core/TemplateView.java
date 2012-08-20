package com.pulsar.framework.core;

import com.pulsar.framework.helper.RequestContext;

/**
 *
 * @author Johnny Richard
 */
public class TemplateView {
    private RequestContext request;
    
    public TemplateView(RequestContext request) {
        this.request = request;
    }
    
    public void add(String key, Object obj) {
        request.getRequest().setAttribute(key, obj);
    }
    
    public void remove(String key) {
        request.getRequest().removeAttribute(key);
    }
}
