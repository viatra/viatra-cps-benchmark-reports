package com.viatra.cps.benchmark.reports.processing.operation;


import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;

public interface Operation {
	
	public abstract BenchmarkResult calculate(List<BenchmarkResult> results);
}
