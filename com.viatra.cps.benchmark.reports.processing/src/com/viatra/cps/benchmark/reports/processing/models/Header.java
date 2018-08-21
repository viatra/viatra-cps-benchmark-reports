package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Header {
	@JsonProperty("size")
	private Integer size;

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	@JsonProperty("operationId")
	private String operationId;

	public Header() {
	}

	public Header(Integer size, String id) {
		this.size = size;
		this.operationId = id;
	}

}
