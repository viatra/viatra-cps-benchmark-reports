package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ResultConfig {
	
	@JsonProperty("Scale")
	protected List<Scale> scales;
	
	@JsonProperty("Build")
	protected List<Build> builds;

	public List<Scale> getScales() {
		return scales;
	}

	public void setScales(List<Scale> scales) {
		this.scales = scales;
	}

	public List<Build> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}
	

}
