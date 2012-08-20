package com.pulsar.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Johnny Richard
 */
public interface Interceptor {
    public void execute(HttpServletRequest request, HttpServletResponse response, InterceptorChain chain);
}
