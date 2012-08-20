package com.pulsar.framework.listener;

import com.metapossum.utils.scanner.reflect.ClassesInPackageScanner;
import com.pulsar.framework.annotation.Controller;
import com.pulsar.framework.annotation.DELETE;
import com.pulsar.framework.annotation.GET;
import com.pulsar.framework.annotation.POST;
import com.pulsar.framework.annotation.PUT;
import com.pulsar.framework.annotation.Route;
import com.pulsar.framework.core.URLMapping;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Johnny Richard
 */
public class ContextStartListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent contextEvent) {
        try {
            ServletContext context = contextEvent.getServletContext();
            Properties appConfig = new Properties();
            appConfig.load(new FileInputStream(context.getRealPath("/WEB-INF/app-config.properties")));

            Map<String, URLMapping> urlMapping = new HashMap<String, URLMapping>();

            Set<Class<?>> controllersClasses = new ClassesInPackageScanner().findAnnotatedClasses(appConfig.getProperty("scan.controller"), Controller.class);

            for (Class classe : controllersClasses) {
                String controller = ((Controller) classe.getAnnotation(Controller.class)).value();

                for (Method method : classe.getMethods()) {
                    GET methodGet = method.getAnnotation(GET.class);
                    POST methodPost = method.getAnnotation(POST.class);
                    PUT methodPut = method.getAnnotation(PUT.class);
                    DELETE methodDelete = method.getAnnotation(DELETE.class);

                    Route route = method.getAnnotation(Route.class);

                    String completeRoute = "/" + controller;

                    if (route != null) {
                        completeRoute += "/" + route.value();
                    }
                    //Contains method GET
                    if (methodGet != null) {
                        String url = "GET:" + completeRoute;
                        System.out.println("URLMapping - " + url);
                        urlMapping.put(url, new URLMapping(classe, method));
                    }
                    //Contains method POST
                    if (methodPost != null) {
                        String url = "POST:" + completeRoute;
                        System.out.println("URLMapping - " + url);
                        urlMapping.put(url, new URLMapping(classe, method));
                    }
                    //Contains method PUT
                    if (methodPut != null) {
                        String url = "PUT:" + completeRoute;
                        System.out.println("URLMapping - " + url);
                        urlMapping.put(url, new URLMapping(classe, method));
                    }
                    //Contains method DELETE
                    if (methodDelete != null) {
                        String url = "DELETE:" + completeRoute;
                        System.out.println("URLMapping - " + url);
                        urlMapping.put(url, new URLMapping(classe, method));
                    }
                }
            }

            context.setAttribute("app-config", appConfig);
            context.setAttribute("url-mapping", urlMapping);
        } catch (Exception e) {
        }
    }

    public void contextDestroyed(ServletContextEvent contextEvent) {
    }
}
