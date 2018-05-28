package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Build {

	@JsonProperty("Name")
	protected String name;

	@JsonProperty("ID")
	protected String id;

	@JsonProperty("ResultData")
	protected List<ResultData> resultData;

	public List<ResultData> getResultData() {
		return resultData;
	}

	public void setResultData(List<ResultData> resultData) {
		this.resultData = resultData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
