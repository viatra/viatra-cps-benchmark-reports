package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Scale {
	
	@JsonProperty("Metric")
	protected String metric;
	
	@JsonProperty("DefaultScale")
	protected String defaultScale;
	
	@JsonProperty("Units")
	protected List<Unit> units;
	
	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
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
		this.units = new ArrayList<>();
	}
	
	public Scale(String metric) {
		this.units = new ArrayList<>();
		this.metric = metric;
		this.defaultScale = "unit";
		this.units.add(new Unit());
	}

}
