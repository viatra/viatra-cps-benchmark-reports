package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.ArrayList;
import java.util.List;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.PhaseResult;

public class Operation {

	public static Double avg(List<BenchmarkResult> results, List<String> phaseNames, List<String> metricNames,
			final int size) {
		final List<Double> values = new ArrayList<>();
		List<List<PhaseResult>> allPhaseResult = ListUtil.getPhaseListByName(results, phaseNames, size);
		if (allPhaseResult != null) {
			allPhaseResult.forEach(phasresults -> {
				phasresults.forEach(phaseResult -> {
					values.add(ListUtil.getMetricByName(phaseResult.getMetrics(), metricNames.get(0)).getValue());
				});
			});
		}
		Double sum = 0.0;
		for (Double value : values) {
			sum += value;
		}
		if (allPhaseResult != null) {
			return sum / allPhaseResult.size();
		} else {
			return -1.0;
		}
	}
}