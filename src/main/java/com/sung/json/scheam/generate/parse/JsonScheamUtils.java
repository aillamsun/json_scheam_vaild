package com.sung.json.scheam.generate.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.util.List;

import com.sung.json.scheam.generate.annotation.JsonScheamFileName;
import com.sung.json.scheam.utils.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;
public class JsonScheamUtils {
	/**
	 * 根据包名 生成对应的jsonSchema文件
	 * 
	 * @ActionLocalDNATModel 格式@JsonScheamFileName("配置，查询DNAT的公网端口范围")
	 *                       属性上必须使用了shcema 注解
	 * @param packageName
	 * @param generateAddress
	 * @param annotationClass
	 */
	public static void generateJsonScheamFiles(String packageName,
			String generateAddress, Class<? extends Annotation> annotationClass)
			throws Exception {
		if (StringUtils.isBlank(packageName)|| StringUtils.isBlank(generateAddress)) {
			throw new NullPointerException("packageName or generateAddress param is not null.");
		}
		List<Class<?>> listClasses = ClassUtils.getClassListByAnnotation(packageName, annotationClass);
		if (null != listClasses) {
			for (Class<?> clazz : listClasses) {
				JsonScheamFileName jsonScheamFileName = clazz.getAnnotation(JsonScheamFileName.class);
				String fileName = jsonScheamFileName.value();
				String schemaContent = JsonSchemaGenerator.generateSchema(clazz);
				System.out.println(clazz.getSimpleName()+"-->"+schemaContent);
				ObjectMapper mapper = new ObjectMapper();
				try {
					mapper.readTree(schemaContent);
				} catch (Exception e) {
					System.out.println("出现异常了 Model Name ：" + clazz.getSimpleName()+"\r\n"+e.getMessage());
				}
				writeFile(generateAddress, fileName, schemaContent);
			}
			
			System.out.println("一共执行生产了[" + listClasses.size()+"]scheam 文件");
		}
	}

	/**
	 * 得到格式化json数据 退格用\t 换行用\r
	 */
	public static String format(String jsonStr) {
		int level = 0;
		StringBuffer jsonForMatStr = new StringBuffer();
		for (int i = 0; i < jsonStr.length(); i++) {
			char c = jsonStr.charAt(i);
			if (level > 0
					&& '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
				jsonForMatStr.append(getLevelStr(level));
			}
			switch (c) {
			case ' ':
				break;
			case '{':
			case '[':
				jsonForMatStr.append(c + "\n");
				level++;
				break;
			case ',':
				jsonForMatStr.append(c + "\n");
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

	private static String getLevelStr(int level) {
		StringBuffer levelStr = new StringBuffer();
		for (int levelI = 0; levelI < level; levelI++) {
			levelStr.append("\t");
		}
		return levelStr.toString();

	}

	private static void writeFile(String filePath, String fileName,
			String content) {
		try {
			File file = new File(filePath + "/" + fileName + ".json");
			FileOutputStream fs = new FileOutputStream(file);
			fs.write(content.getBytes("UTF-8"));
			fs.flush();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
