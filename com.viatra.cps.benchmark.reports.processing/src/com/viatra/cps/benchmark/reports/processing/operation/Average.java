package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Average<T> implements Operation {
	protected String attribute;
	protected List<T> filter;

	public Average(String attribute, List<T> filter) {
		this.attribute = attribute;
		this.filter = filter;
	}

	@Override
	public BenchmarkResult calculate(List<BenchmarkResult> results) {
		System.out.println("calcuate Average");
		return null;
	}
}
