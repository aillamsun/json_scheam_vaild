
package com.sung.json.scheam.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sung.json.scheam.JSONSchema;
import com.sung.json.scheam.JSONSchemaException;
import com.sung.json.scheam.impl.validators.*;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JacksonSchema implements JSONSchema, JSONValidator, Serializable {

	private static final long serialVersionUID = -3585793275135068320L;

	private static final Logger LOG = LoggerFactory.getLogger(JacksonSchema.class);

	protected ObjectMapper mapper;
	public List<JSONValidator> validators;
	protected boolean optional = false;

	public JacksonSchema(ObjectMapper mapper, JsonNode schemaNode) {
		this.mapper = mapper;
		validators = new ArrayList<JSONValidator>();
		read(schemaNode);
	}

	public JacksonSchema(JsonNode schemaNode) {
		validators = new ArrayList<JSONValidator>();
		read(schemaNode);
	}

	@SuppressWarnings("unchecked")
	protected void read(JsonNode schemaNode) {
		Iterator<String> pnames = schemaNode.getFieldNames();
		while (pnames.hasNext()) {
			String pname = pnames.next();
			JsonNode n = schemaNode.get(pname);

			// If a $ref node is contained in the node, then we replace the node
			// with the target of the ref
			// This is an experimental implementation based on the assumption
			// that the $ref property
			// contains a valid URL
			JsonNode refNode = n.get("$ref");
			if (refNode != null) {
				try {
					n = mapper.readTree(new URL(refNode.getTextValue()).openStream());
				} catch (Exception e) {
					LOG.error( "$ref resolution failed: " + refNode.getTextValue(),e);
				}
			}

			// Optional must be defined a priori
			if ("optional".equals(pname)) {
				if (n.isBoolean() && n.getBooleanValue())
					optional = true;

				continue;
			}

			// Additional Properties validator need the list of allowed
			// properties
			if (AdditionalPropertiesValidator.PROPERTY.equals(pname)) {
				validators.add(new AdditionalPropertiesValidator(schemaNode
						.get(PropertiesValidator.PROPERTY), schemaNode
						.get(AdditionalPropertiesValidator.PROPERTY)));
				continue;
			}

			// Minimum needs MinimumCanEqual (if present) ...
			if (MinimumValidator.PROPERTY.equals(pname)) {
				validators.add(new MinimumValidator(schemaNode
						.get(MinimumValidator.PROPERTY), schemaNode
						.get(MinimumValidator.PROPERTY_CANEQUAL)));
				continue;
			}
			// ... and MinimumCanEqual alone is a nonsense
			if (MinimumValidator.PROPERTY_CANEQUAL.equals(pname)) {
				continue;
			}

			// Maximum needs MaximumCanEqual (if present) ...
			if (MaximumValidator.PROPERTY.equals(pname)) {
				validators.add(new MaximumValidator(schemaNode
						.get(MaximumValidator.PROPERTY), schemaNode
						.get(MaximumValidator.PROPERTY_CANEQUAL)));
				continue;
			}
			// ... and MaximumCanEqual alone is a nonsense
			if (MaximumValidator.PROPERTY_CANEQUAL.equals(pname)) {
				continue;
			}

			// General case
			String className = Character.toUpperCase(pname.charAt(0))
					+ pname.substring(1) + "Validator";
			if (n != null) {
				try {
					Class<JSONValidator> clazz = (Class<JSONValidator>) Class.forName("com.sung.json.scheam.impl.validators."+ className);
					Constructor<JSONValidator> c = null;
					try {
						if ("DescriptionValidator".equals(className)) {
							c = clazz.getConstructor(JsonNode.class);
							validators.add(c.newInstance(n));
						}else {
							c = clazz.getConstructor(JsonNode.class);
							validators.add(c.newInstance(n));
						}
					
					} catch (NoSuchMethodException nsme) {
						c = clazz.getConstructor(
						    JsonNode.class,ObjectMapper.class);
						validators.add(c.newInstance(n, mapper));
					}
				} catch (Exception e) {
					//LOG.warn("Could not load validator " + pname, e);
					validators.add(new NoOpValidator(n));
				}
			} else {
				if (TypeValidator.PROPERTY.equals(pname))
					throw new JSONSchemaException("Invalid JSON Schema: \"type\" property not found!");
			}
		}
	}

	// --------------------------------------------------- Implement
	// JSONValidator
	public List<String> validate(JsonNode jsonNode, String at) {
		return validate(jsonNode, null, at);
	}

	public List<String> validate(JsonNode jsonNode, JsonNode parent, String at) {
		List<String> errors = new ArrayList<String>();
		for (JSONValidator v : validators) {
			errors.addAll(v.validate(jsonNode, parent, at));
		}
		return errors;
	}

	// ----------------------------------------------------- Implement
	// JSONSchema
	@Override
	public List<String> validate(String json) {
		try {
			JsonNode jsonNode = mapper.readTree(json);
			return validate(jsonNode, JSONValidator.AT_ROOT);
		} catch (IOException ioe) {
			LOG.error("Failed to load json instance!", ioe);
			throw new JSONSchemaException(ioe);
		}
	}

	@Override
	public List<String> validate(InputStream jsonStream) {
		try {
			JsonNode jsonNode = mapper.readTree(jsonStream);
			return validate(jsonNode, JSONValidator.AT_ROOT);
		} catch (IOException ioe) {
			LOG.error("Failed to load json instance!", ioe);
			throw new JSONSchemaException(ioe);
		}
	}

	@Override
	public List<String> validate(Reader jsonReader) {
		try {
			JsonNode jsonNode = mapper.readTree(jsonReader);
			return validate(jsonNode, JSONValidator.AT_ROOT);
		} catch (IOException ioe) {
			LOG.error("Failed to load json instance!", ioe);
			throw new JSONSchemaException(ioe);
		}
	}

	@Override
	public List<String> validate(URL jsonURL) {
		try {
			JsonNode jsonNode = mapper.readTree(jsonURL.openStream());
			return validate(jsonNode, JSONValidator.AT_ROOT);
		} catch (IOException ioe) {
			LOG.error("Failed to load json instance!", ioe);
			throw new JSONSchemaException(ioe);
		}
	}

	public boolean isOptional() {
		return optional;
	}
}
