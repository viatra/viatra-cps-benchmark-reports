package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Header;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.PhaseResult;
import io.vertx.core.AsyncResult;

public class PhaseNameFilter extends Filter {

	private Map<String, List<PhaseResult>> phaseMap;
	private List<BenchmarkResult> results;

	public PhaseNameFilter(List<Object> elements, String next, String id, String scenario, ObjectMapper mapper) {
		super(elements, next, id, scenario, mapper);
		this.results = new ArrayList<>();
	}

	private void transform(BenchmarkResult benchmarkResult) {
		Set<String> phanesNames = this.phaseMap.keySet();
		phanesNames.forEach(phaseName -> {
			BenchmarkResult filteredResult = this.createBenchmarkResult(benchmarkResult);
			this.phaseMap.get(phaseName).forEach(phase -> {
				filteredResult.addResults(phase);
			});
			this.results.add(filteredResult);
		});
		this.numberOfResults--;
		if (this.numberOfResults == 0) {
			this.sendResultsSize(new Header(this.results.size(), this.operationId),
					(AsyncResult<io.vertx.core.eventbus.Message<Object>> res) -> {
						for (BenchmarkResult newResult : this.results) {
							this.sendResult(newResult);
						}
						return null;
					});
		}
	}

	private void addToMap(PhaseResult phaseResult) {
		List<PhaseResult> phaseList = this.phaseMap.get(phaseResult.getPhaseName());
		if (phaseList == null) {
			phaseList = new ArrayList<>();
			this.phaseMap.put(phaseResult.getPhaseName(), phaseList);
		}
		phaseList.add(phaseResult);
	}

	private Boolean isNeeded(PhaseResult phaseResult, List<Object> elements) {
		Boolean need = elements.stream().filter(phaseName -> {
			return ((String) phaseName).equals(phaseResult.getPhaseName());
		}).findAny().isPresent();
		return need;
	}

	@Override
	public void addResult(BenchmarkResult result) {
		this.phaseMap = new HashMap<>();
		result.getPhaseResults().forEach(phaseResult -> {
			if (this.elements.size() > 0) {
				if (this.isNeeded(phaseResult, elements)) {
					this.addToMap(phaseResult);
				}
			} else {
				this.addToMap(phaseResult);
			}
		});
		this.transform(result);
	}
}
