package org.topq.testsexecutionserver.managment;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class SystemConfig {

	private String jenkinsIp;
	private String jenkinsPort;
	private String maxExecutionHistoryEntries;
	private String scenariosDir;
	private String scenarioName;
	private String sutsDir;

	@PostConstruct
	public void init() throws Exception {
		String osName = System.getProperty("os.name");
		String configPropertiesFileLocation = null;
		if (osName.startsWith("Win")) {
			configPropertiesFileLocation = "C:\\ExecutionManager\\systemConfig.properties";
		} else if (osName.startsWith("Linux")) {
			configPropertiesFileLocation = "/home/nimrod/ExecutionManager/scenarioParseInput.properties";
		} else if (osName.startsWith("Mac OS")) {
			configPropertiesFileLocation = "/Users/nimrodti/Documents/ExecutionManager/systemConfig.properties";
		}

		Properties properties = new Properties();
		FileInputStream input = new FileInputStream(
				configPropertiesFileLocation);
		InputStreamReader inputStreamReader = new InputStreamReader(input,
				"UTF-8");
		properties.load(inputStreamReader);
		jenkinsIp = properties.getProperty("jenkinsMasterIp");
		jenkinsPort = properties.getProperty("jenkinsMasterPort");
		maxExecutionHistoryEntries = properties
				.getProperty("maxExecutionHistoryEntries");
		scenariosDir = properties.getProperty("scenarioFolder");
		scenarioName = properties.getProperty("rootScenarioName");
		sutsDir = properties.getProperty("sutsDir");
	}

	public String getJenkinsIp() {
		return jenkinsIp;
	}

	public void setJenkinsIp(String jenkinsIp) {
		this.jenkinsIp = jenkinsIp;
	}

	public String getJenkinsPort() {
		return jenkinsPort;
	}

	public void setJenkinsPort(String jenkinsPort) {
		this.jenkinsPort = jenkinsPort;
	}

	public String getMaxExecutionHistoryEntries() {
		return maxExecutionHistoryEntries;
	}

	public void setMaxExecutionHistoryEntries(String maxExecutionHistoryEntries) {
		this.maxExecutionHistoryEntries = maxExecutionHistoryEntries;
	}

	public String getScenariosDir() {
		return scenariosDir;
	}

	public void setScenariosDir(String scenariosDir) {
		this.scenariosDir = scenariosDir;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getSutsDir() {
		return sutsDir;
	}

	public void setSutsDir(String sutsDir) {
		this.sutsDir = sutsDir;
	}

}
