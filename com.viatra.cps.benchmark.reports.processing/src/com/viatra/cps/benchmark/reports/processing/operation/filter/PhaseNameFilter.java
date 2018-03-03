package com.viatra.cps.benchmark.reports.processing.operation.filter;

import java.util.List;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.NumericOperation;

import eu.mondo.sam.core.results.BenchmarkResult;

public class PhaseNameFilter extends Filter {

	public PhaseNameFilter(List<Object> elements, Boolean contained) {
		super(elements, contained);
	}

	public PhaseNameFilter(List<Object> elements, Operation next, Boolean contained) {
		super(elements, next, contained);
	}

	@Override
	public void run() {
		while (this.running || !this.queue.isEmpty()) {
			BenchmarkResult benchmarkResult = null;
			synchronized (this.lock) {
				// Wait for result
				if (this.queue.isEmpty()) {
					try {
						this.lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// Work on result
				benchmarkResult = this.queue.poll();
			}
			BenchmarkResult filteredResult = this.createBenchmarkResult(benchmarkResult);
			if (this.elements.size() > 0) {
				benchmarkResult.getPhaseResults().forEach(phaseResult -> {
					Boolean need = elements.stream().filter(phaseName -> {
						return ((String) phaseName).equals(phaseResult.getPhaseName());
					}).findAny().isPresent();
					if (need) {
						filteredResult.addResults(phaseResult);
					}
				});
			} else {
				if (this.next != null) {
					if (this.contained) {
						((NumericOperation) this.next).addFilteredResult(benchmarkResult);
					} else {
						this.next.addResult(benchmarkResult);
					}
				}
			}
			if (this.next != null) {
				if (this.contained) {
					((NumericOperation) this.next).addFilteredResult(filteredResult);
				} else {
					this.next.addResult(filteredResult);
				}
			}
		}
		if(!this.contained && this.next != null) {
			this.next.stop();
		}
	}

	private BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

}
