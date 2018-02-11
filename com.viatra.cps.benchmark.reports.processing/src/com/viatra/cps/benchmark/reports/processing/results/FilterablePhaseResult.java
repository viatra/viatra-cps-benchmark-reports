package com.viatra.cps.benchmark.reports.processing.results;

import java.util.List;
import java.util.stream.Collectors;

import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

public class FilterablePhaseResult extends PhaseResult {

	public FilterablePhaseResult(PhaseResult phaseResult) {
		this.phaseName = phaseResult.getPhaseName();
		this.sequence = phaseResult.getSequence();
		this.metrics = phaseResult.getMetrics();
	}

	public List<MetricResult> getMetricsByMetricName(List<String> metricNames) {
		return this.metrics.stream().filter(
				metric -> metricNames.stream().filter(name -> name.equals(metric.getName())).findAny().isPresent())
				.collect(Collectors.toList());
	}
}
