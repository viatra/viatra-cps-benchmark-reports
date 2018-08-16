package com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

import eu.mondo.sam.core.results.BenchmarkResult;
import eu.mondo.sam.core.results.MetricResult;
import eu.mondo.sam.core.results.PhaseResult;

public class Mean extends NumericOperation {

	public Mean(Filter filter, Operation next,String id) {
		super(filter, next,id);
	}

	@Override
	protected void calculate() {
		next.start();
		try {
			if (filter.getThread().isAlive()) {
				this.filter.getThread().join();
			}
			while (!queue.isEmpty()) {
				DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
				BenchmarkResult res = queue.poll();
				for (PhaseResult pRes : res.getPhaseResults()) {
					descriptiveStatistics.addValue(pRes.getMetrics().get(0).getValue());
				}
				MetricResult m = new MetricResult();
				m.setValue(descriptiveStatistics.getMean());
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
