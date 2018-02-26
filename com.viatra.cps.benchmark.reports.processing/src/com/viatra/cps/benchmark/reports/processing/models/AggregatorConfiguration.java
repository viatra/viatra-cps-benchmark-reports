package com.viatra.cps.benchmark.reports.processing.models;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


public class AggregatorConfiguration {
	
	
	@JsonProperty("Operations")
	private List<OperationConfig> operations;
	
	public void setOperation(List<OperationConfig> operationConfigs) {
		this.operations = operationConfigs;
	}
	
	public List<OperationConfig> getOperations(Boolean reverse) {
		if(reverse) {
			List<OperationConfig> tmp = this.operations.subList(0, this.operations.size());
			Collections.reverse(tmp);
			return tmp;
		}else {
			return this.operations;
		}
	}
}
