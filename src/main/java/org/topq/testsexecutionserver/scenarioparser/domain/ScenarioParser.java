package org.topq.testsexecutionserver.scenarioparser.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import jsystem.framework.scenario.JTest;
import jsystem.framework.scenario.RunnerTest;
import jsystem.framework.scenario.Scenario;

public class ScenarioParser {

	public static ScenarioNode getScenarioModel(String scenarionsDir,
			String scenarioName, String uuid, ScenarioNode parent)
			throws Exception {

		String xmlFilePath = scenarionsDir + File.separator + scenarioName
				+ ".xml";
		String propertiesFilePath = scenarionsDir + File.separator
				+ scenarioName + ".properties";

		Scenario scenario = new Scenario(new File(scenarionsDir), scenarioName);
		ScenarioNode scenarioNode = new ScenarioNode(scenario.getName(),
				xmlFilePath, uuid, parent);

		// load current scenario properties from it's properties file
		BufferedReader br = new BufferedReader(new FileReader(
				propertiesFilePath));
		String line;
		while ((line = br.readLine()) != null) {
			String[] property = line.split("=");
			scenarioNode.addProperty(property[0], property[1]);
		}
		br.close();

		// if the current scenario isn't the root scenario (parent != null),
		// override the properties from the properties file by the parent's
		// properties
		if (parent != null) {
			Map<String, String> parentProperties = parent.getProperties();
			for (Entry<String, String> property : parentProperties.entrySet()) {
				if (property.getKey().startsWith(uuid + ".")) {
					String key = property.getKey().replace(uuid + ".", "");
					String value = property.getValue();
					scenarioNode.addProperty(key, value);
				}
			}
		}

		// load current scenario's child nodes (could be tests or sub-scenarios)
		Vector<JTest> nodes = scenario.getRootTests();
		for (JTest node : nodes) {

			if (node instanceof RunnerTest) {
				RunnerTest runnerTest = (RunnerTest) node;
				TestNode testNode = new TestNode(runnerTest.getMethodName(),
						runnerTest.getClassName(), runnerTest.getUUID());
				scenarioNode.addChildNode(testNode);
			} else if (node instanceof Scenario) {
				Scenario subScenario = (Scenario) node;
				String subScenarioName = subScenario.getName().substring(
						subScenario.getName().lastIndexOf("/") + 1);
				ScenarioNode subScenarioNode = getScenarioModel(scenarionsDir,
						subScenarioName, subScenario.getUUID(), scenarioNode);
				scenarioNode.addChildNode(subScenarioNode);
			}
		}

		return scenarioNode;
	}

}
