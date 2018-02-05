package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Data {

	@JsonProperty("benchmark")
	List<Benchmark> benchmarks;

	public Data() {
		benchmarks = new ArrayList<>();
	}

	public List<Benchmark> getBenchmarks() {
		return benchmarks;
	}

	public void setBenchmarks(List<Benchmark> benchmarks) {
		this.benchmarks = benchmarks;
	}
	
	
}
