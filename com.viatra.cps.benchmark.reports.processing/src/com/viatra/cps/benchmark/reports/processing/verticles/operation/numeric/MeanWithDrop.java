package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.codehaus.jackson.map.ObjectMapper;
import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;

public class MeanWithDrop extends Mean {

	public MeanWithDrop(String next, String id, String scenario, ObjectMapper mapper) {
		super(next, id, scenario, mapper);
	}

	@Override
	protected void calculate() {
		this.sendResultsSize(new Integer(this.results.size()).toString(), (AsyncResult<Message<Object>> res) -> {
			for (BenchmarkResult result : this.results) {
				DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
				Double min = Double.MAX_VALUE;
				Double max = Double.MIN_VALUE;
				PhaseResult minRes = null;
				PhaseResult maxRes = null;
				for (PhaseResult pRes : result.getPhaseResults()) {
					if (pRes.getMetrics().get(0).getValue() < min) {
						min = pRes.getMetrics().get(0).getValue();
						minRes = pRes;
					}
					if (pRes.getMetrics().get(0).getValue() > max) {
						max = pRes.getMetrics().get(0).getValue();
						maxRes = pRes;
					}
				}
				if (result.getPhaseResults().size() > 2) {
					result.getPhaseResults().remove(minRes);
					result.getPhaseResults().remove(maxRes);
				}
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
