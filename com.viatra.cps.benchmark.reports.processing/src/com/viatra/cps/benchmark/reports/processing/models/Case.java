package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Case {
	@JsonProperty("CaseName")
	protected String caseName;
	
	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public List<String> getBuilds() {
		return builds;
	}

	public void setBuilds(List<String> builds) {
		this.builds = builds;
	}

	@JsonProperty("Builds")
	protected List<String> builds;

}
