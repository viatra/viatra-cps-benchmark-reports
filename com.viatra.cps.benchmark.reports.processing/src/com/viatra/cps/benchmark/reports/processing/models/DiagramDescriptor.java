package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class DiagramDescriptor {

	@JsonProperty("CaseName")
	protected String caseName;

	@JsonProperty("Build")
	protected String build;

	@JsonProperty("Scenario")
	protected String scenario;

	@JsonProperty("OperationId")
	protected String operationId;

	@JsonProperty("Opened")
	protected Boolean opened;

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public DiagramDescriptor() {
	}

	public DiagramDescriptor(String caseName, String build, String scenario, String operationId, Boolean opened) {
		this.caseName = caseName;
		this.build = build;
		this.opened = opened;
		this.operationId = operationId;
		this.scenario = scenario;
	}
}
