package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class NumericOperation implements Operation {
	protected Filter filter;
	protected Thread thread;
	protected Boolean running;
	protected Object lock;
	protected String id;
	protected ConcurrentLinkedQueue<BenchmarkResult> queue;
	protected Operation next;

	public NumericOperation(Filter filter, Operation next, String id) {
		this.filter = filter;
		this.next = next;
		this.running = false;
		this.id = id;
	}

	public NumericOperation(Filter filter, String id) {
		this.filter = filter;
		this.id = id;
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
			this.thread.setDaemon(true);
			this.lock = new Object();
			this.queue = new ConcurrentLinkedQueue<>();
			this.running = true;
			this.thread.start();
			return true;
		} catch (IllegalThreadStateException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addResult(BenchmarkResult result) {
		filter.addResult(result);
	}

	public void addFilteredResult(BenchmarkResult result) {
		this.queue.add(result);
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
