package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.verticles.operation.Operation;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class NumericOperation extends Operation {
	protected List<BenchmarkResult> results;

	public NumericOperation(String next, String id, String scenario, ObjectMapper mapper) {
		super(next, id, scenario, mapper);
		this.results = new ArrayList<>();
	}

	@Override
	public void addResult(BenchmarkResult result) {
		this.results.add(result);
		this.numberOfResults--;
		if (this.numberOfResults == 0) {
			this.calculate();
		}
	}

	protected abstract void calculate();
}
