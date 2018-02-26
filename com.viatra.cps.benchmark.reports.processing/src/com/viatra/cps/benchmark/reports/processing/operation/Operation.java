package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Operation<T> {

	protected String attribute;
	protected List<T> filter;
	
	public Operation(String attribute, List<T> filter) {
		this.attribute = attribute;
		this.filter = filter;
	}
	
	public abstract BenchmarkResult calculate();
}
