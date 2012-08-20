package com.pulsar.framework.helper;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Johnny Richard
 */
public class RequestContextFactory {
    
    private RequestContextFactory() {
    }
    
    public static RequestContextFactory getInstance() {
        return new RequestContextFactory();
    }
    
    public RequestContext getRequestContext(ServletRequest request) {
        if(request instanceof HttpServletRequest) {
            return new HttpRequestContext((HttpServletRequest)request);
        }
        return null;
    }
}
