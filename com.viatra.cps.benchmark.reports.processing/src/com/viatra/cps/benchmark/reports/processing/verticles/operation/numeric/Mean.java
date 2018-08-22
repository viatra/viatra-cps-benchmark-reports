package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Header;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;

public class Mean extends NumericOperation {

	public Mean(String next, String id, String scenario, ObjectMapper mapper) {
		super(next, id, scenario, mapper);
	}

	@Override
	protected void calculate() {
		this.sendResultsSize(new Header(results.size(), this.operationId),
				(AsyncResult<Message<Object>> res) -> {
					for (BenchmarkResult result : this.results) {
						DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
						for (PhaseResult pRes : result.getPhaseResults()) {
							descriptiveStatistics.addValue(pRes.getMetrics().get(0).getValue());
						}
						MetricResult metric = new MetricResult();
						metric.setValue(descriptiveStatistics.getMean());
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
