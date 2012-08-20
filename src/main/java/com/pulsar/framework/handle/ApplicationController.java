package com.pulsar.framework.handle;

import com.pulsar.framework.helper.RequestContext;
import com.pulsar.framework.helper.ResponseContext;

/**
 *
 * @author Johnny Richard
 */
public interface ApplicationController {
    public void init();
    public ResponseContext handleRequest(RequestContext request);
    public void handleResponse(RequestContext request, ResponseContext response);
    public void destroy();
}
