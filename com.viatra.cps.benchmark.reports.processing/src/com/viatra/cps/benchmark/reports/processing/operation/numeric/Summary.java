package com.viatra.cps.benchmark.reports.processing.operation.numeric;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Summary extends NumericOperation {

	public Summary(Filter filter, Operation next) {
		super(filter, next);
	}

	@Override
	protected void calculate() {
		System.out.println("calcuate Summary");
		if (filter != null) {
			this.filter.start();
		}
	}

	@Override
	public void run() {
		this.calculate();
	}

	@Override
	public void addFilteredResult(BenchmarkResult result) {
		result.getPhaseResults().forEach(phaseResult -> {
			System.out.print(phaseResult.getMetrics().size() + " " + phaseResult.getPhaseName() + " ");
			phaseResult.getMetrics().forEach(action -> {
				System.out.println(action.getName());
			});
		});
	}

	@Override
	public void addResult(BenchmarkResult result) {
		System.out.println("RunIndex: " +result.getCaseDescriptor().getRunIndex());
		System.out.println("Tool: " + result.getCaseDescriptor().getTool());
		System.out.println("Size: " + result.getCaseDescriptor().getSize());
		System.out.println("Function count: " + result.getPhaseResults().size());
		System.out.println("Function: " + result.getPhaseResults().get(0).getPhaseName());
		System.out.println("Function: " + result.getPhaseResults().get(0).getMetrics().get(0).getValue());
		System.out.println();
	}

}
