package com.steptowin.core.parser.xing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *@desc: 给属性加别名，在某些情况下，Bean中的属性命名不能与标签命名一致（比如，标签使用了java关键字）
 * 可以通过给属性添加此注解，此注解的值将会代替属性名称与标签进行匹配
 *@author zg
 *@time 2016/4/1 0001
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XingParseAlias {
    /**
     * The name of the field alias.
     */
    String value() default "";
}
