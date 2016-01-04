/**
 * Copyright (C) 2010 Nicolas Vahlas <nico@vahlas.eu>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sung.json.scheam.impl.validators;

import com.sung.json.scheam.TYPE;
import com.sung.json.scheam.impl.JSONValidator;
import com.sung.json.scheam.impl.TYPEFactory;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternValidator implements JSONValidator, Serializable {

    private static final long serialVersionUID = 4460940200749213244L;
    private static final Logger LOG = LoggerFactory.getLogger(PatternValidator.class);

    public static final String PROPERTY = "pattern";

    protected String pattern;

    public PatternValidator(JsonNode patternNode) {
        pattern = "";
        if (patternNode != null && patternNode.isTextual()) {
            pattern = patternNode.getTextValue();
        }
    }

    @Override
    public List<String> validate(JsonNode node, String at) {
        LOG.debug("validate( " + node + ", " + at + ")");
        return validate(node, null, at);
    }

    @Override
    public List<String> validate(JsonNode node, JsonNode parent, String at) {
        LOG.debug("validate( " + node + ", " + parent + ", " + at + ")");

        List<String> errors = new ArrayList<String>();

        TYPE nodeType = TYPEFactory.getNodeType(node);
        if (nodeType != TYPE.STRING) {
            errors.add(at + ": cannot match a " + nodeType + " against a regex pattern (" + pattern + ")");
            return errors;
        }

        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(node.getTextValue());
            if (!m.matches()) {
                errors.add(at + ": 不匹配描述信息规则, " + "description :" + DescriptionValidator.DESC_INFO_MAP.get(pattern.trim()));
            }
        } catch (PatternSyntaxException pse) {
            // String is considered valid if pattern is invalid
            LOG.error("Failed to apply pattern on " + at + ": Invalid RE syntax [" + pattern + "]", pse);
        }

        return errors;
    }

}
