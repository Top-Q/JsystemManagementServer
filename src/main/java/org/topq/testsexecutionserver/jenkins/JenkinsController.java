package org.topq.testsexecutionserver.jenkins;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.topq.testsexecutionserver.domain.ExecutionData;
import org.topq.testsexecutionserver.jenkins.services.JenkinsRemoteService;

@Controller
@RequestMapping("jenkins")
public class JenkinsController {

	@Autowired
	private JenkinsRemoteService jenkinsRemoteService;

	@RequestMapping(value = "getAgents", method = RequestMethod.GET)
	public @ResponseBody String getavailableAgents() {
		return jenkinsRemoteService.getAvailableAgents();
	}

	@RequestMapping(value = "executeDefaultJob", method = RequestMethod.GET)
	public @ResponseBody String executeDefaultJob() {
		return jenkinsRemoteService.executeDefaultJob();
	}

	@RequestMapping(value = "executeParameterizedJob", method = RequestMethod.GET)
	public @ResponseBody String executeParameterizedJob(
			@RequestParam(value = "agent", required = true) String agent,
			@RequestParam(value = "scenario", required = true) String scenario) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("node", agent);
		params.put("scenario", scenario);
		return jenkinsRemoteService.executeParameterizedJob(params);
	}
	
	@RequestMapping(value = "getExecutionHistory", method = RequestMethod.GET)
	public @ResponseBody ExecutionData getAllStudents() {
		ExecutionData data = new ExecutionData();
		return data;
	}

}
