package com.sung.json.scheam.impl.validators;

import com.sung.json.scheam.impl.JSONValidator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;

import java.io.Serializable;
import java.util.List;

public class BodyValidator implements JSONValidator, Serializable {

    private static final long serialVersionUID = 1583459368637967478L;

    public static final String PROPERTY = "body";
    public static final String PROPERTY_CANEQUAL = "";

    protected Number minimum;
    protected JsonParser.NumberType numberType;
    protected boolean canEqual = true;


    public BodyValidator(JsonNode minimumNode, JsonNode minimumCanEqualNode) {
        if (minimumNode != null && minimumNode.isNumber()) {
            minimum = minimumNode.getNumberValue();
            numberType = minimumNode.getNumberType();
        }

        if (minimumCanEqualNode != null && minimumCanEqualNode.isBoolean()) {
            canEqual = minimumCanEqualNode.getBooleanValue();
        }
    }

    @Override
    public List<String> validate(JsonNode node, String at) {
        return null;
    }

    @Override
    public List<String> validate(JsonNode node, JsonNode parent, String at) {
        return null;
    }


}
