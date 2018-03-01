package com.viatra.cps.benchmark.reports.processing.operation;


import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Operation {
	protected String attribute;
	protected List<Object> filter;
	public Operation(String attribute,List<Object> filter) {
		this.attribute = attribute;
		this.filter = filter;
	}
	public abstract BenchmarkResult calculate(List<BenchmarkResult> results);
}
