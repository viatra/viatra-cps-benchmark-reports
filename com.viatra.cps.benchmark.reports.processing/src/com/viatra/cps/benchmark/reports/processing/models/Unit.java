package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Unit {
	@JsonProperty("Label")
	protected String label;

	@JsonProperty("Value")
	protected List<Double> value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Double> getValue() {
		return value;
	}

	public void setValue(List<Double> value) {
		this.value = value;
	}

	public Unit() {
		this.value = new ArrayList<>();
		this.value.add(1.0);
		this.value.add(1.0);
		this.label = "u";
	}
}
