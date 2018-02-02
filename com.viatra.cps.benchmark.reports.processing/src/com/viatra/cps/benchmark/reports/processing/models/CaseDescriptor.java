package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class CaseDescriptor {

	@JsonProperty("CaseName")
	private String caseName;

	@JsonProperty("RunIndex")
	private int runIndex;

	@JsonProperty("Scenario")
	private String scenario;

	@JsonProperty("Tool")
	private String tool;

	@JsonProperty("Size")
	private int size;

	public CaseDescriptor(String caseName) {
		this.caseName = caseName;
	}

	public String getCaseName() {
		return caseName;
	}

	public int getRunIndex() {
		return runIndex;
	}

	public String getScenario() {
		return scenario;
	}

	public String getTool() {
		return tool;
	}

	public int getSzie() {
		return size;
	}

	public CaseDescriptor() {
		// TODO Auto-generated constructor stub
	}
}
