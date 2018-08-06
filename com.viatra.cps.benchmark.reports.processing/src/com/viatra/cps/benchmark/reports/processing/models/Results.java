package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Results {
	@JsonProperty("Results")
	protected List<AggregataedResult> results;

	public List<AggregataedResult> getResults() {
		return results;
	}

	public void setResults(List<AggregataedResult> results) {
		this.results = results;
	}

	@JsonProperty("Path")
	protected String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Results() {
		this.results = new ArrayList<>();
	}
	
	public Results(String path) {
		this.path = path;
		this.results = new ArrayList<>();
	}
}
