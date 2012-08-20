package com.pulsar.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Johnny Richard
 */
public class InterceptorManager {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public InterceptorManager(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void interceptFilters(Class<? extends Interceptor>[] interceptorsClass) {
        try {
            Interceptor[] interceptors = new Interceptor[interceptorsClass.length];

            for (int i = 0; i < interceptors.length; i++) {
                interceptors[i] = interceptorsClass[i].newInstance();
            }
            
            InterceptorChain chain = new InterceptorChain(interceptors);
            chain.next(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
