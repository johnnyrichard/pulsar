package com.pulsar.framework.util;

/**
 *
 * @author Johnny Richard
 */
public class StringUtils {

    public static Object padrse(Class classType, String value) {
        
        if (value == null)  {
            return null;
        } else if (classType.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (classType.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (classType.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (classType.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (classType.equals(String.class)) {
            return value;
        }
        return null;
    }
}
