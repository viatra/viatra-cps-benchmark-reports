package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.viatra.cps.benchmark.reports.processing.models.Metric;
import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.Result;

/**
 * 
 * @author zole4
 *
 */
public class ListUtil {
	/**
	 * Get all Phaseresult which name has equals phasName input
	 * 
	 * @param results
	 *            Result list
	 * @param phaseName
	 *            name of phase
	 * @return list of PhaseResult
	 */
	public static List<PhaseResult> getPhaseListByName(List<Result> results, String phaseName) {
		ArrayList<PhaseResult> phaseResults = new ArrayList<>();
		for (Result result : results) {
			phaseResults.addAll(result.getPhasesResults().stream()
					.filter(phaseResult -> phaseResult.getName().equals(phaseName)).collect(Collectors.toList()));
		}
		return phaseResults;
	}

	/**
	 * Get all Phaseresult which name has equals phasName input
	 * 
	 * @param results
	 *            Result list
	 * @param phaseName
	 *            name of phase
	 * @return list of PhaseResult
	 */
	public static List<List<PhaseResult>> getPhaseListByName(List<Result> results, List<String> phaseNames,
			final int size) {
		ArrayList<PhaseResult> phaseResults = new ArrayList<>();
		ArrayList<List<PhaseResult>> allPhaseResult = new ArrayList<>();
		for (Result result : results) {
			if (result.getCase().getSzie() == size) {
				phaseResults.addAll(result.getPhasesResults().stream().filter(phaseResult -> {
					return phaseNames.stream().filter(phaseName -> phaseName.equals(phaseResult.getName())).findAny()
							.isPresent();
				}).collect(Collectors.toList()));
				allPhaseResult.add(phaseResults);
			}
		}
		return allPhaseResult;
	}

	/**
	 * Get metric which name has equals metricName input
	 * 
	 * @param metrics
	 *            list of metrics
	 * @param metricName
	 *            name of metric
	 * @return Metric
	 */
	public static Metric getMetricByName(List<Metric> metrics, String metricName) {
		return metrics.stream().filter(metric -> metric.getName().equals(metricName)).findFirst().get();
	}

	/**
	 * Get all metric which name has equals any element of metricNames
	 * 
	 * @param metrics
	 *            List of metric
	 * @param metricNames
	 *            List of metricNames
	 * @return List of Metric
	 */
	public static List<Metric> getMetricsByList(List<Metric> metrics, List<String> metricNames) {
		return metrics.stream().filter(metric -> {
			return metricNames.stream().filter(metricName -> metricName.equals(metric.getName())).findFirst()
					.isPresent();
		}).collect(Collectors.toList());
	}
}
