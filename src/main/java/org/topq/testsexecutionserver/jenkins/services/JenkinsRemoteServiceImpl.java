package org.topq.testsexecutionserver.jenkins.services;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.topq.testsexecutionserver.domain.Build;
import org.topq.testsexecutionserver.domain.BuildExtraInformation;
import org.topq.testsexecutionserver.domain.BuildsContainer;
import org.topq.testsexecutionserver.domain.ExecutionData;
import org.topq.testsexecutionserver.domain.ExecutionRow;
import org.topq.testsexecutionserver.domain.Parameter;
import org.topq.testsexecutionserver.managment.SystemConfig;
import org.topq.testsexecutionserver.utils.UtilStreamToString;

@Component
public class JenkinsRemoteServiceImpl implements JenkinsRemoteService {

	@Autowired
	private SystemConfig systemConfig;
	
	private String cred;
	private byte[] encodedCred;

	@Override
	public String getAvailableAgents() {
		String jsonRes = null;
		String url = updateRequestUrlAccordingToApi(RemoteApiRequest.GET_NODES);
		try {
			jsonRes = getJsonFromJenkinsRestApi(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jsonRes;
	}

	@Override
	public String executeDefaultJob() {
		String jsonRes = null;
		String url = updateRequestUrlAccordingToApi(RemoteApiRequest.EXECUTE_DEFAULT_JOB);
		try {
			jsonRes = getJsonFromJenkinsRestApi(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jsonRes;
	}

	@Override
	public String executeParameterizedJob(Map<String, String> params) {
		String jsonRes = null;
		String url = updateRequestUrlAccordingToApi(RemoteApiRequest.EXECUTE_PARAMETERIZED_JOB);
		try {
			jsonRes = sendPostRequestToJenkinsRestApi(url, params);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jsonRes;
	}

	@Override
	public String cancelExecution(String buildNumber) {
		String jsonRes = null;
		String url = updateRequestUrlAccordingToApi(RemoteApiRequest.CANCEL_EXECUTION);
		url = updateUrlWithBuildNumber(url, buildNumber);
		try {
			jsonRes = sendPostRequestToJenkinsRestApi(url, null);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jsonRes;
	}

	@Override
	public ExecutionData getExecutionHistory() {

		String jsonRes = null;
		String url = updateRequestUrlAccordingToApi(RemoteApiRequest.GET_JOB_BUILDS_TREE);
		try {
			jsonRes = getJsonFromJenkinsRestApi(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		BuildsContainer buildsContainer = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			buildsContainer = mapper.readValue(jsonRes, BuildsContainer.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExecutionData executionData = new ExecutionData();
		fillExecutionData(executionData, buildsContainer);

		return executionData;
	}

	private void fillExecutionData(ExecutionData executionData,
			BuildsContainer buildsContainer) {
		int maxBuilds = Integer.parseInt(systemConfig
				.getMaxExecutionHistoryEntries());
		if (maxBuilds > buildsContainer.getBuilds().size()) {
			maxBuilds = buildsContainer.getBuilds().size();
		}
		for (int i = 0; i < maxBuilds; i++) {
			Build build = buildsContainer.getBuilds().get(i);
			if (build != null) {
				ExecutionRow executionRow = fillExecutionRow(build);
				executionData.getData().add(executionRow);
			}
		}
	}

	private ExecutionRow fillExecutionRow(Build build) {
		ExecutionRow executionRow = new ExecutionRow();
		if (build.getResult() == null) {
			executionRow.setStatus("RUNNING");
		} else {
			executionRow.setStatus(build.getResult());
		}
		executionRow.setTimeStamp(build.getId());
		executionRow.setExecutionNumber(build.getNumber());

		String jsonRes = null;
		String url = updateRequestUrlAccordingToApi(RemoteApiRequest.GET_BUILD_EXTRA_INFO);
		url = updateUrlWithBuildNumber(url, build.getNumber());
		try {
			jsonRes = getJsonFromJenkinsRestApi(url);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		BuildExtraInformation buildExtraInfo = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			buildExtraInfo = mapper.readValue(jsonRes,
					BuildExtraInformation.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Parameter scenarioParam = buildExtraInfo.getActions().get(0)
					.getParameters().get(1);
			Parameter nodeMarkParam = buildExtraInfo.getActions().get(0)
					.getParameters().get(2);
			Parameter sutParam = buildExtraInfo.getActions().get(0)
					.getParameters().get(3);

			executionRow.setAgent(nodeMarkParam.getValue());
			executionRow.setScenario(scenarioParam.getValue());
			executionRow.setSut(sutParam.getValue());
		} catch (Exception e) {
			executionRow.setAgent("N/A");
			executionRow.setScenario("N/A");
		}

		return executionRow;
	}

	private String updateUrlWithBuildNumber(String url, Integer number) {
		return url.replace("xxx", number.toString());
	}
	
	private String updateUrlWithBuildNumber(String url, String number) {
		return url.replace("xxx", number);
	}

	private String updateRequestUrlAccordingToApi(
			RemoteApiRequest remoteApiRequest) {
		String url = null;
		if (systemConfig.getJenkinsPort().equals("8080")) {
			url = "http://" + systemConfig.getJenkinsIp() + ":"
					+ systemConfig.getJenkinsPort();
		}
		else {
			url = "https://" + systemConfig.getJenkinsIp();
		}
		String jenkinsMainAutomationJob = systemConfig.getJenkinsMainAutomationJob();
		String requestPath = null;

		switch (remoteApiRequest) {
		case GET_NODES:
			requestPath = "/computer/api/json";
			break;
		case EXECUTE_DEFAULT_JOB:
			/*requestPath = "/job/ManagmentJob/build";*/
			/*requestPath = "/job/SmokeTests/build";*/
			requestPath = "/job/" + jenkinsMainAutomationJob + "/build";
			break;
		case EXECUTE_PARAMETERIZED_JOB:
			//requestPath = "/job/ManagmentJob/buildWithParameters";
			requestPath = "/job/" + jenkinsMainAutomationJob + "/buildWithParameters";
			break;
		case GET_JOB_BUILDS_TREE:
			//requestPath = "/job/ManagmentJob/api/json?tree=builds[number,status,id,result,url]";
			requestPath = "/job/" + jenkinsMainAutomationJob + "/api/json?tree=builds[number,status,id,result,url]";
			break;
		case GET_BUILD_EXTRA_INFO:
			//requestPath = "/job/ManagmentJob/xxx/api/json?tree=actions[parameters[name,value]]";
			requestPath = "/job/" + jenkinsMainAutomationJob + "/xxx/api/json?tree=actions[parameters[name,value]]";
			break;
		case CANCEL_EXECUTION:
			//requestPath = "/job/ManagmentJob/xxx/stop";
			requestPath = "/job/" + jenkinsMainAutomationJob + "/xxx/stop";
			break;
		default:
			break;
		}
		url = url + requestPath;
		return url;
	}

	private String sendPostRequestToJenkinsRestApi(String url,
			Map<String, String> params) throws Exception {
		String json = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
		} catch (Exception e) {
			throw new Exception("Could not open a connection");
		}
		if (systemConfig.getUseAuthentication().equals("true")) {
			httpPost.setHeader("Authorization", "Basic " + new String(encodedCred));
		}
		if (params != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));
		}
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String body = UtilStreamToString.convertStreamToString(entity
					.getContent());
			json = body;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return json;
	}

	private String getJsonFromJenkinsRestApi(String url) throws Exception {
		String json = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setBooleanParameter(
				"http.protocol.handle-redirects", false);
		HttpGet httpget = null;
		try {
			httpget = new HttpGet(url);
		} catch (Exception e) {
			throw new Exception("Could not open a connection");
		}
		if (systemConfig.getUseAuthentication().equals("true")) {
			httpget.setHeader("Authorization", "Basic " + new String(encodedCred));
		}
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String body = UtilStreamToString.convertStreamToString(entity
					.getContent());
			json = body;
		} catch (Exception e) {
			throw new Exception("Could not execute http get request");
		}

		return json;
	}
	
	@PostConstruct
	public void init() {
		if (systemConfig.getUseAuthentication().equals("true")) {
			cred = systemConfig.getUserId() + ":" + systemConfig.getApiToken();
			encodedCred = Base64.encodeBase64(cred.getBytes(Charset.forName("US-ASCII")));
		}
	}

	private enum RemoteApiRequest {
		GET_NODES, EXECUTE_DEFAULT_JOB, EXECUTE_PARAMETERIZED_JOB,
		GET_JOB_BUILDS_TREE, GET_BUILD_EXTRA_INFO, CANCEL_EXECUTION
	}
}
