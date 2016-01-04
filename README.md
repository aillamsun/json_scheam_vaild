#json_scheam_vaild

json_scheam_vaild直接通过使用java bean上添加对应类型json scheam注解，自动生成json schema规范，通过验证用户输入的json字符串工具类

###使用说明:

修改com/sung/json/scheam/impl/JacksonSchema.java  反射包名修改为你自己的

###代码如下:[^code]
```java
Class<JSONValidator> clazz = (Class<JSONValidator>) Class.forName("com.sung.json.scheam.impl.validators."+ className);
```
###使用说明:[^code]
```java
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
```


