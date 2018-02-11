package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

import eu.mondo.sam.core.results.MetricResult;

public class Result {

	@JsonProperty("size")
	protected Integer size;

	@JsonProperty("metric")
	MetricResult metric;

	public Result() {
	}

	public Result(Integer size) {
		this.size = size;
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
