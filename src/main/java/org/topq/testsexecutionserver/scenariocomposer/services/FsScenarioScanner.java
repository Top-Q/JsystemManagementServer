package org.topq.testsexecutionserver.scenariocomposer.services;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioNode;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioParser;

@Component
public class FsScenarioScanner implements IScenarioScanner {

	private ScenarioNode scenarioModel;
	private String scenariosDir;
	private String scenarioName;

	@Override
	public ScenarioNode getScenarioModel(String scenariosDir,
			String scenarioName, String uuid, ScenarioNode parent)
			throws Exception {
		scenarioModel.printPropertiesRecursively();
		return scenarioModel;
	}

	@Override
	public ScenarioNode getScenarioModel() throws Exception {
		//scenarioModel.printPropertiesRecursively();
		return scenarioModel;
	}

	@PostConstruct
	public void init() throws Exception {
		System.out.println("This is the service init");
		Properties properties = new Properties();
		InputStream in = this
				.getClass()
				.getResourceAsStream(
						"/org/topq/testsexecutionserver/scenarioparser/resources/scenarioParseInput.properties");
		properties.load(in);
		scenariosDir = properties.getProperty("scenarioFolder");
		scenarioName = properties.getProperty("rootScenarioName");
		scenarioModel = ScenarioParser.getScenarioModel(scenariosDir,
				scenarioName, null, null);
	}

}
