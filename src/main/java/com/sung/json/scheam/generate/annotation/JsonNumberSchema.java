package com.sung.json.scheam.generate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version V1.0
 * @Title: ${file_name}
 * @Package com.ghca.jsonValidator.annotation
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2015-04-19
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JsonNumberSchema {
    /**
     * 节点类型  默认 Integer
     *
     * @return
     */
    JsonElementType type() default JsonElementType.INTEGER;

    /**
     * 最小值
     *
     * @return
     */
    int minimum() default 0;

    /**
     * 最大值
     *
     * @return
     */
    int maximum() default 0;

    /**
     * 取值区间
     *
     * @return
     */

    String[] enums() default {};

    /**
     * 描述
     *
     * @return
     */
    String description() default "";

    /**
     * 是否必填  默认 必填
     *
     * @return
     */
    boolean required() default true;
    /**
     * 是否可选 支持 default
     * @return
     */
    boolean optional() default true;
}
