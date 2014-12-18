package org.topq.testsexecutionserver.jenkins.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

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
import org.springframework.stereotype.Component;
import org.topq.testsexecutionserver.domain.Build;
import org.topq.testsexecutionserver.domain.BuildExtraInformation;
import org.topq.testsexecutionserver.domain.BuildsContainer;
import org.topq.testsexecutionserver.domain.ExecutionData;
import org.topq.testsexecutionserver.domain.ExecutionRow;
import org.topq.testsexecutionserver.domain.Parameter;
import org.topq.testsexecutionserver.utils.UtilStreamToString;

@Component
public class JenkinsRemoteServiceImpl implements JenkinsRemoteService {

	private String masterIp;
	private String masterPort;

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
	public ExecutionData getExecutionHistory() {
		/*ExecutionData data = new ExecutionData();
		return data;*/
		
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
		for (Build build : buildsContainer.getBuilds()) {
			if (build != null) {
				ExecutionRow executionRow = fillExecutionRow(build);
				executionData.getData().add(executionRow);
			}
		}
	}

	private ExecutionRow fillExecutionRow(Build build) {
		ExecutionRow executionRow = new ExecutionRow();
		/*executionRow.setAgent("master");
		executionRow.setScenario("default");*/
		executionRow.setStatus(build.getResult());
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
			buildExtraInfo = mapper.readValue(jsonRes, BuildExtraInformation.class);
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
			Parameter scenarioParam = buildExtraInfo.getActions().get(0).getParameters().get(1);
			Parameter nodeMarkParam = buildExtraInfo.getActions().get(0).getParameters().get(2);
			
			executionRow.setAgent(nodeMarkParam.getValue());
			executionRow.setScenario(scenarioParam.getValue());
		} catch (Exception e) {
			executionRow.setAgent("N/A");
			executionRow.setScenario("N/A");
		}
	
		return executionRow;
	}

	private String updateUrlWithBuildNumber(String url, Integer number) {
		return url.replace("xxx", number.toString());
	}

	@PostConstruct
	public void init() throws Exception {
		Properties properties = new Properties();
		InputStream in = this
				.getClass()
				.getResourceAsStream(
						"/org/topq/testsexecutionserver/scenarioparser/resources/scenarioParseInput.properties");
		properties.load(in);
		masterIp = properties.getProperty("jenkinsMasterIp");
		masterPort = properties.getProperty("jenkinsMasterPort");
	}

	private String updateRequestUrlAccordingToApi(
			RemoteApiRequest remoteApiRequest) {
		String url = "http://" + masterIp + ":" + masterPort;
		String requestPath = null;

		switch (remoteApiRequest) {
		case GET_NODES:
			requestPath = "/computer/api/json";
			break;
		case EXECUTE_DEFAULT_JOB:
			/*requestPath = "/job/SimpleTestsExecution/build";*/
			requestPath = "/job/ManagmentJob/build";
			break;
		case EXECUTE_PARAMETERIZED_JOB:
			/*requestPath = "/job/SimpleTestsExecution/buildWithParameters";*/
			requestPath = "/job/ManagmentJob/buildWithParameters";
			break;
		case GET_JOB_BUILDS_TREE:
			requestPath = "/job/ManagmentJob/api/json?tree=builds[number,status,id,result,url]";
			break;
		case GET_BUILD_EXTRA_INFO:
			requestPath = "/job/ManagmentJob/xxx/api/json?tree=actions[parameters[name,value]]";
			break;
		default:
			break;
		}
		url = url + requestPath;
		return url;
	}
	
	private String sendPostRequestToJenkinsRestApi(String url, Map<String, String> params) throws Exception {
		String json = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
		} catch (Exception e) {
			throw new Exception("Could not open a connection");
		}
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(pairs));
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

	private enum RemoteApiRequest {
		GET_NODES, EXECUTE_DEFAULT_JOB, EXECUTE_PARAMETERIZED_JOB, GET_JOB_BUILDS_TREE, GET_BUILD_EXTRA_INFO
	}

}
