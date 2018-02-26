package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Summary<T> extends Operation<T> {

	public Summary(String attribute, List<T> filter) {
		super(attribute, filter);
	}

	@Override
	public BenchmarkResult calculate() {
		// TODO Auto-generated method stub
		return null;
	}

}
