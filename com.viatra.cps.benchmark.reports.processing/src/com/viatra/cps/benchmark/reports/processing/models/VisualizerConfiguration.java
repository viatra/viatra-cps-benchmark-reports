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

	@JsonProperty("Metric")
	protected List<Metric> metrics;

	public List<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

	public List<Scale> getScales() {
		return scales;
	}

	public void setScales(List<Scale> scales) {
		this.scales = scales;
	}

	public VisualizerConfiguration() {
		this.metrics = new ArrayList<>();
		this.metrics.add(new Metric());
		this.scales = new ArrayList<>();
		this.toolColors = new ArrayList<>();
	}
}
