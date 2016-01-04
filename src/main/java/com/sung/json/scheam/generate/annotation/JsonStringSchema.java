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
public @interface JsonStringSchema {
    /**
     * 节点类型
     *
     * @return
     */
    JsonElementType type() default JsonElementType.STRING;

    /**
     * 最大长度
     *
     * @return
     */
    int maxLength() default 0;

    /**
     * 最小长度
     *
     * @return
     */
    int minLength() default 0;

    /**
     * 正则表达式
     *
     * @return
     */
    String pattern() default "";

    /**
     * 描述
     *
     * @return
     */
    String description() default "";

    /**
     * 取值区间
     *
     * @return
     */

    String[] enums() default {};

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
