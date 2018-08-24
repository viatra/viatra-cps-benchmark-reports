package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Metric {
	@JsonProperty("Units")
	protected List<Unit> units;

	public List<Unit> getUnits() {
		return units;
	}

	@JsonProperty("Title")
	public String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public Metric() {
		this.units = new ArrayList<>();
		this.title = "Default";
		this.units.add(new Unit());
	}
}
