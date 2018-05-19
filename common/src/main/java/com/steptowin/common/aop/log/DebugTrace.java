package com.steptowin.common.aop.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc:日志输入
 * author：zg
 * date:17/5/4
 * time:下午4:33
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR,ElementType.METHOD})
public @interface DebugTrace {
}
