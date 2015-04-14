package org.topq.testsexecutionserver.scenariocomposer.services;

import java.util.List;

import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioNode;

public interface IScenarioScanner {
	
	public ScenarioNode getScenarioModel(String scenariosDir, String scenarioName, String uuid, ScenarioNode parent) throws Exception;
		
	public ScenarioNode getScenarioModel() throws Exception;
	
	public List<String> getScenarios();
	
	public List<String> getSuts();
}
