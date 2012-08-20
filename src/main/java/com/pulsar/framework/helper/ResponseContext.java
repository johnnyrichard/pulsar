package com.pulsar.framework.helper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author Johnny Richard
 */
public interface ResponseContext {

    public boolean isProduces();
    public void setProduces(String mediaType);
    public String getProduces();
    public String getView();
    public void setMethodResponse(Object methodResponse);
    public void redirect();
    public boolean isRedirect();
    public void setResponse(ServletResponse response);
    public ServletResponse getResponse();
    public void writeBody();
    public ServletRequest getRequest();
    public void setRequest(ServletRequest request);
    public boolean isCommitted();
}
