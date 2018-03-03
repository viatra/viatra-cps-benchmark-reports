package com.viatra.cps.benchmark.reports.processing.operation;

import eu.mondo.sam.core.results.BenchmarkResult;

public interface Operation extends Runnable {


	public boolean start();

	public void addResult(BenchmarkResult result);

	public void stop();

	public void setNext(Operation next);

}
