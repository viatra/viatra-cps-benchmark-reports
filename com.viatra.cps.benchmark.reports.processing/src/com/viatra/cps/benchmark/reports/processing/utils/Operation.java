package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.Arrays;
import java.util.List;


import com.viatra.cps.benchmark.reports.processing.models.AggregatedPhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Metric;
import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;

public class Operation {

	public static AggregatedPhaseResult sum(List<PhaseResult> phaseresults, String metricName) {
		String name = phaseresults.get(0).getName();
		Long summary = 0L;
		for (PhaseResult phaseResult : phaseresults) {
			summary += ListUtil.getMetricByName(phaseResult.getMetrics(), metricName).getValue();
		}
		return new AggregatedPhaseResult(name, -1, Arrays.asList(new Metric(metricName, summary)), "SUM");
	}
}
