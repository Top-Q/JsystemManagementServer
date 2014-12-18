package org.topq.testsexecutionserver.jenkins.services;

import java.util.Map;

import org.topq.testsexecutionserver.domain.ExecutionData;

public interface JenkinsRemoteService {
	
	public String getAvailableAgents();
	public String executeDefaultJob();
	public String executeParameterizedJob(Map<String, String> params);
	public ExecutionData getExecutionHistory();
}
