package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PhaseResult {

	@JsonProperty("PhaseName")
	protected String phaseName;

	@JsonProperty("Sequence")
	protected int sequence;

	@JsonProperty("Metrics")
	protected List<MetricResult> metrics;

	public PhaseResult() {
		this.metrics = new ArrayList<>();
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public List<MetricResult> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<MetricResult> metrics) {
		this.metrics = metrics;
	}

}
