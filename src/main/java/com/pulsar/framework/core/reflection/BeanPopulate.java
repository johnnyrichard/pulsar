package com.pulsar.framework.core.reflection;

import com.pulsar.framework.helper.RequestContext;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Johnny Richard
 */
public class BeanPopulate {

    private Map<String, String[]> requestMap;
    private RequestContext request;
    private String rootName;

    public BeanPopulate(RequestContext request) {
        this.requestMap = request.getRequest().getParameterMap();
        this.request = request;
    }

    private boolean containsKeyStartsWith(Set<String> keys, String criteria) {
        for (String key : keys) {
            if (key.startsWith(criteria)) {
                return true;
            }
        }
        return false;
    }

    public <T> T getBeanWithRootName(Class<T> classe, String rootName) throws Exception {
        this.rootName = rootName;
        return getBean(classe, "");
    }

    private <T> T getBean(Class<T> classe, String classPrevious) throws Exception {

        Object bean = classe.newInstance();

        String className;

        if (classPrevious.equals("")) {
            className = rootName;
        } else {
            className = classe.getSimpleName().substring(0, 1).toLowerCase() + classe.getSimpleName().substring(1);
        }

        for (Method method : classe.getMethods()) {
            if (method.getName().startsWith("set")) {
                String methodName = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                String key = classPrevious + className + "." + methodName;
                if (requestMap.containsKey(key)) {
                    BeanUtils.setProperty(bean, methodName, requestMap.get(key)[0]);
                } else if (containsKeyStartsWith(requestMap.keySet(), key)) {
                    BeanUtils.setProperty(bean, methodName, getBean(method.getParameterTypes()[0], className + "."));
                }
            }
        }
        return (T) bean;
    }
}
