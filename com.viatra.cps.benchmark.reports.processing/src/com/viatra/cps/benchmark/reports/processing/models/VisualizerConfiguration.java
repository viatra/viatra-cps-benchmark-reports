package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class VisualizerConfiguration {

	@JsonProperty("ToolColor")
	protected List<ToolColor> toolColors;

	public List<ToolColor> getToolColors() {
		return toolColors;
	}

	public void setToolColors(List<ToolColor> toolColors) {
		this.toolColors = toolColors;
	}

	@JsonProperty("Scale")
	protected List<Scale> scales;

	public List<Scale> getScales() {
		return scales;
	}

	public void setScales(List<Scale> scales) {
		this.scales = scales;
	}
	
	public VisualizerConfiguration() {
		this.scales = new ArrayList<>();
		this.toolColors = new ArrayList<>();
	}
}
