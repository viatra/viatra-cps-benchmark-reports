package com.viatra.cps.benchmark.reports.processing.results;

import java.util.List;
import java.util.stream.Collectors;

import eu.mondo.sam.core.results.BenchmarkResult;

public class FilterableBenchmarkResult extends BenchmarkResult{

	public List<FilterablePhaseResult> getPharesultByPhaseNames(List<String> phaseNames){
		List<FilterablePhaseResult> phaseResults = this.phaseResults.stream().filter(phaseResult -> {
			return phaseNames.stream().filter(phaseName -> phaseName.equals(phaseResult.getPhaseName())).findAny().isPresent();
		}).map(e -> new FilterablePhaseResult(e)).collect(Collectors.toList());
		return phaseResults;
	}
	
}
