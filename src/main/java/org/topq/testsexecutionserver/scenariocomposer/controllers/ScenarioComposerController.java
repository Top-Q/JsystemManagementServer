package org.topq.testsexecutionserver.scenariocomposer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.topq.testsexecutionserver.scenariocomposer.services.IScenarioScanner;
import org.topq.testsexecutionserver.scenarioparser.domain.ScenarioNode;

@Controller
@RequestMapping("/scenarioComposer")
public class ScenarioComposerController {
	
	@Autowired
	private IScenarioScanner fsScenarioScanner;
	
	@RequestMapping(value = "/getScenarioModel", method = RequestMethod.GET)
	public @ResponseBody ScenarioNode getScenarioModel() throws Exception {
		ScenarioNode scenarioModel = fsScenarioScanner.getScenarioModel();
		return scenarioModel;
	}	
	
	@RequestMapping(value = "/getScenarios", method = RequestMethod.GET)
	public @ResponseBody List<String> getScenarios() {
		return fsScenarioScanner.getScenarios();
	}
	
	@RequestMapping(value = "/getSuts", method = RequestMethod.GET)
	public @ResponseBody List<String> getSuts() {
		return fsScenarioScanner.getSuts();
	}
}
