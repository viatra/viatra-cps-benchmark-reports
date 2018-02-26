package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class OperationConfig {

	@JsonProperty("type")
	private String type;
	
	@JsonProperty("attribute")
	private String attribute;
	
	@JsonProperty("filter")
	private List<Object> filter;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public List<Object> getFilter() {
		return filter;
	}
	public void setFilter(List<Object> filter) {
		this.filter = filter;
	}
}
