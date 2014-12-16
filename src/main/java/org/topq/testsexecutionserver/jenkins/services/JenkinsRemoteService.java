package org.topq.testsexecutionserver.jenkins.services;

import java.util.Map;

public interface JenkinsRemoteService {
	
	public String getAvailableAgents();
	public String executeDefaultJob();
	public String executeParameterizedJob(Map<String, String> params);
	
}
