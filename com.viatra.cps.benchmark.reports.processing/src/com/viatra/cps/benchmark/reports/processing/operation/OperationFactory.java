package com.viatra.cps.benchmark.reports.processing.operation;

import java.util.List;

import com.viatra.cps.benchmark.reports.processing.operation.filter.Filter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.MetricFilter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.PhaseNameFilter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.RunIndexFilter;
import com.viatra.cps.benchmark.reports.processing.operation.filter.ToolFilter;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Average;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Mean;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.MeanWithDrop;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.NumericOperation;
import com.viatra.cps.benchmark.reports.processing.operation.numeric.Summary;

public class OperationFactory {
	public static Operation createOperation(Operation next, String operationType, List<Object> elements,
			String attribute, String id) {
		Filter filter = null;
		switch (operationType) {
		case "Average":
			filter = createFilter(elements, null, true, attribute, id);
			if (filter == null) {
				return null;
			}
			NumericOperation avg = new Average(filter, next,id);
			filter.setNext(avg);
			return avg;
		case "Mean":
			filter = createFilter(elements, null, true, attribute, id);
			if (filter == null) {
				return null;
			}
			NumericOperation mean = new Mean(filter, next,id);
			filter.setNext(mean);
			return mean;
		case "Mean-Drop":
			filter = createFilter(elements, null, true, attribute, id);
			if (filter == null) {
				return null;
			}
			NumericOperation meanDrop = new MeanWithDrop(filter, next,id);
			filter.setNext(meanDrop);
			return meanDrop;
		case "Summary":
			filter = createFilter(elements, null, true, attribute, id);
			if (filter == null) {
				return null;
			}
			NumericOperation sum = new Summary(filter, next,id);
			filter.setNext(sum);
			return sum;
		case "Filter":
			return createFilter(elements, next, false, attribute, id);
		default:
			return null;
		}
	}

	private static Filter createFilter(List<Object> elements, Operation next, Boolean contained, String type,
			String id) {
		switch (type) {
		case "Metric":
			return new MetricFilter(elements, next, contained, id);
		case "Phase-Name":
			return new PhaseNameFilter(elements, next, contained, id);
		case "Tool":
			return new ToolFilter(elements, next, contained, id);
		case "RunIndex":
			return new RunIndexFilter(elements, next, contained, id);
		default:
			return null;
		}
	}
}
