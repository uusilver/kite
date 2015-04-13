package com.tmind.kite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 
 * @author lijunying
 * @desc 用来标志线程安全的方法
 */
public @interface ThreadSafe {

}
