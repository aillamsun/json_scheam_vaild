package com.sung;

import com.alibaba.fastjson.JSON;
import com.sung.json.scheam.generate.parse.JsonSchemaGenerator;
import com.sung.json.scheam.impl.JacksonSchema;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungang on 2016/1/4.10:10
 */
public class Main {

    public static void main(String[] args) {

        //test1();

        test2();

        //test3();
    }


    public static void test1(){
        Address address = new Address();
        address.setAddress("成都市");
        Eyes youEye = new Eyes();
        youEye.setEys("右眼");
        Eyes zuoEye = new Eyes();
        zuoEye.setEys("左眼");
        Person person = new Person();
        person.setAddress(address);
        person.setAge(4);
        person.setName("sungang");
        List<Eyes> eyeses = new ArrayList<Eyes>();
        eyeses.add(youEye);
        eyeses.add(zuoEye);
        person.setEyeses(eyeses);



        String personJson = JSON.toJSONString(person);
        System.out.println("JSON->:" + personJson );

        List<String> errors = checkjsonStr(personJson,Person.class);
        if (null != errors){
            for (String err:errors){
                System.out.println(err);
            }
        }
    }


    public static void test2(){
        Address address = new Address();
        address.setAddress("成都市");
        Eyes youEye = new Eyes();
        youEye.setEys("右眼");
        Eyes zuoEye = new Eyes();
        zuoEye.setEys("左眼");
        Person person = new Person();
        person.setAddress(address);
        person.setAge(201);
        //person.setName("sungang");
        List<Eyes> eyeses = new ArrayList<Eyes>();
        eyeses.add(youEye);
        eyeses.add(zuoEye);
        person.setEyeses(eyeses);
        String personJson = JSON.toJSONString(person);
        System.out.println("JSON->:" + personJson );

        List<String> errors = checkjsonStr(personJson,Person.class);
        if (null != errors){
            for (String err:errors){
                System.out.println(err);
            }
        }
    }


    public static void test3(){
        Address address = new Address();
        address.setAddress("成都市");
        Eyes youEye = new Eyes();
        youEye.setEys("右眼");
        Eyes zuoEye = new Eyes();
        zuoEye.setEys("左眼");
        Person person = new Person();
        person.setAddress(address);
        person.setAge(24);
        person.setName("sungangdddddddddddd");
        List<Eyes> eyeses = new ArrayList<Eyes>();
        eyeses.add(youEye);
        eyeses.add(zuoEye);


        person.setEyeses(eyeses);



        String personJson = JSON.toJSONString(person);
        System.out.println("JSON->:" + personJson );

        List<String> errors = checkjsonStr(personJson,Person.class);
        if (null != errors){
            for (String err:errors){
                System.out.println(err);
            }
        }
    }


    private static List<String> checkjsonStr(String jsonData,Class<?> clazz){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode schemaNode = null;
        List<String> errorsMsg = null;
        try {
            String jsonScheam = JsonSchemaGenerator.generateSchema(clazz);
            System.out.println("JSON Scheam ->:" + jsonScheam );
            schemaNode = mapper.readTree(jsonScheam);
            JacksonSchema jschema = new JacksonSchema(mapper, schemaNode);
            errorsMsg = jschema.validate(jsonData);
            if (null != errorsMsg && errorsMsg.size() == 0) {
            } else {
            }
        } catch (IOException e) {
        }
        return errorsMsg;
    }
}
