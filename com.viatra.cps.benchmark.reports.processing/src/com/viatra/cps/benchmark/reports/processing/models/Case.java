package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
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

	public List<String> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<String> scenarios) {
		this.scenarios = scenarios;
	}

	@JsonProperty("Scenarios")
	protected List<String> scenarios;

	public Case() {
		this.scenarios = new ArrayList<>();
	}
}
