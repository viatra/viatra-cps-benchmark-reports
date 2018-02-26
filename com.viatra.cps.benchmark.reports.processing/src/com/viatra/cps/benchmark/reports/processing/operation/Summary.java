package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Summary<T> implements Operation {
	protected String attribute;
	protected List<T> filter;

	public Summary(String attribute, List<T> filter) {
		this.attribute = attribute;
		this.filter = filter;
	}

	@Override
	public BenchmarkResult calculate(List<BenchmarkResult> results) {
		// TODO Auto-generated method stub
		System.out.println("calcuate Summary");
		return null;
	}

}
