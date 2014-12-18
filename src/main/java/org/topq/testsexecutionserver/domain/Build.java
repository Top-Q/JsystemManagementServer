package org.topq.testsexecutionserver.domain;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "number", "result", "url" })
public class Build {

	@JsonProperty("id")
	private String id;
	@JsonProperty("number")
	private Integer number;
	@JsonProperty("result")
	private String result;
	@JsonProperty("url")
	private String url;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The number
	 */
	@JsonProperty("number")
	public Integer getNumber() {
		return number;
	}

	/**
	 * 
	 * @param number
	 *            The number
	 */
	@JsonProperty("number")
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * 
	 * @return The result
	 */
	@JsonProperty("result")
	public String getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 *            The result
	 */
	@JsonProperty("result")
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 
	 * @return The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 *            The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
