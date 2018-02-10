package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

import eu.mondo.sam.core.results.MetricResult;

public class Result {

	@JsonProperty("Size")
	protected Integer size;

	@JsonProperty("Metric")
	MetricResult metric;

	public Result() {
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public MetricResult getMetrics() {
		return metric;
	}

	public void setMetrics(MetricResult metric) {
		this.metric = metric;
	}

}
