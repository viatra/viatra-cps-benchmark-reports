package com.viatra.cps.benchmark.reports.processing.models;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class BenchmarkResult {

	@JsonProperty("Case")
	private CaseDescriptor caseDescriptor;

	@JsonProperty("PhaseResults")
	private List<PhaseResult> phaseResults;

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
		// TODO Auto-generated constructor stub
	}
}
