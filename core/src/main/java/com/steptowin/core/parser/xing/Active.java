package com.steptowin.core.parser.xing;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 *@desc: 当前活动结点
 *@author zg
 *@time 2016/4/1 0001
 */
public class Active {

    private int depth;
    private Class clazz;
    private Object instance;
    private LinkedHashMap<String, FieldWrapper> fieldsMap = new LinkedHashMap<>();

    public Active(int depth, Class clazz) {
        this.depth = depth;
        this.clazz = clazz;
        initActiveInfo();
    }

    private void initActiveInfo() {
        try {
            instance = clazz.newInstance();
            Field[] fields = FieldTool.getFields(clazz, true);
            for (Field field : fields) {
                field.setAccessible(true);
                String name = "";
                if (field.isAnnotationPresent(XingParseAlias.class))
                    name = field.getAnnotation(XingParseAlias.class).value();
                else
                    name = field.getName();
                FieldWrapper fieldWrapper = new FieldWrapper(field);
                fieldWrapper.setBelongEntity(instance);
                fieldsMap.put(name, fieldWrapper);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<String, FieldWrapper> getFieldsMap() {
        return fieldsMap;
    }

    public Object getInstance() {
        return instance;
    }
}
