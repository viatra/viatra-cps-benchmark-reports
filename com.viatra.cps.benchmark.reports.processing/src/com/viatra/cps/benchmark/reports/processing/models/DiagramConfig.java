package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class DiagramConfig {
	
	@JsonProperty("ResultConfig")
	protected ResultConfig resultConfig;
	
	@JsonProperty("ToolColor")
	protected List<ToolColor> toolColors;

	public ResultConfig getResultConfig() {
		return resultConfig;
	}

	public void setResultConfig(ResultConfig resultConfig) {
		this.resultConfig = resultConfig;
	}

	public List<ToolColor> getToolColors() {
		return toolColors;
	}

	public void setToolColors(List<ToolColor> toolColors) {
		this.toolColors = toolColors;
	}
	

}
