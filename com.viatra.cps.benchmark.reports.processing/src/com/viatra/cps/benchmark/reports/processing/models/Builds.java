package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Builds {
	
	@JsonProperty("BuildId")
	protected String buildId;
	
	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public List<Case> getCases() {
		return cases;
	}

	public void setCases(List<Case> cases) {
		this.cases = cases;
	}

	@JsonProperty("Cases")
	protected List<Case> cases;
	
	 public Builds() {
		 this.cases = new ArrayList<>();
	 }
}
