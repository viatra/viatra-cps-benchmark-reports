package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Result {

	protected Integer size;

	MetricResult metrics;

	public Result() {
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public MetricResult getMetrics() {
		return metrics;
	}

	public void setMetrics(MetricResult metrics) {
		this.metrics = metrics;
	}
	
	
}
