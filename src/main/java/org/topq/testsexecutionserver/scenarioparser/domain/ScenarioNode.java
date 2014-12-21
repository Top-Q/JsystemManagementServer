package org.topq.testsexecutionserver.scenarioparser.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({ "parent", "properties", "xmlFilePath" })
public class ScenarioNode extends AbstractScenarioNode {

	private String name;
	@JsonIgnore
	private ScenarioNode parent;
	private List<AbstractScenarioNode> childNodes = new ArrayList<AbstractScenarioNode>();
	@JsonIgnore
	private Map<String, String> properties = new HashMap<String, String>();
	@JsonIgnore
	private String xmlFilePath;

	public ScenarioNode(String name, String xmlFilePath, String uuid,
			ScenarioNode parent) {
		this.name = name;
		this.uuid = uuid;
		this.parent = parent;
		this.xmlFilePath = xmlFilePath;
	}

	public void addChildNode(AbstractScenarioNode childNode) {
		childNodes.add(childNode);
	}

	public void addProperty(String key, String value) {
		properties.put(key, value);
	}

	public List<AbstractScenarioNode> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<AbstractScenarioNode> childNodes) {
		this.childNodes = childNodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ScenarioNode getParent() {
		return parent;
	}

	public void setParent(ScenarioNode parent) {
		this.parent = parent;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}

	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}

	public void printPropertiesRecursively() {

		System.out.println("[SCENARIO]: " + name + "; UUID: " + uuid
				+ " - PROPERTIES:");
		System.out
				.println("----------------------------------------------------------");
		System.out.println(getPropertiesAsString());
		System.out.println("");

		for (AbstractScenarioNode childNode : childNodes) {
			if (childNode instanceof ScenarioNode) {
				((ScenarioNode) childNode).printPropertiesRecursively();
			}
		}
	}

	public String getPropertiesAsString() {
		StringBuilder sb = new StringBuilder();

		for (Entry<String, String> property : properties.entrySet()) {
			sb.append(property.getKey() + "=" + property.getValue() + "\n");
		}

		return sb.toString();
	}

	public Properties getPropertiesObject() {
		Properties props = new Properties();
		props.putAll(properties);
		return props;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append("[SCENARIO]: " + name + "; UUID: " + uuid + "\n");

		for (AbstractScenarioNode childNode : childNodes) {
			sb.append("[" + name + "'s child]: ");
			sb.append(childNode.toString());
		}

		return sb.toString();
	}

}
