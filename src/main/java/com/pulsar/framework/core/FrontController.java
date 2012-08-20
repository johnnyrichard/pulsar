package com.pulsar.framework.core;

import com.pulsar.framework.annotation.Interceptors;
import com.pulsar.framework.helper.RequestContext;
import com.pulsar.framework.helper.RequestContextFactory;
import com.pulsar.framework.interceptor.Interceptor;
import com.pulsar.framework.interceptor.InterceptorManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Johnny Richard
 */
public class FrontController implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
    
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        if (isRequestingStaticFile(request)) {
            chain.doFilter(req, resp);
        } else {
            RequestContext requestContext = RequestContextFactory.getInstance()
                    .getRequestContext(req);
            
            URLMapping urlMapping = requestContext.getURLMapping();
            
            InterceptorManager manager = new InterceptorManager(request, response);
            Interceptors interceptors = (Interceptors) urlMapping.getClasse().getAnnotation(Interceptors.class);

            if (interceptors != null) {
                manager.interceptFilters(interceptors.value());
            } else {
                manager.interceptFilters(new Class[]{});
            }
        }
    }

    public boolean isRequestingStaticFile(HttpServletRequest request) throws ServletException {
        ServletContext context = request.getServletContext();
        try {
            URL resource = context.getResource(request.getServletPath());
            return resource != null && isFile(resource);
        } catch (MalformedURLException e) {
            throw new ServletException(e);
        }
    }

    public boolean isFile(URL resource) {
        return !resource.toString().endsWith("/");
    }

}