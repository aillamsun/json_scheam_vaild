package com.sung;

import com.sung.json.scheam.generate.annotation.JsonArraySchema;
import com.sung.json.scheam.generate.annotation.JsonNumberSchema;
import com.sung.json.scheam.generate.annotation.JsonObjectSchema;
import com.sung.json.scheam.generate.annotation.JsonStringSchema;

import java.util.List;

/**
 * Created by sungang on 2016/1/4.10:04
 */
public class Person {

    @JsonNumberSchema(optional = false,minimum = 1,maximum = 200,description = "")//optional = false 必选
    private Integer age;
    //@JsonStringSchema(optional = false,minLength = 1,maxLength = 13)
    @JsonStringSchema(optional = false,pattern = "^(?![0-9]+$)[0-9a-zA-Z-]{1,13}$",description = "^(?![0-9]+$)[0-9a-zA-Z-]{1,13}$&名字前缀，1-13位字符，名称包含只字母、数字和中划线，同时不能由纯数字组成")
    private String name;

    @JsonObjectSchema(optional = true)
    private Address address;

    @JsonArraySchema(optional = true)
    private List<Eyes> eyeses;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Eyes> getEyeses() {
        return eyeses;
    }

    public void setEyeses(List<Eyes> eyeses) {
        this.eyeses = eyeses;
    }
}
