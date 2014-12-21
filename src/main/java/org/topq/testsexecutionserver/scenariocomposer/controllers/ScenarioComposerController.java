package org.topq.testsexecutionserver.scenariocomposer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
}
