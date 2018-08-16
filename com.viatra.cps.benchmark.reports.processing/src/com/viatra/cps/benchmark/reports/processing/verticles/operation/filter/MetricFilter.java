package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Message;

public class MetricFilter extends Filter {

	public MetricFilter(List<Object> elements, String next, String id, String scenario, ObjectMapper mapper) {
		super(elements, next, id, scenario, mapper);
	}

	private PhaseResult createPhaseResult(PhaseResult phaseResult, MetricResult metricResult) {
		PhaseResult newRes = new PhaseResult();
		newRes.setPhaseName(phaseResult.getPhaseName());
		newRes.setSequence(phaseResult.getSequence());
		newRes.setMetrics(Arrays.asList(metricResult));
		return newRes;
	}

	@Override
	public void addResult(BenchmarkResult result) {
		BenchmarkResult filteredResult = this.createBenchmarkResult(result);
		result.getPhaseResults().forEach(phaseResult -> {
			if (elements.size() > 0) {
				List<MetricResult> metricResults = phaseResult.getMetrics().stream().filter(metric -> {
					return elements.stream().filter(element -> {
						return ((String) element).equals(metric.getName());
					}).findAny().isPresent();
				}).collect(Collectors.toList());
				metricResults.forEach(metricResult -> {
					filteredResult.addResults(createPhaseResult(phaseResult, metricResult));
				});
			} else {
				phaseResult.getMetrics().forEach(metricResult -> {
					filteredResult.addResults(createPhaseResult(phaseResult, metricResult));
				});
			}
		});
		this.sendResults(filteredResult);
	}
}
