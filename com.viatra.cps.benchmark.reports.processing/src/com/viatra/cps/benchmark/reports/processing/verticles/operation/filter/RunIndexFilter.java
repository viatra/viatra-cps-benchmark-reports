package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Header;
import com.viatra.cps.benchmark.reports.processing.models.Message;

import eu.mondo.sam.core.results.BenchmarkResult;
import io.vertx.core.AsyncResult;

public class RunIndexFilter extends Filter {

	private Map<String, Map<Integer, List<BenchmarkResult>>> benchmarkMap;

	public RunIndexFilter(List<Object> elements, String next, String id, String scenario, ObjectMapper mapper) {
		super(elements, next, id, scenario, mapper);
		this.benchmarkMap = new HashMap<>();
	}

	private void calculate() {
		Set<String> toolKeys = this.benchmarkMap.keySet();
		this.sendResultsSize(new Header(this.calculateResultsSize(toolKeys), this.operationId),
				(AsyncResult<io.vertx.core.eventbus.Message<Object>> res) -> {
					toolKeys.forEach(tool -> {
						Set<Integer> sizeKey = this.benchmarkMap.get(tool).keySet();
						sizeKey.forEach(size -> {
							List<BenchmarkResult> results = this.benchmarkMap.get(tool).get(size);
							BenchmarkResult filteredResult = createBenchmarkResult(results.get(0));
							filteredResult.getCaseDescriptor().setRunIndex(-1);
							results.forEach(result -> {
								result.getPhaseResults().forEach(phaseResult -> {
									filteredResult.addResults(phaseResult);
								});
							});
							this.sendResult(filteredResult);
						});
					});
					return null;
				});
	}

	private Integer calculateResultsSize(Set<String> toolKeys) {
		Integer size = 0;
		for (String tool : toolKeys) {
			Set<Integer> sizeKey = this.benchmarkMap.get(tool).keySet();
			size += sizeKey.size();
		}
		return size;
	}

	private void addToMap(BenchmarkResult benchmarkResult) {
		Map<Integer, List<BenchmarkResult>> toolMap = this.benchmarkMap
				.get(benchmarkResult.getCaseDescriptor().getTool());
		if (toolMap == null) {
			toolMap = new HashMap<>();
			this.benchmarkMap.put(benchmarkResult.getCaseDescriptor().getTool(), toolMap);
		}
		List<BenchmarkResult> benchmarkList = toolMap.get(benchmarkResult.getCaseDescriptor().getSize());
		if (benchmarkList == null) {
			benchmarkList = new ArrayList<>();
			toolMap.put(benchmarkResult.getCaseDescriptor().getSize(), benchmarkList);
		}
		benchmarkList.add(benchmarkResult);
	}

	private Boolean isNeeded(BenchmarkResult benchmarkResult, List<Object> elements) {
		Boolean need = elements.stream().filter(element -> {
			return ((Integer) element) == (benchmarkResult.getCaseDescriptor().getRunIndex());
		}).findAny().isPresent();
		return need;
	}

	@Override
	public void addResult(BenchmarkResult result) {
		if (this.elements.size() > 0) {
			if (this.isNeeded(result, elements)) {
				this.addToMap(result);
			}
		} else {
			this.addToMap(result);
		}
		this.numberOfResults--;
		if (this.numberOfResults == 0) {
			this.calculate();
		}
	}
}
