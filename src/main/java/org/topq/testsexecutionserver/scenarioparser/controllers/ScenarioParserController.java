package org.topq.testsexecutionserver.scenarioparser.controllers;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import jsystem.framework.scenario.RunnerTest;
import jsystem.framework.scenario.Scenario;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scenarioParsing")
public class ScenarioParserController {

	@RequestMapping(value = "/parse", method = RequestMethod.GET)
	public @ResponseBody
	String parseScenario(
			@RequestParam(value = "automationProjectLocation", required = true) String automationProjectLocation,
			@RequestParam(value = "rootScenarioName", required = true) String rootScenarioName,
			@RequestParam(value = "scenarioFolder", required = true) String scenarioFolder) {

		System.out.println(automationProjectLocation);
		System.out.println(rootScenarioName);
		System.out.println(scenarioFolder);
		return null;
	}

	@RequestMapping(value = "/parseFromPropertiesFile", method = RequestMethod.GET)
	public @ResponseBody
	String parseScenarioFromPropertiesFile() throws Exception {
		Properties properties = new Properties();
		InputStream in = this
				.getClass()
				.getResourceAsStream(
						"/org/topq/testsexecutionserver/scenarioparser/resources/scenarioParseInput.properties");
		properties.load(in);
		Scenario s = null;
		File scenariosFolder = new File(
				properties.getProperty("scenarioFolder"));
		s = new Scenario(scenariosFolder,
				properties.getProperty("rootScenarioName"));
		RunnerTest test = s.getTest(0);
		System.out.println(test.getMethodName());
		test = s.getTest(1);
		System.out.println(test.getMethodName());
		test = s.getTest(2);
		System.out.println(test.getMethodName());
		return null;
	}

}
