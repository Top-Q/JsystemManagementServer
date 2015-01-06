package org.topq.testsexecutionserver.scenariocomposer.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.topq.testsexecutionserver.managment.SystemConfig;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioNode;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioParser;

@Component
public class FsScenarioScanner implements IScenarioScanner {

	private ScenarioNode scenarioModel;

	@Autowired
	private SystemConfig systemConfig;

	@Override
	public ScenarioNode getScenarioModel(String scenariosDir,
			String scenarioName, String uuid, ScenarioNode parent)
			throws Exception {
		scenarioModel.printPropertiesRecursively();
		return scenarioModel;
	}

	@Override
	public ScenarioNode getScenarioModel() throws Exception {
		return scenarioModel;
	}

	@PostConstruct
	public void init() throws Exception {
		scenarioModel = ScenarioParser.getScenarioModel(
				systemConfig.getScenariosDir(), systemConfig.getScenarioName(),
				null, null);
	}

}
