package com.sung.json.scheam.generate.annotation;

/**
 * @version V1.0
 * @Title: ${file_name}
 * @Package com.ghca.jsonValidator.annotation
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2015-04-19
 */
public enum JsonElementType {

    INTEGER("Integer"),
    LONG("Long"),
    STRING("String"),
    ARRAY("Array"),
    BOOLEAN("Boolean"),
    OBJECT("Object");

    private JsonElementType(String desc) {
    }
}
