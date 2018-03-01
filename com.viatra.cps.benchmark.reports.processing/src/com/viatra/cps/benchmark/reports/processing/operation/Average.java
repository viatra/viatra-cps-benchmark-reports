package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Average extends Operation {

	public Average(String attribute, List<Object> filter) {
		super(attribute, filter);
	}

	@Override
	public BenchmarkResult calculate(List<BenchmarkResult> results) {
		System.out.println("calcuate Average");
		return null;
	}
}
