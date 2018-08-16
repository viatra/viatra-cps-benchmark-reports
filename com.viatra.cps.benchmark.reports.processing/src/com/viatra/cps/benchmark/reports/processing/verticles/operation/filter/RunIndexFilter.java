package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.NumericOperation;

import eu.mondo.sam.core.results.BenchmarkResult;

public class RunIndexFilter extends Filter {

	private Map<String, Map<Integer, List<BenchmarkResult>>> benchmarkMap;

	public RunIndexFilter(List<Object> elements, Operation next, Boolean contained, String id) {
		super(elements, next, contained, id);
		this.benchmarkMap = new HashMap<>();
	}

	public RunIndexFilter(List<Object> elements, Boolean contained, String id) {
		super(elements, contained, id);
		this.benchmarkMap = new HashMap<>();
	}

	@Override
	public void run() {
		while (this.running || !this.queue.isEmpty()) {
			BenchmarkResult benchmarkResult = null;
			try {
				benchmarkResult = this.queue.poll(10, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (benchmarkResult != null) {

				if (this.elements.size() > 0) {
					if (this.isNeeded(benchmarkResult, elements)) {
						this.addToMap(benchmarkResult);
					}
				} else {
					this.addToMap(benchmarkResult);
				}

			}
		}
		this.calculate();

	}

	private void calculate() {
		Set<String> toolKeys = this.benchmarkMap.keySet();
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
				if (this.next != null) {
					if (this.contained) {
						((NumericOperation) this.next).addFilteredResult(filteredResult);
					} else {
						this.next.addResult(filteredResult);
					}
				}
			});
		});
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
}
