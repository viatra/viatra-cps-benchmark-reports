package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.Arrays;

import org.codehaus.jackson.map.ObjectMapper;
import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;

public class Summary extends NumericOperation {

	public Summary(String next, String id, String scenario, ObjectMapper mapper) {
		super(next, id, scenario, mapper);
	}

	@Override
	public void calculate() {
		this.sendResultsSize(new Integer(this.results.size()).toString(), (AsyncResult<Message<Object>> res) -> {
			for (BenchmarkResult result : this.results) {
				Double sum = 0.0;
				for (PhaseResult pRes : result.getPhaseResults()) {
					sum += pRes.getMetrics().get(0).getValue();
				}
				MetricResult metric = new MetricResult();
				metric.setValue(sum);
				BenchmarkResult newResult = new BenchmarkResult();
				newResult.setCaseDescriptor(result.getCaseDescriptor());
				PhaseResult phase = new PhaseResult();
				phase.setPhaseName(result.getPhaseResults().get(0).getPhaseName());
				phase.setMetrics(Arrays.asList(metric));
				newResult.addResults(phase);
				this.sendResult(newResult);
			}
			return null;
		});
	}
}
