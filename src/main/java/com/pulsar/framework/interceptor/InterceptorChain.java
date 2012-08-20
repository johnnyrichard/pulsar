package com.pulsar.framework.interceptor;

import com.pulsar.framework.handle.ApplicationController;
import com.pulsar.framework.handle.ApplicationControllerFactory;
import com.pulsar.framework.helper.RequestContext;
import com.pulsar.framework.helper.RequestContextFactory;
import com.pulsar.framework.helper.ResponseContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Johnny Richard
 */
public class InterceptorChain {

    private List<Interceptor> interceptors;
    private int index = 0;

    public InterceptorChain() {
        interceptors = new ArrayList<Interceptor>();
    }

    public InterceptorChain(Interceptor[] array) {
        interceptors = Arrays.asList(array);
    }

    public void add(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    public void next(HttpServletRequest request, HttpServletResponse response) {
        if (index < interceptors.size()) {
            this.interceptors.get(index++).execute(request, response, this);
        } else {
            ApplicationControllerFactory acf = ApplicationControllerFactory.getInstance();
            ApplicationController applicationController = acf.getApplicationController(request);
            applicationController.init();

            RequestContextFactory requestContextFactory = RequestContextFactory.getInstance();
            RequestContext requestContext = requestContextFactory.getRequestContext(request);
            requestContext.setResponse(response);

            ResponseContext responseContext = applicationController.handleRequest(requestContext);
            responseContext.setResponse(response);
            responseContext.setRequest(request);
            applicationController.handleResponse(requestContext, responseContext);
            applicationController.destroy();
        }
    }
}
