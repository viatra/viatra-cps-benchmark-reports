package com.viatra.cps.benchmark.reports.processing.models;

import org.codehaus.jackson.annotate.JsonProperty;

import eu.mondo.sam.core.results.BenchmarkResult;

public class Data {

	@JsonProperty("oprationId")
	protected String operationId;

	@JsonProperty("result")
	protected BenchmarkResult result;

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public BenchmarkResult getResult() {
		return result;
	}

	public void setResult(BenchmarkResult result) {
		this.result = result;
	}

	public Data() {
	}

	public Data(String id, BenchmarkResult result) {
		this.operationId = id;
		this.result = result;
	}
}
