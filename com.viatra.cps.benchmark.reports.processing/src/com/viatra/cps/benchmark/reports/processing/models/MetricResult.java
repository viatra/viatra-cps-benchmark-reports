package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class MetricResult {

	@JsonProperty("MetricName")
	private String name;

	@JsonProperty("MetricValue")
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return Double.parseDouble(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

}
