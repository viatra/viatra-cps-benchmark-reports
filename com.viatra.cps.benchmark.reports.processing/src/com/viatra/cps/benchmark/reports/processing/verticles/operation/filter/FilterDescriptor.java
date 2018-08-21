package com.viatra.cps.benchmark.reports.processing.verticles.operation.filter;

import java.util.List;

import com.viatra.cps.benchmark.reports.processing.verticles.operation.OperationDescriptor;
import com.viatra.cps.benchmark.reports.processing.verticles.operation.OperationType;

public class FilterDescriptor extends OperationDescriptor {

	protected FilterType filterType;

	public FilterType getFilterType() {
		return filterType;
	}

	public List<Object> getFilterElements() {
		return filterElements;
	}

	protected List<Object> filterElements;

	public FilterDescriptor(String id, String next, OperationType type, FilterType filterType,
			List<Object> filterElements) {
		super(id, next, type);
		this.filterType = filterType;
		this.filterElements = filterElements;
	}

}
