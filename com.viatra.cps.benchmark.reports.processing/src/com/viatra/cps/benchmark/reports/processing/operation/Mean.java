package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Mean extends Operation {

	public Mean(String attribute, List<Object> filter) {
		super(attribute, filter);
	}

	@Override
	public void calculate() {
		// TODO Auto-generated method stub
		System.out.println("calcuate Mean");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
