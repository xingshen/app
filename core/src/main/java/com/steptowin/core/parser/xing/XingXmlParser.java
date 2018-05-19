package com.steptowin.core.parser.xing;

import com.steptowin.core.common.BaseEntity;
import com.steptowin.core.parser.IParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@desc: xml解析器
 * 下面是解析器适用于实体类的一些约定：1、对象属性名与xml相应tag同名，包括list属性。2、如果实体类有用到内部类
 * ，此内部类须静态公共可用且继承{@link BaseEntity}
 * 。3、实体必须继承自{@link BaseEntity}
 *@author zg
 *@time 2016/4/1 0001
 */
public class XingXmlParser implements IParser {
    public enum TagType {
        TEXT, LIST, ENTITY, NONE
    }

    private Class clazz;
    private Map<Integer, Active> actives = new HashMap<>();
    private TagType activeTagType = TagType.NONE;

    public XingXmlParser() {
    }

    @Override
    public <T> T parse(InputStream in, Class<T> clazz) {
        this.clazz = clazz;
        return (T) parseOne(in);
    }

    /**
     * @Desc: 解析生成对象
     * @Author: zg
     * @Time: 2016/1/14 17:46
     */
    public synchronized Object parseOne(InputStream in) {
        try {
            XmlPullParserFactory xmlParserFactory = XmlPullParserFactory
                    .newInstance();
            XmlPullParser parser = xmlParserFactory.newPullParser();
            parser.setInput(in, "utf-8");
            for (int event = parser.getEventType(); event != XmlPullParser.END_DOCUMENT; event = parser
                    .next()) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        FieldWrapper mw = handleTag(parser);
                        invokeField(parser, mw);
                        break;
                    case XmlPullParser.END_TAG:
//                        handleTag(parser);
//                        passEntity(parser);
                        break;
                    case XmlPullParser.END_DOCUMENT:

                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object object = actives.get(1).getInstance();
        actives.clear();
        return object;
    }

    /**
     * @Desc: 根据当前解析位置（深度、所属类）生成活跃级，有三种情况会调用这个方法：
     * 1、刚开始解析，没有第一层活跃级
     * 2、由于不确定的原因，某个层级被忽略了，当解析进行到这个忽略层内层时，会把这个忽略层补上（使用忽略层的外层作活跃级）
     * 3、当某个标签的所属类是list或者{@link BaseEntity}的子类时，会生成内层活跃级
     * @Author: zg
     * @Time: 2016/1/14 17:35
     */
    private Active initActiveInfo(int depth, Class clazz) {
        Active active = new Active(depth, clazz);
        actives.put(depth, active);
        return active;
    }

    /**
     * @Desc: 处理标签，生成活跃级
     * @Author: zg
     * @Time: 2016/1/14 17:46
     */
    private FieldWrapper handleTag(XmlPullParser parser) {
        Active active = actives.get(parser.getDepth());
        if (null == active) {
            if (null != actives.get(parser.getDepth() - 1)) {//向上找活跃级
                actives.put(parser.getDepth(), actives.get(parser.getDepth() - 1));
            } else {
                initActiveInfo(parser.getDepth(), clazz);//直接生成目标实例做当前活跃级
            }
            active = actives.get(parser.getDepth());
        }
        FieldWrapper mw = null;
        if (null != active && null != active.getFieldsMap().get(parser.getName())) {
            mw = active.getFieldsMap().get(parser.getName());
            activeTagType = mw.getType();
        } else
            activeTagType = TagType.NONE;

        if (null != mw) {
            switch (activeTagType) {
                case LIST:
                    initActiveInfo(parser.getDepth() + 1, mw.getGenericType()).getInstance();
                    break;
                case ENTITY:
                    initActiveInfo(parser.getDepth() + 1, mw.getFieldType());
                    break;
            }
        }

        return mw;
    }

    /**
     * @Desc: 给标签属性赋值
     * @Author: zg
     * @Time: 2016/1/14 17:47
     */
    private void invokeField(XmlPullParser parser, FieldWrapper mw) {
        if (null != mw) {
            try {
                switch (activeTagType) {
                    case LIST:
                        List<Object> entityList = (List<Object>) mw.getField().get(mw.getBelongEntity());
                        if (null == entityList) {
                            entityList = new ArrayList<>();
                            mw.getField().set(mw.getBelongEntity(),
                                    entityList);
                        }
                        if (mw.getGenericType() == String.class) {
                            entityList.add(parser.nextText());
                        } else {
                            entityList.add(actives.get(parser.getDepth() + 1).getInstance());
                        }

                        break;
                    case TEXT:
                        Object param = mw.getParamValue(parser.nextText());
                        if (null != param)
                            mw.getField().set(mw.getBelongEntity(),
                                    param);
                        break;
                    case ENTITY:
                        Object entity = mw.getParamValue(parser.getName());
                        mw.getField().set(mw.getBelongEntity(), entity);
                        break;

                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 结束标签的操作
     *
     * @param parser
     */
    private void passEntity(XmlPullParser parser) {
        switch (activeTagType) {
            case LIST:
            case ENTITY:
                actives.remove(parser.getDepth());
                break;

            default:
                break;
        }
    }
}
