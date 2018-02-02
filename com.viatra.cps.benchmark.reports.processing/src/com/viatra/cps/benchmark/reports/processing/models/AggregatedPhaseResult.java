package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

public class AggregatedPhaseResult extends PhaseResult {
	String operation;

	public AggregatedPhaseResult(String name, int sequence, List<MetricResult> metrics, String operation) {
	//	super(name, sequence, metrics);
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}
}
