package com.viatra.cps.benchmark.reports.processing.operation.filter;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MetricFilter extends Filter {

	public MetricFilter(List<Object> elements, Operation next, Boolean contained) {
		super(elements, next, contained);
	}

	public MetricFilter(List<Object> elements, Boolean contained) {
		super(elements, contained);
	}
	
	@Override
	public boolean start() {
		super.start();
		return true;
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
			if (benchmarkResult != null) {
				BenchmarkResult filteredResult = this.createBenchmarkResult(benchmarkResult);

				benchmarkResult.getPhaseResults().forEach(phaseResult -> {
					if (elements.size() > 0) {
						List<MetricResult> metricResults = phaseResult.getMetrics().stream().filter(metric -> {
							return elements.stream().filter(element -> {
								return ((String) element).equals(metric.getName());
							}).findAny().isPresent();
						}).collect(Collectors.toList());
						metricResults.forEach(metricResult -> {
							filteredResult.addResults(createPhaseResult(phaseResult, metricResult));
						});
					} else {
						phaseResult.getMetrics().forEach(metricResult -> {
							filteredResult.addResults(createPhaseResult(phaseResult, metricResult));
						});
					}
				});
				if (next != null) {
					next.addResult(filteredResult);
				}
			}
		}
		if (!this.contained && next != null) {
			next.stop();
		}
	}

	private BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

	private PhaseResult createPhaseResult(PhaseResult phaseResult, MetricResult metricResult) {
		PhaseResult newRes = new PhaseResult();
		newRes.setPhaseName(phaseResult.getPhaseName());
		newRes.setSequence(phaseResult.getSequence());
		newRes.setMetrics(Arrays.asList(metricResult));
		return newRes;
	}

}
