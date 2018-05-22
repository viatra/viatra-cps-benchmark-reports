package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class ToolColor {

	@JsonProperty("ToolName")
	protected String toolName;

	@JsonProperty("Color")
	protected String color;

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolName() {
		return this.toolName;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}
}
