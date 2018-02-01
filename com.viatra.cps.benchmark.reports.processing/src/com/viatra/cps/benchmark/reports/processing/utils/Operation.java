package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.ArrayList;
import java.util.List;

import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Result;

public class Operation {

	public static Double avg(List<Result> results, List<String> phaseNames, List<String> metricNames,final int size) {
		final List<Double> values = new ArrayList<>();
		List<List<PhaseResult>> allPhaseResult = ListUtil.getPhaseListByName(results, phaseNames,size);
		allPhaseResult.forEach(phasresults -> {
			phasresults.forEach(phaseResult -> {
				values.add(ListUtil.getMetricByName(phaseResult.getMetrics(), metricNames.get(0)).getValue());
			});
		});
		Double sum = 0.0;
		for (Double value : values) {
			sum += value;
		}
		return sum / allPhaseResult.size();
	}
}
