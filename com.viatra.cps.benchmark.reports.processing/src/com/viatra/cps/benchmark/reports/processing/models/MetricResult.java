package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonSetter;

public class MetricResult {

	
	private String metricName;

	private Double value;

	public String getName() {
		return metricName;
	}

	@JsonSetter("MetricName")
	public void setName(String metricName) {
		this.metricName = metricName;
	}

	public Double getValue() {
		return value;
	}

	@JsonSetter("MetricValue")
	public void setValue(String value) {
		this.value = Double.parseDouble(value);
	}

	public void setValue(Double value) {
		this.value = value;
	}

}
