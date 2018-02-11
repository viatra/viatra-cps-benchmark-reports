package com.viatra.cps.benchmark.reports.processing.results;

import eu.mondo.sam.core.results.PhaseResult;

public class FilterablePhaseResult extends PhaseResult {

	public FilterablePhaseResult(PhaseResult phaseResult) {
		this.phaseName = phaseResult.getPhaseName();
		this.sequence = phaseResult.getSequence();
		this.metrics = phaseResult.getMetrics();
	}
}
