package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Summary extends Operation {

	public Summary(String attribute, List<Object> filter) {
		super(attribute, filter);
	}

	@Override
	public BenchmarkResult calculate(List<BenchmarkResult> results) {
		// TODO Auto-generated method stub
		System.out.println("calcuate Summary");
		return null;
	}

}
