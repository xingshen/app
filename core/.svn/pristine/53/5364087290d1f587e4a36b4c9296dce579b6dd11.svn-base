package com.steptowin.core.parser.xing;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;

/**
 *@desc: {@link Field} 工具
 *@author zg
 *@time 2016/4/1 0001
 */
public class FieldTool {

    public static boolean isPrimitive(Class clazz) {
        if (clazz.isPrimitive() || clazz == Integer.class
                || clazz == Boolean.class || clazz == Float.class
                || clazz == Short.class || clazz == Double.class) {
            return true;
        }
        return false;
    }

    public static Field[] getFields(Class clazz, boolean includeParentFields) {
        Field[] fields = clazz.getDeclaredFields();

        if (includeParentFields)
            return ArrayUtils.addAll(fields, getParentClassFields(clazz.getSuperclass()));
        return fields;
    }

    private static Field[] getParentClassFields(Class clazz) {
        if(null == clazz){
            return new Field[0];
        }
        Field[] parentFields = clazz.getDeclaredFields();

        if (clazz.getSuperclass() == Object.class) {
            return parentFields;
        }
        return ArrayUtils.addAll(parentFields, getParentClassFields(clazz.getSuperclass()));
    }
}
