package com.viatra.cps.benchmark.reports.processing.operation.numeric;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class NumericOperation implements Operation {
	protected Filter filter;
	protected Thread thread;
	protected Boolean running;
	protected Object lock;
	protected ConcurrentLinkedQueue<BenchmarkResult> queue;
	protected Operation next;

	public NumericOperation(Filter filter, Operation next) {
		this.filter = filter;
		this.next = next;
		this.running = false;
	}

	public NumericOperation(Filter filter) {
		this.filter = filter;
		this.running = false;
	}

	@Override
	public void setNext(Operation next) {
		this.next = next;
	}

	@Override
	public boolean start() {
		try {
			if (filter != null) {
				this.filter.start();
			}
			this.thread = new Thread(this);
			this.lock = new Object();
			this.queue = new ConcurrentLinkedQueue<>();
			this.running = true;
			this.thread.start();

			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

	@Override
	public void addResult(BenchmarkResult result) {
		if (filter != null) {
			filter.addResult(result);
		}
	}

	public void addFilteredResult(BenchmarkResult result) {
		synchronized (this.lock) {
			this.queue.add(result);
			this.lock.notify();
		}
	}

	@Override
	public void stop() {
		synchronized (lock) {
			this.running = false;
			this.lock.notify();
		}
	}

	protected abstract void calculate();
}
