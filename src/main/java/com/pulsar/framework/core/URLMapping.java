package com.pulsar.framework.core;

import com.pulsar.framework.annotation.Controller;
import com.pulsar.framework.annotation.Interceptors;
import com.pulsar.framework.annotation.Route;
import com.pulsar.framework.interceptor.Interceptor;
import java.lang.reflect.Method;

/**
 *
 * @author Johnny Richard
 */
public class URLMapping {

    private Class classe;
    private Method method;

    public URLMapping(Class classe, Method method) {
        this.classe = classe;
        this.method = method;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
    
    public String getUrlRoute() {
        Controller controller = (Controller) classe.getAnnotation(Controller.class);
        Route route = (Route) method.getAnnotation(Route.class);
        return "/" + controller.value() + (route != null ? "/" + route.value() : "");
    }
    
    public Class<? extends Interceptor>[] getInterceptors() {
        Interceptors annotation = (Interceptors) classe.getAnnotation(Interceptors.class);
        if (annotation != null) {
            return annotation.value();
        } else {
            return null;
        }
    }
    
}
