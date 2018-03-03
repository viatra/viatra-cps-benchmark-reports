package com.viatra.cps.benchmark.reports.processing.operation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.NumericOperation;

import eu.mondo.sam.core.results.BenchmarkResult;

public class ToolFilter extends Filter {
	private Map<String, Map<Integer, Map<Integer, List<BenchmarkResult>>>> benchmarkMap;

	public ToolFilter(List<Object> elements, Boolean contained) {
		super(elements, contained);
		this.benchmarkMap = new HashMap<>();
	}

	public ToolFilter(List<Object> elements, Operation next, Boolean contained) {
		super(elements, next, contained);
		this.benchmarkMap = new HashMap<>();
	}

	@Override
	public void run() {
		while (this.running || !this.queue.isEmpty()) {
			BenchmarkResult benchmarkResult = null;
			synchronized (this.lock) {
				if (this.queue.isEmpty()) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			benchmarkResult = this.queue.poll();
			if (this.elements.size() > 0) {
				if (this.isNeeded(benchmarkResult, elements)) {
					this.addToMap(benchmarkResult);
				}
			} else {
				this.addToMap(benchmarkResult);
			}

		}
		this.calculate();
	}

	private void calculate() {
		Set<String> toolKeys = this.benchmarkMap.keySet();
		toolKeys.forEach(tool -> {
			Set<Integer> sizeKey = this.benchmarkMap.get(tool).keySet();
			sizeKey.forEach(size -> {
				Set<Integer> runKeys = this.benchmarkMap.get(tool).get(size).keySet();
				runKeys.forEach(runIndex -> {
					List<BenchmarkResult> results = this.benchmarkMap.get(tool).get(size).get(runIndex);
					BenchmarkResult filteredResult = createBenchmarkResult(results.get(0));
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
		});
	}

	private BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

	private void addToMap(BenchmarkResult benchmarkResult) {
		Map<Integer, Map<Integer, List<BenchmarkResult>>> toolMap = this.benchmarkMap.get(benchmarkResult.getCaseDescriptor().getTool());
		if (toolMap == null) {
			toolMap = new HashMap<>();
			this.benchmarkMap.put(benchmarkResult.getCaseDescriptor().getTool(), toolMap);
		}
		Map<Integer, List<BenchmarkResult>> indexMap = toolMap.get(benchmarkResult.getCaseDescriptor().getSize());
		if (indexMap == null) {
			indexMap = new HashMap<>();
			toolMap.put(benchmarkResult.getCaseDescriptor().getSize(), indexMap);
		}
		List<BenchmarkResult> benchmarkList = indexMap.get(benchmarkResult.getCaseDescriptor().getRunIndex());
		if (benchmarkList == null) {
			benchmarkList = new ArrayList<>();
			indexMap.put(benchmarkResult.getCaseDescriptor().getRunIndex(), benchmarkList);
		}
		benchmarkList.add(benchmarkResult);
	}

	private Boolean isNeeded(BenchmarkResult benchmarkResult, List<Object> elements) {
		Boolean need = elements.stream().filter(element -> {
			return ((String) element).equals(benchmarkResult.getCaseDescriptor().getTool());
		}).findAny().isPresent();
		return need;
	}
}
