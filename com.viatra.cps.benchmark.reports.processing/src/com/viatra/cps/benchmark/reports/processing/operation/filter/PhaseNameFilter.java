package com.viatra.cps.benchmark.reports.processing.operation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.NumericOperation;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.PhaseResult;

public class PhaseNameFilter extends Filter {

	private Map<String, List<PhaseResult>> phaseMap;

	public PhaseNameFilter(List<Object> elements, Boolean contained) {
		super(elements, contained);
	}

	public PhaseNameFilter(List<Object> elements, Operation next, Boolean contained) {
		super(elements, next, contained);
	}

	@Override
	public void run() {
		while (this.running || !this.queue.isEmpty()) {
			BenchmarkResult benchmarkResult = null;
			synchronized (this.lock) {
				// Wait for result
				if (this.queue.isEmpty()) {
					try {
						this.lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// Work on result
				benchmarkResult = this.queue.poll();
			}
			this.phaseMap = new HashMap<>();
			benchmarkResult.getPhaseResults().forEach(phaseResult -> {
				if (this.elements.size() > 0) {
					if (this.isNeeded(phaseResult, elements)) {
						this.addToMap(phaseResult);
					}
				} else {
					this.addToMap(phaseResult);
				}
			});
			this.transform(benchmarkResult);
		}
		if(!this.contained && this.next != null) {
			this.next.stop();
		}
	}

	private void transform(BenchmarkResult benchmarkResult) {
		Set<String> phanesNames = this.phaseMap.keySet();
		phanesNames.forEach(phaseName ->{
			BenchmarkResult filteredResult = this.createBenchmarkResult(benchmarkResult);
			this.phaseMap.get(phaseName).forEach(phase -> {
				filteredResult.addResults(phase);
			});
			if(next != null) {
				if(this.contained) {
					((NumericOperation)this.next).addFilteredResult(filteredResult);
				}else {
					this.next.addResult(filteredResult);
				}

			}
		});
		
	}

	private void addToMap(PhaseResult phaseResult) {
		List<PhaseResult> phaseList = this.phaseMap.get(phaseResult.getPhaseName());
		if (phaseList == null) {
			phaseList = new ArrayList<>();
			this.phaseMap.put(phaseResult.getPhaseName(), phaseList);
		}
		phaseList.add(phaseResult);
	}

	private BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

	private Boolean isNeeded(PhaseResult phaseResult, List<Object> elements) {
		Boolean need = elements.stream().filter(phaseName -> {
			return ((String) phaseName).equals(phaseResult.getPhaseName());
		}).findAny().isPresent();
		return need;
	}

}
