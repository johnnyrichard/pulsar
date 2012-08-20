package com.pulsar.framework.helper;

import com.google.gson.Gson;
import com.pulsar.framework.constant.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Johnny Richard
 */
public class HttpResponseContext implements ResponseContext {

    private Object methodResponse;
    private String mediaType;
    private HttpServletResponse response;
    private HttpServletRequest request;

    public void writeBody() {
        try {
            //PrintWriter writer = response.getWriter();
            ServletOutputStream out = response.getOutputStream();

            response.setContentType(mediaType);
            Gson gson = new Gson();
            if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                out.write(gson.toJson(methodResponse).getBytes());
            } else if (methodResponse instanceof InputStream) {
                InputStream is = (InputStream) methodResponse;
                byte[] outputByte = new byte[4096];
                //copy binary contect to output stream
                while (is.read(outputByte, 0, 4096) != -1) {
                    out.write(outputByte, 0, 4096);
                }
                is.close();
                out.flush();
                out.close();
            } else {
                out.write(methodResponse.toString().getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("Not is possible wirte body response: " + e.getMessage());
        }
    }

    public boolean isProduces() {
        return mediaType != null;
    }

    public String getProduces() {
        return mediaType;
    }

    public void setProduces(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getView() {
        Properties appConfig = (Properties) request.getServletContext().getAttribute("app-config");
        String view = methodResponse.toString();
        if (isRedirect() || view.split(":").length > 1) {
            return view.split(":")[1];
        } else {
            return appConfig.getProperty("view.prefix") + view + appConfig.getProperty("view.suffix");
        }
    }

    public void setMethodResponse(Object methodResponse) {
        this.methodResponse = methodResponse;
    }

    public void redirect() {
        try {
            response.sendRedirect(request.getContextPath() + "/" + getView());
        } catch (IOException ex) {
            Logger.getLogger(HttpResponseContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isRedirect() {
        String view = methodResponse.toString();
        return (view.split(":")[0].equals("redirect"));
    }

    public void setResponse(ServletResponse response) {
        this.response = (HttpServletResponse) response;
    }

    public ServletResponse getResponse() {
        return this.response;
    }

    public void setRequest(ServletRequest request) {
        this.request = (HttpServletRequest) request;
    }

    public ServletRequest getRequest() {
        return this.request;
    }

    public boolean isCommitted() {
        return this.response.isCommitted();
    }
}
