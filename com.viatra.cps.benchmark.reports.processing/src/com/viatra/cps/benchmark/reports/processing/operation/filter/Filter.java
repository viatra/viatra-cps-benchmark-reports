package com.viatra.cps.benchmark.reports.processing.operation.filter;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import eu.mondo.sam.core.results.BenchmarkResult;

public abstract class Filter implements Operation {
	protected List<Object> elements;
	protected Thread thread;
	protected Boolean running;
	protected Object lock;
	protected ConcurrentLinkedQueue<BenchmarkResult> queue;
	protected Operation next;
	protected Boolean contained;

	public Filter(List<Object> elements, Operation next, Boolean contained) {
		this.elements = elements;
		this.contained = contained;
		this.next = next;
		this.running = false;
		this.lock = new Object();
	}

	public Filter(List<Object> elements, Boolean contained) {
		this.elements = elements;
		this.contained = contained;
		this.running = false;
		this.lock = new Object();
	}
	
	public Thread getThread() {
		return thread;
	}

	@Override
	public void setNext(Operation next) {
		this.next = next;
	}

	@Override
	public boolean start() {
		try {
			synchronized (this.lock) {
				this.thread = new Thread(this);
				this.thread.setDaemon(true);
				this.queue = new ConcurrentLinkedQueue<>();
				this.running = true;
				this.thread.start();
				if (!this.contained) {
					return this.next.start();
				}
			}

			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

	@Override
	public void addResult(BenchmarkResult result) {
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
	
	protected BenchmarkResult createBenchmarkResult(BenchmarkResult benchmarkResult) {
		BenchmarkResult newRes = new BenchmarkResult();
		newRes.setCaseDescriptor(benchmarkResult.getCaseDescriptor());
		return newRes;
	}

}
