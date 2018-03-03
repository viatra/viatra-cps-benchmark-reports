package com.viatra.cps.benchmark.reports.processing.operation.numeric;


import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;

public class Average extends NumericOperation {

	
	
	public Average(Filter filter, Operation next) {
		super(filter, next);
	}
	
	public Average(Filter filter) {
		super(filter);
	}

	@Override
	public void calculate() {
		System.out.println("calcuate Average");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
