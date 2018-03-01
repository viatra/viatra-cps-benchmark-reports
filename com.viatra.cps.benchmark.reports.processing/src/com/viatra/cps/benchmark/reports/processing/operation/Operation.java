package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Operation implements Runnable {
	protected String attribute;
	protected List<Object> filter;
	protected Thread thread;
	protected Boolean running;
	protected Object lock;
	protected ConcurrentLinkedQueue<BenchmarkResult> queue;

	public Operation(String attribute, List<Object> filter) {
		this.attribute = attribute;
		this.filter = filter;
		this.running = false;
	}

	public boolean start() {
		try {
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

	public void addResult(BenchmarkResult result) {
		this.queue.add(result);
	}

	public void stop() {
		synchronized (lock) {
			this.running = false;
			this.lock.notify();
		}
	}

	public abstract void calculate();
}
