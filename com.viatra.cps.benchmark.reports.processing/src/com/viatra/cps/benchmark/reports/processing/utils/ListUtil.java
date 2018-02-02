package com.viatra.cps.benchmark.reports.processing.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.viatra.cps.benchmark.reports.processing.models.MetricResult;
import com.viatra.cps.benchmark.reports.processing.models.PhaseResult;
import com.viatra.cps.benchmark.reports.processing.models.BenchmarkResult;

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
	public static List<PhaseResult> getPhaseListByName(List<BenchmarkResult> results, String phaseName) {
		ArrayList<PhaseResult> phaseResults = new ArrayList<>();
		for (BenchmarkResult result : results) {
			phaseResults.addAll(result.getPhaseResults().stream()
					.filter(phaseResult -> phaseResult.getPhaseName().equals(phaseName)).collect(Collectors.toList()));
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
	public static List<List<PhaseResult>> getPhaseListByName(List<BenchmarkResult> results, List<String> phaseNames,
			final int size) {
		ArrayList<PhaseResult> phaseResults = new ArrayList<>();
		ArrayList<List<PhaseResult>> allPhaseResult = new ArrayList<>();
		for (BenchmarkResult result : results) {
			if (result.getCaseDescriptor().getSzie() == size) {
				phaseResults.addAll(result.getPhaseResults().stream().filter(phaseResult -> {
					return phaseNames.stream().filter(phaseName -> phaseName.equals(phaseResult.getPhaseName())).findAny()
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
	public static MetricResult getMetricByName(List<MetricResult> metrics, String metricName) {
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
	public static List<MetricResult> getMetricsByList(List<MetricResult> metrics, List<String> metricNames) {
		return metrics.stream().filter(metric -> {
			return metricNames.stream().filter(metricName -> metricName.equals(metric.getName())).findFirst()
					.isPresent();
		}).collect(Collectors.toList());
	}
}
