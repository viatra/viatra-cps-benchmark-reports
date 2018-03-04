package com.viatra.cps.benchmark.reports.processing.operation.numeric;

import java.util.Arrays;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

public class Summary extends NumericOperation {

	public Summary(Filter filter, Operation next) {
		super(filter, next);
	}

	public Summary(Filter filter) {
		super(filter);
	}

	@Override
	public void calculate() {
		next.start();
		try {
			this.filter.getThread().join();
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

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.next != null) {
			this.next.stop();
		}
	}

	@Override
	public void run() {
		while (this.running || !this.queue.isEmpty()) {
			BenchmarkResult benchmarkResult = this.queue.poll();
			synchronized (this.lock) {
				// Wait for result
				if (benchmarkResult == null) {
					try {
						this.lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					// Work on result
					filter.addResult(benchmarkResult);
				}

			}
		}
		this.filter.stop();
		this.calculate();
	}
}
