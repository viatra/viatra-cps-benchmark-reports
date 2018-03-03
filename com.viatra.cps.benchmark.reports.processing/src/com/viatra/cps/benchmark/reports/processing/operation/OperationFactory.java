package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.MetricFilter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.PhaseNameFilter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.RunIndexFilter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.ToolFilter;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Average;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Mean;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.NumericOperation;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Summary;

public class OperationFactory {
	public static Operation createOperation(Operation next, String operationType, List<Object> elements,
			String attribute) {
		Filter filter = null;
		switch (operationType) {
		case "Average":
			filter = createFilter(elements, null, true, attribute);
			if (filter == null) {
				return null;
			}
			NumericOperation avg = new Average(filter, next);
			filter.setNext(avg);
			return avg;
		case "Mean":
			filter = createFilter(elements, null, true, attribute);
			if (filter == null) {
				return null;
			}
			NumericOperation mean = new Mean(filter, next);
			filter.setNext(mean);
			return mean;
		case "Summary":
			filter = createFilter(elements, null, true, attribute);
			if (filter == null) {
				return null;
			}
			NumericOperation sum = new Summary(filter, next);
			filter.setNext(sum);
			return sum;
		case "Filter":
			return createFilter(elements, next, false, attribute);
		default:
			return null;
		}
	}

	private static Filter createFilter(List<Object> elements, Operation next, Boolean contained, String type) {
		switch (type) {
		case "Metric":
			return new MetricFilter(elements, next, contained);
		case "Phase-Name":
			return new PhaseNameFilter(elements,next, contained);
		case "Tool":
			return new ToolFilter(elements,next, contained);
		case "RunIndex":
			return new RunIndexFilter(elements, next, contained);
		default:
			return null;
		}
	}
}
