package com.pulsar.framework.handle;

import com.google.gson.Gson;
import com.pulsar.framework.constant.MediaType;
import com.pulsar.framework.core.TemplateView;
import com.pulsar.framework.core.URLMapping;
import com.pulsar.framework.core.reflection.BeanPopulate;
import com.pulsar.framework.helper.HttpResponseContext;
import com.pulsar.framework.helper.RequestContext;
import com.pulsar.framework.helper.ResponseContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Johnny Richard
 */
public class WebApplicationController implements ApplicationController {

    public void init() {
    }

    public ResponseContext handleRequest(RequestContext requestContext) {
        ResponseContext responseContext = new HttpResponseContext();

        Gson gson = new Gson();
        try {

            URLMapping urlMapping = requestContext.getURLMapping();

            Object controllerObject = new ControllerFactory().getController(urlMapping);

            injectFields(controllerObject, urlMapping.getClasse(), requestContext);

            Method method = urlMapping.getMethod();

            List parameterArgs = new ArrayList();
            Class<?>[] parameterTypes = method.getParameterTypes();

            Consumes consumes = (Consumes) method.getAnnotation(Consumes.class);

            if (consumes != null) {
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("Only one parameter is required on method " + method.getName());
                }
                Class classe = parameterTypes[0];
                if (consumes.value().equals(MediaType.APPLICATION_JSON)) {
                    parameterArgs.add(gson.fromJson(requestContext.getContentBody(), classe));
                } else {
                    throw new RuntimeException("MediaType " + consumes.value() + " not suported.");
                }
            } else {

                BeanPopulate populate = new BeanPopulate(requestContext);
                int annotationsCount = 0;
                primary:
                for (Annotation[] annotations : method.getParameterAnnotations()) {
                    Class<?> parameterType = parameterTypes[annotationsCount++];
                    for (int i = 0; i < annotations.length; i++) {
                        Annotation annotation = annotations[i];

                        if (annotation instanceof QueryParam) {
                            String paramKey = ((QueryParam) annotation).value();
                            parameterArgs.add(requestContext.getParameter(parameterType, paramKey));
                            continue primary;

                        } else if (annotation instanceof RouteParam) {
                            String paramKey = ((RouteParam) annotation).value();
                            parameterArgs.add(requestContext.getRouteParam(parameterType, paramKey));
                            continue primary;
                        } else if (annotation instanceof RootName) {
                            String rootName = ((RootName) annotation).value();
                            parameterArgs.add(populate.getBeanWithRootName(parameterType, rootName));
                            continue primary;
                        } else {
                            parameterArgs.add(null);
                            continue primary;
                        }
                    }
                }
            }

            Produces produces = method.getAnnotation(Produces.class);

            if (produces != null) {
                responseContext.setProduces(produces.value());
            }
            responseContext.setMethodResponse(method.invoke(controllerObject, parameterArgs.toArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return responseContext;
    }

    public void handleResponse(RequestContext requestContext, ResponseContext responseContext) {
        if (!responseContext.isCommitted()) {
            if (responseContext.isProduces()) {
                responseContext.writeBody();
            } else if (responseContext.isRedirect()) {
                responseContext.redirect();
            } else {
                requestContext.dispatcher(responseContext, responseContext.getView());
            }
        }
    }

    public void injectFields(Object controllerObject, Class classe, RequestContext requestContext) throws IllegalAccessException {
        HttpServletRequest request = (HttpServletRequest) requestContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) requestContext.getResponse();
        HttpSession session = request.getSession();
        ServletContext context = request.getServletContext();

        for (Field field : classe.getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> classType = field.getType();
            if (classType.equals(HttpServletRequest.class)) {
                field.set(controllerObject, request);
                continue;
            } else if (classType.equals(HttpServletResponse.class)) {
                field.set(controllerObject, response);
                continue;
            } else if (classType.equals(HttpSession.class)) {
                field.set(controllerObject, session);
                continue;
            } else if (classType.equals(ServletContext.class)) {
                field.set(controllerObject, context);
                continue;
            } else if (classType.equals(TemplateView.class)) {
                field.set(controllerObject, new TemplateView(requestContext));
            }
            Attribute attribute = (Attribute) field.getAnnotation(Attribute.class);
            if (attribute != null) {
                Object value = request.getAttribute(field.getName());
                if (value != null) {
                    field.set(controllerObject, value);
                    continue;
                }
                value = session.getAttribute(field.getName());
                if (value != null) {
                    field.set(controllerObject, value);
                    continue;
                }
                value = context.getAttribute(field.getName());
                if (value != null) {
                    field.set(controllerObject, value);
                    continue;
                }
            }
        }
    }

    public void destroy() {
    }
}
