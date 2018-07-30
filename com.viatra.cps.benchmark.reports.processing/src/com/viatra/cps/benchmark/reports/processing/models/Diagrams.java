package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Diagrams {

	@JsonProperty("ResultData")
	protected List<ResultData> resultData;

	@JsonProperty("Path")
	protected String path;

	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<ResultData> getResultData() {
		return resultData;
	
	}
	public void setResultData(List<ResultData> resultData) {
		this.resultData = resultData;
	}
	
	public Diagrams(String path) {
		this.path = path;
		this.resultData = new ArrayList<>();
	}
	
	public Diagrams() {
	}
}
