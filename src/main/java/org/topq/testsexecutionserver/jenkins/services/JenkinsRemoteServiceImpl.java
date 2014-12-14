package org.topq.testsexecutionserver.jenkins.services;

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
import org.springframework.stereotype.Component;
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
			requestPath = "/job/SimpleTestsExecution/build";
			break;
		case EXECUTE_PARAMETERIZED_JOB:
			requestPath = "/job/SimpleTestsExecution/buildWithParameters";
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
		GET_NODES, EXECUTE_DEFAULT_JOB, EXECUTE_PARAMETERIZED_JOB
	}

}
