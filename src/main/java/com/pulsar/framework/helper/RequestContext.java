package com.pulsar.framework.helper;

import com.pulsar.framework.core.URLMapping;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author Johnny Richard
 */
public interface RequestContext {
    public URLMapping getURLMapping();
    public String getServletPath();
    public void dispatcher(ResponseContext response, String view);
    public void setRequest(ServletRequest request);
    public String getContentBody();
    public ServletRequest getRequest();
    public ServletResponse getResponse();
    public void setResponse(ServletResponse response);
    public String getParameter(String key);
    public Object getParameter(Class parameterType, String key);
    public Object getRouteParam(Class parameterType, String key);
}
