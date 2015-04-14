package org.topq.testsexecutionserver.domain;

public class ExecutionRow {

	private int executionNumber;
	private String scenario;
	private String agent;
	private String timeStamp;
	private String status;
	private String sut;

	public int getExecutionNumber() {
		return executionNumber;
	}

	public void setExecutionNumber(int executionNumber) {
		this.executionNumber = executionNumber;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSut() {
		return sut;
	}

	public void setSut(String sut) {
		this.sut = sut;
	}

}
