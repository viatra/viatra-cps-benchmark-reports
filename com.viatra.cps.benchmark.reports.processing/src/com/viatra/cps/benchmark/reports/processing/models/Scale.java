package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Scale {

	@JsonProperty("Title")
	protected String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("Metric")
	protected String metric;

	@JsonProperty("DefaultScale")
	protected String defaultScale;

	@JsonProperty("ActualtScale")
	protected String actualScale;

	public String getActualScale() {
		return actualScale;
	}

	public void setActualScale(String actualScale) {
		this.actualScale = actualScale;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getDefaultScale() {
		return defaultScale;
	}

	public void setDefaultScale(String defaultScale) {
		this.defaultScale = defaultScale;
	}

	public Scale() {
		this.metric = "Default";
	}

	public Scale(String title) {
		this.title = title;
		this.defaultScale = "u";
		this.metric = "Default";
		this.actualScale = "u";
	}

}
