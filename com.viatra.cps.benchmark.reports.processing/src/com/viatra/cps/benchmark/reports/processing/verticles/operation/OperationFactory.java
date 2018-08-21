package com.viatra.cps.benchmark.reports.processing.verticles.operation;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.viatra.cps.benchmark.reports.processing.verticles.operation.filter.Filter;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.filter.FilterDescriptor;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.filter.MetricFilter;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.filter.PhaseNameFilter;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.filter.RunIndexFilter;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.filter.ToolFilter;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric.Average;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric.Mean;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric.MeanWithDrop;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric.NumericOperationDescriptor;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.numeric.Summary;

public class OperationFactory {
	public static Operation createOperation(OperationDescriptor operationDescriptor, String scenario,
			ObjectMapper mapper) {
		switch (operationDescriptor.getType()) {
		case NUMERIC:
			return createNumericOperation((NumericOperationDescriptor) operationDescriptor, scenario, mapper);
		case FILTER:
			return createFilter((FilterDescriptor) operationDescriptor, scenario, mapper);
		default:
			return null;
		}
	}

	private static Filter createFilter(FilterDescriptor operationDescriptor, String scenario, ObjectMapper mapper) {
		switch (operationDescriptor.getFilterType()) {
		case METRIC:
			return new MetricFilter(operationDescriptor.getFilterElements(), operationDescriptor.getNext(),
					operationDescriptor.getId(), scenario, mapper);
		case PHASENAME:
			return new PhaseNameFilter(operationDescriptor.getFilterElements(), operationDescriptor.getNext(),
					operationDescriptor.getId(), scenario, mapper);
		case TOOL:
			return new ToolFilter(operationDescriptor.getFilterElements(), operationDescriptor.getNext(),
					operationDescriptor.getId(), scenario, mapper);
		case RUNINDEX:
			return new RunIndexFilter(operationDescriptor.getFilterElements(), operationDescriptor.getNext(),
					operationDescriptor.getId(), scenario, mapper);
		default:
			return null;
		}
	}

	private static Operation createNumericOperation(NumericOperationDescriptor operationDescriptor, String scenario,
			ObjectMapper mapper) {
		switch (operationDescriptor.getOperationType()) {
		case MEAN:
			return new Mean(operationDescriptor.getNext(), operationDescriptor.getId(), scenario, mapper);
		case MEANDROP:
			return new MeanWithDrop(operationDescriptor.getNext(), operationDescriptor.getId(), scenario, mapper);
		case AVERAGE:
			return new Average(operationDescriptor.getNext(), operationDescriptor.getId(), scenario, mapper);
		case SUMMARY:
			return new Summary(operationDescriptor.getNext(), operationDescriptor.getId(), scenario, mapper);
		default:
			return null;
		}
	}
}
