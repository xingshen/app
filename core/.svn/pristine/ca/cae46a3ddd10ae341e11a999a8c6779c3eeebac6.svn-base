package com.steptowin.core.parser.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.steptowin.core.parser.IParser;
import com.steptowin.core.tools.StreamTool;

import java.io.InputStream;

/**
 *@desc: gson解析器
 *@author zg
 *@time 2016/4/1 0001
 */
public class GsonParser implements IParser {


    /**
     *desc
     *edit by zg
     *
     */
    @Override
    public <T> T parse(InputStream in, Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        T result = null;
        try {
            result = gson.fromJson(StreamTool.inputStream2String(in), clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
