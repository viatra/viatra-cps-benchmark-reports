package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Unit {
	@JsonProperty("Label")
	protected String label;

	@JsonProperty("Value")
	protected Double value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Unit() {
		this.value = 1.0;
		this.label = "u";
	}
}
