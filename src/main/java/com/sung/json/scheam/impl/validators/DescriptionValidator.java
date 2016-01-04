package com.sung.json.scheam.impl.validators;

import com.google.common.collect.Maps;
import com.sung.json.scheam.impl.JSONValidator;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DescriptionValidator implements JSONValidator, Serializable {

    private static final long serialVersionUID = 392674659825474202L;

    private static final Logger LOG = LoggerFactory.getLogger(DescriptionValidator.class);

    public static Map<String, Object> DESC_INFO_MAP = Maps.newHashMap();

    public static final String PROPERTY = "description";

    private String description;

    public DescriptionValidator(JsonNode patternNode) {
        description = "";
        if (patternNode != null && patternNode.isTextual()) {
            description = patternNode.getTextValue();
            String temp[] = description.split("&");
            DESC_INFO_MAP.put(temp[0].trim(), temp[1]);
        }
    }

    ;

    @Override
    public List<String> validate(JsonNode node, String at) {
        LOG.debug("validate( " + node + ", " + at + ")");
        return validate(node, null, at);
    }

    @Override
    public List<String> validate(JsonNode node, JsonNode parent, String at) {
        LOG.debug("validate( " + node + ", " + parent + ", " + at + ")");
        List<String> errors = new ArrayList<String>();

//		if ( schemaType == TYPE.UNION) {
//			errors.addAll(unionTypeValidator.validate(node, parent, at));
//			return errors;
//		}
//		
//		TYPE nodeType = TYPEFactory.getNodeType(node);
//		if ( nodeType != schemaType ) {
//			if ( schemaType == TYPE.ANY )
//				return errors;
//			
//			if ( schemaType == TYPE.NUMBER && nodeType == TYPE.INTEGER )
//				return errors;
//			
//			errors.add(at + ": " + nodeType + " found, " + schemaType + " expected");
//		}
//		
        return errors;
    }


}
