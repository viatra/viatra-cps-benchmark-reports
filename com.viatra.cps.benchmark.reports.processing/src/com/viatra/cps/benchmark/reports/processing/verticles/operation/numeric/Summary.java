package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.Arrays;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

public class Summary extends NumericOperation {

	public Summary(Filter filter, Operation next, String id) {
		super(filter, next, id);
	}

	public Summary(Filter filter, String id) {
		super(filter, id);
	}

	@Override
	public void calculate() {
		next.start();
		try {
			if (filter.getThread().isAlive()) {
				this.filter.getThread().join();
			}
			while (!queue.isEmpty()) {
				Double sum = 0.0;
				BenchmarkResult res = queue.poll();
				for (PhaseResult pRes : res.getPhaseResults()) {
					sum += pRes.getMetrics().get(0).getValue();
				}
				MetricResult m = new MetricResult();
				m.setValue(sum);
				BenchmarkResult b = new BenchmarkResult();
				b.setCaseDescriptor(res.getCaseDescriptor());
				PhaseResult p = new PhaseResult();
				p.setPhaseName(res.getPhaseResults().get(0).getPhaseName());
				p.setMetrics(Arrays.asList(m));
				b.addResults(p);
				next.addResult(b);
			}
			if (this.next != null) {
				this.next.stop();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		if (this.running) {
			synchronized (this.lock) {
				try {
					this.lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		this.filter.stop();
		this.calculate();
	}
}
