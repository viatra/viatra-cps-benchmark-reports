package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Summary extends Operation {

	public Summary(String attribute, List<Object> filter) {
		super(attribute, filter);
	}

	@Override
	public void calculate() {
		System.out.println("calcuate Summary");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
