package com.viatra.cps.benchmark.reports.processing.operation.numeric;


import com.viatra.cps.benchmark.reports.processing.operation.Operation;
import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;


public class Mean extends NumericOperation {

	public Mean(Filter filter, Operation next) {
		super(filter, next);
	}

	@Override
	protected void calculate() {
		// TODO Auto-generated method stub
		System.out.println("calcuate Mean");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

}
