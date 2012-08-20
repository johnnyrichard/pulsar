package com.pulsar.framework.handle;

import com.pulsar.framework.core.URLMapping;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Johnny Richard
 */
public class ControllerFactory {
    
    public Object getController(URLMapping urlMapping) {
        Object controller = null;
        try {
            controller = urlMapping.getClasse().newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(ControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ControllerFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return controller;
    }
}
