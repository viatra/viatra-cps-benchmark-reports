package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.models.Header;
import com.viatra.cps.benchmark.reports.processing.models.Message;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.Operation;
import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Filter extends Operation {
	protected List<Object> elements;

	public Filter(List<Object> elements, String next, String id, String scenario, ObjectMapper mapper) {
		super(next, id, scenario, mapper);
		this.elements = elements;
	}

	protected BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

	@Override
	protected void resultsSizeReceived(Message message, io.vertx.core.eventbus.Message<Object> m) {
		try {
			Header header = mapper.readValue(message.getData(), Header.class);
			this.numberOfResults = header.getSize();
			this.operationId = header.getOperationId();
			m.reply("");
		} catch (Exception e) {
			m.fail(20, e.getMessage());
		}
	}
}
