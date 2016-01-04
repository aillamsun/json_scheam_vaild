package com.sung.json.scheam.generate.parse;

import com.sung.json.scheam.generate.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version V1.0
 * @Title: ${file_name}
 * @Package com.ghca.jsonValidator.parse
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2015-04-20
 */
public class JsonSchemaGenerator {


    /**
     * 生成  Json Schema
     *
     * @param clazz 只能是接收 普通对象，不能处理 List ，Map 等集合框架中类
     * @return
     */
    public static String generateSchema(Class clazz) {


        List<Field> fieldList  = new ArrayList<Field>();
        StringBuffer sb = new StringBuffer("{");
       // sb.append("\"" + clazz.getSimpleName() + "\"" + ":" + "{");
        sb.append("\"type\"" + ":" + "\"object\",");
        sb.append("\"properties\":" + "{");

        for(Class<?> c = clazz ; clazz != Object.class ; clazz = clazz.getSuperclass()){
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));

        }
            for (Field f : fieldList) {
            sb.append(generateFieldSchema(f));
        }
        sb.delete(sb.length() - 1, sb.length());
       // sb.append("}");
        sb.append("}");
        sb.append("}");
        
        
        return sb.toString();//format(sb.toString());
    }


    /**
     * 得到格式化json数据  退格用\t 换行用\r
     */
    public static  String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int i=0;i<jsonStr.length();i++){
            char c = jsonStr.charAt(i);
            if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case ' ':break;
                case '{':
                case '[':
                    jsonForMatStr.append(c+"\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c+"\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }

    private static String getLevelStr(int level){
        StringBuffer levelStr = new StringBuffer();
        for(int levelI = 0;levelI<level ; levelI++){
            levelStr.append("\t");
        }
        return levelStr.toString();

}
    /**
     * 生成节点内部  list 的JsonSchema
     *
     * @param field 声明为List 的字段
     * @return
     */
    private static String generateListSchema(Field field) {
        Type type = field.getGenericType();
        if (type == null) return "";
        if (type instanceof ParameterizedType) {
            StringBuffer sb = new StringBuffer();
            sb.append("\"" + field.getName() + "\"" + ":" + "{");
            sb.append("\"type\"" + ":" + "\"array\",");
            ParameterizedType pt = ((ParameterizedType) type);
            Class clazz = (Class) pt.getActualTypeArguments()[0];
            boolean match = false;
            JsonArraySchema schema = null;
            Annotation[] annotations = field.getAnnotations();
            for (Annotation anno : annotations) {
                if (anno instanceof JsonArraySchema) {
                    match = true;
                    schema = ((JsonArraySchema) anno);
                    if (schema.description() != null && !schema.description().equals("")) {
                        sb.append("\"description\"" + ":" + "\"" + schema.description() + "\",");
                    }
                    if (schema.maxItems() != 0) {
						sb.append("\"maxItems\"" + ":" + "\"" + schema.maxItems() + "\",");
                    }
                    if (schema.minItems() != 0) {
                        sb.append("\"minItems\"" + ":" + "\"" + schema.minItems() + "\",");
                    }
                    //sb.append("\"required\"" + ":" + "\"" + schema.required() + "\",");
                    sb.append("\"optional\"" + ":" + "" + schema.optional() + ",");
                }


            }
            if (!match) {
                return "";
            }
            sb.append(generateObjectSchema(clazz));
            sb.delete(sb.length() - 1, sb.length());
            sb.append("},");
            return sb.toString();
        }
        return "";
    }

    /**
     * 生成节点内部  Object 的JsonSchema  用于List 的泛型类型调用
     *
     * @param clazz 声明普通对象类
     * @return
     */
    private static String generateObjectSchema(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        //基本类型的处理
        if (clazz.equals(Integer.class) || clazz.equals(int.class)
                || clazz.equals(Long.class) || clazz.equals(long.class)
                || clazz.equals(Float.class) || clazz.equals(float.class)
                || clazz.equals(Double.class) || clazz.equals(double.class)||clazz.equals(String.class) ) {
            sb.append("\"items\""+":{");
            if (clazz.equals(Integer.class) || clazz.equals(int.class)
                    || clazz.equals(Long.class) || clazz.equals(long.class)
                    || clazz.equals(Float.class) || clazz.equals(float.class)
                    || clazz.equals(Double.class) || clazz.equals(double.class)) {
                sb.append("\"type\"" + ":" + "\"integer\"");
            } else if (clazz.equals(String.class) ) {
                sb.append("\"type\"" + ":" + "\"string\"");
            }
            sb.append("}}");
            return  sb.toString();
        }
        sb.append("\"" + "items" + "\"" + ":" + "{");
        sb.append("\"type\"" + ":" + "\"object\",");
        sb.append("\"properties\":" + "{");
        for (Field f : fields) {
            sb.append(generateFieldSchema(f));
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");
        sb.append("},");
        //System.out.println("单个对象JSON:" + sb.toString());
        return sb.toString();
    }

    /**
     * 生成节点内部  Object 的JsonSchema 用于普通对象的声明
     *
     * @param field 声明普通对象的字段
     * @return
     */
    private static String generateObjectSchema(Field field) {
        Class clazz = field.getType();
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer sb = new StringBuffer();
        boolean match = false;
        sb.append("\"" + field.getName() + "\"" + ":" + "{");

        sb.append("\"type\"" + ":" + "\"object\",");
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonObjectSchema) {
                match = true;
                JsonObjectSchema anno = ((JsonObjectSchema) annotation);
                if (anno.description() != null && !anno.description().equals("")) {
                    sb.append("\"description\"" + ":" + "\"" + anno.description() + "\",");
                }
                //sb.append("\"required\"" + ":" + "\"" + anno.required() + "\",");
                sb.append("\"optional\"" + ":" + "" + anno.optional() + ",");
                break;
            }
        }
        if (!match) {
            return "";
        }
        sb.append("\"properties\":" + "{");
        if(fields.length==0){
            sb.append(",");
        }
        for (Field f : fields) {
            sb.append(generateFieldSchema(f));
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");
        sb.append("},");
        return sb.toString();
    }

    /**
     * 生成节点内部  字段的Json Schema
     *
     * @param field 声明的字段
     * @return
     */
    private static String generateFieldSchema(Field field) {
        Class clazz = field.getType();

        if (clazz.equals(Integer.class) || clazz.equals(int.class)
                || clazz.equals(Long.class) || clazz.equals(long.class)
                || clazz.equals(Float.class) || clazz.equals(float.class)
                || clazz.equals(Double.class) || clazz.equals(double.class)) {
            return generateNumberSchema(field);
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return generateBooleanSchema(field);
        } else if (clazz.equals(String.class)) {
            return generateStringSchema(field);
        } else if (clazz.equals(List.class)) {
            return generateListSchema(field);
		}else {
            boolean match = false;
            JsonObjectSchema schema = null;
            Annotation[] annotations = field.getAnnotations();
            for (Annotation anno : annotations) {
                if (anno instanceof JsonObjectSchema) {
                    match = true;
                }
            }
            if (!match) {
                return "";
            }
            return generateObjectSchema(field);
        }

    }
    
    /**
     * 生成布尔类型的Json Schema
     * @param field
     * @return
     */
    private static String generateBooleanSchema(Field field){
    	Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonBooleanSchema) {
                StringBuffer sb = new StringBuffer();
                sb.append("\"" + field.getName() + "\"" + ":" + "{");
                sb.append("\"type\"" + ":" + "\"boolean\"" + ",");
                JsonBooleanSchema anno = ((JsonBooleanSchema) annotation);
                
//                if (anno.required() == Boolean.TRUE) {
//                    sb.append("\"required\"" + ":" + Boolean.TRUE + ",");
//                }
//                if (anno.required() == Boolean.FALSE) {
//                    sb.append("\"required\"" + ":" + Boolean.FALSE + ",");
//                }
                if (anno.optional() == Boolean.TRUE) {
                    sb.append("\"optional\"" + ":" + Boolean.TRUE + ",");
                }
                if (anno.optional() == Boolean.FALSE) {
                    sb.append("\"optional\"" + ":" + Boolean.FALSE + ",");
                }
                if (anno.description() != null && !anno.description().equals("")) {
                    sb.append("\"description\"" + ":\"" + anno.description() + "\",");
                }
                sb.delete(sb.length() - 1, sb.length());
                sb.append("},");
                return sb.toString();
            }

        }
        return "";
    }

    /**
     * 生成字符串类型的Json Schema
     *
     * @param field 声明的字段
     * @return
     */
    private static String generateStringSchema(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonStringSchema) {
                StringBuffer sb = new StringBuffer();
                sb.append("\"" + field.getName() + "\"" + ":" + "{");
                sb.append("\"type\"" + ":" + "\"string\"" + ",");
                JsonStringSchema anno = ((JsonStringSchema) annotation);
                if (anno.minLength() != 0) {
                    sb.append("\"minLength\"" + ":" + anno.minLength() + ",");
                }
                if (anno.maxLength() != 0) {
                    sb.append("\"maxLength\"" + ":" + anno.maxLength() + ",");

                }
                if (anno.pattern() != null && !anno.pattern().equals("")) {
                    sb.append("\"pattern\"" + ":\"" + anno.pattern() + "\",");

                }
//                if (anno.required() == Boolean.TRUE) {
//                    sb.append("\"required\"" + ":" + Boolean.TRUE + ",");
//                }
//                if (anno.required() == Boolean.FALSE) {
//                    sb.append("\"required\"" + ":" + Boolean.FALSE + ",");
//                }
                if (anno.optional() == Boolean.TRUE) {
                    sb.append("\"optional\"" + ":" + Boolean.TRUE + ",");
                }
                if (anno.optional() == Boolean.FALSE) {
                    sb.append("\"optional\"" + ":" + Boolean.FALSE + ",");
                }
                if (anno.description() != null && !anno.description().equals("")) {
                    sb.append("\"description\"" + ":\"" + anno.description() + "\",");
                }
                if (anno.enums().length > 0) {
                	String  str =  Arrays.toString(anno.enums());
                	str= str.replaceAll("\\[", "");
                    str= str.replaceAll("]","");
                    String[] arr = str.split(",");
                    sb.append("\"enum\":[");
                    for (String enumVal : arr) {
						sb.append("\""+enumVal.trim()+"\",");
					}
                    sb.delete(sb.length() - 1, sb.length());
                    sb.append("],");
                }
                sb.delete(sb.length() - 1, sb.length());
                sb.append("},");
                return sb.toString();
            }

        }

        return "";
    }

    /**
     * 生成数字类型的Json Schema
     *
     * @param field 声明的字段
     * @return
     */

    private static String generateNumberSchema(Field field) {

        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonNumberSchema) {
                JsonNumberSchema anno = ((JsonNumberSchema) annotation);

                StringBuffer sb = new StringBuffer();
                sb.append("\"" + field.getName() + "\"" + ":" + "{");
                if (anno.type() == JsonElementType.INTEGER) {
                    sb.append("\"type\"" + ":\"" + "integer" + "\",");
                }
                if (anno.type() == JsonElementType.LONG) {
                    sb.append("\"type\"" + ":\"" + "long" + "\",");
                }
//                if (anno.required() == Boolean.TRUE) {
//                    sb.append("\"required\"" + ":" + Boolean.TRUE + ",");
//                }
//                if (anno.required() == Boolean.FALSE) {
//                    sb.append("\"required\"" + ":" + Boolean.FALSE + ",");
//                }
                if (anno.optional() == Boolean.TRUE) {
                    sb.append("\"optional\"" + ":" + Boolean.TRUE + ",");
                }
                if (anno.optional() == Boolean.FALSE) {
                    sb.append("\"optional\"" + ":" + Boolean.FALSE + ",");
                }
                if (anno.description() != null && !anno.description().equals("")) {
                    sb.append("\"description\"" + ":\"" + anno.description() + "\",");
                }
                if (anno.enums().length > 0) {
                    String  str =  Arrays.toString(anno.enums());
                    str= str.replaceAll("\\[", "");
                    str= str.replaceAll("]","");
                    sb.append("\"enum\"" + ":[" + str+ "],");
                }
                if (anno.maximum() != 0) {
                    sb.append("\"maximum\"" + ":" + anno.maximum() + ",");
                }
                if (anno.minimum() != 0) {
                    sb.append("\"minimum\"" + ":" + anno.minimum() + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                sb.append("},");
                return sb.toString();
            }
        }
        return "";
    }


}
