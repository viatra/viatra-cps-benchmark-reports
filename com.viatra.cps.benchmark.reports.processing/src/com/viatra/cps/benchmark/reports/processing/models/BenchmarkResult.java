package com.viatra.cps.benchmark.reports.processing.models;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class BenchmarkResult {

	@JsonProperty("Case")
	protected CaseDescriptor caseDescriptor;

	@JsonProperty("PhaseResults")
	protected List<PhaseResult> phaseResults;

	public CaseDescriptor getCaseDescriptor() {
		return caseDescriptor;
	}

	public void setCaseDescriptor(CaseDescriptor caseDescriptor) {
		this.caseDescriptor = caseDescriptor;
	}

	public List<PhaseResult> getPhaseResults() {
		return phaseResults;
	}

	public void setPhaseResults(List<PhaseResult> phaseResults) {
		this.phaseResults = phaseResults;
	}
	

	public BenchmarkResult() {
		phaseResults = new ArrayList<>();
	}
}
