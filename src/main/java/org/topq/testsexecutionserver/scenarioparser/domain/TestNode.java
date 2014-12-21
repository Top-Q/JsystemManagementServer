package org.topq.testsexecutionserver.scenarioparser.domain;

public class TestNode extends AbstractScenarioNode {

	private String className;
	private String methodName;

	public TestNode(String methodName, String className, String uuid) {
		this.methodName = methodName;
		this.className = className;
		this.uuid = uuid;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String toString() {

		return "[TEST]: " + className + "." + methodName + "; UUID: " + uuid
				+ "\n";
	}

}
