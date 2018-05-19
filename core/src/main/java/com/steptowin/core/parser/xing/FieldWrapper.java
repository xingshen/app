package com.steptowin.core.parser.xing;

import android.util.Log;

import com.steptowin.core.common.BaseEntity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *@desc: {@link} 封装类
 *@author zg
 *@time 2016/4/1 0001
 */
public class FieldWrapper {
    Field field;
    Object belongEntity;
    Class type;// 参数的类型
    Class genericType;// 参数的范型类型

    public FieldWrapper(Field f) {
        this.field = f;
        this.type = this.field.getType();
        initGenericType();
    }

    private Class initGenericType() {
        if (XingXmlParser.TagType.LIST == getType()) {
            if (this.type.isAssignableFrom(List.class)) {
                Type t = this.field.getGenericType();
                if (t instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) t;
                    genericType = (Class) pt.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    /**
     * 获取方法的参数值，等待set edit by zg
     *
     * @param text
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException Object
     */
    public Object getParamValue(String text) throws InstantiationException,
            IllegalAccessException {
        if (type == String.class) {
            return getStringParamValue(type, text);
        } else if (FieldTool.isPrimitive(type)) {
            try {
                return getPrimitiveParamValue(type, text);
            } catch (NumberFormatException e) {
                Log.w("warn", "NumberFormatException" + e.getMessage());
            }
            return null;
        } else {
            return getSyntheticParamValue(this, text);
        }
    }

    private Object getStringParamValue(Class paramType, String text) {
        if (paramType != String.class) {
            Log.w("warn", "此参数类型不是String类型");
        }
        return text;
    }

    private Object getPrimitiveParamValue(Class paramType, String text)
            throws NumberFormatException {
        if (paramType == int.class || paramType == Integer.class) {
            return Integer.valueOf(text);
        } else if (paramType == float.class || paramType == Float.class
                || paramType == double.class || paramType == Double.class) {
            return Double.valueOf(text);
        } else if (paramType == boolean.class || paramType == Boolean.class) {
            if ("1".equals(text)) {
                return true;
            } else {
                return false;
            }
        } else if (paramType == short.class || paramType == Short.class) {
            return Short.valueOf(text);
        }
        return null;
    }

    private Object getSyntheticParamValue(FieldWrapper mw, String text) {
        if (mw.getFieldType() == List.class
                || mw.getFieldType() == Collection.class) {
            return new ArrayList<Object>();
        } else if (mw.getFieldType().getSuperclass() == BaseEntity.class) {
            try {
                return mw.getFieldType().newInstance();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取方法的参数类型 edit by zg
     *
     * @return String
     */
    public XingXmlParser.TagType getType() {
        if (this.type == String.class || FieldTool.isPrimitive(this.type)) {
            return XingXmlParser.TagType.TEXT;
        } else if (this.type == List.class
                || this.type == Collection.class) {
            return XingXmlParser.TagType.LIST;
        } else if (this.type.getSuperclass() == BaseEntity.class) {
            return XingXmlParser.TagType.ENTITY;
        }
        return XingXmlParser.TagType.NONE;
    }

    public Class getFieldType() {
        return type;
    }

    /**
     * 获取方法的参数的范型类型，比如{@link List < String >} 的#String 实例 edit by zg
     *
     * @return Object
     */
    @SuppressWarnings({"unchecked"})
    public Object getParamGenericEntity() {
        if (XingXmlParser.TagType.LIST == getType()) {
            if (this.type.isAssignableFrom(List.class)) {
                Type t = this.field.getGenericType();
                if (t instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) t;
                    genericType = (Class) pt.getActualTypeArguments()[0];
                    try {
                        return genericType.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取方法的参数的范型类型，比如{@link List < String >} 的#String edit by zg
     *
     * @return Object
     */


    public Class getGenericType() {
        return genericType;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getBelongEntity() {
        return belongEntity;
    }

    public void setBelongEntity(Object belongEntity) {
        this.belongEntity = belongEntity;
    }
}
