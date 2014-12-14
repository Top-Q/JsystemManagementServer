package org.topq.testsexecutionserver.scenariocomposer.services;

import org.topq.scenarioparser.ScenarioNode;

public interface IScenarioScanner {
	
	public ScenarioNode getScenarioModel(String scenariosDir, String scenarioName, String uuid, ScenarioNode parent) throws Exception;
		
	public ScenarioNode getScenarioModel() throws Exception;
}
