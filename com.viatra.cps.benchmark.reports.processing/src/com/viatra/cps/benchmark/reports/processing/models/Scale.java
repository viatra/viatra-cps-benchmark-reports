package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Scale {
	
	@JsonProperty("Metric")
	protected String metric;
	
	@JsonProperty("DefaultScale")
	protected Integer defaultScale;

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public Integer getDefaultScale() {
		return defaultScale;
	}

	public void setDefaultScale(Integer defaultScale) {
		this.defaultScale = defaultScale;
	}

}
