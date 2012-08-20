package com.pulsar.framework.handle;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Johnny Richard
 */
public class ApplicationControllerFactory {
    
    private ApplicationControllerFactory() {
    }
    
    public static ApplicationControllerFactory getInstance() {
        return new ApplicationControllerFactory();
    }
    /**
     * 
     * @param type is constants alocate on ApplicationControllerFactory
     * @return implementation of ApplicationController specific
     */
    public ApplicationController getApplicationController(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return new WebApplicationController();
        }
        return null;
    }
}
