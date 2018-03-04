package com.viatra.cps.benchmark.reports.processing.operation;

import eu.mondo.sam.core.results.BenchmarkResult;

public interface Operation extends Runnable {
	
	/**
	 * Start operation
	 * @return success
	 */
	public boolean start();

	/**
	 * Add one BenchmarkResult to operation
	 * @param result
	 */
	public void addResult(BenchmarkResult result);
	
	/**
	 * Stop operation
	 */
	public void stop();

	/**
	 * Set next operation
	 * @param next
	 */
	public void setNext(Operation next);

}
