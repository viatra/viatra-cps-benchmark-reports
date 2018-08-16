package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.Operation;
import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Filter extends Operation {
	protected List<Object> elements;

	public Filter(List<Object> elements, String next, String id, String scenario, ObjectMapper mapper) {
		this.elements = elements;
		this.ID = id;
		this.next = next;
		this.scenario = scenario;
		this.mapper = mapper;
	}

	protected BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

}
