package com.pulsar.framework.helper;

import com.pulsar.framework.core.URLMapping;
import com.pulsar.framework.util.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.ConvertUtils;

/**
 *
 * @author Johnny Richard
 */
public class HttpRequestContext implements RequestContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private URLMapping urlMapping;

    public HttpRequestContext(HttpServletRequest request) {
        this.request = request;
    }

    public URLMapping getURLMapping() {
        if (urlMapping != null) return urlMapping;
        
        Map<String, URLMapping> URLMappings = (Map<String, URLMapping>) this.request.getServletContext().getAttribute("url-mapping");
        String servletPath = getServletPath();

        for (String url : URLMappings.keySet()) {
            if (url.equals(servletPath)) {
                return URLMappings.get(url);
            }
        }

        for (String url : URLMappings.keySet()) {
            String regex = url.replaceAll("\\{[a-zA-Z0-9-_]+\\}", "[a-zA-Z0-9-_]+");
            if (servletPath.matches(regex)) {
                urlMapping = URLMappings.get(url);
                break;
            }
        }

        if (urlMapping == null) {
            throw new RuntimeException("URL " + servletPath + " not found.");
        }

        return urlMapping;
    }

    public String getServletPath() {
        return request.getMethod() + ":" + request.getServletPath();
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public void dispatcher(ResponseContext response, String view) {
        try {
            RequestDispatcher rd = request.getRequestDispatcher(view);
            rd.forward(request, response.getResponse());
        } catch (ServletException ex) {
            Logger.getLogger(HttpRequestContext.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpRequestContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRequest(ServletRequest request) {
        this.request = (HttpServletRequest) request;
    }
  
    public String getContentBody() {
        String retorno = "";
        try {
            BufferedReader reader = request.getReader();
            String line;
            while((line = reader.readLine()) != null) {
                retorno += line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return retorno;
    }

    public String getParameter(String key) {
        return request.getParameter(key);
    }

    public Object getParameter(Class parameterType, String key) {
        return ConvertUtils.convert(request.getParameter(key), parameterType);
    }

    public Object getRouteParam(Class parameterType, String key) {
        String[] urlSplited = urlMapping.getUrlRoute().split("/");
        for (int i = 0; i < urlSplited.length; i++) {
            if (urlSplited[i].equals("{" + key + "}")) {
                String url = request.getServletPath();
                return ConvertUtils.convert(url.split("/")[i], parameterType);
            }
        }
        return null;
    }

    public ServletResponse getResponse() {
        return response;
    }

    public void setResponse(ServletResponse response) {
        this.response = (HttpServletResponse) response;
    }
}
